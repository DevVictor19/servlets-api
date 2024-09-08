package org.devvictor.application.exceptions;

import org.devvictor.application.enums.HttpStatus;

public class NotFoundException extends ApplicationException {
    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public NotFoundException() {
        super(httpStatus.getCode(), httpStatus.getDescription());
    }

    public NotFoundException(String message) {
        super(httpStatus.getCode(), message);
    }
}
