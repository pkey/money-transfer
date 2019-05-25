package com.revolut.task;

import com.revolut.task.exception.BalanceNegativeException;
import com.revolut.task.exception.InsufficientFundsException;

import java.math.BigDecimal;

public class CalculateFromBalance extends CalculateBalance {

    public CalculateFromBalance(String id, AccountRepository repo, BigDecimal amount) {
        super(id, repo, amount);
    }

    @Override
    protected BigDecimal calculateNewBalance(BigDecimal balance, BigDecimal amount) throws BalanceNegativeException {
        BigDecimal newBalance = balance.subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceNegativeException();
        }
        return newBalance;
    }
}
