package com.revolut.task;

// import java.math.BigDecimal; 
import com.revolut.task.exception.BalanceNegativeException;
import com.revolut.task.exception.NegativeTransferAmountException;
import com.revolut.task.model.Account;

import java.math.BigDecimal;

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

    public static Account updateAccount(String id, BigDecimal newAmount) {
        return AccountRepository.updateAccount(id, newAmount);
    }

    public static void transferMoney (String accountIdFrom, String accountIdTo, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeTransferAmountException();
        }
        Account accFrom = AccountRepository.getAccount(accountIdFrom);
        Account accTo = AccountRepository.getAccount(accountIdTo);

        if (accFrom.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceNegativeException();
        }

        accFrom.setBalance(accFrom.getBalance().subtract(amount));
        accTo.setBalance(accTo.getBalance().add(amount));
    }
}