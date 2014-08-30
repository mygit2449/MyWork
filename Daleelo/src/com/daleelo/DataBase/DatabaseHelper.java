package com.daleelo.DataBase;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.daleelo.Business.Model.BusinessListModel;
import com.daleelo.DashBoardClassified.Model.GetClassifiedItemsModel;
import com.daleelo.DashBoardEvents.Model.EventsCalenderModel;
import com.daleelo.Dashboard.Model.GetDealsInfoModel;
import com.daleelo.Dashboard.Model.GetSpotLightModel;
import com.daleelo.Hasanat.Model.BusinessDetailModel;
import com.daleelo.Main.Model.GetCitiesModel;
import com.daleelo.Main.Model.GetSearchKeyModel;
import com.daleelo.Masjid.Model.MasjidModel;
import com.daleelo.Qiblah.Activites.PrayerTimeModel;

public class DatabaseHelper extends SQLiteOpenHelper{
 
    //The Android's default system path of your application database.
    String DB_PATH ;
    private static String DB_NAME = "Daleelo.sqlite";
 
    private SQLiteDatabase 		myDataBase = null; 
    private final Context 		myContext;
	
    private final String		CITIES_TABLE = "AppCities";
    private final String		RESENT_CITIES_TABLE = "RecentAppCities";
    private final String		RESENT_SEARCH_KEY = "RecentSearchKey";    
    
	private final String		BUSINESS_TABLE = "BusinessList";
	private final String		SPOT_LIGHT_TABLE = "SpotlightList";
	private final String		DEALS_TABLE = "DealList";
	private final String		EVENTS_TABLE = "EventList";
	private final String		CLASSIFIED_TABLE = "ClassifiedList";
	private final String		HASANAT_TABLE = "HasanatList";
	private final String		MASJID_TABLE = "MasjidList";	

	private final String 		PRAYER_TIMINGS = "PrayerTimings";
	

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     * @throws IOException 
     */
    public DatabaseHelper(Context context) throws IOException 
    {
    	super(context, DB_NAME, null, 1);

    	//super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH="/data/data/" + context.getPackageName() + "/databases/";
        Log.v("The Database Path" , DB_PATH);
        createDataBase();
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	//this.getReadableDatabase();
 
        	try {
    			copyDataBase();
    			
        	}
        	catch(SQLiteException ex){
        			ex.printStackTrace();
        		throw new Error("Error copying database");
        	
        	} catch (IOException e) {
        		e.printStackTrace();
        		throw new Error("Error copying database");
        	}
    		
    		try{
    			openDataBase();
    		}
    		catch(SQLiteException ex){
    			ex.printStackTrace();
    			throw new Error("Error opening database");
    			
    		}catch(Exception e){
    			e.printStackTrace();
    			throw new Error("Error opening database");
    		}
    		
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	//SQLiteDatabase checkDB = null;
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
    	}
    	//if(myDataBase != null){
     	//	checkDB.close();
     	//}
 
    	return myDataBase != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
    	
    	File dbDir = new File(DB_PATH);
        if (!dbDir.exists()) {
            dbDir.mkdir();
            
        }
    	//Open your local db as the input stream
    	Log.v("1 Opening the assest folder db " , myContext.getAssets() + "/"+ DB_NAME);
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	Log.i("2 Copy the db to the path " , outFileName);
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
    	//transfer bytes from the inputfile to the outputfile
    	Log.i("Open the output db " , outFileName);
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
    	Log.i("Copied the database file" , outFileName);
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException
    {
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
        
        if(myDataBase!=null)
    	{
        	
    		myDataBase.close();
    		myDataBase=null;
    		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);    		
    		
    	}else{
        
    		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        
    	}
 
    }
 
    public void closeDataBase()
    {
    	
    	if(myDataBase!=null)
    	{
    		myDataBase.close();
    		myDataBase=null;
    	}
    	
    }
    
    public void insertCity(String cityId, String cityName, String stateCode, String latitude, String longitude, String country_code ) {
    	
    	ContentValues cv = new ContentValues();
    	cv.put("CityId", cityId);
    	cv.put("CityName", cityName);
    	cv.put("StateCode", stateCode);
    	cv.put("Latitude", latitude);
    	cv.put("Longitude", longitude);
    	cv.put("CountryCode", country_code);
    	try {
    		
    		Log.e("return value"," "+myDataBase.insertWithOnConflict(CITIES_TABLE, null, cv,myDataBase.CONFLICT_IGNORE));
    		
    	} catch (Exception e) {
    		// TODO: handle exception
    	}
//		updateDatabase("INSERT INTO " + CITIES_TABLE +" VALUES('"+cityId+"','"+cityName+"', '"+stateCode+"','"+latitude+"', '"+longitude+"');");
    	
    }	
    
    
    public ArrayList<GetCitiesModel> getCitiesFromDB()
	{
    		    
		       ArrayList<GetCitiesModel> result = new ArrayList<GetCitiesModel>();
		    	Cursor c = qureyDatabase("select * from " +CITIES_TABLE+";");
		    	
		    	Log.e("cursorLength*******", c.getCount()+".");
		    	if (c.getCount() > 0) {
		            c.moveToFirst();
		            do {
		            	GetCitiesModel mDatabaseModel = new GetCitiesModel();
		                mDatabaseModel.setCityID(c.getString(c.getColumnIndex("CityId")));
		                mDatabaseModel.setCityName(c.getString(c.getColumnIndex("CityName")));
		                mDatabaseModel.setStateCode(c.getString(c.getColumnIndex("StateCode")));
		                mDatabaseModel.setLatitude(c.getString(c.getColumnIndex("Latitude")));
		                mDatabaseModel.setLongitude(c.getString(c.getColumnIndex("Longitude")));
		                mDatabaseModel.setCountry_code(c.getString(c.getColumnIndex("CountryCode")));
		                result.add(mDatabaseModel);
		                mDatabaseModel = null;
		            	
		            } while (c.moveToNext());
		        }
		    	c.close();
		        return result;
	}
    
    public void insertRecentCity(String cityId, String cityName, String stateCode, String latitude, String longitude ,String country_code) {
    	try {
			
    		ContentValues cv = new ContentValues();
    		cv.put("CityName", cityName);
    		cv.put("StateCode", stateCode);
    		cv.put("Latitude", latitude);
    		cv.put("Longitude", longitude);
    		cv.put("CountryCode", country_code);
    		Log.e("return value"," "+myDataBase.insertWithOnConflict(RESENT_CITIES_TABLE, null, cv,myDataBase.CONFLICT_IGNORE));
    		
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    }
    
    
    public ArrayList<GetCitiesModel> getRecentCitiesFromDB()
	{
    		    
		       ArrayList<GetCitiesModel> result = new ArrayList<GetCitiesModel>();
		    	Cursor c = qureyDatabase("select * from " +RESENT_CITIES_TABLE+";");
		    	
		    	Log.e("cursorLength*******", c.getCount()+".");
		    	if (c.getCount() > 0) {
		            c.moveToFirst();
		            do {
		            	GetCitiesModel mDatabaseModel = new GetCitiesModel();
		                mDatabaseModel.setCityName(c.getString(c.getColumnIndex("CityName")));
		                mDatabaseModel.setStateCode(c.getString(c.getColumnIndex("StateCode")));
		                mDatabaseModel.setLatitude(c.getString(c.getColumnIndex("Latitude")));
		                mDatabaseModel.setLongitude(c.getString(c.getColumnIndex("Longitude")));
		                mDatabaseModel.setCountry_code(c.getString(c.getColumnIndex("CountryCode")));
		                result.add(mDatabaseModel);
		                mDatabaseModel = null;
		            	
		            } while (c.moveToNext());
		        }
		    	c.close();
		        return result;
	}    
    
    
    
    public void insertSearchKey(String SearchKey) {
    	
    	ContentValues cv = new ContentValues();
    	cv.put("SearchKey", SearchKey);
    	Log.e("return value"," "+myDataBase.insertWithOnConflict(RESENT_SEARCH_KEY, null, cv,myDataBase.CONFLICT_IGNORE));
    	
    }
    
    
 	public ArrayList<String> getRecentSearchKeys()
	{
 		    
		ArrayList<String> result = new ArrayList<String>();
		Cursor c = qureyDatabase("select * from " +RESENT_SEARCH_KEY+";");
		  	
		Log.e("cursorLength*******", c.getCount()+".");
			if (c.getCount() > 0) {
				c.moveToFirst();
		        	do {
		        		
		            	String searchKey = c.getString(c.getColumnIndex("SearchKey"));
		                result.add(searchKey);
		                
		            } while (c.moveToNext());
		        }
		c.close();
		return result;
	}  
    
 	
 	/*
 	 *  Prayer timings
 	 */
 	
	public long insertPrayerTimings(int date,int month,int year,String fajr,String shuruq,String dhuru,
			String asr,String maghrib,String isha){
		ContentValues cv = new ContentValues();
		cv.put("date", date);
		cv.put("month", month);
		cv.put("year", year);
		cv.put("fajr", fajr);
		cv.put("shuruq", shuruq);
		cv.put("dhuhr", dhuru);
		cv.put("asr", asr);
		cv.put("maghrib", maghrib);
		cv.put("isha", isha);
		return myDataBase.insertWithOnConflict(PRAYER_TIMINGS, null, cv,myDataBase.CONFLICT_IGNORE);		
	}
	
	public PrayerTimeModel getPrayerTimingsbyDay(String date,String month){
		PrayerTimeModel prayerTimes = new PrayerTimeModel();
		Cursor cursor;
		cursor = myDataBase.query(PRAYER_TIMINGS, null, "date = ? AND month = ?", new String[]{date,month}, null, null, null);
		Log.e("cursor length", cursor.getCount()+" null");
		
		if(cursor != null && cursor.moveToFirst()){
			prayerTimes.prayerTimings = new ArrayList<String>();
			prayerTimes.prayerTimings.add(cursor.getString(cursor.getColumnIndex("fajr")));
			prayerTimes.prayerTimings.add(cursor.getString(cursor.getColumnIndex("shuruq")));
			prayerTimes.prayerTimings.add(cursor.getString(cursor.getColumnIndex("dhuhr")));
			prayerTimes.prayerTimings.add(cursor.getString(cursor.getColumnIndex("asr")));
			prayerTimes.prayerTimings.add(cursor.getString(cursor.getColumnIndex("maghrib")));
			prayerTimes.prayerTimings.add(cursor.getString(cursor.getColumnIndex("isha")));			
			return prayerTimes;
		}
		
		cursor.close();
		return null;
	}
	

	public PrayerTimeModel getAlltimesByType(String type){
		PrayerTimeModel prayerTimes = new PrayerTimeModel();
		Cursor cursor;
		cursor = myDataBase.rawQuery("select * from PrayerTimings", null);
		Log.e("cursor length", cursor.getCount()+" null");
		if (cursor.getCount() > 0) {
			int iCtr = 0;
			prayerTimes.prayerTimings = new ArrayList<String>();
			cursor.moveToPosition(iCtr);
            do 
            {
            	prayerTimes.prayerTimings.add(cursor.getString(cursor.getColumnIndex(type)));
    			iCtr++;
            } while (cursor.moveToNext());
        }
//		cursor.close();
        return prayerTimes;
		
	}

	
	
	public void deletPrayTimes(){
		myDataBase.delete(PRAYER_TIMINGS, null, null);
	}
 	
 	
	/*
	 *  
	 *  Business Listing  methods
	 * 
	 */
	
	public long insertBusinessItem(String BusinessId, String BusinessTitle, String BusinessAddress, String Rating, 
			String ReviewsCount, String Latitude, String Longitude , String CityName ) {

		ContentValues cv = new ContentValues();
		cv.put("BusinessId", BusinessId);
		cv.put("BusinessTitle", BusinessTitle);
		cv.put("BusinessAddress", BusinessAddress);
		cv.put("Rating", Rating);
		cv.put("ReviewsCount", ReviewsCount);
		cv.put("Latitude", Latitude);
		cv.put("Longitude", Longitude);
		cv.put("CityName", CityName);
		
		long result = myDataBase.insertWithOnConflict(BUSINESS_TABLE, null, cv,myDataBase.CONFLICT_IGNORE);
		Log.e("return value","result "+result);
		return result;		

	}
	
	
	
	public void deleteBusinessItem(String key){
		
		updateDatabase("delete from "+BUSINESS_TABLE+" where BusinessId = "+key);
		
	}


	
	public ArrayList<BusinessListModel> getBusinessItems()
	{
    		    
		       ArrayList<BusinessListModel> result = new ArrayList<BusinessListModel>();
		    	Cursor c = qureyDatabase("select * from " +BUSINESS_TABLE+";");
		    	
		    	Log.e("cursorLength*******", c.getCount()+".");
		    	if (c.getCount() > 0) {
		            c.moveToFirst();
		            do {
		            	
		            	BusinessListModel mBusinessModel = new BusinessListModel();
		            	mBusinessModel.setBusinessId(c.getString(c.getColumnIndex("BusinessId")));
		            	mBusinessModel.setBusinessTitle(c.getString(c.getColumnIndex("BusinessTitle")));
		            	mBusinessModel.setBusinessAddress(c.getString(c.getColumnIndex("BusinessAddress")));
		            	mBusinessModel.setBusinessRating(c.getString(c.getColumnIndex("Rating")));
		            	mBusinessModel.setRevierwsCount(c.getString(c.getColumnIndex("ReviewsCount")));
		            	mBusinessModel.setAddressLat(c.getString(c.getColumnIndex("Latitude")));
		            	mBusinessModel.setAddressLong(c.getString(c.getColumnIndex("Longitude")));
		            	mBusinessModel.setCityName(c.getString(c.getColumnIndex("CityName")));
		                result.add(mBusinessModel);
		                mBusinessModel = null;
		            	
		            } while (c.moveToNext());
		        }
		    	c.close();
		        return result;
	}
		
	/*
	 *  
	 *  Spotlight Listing  methods
	 * 
	 */
	
	public long insertSpotLightItem(String SpotlightId, String SpotlightTitle,	String SpotlightDesp,  
			String SpotlightAddress, String SpotlightImage, String BusinessId,
			String BusinessTitle, String latitude, String longitude, String CityName) {

		ContentValues cv = new ContentValues();
		cv.put("SpotlightId", SpotlightId);
		cv.put("SpotlightTitle", SpotlightTitle);
		cv.put("SpotlightDesp", SpotlightDesp);
		cv.put("SpotlightAddress", SpotlightAddress);
		cv.put("SpotlightImage", SpotlightImage);
		cv.put("BusinessId", BusinessId);
		cv.put("BusinessTitle", BusinessTitle);
		cv.put("Latitude", latitude);
		cv.put("Longitude", longitude);
		cv.put("CityName", CityName);
		
		long result = myDataBase.insertWithOnConflict(SPOT_LIGHT_TABLE, null, cv,myDataBase.CONFLICT_IGNORE);
		Log.e("return value","result "+result);
		return result;		

	}	
	
	public void deleteSpotlightItem(String key){
		
		updateDatabase("delete from "+SPOT_LIGHT_TABLE+" where SpotlightId = "+key);
			
	}
	
	public ArrayList<GetSpotLightModel> getSpotLightItems()
	{
    		    
		       ArrayList<GetSpotLightModel> result = new ArrayList<GetSpotLightModel>();
		    	Cursor c = qureyDatabase("select * from " +SPOT_LIGHT_TABLE+";");
		    	
		    	Log.e("cursorLength*******", c.getCount()+".");
		    	if (c.getCount() > 0) {
		            c.moveToFirst();
		            do {
		            	GetSpotLightModel mSpotlightModel = new GetSpotLightModel();
		            	mSpotlightModel.setSpotLightID(c.getString(c.getColumnIndex("SpotlightId")));
		            	mSpotlightModel.setSpotLightName(c.getString(c.getColumnIndex("SpotlightTitle")));
		            	mSpotlightModel.setDescription(c.getString(c.getColumnIndex("SpotlightDesp")));
		            	mSpotlightModel.setAddressLine1(c.getString(c.getColumnIndex("SpotlightAddress")));
		            	mSpotlightModel.setImageUrl(c.getString(c.getColumnIndex("SpotlightImage")));
		            	mSpotlightModel.setBusinessID(c.getString(c.getColumnIndex("BusinessId")));
		            	mSpotlightModel.setBusinessTitle(c.getString(c.getColumnIndex("BusinessTitle")));
		            	mSpotlightModel.setAddressLat(c.getString(c.getColumnIndex("Latitude")));
		            	mSpotlightModel.setAddressLong(c.getString(c.getColumnIndex("Longitude")));
		            	mSpotlightModel.setCityName(c.getString(c.getColumnIndex("CityName")));
		                result.add(mSpotlightModel);
		                mSpotlightModel = null;
		            	
		            } while (c.moveToNext());
		        }
		    	c.close();
		        return result;
	}
	
	
	
	/*
	 *  
	 *  Deal Listing  methods
	 * 
	 */
	
	
	public long insertDealItem(String DealId, String DealTitle, String DealAddress, 
			String DealImage, String BusinessId, String BusinessTitle, String latitude, String longitude, String CityName) {

		ContentValues cv = new ContentValues();
		cv.put("DealId", DealId);
		cv.put("DealTitle", DealTitle);
		cv.put("DealAddress", DealAddress);
		cv.put("DealImage", DealImage);
		cv.put("BusinessId", BusinessId);
		cv.put("BusinessTitle", BusinessTitle);
		cv.put("Latitude", latitude);
		cv.put("Longitude", longitude);
		cv.put("CityName", CityName);
		
		long result = myDataBase.insertWithOnConflict(DEALS_TABLE, null, cv,myDataBase.CONFLICT_IGNORE);
		Log.e("return value","result "+result);
		return result;		

	}
	
	public void deleteDealItem(String key){
		
		updateDatabase("delete from "+DEALS_TABLE+" where DealId = "+key);
		
	}

		
	public ArrayList<GetDealsInfoModel> getDealItems()
	{
    		    
		       ArrayList<GetDealsInfoModel> result = new ArrayList<GetDealsInfoModel>();
		    	Cursor c = qureyDatabase("select * from " +DEALS_TABLE+";");
		    	
		    	Log.e("cursorLength*******", c.getCount()+".");
		    	if (c.getCount() > 0) {
		            c.moveToFirst();
		            do {
		            	GetDealsInfoModel mDealsModel = new GetDealsInfoModel();
		            	mDealsModel.setDealId(c.getString(c.getColumnIndex("DealId")));
		            	mDealsModel.setDealTittle(c.getString(c.getColumnIndex("DealTitle")));
		            	mDealsModel.setAddressLine1(c.getString(c.getColumnIndex("DealAddress")));
		            	mDealsModel.setDealImage(c.getString(c.getColumnIndex("DealImage")));
		            	mDealsModel.setBusinessId(c.getString(c.getColumnIndex("BusinessId")));
		            	mDealsModel.setBusinessTitle(c.getString(c.getColumnIndex("BusinessTitle")));
		            	mDealsModel.setAddressLat(c.getString(c.getColumnIndex("Latitude")));
		            	mDealsModel.setAddressLong(c.getString(c.getColumnIndex("Longitude")));
		            	mDealsModel.setCityName(c.getString(c.getColumnIndex("CityName")));
		                result.add(mDealsModel);
		                mDealsModel = null;
		            	
		            } while (c.moveToNext());
		        }
		    	c.close();
		        return result;
	}	
	
	/*
	 *  
	 *  Event Listing  methods
	 * 
	 */	
	
	public long insertEventItem(String EventId, String EventTitle, String EventLocation, String EventOrganizer, String EventStartDate, 
			String EventEndDate, String EventIsFeatured, String EventDetails, String CityName, String BusinessID,
			String Latitude, String Longitude, String Fundrising) {

		ContentValues cv = new ContentValues();
		cv.put("EventId", EventId);
		cv.put("EventTitle", EventTitle);
		cv.put("EventLocation", EventLocation);
		cv.put("EventOrganizer", EventOrganizer);
		cv.put("EventStartDate", EventStartDate);
		cv.put("EventEndDate", EventEndDate);
		cv.put("EventIsFeatured", EventIsFeatured);
		cv.put("EventDetails", EventDetails);
		cv.put("CityName", CityName);
		cv.put("BusinessId", BusinessID);
		cv.put("Latitude", Latitude);
		cv.put("Longitude", Longitude);
		cv.put("Fundrising", Fundrising);
		
		long result = myDataBase.insertWithOnConflict(EVENTS_TABLE, null, cv,myDataBase.CONFLICT_IGNORE);
		Log.e("return value","result "+result);
		return result;	

	}
	
	public void deleteEventItem(String key){
		
		updateDatabase("delete from "+EVENTS_TABLE+" where EventId = "+key);
		
	}

	
	public ArrayList<EventsCalenderModel> getEventItems()
	{
    		    
		       ArrayList<EventsCalenderModel> result = new ArrayList<EventsCalenderModel>();
		    	Cursor c = qureyDatabase("select * from " +EVENTS_TABLE+";");
		    	
		    	Log.e("cursorLength*******", c.getCount()+".");
		    	if (c.getCount() > 0) {
		            c.moveToFirst();
		            do {
		            	EventsCalenderModel mEventModel = new EventsCalenderModel();
		            	mEventModel.setEventId(c.getString(c.getColumnIndex("EventId")));
		            	mEventModel.setEventTitle(c.getString(c.getColumnIndex("EventTitle")));
		            	mEventModel.setVenueLocation(c.getString(c.getColumnIndex("EventLocation")));
		            	mEventModel.setEventOrganizer(c.getString(c.getColumnIndex("EventOrganizer")));
		            	mEventModel.setEventStartsOn(c.getString(c.getColumnIndex("EventStartDate")));
		            	mEventModel.setEventEndsOn(c.getString(c.getColumnIndex("EventEndDate")));
		            	mEventModel.setEventIsFeatured(c.getString(c.getColumnIndex("EventIsFeatured")));
		            	mEventModel.setBusinessID(c.getString(c.getColumnIndex("BusinessId")));
		            	mEventModel.setCityName(c.getString(c.getColumnIndex("CityName")));
		            	mEventModel.setEventDetails(c.getString(c.getColumnIndex("EventDetails")));
		            	mEventModel.setLatitude(c.getString(c.getColumnIndex("Latitude")));
		            	mEventModel.setLongitude(c.getString(c.getColumnIndex("Longitude")));
		            	mEventModel.setFundrising(c.getString(c.getColumnIndex("Fundrising")));
		                result.add(mEventModel);
		                mEventModel = null;
		            	
		            } while (c.moveToNext());
		        }
		    	c.close();
		        return result;
	}	
	
	/*
	 *  
	 *  Classified Listing  methods
	 * 
	 */	
	
	public long insertClassifiedItem(String ClassifiedId, String ClassifiedTitle, String Description, String Image, String Price,
			String Location, String Distance, String Latitude, String Longitude, String OBO, String CityName ) {

		ContentValues cv = new ContentValues();
		cv.put("ClassifiedId", ClassifiedId);
		cv.put("ClassifiedTitle", ClassifiedTitle);
		cv.put("Description", Description);
		cv.put("Image", Image);
		cv.put("Price", Price);
		cv.put("Location", Location);
		cv.put("Distance", Distance);
		cv.put("Latitude", Latitude);
		cv.put("Longitude", Longitude);
		cv.put("OBO", OBO);
		cv.put("CityName", CityName);
		
		long result = myDataBase.insertWithOnConflict(CLASSIFIED_TABLE, null, cv,myDataBase.CONFLICT_IGNORE);
		Log.e("return value","result "+result);
		return result;		

	}   
	
	public void deleteClassifiedItem(String key){
		
		updateDatabase("delete from "+CLASSIFIED_TABLE+" where ClassifiedId = "+key);
		
	}

	public ArrayList<GetClassifiedItemsModel> getClassifiedItems()
	{
    		    
		       ArrayList<GetClassifiedItemsModel> result = new ArrayList<GetClassifiedItemsModel>();
		    	Cursor c = qureyDatabase("select * from " +CLASSIFIED_TABLE+";");
		    	
		    	Log.e("cursorLength*******", c.getCount()+".");
		    	if (c.getCount() > 0) {
		            c.moveToFirst();
		            do {
		            	GetClassifiedItemsModel mClassfiedModel = new GetClassifiedItemsModel();
		            	mClassfiedModel.setClassifiedId(c.getString(c.getColumnIndex("ClassifiedId")));
		            	mClassfiedModel.setClassifiedTitle(c.getString(c.getColumnIndex("ClassifiedTitle")));
		            	mClassfiedModel.setClassifiedInfo(c.getString(c.getColumnIndex("Description")));
		            	mClassfiedModel.setImages(c.getString(c.getColumnIndex("Image")));
		            	mClassfiedModel.setPrice(c.getString(c.getColumnIndex("Price")));
		            	mClassfiedModel.setLocation(c.getString(c.getColumnIndex("Location")));
		            	mClassfiedModel.setDistance(c.getString(c.getColumnIndex("Distance")));
		            	mClassfiedModel.setLatitude(c.getString(c.getColumnIndex("Latitude")));
		            	mClassfiedModel.setLongitude(c.getString(c.getColumnIndex("Longitude")));
		            	mClassfiedModel.setOBO(c.getString(c.getColumnIndex("OBO")));
		            	mClassfiedModel.setCityname(c.getString(c.getColumnIndex("CityName")));
		                result.add(mClassfiedModel);
		                mClassfiedModel = null;
		            	
		            } while (c.moveToNext());
		        }
		    	c.close();		    	
		        return result;
	}
	
	/*
	 *  
	 *  Hasanat Listing  methods
	 * 
	 */
	
	public long insertHasanatItem(String BusinessId, String BusinessTitle, String BusinessAddress, 
			String PhoneNo, String Latitude
			, String Longitude, String CategoryId, String CityName) {

		ContentValues cv = new ContentValues();
		cv.put("BusinessId", BusinessId);
		cv.put("BusinessTitle", BusinessTitle);
		cv.put("BusinessAddress", BusinessAddress);
		cv.put("PhoneNo", PhoneNo);
		cv.put("Latitude", Latitude);
		cv.put("Longitude", Longitude);
		cv.put("CategoryId", CategoryId);
		cv.put("CityName", CityName);
		
		long result = myDataBase.insertWithOnConflict(HASANAT_TABLE, null, cv,myDataBase.CONFLICT_IGNORE);
		Log.e("return value","result "+result);
		return result;

	}	


	public void deleteHasanatItem(String key){
		
		updateDatabase("delete from "+HASANAT_TABLE+" where BusinessId = "+key);
		
	}
	
	
	public ArrayList<BusinessDetailModel> getHasanatItems()
	{
    		    
		       ArrayList<BusinessDetailModel> result = new ArrayList<BusinessDetailModel>();
		    	Cursor c = qureyDatabase("select * from " +HASANAT_TABLE+";");
		    	
		    	Log.e("cursorLength*******", c.getCount()+".");
		    	if (c.getCount() > 0) {
		            c.moveToFirst();
		            do {
		            	BusinessDetailModel mHasanatModel = new BusinessDetailModel();
		            	mHasanatModel.setBusinessId(c.getString(c.getColumnIndex("BusinessId")));
		            	mHasanatModel.setBusinessTitle(c.getString(c.getColumnIndex("BusinessTitle")));
		            	mHasanatModel.setBusinessAddress(c.getString(c.getColumnIndex("BusinessAddress")));
		            	mHasanatModel.setAddressPhone(c.getString(c.getColumnIndex("PhoneNo")));
		            	mHasanatModel.setAddressLat(c.getString(c.getColumnIndex("Latitude")));
		            	mHasanatModel.setAddressLong(c.getString(c.getColumnIndex("Longitude")));
		            	mHasanatModel.setCategoryId(c.getString(c.getColumnIndex("CategoryId")));
		            	mHasanatModel.setCityName(c.getString(c.getColumnIndex("CityName")));
		                result.add(mHasanatModel);
		                mHasanatModel = null;
		            	
		            } while (c.moveToNext());
		        }
		    	c.close();
		        return result;
	}
		
	/*
	 *  
	 *  Masjid Listing methods
	 * 
	 */
		
	public long insertMasjidItem(String BusinessId, String BusinessTitle, String BusinessAddress, String PhoneNo,
			String Latitude, String Longitude, String CategotyId, String CityName ) {

		ContentValues cv = new ContentValues();
		cv.put("BusinessId", BusinessId);
		cv.put("BusinessTitle", BusinessTitle);
		cv.put("BusinessAddress", BusinessAddress);
		cv.put("PhoneNo", PhoneNo);
		cv.put("Latitude", Latitude);
		cv.put("Longitude", Longitude);
		cv.put("CategoryId", CategotyId);
		cv.put("CityName", CityName);
		
		long result = myDataBase.insertWithOnConflict(MASJID_TABLE, null, cv,myDataBase.CONFLICT_IGNORE);
		Log.e("return value","result "+result);
		return result;		

	}
	
	public void deleteMasjidItem(String key){
		
		updateDatabase("delete from "+MASJID_TABLE+" where BusinessId = "+key);
		
	}
	
	public ArrayList<MasjidModel> getMasjidItems()
	{
    		    
		       ArrayList<MasjidModel> result = new ArrayList<MasjidModel>();
		    	Cursor c = qureyDatabase("select * from " +MASJID_TABLE+";");
		    	
		    	Log.e("cursorLength*******", c.getCount()+".");
		    	if (c.getCount() > 0) {
		            c.moveToFirst();
		            do {
		            	MasjidModel mMasjidDBModel = new MasjidModel();	
		            	mMasjidDBModel.setBusinessId(c.getString(c.getColumnIndex("BusinessId")));
		            	mMasjidDBModel.setBusinessTitle(c.getString(c.getColumnIndex("BusinessTitle")));
		            	mMasjidDBModel.setBusinessAddress(c.getString(c.getColumnIndex("BusinessAddress")));
		            	mMasjidDBModel.setAddressPhone(c.getString(c.getColumnIndex("PhoneNo")));
		            	mMasjidDBModel.setAddressLat(c.getString(c.getColumnIndex("Latitude")));
		            	mMasjidDBModel.setAddressLong(c.getString(c.getColumnIndex("Longitude")));
		            	mMasjidDBModel.setCategoryId(c.getString(c.getColumnIndex("CategoryId")));
		            	mMasjidDBModel.setCityName(c.getString(c.getColumnIndex("CityName")));
		            	result.add(mMasjidDBModel);            	
		            	mMasjidDBModel = null;
		            	
		            } while (c.moveToNext());
		        }
		    	c.close();		    	
		        return result;
	}
	    
	/*
	 * 
	 * other
	 * 
	 */	
	
    public Cursor qureyDatabase(String sql)
    {
    	Log.e("query", sql);
    	Cursor cursor = myDataBase.rawQuery(sql,null);
    	return cursor;  
    }

    public void updateDatabase(String sql)
    {
    	Log.e("query", sql);
    	myDataBase.execSQL(sql);
    	  
    }

    public void deleteAllData(String _tableName)
    {
    	try{
    	myDataBase.delete(_tableName,null,null);
    	}catch(Exception ex){}
    }

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
    
    // Add your public helper methods to access and get content from the database.
   // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
   // to you to create adapters for your views.
 
}
	
	



