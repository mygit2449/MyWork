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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.TripPlanner.Model.TripPlannerLocationPointsModel;
import com.daleelo.TripPlanner.Parser.TripPlannerLocationPointsParser;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;



public class TripPlannerActivity extends Activity implements OnClickListener,OnSeekBarChangeListener,OnCheckedChangeListener{
	
	
	//widgets
	private EditText startpoint_eText 	= null;
	private EditText endpoint_eText   	= null;
	private Button   navigate_btn     	= null;
	private RelativeLayout importOwnList_layout;
	
	//seekbars
	private SeekBar  all_seekbar	  	= null;
	private SeekBar  gasstation_seekbar	= null;
	private SeekBar  grocery_seekbar	= null;
	private SeekBar  masjid_seekbar	    = null;
	private SeekBar  restareas_seekbar	= null;
	private SeekBar  restaurant_seekbar	= null;
	//checkboxes
	private CheckBox all_checkbox      	=  null;
	private CheckBox gas_checkbox       =  null;
	private CheckBox grocery_checkbox   =  null;
	private CheckBox masjid_checkbox    =  null;
	private CheckBox restAreas_checkbox =  null;
	private CheckBox restaurant_checkbox=  null;	
	
	private ProgressDialog progressDialog;	
	private int REQUEST_CODE = 47;				
	
	public ArrayList<double[]> cities_latlong = null;
	public ArrayList<double[]> cities_latlong_dump = null;
	//public ArrayList<String> city_names = null;
	//public ArrayList<String> city_names_dump = null;
	private String[] module_ids = {"148","55","146","147","85"};
	public ArrayList<TripPlannerLocationPointsModel> loc_points_arr,mRouteModelFeeds = null,mRouteModelFeedsdump;
	private TripPlannerLocationPointsModel model_object;
	Intent mapIntent;
	private String url ="";	
	
	private StringBuilder sb_all_module;
	private StringBuilder sb_all_distance;
	private StringBuilder sb_few_distance;
	private StringBuilder sb_few_modules;
	
	private String start_point = "";
	private String end_point   = "";
	private boolean showAlert  = false; 
	private boolean from_navigate_btn = false;
	
	
//	ArrayList<RouteCitiesModel> mRouteModelFeeds;
//	RouteCitiesModel mRouteCitiesModel;
	int routeCitiesCount = 0;
	
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripplanner);
        intializeUI();             
     
        
    }
    
    private void intializeUI() {
		
		navigate_btn 				=  (Button)findViewById(R.id.tripplanner_navigate_btn);
		startpoint_eText	 		=  (EditText)findViewById(R.id.tripplanner_startpoint_eText);
		endpoint_eText		 		=  (EditText)findViewById(R.id.tripplanner_endpoint_eText);
		importOwnList_layout 		=  (RelativeLayout)findViewById(R.id.tripplanner_addyourOwn_layout);
		
		//seekbars
		all_seekbar			 		=  (SeekBar)findViewById(R.id.all_seekbar);
		gasstation_seekbar  	    =  (SeekBar)findViewById(R.id.gasstation_seekbar);
		grocery_seekbar		 		=  (SeekBar)findViewById(R.id.grocery_seekbar); 
		masjid_seekbar			    =  (SeekBar)findViewById(R.id.masjid_seekbar);
		restareas_seekbar	 		=  (SeekBar)findViewById(R.id.restareas_seekbar);
		restaurant_seekbar	 		=  (SeekBar)findViewById(R.id.restaurant_seekbar);			
		
		//checkboxes	
		all_checkbox		        =  (CheckBox)findViewById(R.id.tripplanner_all_chBox);
		gas_checkbox				=  (CheckBox)findViewById(R.id.tripplanner_gasStation_chBox);
		grocery_checkbox			=  (CheckBox)findViewById(R.id.tripplanner_Grocery_chBox);
		masjid_checkbox				=  (CheckBox)findViewById(R.id.tripplanner_masjid_chBox);
		restAreas_checkbox			=  (CheckBox)findViewById(R.id.tripplanner_Rest_chBox);
		restaurant_checkbox			=  (CheckBox)findViewById(R.id.tripplanner_Restaurant_chBox);		
		
		//seekbar onseekbarchangelisteners
		all_seekbar.setOnSeekBarChangeListener(this);
		gasstation_seekbar.setOnSeekBarChangeListener(this);
		grocery_seekbar.setOnSeekBarChangeListener(this);
		masjid_seekbar.setOnSeekBarChangeListener(this);
		restareas_seekbar.setOnSeekBarChangeListener(this);
		restaurant_seekbar.setOnSeekBarChangeListener(this);
		
		//checkbox onclick listeners
		all_checkbox.setOnCheckedChangeListener(this);
		gas_checkbox.setOnCheckedChangeListener(this);
		grocery_checkbox.setOnCheckedChangeListener(this);
		masjid_checkbox.setOnCheckedChangeListener(this);
		restAreas_checkbox.setOnCheckedChangeListener(this);
		restaurant_checkbox.setOnCheckedChangeListener(this);	
		
		navigate_btn.setOnClickListener(this);
		importOwnList_layout.setOnClickListener(this);
		
		//city_names			 =  new ArrayList<String>();	
		//cities_latlong       =  new ArrayList<double[]>();
		loc_points_arr 		 =  new ArrayList<TripPlannerLocationPointsModel>();	
		mRouteModelFeeds 	 =  new ArrayList<TripPlannerLocationPointsModel>();
				
	}
    	@Override
	protected void onStop() {
    		 super.onStop();
    		//from_onActivityforResult=false;
    		//city_names.clear();    
    		//loc_points_arr.clear();  
    		//cities_latlong.clear();
    		//mRouteModelFeeds.clear();
    		//routeCitiesCount = 0;
    		//Log.v("onstoppppppppppppp","onstoppp");
    		if(from_navigate_btn){
    			loc_points_arr.clear();
    			mRouteModelFeeds.clear();
    			routeCitiesCount=0;
    		}
    			
    	
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
			case R.id.tripplanner_navigate_btn:		
				
					from_navigate_btn = true;
					showAlert   = false;
					start_point = startpoint_eText.getText().toString();
					end_point   = endpoint_eText.getText().toString();
					
					 if(start_point.length()<1){
						 start_point = CurrentLocationData.CURRENT_CITY;
				     }
					
					if(end_point.length()>0 && !end_point.equalsIgnoreCase("")){	
						 progressDialog =ProgressDialog.show(TripPlannerActivity.this, "", "Loading map....");
						 mapIntent =new Intent(TripPlannerActivity.this,TripPlannerMapActivityNew.class);

						 new AsynGetCity().execute(new String[]{start_point,end_point});						 
						
					}else
						Toast.makeText(TripPlannerActivity.this, "Please Enter Destination Field", Toast.LENGTH_LONG).show();
					
			 break;
			 
			case R.id.tripplanner_addyourOwn_layout:
				
				//city_names.clear();				
				Intent addyourown_intent = new Intent(TripPlannerActivity.this,TripPlannerAddYourOwnActivity.class);				    
				startActivityForResult(addyourown_intent,REQUEST_CODE);
				
			 break;					
		
		}
     }
		
	
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		
		switch (seekBar.getId()) {
		
		case R.id.all_seekbar:
			
			all_checkbox.setChecked(true);
			unCheckRemaining();			
			
		break;
		case R.id.gasstation_seekbar:
			
			gas_checkbox.setChecked(true);
			unCheckAllCheckbox();					
			
		break;	
		case R.id.grocery_seekbar:
			
			grocery_checkbox.setChecked(true);
			unCheckAllCheckbox();					
			
		break;
		case R.id.masjid_seekbar:
			
			masjid_checkbox.setChecked(true);
			unCheckAllCheckbox();
			
			
		break;
		case R.id.restareas_seekbar:
			
			restAreas_checkbox.setChecked(true);
			unCheckAllCheckbox();			
			
		break;
		case R.id.restaurant_seekbar:
			
			restaurant_checkbox.setChecked(true);
			unCheckAllCheckbox();	
			
		break;

		
		}
		
		
		
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {}
	@Override
	public void onCheckedChanged(CompoundButton View, boolean isChecked) {		
		
		switch (View.getId()) {
		
		case R.id.tripplanner_all_chBox:
			
			if(isChecked){				
				unCheckRemaining();
			}
				
			
		break;
		case R.id.tripplanner_gasStation_chBox:
			
			if(isChecked){								
				unCheckAllCheckbox();			
			}
			
		break;	
		case R.id.tripplanner_Grocery_chBox:
			
			if(isChecked){								
				unCheckAllCheckbox();				
			}
			
		break;
		case R.id.tripplanner_masjid_chBox:
			
			if(isChecked){							
				unCheckAllCheckbox();				
			}	
			
		break;
		case R.id.tripplanner_Rest_chBox:
			
			if(isChecked){						
				unCheckAllCheckbox();				
			}
			
		break;
		case R.id.tripplanner_Restaurant_chBox:
			
			if(isChecked){							
				unCheckAllCheckbox();				
			}
			
		break;

		
		}
		
		
		
		
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		
		
		if(resultCode==RESULT_OK && requestCode==47){				
			
			mRouteModelFeedsdump  = (ArrayList<TripPlannerLocationPointsModel>) data.getExtras().getSerializable("data");
			
			for(int i=0;i<mRouteModelFeedsdump.size();i++){
				mRouteModelFeeds.add(mRouteModelFeedsdump.get(i)); 
			}
			
			for(int i=0;i<mRouteModelFeedsdump.size();i++){
				
				Log.v("citynames******",""+mRouteModelFeedsdump.get(i).getCityName());
			}
			
			
			  
			//unCheckRemaining();
			//unCheckAllCheckbox();
		
		}
		
	}
	
	
	private void unCheckAllCheckbox() {
		    all_checkbox.setChecked(false);		
	}
	private void unCheckRemaining() {
			gas_checkbox.setChecked(false);
			grocery_checkbox.setChecked(false);
			masjid_checkbox.setChecked(false);
			restAreas_checkbox.setChecked(false);
			restaurant_checkbox.setChecked(false);
		
	}
	
	
	
		Handler mHandler = new Handler() {    		
    	
			public void handleMessage(android.os.Message msg) { 
	    				
						if(msg.what==4){	    			
	    				
							String errorMsg=msg.getData().getString("HttpError");
//							if(errorMsg.length()>0)
//							{								
//								showAlertDialog();										
//							}else{
								
			    				routeCitiesCount++;
			    			 	if(routeCitiesCount < mRouteModelFeeds.size()-1){
			    			 		
			    			 		
//			    			 		model_object = null;
//			    			 		model_object = new TripPlannerLocationPointsModel();
//							  		model_object.setCityName(mRouteModelFeeds.get(routeCitiesCount).getCityName());
//							  		model_object.setAddressLat(mRouteModelFeeds.get(routeCitiesCount).getAddressLat());
//							  		model_object.setAddressLong(mRouteModelFeeds.get(routeCitiesCount).getAddressLong());
//							  		model_object.setMiddlecity(true);
//									model_object.setType("p");
//									model_object.setCategory("main");
//			    			 		loc_points_arr.add(model_object);	
			    			 		loc_points_arr.add(mRouteModelFeeds.get(routeCitiesCount));			 			    			 		
			    			 		getParseData();
			    			 		 
			    			 	}else{
			    			 		
//			    			 		model_object = null;
//			    			 		model_object = new TripPlannerLocationPointsModel();
//							  		model_object.setCityName(mRouteModelFeeds.get(routeCitiesCount).getCityName());
//							  		model_object.setAddressLat(mRouteModelFeeds.get(routeCitiesCount).getAddressLat());
//							  		model_object.setAddressLong(mRouteModelFeeds.get(routeCitiesCount).getAddressLong());							  		
//									model_object.setType("p");
//									model_object.setCategory("main");			    			 		
									//loc_points_arr.add(model_object);	
			    			 		
			    			 		loc_points_arr.add(mRouteModelFeeds.get(routeCitiesCount));	
									progressDialog.dismiss();
					    			Log.v("size of webservice date",""+loc_points_arr.size());
					    			mapIntent.putExtra("service_data", loc_points_arr);
					    			startActivity(mapIntent);
			    			 		
			    			 	}
						    // }
	    				
	    			
	    			
	     	}		
	    }	 
	};
	
	
	class AsynGetCity extends AsyncTask<String, Void, ArrayList<double[]>>{

		ArrayList<double[]> cities_latlong_arr = new ArrayList<double[]>();
		
		@Override
		protected ArrayList<double[]> doInBackground(String... params) {
			
			
			
			for(String param : params) {			
			
				cities_latlong_arr.add(getLatLong(getLocationInfo(param)));			
			}	
		
			return cities_latlong_arr;
		}
		
		private double[] getLatLong(JSONObject jsonObject) {

	        try {
	        	
	        		Log.v("status",""+(String)jsonObject.get("status"));
		        	 String status = (String)jsonObject.get("status"); 	 
		        	
		                
		        	 if(status.equalsIgnoreCase("OK")){             
		        		
		        		 Log.v("coming","**************************");
		        		 Double longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
			                     .getJSONObject("geometry").getJSONObject("location")
			                     .getDouble("lng");
			             			             	

			            Double latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
			                     .getJSONObject("geometry").getJSONObject("location")
			                     .getDouble("lat");		
			            
			            //city_names.add(new double[]{latitude,longitude});
			            
			            return  new double[]{latitude,longitude};
		        	 }
		        		 
		        	 
		        	 return null;
		        	    

	          }catch(JSONException e){
	        	
	        	e.printStackTrace();
	            return null;

	          }

	       
	    }

		@Override
		protected void onPostExecute(ArrayList<double[]> lat_long_arr) {
			
			Log.e("", "mRouteModelFeeds size "+mRouteModelFeeds.size());
			
			for(int i=0;i<lat_long_arr.size();i++){
				if(lat_long_arr.get(i)==null){
					showAlert = true;
				}
			}			
			if(showAlert){				
				showAlertDialog();
			}else{
				
				model_object = new TripPlannerLocationPointsModel();			
				model_object.setCityName(start_point);
				model_object.setAddressLat(lat_long_arr.get(0)[0]);			            
				model_object.setAddressLong(lat_long_arr.get(0)[1]);
				model_object.setType("p");
				model_object.setCategory("main");
			 	mRouteModelFeeds.add(0,model_object);
			 	model_object = null;
		        
		        Log.e("", "mRouteModelFeeds size "+mRouteModelFeeds.size());
		        
		        int mSize = mRouteModelFeeds.size();
		        
		        model_object = new TripPlannerLocationPointsModel();	
		        Log.v("end point ************",""+end_point);
		        model_object.setCityName(end_point);
		        model_object.setAddressLat(lat_long_arr.get(1)[0]);			            
		        model_object.setAddressLong(lat_long_arr.get(1)[1]);
		        model_object.setType("p");
				model_object.setCategory("main");
			 	mRouteModelFeeds.add(mSize,model_object);
			 	model_object = null;
				
		        Log.e("", "mRouteModelFeeds size "+mRouteModelFeeds.size());
					 
				
				if(all_checkbox.isChecked()){	
					 
					 sb_all_module = new StringBuilder();
			  		 String sep = "";
			  		 for(int i=0;i<module_ids.length;i++){
			  			sb_all_module.append(sep+module_ids[i]);
			  			sep = ",";
			  		 }
			  		 
			  		 sb_all_distance = new StringBuilder();
		  			 String sep1 = "";
			  		 for(int i=0;i<module_ids.length;i++){
			  			sb_all_distance.append(sep1+all_seekbar.getProgress());
			  			sep1 = ",";
			  		 }
					 
//			  		model_object = new TripPlannerLocationPointsModel();
//			  		model_object.setCityName(mRouteModelFeeds.get(routeCitiesCount).getCityName());
//			  		model_object.setAddressLat(mRouteModelFeeds.get(routeCitiesCount).getAddressLat());
//			  		model_object.setAddressLong(mRouteModelFeeds.get(routeCitiesCount).getAddressLong());			  	
//			  	
//					model_object.setType("p");
//					model_object.setCategory("main");
					
					loc_points_arr.add(mRouteModelFeeds.get(routeCitiesCount));				
					
					modules =   sb_all_module.toString();
					distances = sb_all_distance.toString();
			  		getParseData();
							 
							 
	 		   }else{				
				   
	 			   ArrayList<String> arr_few_modules = new ArrayList<String>();
	 			   ArrayList<String> arr_few_distances = new ArrayList<String>();
	 			   	if(gas_checkbox.isChecked()){		  			   		
	 			   		arr_few_modules.add(module_ids[0]);
	 			   		arr_few_distances.add(gasstation_seekbar.getProgress()+"");
	 			   	}if(grocery_checkbox.isChecked()){
		  			   	arr_few_modules.add(module_ids[1]);
	 			   		arr_few_distances.add(grocery_seekbar.getProgress()+"");
	 			   	}if(masjid_checkbox.isChecked()){
		  			   	arr_few_modules.add(module_ids[2]);
	 			   		arr_few_distances.add(masjid_seekbar.getProgress()+"");
	 			   	}if(restAreas_checkbox.isChecked()){
		  			   	arr_few_modules.add(module_ids[3]);
	 			   		arr_few_distances.add(restareas_seekbar.getProgress()+"");
	 			   	}if(restaurant_checkbox.isChecked()){
		  			   	arr_few_modules.add(module_ids[4]);
	 			   		arr_few_distances.add(restaurant_seekbar.getProgress()+"");
	 			   	}
				   	
					   
					 
					    sb_few_distance = new StringBuilder();
				  		String sep = "";
				  		for(int i=0;i<arr_few_distances.size();i++){
				  			
				  				sb_few_distance.append(sep+arr_few_distances.get(i));
				  				sep = ",";
				  			
				  		}	
				  		
				  		Log.v("sb few distance",""+sb_few_distance);
				  		sb_few_modules = new StringBuilder();
				  		String sep1 = "";
				  		for(int i=0;i<arr_few_modules.size();i++){	
				  			
				  				sb_few_modules.append(sep1+arr_few_modules.get(i));
				  				sep1 = ",";
				  				
				  		}	
				  		
				  		Log.v("sb few distance",""+sb_few_modules);
				  		
				  		
//				  		model_object = new TripPlannerLocationPointsModel();
//				  		model_object.setCityName(mRouteModelFeeds.get(routeCitiesCount).getCityName());
//				  		model_object.setAddressLat(mRouteModelFeeds.get(routeCitiesCount).getAddressLat());
//				  		model_object.setAddressLong(mRouteModelFeeds.get(routeCitiesCount).getAddressLong());				  	
//						model_object.setType("p");
//						model_object.setCategory("main");
//						loc_points_arr.add(model_object);		
				  		 
						loc_points_arr.add(mRouteModelFeeds.get(routeCitiesCount));	
						modules   = sb_few_modules.toString();
						distances = sb_few_distance.toString();
				  		getParseData();
				 					 
				 
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

	
	String modules, distances;
	
	
	private void  getParseData(){
		
		
  			url =String.format(Urls.TRIPPLANNER_URL
  					,mRouteModelFeeds.get(routeCitiesCount).getCityName()
  					,mRouteModelFeeds.get(routeCitiesCount+1).getCityName()
  					,modules
  					,distances);
  			
  			
  			TripPlannerLocationPointsParser mLocPointsParser= new TripPlannerLocationPointsParser(mHandler, url , loc_points_arr);
			mLocPointsParser.start();  	
  		
		
	}

	public void showAlertDialog() {
		
		new AlertDialogMsg(TripPlannerActivity.this, "No corresponding geographic location could be found for one of the specified addresses").setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

			@Override
				public void onClick(DialogInterface dialog, int which){							
				progressDialog.dismiss();
				showAlert = false;
				}
			
			}).create().show();
		
	}
	
}