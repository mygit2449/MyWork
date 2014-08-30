package com.daleelo.DashBoardClassified.Activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.DashBoardClassified.Model.GetClassifiedModel;
import com.daleelo.DashBoardClassified.Parser.GetClassifiedParser;
import com.daleelo.Dashboard.Model.GetHomePageCategoriesModel;
import com.daleelo.Main.Parser.GetHomePageCategoriesParser;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;

public class PostClassifiedActivity extends Activity implements OnClickListener {


	private EditText mLocation, mZipCode;
	private Button btnDone;
	private RadioGroup mTypeGroup;  
	private RadioButton radioTypeButton;
	public SharedPreferences sharedpreference;
	
	Spinner mSectionList, mCategoryList;
	boolean mGotSection = false, mGotCategory = false;
	ArrayList<String> strSection, strCategory;
	
	
	String mSection = "", mCategory = "", mLocName = "", mLat = "", mLong = "", mtype = "1";
	 
	ProgressDialog progressdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_classified_post_screen);
		initializeUI();
		
		
		try {
			progressdialog = ProgressDialog.show(PostClassifiedActivity.this, "","Please wait...", true);
			getSectionData();
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

	

	private void initializeUI() {
		
		
		btnDone = (Button)findViewById(R.id.classified_post_BTN_continue);
		
		mLocation = (EditText)findViewById(R.id.classified_post_ET_loction);
		mZipCode = (EditText)findViewById(R.id.classified_post_ET_zipcaode);
		
		mSectionList = (Spinner)findViewById(R.id.classified_post_SPIN_section);
		mCategoryList = (Spinner)findViewById(R.id.classified_post_SPIN_category);
		
		mTypeGroup  = (RadioGroup)findViewById(R.id.classified_post_RG_type);
		
		btnDone.setOnClickListener(this);
		
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.classified_post_BTN_continue:
			
			int selectedId = mTypeGroup.getCheckedRadioButtonId();			
			radioTypeButton = (RadioButton) findViewById(selectedId);
			
			if(radioTypeButton.getText().toString().equalsIgnoreCase("local")){				
				mtype = "1";				
			}else{				
				mtype = "2";				
			}
			
			Log.e("", "mSection  "+mSection +"mCategory  "+mCategory);
			
			if(mSection.equalsIgnoreCase("none")){
				Toast.makeText(PostClassifiedActivity.this, "Please select Section", Toast.LENGTH_SHORT).show();
			}else{
				if(mCategory.equalsIgnoreCase("none")){
					Toast.makeText(PostClassifiedActivity.this, "Please select Category", Toast.LENGTH_SHORT).show();
				}else{
					if(mLocation.getText().toString().equalsIgnoreCase("")){
						Toast.makeText(PostClassifiedActivity.this, "Please enter the location", Toast.LENGTH_SHORT).show();
					}else{
						
						if(mZipCode.getText().toString().equalsIgnoreCase("")){
							Toast.makeText(PostClassifiedActivity.this, "Please enter the ZipCode", Toast.LENGTH_SHORT).show();
						}else{
							
							if(mZipCode.getText().length() < 5){
								Toast.makeText(PostClassifiedActivity.this, "Please enter valied ZipCode", Toast.LENGTH_SHORT).show();
							}else{
							
								mLocName = mLocation.getText().toString();
								new AsynGetLatLong().execute();
								
							}
						
						}
					}
				}
			}
			
			break;

		}

	}


//	private void setSpinnerData(){
//		
//		
//		ArrayAdapter<String> sectionAdapter=new ArrayAdapter<String>(this, R.layout.spinner_background, strSection);
//		mSectionList.setAdapter(sectionAdapter);
//		
//		ArrayAdapter<String> CategoryAdapter=new ArrayAdapter<String>(this, R.layout.spinner_background, strCategory);
//		mCategoryList.setAdapter(CategoryAdapter);
//		
//		mSectionList.setOnItemSelectedListener(new OnItemSelectedListener() {
//	            @Override
//	            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//	                // your code here
////	                String mItem;
////	                mItem = mSectionList.getSelectedItem().toString();
////	                Log.e("", "mItem " +mItem);
////	                Log.e("", "mSectionFeeds "+ mSectionFeeds.get(position).getCategoryId());
//	                mSection =  mSectionFeeds.get(position).getCategoryId();
//	                
//	                getCategoryData();
//	            }
//
//	            @Override
//	            public void onNothingSelected(AdapterView<?> parentView) {
//	                // your code here
//	            }
//
//	        });
//		
//		mCategoryList.setOnItemSelectedListener(new OnItemSelectedListener() {
//	            @Override
//	            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//	                // your code here
////	                String mItem;
////	                mItem = mCategoryList.getSelectedItem().toString();
////	                Log.e("", "mItem " +mItem);
////	                Log.e("", "mCategoryFeeds "+ mCategoryFeeds.get(position).getCategoryId());
//	                mCategory = mCategoryFeeds.get(position).getCategoryId();
//	            }
//
//	            @Override
//	            public void onNothingSelected(AdapterView<?> parentView) {
//	                // your code here
//	            }
//
//	        });
//		
//	}
	
	private void setSectionData(){
		
		
		ArrayAdapter<String> sectionAdapter=new ArrayAdapter<String>(this, R.layout.spinner_background, strSection);
		mSectionList.setAdapter(sectionAdapter);
		
	
		mSectionList.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	                // your code here

	            	if(!strSection.get(position).equalsIgnoreCase("Data not found")){
	            	
	            		mSection =  mSectionFeeds.get(position).getCategoryId();	                
	            		getCategoryData();
	            		
	            	}else{
	            		
	            		mCategoryList.setEnabled(false);
	            		strCategory = new ArrayList<String>();
						strCategory.add("Data not found");
						setCategoryData();
						mCategory = "none"; 
	            	}
	            }

	            @Override
	            public void onNothingSelected(AdapterView<?> parentView) {
	                // your code here
	            }

	        });
		
	
		
	}
	
	private void setCategoryData(){
		
	
		ArrayAdapter<String> CategoryAdapter=new ArrayAdapter<String>(this, R.layout.spinner_background, strCategory);
		mCategoryList.setAdapter(CategoryAdapter);
		
			
		mCategoryList.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	                // your code here

	            	if(!strCategory.get(position).equalsIgnoreCase("Data not found")){
		            	
	 
	            		mCategory = mCategoryFeeds.get(position).getCategoryId();
	            		
	            	}
	            }

	            @Override
	            public void onNothingSelected(AdapterView<?> parentView) {
	                // your code here
	            }

	        });
		
	}
	
	
	
//	*******************************
//	get lat long by address
	
	
	public double longitute, latitude;
	private String mCity = "none";
	
	public static JSONObject getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

        address = address.replaceAll(" ","%20");  
        Log.e("", "url "+"http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
        HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }
	
	
	public boolean getLatLong(JSONObject jsonObject) {

        try {
        	

        	 latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
             .getJSONObject("geometry").getJSONObject("location")
             .getDouble("lat");
         
        	 mLat = latitude+"";
         
            longitute = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lng");
        	
            mLong = longitute+"";     
            
            mCity =  
            	((JSONArray)jsonObject.get("results"))
            	.getJSONObject(0)
            	.getJSONArray("address_components")
            	.getJSONObject(0)
            	.getString("long_name");
            	
            
            
            Log.e("", "longitute "+longitute+"latitude "+latitude+"mCityAddress "+mCity);

         
           
            

        } catch (JSONException e) {
        	
        	e.printStackTrace();
            return false;

        }

        return true;
    }
	
//	*******************************
	
	
	 public void getSectionData() throws MalformedURLException{
	    	
	    	
	    	try {
	    		
	        	String mCity = URLEncoder.encode(CurrentLocationData.CURRENT_CITY, "UTF-8");
	        	String mUrl = Urls.BASE_URL+"GetClassifiedCategories";
	    		new mAsyncSectionFeedFetcher(mUrl, new SectionParserHandler()).start();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	

	    }
	    
	    
	    class mAsyncSectionFeedFetcher extends Thread
		{
			String  feedUrl;
			Handler handler;
			
			public mAsyncSectionFeedFetcher(String mUrl, Handler handler) throws MalformedURLException {
			
				
				Log.i("", "mUrl********* "+mUrl);			
				
				this.feedUrl = mUrl;			
				this.handler = handler;
			
			} 
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try
				{
					
					new GetClassifiedParser(feedUrl, handler).parser();
				
				}catch(Exception e){
					
					e.printStackTrace();
				}
				super.run();
			}
			
		}
	    
	    
	    String noDataMsg = "Local spotlights not found";
	    ArrayList<GetClassifiedModel> mSectionFeeds = null;
	    
		class SectionParserHandler extends Handler {
			public void handleMessage(android.os.Message msg) {
				
				progressdialog.dismiss();
				
				strSection = new ArrayList<String>();
				mSection = "none";

				
				if(msg.what==0)
				{	

					mSectionList.setEnabled(true);
					mSectionFeeds = (ArrayList<GetClassifiedModel>) msg.getData().getSerializable("datafeeds");
					for(int i=0 ;i<mSectionFeeds.size(); i++)
						strSection.add(mSectionFeeds.get(i).getCategoryName());
					
					setSectionData();
//					mGotSection = true;
//					mSetDataHandler.sendEmptyMessage(0);
					
//					  mSection =  mSectionFeeds.get(0).getCategoryId();			                
//		              getCategoryData();

				}else{
					
					mSectionList.setEnabled(false);
					strSection.add("Data not found");
					setSectionData();
					Toast.makeText(PostClassifiedActivity.this, "No Sections found", Toast.LENGTH_SHORT).show();
					
				}

			}
		}
	
		public void getCategoryData(){
			
//			if(mGotSection)
				progressdialog = ProgressDialog.show(PostClassifiedActivity.this, "","Please wait...", true);
    	
		try {
			new mAsyncCategoryFeedFetcher(Urls.BASE_URL+"GetSubCategories?CategoryId="+mSection,new CategoryParserHandler()).start();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
	    
		ArrayList<GetHomePageCategoriesModel>  mCategoryFeeds = null;
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
	    
	    
	   
		class CategoryParserHandler extends Handler
		{
			public void handleMessage(android.os.Message msg)
			{
				
				progressdialog.dismiss();
				
				strCategory = new ArrayList<String>();
				mCategory = "none"; 
				
				if(msg.what==0)
				{	
            		mCategoryList.setEnabled(true);	            		

					Log.e("", "mHomeClassifiedItems ");
					
					mCategoryFeeds = (ArrayList<GetHomePageCategoriesModel>) msg.getData().getSerializable("datafeeds");					
					
					for(int i=0 ;i<mCategoryFeeds.size(); i++)
						strCategory.add(mCategoryFeeds.get(i).getCategoryName());
					
//					mGotCategory = true;
//					mSetDataHandler.sendEmptyMessage(0);
					
					setCategoryData();
					
				}else{
					
            		mCategoryList.setEnabled(false);	
            		strCategory.clear();
					strCategory.add("Data not found");
					setCategoryData();
					mCategory = "none"; 
					Toast.makeText(PostClassifiedActivity.this, "No Categories found", Toast.LENGTH_SHORT).show();
				}
				
				
			}
		}
	
		
		
		
	
	
			SetDataHandler mSetDataHandler = new SetDataHandler();
			
			
			class SetDataHandler extends Handler {
				public void handleMessage(android.os.Message msg) {
					
					
					if(mGotCategory == true && mGotSection ==true){
						progressdialog.dismiss();
//						setSpinnerData();
						
					}
				}
			}
			
			
			class AsynGetLatLong extends AsyncTask<Void, Void, String>
		    {
				//String result;
		    	 ProgressDialog mProgressDialog;
				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					this.mProgressDialog=ProgressDialog.show(PostClassifiedActivity.this, "", "Validating location..");
				}

				@Override
				protected String doInBackground(Void... params) {
					
					getLatLong(getLocationInfo(mLocation.getText().toString()));
					
					return mCity;
		  
					
				}

				@Override
				protected void onPostExecute(String result) {
					super.onPostExecute(result);
					mProgressDialog.dismiss();
					
					if(mCity.equalsIgnoreCase("none")){
						
						
						Toast.makeText(PostClassifiedActivity.this, "Please enter vaild address..", Toast.LENGTH_SHORT).show();
					}else{
						
						myAlertDialog();
//						Toast.makeText(PostClassifiedActivity.this, mCity, Toast.LENGTH_SHORT).show();
					}
					
					

					
				}
		    	
		    }
			
			
			
			
//		AlertDialogs
//		*************************			
		
			
			private void myAlertDialog(){
				
			
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Daleelo");
				builder.setMessage("You have given the address in the City "+"\""+mCity+"\""+" ; Do you want to proceed")
				       .setCancelable(false)
				       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   
				        	   startActivity(new Intent(PostClassifiedActivity.this, PostClassifiedSecondActivity.class)
				               .putExtra("section", mSection)
				               .putExtra("category", mCategory)
				               .putExtra("location", mLocName)
				               .putExtra("city", mCity)
				               .putExtra("latitude", mLat)
				               .putExtra("longitude", mLong)
				               .putExtra("type", mtype)
				               .putExtra("from", "post"));
				               
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
