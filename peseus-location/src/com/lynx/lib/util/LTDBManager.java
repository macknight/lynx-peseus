package com.lynx.lib.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * @author chris.liu
 *
 */
public class LTDBManager {
	private static final String Tag = "LTDBManager";
	
	private LTDBHelper db_helper;
	private SQLiteDatabase db;
	
	public LTDBManager(Context context) {
		db_helper = new LTDBHelper(context);
		db = db_helper.getWritableDatabase();
	}
	
	public List<LTDBDataPair> query() {
		List<LTDBDataPair> datas = new ArrayList<LTDBDataPair>();
		Cursor cursor = db.rawQuery("SELECT * FROM location", null);
		while (cursor.moveToNext()) {
			LTDBDataPair data = new LTDBDataPair(cursor.getString(cursor.getColumnIndex("name")),
					cursor.getString(cursor.getColumnIndex("val")),
					cursor.getLong(cursor.getColumnIndex("add_time")));
			datas.add(data);
		}
		return datas;
	}
	
	public LTDBDataPair query(String name) {
		LTDBDataPair data = null;
		Cursor cursor = db.rawQuery("SELECT * FROM location WHERE name=?", new String[]{name});
		while (cursor.moveToNext()) {
			data = new LTDBDataPair(cursor.getString(cursor.getColumnIndex("name")),
					cursor.getString(cursor.getColumnIndex("val")),
					cursor.getLong(cursor.getColumnIndex("add_time")));
		}
		return data;
	}
	
	public void update(List<BasicNameValuePair> datas) {
		db.beginTransaction();
		try {
			for (BasicNameValuePair data : datas) {
				db.execSQL("REPLACE INTO location(name, val, add_time) VALUES(?, ?, ?)", 
						new Object[]{data.getName(), data.getValue(), System.currentTimeMillis()});
			}
			db.setTransactionSuccessful();
		}
		finally {
			db.endTransaction();
		}
	}
	
	public void update(BasicNameValuePair data) {
		db.beginTransaction();
		try {
			db.execSQL("REPLACE INTO location(name, val, add_time) VALUES(?, ?, ?)", 
					new Object[]{data.getName(), data.getValue(), System.currentTimeMillis()});
			db.setTransactionSuccessful();
		}
		finally {
			db.endTransaction();
		}
	}
	
	public void delete(String name) {
		db.beginTransaction();
		db.delete("location", "name=?", new String[]{name});
		db.endTransaction();
	}
	
	public void closeDB() {
		db.close();
		Log.d(Tag, "closeDB()");
	}
}
