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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Ads.Activities.AdvertiseActivity;
import com.daleelo.Business.Activities.BusinessWriteReviewScreen;
import com.daleelo.DashBoardClassified.Activities.MyClassifiedListActivity;
import com.daleelo.User.Model.UserDetailsModel;
import com.daleelo.User.Parser.ClassifiedUserLoginParser;
import com.daleelo.Utilities.Urls;


public class LoginActivity extends Activity implements OnClickListener, OnFocusChangeListener{
	
	
	
	private EditText 	medt_email, medt_pass;
	private Button 		btn_login, btn_signUP;
	private CheckBox 	chk_remember,chk_policy;
	private TextView	txt_forget;
	private String 		requestFrom;
    public SharedPreferences sharedpreference;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_classified_login_screen);
		Intent recieverIntent=getIntent();
		requestFrom= recieverIntent.getStringExtra("requestfrom");
		sharedpreference= getSharedPreferences("userlogin",MODE_PRIVATE);
		initializeUI();		
		
	

       
	}           
			private void initializeUI()
			{	
				
				medt_pass=(EditText)findViewById(R.id.classified_login_ET_password);
				medt_email=(EditText)findViewById(R.id.classified_login_ET_email);
				chk_remember=(CheckBox)findViewById(R.id.classified_login_CB_remember);
				btn_login=(Button)findViewById(R.id.classified_login_BTN_login); 
				btn_signUP=(Button)findViewById(R.id.classified_login_BTN_signup);
				txt_forget = (TextView)findViewById(R.id.classified_login_TXT_forget);
				
				try{
					if(sharedpreference.getString("remember", "false").equalsIgnoreCase("true")){
						
						chk_remember.setChecked(true);
						medt_email.setText(sharedpreference.getString("useremail", ""));
						medt_pass.setText(sharedpreference.getString("password", ""));
						
					}
				}catch (Exception e) {
					// TODO: handle exception
					
				}
				
				
				btn_login.setOnClickListener(this);
				btn_signUP.setOnClickListener(this);
				txt_forget.setOnClickListener(this);
				
				chk_remember.setOnCheckedChangeListener(new  OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						
						if(isChecked){
							
							 Editor e = sharedpreference.edit();
			        	      e.putString("remember", "true");
			        	      e.commit();						
							
						}else{
							
							 Editor e = sharedpreference.edit();
			        	      e.putString("remember", "false");
			        	      e.commit();	
							
							
						}
						
					}
				});
				
				
			}

			
			
			ProgressDialog progressdialog;
			
			@Override
			public void onClick(View v) {
				
				Intent i;
				
				switch(v.getId()){
				
				
				
				case R.id.classified_login_BTN_signup:
					
					i = new Intent(LoginActivity.this, RegistrationActivity.class);
					i.putExtra("from", getIntent().getExtras().getString("from"));
					startActivity(i);
					
					break;
					
				case R.id.classified_login_TXT_forget:
					
					i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
					startActivity(i);
					
					break;
				
				
					
				case R.id.classified_login_BTN_login:
					
					boolean returnValue=allFieldsValidator(
							medt_email.getText().toString(),
							medt_pass.getText().toString());
					
					if(returnValue){
						
						String status = "";
						try {						

							progressdialog = ProgressDialog.show(this, "","Please wait...", true);
							progressdialog.show();
							
//							LoginVerify?UserName=string&Password=string
							
							ClassifiedUserLoginParser mUserAuth = new ClassifiedUserLoginParser(String.format(
									Urls.BASE_URL+"LoginVerify?UserName=%s&Password=%s",									
									URLEncoder.encode(medt_email.getText().toString(),"UTF-8"),
									URLEncoder.encode(medt_pass.getText().toString(),"UTF-8")), mainHandler);		
							
							mUserAuth.start();
						
													
										} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
					
					break;
					
		
				}
				
			}
		
			
			
			
			public Handler mainHandler = new Handler() 
		    {
				public void handleMessage(android.os.Message msg) 
				{
					
					progressdialog.dismiss();
					
					String handleErrMsg="", result;
					
					handleErrMsg = msg.getData().getString("httpError");
					result = msg.getData().getString("result");
					
					if(result.equalsIgnoreCase("True")){
						
						Toast.makeText(LoginActivity.this, handleErrMsg, Toast.LENGTH_SHORT).show();	
						saveData();						
						
						if(getIntent().getExtras().getString("from").equalsIgnoreCase("classified"))
							startActivity(new Intent(LoginActivity.this, MyClassifiedListActivity.class));
						else if(getIntent().getExtras().getString("from").equalsIgnoreCase("review"))
							startActivity(new Intent(LoginActivity.this, BusinessWriteReviewScreen.class));
						else if(getIntent().getExtras().getString("from").equalsIgnoreCase("forads"))
							startActivity(new Intent(LoginActivity.this, AdvertiseActivity.class));
						
						finish();
					
					}else{

						Toast.makeText(LoginActivity.this, handleErrMsg, Toast.LENGTH_SHORT).show();	
					}				
					
				}
			};
			
			  public void saveData() {
				  
        	      Editor e = sharedpreference.edit();
        	      e.putString("userid", UserDetailsModel.UserID);
        	      e.putString("useremail", UserDetailsModel.UserName);
        	      e.putString("nickname", UserDetailsModel.NickName);
        	      e.putString("phone", UserDetailsModel.Phone);
        	      e.putString("password", medt_pass.getText().toString());
        	      e.commit();
        	      Log.e("", "saveData");
        	      UserDetailsModel.UserID = "";
        	      UserDetailsModel.UserName = null;
        	      UserDetailsModel.NickName = null;
	        	}
					

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(!hasFocus){
						
						switch (v.getId()) {
						
						case R.id.classified_login_ET_email:
							
							if(editIsEmpty(medt_email.getText().toString()))
								medt_email.setError("Email is empty.");
							else if(!emailvalidator(medt_email.getText().toString()))
								medt_email.setError("Email Id is not valid.");
							else
								medt_email.setError(null);
							break;
						
						
						case R.id.classified_login_ET_password:
							
							if(editIsEmpty(medt_email.getText().toString()))
								medt_email.setError("Passwrod is empty.");
							else 
								medt_email.setError(null);
							break;
						
							
						
						default:
							break;
						}
					}
				}
			
			
			
			public boolean editIsEmpty(String text)
			{
				if(text.trim().equalsIgnoreCase(""))
				{
					return true;
				}
				return false;
			}
			
			
			
			public Boolean emailvalidator(String email)
			{
				Pattern emailPattern = Pattern
				.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
						+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
						+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
				Matcher matcher = emailPattern.matcher(email);
				return matcher.matches();
				
			}
			
			public boolean allFieldsValidator( String email, String password){
				
				if(	password.trim().equalsIgnoreCase("") || email.trim().equalsIgnoreCase("") ){
					
						Toast.makeText(this, "Enter vaild login details", Toast.LENGTH_SHORT).show();
					return false;
					
				}else {
					
					if(!emailvalidator(email.trim())){
						
						Toast.makeText(this, "Enter valid email id", Toast.LENGTH_SHORT).show();
						return false;
						
					}if(password.equalsIgnoreCase("")){
						
						Toast.makeText(this, "Enter vailed password", Toast.LENGTH_SHORT).show();
						return false;
						
					}else{
						return true;
					}
				}
				
			}
			
			
}
