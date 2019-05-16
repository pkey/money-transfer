package com.revolut.task;

// import java.math.BigDecimal; 
import com.revolut.task.model.Account;
import java.util.UUID;
import java.math.BigDecimal; 
import com.google.inject.Inject;

public class AccountService {
    public static Account getAccount(String id) {
        return AccountRepository.getAccount(id);
    }

    public static Account createAccount() {
        return AccountRepository.createAccount();
    }

    public static Account deleteAccount(String id) {
        return AccountRepository.deleteAccount(id);
    }

    //TODO: Handle wrong amounts or sth
    public static Account updateAccount(String id, BigDecimal newAmount) {
        return AccountRepository.updateAccount(id, newAmount);
    }
}