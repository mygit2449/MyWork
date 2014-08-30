package com.skeyedex.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.skeyedex.R;
import com.skeyedex.Home.HomeScreenView;

public class SettingsScreenView extends Activity implements OnClickListener{

	RelativeLayout accounts_settings_layout = null; 
	RelativeLayout group_settings_layout    = null;
	RelativeLayout mProceed_Home_layout     = null ;
    
    Intent receiverIntent	= null;
	
    @Override
	public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_screen);
		
		receiverIntent = getIntent();
		Button mProceed_home = (Button)findViewById(R.id.proceed_home_BTN);
		accounts_settings_layout = (RelativeLayout) findViewById(R.id.account_REL_settings);
		group_settings_layout = (RelativeLayout) findViewById(R.id.group_REL_settings);
		mProceed_Home_layout = (RelativeLayout)findViewById(R.id.proceed_home_REL);
		
		mProceed_home.setOnClickListener(this);
        accounts_settings_layout.setOnClickListener(this);
        group_settings_layout.setOnClickListener(this);
			
	}
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	SharedPreferences sharedPreferences;
	    sharedPreferences = this.getSharedPreferences("skeyedex", MODE_WORLD_READABLE);
		
		boolean accountExists = sharedPreferences.getBoolean("AccountExists",false);
		if(accountExists)
			mProceed_Home_layout.setVisibility(View.VISIBLE);
    }
    
	public void onClick(View v) 
	{
		
		switch (v.getId()) 
		{
			case R.id.account_REL_settings:
				startActivity(new Intent(SettingsScreenView.this, AccountsScreen.class));
				break;
	
			case R.id.group_REL_settings:
				startActivity(new Intent(SettingsScreenView.this, AddFamilyAndFriends.class));
				break;
	        case R.id.proceed_home_BTN:
	        	   startActivity(new Intent(SettingsScreenView.this, HomeScreenView.class));
	        	   finish();
	        	   break;
			}
			
	 }

	
}
