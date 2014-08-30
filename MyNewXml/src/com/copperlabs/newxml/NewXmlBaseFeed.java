package com.copperlabs.newxml;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class NewXmlBaseFeed {

	protected String ITEM = "item";
	protected String TITLE = "title";
	protected String DESCRIPTION = "description";
	protected String PUBDATE = "pubdate";
	protected String THUMBNAIL = "thumbnail";
	
	private static final String TAG = NewXmlBaseFeed.class.getSimpleName();
	
	private URL feedUrl;
	
	protected NewXmlBaseFeed(String feedUrl){
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	protected InputStream getInputStream(){
		try {
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Inter Net Connection Error");
			throw new RuntimeException(e);
		}
		
	}
}
