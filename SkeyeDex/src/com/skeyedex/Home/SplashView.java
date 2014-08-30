package com.skeyedex.Home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import com.skeyedex.R;
import com.skeyedex.EmailDownLoader.EmailDownLoadService;
public class SplashView extends Activity
{
  ImageView splash_image;
  int miImageCtr = 0;
  
  Handler splash_handler = new Handler();
  
  int[] marrImages = {R.drawable.s01,  R.drawable.s02, R.drawable.s03, R.drawable.s04, R.drawable.s05, R.drawable.s06, R.drawable.s07, R.drawable.s08,
		  R.drawable.s09, R.drawable.s10, R.drawable.s11, R.drawable.s12, R.drawable.s13, R.drawable.s14, R.drawable.s15, R.drawable.s16, R.drawable.s17,
		  R.drawable.s18,R.drawable.s19,R.drawable.s20,R.drawable.s21,R.drawable.s22,R.drawable.s23,R.drawable.s24,R.drawable.s25};
  
  SharedPreferences sharedPreferences;
  SharedPreferences.Editor preferencesEdit;
  boolean mAppLaunchFirstTime = false;
  public  boolean mTermsAndConditionsStatus = false;
  
  /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	   
	    sharedPreferences = this.getSharedPreferences("skeyedex", MODE_WORLD_READABLE);
    	setContentView(R.layout.splashview);
    	mAppLaunchFirstTime = sharedPreferences.getBoolean("AppLaunchFirstTime",false);	 
        splash_image = new ImageView(this);
        showSplash();
        startService(new Intent(SplashView.this, EmailDownLoadService.class));

        
   }
			
	/* Animation For The Images */
	private void showSplash() 
	{
		 Runnable runnable = new Runnable() 
		 {
         
			public void run() 
			{
				if (miImageCtr == marrImages.length) {
					Log.i("tag check ", "splash1 "+mAppLaunchFirstTime+" terms1 "+mTermsAndConditionsStatus);

					if (!mAppLaunchFirstTime) {
						
						startActivity(new Intent(SplashView.this, TermsAndConditionsView.class));
						Log.i("tag check ", "splash2 "+mAppLaunchFirstTime+" terms2 "+mTermsAndConditionsStatus);
						finish();
						
					}else {
						
						boolean accountsExists = sharedPreferences.getBoolean("AccountExists",false);
						
						if(accountsExists)
							startActivity(new Intent(SplashView.this, HomeScreenView.class));						
						else
							startActivity(new Intent(SplashView.this, HomeScreenOutLineView.class));

						finish();
					}

				}else {
					splash_image.setBackgroundResource(marrImages[miImageCtr]);
					setContentView(splash_image);
					miImageCtr++;
					showSplash();
				}
			}
		 
		 };
		splash_handler.postDelayed(runnable, 10);
	}
	
}