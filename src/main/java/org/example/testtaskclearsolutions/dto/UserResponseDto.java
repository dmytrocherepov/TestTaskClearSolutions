package org.example.testtaskclearsolutions.dto;

import java.time.LocalDate;

public record UserResponseDto(
    Long id,
    String email,
    String firstName,
    String lastName,
    LocalDate birthDate ,
    String phoneNumber,
    String address
) {
}
