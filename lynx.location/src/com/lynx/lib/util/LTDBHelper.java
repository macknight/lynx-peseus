package com.lynx.lib.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LTDBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "location.db"; 
	private static final int DATABASE_VERSION = 1; 
	
	public LTDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//数据库第一次被创建时onCreate会被调用
		db.execSQL("CREATE TABLE IF NOT EXISTS location" +
				"(name VARCHAR PRIMARY KEY , val VARCHAR, add_time INTEGER)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
		db.execSQL("ALTER TABLE location COLUMN other STRING");
		
	}

}
