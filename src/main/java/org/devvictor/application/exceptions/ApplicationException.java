package org.devvictor.application.exceptions;

abstract class ApplicationException extends RuntimeException {
    private final Integer status;
    private final String message;

    public ApplicationException(Integer status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
