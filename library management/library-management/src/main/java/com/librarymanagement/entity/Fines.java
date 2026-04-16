package com.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fines")
@Data
public class Fines {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fineId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    private String status;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @OneToMany(mappedBy = "fine", cascade = CascadeType.ALL)
    private List<FineItems> items = new ArrayList<>();
}
