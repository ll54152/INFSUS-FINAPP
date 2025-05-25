package com.infsus.finapp;
import com.infsus.finapp.domain.Currency;
import com.infsus.finapp.service.CurrencyService;
import com.infsus.finapp.rest.CurrencyController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @WithMockUser(username = "testuser")
    @Test
    public void testListCurrencies() throws Exception {
        Currency eur = new Currency();
        eur.setCurrencyCode("EUR");
        eur.setConversionToEuro(1.0);

        Currency usd = new Currency();
        usd.setCurrencyCode("USD");
        usd.setConversionToEuro(1.1);

        List<Currency> currencies = Arrays.asList(eur, usd);

        Mockito.when(currencyService.listAll()).thenReturn(currencies);

        mockMvc.perform(get("/currency")
                        .with(httpBasic("testuser", "testpassword")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("EUR"))
                .andExpect(jsonPath("$[1]").value("USD"));
    }
}