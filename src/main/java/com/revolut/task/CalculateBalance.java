package com.revolut.task;

import com.revolut.task.exception.BalanceNegativeException;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

public abstract class CalculateBalance implements Callable<BigDecimal> {

    protected String id;
    protected AccountRepository repo;
    protected BigDecimal amount;

    public CalculateBalance(String id, AccountRepository repo, BigDecimal amount) {
        this.id = id;
        this.repo = repo;
        this.amount = amount;
    }

    @Override
    public BigDecimal call() throws BalanceNegativeException {
        BigDecimal balance = this.repo.getAccount(this.id).getBalance();
        return calculateNewBalance(balance, this.amount);
    }

    protected abstract BigDecimal calculateNewBalance(BigDecimal balance, BigDecimal amount) throws BalanceNegativeException;
}
