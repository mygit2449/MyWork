package com.daleelo.Hasanat.Activities;

import java.net.MalformedURLException;
import java.util.ArrayList;

import com.daleelo.R;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.Hasanat.Activities.SadaqaFilterActivity.FeedParserHandler;
import com.daleelo.Hasanat.Activities.SadaqaFilterActivity.mAsyncFeedFetcher;
import com.daleelo.Hasanat.Model.CategoryModel;
import com.daleelo.Hasanat.Model.CountryModel;
import com.daleelo.Hasanat.Parser.CountryParser;
import com.daleelo.Utilities.Urls;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RelativeLayout.LayoutParams;

public class ZakatFilterActivity extends Activity implements OnClickListener{
	
	private Button mbtn_done;
	private Spinner mSpinnerCountry;
	
	private String mCountryId;

	private ProgressDialog progressdialog;
	private ArrayList<CountryModel> mCountryDataList;
	private ArrayList<String> spinnerArray;
	private RadioGroup mSortBy;
	private RadioButton mSortType, mRBName, mRBDistance, mRBBestMatch;
	
	private String mSubCategoryId, mCategoryId;
	private ArrayList<String> mData;
	
	private int selectedItem;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zakat_filter);
		
		initializeUI();		
		getCountries();
		
	}
	
	
	
	private void getCountries(){
		
		progressdialog = ProgressDialog.show(ZakatFilterActivity.this, "","Please wait...", true);
		String mUrl = String.format(Urls.COUNTRY_URL);
		try {
			new mAsyncFeedFetcher(mUrl, new FeedParserHandler()).start();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void initializeUI(){
		
		mbtn_done = (Button)findViewById(R.id.zakat_filter_BTN_done);
		mSpinnerCountry = (Spinner)findViewById(R.id.zakat_filter_SPN_country);
		mSortBy = (RadioGroup)findViewById(R.id.zakat_filter_radiogroup);
		mRBName = (RadioButton)findViewById(R.id.zakat_filter_radio_name);
		mRBDistance = (RadioButton)findViewById(R.id.zakat_filter_radio_distance);
		
		
		mCategoryId = getIntent().getStringExtra("category");
		mData = (ArrayList<String>) getIntent().getSerializableExtra("data");
		
		if(mData.get(7).equalsIgnoreCase("1")){
			
			mRBName.setChecked(true);
			
		}else{			
			
			mRBDistance.setChecked(true);	
			
		}
		
		selectedItem = Integer.parseInt(mData.get(8));
		
		mbtn_done.setOnClickListener(this);
		
		
	}
	
	
	private void setSpinnerData(){
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ZakatFilterActivity.this,android.R.layout.simple_spinner_item, spinnerArray);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    mSpinnerCountry.setAdapter(adapter);
	    mSpinnerCountry.setSelection(selectedItem);
	    mSpinnerCountry.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				mData.remove(8);
				mData.add(8,arg2+"");
				
				if(arg2 == 0){
				
					mData.remove(2);
					mData.add(2,"0");	
					
					
				}else{
					
					
					mCountryId = mCountryDataList.get(arg2-1).getCountryId();
					mData.remove(2);
					mData.add(2,"1");	
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.zakat_filter_BTN_done:
			
			Intent CategoryIntent = new Intent();
			if(mSortBy.getCheckedRadioButtonId() == R.id.zakat_filter_radio_name){
				
				CategoryIntent.putExtra("orderby", 1);
				
				mData.remove(7);
				mData.add(7,"1");	
				
			}else{
				
				CategoryIntent.putExtra("orderby", 2);
				
				mData.remove(7);
				mData.add(7,"2");	
				
			}
			
			Intent in = new Intent();
			in.putExtra("data", mData);
            setResult(101,in);			
			finish();
			
			break;

		default:
			break;
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
				
				new CountryParser(feedUrl, handler).parser();
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			super.run();
		}
		
	}
	
	
	class FeedParserHandler extends Handler {
		
		public void handleMessage(android.os.Message msg) {
			
			progressdialog.dismiss();
			
			if(msg.what==0)
			{	
				mCountryDataList = null;
				mCountryDataList = (ArrayList<CountryModel>) msg.getData().getSerializable("datafeeds");
				spinnerArray = new ArrayList<String>();
				spinnerArray.add("Any");
				for(int i=0; i<mCountryDataList.size(); i++){
					spinnerArray.add(mCountryDataList.get(i).getCountryName());
				}
				
				setSpinnerData();
				

			}else if(msg.what==1){
				
				
			}else if(msg.what==2){
				
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(ZakatFilterActivity.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
//						MainHomeScreen.this.finish();
						
					}
					
				}).create().show();
			}

		}
	}
	
		
}