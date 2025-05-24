package com.infsus.finapp.rest;

import com.infsus.finapp.domain.Account;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.domain.Transaction;
import com.infsus.finapp.dto.TransactionDTO;
import com.infsus.finapp.service.AccountService;
import com.infsus.finapp.service.PersonService;
import com.infsus.finapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PersonService personService;

    @Autowired
    private AccountService accountService;

    @PostMapping(path = "/createTransaction/{accountName}")
    public void createTransaction(@PathVariable("accountName") String accountName, @RequestBody TransactionDTO transactionDTO, @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        Person person = personService.findByEmail(email);
        List<Account> accounts = accountService.listPersonsAccounts(email);
        Account account = null;

        if (!(accountName == null)) {
            for (Account accountt : accounts) {
                if (accountName.equals(accountt.getAccountName())) {
                    account = accountt;
                }
            }
        }
        transactionService.createTransaction(transactionDTO, person, account);
    }

    @GetMapping("/listTransactionNames")
    public List<String> listPersonsTransactionNames(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        List<Transaction> transactions = transactionService.listPersonsTransactions(email);
        List<String> show = new ArrayList<>();

        for (Transaction transaction : transactions) {
            show.add(transaction.getTransactionName());
        }
        return show;
    }

    @GetMapping("/listTransactionDetails")
    public List<TransactionDTO> listPersonsTransactions(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        List<Transaction> transactions = transactionService.listPersonsTransactions(email);
        return transactions.stream()
                .map(transaction -> new TransactionDTO(transaction.getId(), transaction.getTransactionName(), transaction.getDateOfTransaction(), transaction.getNote(), transaction.getTransactionType(), transaction.getTransactionAmount(), transaction.getCurrency(), transaction.getCategory(), transaction.getAccount().getAccountName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/listTodaysTransactions")
    public List<TransactionDTO> listTodaysTransactions(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        List<Transaction> transactions = transactionService.listPersonsTransactions(email);

        LocalDate today = LocalDate.now();

        return transactions.stream()
                .filter(transaction -> isToday(transaction.getDateOfTransaction()))
                .map(transaction -> new TransactionDTO(
                        transaction.getId(),
                        transaction.getTransactionName(),
                        transaction.getDateOfTransaction(),
                        transaction.getNote(),
                        transaction.getTransactionType(),
                        transaction.getTransactionAmount(),
                        transaction.getCurrency(),
                        transaction.getCategory(),
                        transaction.getAccount().getAccountName()))
                .collect(Collectors.toList());
    }

    private boolean isToday(Date date) {
        LocalDate transactionDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate today = LocalDate.now();
        return transactionDate.isEqual(today);
    }

    @GetMapping("/deleteTransaction/{id}")
    public void deleteTransaction(@PathVariable("id") long id, @AuthenticationPrincipal User user) {

        Person person = personService.findByEmail(user.getUsername());
        transactionService.deleteTransaction(id, person);
    }

    @PostMapping("/updateTransaction/{id}")
    public void updateTransaction(@PathVariable("id") long id, @RequestBody TransactionDTO transactionDTO, @AuthenticationPrincipal User user) {
        Person person = personService.findByEmail(user.getUsername());
        transactionService.updateTransaction(id, transactionDTO, person);
    }

    @GetMapping("/listAccountTransactions")
    public List<TransactionDTO> listAccountTransactions(@RequestParam String accountName,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                                                        @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        List<Transaction> transactions = transactionService.listTransactionsByDateAndAccountPDF(email, startDate, endDate, accountName);
        return transactions.stream()
                .map(transaction -> new TransactionDTO(
                        transaction.getId(),
                        transaction.getTransactionName(),
                        transaction.getTransactionAmount(),
                        transaction.getDateOfTransaction(),
                        transaction.getTransactionType(),
                        transaction.getNote(),
                        transaction.getCategory().getCategoryName(),
                        transaction.getAccount().getAccountName(),
                        transaction.getCurrency().getCurrencyCode()))
                .collect(Collectors.toList());
    }
}
