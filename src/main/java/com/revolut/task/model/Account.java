package com.revolut.task.model;

import java.math.BigDecimal; 
import java.util.UUID;

public class Account {
    private String id;
    private BigDecimal amount;

    public Account() {
        this.id = UUID.randomUUID().toString();
        this.amount = new BigDecimal(0);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public String getId() {
        return this.id;
    }

    
}