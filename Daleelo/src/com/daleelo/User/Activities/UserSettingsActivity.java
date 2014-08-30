package com.daleelo.User.Activities;

import java.net.URLEncoder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.More.Activities.PersonalProfileDetails;
import com.daleelo.User.Parser.ChangePasswordParser;
import com.daleelo.Utilities.Urls;




public class UserSettingsActivity extends Activity implements OnClickListener{
	
	EditText mEtNewPassword, mEtConPassword;
	TextView mTxtUpdateProfile, mTxtChangePassword, mTitleUserName;
	ImageView mImgUpdateProfile, mImgChangePassword;
	Button mDone;
	LinearLayout mLinUpdatePassword;
	
	ProgressDialog mProgressDialog;
	
	boolean mShowChangePassword = false;
    public SharedPreferences sharedpreference;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_settings_screen);
		initializeUI();       
	}           
		
	private void initializeUI(){
	

		sharedpreference= getSharedPreferences("userlogin",MODE_PRIVATE);
		
		mEtNewPassword = (EditText)findViewById(R.id.user_settings_ET_new_password);
		mEtConPassword = (EditText)findViewById(R.id.user_settings_ET_confirm_password);
		
		mTxtUpdateProfile = (TextView)findViewById(R.id.user_settings_TXT_profile);
		mTxtChangePassword = (TextView)findViewById(R.id.user_settings_TXT_change_password);
		mTitleUserName = (TextView)findViewById(R.id.user_settings_TXT_main_title);
		
		mImgUpdateProfile = (ImageView)findViewById(R.id.user_settings_IMG_profile);
		mImgChangePassword = (ImageView)findViewById(R.id.user_settings_IMG_change_password);
		
		mDone = (Button)findViewById(R.id.user_settings_BTN_done);
		
		mLinUpdatePassword = (LinearLayout)findViewById(R.id.user_settings_LIN_update_password);		
		
		mTitleUserName.setText("Welcome "+sharedpreference.getString("nickname", "User"));
		
		
		mTxtUpdateProfile.setOnClickListener(this);
		mImgUpdateProfile.setOnClickListener(this);
		mTxtChangePassword.setOnClickListener(this);
		mImgChangePassword.setOnClickListener(this);
		mDone.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		
		case R.id.user_settings_TXT_profile:
			
			startActivity(new Intent(UserSettingsActivity.this, PersonalProfileDetails.class));
			
			break;
			
		case R.id.user_settings_IMG_profile:
			
			startActivity(new Intent(UserSettingsActivity.this, PersonalProfileDetails.class));
			
			break;
			
		case R.id.user_settings_TXT_change_password:
						
			showUpdatePassword();		
			
			break;			
			
		case R.id.user_settings_IMG_change_password:			
			
			showUpdatePassword();
			
			break;
			
		case R.id.user_settings_BTN_done:
			
			if(validatePassword()){				
				
				changePassword();
			}
			
			break;
			
		
		}
		
	}
	
	
	private void showUpdatePassword(){
		
		if(!mShowChangePassword){	
			
			mShowChangePassword = true;
			mLinUpdatePassword.setVisibility(View.VISIBLE);
			
		}else{
			
			mShowChangePassword = false;
			mLinUpdatePassword.setVisibility(View.INVISIBLE);		
			
		}
	}
	
	
	private boolean validatePassword(){
		
		
		if(mEtNewPassword.getText().length()<1 && mEtConPassword.getText().length()<1){
			
			Toast.makeText(this, "Password and Confirm password fields should not be empty", Toast.LENGTH_SHORT).show();			
			return false;
			
		}else if(mEtNewPassword.getText().length()<1 ){
			
			Toast.makeText(this, "New password field should not be empty", Toast.LENGTH_SHORT).show();			
			return false;
			
		}else if(mEtConPassword.getText().length()<1){
			
			Toast.makeText(this, "Confirm password field should not be empty", Toast.LENGTH_SHORT).show();			
			return false;
			
		}else if(!mEtNewPassword.getText().toString().equalsIgnoreCase(mEtConPassword.getText().toString())){
			
			Toast.makeText(this, "Password and Confirm password is not match!", Toast.LENGTH_SHORT).show();			
			return false;
		}
		
		
		return true;
		
	}
	
	
	
	private void changePassword(){

		try{
			
		mProgressDialog = ProgressDialog.show(UserSettingsActivity.this, "", "Updating password...", true);
		
		String userId =  sharedpreference.getString("userid", "0");
		
		ChangePasswordParser changePasswordParser = new ChangePasswordParser(String.format(
				Urls.CHANGE_PASSWORD,									
				URLEncoder.encode(userId,"UTF-8"),
				URLEncoder.encode(mEtNewPassword.getText().toString(),"UTF-8")), mainHandler);		
		
		changePasswordParser.start();
		
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

	public Handler mainHandler = new Handler() 
    {
		public void handleMessage(android.os.Message msg) 
		{
			String handleErrMsg="";
			mProgressDialog.dismiss();
			
			saveData();
			
			mEtNewPassword.setText("");
			mEtConPassword.setText("");
			
			handleErrMsg=msg.getData().getString("httpError");
			
			if(handleErrMsg.equalsIgnoreCase("success")){
							

				mShowChangePassword = false;
				mLinUpdatePassword.setVisibility(View.INVISIBLE);
				
				Toast.makeText(UserSettingsActivity.this, " Password Updated Succesfully", Toast.LENGTH_SHORT).show();				
								
				
			}else{
				
				Toast.makeText(UserSettingsActivity.this, handleErrMsg, Toast.LENGTH_SHORT).show();
			
			}
			
			
		}
	};

	
	
	  public void saveData() {
		  
		  Editor e = sharedpreference.edit();
	      e.putString("password", mEtNewPassword.getText().toString());
	      e.commit();
	      Log.e("", "saveData");
	      
    	}
	
			
}
