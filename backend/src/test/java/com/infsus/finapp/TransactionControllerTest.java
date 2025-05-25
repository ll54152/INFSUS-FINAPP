package com.infsus.finapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infsus.finapp.domain.Account;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.domain.Transaction;
import com.infsus.finapp.dto.TransactionDTO;
import com.infsus.finapp.service.AccountService;
import com.infsus.finapp.service.PersonService;
import com.infsus.finapp.service.TransactionService;
import com.infsus.finapp.rest.TransactionController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)

public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private PersonService personService;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private Person mockPerson;
    private Account mockAccount;
    private Transaction mockTransaction;

    @BeforeEach
    public void setup() {
        mockPerson = new Person();
        mockPerson.setPersonId(1L);
        mockPerson.setEmail("user@example.com");

        mockAccount = new Account();
        mockAccount.setAccountName("TestAccount");
        mockAccount.setId(1L);

        mockTransaction = new Transaction();
        mockTransaction.setId(1L);
        mockTransaction.setTransactionName("TestTransaction");
        mockTransaction.setDateOfTransaction(new Date());
        mockTransaction.setAccount(mockAccount);
    }

    @Test
    public void createTransaction_shouldCallServices() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionName("TestTransaction");
        transactionDTO.setTransactionAmount(100.0);

        when(personService.findByEmail(anyString())).thenReturn(mockPerson);
        when(accountService.listPersonsAccounts(anyString())).thenReturn(List.of(mockAccount));


        mockMvc.perform(post("/transaction/createTransaction/{accountName}", "TestAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO))
                        .with(user("user@example.com"))
                        .with(csrf())
                )
                .andExpect(status().isOk());

        verify(personService).findByEmail("user@example.com");
        verify(accountService).listPersonsAccounts("user@example.com");
        verify(transactionService).createTransaction(any(TransactionDTO.class), eq(mockPerson), eq(mockAccount));
    }

    @Test
    public void listPersonsTransactionNames_shouldReturnNames() throws Exception {
        when(transactionService.listPersonsTransactions(anyString())).thenReturn(List.of(mockTransaction));

        mockMvc.perform(get("/transaction/listTransactionNames")
                        .with(user("user@example.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("TestTransaction"));

        verify(transactionService).listPersonsTransactions("user@example.com");
    }

    @Test
    public void deleteTransaction_shouldCallService() throws Exception {
        when(personService.findByEmail(anyString())).thenReturn(mockPerson);

        mockMvc.perform(get("/transaction/deleteTransaction/{id}", 1L)
                        .with(user("user@example.com")))
                .andExpect(status().isOk());

        verify(transactionService).deleteTransaction(1L, mockPerson);
    }

    @Test
    public void updateTransaction_shouldCallService() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionName("UpdatedTransaction");

        when(personService.findByEmail(anyString())).thenReturn(mockPerson);

        mockMvc.perform(post("/transaction/updateTransaction/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO))
                        .with(user("user@example.com")).with(csrf()))

                .andExpect(status().isOk());

        verify(transactionService).updateTransaction(eq(1L), any(TransactionDTO.class), eq(mockPerson));
    }
}