package com.revolut.task.model;

import java.math.BigDecimal; 
import java.util.UUID;

public class Account {
    private String id;
    private BigDecimal balance;

    public Account() {
        this.id = UUID.randomUUID().toString();
        this.balance = new BigDecimal(0);
    }

    public Account(String id) {
        this.id = id;
        this.balance = new BigDecimal(0);
    }

    public Account(BigDecimal balance) {
        this.id = UUID.randomUUID().toString();
        this.balance = balance;
    }

    public Account(String id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public String getId() {
        return this.id;
    }

    
}