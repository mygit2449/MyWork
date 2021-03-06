package com.daleelo.TripPlanner.Activities;

import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.Masjid.Helper.AlertDialogMessage;
import com.daleelo.TripPlanner.Model.TripPlannerRouteDetailsModel;
import com.daleelo.TripPlanner.Parser.TripPlannerMapRouteDetailsParser;


public class TripPlannerRouteWebView extends Activity {
	private ProgressDialog progressDialog;
	private ArrayList<TripPlannerRouteDetailsModel> mapRouteDetails_arr=null;	
	Intent receiveIntent;
	private ListView    dir_listView;
	private TextView    dir_heading;
	private ArrayList<String> citynames;	
	private boolean parsing_the_data = true;	
	private int cityCount = 0;
	ArrayList<String> distance_arr,duration_arr;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState); 	        
	    setContentView(R.layout.tripplanner_direction);
	    initializeUI();	         
	    citynames = (ArrayList<String>) receiveIntent.getExtras().get("citynames");
	    progressDialog = ProgressDialog.show(TripPlannerRouteWebView.this, "", "please wait");
	    getDirections();	
			
	}
                                                                     
	private void initializeUI() {
		
			dir_listView = (ListView)findViewById(R.id.tripplanner_dir_list);
	        dir_heading  = (TextView)findViewById(R.id.tripplanner_dir_heading);          
	        receiveIntent = getIntent();
	        mapRouteDetails_arr = new ArrayList<TripPlannerRouteDetailsModel>();
	        distance_arr	= new ArrayList<String>();
	        duration_arr	= new ArrayList<String>();
	        
	}

	private void getDirections(){
		
		try {
			
			String url ="http://maps.googleapis.com/maps/api/directions/xml?origin="+URLEncoder.encode(citynames.get(cityCount),"UTF-8")+"&destination="+URLEncoder.encode(citynames.get(++cityCount),"UTF-8")+"&getPolyline=true&sensor=true&mode=driving&units=imperial";
			Log.e("", "url "+url);
			TripPlannerMapRouteDetailsParser mTripPlannerMapRouteDetailsParser = new TripPlannerMapRouteDetailsParser(new MapRouteDetailsHandler(), url , mapRouteDetails_arr);
			mTripPlannerMapRouteDetailsParser.start();    
			
		} catch (Exception e) { 
			// TODO: handle exception
		}
		
		if(cityCount >= (citynames.size()-1))
			parsing_the_data = false;
		
	}     
    
    
   
    
    public void alertDialogWithMsg(String title, String msg)
	{			
		new AlertDialogMessage(TripPlannerRouteWebView.this, title, msg ).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){
	
			public void onClick(DialogInterface dialog, int which)
			{
								
					finish();			
			}
			
		   }).create().show();		
	}

    
    public String getTotalDuration(int totalMinutes) {   	
    	
    	
    	int days,hours,mins;
    	 days=hours=mins=0;
    	 if (totalMinutes<60){
    		 mins=totalMinutes;
    		 return mins+"min";
    	 }else if(totalMinutes>60){
    		 hours=(totalMinutes/60);
    	 	mins=(totalMinutes%60);
    	 	if(hours<24)
    	 	return hours+"hr"+" "+mins+"min";
    	 }
    	 if (hours>=24) {
    		 days=hours/24;
    		 hours=hours%24;
    		 if(days>1)
    			 return days+"days"+" "+hours+"hr";
    		 else
    			 return days+"day"+" "+hours+"hr"; 
    	 }

    	return "";
	}

		private int getMinutesfromString(String duration) {
			int InMinutes=0;
			ArrayList<String> duration_arr = new ArrayList<String>();
			
			 int count=1;
		        for(int i=0;i<duration.length();i++)
		        	if(duration.charAt(i)==' ')
		        		count++;
		        
			
			for(int i=0;i<count;i++){
				duration_arr.add(duration.split(" ")[i]);
			
			}
				
			
			  for (int i=1; i<duration_arr.size(); i+=2) {
				    
				    if (duration_arr.get(i).equalsIgnoreCase("day")||duration_arr.get(i).equalsIgnoreCase("days")){
				    	InMinutes=InMinutes+(Integer.parseInt(duration_arr.get(i-1))*24*60);
				    }else if(duration_arr.get(i).equalsIgnoreCase("hour")||duration_arr.get(i).equalsIgnoreCase("hours")){
				    	InMinutes=InMinutes+(Integer.parseInt(duration_arr.get(i-1))*60);
				    }else{					    	
				    	InMinutes=InMinutes+Integer.parseInt(duration_arr.get(i-1));
				    }	
			  }
			  
		 	  
		  return InMinutes;
		}

	
	private String getValuefromString(String string) {
		
		StringBuilder sb = new StringBuilder();
			if(string.indexOf("km")>0){			
			
				for(int i=0;i<string.length();i++){
					 
					if(string.charAt(i)=='k')
						break;
					if(string.charAt(i)!=',')
					sb.append(string.charAt(i));			
				}
				
				Log.v("sb.tostring",""+sb.toString());		
			
			
		   }else if(string.indexOf("mi")>0){		
			
				for(int i=0;i<string.length();i++){
					 
					if(string.charAt(i)=='m')
						break;
					if(string.charAt(i)!=',')
					sb.append(string.charAt(i));			
				}
		   }else if(string.indexOf("ft")>0){			
				
					sb.append("0");			
				
		   }
			return sb.toString();
	}

	public class DirectionAdapter extends BaseAdapter{
    	
    	
    	ArrayList<TripPlannerRouteDetailsModel> mDataFeeds;    	
    	

    	public DirectionAdapter(ArrayList<TripPlannerRouteDetailsModel> mDataFeeds) {
    		
        	this.mDataFeeds = mDataFeeds;          
        	
        }
		@Override
		public int getCount() 
		{			
			return mDataFeeds.size();
		}

		@Override
		public Object getItem(int position){	
			
			return position;
		}

		@Override
		public long getItemId(int position){	
			 
			return position;
		}

		@Override
		public View getView(int position, View v, ViewGroup parent)
		{

			
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.tripplanner_dir_list_row, null);
			 
				 TextView direction_txt	 = (TextView)v.findViewById(R.id.tripplanner_dir_textView);
				 ImageView direction_img = (ImageView)v.findViewById(R.id.tripplanner_dir_imgView);
				 ImageView line_img      = (ImageView)v.findViewById(R.id.tripplanner_dir_line_imgView);
				 TextView distance_txt	 = (TextView)v.findViewById(R.id.tripplanner_dir_distance_textView);
				 
				 if(mDataFeeds.get(position).getDest_ending().length()>0){
					 direction_txt.setText(mDataFeeds.get(position).getDest_ending());
					 direction_txt.setTextColor(Color.RED);
					 direction_txt.setTextSize(20);
					 direction_img.setImageResource(R.drawable.star_tripplanner);
					 distance_txt.setText(mDataFeeds.get(position).getDistance_text().trim());
				 }else{
					 String string =mDataFeeds.get(position).getHtml_instructions();				 
					 if(string!=null){
						 
							 direction_txt.setText(Html.fromHtml(string));
							 
						 if(string.indexOf("right")>=0)			
							 direction_img.setImageResource(R.drawable.right);
						 else if(string.indexOf("left")>=0)			
							 direction_img.setImageResource(R.drawable.left);
						 else
							 direction_img.setImageResource(R.drawable.up);
					 }
					 
					 if(mDataFeeds.get(position).getStep_distance_text()!=null)
						 distance_txt.setText(mDataFeeds.get(position).getStep_distance_text().trim()); 
				 }
				 line_img.setImageResource(R.drawable.line); 
				 
		
					 
			return v;
		}
		   	
}
    
	class MapRouteDetailsHandler extends Handler{
		
		
		 public void handleMessage(android.os.Message msg)
		 {
				
			    String errorMsg=msg.getData().getString("HttpError");
			    
			if(errorMsg.length()>0){	
				
				progressDialog.dismiss();
				alertDialogWithMsg("Daleelo",errorMsg);				
				
			}else{
				
				Log.v("maproute size",""+mapRouteDetails_arr.size());
				if(mapRouteDetails_arr.size()>0){
					
					distance_arr.add(mapRouteDetails_arr.get(mapRouteDetails_arr.size()-1).getDistance_text());
					duration_arr.add( mapRouteDetails_arr.get(mapRouteDetails_arr.size()-1).getDuration_text());
				   

					
					
					
					TripPlannerRouteDetailsModel ending_dest = new TripPlannerRouteDetailsModel();
					ending_dest.setDest_ending(citynames.get(cityCount));
					ending_dest.setDistance_text(mapRouteDetails_arr.get(mapRouteDetails_arr.size()-1).getDistance_text());
					mapRouteDetails_arr.remove(mapRouteDetails_arr.size()-1);
					mapRouteDetails_arr.add(ending_dest);
					
					
					if(parsing_the_data){					
						getDirections();				
						//parsing is completed then goes to else
					}else{	
						
						double total_distance = 0;
						int    total_duration = 0;				

						
						for(int i=0;i<distance_arr.size();i++){
							
							total_distance = total_distance + getDistanceFromString(distance_arr.get(i));
							total_duration = total_duration + getMinutesfromString(duration_arr.get(i));
							
						}
						
						String duration_str = getTotalDuration(total_duration);
						
						
						progressDialog.dismiss();
						
						if(citynames.size()>2)
							dir_heading.setText("Driving Directions from "+citynames.get(0)+" to "+citynames.get(citynames.size()-1)+" "+total_distance+"miles"+" "+duration_str);
						else
							dir_heading.setText("Driving Directions from "+citynames.get(0)+" to "+citynames.get(1)+" "+total_distance+"miles"+" "+duration_str);
				        dir_listView.setAdapter(new DirectionAdapter(mapRouteDetails_arr));
					}    
				}else{
					
					progressDialog.dismiss();
					dir_heading.setText("Driving Directions from "+receiveIntent.getStringExtra("start")+" to "+receiveIntent.getStringExtra("end")+" "+"Not Found");
				}
				
			}
					 
		}

		private double getDistanceFromString(String distance) {			
				
				String source_value; 
				try{
					 source_value  = getValuefromString(distance);   
				}catch(Exception e){
					 return 0;
				}		    	
		    		
		    	
				return Math.ceil(new Double(new Double(source_value)));
			
		}
}
    
}