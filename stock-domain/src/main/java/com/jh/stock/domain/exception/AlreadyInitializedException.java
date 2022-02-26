package com.jh.stock.domain.exception;

public class AlreadyInitializedException extends RuntimeException {
    public AlreadyInitializedException(String msg) {
        super(msg + " is already initialized.");
    }
}
