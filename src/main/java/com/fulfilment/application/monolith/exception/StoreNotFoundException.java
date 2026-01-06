package com.fulfilment.application.monolith.exception;

public class StoreNotFoundException extends RuntimeException {
    private final int errorCode;

    public StoreNotFoundException(String exceptionMessage, int errorCode) {
        super(exceptionMessage);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
