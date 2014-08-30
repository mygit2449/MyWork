package com.daleelo.DashBoardClassified.Activities;

import java.net.MalformedURLException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Dashboard.Model.GetHomePageCategoriesModel;
import com.daleelo.Main.Parser.GetHomePageCategoriesParser;
import com.daleelo.Utilities.Urls;

public class ClassifiedFilterActivity extends Activity{
	
	RadioGroup mSortBy;
	RelativeLayout mRelPrice, mRelCategory;
	RadioButton mSortType, mRBLocal, mRBNational;
	SeekBar mDistance;
	EditText mETPriceFrom, mETPriceTo;
	Spinner mSpinCategory, mSpinSort;
	Button mDone;
	TextView mSearchTitle;

	String[] strCategory, strSort;	
	String mSubCategoryId, mCategoryId, searchTitle;
	ArrayList<String> mData;
    public SharedPreferences sharedpreference;
	
	String mLocName = "", mLat = "", mLong = "", mSort = "";
	
 	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.classified_filter);
   
        intilizationUI();    
                
        getCategoryData();
    }
	
	

	private void intilizationUI(){
		// TODO Auto-generated method stub
		
		searchTitle = getIntent().getExtras().getString("title");
		
		mSearchTitle = (TextView)findViewById(R.id.classified_filter_TXT_sort_title);
		
		mSearchTitle.setText("Sort "+searchTitle+" by");
		
		mData = (ArrayList<String>) getIntent().getExtras().getSerializable("data");

		
		strSort = new String[]{"Most Recent","Name","Price Low-High","Price High-Low"};
		
		mDone = (Button)findViewById(R.id.classified_filter_BTN_done);
		mDistance = (SeekBar)findViewById(R.id.classified_filter_Seekbar_distance);
		
		mETPriceFrom = (EditText)findViewById(R.id.classified_filter_ET_price_from);
		mETPriceTo = (EditText)findViewById(R.id.classified_filter_ET_price_to);

		mSortBy = (RadioGroup)findViewById(R.id.classified_filter_RBG_text);
		mRBLocal = (RadioButton)findViewById(R.id.classified_filter_RB_Name);
		mRBNational = (RadioButton)findViewById(R.id.classified_filter_RB_Distance);
		
		mSpinCategory = (Spinner)findViewById(R.id.classified_filter_SPIN_category);
		mSpinSort = (Spinner)findViewById(R.id.classified_filter_SPIN_sort); 

		
		mDistance.setProgress(Integer.parseInt(mData.get(7)));
		
		
		if(mData.get(3).equalsIgnoreCase("1")){
			
			mRBLocal.setChecked(true);
			
		}else{			
			
			mRBNational.setChecked(true);	
			
		}
		
		mETPriceFrom.setText(mData.get(8));
		mETPriceTo.setText(mData.get(9));

			
		mDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int selectedId = mSortBy.getCheckedRadioButtonId();			
				mSortType = (RadioButton) findViewById(selectedId);
				
				if(mSortType.getText().toString().equalsIgnoreCase("Local")){		
					mData.remove(3);
					mData.add(3,"1");
					 Toast.makeText(ClassifiedFilterActivity.this, "0", Toast.LENGTH_SHORT).show();
				}else{				
					mData.remove(3);
					mData.add(3,"2");	
					 Toast.makeText(ClassifiedFilterActivity.this, "1", Toast.LENGTH_SHORT).show();
				}				

				
				
				mData.remove(7);
				mData.add(7,mDistance.getProgress()+"");
				mData.remove(8);
				mData.add(8,mETPriceFrom.getText().toString());
				mData.remove(9);
				mData.add(9,mETPriceTo.getText().toString());
				
				
				Log.e("", "Datd "+mData);				
				
				Intent in = new Intent();
				in.putExtra("data", mData);
	            setResult(101,in);			
				finish();
			}
		});		
	}
	
	
	private void setSpinnerData(){
		
		
		ArrayAdapter<String> sortAdapter=new ArrayAdapter<String>(this, R.layout.spinner_background, strSort);
		mSpinSort.setAdapter(sortAdapter);
		
		mSpinSort.setSelection(Integer.parseInt(mData.get(0))-1);		
		
		mSpinSort.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

	            	mData.remove(0);
	    			mData.add(0,(position+1)+"");
	            }

	            @Override
	            public void onNothingSelected(AdapterView<?> parentView) {
	                // your code here
	            }

	        });
		
		ArrayAdapter<String> sectionAdapter=new ArrayAdapter<String>(this, R.layout.spinner_background, strCategory);
		mSpinCategory.setAdapter(sectionAdapter);
		
		mSpinCategory.setSelection(Integer.parseInt(mData.get(10)));		
		
		mSpinCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

	            	mSubCategoryId =  mCategoryFeeds.get(position).getCategoryId();
	            	mData.remove(2);
	    			mData.add(2,mSubCategoryId);
	    			mData.remove(10);
	    			mData.add(10,position+"");
	            }

	            @Override
	            public void onNothingSelected(AdapterView<?> parentView) {
	                // your code here
	            }

	        });
		
		

		
	}
	
	
	
	ProgressDialog progressdialog;
	
	public void getCategoryData(){
	
		progressdialog = ProgressDialog.show(ClassifiedFilterActivity.this, "","Please wait...", true);
	
	try {
		new mAsyncCategoryFeedFetcher(Urls.BASE_URL+"GetMastercategores",new CategoryParserHandler()).start();
		
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
    

    class mAsyncCategoryFeedFetcher extends Thread
	{
		String  feedUrl;
		Handler handler;
		
		public mAsyncCategoryFeedFetcher(String mUrl, Handler handler) throws MalformedURLException {
		
			
			Log.i("", "mUrl********* "+mUrl);

			
			this.feedUrl = mUrl;			
			this.handler = handler;
		
		} 
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try
			{
				
				new GetHomePageCategoriesParser(feedUrl,handler).parser();
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			super.run();
		}
		
	}
    
    
	ArrayList<GetHomePageCategoriesModel>  mCategoryFeeds = null;
	GetHomePageCategoriesModel tempGetHomePageCategoriesModel = null;
	class CategoryParserHandler extends Handler
	{
		public void handleMessage(android.os.Message msg)
		{
			progressdialog.dismiss();
			
			if(msg.what==0)
			{	
				
				Log.e("", "mHomeClassifiedItems ");
				
				mCategoryFeeds = (ArrayList<GetHomePageCategoriesModel>) msg.getData().getSerializable("datafeeds");
				tempGetHomePageCategoriesModel=new GetHomePageCategoriesModel();
				tempGetHomePageCategoriesModel.setCategoryId("0");
				tempGetHomePageCategoriesModel.setCategoryName("All");
				mCategoryFeeds.add(0,tempGetHomePageCategoriesModel);
				
				strCategory = new String[mCategoryFeeds.size()];
				for(int i=0 ;i<mCategoryFeeds.size(); i++)
					strCategory[i] = mCategoryFeeds.get(i).getCategoryName();
				
				setSpinnerData();
				
			}else{
				
				Toast.makeText(ClassifiedFilterActivity.this, "Invalies response form server", Toast.LENGTH_SHORT).show();
			}
			
			
		}
	}
}
	
	