package com.infsus.finapp.service.impl;

import com.infsus.finapp.dao.AccountRepository;
import com.infsus.finapp.domain.Account;
import com.infsus.finapp.domain.Currency;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.AccountDTO;
import com.infsus.finapp.service.AccountService;
import com.infsus.finapp.service.CurrencyService;
import com.infsus.finapp.service.PersonService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class AccountServiceJpa implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private CurrencyService currencyService;

    @Override
    public List<Account> listAll() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> listPersonsAccounts(String email) {
        Person person = personService.findByEmail(email);
        return accountRepository.findAllByPerson_PersonId(person.getPersonId());
    }

    @Override
    public int countByAccountName(String accountName) {
        return accountRepository.countByAccountName(accountName);
    }

    @Override
    public Account findByAccountName(String accountName) {
        return accountRepository.findByAccountName(accountName);
    }

    @Override
    public String createAccount(AccountDTO accountDTO, Person person) {

        Assert.hasText(accountDTO.getAccountName(), "Ime računa mora biti navedeno!");
        if (person.getAccounts().contains(accountRepository.findByAccountName(accountDTO.getAccountName()))) {
            Assert.hasText("", "Račun s tim nazivom već postoji!");
        }
        Assert.notNull(accountDTO.getBalance(), "Stanje računa mora biti navedeno!");
        Currency currency = currencyService.findByCurrencyCode(accountDTO.getCurrency());
        Assert.notNull(currency, "Morate odabrati valutu!");

        Account account = new Account(
                accountDTO.getId(),
                accountDTO.getAccountName(),
                accountDTO.getBalance(),
                person,
                currency
        );
        accountRepository.save(account);
        return account.getAccountName();
    }

    @Override
    @Transactional
    public void updateAccount(AccountDTO accountDTO, Person person) {

        Account existingAccount = accountRepository.findById(accountDTO.getId());
        if (existingAccount == null) {
            throw new IllegalArgumentException("Račun s nazivom " + accountDTO.getAccountName() + " ne postoji!");
        }

        Assert.hasText(accountDTO.getAccountName(), "Ime računa mora biti navedeno!");
        Assert.notNull(accountDTO.getBalance(), "Stanje računa mora biti navedeno!");

        Currency currency = currencyService.findByCurrencyCode(accountDTO.getCurrency());
        Assert.notNull(currency, "Morate odabrati valutu!");

        existingAccount.setAccountName(accountDTO.getAccountName());
        existingAccount.setBalance(accountDTO.getBalance());
        existingAccount.setCurrency(currency);

        accountRepository.save(existingAccount);
    }


    @Override
    @Transactional
    public void deleteAccount(long id) {
        Account account = accountRepository.findById(id);
        if (account == null) {
            Assert.hasText("", "Račun s nazivom " + account.getAccountName() + " ne postoji!");
        }
        account.getTransactions().clear();
        accountRepository.deleteById(id);
    }

    @Override
    public Account findByAccountId(long accountId) {
        return accountRepository.findById(accountId);
    }
}
