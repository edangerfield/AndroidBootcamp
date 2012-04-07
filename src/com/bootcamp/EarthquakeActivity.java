package com.bootcamp;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bootcamp.data.XMLData;
import com.bootcamp.database.DbAdapter;
import com.bootcamp.parser.XMLPullParser;

public class EarthquakeActivity extends Activity {
    
	
	//private static final String rssURL = "http://earthquake.usgs.gov/earthquakes/shakemap/rss.xml"; 
	private static final String rssURL = "http://10.61.21.41:7000/rss.xml";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        
        //set custom title
        if (customTitleSupported) {
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        }
                
        //fetch data from url, insert into db
        PopulateDatabase();
                
        //populate table layout from db
        PopulateTableLayout();
                
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
 		   DbAdapter db = new DbAdapter(this);
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
    //Title: PopualeTableLayout
    //Purpose: To populate the table layout w/ contents from the database
    //********************************
	private void PopulateTableLayout() {
			
		//open db
		DbAdapter db = new DbAdapter(this);
		db.open();
		
		//fetch data
		Cursor cursor = db.selectAllRows();
		
		DateFormat dateF = DateFormat.getDateTimeInstance();
		TableLayout t1 = (TableLayout)findViewById(R.id.myTableLayout);
		
		//iterate over cursor, add item to row, add row to table layout		
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			
		   int value_id     	= cursor.getInt(0);	
		   String value_Title	= cursor.getString(1);
		   String value_lat		= cursor.getString(2);
		   String value_long	= cursor.getString(3);
		   long value_sec		= cursor.getLong(4);
			
		   //create row, apply layout
		   TableRow tr = new TableRow(this);		   		   
		   tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
		   
		   //create and set textViews
		   TextView tv = new TextView(this);			   
		   tv.setText( value_Title );
		   tv.setPadding(3, 3, 25, 3);
		   		   
		   TextView tv2 = new TextView(this);	   
		   tv2.setText( value_lat );
		   tv2.setPadding(3, 3, 25, 3);
		   
		   TextView tv3 = new TextView(this);	   
		   tv3.setText( value_long );
		   tv3.setPadding(3, 3, 25, 3);
		   
		   Date d = new Date (value_sec * 1000);		   
		   TextView tv4 = new TextView(this);	   
		   tv4.setText(dateF.format(d));
		   tv4.setPadding(3, 3, 15, 3);
		   
		   //add textView to row
		   tr.addView(tv);
		   tr.addView(tv2);
		   tr.addView(tv3);
		   tr.addView(tv4);
		   
		   //add row to table layout
		   t1.addView(tr, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
			
		   cursor.moveToNext();
		}
		
		db.close();
		
		
//	   XMLData currentItem = null;
//	   DateFormat dateF = DateFormat.getDateTimeInstance();
//	   
//	   //Obtain data 
//	   XMLPullParser parser = new XMLPullParser(rssURL);
//	   List<XMLData> dataList = parser.parse();
//	   	   
//	   TableLayout t1 = (TableLayout)findViewById(R.id.myTableLayout);
//	   	   	   
//	   //iterate over list, add item to row, add row to table layout
//	   Iterator<XMLData> itr = dataList.iterator();
//	   while (itr.hasNext()) {
//		   
//		   currentItem = itr.next();
//		   
//		   //create row, apply layout
//		   TableRow tr = new TableRow(this);		   		   
//		   tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//		   
//		   //create and set textViews
//		   TextView tv = new TextView(this);			   
//		   tv.setText( currentItem.getTitle() );
//		   tv.setPadding(3, 3, 25, 3);
//		   		   
//		   TextView tv2 = new TextView(this);	   
//		   tv2.setText( String.valueOf(currentItem.getLatitude()) );
//		   tv2.setPadding(3, 3, 25, 3);
//		   
//		   TextView tv3 = new TextView(this);	   
//		   tv3.setText( String.valueOf(currentItem.getLongitude()) );
//		   tv3.setPadding(3, 3, 25, 3);
//		   
//		   Date d = new Date (currentItem.getSeconds()*1000);		   
//		   TextView tv4 = new TextView(this);	   
//		   tv4.setText(dateF.format(d));
//		   tv4.setPadding(3, 3, 15, 3);
//		   
//		   //add textView to row
//		   tr.addView(tv);
//		   tr.addView(tv2);
//		   tr.addView(tv3);
//		   tr.addView(tv4);
//		   
//		   //add row to table layout
//		   t1.addView(tr, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));		   
//	   }	   	   
	}	
}