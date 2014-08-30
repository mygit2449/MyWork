package com.daleelo.Dashboard.Activities;

import java.net.MalformedURLException;
import java.util.ArrayList;

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
import com.daleelo.Dashboard.Model.GetSpotLightModel;
import com.daleelo.Dashboard.Parser.SpotlightListParser;
import com.daleelo.DataBase.DatabaseHelper;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Hasanat.Activities.BusinessDetailsDescActivity;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.helper.FacebookShareHelper;
import com.daleelo.helper.FacebookShareHelperModified;
import com.daleelo.helper.ImageDownloader;
import com.daleelo.twitter.android.TwitterPostActivity;

public class SpotlightDetailDesp extends Activity implements OnClickListener{
	
	Intent reciverIntent;
	TextView mMainTitle, mName, mAddress, mPhone, mTitle, mSubTitle, mDesp, mDistance;
	
	ArrayList<GetSpotLightModel> mDataModelList = null;
	GetSpotLightModel mGetSpotLightModel = null;
	
	ImageButton mNext, mPrevious,call_btn; 
	ImageView mImgSave, mMap, mDirection, mShare, mImage;
	
	RelativeLayout mDespAddress;
	int mPosition;
	DatabaseHelper mDbHelper;
	
	Button mbtn_close, mbtn_twitter, mbtn_facebook, mbtn_email, mbtn_sms;
	private String mtext_to_share, mtext_to_send, mSubject, mtext_to_facebook_share
	, mtext_to_twitter_share, shareUrl;
	
	private RelativeLayout mRel_share;
	String mSpotLightId;


	private Animation slideout_left_animation;
	private Animation slidein_left_animation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.db_spotlight_detail_desp);		
		initializeUI();
		
	}
	
	private void initializeUI(){
		
		reciverIntent = getIntent();
		
		slidein_left_animation = AnimationUtils.loadAnimation(SpotlightDetailDesp.this, R.anim.slide_in_left_animation);
		slideout_left_animation = AnimationUtils.loadAnimation(SpotlightDetailDesp.this, R.anim.slide_out_left_animation);
		mRel_share = (RelativeLayout)findViewById(R.id.give_desc_REL_share);
		
		mMainTitle = (TextView)findViewById(R.id.dashboard_spot_TXT_main_title);
		mName  = (TextView)findViewById(R.id.dashboard_spot_TXT_desp_name);
		mAddress   = (TextView)findViewById(R.id.dashboard_spot_TXT_desp_address);
		mPhone   = (TextView)findViewById(R.id.dashboard_spot_TXT_desp_phone_number);
		mTitle   = (TextView)findViewById(R.id.dashboard_spot_TXT_title);
		mSubTitle   = (TextView)findViewById(R.id.dashboard_spot_TXT_sub_title);
		mDesp   = (TextView)findViewById(R.id.dashboard_spot_TXT_title_desp);
		mDistance  = (TextView)findViewById(R.id.dashboard_spot_TXT_desp_distance);
		
		mbtn_close = (Button)findViewById(R.id.share_BTN_close);
		mbtn_facebook = (Button)findViewById(R.id.share_BTN_facebook);
		mbtn_twitter = (Button)findViewById(R.id.share_BTN_twitter);
		mbtn_sms = (Button)findViewById(R.id.share_BTN_sms);
		mbtn_email = (Button)findViewById(R.id.share_BTN_email);
		
		mDespAddress = (RelativeLayout)findViewById(R.id.dashboard_spot_REL_desp_address_one);

		mNext  = (ImageButton)findViewById(R.id.dashboard_spot_IMGB_right);
		mPrevious  = (ImageButton)findViewById(R.id.dashboard_spot_IMGB_left);
		
		mImage = (ImageView)findViewById(R.id.dashboard_spot_TXT_title_image);
		mImgSave = (ImageView)findViewById(R.id.dashboard_spot_IMG_save);
		mMap = (ImageView)findViewById(R.id.dashboard_spot_IMG_map);
		mDirection = (ImageView)findViewById(R.id.dashboard_spot_IMG_direction);
		mShare = (ImageView)findViewById(R.id.dashboard_spot_IMG_share);
		call_btn = (ImageButton)findViewById(R.id.spotlight_desc_IMGB_call);
		
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
		call_btn.setOnClickListener(this);
		
		if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("list")){		
		
			mDataModelList = (ArrayList<GetSpotLightModel>) reciverIntent.getExtras().getSerializable("data");
			mPosition = reciverIntent.getExtras().getInt("position");
			
			if(mPosition == (mDataModelList.size()-1))
				mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			
			if(mPosition == 0)
				mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);			
			
			mGetSpotLightModel = mDataModelList.get(mPosition);			
	
			mNext.setOnClickListener(this);		
			mPrevious.setOnClickListener(this);			
			setData();		
		
		}else if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("home")){			
			
			mGetSpotLightModel = (GetSpotLightModel) reciverIntent.getExtras().getSerializable("data");
			mNext.setVisibility(View.INVISIBLE);
			mPrevious.setVisibility(View.INVISIBLE);
			setData();		
			
		}else if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("item")){
			
			mSpotLightId = reciverIntent.getExtras().getString("id");
			
			try {
				
				mGetSpotLightModel = null;
				getParserData();
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mNext.setVisibility(View.GONE);
			mPrevious.setVisibility(View.GONE);
			
		}else{			
			
			mDataModelList = (ArrayList<GetSpotLightModel>) reciverIntent.getExtras().getSerializable("data");
			mPosition = reciverIntent.getExtras().getInt("position");
			mSpotLightId = mDataModelList.get(mPosition).getSpotLightID();	
			
			if(mPosition == (mDataModelList.size()-1))
				mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			
			if(mPosition == 0)
				mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);			
	
			mNext.setOnClickListener(this);		
			mPrevious.setOnClickListener(this);	
			
			try {
				
				mGetSpotLightModel = null;
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
				
				startActivity(new Intent(SpotlightDetailDesp.this, GlobalSearchActivity.class));
				
			}
		});	
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(SpotlightDetailDesp.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(SpotlightDetailDesp.this,MyStuffActivity.class)
				.putExtra("from", "bottom"));  
			
			}
		});
		
		
	}
	
	
	public void onClick(View v){
		
		switch(v.getId()){
		
		
		
			case R.id.dashboard_spot_IMGB_left:
			
				mPosition--;
				
				if(mPosition >= 0){				
					
					
					if(mPosition == 0)
						mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);
					else
						mPrevious.setBackgroundResource(R.drawable.left_arrow);	
					
					mNext.setBackgroundResource(R.drawable.right_arrow);
					
					if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("stuff")){
					
						mSpotLightId = mDataModelList.get(mPosition).getSpotLightID();	
					
						try {
							
							mGetSpotLightModel = null;
							getParserData();
							
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}else{
						
						mGetSpotLightModel = mDataModelList.get(mPosition);
						setData();
						
					}
					
				}else{
					
					mPosition++;
					
				}				
			
			break;
			
		case R.id.dashboard_spot_IMGB_right:
			
			mPosition++;
			
			if(mPosition <= (mDataModelList.size()-1)){
				
								
				if(mPosition == (mDataModelList.size()-1))
					mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
				else
					mNext.setBackgroundResource(R.drawable.right_arrow);
				
				mPrevious.setBackgroundResource(R.drawable.left_arrow);	
				
				if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("stuff")){
					
					mSpotLightId = mDataModelList.get(mPosition).getSpotLightID();	
				
					try {
						
						mGetSpotLightModel = null;
						getParserData();
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					
					mGetSpotLightModel = mDataModelList.get(mPosition);
					setData();
					
				}
				
								
			}else{
				
				mPosition--;
			}
			
			break;
		
	case R.id.dashboard_spot_REL_desp_address_one:
			
			startActivity(new Intent(SpotlightDetailDesp.this,BusinessDetailDesp.class)
			.putExtra("from", "item")
			.putExtra("id", mGetSpotLightModel.getBusinessID()));			
			
			break;
			
		case R.id.dashboard_spot_IMG_save:
			
			try {
				
				mImgSave.setClickable(false);
				
				mDbHelper = new DatabaseHelper(getApplicationContext());
			
				mDbHelper.openDataBase();
				long result = mDbHelper.insertSpotLightItem(
						mGetSpotLightModel.getSpotLightID(), 
						mGetSpotLightModel.getSpotLightName(), 
						mGetSpotLightModel.getDescription(), 
						mGetSpotLightModel.getAddressLine1(), 
						mGetSpotLightModel.getImageUrl(), 
						mGetSpotLightModel.getBusinessID(), 
						mGetSpotLightModel.getBusinessTitle(), 
						mGetSpotLightModel.getAddressLat(), 
						mGetSpotLightModel.getAddressLong(), 
						mGetSpotLightModel.getCityName());			
				
				if(result == 0){
					
					alertDialogDB("Spotlight item already saved to My Stuff");				
					
				}else if(result == -1){
					
					alertDialogDB("Unable to save the item to My Stuff");			
					
				}else{
					
					alertDialogDB("Spotlight item saved successfully to My Stuff");	
				}
				
				mDbHelper.closeDataBase();
				mDbHelper = null;
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				mImgSave.setClickable(true);
				e.printStackTrace();
			}
				
			
			break;	
			
			case R.id.dashboard_spot_IMG_map:
				
				startActivity(new Intent(SpotlightDetailDesp.this,SpotLightMapActivity.class)
				.putExtra("data", mGetSpotLightModel)
				.putExtra("from","desp"));			
				
				break;			
				
			case R.id.dashboard_spot_IMG_direction:			
				
				 Intent navigateIntent = new Intent(android.content.Intent.ACTION_VIEW,  
						 Uri.parse("http://maps.google.com/maps?" +
						 		"saddr="+CurrentLocationData.LATITUDE+","+CurrentLocationData.LONGITUDE+
						 		"&daddr="+mGetSpotLightModel.getAddressLat()+"," +
						 				""+mGetSpotLightModel.getAddressLong())); 
				 startActivity(navigateIntent);	
				
				break;
				
			case R.id.dashboard_spot_IMG_share:	
				
				mRel_share.setVisibility(View.VISIBLE);
				mRel_share.startAnimation(slidein_left_animation);
				
				break;
				
			case R.id.share_BTN_close:
				
				mRel_share.startAnimation(slideout_left_animation);
				mRel_share.setVisibility(View.GONE);
				
				break;
				
			case R.id.share_BTN_facebook:
				
				shareUrl = String.format(Urls.SHARE_BUSINESS_URL, mGetSpotLightModel.getBusinessID());
				Log.e("", "mtext_to_share "+mtext_to_share);
				mRel_share.startAnimation(slideout_left_animation);
				mRel_share.setVisibility(View.GONE);
				new FacebookShareHelperModified(SpotlightDetailDesp.this, mtext_to_facebook_share , mShareName, shareUrl);
						
				break;
				
			case R.id.share_BTN_twitter:

				shareUrl = String.format(Urls.SHARE_BUSINESS_URL, mGetSpotLightModel.getBusinessID());
				mRel_share.startAnimation(slideout_left_animation);
				mRel_share.setVisibility(View.GONE);
				startActivity(new Intent(SpotlightDetailDesp.this,TwitterPostActivity.class)
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
				     Toast.makeText(SpotlightDetailDesp.this, "SMS facility is not available in your device.", Toast.LENGTH_SHORT).show();
				   }
				
				break;
			case R.id.spotlight_desc_IMGB_call:
				
				new AlertDialogMsg(SpotlightDetailDesp.this, "Are you sure you want to call" 
						+ ((mGetSpotLightModel.getAddressPhone() != null)?mGetSpotLightModel.getAddressPhone():""))
				.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						String phoneNumber = mGetSpotLightModel.getAddressPhone();
						Uri uri = Uri.fromParts("tel", phoneNumber, null);
						Intent callIntent = new Intent(Intent.ACTION_CALL, uri);
						//callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(callIntent);
					}
					
				})
				.setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
					
				})
				.create().show();
				
				break;
		
		}
	
	}
	
	String mShareName, mShareAddressOne, mShareAddressTwo, mShareCity, mShareState, mShareZip;

	
	private void setData(){
		
		mMainTitle.setText(mGetSpotLightModel.getBusinessTitle());
		mName.setText(mGetSpotLightModel.getBusinessTitle());
		mAddress.setText(mGetSpotLightModel.getBusinessAddress());
		mPhone.setText(mGetSpotLightModel.getAddressPhone());
		mTitle.setText(mGetSpotLightModel.getSpotLightName());
		mSubTitle.setText(mGetSpotLightModel.getSpotlightsubname());
		mDesp.setText(mGetSpotLightModel.getCreatedDate());
		mDistance.setText(mGetSpotLightModel.getDistance().toString()+" mi");
				

		mShareName = mGetSpotLightModel.getBusinessTitle();
		mShareAddressOne = (mGetSpotLightModel.getAddressLine1().length()>0 ? mGetSpotLightModel.getAddressLine1()+", ":"");
		mShareAddressTwo = (mGetSpotLightModel.getAddressLine2().length()>0 ? mGetSpotLightModel.getAddressLine2()+", ":"");
		mShareCity =  (mGetSpotLightModel.getCityName().length()>0 ? mGetSpotLightModel.getCityName()+", ":"");
		mShareState =  (mGetSpotLightModel.getStateCode().length()>0 ? mGetSpotLightModel.getStateCode()+", ":"");
		mShareZip = (mGetSpotLightModel.getAddressZipcode().length()>0 ? mGetSpotLightModel.getAddressZipcode():"");
		
		mtext_to_send = mShareName+"\n"+mShareAddressOne+""+mShareAddressTwo+"\n"+mShareCity+""+mShareState+""+mShareZip;			
		
		mtext_to_share="<center></center><b>"+mShareName+"</b><center></center>" +
		"Address:<center></center>"
		+mShareAddressOne+""+mShareAddressTwo+"<center></center>"
		+mShareCity+""+mShareState+""+mShareZip+"<center></center>";
		
		mtext_to_facebook_share = "Address:<center></center>"
			+mShareAddressOne+""+mShareAddressTwo+"<center></center>"
			+mShareCity+""+mShareState+""+mShareZip+"<center></center>";	
		
		mtext_to_twitter_share = "Check out what I found on @DaleeloApp!";
		
		if(mGetSpotLightModel.getImageUrl() != null)
			if(mGetSpotLightModel.getImageUrl().length() > 3)
				new ImageDownloader().download(String.format(Urls.SL_IMG_URL,mGetSpotLightModel.getImageUrl()), mImage);
		
		
		
		
	}
	
	
//	Web Service Calls
//	*****************
	
	ProgressDialog progressdialog;
	
	  public void getParserData() throws MalformedURLException{
	    	
	    	
	        	progressdialog = ProgressDialog.show(SpotlightDetailDesp.this, "","Please wait...", true);
	        	try {
		    		
		        	String mUrl = Urls.BASE_URL+"GetSpotlightitemsbySpotLight?SpotLightID="+mSpotLightId;
		    		new mAsyncClassifiedsFeedFetcher(mUrl, new FeedParserHandler()).start();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	

	    }
	    
	    
	    class mAsyncClassifiedsFeedFetcher extends Thread
		{
			String  feedUrl;
			Handler handler;
			
			public mAsyncClassifiedsFeedFetcher(String mUrl, Handler handler) throws MalformedURLException {
			
				
				Log.i("", "mUrl********* "+mUrl);			
				
				this.feedUrl = mUrl;			
				this.handler = handler;
			
			} 
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try
				{
				
					new SpotlightListParser(feedUrl, handler).parser();
				
				}catch(Exception e){
					
					e.printStackTrace();
				}
				super.run();
			}
			
		}
	    
	    
	    String noDataMsg = "Local spotlights not found";
	    
		class FeedParserHandler extends Handler {
			public void handleMessage(android.os.Message msg) {
				
				progressdialog.dismiss();
				
				
				
				if(msg.what==0)
				{	
					
					mGetSpotLightModel = (GetSpotLightModel) msg.getData().getSerializable("datafeeds");
					setData();					

				}else if(msg.what==1){					
					
					new AlertDialogMsg(SpotlightDetailDesp.this, noDataMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							
						}
						
					}).create().show();
					
					
				}else if(msg.what==2){
					
					String mmsg = "Error connecting network or server not responding please try again..";
					
					new AlertDialogMsg(SpotlightDetailDesp.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							SpotlightDetailDesp.this.finish();
							
						}
						
					}).create().show();
				}

			}
		}
	
//	*****************
	
	
	

	private void alertDialogDB(String msg){
		
		
		mImgSave.setClickable(true);
		
		
		new AlertDialogMsg(SpotlightDetailDesp.this, msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				
			}
			
		}).create().show();
		
		
	}

}
