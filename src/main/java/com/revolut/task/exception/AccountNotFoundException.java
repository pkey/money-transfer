package com.revolut.task.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException() {
        super();
    }
    public AccountNotFoundException(String s) {
        super(s);
    }
  }