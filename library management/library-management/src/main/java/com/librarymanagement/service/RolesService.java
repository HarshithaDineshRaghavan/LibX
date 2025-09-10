package com.librarymanagement.service;

import com.librarymanagement.entity.Roles;
import com.librarymanagement.repository.RolesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolesService {

    private final RolesRepository rolesRepository;

    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public Optional<Roles> getRoleById(Integer id) {
        return rolesRepository.findById(id);
    }
    public List<Roles> findAll() {
        return rolesRepository.findAll();
    }
}

