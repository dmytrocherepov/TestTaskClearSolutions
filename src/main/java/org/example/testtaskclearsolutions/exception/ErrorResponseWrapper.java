package org.example.testtaskclearsolutions.exception;

import java.time.LocalDateTime;

public record ErrorResponseWrapper(
        Object message,
        LocalDateTime localDateTime
) {
}
