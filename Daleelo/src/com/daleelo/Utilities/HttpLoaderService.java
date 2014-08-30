package com.daleelo.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;
//updated
public class HttpLoaderService extends Thread{
	
	public String url="";
	public String errMsg="";
	public StringBuffer httpResponseMessage=new StringBuffer();
	protected HttpLoaderService(String url){
		this.url = url;
	}
	@Override
	public void run() {
		super.run();
		StringBuffer errMsgConnection=new StringBuffer();
		try {
				httpResponseMessage.delete(0, httpResponseMessage.length());
				Log.v("Network","Requesting the url " + url);
				int BUFFER_SIZE = 2000;
		        InputStream in = null;

		        try {
		        	in = openHttpConnection(url,errMsgConnection);
		            if(errMsgConnection.length()==0)
		            {	
			            InputStreamReader isr = new InputStreamReader(in);
			            int charRead;
			             
			              char[] inputBuffer = new char[BUFFER_SIZE];
	
			                  while ((charRead = isr.read(inputBuffer))>0)
			                  {                    
			                      //---convert the chars to a String---
			                      String readString = String.copyValueOf(inputBuffer, 0, charRead);                    
			                      httpResponseMessage.append( readString);
			                      
			                      inputBuffer = new char[BUFFER_SIZE];
			                  }
			                
			                  in.close();
			                  isr.close();
		            } else{
		            	errMsg = errMsgConnection.toString();
		            }
				}catch (IOException e) {

//					errMsg = "Error connecting network or server not responding please try again..";
					errMsg  = "Server is not responding or the network timedout. Please try again later.";
	                e.printStackTrace();
	            }
				
		} 
		catch (Exception e) 
		{
//			errMsg ="Error connecting network, server not responding please try again..";
			errMsg= "Server is not responding or the network timedout. Please try again later.";

		}
		
		
		handleHttpResponse();
	}

	public void handleHttpResponse(){
		
		
	}

	private InputStream openHttpConnection(String urlStr,StringBuffer errMsg) {
		InputStream in = null;
		int resCode = -1;
		
		try {
			URL url = new URL(urlStr);
			URLConnection urlConn = url.openConnection();
			
			if (!(urlConn instanceof HttpURLConnection)) {
				errMsg.append("URL is not an Http URL");
				//throw new IOException ("URL is not an Http URL");
			}else{
				HttpURLConnection httpConn = (HttpURLConnection)urlConn;
				httpConn.setAllowUserInteraction(false);
				httpConn.setConnectTimeout(20000);
	            httpConn.setInstanceFollowRedirects(true);
	            httpConn.setRequestMethod("GET");
	            httpConn.connect(); 
	            resCode = httpConn.getResponseCode();                 
	            if (resCode == HttpURLConnection.HTTP_OK) {
	                in = httpConn.getInputStream();                                 
	            }else{
	            	
//	            	errMsg.append("Invalid request to the Server, please check the URL requested...");
	            	errMsg.append("Server is not responding or the network timedout. Please try again later.");
	           
	            }
			}    
		} catch (MalformedURLException e) {
			e.printStackTrace();

			errMsg.append("Server is not responding or the network timedout. Please try again later.");
		} catch (IOException e) {

			e.printStackTrace();
			errMsg.append("Server is not responding or the network timedout. Please try again later.");
		}catch (Exception e) {
			e.printStackTrace();
			errMsg.append("Server is not responding or the network timedout. Please try again later.");
		}
		return in;
	}


}
