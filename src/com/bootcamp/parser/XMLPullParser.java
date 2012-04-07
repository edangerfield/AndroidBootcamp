package com.bootcamp.parser;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.bootcamp.data.XMLData;

public class XMLPullParser extends BaseParser {

	//constructor
	public XMLPullParser (String url) {
		super(url);
	}
	
	//****************************
	//Title: parse
	//Purpose: parse XML from URL
	//****************************
	public List<XMLData> parse() {
		
		List<XMLData> dataItemList = null;
		
		XmlPullParser parser = Xml.newPullParser();
		
		try {
			
			parser.setInput(this.getInputStream(), null);
			int eventType = parser.getEventType();
			XMLData currentDataItem = null;
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
			    String tagName = null;
			    
				switch(eventType) {
				   case XmlPullParser.START_DOCUMENT:  
					   dataItemList = new ArrayList<XMLData>();
					   break;
				   case XmlPullParser.START_TAG:
					   tagName = parser.getName();
				       if (tagName.equalsIgnoreCase(ITEM)) {
				    	   currentDataItem = new XMLData();
				       } else if (currentDataItem != null ) { 	   
					       if (tagName.equalsIgnoreCase(TITLE)) {
					    	   currentDataItem.setTitle(parser.nextText());
					       } else if (tagName.equalsIgnoreCase(DESCRIPTION)) {
					    	   currentDataItem.setDescription(parser.nextText());
					       } else if (tagName.equalsIgnoreCase(LINK)) {
					    	   currentDataItem.setLink(parser.nextText());
					       } else if (tagName.equalsIgnoreCase(PUB_DATE)) {
					           currentDataItem.setDate(parser.nextText());
					       } else if (tagName.equalsIgnoreCase(LAT)) {
					    	   currentDataItem.setLatitude(Float.parseFloat(parser.nextText()));
					       } else if (tagName.equalsIgnoreCase(LONG)) {
					    	   currentDataItem.setLongitude(Float.parseFloat(parser.nextText()));
					       } else if (tagName.equalsIgnoreCase(DC)) {
					    	   currentDataItem.setDc(Integer.parseInt(parser.nextText()));
					       } else if (tagName.equalsIgnoreCase(SECONDS)) {
					    	   currentDataItem.setSeconds(Long.parseLong(parser.nextText()));
					       } else if (tagName.equalsIgnoreCase(DEPTH)) {
					    	   currentDataItem.setDepth(Float.parseFloat(parser.nextText()));
					       } else if (tagName.equalsIgnoreCase(REGION)) {
					    	   currentDataItem.setRegion(parser.nextText());
					       }
				       }
				       break;
				   case XmlPullParser.END_TAG:
					   tagName = parser.getName();
					   if (tagName.equalsIgnoreCase(ITEM)) {
						   dataItemList.add(currentDataItem);
					   }
					   break;
				}
				eventType = parser.next();
			}
			
		} catch (Exception e) {
		  throw new RuntimeException(e);	
		}
				
		return dataItemList;
	}
	
}
