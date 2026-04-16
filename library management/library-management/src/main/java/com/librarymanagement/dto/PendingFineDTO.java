package com.librarymanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PendingFineDTO(
        Integer fineId,
        String userName,
        String bookName,
        LocalDate dueDate,
        Integer daysOverdue,
        BigDecimal amount
) {}
