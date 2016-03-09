package com.geniusmart.loveut.sqlite;

import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.io.File;
import java.net.URISyntaxException;

@Implements(AccountDBHelper.class)
public class ShadowAccountDBHelper {

    // 此路径相对于 ${project_root}/src/test/resources
    private static final String DB_PATH = "/database/loveut.db";
    private static String dbPath;
    private static AccountDBHelper mAccountDBHelper;

    @Implementation
    public static AccountDBHelper getInstance() throws URISyntaxException {

        if (null == mAccountDBHelper) {
            String path = AccountDBHelper.class.getResource(DB_PATH).toURI().getPath();
            File dbFile = new File(path);
            dbPath = dbFile.getAbsolutePath();
            mAccountDBHelper = new AccountDBHelper(RuntimeEnvironment.application, dbPath, null, AccountDBHelper.DB_VERSION);
        }
        return mAccountDBHelper;
    }

    public static void reset() {
        mAccountDBHelper = null;
    }
}
