package com.skeyedex.TabListActivities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import com.skeyedex.R;
import com.skeyedex.EmailsList.Emails_ListActivity;
import com.skeyedex.GroupList.Group_ListActivity;
import com.skeyedex.TextMessages.Text_listActivity;

public class SkeyedexTabListActivity extends TabActivity{

	@Override
	public void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.skeyedex_tab);
		Intent intent = getIntent();
		String intentindex = intent.getExtras().getString("tag");
		int item_position = intent.getExtras().getInt("item_position");
		
		TabHost tabhost = (TabHost)findViewById(android.R.id.tabhost);
		
		Intent intentnext;

		intentnext = new Intent(SkeyedexTabListActivity.this, Emails_ListActivity.class);
		tabhost.addTab(tabhost.newTabSpec("EmailTab")
				.setIndicator("Emails", SkeyedexTabListActivity.this.getResources().getDrawable(R.drawable.email_icon))
				.setContent(intentnext));
		
		intentnext = new Intent(SkeyedexTabListActivity.this, Group_ListActivity.class);
		intentnext.putExtra("item_position", item_position);
		Log.v("In Tab List"," "+item_position);
		tabhost.addTab(tabhost.newTabSpec("GroupTab")
				.setIndicator(" ", SkeyedexTabListActivity.this.getResources().getDrawable(R.drawable.group_icon))
				.setContent(intentnext));

		intentnext = new Intent(SkeyedexTabListActivity.this, Text_listActivity.class);
		tabhost.addTab(tabhost.newTabSpec("TextlistTab")
				.setIndicator("Text List", SkeyedexTabListActivity.this.getResources().getDrawable(R.drawable.text_icon))
				.setContent(intentnext));
		
		tabhost.setCurrentTabByTag(intentindex);
	}
}
