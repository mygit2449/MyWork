package com.daleelo.Dashboard.Activities;

import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Ads.Activities.AdvertiseActivity;
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.Business.Activities.BusinessListByID;
import com.daleelo.Dashboard.Model.GetDealsInfoModel;
import com.daleelo.Dashboard.Model.GetHomePageCategoriesModel;
import com.daleelo.Dashboard.Parser.DealsListParser;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.Dialog.LocationNotFoundDialog;
import com.daleelo.Dialog.NetworkNotFoundDialog;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.Main.Activities.SelectCityScreen;
import com.daleelo.Masjid.Activities.MasjidListActivity;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.User.Activities.LoginActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;
import com.daleelo.helper.ImageDownloader;
import com.daleelo.location.helper.CurrentLocation;
import com.daleelo.location.helper.LocationHelper;
import com.daleelo.location.helper.NetworkConnectionHelper;

public class DealsListActivity extends Activity implements CurrentLocation, OnClickListener{

	private ProgressDialog progressdialog;
	private boolean fromCategory = false;
	Intent reciverIntent;
	MyAdapter mDataAdapter;

	TextView mTitle;
	Button mLocal, mNational, mLocation, mPostAds;
	ListView mSpotlightList ;
	
	GetDealsInfoModel mGetDealsInfoModel;
	ArrayList<GetDealsInfoModel> mDataModelList, mDataModelListDump;
	ArrayList<String> mCategoriesIDs;
	private boolean hasData = false;
	public SharedPreferences sharedpreference;
	
	ImageButton mMap;
	
	LinearLayout mHomeCategories;
	ArrayList<GetHomePageCategoriesModel> mHomeMoreCategoryItemsDump, mHomeCategoryItems;
	GetHomePageCategoriesModel mGetHomePageCategoriesModel;
	
	int mType = 1;
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.db_deals_list_screen);
   
        
        
        if(CurrentLocationData.GET_LOCATION){
	        intilizationUI();
	        
	        try {
				getParserData();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }else{
        	
        	locationNotFound();
        }
                
    }
	
	
	private void intilizationUI() {
		// TODO Auto-generated method stub
		
		 mTitle = (TextView)findViewById(R.id.deals_TXT_main_title);
		 mLocal = (Button)findViewById(R.id.deals_BTN_local);
		 mLocation = (Button)findViewById(R.id.deals_BTN_current_location);
		 mNational = (Button)findViewById(R.id.deals_BTN_national);
		 mPostAds = (Button)findViewById(R.id.btn_post_ads);
		 		 
		 mMap = (ImageButton)findViewById(R.id.deals_IMGB_map);
		 sharedpreference = getSharedPreferences("userlogin",MODE_PRIVATE);
		 mHomeCategories = (LinearLayout) findViewById(R.id.deals_LIN_category_items);
		 mLocation.setText(CurrentLocationData.ADDRESS_LINE);
		 mNational.setTextColor(Color.GRAY);
		 
		 mDataModelList = new ArrayList<GetDealsInfoModel>();
		 mDataModelListDump = new ArrayList<GetDealsInfoModel>();
		 
		 mHomeMoreCategoryItemsDump = (ArrayList<GetHomePageCategoriesModel>) getIntent().getExtras().getSerializable("morecat");
		 mGetHomePageCategoriesModel = new GetHomePageCategoriesModel();
		 
		 mLocal.setOnClickListener(this);
		 mNational.setOnClickListener(this);
		 mMap.setOnClickListener(this);
		 mLocation.setOnClickListener(this);
		 mPostAds.setOnClickListener(this);
		
		 setBottomBar();
			
	}
	
	private ImageButton mHome, mMyStuff;
	private EditText mSearch;
	
	private void setBottomBar(){
		
		
		mHome = (ImageButton)findViewById(R.id.business_IMGB_home);
		mMyStuff = (ImageButton)findViewById(R.id.business_IMGB_mystuff);
		mSearch = (EditText)findViewById(R.id.business_TXT_serach);
		
		mSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(DealsListActivity.this, GlobalSearchActivity.class));
				
			}
		});		
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(DealsListActivity.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(DealsListActivity.this,MyStuffActivity.class)
				.putExtra("from", "bottom"));  
			
			}
		});
		
		
	}
	
	
	int selectedCat = 0;
	
	private void setCategoriesData() {
		
		mHomeCategories.removeAllViews();
		
		
		for(int i=0; i<mHomeCategoryItems.size(); i++){			
			
			LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = vi.inflate(R.layout.deal_category_icon_list_row, null);	
			TextView mText = (TextView)v.findViewById(R.id.deal_icon_row_TXT_name);
			ImageView mImg = (ImageView)v.findViewById(R.id.deal_icon_row_IMG_data);
			v.setId(1);
			v.setTag(i);	
			v.setClickable(true);
			v.setOnClickListener(this);
			
			if(i == 0){
				

				mText.setVisibility(View.VISIBLE);
				mImg.setVisibility(View.INVISIBLE);
				
				if(selectedCat == i){
					
					mText.setTextColor(Color.WHITE);
					
				}else{
				
					mText.setTextColor(Color.YELLOW);	
				
				}
				
			}else{
				
				mText.setVisibility(View.INVISIBLE);			
			
				if(selectedCat == i)
					new ImageDownloader().download(String.format(Urls.CAT_IMG_URL,mHomeCategoryItems.get(i).getImageurl()), mImg);
				else
					new ImageDownloader().download(String.format(Urls.CAT_IMG_URL,mHomeCategoryItems.get(i).getImageurl1()), mImg);
			
			}
			mHomeCategories.addView(v);
			
		}
	}

	
	public void setParseData(){
		
		
		if(!fromCategory){
			
			mHomeCategoryItems = new ArrayList<GetHomePageCategoriesModel>();
			mCategoriesIDs = new ArrayList<String>();
			mHomeCategoryItems.clear();
			mCategoriesIDs.clear();			
			mHomeCategoryItems.add(mGetHomePageCategoriesModel);			
			for( int i = 0; i < mDataModelList.size(); i++ ){
				
				if(!mCategoriesIDs.contains(mDataModelList.get(i).getMasterCategory())){
					
					Log.e("Deals one ", "mCategoriesIDs "+mCategoriesIDs+" size"+mHomeCategoryItems.size());				
					mCategoriesIDs.add(mDataModelList.get(i).getMasterCategory());
					Log.e("Deals two ", "mCategoriesIDs "+mCategoriesIDs+" size"+mHomeCategoryItems.size());
					
					for( int j = 0; j < mHomeMoreCategoryItemsDump.size(); j++ ){
						
						if(mHomeMoreCategoryItemsDump.get(j).getCategoryId().equalsIgnoreCase(mDataModelList.get(i).getMasterCategory())){
							
							mHomeCategoryItems.add(mHomeMoreCategoryItemsDump.get(j));
							Log.e("", "before break 2 "+mHomeMoreCategoryItemsDump.get(j).getCategoryId());
							break;
							
						}							
					}				
				}				
			}
			
			Log.e("Deals", "mCategoriesIDs "+mCategoriesIDs+" size"+mHomeCategoryItems.size());
			
			setCategoriesData();
			
		}
		
		mDataAdapter = new MyAdapter(this, mDataModelList);
		mSpotlightList = (ListView)findViewById(R.id.deals_LIST_view);
		mSpotlightList.setAdapter(mDataAdapter);
		
		
//		
		mSpotlightList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View vv, int position,long arg3) {
				
					startActivity(new Intent(DealsListActivity.this,DealsDetailDesp.class)
					.putExtra("data", mDataModelList)
					.putExtra("position", position)
					.putExtra("from", "list"));
				
			}
			
		});
		
	
		
	}
	
		
		
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	
		
		switch (v.getId()) {
		
		
		
		case 1:
			
				fromCategory = true;
				
				if(mDataAdapter != null)
					mDataAdapter.clear();				
				
				selectedCat = (Integer)v.getTag();
				setCategoriesData();
				
							
				if(selectedCat == 0){
				
					mDataModelList.clear();
					
					for(int i=0; i<mDataModelListDump.size();i++){
						
						mDataModelList.add(mDataModelListDump.get(i));
						
					}
					
					mDataAdapter.notifyDataSetChanged();
				
					Log.e("", "mDataModelList "+mDataModelList.size());
					Log.e("", "mDataModelListDump "+mDataModelListDump.size());
					
					
				}else{
					
					filterByCategory(mHomeCategoryItems.get(selectedCat).getCategoryId());
					
				}
				
			
			break;
		
		case R.id.deals_BTN_local:
			
			fromCategory = false;
			selectedCat = 0;
			
			mLocal.setBackgroundResource(R.drawable.bg_blue_strip);
			mLocal.setTextColor(Color.WHITE);
			mNational.setBackgroundResource(R.drawable.bg_light_strip);
			mNational.setTextColor(Color.GRAY);
			mTitle.setText("Local Deals");	
			mType = 1;
			noDataMsg = "Local deals not found";
			
			if(mDataAdapter != null)
				mDataAdapter.clear();
			
			try {
				getParserData();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			break;
			
			
		case R.id.deals_BTN_national:
			
			fromCategory = false;
			selectedCat = 0;
			
			mNational.setBackgroundResource(R.drawable.bg_blue_strip);
			mNational.setTextColor(Color.WHITE);
			mLocal.setBackgroundResource(R.drawable.bg_light_strip);
			mLocal.setTextColor(Color.GRAY);
			mTitle.setText("National Deals");	
			mType = 2;
			noDataMsg = "National deals not found";
			
			
			if(mDataAdapter != null)
				mDataAdapter.clear();
			
			
			try {
				getParserData();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
			
		case R.id.btn_post_ads:
			
			
			if(!sharedpreference.getString("userid", "0").equalsIgnoreCase("0")){
				
				startActivity(new Intent(DealsListActivity.this,AdvertiseActivity.class));
							
			}else{
						
				myAlertDialog();
			}		
			
			break;
			
		case R.id.deals_IMGB_map:
			
			
			if(hasData){
				
				startActivity(new Intent(DealsListActivity.this,DealsMapActivity.class)
				.putExtra("data", mDataModelList)
				.putExtra("from","list"));
				
				}else{
					
					Toast.makeText(this, "No Deals to display", Toast.LENGTH_SHORT).show();
				}
			
			break;
			
			
		case R.id.deals_BTN_current_location:
			
			startActivityForResult(new Intent(DealsListActivity.this,SelectCityScreen.class), SELECT_CITY);			
		
			
			break;
		
		}
		
		
	}
	

	private void filterByCategory(String catId){
		
		
		Log.e("", "mDataModelList "+mDataModelList.size());
		Log.e("", "mDataModelListDump "+mDataModelListDump.size()+" catId "+catId);
		
		mDataModelList.clear();
		
		
		
		for(int i=0 ; i<mDataModelListDump.size();i++){
			
			if(catId.equalsIgnoreCase(mDataModelListDump.get(i).getMasterCategory()))
				mDataModelList.add(mDataModelListDump.get(i));
		}
		
		mDataAdapter.notifyDataSetChanged();
		
		Log.e("", "mDataModelList "+mDataModelList.size());
		Log.e("", "mDataModelListDump "+mDataModelListDump.size());
		
	}
	
	
	
	class MyAdapter extends ArrayAdapter<GetDealsInfoModel>{
    	
    	private LayoutInflater mInflater;
    	ArrayList<GetDealsInfoModel> mDataFeeds;
    	Context context;
    	
    	public MyAdapter(Context context, ArrayList<GetDealsInfoModel> mDataFeeds) {
    		
    		super(context, R.layout.db_spotlight_list_row, mDataFeeds);
    		this.context=context;
        	this.mDataFeeds = mDataFeeds;
          
        }


		@Override
		public GetDealsInfoModel getItem(int position) {
			// TODO Auto-generated method stub
			return mDataFeeds.get(position);
		}

		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
						 
			 LayoutInflater mInflater= LayoutInflater.from(context);
			 
			 convertView = mInflater.inflate(R.layout.db_spotlight_list_row, null);
			 
			 TextView mName = (TextView)convertView.findViewById(R.id.spotlight_row_TXT_name);
			 TextView mDesp = (TextView)convertView.findViewById(R.id.spotlight_row_TXT_desp);
			 TextView mDistance = (TextView)convertView.findViewById(R.id.spotlight_row_TXT_distance);
			 ImageView mImg = (ImageView)convertView.findViewById(R.id.spotlight_row_IMG);
			 	 
			 mName.setText(mDataFeeds.get(position).getDealInfo());
			 mDesp.setText(mDataFeeds.get(position).getBusinessTitle());
			 mDistance.setText(mDataFeeds.get(position).getDistance()+" mi");
			
			 if(mType == 1)
				 mDistance.setText(mDataFeeds.get(position).getDistance()+" mi");
			else
				mDistance.setVisibility(View.INVISIBLE);
			 
			 
			 if(mDataFeeds.get(position).getDealImage().length()>0){
				 new ImageDownloader().download(String.format(Urls.DEAL_IMG_URL,mDataFeeds.get(position).getDealImage()), mImg);
			 }
			 

			return convertView;
		}
		
	}
			
		
		
	 
    
    public void getParserData() throws MalformedURLException{
    	
    	
    	try {
        	progressdialog = ProgressDialog.show(DealsListActivity.this, "","Please wait...", true);
        	String mCity = URLEncoder.encode(CurrentLocationData.CURRENT_CITY, "UTF-8");
        	
        	String mUrl = String.format(
        			Urls.DEALS_BY_CITY_URL, 
        			mCity, 
        			CurrentLocationData.LATITUDE,
        			CurrentLocationData.LONGITUDE,
        			mType,
        			Utils.RANGE);
        	
    		new mAsyncFeedFetcher(mUrl, new FeedParserHandler()).start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }
    
    
    public void getDealParserData(String cId) throws MalformedURLException{
    	
    	
    	try {
        	progressdialog = ProgressDialog.show(DealsListActivity.this, "","Please wait...", true);
        	String mCity = URLEncoder.encode(CurrentLocationData.CURRENT_CITY, "UTF-8");
        	
        	String mUrl = String.format(
        			Urls.DEALS_BY_CATEGORY_URL,
        			cId,
        			mCity, 
        			CurrentLocationData.LATITUDE,
        			CurrentLocationData.LONGITUDE,
        			mType,
        			Utils.RANGE);
        
    		new mAsyncFeedFetcher(mUrl, new FeedParserHandler()).start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }
    
    
    class mAsyncFeedFetcher extends Thread
	{
		String  feedUrl;
		Handler handler;
		
		public mAsyncFeedFetcher(String mUrl, Handler handler) throws MalformedURLException {
		
			
			Log.i("", "mUrl********* "+mUrl);			
			
			this.feedUrl = mUrl;			
			this.handler = handler;
		
		} 
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try
			{
				
//				new BuyProductsByBrandIDParser(feedUrl,handler).parser();
				new DealsListParser(feedUrl, handler).parser();
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			super.run();
		}
		
	}
    
    String noDataMsg = "Local deals not found";
   
	class FeedParserHandler extends Handler {
		public void handleMessage(android.os.Message msg) {
			
			progressdialog.dismiss();
			
			
			
			if(msg.what==0)
			{	
				hasData = true;
				mDataModelListDump.clear();
				mDataModelListDump  = (ArrayList<GetDealsInfoModel>) msg.getData().getSerializable("datafeeds");
				
				mDataModelList.clear();
				for(int i=0; i<mDataModelListDump.size();i++){
					
					mDataModelList.add(mDataModelListDump.get(i));
					
				}
				Log.e("", "mDataModelList "+mDataModelList.size());
				Log.e("", "mDataModelListDump "+mDataModelListDump.size());
				
//				Log.e("", "sptname "+mDataModelList.get(0).getSpotLightName());
				setParseData();			
				

			}else if(msg.what==1){
				
				hasData = false;
				
				new AlertDialogMsg(DealsListActivity.this, noDataMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
					}
					
				}).create().show();
				
				
			}else if(msg.what==2){
				
				hasData = false;
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(DealsListActivity.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
//						MainHomeScreen.this.finish();
						
					}
					
				}).create().show();
			}

		}
	}
	
	
	
//	****************************
	  
//	Alert Dialogs 
  
  private void locationNotFound(){

	  new LocationNotFoundDialog(DealsListActivity.this).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
		
		
		  @Override
		
		  public void onClick(DialogInterface dialog, int which) {
			
			
		
		  }
		
	
	  }).create().show();
  
  }
  
  
//  ****************************
//  OnActivityResult
  

	private final int SELECT_CITY = 1;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.i("", "resultCode *************************" + resultCode);
		// TODO Auto-generated method stub

		
		if (requestCode == SELECT_CITY) {
			
			if (resultCode == 100) {
				
				if(!CurrentLocationData.LOCATION_OLD.equalsIgnoreCase(CurrentLocationData.LOCATION_NEW)){
					
					fromCategory = false;
					
					Log.e("",CurrentLocationData.LOCATION_OLD+"   "+CurrentLocationData.LOCATION_NEW);
					if(CurrentLocationData.LOCATION_NEW.equalsIgnoreCase("Current Location")){
						Log.e("","current   "+CurrentLocationData.LOCATION_NEW);
						
						if(!CurrentLocationData.IS_CURRENT_LOCATION){
							
							getCurrentLocation();
							
						}else{
							
							mLocation.setText(CurrentLocationData.ADDRESS_LINE);
							
							try {								
								
								getParserData();
								
							} catch (MalformedURLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}else{
						
						Log.e("","other   "+CurrentLocationData.LOCATION_NEW);
						
						mLocation.setText(CurrentLocationData.ADDRESS_LINE);						
						
						try {
							
							
							getParserData();
							
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}					
				}
			}
		}
	}
	
  
//  ****************************
  
	
	
//	****************************	
	
//	GetCurrentLocation	
	
LocationHelper mLocationHelper;
Thread myThread;

private void getCurrentLocation(){

if(NetworkConnectionHelper.checkNetworkAvailability()){
	
		    if(!CurrentLocationData.IS_CURRENT_LOCATION){
		    	
 		    mLocation.setText("Fetching Location");
	    	progressdialog = ProgressDialog.show(DealsListActivity.this, "","Fetching current location...", true);
	    	mLocationHelper = new  LocationHelper(this);
	    	
	    	
	    	myThread = new Thread(new Runnable() {

				@Override
				public void run() {

					try {
						Log.e("****before Thread", "getting location");
						Thread.sleep(30000);
						mLocationHelper.stopFetchingCurrentLocation();
						mainLocationHandler.sendEmptyMessage(0);

						Log.e("****after Thread", "getting loaction");

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("****Thread", "InterruptedException");
					}

				}
			});
			myThread.start();
		
	    }	
  
	}else{
	
		mLocation.setText("Location not found");
		networkNotFound();
	
}

}

private void networkNotFound(){

new NetworkNotFoundDialog(DealsListActivity.this).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
	
	
	  @Override
	
	  public void onClick(DialogInterface dialog, int which) {
		
		
	
	  }
	

}).create().show();

}


public Handler mainLocationHandler = new Handler() {
public void handleMessage(android.os.Message msg) {
	progressdialog.dismiss();
	if (msg.what == 0) {
		interruptThread();
		mLocation.setText("Location not found");

		String mMsg = "Unable to get current location please select the location manually";
		try{
		new AlertDialogMsg(DealsListActivity.this, mMsg)
				.setPositiveButton(
						"ok",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								

							}

						}).create().show();
		}catch (Exception e) {
			// TODO: handle exception
		}

	}}};
	
	
	
	private void interruptThread() {

		try {
			
			if (myThread != null)
				myThread.interrupt();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	

	

	@Override
	public void getCurrentLocation(String locationName, String addressline,
			String currentState, String currentCountry, double longitude,
			double latitude,String country_short_name,String state_short_name) {
		// TODO Auto-generated method stub

		interruptThread();
		
		if(!CurrentLocationData.IS_CURRENT_LOCATION ){
			progressdialog.dismiss();
			if(!locationName.equalsIgnoreCase("")){
					
				Log.e("LogCat", "Location "+locationName);
				CurrentLocationData.CURRENT_CITY_DUMP = CurrentLocationData.CURRENT_CITY  = locationName;
				CurrentLocationData.ADDRESS_LINE = locationName+", "+currentState;
				CurrentLocationData.CURRENT_STATE = currentState;
				CurrentLocationData.CURRENT_COUNTRY = currentCountry;
				CurrentLocationData.LATITUDE_DUMP = CurrentLocationData.LATITUDE = Double.toString(latitude);
				CurrentLocationData.LONGITUDE_DUMP = CurrentLocationData.LONGITUDE = Double.toString(longitude);
				CurrentLocationData.IS_CURRENT_LOCATION = true;	
				CurrentLocationData.GET_LOCATION = true;
				CurrentLocationData.LOCATION_OLD = CurrentLocationData.LOCATION_NEW;
				CurrentLocationData.LOCATION_NEW = locationName+", "+currentState;
				CurrentLocationData.CURRENT_COUNTRY_SHORT_NAME = country_short_name;
				CurrentLocationData.CURRENT_STATE_SHORT_NAME = state_short_name;
				mLocation.setText(CurrentLocationData.ADDRESS_LINE);
				
			
			}else{
				
				mLocation.setText("Location not found");
				progressdialog.dismiss();
//				locationNotFound();
			}
				
		}else{

			mLocation.setText("Location not found");
			progressdialog.dismiss();
//			locationNotFound();
		}
	}
	
	
//	****************************			  
//	Alert Dialogs 
	
	
	
	private void myAlertDialog(){
		
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Daleelo");
		builder.setMessage("Please login to  reserve your Deals")
		       .setCancelable(false)
		       .setPositiveButton("Login", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
						startActivity(new Intent(DealsListActivity.this,LoginActivity.class)
						.putExtra("from", "forads"));
		               
		           }
		       })
		       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}

}
	
	