package com.revolut.task;

import com.google.inject.Inject;
import com.revolut.task.exception.BalanceNegativeException;
import com.revolut.task.exception.InsufficientFundsException;
import com.revolut.task.exception.InvalidAmountException;
import com.revolut.task.exception.SelfTransferException;
import com.revolut.task.model.Account;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException();
        }
        return this.accountRepository.updateAccount(id, newAmount);
    }

    public void transferMoney(String accountIdFrom, String accountIdTo, BigDecimal amount) throws InsufficientFundsException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }

        if (accountIdFrom.equals(accountIdTo)) {
            throw new SelfTransferException();
        }

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<BigDecimal> fromCalculation = executor.submit(new CalculateFromBalance(accountIdFrom, this.accountRepository, amount));
        Future<BigDecimal> toCalculation = executor.submit(new CalculateToBalance(accountIdTo, this.accountRepository, amount));

        //TODO: Transfer money later but make sure threads execute
        try {
            while (!fromCalculation.isDone());
            BigDecimal fromAmount = fromCalculation.get();
            while (!toCalculation.isDone());
            BigDecimal toAmount = toCalculation.get();
            executor.execute(() -> accountRepository.updateAccount(accountIdFrom, fromAmount));
            executor.execute(() -> accountRepository.updateAccount(accountIdTo, toAmount));
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        } catch (ExecutionException ex) {
            if (ex.getCause() instanceof BalanceNegativeException) {
                throw new InsufficientFundsException();
            }
        } finally {
            executor.shutdown();
            while (!executor.isTerminated());
        }
    }
}