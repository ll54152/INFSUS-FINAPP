package com.infsus.finapp.rest;

import com.infsus.finapp.domain.Currency;
import com.infsus.finapp.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("")
    public List<String> listCurrencies() {
        List<Currency> currencies = currencyService.listAll();
        List<String> show = new ArrayList<>();

        for (Currency currency : currencies) {
            show.add(currency.getCurrencyCode());
        }
        return show;
    }
}

