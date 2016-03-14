package com.geniusmart.loveut.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    public static long save(Account account) {
        ContentValues cv = getContentValues(account);
        return AccountDBHelper.getInstance().getWritableDatabase().insert(AccountTable.TABLE_NAME, null, cv);
    }

    public static int update(Account account) {
        ContentValues cv = getContentValues(account);
        return AccountDBHelper.getInstance().getWritableDatabase().update(AccountTable.TABLE_NAME, cv, AccountTable.ACCOUNT_ID + "=?", new String[]{account.id});
    }

    public static Account get(String id) {
        Cursor cursor = AccountDBHelper.getInstance().getWritableDatabase().query(AccountTable.TABLE_NAME, null, AccountTable.ACCOUNT_ID + "=?", new String[]{id}, null, null, null);
        Account account = null;
        if (null != cursor && cursor.moveToFirst()) {
            account = new Account();
            account.id = cursor.getString(cursor.getColumnIndex(AccountTable.ACCOUNT_ID));
            account.name = cursor.getString(cursor.getColumnIndex(AccountTable.ACCOUNT_NAME));
            account.email = cursor.getString(cursor.getColumnIndex(AccountTable.ACCOUNT_EMAIL));
            account.password = cursor.getString(cursor.getColumnIndex(AccountTable.ACCOUNT_PASSWORD));
        }
        if (null != cursor) {
            cursor.close();
            cursor = null;
        }
        return account;
    }

    public static List<Account> query() {
        List<Account> list = null;
        Cursor cursor = AccountDBHelper.getInstance().getWritableDatabase().query(AccountTable.TABLE_NAME, null, null, null, null, null, null);
        if (null != cursor) {
            list = new ArrayList<>();
            while (cursor.moveToNext()) {
                Account account = new Account();
                account.id = cursor.getString(cursor.getColumnIndex(AccountTable.ACCOUNT_ID));
                account.name = cursor.getString(cursor.getColumnIndex(AccountTable.ACCOUNT_NAME));
                account.email = cursor.getString(cursor.getColumnIndex(AccountTable.ACCOUNT_EMAIL));
                account.password = cursor.getString(cursor.getColumnIndex(AccountTable.ACCOUNT_PASSWORD));
                list.add(account);
            }
        }
        return list;
    }

    public static int deleteAll() {
        return AccountDBHelper.getInstance().getWritableDatabase().delete(AccountTable.TABLE_NAME, null, null);
    }


    public static boolean isExist(Account account) {
        Cursor cursor = null;
        boolean exist = false;
        try {
            cursor = AccountDBHelper.getInstance().getWritableDatabase().query(AccountTable.TABLE_NAME, new String[]{AccountTable.ACCOUNT_ID}, AccountTable.ACCOUNT_ID + "=?", new String[]{account.id}, null, null, null);
            if (cursor != null && cursor.getCount() == 1) {
                exist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return exist;
    }

    @NonNull
    private static ContentValues getContentValues(Account account) {
        ContentValues cv = new ContentValues();
        cv.put(AccountTable.ACCOUNT_ID, account.id);
        cv.put(AccountTable.ACCOUNT_EMAIL, account.email);
        cv.put(AccountTable.ACCOUNT_NAME, account.name);
        cv.put(AccountTable.ACCOUNT_PASSWORD, account.password);
        return cv;
    }
}
