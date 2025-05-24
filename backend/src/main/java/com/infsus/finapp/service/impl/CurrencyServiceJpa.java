package com.infsus.finapp.service.impl;

import com.infsus.finapp.dao.CurrencyRepository;
import com.infsus.finapp.domain.Currency;
import com.infsus.finapp.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyServiceJpa implements CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public List<Currency> listAll() {
        return currencyRepository.findAll();
    }

    @Override
    public Currency findByCurrencyCode(String currencyCode) {
        return currencyRepository.findByCurrencyCode(currencyCode);
    }

    @Override
    public int countByCurrencyCode(String currencyCode) {
        return currencyRepository.countByCurrencyCode(currencyCode);
    }
}
