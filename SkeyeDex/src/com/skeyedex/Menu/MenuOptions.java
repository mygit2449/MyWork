package com.skeyedex.Menu;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.skeyedex.R;
import com.skeyedex.Home.HomeScreenView;
import com.skeyedex.Settings.SettingsScreenView;

public abstract class MenuOptions extends Activity {
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater= getMenuInflater();
		inflater.inflate(R.menu.emaillist_menuoptions, menu);
		return true;
	}
	
	
	public void deleteEmails(){
		
	}
	
	public void refresh(){
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()){
		
		case R.id.eamillist_optionsmenu_item_home:
			startActivity(new Intent(MenuOptions.this,HomeScreenView.class));
			return true;
		
		case R.id.eamillist_optionsmenu_item_newmessage:
			
			Intent intent= new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_SUBJECT, " ");
			intent.putExtra(intent.EXTRA_TEXT, " ");
			intent.setType("plain/text");
			startActivity(Intent.createChooser(intent, "Send mail"));
			
			return true;
		
		case R.id.eamillist_optionsmenu_item_delete:
			//deleteEmails();
			DeleteAsync email_delete = new DeleteAsync(this);
			email_delete.execute();
			return true;
		
		case R.id.eamillist_optionsmenu_item_settings:
			startActivity(new Intent(MenuOptions.this,SettingsScreenView.class));
			return true;
	
		default:
			return super.onOptionsItemSelected(item);
		}	
	}

		public class DeleteAsync extends AsyncTask<String, Void, Void>
		{
	  
			
			Context mContext;
			ProgressDialog mDialog;
			String mErrorMsg = "";
			int type;
			
			public DeleteAsync(Context context) 
			{
				
				mContext = context;
				mDialog = ProgressDialog.show(context, "", "Deleting Emails....", true, false);
				mDialog.setCancelable(true);
			
			}
			
			@Override
			protected Void doInBackground(String... params) 
			{
				try
				{
					deleteEmails();
					
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					mErrorMsg = "Error in Deleting Emails ," + e.getMessage();
				}
				return null;
			}
			
			public void onPostExecute(Void result) 
			{
				
					//mDialog.dismiss();
				 try 
				 {
					 
					 mDialog.dismiss();
					 refresh();
					 mDialog = null;
					 
				    } catch (Exception e) {
				        // nothing
				    }
				 
			}
		}
		

}
