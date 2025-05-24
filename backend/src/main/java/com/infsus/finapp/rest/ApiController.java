package com.infsus.finapp.rest;

import com.infsus.finapp.dao.AccountRepository;
import com.infsus.finapp.dao.TransactionRepository;
import com.infsus.finapp.domain.Account;
import com.infsus.finapp.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping(path = "/checkTransactions")
    public void checkTransactions() {
        processTransactions();
    }

    @GetMapping(path = "/checkTransactions")
    public void checkTransactionsGet() {
        processTransactions();
    }

    private void processTransactions() {
        ZoneId zoneId = ZoneId.of("Europe/Zagreb");
        LocalDate today = LocalDate.now(zoneId);
        List<Transaction> transactions = transactionRepository.findAll();

        for (Transaction transaction : transactions) {

            LocalDate transactionDate = transaction.getDateOfTransaction().toInstant().atZone(zoneId).toLocalDate();
            Account account = transaction.getAccount();

            if ((transactionDate.isEqual(today) || transactionDate.isBefore(today)) && !transaction.isDone()) {

                if (transaction.getTransactionType().equals("prihod")) {

                    if (transaction.getCurrency().equals(account.getCurrency())) {
                        account.setBalance(account.getBalance() + transaction.getTransactionAmount());
                    } else {
                        double amount = (transaction.getTransactionAmount() * transaction.getCurrency().getConversionToEuro()) / account.getCurrency().getConversionToEuro();
                        account.setBalance(account.getBalance() + amount);
                    }
                    transaction.setDone(true);

                } else if (transaction.getTransactionType().equals("rashod")) {

                    if (transaction.getCurrency().equals(account.getCurrency())) {
                        account.setBalance(account.getBalance() - transaction.getTransactionAmount());
                    } else {
                        double amount = (transaction.getTransactionAmount() * transaction.getCurrency().getConversionToEuro()) / account.getCurrency().getConversionToEuro();
                        account.setBalance(account.getBalance() - amount);
                    }
                    transaction.setDone(true);
                } else if (transaction.getTransactionType().equals("transakcijaIzmeduRacuna")) {

                }
            }
            accountRepository.save(account);
            transactionRepository.save(transaction);
        }
    }
}

