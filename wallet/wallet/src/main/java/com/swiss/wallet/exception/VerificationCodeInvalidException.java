package com.swiss.wallet.exception;

public class VerificationCodeInvalidException extends RuntimeException {
    public VerificationCodeInvalidException(String message) {
        super(message);
    }
}
