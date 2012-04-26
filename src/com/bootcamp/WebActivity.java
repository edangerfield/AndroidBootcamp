package com.bootcamp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String link = this.getIntent().getStringExtra("link");
		//Toast.makeText(this, "link:"+link, Toast.LENGTH_SHORT).show();
		
		setContentView(R.layout.myweblayout);
		WebView myWebView = (WebView) findViewById(R.id.webview);
		myWebView.loadUrl(link);
	}
	
	@Override
	public void onBackPressed() {
	   Intent intent = new Intent(this, EarthquakeActivity.class);	
	   this.startActivity(intent);	
	}
		
}
