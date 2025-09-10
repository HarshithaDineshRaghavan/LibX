package com.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="authors")
public class Authors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Integer authorId;

    @Column(name = "name")
    private String name;

    @Column(name="description")
    private String description;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "author_id")
    private List<Books> books = new ArrayList<>();

}
