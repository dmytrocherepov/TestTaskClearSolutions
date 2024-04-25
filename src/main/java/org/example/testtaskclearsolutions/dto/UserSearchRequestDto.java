package org.example.testtaskclearsolutions.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserSearchRequestDto(
        @NotNull
        LocalDate fromDate,
        @NotNull
        LocalDate toDate
) {
}
