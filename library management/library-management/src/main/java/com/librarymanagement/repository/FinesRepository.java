package com.librarymanagement.repository;

import com.librarymanagement.entity.Fines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinesRepository extends JpaRepository<Fines, Integer> {

    Optional<Fines> findByUserIdAndStatus(Integer userId, String status);

    List<Fines> findByStatus(String status);
}
