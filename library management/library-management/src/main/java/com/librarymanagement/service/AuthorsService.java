package com.librarymanagement.service;

import com.librarymanagement.entity.Authors;
import com.librarymanagement.repository.AuthorsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorsService {

    private final AuthorsRepository authorsRepository;

    public AuthorsService(AuthorsRepository authorsRepository) {
        this.authorsRepository = authorsRepository;
    }

    public Optional<Authors> getAuthorById(Integer id) {
        return authorsRepository.findById(id);
    }

    public List<Authors> getAllAuthors() {
        return authorsRepository.findAll();
    }
}
