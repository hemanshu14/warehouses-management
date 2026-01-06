package com.fulfilment.application.monolith.exception;

public class WarehouseNotFoundException extends RuntimeException {
    public WarehouseNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
