package org.devvictor.application.exceptions;

import org.devvictor.application.enums.HttpStatus;

public class InternalServerErrorException extends ApplicationException {
    private static final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public InternalServerErrorException() {
        super(httpStatus.getCode(), httpStatus.getDescription());
    }

    public InternalServerErrorException(String message) {
        super(httpStatus.getCode(), message);
    }
}
