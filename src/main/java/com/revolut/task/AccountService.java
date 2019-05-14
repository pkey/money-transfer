package com.revolut.task;

// import java.math.BigDecimal; 
import com.revolut.task.Account;
import java.util.UUID;
import java.math.BigDecimal; 

public class AccountService {
    Account getAccount(String id) {
        return new Account();
    }

    Account createAccount() {
        return new Account();
    }

    Account deleteAccount(String id) {
        return new Account();
    }

    Account updateAccount(String id, BigDecimal newAmount) {
        Account acc = new Account();
        acc.setAmount(newAmount);
        return acc;
    }
}