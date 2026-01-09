package com.fulfilment.application.monolith.exception;

public class WarehousePersistenceException extends RuntimeException {
    public WarehousePersistenceException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
