package com.skeyedex.Home;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.skeyedex.R;
import com.skeyedex.EmailDownLoader.EmailDownLoadService;
import com.skeyedex.Settings.SettingsScreenView;

public class HomeScreenOutLineView extends Activity{

	Button btn_mProceed;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homescreen_outline);
		btn_mProceed = (Button)findViewById(R.id.proceed_HomeScreen_BTN);
		
		btn_mProceed.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				
				startActivity(new Intent(HomeScreenOutLineView.this, SettingsScreenView.class));
				
			}
		});

	}
	
	@Override
    public void onBackPressed() 
	{
        
        //startActivity(setIntent); 
		SharedPreferences sharedPreferences = this.getSharedPreferences("skeyedex", MODE_WORLD_READABLE);
		boolean accountsExists = sharedPreferences.getBoolean("AccountExists",false);
		if(!accountsExists)
		{
			 stopService(new Intent(HomeScreenOutLineView.this, EmailDownLoadService.class));
		}
		finish();
		
        return;
    }   
}
