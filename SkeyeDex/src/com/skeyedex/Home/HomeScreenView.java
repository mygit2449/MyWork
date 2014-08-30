package com.skeyedex.Home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.skeyedex.R;
import com.skeyedex.GroupMessagesAndEmails.BussinessAndDocumentsReader;
import com.skeyedex.GroupMessagesAndEmails.EventsAndAddressReader;
import com.skeyedex.GroupMessagesAndEmails.GeneralAndFamilyReader;
import com.skeyedex.GroupMessagesAndEmails.MediaAndPhotosReader;
import com.skeyedex.Menu.ContextMenuScreen;
import com.skeyedex.SearchEmails.SearchEmailsListActivity;
import com.skeyedex.TabListActivities.SkeyedexTabListActivity;

public class HomeScreenView extends ContextMenuScreen implements	OnClickListener, OnLongClickListener {
	

	GeneralAndFamilyReader generalmessages = new GeneralAndFamilyReader(this);
	
	EventsAndAddressReader eventsmessages = new EventsAndAddressReader(this);
	
	MediaAndPhotosReader mediamessages = new MediaAndPhotosReader(this);
	
	BussinessAndDocumentsReader businessmessages = new BussinessAndDocumentsReader(this);
	
	
	StringBuffer stringBuff = new StringBuffer();
	
	int eventsListSize = 0, greenListSize = 0, whiteListSize = 0, blueListSize = 0;
	
	TextView yellowList, green, white, blue;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.homescreen);
		
		 yellowList = (TextView)findViewById(R.id.homescreen_Yellow_total_mails);
		 green = (TextView)findViewById(R.id.homescreen_Green_total_mails);
		 white = (TextView)findViewById(R.id.homescreen_White_total_mails);
		 blue = (TextView)findViewById(R.id.homescreen_Blue_total_mails);
		
		initializeUI();
		
		
	 }

	
	@Override
	public void onResume(){
		
		super.onResume();
		HomeScreenAsync mHomeScreenAsync =new HomeScreenAsync(this);
		mHomeScreenAsync.execute();
		
	}
	/* Menu Implementation */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	
	public class HomeScreenAsync extends AsyncTask<String, Void, Void>
	{
  
		
		Context mContext;
		ProgressDialog mDialog;
		String mErrorMsg = "";
		int type;
		
		public HomeScreenAsync(Context context) 
		{
			
			mContext = context;
			mDialog = ProgressDialog.show(context, "", "Loading HomeScreen....", true, false);
			mDialog.setCancelable(true);
		
		}
		
		@Override
		protected Void doInBackground(String... params) 
		{
			try 
			{
				
				eventsListSize = eventsmessages.get_Events_messages(stringBuff).size();
				
				greenListSize = mediamessages.get_Media_messages(stringBuff).size();
				
				whiteListSize = generalmessages.get_General_messages(stringBuff).size();
				
				blueListSize = businessmessages.get_Business_messages(stringBuff).size();
				
				Log.v("Home Screen",""+eventsListSize);
				
		     } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return null;
		}
		
		public void onPostExecute(Void result) 
		{
			
			try 
			 {
				 mDialog.dismiss();
				 mDialog = null;
			
			 } catch (Exception e) {
			        // nothing
			    }

			yellowList.setText(""+eventsListSize);
			green.setText(""+greenListSize);
			white.setText(""+whiteListSize);
			blue.setText(""+blueListSize);
			
		}
	}
 
	/* Initializing UI Here */
	public void initializeUI() 
	{

		
		
		ImageView text_icon = (ImageView) findViewById(R.id.homescreen_img_txt);
		text_icon.setOnClickListener(this);

		ImageView email_icon = (ImageView) findViewById(R.id.homescreen_img_email);
		email_icon.setOnClickListener(this);

		ImageView yellow_img = (ImageView) findViewById(R.id.homescreen_yellowbar);
		yellow_img.setOnClickListener(this);

		ImageView green_icon = (ImageView) findViewById(R.id.homescreen_greenbar);
		green_icon.setOnClickListener(this);

		ImageView white_icon = (ImageView) findViewById(R.id.homescreen_white);
		white_icon.setOnClickListener(this);

		ImageView blue_icon = (ImageView) findViewById(R.id.homescreen_blue);
		blue_icon.setOnClickListener(this);

		ImageView group_icon = (ImageView) findViewById(R.id.homescreen_group_img);
		group_icon.setOnClickListener(this);
        group_icon.setOnLongClickListener(this);
		
		ImageView today_icon = (ImageView) findViewById(R.id.homescreen_today);
		today_icon.setOnClickListener(this);

		ImageView yestarday_icon = (ImageView) findViewById(R.id.homescreen_yestarday);
		yestarday_icon.setOnClickListener(this);

		ImageView threedays_icon = (ImageView) findViewById(R.id.homescreen_threedays);
		threedays_icon.setOnClickListener(this);

		ImageView search_icon = (ImageView) findViewById(R.id.homescreen_search);
		search_icon.setOnClickListener(this);
	}

	public void onClick(View v) 
	{
		Intent intent = null;
		switch (v.getId()) 
		{
	  
	  		case R.id.homescreen_img_txt:
  
			    intent=new Intent(HomeScreenView.this,SkeyedexTabListActivity.class);
				intent.putExtra("tag","TextlistTab");
				startActivity(intent);
				break;
						  
		  	case R.id.homescreen_img_email:
			  
			  	intent=new Intent(HomeScreenView.this,SkeyedexTabListActivity.class);
				intent.putExtra("tag","EmailTab");
				startActivity(intent);
				break; 
	
		  	case R.id.homescreen_yellowbar:
		  		
	  			intent=new Intent(HomeScreenView.this,SkeyedexTabListActivity.class);
			    intent.putExtra("item_position", 1);
				intent.putExtra("tag","GroupTab");
				startActivity(intent);
	            break; 
		  
		  	case R.id.homescreen_greenbar:
				
		  		intent=new Intent(HomeScreenView.this,SkeyedexTabListActivity.class);
	  			intent.putExtra("tag","GroupTab");
	  			intent.putExtra("item_position", 2);
	  			startActivity(intent);
	  			break; 
		
		  	case R.id.homescreen_white:
			  
	  			intent=new Intent(HomeScreenView.this,SkeyedexTabListActivity.class);
	  			intent.putExtra("tag","GroupTab");
	  			intent.putExtra("item_position", 3);
	  			startActivity(intent);
				break; 
		 	
		  	case R.id.homescreen_blue:
		 		
		 		intent=new Intent(HomeScreenView.this,SkeyedexTabListActivity.class);
			    intent.putExtra("item_position", 4);
				intent.putExtra("tag","GroupTab");
				startActivity(intent);
			  	break; 
		 	
		  	case R.id.homescreen_group_img:
			  
			    intent=new Intent(HomeScreenView.this,SkeyedexTabListActivity.class);
			    intent.putExtra("item_position", 0);
				intent.putExtra("tag","GroupTab");
				startActivity(intent);
				break; 
			
		  	case R.id.homescreen_today:
			 
	  			intent=new Intent(HomeScreenView.this,SkeyedexTabListActivity.class);
	  			intent.putExtra("tag","GroupTab");
	  			intent.putExtra("item_position", 5);
	  			startActivity(intent);
	  			break; 
		  	
			  case R.id.homescreen_yestarday:
			  
	  			intent=new Intent(HomeScreenView.this,SkeyedexTabListActivity.class);
	  			intent.putExtra("tag","GroupTab");
	  			intent.putExtra("item_position", 6);
	  			startActivity(intent);
	  			break;
			   
			  case R.id.homescreen_threedays:

	  			intent=new Intent(HomeScreenView.this,SkeyedexTabListActivity.class);
	  			intent.putExtra("tag","GroupTab");
	  			intent.putExtra("item_position", 7);
	  			startActivity(intent);
	  			break; 
		  			
			  case R.id.homescreen_search:
				  
				intent = new Intent(HomeScreenView.this, SearchEmailsListActivity.class);
				startActivity(intent);
				break; 
		 
		}
		 
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		
		 Intent intent=new Intent(Intent.ACTION_SEND);
		 intent.putExtra(Intent.EXTRA_SUBJECT, "This is Skeyedex");
		 intent.putExtra(Intent.EXTRA_TEXT, "Skeyedex");
		 intent.setType("text/plain");
		 startActivity(intent);
		 
		return true;
	}
}
