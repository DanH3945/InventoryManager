package com.hereticpurge.inventorymanager.utils;

import java.util.Currency;
import java.util.Locale;

public final class CurrencyUtils {

    private CurrencyUtils(){}

    public static String addLocalCurrencySymbol(String string){
        Currency currency = Currency.getInstance(Locale.getDefault());
        return currency.getSymbol() + string;
    }

    public static String removeLocalCurrencySymbol(String string){
        Currency currency = Currency.getInstance(Locale.getDefault());
        String localCurrencySymbol = currency.getSymbol();

        String resultString = string;

        if (resultString != null && resultString.charAt(0) == localCurrencySymbol.charAt(0)){
            resultString = resultString.substring(1);
        }
        return resultString;
    }
}
