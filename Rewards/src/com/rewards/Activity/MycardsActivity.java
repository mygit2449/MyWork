package com.rewards.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MycardsActivity extends MenuOptionsActivity implements OnClickListener{

	

	private LayoutInflater factory = null;
	private View myCardsView = null;
	private ImageButton mAddByCard_ImgBtn, mAddByBusiness_ImgBtn, mAddByScan_Imgbtn;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		 factory = LayoutInflater.from(this);
	        
	     myCardsView = factory.inflate(R.layout.mycards, null);
	     super.mParentLayout.addView(myCardsView);
	       
	     super.mMycards_Ibn.setImageResource(R.drawable.mycards_ac);
	     super.mMycards_Ibn.setClickable(false);
	     super.mMessages_Ibtn.setBackgroundResource(R.drawable.message_dac);

	}

	public void onAddcardClick(View v){
		
		intent = new Intent();
		intent.setClassName(this, AddCardsActivity.class.getName());
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent); 
		

		
	}
}
