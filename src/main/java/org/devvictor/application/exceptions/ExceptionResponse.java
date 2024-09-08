package org.devvictor.application.exceptions;

public record ExceptionResponse(
        int status,
        String message
) {
}
