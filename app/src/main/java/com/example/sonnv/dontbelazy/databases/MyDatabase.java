package com.example.sonnv.dontbelazy.databases;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Nttung PC on 9/5/2017.
 */

public class MyDatabase extends SQLiteAssetHelper{
    private static final String DATABASE_NAME = "databases.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
