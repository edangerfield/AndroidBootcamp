package com.bootcamp.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.bootcamp.EarthquakeActivity;

public class DbAdapter  {
								// Database fields
	public static final String CON_KEY_ROWID = "_id";
	public static final String CON_TITLE = "TITLE";
	public static final String CON_LATITUDE = "LATITUDE";
	public static final String CON_LONGITUDE = "LONGITUDE";
	public static final String CON_QUAKE_DATE = "QUAKE_DATE";
	public static final String CON_LINK = "LINK";
	
	
	private static final String DATABASE_TABLE = "earthquakes";
	private static final String DATABASE_TABLE_EQ_DELETED = "earthquakes_deleted";
	private EarthquakeActivity earthquakeActivity;
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;

	public DbAdapter(Context context) {
		this.earthquakeActivity = (EarthquakeActivity)context;
	}

	
	//open db connection
	public DbAdapter open() throws SQLException {

		dbHelper = new DatabaseHelper(earthquakeActivity);

		database = dbHelper.getWritableDatabase();
		return this;
	}

	
	//close db connection
	public void close() {
		dbHelper.close();
	}		

	

	//insert earthquake date
	public void insert(String title, float latitude, float longitude, long seconds, String link) {

		//build insert stmt
		String sqlstmt = "insert into " + DATABASE_TABLE + 
				            " (" + CON_TITLE + "," + CON_LATITUDE + "," + CON_LONGITUDE + "," + CON_QUAKE_DATE + "," + CON_LINK + ") " +
				            " select '" + title + "','" + latitude + "','" + longitude + "','" + seconds + "','" + link + "'" + 
				            " where not exists (" +
				                    " select 1 from " + DATABASE_TABLE +
				                    " where " + CON_LINK + "='" + link + "')" +
				            " and not exists (" +
				                    " select 1 from " + DATABASE_TABLE_EQ_DELETED +
				                    " where " + CON_LINK + "='" + link + "')";
					   
		//exec the sql statement
		database.execSQL(sqlstmt);
	}
	
	
	//Return a Cursor over the rows in the specified table
	public Cursor selectAllRows() {
		return database.query(DATABASE_TABLE, 
							  new String[] {CON_KEY_ROWID, CON_TITLE, CON_LATITUDE, CON_LONGITUDE, CON_QUAKE_DATE, CON_LINK},
							  null, null, null, null, CON_QUAKE_DATE + " DESC");
	}
	
	
	public boolean delete(String link) {
		return database.delete(DATABASE_TABLE, CON_LINK + "='" + link + "'", null) > 0;
	}
	
	
	//insert deleted earthquakes into separate table
	public void insertDeletedQuakes(String link) {

		//build insert stmt
		String sqlstmt = "insert into " + DATABASE_TABLE_EQ_DELETED + " (" + CON_LINK + ") values ('" + link + "')";
					   
		//exec the sql statement
		database.execSQL(sqlstmt);
	}
	
	// Put String values into a Content object
//	private ContentValues createContentValues(String category, String summary, String description) {
//		ContentValues values = new ContentValues();
//		values.put(KEY_CATEGORY, java.lang.System.currentTimeMillis());		
//		return values;
//	}
//	
//	
// Update the todo
//	public boolean updateTodo(long rowId, String category, String summary, String description) {
//		ContentValues updateValues = createContentValues(category, summary, description);
//
//		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
//	}
//								// Deletes todo row
//	public boolean deleteTodo(long rowId) {
//		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
//	}
//	
//								// Return a Cursor positioned at the defined todo
//	public Cursor selectTodo(long rowId) throws SQLException {
//		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
//				KEY_ROWID, KEY_CATEGORY, KEY_SUMMARY, KEY_DESCRIPTION },
//				KEY_ROWID + "=" + rowId, null, null, null, null, null);
//		if (mCursor != null) 
//			mCursor.moveToFirst();
//
//		return mCursor;
//	}
//
//	public Cursor selectTodoCategory(String selectCategory) throws SQLException {
//		Cursor mCursor = database.rawQuery(
//				"Select * from todo where category = ?", new String[] { selectCategory });
//		if (mCursor != null)
//			mCursor.moveToFirst();
//		return mCursor;
//	}
}
