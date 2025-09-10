package com.librarymanagement.service;

import com.librarymanagement.entity.Authors;
import com.librarymanagement.entity.Books;
import com.librarymanagement.repository.BooksRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BooksService {

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public Optional<Books> getBookById(Integer id) {
        return booksRepository.findById(id);
    }
    public List<Books> getAllBooks() {
        return booksRepository.findAll();
    }

    public Books saveBook(Books book) {
        return booksRepository.save(book);
    }

    public Books updateBook(Integer id, Books updatedBook) {
        Books book = booksRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        book.setBookName(updatedBook.getBookName());
        book.setGenre(updatedBook.getGenre());
        book.setTotalCopies(updatedBook.getTotalCopies());
        book.setAvailableCopies(updatedBook.getAvailableCopies());
        book.setAuthorId(updatedBook.getAuthorId());
        return booksRepository.save(book);
    }

    public void deleteBook(Integer id) {
        booksRepository.deleteById(id);
    }


}


