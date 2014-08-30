package com.daleelo.GlobalSearch;

import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.Dashboard.Model.GetDealsInfoModel;
import com.daleelo.DataBase.DatabaseHelper;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.Main.Model.GetCitiesModel;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;

public class GlobalSearchActivity extends ListActivity implements OnClickListener{

	private Button mBTN_Cancel, mBTN_Search;
	private EditText mETXT_SearchItem;	
	
	ArrayList<String> mGetRecentSearchKeys = null, mGetRecentSearchKeysDump = null;
	
	GetCitiesModel mCurrentLocationModel;

	AppCitiesAdapter mRecentSearchKeysAdapter, mRecentSearchKeysAdapterDump;
	DatabaseHelper mDbHelper;

	boolean main_list = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.global_search_screen);
		initializeUI();
	}

	
	public void initializeUI(){		

		mETXT_SearchItem = (EditText)findViewById(R.id.search_screen_ETXT_what);
		mBTN_Cancel = (Button)findViewById(R.id.search_screen_BTN_cancel);
		mBTN_Search = (Button)findViewById(R.id.search_screen_BTN_search);
		
		mSearchHandler = new SearchHandler();
		mGetRecentSearchKeys = new ArrayList<String>();
		
		new mAsyncFetchCityFormDB().start();
    		
		mBTN_Cancel.setOnClickListener(this);
		mBTN_Search.setOnClickListener(this);		
		
	}
	
	private void setDataToList(){
		
		mRecentSearchKeysAdapter =  new AppCitiesAdapter(this, mGetRecentSearchKeys);
		mRecentSearchKeysAdapterDump =  new AppCitiesAdapter(this, mGetRecentSearchKeysDump);
		setListAdapter(mRecentSearchKeysAdapterDump);
		
		

		mETXT_SearchItem.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				mGetRecentSearchKeys.clear();
				mRecentSearchKeysAdapter.clear();
				
				int textlength = mETXT_SearchItem.getText().length();
				
				if(textlength==0){
					
		        	  main_list=true;
		        	  setListAdapter(mRecentSearchKeysAdapterDump);
		        	  
		          }else{
		        	  
		        	  main_list=false;

					for (int i = 0; i < mGetRecentSearchKeysDump.size(); i++) {
	
						try {
	
							if (mETXT_SearchItem.getText().toString().trim().toLowerCase()
									.equalsIgnoreCase((String) mGetRecentSearchKeysDump.get(i).trim().toLowerCase().subSequence(0, textlength))) {
								
								mGetRecentSearchKeys.add(mGetRecentSearchKeysDump.get(i));
								
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					
					setListAdapter(mRecentSearchKeysAdapter);
				}	

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

	}
 

	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if(main_list){
			
			mETXT_SearchItem.setText(mGetRecentSearchKeysDump.get(position));
			
		}else{
			
			mETXT_SearchItem.setText(mGetRecentSearchKeys.get(position));			
			
		}
		
		mGetRecentSearchKeys.clear();
		mRecentSearchKeysAdapter.clear();
		setListAdapter(mRecentSearchKeysAdapter);
		
	}
 
	class mAsyncFetchCityFormDB extends Thread
	{
		

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try
			{
							
				mDbHelper= new DatabaseHelper(getApplicationContext());			
				mDbHelper.openDataBase();
				
				mGetRecentSearchKeysDump = mDbHelper.getRecentSearchKeys();				
				mSearchHandler.sendEmptyMessage(0);
				Log.e("", "mAsyncFetchCityFormDB");
				
			}catch(Exception e){
				e.printStackTrace();
			}
			super.run();
		}
		
	}
	

	SearchHandler mSearchHandler;
	
	class SearchHandler extends Handler
	{
		public void handleMessage(android.os.Message msg)
		{
			Log.e("", "SearchHandler");
			
			if(msg.what==0)
			{
				Log.e("", "SearchHandler  wnat");
				setDataToList();
			}			
			
		}
	}
	
	
	class AppCitiesAdapter extends ArrayAdapter<String>
	{
	
		ArrayList<String> feed;
		Context context;
		public AppCitiesAdapter(Context context, ArrayList<String> feed) 
		{
			super(context, R.layout.city_list_row,feed);
			this.context=context;
			this.feed=feed;
		}

		

		@Override
		public String getItem(int position) {
			// TODO Auto-generated method stub
			return feed.get(position);
		}		

	

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) 
		{
			
			LayoutInflater inflater= LayoutInflater.from(context);
			convertView=inflater.inflate(R.layout.city_list_row, null);		
			TextView txt_type_name=(TextView)convertView.findViewById(R.id.city_TXT_list_row_name);	
			txt_type_name.setText(feed.get(position));
					
			return convertView;
		}
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_screen_BTN_search:
			
			if (mETXT_SearchItem.getText().length() > 0) {

				try {
					getSearchData();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else{
				
				alertDialogwithMessage("Please enter search query");	
				
			}
		
			break;
		case R.id.search_screen_BTN_cancel:
			finish();
			break;
		
		}
	}
	
	
	
	private ProgressDialog progressdialog;
	
    public void getSearchData() throws MalformedURLException{
    	
    	
    	try {
        	progressdialog = ProgressDialog.show(GlobalSearchActivity.this, "","Please wait...", true);
        	
        	String mUrl = String.format(
    				Urls.GLOBAL_SEARCH,									
    				URLEncoder.encode(mETXT_SearchItem.getText().toString(),"UTF-8"),
    				Utils.SEARCH_SUB_KEY,
    				URLEncoder.encode(CurrentLocationData.CURRENT_CITY,"UTF-8"),
    				CurrentLocationData.LATITUDE,
        			CurrentLocationData.LONGITUDE,
        			Utils.RANGE);
        	
    		new mAsyncDataFetcher(mUrl, new DataParserHandler()).start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }
    
    
    class mAsyncDataFetcher extends Thread
	{
		String  feedUrl;
		Handler handler;
		
		public mAsyncDataFetcher(String mUrl, Handler handler) throws MalformedURLException {
		
			
			Log.i("", "mUrl********* "+mUrl);			
			
			this.feedUrl = mUrl;			
			this.handler = handler;
		
		} 
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try
			{				

				new GlobalSearchParser(feedUrl, handler).parser();
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			super.run();
		}
		
	}
    
    
    
    String noDataMsg = "Currently no data found";
    ArrayList<GlobalSearchModel> mSearchFeeds = null;
    
	class DataParserHandler extends Handler {
		public void handleMessage(android.os.Message msg) {
			
			progressdialog.dismiss();
			
			
			
			if(msg.what==0)
			{	
						
				mDbHelper.insertSearchKey(mETXT_SearchItem.getText().toString());
				mSearchFeeds  = (ArrayList<GlobalSearchModel>) msg.getData().getSerializable("datafeeds");
				Log.e("", "***********data"+mSearchFeeds.get(0).getBusinessTitle());
				sendDataToList();


			}else if(msg.what==1){
				
				
				new AlertDialogMsg(GlobalSearchActivity.this, noDataMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
					}
					
				}).create().show();
				
				
			}else if(msg.what==2){				
				
				
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(GlobalSearchActivity.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
//						MainHomeScreen.this.finish();
						
					}
					
				}).create().show();
			}

		}
	}
	
	
	
	
	private void sendDataToList(){
		
		startActivity(new Intent(GlobalSearchActivity.this,GlobalSearchListActivity.class)
		.putExtra("data", mSearchFeeds));
		
	}
	
	
	
	
	public void alertDialogwithMessage(String message){
		
		new AlertDialogMsg(GlobalSearchActivity.this, message).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).create().show();
	}
}
