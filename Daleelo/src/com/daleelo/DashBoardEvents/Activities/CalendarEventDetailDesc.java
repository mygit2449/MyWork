package com.daleelo.DashBoardEvents.Activities;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Ads.Activities.AdvertiseActivity;
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.Business.Activities.BusinessSpecialOfferDetailDesp;
import com.daleelo.Business.Model.BusinessListModel;
import com.daleelo.DashBoardEvents.Model.EventsCalenderModel;
import com.daleelo.DashBoardEvents.Parser.EventsCalendarParser;
import com.daleelo.DataBase.DatabaseHelper;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.helper.DateFormater;
import com.daleelo.helper.FacebookShareHelper;
import com.daleelo.helper.FacebookShareHelperModified;
import com.daleelo.helper.PhotoGalleryActivity;
import com.daleelo.twitter.android.TwitterPostActivity;

public class CalendarEventDetailDesc extends Activity implements OnClickListener{
	
	Intent reciverIntent;
	TextView mMainTitle, mName, mAddress, mPhone, mDay, mTime, mDetail, mVenue, mOrganizer;
	ArrayList<EventsCalenderModel> mDataModelList = null, mDataModelListToParser = null;
	EventsCalenderModel mEventsCalenderModel = null;
	ImageView mRating, mRveiwArrow, mDealArrow, mMap, mDirection, mShare, mImgSave;
	Button mDonate, mPhoto, mWebsite, mEmail, mMoreinfo, mbtn_close, mbtn_twitter, mbtn_facebook, mbtn_email, mbtn_sms;;
	DatabaseHelper mDbHelper;
	RelativeLayout mMainView;
	
	ImageButton mNext, mPrevious, mCall;
	String Address="";
	private int mPosition;
	
	String mStrAddress, mEventId;
	private RelativeLayout mRel_share;
	private Animation slideout_left_animation;
	private Animation slidein_left_animation;
	private String mtext_to_send, mtext_to_share, mSubject, mtext_to_facebook_share
	, mtext_to_twitter_share, shareUrl;
	
	public  static final String[] 	MONTH			=	{"Jan","Feb","March","April","May","June","July","Aug","Sep","Oct","Nov","Dec"};
	public  static final String[] 	DAY_OF_WEEK		=	{"Sun","Mon","Tue","Wed","Thur","Fri","Sat"}; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.db_event_detail_desp);		
		initializeUI();
		
	}
	
	private void initializeUI(){
		
		reciverIntent = getIntent();
		
		slidein_left_animation = AnimationUtils.loadAnimation(CalendarEventDetailDesc.this, R.anim.slide_in_left_animation);
		slideout_left_animation = AnimationUtils.loadAnimation(CalendarEventDetailDesc.this, R.anim.slide_out_left_animation);
		mRel_share = (RelativeLayout)findViewById(R.id.give_desc_REL_share);
	
		mMainView = (RelativeLayout)findViewById(R.id.dashboard_event_REL_desp_one);
		mbtn_close = (Button)findViewById(R.id.share_BTN_close);
		mbtn_facebook = (Button)findViewById(R.id.share_BTN_facebook);
		mbtn_twitter = (Button)findViewById(R.id.share_BTN_twitter);
		mbtn_sms = (Button)findViewById(R.id.share_BTN_sms);
		mbtn_email = (Button)findViewById(R.id.share_BTN_email);
		
		mMainTitle = (TextView)findViewById(R.id.dashboard_event_TXT_main_title);
		mName = (TextView)findViewById(R.id.dashboard_event_TXT_desp_name);
		mAddress  = (TextView)findViewById(R.id.dashboard_event_TXT_desp_address);
		mDay = (TextView)findViewById(R.id.dashboard_event_TXT_event_day);
		mTime = (TextView)findViewById(R.id.dashboard_event_TXT_event_time);
		mDetail = (TextView)findViewById(R.id.dashboard_event_TXT_detail);
		mVenue = (TextView)findViewById(R.id.dashboard_event_TXT_venue);
		mOrganizer = (TextView)findViewById(R.id.dashboard_event_TXT_organizer);
		
		mImgSave = (ImageView)findViewById(R.id.dashboard_event_BTN_save);
		mMap = (ImageView)findViewById(R.id.dashboard_event_BTN_map);
		mDirection = (ImageView)findViewById(R.id.dashboard_event_BTN_direction);
		mShare = (ImageView)findViewById(R.id.dashboard_event_BTN_share);
		
		mNext  = (ImageButton)findViewById(R.id.dashboard_event_desp_IMGB_right);
		mPrevious  = (ImageButton)findViewById(R.id.dashboard_event_desp_IMGB_left);
		mCall  = (ImageButton)findViewById(R.id.dashboard_event_BTN_call); 
		
		mDonate = (Button)findViewById(R.id.dashboard_event_BTN_donate);
		mPhoto = (Button)findViewById(R.id.dashboard_event_BTN_photo);
		mWebsite = (Button)findViewById(R.id.dashboard_event_BTN_website);
		mEmail = (Button)findViewById(R.id.dashboard_event_BTN_email);
		mMoreinfo = (Button)findViewById(R.id.dashboard_event_BTN_moreinfo);		
		
		mMap.setOnClickListener(this);
		mDirection.setOnClickListener(this);
		mShare.setOnClickListener(this);
		mImgSave.setOnClickListener(this);
		mCall.setOnClickListener(this);
		
		mbtn_close.setOnClickListener(this);
		mbtn_twitter.setOnClickListener(this);
		mbtn_facebook.setOnClickListener(this);
		mbtn_email.setOnClickListener(this);
		mbtn_sms.setOnClickListener(this);
		
		mDonate.setOnClickListener(this);
		mPhoto.setOnClickListener(this);
		mWebsite.setOnClickListener(this);
		mEmail.setOnClickListener(this);
		mMoreinfo.setOnClickListener(this);
		mImgSave.setOnClickListener(this);		
		
		if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("list")){
			
			mDataModelList = (ArrayList<EventsCalenderModel>) reciverIntent.getExtras().getSerializable("data");
			
			mPosition = reciverIntent.getExtras().getInt("position");	

			
			if(mPosition == (mDataModelList.size()-1))
				mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			
			if(mPosition == 0)
				mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);			
			
			mEventsCalenderModel = mDataModelList.get(mPosition);	
			
			mNext.setOnClickListener(this);		
			mPrevious.setOnClickListener(this);	
			setData();
			
		}else if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("home")){
			
			mEventsCalenderModel = (EventsCalenderModel) reciverIntent.getExtras().getSerializable("data");
			mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);			
			setData();
			
		}else if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("stuff")){
			

			mDataModelList = (ArrayList<EventsCalenderModel>) reciverIntent.getExtras().getSerializable("data");
			mPosition = reciverIntent.getExtras().getInt("position");
			
			
			if(mPosition == (mDataModelList.size()-1))
				mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			
			if(mPosition == 0)
				mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);			
			
		
			mNext.setOnClickListener(this);		
			mPrevious.setOnClickListener(this);	
			
			try {
				
				mDataModelListToParser = new ArrayList<EventsCalenderModel>();
				getParserData();
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("item")){
			
			mEventId = reciverIntent.getExtras().getString("id");
			
			
			mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);	
			
			try {
				
				mDataModelListToParser = new ArrayList<EventsCalenderModel>();
				getParserDataById();
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		setBottomBar();
		
	}
	
	private ImageButton mHome, mMyStuff;
	private EditText mSearch;
	
	private void setBottomBar(){
		
		
		mHome = (ImageButton)findViewById(R.id.business_IMGB_home);
		mMyStuff = (ImageButton)findViewById(R.id.business_IMGB_mystuff);
		mSearch = (EditText)findViewById(R.id.business_TXT_serach);
		
		mSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(CalendarEventDetailDesc.this, GlobalSearchActivity.class));
				
			}
		});	
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(CalendarEventDetailDesc.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(CalendarEventDetailDesc.this,MyStuffActivity.class)
				.putExtra("from", "bottom"));  
			
			}
		});
	}
	
	
	String mShareName, mShareAddressOne, mShareAddressTwo, mShareCity, mShareState, mShareZip;
	
	private void setData(){
		
		mMainView.setVisibility(View.VISIBLE);
		if(Boolean.parseBoolean(mEventsCalenderModel.getFundrising())){
			mDonate.setVisibility(View.VISIBLE);
		}else{
			mDonate.setVisibility(View.GONE);
		}
		mMainTitle.setText(mEventsCalenderModel.getEventTitle());
		mName.setText(mEventsCalenderModel.getEventTitle());
		
		mShareName = mEventsCalenderModel.getEventTitle();
		mShareAddressOne = (mEventsCalenderModel.getVenueLocation().length()>0 ? mEventsCalenderModel.getVenueLocation()+", ":"");
		mShareCity = (mEventsCalenderModel.getVenueCity().length()>0 ? mEventsCalenderModel.getVenueCity()+", ":"");
		mShareState =  (mEventsCalenderModel.getStateCode().length()>0 ? mEventsCalenderModel.getStateCode()+", ":"");
		mShareZip = (mEventsCalenderModel.getZipcode().length()>0 ? mEventsCalenderModel.getZipcode():"");
		
		mStrAddress = mShareAddressOne+"\n"+mShareCity+""+mShareState+""+mShareZip;			
		mtext_to_send = mShareName+"\n"+mStrAddress;
		
		mtext_to_share="<center></center><b>"+mShareName+"</b><center></center>" +
		"Address:<center></center>"
		+mShareAddressOne+"<center></center>"
		+mShareCity+""+mShareState+""+mShareZip+"<center></center>";	
		
		mtext_to_facebook_share = "Address:<center></center>"
			+mShareAddressOne+"<center></center>"
			+mShareCity+""+mShareState+""+mShareZip+"<center></center>";	
		
		mtext_to_twitter_share = "Check out what I found on @DaleeloApp!";
		
		mAddress.setText(mStrAddress);
				 
		
		mDetail.setText(mEventsCalenderModel.getEventTitle()+" Details: "+mEventsCalenderModel.getEventDetails());
		mVenue.setText("Venue: "+mEventsCalenderModel.getVenueName());
		mOrganizer.setText("Organizer: "+mEventsCalenderModel.getEventOrganizer());
		
		long timeStampOne = DateFormater.parseDate(mEventsCalenderModel.getEventStartsOn(), "MM/dd/yyyy h:mm:ss a");
		long timeStampTwo = DateFormater.parseDate(mEventsCalenderModel.getEventEndsOn(), "MM/dd/yyyy h:mm:ss a");
		
		Date dateOne = new Date(timeStampOne);
		Date dateTwo = new Date(timeStampTwo);
		
		Calendar calendarOne=Calendar.getInstance();
		calendarOne.setTime(dateOne);
		
		Calendar calendarTwo=Calendar.getInstance();
		calendarTwo.setTime(dateTwo);
		
		String mDAyy = DAY_OF_WEEK[calendarOne.get(Calendar.DAY_OF_WEEK)-1]+", "+MONTH[dateOne.getMonth()]+" "+dateOne.getDate()+", " + (dateOne.getYear()+1900)+" to "+
			DAY_OF_WEEK[calendarTwo.get(Calendar.DAY_OF_WEEK)-1]+", "+MONTH[dateTwo.getMonth()]+" "+dateTwo.getDate()+", " + (dateTwo.getYear()+1900);
		
		String mTimee = calendarOne.get(Calendar.HOUR) +":"+ calendarOne.get(Calendar.MINUTE)+" "+((calendarOne.get(Calendar.AM_PM) == 0) ? "AM" : "PM")+" to "+
		calendarTwo.get(Calendar.HOUR) +":"+ calendarTwo.get(Calendar.MINUTE)+" "+((calendarTwo.get(Calendar.AM_PM) == 0) ? "AM" : "PM");
		
		mDay.setText(mDAyy);
		mTime.setText(mTimee);
		
	}

//	****************************	
	
//	OnclickListener actions
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		
		
		case R.id.dashboard_event_desp_IMGB_left:
			
			mPosition--;
			
			if(mPosition >= 0){				
				
				
				if(mPosition == 0)
					mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);
				else
					mPrevious.setBackgroundResource(R.drawable.left_arrow);	
				
				mNext.setBackgroundResource(R.drawable.right_arrow);				
				
				
				if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("stuff")){
					
					try {
						
						mDataModelListToParser = new ArrayList<EventsCalenderModel>();
						getParserData();
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{	
				
					mEventsCalenderModel = mDataModelList.get(mPosition);
					setData();
					
				}
				
			}else{
				
				mPosition++;
				
			}				
		
		break;
		
	case R.id.dashboard_event_desp_IMGB_right:
		
		mPosition++;
		
		if(mPosition < (mDataModelList.size())){
			
						
			if(mPosition == (mDataModelList.size()-1))
				mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			else
				mNext.setBackgroundResource(R.drawable.right_arrow);
			
			mPrevious.setBackgroundResource(R.drawable.left_arrow);	
			
			if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("stuff")){
				
				try {
					
					mDataModelListToParser = new ArrayList<EventsCalenderModel>();
					getParserData();
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else{
				
				mEventsCalenderModel = mDataModelList.get(mPosition);
				setData();
			}

							
		}else{
			
			mPosition--;
		}
		
		break;
		
		
		case R.id.dashboard_event_BTN_call:
			
			if(mEventsCalenderModel.getAddressPhone()!= null){
			
				if(mEventsCalenderModel.getAddressPhone().length()>9){
					
					String phoneNumber = mEventsCalenderModel.getAddressPhone();
					Uri uri = Uri.fromParts("tel", phoneNumber, null);
					Intent callIntent = new Intent(Intent.ACTION_CALL, uri);
					startActivity(callIntent);
					
				}else{
					
					Toast.makeText(this, "Contact number not available.", Toast.LENGTH_SHORT).show();
				}
			
			}else{
				
				Toast.makeText(this, "Contact number not available.", Toast.LENGTH_SHORT).show();
			}
		
			
			break;

			
			
		case R.id.dashboard_event_BTN_save:
			
			
			try {
				
				mDbHelper = new DatabaseHelper(getApplicationContext());
				
			
				mDbHelper.openDataBase();
				
				long result = mDbHelper.insertEventItem(
						mEventsCalenderModel.getEventId(), 
						mEventsCalenderModel.getEventTitle(), 
						mEventsCalenderModel.getVenueLocation(), 
						mEventsCalenderModel.getEventOrganizer(), 
						mEventsCalenderModel.getEventStartsOn(), 
						mEventsCalenderModel.getEventEndsOn(), 
						mEventsCalenderModel.getEventIsFeatured(),
						mEventsCalenderModel.getEventDetails(), 
						mEventsCalenderModel.getVenueCity(), 
						mEventsCalenderModel.getBusinessID(), 
						mEventsCalenderModel.getLatitude(), 
						mEventsCalenderModel.getLongitude(), 
						mEventsCalenderModel.getFundrising());
				
				if(result == 0){
					
					alertDialogDB("Event already saved to My Stuff");				
					
				}else if(result == -1){
					
					alertDialogDB("Unable to save the item to My Stuff");			
					
				}else{
					
					alertDialogDB("Event saved successfully to My Stuff");	
				}
				
				mDbHelper.closeDataBase();
				mDbHelper = null;
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			break;
			
		case R.id.dashboard_event_BTN_photo:
			
			startActivity(new Intent(CalendarEventDetailDesc.this,PhotoGalleryActivity.class));		
		
			break;
			
		case R.id.dashboard_event_BTN_website:
			
			
//			if(mEventsCalenderModel.get.length()<9){
				
				Toast.makeText(this, "Website address not found.", Toast.LENGTH_SHORT).show();
			
//			}else{
//				
//				String url = "http://"+mEventsCalenderModel.getAddressWeburl();
//				Intent i = new Intent(Intent.ACTION_VIEW);
//				i.setData(Uri.parse(url));
//				startActivity(i);			
//			}
	
			
			
			break;
			
		case R.id.dashboard_event_BTN_email:
			
			Toast.makeText(this, "Email address not found.", Toast.LENGTH_SHORT).show();
			
//			Intent intent=new Intent(Intent.ACTION_SEND);
//            intent.putExtra(Intent.EXTRA_SUBJECT, "Daleelo Application");
//            intent.setType("plain/text");
//            startActivity(intent );
			
			break;
			
		case R.id.dashboard_event_BTN_moreinfo:
			
			Toast.makeText(this, "Currently no data to display.", Toast.LENGTH_SHORT).show();		
			
			break;
			
		case R.id.dashboard_event_BTN_map:			
			
				
				if(mEventsCalenderModel != null){
					
					startActivity(new Intent(CalendarEventDetailDesc.this,EventsMapActivity.class)
					.putExtra("data", mEventsCalenderModel)
					.putExtra("from","desp"));
					
				}else{
					
					Toast.makeText(this, "nodata", Toast.LENGTH_SHORT).show();
				}
				
							
		
			break;
			
		case R.id.dashboard_event_BTN_direction:
			
			
			 Intent navigateIntent = new Intent(android.content.Intent.ACTION_VIEW,  
					 Uri.parse("http://maps.google.com/maps?" +
					 		"saddr="+CurrentLocationData.LATITUDE+","+CurrentLocationData.LONGITUDE+
					 		"&daddr="+mEventsCalenderModel.getLatitude()+"," +
					 				""+mEventsCalenderModel.getLongitude())); 
			 startActivity(navigateIntent);	
			
			break;
			
		case R.id.dashboard_event_BTN_share:
			
			mRel_share.setVisibility(View.VISIBLE);
			mRel_share.startAnimation(slidein_left_animation);
			
			break;
			
		case R.id.dashboard_event_BTN_donate:
			
			startActivity(new Intent(CalendarEventDetailDesc.this,AdvertiseActivity.class));
			
			break;
			
		case R.id.share_BTN_close:
			
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			
			break;
			
		case R.id.share_BTN_facebook:
			
			shareUrl = String.format(Urls.SHARE_BUSINESS_URL, mEventsCalenderModel.getBusinessID());
			Log.e("", "mtext_to_share "+mtext_to_share);
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			new FacebookShareHelperModified(CalendarEventDetailDesc.this, mtext_to_facebook_share , mShareName, shareUrl);
				
			break;
			
		case R.id.share_BTN_twitter:
			
			shareUrl = String.format(Urls.SHARE_BUSINESS_URL, mEventsCalenderModel.getBusinessID());
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			startActivity(new Intent(CalendarEventDetailDesc.this,TwitterPostActivity.class)
			.putExtra("message", mtext_to_twitter_share)
			.putExtra("shareurl", shareUrl));
		
			
			break;
			
		case R.id.share_BTN_email:	
			
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			  Intent emailIntentone=new Intent(Intent.ACTION_SEND);
			  emailIntentone.putExtra(Intent.EXTRA_SUBJECT,mShareName );
			  emailIntentone.putExtra(emailIntentone.EXTRA_TEXT, mtext_to_send);
			  emailIntentone.setType("plain/text");
			  startActivity(emailIntentone );
			
			break;
			
		case R.id.share_BTN_sms:
			
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			 try
			   {
			     String number = "";  // The number on which you want to send SMS
			           startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)).
			                   putExtra("sms_body", mtext_to_send));
			   }catch (Exception e) {
			     Toast.makeText(CalendarEventDetailDesc.this, "SMS facility is not available in your device.", Toast.LENGTH_SHORT).show();
			   }
			
			break;
			
			
		}
	}
	
//	*******************
	private ProgressDialog progressdialog;
	
	public void getParserData() throws MalformedURLException{
		
		mMainView.setVisibility(View.INVISIBLE);
    	
    	try {
        	progressdialog = ProgressDialog.show(CalendarEventDetailDesc.this, "","Please wait...", true);
        	
        	mEventId = mDataModelList.get(mPosition).getEventId();
        	String mUrl = String.format(Urls.EVENT_DETAILS_BY_ID_URL, mEventId);
        	
    		
    		EventsCalendarParser mEventsCalenderParser = new EventsCalendarParser(new FeedParserHandler(), mUrl , mDataModelListToParser);
    		mEventsCalenderParser.start(); 
        	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }
	
	
	public void getParserDataById() throws MalformedURLException{
		
		mMainView.setVisibility(View.INVISIBLE);
    	
    	try {
        	progressdialog = ProgressDialog.show(CalendarEventDetailDesc.this, "","Please wait...", true);
        	
        	String mUrl = String.format(Urls.EVENT_DETAILS_BY_ID_URL, mEventId);    		
    		EventsCalendarParser mEventsCalenderParser = new EventsCalendarParser(new FeedParserHandler(), mUrl , mDataModelListToParser);
    		mEventsCalenderParser.start(); 
        	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }
    
 
    
    String noDataMsg = "Event data not found";        
    
	class FeedParserHandler extends Handler {
		public void handleMessage(android.os.Message msg) {
			
			progressdialog.dismiss();
			
			if(msg.what==0)
			{					
				mEventsCalenderModel = mDataModelListToParser.get(0);
				setData();				

			}else if(msg.what==1){
				
				
				new AlertDialogMsg(CalendarEventDetailDesc.this, noDataMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
						
					}
					
				}).create().show();
				
				
			}else if(msg.what==2){
				
				
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(CalendarEventDetailDesc.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						CalendarEventDetailDesc.this.finish();
						
					}
					
				}).create().show();
			}

		}
		
	}
	
	
//	*******************
	
	
	
	private void alertDialogDB(String msg){
		
		
		
		new AlertDialogMsg(CalendarEventDetailDesc.this, msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				
			}
			
		}).create().show();
		
		
	}
	
	


}
