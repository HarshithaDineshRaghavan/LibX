package com.librarymanagement.service;

import com.librarymanagement.dto.PaidFineDTO;
import com.librarymanagement.entity.Borrows;
import com.librarymanagement.entity.FineItems;
import com.librarymanagement.entity.Fines;
import com.librarymanagement.entity.Users;
import com.librarymanagement.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.librarymanagement.dto.PendingFineDTO;

@Service
public class FineService {

    private final BorrowsRepository borrowsRepository;
    private final UsersRepository usersRepository;
    private final FinesRepository finesRepository;
    private final FineItemsRepository fineItemsRepository;
    private final BooksRepository booksRepository;

    public FineService(
            BorrowsRepository borrowsRepository,
            UsersRepository usersRepository,
            FinesRepository finesRepository,
            FineItemsRepository fineItemsRepository,
            BooksRepository booksRepository
    ) {

        this.borrowsRepository = borrowsRepository;
        this.usersRepository = usersRepository;
        this.finesRepository = finesRepository;
        this.fineItemsRepository = fineItemsRepository;
        this.booksRepository=booksRepository;
    }

    public void generatePendingFines() {

        LocalDate today = LocalDate.now();

        List<Borrows> overdueBorrows =
                borrowsRepository.findByIsReturnedFalse()
                        .stream()
                        .filter(b -> b.getDueDate() != null && b.getDueDate().isBefore(today))
                        .toList();

        for (Borrows b : overdueBorrows) {

            Integer userId = b.getUserId();

            Fines fine = finesRepository
                    .findByUserIdAndStatus(userId, "PENDING")
                    .orElseGet(() -> {
                        Fines f = new Fines();
                        f.setUserId(userId);
                        f.setStatus("PENDING");
                        f.setCreatedDate(today);
                        f.setTotalAmount(BigDecimal.ZERO);
                        return finesRepository.save(f);
                    });

            boolean exists =
                    fineItemsRepository.existsByBorrowIdAndFine_FineId(
                            b.getBorrowId(),
                            fine.getFineId()
                    );

            if (exists) {
                continue;
            }
            boolean alreadyPaid =
                    fineItemsRepository.existsByBorrowIdAndFine_Status(
                            b.getBorrowId(),
                            "PAID"
                    );

            if (alreadyPaid) {
                continue;
            }

            long days = ChronoUnit.DAYS.between(b.getDueDate(), today);

            Users user = usersRepository.findById(userId).orElseThrow();

            int perDay = "yes".equalsIgnoreCase(user.getMembership()) ? 2 : 5;

            BigDecimal amount = BigDecimal.valueOf(days * perDay);

            FineItems item = new FineItems();
            item.setFine(fine);
            item.setBorrowId(b.getBorrowId());
            item.setBookId(b.getBookId());
            item.setDueDate(b.getDueDate());
            item.setDaysOverdue((int) days);
            item.setFineAmount(amount);

            fineItemsRepository.save(item);
        }

        List<Fines> fines = finesRepository.findByStatus("PENDING");

        for (Fines f : fines) {
            List<FineItems> items = fineItemsRepository.findByFine_FineId(f.getFineId());

            BigDecimal total = items.stream()
                    .map(FineItems::getFineAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            f.setTotalAmount(total);
            finesRepository.save(f);
        }
    }
    public List<PendingFineDTO> getPendingFineDTOs() {

        return fineItemsRepository.findByFine_Status("PENDING")
                .stream()
                .map(f -> new PendingFineDTO(
                        f.getFine().getFineId(),
                        usersRepository.findById(f.getFine().getUserId())
                                .orElseThrow()
                                .getName(),
                        booksRepository.findById(f.getBookId())
                                .orElseThrow()
                                .getBookName(),
                        f.getDueDate(),
                        f.getDaysOverdue(),
                        f.getFineAmount()
                ))
                .toList();
    }

    @Transactional
    public void clearFine(Integer fineId) {

        Fines fine = finesRepository.findById(fineId).orElseThrow();

        fine.setStatus("PAID");
        fine.setPaidDate(LocalDate.now());

        List<FineItems> items = fineItemsRepository.findByFine_FineId(fineId);

        for (FineItems item : items) {

            Borrows borrow = borrowsRepository
                    .findById(item.getBorrowId())
                    .orElseThrow();

            borrow.setIsOverdue(false);
            borrow.setFineAmount(BigDecimal.ZERO);

            borrowsRepository.save(borrow);
        }

        finesRepository.save(fine);
    }
    public List<PendingFineDTO> getPendingFineDTOs(String q) {

        return fineItemsRepository.findByFine_Status("PENDING")
                .stream()
                .map(f -> new PendingFineDTO(
                        f.getFine().getFineId(),
                        usersRepository.findById(f.getFine().getUserId())
                                .orElseThrow()
                                .getName(),
                        booksRepository.findById(f.getBookId())
                                .orElseThrow()
                                .getBookName(),
                        f.getDueDate(),
                        f.getDaysOverdue(),
                        f.getFineAmount()
                ))
                .filter(dto -> {
                    if (q == null || q.isBlank()) return true;

                    String s = q.toLowerCase();
                    return dto.userName().toLowerCase().contains(s)
                            || dto.bookName().toLowerCase().contains(s);
                })
                .toList();
    }
    @Transactional
    public void recalculateAndSyncPendingFines() {

        LocalDate today = LocalDate.now();

        List<FineItems> items =
                fineItemsRepository.findByFine_Status("PENDING");

        for (FineItems item : items) {

            long daysOverdue =
                    ChronoUnit.DAYS.between(item.getDueDate(), today);

            daysOverdue = Math.max(daysOverdue, 0);

            Users user =
                    usersRepository.findById(
                            item.getFine().getUserId()
                    ).orElseThrow();

            int perDay =
                    "yes".equalsIgnoreCase(user.getMembership()) ? 2 : 5;

            BigDecimal liveAmount =
                    BigDecimal.valueOf(daysOverdue * perDay);

            item.setDaysOverdue((int) daysOverdue);
            item.setFineAmount(liveAmount);
        }

        fineItemsRepository.saveAll(items);

        Map<Fines, BigDecimal> totals = new HashMap<>();

        for (FineItems item : items) {
            totals.merge(
                    item.getFine(),
                    item.getFineAmount(),
                    BigDecimal::add
            );
        }

        for (Map.Entry<Fines, BigDecimal> e : totals.entrySet()) {
            e.getKey().setTotalAmount(e.getValue());
            finesRepository.save(e.getKey());
        }
    }
    public List<PaidFineDTO> getPaidFineDTOs() {

        return fineItemsRepository.findByFine_Status("PAID")
                .stream()
                .map(item -> new PaidFineDTO(
                        item.getFine().getFineId(),
                        usersRepository.findById(item.getFine().getUserId())
                                .orElseThrow()
                                .getName(),
                        booksRepository.findById(item.getBookId())
                                .orElseThrow()
                                .getBookName(),
                        item.getFineAmount(),
                        item.getFine().getPaidDate()
                ))
                .toList();
    }

}