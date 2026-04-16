package com.librarymanagement.repository;

import com.librarymanagement.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {
     Roles findFirstByRole(String role);
     //Optional<Roles> findByRoleName(String roleName);
}
