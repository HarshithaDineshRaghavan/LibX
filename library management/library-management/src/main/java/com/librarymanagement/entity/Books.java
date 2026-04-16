package com.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="books")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;

    @Column(name = "book_name")
    private String bookName;

    @Column(name="total_copies")
    private Integer totalCopies;

    @Column(name = "available_copies")
    private Integer availableCopies;

    @Column(name="author_id")
    private Integer authorId;

    @Column(name = "genre")
    private String genre;

    @Transient
    private String authorName;

    @OneToMany
    @JoinColumn(name = "book_id")
    private List<Borrows> borrows = new ArrayList<>();
}
