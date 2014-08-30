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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.TripPlanner.Model.TripPlannerLocationPointsModel;



public class TripPlannerAddFromContacts extends Activity{
	
	
	private ListView contacts_listview;
	private ArrayList<String> contactNames_arr;	
	private ImageButton done_btn;
	private ProgressDialog progressDialog;
	private ArrayList<TripPlannerLocationPointsModel> mRouteCitiesFeeds;
	private TripPlannerLocationPointsModel mRouteCitiesModel;	
	private String already_added_msg = "This Contact Cityname is already added to POI's";
	private String invalid_address_msg = "This contact has invalid address";	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tripplanner_addfrom_contact);
		intializeUI();	
		
		
		Cursor names_cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, null, null, null);
		if(names_cursor.getCount()>0){
			
			 while(names_cursor.moveToNext()) { 

				 String contactname = names_cursor.getString(names_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				 contactNames_arr.add(contactname);
				 Log.v("contactname",""+contactname);
			
			 }
			 Log.v("contactNames_arr size",""+contactNames_arr.size());
			 contacts_listview.setAdapter(new ContactAdapter(contactNames_arr));
			
		}else{
			AlertMsgDialog("contacts are not available to show");
		}
		
		
		 
		 contacts_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				boolean exist = false;
				String cityname = "";
				Cursor citynames_cursor = getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,null, null, null, null);
				Log.v("on listvie w click",""+arg2);
				String name = contactNames_arr.get(arg2);
				Log.v("name",""+name);
				Log.v("count",""+citynames_cursor.getCount());
				
				 
				while(citynames_cursor.moveToNext()) { 

					 String contactname = citynames_cursor.getString(citynames_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME));
					 Log.v("contact name",""+contactname);
					 if(name.equalsIgnoreCase(contactname)){
					 	cityname =  citynames_cursor.getString(citynames_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
					 	Log.v("onclick ciyname",""+cityname);
					 	break;
					 }							
				}
			
				if(mRouteCitiesFeeds.size()>0){
					
					for(int i=0;i<mRouteCitiesFeeds.size();i++){
						 if(mRouteCitiesFeeds.get(i).getCityName().equalsIgnoreCase(cityname)){
							 Log.v(""+mRouteCitiesFeeds.get(i).getCityName(),""+cityname);
							 AlertMsgDialog(already_added_msg);
							 exist =true;
						 }
					}
					
				}
				
				if(!exist){
					if(cityname.length()>0){
						showAlertDialog(cityname);	
					}else{
						 AlertMsgDialog(invalid_address_msg);
					}
									
				}
				
				
			}
		 });
		 
		 done_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				

				 Intent returnIntent = new Intent();	
				 Log.v("size in done",""+mRouteCitiesFeeds.size());
				 if(mRouteCitiesFeeds.size()>0){
					
					 returnIntent.putExtra("data",mRouteCitiesFeeds);				     
				     setResult(RESULT_OK,returnIntent);    	
				     finish();
				 }else{
					 setResult(RESULT_CANCELED,returnIntent);   
					 finish();
				 }
			     
				
			}
		});
	}
	
	
	protected void showAlertDialog(final String cityname) {
		
		
		new AlertDialogMsg(TripPlannerAddFromContacts.this, "Do you want to add these addresses to the route").setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

			@Override
				public void onClick(DialogInterface dialog, int which) {							
				
				progressDialog = ProgressDialog.show(TripPlannerAddFromContacts.this, "", "please wait");					
				new AsynGetCity().execute(new String[]{cityname});
				
				}
			
			}).setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener(){

				@Override
					public void onClick(DialogInterface dialog, int which) {							
					
					
					}
				
				}).create().show();
		
	}


	protected void AlertMsgDialog(String msg) {
		
	
		new AlertDialogMsg(TripPlannerAddFromContacts.this, msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

			@Override
				public void onClick(DialogInterface dialog, int which) {							
				
				if(contactNames_arr.size()<1){
					Intent returnIntent = new Intent();	
					setResult(RESULT_CANCELED,returnIntent);   
					finish();
				}
				
				}
			
			}).create().show();
				
			
		
	}


	private void intializeUI() {
		
		contacts_listview = (ListView)findViewById(R.id.tripplanner_addfrom_contacts_LIST_view);
		contactNames_arr  =  new ArrayList<String>();
				
		mRouteCitiesFeeds = new ArrayList<TripPlannerLocationPointsModel>();
	    mRouteCitiesModel = new TripPlannerLocationPointsModel();
		done_btn = (ImageButton)findViewById(R.id.tripplanner_addcontact_done_btn);
	}
	

	class AsynGetCity extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			
			
			
			for(String param : params) {				
				
				 return getLatLong(getLocationInfo(param),param);
				
				
			}	
		
			return false;
		}
		
		private boolean getLatLong(JSONObject jsonObject, String cityname) {

	        try {
	        	
	        		Log.v("status",""+(String)jsonObject.get("status"));
		        	 String status = (String)jsonObject.get("status"); 	 
		        	 
		                
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
			
			
			if(!found){
				
				AlertMsgDialog(invalid_address_msg);
				
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
		
		
		class ContactAdapter extends BaseAdapter{
			
			ArrayList<String> contactNames_arr;
			public ContactAdapter(ArrayList<String> contactNames_arr) {

				this.contactNames_arr = contactNames_arr;
			}
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return contactNames_arr.size();
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return contactNames_arr.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public View getView(int position, View v, ViewGroup arg2) {
				 
				if (v == null){
					 LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                 v = inflater.inflate(R.layout.city_list_row, null);

	            }
				 
				 //RelativeLayout rl1=(RelativeLayout)v.findViewById(R.id.dispatchScreen_listview_main);
				 
				 TextView contact_name		 = (TextView)v.findViewById(R.id.city_TXT_list_row_name);			
				 contact_name.setText(contactNames_arr.get(position));				 
				 
				  return v;
			}
			
		}

		}
