package com.bootcamp;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bootcamp.data.XMLData;
import com.bootcamp.database.DbAdapter;
import com.bootcamp.parser.XMLPullParser;
import com.bootcamp.action.ActionModeHelper;


//*************************************************************
//Title:  PopulateObjects
//Purpose: Class for async thread to populate database and table layout
//*************************************************************
public class PopulateObjects extends AsyncTask<String, Void, String> {
	
	//private static final String rssURL = "http://earthquake.usgs.gov/earthquakes/shakemap/rss.xml"; 
	private static final String rssURL = "http://10.61.21.41:7000/rss.xml";
	//private static final String rssURL = "http://192.168.1.6:7000/rss.xml";
	//private Context context = null;
    private EarthquakeActivity earthquakeActivity = null;
    
	
	    public void init(Context context) {
	        //this.context = context;    
	        earthquakeActivity = (EarthquakeActivity)context;
	    }
	
	   
	    	
    	@Override
    	protected String doInBackground(String... placeHolder) {
    	    
    	    //fetch data from url, insert into db
            PopulateDatabase();
                        	    
            return "";
    	}
    	
    	
    	
    	@Override
    	protected void onPostExecute(String result) {
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
    		
    		//format query results 
    		notes.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
    			
    			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
    		     
    				switch(view.getId()) {
    				case R.id.Date:
    				  
                       TextView text = (TextView) view;  						// get your View               
                       //text.setText(cursor.getString(1));
                       DateFormat dateF = DateFormat.getDateTimeInstance();	// get Date formatter
                       text.setText( dateF.format( new Date ( cursor.getLong(4) * 1000 )));  // get date and apply formatter
                       return true;
    				}
    				
    				return false;    				
    				
    		    }			
    		});
    		
    		earthquakeActivity.setListAdapter(notes);
    		    		
    		db.close();
    		
    		//set long press listener on view
    		earthquakeActivity.getListView().setOnItemLongClickListener(
    				 new ActionModeHelper(earthquakeActivity, notes));
    	}	
    	
    }