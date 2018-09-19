package com.hereticpurge.inventorymanager.utils;

import java.util.Currency;
import java.util.Locale;

public final class CurrencyUtils {

    // Class that takes a string and adds or removes the system local currency symbol from
    // the beginning.

    private CurrencyUtils() {
    }

    public static String addLocalCurrencySymbol(String string) {
        Currency currency = Currency.getInstance(Locale.getDefault());
        return currency.getSymbol() + string;
    }

    public static String removeLocalCurrencySymbol(String string) {
        Currency currency = Currency.getInstance(Locale.getDefault());
        String localCurrencySymbol = currency.getSymbol();

        String resultString = string;

        // Perform checks on the string.  Making sure the string exists.  Isn't "" (the user entered
        // no value.  The first character matches the currency symbol for the device's region.
        if (resultString != null
                && !resultString.equals("")
                && resultString.charAt(0) == localCurrencySymbol.charAt(0)) {
            resultString = resultString.substring(1);
        }
        return resultString;
    }
}
