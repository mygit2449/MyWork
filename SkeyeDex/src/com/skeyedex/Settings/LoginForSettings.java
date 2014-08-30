package com.skeyedex.Settings;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.accounts.AccountAuthenticatorActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skeyedex.R;

public class LoginForSettings extends AccountAuthenticatorActivity{

	Button mNext_button;
	EditText mUsername, mPassword;
	String username, password;

	@Override
	public void onCreate(Bundle accounts)
	{
		super.onCreate(accounts);
		setContentView(R.layout.login_screen);
		
		mUsername = (EditText)findViewById(R.id.Login_username);
		mPassword = (EditText)findViewById(R.id.Login_password);
		mNext_button = (Button)findViewById(R.id.next_BTN_login);
		mUsername.setText("skeyedex.project@yahoo.com");
		mPassword.setText("copperlabs123");
		
		//Next_Button Action....
		mNext_button.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				String user = mUsername.getText().toString().trim().toLowerCase();
				String password = mPassword.getText().toString().trim().toLowerCase();
				
				
				if (validateEmail(user) && password.length() > 0) 
				{
					 startActivity(new Intent(LoginForSettings.this,AccountsSettings.class).putExtra("username", user).putExtra("password", password));
				}else {
					Toast.makeText(getApplicationContext(), "Enter Valid Email id And Password", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private boolean validateEmail(String userEmailId)
	{
		
		Pattern patternEmail = null;
		Matcher matcher = null;
		String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
		patternEmail = Pattern.compile(EMAIL_PATTERN);
			 
		matcher = patternEmail.matcher(userEmailId);
		return matcher.matches();

	}
	
	
 } 
	