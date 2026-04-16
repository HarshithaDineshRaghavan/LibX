package com.librarymanagement.repository;

import com.librarymanagement.entity.Borrows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowsRepository extends JpaRepository<Borrows, Integer> {
    List<Borrows> findByIsReturnedFalse();

    List<Borrows> findByIsOverdueTrueAndIsReturnedFalse();

}
