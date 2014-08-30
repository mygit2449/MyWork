package com.daleelo.DashBoardClassified.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.User.Activities.LoginActivity;


public class ClassifiedPostConfirmationActivity extends Activity{
		
	TextView mEmail;
	Button mDone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_post_classified_confirm_screen);
		initializeUI();       
	}           
		
	private void initializeUI(){
		
		mEmail = (TextView)findViewById(R.id.post_classified_confirm_TXT_email);
		mDone = (Button)findViewById(R.id.post_classified_confirm_BTN_done);
				
		mEmail.setText(getIntent().getExtras().getString("email"));
		
		mDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					 
					startActivity(new Intent(ClassifiedPostConfirmationActivity.this,ClassifiedListActivity.class)
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				
				
				
				finish();
				
			}
		});
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(ClassifiedPostConfirmationActivity.this,ClassifiedListActivity.class)
		.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
		finish();
	}

	
	
	
			
}
