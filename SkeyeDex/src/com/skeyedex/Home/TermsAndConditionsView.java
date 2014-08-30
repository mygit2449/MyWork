package com.skeyedex.Home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.skeyedex.R;

public class TermsAndConditionsView extends Activity implements OnClickListener 
{
	
	private Button btn_mAccept, btn_mDecline;

	private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor preferencesEdit;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terms_conditions);
		intializeUI();
	    sharedPreferences = this.getSharedPreferences("skeyedex", MODE_WORLD_READABLE);

	}

	/* Initializing UI Here */
	private void intializeUI() {
		btn_mAccept = (Button) findViewById(R.id.TCView_btn_accept);
		btn_mAccept.setOnClickListener(this);

		btn_mDecline = (Button) findViewById(R.id.TCView_btn_decline);
		btn_mDecline.setOnClickListener(this);

	}
	
	/* on button click */
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.TCView_btn_accept:
			
			preferencesEdit = sharedPreferences.edit();
			preferencesEdit.putBoolean("AppLaunchFirstTime",true);
			preferencesEdit.commit();
			startActivity(new Intent(TermsAndConditionsView.this,HomeScreenOutLineView.class));
			finish();
			
			break;

		case R.id.TCView_btn_decline:
			
			preferencesEdit = sharedPreferences.edit();
			preferencesEdit.putBoolean("AppLaunchFirstTime",false);
			preferencesEdit.commit();
			finish();
			
			break;
		default:
			break;
		}

	}

	
}
