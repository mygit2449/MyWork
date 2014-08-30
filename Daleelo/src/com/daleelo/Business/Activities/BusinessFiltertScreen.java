package com.daleelo.Business.Activities;

import java.net.MalformedURLException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daleelo.R;
import com.daleelo.Dashboard.Model.GetHomePageCategoriesModel;
import com.daleelo.Main.Parser.GetHomePageCategoriesParser;
import com.daleelo.Utilities.Urls;

public class BusinessFiltertScreen extends Activity{
	
	RadioGroup mSortBy;
	RadioButton mSortType, mRBName, mRBDistance, mRBBestMatch;
	SeekBar mDistance;
	ToggleButton mDealOnly;
	EditText mBusinessName, mBusinessOwner;
	Spinner mSubCategory;
	String[] strSubCategory;	
	String mSubCategoryId, mCategoryId;
	ArrayList<String> mData;
	Button mDone;
	
 	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.business_list_filter_screen);
   
        intilizationUI();
        
        getHomeCategoriesData();
      
                
    }
	
	

	private void intilizationUI(){
		// TODO Auto-generated method stub
		
		mCategoryId = getIntent().getStringExtra("category");
		mData = (ArrayList<String>) getIntent().getSerializableExtra("data");
		
		mDone = (Button)findViewById(R.id.business_filter_BTN_done);
		mSortBy = (RadioGroup)findViewById(R.id.business_filter_RBG_sort);
		mDistance = (SeekBar)findViewById(R.id.business_filter_Seekbar_distance);
		mDealOnly = (ToggleButton)findViewById(R.id.business_filter_TB_deal);
		mBusinessName = (EditText)findViewById(R.id.business_filter_ET_business_name);
		mBusinessOwner = (EditText)findViewById(R.id.business_filter_ET_business_owner);
		mSubCategory = (Spinner)findViewById(R.id.business_filter_SPIN_business_category);
		mRBBestMatch = (RadioButton)findViewById(R.id.business_filter_RB_Match);
		mRBName = (RadioButton)findViewById(R.id.business_filter_RB_Name);
		mRBDistance = (RadioButton)findViewById(R.id.business_filter_RB_Distance);
		
		
		
		mDistance.setProgress(Integer.parseInt(mData.get(7)));
		
		
		if(mData.get(10).equalsIgnoreCase("2")){
			
			mRBBestMatch.setChecked(true);
			
		}else if(mData.get(10).equalsIgnoreCase("0")){
			
			mRBDistance.setChecked(true);	
			
		}else{			
			
			mRBName.setChecked(true);
			
		}
			
			
		if(mData.get(11).toString().equalsIgnoreCase("1"))
			mDealOnly.setChecked(true);
		else
			mDealOnly.setChecked(false);
			
		mDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int selectedId = mSortBy.getCheckedRadioButtonId();			
				mSortType = (RadioButton) findViewById(selectedId);
				
				if(mSortType.getText().toString().equalsIgnoreCase("Best Match")){		
					mData.remove(10);
					mData.add(10,"2");
				}else if(mSortType.getText().toString().equalsIgnoreCase("name")){		
					mData.remove(10);
					mData.add(10,"1");
				}else{				
					mData.remove(10);
					mData.add(10,"0");	
				}
				

				mData.remove(0);
				mData.add(0,mBusinessName.getText().toString());	

				mData.remove(1);
				mData.add(1,mBusinessOwner.getText().toString());	
				
				mData.remove(7);
				mData.add(7,mDistance.getProgress()+"");
				
				Log.e("", "Datd "+mData);				
				
				Intent in = new Intent();
				in.putExtra("data", mData);
	            setResult(101,in);			
				finish();
				
			}
		});
		
		
		
		
	}
	
	
	
	public void onToggleClicked(View v) {
	    // Perform action on clicks
	    if (((ToggleButton) v).isChecked()) {
	    	
	    	mData.remove(11);
			mData.add(11,"1");
	        Toast.makeText(BusinessFiltertScreen.this, "Toggle on", Toast.LENGTH_SHORT).show();
	    } else {
	    	mData.remove(11);
			mData.add(11,"0");
	        Toast.makeText(BusinessFiltertScreen.this, "Toggle off", Toast.LENGTH_SHORT).show();
	    }
	}
	
	
	
	
	private void setSpinnerData(){
		
		
		ArrayAdapter<String> sectionAdapter=new ArrayAdapter<String>(this, R.layout.spinner_background, strSubCategory);
		mSubCategory.setAdapter(sectionAdapter);
		
		mSubCategory.setSelection(Integer.parseInt(mData.get(12)));		
		
		mSubCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

	            	mSubCategoryId =  mHomeCategoryItems.get(position).getCategoryId();
	            	mData.remove(3);
	    			mData.add(3,mSubCategoryId);
	    			mData.remove(12);
	    			mData.add(12,position+"");
	            }

	            @Override
	            public void onNothingSelected(AdapterView<?> parentView) {
	                // your code here
	            }

	        });
		

		
	}
	
	ProgressDialog progressdialog;
	
	public void getHomeCategoriesData(){
    	
		progressdialog = ProgressDialog.show(BusinessFiltertScreen.this, "","Please wait...", true);
    	
		try {
			
			new mAsyncCategoriesFeedFetcher(Urls.BASE_URL+"GetSubCategories?CategoryId="+mCategoryId,new CategoriesParserHandler()).start();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
	    
	    
	    class mAsyncCategoriesFeedFetcher extends Thread
		{
			String  feedUrl;
			Handler handler;
			
			public mAsyncCategoriesFeedFetcher(String mUrl, Handler handler) throws MalformedURLException {
			
				
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
	    
	    ArrayList<GetHomePageCategoriesModel> mHomeCategoryItems = null;
	    GetHomePageCategoriesModel  mGetHomePageCategoriesModel = null;
	   
		class CategoriesParserHandler extends Handler
		{
			public void handleMessage(android.os.Message msg)
			{
				
				progressdialog.dismiss();
				
				if(msg.what==0)
				{	
					
					Log.e("", "mHomeCategoryItems ");
					
					mHomeCategoryItems = (ArrayList<GetHomePageCategoriesModel>) msg.getData().getSerializable("datafeeds");
					
					mGetHomePageCategoriesModel = new GetHomePageCategoriesModel();
					mGetHomePageCategoriesModel.setCategoryId("0");
					mGetHomePageCategoriesModel.setCategoryName("All");
					
					mHomeCategoryItems.add(0,mGetHomePageCategoriesModel);
					mGetHomePageCategoriesModel = null;
					
					strSubCategory = new String[mHomeCategoryItems.size()];
					for(int i=0 ;i<mHomeCategoryItems.size(); i++)
						strSubCategory[i] = mHomeCategoryItems.get(i).getCategoryName();
					setSpinnerData();

				}
				
				
			}
		}

		


			
}
	
	