package com.revolut.task;

import com.revolut.task.exception.AccountNotFoundException;
import com.revolut.task.exception.BalanceNegativeException;
import com.revolut.task.model.Account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class AccountRepository {

    private ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    public AccountRepository() { }

    public Account getAccount(String id) {
        if (!accounts.containsKey(id)) throw new AccountNotFoundException();
        return accounts.get(id);
    }

    public Account createAccount() {
        Account acc = new Account();
        accounts.put(acc.getId(), acc);
        return acc;
    }

    public Account deleteAccount(String id) {
        Account acc = getAccount(id);
        accounts.remove(id);
        return acc;
    }

    public Account updateAccount(String id, BigDecimal newAmount) {
        Account acc = getAccount(id);
        acc.setBalance(newAmount);
        return acc;
    }
}