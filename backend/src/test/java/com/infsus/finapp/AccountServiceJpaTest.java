package com.infsus.finapp;

import com.infsus.finapp.dao.AccountRepository;
import com.infsus.finapp.domain.Account;
import com.infsus.finapp.domain.Currency;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.AccountDTO;
import com.infsus.finapp.service.CurrencyService;
import com.infsus.finapp.service.PersonService;
import com.infsus.finapp.service.impl.AccountServiceJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceJpaTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PersonService personService;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private AccountServiceJpa accountService;

    private Person person;
    private Currency currency;
    private Account account;
    private AccountDTO accountDTO;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setPersonId(1L);

        currency = new Currency();
        currency.setCurrencyCode("RSD");

        account = new Account();
        account.setId(1L);
        account.setAccountName("TestAccount");
        account.setBalance(100.00);
        account.setPerson(person);
        account.setCurrency(currency);

        accountDTO = new AccountDTO();
        accountDTO.setId(1L);
        accountDTO.setAccountName("TestAccount");
        accountDTO.setBalance(100.00);
        accountDTO.setCurrencyId("RSD");
    }

    @Test
    void listAll_shouldReturnAccounts() {
        given(accountRepository.findAll()).willReturn(List.of(account));

        List<Account> accounts = accountService.listAll();

        assertEquals(1, accounts.size());
        verify(accountRepository).findAll();
    }

    @Test
    void listPersonsAccounts_shouldReturnAccountsForPerson() {
        given(personService.findByEmail("email@test.com")).willReturn(person);
        given(accountRepository.findAllByPerson_PersonId(person.getPersonId())).willReturn(List.of(account));

        List<Account> accounts = accountService.listPersonsAccounts("email@test.com");

        assertEquals(1, accounts.size());
        verify(personService).findByEmail("email@test.com");
        verify(accountRepository).findAllByPerson_PersonId(person.getPersonId());
    }

    @Test
    void createAccount_shouldSaveAndReturnAccountName() {
        given(currencyService.findByCurrencyCode("RSD")).willReturn(currency);
        given(accountRepository.findByAccountName("TestAccount")).willReturn(null);

        String accountName = accountService.createAccount(accountDTO, person);

        assertEquals("TestAccount", accountName);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_duplicateAccount_shouldThrow() {
        given(accountRepository.findByAccountName("TestAccount")).willReturn(account);
        person.getAccounts().add(account);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount(accountDTO, person);
        });

        assertTrue(exception.getMessage().contains("Račun s tim nazivom već postoji"));
    }

    @Test
    void updateAccount_existingAccount_shouldUpdate() {
        given(accountRepository.findById(1L)).willReturn(account);
        given(currencyService.findByCurrencyCode("RSD")).willReturn(currency);

        accountService.updateAccount(accountDTO, person);

        verify(accountRepository).save(account);
        assertEquals("TestAccount", account.getAccountName());
        assertEquals(100.00, account.getBalance());
        assertEquals(currency, account.getCurrency());
    }

    @Test
    void updateAccount_nonExistingAccount_shouldThrow() {
        given(accountRepository.findById(1L)).willReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.updateAccount(accountDTO, person);
        });

        assertTrue(exception.getMessage().contains("ne postoji"));
    }

    @Test
    void deleteAccount_existingAccount_shouldDelete() {
        given(accountRepository.findById(1L)).willReturn(account);

        accountService.deleteAccount(1L);

        verify(accountRepository).deleteById(1L);
    }

    @Test
    void findByAccountId_shouldReturnAccount() {
        given(accountRepository.findById(1L)).willReturn(account);

        Account result = accountService.findByAccountId(1L);

        assertEquals(account, result);
    }

    @Test
    void countByAccountName_shouldReturnCount() {
        given(accountRepository.countByAccountName("TestAccount")).willReturn(2);

        int count = accountService.countByAccountName("TestAccount");

        assertEquals(2, count);
    }
}
