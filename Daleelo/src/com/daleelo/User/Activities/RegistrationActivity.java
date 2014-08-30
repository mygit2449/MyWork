package com.daleelo.User.Activities;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.DashBoardClassified.Activities.ClassifiedListActivity;
import com.daleelo.User.Parser.ClassifiedUserRegistrationParser;
import com.daleelo.Utilities.Urls;

public class RegistrationActivity extends Activity
		implements OnClickListener, OnFocusChangeListener {


	private EditText mNickName, mEmail, mPhoneNo;
	private Button btnDone;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_classified_registration_screen);
		initializeUI();

	}

	

	private void initializeUI() {
		
		
		mEmail = (EditText) findViewById(R.id.classified_register_ET_email);
		mNickName = (EditText) findViewById(R.id.classified_register_ET_nickname);
		mPhoneNo = (EditText) findViewById(R.id.classified_register_ET_phone);
		btnDone = (Button)findViewById(R.id.classified_register_BTN_done);
		

		mEmail.setOnFocusChangeListener(this);
		mNickName.setOnFocusChangeListener(this);
		mPhoneNo.setOnFocusChangeListener(this);
		btnDone.setOnClickListener(this);
		
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.classified_register_BTN_done:

			boolean returnValue = allFieldsValidator(
					mNickName.getText().toString(), 
					mEmail.getText().toString(), 
					mPhoneNo.getText().toString());
			
			if (returnValue) {

				try{
				mProgressDialog = ProgressDialog.show(RegistrationActivity.this, "", "Please wait...", true);
				
//				ClassifiedUserRegistration?Nickname=string&Email=string&PhoneNumber=string
				
				ClassifiedUserRegistrationParser mUserAuth = new ClassifiedUserRegistrationParser(String.format(
						Urls.BASE_URL+"ClassifiedUserRegistration?Nickname=%s&Email=%s&PhoneNumber=%s",									
						URLEncoder.encode(mNickName.getText().toString(),"UTF-8"),
						URLEncoder.encode(mEmail.getText().toString(),"UTF-8"),
						URLEncoder.encode(mPhoneNo.getText().toString(),"UTF-8")), mainHandler);		
				
				mUserAuth.start();
				}catch (Exception e) {
					// TODO: handle exception
				}
			
			}
			
			break;

		}

	}

	public Handler mainHandler = new Handler() 
    {
		public void handleMessage(android.os.Message msg) 
		{
			String handleErrMsg="";
			mProgressDialog.dismiss();
			
			handleErrMsg=msg.getData().getString("httpError");
			
			if(handleErrMsg.equalsIgnoreCase("Sucess")){
							
				Toast.makeText(RegistrationActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
				
				startActivity(new Intent(RegistrationActivity.this, RegisterConfirmationActivity.class)
					.putExtra("email", mEmail.getText().toString())
					.putExtra("from", getIntent().getExtras().getString("from")));				
				
			}else{
				
				Toast.makeText(RegistrationActivity.this, handleErrMsg, Toast.LENGTH_SHORT).show();
			
			}
			
			
		}
	};
	ProgressDialog mProgressDialog;

	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {

			switch (v.getId()) {
			case R.id.classified_register_ET_nickname:
				
				if (editIsEmpty(mNickName.getText().toString()))
					mNickName.setError("NickName is empty");
				else
					mNickName.setError(null);
				
				break;
				
			case R.id.classified_register_ET_email:
				
				if(editIsEmpty(mEmail.getText().toString()))
					mEmail.setError("Email is empty.");
				else if(!emailvalidator(mEmail.getText().toString()))
					mEmail.setError("Email Id is not valid.");
				else
					mEmail.setError(null);
				break;
				
			case R.id.classified_register_ET_phone:
				
				if (mPhoneNo.getText().toString().length() > 10
						|| mPhoneNo.getText().toString().length() < 10)
					
					mPhoneNo.setError("Phone Number is not valid");
				else
					mPhoneNo.setError(null);
				
				break;
			
			
			default:
				break;
			}
		}
	}

	public boolean editIsEmpty(String text) {
		if (text.trim().equalsIgnoreCase("")) {
			return true;
		}
		return false;
	}

	public Boolean emailvalidator(String email) {
		Pattern emailPattern = Pattern
				.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
						+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
						+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
		Matcher matcher = emailPattern.matcher(email);
		return matcher.matches();

	}

	public boolean allFieldsValidator(
			String nickname, 
			String email,
			String phone ) {

		if (nickname.trim().equalsIgnoreCase("")
				|| email.trim().equalsIgnoreCase("")) {

			Toast.makeText(this, "Please fill your details.", Toast.LENGTH_SHORT).show();
			return false;
		} else {

			if (!emailvalidator(email)) {
				
				Toast.makeText(this, "Enter valid EmailID", Toast.LENGTH_SHORT).show();
				return false;
				
			}else if (nickname.length() < 2) {
				
				Toast.makeText(this, "Enter valid NickName", Toast.LENGTH_SHORT).show();
				return false;
				
			} else if (phone.trim().length() > 10) {
				
				Toast.makeText(this, "Enter valid phone no", Toast.LENGTH_SHORT).show();
				return false;
				
			} else {
				
				return true;
				
			}
		}
	}
	
	
	
	

}
