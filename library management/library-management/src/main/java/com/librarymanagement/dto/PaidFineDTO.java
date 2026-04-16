package com.librarymanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaidFineDTO(
        Integer fineId,
        String userName,
        String bookName,
        BigDecimal totalAmount,
        LocalDate paidDate
) {}
