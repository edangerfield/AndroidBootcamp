package com.bootcamp.action;


import java.util.Iterator;
import java.util.List;

import android.database.Cursor;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import com.bootcamp.EarthquakeActivity;
import com.bootcamp.PopulateObjects;
import com.bootcamp.data.XMLData;
import com.bootcamp.database.DbAdapter;
import com.bootcamp.parser.XMLPullParser;

public class ActionModeHelper 
  implements AdapterView.OnItemLongClickListener, ActionMode.Callback{

	
	private ActionMode myActionMode;
	private EarthquakeActivity   earthquakeActivity;
	int lastPosition = -1;
	private SimpleCursorAdapter scAdapter; 
	
	//Constructor
	public ActionModeHelper(EarthquakeActivity earthquakeActivity, SimpleCursorAdapter adapter) {
		this.earthquakeActivity = earthquakeActivity;
		this.scAdapter = adapter;
	}
	
	//***********************************************
	// Purpose: response to long press in list view
	// Required for OnItemLongClickListener
	//***********************************************
	public boolean onItemLongClick(AdapterView<?> view, 
			  				  	   View row,
			  					   int position, 
			  					   long id) {
		
		lastPosition = position;
		myActionMode=earthquakeActivity.startActionMode(this);
		
		return(true);
	}
	

	//***********************************************
	// Purpose: Called when action mode is first created.
	// Required for: ActionMode.Callback
	//***********************************************
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	    MenuInflater inflater=earthquakeActivity.getMenuInflater();
	    	  
	    inflater.inflate(com.bootcamp.R.menu.cab, menu);

	    return(true);
	}
	
		
	//***********************************************
	// Purpose: Called to report a user click on an action button.
	// Required for: ActionMode.Callback
	//***********************************************
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	    
    	DbAdapter db = new DbAdapter(earthquakeActivity);
    	
	    if (item.getItemId() == com.bootcamp.R.id.remove) {
	    	
	    	myActionMode.finish();
	    	
	    	//obtain link of selected item from cursor
	    	Cursor cursor = scAdapter.getCursor();
	    	String link = cursor.getString(cursor.getColumnIndex("link"));	    	
	    	
	    	//delete row w/ link from DB	    	
    		db.open();
    		if (db.delete(link) ) {
    			db.insertDeletedQuakes(link);
    		}
    		db.close();    		
	    	
    		//add link to list of those deleted
    		//earthquakeActivity.getLinksDeleted().add(link);    			    		    		    	
	    }
	
	    //query DB to get new cursor
		db.open();
    	Cursor cursor_requeried = db.selectAllRows();
    	cursor_requeried.moveToFirst();
    	db.close();
	    
    	//set cursor in adapter
    	scAdapter.changeCursor(cursor_requeried);
    	
    	return true;
	}	
	
    
	//***********************************************
	// Purpose: Called to refresh an action mode's action menu whenever it is invalidated.
	// Required for: ActionMode.Callback
	//***********************************************
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
       return false;
    }
    
  
	//***********************************************
	// Purpose: Called when an action mode is about to be exited and destroyed.
	// Required for: ActionMode.Callback
	//***********************************************
    public void onDestroyActionMode(ActionMode mode) {
    }
}
