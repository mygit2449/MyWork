package com.daleelo.Hasanat.Activities;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.Dialog.NetworkNotFoundDialog;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Hasanat.Model.BusinessDetailModel;
import com.daleelo.Hasanat.Model.CategoryModel;
import com.daleelo.Hasanat.Parser.FeaturedMosqueParser;
import com.daleelo.Hasanat.Parser.HasanatCategoryParser;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.Main.Activities.SelectCityScreen;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;
import com.daleelo.location.helper.CurrentLocation;
import com.daleelo.location.helper.LocationHelper;
import com.daleelo.location.helper.NetworkConnectionHelper;

public class OrganizationActivity extends Activity implements OnClickListener, CurrentLocation{
	
	
	
	private static final int SELECT_CITY = 10;
	private final int REQUEST_FOR_FILTER = 5;
	
	private ProgressDialog progressdialog;
	
	private ArrayList<BusinessDetailModel> mFeaturedDataList;
	private ArrayList<BusinessDetailModel> mFilteredDataList;
	private ArrayList<CategoryModel> mCategoryList;
	private CategoryModel mCategoryModel ;
	
	private RelativeLayout mFeaturedLayout;
	private ImageView mFeatured_Phone_img;
	private TextView mtxt_Featured_Phone, mtxt_FeaturedAddress, mtxt_Featured_Name, organization_txt_main_title;
	private Button  btn_Location;
	private ImageButton btn_filter, btn_map;
	private ListView organization_list;
	
	private String mCategoryId = "";
	private int count =0;
	
	private boolean category_found = false, category_found_second = false;;
	private boolean show = true;

	private OrganizationListAdapter mOrganizationListAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.give_list);
		initializeUI();
		
		
		
		try {
			
			getCategory();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mFeaturedLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mFeaturedDataList != null && count != 0){
					startActivity(new Intent(OrganizationActivity.this,BusinessDetailsDescActivity.class).putExtra("data", mFeaturedDataList.get(count-1)));
				}

			}
		});
		
	}
	
	private void initializeUI() {
		
		organization_list = (ListView)findViewById(R.id.give_LIST_view);
		organization_txt_main_title = (TextView)findViewById(R.id.give_TXT_main_title);
		organization_txt_main_title.setText("Organization");
		mFeaturedLayout = (RelativeLayout)findViewById(R.id.give_features_REL_data);
		mFeaturedLayout.setVisibility(View.GONE);
		mtxt_Featured_Name = (TextView)findViewById(R.id.give_features_TXT_name);
		mtxt_FeaturedAddress = (TextView)findViewById(R.id.give_features_TXT_address);
		mtxt_Featured_Phone = (TextView)findViewById(R.id.give_features_TXT_phone);
		mFeatured_Phone_img = (ImageView)findViewById(R.id.give_features_IMG_phone);
		
		btn_Location = (Button)findViewById(R.id.give_BTN_current_location);
		btn_filter = (ImageButton)findViewById(R.id.give_IMGB_filter);
		btn_map = (ImageButton)findViewById(R.id.give_IMGB_map);
		
		btn_Location.setText(CurrentLocationData.ADDRESS_LINE);
		
		btn_Location.setOnClickListener(this);
		btn_filter.setOnClickListener(this);
		btn_map.setOnClickListener(this);
		setBottomBar();
		
	}
	
	private ImageButton mHome, mMyStuff;
	private ArrayList<String> mData;
	private EditText mSearch;
	
	private void setBottomBar(){
		
		
		mHome = (ImageButton)findViewById(R.id.business_IMGB_home);
		mMyStuff = (ImageButton)findViewById(R.id.business_IMGB_mystuff);
		mSearch = (EditText)findViewById(R.id.business_TXT_serach);
		
		mSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(OrganizationActivity.this, GlobalSearchActivity.class));
				
			}
		});	
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(OrganizationActivity.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(OrganizationActivity.this,MyStuffActivity.class)
				.putExtra("from", "bottom"));  
			
			}
		});
		
		
	}

	
	public void setData(){		
		try {
			
			mData = new ArrayList<String>();
			mData.add(mCategoryId);
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


	public class OrganizationListAdapter extends ArrayAdapter<String>{

		Context context;
		ArrayList<BusinessDetailModel> feeds;
		public OrganizationListAdapter(Context context,  ArrayList<BusinessDetailModel> mFilteredDataList) {
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
	
	
    public void getCategory() throws MalformedURLException{
    	
    	
    	try {
        	progressdialog = ProgressDialog.show(OrganizationActivity.this, "","Please wait...", true);
    		new mAsyncFeedFetcher(Urls.ORGANIZATIONS_CATEGORY_URL, new FeedParserHandler()).start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }
    
    
    private void getParsedData(){
	
    	if(category_found_second)
    		progressdialog = ProgressDialog.show(OrganizationActivity.this, "","Please wait...", true);
    	
    	category_found_second = true;
    	
    	try {
    	
		String mUrl = String.format(
				Urls.MOSQUE_BY_FILTER_URL, 
				mData.get(0),
				URLEncoder.encode(mData.get(1),"UTF-8"),
				mData.get(2),
				mData.get(3),
				mData.get(4),
				mData.get(5),
				mData.get(6), 
				mData.get(7));
		
    	
			new mAsyncFeedFetcher(mUrl, new FeedParserHandler()).start();
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
    
    public void getFeaturedData(){
    	
    	
    	try {
    		String city;
			try {
				
				city = URLEncoder.encode(CurrentLocationData.CURRENT_CITY,"UTF-8");
				String mUrl = String.format(
						Urls.FEATURED_MOSQUE_URL,
						mCategoryId,
						city,
						CurrentLocationData.LATITUDE,
						CurrentLocationData.LONGITUDE
						,Utils.RANGE
						,1
						,100000);
				
				new mAsyncFeaturedFetcher(mUrl, new FeaturedParserHandler()).start();
				
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }
    
    
    public void setParseData(){
		
    	mOrganizationListAdapter = new OrganizationListAdapter(this, mFilteredDataList);
		organization_list.setAdapter(mOrganizationListAdapter);
		organization_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				startActivity(new Intent(OrganizationActivity.this,GiveDetailDescActivity.class).putExtra("pos", arg2).putExtra("data", mFilteredDataList));
				//startActivity(new Intent(OrganizationActivity.this,BusinessDetailsDescActivity.class).putExtra("data", mFilteredDataList.get(arg2)));
				
			}
		});
		
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
				
				if(!category_found)
					new HasanatCategoryParser(feedUrl, handler).parser();
				else
					new FeaturedMosqueParser(feedUrl, handler).parser();
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			super.run();
		}
		
	}
    
    
   
    String noDataMsg = "No Organizations found";
   
	class FeedParserHandler extends Handler {
		

		public void handleMessage(android.os.Message msg) {
			
//			progressdialog.dismiss();
			
			if(msg.what==0)
			{	
				
				if(!category_found){
					category_found = true;
					mCategoryList = (ArrayList<CategoryModel>)msg.getData().getSerializable("datafeeds");					
					
					mCategoryId = mCategoryList.get(0).getCategoryMasterId();
					
					setData();
					getParsedData();
					getFeaturedData();
					
					
				}else{
					
					
					progressdialog.dismiss();
					mFilteredDataList = null;
					mFilteredDataList = (ArrayList<BusinessDetailModel>) msg.getData().getSerializable("datafeeds");
					
					setParseData();	
				}

			}else if(msg.what==1){
				
				progressdialog.dismiss();
				new AlertDialogMsg(OrganizationActivity.this, noDataMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
					}
					
				}).create().show();
				
			}else if(msg.what==2){
				
				progressdialog.dismiss();
				
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(OrganizationActivity.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
//						MainHomeScreen.this.finish();
						
					}
					
				}).create().show();
			}

		}
	}
	
	
	class mAsyncFeaturedFetcher extends Thread
	{
		String  feedUrl;
		Handler handler;
		
		public mAsyncFeaturedFetcher(String mUrl, Handler handler) throws MalformedURLException {
		
			
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
	
	
	class FeaturedParserHandler extends Handler {
		

		public void handleMessage(android.os.Message msg) {
			
			
			if(msg.what==0)
			{	
				mFeaturedDataList = null;
				mFeaturedDataList = (ArrayList<BusinessDetailModel>) msg.getData().getSerializable("datafeeds");
				mFeaturedLayout.setVisibility(View.VISIBLE);
				show = true;
				showFeaturedData();		
				
				

			}else if(msg.what==1){
				
				mFeaturedDataList = null;
				mFeaturedLayout.setVisibility(View.GONE);
				show = false;
				showFeaturedData();
				
			}else if(msg.what==2){
				
				
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(OrganizationActivity.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
//						MainHomeScreen.this.finish();
						
					}
					
				}).create().show();
			}

		}
	}


	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
							
			case R.id.give_BTN_current_location:
				
				startActivityForResult(
						new Intent(OrganizationActivity.this,SelectCityScreen.class)
						,SELECT_CITY);
				break;
			
			case R.id.give_IMGB_map:
				
				if(mFilteredDataList != null){
					
					startActivity(new Intent(OrganizationActivity.this, HasanatMapActivity.class)
					.putExtra("data", mFilteredDataList)
					.putExtra("from","list"));
					
				}else{
					
					Toast.makeText(OrganizationActivity.this, "No data to display", Toast.LENGTH_LONG).show();
				}
				
				break;
				
			case R.id.give_IMGB_filter:
				
				if(mCategoryList != null){
					
					startActivityForResult(new Intent(OrganizationActivity.this, OrganizationFilterActivity.class)
					.putExtra("category", mCategoryList)
					.putExtra("data", mData)
					,REQUEST_FOR_FILTER);
				}
				
				break;
				
			default:
				break;
		}
	}
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == REQUEST_FOR_FILTER){
			if(resultCode == RESULT_OK){
				if(data != null){
					
					if(mOrganizationListAdapter != null){
						mOrganizationListAdapter.clear();
						mFilteredDataList.clear();
					}
					
					organization_txt_main_title.setText(data.getExtras().get("selectedCategorynames").toString());
					
					mData =  (ArrayList<String>) data.getExtras().getSerializable("data");		
					
					getParsedData();
					
				}
			}
				
		}
		
		if (requestCode == SELECT_CITY) {
			
			if (resultCode == 100) {
				
				if(!CurrentLocationData.LOCATION_OLD.equalsIgnoreCase(CurrentLocationData.LOCATION_NEW)){
										
					if(mOrganizationListAdapter != null){
						mOrganizationListAdapter.clear();
						mFilteredDataList.clear();
					}				
					setData();

					if(CurrentLocationData.LOCATION_NEW.equalsIgnoreCase("Current Location")){
						Log.e("","current   "+CurrentLocationData.LOCATION_NEW);
						
						if(!CurrentLocationData.IS_CURRENT_LOCATION){
							
							getCurrentLocation();
							
						}else{
							
							btn_Location.setText(CurrentLocationData.ADDRESS_LINE);
							getParsedData();
							getFeaturedData();
						}
						
					}else{
						
						btn_Location.setText(CurrentLocationData.ADDRESS_LINE);
						getParsedData();
						getFeaturedData();
						
					}
					
				}
				


			}
		}
		
	}
	
	
	
	LocationHelper mLocationHelper;
	Thread myThread;
	
	private void getCurrentLocation(){
		
		if(NetworkConnectionHelper.checkNetworkAvailability()){
			
	   		    if(!CurrentLocationData.IS_CURRENT_LOCATION){
	   		    	
	   		    	btn_Location.setText("Fetching Location");
			    	progressdialog = ProgressDialog.show(OrganizationActivity.this, "","Fetching current location...", true);
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
			
				btn_Location.setText("Location not found");
				networkNotFound();
			
		}
		
	}
	
	 private void networkNotFound(){

		  new NetworkNotFoundDialog(OrganizationActivity.this).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
			
			
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
				btn_Location.setText("Location not found");

				String mMsg = "Unable to get current location please select the location manually";
				try{
				new AlertDialogMsg(OrganizationActivity.this, mMsg)
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
						btn_Location.setText(CurrentLocationData.ADDRESS_LINE);
						
						getParsedData();
						getFeaturedData();
					
					}else{
						
						btn_Location.setText("Location not found");
						progressdialog.dismiss();
//						locationNotFound();
					}
						
				}else{

					btn_Location.setText("Location not found");
					progressdialog.dismiss();
//					locationNotFound();
				}
			}
			
			
			
//			****************************	
			
			

	

    	
	

	private void showFeaturedData(){
		
		final Thread showthread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while(show){
					try {
						showFeaturedDataHandler.sendEmptyMessage(5);
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		showthread.start();
		
	}
	
	
	Handler showFeaturedDataHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == 5){
				changeFeaturedData();
			}
				
		}
		
		
	};
	
	private void changeFeaturedData(){
		
		if(count < mFeaturedDataList.size()){
		
			if(mFeaturedDataList.get(count).getAddressPhone().length() > 0){
				mFeatured_Phone_img.setVisibility(View.VISIBLE);
				mtxt_Featured_Phone.setVisibility(View.VISIBLE);
				mtxt_Featured_Phone.setText(mFeaturedDataList.get(count).getAddressPhone());
			}else{
				mFeatured_Phone_img.setVisibility(View.GONE);
				mtxt_Featured_Phone.setVisibility(View.GONE);
			}
			
			if(mFeaturedDataList.get(count).getBusinessTitle().length() > 0){
				mtxt_Featured_Name.setText(mFeaturedDataList.get(count).getBusinessTitle());
			}else{
				mtxt_Featured_Name.setText("");				
			}
			mtxt_FeaturedAddress.setText((mFeaturedDataList.get(count).getAddressLine1().length()>0 ? mFeaturedDataList.get(count).getAddressLine1()+", ":"")+
					 (mFeaturedDataList.get(count).getAddressLine2().length()>0 ? mFeaturedDataList.get(count).getAddressLine2()+", ":"")+
					 (mFeaturedDataList.get(count).getCityName().length()>0 ? mFeaturedDataList.get(count).getCityName()+", ":"")+""+
					 (mFeaturedDataList.get(count).getStateCode().length()>0 ? mFeaturedDataList.get(count).getStateCode()+", ":"")+""+
					 (mFeaturedDataList.get(count).getAddressZipcode().length()>0 ? mFeaturedDataList.get(count).getAddressZipcode():""));
			
			
			count++;
		}else{
			count = 0;
			
			if(mFeaturedDataList.get(count).getAddressPhone().length() > 0){
				mFeatured_Phone_img.setVisibility(View.VISIBLE);
				mtxt_Featured_Phone.setVisibility(View.VISIBLE);
				mtxt_Featured_Phone.setText(mFeaturedDataList.get(count).getAddressPhone());
			}else{
				mFeatured_Phone_img.setVisibility(View.GONE);
				mtxt_Featured_Phone.setVisibility(View.GONE);
			}
			
			if(mFeaturedDataList.get(count).getBusinessTitle().length() > 0){
				mtxt_Featured_Name.setText(mFeaturedDataList.get(count).getBusinessTitle());
			}else{
				mtxt_Featured_Name.setText("");
			}
						
				mtxt_FeaturedAddress.setText((mFeaturedDataList.get(count).getAddressLine1().length()>0 ? mFeaturedDataList.get(count).getAddressLine1()+", ":"")+
						 (mFeaturedDataList.get(count).getAddressLine2().length()>0 ? mFeaturedDataList.get(count).getAddressLine2()+", ":"")+
						 (mFeaturedDataList.get(count).getCityName().length()>0 ? mFeaturedDataList.get(count).getCityName()+", ":"")+""+
						 (mFeaturedDataList.get(count).getStateCode().length()>0 ? mFeaturedDataList.get(count).getStateCode()+", ":"")+""+
						 (mFeaturedDataList.get(count).getAddressZipcode().length()>0 ? mFeaturedDataList.get(count).getAddressZipcode():""));
				
			
			
			count++;
		}
	}

}
