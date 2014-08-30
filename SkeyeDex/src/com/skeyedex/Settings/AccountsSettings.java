package com.skeyedex.Settings;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Store;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.skeyedex.R;
import com.skeyedex.EmailDataBase.ServerSettingsDataBase;
import com.skeyedex.EmailDownLoader.EmailAuthenticatorActivity;
import com.skeyedex.dialog.AlertDialogMsg;

public class AccountsSettings extends Activity {

	EditText mTxtUsername, mTxtPassword, mTxtImap_Server, mTxtPort, mTxtImap_Path_Prifix;
	Button btn_mNext;
	Spinner mSpinnerSSL;
	
	String[] security_types = { "SSL", "TLS" };
	ServerSettingsDataBase mServerSettingsDataBase;
    
	boolean mCheck = true;
	String mErrorMsg = "";
	String mEmailId = "";
	String mSpinner_Selected_Type = "";
	String mEmailType = "";
	int mAlertType = 0;
	
	@Override
	public void onCreate(Bundle addAccounts)
	{
			super.onCreate(addAccounts);
			setContentView(R.layout.userdetails_screen);
			initializingUI();
			
			Intent receiveIntent = getIntent();
			mEmailId = receiveIntent.getExtras().getString("username");
			String[] arrEmail = mEmailId.split("@");
			String domain = arrEmail[1];
			String username = arrEmail[0];

			int indexPos = domain.toString().indexOf(".");
			String tempEmailType = domain;
			if(indexPos >0)
			{
				tempEmailType = domain.substring(0,indexPos);
			}
			
			if(tempEmailType.length()>1)
				mEmailType = tempEmailType.substring(0, 1).toUpperCase() +  tempEmailType.substring(1, tempEmailType.length());
			else
				mEmailType = tempEmailType.substring(0, 1).toUpperCase();
			
			
			
			String password = receiveIntent.getExtras().getString("password");
			mTxtUsername.setText("" + username);
			mTxtPassword.setText("" + password);
			
			mServerSettingsDataBase = new ServerSettingsDataBase(getApplicationContext());
			
			if (domain.equalsIgnoreCase("gmail.com"))
			{
				
				mTxtImap_Server.setText(""+"imap.gmail.com");
				mTxtPort.setText(""+"993");
				
			}else if (domain.equalsIgnoreCase("yahoo.com") || domain.equalsIgnoreCase("yahoo.co.in") || domain.equalsIgnoreCase("ymail.com")) 
			{
				mTxtImap_Server.setText(""+"imap.mail.yahoo.com");
				mTxtPort.setText(""+"993");
			}
			

		//Setting Spinner Adapter..    
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,	android.R.layout.simple_spinner_item, security_types);
		mSpinnerSSL.setAdapter(adapter);

		//Selecting Item On Spinner
		mSpinnerSSL.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			public void onItemSelected(AdapterView<?> parent, View arg1,int arg2, long arg3) 
			{
				mSpinner_Selected_Type = (String) mSpinnerSSL.getSelectedItem();
			}

			public void onNothingSelected(AdapterView<?> arg0) 
			{
				// TODO Auto-generated method stub

			}
		});

		//Next_Button Click...
		btn_mNext.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
			
					LoginTask t = new LoginTask(AccountsSettings.this);
					t.execute();
			        //startActivity(new Intent(getApplicationContext(), AccountsScreen.class));
			}
		});
	}
	
	//Initializing UI Here
	public void initializingUI()
	{
		mTxtUsername = (EditText) findViewById(R.id.username_edit_AccSettings);
		mTxtPassword = (EditText) findViewById(R.id.password_edit_AccSettings);
		mTxtImap_Server = (EditText) findViewById(R.id.imap_edit_AccSettings);
		mTxtPort = (EditText) findViewById(R.id.port_edit_AccSettings);
		mTxtImap_Path_Prifix = (EditText) findViewById(R.id.imap_path_edit_AccSettings);
		btn_mNext = (Button) findViewById(R.id.next_BTN_AccSettings);
		mSpinnerSSL = (Spinner) findViewById(R.id.security_spinner_AccSettings);
	}


	//Store All Server Settings Into DataBase..
	public boolean storeSettingsInDatabase() 
	{
		String username = mTxtUsername.getText().toString();
		String password = mTxtPassword.getText().toString();
		String imap_Server = mTxtImap_Server.getText().toString();
		String port = mTxtPort.getText().toString();
		String imap_Path_Prifix = mTxtImap_Path_Prifix.getText().toString();
		
		
		if (mServerSettingsDataBase.saveIncomingSettings(username, password, imap_Server, port, mSpinner_Selected_Type, imap_Path_Prifix, mEmailId, mEmailType) <=0)
		{
			return false;
		}else{
			return true;
		}
	}
	
	public void alertDialogWithMsg(String title, String msg)
	{
			
		new AlertDialogMsg(AccountsSettings.this, title, msg ).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){
	
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(mAlertType == 0)
				{
					Intent intent = new Intent(AccountsSettings.this,AccountsScreen.class);
			        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			        startActivity(intent);
				}
			}
			
		}).create().show();		
	
	}


	//Async Task For Connecting To Mail Server
	private class LoginTask extends AsyncTask<String, Void, Void> 
	{
		Context mContext;
		ProgressDialog mDialog;
	  
		LoginTask(Context context) {
			mContext = context;
			mDialog = ProgressDialog.show(context, "", getString(R.string.authenticating), true, false);
			mDialog.setCancelable(true);
			mErrorMsg = "";
		}

		 public Void doInBackground(final String... args) 
		 {
			Store store = null;
	 		try 
	 		{
				Properties props = new Properties();
				props.put("mail.imap.ssl.enable", "true");
				String username = mTxtUsername.getText().toString();
				String password = mTxtPassword.getText().toString();
				String imap_Server = mTxtImap_Server.getText().toString();
				int port = Integer.parseInt(mTxtPort.getText().toString());
				Log.v("AccountSettings", " Connecting to server");
				javax.mail.Authenticator authenticator = new EmailAuthenticatorActivity(username, password);
				// Get session
				Session session = Session.getInstance(props, authenticator);
				store = session.getStore("imaps");
				Log.v("AccountSettings", " Connecting to server " + imap_Server + " port " + port);
				store.connect( imap_Server, port, username, password);
				if(!storeSettingsInDatabase())
					mErrorMsg = "The Account for the Email provided already Exists";
				Log.v("AccountSettings","Connection established with IMAP Server Successfully.");
				store.close();
			  }catch (Exception e) {
				  e.printStackTrace();
				  mErrorMsg = "Issue connecting to the specified IMAP setting : " + e.getMessage();
			 } 
	 		 finally{
	 			 try{
	 				store.close(); 
	 			 }catch(Exception ex){}
	 		 }
			return null;
		 }

		public void onPostExecute(Void result) 
		{
			mDialog.dismiss();
			
			if(mErrorMsg.length()>0)
			{
				mAlertType = 1;
				alertDialogWithMsg("Skeyedex", mErrorMsg);
	
			}else{
				mAlertType = 0;
				//this is checked in the spalsh scrren if any account exists then the screen displayed is Home Screen
				SharedPreferences sharedPreferences;
			    sharedPreferences = AccountsSettings.this.getSharedPreferences("skeyedex", MODE_WORLD_READABLE);
				SharedPreferences.Editor preferencesEdit;
				preferencesEdit = sharedPreferences.edit();
				preferencesEdit.putBoolean("AccountExists",true);
				preferencesEdit.commit();
				alertDialogWithMsg("Skeyedex", "Connection established with IMAP Server Successfully.");
			}
		}
	}
	
	
}
