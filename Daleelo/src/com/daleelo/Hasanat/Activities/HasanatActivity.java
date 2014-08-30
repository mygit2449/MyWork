package com.daleelo.Hasanat.Activities;

import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import com.daleelo.R;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Hasanat.Model.BannerModel;
import com.daleelo.Hasanat.Model.BusinessDetailModel;
import com.daleelo.Hasanat.Model.CategoryModel;
import com.daleelo.Hasanat.Parser.BannerParser;
import com.daleelo.Hasanat.Parser.FeaturedMosqueParser;
import com.daleelo.Hasanat.Parser.HasanatCategoryParser;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.Main.Activities.SelectCityScreen;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;
import com.daleelo.helper.ImageDownloader;

public class HasanatActivity extends Activity implements OnClickListener{
	
	
	
	private static final int SELECT_CITY = 10;
	private String mHasanat_ListItems[] = {"Zakat", "Sadaqa", "Fundraising Calendar" , "Organizations"};
	private int mHasanat_ListIcons[] = {R.drawable.hasanat_categories, R.drawable.hasanat_sadaqa, R.drawable.hasanat_fundraisingcalender , R.drawable.hasanat_organizations};
	private ListView hasanat_list;
	private HasanatListAdapter mHasanatAdapter;
	private ProgressDialog progressdialog;
	
	private ArrayList<BusinessDetailModel> mFeaturedDataList;
	private ArrayList<CategoryModel> mCategoryList = null;
	private CategoryModel mCategoryModel ;
	
	private RelativeLayout mFeaturedLayout;
	private ImageView mFeatured_Phone_img;
	private TextView mtxt_Featured_Phone, mtxt_FeaturedAddress, mtxt_Featured_Name;
	
	private Button btn_Location;
	private ImageView mHasanatBanner;
	
	private String mCategoryId = "";
	private int count = 0;
	
	private boolean category_found = false;
	private boolean show = true, First_Time = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hasanat_list);
		initializeUI();
		
		try {
			
			getParserData();
			getBannerData();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Utils.mZakatDueAmount = 0;
		mHasanatAdapter = new HasanatListAdapter(this, mHasanat_ListItems);
		hasanat_list.setAdapter(mHasanatAdapter);
		
		hasanat_list.setOnItemClickListener(new OnItemClickListener() {
			

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				switch (arg2) {
				case 0:
					startActivity(new Intent(HasanatActivity.this, ZakatActivity.class));
					break;
				
				case 1:
					startActivity(new Intent(HasanatActivity.this, SadakaActivity.class));
					break;
				
				case 2:
					startActivity(new Intent(HasanatActivity.this, FundRaisingCalendar.class));
					break;
					
				case 3:
					startActivity(new Intent(HasanatActivity.this, OrganizationActivity.class));
					break;
					
				default:
					break;
				}
				
			}
		});
		
	}
	
	private void initializeUI() {
		
		hasanat_list = (ListView)findViewById(R.id.hasanat_LIST_view);
		
		mFeaturedLayout = (RelativeLayout)findViewById(R.id.hasanat_features_REL_data);
		mFeaturedLayout.setVisibility(View.GONE);
		mtxt_Featured_Name = (TextView)findViewById(R.id.hasanat_features_TXT_name);
		mtxt_FeaturedAddress = (TextView)findViewById(R.id.hasanat_features_TXT_address);
		mtxt_Featured_Phone = (TextView)findViewById(R.id.hasanat_features_TXT_phone);
		mFeatured_Phone_img = (ImageView)findViewById(R.id.hasanat_features_IMG_phone);
		
		mHasanatBanner = (ImageView)findViewById(R.id.hasanat_IMG_Banner);
		btn_Location = (Button)findViewById(R.id.hasanat_BTN_current_location);
		if(CurrentLocationData.GET_LOCATION)
			btn_Location.setText(CurrentLocationData.ADDRESS_LINE);
		
		btn_Location.setOnClickListener(this);
		mHasanatBanner.setOnClickListener(this);
		
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
				
				startActivity(new Intent(HasanatActivity.this, GlobalSearchActivity.class));
				
			}
		});		
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(HasanatActivity.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(HasanatActivity.this,MyStuffActivity.class)
				.putExtra("from", "bottom"));  
			
			}
		});		
		
	}
	

	public class HasanatListAdapter extends ArrayAdapter<String>{

		Context context;
		String[] data;
		public HasanatListAdapter(Context context,  String[] mHasanat_ListItems) {
			super(context, R.layout.hasanat_listrow,  mHasanat_ListItems);
			this.context = context;
			data = mHasanat_ListItems;
		}
		
		@Override
		public int getCount() {
			return mHasanat_ListItems.length;
		}

		@Override
		public View getView(int count, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.hasanat_listrow, null);
			TextView txt_icon = (TextView)convertView.findViewById(R.id.hasanat_row_TXT_name);
			ImageView img_icon = (ImageView)convertView.findViewById(R.id.hasanat_row_IMG_image);
			txt_icon.setText(data[count]);
			img_icon.setImageResource(mHasanat_ListIcons[count]);
			return convertView;
		}
		
		
		
	}
	
	
	
	 public void setParseData(){
			
			
	    	if(mFeaturedDataList == null){
	    		show = false;
	    		mFeaturedLayout.setVisibility(View.GONE);
	    	}else{
	    		mFeaturedLayout.setVisibility(View.VISIBLE);
	    		show = true;
	    		showFeaturedData();
	    		
	    		mFeaturedLayout.setOnClickListener(new OnClickListener() {
	    			
	    			@Override
	    			public void onClick(View v) {
	    				
	    				if(count > 0 ){
	    					startActivity(new Intent(HasanatActivity.this,BusinessDetailsDescActivity.class).putExtra("data", mFeaturedDataList.get(count - 1)));
	    				}
	    				
	    			}
	    		});
	    	}
	    	
			
		}
	    
	 
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
	
			
				if (requestCode == SELECT_CITY) {
					
					if (resultCode == 100) {
						
						if(!CurrentLocationData.LOCATION_OLD.equalsIgnoreCase(CurrentLocationData.LOCATION_NEW)){
							
							try {
								getFeaturedData();
							} catch (MalformedURLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}						
						}
					}
				}
			}
		
//	********* Parsed Data
	 
	public void getParserData() throws MalformedURLException{
    	
    	
    	try {
        	progressdialog = ProgressDialog.show(HasanatActivity.this, "","Please wait...", true);
    		new mAsyncFeedFetcher(Urls.HASANAT_CATEGORY_URL, new FeedParserHandler()).start();
			
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
				
				new HasanatCategoryParser(feedUrl, handler).parser();				
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			super.run();
		}
		
	}
    
    String noDataMsg = "No data";
   
	class FeedParserHandler extends Handler {
		

		public void handleMessage(android.os.Message msg) {
			
			progressdialog.dismiss();
			
			if(msg.what==0)
			{	
				
				mCategoryList = (ArrayList<CategoryModel>)msg.getData().getSerializable("datafeeds");

				if(mCategoryList != null){
					mCategoryId = mCategoryList.get(0).getCategoryMasterId();
					try {
						getFeaturedData();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				

			}else if(msg.what==1){
				
			
				
			}else if(msg.what==2){
				
				
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(HasanatActivity.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
					}
					
				}).create().show();
			}

		}
	}
	
	
//	*********************
//	Featured data
	
	
	public void getFeaturedData() throws MalformedURLException{
    	
    	
    	try {
        	
        	String city = URLEncoder.encode(CurrentLocationData.CURRENT_CITY,"UTF-8");
			String mUrl = String.format(Urls.FEATURED_MOSQUE_URL,mCategoryId,city,CurrentLocationData.LATITUDE,CurrentLocationData.LONGITUDE,25,1,10);
			Log.e("", " ************mUrl "+ mUrl);
			new mAsyncFeaturedFetcher(mUrl, new FeaturedParserHandler()).start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
					Log.e("", "************ Featured Data Found");
				mFeaturedDataList = null;
				mFeaturedDataList = (ArrayList<BusinessDetailModel>) msg.getData().getSerializable("datafeeds");
				setParseData();		

			}else if(msg.what==1){
				
				mFeaturedDataList = null;
				setParseData();

				
			}else if(msg.what==2){
				
//				
//				String mmsg = "Error connecting network or server not responding please try again..";
//				
//				new AlertDialogMsg(HasanatActivity.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						
//						
//					}
//					
//				}).create().show();
			}

		}
	}
	
//	*********************
	
	
	
	
	public void getBannerData() throws MalformedURLException{
    	
    	
    	try {
    		
    		new mAsyncBannerFetcher(Urls.GET_HASANATH_BANNERS, new BannerParserHandler()).start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }
    
    class mAsyncBannerFetcher extends Thread
	{
		String  feedUrl;
		Handler handler;
		
		public mAsyncBannerFetcher(String mUrl, Handler handler) throws MalformedURLException {
		
			
			Log.i("", "mUrl********* "+mUrl);			
			
			this.feedUrl = mUrl;			
			this.handler = handler;
		
		} 
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try
			{
				
				new BannerParser(feedUrl, handler).parser();				
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			super.run();
		}
		
	}
    
	ArrayList<BannerModel> mBannerFeeds = null;
	class BannerParserHandler extends Handler {
		

		public void handleMessage(android.os.Message msg) {
			
			
			if(msg.what==0){
				
				mBannerFeeds = (ArrayList<BannerModel>) msg.getData().getSerializable("datafeeds");
				
				if(mBannerFeeds != null){
					mHasanatBanner.setVisibility(View.VISIBLE);
					if(mBannerFeeds.get(0).getBannerImageUrl().length()>0){
						
						new ImageDownloader().download(String.format(Urls.HASANAT_BANNER_IMG_URL,mBannerFeeds.get(0).getBannerImageUrl()), mHasanatBanner);
						
					}
				
				}
				
				Log.e("", "******* Banner Found");
			}else{
				
				Log.e("", "******* Banner Not Found");
				
			}
		}
	}
	
//	************ OnClick
	
	

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
						
			case R.id.hasanat_BTN_current_location:
				mBtnSelectCity = true;
				startActivityForResult(new Intent(HasanatActivity.this,SelectCityScreen.class), SELECT_CITY);
				break;
				
			case R.id.hasanat_IMG_Banner:
				
				if(mBannerFeeds!=null){
					
					if(mBannerFeeds.get(0).getDonationUrl().length()>5){
						String url;
						
						if(mBannerFeeds.get(0).getDonationUrl().toString().startsWith("http"))
							url = mBannerFeeds.get(0).getDonationUrl();
						else
							url = "http://"+mBannerFeeds.get(0).getDonationUrl();
						
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(url));
						startActivity(i);
						
					}else{
						
						startActivity(new Intent(HasanatActivity.this, HasanatDonateActivity.class));

					}					
					
				}else{
					
					startActivity(new Intent(HasanatActivity.this, HasanatDonateActivity.class));
				}
				break;
				
			default:
				break;
		}
		
	}
	

	
	
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
		
		if(mFeaturedDataList != null){
		
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
				
//				if(mFeaturedDataList.get(count).getBusinessAddress().length() > 0){
//					mtxt_FeaturedAddress.setText(mFeaturedDataList.get(count).getBusinessAddress());
//				}else{
//					mtxt_FeaturedAddress.setText("");
//				}
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
				
//				if(mFeaturedDataList.get(count).getBusinessAddress().length() > 0){
//					mtxt_FeaturedAddress.setText(mFeaturedDataList.get(count).getBusinessAddress());
//				}else{
//					mtxt_FeaturedAddress.setText("");
//				}
				
				mtxt_FeaturedAddress.setText((mFeaturedDataList.get(count).getAddressLine1().length()>0 ? mFeaturedDataList.get(count).getAddressLine1()+", ":"")+
						 (mFeaturedDataList.get(count).getAddressLine2().length()>0 ? mFeaturedDataList.get(count).getAddressLine2()+", ":"")+
						 (mFeaturedDataList.get(count).getCityName().length()>0 ? mFeaturedDataList.get(count).getCityName()+", ":"")+""+
						 (mFeaturedDataList.get(count).getStateCode().length()>0 ? mFeaturedDataList.get(count).getStateCode()+", ":"")+""+
						 (mFeaturedDataList.get(count).getAddressZipcode().length()>0 ? mFeaturedDataList.get(count).getAddressZipcode():""));
				
				
				count++;
			}
		}
	}
	
	private boolean mBtnSelectCity = true;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		 if(!mBtnSelectCity){
			 
			

			if(!btn_Location.getText().toString().equalsIgnoreCase(CurrentLocationData.ADDRESS_LINE)){
				btn_Location.setText(CurrentLocationData.ADDRESS_LINE);
				
				try {
					getFeaturedData();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
		}		
		 
		 mBtnSelectCity = false;
	}
	
	
}
