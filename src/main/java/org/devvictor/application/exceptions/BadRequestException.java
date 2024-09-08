package org.devvictor.application.exceptions;

import org.devvictor.application.enums.HttpStatus;

public class BadRequestException extends ApplicationException {
    private static final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public BadRequestException() {
        super(httpStatus.getCode(), httpStatus.getDescription());
    }

    public BadRequestException(String message) {
        super(httpStatus.getCode(), message);
    }
}
