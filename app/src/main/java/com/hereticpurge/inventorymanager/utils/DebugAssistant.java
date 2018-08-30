package com.hereticpurge.inventorymanager.utils;

import android.util.Log;

public class DebugAssistant {

    public static void nullityCheck(Object object){
        Log.e("NULLITY CHECK", "OBJECT IS: " + (object == null ? "NULL" : "NOT NULL"));
    }

    public static void callCheck(String statement){
        Log.e("CALL CHECK", "Method was called with message: " + statement);
    }

    public static void callCheck(){
        callCheck("no statement");
    }
}
