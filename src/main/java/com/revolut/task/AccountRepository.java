package com.revolut.task;

// import java.math.BigDecimal; 
import com.revolut.task.model.Account;
import com.revolut.task.exception.AccountNotFoundException;

import java.util.UUID;
import java.math.BigDecimal; 
import java.util.ArrayList;
import java.util.List;


public class AccountRepository {

    private static List<Account> accounts = new ArrayList<>();

    public static Account getAccount(String id) {
        return accounts.stream().filter(a -> a.getId() == id).findFirst().orElseThrow(AccountNotFoundException::new);
    }
    
    public static Account createAccount() {
        Account acc = new Account();
        accounts.add(acc);
        return acc;
    }

    public static Account deleteAccount(String id) {
        Account acc = accounts.stream().filter(a -> a.getId() == id).findFirst().get();
        accounts.remove(acc);
        return acc;
    }

    public static Account updateAccount(String id, BigDecimal newAmount) {
        Account acc = accounts.stream().filter(a -> a.getId() == id).findFirst().orElseThrow(AccountNotFoundException::new);
        acc.setAmount(newAmount);
        return acc;
    }
}