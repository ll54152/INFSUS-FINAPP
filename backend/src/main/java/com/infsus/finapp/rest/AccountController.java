package com.infsus.finapp.rest;

import com.infsus.finapp.domain.Account;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.domain.Transaction;
import com.infsus.finapp.dto.AccountDTO;
import com.infsus.finapp.dto.TransactionDTO;
import com.infsus.finapp.service.AccountService;
import com.infsus.finapp.service.PersonService;
import com.infsus.finapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PersonService personService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping(path = "/createAccount")
    public void createAccount(@RequestBody AccountDTO accountDTO, @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        Person person = personService.findByEmail(email);
        accountService.createAccount(accountDTO, person);
    }

    @GetMapping("/listAccountNames")
    public List<String> listPersonsAccountsNames(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        List<Account> accounts = accountService.listPersonsAccounts(email);
        List<String> show = new ArrayList<>();

        for (Account account : accounts) {
            show.add(account.getAccountName());
        }

        return show;
    }

    @GetMapping("/listAccountDetails")
    public List<AccountDTO> listPersonsAccounts(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        List<Account> accounts = accountService.listPersonsAccounts(email);
        return accounts.stream()
                .map(account -> new AccountDTO(account.getId(), account.getAccountName(), account.getBalance(), account.getCurrency()))
                .collect(Collectors.toList());
    }

    @GetMapping("/deleteAccount/{id}")
    public void deleteAccount(@PathVariable("id") long id) {
        accountService.deleteAccount(id);
    }

    @PostMapping("/updateAccount/{accountName}")
    public void updateAccount(@RequestBody AccountDTO accountDTO, @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        Person person = personService.findByEmail(email);
        accountService.updateAccount(accountDTO, person);
    }

    @GetMapping("/listAccountsTransactions/{id}")
    public List<TransactionDTO> listAccountsTransactiions(@PathVariable("id") long id) {

        List<Transaction> transactions = transactionService.listAccountsTransactions(id);
        return transactions.stream()
                .map(transaction -> new TransactionDTO(transaction.getId(), transaction.getTransactionName(), transaction.getTransactionAmount(), transaction.getDateOfTransaction(), transaction.getTransactionType(), transaction.getNote(), transaction.getCategory().getCategoryName(), transaction.getAccount().getAccountName(), transaction.getCurrency().getCurrencyCode()))
                .collect(Collectors.toList());
    }
}
