package com.jparestservice.exceptions;

public class EmployeeDeletionException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public EmployeeDeletionException() {
        this("Employee can't be deleted");
    }

    public EmployeeDeletionException(String message) {
        this(message, null);
    }

    public EmployeeDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
