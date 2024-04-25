package org.example.testtaskclearsolutions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import org.example.testtaskclearsolutions.validation.FieldMatch;

import java.time.LocalDate;

@FieldMatch(field = {"password", "repeatPassword"}, message = "Passwords do not match")
public record UserRegistrationRequestDto(
        @NotBlank
        @Size(min = 2, max = 20)
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d_-]+$",
                message = "Password must contain at least one digit and letter"
        )
        String password,
        @NotBlank
        @Size(min = 2, max = 20)
        String repeatPassword,
        @Email
        String email,
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
