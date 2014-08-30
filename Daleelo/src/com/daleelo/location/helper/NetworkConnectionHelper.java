package com.daleelo.location.helper;

import android.content.Context;
import android.net.ConnectivityManager;

import com.daleelo.Application.DaleeloApplication;


public class NetworkConnectionHelper {

	static ConnectivityManager CONNECTIVITY_MANAGER;
	
	public static boolean checkNetworkAvailability()
	{
		CONNECTIVITY_MANAGER=(ConnectivityManager)DaleeloApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if(CONNECTIVITY_MANAGER.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()
				||CONNECTIVITY_MANAGER.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected())
			return true;
		return false;
	}
}
