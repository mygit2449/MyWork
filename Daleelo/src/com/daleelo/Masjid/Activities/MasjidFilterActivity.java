package com.daleelo.Masjid.Activities;

import java.io.IOException;
import java.io.InputStream;
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
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.daleelo.R;

public class MasjidFilterActivity extends Activity{
	
	RadioGroup mSortBy;
	RadioButton mSortType, mRBName, mRBDistance;
	SeekBar mDistance;
	EditText mLocation, mBusinessOwner;
	Spinner mSubCategory;
	Button mDone;
    public SharedPreferences sharedpreference;
	
	String mLocName = "", mLat = "", mLong = "", mRange = "", mSort = "";
	
 	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.masjid_filter);
   
        intilizationUI();        
      
                
    }
	
	

	private void intilizationUI(){
		// TODO Auto-generated method stub
		

		sharedpreference = getSharedPreferences("masjid",MODE_PRIVATE);
		
		mDone = (Button)findViewById(R.id.Masjid_filter_BTN_done);
		mSortBy = (RadioGroup)findViewById(R.id.masjid_filter_RBG_text);
		mDistance = (SeekBar)findViewById(R.id.Masjid_filter_Seekbar_distance);
		mLocation = (EditText)findViewById(R.id.Masjid_filter_ET_location);
		mRBName = (RadioButton)findViewById(R.id.masjid_filter_RB_Name);
		mRBDistance = (RadioButton)findViewById(R.id.masjid_filter_RB_Distance);
		
		mLocation.setText(sharedpreference.getString("location",""));
		
		mDistance.setProgress(Integer.parseInt(sharedpreference.getString("range","")));
		
		
		if(sharedpreference.getString("sort","").equalsIgnoreCase("0")){
			
			mRBName.setChecked(true);
			
		}else{			
			
			mRBDistance.setChecked(true);	
			
		}
			
			
		
			
		mDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
							
				mRange = mDistance.getProgress()+"";
				
				int selectedId = mSortBy.getCheckedRadioButtonId();			
				mSortType = (RadioButton) findViewById(selectedId);
				
				if(mSortType.getText().toString().equalsIgnoreCase("name")){		
					
					mSort = "1";
					
				}else{	
					
					mSort = "2";	
				}
				
				if(mLocation.getText().toString().equalsIgnoreCase("")){
					Toast.makeText(MasjidFilterActivity.this, "Please enter the location", Toast.LENGTH_SHORT).show();
				}else{
					mLocName = mLocation.getText().toString();					
					new AsynGetLatLong().execute();
				}

				
				
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
        	 mLat = ""+latitude; 
        	
        	 longitute = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lng");
            mLong = ""+longitute;          
            
            
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
	
	
	
	class AsynGetLatLong extends AsyncTask<Void, Void, String>
    {
		//String result;
    	 ProgressDialog mProgressDialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			this.mProgressDialog=ProgressDialog.show(MasjidFilterActivity.this, "", "Validating location..");
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
				
				Toast.makeText(MasjidFilterActivity.this, "Please enter valid address..", Toast.LENGTH_SHORT).show();
			}else{
				
				
				Editor e = sharedpreference.edit();
			      e.putString("location", mLocName);
			      e.putString("lat", mLat);
			      e.putString("long", mLong);
			      e.putString("range", mRange);
			      e.putString("sort", mSort);
			      e.commit();
					
				Intent in = new Intent();
	            setResult(101,in);			
				finish();
			}
			
			

			
		}
    	
    }
	
	
	
//	*******************************
	
	
}
	
	