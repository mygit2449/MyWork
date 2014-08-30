package com.daleelo.Dashboard.Activities;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
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
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.Business.Activities.BusinessSpecialOfferDetailDesp;
import com.daleelo.Dashboard.Model.GetDealsInfoModel;
import com.daleelo.Dashboard.Parser.DealsListParser;
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
import com.daleelo.twitter.android.TwitterPostActivity;

public class DealsDetailDesp extends Activity implements OnClickListener{
	
	Intent reciverIntent;
	TextView mMainTitle, mName, mAddress, mPhone, mTitle, mSubTitle, mTimings, mDiscountPrice, mOrginalPrice, mDistance;
	
	ImageButton mNext, mPrevious;
	ImageView mImgSave, mMap, mDirection, mShare;
	
	RelativeLayout mDespAddress;
	
	Button mbtn_close, mbtn_twitter, mbtn_facebook, mbtn_email, mbtn_sms;
	private String mtext_to_share ,mtext_to_send, mtext_to_facebook_share
	, mtext_to_twitter_share, shareUrl;
	
	
	ArrayList<GetDealsInfoModel> mMainGetDealsInfoModel = null;
	GetDealsInfoModel mGetDealsInfoModel;
	DatabaseHelper mDbHelper;

	private RelativeLayout mRel_share;
	String mDealId;
	
	int mPosition;
	
	public  static final String[] 	MONTH			=	{
		
		"January"
		,"February"
		,"March"
		,"April"
		,"May"
		,"June"
		,"July"
		,"August"
		,"September"
		,"October"
		,"November"
		,"December"};
	

	private Animation slideout_left_animation;
	private Animation slidein_left_animation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.db_deal_detail_desp);		
		initializeUI();
		
	}
	
	private void initializeUI(){
		
		reciverIntent = getIntent();	
		
		slidein_left_animation = AnimationUtils.loadAnimation(DealsDetailDesp.this, R.anim.slide_in_left_animation);
		slideout_left_animation = AnimationUtils.loadAnimation(DealsDetailDesp.this, R.anim.slide_out_left_animation);
		mRel_share = (RelativeLayout)findViewById(R.id.give_desc_REL_share);
		
		mMainTitle = (TextView)findViewById(R.id.dashboard_deal_TXT_main_title);
		mName = (TextView)findViewById(R.id.dashboard_deal_TXT_desp_name);
		mAddress  = (TextView)findViewById(R.id.dashboard_deal_TXT_desp_address);
		mPhone  = (TextView)findViewById(R.id.dashboard_deal_TXT_desp_phone_number);
		mTitle  = (TextView)findViewById(R.id.dashboard_deal_TXT_title);
		mSubTitle   = (TextView)findViewById(R.id.dashboard_deal_TXT_sub_title);
		mTimings  = (TextView)findViewById(R.id.dashboard_deal_TXT_timings);
		mDiscountPrice = (TextView)findViewById(R.id.dashboard_deal_TXT_discount);		
		mOrginalPrice = (TextView)findViewById(R.id.dashboard_deal_TXT_orginal);
		mDistance = (TextView)findViewById(R.id.dashboard_deal_TXT_distance);
		mbtn_close = (Button)findViewById(R.id.share_BTN_close);
		mbtn_facebook = (Button)findViewById(R.id.share_BTN_facebook);
		mbtn_twitter = (Button)findViewById(R.id.share_BTN_twitter);
		mbtn_sms = (Button)findViewById(R.id.share_BTN_sms);
		mbtn_email = (Button)findViewById(R.id.share_BTN_email);
		
			
		mDespAddress = (RelativeLayout)findViewById(R.id.dashboard_deal_REL_desp_address_one);
		
		mNext = (ImageButton)findViewById(R.id.dashboard_deal_IMGB_right);
		mPrevious = (ImageButton)findViewById(R.id.dashboard_deal_IMGB_left);
		mImgSave = (ImageView)findViewById(R.id.dashboard_deal_IMG_save);
		mMap = (ImageView)findViewById(R.id.dashboard_deal_IMG_map);
		mDirection = (ImageView)findViewById(R.id.dashboard_deal_IMG_direction);
		mShare = (ImageView)findViewById(R.id.dashboard_deal_IMG_share);
		
		mDespAddress.setOnClickListener(this);
		mImgSave.setOnClickListener(this);
		mMap.setOnClickListener(this);
		mDirection.setOnClickListener(this);
		mShare.setOnClickListener(this);
		mbtn_close.setOnClickListener(this);
		mbtn_twitter.setOnClickListener(this);
		mbtn_facebook.setOnClickListener(this);
		mbtn_email.setOnClickListener(this);
		mbtn_sms.setOnClickListener(this);
		
		
		if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("list")){		
			
			mMainGetDealsInfoModel = (ArrayList<GetDealsInfoModel>) reciverIntent.getExtras().getSerializable("data");
			mPosition = reciverIntent.getExtras().getInt("position");
			
			if(mPosition == (mMainGetDealsInfoModel.size()-1))
				mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			
			if(mPosition == 0)
				mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);			
			
			mGetDealsInfoModel = mMainGetDealsInfoModel.get(mPosition);
	
			mNext.setOnClickListener(this);		
			mPrevious.setOnClickListener(this);
			setData();
		
		
		}else if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("home")){
			
			
			mGetDealsInfoModel = (GetDealsInfoModel) reciverIntent.getExtras().getSerializable("data");
			mNext.setVisibility(View.INVISIBLE);
			mPrevious.setVisibility(View.INVISIBLE);
			setData();
			
		}else if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("item")){
			
			mDealId = reciverIntent.getExtras().getString("id");
			
			try {
				
				mGetDealsInfoModel= null;
				getParserData();
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mNext.setVisibility(View.GONE);
			mPrevious.setVisibility(View.GONE);
			
		}else{			
			
			mMainGetDealsInfoModel = (ArrayList<GetDealsInfoModel>) reciverIntent.getExtras().getSerializable("data");
			mPosition = reciverIntent.getExtras().getInt("position");
			mDealId = mMainGetDealsInfoModel.get(mPosition).getDealId();
			
			if(mPosition == (mMainGetDealsInfoModel.size()-1))
				mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			
			if(mPosition == 0)
				mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);
			
	
			mNext.setOnClickListener(this);		
			mPrevious.setOnClickListener(this);
			
			try {
				
				mGetDealsInfoModel= null;
				getParserData();
				
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
				
				startActivity(new Intent(DealsDetailDesp.this, GlobalSearchActivity.class));
				
			}
		});	
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(DealsDetailDesp.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(DealsDetailDesp.this,MyStuffActivity.class)
				.putExtra("from", "bottom")); 
			
			}
		});
		
		
	}
	
	
	
	public void onClick(View v){
		
		switch(v.getId()){
		
		case R.id.dashboard_deal_IMGB_left:
			
			mPosition--;
			
			if(mPosition >= 0){				
				
				
				if(mPosition == 0)
					mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);
				else
					mPrevious.setBackgroundResource(R.drawable.left_arrow);	
				
				mNext.setBackgroundResource(R.drawable.right_arrow);
				
				if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("stuff")){
					
					mDealId = mMainGetDealsInfoModel.get(mPosition).getDealId();
					
					try {
						
						mGetDealsInfoModel= null;
						getParserData();
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					
					mGetDealsInfoModel = mMainGetDealsInfoModel.get(mPosition);
					setData();				
				}	
				
			}else{
				
				mPosition++;
				
			}				
			
			break;
			
		case R.id.dashboard_deal_IMGB_right:
			
			mPosition++;
			
			if(mPosition <= (mMainGetDealsInfoModel.size()-1)){			
				
				if(mPosition == (mMainGetDealsInfoModel.size()-1))
					mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
				else
					mNext.setBackgroundResource(R.drawable.right_arrow);
				
				mPrevious.setBackgroundResource(R.drawable.left_arrow);	
				
				
				if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("stuff")){
					
					mDealId = mMainGetDealsInfoModel.get(mPosition).getDealId();
					
					try {
						
						mGetDealsInfoModel= null;
						getParserData();
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					
					mGetDealsInfoModel = mMainGetDealsInfoModel.get(mPosition);
					setData();				
				}
								
			}else{
				
				mPosition--;
			}
			
			break;
			
		case R.id.dashboard_deal_REL_desp_address_one:
			
			startActivity(new Intent(DealsDetailDesp.this,BusinessDetailDesp.class)
			.putExtra("from", "item")
			.putExtra("id", mGetDealsInfoModel.getBusinessId()));			
			
			break;				
			
		case R.id.dashboard_deal_IMG_save:
			
			try {
				
				mImgSave.setClickable(false);
				
				mDbHelper = new DatabaseHelper(getApplicationContext());
			
				mDbHelper.openDataBase();
				long result = mDbHelper.insertDealItem(
						mGetDealsInfoModel.getDealId(), 
						mGetDealsInfoModel.getDealTittle(), 
						mGetDealsInfoModel.getAddressLine1(), 
						mGetDealsInfoModel.getDealImage(), 
						mGetDealsInfoModel.getBusinessId(), 
						mGetDealsInfoModel.getBusinessTitle(), 
						mGetDealsInfoModel.getAddressLat(), 
						mGetDealsInfoModel.getAddressLong(), 
						mGetDealsInfoModel.getCityName());			
				
				if(result == 0){
					
					alertDialogDB("Deal item already saved to My Stuff");				
					
				}else if(result == -1){
					
					alertDialogDB("Unable to save the item to My Stuff");			
					
				}else{
					
					alertDialogDB("Deal item saved successfully to My Stuff");	
				}
				
				mDbHelper.closeDataBase();
				mDbHelper = null;
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				mImgSave.setClickable(true);
				e.printStackTrace();
			}				
			
			break;				
			
		case R.id.dashboard_deal_IMG_map:
			
			startActivity(new Intent(DealsDetailDesp.this,DealsMapActivity.class)
			.putExtra("data", mGetDealsInfoModel)
			.putExtra("from","desp"));			
			
			break;			
			
		case R.id.dashboard_deal_IMG_direction:			
			
			 Intent navigateIntent = new Intent(android.content.Intent.ACTION_VIEW,  
					 Uri.parse("http://maps.google.com/maps?" +
					 		"saddr="+CurrentLocationData.LATITUDE+","+CurrentLocationData.LONGITUDE+
					 		"&daddr="+mGetDealsInfoModel.getAddressLat()+"," +
					 				""+mGetDealsInfoModel.getAddressLong())); 
			 startActivity(navigateIntent);	
			
			break;
			
		case R.id.dashboard_deal_IMG_share:	
			
			mRel_share.setVisibility(View.VISIBLE);
			mRel_share.startAnimation(slidein_left_animation);
			
			break;
			
		case R.id.share_BTN_close:
			
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			
			break;
			
		case R.id.share_BTN_facebook:
			
			shareUrl = String.format(Urls.SHARE_BUSINESS_URL, mGetDealsInfoModel.getBusinessId());
			Log.e("", "mtext_to_share "+mtext_to_share);
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			new FacebookShareHelperModified(DealsDetailDesp.this, mtext_to_facebook_share , mShareName, shareUrl);
			
			break;
			
		case R.id.share_BTN_twitter:
			
			shareUrl = String.format(Urls.SHARE_BUSINESS_URL, mGetDealsInfoModel.getBusinessId());
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			startActivity(new Intent(DealsDetailDesp.this,TwitterPostActivity.class)
			.putExtra("message", mtext_to_twitter_share)
			.putExtra("shareurl", shareUrl));
			
		
			
			break;
			
		case R.id.share_BTN_email:
			
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			  Intent emailIntent=new Intent(Intent.ACTION_SEND);
			  emailIntent.putExtra(Intent.EXTRA_SUBJECT,mShareName );
			  emailIntent.putExtra(emailIntent.EXTRA_TEXT, mtext_to_send);
			  emailIntent.setType("plain/text");
			  startActivity(emailIntent );
			
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
			     Toast.makeText(DealsDetailDesp.this, "SMS facility is not available in your device.", Toast.LENGTH_SHORT).show();
			   }
			
			break;
			
		}
	}
	
	

	DecimalFormat desimalFormat = new DecimalFormat("##0.00");
	String mShareName, mShareAddressOne, mShareAddressTwo, mShareCity, mShareState, mShareZip;
	
	private void setData(){
		
		mMainTitle.setText(mGetDealsInfoModel.getBusinessTitle());
		mName.setText(mGetDealsInfoModel.getBusinessTitle());
		mAddress.setText(mGetDealsInfoModel.getBusinessAddress());
		mPhone.setText(mGetDealsInfoModel.getAddressPhone());
		mDistance.setText(mGetDealsInfoModel.getDistance()+" mi");
		mTitle.setText(mGetDealsInfoModel.getDealInfo());
		mSubTitle.setText(mGetDealsInfoModel.getSubTittle());
		mTimings.setText("Valid: "+vailedUpTo(mGetDealsInfoModel.getDealValidFrom(), mGetDealsInfoModel.getDealValidTo()));
				
		
		mShareName = mGetDealsInfoModel.getBusinessTitle();
		mShareAddressOne = (mGetDealsInfoModel.getAddressLine1().length()>0 ? mGetDealsInfoModel.getAddressLine1()+", ":"");
		mShareAddressTwo = (mGetDealsInfoModel.getAddressLine2().length()>0 ? mGetDealsInfoModel.getAddressLine2()+", ":"");
		mShareCity =  (mGetDealsInfoModel.getCityName().length()>0 ? mGetDealsInfoModel.getCityName()+", ":"");
		mShareState =  (mGetDealsInfoModel.getStateCode().length()>0 ? mGetDealsInfoModel.getStateCode()+", ":"");
		mShareZip = (mGetDealsInfoModel.getAddressZipcode().length()>0 ? mGetDealsInfoModel.getAddressZipcode():"");
		
		mtext_to_send = mShareName+"\n"+mShareAddressOne+""+mShareAddressTwo+"\n"+mShareCity+""+mShareState+""+mShareZip;			
		
		mtext_to_share="<center></center><b>"+mShareName+"</b><center></center>" +
		"Address:<center></center>"
		+mShareAddressOne+""+mShareAddressTwo+"<center></center>"
		+mShareCity+""+mShareState+""+mShareZip+"<center></center>";
		
		mtext_to_facebook_share = "Address:<center></center>"
			+mShareAddressOne+""+mShareAddressTwo+"<center></center>"
			+mShareCity+""+mShareState+""+mShareZip+"<center></center>";	
		
		mtext_to_twitter_share = "Check out what I found on @DaleeloApp!";
	
		
			double tempStr = Double.parseDouble(mGetDealsInfoModel.getDealPrice());
	    	
	    	if(tempStr != 0.0){        
	    		
	    		mDiscountPrice.setText("Discount Price: $ "+desimalFormat.format(tempStr));	                            	
	    		mDiscountPrice.setVisibility(View.VISIBLE);
	    		
	    	}
	
    	
	    	double tempStrOrg = Double.parseDouble(mGetDealsInfoModel.getDealOriginalprice());
	    	
	    	if(tempStrOrg != 0.0){    
	    		
	    		mOrginalPrice.setText("Original Price: $ "+desimalFormat.format(tempStrOrg));	                            	
	    		mOrginalPrice.setVisibility(View.VISIBLE);
	    		
	    	}
    	
		
	}
	
	
//	WebService Call
//	***************
	
	ProgressDialog progressdialog;
	
	public void getParserData() throws MalformedURLException{
    	
    	
    	try {
    		
    	   	progressdialog = ProgressDialog.show(DealsDetailDesp.this, "","Please wait...", true);
        	
        	String mUrl = Urls.BASE_URL+"GetDealsByDealid?Deal_Id="+mDealId;
    		new mAsyncFeedFetcher(mUrl, new FeedParserHandler()).start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }
    
   
    
    class mAsyncFeedFetcher extends Thread
	{
		String  feedUrl;
		Handler handler;
		
		public mAsyncFeedFetcher(String mUrl, Handler handler) throws MalformedURLException {
		
			
			Log.i("", "mUrl********* "+mUrl);			
			
			this.feedUrl = mUrl;			
			this.handler = handler;
		
		} 
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try
			{
				
				new DealsListParser(feedUrl, handler).parser();
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			super.run();
		}
		
	}
    
    
    String noDataMsg = "Deals information not found";
    
	class FeedParserHandler extends Handler {
		public void handleMessage(android.os.Message msg) {
			
			progressdialog.dismiss();
			
			
			
			if(msg.what==0)
			{	
				mGetDealsInfoModel = ((ArrayList<GetDealsInfoModel>) msg.getData().getSerializable("datafeeds")).get(0);
				setData();							

			}else if(msg.what==1){
				
				
				
				new AlertDialogMsg(DealsDetailDesp.this, noDataMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
					}
					
				}).create().show();
				
				
		}else if(msg.what==2){
				
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(DealsDetailDesp.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						DealsDetailDesp.this.finish();
						
					}
					
				}).create().show();
			}

		}
	}
	
//	***************
	
	private String vailedUpTo(String start, String end){
		
		long timeStampOne = DateFormater.parseDate(start, "MM/dd/yyyy h:mm:ss a");
		long timeStampTwo = DateFormater.parseDate(end, "MM/dd/yyyy h:mm:ss a");
		
		Date dateOne = new Date(timeStampOne);
		Date dateTwo = new Date(timeStampTwo);
		
		Calendar calendarOne=Calendar.getInstance();
		calendarOne.setTime(dateOne);
		
		Calendar calendarTwo=Calendar.getInstance();
		calendarTwo.setTime(dateTwo);
		
		String mDayOne = MONTH[dateOne.getMonth()]+" "+dateOne.getDate()+", " + (dateOne.getYear()+1900);
		String mDayTwo = MONTH[dateTwo.getMonth()]+" "+dateTwo.getDate()+", " + (dateTwo.getYear()+1900);
		
		return mDayOne+" to "+mDayTwo;
		
		
	}
	
	

	private void alertDialogDB(String msg){
		
		mImgSave.setClickable(true);
		
		new AlertDialogMsg(DealsDetailDesp.this, msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				
			}
			
		}).create().show();
		
		
	}
	

}
