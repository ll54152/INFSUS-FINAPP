package com.infsus.finapp.dao;

import com.infsus.finapp.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findByCurrencyCode(String currencyCode);

    int countByCurrencyCode(String currencyCode);
}

