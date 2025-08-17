package com.akkodis.codingchallenge.taco.service.exceptions;

public class NotEnoughAmountException extends RuntimeException {
    public NotEnoughAmountException(String msg) {
        super(msg);
    }
}
