package com.daleelo.TripPlanner.Activities;

import java.io.Serializable;
import java.util.ArrayList;



public class TripPlannerRoadModel implements Serializable{

	   public String mName; 
	   
       public String mDescription; 
	   
       public int mColor; 
	   
       public int mWidth; 
	   
       public double[][] mRoute = new double[][] {}; 
	   
       public TripPlannerPointsModel[] mPoints = new TripPlannerPointsModel[] {}; 
       
       public ArrayList<Double> lat_arr = new ArrayList<Double>();
       
       public ArrayList<Double> long_arr = new ArrayList<Double>();
       
	   
}
	 
 
	