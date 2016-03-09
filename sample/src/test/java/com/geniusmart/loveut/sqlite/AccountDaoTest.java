package com.geniusmart.loveut.sqlite;

import com.geniusmart.loveut.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowSQLiteConnection;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(DatabaseTestRunner.class)
@Config(constants = BuildConfig.class, shadows = {ShadowAccountDBHelper.class})
public class AccountDaoTest {

    @Before
    public void tearDown(){
        ShadowSQLiteConnection.reset();
    }


    public Account createAccount(String id){
        Account account = new Account();
        account.id = id;
        account.name = "geniusmart";
        account.email = "loveut@gmail.com";
        account.password = "123456";
        return account;
    }

    @Test
    public void getDBHelp() {
        AccountDBHelper instance = AccountDBHelper.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void save() {
        Account account = createAccount("1");
        long result = AccountDao.save(account);
        assertTrue(result > 0);
    }

    @Test
    public void update(){
        Account account = createAccount("1");
        AccountDao.save(account);

        account.name = "geniusmart_update";
        int result = AccountDao.update(account);
        assertTrue(result > 0);

        Account newAccount = AccountDao.get("1");
        assertEquals(newAccount.name, "geniusmart_update");
    }

    @Test
    public void query(){
        AccountDao.save(createAccount("1"));
        AccountDao.save(createAccount("1"));
        AccountDao.save(createAccount("2"));
        AccountDao.save(createAccount("3"));

        List<Account> accountList = AccountDao.query();
        assertEquals(accountList.size(),3);
    }
}
