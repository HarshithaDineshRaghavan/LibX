package com.librarymanagement.service;

import com.librarymanagement.entity.Borrows;
import com.librarymanagement.entity.Users;
import com.librarymanagement.repository.BorrowsRepository;
import com.librarymanagement.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private BorrowsRepository borrowsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MailService mailService;

    public void sendOverdueReminders() {

        List<Borrows> overdue =
                borrowsRepository.findByIsOverdueTrueAndIsReturnedFalse();

        for (Borrows b : overdue) {

            Users u = usersRepository.findById(b.getUserId()).orElse(null);
            if (u == null || u.getEmail() == null) continue;

            int perDay =
                    "yes".equalsIgnoreCase(u.getMembership()) ? 2 : 5;

            String body = """
                Book Overdue Notice

                Book: %s
                Due Date: %s

                Fine per day: ₹%d
                Total Fine till today: ₹%d

                Please return the book immediately
                to stop further fine accumulation.

                – Library Management
                """.formatted(
                    b.getBookName(),
                    b.getDueDate(),
                    perDay,
                    b.getFineAmount()
            );

            mailService.sendMail(
                    u.getEmail(),
                    "Overdue Book – Fine Applied",
                    body
            );
        }
    }
//    public void sendBorrowConfirmation(String email, String bookName, LocalDate dueDate) {
//
//        String body = """
//            Book Issued Successfully
//
//            Book: %s
//            Due Date: %s
//
//            Please return the book on or before the due date
//            to avoid late fines.
//
//            – Library Management
//            """.formatted(bookName, dueDate);
//
//        mailService.sendMail(
//                email,
//                "Book Issued – Library",
//                body
//        );
//    }
public void sendBorrowConfirmation(String email, String bookName, LocalDate dueDate) {

    if (email == null || email.isBlank()) {
        return; // silently skip mail
    }

    String body = """
        Book Issued Successfully

        Book: %s
        Due Date: %s

        Please return the book on or before the due date
        to avoid late fines.

        – Library Management
        """.formatted(bookName, dueDate);

    try {
        mailService.sendMail(
                email,
                "Book Issued – Library",
                body
        );
    } catch (Exception e) {
        // DO NOT crash issue flow
        System.out.println("Mail failed for " + email);
    }
}

    public void sendDueDateReminders() {

        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<Borrows> dueTomorrow =
                borrowsRepository.findByIsReturnedFalse()
                        .stream()
                        .filter(b -> b.getDueDate() != null)
                        .filter(b -> b.getDueDate().isEqual(tomorrow))
                        .toList();

        for (Borrows b : dueTomorrow) {

            Users u = usersRepository.findById(b.getUserId()).orElse(null);
            if (u == null || u.getEmail() == null) continue;

            String body = """
                Due Date Reminder

                Book: %s
                Due Date: %s

                Please return the book tomorrow
                to avoid late fine charges.

                – Library Management
                """.formatted(b.getBookName(), b.getDueDate());

            mailService.sendMail(
                    u.getEmail(),
                    "Reminder: Book Due Tomorrow",
                    body
            );
        }
    }


}
