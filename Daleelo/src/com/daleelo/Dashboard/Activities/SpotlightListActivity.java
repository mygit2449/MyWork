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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Ads.Activities.AdvertiseActivity;
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.Business.Activities.BusinessListByID;
import com.daleelo.Business.Activities.BusinessMapActivity;
import com.daleelo.Business.Activities.BusinessWriteReviewScreen;
import com.daleelo.Dashboard.Model.GetSpotLightModel;
import com.daleelo.Dashboard.Parser.SpotlightListParser;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.User.Activities.LoginActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;
import com.daleelo.helper.ImageDownloader;

public class SpotlightListActivity extends Activity implements OnClickListener{
	
	


	private ProgressDialog progressdialog;
	Intent reciverIntent;
	MyAdapter mDataAdapter;

	TextView mTitle;
	Button mLocal, mNational, mPostAds;
	ListView mSpotlightList ;
	
	ImageButton mMap;
	private boolean hasData = false;
	
	GetSpotLightModel mGetSpotLightModel;
	ArrayList<GetSpotLightModel> mDataModelList;
	
	int mType = 1;
	public SharedPreferences sharedpreference;
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.db_spotlight_list_screen);
   
        intilizationUI();
        
        try {
			getParserData();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                
    }
	
	
	private void intilizationUI() {
		// TODO Auto-generated method stub
		
		sharedpreference = getSharedPreferences("userlogin",MODE_PRIVATE);
		
		 mTitle = (TextView)findViewById(R.id.spotlight_TXT_main_title);
		 mLocal = (Button)findViewById(R.id.spotlight_BTN_local);
		 mNational= (Button)findViewById(R.id.spotlight_BTN_national);
		 mPostAds = (Button)findViewById(R.id.btn_post_ads);
		 mMap = (ImageButton)findViewById(R.id.spotlight_IMGB_map);
		 
		 mNational.setTextColor(Color.GRAY);		 
		 
		 mMap.setOnClickListener(this);
		 mLocal.setOnClickListener(this);
		 mNational.setOnClickListener(this);
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
				
				startActivity(new Intent(SpotlightListActivity.this, GlobalSearchActivity.class));
				
			}
		});	
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(SpotlightListActivity.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(SpotlightListActivity.this,MyStuffActivity.class)
				.putExtra("from", "bottom"));  
			
			}
		});
		
		
	}
	
	
	
	public void setListData(){
		
		
		mDataAdapter = new MyAdapter(this, mDataModelList);
		mSpotlightList = (ListView)findViewById(R.id.spotlight_LIST_view);
		mSpotlightList.setAdapter(mDataAdapter);
		
		
//		
		mSpotlightList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View vv, int position,long arg3) {
				
				
				
					startActivity(new Intent(SpotlightListActivity.this,SpotlightDetailDesp.class)
					.putExtra("data", mDataModelList)
					.putExtra("position", position)
					.putExtra("from", "list"));
				
				
			}
			
		});
		
	
		
	}
	
		
		
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Intent intent;
		
		switch (v.getId()) {
		
		case R.id.spotlight_BTN_local:
			
			mLocal.setBackgroundResource(R.drawable.bg_blue_strip);
			mLocal.setTextColor(Color.WHITE);
			mNational.setBackgroundResource(R.drawable.bg_light_strip);
			mNational.setTextColor(Color.GRAY);
			mTitle.setText("Local Spotlight");	
			mType = 1;
			noDataMsg = "Local spotlights not found";
			
			if(mDataAdapter != null)
				mDataAdapter.clear();
			
			try {
				getParserData();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			break;
			
			
		case R.id.spotlight_BTN_national:
			
			mNational.setBackgroundResource(R.drawable.bg_blue_strip);
			mNational.setTextColor(Color.WHITE);
			mLocal.setBackgroundResource(R.drawable.bg_light_strip);
			mLocal.setTextColor(Color.GRAY);
			mTitle.setText("National Spotlight");	
			mType = 2;
			noDataMsg = "National spotlights not found";
			
			
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
				
				startActivity(new Intent(SpotlightListActivity.this,AdvertiseActivity.class));
							
			}else{
						
				myAlertDialog();
			}		
			
			
			break;
			
		case R.id.spotlight_IMGB_map:
			
			
			if(hasData){
				
				startActivity(new Intent(SpotlightListActivity.this,SpotLightMapActivity.class)
				.putExtra("data", mDataModelList)
				.putExtra("from","list"));
				
				}else{
					
					Toast.makeText(this, "No Spotlights to display", Toast.LENGTH_SHORT).show();
				}
			
			break;
		
		}
		
		
	}
	

	
	class MyAdapter extends ArrayAdapter<GetSpotLightModel>{
    	
    	private LayoutInflater mInflater;
    	ArrayList<GetSpotLightModel> mDataFeeds;
    	Context context;
    	
    	public MyAdapter(Context context, ArrayList<GetSpotLightModel> mDataFeeds) {
    		
    		super(context, R.layout.db_spotlight_list_row, mDataFeeds);
    		this.context=context;
        	this.mDataFeeds = mDataFeeds;
          
        }


		@Override
		public GetSpotLightModel getItem(int position) {
			// TODO Auto-generated method stub
			return mDataFeeds.get(position);
		}

		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
						 
			 LayoutInflater mInflater= LayoutInflater.from(context);
			 
			 convertView = mInflater.inflate(R.layout.db_spotlight_list_row, null);
			 
			 TextView mName = (TextView)convertView.findViewById(R.id.spotlight_row_TXT_name);
			 TextView mDesp = (TextView)convertView.findViewById(R.id.spotlight_row_TXT_desp);
			 TextView mDis = (TextView)convertView.findViewById(R.id.spotlight_row_TXT_distance);
			 ImageView mImg = (ImageView)convertView.findViewById(R.id.spotlight_row_IMG);
			 	 
			 mName.setText(mDataFeeds.get(position).getSpotLightName());
			 mDesp.setText(mDataFeeds.get(position).getBusinessTitle());
			 mDis.setText(mDataFeeds.get(position).getDistance()+" mi");
			 
			 if(mType == 1)
				 mDis.setText(mDataFeeds.get(position).getDistance()+" mi");
			 else
				 mDis.setVisibility(View.INVISIBLE);
			 
			 if(mDataFeeds.get(position).getImageUrl().length()>0){
				 new ImageDownloader().download(String.format(Urls.SL_IMG_URL,mDataFeeds.get(position).getImageUrl()), mImg);
			 }
			
			return convertView;
		}
		
	}
			
		
		
	 
    
    public void getParserData() throws MalformedURLException{
    	
    	
    	try {
        	progressdialog = ProgressDialog.show(SpotlightListActivity.this, "","Please wait...", true);
//        	GetSpotLightItemsByCity?CityName=string&Type=string&startvalue=string&endvalue=string
        	String mCity = URLEncoder.encode(CurrentLocationData.CURRENT_CITY, "UTF-8");
        	String mUrl = String.format(
        			Urls.SPOTLIGHT_BY_CITY_URL, 
        			mCity,
        			mType,
        			CurrentLocationData.LATITUDE,
        			CurrentLocationData.LONGITUDE,
        			Utils.RANGE);
        	
 
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
				new SpotlightListParser(feedUrl, handler).parser();
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			super.run();
		}
		
	}
    
    
    String noDataMsg = "Local spotlights not found";
    
	class FeedParserHandler extends Handler {
		public void handleMessage(android.os.Message msg) {
			
			progressdialog.dismiss();
			
			
			
			if(msg.what==0)
			{	
				hasData = true;
				mDataModelList = null;
				mDataModelList = (ArrayList<GetSpotLightModel>) msg.getData().getSerializable("datafeeds");
				Log.e("", "sptname "+mDataModelList.get(0).getSpotLightName());
				setListData();			
				

			}else if(msg.what==1){
				
				hasData = false;
				
				new AlertDialogMsg(SpotlightListActivity.this, noDataMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
					}
					
				}).create().show();
				
				
			}else if(msg.what==2){				
				
				hasData = false;
				
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(SpotlightListActivity.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

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
	
	private void myAlertDialog(){
		
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Daleelo");
		builder.setMessage("Please login to reserve your Spotlilght")
		       .setCancelable(false)
		       .setPositiveButton("Login", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
						startActivity(new Intent(SpotlightListActivity.this,LoginActivity.class)
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
	
	