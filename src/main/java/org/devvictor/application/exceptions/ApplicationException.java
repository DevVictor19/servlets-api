package org.devvictor.application.exceptions;

public class ApplicationException extends RuntimeException {
    private final int status;
    private final String message;

    public ApplicationException(Integer status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
