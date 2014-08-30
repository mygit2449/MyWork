package com.rewards.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class ScanActivity extends MenuOptionsActivity{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 super.onCreate(savedInstanceState);
		
		 LayoutInflater factory = LayoutInflater.from(this);
	        
	     View myView = factory.inflate(R.layout.scan, null);
	     super.mParentLayout.addView(myView);
	     
	     super.mScan_Ibn.setImageResource(R.drawable.scan_ac);
	     super.mMessages_Ibtn.setBackgroundResource(R.drawable.message_dac);
		 super.mScan_Ibn.setClickable(false);

	}
	
}
