package com.revolut.task.exception;

public class BalanceNegativeException extends Exception {
    public BalanceNegativeException() {
        super();
    }

    public BalanceNegativeException(String s) {
        super(s);
    }
}