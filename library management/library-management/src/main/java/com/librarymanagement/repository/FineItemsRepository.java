package com.librarymanagement.repository;

import com.librarymanagement.entity.FineItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineItemsRepository extends JpaRepository<FineItems, Integer> {
    List<FineItems> findByFine_FineId(Integer fineId);
    boolean existsByBorrowIdAndFine_FineId(Integer borrowId, Integer fineId);
    List<FineItems> findByFine_Status(String status);
    boolean existsByBorrowIdAndFine_Status(Integer borrowId, String status);
}
