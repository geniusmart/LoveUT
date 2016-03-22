package com.geniusmart.loveut.util;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.geniusmart.loveut.sqlite.Account;
import com.geniusmart.loveut.sqlite.AccountTable;

import java.lang.reflect.Field;

public class AccountUtil {

    public static Account createAccount(String id){
        Account account = new Account();
        account.id = id;
        account.name = "geniusmart";
        account.email = "loveut@gmail.com";
        account.password = "123456";
        return account;
    }

    @NonNull
    public static ContentValues getContentValues(Account account) {
        ContentValues cv = new ContentValues();
        cv.put(AccountTable.ACCOUNT_ID, account.id);
        cv.put(AccountTable.ACCOUNT_EMAIL, account.email);
        cv.put(AccountTable.ACCOUNT_NAME, account.name);
        cv.put(AccountTable.ACCOUNT_PASSWORD, account.password);
        return cv;
    }

    @NonNull
    public static ContentValues getContentValues(String id) {
        Account account = createAccount(id);
        ContentValues cv = new ContentValues();
        cv.put(AccountTable.ACCOUNT_ID, account.id);
        cv.put(AccountTable.ACCOUNT_EMAIL, account.email);
        cv.put(AccountTable.ACCOUNT_NAME, account.name);
        cv.put(AccountTable.ACCOUNT_PASSWORD, account.password);
        return cv;
    }

    public static void resetSingleton(Class clazz, String fieldName) {
        Field instance;
        try {
            instance = clazz.getDeclaredField(fieldName);
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
