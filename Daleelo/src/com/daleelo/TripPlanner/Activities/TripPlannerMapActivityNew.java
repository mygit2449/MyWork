package com.daleelo.TripPlanner.Activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.balloon.readystatesoftware.maps.OnSingleTapListener;
import com.balloon.readystatesoftware.maps.TapControlledMapView;
import com.daleelo.R;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.TripPlanner.Helper.TripPlannerCustomOverlayItem;
import com.daleelo.TripPlanner.Helper.TripPlannerPathOverLay;
import com.daleelo.TripPlanner.Model.TripPlannerLocationPointsModel;
import com.daleelo.TripPlanner.Parser.TripPlannerLocationPointsParserNew;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.Overlay;



public class TripPlannerMapActivityNew extends MapActivity{
	
	
	
	//widgets
	private TapControlledMapView mapView = null;
	private ImageView routelist_iv=null;
	private TextView  start_to_end_txt=null;
	private TextView  start_to_end_distance_txt=null;
	private ProgressDialog progressDialog;	
	private Drawable defaultdrawable,gasstation_drawable,groceries_drawable,masjid_drawable,
	restareas_drawable,restaurant_drawable,start_drawable,middle_drawable;
	Intent  receive_intent;	
	//convert value from km to miles 1.609344000000865	
	double[] start_point = new double[2];
	double[] end_point   = new double[2];	
	private ArrayList<TripPlannerRoadModel> mRoad = new ArrayList<TripPlannerRoadModel>();	
	public  ArrayList<String> citynames = null; 
	public  ArrayList<String> citynames_dump = null; 	
	private ArrayList<Double> dist_arr = null ;
	private TripPlannerCustomizedOverlay<TripPlannerCustomOverlayItem> mLocationOverlay = null;	
	private TripPlannerPathOverLay mpathOverlay = null;	
	public ArrayList<TripPlannerLocationPointsModel> loc_points_arr = null;		
	private String start_loc = "";
	private String end_loc   = "";		
	int city_count=0;
	double total_distance=0;
	ArrayList<double[]> latlong_arr = null;		  
  

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        setContentView(R.layout.tripplanner_mapview);             
        intializeUI();              
        showRoute();  	
         
	       
         routelist_iv.setOnClickListener(new OnClickListener(){        	 
			
			@Override
			public void onClick(View v) {				
				Intent route_intent =new Intent(TripPlannerMapActivityNew.this,TripPlannerRouteWebView.class);			
				
				final ArrayList<String> citynames = new ArrayList<String>();
				  
				  for(int i=0;i<loc_points_arr.size();i++){
					  if(loc_points_arr.get(i).getCategory().equalsIgnoreCase("main")){
						 if(loc_points_arr.get(i).getType().equalsIgnoreCase("t")){
							 StringBuffer sb = new StringBuffer();
							 sb.append((loc_points_arr.get(i).getAddressLine1().length()>0)?loc_points_arr.get(i).getAddressLine1()+",":""); 
							 sb.append((loc_points_arr.get(i).getAddressLine2().length()>0)?loc_points_arr.get(i).getAddressLine2()+",":"");
							 sb.append(loc_points_arr.get(i).getCityName());
							 citynames.add(sb.toString());
						 }else{
							 citynames.add(loc_points_arr.get(i).getCityName());
						 }
						 	
					  }
				  }		
				
				route_intent.putExtra("citynames", citynames);				
				startActivity(route_intent);							
			}
		});
        
         mapView.setOnSingleTapListener(new OnSingleTapListener() {		
      	  
			public boolean onSingleTap(MotionEvent e) {
				mLocationOverlay.hideAllBalloons();				
				return true;
			}
         });
      
       
	      
	          	
    } 
	

	
	
	
	 
	 //intialize the widgets
	 private void intializeUI() {		
		 
		 	receive_intent = getIntent();
	        loc_points_arr = (ArrayList<TripPlannerLocationPointsModel>) receive_intent.getExtras().get("service_data");
	        //getting the intent data  
	        start_loc = loc_points_arr.get(0).getCityName();       	
	        end_loc   = loc_points_arr.get(loc_points_arr.size()-1).getCityName();        
	       	     
		           
			mapView 				 = (TapControlledMapView)findViewById(R.id.map_view);
			mapView.setBuiltInZoomControls(true);     
			citynames				 = new ArrayList<String>();
	    	//global_lat_long_values   = new ArrayList<double[]>();
	    
			routelist_iv 			 = (ImageView)findViewById(R.id.routepath_list_imgView);		
			defaultdrawable			 = this.getResources().getDrawable(R.drawable.pin_red);
			middle_drawable		     = this.getResources().getDrawable(R.drawable.pin_purple);
			middle_drawable.setBounds(-middle_drawable.getIntrinsicWidth()/2, -middle_drawable.getIntrinsicHeight(), middle_drawable.getIntrinsicWidth()/2, 0); 
			start_drawable		     = this.getResources().getDrawable(R.drawable.pin_green);
			start_drawable.setBounds(-start_drawable.getIntrinsicWidth()/2, -start_drawable.getIntrinsicHeight(), start_drawable.getIntrinsicWidth()/2, 0);
			//start_drawable.setBounds(0,0,0,0); 
		    gasstation_drawable		 = this.getResources().getDrawable(R.drawable.gasstation_pin);
		    gasstation_drawable.setBounds(0, 0, gasstation_drawable.getIntrinsicWidth(), gasstation_drawable.getIntrinsicHeight()); 
		    
		    groceries_drawable		 = this.getResources().getDrawable(R.drawable.grocery_pin);
		    groceries_drawable.setBounds(0, 0, groceries_drawable.getIntrinsicWidth(), groceries_drawable.getIntrinsicHeight()); 
		    
		    masjid_drawable		 	 = this.getResources().getDrawable(R.drawable.masjid_pin);
		    masjid_drawable.setBounds(0, 0, masjid_drawable.getIntrinsicWidth(), masjid_drawable.getIntrinsicHeight()); 
		    
		    restareas_drawable		 = this.getResources().getDrawable(R.drawable.restareas_pin);
		    restareas_drawable.setBounds(0, 0, restareas_drawable.getIntrinsicWidth(), restareas_drawable.getIntrinsicHeight()); 
		    
		    restaurant_drawable		 = this.getResources().getDrawable(R.drawable.restaurant_pin);
		    restaurant_drawable.setBounds(0, 0, restaurant_drawable.getIntrinsicWidth(), restaurant_drawable.getIntrinsicHeight()); 
		    
		    start_to_end_txt			 = (TextView)findViewById(R.id.from_to_end_txtView);
		    start_to_end_distance_txt	 = (TextView)findViewById(R.id.from_to_end_distance_txtView);
		    start_to_end_txt.setText(start_loc + " - " + end_loc);  
		    
		}

	 
	
	
	  //it shows the path on the map
	  public void showRoute(){	    	
		   latlong_arr = null;
		   progressDialog =ProgressDialog.show(this, "", "Refreshing....");			  
		   latlong_arr = new ArrayList<double[]>();		  
		   for(int i=0;i<loc_points_arr.size();i++){
			  if(loc_points_arr.get(i).getCategory().equalsIgnoreCase("main")){
				  latlong_arr.add(new double[]{loc_points_arr.get(i).getAddressLat(),loc_points_arr.get(i).getAddressLong()});
			  }
		  }
		  
	       mRoad.clear();	 	         				
		   addRouteOnMap(latlong_arr.get(city_count)[0], latlong_arr.get(city_count)[1], latlong_arr.get(city_count+1)[0], latlong_arr.get(city_count+1)[1]);
		    	    
	}  
   

     protected void addRouteOnMap(double fromLat,double fromLon,double toLat,double toLon) {	  	
	  	String url = String.format("http://maps.googleapis.com/maps/api/directions/xml?origin=%s,%s&destination=%s,%s&sensor=false",fromLat,fromLon,toLat,toLon);   
	     	TripPlannerLocationPointsParserNew path_obj = new TripPlannerLocationPointsParserNew(mHandler,url);
	     	path_obj.start();   	
		
	}
    
    //handler show the progress until complete the task
	Handler mHandler = new Handler() { 		
		
		
    	public void handleMessage(android.os.Message msg) { 

	    	
    		
	   if(msg.what==10){
		    progressDialog.dismiss();
	   		String errorMsg = msg.getData().getString("HttpError");
	   	   	if(errorMsg.length()>0){
		   	   		
		   	   	new AlertDialogMsg(TripPlannerMapActivityNew.this, "No corresponding geographic location could be found for one of the specified addresses").setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){
	
					@Override
					public void onClick(DialogInterface dialog, int which) {						
	
						finish();
						
					}
					
				}).create().show();
	   	   	
	   	   	}else{
	   	   		
		   	   	TripPlannerRoadModel mTripPlannerRoadModel = (TripPlannerRoadModel) msg.getData().getSerializable("points");  	  
		   	   
		   		mRoad.add(mTripPlannerRoadModel);
		   		
		   		
		   		
	   		if(latlong_arr.size()==mRoad.size()+1){
	   			
	   			    progressDialog.dismiss();
		   			city_count=0;
		   			mapView.getOverlays().clear();    		
		    		mLocationOverlay = new TripPlannerCustomizedOverlay<TripPlannerCustomOverlayItem>(defaultdrawable, TripPlannerMapActivityNew.this,mapView);
		    		mpathOverlay     = new TripPlannerPathOverLay(mRoad, mapView);
		    		total_distance = 0; 
			  		dist_arr = new ArrayList<Double>();	   				
			  		
			  		Log.v("road size******",""+mRoad.size());
			  	    if(mRoad.size()>0){
			  	    	
			  	    	for(int i=0;i<mRoad.size();i++){
			  				
			  				String distance = mRoad.get(i).mDescription;
			  				double dist_double = getOnlyDistanceValue(distance);
			  				dist_arr.add(dist_double);
			  				Log.v("dist arr",""+dist_arr.get(i));
			  				total_distance  = total_distance +  dist_double;		  				
			  			}
			  	    	
			  	    }else{
			  	    		dist_arr.add((double)0);
			  	    }
			  	    	//setMapZoomLevel();
			  			start_to_end_distance_txt.setText("Distance - " + total_distance+ " Miles ");		  			
			  			
				    	List<Overlay> mapOverlays = mapView.getOverlays(); 	    	
				  		addingOverlayItems();		  		
				  		mapOverlays.add(mpathOverlay);    	  		
				  		mapOverlays.add(mLocationOverlay);  
				  		mapView.invalidate();  
	   			
	   		}else{
	   				city_count++;
	   				addRouteOnMap(latlong_arr.get(city_count)[0], latlong_arr.get(city_count)[1], latlong_arr.get(city_count+1)[0], latlong_arr.get(city_count+1)[1]);
	   		}
	   		
		  		
	   		 	 
	   		
	   	}	
	   	   	
	   	   		
	   	   	}
	   	
	   		
	   }; 
	    
	   
  };  
    
    
  

		protected void addingOverlayItems() {
		int i=0;
		for(int iCtr = 0; iCtr < loc_points_arr.size();iCtr++){
			
			
			if(loc_points_arr.get(iCtr).getCategory().equalsIgnoreCase("main")){
				if(loc_points_arr.get(iCtr).getType().equalsIgnoreCase("t")&& loc_points_arr.get(iCtr).isMiddlecity()){
					
					mLocationOverlay.addOverlay(new TripPlannerCustomOverlayItem( new GeoPoint((int) (new Double(loc_points_arr.get(iCtr).getAddressLat())*1E6)
							,(int) (new Double(loc_points_arr.get(iCtr).getAddressLong())*1E6))
						    ,loc_points_arr.get(iCtr).getBusinessTitle()
						    ,loc_points_arr.get(iCtr).getAddressLine1()+ "," +loc_points_arr.get(iCtr).getAddressLine2()
						    ,TripPlannerMapActivityNew.this,middle_drawable
						    ,TripPlannerCustomOverlayItem.MIDDLE_REMOVE));
				}else if(loc_points_arr.get(iCtr).getType().equalsIgnoreCase("t")){
					
					mLocationOverlay.addOverlay(new TripPlannerCustomOverlayItem( new GeoPoint((int) (new Double(loc_points_arr.get(iCtr).getAddressLat())*1E6)
							,(int) (new Double(loc_points_arr.get(iCtr).getAddressLong())*1E6))
							,loc_points_arr.get(iCtr).getBusinessTitle()
							,dist_arr.get(i)+" Mi Awary From"				
						    ,TripPlannerMapActivityNew.this,middle_drawable
						    ,TripPlannerCustomOverlayItem.MIDDLE_REMOVE));
					i++;
				}else if(loc_points_arr.get(iCtr).getType().equalsIgnoreCase("p") && loc_points_arr.get(iCtr).isMiddlecity()){
					
					mLocationOverlay.addOverlay(new TripPlannerCustomOverlayItem( new GeoPoint((int) (new Double(loc_points_arr.get(iCtr).getAddressLat())*1E6)
							,(int) (new Double(loc_points_arr.get(iCtr).getAddressLong())*1E6))
						,loc_points_arr.get(iCtr).getCityName(),dist_arr.get(i)+" Mi Awary From"
						,TripPlannerMapActivityNew.this
						,middle_drawable
						,TripPlannerCustomOverlayItem.MIDDLE_REMOVE));
					
					i++;
				}else{
					if(iCtr ==loc_points_arr.size()-1){
						mLocationOverlay.addOverlay(new TripPlannerCustomOverlayItem( new GeoPoint((int) (new Double(loc_points_arr.get(iCtr).getAddressLat())*1E6)
								,(int) (new Double(loc_points_arr.get(iCtr).getAddressLong())*1E6))
								,loc_points_arr.get(iCtr).getCityName(),dist_arr.get(i)+" Mi Awary From"
								,TripPlannerMapActivityNew.this
								,defaultdrawable
								,TripPlannerCustomOverlayItem.DESTINATION));
					}else{
						mLocationOverlay.addOverlay(new TripPlannerCustomOverlayItem( new GeoPoint((int) (new Double(loc_points_arr.get(iCtr).getAddressLat())*1E6)
								,(int) (new Double(loc_points_arr.get(iCtr).getAddressLong())*1E6))
								,loc_points_arr.get(iCtr).getCityName(),"",TripPlannerMapActivityNew.this
								,start_drawable
								,TripPlannerCustomOverlayItem.SOURCE));
					}	
				}
  					
  			}else if(loc_points_arr.get(iCtr).getCategoryName().equalsIgnoreCase("Gas stations")){		  					
  				mLocationOverlay.addOverlay(new TripPlannerCustomOverlayItem( new GeoPoint((int) (new Double(loc_points_arr.get(iCtr).getAddressLat())*1E6)
  						,(int) (new Double(loc_points_arr.get(iCtr).getAddressLong())*1E6))
  						,loc_points_arr.get(iCtr).getBusinessTitle()
  						,loc_points_arr.get(iCtr).getAddressLine1()+ "," +loc_points_arr.get(iCtr).getAddressLine2()
  						,TripPlannerMapActivityNew.this
  						,gasstation_drawable
  						,TripPlannerCustomOverlayItem.MIDDLE_ADD));
  					
  			}else if(loc_points_arr.get(iCtr).getCategoryName().equalsIgnoreCase("Groceries")){		  					
  				mLocationOverlay.addOverlay(new TripPlannerCustomOverlayItem( new GeoPoint((int) (new Double(loc_points_arr.get(iCtr).getAddressLat())*1E6)
  						,(int) (new Double(loc_points_arr.get(iCtr).getAddressLong())*1E6))
  						,loc_points_arr.get(iCtr).getBusinessTitle()
  						,loc_points_arr.get(iCtr).getAddressLine1()+ "," +loc_points_arr.get(iCtr).getAddressLine2()
  						,TripPlannerMapActivityNew.this
  						,groceries_drawable
  						,TripPlannerCustomOverlayItem.MIDDLE_ADD));
  					
  			}else if(loc_points_arr.get(iCtr).getCategoryName().equalsIgnoreCase("Mosques/Islamic Centers")){		  					
  				mLocationOverlay.addOverlay(new TripPlannerCustomOverlayItem( new GeoPoint((int) (new Double(loc_points_arr.get(iCtr).getAddressLat())*1E6)
  						,(int) (new Double(loc_points_arr.get(iCtr).getAddressLong())*1E6))
  				,loc_points_arr.get(iCtr).getBusinessTitle()
  				,loc_points_arr.get(iCtr).getAddressLine1()+ "," +loc_points_arr.get(iCtr).getAddressLine2()
  				,TripPlannerMapActivityNew.this
  				,masjid_drawable
  				,TripPlannerCustomOverlayItem.MIDDLE_ADD));
  					
  			}else if(loc_points_arr.get(iCtr).getCategoryName().equalsIgnoreCase("Rest Areas")){		  					
  				mLocationOverlay.addOverlay(new TripPlannerCustomOverlayItem( new GeoPoint((int) (new Double(loc_points_arr.get(iCtr).getAddressLat())*1E6)
  						,(int) (new Double(loc_points_arr.get(iCtr).getAddressLong())*1E6))
  						,loc_points_arr.get(iCtr).getBusinessTitle()
  						,loc_points_arr.get(iCtr).getAddressLine1()+ "," +loc_points_arr.get(iCtr).getAddressLine2()
  						,TripPlannerMapActivityNew.this
  						,restareas_drawable
  						,TripPlannerCustomOverlayItem.MIDDLE_ADD));
  					
  			}else if(loc_points_arr.get(iCtr).getCategoryName().equalsIgnoreCase("Restaurants")){		  					
  				mLocationOverlay.addOverlay(new TripPlannerCustomOverlayItem( new GeoPoint((int) (new Double(loc_points_arr.get(iCtr).getAddressLat())*1E6)
  						,(int) (new Double(loc_points_arr.get(iCtr).getAddressLong())*1E6))
  						,loc_points_arr.get(iCtr).getBusinessTitle()
  						,loc_points_arr.get(iCtr).getAddressLine1()+ "," +loc_points_arr.get(iCtr).getAddressLine2()
  						,TripPlannerMapActivityNew.this
  						,restaurant_drawable
  						,TripPlannerCustomOverlayItem.MIDDLE_ADD));
  					
  			}								
					
  		}
		
	}



	protected void setMapZoomLevel() {
			if(total_distance<50){
	    		mapView.getController().setZoom(12);
	    	}else if(total_distance>50 && total_distance<200){
	    		mapView.getController().setZoom(7);
	    	}else if(total_distance>200 && total_distance<300){
	    		mapView.getController().setZoom(7);
	    	}else if(total_distance>300 && total_distance<400){
	    		mapView.getController().setZoom(7);
	    	}else if(total_distance<500){
	    		mapView.getController().setZoom(7);
	    	}else {
	    		mapView.getController().setZoom(7);
	    	}
	}






	protected double getOnlyDistanceValue(String string){
		
		Log.v("sting is ",string);
		Log.v("srng lengjth",""+string.length());
		if(string.indexOf("km")>0){
			
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<string.length();i++){
				 
				if(string.charAt(i)=='k')
					break;
				if(string.charAt(i)!=',')
				sb.append(string.charAt(i));			
			}
			
			Log.v("sb.tostring",""+sb.toString());
			return Math.ceil(new Double(new Double(sb.toString())/1.609344000000865));
			
			
		}else if(string.indexOf("mi")>0){

			
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<string.length();i++){
				 
				if(string.charAt(i)=='m')
					break;
				if(string.charAt(i)!=',')
				sb.append(string.charAt(i));			
			}
			
			Log.v("sb.tostring",""+sb.toString());
			return Math.ceil(new Double(sb.toString()));
			
	
		
			
		}
		
	//	07-25 15:26:21.683: V/sting is(31035): Distance: 0.0mi (about 0 secs)
	//	07-25 15:27:53.142: V/sting is(31073): Distance: 164km (about 2 hours 52 mins)

		return 0;
	}


	

	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

	@Override
	public void onBackPressed() {
       loc_points_arr.clear();
		super.onBackPressed();
	}


		
		
	
}
    
    
