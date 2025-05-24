package com.infsus.finapp.service;

import com.infsus.finapp.domain.Currency;

import java.util.List;

public interface CurrencyService {
    List<Currency> listAll();

    Currency findByCurrencyCode(String currencyCode);

    int countByCurrencyCode(String currencyCode);
}

