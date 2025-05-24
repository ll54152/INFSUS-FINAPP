package com.infsus.finapp.dto;

import com.infsus.finapp.domain.Category;
import com.infsus.finapp.domain.Currency;
import com.infsus.finapp.domain.Person;

import java.util.Date;

public class TransactionDTO {

    private long id;
    private String transactionName;
    private Date dateOfTransaction;
    private String note;
    private String typeOfTransaction;
    private double transactionAmount;
    private Person person;
    private String currency;
    private String category;
    private String accountName;

    public TransactionDTO() {
    }

    public TransactionDTO(long id, String transactionName, Date dateOfTransaction, String note, String typeOfTransaction, double transactionAmount, Person person, String currency, String category, String accountName) {
        this.id = id;
        this.transactionName = transactionName;
        this.dateOfTransaction = dateOfTransaction;
        this.note = note;
        this.typeOfTransaction = typeOfTransaction;
        this.transactionAmount = transactionAmount;
        this.person = person;
        this.currency = currency;
        this.category = category;
        this.accountName = accountName;
    }

    public TransactionDTO(long id, String transactionName, Date dateOfTransaction, String note, String transactionType, double transactionAmount, Currency currency, Category category, String accountName) {
        this.id = id;
        this.transactionName = transactionName;
        this.dateOfTransaction = dateOfTransaction;
        this.note = note;
        this.typeOfTransaction = transactionType;
        this.transactionAmount = transactionAmount;
        this.currency = currency.getCurrencyCode();
        this.category = category.getCategoryName();
        this.accountName = accountName;
    }

    public TransactionDTO(long id, String transactionName, double transactionAmount, Date dateOfTransaction, String transactionType, String note, String categoryName, String accountName, String currency) {
        this.id = id;
        this.transactionName = transactionName;
        this.dateOfTransaction = dateOfTransaction;
        this.note = note;
        this.typeOfTransaction = transactionType;
        this.transactionAmount = transactionAmount;
        this.category = categoryName;
        this.accountName = accountName;
        this.currency = currency;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTypeOfTransaction() {
        return typeOfTransaction;
    }

    public void setTypeOfTransaction(String typeOfTransaction) {
        this.typeOfTransaction = typeOfTransaction;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }
}
