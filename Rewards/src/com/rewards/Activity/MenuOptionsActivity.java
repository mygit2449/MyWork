package com.rewards.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MenuOptionsActivity extends Activity implements OnClickListener{

	public RelativeLayout mParentLayout;
	public ImageButton mMessages_Ibtn, mMycards_Ibn, mScan_Ibn, mRedeem_Ibn, mSettings_Ibn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.footer_layout);
		
		mParentLayout = (RelativeLayout)findViewById(R.id.footer_layout_addRelative);
		initializeUI();
	}

	
	public void initializeUI(){
		
		mMessages_Ibtn = (ImageButton)findViewById(R.id.footer_layout_BTN_messages);
		mMycards_Ibn = (ImageButton)findViewById(R.id.footer_layout_BTN_mycards);
		mScan_Ibn = (ImageButton)findViewById(R.id.footer_layout_BTN_scan);
		mRedeem_Ibn = (ImageButton)findViewById(R.id.footer_layout_BTN_redeem);
		mSettings_Ibn = (ImageButton)findViewById(R.id.footer_layout_BTN_settings);
		
		mMessages_Ibtn.setOnClickListener(this);
		mMycards_Ibn.setOnClickListener(this);
		mScan_Ibn.setOnClickListener(this);
		mRedeem_Ibn.setOnClickListener(this);
		mRedeem_Ibn.setOnClickListener(this);
		mSettings_Ibn.setOnClickListener(this);

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		
		case R.id.footer_layout_BTN_messages:
			
			intent = new Intent();
			intent.setClassName(this, RewardsMessagesActivity.class.getName());
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent); 

			break;

		case R.id.footer_layout_BTN_mycards:
			
			intent = new Intent();
			intent.setClassName(this, MycardsActivity.class.getName());
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent); 
			
			break;
			
		case R.id.footer_layout_BTN_scan:
	
			intent = new Intent();
			intent.setClassName(this, ScanActivity.class.getName());
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
	
			
			break;
	
		case R.id.footer_layout_BTN_redeem:
	
			intent = new Intent();
			intent.setClassName(this, RedeemActivity.class.getName());
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			
			
			break;
	
		case R.id.footer_layout_BTN_settings:
	
			intent = new Intent();
			intent.setClassName(this, SettingsActivity.class.getName());
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);;
		
			
			break;
		default:
			break;
		}
	}

}
