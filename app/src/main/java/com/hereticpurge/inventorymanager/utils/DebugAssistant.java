package com.hereticpurge.inventorymanager.utils;

import android.util.Log;

final class DebugAssistant {

    // Little utility class to assist in debugging sections of code when permanent log statements
    // are not necessary and annoying to add and delete.

    private DebugAssistant() {
    }

    public static void nullityCheck(Object object) {
        Log.e("NULLITY CHECK", "OBJECT IS: " + (object == null ? "NULL" : "NOT NULL"));
    }

    private static void callCheck(String statement) {
        Log.e("CALL CHECK", "Method was called with message: " + statement);
    }

    public static void callCheck() {
        callCheck("no statement");
    }
}
