package com.fulfilment.application.monolith.exception;

public class WarehouseValidationException extends RuntimeException {
    public WarehouseValidationException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
