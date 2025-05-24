package com.infsus.finapp.service;

import com.infsus.finapp.domain.Account;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.AccountDTO;

import java.util.List;

public interface AccountService {
    List<Account> listAll();

    List<Account> listPersonsAccounts(String email);

    int countByAccountName(String accountName);

    Account findByAccountName(String accountName);

    String createAccount(AccountDTO accountDTO, Person person);

    void updateAccount(AccountDTO accountDTO, Person person);

    void deleteAccount(long id);

    Account findByAccountId(long accountId);
}
