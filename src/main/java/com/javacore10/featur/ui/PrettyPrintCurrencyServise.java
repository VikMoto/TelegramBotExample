package com.javacore10.featur.ui;

import com.javacore10.featur.currency.dto.Currency;

public class  PrettyPrintCurrencyServise {
    public String convert(double rate, Currency currency) {
        String template = "Exchange rate {currency} => UAH = {rate} ";

        float roundedRate = Math.round(rate * 100d)/100f;

        return template
                .replace("{currency}", currency.name())
                .replace("{rate}", roundedRate + "");
    }
}
