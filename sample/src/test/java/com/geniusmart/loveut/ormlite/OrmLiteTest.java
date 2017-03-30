package com.geniusmart.loveut.ormlite;

import com.geniusmart.loveut.BuildConfig;
import com.j256.ormlite.dao.Dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.sql.SQLException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class , sdk = 21)
public class OrmLiteTest {

    DatabaseHelper helper;
    Dao<SimpleData, Integer> dao;

    @Before
    public void setUp() throws SQLException {
        helper = DatabaseHelper.getHelper();
        dao = helper.getDao();
    }

    @After
    public void tearDown(){
        DatabaseHelper.releaseHelper();
    }

    @Test
    public void save() throws SQLException {

        long millis = System.currentTimeMillis();
        dao.create(new SimpleData(millis));
        dao.create(new SimpleData(millis + 1));
        dao.create(new SimpleData(millis + 2));

        assertEquals(dao.countOf(), 3);

        List<SimpleData> simpleDatas = dao.queryForAll();
        assertEquals(simpleDatas.get(0).millis, millis);
        assertEquals(simpleDatas.get(1).string, ((millis + 1) % 1000) + "ms");
        assertEquals(simpleDatas.get(2).millis, millis + 2);
    }

    @Test
    public void queryForId() throws SQLException {
        long millis = System.currentTimeMillis();
        SimpleData simpleData = dao.createIfNotExists(new SimpleData(millis));
        assertNotNull(dao.queryForId(simpleData.id));
    }
}
