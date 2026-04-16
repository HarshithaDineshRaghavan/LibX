package com.librarymanagement.service;

import com.librarymanagement.entity.Borrows;
import com.librarymanagement.entity.Users;
import com.librarymanagement.repository.BorrowsRepository;
import com.librarymanagement.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowsService {
    private final BorrowsRepository borrowsRepository;
    private final UsersRepository usersRepository;

    public BorrowsService(BorrowsRepository borrowsRepository, UsersRepository usersRepository) {

        this.borrowsRepository = borrowsRepository;
        this.usersRepository = usersRepository;
    }

    public Optional<Borrows> getBorrowById(Integer id) {

        return borrowsRepository.findById(id);
    }
    public Borrows saveBorrow(Borrows b) {

        return borrowsRepository.save(b);
    }
    public List<Borrows> getAllBorrows() {

        return borrowsRepository.findAll();
    }
    public void updateOverdueAndFines() {
        LocalDate today = LocalDate.now();

        List<Borrows> activeBorrows =
                borrowsRepository.findByIsReturnedFalse();

        for (Borrows b : activeBorrows) {

            if (b.getDueDate() != null && b.getDueDate().isBefore(today)) {

                long daysOverdue =
                        ChronoUnit.DAYS.between(b.getDueDate(), today);

                Users user = usersRepository
                        .findById(b.getUserId())
                        .orElseThrow();

                int finePerDay =
                        "yes".equalsIgnoreCase(user.getMembership()) ? 2 : 5;

                b.setIsOverdue(true);
                BigDecimal fine =
                        BigDecimal.valueOf(daysOverdue)
                                .multiply(BigDecimal.valueOf(finePerDay));

                b.setFineAmount(fine);

                borrowsRepository.save(b);
            }
        }
    }

}


