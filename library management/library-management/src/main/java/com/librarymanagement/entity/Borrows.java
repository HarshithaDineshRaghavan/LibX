package com.librarymanagement.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Data
@Table(name="borrows")
public class Borrows {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer borrowId;
    @Column(name="fine_amount")
    private BigDecimal fineAmount;

    @Column(name="user_id")
    private Integer userId;

    @Column(name = "borrow_date")
    private LocalDate borrowDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "is_returned")
    private Boolean isReturned;

    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "is_extended")
    private Boolean isExtended = false;

    @Column(name = "is_overdue")
    private Boolean isOverdue;

    @Transient
    private String userName;

    @Transient
    private String bookName;

    public Integer getBorrowsBookId() {
        return this.bookId;
    }

}
