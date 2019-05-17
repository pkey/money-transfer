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