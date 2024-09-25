package com.swiss.wallet.exception;

public class OrderCartAlreadyPaidException extends RuntimeException {

    public OrderCartAlreadyPaidException(String message) {
        super(message);
    }
}
