package com.fulfilment.application.monolith.exception;

public class InvalidLocationException extends RuntimeException {
    public InvalidLocationException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
