package com.infsus.finapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import lombok.NonNull;

import java.util.Date;

@Entity
@Table(name = "Transaction")
public class Transaction {

    @Id
    @GeneratedValue
    private long id;
    @NonNull
    private Date dateOfTransaction;
    @NonNull
    private String transactionName;
    @NonNull
    private double transactionAmount;
    @NonNull
    private String transactionType;

    private String note;
    boolean isDone;
    @ManyToOne
    @JoinColumn(name = "personId")
    private Person person;
    @ManyToOne
    @JoinColumn(name = "currencyCode")
    private Currency currency;
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    public Transaction() {
    }

    public Transaction(long id, @NonNull Date dateOfTransaction, @NonNull String transactionName, @NonNull double transactionAmount, @NonNull String transactionType, String note, boolean isDone, Person person, Currency currency, Category category, Account account) {
        this.id = id;
        this.dateOfTransaction = dateOfTransaction;
        this.transactionName = transactionName;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.note = note;
        this.isDone = isDone;
        this.person = person;
        this.currency = currency;
        this.category = category;
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
