package com.skeyedex.Menu;

import com.skeyedex.R;

import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class EmailAccountDeletion extends Activity{

	String msg = "Are You Sure you would like to delete this persoon from this familyfriend contact list";
	Context mContext;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater= getMenuInflater();
		inflater.inflate(R.menu.delete_option, menu);
		return true;
	}
	
	
	public void deleteEmailAccount(){
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()){
		
		case R.id.emaillist_optionsmenu_item_delete:
			deleteEmailAccount();
			  
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
