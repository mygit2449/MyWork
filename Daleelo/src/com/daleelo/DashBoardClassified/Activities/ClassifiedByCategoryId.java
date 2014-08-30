package com.daleelo.DashBoardClassified.Activities;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.DashBoardClassified.Model.GetClassifiedItemsModel;
import com.daleelo.DashBoardClassified.Parser.GetClassifiedItemsParser;
import com.daleelo.Dashboard.Activities.DealsListActivity;
import com.daleelo.Dashboard.Activities.SpotlightDetailDesp;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.Dialog.NetworkNotFoundDialog;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.Main.Activities.SelectCityScreen;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;
import com.daleelo.location.helper.CurrentLocation;
import com.daleelo.location.helper.LocationHelper;
import com.daleelo.location.helper.NetworkConnectionHelper;

public class ClassifiedByCategoryId extends Activity implements OnClickListener, CurrentLocation{
	
	

	private ProgressDialog progressdialog;
	Intent reciverIntent;
	MyAdapter mDataAdapter;

	TextView mTitle;
	ListView mClassifiedList ;	

	Button mLocation, mFilter;
	
	GetClassifiedItemsModel mGetClassifiedByIdModel, mGetClassifiedByIdModelTemp;
	ArrayList<GetClassifiedItemsModel> mDataModelList;
	ArrayList<String> mData;
	
	DecimalFormat desimalFormat = new DecimalFormat("##0.00");
	
	int mType = 1;
	
	private String  mClassifiedSection = "0";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.db_classified_list_by_id_screen);
   
        intilizationUI();
        
        try {
			getParserData();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                
    }
	
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
		
		
	}




	private void intilizationUI() {
		// TODO Auto-generated method stub
		
		mData = new ArrayList<String>();
		mTitle = (TextView)findViewById(R.id.classified_by_id_TXT_main_title);
		mClassifiedSection = getIntent().getExtras().getString("section");
		mTitle.setText(getIntent().getExtras().getString("title"));
		mLocation  = (Button)findViewById(R.id.classified_by_id_BTN_current_location);
		mFilter = (Button)findViewById(R.id.classified_by_id_BTN_filter);
		
		mLocation.setText(CurrentLocationData.ADDRESS_LINE);
		
		mFilter.setOnClickListener(this);
		mLocation.setOnClickListener(this);
	
		String mCity = "";
		
		try {
			mCity = URLEncoder.encode(CurrentLocationData.CURRENT_CITY, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mData.add("1");//0
		mData.add(mClassifiedSection);//1
		mData.add("0");//2
		mData.add("1");//3
		mData.add(mCity);//4
		mData.add(CurrentLocationData.LATITUDE);//5
		mData.add(CurrentLocationData.LONGITUDE);//6
		mData.add(Utils.RANGE);//7
		mData.add("0");//8
		mData.add("0");//9
		mData.add("0");//10
		
		
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
				
				startActivity(new Intent(ClassifiedByCategoryId.this, GlobalSearchActivity.class));
				
			}
		});	
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(ClassifiedByCategoryId.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(ClassifiedByCategoryId.this,MyStuffActivity.class)
				.putExtra("from", "bottom"));  
			
			}
		});
		
		
	}
	
	
	
	public void setListData(){		
		
		mDataAdapter = new MyAdapter(this, mDataModelList);
		mClassifiedList = (ListView)findViewById(R.id.classified_by_id_LIST_view);
		mClassifiedList.setAdapter(mDataAdapter);
		
		
		mClassifiedList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View vv, int position,long arg3) {
								
				
					startActivity(new Intent(ClassifiedByCategoryId.this,ClassifiedItemDetailDesp.class)
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
		
		case R.id.classified_by_id_BTN_filter:
			
			startActivityForResult(new Intent(ClassifiedByCategoryId.this,ClassifiedFilterActivity.class)
			.putExtra("data", mData)
			.putExtra("title", getIntent().getExtras().getString("title"))
			,FILTER);
			
			break;
			
			
		case R.id.classified_by_id_BTN_current_location:
			
			startActivityForResult(new Intent(ClassifiedByCategoryId.this,SelectCityScreen.class), SELECT_CITY);			
		
			
			break;
		
		}	
		
	}
	

	
	class MyAdapter extends ArrayAdapter<GetClassifiedItemsModel>{
    	
    	private LayoutInflater mInflater;
    	ArrayList<GetClassifiedItemsModel> mDataFeeds;
    	Context context;
    	
    	public MyAdapter(Context context, ArrayList<GetClassifiedItemsModel> mDataFeeds) {
    		
    		super(context, R.layout.db_classified_by_id_list_row, mDataFeeds);
    		this.context=context;
        	this.mDataFeeds = mDataFeeds;
          
        }


		@Override
		public GetClassifiedItemsModel getItem(int position) {
			// TODO Auto-generated method stub
			return mDataFeeds.get(position);
		}

		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
						 
			 LayoutInflater mInflater= LayoutInflater.from(context);
			 
			 convertView = mInflater.inflate(R.layout.db_classified_by_id_list_row, null);
			 
			 TextView mPrice = (TextView)convertView.findViewById(R.id.classified_by_id_row_TXT_price);
			 TextView mName = (TextView)convertView.findViewById(R.id.classified_by_id_row_TXT_name);
			 TextView mDistace = (TextView)convertView.findViewById(R.id.classified_by_id_row_TXT_distance);
			 
			 String mPriceStr;
			 double tempStr = Double.parseDouble(mDataFeeds.get(position).getPrice());
			 
			 if(mDataFeeds.get(position).getOBO().equalsIgnoreCase("True"))				 
				 mPriceStr = desimalFormat.format(tempStr)+" OBO";
			 else
				 mPriceStr = desimalFormat.format(tempStr);
			
			 mPrice.setText(mPriceStr);
			 mName.setText("- "+mDataFeeds.get(position).getClassifiedTitle());
			 mDistace.setText(mDataFeeds.get(position).getDistance()+" mi");
			 			
			return convertView;
			
		}
		
	}
			
		
		
	 
    
    public void getParserData() throws MalformedURLException{
    	
    	
    	try {
    		
    		if(mDataAdapter != null)
    			mDataAdapter.clear();
    		
        	progressdialog = ProgressDialog.show(ClassifiedByCategoryId.this, "","Please wait...", true);
        	
        	String mCity = URLEncoder.encode(CurrentLocationData.CURRENT_CITY, "UTF-8");
        	
        	String mUrl = String.format(
        			Urls.CLASSIFIEDS_BY_CITY_URL, 
        			mClassifiedSection,
        			mCity,
        			CurrentLocationData.LATITUDE,
        			CurrentLocationData.LONGITUDE,
        			Utils.RANGE,
        			"1",
        			"10000");
        	

        	
    		new mAsyncClassifiedsFeedFetcher(mUrl, new FeedParserHandler()).start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }
    
    
    
    
    public void getFilterrData() throws MalformedURLException{
    	
    	
    	try {
    		
    		if(mDataAdapter != null)
    			mDataAdapter.clear();
    		
        	progressdialog = ProgressDialog.show(ClassifiedByCategoryId.this, "","Please wait...", true);
        	
        	String mUrl = String.format(
        			Urls.CLASSIFIEDS_BY_FILTER_URL, 
        			mData.get(0),
        			mData.get(1),
        			mData.get(2),
        			mData.get(3),
        			mData.get(4),
        			mData.get(5),
        			mData.get(6),
        			mData.get(7),
        			mData.get(8),
        			mData.get(9));
//        	
//        	String mUrl = Urls.BASE_URL+"ClasifiedbyFilters?" +
//        			"Sortfor="++"&" +
//        			"ClassifiedSection="+mData.get(1)+"&" +
//        			"ClassifiedCategory="+mData.get(2)+"&" +
//        			"Natinaltype="+mData.get(3)+"&" +
//        			"CityName="+mData.get(4)+"&" +
//        			"latitude="+mData.get(5)+"&" +
//        			"longitude="+mData.get(6)+"&" +
//        			"Range="+mData.get(7)+"&" +
//        			"PriceMin="+mData.get(8)+"&" +
//        			"PriceMax="+mData.get(9);
        	
        	
    		new mAsyncClassifiedsFeedFetcher(mUrl, new FeedParserHandler()).start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }
    
    class mAsyncClassifiedsFeedFetcher extends Thread
	{
		String  feedUrl;
		Handler handler;
		
		public mAsyncClassifiedsFeedFetcher(String mUrl, Handler handler) throws MalformedURLException {
		
			
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
				new GetClassifiedItemsParser(feedUrl, handler).parser();
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			super.run();
		}
		
	}
    
    
    String noDataMsg = "Classified data not found";
    
	class FeedParserHandler extends Handler {
		public void handleMessage(android.os.Message msg) {
			
			progressdialog.dismiss();
			
			
			
			if(msg.what==0)
			{	
				mDataModelList = null;
				mDataModelList = (ArrayList<GetClassifiedItemsModel>) msg.getData().getSerializable("datafeeds");
				Log.e("", "sptname "+mDataModelList.get(0).getCategoryName());
				setListData();			
				

			}else if(msg.what==1){
				
				
				
				new AlertDialogMsg(ClassifiedByCategoryId.this, noDataMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
					}
					
				}).create().show();
				
				
			}else if(msg.what==2){
				
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(ClassifiedByCategoryId.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
//						MainHomeScreen.this.finish();
						
					}
					
				}).create().show();
			}

		}
	}
	
	
	
	
//  ****************************
//  OnActivityResult
	
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
					 	
					 	mData = (ArrayList<String>) data.getExtras().getSerializable("data");
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
	
	
	
  
//  ****************************
	
//	****************************	
	
//	GetCurrentLocation	

LocationHelper mLocationHelper;
Thread myThread;

private void getCurrentLocation(){

if(NetworkConnectionHelper.checkNetworkAvailability()){
	
		    if(!CurrentLocationData.IS_CURRENT_LOCATION){
		    	
   		    mLocation.setText("Fetching Location");
	    	progressdialog = ProgressDialog.show(ClassifiedByCategoryId.this, "","Fetching current location...", true);
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

  new NetworkNotFoundDialog(ClassifiedByCategoryId.this).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
	
	
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
		new AlertDialogMsg(ClassifiedByCategoryId.this, mMsg)
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
	
	