package com.rewards.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class RedeemActivity extends MenuOptionsActivity{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 super.onCreate(savedInstanceState);
		
		 LayoutInflater factory = LayoutInflater.from(this);
	        
	     View myView = factory.inflate(R.layout.redeem, null);
	     super.mParentLayout.addView(myView);
	     
	     super.mRedeem_Ibn.setImageResource(R.drawable.redeem_ac);
	     super.mRedeem_Ibn.setClickable(false);
	     super.mMessages_Ibtn.setBackgroundResource(R.drawable.message_dac);

	}
}
