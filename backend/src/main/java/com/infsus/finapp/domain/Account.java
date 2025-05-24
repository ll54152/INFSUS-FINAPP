package com.infsus.finapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Account")
public class Account {

    @Id
    @GeneratedValue
    private long id;
    @NotNull
    //@Column(unique = true)
    private String accountName;
    @NotNull
    private double balance;

    @ManyToOne
    @JoinColumn(name = "personId")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "currencyCode")
    private Currency currency;

    @JsonIgnore
    @OneToMany(mappedBy = "account", orphanRemoval = true)
    private Set<Transaction> transactions = new HashSet<>();

    public Account() {
    }

    public Account(long id, String accountName, double balance, Person person, Currency currency) {
        this.id = id;
        this.accountName = accountName;
        this.balance = balance;
        this.person = person;
        this.currency = currency;
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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
}

