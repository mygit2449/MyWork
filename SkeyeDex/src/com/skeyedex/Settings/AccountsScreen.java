package com.skeyedex.Settings;
import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skeyedex.R;
import com.skeyedex.EmailDataBase.EmailDataBase;
import com.skeyedex.EmailDataBase.ServerSettingsDataBase;
import com.skeyedex.EmailDownLoader.EmailDownLoadService;
import com.skeyedex.Home.HomeScreenOutLineView;
import com.skeyedex.Menu.EmailAccountDeletion;
import com.skeyedex.Model.ServerSettingsModel;
import com.skeyedex.dialog.AlertDialogMsg;

public class AccountsScreen extends EmailAccountDeletion implements OnItemClickListener{
	
	Button btn_mAddAccount;
	ArrayList<ServerSettingsModel> mEmail_Ids = null;
	DisplayMailsAdapter mDisplayMailsAdapter = null;
	ListView mListview;
	String selected_emailid;
	ServerSettingsDataBase mDatabase = null;
	EmailDataBase mEmailDataBase = null;
	boolean onclickFlag = false;
	Boolean[] selectedItem = null;
	
	 SharedPreferences sharedPreferences;
	 SharedPreferences.Editor preferencesEdit;
	
	@Override
	public void onCreate(Bundle settingsInstance)
	{
		super.onCreate(settingsInstance);
	    setContentView(R.layout.add_account);

	    sharedPreferences = this.getSharedPreferences("skeyedex", MODE_WORLD_READABLE);
	    
	    
	 	btn_mAddAccount = (Button)findViewById(R.id.add_account_BTN_add);
		mListview = (ListView)findViewById(R.id.email_list);
		mListview.setOnItemClickListener(this);
		mListview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		//add_ButtonClick
		btn_mAddAccount.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				
				startActivity(new Intent(AccountsScreen.this, LoginForSettings.class));
			       
			}
		});
	}
	
	
	public void refresh()
	{
		
		mDatabase = new ServerSettingsDataBase(getApplicationContext());
	    mEmailDataBase = new EmailDataBase(this);
		mEmail_Ids = new ArrayList<ServerSettingsModel>();
		
		mEmail_Ids = mDatabase.getServerDetailsFromDb();
		mDisplayMailsAdapter = new DisplayMailsAdapter(AccountsScreen.this, mEmail_Ids);
		mListview.setAdapter(mDisplayMailsAdapter);
		mDisplayMailsAdapter.notifyDataSetChanged();
	}
	@Override
	public void onResume ()
	{
		 super.onResume();
		 refresh();
         
	}
	
	//Adapter For Displaying Email_Id And Email_Type
	class DisplayMailsAdapter extends ArrayAdapter<ServerSettingsModel>
	{

		ArrayList<ServerSettingsModel> display_Array;
		Context context;
		public DisplayMailsAdapter(Context context, ArrayList<ServerSettingsModel> result) 
		{
			super(context, R.layout.emails_details, result);
			this.context = context;
			this.display_Array = result;
			
			selectedItem = new Boolean[display_Array.size()];
			for (int count = 0; count < selectedItem.length; count++) {
				selectedItem[count] = false;
			}
		}
	
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			if(convertView == null)
			{
				LayoutInflater inflater =  LayoutInflater.from(context);
				convertView  =  inflater.inflate(R.layout.emails_details, null);
			}
			
			TextView email_type = (TextView)convertView.findViewById(R.id.email_type);
			TextView email_id  = (TextView)convertView.findViewById(R.id.email_id);
			email_type.setText(display_Array.get(position).getEmail_type());
			email_id.setText(display_Array.get(position).getEmail_id());
			
			if (selectedItem[position]) 
				convertView.setBackgroundColor(Color.CYAN);
			else
				convertView.setBackgroundColor(Color.BLACK);
			
			return convertView;
			
		}
	}

	
	//deletion on selected email account
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		selected_emailid = mEmail_Ids.get(position).getEmail_id();
		int selected_item_position = mListview.getCheckedItemPosition();
		for (int iCtr = 0; iCtr < selectedItem.length; iCtr++) {
			selectedItem[iCtr] = false;
		}
		
		selectedItem[selected_item_position] = true;
		mDisplayMailsAdapter.notifyDataSetChanged();
		onclickFlag = true;
	}
	
	
	@Override
	public void deleteEmailAccount(){
		
		Log.v("on click", " "+onclickFlag);
		if (onclickFlag){ 
			 alertDialogWithMsg("Email Account", "Are you sure you want to delete the contact from Email accounts list?");
		     onclickFlag = false;
		}
		else
		Toast.makeText(AccountsScreen.this, "Please Select Any One Contact To Delete", Toast.LENGTH_SHORT).show();
		
	}
	
	public void alertDialogWithMsg(String title, String msg)
	{
			
		new AlertDialogMsg(AccountsScreen.this, title, msg).setPositiveButton("Delete", new android.content.DialogInterface.OnClickListener(){
	
			@Override
			public void onClick(DialogInterface dialog, int which)
			{

				EmailDownLoadService.stopDownload();
				
				String imap_server = mDatabase.getImapServerFromDatabase(selected_emailid);
				
				preferencesEdit = sharedPreferences.edit();
				preferencesEdit.remove(imap_server);
				preferencesEdit.commit();		
				
				mEmailDataBase.deleteMails(selected_emailid);
				mDatabase.deleteSelectedContact(selected_emailid);
				
//				stopService(new Intent(AccountsScreen.this, EmailDownLoadService.class));
				
				refresh();
				Log.v("Item to delete"," "+selected_emailid);
			}
			
			
		})
		.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener(){
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				//finish();
			}
			
			
		}).create().show();		
	
	}
	
	
	
	
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        Intent intent = new Intent(this,HomeScreenOutLineView.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(intent);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}*/
	
}
