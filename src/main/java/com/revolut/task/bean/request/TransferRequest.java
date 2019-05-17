package com.revolut.task.bean.request;

import java.math.BigDecimal;

public class TransferRequest {
    private String accountFromId;
    private String accountToId;
    private BigDecimal amount;

    public TransferRequest() {
    }

    public String getAccountFromId() {
        return accountFromId;
    }

    public String getAccountToId() {
        return accountToId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
