package com.infsus.finapp.service;

import com.infsus.finapp.domain.Account;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.domain.Transaction;
import com.infsus.finapp.dto.CategoryTransactionSummaryDTO;
import com.infsus.finapp.dto.DailyTransactionSummaryDTO;
import com.infsus.finapp.dto.TransactionDTO;

import java.util.Date;
import java.util.List;

public interface TransactionService {
    List<Transaction> listAll();

    List<Transaction> listPersonsTransactions(String email);

    List<Transaction> listAccountsTransactions(long id);

    List<TransactionDTO> listCategoryTransactions(long id);

    int countByTransactionName(String transactionName);

    Transaction findById(long id);

    Transaction findByTransactionName(String transactionName);

    void createTransaction(TransactionDTO transactionDTO, Person person, Account account);

    void updateTransaction(long id, TransactionDTO transactionDTO, Person person);

    void deleteTransaction(long id, Person person);

    List<DailyTransactionSummaryDTO> listTransactionsByDateAndAccount(String email, Date startDate, Date endDate, String accountName);

    List<CategoryTransactionSummaryDTO> listTransactionsByDateAndAccountForPie(String email, Date startDate, Date endDate, String accountName);

    List<Transaction> listTransactionsByDateAndAccountPDF(String email, Date startDate, Date endDate, String accountName);

}
