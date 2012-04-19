package com.bootcamp;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;


public class EarthquakeActivity extends ListActivity {
    
	public ArrayList<String> getLinksDeleted() {
		return linksDeleted;
	}

	public void setLinksDeleted(ArrayList<String> linksDeleted) {
		this.linksDeleted = linksDeleted;
	}

	private ArrayList<String> linksDeleted = new ArrayList<String>();
		
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //setContentView(R.layout.main);
        
        //set custom title
        //if (customTitleSupported) {
        //    getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        //}
                
           
        //Create and start new thread
        PopulateObjects p = new PopulateObjects();
        p.init(this);
        p.execute(new String[] { "temp" });
    }
        
}


// Option 1
//    Since cursor is bound to the DB, upon selection of remove item, obtain the link from the cursor,
//      store it and delete it from the DB table.  Update initial fetch and refreshes, to pull in all records except those in this list.
//
//			- pass simpleCursorAdapter in constructor of ActionmodeHelper
//			- using simpleCursorAdapter in ActionmodeHelper.onActionItemClick, invoke a delete op on DB. Then
//            create new cursor and set in simpleCursorAdapter (sCA.ChangeCursor(newCursor))
//			- will need new DbAdapter to obtain cursor
//					- delete functionality working, need to work on the requery and refreshing the listview


//    action bar icons: http://developer.android.com/design/style/iconography.html

// Option 2
//    Do not use a cursor adapter.  Fetch from DB into array list, then populate ListView from arrayList.
//    Upon selection of remove item, delete item from array list and refresh list view