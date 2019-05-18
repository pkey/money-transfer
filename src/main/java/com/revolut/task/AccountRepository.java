package com.revolut.task;

import com.revolut.task.exception.AccountNotFoundException;
import com.revolut.task.exception.BalanceNegativeException;
import com.revolut.task.model.Account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class AccountRepository {

    private List<Account> accounts = new ArrayList<>();

    public AccountRepository() {
    }

    public Account getAccount(String id) {
        return accounts.stream().filter(a -> a.getId().equals(id)).findFirst().orElseThrow(AccountNotFoundException::new);
    }

    public Account createAccount() {
        Account acc = new Account();
        accounts.add(acc);
        return acc;
    }

    public Account deleteAccount(String id) {
        Account acc = accounts.stream().filter(a -> a.getId().equals(id)).findFirst().orElseThrow(AccountNotFoundException::new);
        accounts.remove(acc);
        return acc;
    }

    public Account updateAccount(String id, BigDecimal newAmount) {
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceNegativeException();
        }
        Account acc = accounts.stream().filter(a -> a.getId().equals(id)).findFirst().orElseThrow(AccountNotFoundException::new);
        acc.setBalance(newAmount);
        return acc;
    }
}