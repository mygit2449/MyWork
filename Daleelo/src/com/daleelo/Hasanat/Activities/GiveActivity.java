package com.daleelo.Hasanat.Activities;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Ads.Activities.AdvertiseActivity;
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.Business.Model.BusinessListModel;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.Dialog.NetworkNotFoundDialog;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Hasanat.Model.BusinessDetailModel;
import com.daleelo.Hasanat.Parser.FeaturedMosqueParser;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.Main.Activities.SelectCityScreen;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.TripPlanner.Model.TripPlannerLocationPointsModel;
import com.daleelo.User.Activities.LoginActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;
import com.daleelo.location.helper.CurrentLocation;
import com.daleelo.location.helper.LocationHelper;
import com.daleelo.location.helper.NetworkConnectionHelper;

public class GiveActivity extends Activity implements OnClickListener, CurrentLocation{

	private ProgressDialog progressdialog;
	Intent reciverIntent;
	GiveListAdapter mDataAdapter;

	RelativeLayout mRelFeaturedData, mRelNoData;
	TextView mTitle, mFeatureItemName, mFeaturedItemAddress, mFeaturedItemPhone;
	ListView mBusinessItemList ;
	Button mLocation, mPostAds;
	
	String mCategoryID;
	private int mSlideDataCount = 0;
	public SharedPreferences sharedpreference;
	
	BusinessListModel mBusinessListModel;
	ArrayList<BusinessDetailModel> mDataModelList, mFeaturedDataModelList;
	ArrayList<String> mAddedBusinessId;
	
	
	String mMsg;
	TripPlannerLocationPointsModel mTripPlannerLocationPointsModel;
	ArrayList <TripPlannerLocationPointsModel> mTripFeeds;
	
	int mType = 1, mPageCount = 1;
	
	private int mStartValue = 1, mEndValue = 10;
	private boolean hasData = false;
	private ImageButton mFilter, mMap, mDone;
	ArrayList<String> mData;
		
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.give_list);
   
        intilizationUI();
        getBusinessData();
    }
	
	
	private void refreshData(){
		
		setData();	

		
		if(mDataAdapter != null){
			mDataModelList.clear();
			mDataAdapter.clear();
		}
		
		mRelFeaturedData.setVisibility(View.GONE);	
		
	}
	
	
	private void getBusinessData(){	
		
	        
	        try {
	        	getFeaturedData();
				getParserData();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	


	private void intilizationUI() {
		// TODO Auto-generated method stub
		
		reciverIntent = getIntent();
		mAddedBusinessId = new ArrayList<String>();
		sharedpreference = getSharedPreferences("userlogin",MODE_PRIVATE);
		mCategoryID = reciverIntent.getExtras().getString("categoryId");
		Log.e("", "mCategoryID "+mCategoryID);		
		
		mTitle = (TextView)findViewById(R.id.give_TXT_main_title);	
		mRelFeaturedData  = (RelativeLayout)findViewById(R.id.give_features_REL_data);
		mFeatureItemName = (TextView)findViewById(R.id.give_features_TXT_name);
		mFeaturedItemAddress = (TextView)findViewById(R.id.give_features_TXT_address);
		mFeaturedItemPhone = (TextView)findViewById(R.id.give_features_TXT_phone);
		mLocation  = (Button)findViewById(R.id.give_BTN_current_location);
		mFilter = (ImageButton)findViewById(R.id.give_IMGB_filter);
		mMap = (ImageButton)findViewById(R.id.give_IMGB_map);		
	
		mRelFeaturedData.setVisibility(View.GONE);	
		mRelFeaturedData.setOnClickListener(this);
		mFilter.setOnClickListener(this);
		mLocation.setOnClickListener(this);
		mMap.setOnClickListener(this);
		
		setData();
		
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
				
				startActivity(new Intent(GiveActivity.this, GlobalSearchActivity.class));
				
			}
		});	
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(GiveActivity.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(GiveActivity.this,MyStuffActivity.class)
				.putExtra("from", "bottom"));
			
			}
		});
		
		
	}
	
	
	public void setData(){
		
		mLocation.setText(CurrentLocationData.ADDRESS_LINE);	
		mTitle.setText(reciverIntent.getExtras().getString("categoryName"));
		mDataModelList = new ArrayList<BusinessDetailModel>();
		
		try {
			
			mData = new ArrayList<String>();
			mData.add(mCategoryID);
			mData.add("0");
			mData.add("0");
			mData.add(URLEncoder.encode(CurrentLocationData.CURRENT_CITY, "UTF-8"));
			mData.add(CurrentLocationData.LATITUDE);
			mData.add(CurrentLocationData.LONGITUDE);
			mData.add(Utils.RANGE);
			mData.add("1");
			mData.add("0");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	
	}
	

	
	
	
	public void setListData(){
		
		if(mDataAdapter != null)
			mDataAdapter.clear();		
		mDataAdapter = new GiveListAdapter(this, mDataModelList);
		mBusinessItemList = (ListView)findViewById(R.id.give_LIST_view);
		mBusinessItemList.setAdapter(mDataAdapter);		
	
		
		mBusinessItemList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View vv, final int position,long arg3) {
				
					mShowStatus = false;	
				
					startActivity(new Intent(GiveActivity.this,GiveDetailDescActivity.class)
					.putExtra("data", mDataModelList)
					.putExtra("pos", position));				
			
			}
			
		});
		
	
		
	}
	
	
	
//	************************
	
//	SlideShow
	
	Thread myThread;
	private boolean mShowStatus = true;
	
	private void slideShow(){
		
		myThread = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					
					
					while(mShowStatus){		
						
					mainHandler.sendEmptyMessage(5);						
					Thread.sleep(3000);		
					
					Log.e("Thread", "running");
					
					}
					

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("****Thread", "InterruptedException");
				}

			}
		});
		myThread.start();
	}
	
	
	
	private void mChangeSlideDate(){
		
		if(mSlideDataCount < mFeaturedDataModelList.size()){
			
			mFeatureItemName.setText(mFeaturedDataModelList.get(mSlideDataCount).getBusinessTitle());
			mFeaturedItemAddress.setText(
					 (mFeaturedDataModelList.get(mSlideDataCount).getAddressLine1().length()>0 ? mFeaturedDataModelList.get(mSlideDataCount).getAddressLine1()+", ":"")+
					 (mFeaturedDataModelList.get(mSlideDataCount).getAddressLine2().length()>0 ? mFeaturedDataModelList.get(mSlideDataCount).getAddressLine2()+", ":"")+
					 (mFeaturedDataModelList.get(mSlideDataCount).getCityName().length()>0 ? "\n"+mFeaturedDataModelList.get(mSlideDataCount).getCityName()+", ":"\n")+""+
					 (mFeaturedDataModelList.get(mSlideDataCount).getStateCode().length()>0 ? mFeaturedDataModelList.get(mSlideDataCount).getStateCode()+", ":"")+""+
					 (mFeaturedDataModelList.get(mSlideDataCount).getAddressZipcode().length()>0 ? mFeaturedDataModelList.get(mSlideDataCount).getAddressZipcode():""));
			 mFeaturedItemPhone.setText(mFeaturedDataModelList.get(mSlideDataCount).getAddressPhone());
			mSlideDataCount++;
			
		}else{
			
			mSlideDataCount = 0;
			mFeatureItemName.setText(mFeaturedDataModelList.get(mSlideDataCount).getBusinessTitle());
			mFeaturedItemAddress.setText(
					 (mFeaturedDataModelList.get(mSlideDataCount).getAddressLine1().length()>0 ? mFeaturedDataModelList.get(mSlideDataCount).getAddressLine1()+", ":"")+
					 (mFeaturedDataModelList.get(mSlideDataCount).getAddressLine2().length()>0 ? mFeaturedDataModelList.get(mSlideDataCount).getAddressLine2()+", ":"")+
					 (mFeaturedDataModelList.get(mSlideDataCount).getCityName().length()>0 ? "\n"+mFeaturedDataModelList.get(mSlideDataCount).getCityName()+", ":"\n")+""+
					 (mFeaturedDataModelList.get(mSlideDataCount).getStateCode().length()>0 ? mFeaturedDataModelList.get(mSlideDataCount).getStateCode()+", ":"")+""+
					 (mFeaturedDataModelList.get(mSlideDataCount).getAddressZipcode().length()>0 ? mFeaturedDataModelList.get(mSlideDataCount).getAddressZipcode():""));
			mFeaturedItemPhone.setText(mFeaturedDataModelList.get(mSlideDataCount).getAddressPhone());
			mSlideDataCount++;
		}
		
		
	}
	
	
	public Handler mainHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.what == 5) {			
				
				Log.e("", "Add display");
				
				mChangeSlideDate();	
				

			}

		}
	};
	
//	************************
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		
		switch (v.getId()) {
		
		case R.id.give_features_REL_data:
			
			mShowStatus = false;
			
			startActivity(new Intent(GiveActivity.this,BusinessDetailsDescActivity.class)
			.putExtra("data", mFeaturedDataModelList.get(mSlideDataCount-1))
			.putExtra("from", "featured"));						
			
			break;
			
	case R.id.give_IMGB_map:
			
			mShowStatus = false;
			
			if(mDataModelList.size()>0){
			
				startActivity(new Intent(GiveActivity.this, HasanatMapActivity.class)
				.putExtra("data", mDataModelList)
				.putExtra("from","list"));
			
			}else{
				
				Toast.makeText(this, "No Business data to display", Toast.LENGTH_SHORT).show();
			}
						
			
			break;
			
		case R.id.give_IMGB_filter:
			
			mShowStatus = false;
			Log.e("", "Datd "+mData);
			
			startActivityForResult(
					new Intent(GiveActivity.this,ZakatFilterActivity.class)
					.putExtra("data", mData)
					.putExtra("category", mCategoryID)
					,FILTER);
						
			
			break;
			

		case R.id.give_BTN_current_location:
			
			startActivityForResult(new Intent(GiveActivity.this,SelectCityScreen.class), SELECT_CITY);			
		
			
			break;
			
		case R.id.btn_post_ads:
			
			if(!sharedpreference.getString("userid", "0").equalsIgnoreCase("0")){
				
				startActivity(new Intent(GiveActivity.this,AdvertiseActivity.class));
							
			}else{
						
				myAlertDialog();
			}		
			
			break;	
			
		}
		
		
	}
	private final int SELECT_CITY = 1;
	private final int FILTER = 2;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.i("", "resultCode *************************" + resultCode);
		// TODO Auto-generated method stub
		
		if (requestCode == FILTER) {
			
			if (resultCode == 101) {
				
				if(mDataAdapter != null){
					mDataModelList.clear();
					mDataAdapter.clear();
				}
				
				mData =  (ArrayList<String>) data.getExtras().getSerializable("data");			
				
				 try {
						getParserData();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				


			}
		
		
		
		
		if (requestCode == SELECT_CITY) {
			
			if (resultCode == 100) {
				
				if(!CurrentLocationData.LOCATION_OLD.equalsIgnoreCase(CurrentLocationData.LOCATION_NEW)){
										
					refreshData();					
					
					Log.e("",CurrentLocationData.LOCATION_OLD+"   "+CurrentLocationData.LOCATION_NEW);

					if(CurrentLocationData.LOCATION_NEW.equalsIgnoreCase("Current Location")){
						Log.e("","current   "+CurrentLocationData.LOCATION_NEW);
						
						if(!CurrentLocationData.IS_CURRENT_LOCATION){
							
							getCurrentLocation();
							
						}else{
							
							mLocation.setText(CurrentLocationData.ADDRESS_LINE);
							getBusinessData();
						}
						
					}else{
						
						Log.e("","other   "+CurrentLocationData.LOCATION_NEW);
						
						mLocation.setText(CurrentLocationData.ADDRESS_LINE);						
						getBusinessData();
					}
					
				}
				


			}
		}
		}
	
	
	LocationHelper mLocationHelper;
	
	private void getCurrentLocation(){
		
		if(NetworkConnectionHelper.checkNetworkAvailability()){
			
	   		    if(!CurrentLocationData.IS_CURRENT_LOCATION){
	   		    	
		   		    mLocation.setText("Fetching Location");
			    	progressdialog = ProgressDialog.show(GiveActivity.this, "","Fetching current location...", true);
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

		  new NetworkNotFoundDialog(GiveActivity.this).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
			
			
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
				new AlertDialogMsg(GiveActivity.this, mMsg)
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
			
			
//			****************************	
			
//			GetCurrentLocation	


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
						getBusinessData();
					
					}else{
						
						mLocation.setText("Location not found");
						progressdialog.dismiss();
//						locationNotFound();
					}
						
				}else{

					mLocation.setText("Location not found");
					progressdialog.dismiss();
//					locationNotFound();
				}
			}
			
			
			
//			****************************	
			
			

	

    	
    	class GiveListAdapter extends ArrayAdapter<BusinessDetailModel>{

    		Context context;
    		ArrayList<BusinessDetailModel> feeds;
    		public GiveListAdapter(Context context,  ArrayList<BusinessDetailModel> mFilteredDataList) {
    			super(context, R.layout.give_list_row);
    			this.context = context;
    			feeds = mFilteredDataList;
    		}
    		
    		@Override
    		public int getCount() {
    			return feeds.size();
    		}

    		@Override
    		public View getView(int position, View convertView, ViewGroup parent) {
    			
    			LayoutInflater inflater = LayoutInflater.from(context);
    			convertView = inflater.inflate(R.layout.give_list_row, null);
    			TextView txt_Name = (TextView)convertView.findViewById(R.id.give_listrow_TXT_name);
    			TextView txt_Address = (TextView)convertView.findViewById(R.id.give_listrow_TXT_address);		
    			TextView txt_Phone = (TextView)convertView.findViewById(R.id.give_listrow_TXT_phone);
    			TextView txt_Distance = (TextView)convertView.findViewById(R.id.give_listrow_TXT_distance);
    			ImageView Phone_img = (ImageView)convertView.findViewById(R.id.give_listrow_IMG_phone);
    			
    			txt_Name.setText(feeds.get(position).getBusinessTitle());
    			//txt_Address.setText(feeds.get(position).getBusinessAddress());
    			txt_Address.setText((feeds.get(position).getAddressLine1().length()>0 ? feeds.get(position).getAddressLine1()+", ":"")+
   					 (feeds.get(position).getAddressLine2().length()>0 ? feeds.get(position).getAddressLine2()+", ":"")+
   					 (feeds.get(position).getCityName().length()>0 ? feeds.get(position).getCityName()+", ":"")+""+
   					 (feeds.get(position).getStateCode().length()>0 ? feeds.get(position).getStateCode()+", ":"")+""+
   					 (feeds.get(position).getAddressZipcode().length()>0 ? feeds.get(position).getAddressZipcode():""));
   			
    			
    			txt_Phone.setText(feeds.get(position).getAddressPhone());
    			txt_Distance.setText(feeds.get(position).getDistance()+" mi");
    			
    			return convertView;
    		}
    		
    		
    		
    	}
			
		
		
	 String mBusinesName = "", mBusnessOwner = "", mSubCategory, mOrderBy = "0", mDealsOnly = "0";
    
    public void getParserData() throws MalformedURLException{
    	
    	
    	try {
        	progressdialog = ProgressDialog.show(GiveActivity.this, "","Please wait...", true);

        	String mUrl = String.format(Urls.MOSQUE_BY_FILTER_URL,
        			mData.get(0),
        			mData.get(1),
        			mData.get(2),
        			mData.get(3),
        			mData.get(4),
        			mData.get(5),
        			mData.get(6),
        			mData.get(7));
        	
    		new mBusinessDataFetcher(mUrl, new FeedParserHandler()).start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }
    
    
    class mBusinessDataFetcher extends Thread
	{
		String  feedUrl;
		Handler handler;
		
		public mBusinessDataFetcher(String mUrl, Handler handler) throws MalformedURLException {
		
			
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
				new FeaturedMosqueParser(feedUrl, handler).parser();
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			super.run();
		}
		
	}
    
    
    String noDataMsg = "Business data not found", mTotalCount;
    int mTotalItems;
    
    
    BusinessListModel tempBusinessListModel = null;
    
	class FeedParserHandler extends Handler {
		public void handleMessage(android.os.Message msg) {
			
			progressdialog.dismiss();
			
			
			
			if(msg.what==0)
			{	
				hasData = true;
				mDataModelList = null;
				mDataModelList = (ArrayList<BusinessDetailModel>) msg.getData().getSerializable("datafeeds");
				
				setListData();			
				

			}else if(msg.what==1){
				
				
				
				new AlertDialogMsg(GiveActivity.this, noDataMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						if(mPageCount == 1){
							
							mGotBusinessData = false;
							mDataFoundHandler.sendEmptyMessage(0);
							
						}
						
					}
					
				}).create().show();
				
				
			}else if(msg.what==2){
				
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(GiveActivity.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						GiveActivity.this.finish();
						
					}
					
				}).create().show();
			}

		}
		
	}
		
		
		public void getFeaturedData() throws MalformedURLException{
	    	
	    	
	    	try {
	    		
	    		String mUrl = String.format(Urls.FEATURED_MOSQUE_URL,
	        			mCategoryID,
	        			CurrentLocationData.CURRENT_CITY,
	        			CurrentLocationData.LATITUDE,
	        			CurrentLocationData.LONGITUDE,
	        			Utils.RANGE,
	        			"1",
	        			"100000");
	    		
	    		new mFeaturedDataFetcher(mUrl, new FeaturedDataHandler()).start();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	

	    }
	    
		
		 class mFeaturedDataFetcher extends Thread
			{
				String  feedUrl;
				Handler handler;
				
				public mFeaturedDataFetcher(String mUrl, Handler handler) throws MalformedURLException {
				
					
					Log.i("", "mUrl********* "+mUrl);			
					
					this.feedUrl = mUrl;			
					this.handler = handler;
				
				} 
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try
					{
						
						new FeaturedMosqueParser(feedUrl, handler).parser();
					
					}catch(Exception e){
						
						e.printStackTrace();
					}
					super.run();
				}
				
			}
		    
		    
	boolean dataFound  = true;
	class FeaturedDataHandler extends Handler {
		public void handleMessage(android.os.Message msg) {
			if(msg.what==0)
			{	
				mRelFeaturedData.setVisibility(View.VISIBLE);
				mFeaturedDataModelList =  (ArrayList<BusinessDetailModel>) msg.getData().getSerializable("datafeeds");
						
				if(dataFound){							
					slideShow();
					dataFound  = false;
				}
			}else{
				
				mGotFeaturedData = false;
				mDataFoundHandler.sendEmptyMessage(0);
				
			}
		}			
	}
			
			
			
			DataFoundHandler mDataFoundHandler = new DataFoundHandler();
			boolean mGotFeaturedData = true, mGotBusinessData = true;
			
			class DataFoundHandler extends Handler {
				public void handleMessage(android.os.Message msg) {
					
					
					if(!mGotFeaturedData  && !mGotBusinessData){
					
					}
				}
			}
			
			
//			****************************			  
//			Alert Dialogs 
			
		  private void alertDialog(String msg){

			  new AlertDialogMsg(GiveActivity.this, msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
				
				
				  @Override
				
				  public void onClick(DialogInterface dialog, int which) {
					
					
				
				  }
				
			
			  }).create().show();
		  
		  }
		  
		  
			private void myAlertDialog(){
				
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Daleelo");
				builder.setMessage("Please login to post your Advertise")
				       .setCancelable(false)
				       .setPositiveButton("Login", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   
								startActivity(new Intent(GiveActivity.this,LoginActivity.class)
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
	
	