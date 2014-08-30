package com.daleelo.MyStuff.Activities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.Business.Model.BusinessListModel;
import com.daleelo.DashBoardClassified.Activities.ClassifiedItemDetailDesp;
import com.daleelo.DashBoardClassified.Activities.MyClassifiedListActivity;
import com.daleelo.DashBoardClassified.Activities.PostClassifiedSecondActivity;
import com.daleelo.DashBoardClassified.Model.GetClassifiedItemsModel;
import com.daleelo.DashBoardEvents.Activities.CalendarEventDetailDesc;
import com.daleelo.DashBoardEvents.Model.EventsCalenderModel;
import com.daleelo.Dashboard.Activities.DealsDetailDesp;
import com.daleelo.Dashboard.Activities.SpotlightDetailDesp;
import com.daleelo.Dashboard.Model.GetDealsInfoModel;
import com.daleelo.Dashboard.Model.GetSpotLightModel;
import com.daleelo.DataBase.DatabaseHelper;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.Hasanat.Activities.DetailDescActivityById;
import com.daleelo.Hasanat.Model.BusinessDetailModel;
import com.daleelo.Masjid.Activities.MasjidDetailDescription;
import com.daleelo.Masjid.Model.MasjidModel;
import com.daleelo.TripPlanner.Model.TripPlannerLocationPointsModel;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;
import com.daleelo.helper.ImageDownloader;

public class MyStuffActivity extends ListActivity implements OnClickListener{
	
	private ProgressDialog progressdialog;
	
	LinearLayout mMainData;
	
	ArrayList<BusinessListModel> mBusinessModelFeeds = null;
	ArrayList<GetSpotLightModel> mSpotlightModelFeeds = null;
	ArrayList<GetDealsInfoModel> mDealsModelFeeds = null;
	ArrayList<EventsCalenderModel> mEventModelFeeds = null;
	ArrayList<GetClassifiedItemsModel> mClassfiedModelFeeds = null;
	ArrayList<BusinessDetailModel>  mHasanatModelFeeds = null;
	ArrayList<MasjidModel> mMasjidModelFeeds = null;
	
	
	ArrayList<String> mAddedBusinessId, mAddedDealsId, mAddedSpotlightId, mAddedEventId, mAddedMasjidId, mAddedClassifiedId, mAddedHasanatId;
	
	DatabaseHelper mDbHelper;
	
	DecimalFormat desimalFormat = new DecimalFormat("##0.00");
	
	
	String mMsg;
	TripPlannerLocationPointsModel mTripPlannerLocationPointsModel;
	ArrayList <TripPlannerLocationPointsModel> mTripFeeds;
	
	Button mBtnEdit, mBtnDoneForEdit, mBtnDone;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.my_stuff_screen);
   
        initializeUI();           
                
    }
	
	String[] strName={"Business", "Spotlight","Deals","Events","Classified","Hasant","Masjid"};
	int mItemImg[] = {R.drawable.icon_mystuff_business,R.drawable.icon_spotlight,R.drawable.icon_deals,
			R.drawable.icon_events,R.drawable.icon_classifieds,R.drawable.icon_mystuff_hasanat,R.drawable.icon_masjid_mystuff,};
	
	private void initializeUI(){	
		
		
		
		mBtnEdit = (Button)findViewById(R.id.mystuff_BTN_edit);
		mBtnDone = (Button)findViewById(R.id.mystuff_BTN_done);
		mBtnDoneForEdit = (Button)findViewById(R.id.mystuff_BTN_done_for_edit);
		mMainData = (LinearLayout)findViewById(R.id.mystuff_LIN_category_items);
		
		mBtnEdit.setOnClickListener(this);
		
		
		mBtnDone.setOnClickListener(this);
		mBtnDoneForEdit.setOnClickListener(this);
		
		if(getIntent().getExtras().getString("from").equalsIgnoreCase("trip")){
			
			mTripFeeds = new ArrayList<TripPlannerLocationPointsModel>();
			mBtnEdit.setVisibility(View.GONE);
			mBtnDone.setVisibility(View.VISIBLE);
			
			mAddedBusinessId = new ArrayList<String>();
			mAddedSpotlightId = new ArrayList<String>();
			mAddedDealsId = new ArrayList<String>();
			mAddedEventId = new ArrayList<String>();
			mAddedMasjidId = new ArrayList<String>(); 
			mAddedClassifiedId = new ArrayList<String>();
			mAddedHasanatId = new ArrayList<String>();
			
		}else{
			
			
			mBtnEdit.setVisibility(View.VISIBLE);
			mBtnDone.setVisibility(View.GONE);
			
		}

		int mWidth = Utils.WIDTH/5;
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		for (int i = 0; i < 7; i++) {
			
			LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = vi.inflate(R.layout.mystuff_icon_list_row, null);		
			RelativeLayout mData = (RelativeLayout)v.findViewById(R.id.mystuff_REL_icon);
			ImageView mImg = (ImageView)v.findViewById(R.id.mystuff_icon_row_IMG_data);
			TextView mName = (TextView)v.findViewById(R.id.mystuff_icon_row_TXT_name);	
			mData.setLayoutParams(layoutParams);
			mName.setText(strName[i]);
			mImg.setImageResource(mItemImg[i]);
			
			v.setId(i);
			v.setTag(i);	
			v.setClickable(true);
			v.setOnClickListener(this);
			mMainData.addView(v);
			
		}	    	
		
		 mMyStuffHandler = new MyStuffHandler();
		 
		 new mAsyncFetchMyStuffFormDB().start();
		
	}
	

	
	
//	************************
	
	int mType = 1;
		
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	
		
		switch (v.getId()) {
		
		case 0:
			
			mType = 1;
			new mAsyncFetchMyStuffFormDB().start();
			
			break;
			
		case 1:
			
			mType = 2;
			new mAsyncFetchMyStuffFormDB().start();
					
			break;
			
		case 2:
			
			mType = 3;
			new mAsyncFetchMyStuffFormDB().start();
			break;
			
		case 3:
			
			mType = 4;
			new mAsyncFetchMyStuffFormDB().start();
			
			break;
			
		case 4:
			
			mType = 5;
			new mAsyncFetchMyStuffFormDB().start();
			
			break;
			
		case 5:
			
			mType = 6;
			new mAsyncFetchMyStuffFormDB().start();
			
			break;
			
		case 6:
			
			mType = 7;
			new mAsyncFetchMyStuffFormDB().start();
			
			break;
		
		case R.id.mystuff_BTN_edit:
			
			showDelete();	
			onContentChanged();
			
			break;
			
		case R.id.mystuff_BTN_done_for_edit:
			
			hideDelete();	
			onContentChanged();
			
			break;
			
		case R.id.mystuff_BTN_done:
			
			
			Intent in = new Intent();
			in.putExtra("data", mTripFeeds);
            setResult(RESULT_OK,in);			
			finish();
		
			
			break;
			
		case 100:
			
			mSelectedPositionToDelete = (Integer) v.getTag();
			myAlertDialog( );
		
			break;
			
		}
			
	}
	
	
	boolean mActionType = true;
	int mSelectedPositionToDelete;
	
//	edit option
	
	private void editOrDone(){			
			
			if(mActionType){
				
				Arrays.fill(setRadio, Boolean.TRUE);
				
			
			}else{
				
				Arrays.fill(setRadio, Boolean.FALSE);
				onContentChanged();
			}		
	}
	
	
	
	private void deleteRecord(){
		
//		ArrayList<BusinessListModel> mBusinessModelFeeds = null;
//		ArrayList<GetSpotLightModel> mSpotlightModelFeeds = null;
//		ArrayList<GetDealsInfoModel> mDealsModelFeeds = null;
//		ArrayList<EventsCalenderModel> mEventModelFeeds = null;
//		ArrayList<GetClassifiedItemsModel> mClassfiedModelFeeds = null;
//		ArrayList<BusinessDetailModel>  mHasanatModelFeeds = null;
//		ArrayList<MasjidModel> mMasjidModelFeeds = null;
		mDbHelper.openDataBase();
		
		if(mType == 1){
		
			mDbHelper.deleteBusinessItem(mBusinessModelFeeds.get(mSelectedPositionToDelete).getBusinessId());
			mBusinessModelFeeds.remove(mSelectedPositionToDelete);
			onContentChanged();
		
		}else if(mType == 2){
			
			mDbHelper.deleteSpotlightItem(mSpotlightModelFeeds.get(mSelectedPositionToDelete).getSpotLightID());
			mSpotlightModelFeeds.remove(mSelectedPositionToDelete);
			onContentChanged();	
			
		}else if(mType == 3){
			
			mDbHelper.deleteDealItem(mDealsModelFeeds.get(mSelectedPositionToDelete).getDealId());
			mDealsModelFeeds.remove(mSelectedPositionToDelete);
			onContentChanged();
			
		}else if(mType == 4){
			
			mDbHelper.deleteEventItem(mEventModelFeeds.get(mSelectedPositionToDelete).getEventId());
			mEventModelFeeds.remove(mSelectedPositionToDelete);
			onContentChanged();	
			
		}else if(mType == 5){
			
			mDbHelper.deleteClassifiedItem(mClassfiedModelFeeds.get(mSelectedPositionToDelete).getClassifiedId());
			mClassfiedModelFeeds.remove(mSelectedPositionToDelete);
			onContentChanged();	
			
		}else if(mType == 6){
		
			mDbHelper.deleteHasanatItem(mHasanatModelFeeds.get(mSelectedPositionToDelete).getBusinessId());
			mHasanatModelFeeds.remove(mSelectedPositionToDelete);
			onContentChanged();
			
		}else if(mType == 7){
			
			mDbHelper.deleteMasjidItem(mMasjidModelFeeds.get(mSelectedPositionToDelete).getBusinessId());
			mMasjidModelFeeds.remove(mSelectedPositionToDelete);
			onContentChanged();
		}
		
		mDbHelper.closeDataBase();
	}
	
//	****************************
	
	
	class mAsyncFetchMyStuffFormDB extends Thread
	{
		

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try
			{
							
				mDbHelper= new DatabaseHelper(getApplicationContext());			
				mDbHelper.openDataBase();
				
				if(mType == 1)
					mBusinessModelFeeds = mDbHelper.getBusinessItems();
				else if(mType == 2)
					mSpotlightModelFeeds = mDbHelper.getSpotLightItems();
				else if(mType == 3)
					mDealsModelFeeds = mDbHelper.getDealItems();
				else if(mType == 4)
					mEventModelFeeds = mDbHelper.getEventItems();
				else if(mType == 5)
					mClassfiedModelFeeds = mDbHelper.getClassifiedItems();
				else if(mType == 6)
					mHasanatModelFeeds = mDbHelper.getHasanatItems();
				else if(mType == 7)
					mMasjidModelFeeds = mDbHelper.getMasjidItems();

				mMyStuffHandler.sendEmptyMessage(0);
				Log.e("", "mAsyncFetchMyStuffFormDB");
				
			}catch(Exception e){
				e.printStackTrace();
				
			}
			super.run();
		}
		
	}
	
	
	
	
	MyStuffHandler mMyStuffHandler;

	private boolean[] setRadio;
	
	class MyStuffHandler extends Handler
	{
		public void handleMessage(android.os.Message msg)
		{
			Log.e("", "MyStuffHandler");
			mDbHelper.closeDataBase();
			if(msg.what==0)
			{
				Log.e("", "MyStuffHandler  wnat");
				setDataToList();
			}			
			
		}
	}
	
	private void hideDelete(){
		
		Arrays.fill(setRadio, Boolean.FALSE);
		mBtnEdit.setVisibility(View.VISIBLE);
		mBtnDoneForEdit.setVisibility(View.GONE);
		
	}
	
	private void showDelete(){
		
		Arrays.fill(setRadio, Boolean.TRUE);
		mBtnEdit.setVisibility(View.GONE);
		mBtnDoneForEdit.setVisibility(View.VISIBLE);
		
	}
	
	
	
//	***************** SET LIST DATA
	
	private void setDataToList(){
		
		if(mType == 1){
			
			
			
			if( mBusinessModelFeeds.size() > 0 ){
				
				setRadio = new boolean[mBusinessModelFeeds.size()]; 
				hideDelete();
				setListAdapter(new BussinessAdapter(this, mBusinessModelFeeds));
			
			}else
				alertDialog("Bussiness data not found.");
			
		}else if(mType == 2){
			
			setListAdapter(null);
			if( mSpotlightModelFeeds.size() > 0 ){
				
				setRadio = new boolean[mSpotlightModelFeeds.size()]; 
				hideDelete();
				setListAdapter(new SpotlightAdapter(this, mSpotlightModelFeeds));
				
			}else
				alertDialog("Spotlight data not found.");
			
		}else if(mType == 3){
			
			
			if( mDealsModelFeeds.size() > 0 ){
				
				setRadio = new boolean[mDealsModelFeeds.size()]; 
				hideDelete();
				setListAdapter(new DealsAdapter(this, mDealsModelFeeds));
				
			}else
				alertDialog("Deal data not found.");
			
		}else if(mType == 4){
			
			
			if( mEventModelFeeds.size() > 0 ){
				
				setRadio = new boolean[mEventModelFeeds.size()]; 
				hideDelete();
				setListAdapter(new EventsListAdapter(this, mEventModelFeeds));
				
			}else
				alertDialog("Event data not found.");
			
		}else if(mType == 5){
			
			
			if( mClassfiedModelFeeds.size() > 0 ){
				
				setRadio = new boolean[mClassfiedModelFeeds.size()]; 
				hideDelete();
				setListAdapter(new ClassifiedAdapter(this, mClassfiedModelFeeds));
				
			}else
				alertDialog("Classified data not found.");
						
		}else if(mType == 6){
			
		
			if( mHasanatModelFeeds.size() > 0 ){
				
				setRadio = new boolean[mHasanatModelFeeds.size()]; 
				hideDelete();
				setListAdapter(new HasanatAdapter(this, mHasanatModelFeeds));
				
			}else
				alertDialog("Hasanat data not found.");
			
		}else if(mType == 7){
		
			if( mMasjidModelFeeds.size() > 0 ){
				
				setRadio = new boolean[mMasjidModelFeeds.size()]; 
				hideDelete();
				setListAdapter(new MasjidAdapter(this, mMasjidModelFeeds));
				
			}else
				alertDialog("Masjid data not found.");
			
		}
	
	}
	

	
//	List item click listener
	
	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		super.onListItemClick(l, v, position, id);

		
		if(mType == 1){
			
			if(getIntent().getExtras().getString("from").equalsIgnoreCase("trip")){
				
				mMsg = "Are you sure want to add this Business Listing as a destination";	
				
				
				mTripPlannerLocationPointsModel = new TripPlannerLocationPointsModel();
				
				mTripPlannerLocationPointsModel.setBusinessId(mBusinessModelFeeds.get(position).getBusinessId());
				mTripPlannerLocationPointsModel.setBusinessTitle(mBusinessModelFeeds.get(position).getBusinessTitle());
				mTripPlannerLocationPointsModel.setAddressLine1(mBusinessModelFeeds.get(position).getAddressLine1());
				mTripPlannerLocationPointsModel.setAddressLine2(mBusinessModelFeeds.get(position).getAddressLine2());
				mTripPlannerLocationPointsModel.setAddressLat(Double.parseDouble(mBusinessModelFeeds.get(position).getAddressLat()));
				mTripPlannerLocationPointsModel.setAddressLong(Double.parseDouble(mBusinessModelFeeds.get(position).getAddressLong()));
				Log.v("business city name",""+mBusinessModelFeeds.get(position).getCityName());
				mTripPlannerLocationPointsModel.setCityName(mBusinessModelFeeds.get(position).getCityName());
				mTripPlannerLocationPointsModel.setMiddlecity(true);
				mTripPlannerLocationPointsModel.setCategory("main");
				mTripPlannerLocationPointsModel.setType("t");
				mTripPlannerLocationPointsModel.setItemType("business");
				
				if(mAddedBusinessId.contains(mBusinessModelFeeds.get(position).getBusinessId())){
					
					mMsg = "Item already added to the Destination Listing";
					alertDialog(mMsg);
					
				}else{

					  new AlertDialogMsg(MyStuffActivity.this, mMsg)
					  .setPositiveButton("YES", new android.content.DialogInterface.OnClickListener() {
						
						
						  @Override
						
						  public void onClick(DialogInterface dialog, int which) {	
							  
							  mAddedBusinessId.add(mBusinessModelFeeds.get(position).getBusinessId());
							  
							  mTripFeeds.add(mTripPlannerLocationPointsModel);
							  
							  mTripPlannerLocationPointsModel = null;
							  
						  }
						
					
					  }).setNegativeButton("NO", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).create().show();
				
				}

			}else{
			
				startActivity(new Intent(MyStuffActivity.this,BusinessDetailDesp.class)
				.putExtra("data", mBusinessModelFeeds)
				.putExtra("position", position)
				.putExtra("from", "stuff"));
			
			}
			
			
		}else if(mType == 2){
			
			if(getIntent().getExtras().getString("from").equalsIgnoreCase("trip")){
				
				
				mMsg = "Are you sure want to add this Spot LightListing as a destination";	
				
				mTripPlannerLocationPointsModel = new TripPlannerLocationPointsModel();
				
				mTripPlannerLocationPointsModel.setBusinessId(mSpotlightModelFeeds.get(position).getSpotLightID());
				mTripPlannerLocationPointsModel.setBusinessTitle(mSpotlightModelFeeds.get(position).getBusinessTitle());
				mTripPlannerLocationPointsModel.setAddressLine1(mSpotlightModelFeeds.get(position).getAddressLine1());
				mTripPlannerLocationPointsModel.setAddressLine2(mSpotlightModelFeeds.get(position).getAddressLine2());
				mTripPlannerLocationPointsModel.setAddressLat(Double.parseDouble(mSpotlightModelFeeds.get(position).getAddressLat()));
				mTripPlannerLocationPointsModel.setAddressLong(Double.parseDouble(mSpotlightModelFeeds.get(position).getAddressLong()));
				Log.v("spotlight city name",""+mSpotlightModelFeeds.get(position).getCityName());
				mTripPlannerLocationPointsModel.setCityName(mSpotlightModelFeeds.get(position).getCityName());
				mTripPlannerLocationPointsModel.setMiddlecity(true);
				mTripPlannerLocationPointsModel.setCategory("main");
				mTripPlannerLocationPointsModel.setType("t");
				mTripPlannerLocationPointsModel.setItemType("spotlight");
				
				if(mAddedSpotlightId.contains(mSpotlightModelFeeds.get(position).getSpotLightID())){
					
					mMsg = "Item already added to the Destination Listing";
					alertDialog(mMsg);
					
				}else{

				  new AlertDialogMsg(MyStuffActivity.this, mMsg)
				  .setPositiveButton("YES", new android.content.DialogInterface.OnClickListener() {
					
					
					  @Override
					
					  public void onClick(DialogInterface dialog, int which) {	
						  
						  
						  	mAddedSpotlightId.add(mSpotlightModelFeeds.get(position).getSpotLightID());
						
							mTripFeeds.add(mTripPlannerLocationPointsModel);
						  
							mTripPlannerLocationPointsModel = null;
					
					  }
						
						
				  }).setNegativeButton("NO", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
				  }).create().show();
				
				}
			
			}else{
			
				startActivity(new Intent(MyStuffActivity.this,SpotlightDetailDesp.class)
				.putExtra("data", mSpotlightModelFeeds)
				.putExtra("position", position)
				.putExtra("from", "stuff"));
			}
			
		}else if(mType == 3){
			
			
			if(getIntent().getExtras().getString("from").equalsIgnoreCase("trip")){
				
				mMsg = "Are you sure want to add this Deal Listing as a destination";
				
				mTripPlannerLocationPointsModel = new TripPlannerLocationPointsModel();
				
				mTripPlannerLocationPointsModel.setBusinessId(mDealsModelFeeds.get(position).getDealId());
				mTripPlannerLocationPointsModel.setBusinessTitle(mDealsModelFeeds.get(position).getBusinessTitle());
				mTripPlannerLocationPointsModel.setAddressLine1(mDealsModelFeeds.get(position).getAddressLine1());
				mTripPlannerLocationPointsModel.setAddressLine2(mDealsModelFeeds.get(position).getAddressLine2());
				mTripPlannerLocationPointsModel.setAddressLat(Double.parseDouble(mDealsModelFeeds.get(position).getAddressLat()));
				mTripPlannerLocationPointsModel.setAddressLong(Double.parseDouble(mDealsModelFeeds.get(position).getAddressLong()));
				Log.v("deal city name",""+mDealsModelFeeds.get(position).getCityName());				
				mTripPlannerLocationPointsModel.setCityName(mDealsModelFeeds.get(position).getCityName());
				mTripPlannerLocationPointsModel.setMiddlecity(true);
				mTripPlannerLocationPointsModel.setCategory("main");
				mTripPlannerLocationPointsModel.setType("t");
				mTripPlannerLocationPointsModel.setItemType("deal");
				
				if( mAddedDealsId.contains(mDealsModelFeeds.get(position).getDealId())){
					
					mMsg = "Item already added to the Destination Listing";
					alertDialog(mMsg);
					
				}else{

				  new AlertDialogMsg(MyStuffActivity.this, mMsg)
				  .setPositiveButton("YES", new android.content.DialogInterface.OnClickListener() {
					
					
					  @Override
					
					  public void onClick(DialogInterface dialog, int which) {	
						  						  
						  mAddedDealsId.add(mDealsModelFeeds.get(position).getDealId());
						  mTripFeeds.add(mTripPlannerLocationPointsModel);
						  mTripPlannerLocationPointsModel = null;
						
					  }
						
						
				  }).setNegativeButton("NO", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).create().show();
				}
				
			
			}else{
			
				startActivity(new Intent(MyStuffActivity.this,DealsDetailDesp.class)
				.putExtra("data", mDealsModelFeeds)
				.putExtra("position", position)
				.putExtra("from", "stuff"));
			
			}
			
		}else if(mType == 4){			
					
			if(getIntent().getExtras().getString("from").equalsIgnoreCase("trip")){
				
				mMsg = "Are you sure want to add this Event Listing as a destination";
				
				mTripPlannerLocationPointsModel = new TripPlannerLocationPointsModel();
				
				mTripPlannerLocationPointsModel.setBusinessId(mEventModelFeeds.get(position).getEventId());
				mTripPlannerLocationPointsModel.setBusinessTitle(mEventModelFeeds.get(position).getBusinessTitle());
				mTripPlannerLocationPointsModel.setAddressLine1(mEventModelFeeds.get(position).getAddressLine1());
				mTripPlannerLocationPointsModel.setAddressLine2(mEventModelFeeds.get(position).getAddressLine2());
				mTripPlannerLocationPointsModel.setAddressLat(Double.parseDouble(mEventModelFeeds.get(position).getLatitude()));
				mTripPlannerLocationPointsModel.setAddressLong(Double.parseDouble(mEventModelFeeds.get(position).getLongitude()));
				Log.v("deal city name",""+mDealsModelFeeds.get(position).getCityName());				
				mTripPlannerLocationPointsModel.setCityName(mEventModelFeeds.get(position).getCityName());
				mTripPlannerLocationPointsModel.setMiddlecity(true);
				mTripPlannerLocationPointsModel.setCategory("main");
				mTripPlannerLocationPointsModel.setType("t");
				mTripPlannerLocationPointsModel.setItemType("event");
				
				if( mAddedEventId.contains(mEventModelFeeds.get(position).getEventId())){
					
					mMsg = "Item already added to the Destination Listing";
					alertDialog(mMsg);
					
				}else{

				  new AlertDialogMsg(MyStuffActivity.this, mMsg)
				  .setPositiveButton("YES", new android.content.DialogInterface.OnClickListener() {
					
					
					  @Override
					
					  public void onClick(DialogInterface dialog, int which) {	
						  						  
						  mAddedEventId.add(mEventModelFeeds.get(position).getEventId());
						  mTripFeeds.add(mTripPlannerLocationPointsModel);
						  mTripPlannerLocationPointsModel = null;
						
					  }
						
						
				  }).setNegativeButton("NO", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).create().show();
				}
				
			
			}else{
			
				startActivity(new Intent(MyStuffActivity.this,CalendarEventDetailDesc.class)
				.putExtra("data", mEventModelFeeds)
				.putExtra("position", position)
				.putExtra("from", "stuff"));
			
			}
			
		}else if(mType == 5){			
			
			if(getIntent().getExtras().getString("from").equalsIgnoreCase("trip")){
				
				mMsg = "Are you sure want to add this Classified Listing as a destination";		
				
				mTripPlannerLocationPointsModel = new TripPlannerLocationPointsModel();
				
				mTripPlannerLocationPointsModel.setBusinessId(mClassfiedModelFeeds.get(position).getClassifiedId());
				mTripPlannerLocationPointsModel.setBusinessTitle(mClassfiedModelFeeds.get(position).getClassifiedTitle());
				mTripPlannerLocationPointsModel.setAddressLine1(mClassfiedModelFeeds.get(position).getLocation());
				mTripPlannerLocationPointsModel.setAddressLine2(" ");
				mTripPlannerLocationPointsModel.setAddressLat(Double.parseDouble(mClassfiedModelFeeds.get(position).getLatitude()));
				mTripPlannerLocationPointsModel.setAddressLong(Double.parseDouble(mClassfiedModelFeeds.get(position).getLongitude()));	
				Log.v("classified city name",""+mClassfiedModelFeeds.get(position).getCityname());
				mTripPlannerLocationPointsModel.setCityName(mClassfiedModelFeeds.get(position).getCityname());
				mTripPlannerLocationPointsModel.setMiddlecity(true);	
				mTripPlannerLocationPointsModel.setCategory("main");
				mTripPlannerLocationPointsModel.setType("t");
				mTripPlannerLocationPointsModel.setItemType("classified");
				
				
				if(mAddedClassifiedId.contains(mClassfiedModelFeeds.get(position).getClassifiedId())){
					
					mMsg = "Item already added to the Destination Listing";
					alertDialog(mMsg);
					
				}else{

				  new AlertDialogMsg(MyStuffActivity.this, mMsg).setPositiveButton("YES", new android.content.DialogInterface.OnClickListener() {
					
					
					  @Override
					
					  public void onClick(DialogInterface dialog, int which) {	
						  
							mAddedClassifiedId.add(mClassfiedModelFeeds.get(position).getClassifiedId());
							mTripFeeds.add(mTripPlannerLocationPointsModel);							  
							mTripPlannerLocationPointsModel = null;
				
					  }
						
						
				  }).setNegativeButton("NO", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).create().show();
				  
				}
				
			}else{
			
				startActivity(new Intent(MyStuffActivity.this,ClassifiedItemDetailDesp.class)
				.putExtra("data", mClassfiedModelFeeds)
				.putExtra("position", position)
				.putExtra("from", "stuff"));
			
			}
		}else if(mType == 6){
			
			
			if(getIntent().getExtras().getString("from").equalsIgnoreCase("trip")){
				
				mMsg = "Are you sure want to add this Business Listing as a destination";	
				
				
				mTripPlannerLocationPointsModel = new TripPlannerLocationPointsModel();
				
				mTripPlannerLocationPointsModel.setBusinessId(mHasanatModelFeeds.get(position).getBusinessId());
				mTripPlannerLocationPointsModel.setBusinessTitle(mHasanatModelFeeds.get(position).getBusinessTitle());
				mTripPlannerLocationPointsModel.setAddressLine1(mHasanatModelFeeds.get(position).getAddressLine1());
				mTripPlannerLocationPointsModel.setAddressLine2(mHasanatModelFeeds.get(position).getAddressLine2());
				mTripPlannerLocationPointsModel.setAddressLat(Double.parseDouble(mHasanatModelFeeds.get(position).getAddressLat()));
				mTripPlannerLocationPointsModel.setAddressLong(Double.parseDouble(mHasanatModelFeeds.get(position).getAddressLong()));
				Log.v("business city name",""+mHasanatModelFeeds.get(position).getCityName());
				mTripPlannerLocationPointsModel.setCityName(mHasanatModelFeeds.get(position).getCityName());
				mTripPlannerLocationPointsModel.setMiddlecity(true);
				mTripPlannerLocationPointsModel.setCategory("main");
				mTripPlannerLocationPointsModel.setType("t");
				mTripPlannerLocationPointsModel.setItemType("hasanat");
				
				if(mAddedHasanatId.contains(mHasanatModelFeeds.get(position).getBusinessId())){
					
					mMsg = "Item already added to the Destination Listing";
					alertDialog(mMsg);
					
				}else{

					  new AlertDialogMsg(MyStuffActivity.this, mMsg)
					  .setPositiveButton("YES", new android.content.DialogInterface.OnClickListener() {
						
						
						  @Override
						
						  public void onClick(DialogInterface dialog, int which) {	
							  
							  mAddedHasanatId.add(mHasanatModelFeeds.get(position).getBusinessId());
							  
							  mTripFeeds.add(mTripPlannerLocationPointsModel);
							  
							  mTripPlannerLocationPointsModel = null;
							  
						  }
						
					
					  }).setNegativeButton("NO", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).create().show();
				
				}

			}else{
			
				startActivity(new Intent(MyStuffActivity.this,DetailDescActivityById.class)
				.putExtra("data", mHasanatModelFeeds)
				.putExtra("position", position)
				.putExtra("from", "stuff"));
			
			}
			
		}else if(mType == 7){
			
			if(getIntent().getExtras().getString("from").equalsIgnoreCase("trip")){
				
				mMsg = "Are you sure want to add this Masjid Listing as a destination";			

				mTripPlannerLocationPointsModel = new TripPlannerLocationPointsModel();
				
				mTripPlannerLocationPointsModel.setBusinessId(mMasjidModelFeeds.get(position).getBusinessId());
				mTripPlannerLocationPointsModel.setBusinessTitle(mMasjidModelFeeds.get(position).getBusinessTitle());
				mTripPlannerLocationPointsModel.setAddressLine1(mMasjidModelFeeds.get(position).getAddressLine1());
				mTripPlannerLocationPointsModel.setAddressLine2(mMasjidModelFeeds.get(position).getAddressLine2());
				mTripPlannerLocationPointsModel.setAddressLat(Double.parseDouble(mMasjidModelFeeds.get(position).getAddressLat()));
				mTripPlannerLocationPointsModel.setAddressLong(Double.parseDouble(mMasjidModelFeeds.get(position).getAddressLong()));	
				Log.v("masjid city name",""+mMasjidModelFeeds.get(position).getCityName());
				mTripPlannerLocationPointsModel.setCityName(mMasjidModelFeeds.get(position).getCityName());
				mTripPlannerLocationPointsModel.setMiddlecity(true);
				mTripPlannerLocationPointsModel.setCategory("main");
				mTripPlannerLocationPointsModel.setType("t");
				mTripPlannerLocationPointsModel.setItemType("masjid");
				
				if(mAddedMasjidId.contains(mMasjidModelFeeds.get(position).getBusinessId())){
					
					mMsg = "Item already added to the Destination Listing";
					alertDialog(mMsg);
					
				}else{
				  new AlertDialogMsg(MyStuffActivity.this, mMsg).setPositiveButton("YES", new android.content.DialogInterface.OnClickListener() {
					
					
					  @Override
					
					  public void onClick(DialogInterface dialog, int which) {	
						  
							mAddedMasjidId.add(mMasjidModelFeeds.get(position).getBusinessId());
							mTripFeeds.add(mTripPlannerLocationPointsModel);							  
							mTripPlannerLocationPointsModel = null;
				
					  }
						
						
				  	}).setNegativeButton("NO", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).create().show();
				}
			
			}else{
			
				startActivity(new Intent(MyStuffActivity.this,MasjidDetailDescription.class)
				.putExtra("data", mMasjidModelFeeds)
				.putExtra("position", position)
				.putExtra("from", "stuff"));
			
			}			
		}
	}
	
	
	
//	************************
//	Data Adapters
	
	
	
	class BussinessAdapter extends ArrayAdapter<BusinessListModel>
	{
		private LayoutInflater mInflater;
		ArrayList<BusinessListModel> feed;
		Context context;
		
		public BussinessAdapter(Context context, ArrayList<BusinessListModel> feed) 
		{
			super(context, R.layout.business_list_row,feed);
			this.context=context;
			this.feed=feed;
		}

		

		@Override
		public BusinessListModel getItem(int position) {
			// TODO Auto-generated method stub
			return feed.get(position);
		}		

	

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) 
		{
			
			mInflater= LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.business_list_row, null);
			
			 TextView mName = (TextView)convertView.findViewById(R.id.business_row_TXT_name);
			 TextView mAddress = (TextView)convertView.findViewById(R.id.business_row_TXT_address);
			 TextView mReview = (TextView)convertView.findViewById(R.id.business_row_TXT_review);
			 TextView mDis = (TextView)convertView.findViewById(R.id.business_row_TXT_distance);
			 ImageView mRating = (ImageView)convertView.findViewById(R.id.business_row_IMG_rate);	
			 Button mDelete  =  (Button)convertView.findViewById(R.id.business_row_BTN_delete);
			 	 
			 
			 if(setRadio[position]){
				 
				 mDelete.setVisibility(View.VISIBLE);
				 mDelete.setId(100);
				 mDelete.setTag(position);
				 mDelete.setOnClickListener(MyStuffActivity.this);
				 
			 }else{
				 
				 mDelete.setVisibility(View.GONE);	
			 }
			 
			
			 
			 mName.setText(feed.get(position).getBusinessTitle());
			 mAddress.setText(feed.get(position).getBusinessAddress());
			 
			 if(feed.get(position).getRevierwsCount().equalsIgnoreCase("0"))
				 mReview.setText("No Reviews");
			 else
				 mReview.setText(feed.get(position).getRevierwsCount()+" Reviews");
			 mDis.setVisibility(View.INVISIBLE);
			 Log.e("", "ratingnn "+feed.get(position).getBusinessRating().toString());
			 
			try{
				
			 int rate = Integer.parseInt(feed.get(position).getBusinessRating().toString().trim());
				
				if(rate == 1)		
					mRating.setImageResource(R.drawable.icon_one_star);
				if(rate == 2)		
					mRating.setImageResource(R.drawable.icon_two_star);
				if(rate == 3)		
					mRating.setImageResource(R.drawable.icon_three_star);
				if(rate == 4)		
					mRating.setImageResource(R.drawable.icon_four_star);
				if(rate == 5)		
					mRating.setImageResource(R.drawable.icon_five_star);
				
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
							 



			return convertView;
		}
		
	}
	
	
	class SpotlightAdapter extends ArrayAdapter<GetSpotLightModel>{
    	
    	private LayoutInflater mInflater;
    	ArrayList<GetSpotLightModel> mDataFeeds;
    	Context context;
    	
    	public SpotlightAdapter(Context context, ArrayList<GetSpotLightModel> mDataFeeds) {
    		
    		super(context, R.layout.db_spotlight_list_row, mDataFeeds);
    		this.context=context;
        	this.mDataFeeds = mDataFeeds;
          
        }


		@Override
		public GetSpotLightModel getItem(int position) {
			// TODO Auto-generated method stub
			return mDataFeeds.get(position);
		}

		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
						 
			 LayoutInflater mInflater= LayoutInflater.from(context);
			 
			 convertView = mInflater.inflate(R.layout.db_spotlight_list_row, null);
			 
			 TextView mName = (TextView)convertView.findViewById(R.id.spotlight_row_TXT_name);
			 TextView mDesp = (TextView)convertView.findViewById(R.id.spotlight_row_TXT_desp);
			 TextView mDis = (TextView)convertView.findViewById(R.id.spotlight_row_TXT_distance);
			 ImageView mImg = (ImageView)convertView.findViewById(R.id.spotlight_row_IMG);
			 Button mDelete  = (Button)convertView.findViewById(R.id.spotlight_row_BTN_delete);
			 	 
			 if(setRadio[position]){
				 
				 mDelete.setVisibility(View.VISIBLE);
				 mDelete.setId(100);
				 mDelete.setTag(position);
				 mDelete.setOnClickListener(MyStuffActivity.this);
				 
			 }else{
				 
				 mDelete.setVisibility(View.GONE);	
			 }

			 mDis.setVisibility(View.INVISIBLE);
			 mName.setText(mDataFeeds.get(position).getSpotLightName());
			 mDesp.setText(mDataFeeds.get(position).getDescription());
			 
			 if(mDataFeeds.get(position).getImageUrl().length()>0){
				 new ImageDownloader().download(String.format(Urls.SL_IMG_URL,mDataFeeds.get(position).getImageUrl()), mImg);
			 }
			
			return convertView;
		}
		
	}
	
	
	class DealsAdapter extends ArrayAdapter<GetDealsInfoModel>{
    	
    	private LayoutInflater mInflater;
    	ArrayList<GetDealsInfoModel> mDataFeeds;
    	Context context;
    	
    	public DealsAdapter(Context context, ArrayList<GetDealsInfoModel> mDataFeeds) {
    		
    		super(context, R.layout.db_spotlight_list_row, mDataFeeds);
    		this.context=context;
        	this.mDataFeeds = mDataFeeds;
          
        }


		@Override
		public GetDealsInfoModel getItem(int position) {
			// TODO Auto-generated method stub
			return mDataFeeds.get(position);
		}

		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
						 
			 LayoutInflater mInflater= LayoutInflater.from(context);
			 
			 convertView = mInflater.inflate(R.layout.db_spotlight_list_row, null);
			 
			 TextView mName = (TextView)convertView.findViewById(R.id.spotlight_row_TXT_name);
			 TextView mDesp = (TextView)convertView.findViewById(R.id.spotlight_row_TXT_desp);
			 TextView mDistance = (TextView)convertView.findViewById(R.id.spotlight_row_TXT_distance);
			 
			 Button mDelete  = (Button)convertView.findViewById(R.id.spotlight_row_BTN_delete);
		 	 
			 if(setRadio[position]){
				 
				 mDelete.setVisibility(View.VISIBLE);
				 mDelete.setId(100);
				 mDelete.setTag(position);
				 mDelete.setOnClickListener(MyStuffActivity.this);
				 
			 }else{
				 
				 mDelete.setVisibility(View.GONE);	
			 }
			 
			 mDistance.setVisibility(View.INVISIBLE);
			 mName.setText(mDataFeeds.get(position).getDealTittle());
			 mDesp.setText(mDataFeeds.get(position).getBusinessTitle());
			 

			return convertView;
		}
		
	}
	
	
	class EventsListAdapter extends ArrayAdapter<EventsCalenderModel>{

		Context context;
		ArrayList<EventsCalenderModel> feeds;
		public EventsListAdapter(Context context,  ArrayList<EventsCalenderModel> mEventsList) {
			super(context, R.layout.calendar_events_listrow);
			this.context = context;
			feeds = mEventsList;
		}
		
		@Override
		public int getCount() {
			return feeds.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.calendar_events_listrow, null);
			TextView txt_date = (TextView)convertView.findViewById(R.id.calendar_events_listrow_TXT_date);
			TextView txt_time = (TextView)convertView.findViewById(R.id.calendar_events_listrow_TXT_time);
			TextView txt_desc = (TextView)convertView.findViewById(R.id.calendar_events_listrow_TXT_desc);
			ImageView dot_img = (ImageView)convertView.findViewById(R.id.calendar_events_listrow_IMG_dot);
			Button mDelete  = (Button)convertView.findViewById(R.id.calendar_events_listrow_BTN_delete);
		 	 
			 if(setRadio[position]){
				 
				 mDelete.setVisibility(View.VISIBLE);
				 mDelete.setId(100);
				 mDelete.setTag(position);
				 mDelete.setOnClickListener(MyStuffActivity.this);
				 
			 }else{
				 
				 mDelete.setVisibility(View.GONE);	
			 }
			txt_date.setText(feeds.get(position).getEventStartsOn().split(" ")[0]);
			txt_time.setText(feeds.get(position).getEventStartsOn().split(" ")[1]+" "+feeds.get(position).getEventStartsOn().split(" ")[2]);
			txt_desc.setText(feeds.get(position).getEventTitle());
			
			if(feeds.get(position).getFundrising().equalsIgnoreCase("true")){
				dot_img.setBackgroundResource(R.drawable.green_dot);
			}else{
				dot_img.setBackgroundResource(R.drawable.grey_dot);
			}
			
			
			return convertView;
		}
		
		
		
	}
			
	
	class ClassifiedAdapter extends ArrayAdapter<GetClassifiedItemsModel>{
    	
    	private LayoutInflater mInflater;
    	ArrayList<GetClassifiedItemsModel> mDataFeeds;
    	Context context;
    	
    	public ClassifiedAdapter(Context context, ArrayList<GetClassifiedItemsModel> mDataFeeds) {
    		
    		super(context, R.layout.db_classified_by_id_list_row, mDataFeeds);
    		this.context=context;
        	this.mDataFeeds = mDataFeeds;
          
        }


		@Override
		public GetClassifiedItemsModel getItem(int position) {
			// TODO Auto-generated method stub
			return mDataFeeds.get(position);
		}

		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
						 
			 LayoutInflater mInflater= LayoutInflater.from(context);
			 
			 convertView = mInflater.inflate(R.layout.db_classified_by_id_list_row, null);
			 
			 TextView mPrice = (TextView)convertView.findViewById(R.id.classified_by_id_row_TXT_price);
			 TextView mName = (TextView)convertView.findViewById(R.id.classified_by_id_row_TXT_name);
			 TextView mDistace = (TextView)convertView.findViewById(R.id.classified_by_id_row_TXT_distance);
			 Button mDelete  = (Button)convertView.findViewById(R.id.classified_by_id_row_BTN_delete);
		 	 
			 if(setRadio[position]){
				 
				 mDelete.setVisibility(View.VISIBLE);
				 mDelete.setId(100);
				 mDelete.setTag(position);
				 mDelete.setOnClickListener(MyStuffActivity.this);
				 
			 }else{
				 
				 mDelete.setVisibility(View.GONE);	
			 }
			 String mPriceStr;
			 double tempStr = Double.parseDouble(mClassfiedModelFeeds.get(position).getPrice());
			 
			 if(mClassfiedModelFeeds.get(position).getOBO().equalsIgnoreCase("True"))				 
				 mPriceStr = desimalFormat.format(tempStr)+" OBO";
			 else
				 mPriceStr = desimalFormat.format(tempStr);
			
			 mPrice.setText(mPriceStr);
			 mName.setText("- "+mClassfiedModelFeeds.get(position).getClassifiedTitle());
			 mDistace.setVisibility(View.INVISIBLE);
			 			
			return convertView;
			
		}
		
	}
	
	
	class HasanatAdapter extends ArrayAdapter<BusinessDetailModel>{

		Context context;
		ArrayList<BusinessDetailModel> feeds;
		public HasanatAdapter(Context context,  ArrayList<BusinessDetailModel> mFilteredDataList) {
			super(context, R.layout.give_list_row);
			this.context = context;
			feeds = mFilteredDataList;
		}
		

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return feeds.size();
		}


		@Override
		public BusinessDetailModel getItem(int position) {
			// TODO Auto-generated method stub
			return feeds.get(position);
		}

		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.give_list_row, null);
			TextView txt_Name = (TextView)convertView.findViewById(R.id.give_listrow_TXT_name);
			TextView txt_Address = (TextView)convertView.findViewById(R.id.give_listrow_TXT_address);
			TextView txt_Phone = (TextView)convertView.findViewById(R.id.give_listrow_TXT_phone);
			TextView txt_Dis = (TextView)convertView.findViewById(R.id.give_listrow_TXT_distance);
			Button mDelete  = (Button)convertView.findViewById(R.id.give_listrow_BTN_delete);
		 	 
			 if(setRadio[position]){
				 
				 mDelete.setVisibility(View.VISIBLE);
				 mDelete.setId(100);
				 mDelete.setTag(position);
				 mDelete.setOnClickListener(MyStuffActivity.this);
				 
			 }else{
				 
				 mDelete.setVisibility(View.GONE);	
			 }
			txt_Name.setText(feeds.get(position).getBusinessTitle());
			txt_Address.setText(feeds.get(position).getBusinessAddress());
			txt_Phone.setText(feeds.get(position).getAddressPhone());
			txt_Dis.setVisibility(View.INVISIBLE);
			
			return convertView;
		}
		
		
		
	}
	
	
	class MasjidAdapter extends ArrayAdapter<MasjidModel>{
    	
    	private LayoutInflater mInflater;
    	ArrayList<MasjidModel> mDataFeeds;
    	Context context;
    	int mainCount; 
    	
    	
    	public MasjidAdapter(Context context, ArrayList<MasjidModel> mDataFeeds) {
    		
    		super(context, R.layout.masjid_listrow, mDataFeeds);
    		this.context=context;
        	this.mDataFeeds = mDataFeeds;        	
        	
          
        }


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDataFeeds.size();
		}


		@Override
		public MasjidModel getItem(int position) {
			// TODO Auto-generated method stub
			return mDataFeeds.get(position);
		}

		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			Log.v("logcat","in getview");
						 
			mInflater= LayoutInflater.from(context);
				convertView = mInflater.inflate(R.layout.masjid_listrow, null);

			Log.e("", "mainCount "+mainCount+" position "+position);
			
			 
			 TextView mName = (TextView)convertView.findViewById(R.id.masjidlisting_TXT_dallasislamic_center);
			 TextView mAddress = (TextView)convertView.findViewById(R.id.masjidlisting_TXT_masjid_address);
			 TextView mPhone = (TextView)convertView.findViewById(R.id.masjidlisting_TXT_phone);	
			 TextView mDis = (TextView)convertView.findViewById(R.id.masjidlisting_TXT_masjid_distance);
				Button mDelete  = (Button)convertView.findViewById(R.id.masjidlisting_BTN_delete);
			 	 
				 if(setRadio[position]){
					 
					 mDelete.setVisibility(View.VISIBLE);
					 mDelete.setId(100);
					 mDelete.setTag(position);
					 mDelete.setOnClickListener(MyStuffActivity.this);
					 
				 }else{
					 
					 mDelete.setVisibility(View.GONE);	
				 }
			 mName.setText(mDataFeeds.get(position).getBusinessTitle());			 
			 mAddress.setText(mDataFeeds.get(position).getBusinessAddress());			 
			 mPhone.setText(mDataFeeds.get(position).getAddressPhone());	
			 mDis.setVisibility(View.INVISIBLE);
			
			return convertView;
		}
		
	}
	    
	


//	****************************
	  
//	Alert Dialogs 
	
	private void alertDialog(String msg){

	  new AlertDialogMsg(MyStuffActivity.this, msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
		
		
		  @Override
		
		  public void onClick(DialogInterface dialog, int which) {
			
			
		
		  }
		
	
	  }).create().show();
  
  }
  
  
	private void myAlertDialog(){	
		
		mMsg = "Do you want to delete this item.";
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Daleelo");
		builder.setMessage(mMsg)
		       .setCancelable(false)
		       .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
		                dialog.cancel(); 
		                deleteRecord();
		               
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
  
//	****************************
}
	
	