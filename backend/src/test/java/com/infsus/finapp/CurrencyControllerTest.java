package com.infsus.finapp;

import com.infsus.finapp.domain.Currency;
import com.infsus.finapp.rest.CurrencyController;
import com.infsus.finapp.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc(addFilters = false)

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Test
    void listCurrencies_shouldReturnCurrencyCodes() throws Exception {
        Currency eur = new Currency();
        eur.setCurrencyCode("EUR");
        eur.setConversionToEuro(1.0);

        Currency usd = new Currency();
        usd.setCurrencyCode("USD");
        usd.setConversionToEuro(1.1);

        when(currencyService.listAll())
                .thenReturn(List.of(eur, usd));

        String json = mockMvc.perform(get("/currency"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(json).contains("EUR");
        assertThat(json).contains("USD");
    }
}