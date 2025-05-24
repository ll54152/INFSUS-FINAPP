package com.infsus.finapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "Currency")
public class Currency {
    @NotNull
    private String currencyName;
    @Id
    private String currencyCode;
    private String currencySymbol;
    @NonNull
    private double conversionToEuro;

    public Currency() {
    }

    public Currency(String currencyName, String currencyCode, String currencySymbol, @NonNull double conversionToEuro) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.currencySymbol = currencySymbol;
        this.conversionToEuro = conversionToEuro;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public double getConversionToEuro() {
        return conversionToEuro;
    }

    public void setConversionToEuro(double conversionToEuro) {
        this.conversionToEuro = conversionToEuro;
    }
}
