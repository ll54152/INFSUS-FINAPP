package com.infsus.finapp.dao;

import com.infsus.finapp.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByTransactionName(String transactionName);

    Transaction findById(long id);

    int countByTransactionName(String transactionName);

    List<Transaction> findAllByPerson_PersonId(Long personId);

    List<Transaction> findAllByAccount_Id(Long accountId);

    List<Transaction> findAllByCategory_Id(Long categoryId);

    List<Transaction> findAllByAccount_AccountName(String accountName);

    //List<Transaction> findAllByAccount_AccountNameAndDateOfTransactionBetween(String accountName, Date startDate, Date endDate);
}

