package com.bootcamp.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "db2";
	private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// creates database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL("create table earthquakes ("+
	                        " _id integer primary key autoincrement, " +
							" title text not null," +
							" latitude text not null," +
							" longitude text not null," +
							" quake_date text not null," +
							" link text not null);");
	}
	
	// increases the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS db2.earthquakes");
		onCreate(database);
	}
}
