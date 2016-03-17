package com.geniusmart.loveut.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.geniusmart.loveut.LoveUTApplication;
import com.geniusmart.loveut.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "helloAndroid.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<SimpleData, Integer> simpleDao = null;
	private RuntimeExceptionDao<SimpleData, Integer> simpleRuntimeDao = null;
	private static DatabaseHelper databaseHelper;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, SimpleData.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
	}

	public Dao<SimpleData, Integer> getDao() throws SQLException {
		if (simpleDao == null) {
			simpleDao = getDao(SimpleData.class);
		}
		return simpleDao;
	}

	public RuntimeExceptionDao<SimpleData, Integer> getSimpleDataDao() {
		if (simpleRuntimeDao == null) {
			simpleRuntimeDao = getRuntimeExceptionDao(SimpleData.class);
		}
		return simpleRuntimeDao;
	}

	@Override
	public void close() {
		super.close();
		simpleDao = null;
		simpleRuntimeDao = null;
	}

	public static DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = new DatabaseHelper(LoveUTApplication.getInstance());
		}
		return databaseHelper;
	}

	public static void releaseHelper(){
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}
}
