package com.skeyedex.Menu;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.skeyedex.R;
import com.skeyedex.EmailDataBase.ContactsDataBase;
import com.skeyedex.GroupList.Group_ListActivity;
import com.skeyedex.dialog.AlertDialogMsg;

public class DeleteOption extends Activity{

	
	Context mContext;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater= getMenuInflater();
		inflater.inflate(R.menu.delete_option, menu);
		return true;
	}
	
	
	public void deleteAccount(){
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()){
		
		case R.id.emaillist_optionsmenu_item_delete:
			deleteAccount();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
