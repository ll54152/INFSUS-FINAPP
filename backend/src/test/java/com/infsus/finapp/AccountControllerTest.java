package com.infsus.finapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.AccountDTO;
import com.infsus.finapp.rest.AccountController;
import com.infsus.finapp.service.AccountService;
import com.infsus.finapp.service.PersonService;
import com.infsus.finapp.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private PersonService personService;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "test@example.com")
    @Test
    void createAccount_shouldCallService() throws Exception {
        AccountDTO dto = new AccountDTO();
        dto.setAccountName("Test Account");
        dto.setBalance(1000);
        dto.setCurrencyId("EUR");

        User mockUser = new User("test@example.com", "pass", List.of());
        Person person = new Person();
        when(personService.findByEmail("test@example.com")).thenReturn(person);

        mockMvc.perform(post("/account/createAccount")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .principal(() -> "test@example.com"))
                .andExpect(status().isOk());

        verify(accountService).createAccount(any(AccountDTO.class), eq(person));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void listAccountNames_shouldReturnList() throws Exception {
        when(accountService.listPersonsAccounts("test@example.com"))
                .thenReturn(List.of(new com.infsus.finapp.domain.Account(1L, "Dev Account", 300, null, null)));

        mockMvc.perform(get("/account/listAccountNames")
                        .principal(() -> "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Dev Account"));
    }

    @Test
    void deleteAccount_shouldReturnNoContent() throws Exception {
        doNothing().when(accountService).deleteAccount(1L);

        mockMvc.perform(get("/account/deleteAccount/1"))
                .andExpect(status().isOk());

        verify(accountService).deleteAccount(1L);
    }
}
