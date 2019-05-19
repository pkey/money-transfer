package com.revolut.task;

import com.google.inject.Inject;
import com.revolut.task.exception.BalanceNegativeException;
import com.revolut.task.exception.InvalidAmountException;
import com.revolut.task.exception.SelfTransferException;
import com.revolut.task.model.Account;

import java.math.BigDecimal;

public class AccountService {

    private final AccountRepository accountRepository;

    @Inject
    AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(String id) {
        return this.accountRepository.getAccount(id);
    }

    public Account createAccount() {
        return this.accountRepository.createAccount();
    }

    public Account deleteAccount(String id) {
        return this.accountRepository.deleteAccount(id);
    }

    public Account updateAccount(String id, BigDecimal newAmount) {
        return this.accountRepository.updateAccount(id, newAmount);
    }

    public void transferMoney(String accountIdFrom, String accountIdTo, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }
        if (accountIdFrom.equals(accountIdTo)) {
            throw new SelfTransferException();
        }
        Account accFrom = this.accountRepository.getAccount(accountIdFrom);
        Account accTo = this.accountRepository.getAccount(accountIdTo);

        if (accFrom.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceNegativeException();
        }

        this.accountRepository.updateAccount(accountIdFrom, accFrom.getBalance().subtract(amount));
        this.accountRepository.updateAccount(accountIdTo, accTo.getBalance().add(amount));
    }
}