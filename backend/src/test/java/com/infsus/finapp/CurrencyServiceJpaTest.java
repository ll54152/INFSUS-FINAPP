package com.infsus.finapp;

import com.infsus.finapp.dao.CurrencyRepository;
import com.infsus.finapp.domain.Currency;
import com.infsus.finapp.service.impl.CurrencyServiceJpa;
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
class CurrencyServiceJpaTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyServiceJpa currencyService;

    private Currency currency;

    @BeforeEach
    void setUp() {
        currency = new Currency();
        currency.setCurrencyCode("USD");
    }

    @Test
    void listAll_shouldReturnCurrencies() {
        given(currencyRepository.findAll()).willReturn(List.of(currency));

        List<Currency> currencies = currencyService.listAll();

        assertEquals(1, currencies.size());
        verify(currencyRepository).findAll();
    }

    @Test
    void findByCurrencyCode_shouldReturnCurrency() {
        given(currencyRepository.findByCurrencyCode("USD")).willReturn(currency);

        Currency result = currencyService.findByCurrencyCode("USD");

        assertEquals(currency, result);
        verify(currencyRepository).findByCurrencyCode("USD");
    }

    @Test
    void countByCurrencyCode_shouldReturnCount() {
        given(currencyRepository.countByCurrencyCode("USD")).willReturn(5);

        int count = currencyService.countByCurrencyCode("USD");

        assertEquals(5, count);
        verify(currencyRepository).countByCurrencyCode("USD");
    }
}

