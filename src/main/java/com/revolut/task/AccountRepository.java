package com.revolut.task;

// import java.math.BigDecimal; 
import com.revolut.task.model.Account;
import java.util.UUID;
import java.math.BigDecimal; 

public class AccountRepository {
    
    public static Account getAccount(String id) {
        return new Account();
    }

    public static Account createAccount() {
        return new Account();
    }

    public static Account deleteAccount(String id) {
        return new Account();
    }

    //TODO: Handle wrong amounts or sth
    public static Account updateAccount(String id, BigDecimal newAmount) {
        Account acc = new Account();
        acc.setAmount(newAmount);
        return acc;
    }
}