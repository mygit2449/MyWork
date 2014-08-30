package com.daleelo.Business.Activities;

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
import com.daleelo.Business.Model.BusinessListModel;
import com.daleelo.Business.Parser.BusinessListParser;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.Dialog.NetworkNotFoundDialog;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
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

public class BusinessListByID extends Activity implements OnClickListener, CurrentLocation{

	private ProgressDialog progressdialog;
	Intent reciverIntent;
	MyAdapter mDataAdapter;

	RelativeLayout mRelFeaturedData, mRelNoData, mRelFeaturedNoData;
	TextView mTitle, mFeatureItemName, mFeaturedItemAddress, mFeaturedItemPhone;
	ListView mBusinessItemList ;
	Button mLocation, mPostAds;
	
	String mCategoryID;
	private int mSlideDataCount = 0;
	public SharedPreferences sharedpreference;
	
	BusinessListModel mBusinessListModel;
	ArrayList<BusinessListModel> mDataModelList, mFeaturedDataModelList;
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
        
        setContentView(R.layout.business_list_screen);
   
        intilizationUI();
        getBusinessData();
    }
	
	
	private void refreshData(){
		
		setData();	
		mStartValue = 1; 
		mEndValue = 10;		
		mPageCount = 1;
		
		if(mDataAdapter != null)
			mDataAdapter.clear();
		
		mRelFeaturedData.setVisibility(View.GONE);	
		mRelNoData.setVisibility(View.GONE);
		mRelFeaturedNoData.setVisibility(View.GONE);
		
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
		mSubCategory = mCategoryID;
		Log.e("", "mCategoryID "+mCategoryID);		
		
		mTitle = (TextView)findViewById(R.id.business_TXT_main_title);	
		mRelFeaturedData  = (RelativeLayout)findViewById(R.id.business_features_REL_data);
		mRelFeaturedNoData = (RelativeLayout)findViewById(R.id.business_features_REL_no_data);
		mRelNoData = (RelativeLayout)findViewById(R.id.business_REL_no_data);
		mFeatureItemName = (TextView)findViewById(R.id.business_features_TXT_name);
		mFeaturedItemAddress = (TextView)findViewById(R.id.business_features_TXT_address);
		mFeaturedItemPhone = (TextView)findViewById(R.id.business_features_TXT_phone);
		mLocation  = (Button)findViewById(R.id.business_BTN_current_location);
		mPostAds = (Button)findViewById(R.id.btn_post_ads);
		mFilter = (ImageButton)findViewById(R.id.business_IMGB_filter);
		mMap = (ImageButton)findViewById(R.id.business_IMGB_map);
		mDone = (ImageButton)findViewById(R.id.business_IMGB_done);
		mHome = (ImageButton)findViewById(R.id.business_IMGB_home);
		mMyStuff = (ImageButton)findViewById(R.id.business_IMGB_mystuff);
		
		


		if(getIntent().getExtras().getString("from").equalsIgnoreCase("trip")){
			
			mTripFeeds = new ArrayList<TripPlannerLocationPointsModel>();
			mMap.setVisibility(View.GONE);
			mDone.setVisibility(View.VISIBLE);
			
		}else{
			
			mMap.setVisibility(View.VISIBLE);
			mDone.setVisibility(View.GONE);
			
		}
		
		
		mHome.setOnClickListener(this);
		mMyStuff.setOnClickListener(this);		
		mRelFeaturedData.setOnClickListener(this);
		mFilter.setOnClickListener(this);
		mLocation.setOnClickListener(this);
		mMap.setOnClickListener(this);
		mDone.setOnClickListener(this);
		mPostAds.setOnClickListener(this);
		
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
				
				startActivity(new Intent(BusinessListByID.this, GlobalSearchActivity.class));
				
			}
		});
		
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(BusinessListByID.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(BusinessListByID.this,MyStuffActivity.class)
				.putExtra("from", "bottom"));
			
			}
		});
		
		
	}
	
	
	public void setData(){
		
		mLocation.setText(CurrentLocationData.ADDRESS_LINE);	
		mTitle.setText(reciverIntent.getExtras().getString("categoryName"));
		
		try {
			
			mData = new ArrayList<String>();
			mData.add("");
			mData.add("");
			mData.add(mCategoryID);
			mData.add("0");
			mData.add(URLEncoder.encode(CurrentLocationData.CURRENT_CITY, "UTF-8"));
			mData.add(CurrentLocationData.LATITUDE);
			mData.add(CurrentLocationData.LONGITUDE);
			mData.add(Utils.RANGE);
			mData.add(1+"");
			mData.add(10+"");
			mData.add("2");
			mData.add("0");
			mData.add("0");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	
	}
	

	
	
	
	public void setListData(){
		
		if(mDataAdapter != null)
			mDataAdapter.clear();
		
		
		
		mDataAdapter = new MyAdapter(this, mDataModelList);
		mBusinessItemList = (ListView)findViewById(R.id.business_LIST_view);
		mBusinessItemList.setAdapter(mDataAdapter);		
	
		
		mBusinessItemList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View vv, final int position,long arg3) {
				
				mShowStatus = false;				
				
				if(getIntent().getExtras().getString("from").equalsIgnoreCase("trip")){					
					
					mMsg = "Are you sure want to add this Business Listing as a destination";
					
					mTripPlannerLocationPointsModel = new TripPlannerLocationPointsModel();
					
					mTripPlannerLocationPointsModel.setBusinessId(mDataModelList.get(position).getBusinessId());
					mTripPlannerLocationPointsModel.setBusinessTitle(mDataModelList.get(position).getBusinessTitle());
					mTripPlannerLocationPointsModel.setAddressLine1(mDataModelList.get(position).getAddressLine1());
					mTripPlannerLocationPointsModel.setAddressLine2(mDataModelList.get(position).getAddressLine2());
					mTripPlannerLocationPointsModel.setAddressLat(Double.parseDouble(mDataModelList.get(position).getAddressLat()));
					mTripPlannerLocationPointsModel.setAddressLong(Double.parseDouble(mDataModelList.get(position).getAddressLong()));
					mTripPlannerLocationPointsModel.setCityName(mDataModelList.get(position).getCityName());
					mTripPlannerLocationPointsModel.setMiddlecity(true);
					mTripPlannerLocationPointsModel.setCategory("main");
					mTripPlannerLocationPointsModel.setType("t");
					mTripPlannerLocationPointsModel.setItemType("business");
					
					Log.e("", "mTripFeeds "+mTripFeeds.size());
					
					if( mAddedBusinessId.contains(mDataModelList.get(position).getBusinessId())){
						
						mMsg = "Item already added to the Destination Listing";
						alertDialog(mMsg);
						
					}else{
					

					  new AlertDialogMsg(BusinessListByID.this, mMsg)
					  .setPositiveButton("YES", new android.content.DialogInterface.OnClickListener() {
						
						
						  @Override
						
						  public void onClick(DialogInterface dialog, int which) {		
					
							  mAddedBusinessId.add(mDataModelList.get(position).getBusinessId());							  
							  mTripFeeds.add(mTripPlannerLocationPointsModel);							
							  mTripPlannerLocationPointsModel = null;					  
					
						  }
							
							
					  }).setNegativeButton("NO", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).create().show();					
					
					}
				
				}else{
				
					startActivity(new Intent(BusinessListByID.this,BusinessDetailDesp.class)
					.putExtra("data", mDataModelList)
					.putExtra("position", position)
					.putExtra("from", "list"));
				
				}
			}
			
		});
		
	
		
	}
	
	
	
//	************************
	
//	SlideShow
	



	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
		if(!mShowStatus){
			slideShow();
			mShowStatus = true;
		}
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mShowStatus = false;
	}

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
					
					Thread.sleep(100);	
					mainHandler.sendEmptyMessage(6);
					Thread.sleep(3000);	

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
		
		if(mFeaturedDataModelList != null){
			
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
	}
	
	
	public Handler mainHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.what == 5) {			
				
				Log.e("", "Add display");
				
				mChangeSlideDate();	
				

			}else{
				
				try {
					
					myThread.interrupt();	
					myThread = null;
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		}
	};
	
//	************************
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		
		switch (v.getId()) {
		
		case R.id.business_features_REL_data:
			
			mShowStatus = false;
			
			startActivity(new Intent(BusinessListByID.this,BusinessDetailDesp.class)
			.putExtra("data", mFeaturedDataModelList.get(mSlideDataCount-1))
			.putExtra("from", "featured"));
						
			
			break;
			
	case R.id.business_IMGB_map:
			
			mShowStatus = false;
			
			if(hasData){
			
			startActivity(new Intent(BusinessListByID.this,BusinessMapActivity.class)
			.putExtra("data", mDataModelList)
			.putExtra("from","list"));
			
			}else{
				
				Toast.makeText(this, "No Business data to display", Toast.LENGTH_SHORT).show();
			}
						
			
			break;
			
		case R.id.business_IMGB_filter:
			
			mShowStatus = false;
			Log.e("", "Datd "+mData);
			startActivityForResult(
					new Intent(BusinessListByID.this,BusinessFiltertScreen.class)
					.putExtra("data", mData)
					.putExtra("category", mCategoryID)
					,FILTER);
						
			
			break;
			
		case R.id.business_IMGB_done:
			
			Intent in = new Intent();
			in.putExtra("data", mTripFeeds);
            setResult(100,in);            
			finish();			
		
			break;
			
		case R.id.business_BTN_current_location:
			
			startActivityForResult(new Intent(BusinessListByID.this,SelectCityScreen.class), SELECT_CITY);			
		
			
			break;
			
		case R.id.btn_post_ads:
			
			if(!sharedpreference.getString("userid", "0").equalsIgnoreCase("0")){
				
				startActivity(new Intent(BusinessListByID.this,AdvertiseActivity.class));
							
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
				
				refreshData();
				mData =  (ArrayList<String>) data.getExtras().getSerializable("data");			
				
				 try {
			        	getFeaturedData();
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
//					CurrentLocationData.LOCATION_OLD = CurrentLocationData.LOCATION_NEW;
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
			    	progressdialog = ProgressDialog.show(BusinessListByID.this, "","Fetching current location...", true);
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

		  new NetworkNotFoundDialog(BusinessListByID.this).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
			
			
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
				new AlertDialogMsg(BusinessListByID.this, mMsg)
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
			
			

	
	class MyAdapter extends ArrayAdapter<BusinessListModel>{
    	
    	private LayoutInflater mInflater;
    	ArrayList<BusinessListModel> mDataFeeds;
    	Context context;
    	int mainCount; 
    	
    	
    	public MyAdapter(Context context, ArrayList<BusinessListModel> mDataFeeds) {
    		
    		super(context, R.layout.business_list_row, mDataFeeds);
    		this.context=context;
        	this.mDataFeeds = mDataFeeds;        	
        
        }


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDataFeeds.size();
		}


		@Override
		public BusinessListModel getItem(int position) {
			// TODO Auto-generated method stub
			return mDataFeeds.get(position);
		}

		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			Log.v("logcat","in getview");
						 

				mInflater= LayoutInflater.from(context);
				convertView = mInflater.inflate(R.layout.business_list_row, null);

			
			RelativeLayout dataLayout = (RelativeLayout)convertView.findViewById(R.id.business_row_REL_data);
			RelativeLayout buttonsLayout = (RelativeLayout)convertView.findViewById(R.id.business_row_REL_more_buttons);
		
			Log.e("", "mPageCount "+mPageCount+" mTotalItems "+mTotalItems);
			
			int pageItemCount = 0;
			
			
			if((mPageCount*10) <= mTotalItems){
				
				pageItemCount = 10;
				
			}else{
				
				pageItemCount = mTotalItems%10;				
				
			}
			
			if( position < pageItemCount ){
			 
			 TextView mName = (TextView)convertView.findViewById(R.id.business_row_TXT_name);
			 TextView mAddress = (TextView)convertView.findViewById(R.id.business_row_TXT_address);
			 TextView mReview = (TextView)convertView.findViewById(R.id.business_row_TXT_review);
			 TextView mMiles = (TextView)convertView.findViewById(R.id.business_row_TXT_distance);
			 ImageView mRating = (ImageView)convertView.findViewById(R.id.business_row_IMG_rate);
			 	 
			 	 
			 mName.setText(mDataFeeds.get(position).getBusinessTitle());
			 
			 
			 mAddress.setText(
					 (mDataFeeds.get(position).getAddressLine1().length()>0 ? mDataFeeds.get(position).getAddressLine1()+", ":"")+
					 (mDataFeeds.get(position).getAddressLine2().length()>0 ? mDataFeeds.get(position).getAddressLine2()+", ":"")+
					 (mDataFeeds.get(position).getCityName().length()>0 ? "\n"+mDataFeeds.get(position).getCityName()+", ":"\n")+""+
					 (mDataFeeds.get(position).getStateCode().length()>0 ? mDataFeeds.get(position).getStateCode()+", ":"")+""+
					 (mDataFeeds.get(position).getAddressZipcode().length()>0 ? mDataFeeds.get(position).getAddressZipcode():""));
			 
			 if(mDataFeeds.get(position).getRevierwsCount() != null){
				 if(!mDataFeeds.get(position).getRevierwsCount().equalsIgnoreCase("0"))
					 mReview.setText(mDataFeeds.get(position).getRevierwsCount()+" Reviews");
				 else
					 mReview.setText("No Reviews");
			 }else{
				 
				 mReview.setText("No Reviews");
			 }
				 
			 mMiles.setText(mDataFeeds.get(position).getDistance()+" mi");
			 
			 try {
				 
				 Log.e("", " ****** "+mDataFeeds.get(position).getBusinessRating().toString());
				 
				 int rate = Integer.parseInt(mDataFeeds.get(position).getBusinessRating().toString());
					
					if(rate == 1)		
						mRating.setImageResource(R.drawable.icon_one_star);
					else if(rate == 2)		
						mRating.setImageResource(R.drawable.icon_two_star);
					else if(rate == 3)		
						mRating.setImageResource(R.drawable.icon_three_star);
					else if(rate == 4)		
						mRating.setImageResource(R.drawable.icon_four_star);
					else if(rate == 5)		
						mRating.setImageResource(R.drawable.icon_five_star);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			 
				
			}else{
				
				String mEndCount;

				buttonsLayout.setVisibility(View.VISIBLE);	
				dataLayout.setVisibility(View.GONE);
				Button mViewMore = (Button)convertView.findViewById(R.id.business_row_BTN_view_more);				
				Button mNext = (Button)convertView.findViewById(R.id.business_row_BTN_view_next);				
				Button mPrevious = (Button)convertView.findViewById(R.id.business_row_BTN_view_previous);
				TextView mCount = (TextView)convertView.findViewById(R.id.business_row_TXT_count);
				View mView = (View)convertView.findViewById(R.id.business_row_VIEW_line);					
				mView.setVisibility(View.VISIBLE);				
				
				if(mPageCount == 1){
					
					mViewMore.setVisibility(View.VISIBLE);
					mNext.setVisibility(View.GONE);
					mPrevious.setVisibility(View.GONE);
					
				}else{
					
					mViewMore.setVisibility(View.GONE);								
					
				}
				
				
				
				if(mPageCount != 1){
					
					if((mPageCount*10) < mTotalItems){
						
						mEndCount = ""+(mEndValue);
						mNext.setVisibility(View.VISIBLE);
						mPrevious.setVisibility(View.VISIBLE);
						
					}else{
						
						mEndCount = mTotalCount;
						mNext.setVisibility(View.GONE);
						mPrevious.setVisibility(View.VISIBLE);
					}
				}else{
					
					mEndCount = ""+(mEndValue);
				}
				
							
				
				mCount.setText("Results "+mStartValue+" - "+mEndCount+" of "+mTotalCount);
				
				
				mViewMore.setOnClickListener( new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						mStartValue = mStartValue + 10;
						mEndValue = mEndValue + 10;
						mPageCount += 1;
						
						try {
							getParserData();
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
				
				mNext.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						
						mStartValue = mStartValue + 10;
						mEndValue = mEndValue + 10;
						mPageCount += 1;
						
						try {
							getParserData();
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
				
				mPrevious.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						
						mStartValue = mStartValue - 10;
						mEndValue = mEndValue - 10 ;
						mPageCount -= 1;
						
						try {
							getParserData();
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
				
				
				
				
				
			}
			 

			return convertView;
		}
		
	}
			
		
		
	 String mBusinesName = "", mBusnessOwner = "", mSubCategory, mOrderBy = "0", mDealsOnly = "0";
    
    public void getParserData() throws MalformedURLException{
    	
    	
    	try {
        	progressdialog = ProgressDialog.show(BusinessListByID.this, "","Please wait...", true);

//        	GetBusinessdetailsByFilters?
//        			BusinesName=string&
//        			BusnessOwner=string&
//        			CategoryId=string&
//        			SubCategory=string&
//        			CityName=string&
//        			Latitude=string&
//        			Longitude=string&
//        			Range=string&
//        			startvalue=string&
//        			endvalue=string&
//        			OrderBy=string&
//        			DealsOnly=string 

        	
        	
        	String mUrl = String.format(Urls.BUSINESS_DATA_URL,
        			mData.get(0),
        			mData.get(1),
        			mData.get(2),
        			mData.get(3),
        			mData.get(4),
        			mData.get(5),
        			mData.get(6),
        			mData.get(7),
        			mStartValue,
        			mEndValue,
        			mData.get(10),
        			mData.get(11));
        	
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
				new BusinessListParser(feedUrl, handler).parser();
			
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
				mDataModelList = (ArrayList<BusinessListModel>) msg.getData().getSerializable("datafeeds");
				
				mTotalCount = mDataModelList.get(mDataModelList.size()-1).getItemsCount();		
				mTotalItems = Integer.parseInt(mTotalCount);
				
				if( mTotalItems>10){
					
					tempBusinessListModel = new BusinessListModel();
					tempBusinessListModel.setBusinessId("0");		
					mDataModelList.add(tempBusinessListModel);
					tempBusinessListModel = null;
					
				}
				
				mRelNoData.setVisibility(View.GONE);
				mRelFeaturedNoData.setVisibility(View.GONE);
				setListData();			
				

			}else if(msg.what==1){
				
				
				
				new AlertDialogMsg(BusinessListByID.this, noDataMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
					
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
				
				new AlertDialogMsg(BusinessListByID.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						BusinessListByID.this.finish();
						
					}
					
				}).create().show();
			}

		}
		
	}
		
		
		public void getFeaturedData() throws MalformedURLException{
	    	
	    	
	    	try {
	    		
	    		String mUrl = String.format(Urls.BUSINESS_FEATURED_DATA_URL,
	        			mCategoryID,
	        			CurrentLocationData.CURRENT_CITY,
	        			CurrentLocationData.LATITUDE,
	        			CurrentLocationData.LONGITUDE,
	        			mData.get(7),
	        			mStartValue,
	        			mEndValue);
	    		
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
						
//						new BuyProductsByBrandIDParser(feedUrl,handler).parser();
						new BusinessListParser(feedUrl, handler).parser();
					
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
				mRelNoData.setVisibility(View.GONE);
				mRelFeaturedNoData.setVisibility(View.GONE);
				mFeaturedDataModelList = (ArrayList<BusinessListModel>) msg.getData().getSerializable("datafeeds");
						
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
						
						mRelNoData.setVisibility(View.VISIBLE);
						mRelFeaturedNoData.setVisibility(View.VISIBLE);
										
					}
				}
			}
			
			
//			****************************			  
//			Alert Dialogs 
			
		  private void alertDialog(String msg){

			  new AlertDialogMsg(BusinessListByID.this, msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
				
				
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
				        	   
								startActivity(new Intent(BusinessListByID.this,LoginActivity.class)
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
	
	