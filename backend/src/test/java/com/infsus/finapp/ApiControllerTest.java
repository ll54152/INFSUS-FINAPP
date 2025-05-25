package com.infsus.finapp;

import com.infsus.finapp.dao.AccountRepository;
import com.infsus.finapp.dao.TransactionRepository;
import com.infsus.finapp.domain.Account;
import com.infsus.finapp.domain.Currency;
import com.infsus.finapp.domain.Transaction;
import com.infsus.finapp.rest.ApiController;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ApiController.class)
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @Test
    void checkTransactions_post_shouldProcessAndSaveTransactions() throws Exception {
        ZoneId zoneId = ZoneId.of("Europe/Zagreb");
        LocalDate today = LocalDate.now(zoneId);

        Currency eur = new Currency();
        eur.setCurrencyCode("EUR");
        eur.setConversionToEuro(1.0);
        Currency usd = new Currency();
        usd.setCurrencyCode("USD");
        usd.setConversionToEuro(0.9);

        Account account = new Account();
        account.setId(1L);
        account.setBalance(1000);
        account.setCurrency(eur);

        Transaction transaction1 = new Transaction();
        transaction1.setId(10L);
        transaction1.setDateOfTransaction(Date.from(today.atStartOfDay(zoneId).toInstant()));
        transaction1.setDone(false);
        transaction1.setTransactionType("prihod");
        transaction1.setTransactionAmount(100);
        transaction1.setCurrency(eur);
        transaction1.setAccount(account);

        Transaction transaction2 = new Transaction();
        transaction2.setId(11L);
        transaction2.setDateOfTransaction(Date.from(today.atStartOfDay(zoneId).toInstant()));
        transaction2.setDone(false);
        transaction2.setTransactionType("rashod");
        transaction2.setTransactionAmount(50);
        transaction2.setCurrency(usd);
        transaction2.setAccount(account);

        List<Transaction> transactions = List.of(transaction1, transaction2);

        when(transactionRepository.findAll()).thenReturn(transactions);
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(post("/api/checkTransactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(2)).save(accountCaptor.capture());

        Account updatedAccount = accountCaptor.getValue();


        assertThat(updatedAccount.getBalance()).isEqualTo(1055);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(2)).save(transactionCaptor.capture());

        for (Transaction t : transactionCaptor.getAllValues()) {
            assertThat(t.isDone()).isTrue();
        }
    }

    @Test
    void checkTransactions_get_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/checkTransactions"))
                .andExpect(status().isOk());

        verify(transactionRepository).findAll();
    }
}