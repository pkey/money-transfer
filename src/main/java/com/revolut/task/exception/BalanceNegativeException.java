package com.revolut.task.exception;

public class BalanceNegativeException extends RuntimeException {
    public BalanceNegativeException() {
        super();
    }

    public BalanceNegativeException(String s) {
        super(s);
    }
}