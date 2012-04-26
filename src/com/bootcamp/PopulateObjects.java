package com.bootcamp;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.bootcamp.action.ActionModeHelper;
import com.bootcamp.data.XMLData;
import com.bootcamp.database.DbAdapter;
import com.bootcamp.parser.XMLPullParser;


//*************************************************************
//Title:  PopulateObjects
//Purpose: Class for async thread to populate database and table layout
//*************************************************************
public class PopulateObjects extends AsyncTask<String, Void, String> {
	
		//protected static final String rssURL = "http://earthquake.usgs.gov/earthquakes/shakemap/rss.xml"; 
		//protected static final String rssURL = "http://10.61.21.41:7000/rss.xml";
		protected static final String rssURL = "http://192.168.1.2:7000/rss.xml";

        private EarthquakeActivity earthquakeActivity = null;
        ProgressDialog dialog;
    
	    public PopulateObjects(Context context) {
	        earthquakeActivity = (EarthquakeActivity)context;
	    }
	
	    @Override
	   	protected void onPreExecute() {
	    	dialog = ProgressDialog.show(earthquakeActivity,"","Loading... Please wait.");
	    }
	   	
    	@Override
    	protected String doInBackground(String... placeHolder) {    	    
            PopulateDatabase();                        	    
            return "";
    	}
    	
    	    	
    	@Override
    	protected void onPostExecute(String result) {
    		dialog.dismiss();
     	    PopulateLayout();
    	}
    	
    	
    	
        //********************************
        //Title: PopualeDatabase
        //Purpose: To populate the database table with data from URL
        //********************************    
        private void PopulateDatabase() {
      	
           XMLData currentItem = null;
        	
           //Obtain data from RSS URL.  Parse into list of objects. 
     	   XMLPullParser parser = new XMLPullParser(rssURL);
     	   List<XMLData> dataList = parser.parse();
     	   
     	   if (dataList.size() > 0) {
     	   
     		   //open db
     		   DbAdapter db = new DbAdapter(earthquakeActivity);
     		   db.open();
     		   
    	 	   //loop thru list and insert new earthquakes
    	 	   Iterator<XMLData> itr = dataList.iterator();
    	 	   while (itr.hasNext()) {
    	 		   
    	 		   currentItem = itr.next();
    	 		   db.insert(currentItem.getTitle(), currentItem.getLatitude(), currentItem.getLongitude(), currentItem.getSeconds(), currentItem.getLink() );
    	 	   }
    	 	   
    	 	   db.close();
     	   }
     	  
        }
        
        
        
        //********************************
        //Title: PopualeLayout
        //Purpose: To populate the layout w/ contents from the database
        //********************************
    	private void PopulateLayout() {
    			
    		//open db
    		DbAdapter db = new DbAdapter(earthquakeActivity);
    		db.open();
    		
    		//fetch data
    		Cursor cursor = db.selectAllRows();
    		cursor.moveToFirst();
    		   		
    		earthquakeActivity.startManagingCursor(cursor);
    		
    		String[] from = new String[] { "title", "quake_date", "latitude", "longitude" };
    		int[] to = new int[] {R.id.Title, R.id.Date, R.id.Lat, R.id.Long };
    		
    		SimpleCursorAdapter notes = new SimpleCursorAdapter(earthquakeActivity, R.layout.mylist, cursor, from, to);
    		
    		//format date from cursor  
    		notes.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
    			
    			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
    		     
    				switch(view.getId()) {
    				case R.id.Date:
    				  
                       TextView text = (TextView) view;  					               
                       DateFormat dateF = DateFormat.getDateTimeInstance();	
                       text.setText( dateF.format( new Date ( cursor.getLong(4) * 1000 )));  // get date and apply formatter
                       return true;
    				
    				case R.id.Title:
    					
    					String title = cursor.getString(1);
    					TextView tv2 = (TextView) view;  				    					
    					tv2.setText(title);
    					
    					Character firstChar = title.charAt(0);
    					    					
    					if (Character.isDigit(firstChar)) { 
    						int firstDigit = Character.digit(firstChar,10);    						 
    						
    						if (firstDigit >= 7 ) {
    							tv2.setTextColor(Color.RED);
    						} else if (firstDigit >= 5 ) {
    							tv2.setTextColor(Color.rgb(250, 128, 114));
    						} else {
    							tv2.setTextColor(Color.rgb(255, 255, 255));
    						}	
    					} else {
    						tv2.setTextColor(Color.rgb(255, 255, 255));
    					}    						
    					
    					return true;
    				}
    				
    				return false;    				    				
    		    }			
    		});
    		
    		earthquakeActivity.setListAdapter(notes);
    		    		
    		db.close();
    		
    		//set long press/click listener
    		earthquakeActivity.getListView().setOnItemLongClickListener(
    				 new ActionModeHelper(earthquakeActivity, notes));
    		
    		//set normal (non-long) press/click listener
    		earthquakeActivity.getListView().setOnItemClickListener( 
    			     new WebHelper(earthquakeActivity));
    				    	
    	}	
    	
    }