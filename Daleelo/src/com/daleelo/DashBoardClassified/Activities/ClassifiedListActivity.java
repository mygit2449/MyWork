package com.daleelo.DashBoardClassified.Activities;

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
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.DashBoardClassified.Model.GetClassifiedModel;
import com.daleelo.DashBoardClassified.Parser.GetClassifiedParser;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.More.Activities.TermsAndConditionsActivity;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.User.Activities.LoginActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.helper.ImageDownloader;

public class ClassifiedListActivity extends Activity implements OnClickListener{
	
	

	private ProgressDialog progressdialog;
	Intent reciverIntent;
	MyAdapter mDataAdapter;

	TextView mTitle;
	ListView mClassifiedList ;
	
	GetClassifiedModel mGetClassifiedModel, mGetClassifiedModelTemp;
	ArrayList<GetClassifiedModel> mDataModelList;
	
	Button mPost, mLogin;
	
	int mType = 1;
	
	 public SharedPreferences sharedpreference;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.db_classified_list_screen);
   
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
		
		 mTitle = (TextView)findViewById(R.id.classified_TXT_main_title);
		 mPost = (Button)findViewById(R.id.classified_BTN_post);
		 mLogin = (Button)findViewById(R.id.classified_BTN_login);
		 
		 mPost.setOnClickListener(this);
		 mLogin.setOnClickListener(this);
		 
		 mGetClassifiedModelTemp = new GetClassifiedModel();
		 mGetClassifiedModelTemp.setCategoryId("0");
		 mGetClassifiedModelTemp.setCategoryName("Terms & Conditions");		
			
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
					
					startActivity(new Intent(ClassifiedListActivity.this, GlobalSearchActivity.class));
					
				}
			});		
			mHome.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					startActivity(new Intent(ClassifiedListActivity.this,MainHomeScreen.class)
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
					finish();
					
				}
			});
			
			mMyStuff.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					startActivity(new Intent(ClassifiedListActivity.this,MyStuffActivity.class)
					.putExtra("from", "bottom"));  
				
				}
			});
			
			
		}
	
	
	
	public void setListData(){
		
		sharedpreference = getSharedPreferences("userlogin",MODE_PRIVATE);
		
		mDataAdapter = new MyAdapter(this, mDataModelList);
		mClassifiedList = (ListView)findViewById(R.id.classified_LIST_view);
		mClassifiedList.setAdapter(mDataAdapter);
		mClassifiedList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View vv, int position,long arg3) {
					
				if(!mDataModelList.get(position).getCategoryId().equalsIgnoreCase("0")){
				
					startActivity(new Intent(ClassifiedListActivity.this,ClassifiedByCategoryId.class)
					.putExtra("section", mDataModelList.get(position).getCategoryId())
					.putExtra("title", mDataModelList.get(position).getCategoryName()));
					
				}else{
					
					startActivity(new Intent(ClassifiedListActivity.this,TermsAndConditionsActivity.class));
				}				
			}			
		});			
	}
	
		
		
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Intent intent;
		
		switch (v.getId()) {
		
		case R.id.classified_BTN_post:
			
			
			
			if(!sharedpreference.getString("userid", "0").equalsIgnoreCase("0")){
				
//				Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
			
				intent = new Intent(ClassifiedListActivity.this, MyClassifiedListActivity.class);
				intent.putExtra("from", "classified");
				startActivity(intent);
			
			}else{
				
				myAlertDialog();
			}
			
			break;
			
			
		case R.id.classified_BTN_login:
			
			intent = new Intent(ClassifiedListActivity.this, LoginActivity.class);
			intent.putExtra("from", "classified");
			startActivity(intent);
			
			break;
		
		
		
		}
		
		
	}
	
	
	
	private void myAlertDialog(){
		
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Daleelo");
		builder.setMessage("Please login to post your classified")
		       .setCancelable(false)
		       .setPositiveButton("Login", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
						startActivity(new Intent(ClassifiedListActivity.this,LoginActivity.class)
						.putExtra("from", "classified"));
		               
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
	

	
	class MyAdapter extends ArrayAdapter<GetClassifiedModel>{
    	
    	private LayoutInflater mInflater;
    	ArrayList<GetClassifiedModel> mDataFeeds;
    	Context context;
    	
    	public MyAdapter(Context context, ArrayList<GetClassifiedModel> mDataFeeds) {
    		
    		super(context, R.layout.db_classified_list_row, mDataFeeds);
    		this.context=context;
        	this.mDataFeeds = mDataFeeds;
          
        }


		@Override
		public GetClassifiedModel getItem(int position) {
			// TODO Auto-generated method stub
			return mDataFeeds.get(position);
		}

		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
						 
			 LayoutInflater mInflater= LayoutInflater.from(context);
			 
			 convertView = mInflater.inflate(R.layout.db_classified_list_row, null);
			 
			 TextView mName = (TextView)convertView.findViewById(R.id.classified_row_TXT_name);
			 ImageView mImg = (ImageView)convertView.findViewById(R.id.classified_row_IMG);
			 	 
			 mName.setText(mDataFeeds.get(position).getCategoryName());
			 
			 if(mDataFeeds.get(position).getImageurl().length()>0)
				 new ImageDownloader().download(String.format(Urls.CAT_IMG_URL,mDataFeeds.get(position).getImageurl()), mImg);
			 
			 if(mDataFeeds.get(position).getCategoryId().equalsIgnoreCase("0")){
				 
				 mImg.setImageResource(R.drawable.img_icon_terms_conditions);
			 }
			
			return convertView;
		}
		
	}
			
		
		
	 
    
    public void getParserData() throws MalformedURLException{
    	
    	
    	try {
        	progressdialog = ProgressDialog.show(ClassifiedListActivity.this, "","Please wait...", true);
//        	GetSpotLightItemsByCity?CityName=string&Type=string&startvalue=string&endvalue=string
        	String mCity = URLEncoder.encode(CurrentLocationData.CURRENT_CITY, "UTF-8");
        	String mUrl = Urls.BASE_URL+"GetClassifiedCategories";
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
				new GetClassifiedParser(feedUrl, handler).parser();
			
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
				mDataModelList = null;
				mDataModelList = (ArrayList<GetClassifiedModel>) msg.getData().getSerializable("datafeeds");
				mDataModelList.add(mGetClassifiedModelTemp);
				Log.e("", "sptname "+mDataModelList.get(0).getCategoryName());
				setListData();			
				

			}else if(msg.what==1){
				
				
				mDataModelList = null;
				mDataModelList = new ArrayList<GetClassifiedModel>();
				mDataModelList.add(mGetClassifiedModelTemp);
				Log.e("", "sptname "+mDataModelList.get(0).getCategoryName());
				setListData();			
				
				new AlertDialogMsg(ClassifiedListActivity.this, noDataMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
					}
					
				}).create().show();
				
				
			}else if(msg.what==2){
				
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(ClassifiedListActivity.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
//						MainHomeScreen.this.finish();
						
					}
					
				}).create().show();
			}

		}
	}
}
	
	