package com.geniusmart.loveut.sqlite;

import com.geniusmart.loveut.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class AccountDaoTest {

    @Before
    public void tearDown(){
        /*
          执行每个test时，实例对象要重置为null，否则会出现如下异常：
          java.lang.RuntimeException: java.lang.IllegalStateException: Illegal connection pointer 37. Current pointers for thread Thread[pool-1-thread-1,5,main] []
          参考这个issues
          https://github.com/robolectric/robolectric/issues/1890
        */
        resetSingleton(AccountDBHelper.class, "mAccountDBHelper");
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
    public void save() {
        Account account = createAccount("1");
        long result = AccountDao.save(account);
        assertTrue(result > 0);
    }

    @Test
    public void update(){
        Account account = createAccount("2");
        AccountDao.save(account);

        account.name = "geniusmart_update";
        int result = AccountDao.update(account);
        assertEquals(result, 1);

        Account newAccount = AccountDao.get("2");
        assertEquals(newAccount.name, "geniusmart_update");
    }

    @Test
    public void query(){
        AccountDao.deleteAll();
        AccountDao.save(createAccount("3"));
        AccountDao.save(createAccount("4"));
        AccountDao.save(createAccount("5"));
        AccountDao.save(createAccount("5"));

        List<Account> accountList = AccountDao.query();
        assertEquals(accountList.size(), 3);
    }

    private void resetSingleton(Class clazz, String fieldName) {
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
