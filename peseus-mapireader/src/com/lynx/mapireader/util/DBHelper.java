package com.lynx.mapireader.util;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author chris.liu
 * 
 */
public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "location.db";
	private static final int DB_VERSION = 1;
	private static final String TBL_LOCATION = "location";
	private static final String COL_NAME = "name";
	private static final String COL_LAT = "lat";
	private static final String COL_LNG = "lng";
	private static final String COL_ACC = "acc";
	private static final String COL_TIMESTAMP = "timestamp";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = String
				.format("CREATE TABLE %s (%s primary key, %s double, %s double, %s text)",
						TBL_LOCATION, COL_NAME, COL_LAT, COL_LNG, COL_ACC,
						COL_TIMESTAMP);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTES " + TBL_LOCATION;
		db.execSQL(sql);
		onCreate(db);
	}

	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor curosr = db.query(TBL_LOCATION, null, null, null, null, null,
				null);
		return curosr;
	}

	public long insert(String name, double lat, double lng, double acc) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cvs = new ContentValues();
		cvs.put(COL_NAME, name);
		cvs.put(COL_LAT, lat);
		cvs.put(COL_LNG, lng);
		cvs.put(COL_ACC, acc);
		cvs.put(COL_TIMESTAMP, new Date().getTime());
		return db.insert(TBL_LOCATION, null, cvs);
	}

	/**
	 * 删除历史记录中时间最旧的定位记录
	 */
	public void delete(String[] whereArgs) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = COL_NAME + "=?";
		db.delete(TBL_LOCATION, whereClause, whereArgs);
	}

	public void update(String name, double lat, double lng, double acc) {
		SQLiteDatabase db = getWritableDatabase();
		String where = COL_NAME + "=?";
		String[] whereArgs = {};

		ContentValues cvs = new ContentValues();
		cvs.put(COL_LAT, lat);
		cvs.put(COL_LNG, lng);
		cvs.put(COL_TIMESTAMP, new Date().getTime());
		db.update(TBL_LOCATION, cvs, where, whereArgs);
	}
}
