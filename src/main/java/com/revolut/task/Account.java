package com.revolut.task;

import java.math.BigDecimal; 
import java.util.UUID;

public class Account {
    private String id;
    private BigDecimal amount;

    Account() {
        this.id = UUID.randomUUID().toString();
        this.amount = new BigDecimal(0);
    }

    void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    BigDecimal getAmount() {
        return this.amount;
    }
}