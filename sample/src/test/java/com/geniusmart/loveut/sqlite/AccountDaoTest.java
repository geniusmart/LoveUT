package com.geniusmart.loveut.sqlite;

import com.geniusmart.loveut.BuildConfig;
import com.geniusmart.loveut.util.AccountUtil;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class , sdk = 21)
public class AccountDaoTest {

    @After
    public void tearDown(){
        /*
          执行每个test时，实例对象要重置为null，否则会出现如下异常：
          java.lang.RuntimeException: java.lang.IllegalStateException: Illegal connection pointer 37. Current pointers for thread Thread[pool-1-thread-1,5,main] []
          参考这个issues
          https://github.com/robolectric/robolectric/issues/1890
        */
        AccountUtil.resetSingleton(AccountDBHelper.class, "mAccountDBHelper");
    }


    @Test
    public void save() {
        Account account = AccountUtil.createAccount("1");
        long result = AccountDao.save(account);
        assertTrue(result > 0);
    }

    @Test
    public void update(){
        Account account = AccountUtil.createAccount("2");
        AccountDao.save(account);

        account.name = "geniusmart_update";
        int result = AccountDao.update(account);
        assertEquals(result, 1);

        Account newAccount = AccountDao.get("2");
        assertEquals(newAccount.name, "geniusmart_update");
    }

    @Test
    public void query(){
        AccountDao.save(AccountUtil.createAccount("3"));
        AccountDao.save(AccountUtil.createAccount("4"));
        AccountDao.save(AccountUtil.createAccount("5"));
        AccountDao.save(AccountUtil.createAccount("5"));

        List<Account> accountList = AccountDao.query();
        assertEquals(accountList.size(), 3);
    }

}
