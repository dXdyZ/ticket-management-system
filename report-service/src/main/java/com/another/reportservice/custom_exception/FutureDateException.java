package com.another.reportservice.custom_exception;

public class FutureDateException extends Exception{
    public FutureDateException(String message) {
        super(message);
    }
}
