package com.daleelo.Masjid.Activities;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.Dialog.NetworkNotFoundDialog;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.Main.Activities.SelectCityScreen;
import com.daleelo.Masjid.Model.MasjidModel;
import com.daleelo.Masjid.Parser.MasjidLocationParser;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;
import com.daleelo.location.helper.CurrentLocation;
import com.daleelo.location.helper.LocationHelper;
import com.daleelo.location.helper.NetworkConnectionHelper;

public class MasjidListActivity extends Activity implements OnClickListener, CurrentLocation{
	
	


	private ProgressDialog progressdialog;
	Intent reciverIntent;
	MyAdapter mDataAdapter;

	RelativeLayout mRelFeaturedData;
	TextView mTitle, mFeatureItemName, mFeaturedItemAddress, mFeaturedItemPhone;
	ListView mBusinessItemList ;
	Button mLocation;
	
	String mCategoryID;
	
	MasjidModel mMasjidLocationModel;
	ArrayList<MasjidModel> mDataModelList;
	
	int mType = 1;
	
	private Button mFilter, mMap;
	ArrayList<String> mData;
	ProgressDialog progressDialog; 
	Thread myThread;
    public SharedPreferences sharedpreference;
    boolean DATA_CHANGE = false;
    DecimalFormat desimalFormat = new DecimalFormat("##0.0");
	
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.masjid_listview);
   
        intilizationUI();
        
    }
	
	
	Thread mThread;

	private void intilizationUI() {
		// TODO Auto-generated method stub
		
		reciverIntent = getIntent();
		sharedpreference= getSharedPreferences("masjid",MODE_PRIVATE);		
		mDataModelList = (ArrayList<MasjidModel>) reciverIntent.getExtras().getSerializable("data");
		
		mTitle = (TextView)findViewById(R.id.masjidlist_TXT_main_title);	
		mLocation  = (Button)findViewById(R.id.masjidlist_BTN_current_location);
		mFilter = (Button)findViewById(R.id.masjidlist_BTN_filter);
		mMap = (Button)findViewById(R.id.masjidlist_BTN_map);
		
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
				
				startActivity(new Intent(MasjidListActivity.this, GlobalSearchActivity.class));
				
			}
		});	
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(MasjidListActivity.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(MasjidListActivity.this,MyStuffActivity.class)
				.putExtra("from", "bottom"));  
			
			}
		});
		
		
	}
	
	
	public void setData(){
		
		mLocation.setText(CurrentLocationData.ADDRESS_LINE);
		setListData();	
	
	}
	

	
//	************************
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		
		switch (v.getId()) {
		
		
	case R.id.masjidlist_BTN_map:
		Log.e("", "****************************DATA_CHANGE YES");
		
		if(DATA_CHANGE){
			
			Intent in = new Intent();
			in.putExtra("data", mDataModelList);
			setResult(103,in);   
			Log.e("", "DATA_CHANGE YES");
		
		}else{
			
			Intent in = new Intent();
			setResult(104,in);  
			Log.e("", "DATA_CHANGE NO");
			
		}
			finish();
						
			
			break;
		case R.id.masjidlist_BTN_filter:
			
			
			Log.e("", "Datd "+mData);
			startActivityForResult(new Intent(MasjidListActivity.this,MasjidFilterActivity.class),FILTER);
						
			
			break;
			
		case R.id.masjidlist_BTN_current_location:
			
			startActivityForResult(new Intent(MasjidListActivity.this,SelectCityScreen.class), SELECT_CITY);			
		
			
			break;
			
		}
		
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();		
		
		if(DATA_CHANGE){
			
			Intent in = new Intent();
			in.putExtra("data", mDataModelList);
			setResult(103,in);   
			Log.e("", "DATA_CHANGE YES");
		
		}else{
			
			Intent in = new Intent();
			setResult(104,in);  
			Log.e("", "DATA_CHANGE NO");
			
		}
		
		finish();
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
				
				 try {
					 	
						getFilterrData();
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
			}
		
		if (requestCode == SELECT_CITY) {
			
			if (resultCode == 100) {
				
				if(!CurrentLocationData.LOCATION_OLD.equalsIgnoreCase(CurrentLocationData.LOCATION_NEW)){
					
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
	
			
		
		
	 String mBusinesName = "", mBusnessOwner = "", mSubCategory, mOrderBy = "0", mDealsOnly = "0";
    
    public void getParserData() throws MalformedURLException{    	
    	
    	try {	
    		DATA_CHANGE = true;
    		mDataModelList.clear();
    		
    		progressDialog = ProgressDialog.show(MasjidListActivity.this, "","Loading please wait...", true,false); 
				
			MasjidLocationParser mUserAuth = new MasjidLocationParser(String.format(
						Urls.MOSQUE_BY_CATEGORY_URL
						,"146",CurrentLocationData.CURRENT_CITY
						,CurrentLocationData.LATITUDE
						,CurrentLocationData.LONGITUDE
						,Utils.RANGE,"1","10000"), mainHandler, mDataModelList);		
			
			mUserAuth.start();
		
									
						} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
 
    public void getFilterrData() throws MalformedURLException{    	
    	
    	try {	
    		DATA_CHANGE = true;
    		mDataModelList = null;    		
    		mDataModelList = new ArrayList<MasjidModel>();
    		
    		 progressDialog = ProgressDialog.show(MasjidListActivity.this, "","Loading please wait...", true,false); 
				
    		 MasjidLocationParser mUserAuth = new MasjidLocationParser(String.format(
						Urls.MOSQUE_BY_FILTER_URL								
						,"146","0","0"
						,sharedpreference.getString("location", "")
						,sharedpreference.getString("lat", "")
						,sharedpreference.getString("long", "")
						,sharedpreference.getString("range", "")
						,sharedpreference.getString("sort", "")), mainHandler, mDataModelList);				
			
			mUserAuth.start();
		
									
						} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    String noDataMsg = "Business data not found";
    
    public Handler mainHandler = new Handler() 
    {
		public void handleMessage(android.os.Message msg) 
		{
			
			progressDialog.dismiss();
			
			String handleErrMsg = "";
			
			handleErrMsg = msg.getData().getString("httpError");
			
			Log.e("", "handleErrMsg "+handleErrMsg);
			
			if(handleErrMsg.equalsIgnoreCase("")){
				
				Log.e("", "handleErr list "+handleErrMsg);
				
				setListData();
				
					
			}else{
				
				
				if(mDataAdapter != null)
					mDataAdapter.clear();
				
				new AlertDialogMsg(MasjidListActivity.this, handleErrMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
						
					}
					
				}).create().show();
				
			}				
			
		}
	};	
	
	
	
	
	public void setListData(){
		
		if(mDataAdapter != null)
			mDataAdapter.clear();
		
		mDataAdapter = new MyAdapter(this, mDataModelList);
		mBusinessItemList = (ListView)findViewById(R.id.masjidList_listview);
		mBusinessItemList.setAdapter(mDataAdapter);		
	
		
		mBusinessItemList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View vv, int position,long arg3) {
				
				
				startActivity(new Intent(MasjidListActivity.this,MasjidDetailDescription.class)
				.putExtra("data", mDataModelList)
				.putExtra("position", position)
				.putExtra("from", "list"));
			}
			
		});
		
	
		
	}
	
	
	
	class MyAdapter extends ArrayAdapter<MasjidModel>{
    	
    	private LayoutInflater mInflater;
    	ArrayList<MasjidModel> mDataFeeds;
    	Context context;
    	int mainCount; 
    	
    	
    	public MyAdapter(Context context, ArrayList<MasjidModel> mDataFeeds) {
    		
    		super(context, R.layout.masjid_listrow, mDataFeeds);
    		this.context=context;
        	this.mDataFeeds = mDataFeeds;        	
        	
          
        }


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDataFeeds.size();
		}


		@Override
		public MasjidModel getItem(int position) {
			// TODO Auto-generated method stub
			return mDataFeeds.get(position);
		}

		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			Log.v("logcat","in getview");
						 
			mInflater= LayoutInflater.from(context);
				convertView = mInflater.inflate(R.layout.masjid_listrow, null);

			Log.e("", "mainCount "+mainCount+" position "+position);
			
			 
			 TextView mName = (TextView)convertView.findViewById(R.id.masjidlisting_TXT_dallasislamic_center);
			 TextView mAddress = (TextView)convertView.findViewById(R.id.masjidlisting_TXT_masjid_address);
			 TextView mMiles = (TextView)convertView.findViewById(R.id.masjidlisting_TXT_masjid_distance);
			 TextView mPhone = (TextView)convertView.findViewById(R.id.masjidlisting_TXT_phone);
			 	 
			 	 
			 mName.setText(mDataFeeds.get(position).getBusinessTitle());
			 
			 
			 mAddress.setText(
					 (mDataFeeds.get(position).getAddressLine1().length()>0 ? mDataFeeds.get(position).getAddressLine1()+", ":"")+
					 (mDataFeeds.get(position).getAddressLine2().length()>0 ? mDataFeeds.get(position).getAddressLine2()+", ":"")+
					 (mDataFeeds.get(position).getCityName().length()>0 ? mDataFeeds.get(position).getCityName()+", ":"")+""+
					 (mDataFeeds.get(position).getStateCode().length()>0 ? mDataFeeds.get(position).getStateCode()+", ":"")+""+
					 (mDataFeeds.get(position).getAddressZipcode().length()>0 ? mDataFeeds.get(position).getAddressZipcode():""));
			 
			
			 
			 double tempStr = Double.parseDouble(mDataFeeds.get(position).getDistance());
		    	
		    	if(tempStr != 0.0)	                            	
		    		 mMiles.setText(desimalFormat.format(tempStr)+" mi");	                            	
		    	else
		    		 mMiles.setVisibility(View.INVISIBLE);
			 
			 
			 mPhone.setText(mDataFeeds.get(position).getAddressPhone());
			 
			
			return convertView;
		}
		
	}
	    
	
	
	
	
	
//	****************************	
	
//	GetCurrentLocation	




LocationHelper mLocationHelper;

private void getCurrentLocation(){

if(NetworkConnectionHelper.checkNetworkAvailability()){
	
		    if(!CurrentLocationData.IS_CURRENT_LOCATION){
		    	
   		    mLocation.setText("Fetching Location");
	    	progressdialog = ProgressDialog.show(MasjidListActivity.this, "","Fetching current location...", true);
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

  new NetworkNotFoundDialog(MasjidListActivity.this).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
	
	
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
		new AlertDialogMsg(MasjidListActivity.this, mMsg)
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


	 
	    
	    
	   
}
	
	