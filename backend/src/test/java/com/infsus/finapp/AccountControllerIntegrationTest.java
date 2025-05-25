package com.infsus.finapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infsus.finapp.dao.CurrencyRepository;
import com.infsus.finapp.dao.PersonRepository;
import com.infsus.finapp.domain.Currency;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.AccountDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    public void setup() {
        personRepository.deleteAll();

        Person person = new Person();
        person.setEmail("testuser@example.com");
        person.setName("Test User");
        personRepository.save(person);
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    public void testCreateAccountAndListAccountNames() throws Exception {
        AccountDTO newAccount = new AccountDTO();
        newAccount.setAccountName("Test Account");
        newAccount.setBalance(1000.0);
        Currency currency = new Currency();
        currency.setCurrencyCode("EUR");
        currency.setCurrencyName("Euro");
        currencyRepository.save(currency);
        newAccount.setCurrencyId("EUR");

        mockMvc.perform(post("/account/createAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/account/listAccountNames"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasItem("Test Account")));
    }
}