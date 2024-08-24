package com.swiss.wallet.exception;

public class UserUniqueViolationException extends RuntimeException {

    public UserUniqueViolationException(String message) {
        super(message);
    }
}

