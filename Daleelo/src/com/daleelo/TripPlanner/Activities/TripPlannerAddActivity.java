package com.daleelo.TripPlanner.Activities;


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

import android.R.layout;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.TripPlanner.Model.RouteCitiesModel;
import com.daleelo.TripPlanner.Model.TripPlannerLocationPointsModel;
  
public class TripPlannerAddActivity  extends Activity{	


	Intent receiver_intent;
	private int chosen_item;
	private int id =0, et_pos = -1;
	int REQUESTCODE=47;
	private ArrayList<String> citynames_arr;
	private String cityname="";
	private boolean cityfound = true;	
	private ArrayList<EditText> mDestanitions;
	private ArrayList<Boolean> mValidAddress;
	private ProgressDialog progressDialog;
	private LinearLayout add_destinations;
	private ArrayList<double[]> lat_long_arr;
	private boolean from_done_button =false;
	
	private ArrayList<TripPlannerLocationPointsModel> mRouteCitiesFeeds;
	TripPlannerLocationPointsModel mRouteCitiesModel;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        lat_long_arr 	= new ArrayList<double[]>();
        mRouteCitiesFeeds = new ArrayList<TripPlannerLocationPointsModel>();
        mRouteCitiesModel = new TripPlannerLocationPointsModel();
        receiver_intent = getIntent();
        chosen_item 	= receiver_intent.getIntExtra("heading", 0);       
        
        
        switch(chosen_item){
        	
        	case 1:
        		
        		 setContentView(R.layout.tripplanner_addmoredest); 
        		
        		 add_destinations			  = (LinearLayout)findViewById(R.id.tripplanner_addmore);
        		 TextView addmoredest		  = (TextView)findViewById(R.id.addmore_txtView);
        		 ImageView done_imgView		  = (ImageView)findViewById(R.id.done_imgView);
        		 citynames_arr				  = new ArrayList<String>();
        		 mDestanitions				  = new ArrayList<EditText>();
        		 mValidAddress = new ArrayList<Boolean>();
        		 
        		 done_imgView.setOnClickListener(new OnClickListener() {
		   	 
					@Override
					public void onClick(View v) {
						
						if(add_destinations.getChildCount()>0){	
							LinearLayout mlastlinearlayout = (LinearLayout) add_destinations.getChildAt(add_destinations.getChildCount()-1);
							EditText     mlastet 	       = (EditText) mlastlinearlayout.getChildAt(1);	
		        			
							if(mlastet.getText().toString().length()>0){
								
								for(int i=0;i<add_destinations.getChildCount();i++){
									LinearLayout mlinearlayout = (LinearLayout) add_destinations.getChildAt(i);
									EditText     et 		   = (EditText) mlinearlayout.getChildAt(1);	
									Log.v("city add","8"+et.getText().toString());
									citynames_arr.add(et.getText().toString());
								}
							}
							cityname = mDestanitions.get(et_pos).getText().toString();						
							from_done_button = true;
							progressDialog = ProgressDialog.show(TripPlannerAddActivity.this, "", "Searching for the city");
							Log.v("cityname in done",cityname);
							new AsynGetCity().execute(new String[]{cityname});
						}else{		
							 Intent returnIntent = new Intent();	    
						     setResult(RESULT_CANCELED,returnIntent);    	
						     finish(); 
						}
						
						
						 
										
					}
				});
        		 
        		 
        		 addmoredest.setOnClickListener(new OnClickListener() {
					
        			 
					@Override
					public void onClick(View v) {
						
						from_done_button = false;
						
						if(add_destinations.getChildCount()==0)
							refreshWholeData();
						
						Log.v("cityname",""+cityname);
						Log.v("first",""+et_pos);
						
						if(et_pos >= 0){
							
							Log.v("second",""+et_pos);
						
							try {
									Log.v("cityname","edit text "+mDestanitions.get(et_pos).getText());
									cityname = mDestanitions.get(et_pos).getText().toString();
									Log.e("", "mDestanitions "+ mDestanitions+" && "+mValidAddress);
									if(cityname.length()>0){
									
										if(mValidAddress.get(et_pos)){
											et_pos++;
											addLayout();
										}else{
											progressDialog = ProgressDialog.show(TripPlannerAddActivity.this, "", "Searching for the city");
											new AsynGetCity().execute(new String[]{cityname});
										}
									}else if(!cityfound){
										
										AddressAlert();
										
									}
							
								} catch (Exception e) {
							// TODO: handle exception
									e.printStackTrace();
								}
							}else{
							
								et_pos++;
								Log.v("third",""+et_pos);
								addLayout();
								
							}
						
					}
				});
        		 
        		 break;      		
        
        }  
    } 

    
    protected void refreshWholeData() {
    	citynames_arr.clear();
    	mDestanitions.clear();
    	mValidAddress.clear();
    	id=0;
    	et_pos=-1;
    	cityname = "";
    	cityfound=true;
		
	}


	protected void addLayout() {
    	
    		
		final LinearLayout mLayout = new LinearLayout(TripPlannerAddActivity.this);					
		mLayout.setOrientation(LinearLayout.HORIZONTAL);					
		mLayout.setId(id);
		ImageView    iv = new ImageView(TripPlannerAddActivity.this);
		iv.setId(id);
		
			iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					DeleteItOrNot();	
					
					
				}

				private void DeleteItOrNot() {
					
					new AlertDialogMsg(TripPlannerAddActivity.this, "Do you want to delete it").setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener(){

						@Override
							public void onClick(DialogInterface dialog, int which) {							
							
							add_destinations.removeView(mLayout);					
							mValidAddress.remove(et_pos);
							mValidAddress.add(et_pos, true);
							et_pos--;
							
							}
						
						}).setNegativeButton("No", new android.content.DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								
						}}).create().show();
					
					
					
				}
			});
			
			
		iv.setImageResource(R.drawable.delete_view);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.topMargin=10;
		params.leftMargin=5;
		iv.setLayoutParams(params);
		final EditText     et = new EditText(TripPlannerAddActivity.this);
		et.setTag(et_pos);
		mDestanitions.add(et);	
		mValidAddress.add(false);
		
		
		
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT	,80);
		params1.leftMargin=5;
		params1.topMargin=5;
		et.setSingleLine(true);
		et.setLayoutParams(params1);	
	   
		
		mLayout.addView(iv);
		mLayout.addView(et);								
		
		add_destinations.addView(mLayout);
		id++;
	}


	class AsynGetCity extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			
			
			
			for(String param : params) {				
				
				 return getLatLong(getLocationInfo(param));
				
				
			}	
		
			return false;
		}
		
		private boolean getLatLong(JSONObject jsonObject) {

	        try {
	        	
	        		Log.v("status",""+(String)jsonObject.get("status"));
		        	 String status = (String)jsonObject.get("status"); 	 
		        	 boolean exist = false;
		                
		        	 if(!status.equalsIgnoreCase("OK"))             
		        		 return  false; 
		        	 else{
		        		 
		        		 mRouteCitiesModel = new TripPlannerLocationPointsModel();
		        		 
		        		 Log.v("coming","**************************");
		        		 Double longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
			                     .getJSONObject("geometry").getJSONObject("location")
			                     .getDouble("lng");
			             			             	
		        		
			            Double latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
			                     .getJSONObject("geometry").getJSONObject("location").getDouble("lat");
			            
			            mRouteCitiesModel.setCityName(cityname);
			            mRouteCitiesModel.setAddressLong(longitude);
			            mRouteCitiesModel.setAddressLat(latitude);	
			            mRouteCitiesModel.setType("p");
			            mRouteCitiesModel.setCategory("main");
			            mRouteCitiesModel.setMiddlecity(true);
			            mRouteCitiesFeeds.add(mRouteCitiesModel);
			            mRouteCitiesModel = null;
			            
			            //to remove duplicate data
			            if(lat_long_arr.size()>0){
				            for(int i=0;i<lat_long_arr.size();i++){
				            	Log.v("if data",""+lat_long_arr.get(i)[0]+"=="+latitude+"&&"+lat_long_arr.get(i)[1]+"=="+longitude);
				            	if(lat_long_arr.get(i)[0]==latitude && lat_long_arr.get(i)[1]==longitude){				            		 
				            		exist =true;
				            	}
				            }
				            if(!exist){
				            	
				            	lat_long_arr.add(new double[]{latitude,longitude});
				            }
			            }else{
			            		lat_long_arr.add(new double[]{latitude,longitude});
			            }
			            Log.v("lat long size",""+lat_long_arr.size());
			          
		        	 }
		        		 return  true;
		        	
		        
		        	    

	          }catch(JSONException e){
	        	
	        	e.printStackTrace();
	            return false;

	          }

	       
	    }

		@Override
		protected void onPostExecute(Boolean found) {
			
			progressDialog.dismiss();
			
			if(from_done_button){
				
				
				if(!found){											
				 AddressAlert();
			    }else{	
			    	Log.e("", "route city >> "+mRouteCitiesFeeds.get(0).getCityName());
					 Intent returnIntent = new Intent();	
				    //  returnIntent.putExtra("citynames_latlong",lat_long_arr);
				    // returnIntent.putExtra("citynames",citynames_arr);
				     returnIntent.putExtra("data",mRouteCitiesFeeds);
				     
				     setResult(RESULT_OK,returnIntent);    	
				     finish(); 
				 }		     
			}else{
				
				if(found){
					cityname="";
					mValidAddress.remove(et_pos);
					mValidAddress.add(et_pos,true);	
					Log.e("", "mDestanitions "+ mDestanitions+" && "+mValidAddress);
					et_pos++;
					addLayout();
					
				}		
				else{
					cityfound = false;
					cityname ="";
					AddressAlert();
				}	
			}	
		}
		

		private JSONObject getLocationInfo(String address) {
			
		        StringBuilder stringBuilder = new StringBuilder();
		        try{

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
		        }catch (ClientProtocolException e) {
		        }catch (IOException e) {
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
			

		}
	
	
	private void AddressAlert(){
		
		
		
		new AlertDialogMsg(TripPlannerAddActivity.this, "Please enter valid cityname").setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

			@Override
				public void onClick(DialogInterface dialog, int which) {							
				
				
				}
			
			}).create().show();
		
		
	}
	
	@Override
	public void onBackPressed() {
		 Intent returnIntent = new Intent();	    
	     setResult(RESULT_CANCELED,returnIntent);    	
	     finish(); 
		super.onBackPressed();
	}
}