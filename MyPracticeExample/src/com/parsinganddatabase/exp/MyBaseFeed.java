package com.parsinganddatabase.exp;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MyBaseFeed {

	public String ITEM = "item";
	public String TITLE = "title";
	public String DESCRIPTION = "description";
	public String PUBDATE = "pubdate";
	public String CHANEL = "channel";
	
	URL feedUrl;
	
	public MyBaseFeed(String feedUrl){
		
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	protected InputStream getInputStream(){
		
		try {
			return feedUrl.openConnection().getInputStream();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
