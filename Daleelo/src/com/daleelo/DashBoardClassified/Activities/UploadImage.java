package com.daleelo.DashBoardClassified.Activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;


public class UploadImage {
	
	private final String TAG = UploadImage.class.getSimpleName();
	String xml;
	
	public String postClassified(String url, File image, String cId) throws ClientProtocolException, IOException 
	 {
	    String BOUNDRY = "----_Part_40_30154021.1312258728328";
	     StringBuilder dataBuffer = new StringBuilder();
	  try 
	  {
		   HttpClient httpClient = new DefaultHttpClient();
		   HttpPost httppost = new HttpPost(url);
		   
		   FileBody bin = new FileBody(image,"image/jpeg",MIME.ENC_BINARY);
		   MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,BOUNDRY,MIME.DEFAULT_CHARSET);
	
		   reqEntity.addPart("File", bin);
		   reqEntity.addPart("dummy", new StringBody("dummy"));
		   reqEntity.addPart("ClassifiedID", new StringBody(cId));
	
		   httppost.addHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDRY);
		   httppost.setEntity(reqEntity);
		   Log.d("httppost", httppost.toString());
		   HttpResponse response = httpClient.execute(httppost);
		   BufferedReader reader = new BufferedReader(new InputStreamReader(
		    response.getEntity().getContent(), "UTF-8"));
		   String sResponse;
	
		   while ((sResponse = reader.readLine()) != null) 
		   {
			   dataBuffer = dataBuffer.append(sResponse);
		   }
		     
	  } catch (Exception e) {
	   // handle exception here
	   Log.e(e.getClass().getName(), e.getMessage());
	  }
	  
	  Log.e(TAG, "Resopons"+dataBuffer.toString());
	  return parser(dataBuffer.toString());
	  
	  
	 }	
	
//	Response: <ReturnValue> Success</ReturnValue>	
	
	public String parser(String xmlData) {
		// TODO Auto-generated method stub

		XmlPullParser parser = Xml.newPullParser();
		String result = "fail";
		
		try {

			parser.setInput(new StringReader(xmlData));
			int eventType = parser.getEventType();
			boolean done = false;	

			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("ReturnValue")) {

						result = parser.nextText();		
						Log.e("", "result "+result);
						

					}
					
					Log.e("", "result "+result);
					done = true;					
					
					break;
					
				
				}
				
				eventType = parser.next();
				
			}
			
		} catch (Exception e) {
			
			result = "fail";
		}
		
		 return result;

	}
}
