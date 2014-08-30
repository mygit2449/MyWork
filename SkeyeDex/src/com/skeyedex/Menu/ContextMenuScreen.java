package com.skeyedex.Menu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.skeyedex.R;
import com.skeyedex.Home.HomeScreenOutLineView;
import com.skeyedex.Settings.SettingsScreenView;

public class ContextMenuScreen extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	 }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{

		switch (item.getItemId()) 
		{
			case R.id.text:
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:")));
				return true;
	
			case R.id.newmsg:
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				startActivity(emailIntent); 
				return true;
	
			case R.id.info:
				startActivity(new Intent(ContextMenuScreen.this,
						HomeScreenOutLineView.class));
				return true;
	
			case R.id.settings:
				startActivity(new Intent(ContextMenuScreen.this,
						SettingsScreenView.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}
}
