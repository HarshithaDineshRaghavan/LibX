package com.librarymanagement.security;

import com.librarymanagement.service.BooksService;
import com.librarymanagement.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @Autowired
    private BooksService booksService;

    @Autowired
    private UsersService usersService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("totalBooks", booksService.getAllBooks().size());
        model.addAttribute("totalUsers", usersService.getAllUsers().size());

        int availableBooks = booksService.getAllBooks()
                .stream()
                .mapToInt(b -> b.getAvailableCopies())
                .sum();

        model.addAttribute("availableBooks", availableBooks);

        int issuedBooks = booksService.getAllBooks()
                .stream()
                .mapToInt(b -> b.getTotalCopies() - b.getAvailableCopies())
                .sum();

        model.addAttribute("issuedBooks", issuedBooks);
        var recentBooks = booksService.getAllBooks()
                .stream()
                .sorted((a, b) -> b.getBookId() - a.getBookId())
                .limit(5)
                .toList();

        model.addAttribute("recentBooks", recentBooks);
        return "home";
    }


}