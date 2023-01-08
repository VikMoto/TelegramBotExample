package com.javacore10.featur.currency;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javacore10.featur.currency.dto.Currency;
import com.javacore10.featur.currency.dto.CurrencyItem;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrivatBankCurrencyService implements CurrencyService{
    Currency currency;
    Gson gson = new Gson();


    @Override
    public double getRate(Currency currency) {
        String url = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
        final String response;
        try {
            response = Jsoup
                    .connect(url)
                    .ignoreContentType(true)
                    .get()
                    .body()
                    .text();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can`t to connect Privat API");
        }

        List<CurrencyItem> currencyItems = new ArrayList<>();
/** getParameterized getType*/
//        final TypeToken<?> typeToken = TypeToken
//                .getParameterized(List.class, CurrencyItem.class);
//        List<CurrencyItem> result = (List<CurrencyItem>) gson.fromJson(response, typeToken);
//        for (CurrencyItem item : result) {
//            System.out.println("item = " + item);
//        }

/** one more  getType and convert Json => Java Object */
        Type collectionType = new TypeToken<List<CurrencyItem>>(){}.getType();
        List<CurrencyItem> enums = gson.fromJson(response, collectionType);

//                for (CurrencyItem currencyItem : enums) {
//                    System.out.println(currencyItem);
//                }
/** find USD/UAH */
        Float aFloat = enums
                .stream()
                .filter(currencyItem -> currencyItem.getCcy() == currency)
                .filter(it -> it.getBase_ccy() == currency.UAH)
                .map(it -> it.getBuy())
                .findFirst()
                .orElseThrow();

        System.out.println("USD/UAH " + aFloat);

        return aFloat;
    }
}
