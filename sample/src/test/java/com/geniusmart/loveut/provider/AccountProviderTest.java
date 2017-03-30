package com.geniusmart.loveut.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.geniusmart.loveut.BuildConfig;
import com.geniusmart.loveut.sqlite.AccountDBHelper;
import com.geniusmart.loveut.sqlite.AccountTable;
import com.geniusmart.loveut.util.AccountUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowContentResolver;
import org.robolectric.shadows.ShadowLog;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class , sdk = 21)
public class AccountProviderTest {

    private ContentResolver mContentResolver;
    private ShadowContentResolver mShadowContentResolver;
    private AccountProvider mProvider;
    private String AUTHORITY = "com.geniusmart.loveut.AccountProvider";
    private Uri URI_PERSONAL_INFO = Uri.parse("content://" + AUTHORITY + "/" + AccountTable.TABLE_NAME);

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;

        mProvider = new AccountProvider();
        mContentResolver = RuntimeEnvironment.application.getContentResolver();
        //创建ContentResolver的Shadow对象
        mShadowContentResolver = Shadows.shadowOf(mContentResolver);

        mProvider.onCreate();
        //注册ContentProvider对象和对应的AUTHORITY
        ShadowContentResolver.registerProviderInternal(AUTHORITY, mProvider);
    }

    @After
    public void tearDown() {
        AccountUtil.resetSingleton(AccountDBHelper.class, "mAccountDBHelper");
    }


    @Test
    public void query() {
        ContentValues contentValues1 = AccountUtil.getContentValues("1");
        ContentValues contentValues2 = AccountUtil.getContentValues("2");

        mShadowContentResolver.insert(URI_PERSONAL_INFO, contentValues1);
        mShadowContentResolver.insert(URI_PERSONAL_INFO, contentValues2);

        //查询所有数据
        Cursor cursor1 = mShadowContentResolver.query(URI_PERSONAL_INFO, null, null, null, null);
        assertEquals(cursor1.getCount(), 2);

        //查询id为2的数据
        Uri uri = ContentUris.withAppendedId(URI_PERSONAL_INFO, 2);
        Cursor cursor2 = mShadowContentResolver.query(uri, null, null, null, null);
        assertEquals(cursor2.getCount(), 1);
    }

    @Test
    public void queryNoMatch() {
        Uri noMathchUri = Uri.parse("content://com.geniusmart.loveut.AccountProvider/tabel/");
        Cursor cursor = mShadowContentResolver.query(noMathchUri, null, null, null, null);
        assertNull(cursor);
    }

    @Test
    public void insert() {
        ContentValues contentValues1 = AccountUtil.getContentValues("1");
        mShadowContentResolver.insert(URI_PERSONAL_INFO, contentValues1);
        Cursor cursor = mShadowContentResolver.query(URI_PERSONAL_INFO, null, AccountTable.ACCOUNT_ID + "=?", new String[]{"1"}, null);
        assertEquals(cursor.getCount(), 1);
        cursor.close();
    }

    @Test
    public void update() {
        ContentValues contentValues = AccountUtil.getContentValues("2");
        Uri uri = mShadowContentResolver.insert(URI_PERSONAL_INFO, contentValues);

        contentValues.put(AccountTable.ACCOUNT_NAME, "geniusmart_update");
        int update = mShadowContentResolver.update(uri, contentValues, null, null);
        assertEquals(update, 1);

        Cursor cursor = mShadowContentResolver.query(URI_PERSONAL_INFO, null, AccountTable.ACCOUNT_ID + "=?", new String[]{"2"}, null);
        cursor.moveToFirst();
        String accountName = cursor.getString(cursor.getColumnIndex(AccountTable.ACCOUNT_NAME));
        assertEquals(accountName, "geniusmart_update");
        cursor.close();
    }

    @Test
    public void delete() {
        try {
            mShadowContentResolver.delete(URI_PERSONAL_INFO, null, null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Delete not supported");
        }
    }

    @Test
    public void getType() {
    }
}
