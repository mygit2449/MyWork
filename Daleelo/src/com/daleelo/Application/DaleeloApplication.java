package com.daleelo.Application;

import android.app.Application;
import android.content.Context;

public class DaleeloApplication extends Application {
	public static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		DaleeloApplication.context = getApplicationContext();

	}

}
