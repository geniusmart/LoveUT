package com.geniusmart.loveut.ormlite;

import com.geniusmart.loveut.BuildConfig;
import com.j256.ormlite.dao.Dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.sql.SQLException;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class OrmLiteTest {


    @Before
    public void setUp() {

    }

    @Test
    public void save() throws SQLException {

        DatabaseHelper helper = DatabaseHelper.getHelper();
        Dao<SimpleData, Integer> dao = helper.getDao();

        long millis = System.currentTimeMillis();
        dao.create(new SimpleData(millis));
        dao.create(new SimpleData(millis + 1));
        dao.create(new SimpleData(millis + 2));

        List<SimpleData> simpleDatas = dao.queryForAll();
        assertEquals(simpleDatas.size(), 3);
        assertEquals(simpleDatas.get(0).millis, millis);
        assertEquals(simpleDatas.get(1).string, ((millis + 1) % 1000) + "ms");
        assertEquals(simpleDatas.get(2).millis, millis + 2);
    }
}
