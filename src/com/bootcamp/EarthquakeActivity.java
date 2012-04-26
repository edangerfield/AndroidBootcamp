package com.bootcamp;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class EarthquakeActivity extends ListActivity {    
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                     
                   
        //Create and start new thread
        PopulateObjects p = new PopulateObjects(this);
        p.execute(new String[] { "" });
    }
 
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	
        //Create and start new thread
        PopulateObjects p = new PopulateObjects(this);
        p.execute(new String[] { "" });    	
    	
    	return true;
    }
}


//////////////////////////////////////////////////////
// To Do's
//
// Major:
//   - Navigation from WebView back to inital view 
//          look into how to get rid of blank screen that is btw first and second activity
//   
//
// Minor:    
//   - Update ActionModelHelper to acquire adapter (remove it from constructor)
//   - add field name to each TextView on initial view
//
//    action bar icons: http://developer.android.com/design/style/iconography.html
//      ref: http://developer.android.com/guide/topics/ui/actionbar.html
//
/////////////////////////////////////////////////////
