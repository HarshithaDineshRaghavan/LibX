package com.librarymanagement.controller;

import com.librarymanagement.entity.Books;
import com.librarymanagement.entity.Borrows;
import com.librarymanagement.entity.Users;
import com.librarymanagement.service.BooksService;
import com.librarymanagement.service.BorrowsService;
import com.librarymanagement.service.NotificationService;
import com.librarymanagement.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/borrow")
public class BorrowController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BooksService booksService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private BorrowsService borrowsService;

    @GetMapping("/issue")
    public String showIssuePage(
            @RequestParam(required = false) Integer bookId,
            @RequestParam(required = false) String error,
            Model model) {

        model.addAttribute("users", usersService.getAllUsers());
        model.addAttribute("books", booksService.getAllBooks());
        model.addAttribute("selectedBook",
                bookId != null ? booksService.getBookById(bookId).orElse(null) : null);

        model.addAttribute("error", error);

        return "issue-book";
    }


    @PostMapping("/issue")
    public String issueBook(
            @RequestParam Integer userId,
            @RequestParam Integer bookId) {

        Books book = booksService.getBookById(bookId).orElseThrow();
        Users user = usersService.getUserById(userId).orElseThrow();

        if (book.getAvailableCopies() <= 0) {
            return "redirect:/admin/borrow/issue?error=noCopies";
        }

        String membership = user.getMembership();

        long activeBorrows = user.getBorrows()
                .stream()
                .filter(b -> Boolean.FALSE.equals(b.getIsReturned()))
                .count();

        int limit = "yes".equalsIgnoreCase(membership) ? 10 : 3;

        if (activeBorrows >= limit) {
            return "redirect:/admin/borrow/issue?error=limit";
        }

        Borrows borrow = new Borrows();
        borrow.setUserId(userId);
        borrow.setBookId(bookId);
        borrow.setBorrowDate(LocalDate.now());
        borrow.setDueDate(LocalDate.now().plusDays(15));
        borrow.setIsReturned(false);

        borrowsService.saveBorrow(borrow);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        booksService.saveBook(book);

        notificationService.sendBorrowConfirmation(
                user.getEmail(),
                book.getBookName(),
                borrow.getDueDate()
        );

        return "redirect:/admin/borrow/records";
    }


    //@GetMapping("/return")
//public String showReturnPage(Model model) {
//    var records = borrowsService.getAllBorrows()
//            .stream()
//            .filter(b -> Boolean.FALSE.equals(b.getIsReturned()))
//            .toList();
//
//    for (Borrows r : records) {
//        usersService.getUserById(r.getUserId())
//                .ifPresent(u -> r.setUserName(u.getName()));
//        booksService.getBookById(r.getBookId())
//                .ifPresent(b -> r.setBookName(b.getBookName()));
//    }
//
//    model.addAttribute("records", records);
//    return "return-book";
//}
//@GetMapping("/records")
//public String borrowRecords(Model model) {
//
//    List<Borrows> records = borrowsService.getAllBorrows();
//    model.addAttribute("records", records);
//    return "borrow-records";
//}
@GetMapping("/records")
public String borrowRecords(Model model) {

    List<Borrows> records = borrowsService.getAllBorrows();

    for (Borrows r : records) {

        if (r.getUserId() != null) {
            usersService.getUserById(r.getUserId())
                    .ifPresent(u -> r.setUserName(u.getName()));
        }

        if (r.getBookId() != null) {
            booksService.getBookById(r.getBookId())
                    .ifPresent(b -> r.setBookName(b.getBookName()));
        }
    }

    model.addAttribute("records", records);
    return "borrow-records";
}

    @GetMapping("/return")
    public String showReturnPage(Model model) {
        List<Borrows> records = borrowsService.getAllBorrows()
                .stream()
                .filter(java.util.Objects::nonNull)
                .filter(b -> Boolean.FALSE.equals(b.getIsReturned()))
                .toList();

        for (Borrows r : records) {
            if (r.getUserId() != null) {
                usersService.getUserById(r.getUserId()).ifPresent(u -> r.setUserName(u.getName()));
            }
            if (r.getBookId() != null) {
                booksService.getBookById(r.getBookId()).ifPresent(b -> r.setBookName(b.getBookName()));
            }
        }

        model.addAttribute("records", records);
        return "return-book";
    }

    @PostMapping("/return")
    public String returnBook(@RequestParam Integer borrowId) {

        Borrows record = borrowsService.getBorrowById(borrowId)
                .orElseThrow(() -> new IllegalArgumentException("Borrow record not found: " + borrowId));

        Integer bookId = record.getBookId();
        Books book = booksService.getBookById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found for borrow: " + bookId));

        record.setIsReturned(true);
        record.setReturnDate(LocalDate.now());
        borrowsService.saveBorrow(record);

        book.setAvailableCopies(Math.max(0, book.getAvailableCopies() + 1)); // defensive
        booksService.saveBook(book);

        return "redirect:/admin/borrow/records";
    }
//    @GetMapping("/records")
//    public String borrowRecords(Model model) {
//
//        List<Borrows> records = borrowsService.getAllBorrows();
//
//        for (Borrows r : records) {
//            usersService.getUserById(r.getUserId())
//                    .ifPresent(u -> r.setUserName(u.getName()));
//
//            booksService.getBookById(r.getBookId())
//                    .ifPresent(b -> r.setBookName(b.getBookName()));
//        }
//
//        model.addAttribute("records", records);
//        return "borrow-records";
//    }


}
