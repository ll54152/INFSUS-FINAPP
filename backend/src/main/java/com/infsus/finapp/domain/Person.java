package com.infsus.finapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Person")
public class Person {

    @Id
    @GeneratedValue
    private Long personId;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;
    @NotNull
    private String name;
    @NotNull
    private String surname;

    @JsonIgnore
    @OneToMany(mappedBy = "person", orphanRemoval = true)
    private Set<Account> accounts = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "person", orphanRemoval = true)
    private Set<Transaction> transactions = new HashSet<>();

//    @JsonIgnore
//    @OneToMany(mappedBy = "person", orphanRemoval = true)
//    private Set<Budget> budgets = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "person", orphanRemoval = true)
    private Set<Category> categories = new HashSet<>();


    public Person(Long personId, String email, String password, String name, String surname) {
        this.personId = personId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

//    public Set<Budget> getBudgets() {
//        return budgets;
//    }
//
//    public void setBudgets(Set<Budget> budgets) {
//        this.budgets = budgets;
//    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
