package com.revolut.task;

import java.math.BigDecimal;

public class CalculateToBalance extends CalculateBalance {

    public CalculateToBalance(String id, AccountRepository repo, BigDecimal amount) {
        super(id, repo, amount);
    }

    @Override
    protected BigDecimal calculateNewBalance(BigDecimal balance, BigDecimal amount) {
        return balance.add(amount);
    }
}
