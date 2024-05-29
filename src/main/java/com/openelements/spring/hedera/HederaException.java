package com.openelements.spring.hedera;

public class HederaException extends Exception {

    public HederaException(String message) {
        super(message);
    }

    public HederaException(String message, Throwable cause) {
        super(message, cause);
    }
}
