package com.librarymanagement.controller;

import com.librarymanagement.dto.UserRequest;
import com.librarymanagement.entity.Authors;
import com.librarymanagement.entity.Books;
import com.librarymanagement.repository.BooksRepository;
import com.librarymanagement.service.AuthorsService;
import com.librarymanagement.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/books/")
public class BooksController {

    private final BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @Autowired
    private AuthorsService authorsService;


    @GetMapping("/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable Integer id) {
        return booksService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/list")
    public String listBooks(Model model) {
        model.addAttribute("books", booksService.getAllBooks());
        return "list-books";
    }

//    @PostMapping("/save")
//    public String saveBook(@ModelAttribute Books book) {
//        booksService.saveBook(book);
//        booksService.saveBookWithAuthorName(book, book.getAuthorName(), authorsService);
//        return "redirect:/admin/books/list";
//    }
@PostMapping("/save")
public String saveBook(@ModelAttribute Books book) {
    // Trim whitespace and check for empty author
    if (book.getAuthorName() == null || book.getAuthorName().trim().isEmpty()) {
        throw new IllegalArgumentException("Author name cannot be empty");
    }
    Authors author = authorsService.getAllAuthors().stream()
            .filter(a -> a.getName().equalsIgnoreCase(book.getAuthorName().trim()))
            .findFirst()
            .orElse(null);
    if (author == null) {
        author = new Authors();
        author.setName(book.getAuthorName().trim());
        author = authorsService.saveAuthor(author);
    }
    book.setAuthorId(author.getAuthorId());
    booksService.saveBook(book);

    return "redirect:/admin/books/list";
}


//    @GetMapping("/edit/{id}")
//    public String editBook(@PathVariable Integer id, Model model) {
//        Books book = booksService.getBookById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
//
//        model.addAttribute("book", book);
//
//        List<Authors> authors = authorsService.getAllAuthors();
//
//        model.addAttribute("authors", authors);
//
//        return "edit-book";
//    }
    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable Integer id, Model model) {
        Books book = booksService.getBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));

        // Fetch author name using authorId
        if (book.getAuthorId() != null) {
            Authors author = authorsService.getAllAuthors().stream()
                    .filter(a -> a.getAuthorId().equals(book.getAuthorId()))
                    .findFirst()
                    .orElse(null);
            if (author != null) {
                book.setAuthorName(author.getName());
            }
        }

        model.addAttribute("book", book);
        return "edit-book";
    }

    //    @PostMapping("/update/{id}")
//    public String updateBook(@PathVariable Integer id, @ModelAttribute Books book) {
//        booksService.updateBook(id, book);
//        return "redirect:/admin/books/list";
//    }
@PostMapping("/update/{id}")
public String updateBook(@PathVariable Integer id, @ModelAttribute Books book) {
    if (book.getAuthorName() == null || book.getAuthorName().trim().isEmpty()) {
        throw new IllegalArgumentException("Author name cannot be empty");
    }

    Authors author = authorsService.getAllAuthors().stream()
            .filter(a -> a.getName().equalsIgnoreCase(book.getAuthorName().trim()))
            .findFirst()
            .orElse(null);

    if (author == null) {
        author = new Authors();
        author.setName(book.getAuthorName().trim());
        author = authorsService.saveAuthor(author);
    }

    book.setAuthorId(author.getAuthorId());

    booksService.updateBook(id, book);
    return "redirect:/admin/books/list";
}

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Integer id) {
        booksService.deleteBook(id);
        return "redirect:/admin/books/list";
    }
    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Books());

        //List<Authors> authors = authorsService.getAllAuthors();
        //model.addAttribute("authorNames", authors);
        return "add-book";
    }


}
