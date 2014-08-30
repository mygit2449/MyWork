package com.json.sample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MyJsonActivity extends Activity {
	
	TextView mFirst_Txt;
	String generated_url;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mFirst_Txt = (TextView)findViewById(R.id.first_txt);
        
        generated_url = readDataFeed();
        try {
			JSONArray myArray = new JSONArray(generated_url);
			
			Log.v(getClass().getSimpleName(), "lenght "+myArray.length());

			for (int i = 0; i < myArray.length(); i++) {
				JSONObject mJsonObject = myArray.getJSONObject(i);
				Log.v(getClass().getSimpleName(), "string text "+mJsonObject.getString("text"));
				mFirst_Txt.setText(mJsonObject.getString("text"));
			
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }
    
    
    public String readDataFeed(){
    	
    	StringBuilder generate_stbr = new StringBuilder();
    	HttpClient client = new DefaultHttpClient();
    	HttpGet httpGet = new HttpGet("https://twitter.com/statuses/user_timeline/j_satya.json");
    	
    	try 
    	{
    		
    		HttpResponse responce = client.execute(httpGet);
			StatusLine statusLine = responce.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			
			if (statusCode == 200) {
				HttpEntity entity = responce.getEntity();
				InputStream stream = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				String line = "";
				while ((line = reader.readLine()) != null) {
					generate_stbr.append(line);
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	
    	
    	
    	return generate_stbr.toString();
    }
}