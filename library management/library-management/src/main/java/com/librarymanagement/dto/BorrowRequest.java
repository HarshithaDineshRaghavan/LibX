package com.librarymanagement.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BorrowRequest {

    private Integer borrowId;
    private Integer userId;

    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    private Boolean isReturned;
}
