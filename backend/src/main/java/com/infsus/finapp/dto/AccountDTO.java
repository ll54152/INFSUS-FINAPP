package com.infsus.finapp.dto;

import com.infsus.finapp.domain.Currency;

public class AccountDTO {
    private long id;
    private String accountName;
    private double balance;
    private long personId;
    private String currency;

    public AccountDTO() {
    }

    public AccountDTO(long id, String accountName, double balance, long personId, String currency) {
        this.id = id;
        this.accountName = accountName;
        this.balance = balance;
        this.personId = personId;
        this.currency = currency;
    }

    public AccountDTO(long id, String accountName, double balance, Currency currency) {
        this.id = id;
        this.accountName = accountName;
        this.balance = balance;
        this.currency = currency.getCurrencyCode();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrencyId(String currencyCode) {
        this.currency = currency;
    }
}

