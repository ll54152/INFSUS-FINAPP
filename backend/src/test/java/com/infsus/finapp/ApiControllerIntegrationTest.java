package com.infsus.finapp;

import com.infsus.finapp.dao.AccountRepository;
import com.infsus.finapp.dao.TransactionRepository;
import com.infsus.finapp.domain.Account;
import com.infsus.finapp.domain.Currency;
import com.infsus.finapp.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Account account;
    private Currency currency;

    @BeforeEach
    public void setup() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();

        currency = new Currency();
        currency.setCurrencyCode("EUR");
        currency.setConversionToEuro(1.0);

        account = new Account();
        account.setAccountName("Test Account");
        account.setBalance(1000.0);
        account.setCurrency(currency);

        accountRepository.save(account);
    }

    @Test
    public void testCheckTransactions_post() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTransactionName("Test Income");
        transaction.setTransactionAmount(100.0);
        transaction.setDateOfTransaction(Date.from(LocalDate.now()
                .atStartOfDay(ZoneId.of("Europe/Zagreb")).toInstant()));
        transaction.setTransactionType("prihod");
        transaction.setDone(false);
        transaction.setAccount(account);
        transaction.setCurrency(currency);

        transactionRepository.save(transaction);

        mockMvc.perform(post("/api/checkTransactions"))
                .andExpect(status().isOk());

        Account updatedAccount = accountRepository.findById(account.getId());
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId());

        assert (updatedAccount.getBalance() == 1100.0);
        assert (updatedTransaction.isDone());
    }

    @Test
    public void testCheckTransactions_get() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTransactionName("Test Expense");
        transaction.setTransactionAmount(200.0);
        transaction.setDateOfTransaction(Date.from(LocalDate.now()
                .atStartOfDay(ZoneId.of("Europe/Zagreb")).toInstant()));
        transaction.setTransactionType("rashod");
        transaction.setDone(false);
        transaction.setAccount(account);
        transaction.setCurrency(currency);

        transactionRepository.save(transaction);

        mockMvc.perform(get("/api/checkTransactions"))
                .andExpect(status().isOk());

        Account updatedAccount = accountRepository.findById(account.getId());
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId());

        assert (updatedAccount.getBalance() == 800.0);
        assert (updatedTransaction.isDone());
    }
}
