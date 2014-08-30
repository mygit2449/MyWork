package com.daleelo.DashBoardClassified.Parser;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

import android.util.Log;


public class DaleeloPostClassifiedHelper {
	
	private final String TAG = DaleeloPostClassifiedHelper.class.getSimpleName();
	
	public String postClassified(String url,File image, ArrayList<String> data) throws ClientProtocolException, IOException 
	 {
	    String BOUNDRY = "----_Part_40_30154021.1312258728328";
	     StringBuilder s = new StringBuilder();
	  try 
	  {
		   HttpClient httpClient = new DefaultHttpClient();
		   HttpPost httppost = new HttpPost(url);
		   FileBody bin = new FileBody(image,"image/jpeg",MIME.ENC_BINARY);
		   MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,BOUNDRY,MIME.DEFAULT_CHARSET);
	
		   reqEntity.addPart("ImageFile", bin);
		   reqEntity.addPart("dummy", new StringBody("dummy"));		   
		   reqEntity.addPart("Classified_Title", new StringBody(data.get(0)));
		   reqEntity.addPart("Classified_Info", new StringBody(data.get(1)));
		   reqEntity.addPart("Classified_Owner", new StringBody(data.get(2)));
		   reqEntity.addPart("Classified_CreatedBy", new StringBody(data.get(3)));
		   reqEntity.addPart("Classified_Category", new StringBody(data.get(4)));
		   reqEntity.addPart("Price", new StringBody(data.get(5)));
		   reqEntity.addPart("Location", new StringBody(data.get(6)));
		   reqEntity.addPart("PhoneNum", new StringBody(data.get(7)));
		   reqEntity.addPart("Email", new StringBody(data.get(8)));
		   reqEntity.addPart("Latitude", new StringBody(data.get(9)));
		   reqEntity.addPart("Longitude", new StringBody(data.get(10)));
		   reqEntity.addPart("PostFrom", new StringBody(data.get(11)));
		   reqEntity.addPart("obo", new StringBody(data.get(12)));
		   reqEntity.addPart("cityname", new StringBody(data.get(13)));
		   reqEntity.addPart("Classified_Section", new StringBody(data.get(14)));
		   reqEntity.addPart("PaymentResponse", new StringBody(data.get(15)));
		   reqEntity.addPart("National/ local", new StringBody(data.get(16)));
	
		   httppost.addHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDRY);
		   httppost.setEntity(reqEntity);
		   Log.d("httppost", httppost.toString());
		   HttpResponse response = httpClient.execute(httppost);
		   BufferedReader reader = new BufferedReader(new InputStreamReader(
		    response.getEntity().getContent(), "UTF-8"));
		   String sResponse;
	
		   while ((sResponse = reader.readLine()) != null) 
		   {
		    s = s.append(sResponse);
		   }
		   System.out.println("Response: " + s);
	  } catch (Exception e) {
	   // handle exception here
	   Log.e(e.getClass().getName(), e.getMessage());
	  }
	  
	  Log.e(TAG, "Resopons"+s.toString());
	  return s.toString();
	  
	  
	 }
}
