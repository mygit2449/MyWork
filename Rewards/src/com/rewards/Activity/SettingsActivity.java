package com.rewards.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class SettingsActivity extends MenuOptionsActivity{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		 LayoutInflater factory = LayoutInflater.from(this);
	        
	     View myView = factory.inflate(R.layout.settings, null);
	     super.mParentLayout.addView(myView);
	     
	     super.mSettings_Ibn.setImageResource(R.drawable.settings_ac);
	     super.mMessages_Ibtn.setBackgroundResource(R.drawable.message_dac);

	     super.mSettings_Ibn.setClickable(false);
	}
	
}
