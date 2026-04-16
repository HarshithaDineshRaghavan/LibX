package com.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fine_items")
@Data
public class FineItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fineItemId;

    @ManyToOne
    @JoinColumn(name = "fine_id")
    private Fines fine;

    @Column(name = "borrow_id")
    private Integer borrowId;

    @Column(name = "book_id")
    private Integer bookId;

    private LocalDate dueDate;
    private Integer daysOverdue;
    private BigDecimal fineAmount;
}
