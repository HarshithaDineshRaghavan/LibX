package com.librarymanagement.controller;

import com.librarymanagement.entity.Borrows;
import com.librarymanagement.service.BorrowsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrows")
public class BorrowsController {

    private final BorrowsService borrowsService;

    public BorrowsController(BorrowsService borrowsService) {
        this.borrowsService = borrowsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borrows> getBorrowById(@PathVariable Integer id) {
        return borrowsService.getBorrowById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}


