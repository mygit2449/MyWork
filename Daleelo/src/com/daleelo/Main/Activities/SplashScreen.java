package com.daleelo.Main.Activities;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.daleelo.R;
import com.daleelo.DataBase.DatabaseHelper;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.Main.Model.GetCitiesModel;
import com.daleelo.Main.Parser.GetCitiesParser;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;

public class SplashScreen extends Activity {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_RESULT = 2;
	private final int RESULT_FINISH = 3;

	public Intent intent;
	private FeedParserHandler mFeedParserHandler = new FeedParserHandler();	
	DatabaseHelper mDbHelper;
	public SharedPreferences sharedpreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		
		
		
		 if(checkNetwork()){ 
			 
			Utils.OS_LEVEL = android.os.Build.VERSION.SDK_INT;
			Utils.OS_VERSION = System.getProperty("os.version");
			
			sharedpreference= getSharedPreferences("userlogin",MODE_PRIVATE);
			Editor et = sharedpreference.edit();
		    et.putString("userid", "0");
		    et.commit();	
			
			try {
				
				mDbHelper= new DatabaseHelper(getApplicationContext());			
				mDbHelper.openDataBase();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new mAsyncSheFeedFetcher(mFeedParserHandler).start();
			
		 }else{	 
			 
			 networkNotfound("Internet connection not available.");
			 
		 }

	}

	
	class mAsyncSheFeedFetcher extends Thread {
		Handler handler;
		String url;

		public mAsyncSheFeedFetcher(Handler handler) {
			url = String.format(Urls.BASE_URL+"GetAPPGlobalCityCities");
			this.handler = handler;
			Log.e("", "url "+url);
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				new GetCitiesParser(url, handler).parser();

			} catch (Exception e) {
				e.printStackTrace();
			}
			super.run();
		}

	}
	
	GetCitiesModel mCityItem = null;

	class FeedParserHandler extends Handler {
		
		public void handleMessage(android.os.Message msg) {
			
			try {				
			
				
				if (msg.what == FOUND_RESULT) {			
					
					mCityItem  = (GetCitiesModel) msg.getData().getSerializable("datafeed");				
					mDbHelper.insertCity(mCityItem.getCityID(), mCityItem.getCityName(), mCityItem.getStateCode(), mCityItem.getLatitude(), mCityItem.getLongitude(), mCityItem.getCountry_code());
									
					
				} else if (msg.what == NO_RESULT) {
					
	
				}  else if (msg.what == ERROR_RESULT) {
					
					networkNotfound("Server not responding, please try again later");
	
				} else if (msg.what == RESULT_FINISH) {
					
					mDbHelper.closeDataBase();
					mDbHelper.close();
					callHome();
	
				}	
			
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}
	
	private void callHome(){
		
		startActivity(new Intent(SplashScreen.this,MainHomeScreen.class));
		finish();
		
	}

	
	public boolean checkNetwork() {
		
		boolean isWifiavailable = true;
		boolean isGprsAvailabe = true;
		
		NetworkInfo conMgrMobile = ((ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		NetworkInfo conMgrWifi = ((ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


		if (conMgrMobile.getState() == NetworkInfo.State.DISCONNECTED) {
			Log.i("conMgrMobile", "conMgrMobile no");
			isGprsAvailabe = false;

		}
		
		Log.i("conMgrWifi", "conMgrWifi yes");
		if (conMgrWifi.getState() == NetworkInfo.State.DISCONNECTED) {
			Log.i("conMgrWifi", "conMgrWifi no");
			isWifiavailable = false;
		}
		
		return isGprsAvailabe || isWifiavailable;
	}


	private void networkNotfound(String msg){	
		
		try {
			
//			String msg = "Internet connection not available.";
			 
			 new AlertDialogMsg(SplashScreen.this, msg).setPositiveButton("Exit", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						SplashScreen.this.finish();						
						
					}
					
				}).create().show();		
			 
		} catch (Exception e) {
			// TODO: handle exception
		}
		 
	}
	
}













//[2012-05-09 15:01:16 - ddmlib]Broken pipe
//java.io.IOException: Broken pipe
//	at sun.nio.ch.FileDispatcher.write0(Native Method)
//	at sun.nio.ch.SocketDispatcher.write(SocketDispatcher.java:29)
//	at sun.nio.ch.IOUtil.writeFromNativeBuffer(IOUtil.java:104)
//	at sun.nio.ch.IOUtil.write(IOUtil.java:75)
//	at sun.nio.ch.SocketChannelImpl.write(SocketChannelImpl.java:334)
//	at com.android.ddmlib.JdwpPacket.writeAndConsume(JdwpPacket.java:213)
//	at com.android.ddmlib.Client.sendAndConsume(Client.java:575)
//	at com.android.ddmlib.HandleHeap.sendREAQ(HandleHeap.java:348)
//	at com.android.ddmlib.Client.requestAllocationStatus(Client.java:421)
//	at com.android.ddmlib.DeviceMonitor.createClient(DeviceMonitor.java:854)
//	at com.android.ddmlib.DeviceMonitor.openClient(DeviceMonitor.java:822)
//	at com.android.ddmlib.DeviceMonitor.deviceClientMonitorLoop(DeviceMonitor.java:618)
//	at com.android.ddmlib.DeviceMonitor.access$100(DeviceMonitor.java:42)
//	at com.android.ddmlib.DeviceMonitor$3.run(DeviceMonitor.java:577)
