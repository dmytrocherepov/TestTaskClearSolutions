package org.example.testtaskclearsolutions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserFullUpdateRequestDto(
        @NotBlank
        @Size(max = 50)
        String firstName,
        @NotBlank
        @Size(max = 50)
        String lastName,
        @Pattern(
                regexp = "^\\+380\\d{9}$",
                message = "Incorrect phone number , correct format is +380XXXXXXXXX "

        )
        String phoneNumber,
        @NotNull
        @JsonFormat(pattern="yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
        LocalDate birthDate,
        String address
) {
}
