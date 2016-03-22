package com.geniusmart.loveut.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.geniusmart.loveut.sqlite.AccountDBHelper;
import com.geniusmart.loveut.sqlite.AccountTable;

public class AccountProvider extends ContentProvider {

    private static final String TAG = "AccountProvider";

    public static final int MATCH_CODE_ITEM = 1;
    public static final int MATCH_CODE_ITEM_ID = 2;
    public static final String AUTHORITIE = "com.geniusmart.loveut.AccountProvider";

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITIE, AccountTable.TABLE_NAME, MATCH_CODE_ITEM);
        MATCHER.addURI(AUTHORITIE, AccountTable.TABLE_NAME + "/#", MATCH_CODE_ITEM_ID);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (MATCHER.match(uri)) {
            case MATCH_CODE_ITEM:
                return AccountDBHelper.getInstance().getWritableDatabase().query(AccountTable.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
            case MATCH_CODE_ITEM_ID:
                String id = uri.getPathSegments().get(1);
                return AccountDBHelper.getInstance().getWritableDatabase().query(AccountTable.TABLE_NAME,
                        projection, AccountTable.ACCOUNT_ID + "=?", new String[]{id}, null, null, sortOrder);
            default:
                Log.i(TAG, "didn't match anything");
                break;
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowid = AccountDBHelper.getInstance().getWritableDatabase().insert(AccountTable.TABLE_NAME,
                null, values);
        Uri insertUri = ContentUris.withAppendedId(uri, rowid);
        return insertUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Delete not supported");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return AccountDBHelper.getInstance().getWritableDatabase().update(AccountTable.TABLE_NAME,
                values, selection, selectionArgs);
    }
}
