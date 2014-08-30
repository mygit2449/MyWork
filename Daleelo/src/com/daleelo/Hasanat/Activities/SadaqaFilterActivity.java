package com.daleelo.Hasanat.Activities;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.daleelo.R;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.Hasanat.Activities.SadakaActivity.FeedParserHandler;
import com.daleelo.Hasanat.Activities.SadakaActivity.mAsyncFeedFetcher;
import com.daleelo.Hasanat.Model.BusinessDetailModel;
import com.daleelo.Hasanat.Model.CategoryModel;
import com.daleelo.Hasanat.Model.CountryModel;
import com.daleelo.Hasanat.Parser.CountryParser;
import com.daleelo.Hasanat.Parser.FeaturedMosqueParser;
import com.daleelo.Utilities.Urls;

public class SadaqaFilterActivity extends Activity implements OnClickListener, OnCheckedChangeListener{
	
	private Button mbtn_done;
	private RelativeLayout mrel_chk;
	private CheckBox[] chkbxes;
	private Spinner mSpinnerCountry;
	
	private String mCountryId;

	private ProgressDialog progressdialog;
	private ArrayList<CategoryModel> mCategoryList;
	private ArrayList<CountryModel> mCountryDataList;
	private ArrayList<String> spinnerArray;
	private RadioGroup mFilterRadioGroup;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sadaka_filter);
		initializeUI();
		progressdialog = ProgressDialog.show(SadaqaFilterActivity.this, "","Please wait...", true);
		String mUrl = String.format(Urls.COUNTRY_URL);
    	try {
			new mAsyncFeedFetcher(mUrl, new FeedParserHandler()).start();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initializeUI(){
		mCategoryList = (ArrayList<CategoryModel>) getIntent().getExtras().get("category");
		
		mbtn_done = (Button)findViewById(R.id.sadaka_filter_BTN_done);
		mrel_chk = (RelativeLayout)findViewById(R.id.sadaka_filter_REL_chk);
		mSpinnerCountry = (Spinner)findViewById(R.id.sadaka_filter_SPN_country);
		mFilterRadioGroup = (RadioGroup)findViewById(R.id.sadaka_filter_radiogroup);
		
		mbtn_done.setOnClickListener(this);
		
		chkbxes = new CheckBox[(mCategoryList.size()+1)];
		
		setCheckboxes();
	}
	
	private void setCheckboxes(){
		
		for(int i=0; i<(mCategoryList.size()+1); i++){
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			chkbxes[i] = new CheckBox(this);
			chkbxes[i].setTextColor(R.color.black);
			chkbxes[i].setButtonDrawable(R.drawable.customised_filter_checkbox);
			chkbxes[i].setId(i+1);
			chkbxes[i].setOnCheckedChangeListener(this);
			
			if(i == 0){
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				chkbxes[i].setText("All");
				chkbxes[i].setLayoutParams(params);
				
			}else if(i == 1){
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				chkbxes[i].setText(mCategoryList.get(i-1).getCategoryName());
				chkbxes[i].setLayoutParams(params);
				
			}else {
				chkbxes[i].setText(mCategoryList.get(i-1).getCategoryName());
				if(( (i) % 2) == 0){
					params.addRule(RelativeLayout.BELOW, chkbxes[(i)-1].getId());
					chkbxes[i].setLayoutParams(params);
				}
				else{
					params.addRule(RelativeLayout.BELOW, chkbxes[(i)-2].getId());
					params.addRule(RelativeLayout.ALIGN_LEFT, chkbxes[(i)-2].getId());
					chkbxes[i].setLayoutParams(params);
				}
				
			}
			mrel_chk.addView(chkbxes[i]); 
		}
		chkbxes[0].setChecked(true);
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		switch (buttonView.getId()) {
		case 1:
			if(isChecked){
//				chkbxes[0].setChecked(true);
				for(int i=1; i< chkbxes.length;i++){
					chkbxes[i].setChecked(false);
				}
			}
			break;
		default:
			if(isChecked)
				chkbxes[0].setChecked(false);	
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.sadaka_filter_BTN_done:
			Set<String> selectedCategory = new HashSet<String>();
			Set<String> selectedCategoryNameSet = new HashSet<String>();
			
			for(int i=0; i< chkbxes.length; i++){

					if(chkbxes[i].isChecked()){
						if(i == 0){
							//selectedCategory.add(mCategoryList.get(1).getCategoryMasterId());
							selectedCategory.add("0");
							selectedCategoryNameSet.add("Sadaka");
						}
						else{
							selectedCategory.add(mCategoryList.get(i-1).getCategoryId());
							selectedCategoryNameSet.add(mCategoryList.get(i-1).getCategoryName());
						}
					}
			}
			
			String selectedCategoryId = selectedCategory.toString().substring(1,selectedCategory.toString().length()-1 );
			String selectedCategoryNames = selectedCategoryNameSet.toString().substring(1,selectedCategoryNameSet.toString().length()-1 );
			Log.e("selectedCategory",selectedCategoryId);
			
			Intent CategoryIntent = new Intent();
			CategoryIntent.putExtra("selectedCategory", selectedCategoryId);
			CategoryIntent.putExtra("selectedCountry", mCountryId);
			CategoryIntent.putExtra("selectedCategorynames", selectedCategoryNames);
			if(mFilterRadioGroup.getCheckedRadioButtonId() == R.id.sadaka_filter_radio_name){
				CategoryIntent.putExtra("orderby", 1);
			}else{
				CategoryIntent.putExtra("orderby", 2);
			}
			setResult(RESULT_OK, CategoryIntent);
			SadaqaFilterActivity.this.finish();
			
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
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(SadaqaFilterActivity.this,android.R.layout.simple_spinner_item, spinnerArray);
			    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			    mSpinnerCountry.setAdapter(adapter);
			    mSpinnerCountry.setSelection(0);
			    mSpinnerCountry.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						
						if(arg2 == 0)
							mCountryId = "0";
						else
							mCountryId = mCountryDataList.get(arg2-1).getCountryId();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});

			}else if(msg.what==1){
				
				
			}else if(msg.what==2){
				
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(SadaqaFilterActivity.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
//						MainHomeScreen.this.finish();
						
					}
					
				}).create().show();
			}

		}
	}
	
		
}