package com.rewards.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class SearchCardsActivity extends MenuOptionsActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 super.onCreate(savedInstanceState);
		
		 LayoutInflater factory = LayoutInflater.from(this);
	        
	     View myView = factory.inflate(R.layout.cards_search, null);
	     super.mParentLayout.addView(myView);
	     
	     super.mMycards_Ibn.setImageResource(R.drawable.mycards_ac);
	     super.mMycards_Ibn.setClickable(false);
	     super.mMessages_Ibtn.setBackgroundResource(R.drawable.message_dac);

	}
	
}
