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
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Hasanat.Activities.HasanatActivity.BannerParserHandler;
import com.daleelo.Hasanat.Activities.HasanatActivity.mAsyncBannerFetcher;
import com.daleelo.Hasanat.Model.BannerModel;
import com.daleelo.Hasanat.Model.BusinessDetailModel;
import com.daleelo.Hasanat.Model.CategoryModel;
import com.daleelo.Hasanat.Parser.BannerParser;
import com.daleelo.Hasanat.Parser.FeaturedMosqueParser;
import com.daleelo.Hasanat.Parser.HasanatCategoryParser;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.Main.Activities.SelectCityScreen;
import com.daleelo.More.Activities.FrequentlyAskedQuestions;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.helper.ImageDownloader;

public class ZakatActivity extends Activity implements OnClickListener{
	
	
	
	
	private static final int SELECT_CITY = 10;
	private String mZakat_ListItems[] = {"Learn", "FAQ", "Calculator" , "Give"};
	private int mZakat_ListIcons[] = {R.drawable.zakat_learn, R.drawable.zakat_faq, R.drawable.zakat_calculator , R.drawable.zakat_give};
	private ListView zakat_list;
	private ProgressDialog progressdialog;
	
	private ZakatListAdapter mZakatAdapter;
	private ArrayList<BusinessDetailModel> mFeaturedDataList;
	private ArrayList<CategoryModel> mCategoryList;
	private CategoryModel mCategoryModel ;
	
	private RelativeLayout mFeaturedLayout;
	private ImageView mFeatured_Phone_img;
	private TextView mtxt_Featured_Phone, mtxt_FeaturedAddress, mtxt_Featured_Name, zakat_txt_main_title;
	private Button  btn_Location;
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
			getBannerData();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mZakatAdapter = new ZakatListAdapter(this, mZakat_ListItems);
		zakat_list.setAdapter(mZakatAdapter);
		
		zakat_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				switch (arg2) {
				case 0:
					
					show = false;
					startActivity(new Intent(ZakatActivity.this, ZakatLearnView.class));
					break;
				
				case 1:
					
					show = false;
					startActivity(new Intent(ZakatActivity.this, ZakatFAQActivity.class));
					break;
				
				case 2:
					show = false;
					startActivity(new Intent(ZakatActivity.this, ZakatCalculatorActivity.class).putExtra("featuredData", mFeaturedDataList).putExtra("categoryId", mCategoryId));
					break;
					
				case 3:
					show = false;
					startActivity(new Intent(ZakatActivity.this, GiveActivity.class).putExtra("featuredData", mFeaturedDataList).putExtra("categoryId", mCategoryId));
					break;
					
				default:
					
					break;
				}
				
			}
		});
		
	}
	
	private void initializeUI() {
		
		zakat_list = (ListView)findViewById(R.id.hasanat_LIST_view);
		zakat_txt_main_title = (TextView)findViewById(R.id.hasanat_TXT_main_title);
		zakat_txt_main_title.setText("Zakat");
		
		mFeaturedLayout = (RelativeLayout)findViewById(R.id.hasanat_features_REL_data);
		mFeaturedLayout.setVisibility(View.GONE);
		mtxt_Featured_Name = (TextView)findViewById(R.id.hasanat_features_TXT_name);
		mtxt_FeaturedAddress = (TextView)findViewById(R.id.hasanat_features_TXT_address);
		mtxt_Featured_Phone = (TextView)findViewById(R.id.hasanat_features_TXT_phone);
		mFeatured_Phone_img = (ImageView)findViewById(R.id.hasanat_features_IMG_phone);
		
		btn_Location = (Button)findViewById(R.id.hasanat_BTN_current_location);

		mHasanatBanner = (ImageView)findViewById(R.id.hasanat_IMG_Banner);
		
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
				
				startActivity(new Intent(ZakatActivity.this, GlobalSearchActivity.class));
				
			}
		});	
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(ZakatActivity.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(ZakatActivity.this,MyStuffActivity.class)
				.putExtra("from", "bottom"));  
			
			}
		 });
		
		
	}

	public class ZakatListAdapter extends ArrayAdapter<String>{

		Context context;
		String[] data;
		public ZakatListAdapter(Context context,  String[] mHasanat_ListItems) {
			super(context, R.layout.hasanat_listrow,  mHasanat_ListItems);
			this.context = context;
			data = mHasanat_ListItems;
		}
		
		@Override
		public int getCount() {
			return mZakat_ListItems.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.hasanat_listrow, null);
			TextView txt_icon = (TextView)convertView.findViewById(R.id.hasanat_row_TXT_name);
			ImageView img_icon = (ImageView)convertView.findViewById(R.id.hasanat_row_IMG_image);
			txt_icon.setText(data[position]);
			img_icon.setImageResource(mZakat_ListIcons[position]);
			return convertView;
		}
		
		
		
	}
	
	
	  public void setParseData(){
			
			
	    	if(mFeaturedDataList == null){
	    		mFeaturedLayout.setVisibility(View.GONE);
	    		show = false;
	    	}else{
	    		mFeaturedLayout.setVisibility(View.VISIBLE);
	    		show = true;
	    		showFeaturedData();
	    		
	    		mFeaturedLayout.setOnClickListener(new OnClickListener() {
	    			
	    			@Override 
	    			public void onClick(View v) {
	    				
	    				if(count > 0 ){
	    					show = false;
	    					startActivity(new Intent(ZakatActivity.this,BusinessDetailsDescActivity.class).putExtra("data", mFeaturedDataList.get(count - 1)));
	    				}
	    				
	    			}
	    		});
	    	}
	    	
			
		}
	    
	  
//	********** ParsedData
	
    public void getParserData() throws MalformedURLException{
    	
    	
    	try {
        	progressdialog = ProgressDialog.show(ZakatActivity.this, "","Please wait...", true);
    		new mAsyncFeedFetcher(Urls.ZAKAT_CATEGORY_URL, new FeedParserHandler()).start();
			
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
    
    String noDataMsg = "No data";
   
	class FeedParserHandler extends Handler {
		

		public void handleMessage(android.os.Message msg) {
			
			progressdialog.dismiss();
			
			if(msg.what==0)
			{	
				
				if(!category_found){
					category_found = true;
					mCategoryList = (ArrayList<CategoryModel>)msg.getData().getSerializable("datafeeds");
					//check
					mCategoryId = mCategoryList.get(0).getCategoryMasterId();
					
					String city;
					try {
						city = URLEncoder.encode(CurrentLocationData.CURRENT_CITY,"UTF-8");
						String mUrl = String.format(Urls.FEATURED_MOSQUE_URL,mCategoryId,city,CurrentLocationData.LATITUDE,CurrentLocationData.LONGITUDE,25,1,10);
						new mAsyncFeedFetcher(mUrl, new FeedParserHandler()).start();
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					
//					progressdialog.dismiss();
					mFeaturedDataList = null;
					mFeaturedDataList = (ArrayList<BusinessDetailModel>) msg.getData().getSerializable("datafeeds");
					setParseData();		
				}

			}else if(msg.what==1){
				
//				progressdialog.dismiss();
				mFeaturedDataList = null;
				setParseData();

				
			}else if(msg.what==2){
				
//				progressdialog.dismiss();
				
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(ZakatActivity.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
//						MainHomeScreen.this.finish();
						
					}
					
				}).create().show();
			}

		}
	}
	
	public void getBannerData() throws MalformedURLException{
    	
    	
    	try {
    		
    		new mAsyncBannerFetcher(Urls.GET_ZAKAT_BANNERS, new BannerParserHandler()).start();
			
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
			
			progressdialog.dismiss();
			
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
	
	
//	*********** Onclilck
	


	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
							
			case R.id.hasanat_BTN_current_location:
				show = false;
				startActivityForResult(new Intent(ZakatActivity.this,SelectCityScreen.class),SELECT_CITY);
				break;
				
			case R.id.hasanat_IMG_Banner:
				show = false;
				
				if(mBannerFeeds!=null){
					
					if(mBannerFeeds.get(0).getDonationUrl().length()>5){
						
						String url;
						
						if(mBannerFeeds.get(0).getDonationUrl().toString().startsWith("http"))
							url = mBannerFeeds.get(0).getDonationUrl();
						else
							url = "http://"+mBannerFeeds.get(0).getDonationUrl();
						
//						String url = "http://"+mBannerFeeds.get(0).getDonationUrl();
						
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(url));
						startActivity(i);
						
					}else{
						
						startActivity(new Intent(ZakatActivity.this, HasanatDonateActivity.class));

					}					
					
				}else{
					
					startActivity(new Intent(ZakatActivity.this, HasanatDonateActivity.class));
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
			
			/*if(mFeaturedDataList.get(count).getBusinessAddress().length() > 0){
				mtxt_FeaturedAddress.setText(mFeaturedDataList.get(count).getBusinessAddress());
			}else{
				mtxt_FeaturedAddress.setText("");
			}*/
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
			
			/*if(mFeaturedDataList.get(count).getBusinessAddress().length() > 0){
				mtxt_FeaturedAddress.setText(mFeaturedDataList.get(count).getBusinessAddress());
			}else{
				mtxt_FeaturedAddress.setText("");
			}*/
			mtxt_FeaturedAddress.setText((mFeaturedDataList.get(count).getAddressLine1().length()>0 ? mFeaturedDataList.get(count).getAddressLine1()+", ":"")+
					 (mFeaturedDataList.get(count).getAddressLine2().length()>0 ? mFeaturedDataList.get(count).getAddressLine2()+", ":"")+
					 (mFeaturedDataList.get(count).getCityName().length()>0 ? mFeaturedDataList.get(count).getCityName()+", ":"")+""+
					 (mFeaturedDataList.get(count).getStateCode().length()>0 ? mFeaturedDataList.get(count).getStateCode()+", ":"")+""+
					 (mFeaturedDataList.get(count).getAddressZipcode().length()>0 ? mFeaturedDataList.get(count).getAddressZipcode():""));
			
			
			count++;
		}
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!btn_Location.getText().toString().equalsIgnoreCase(CurrentLocationData.ADDRESS_LINE)){
			btn_Location.setText(CurrentLocationData.ADDRESS_LINE);
			category_found = false;
			
			try {
				getParserData();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		if(First_Time){
			First_Time = false;
			category_found = false;
			
			try {
				getParserData();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	

}
