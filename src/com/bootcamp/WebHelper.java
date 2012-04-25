package com.bootcamp;

import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;

public class WebHelper implements OnItemClickListener {

	private EarthquakeActivity  earthquakeActivity;
	
	
	public WebHelper(EarthquakeActivity earthquakeActivity) {
		this.earthquakeActivity = earthquakeActivity;
	}
	
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		Adapter adapter = parent.getAdapter();
		SimpleCursorAdapter simpleCursorAdapter = (SimpleCursorAdapter)adapter; 
		Cursor cursor = (Cursor)simpleCursorAdapter.getItem(position);
		
		String link = cursor.getString(cursor.getColumnIndex("link"));		
				
		Intent intent = new Intent(earthquakeActivity, WebActivity.class);
		intent.putExtra("link", link);
		earthquakeActivity.startActivity(intent);
	}
}
