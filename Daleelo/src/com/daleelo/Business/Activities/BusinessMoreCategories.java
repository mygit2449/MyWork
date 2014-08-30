package com.daleelo.Business.Activities;

import java.net.MalformedURLException;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.Business.Model.BusinessMoreCategoriesModel;
import com.daleelo.Business.Parser.BusinessMoreCatParser;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.Utilities.Urls;
import com.daleelo.helper.ImageDownloader;

public class BusinessMoreCategories extends Activity{

	private ProgressDialog progressdialog;
	Intent reciverIntent;
	MyAdapter mDataAdapter;

	TextView mTitle;
	ListView mCategoriesList ;
	Button mLocation;
	Thread mThread;
	ProgressDialog progressDialog;
	
	BusinessMoreCategoriesModel mBusinessMoreCategoriesModel;
	ArrayList<BusinessMoreCategoriesModel> mDataModelList;

	int mType = 1;
	
	final int FROM_TRIP = 1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.business_more_cat_screen);   
        intilizationUI();       
        
        
        try {
			getParserData();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
    }
	
	private void intilizationUI() {
		
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
				
				startActivity(new Intent(BusinessMoreCategories.this, GlobalSearchActivity.class));
				
			}
		});		
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(BusinessMoreCategories.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(BusinessMoreCategories.this,MyStuffActivity.class)
				.putExtra("from", "bottom"));
			
			}
		});
	}
		
	
	public void setListData(){
		
		
		mDataAdapter = new MyAdapter(this, mDataModelList);
		mCategoriesList = (ListView)findViewById(R.id.more_cat_LIST_view);
		mCategoriesList.setAdapter(mDataAdapter);
		
		
		mCategoriesList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View vv, int position,long arg3) {
				
				
				
				
				if(getIntent().getExtras().getString("from").equalsIgnoreCase("trip")){					
					
					startActivityForResult(new Intent(BusinessMoreCategories.this,BusinessListByID.class)
					.putExtra("categoryId", mDataModelList.get(position).getCategoryId())
					.putExtra("categoryName", mDataModelList.get(position).getCategoryName())
					.putExtra("from", "trip"), FROM_TRIP);
				
				}else{
				
					startActivity(new Intent(BusinessMoreCategories.this,BusinessListByID.class)
					.putExtra("categoryId", mDataModelList.get(position).getCategoryId())
					.putExtra("categoryName", mDataModelList.get(position).getCategoryName())
					.putExtra("from", "none"));
				
				}
				
			}
			
		});		
	
		
	}
	
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == FROM_TRIP) {
			
			if (resultCode == 100) {				
				
				setResult(RESULT_OK,data);			
				finish();
				
			}
		}	
	}




	class MyAdapter extends ArrayAdapter<BusinessMoreCategoriesModel>{
    	
    	private LayoutInflater mInflater;
    	ArrayList<BusinessMoreCategoriesModel> mDataFeeds;
    	Context context;
    	
    	public MyAdapter(Context context, ArrayList<BusinessMoreCategoriesModel> mDataFeeds) {
    		
    		super(context, R.layout.business_more_cat_list_row, mDataFeeds);
    		this.context=context;
        	this.mDataFeeds = mDataFeeds;
          
        }


		@Override
		public BusinessMoreCategoriesModel getItem(int position) {
			// TODO Auto-generated method stub
			return mDataFeeds.get(position);
		}


		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
						 
			 mInflater= LayoutInflater.from(context);
			 
			 convertView = mInflater.inflate(R.layout.business_more_cat_list_row, null);
			 
			 TextView mName = (TextView)convertView.findViewById(R.id.more_cat_row_TXT_name);
			 ImageView mImage  = (ImageView)convertView.findViewById(R.id.more_cat_row_IMG_item);
			 	 
			 mName.setText(mDataFeeds.get(position).getCategoryName());
			 new ImageDownloader().download(String.format(Urls.CAT_IMG_URL,mDataFeeds.get(position).getImageurl2()), mImage);
			 

			return convertView;
		}		
	}	
	
	
	
	 
    public void getParserData() throws MalformedURLException{
    	
    	
    	try {
        	progressdialog = ProgressDialog.show(BusinessMoreCategories.this, "","Please wait...", true);
        	String mUrl = Urls.HOME_PAGE_MORE_CATEGORIES_URL ;
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
					
					new BusinessMoreCatParser(feedUrl, handler).parser();
				
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
					mDataModelList = (ArrayList<BusinessMoreCategoriesModel>) msg.getData().getSerializable("datafeeds");
					setListData();			
					

				}else if(msg.what==1){
					
					
					
					new AlertDialogMsg(BusinessMoreCategories.this, noDataMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							
						}
						
					}).create().show();
					
					
				}else if(msg.what==2){
					
					String mmsg = "Error connecting network or server not responding please try again..";
					
					new AlertDialogMsg(BusinessMoreCategories.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
//							MainHomeScreen.this.finish();
							
						}
						
					}).create().show();
				}

			}
		}

}
	
	