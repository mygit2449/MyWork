package com.rewards.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class AddCardsActivity extends MenuOptionsActivity implements OnClickListener{

	private ImageButton mAddByCard_ImgBtn, mAddByBusiness_ImgBtn, mAddByScanning_ImgBtn;
	private LayoutInflater factory = null;
	private View myCardsView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 super.onCreate(savedInstanceState);
		
		 factory = LayoutInflater.from(this);
	        
		 myCardsView = factory.inflate(R.layout.add_cards, null);
	     super.mParentLayout.addView(myCardsView);
	     
	     super.mMycards_Ibn.setImageResource(R.drawable.mycards_ac);
	     super.mMycards_Ibn.setClickable(false);
	     super.mMessages_Ibtn.setBackgroundResource(R.drawable.message_dac);

	     initializingUI();
	}
	
	
	public void initializingUI(){
		
		mAddByCard_ImgBtn = (ImageButton)myCardsView.findViewById(R.id.addcards_ImgBtn_addByCard);
		mAddByBusiness_ImgBtn = (ImageButton)myCardsView.findViewById(R.id.addcards_ImgBtn_addByBusiness);
		mAddByScanning_ImgBtn = (ImageButton)myCardsView.findViewById(R.id.addcards_ImgBtn_addByScan);
		
		mAddByCard_ImgBtn.setOnClickListener(this);
		mAddByBusiness_ImgBtn.setOnClickListener(this);
		mAddByScanning_ImgBtn.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		super.onClick(v);
		
		switch (v.getId()) {
		case R.id.addcards_ImgBtn_addByCard:
			
			intent = new Intent();
			intent.setClassName(this, SearchCardsActivity.class.getName());
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent); 
			
			break;

		case R.id.addcards_ImgBtn_addByBusiness:
			
			intent = new Intent();
			intent.setClassName(this, SearchCardsActivity.class.getName());
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			
			break;
			
		case R.id.addcards_ImgBtn_addByScan:
			
			intent = new Intent();
			intent.setClassName(this, ScanActivity.class.getName());
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			
			break;
		default:
			break;
		}
	}
}
