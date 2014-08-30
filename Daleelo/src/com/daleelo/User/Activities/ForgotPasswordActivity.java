package com.daleelo.User.Activities;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.DashBoardClassified.Activities.ClassifiedListActivity;
import com.daleelo.User.Parser.ForgotPasswordParser;
import com.daleelo.Utilities.Urls;




public class ForgotPasswordActivity extends Activity implements OnClickListener{
	
	EditText mEtEmailId;
	Button mDone;
	
	ProgressDialog mProgressDialog;
	
	boolean mShowforgotPassword = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_forgot_password_screen);
		initializeUI();       
	}           
		
	private void initializeUI(){
		
		mEtEmailId = (EditText)findViewById(R.id.user_forgot_password_ET_new_password);
	
		mDone = (Button)findViewById(R.id.user_forgot_password_BTN_done);

		mDone.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		
			
		case R.id.user_forgot_password_BTN_done:
			
			if(emailvalidator(mEtEmailId.getText().toString())){				
				
				forgotPassword();
			}else{
				
				Toast.makeText(ForgotPasswordActivity.this, "Enter vailed Email Address", Toast.LENGTH_SHORT).show();
			
			}
			
			break;
			
		
		}
		
	}
	

	
	private void forgotPassword(){

		try{
			
		mProgressDialog = ProgressDialog.show(ForgotPasswordActivity.this, "", "Please wait..", true);
		
		
		ForgotPasswordParser forgotPasswordParser = new ForgotPasswordParser(String.format(
				Urls.FORGOT_PASSWORD,									
				URLEncoder.encode(mEtEmailId.getText().toString(),"UTF-8")), mainHandler);		
		
		forgotPasswordParser.start();
		
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
			
			mEtEmailId.setText("");
			
			handleErrMsg=msg.getData().getString("httpError");
			
			if(handleErrMsg.equalsIgnoreCase("Sucess")){							

				alertDialog();								
				
			}else{
				
				Toast.makeText(ForgotPasswordActivity.this, handleErrMsg, Toast.LENGTH_SHORT).show();
			
			}
			
			
		}
	};

	public Boolean emailvalidator(String email)
	{
		Pattern emailPattern = Pattern
		.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
				+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
				+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
		Matcher matcher = emailPattern.matcher(email);
		return matcher.matches();
		
	}
	
	
	private void alertDialog(){
		
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Daleelo");
		builder.setMessage("Your Password has been mailed to Your Email ID")
		       .setCancelable(false)
		       .setPositiveButton("Login", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	
		           }
		       });
		     
		AlertDialog alert = builder.create();
		alert.show();
	}	
}
