package com.fulfilment.application.monolith.exception;

public class InvalidStoreException extends RuntimeException {
    private final int errorCode;

    public InvalidStoreException(String exceptionMessage, int errorCode) {
        super(exceptionMessage);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
