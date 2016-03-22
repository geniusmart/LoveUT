package com.geniusmart.loveut.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.geniusmart.loveut.LoveUTApplication;

public class AccountDBHelper extends SQLiteOpenHelper {

    private static AccountDBHelper mAccountDBHelper;
    public static final int DB_VERSION = 1;
    private static final String DB_NAME = "loveut.db";

    public AccountDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static AccountDBHelper getInstance() {
        if (null == mAccountDBHelper) {
            mAccountDBHelper = new AccountDBHelper(LoveUTApplication.getInstance(), AccountDBHelper.DB_NAME, null, AccountDBHelper.DB_VERSION);
        }
        return mAccountDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        AccountTable.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
