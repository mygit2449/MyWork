package com.parsinganddatabase.exp;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

public class MyExpParserClass extends MyBaseFeed{

	Handler parentHandler;
	Context mContext;
	MyExampleModel mMyExampleModel = null;
	MyExpDAO mExpDAO;
	SQLiteDatabase mDatabase;
	
	public MyExpParserClass(String feedUrl, Handler handler, Context context) {
		super(feedUrl);
		// TODO Auto-generated constructor stub
		this.parentHandler = handler;
		this.mContext = context;
	}

	
	public ArrayList<MyExampleModel> parse(){
		
		ArrayList<MyExampleModel> mAllDetails = null;
		
		XmlPullParser parser = Xml.newPullParser();
		
		try 
		{
			
			parser.setInput(this.getInputStream(), null);
			int eventType = parser.getEventType();
			
			while(eventType != XmlPullParser.END_DOCUMENT){
				String name = null;

				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					mAllDetails = new ArrayList<MyExampleModel>();
					break;

				case XmlPullParser.START_TAG:
					name = parser.getName();
					
					if (name.equalsIgnoreCase(ITEM)) {
						mMyExampleModel = new MyExampleModel();
					}else if (mMyExampleModel != null) {
						
						if (name.equalsIgnoreCase(TITLE)) {
							mMyExampleModel.setTitle(parser.nextText());
						}else if (name.equalsIgnoreCase(DESCRIPTION)) {
							mMyExampleModel.setDescription(parser.nextText());
						}else if (name.equalsIgnoreCase(PUBDATE)) {
							mMyExampleModel.setPubdate(PUBDATE);
						}
					}
					
					
					break;
					
				case XmlPullParser.END_TAG:
					name = parser.getName();	
					
					if (name.equalsIgnoreCase(ITEM) && mMyExampleModel != null) {
						
						mAllDetails.add(mMyExampleModel);
						
						Message messageToparent = new Message();
						Bundle messageData = new Bundle();
						messageToparent.what = 0;
						messageData.putSerializable("data", mMyExampleModel);
						messageToparent.setData(messageData);
						parentHandler.sendMessage(messageToparent);
						
						Log.v(getClass().getSimpleName(), " contex "+mContext);
						mExpDAO =  new MyExpDAO(mContext);
						mExpDAO.saveDetailsIndatabase(mMyExampleModel.getTitle(), mMyExampleModel.getDescription(), mMyExampleModel.getPubdate());
						mMyExampleModel = null;
					}else if (name.equalsIgnoreCase(CHANEL)) {
						
						Message messageToparent = new Message();
						Bundle messageData = new Bundle();
						messageToparent.what = 1;
						messageData.putSerializable("nodata", "nodata");
						messageToparent.setData(messageData);
						parentHandler.sendMessage(messageToparent);
					}
					break;
				default:
					break;
				}
				
				eventType = parser.next();
				
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.v(getClass().getSimpleName()," socket error");
		}
		
		return mAllDetails;
	}
	
	
	
}
