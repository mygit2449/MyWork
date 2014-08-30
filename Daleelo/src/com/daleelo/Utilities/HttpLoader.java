package com.daleelo.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import android.util.Log;
//satheesh
public class HttpLoader extends Thread {
	
	private String		 feedUrl;
	public StringBuffer ERROR_MESSAGE;
	public  StringBuffer text=new StringBuffer();
	public  boolean 	 isPost = false;
	public  String 	 	 postData = "";
	public  boolean 	 isUpload = false;
	public  byte[] 		 responseBytes = null;
	
	public HttpLoader(String feedUrl) 
	{
		this.feedUrl=feedUrl;		
	}


	@Override
	public void run()
	{		
		super.run();
	
		ERROR_MESSAGE = new StringBuffer();			
		int BUFFER_SIZE = 2000;
		
		InputStreamReader isr = null;
		
		HttpURLConnection httpConn = null;
		try {
				httpConn = openHttpConnection(feedUrl, ERROR_MESSAGE);
				if (ERROR_MESSAGE.length() == 0)
				{				
					isr = new InputStreamReader(httpConn.getInputStream());
					int charRead;
					char[] inputBuffer = new char[BUFFER_SIZE];
					while ((charRead = isr.read(inputBuffer)) > 0)
					{
						// ---convert the chars to a String---
						String readString = String.copyValueOf(inputBuffer,0, charRead);
						text.append(readString);
						inputBuffer = new char[BUFFER_SIZE];
					}
	
					
					isr.close();
					httpConn.disconnect();
					responseBytes = null;
			   }
			Log.v("Serivce Data",text.toString());
			parse(text.toString(),ERROR_MESSAGE.toString());
			
		  }
		  catch (IOException e) 
		  {
			ERROR_MESSAGE.append("Error connecting network or server not responding please try again..");
			e.printStackTrace();
		  }
		 catch (Exception e)
		 {
			ERROR_MESSAGE.append("Error connecting network or server not responding please try again..");
			e.printStackTrace();
		 }
		finally{
			try{
				isr.close();
				responseBytes = null;
			}catch(Exception ex){}
		}
		
		
	}

	protected void parse(String text2,StringBuffer errMsg) 
	{		
	}
	
	protected void parse(String text2,String errMsg) 
	{		
	}


	private HttpURLConnection openHttpConnection(String Url,	StringBuffer errMsg) 
	{
		int resCode=-1;
		
		HttpURLConnection httpConn = null;
		OutputStream ost = null;
		try
		{
			Log.v("Service Url " , Url);
			URL url= new URL(Url);
			URLConnection urlconnection = url.openConnection();
			if(!(urlconnection instanceof HttpURLConnection))
				ERROR_MESSAGE.append("URL is not an Http URL");
			
			else
			{
				httpConn = (HttpURLConnection)urlconnection;
				httpConn.setConnectTimeout(20000);
				
	            if(!isPost)
	            {
	            	httpConn.setAllowUserInteraction(false);
	            	httpConn.setRequestMethod("GET");
	            	httpConn.setInstanceFollowRedirects(true);
	            	httpConn.connect(); 
	            }else{
	            	Log.v("Service Post","post method");
	            	try{
		            	if(!isUpload)
		            	{	
		            		//httpConn.setRequestMethod("POST");
		            		//httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		            		//httpConn.setRequestProperty("Content-Language", "en-US"); 										
		            	}
		            	httpConn.setDoOutput(true);  
                    
                        if(!isUpload)
                    	responseBytes  = postData.getBytes("UTF-8");
                    
                        Log.v("HttpLoader",""+ responseBytes.length);
                    //if(!isUpload)
                    	//httpConn.setRequestProperty("Content-Length", "" + responseBytes.length);
	            	
	            	ost = httpConn.getOutputStream();
	            	if(!isUpload)
	            	{
		            	for (int iCtr = 0; iCtr < responseBytes.length; iCtr++) {
					        ost.write(responseBytes[iCtr]);
					      }
	            	}else{
	            		ost.write(responseBytes);
	            	}
					ost.flush();
					ost.close();   
	            	}catch(Exception ex){
	            		ERROR_MESSAGE.append("Error uploading data" + ex.getMessage());
	            	}finally{
	            		try{
	            		ost.flush();
						ost.close();
	            		}catch(Exception ex){}
	            	}
	            	
	            }
	            
	          
	            
	            resCode = httpConn.getResponseCode();    
	            Log.v("resCode",""+resCode);
	            if (resCode == HttpURLConnection.HTTP_OK)
	            {	            	
	                                                 
	            }
			
	            else
	            {
	               	ERROR_MESSAGE.append("Invalid or Bad URL request to the Server, HTTP error code " + resCode);	           
	            }
			}    
			
		}
		 catch (MalformedURLException e)
		 {
			 e.printStackTrace();
			ERROR_MESSAGE.append("Server is not responding or the network timedout. Please try again later...");
		 }
		 catch (IOException e)
		 {
			 e.printStackTrace();
			 ERROR_MESSAGE.append("Server is not responding or the network timedout. Please try again later...");
		 }
		 catch (Exception e)
		 {
			 e.printStackTrace();
			 ERROR_MESSAGE.append("Server is not responding or the network timedout. Please try again later...");
		 }
		Log.v("Service",ERROR_MESSAGE.toString());
		
		return httpConn;
	}
	
	
}
