package com.daleelo.User.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.DashBoardClassified.Activities.ClassifiedListActivity;


public class RegisterConfirmationActivity extends Activity{
		
	TextView mEmail;
	Button mDone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_classified_register_confirm_screen);
		initializeUI();       
	}           
		
	private void initializeUI(){
		
		mEmail = (TextView)findViewById(R.id.classified_confirm_TXT_email);
		mDone = (Button)findViewById(R.id.classified_confirm_BTN_done);
				
		mEmail.setText(getIntent().getExtras().getString("email"));
		
		 if(getIntent().getExtras().getString("from").equalsIgnoreCase("review") 
				 || getIntent().getExtras().getString("from").equalsIgnoreCase("forads")){
			 
			 mDone.setBackgroundResource(R.drawable.btn_icon_done_black);
			 
		 }
		
		mDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(getIntent().getExtras().getString("from").equalsIgnoreCase("classified")){
					 
					startActivity(new Intent(RegisterConfirmationActivity.this,ClassifiedListActivity.class)
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				
				}else if(getIntent().getExtras().getString("from").equalsIgnoreCase("review")
						|| getIntent().getExtras().getString("from").equalsIgnoreCase("forads")){
					 
					startActivity(new Intent(RegisterConfirmationActivity.this,LoginActivity.class)
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					 
				}
				
				finish();				
			}
		});		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(RegisterConfirmationActivity.this,ClassifiedListActivity.class)
		.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
		finish();
	}			
}
