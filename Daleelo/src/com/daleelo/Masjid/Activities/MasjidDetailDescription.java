package com.daleelo.Masjid.Activities;

import java.net.MalformedURLException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.Business.Activities.BusinessMapActivity;
import com.daleelo.Business.Activities.BusinessSpecialOfferDetailDesp;
import com.daleelo.Dashboard.Activities.SpotlightDetailDesp;
import com.daleelo.DataBase.DatabaseHelper;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Hasanat.Activities.GiveDetailDescActivity;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.Masjid.Model.MasjidModel;
import com.daleelo.Masjid.Parser.MasjidLocationParser;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.helper.FacebookShareHelper;
import com.daleelo.helper.FacebookShareHelperModified;
import com.daleelo.helper.PhotoGalleryActivity;
import com.daleelo.helper.PrayerScheduleActivity;
import com.daleelo.twitter.android.TwitterPostActivity;

public class MasjidDetailDescription extends Activity implements OnClickListener{
	
	Intent reciverIntent;
	TextView mMainTitle, mName, mAddress, mPhone, mReviews, mSubTitle, mDesp,
	mHours, mPayment, mReviesCount, mOfferLabel, mOfferCount, mImam, mDemonination, mDemographic, mLanguage, mService;
	
	StringBuffer mPayOptins;
	ImageButton mWrite, mNext, mPrevious;
	
	RelativeLayout mRelHours, mRelPayments, mReview, mSpecilaOffer, mSpecilaOfferLabel;
	LinearLayout   mSpecilaOfferData;
	
	ImageView mRating, mRveiwArrow;
	Button mImgSave, mCall, mMap, mDirection, mShare, mPrayerTime, mPhoto, mWebSite, mEmail, mMoreInfo;
	View mHourPaySeperator;
	ArrayList<MasjidModel> MasjidDataFeeds =null, MasjidDataFeedsTwo = null;
	MasjidModel mMainMasjidLocationModel = null;
	DatabaseHelper mDbHelper;
	String mStrAddress;
		
	private RelativeLayout mRel_share;
	Button mbtn_close, mbtn_twitter, mbtn_facebook, mbtn_email, mbtn_sms;
	private String mtext_to_share,mtext_to_send, mtext_to_facebook_share
	, mtext_to_twitter_share, shareUrl;
	
	private Animation slideout_left_animation;
	private Animation slidein_left_animation;
	
	String mRate, mReviewCount = "1";
	
	public static String mBusinessId;
	
	public SharedPreferences sharedpreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.masjid_detail_desc);		
		initializeUI();
		
	}
	
	private void initializeUI(){
		
		reciverIntent = getIntent();
		
		sharedpreference = getSharedPreferences("userlogin",MODE_PRIVATE);
		
		slidein_left_animation = AnimationUtils.loadAnimation(MasjidDetailDescription.this, R.anim.slide_in_left_animation);
		slideout_left_animation = AnimationUtils.loadAnimation(MasjidDetailDescription.this, R.anim.slide_out_left_animation);
		mRel_share = (RelativeLayout)findViewById(R.id.give_desc_REL_share);
		mbtn_close = (Button)findViewById(R.id.share_BTN_close);
		mbtn_facebook = (Button)findViewById(R.id.share_BTN_facebook);
		mbtn_twitter = (Button)findViewById(R.id.share_BTN_twitter);
		mbtn_sms = (Button)findViewById(R.id.share_BTN_sms);
		mbtn_email = (Button)findViewById(R.id.share_BTN_email);
		
		mMainTitle = (TextView)findViewById(R.id.masjid_TXT_main_title);
		mName  = (TextView)findViewById(R.id.masjid_TXT_name);
		mAddress   = (TextView)findViewById(R.id.masjid_TXT_address);
		mPhone   = (TextView)findViewById(R.id.masjid_TXT_phone_number);
		mImam = (TextView)findViewById(R.id.masjid_TXT_imam);
		mDemonination = (TextView)findViewById(R.id.masjid_TXT_denomination); 
		mDemographic = (TextView)findViewById(R.id.masjid_TXT_demographics); 
		mLanguage = (TextView)findViewById(R.id.masjid_TXT_language);
		mService = (TextView)findViewById(R.id.masjid_TXT_service);
		
		mImgSave = (Button)findViewById(R.id.masjid_BTN_save);
		mCall = (Button)findViewById(R.id.masjid_IMGB_call);
		mMap = (Button)findViewById(R.id.masjid_BTN_map);
		mDirection = (Button)findViewById(R.id.masjid_BTN_direction);
		mShare = (Button)findViewById(R.id.masjid_BTN_share);
		mPrayerTime = (Button)findViewById(R.id.masjid_BTN_prayer);
		
		mPhoto = (Button)findViewById(R.id.masjid_BTN_photo);
		mWebSite = (Button)findViewById(R.id.masjid_BTN_website);
		mEmail = (Button)findViewById(R.id.masjid_BTN_email);
		mMoreInfo = (Button)findViewById(R.id.masjid_BTN_moreinfo);
		
		mNext  = (ImageButton)findViewById(R.id.masjid_IMGB_right);
		mPrevious  = (ImageButton)findViewById(R.id.masjid_IMGB_left);
		
		
		mCall.setOnClickListener(this);
		mImgSave.setOnClickListener(this);
		mCall.setOnClickListener(this);
		mMap.setOnClickListener(this);
		mDirection.setOnClickListener(this);
		mShare.setOnClickListener(this);
		mPrayerTime.setOnClickListener(this);
		mPhoto.setOnClickListener(this);
		mWebSite.setOnClickListener(this);
		mEmail.setOnClickListener(this);
		mMoreInfo.setOnClickListener(this);
		mbtn_close.setOnClickListener(this);
		mbtn_twitter.setOnClickListener(this);
		mbtn_facebook.setOnClickListener(this);
		mbtn_email.setOnClickListener(this);
		mbtn_sms.setOnClickListener(this);
		
		if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("list")){
			
			MasjidDataFeeds = (ArrayList<MasjidModel>) reciverIntent.getExtras().getSerializable("data");
			mPosition = reciverIntent.getExtras().getInt("position");
			
			if(mPosition == (MasjidDataFeeds.size()-1))
				mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			
			if(mPosition == 0)
				mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);			
			
			mMainMasjidLocationModel = MasjidDataFeeds.get(mPosition);	
	
			mNext.setOnClickListener(this);		
			mPrevious.setOnClickListener(this);	
			setData();
		
		}else if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("other")){
			
			mMainMasjidLocationModel = (MasjidModel) reciverIntent.getExtras().getSerializable("data");	
			mNext.setVisibility(View.GONE);
			mPrevious.setVisibility(View.GONE);
			setData();
			
		}else if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("item")){
			
			mBusinessId = reciverIntent.getExtras().getString("id");
			
			try {
				
				getParsedData();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mNext.setVisibility(View.GONE);
			mPrevious.setVisibility(View.GONE);
			
		}else{
			
			MasjidDataFeeds = (ArrayList<MasjidModel>) reciverIntent.getExtras().getSerializable("data");
			mPosition = reciverIntent.getExtras().getInt("position");
			mBusinessId = MasjidDataFeeds.get(mPosition).getBusinessId();
			
			if(mPosition == (MasjidDataFeeds.size()-1))
				mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			
			if(mPosition == 0)
				mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);			
			
			try {
				
				getParsedData();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	
			mNext.setOnClickListener(this);		
			mPrevious.setOnClickListener(this);				
			
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
				
				startActivity(new Intent(MasjidDetailDescription.this, GlobalSearchActivity.class));
				
			}
		});	
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(MasjidDetailDescription.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(MasjidDetailDescription.this,MyStuffActivity.class)
				.putExtra("from", "bottom"));  
			
			}
		});
		
		
	}
	
	int rate  = 0;
	
	String mShareName, mShareAddressOne, mShareAddressTwo, mShareCity, mShareState, mShareZip;

	
	private void setData(){
		
		mBusinessId = mMainMasjidLocationModel.getBusinessId();		
		mMainTitle.setText(mMainMasjidLocationModel.getBusinessTitle());
		mName.setText(mMainMasjidLocationModel.getBusinessTitle());
		
		
		
		mShareName = mMainMasjidLocationModel.getBusinessTitle();
		mShareAddressOne = (mMainMasjidLocationModel.getAddressLine1().length()>0 ? mMainMasjidLocationModel.getAddressLine1()+", ":"");
		mShareAddressTwo = (mMainMasjidLocationModel.getAddressLine2().length()>0 ? mMainMasjidLocationModel.getAddressLine2()+", ":"");
		mShareCity = (mMainMasjidLocationModel.getCityName().length()>0 ? mMainMasjidLocationModel.getCityName()+", ":"");
		mShareState =  (mMainMasjidLocationModel.getStateCode().length()>0 ? mMainMasjidLocationModel.getStateCode()+", ":"");
		mShareZip = (mMainMasjidLocationModel.getAddressZipcode().length()>0 ? mMainMasjidLocationModel.getAddressZipcode():"");
		
		mStrAddress = mShareAddressOne+""+mShareAddressTwo+"\n"+mShareCity+""+mShareState+""+mShareZip;			
		mtext_to_send = mShareName+"\n"+mStrAddress;
		
		mtext_to_share="<center></center><b>"+mShareName+"</b><center></center>" +
		"Address:<center></center>"
		+mShareAddressOne+""+mShareAddressTwo+"<center></center>"
		+mShareCity+""+mShareState+""+mShareZip+"<center></center>";
			
		mtext_to_send="<center>"+mShareName+"</center><center> " +
		"Address:</center><center>"
		+(mMainMasjidLocationModel.getBusinessAddress().length()>0 ? mMainMasjidLocationModel.getBusinessAddress()+", ":"")+"</center><center>"
		+(mMainMasjidLocationModel.getCityName().length()>0 ? "\n"+mMainMasjidLocationModel.getCityName()+", ":"\n")+"</center><center>"
		+(mMainMasjidLocationModel.getStateCode().length()>0 ? mMainMasjidLocationModel.getStateCode()+", ":"")+"</center><center>"
		+(mMainMasjidLocationModel.getAddressZipcode().length()>0 ? mMainMasjidLocationModel.getAddressZipcode():"")+"</center>";
		
		mtext_to_facebook_share = "Address:<center></center>"
			+mShareAddressOne+""+mShareAddressTwo+"<center></center>"
			+mShareCity+""+mShareState+""+mShareZip+"<center></center>";	
		
		mtext_to_twitter_share = "Check out what I found on @DaleeloApp!";
		
		mAddress.setText(mStrAddress);
		mPhone.setText(mMainMasjidLocationModel.getAddressPhone());
		
		if(mMainMasjidLocationModel.getBusinessDescription() != null){
			
			if(mMainMasjidLocationModel.getBusinessDescription().length()>0)
				mService.setText(mMainMasjidLocationModel.getBusinessDescription());
			else
				mService.setText(mMainMasjidLocationModel.getBusinessDescription());
			
		}else{
			
			mService.setText(mMainMasjidLocationModel.getBusinessDescription());
			
		}
				
		
		if(mMainMasjidLocationModel.getMosqueDataFeeds() != null){
			
			if(mMainMasjidLocationModel.getMosqueDataFeeds().size()>0){
			
				mImam.setText(mMainMasjidLocationModel.getMosqueDataFeeds().get(0).getMosqueImam());
				mDemonination.setText(mMainMasjidLocationModel.getMosqueDataFeeds().get(0).getMosqueDenomination());
				mDemographic.setText(mMainMasjidLocationModel.getMosqueDataFeeds().get(0).getMosqueDemographic());
				mLanguage.setText(mMainMasjidLocationModel.getMosqueDataFeeds().get(0).getMosqueLanguages());
			
			}else{
				
				mImam.setText("No data");
				mDemonination.setText("No data");
				mDemographic.setText("No data");
				mLanguage.setText("No data");				
				
			}
			
		}else{
			
			mImam.setText("No data");
			mDemonination.setText("No data");
			mDemographic.setText("No data");
			mLanguage.setText("No data");	
			
		}
	
		
	}
	
	
	
//	****************************	
	
//	OnclickListener actions
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		
			
		
		case R.id.masjid_IMGB_call:
			
			
			String phoneNumber = mMainMasjidLocationModel.getAddressPhone();
			Uri uri = Uri.fromParts("tel", phoneNumber, null);
			Intent callIntent = new Intent(Intent.ACTION_CALL, uri);
			startActivity(callIntent);
			
			break;			
			
		case R.id.masjid_BTN_save:
			
		try{
			
			mDbHelper = new DatabaseHelper(getApplicationContext());
			
			mDbHelper.openDataBase();
			long result = mDbHelper.insertMasjidItem(
					mMainMasjidLocationModel.getBusinessId(), 
					mMainMasjidLocationModel.getBusinessTitle(), 
					mStrAddress, 
					mMainMasjidLocationModel.getAddressPhone(), 
					mMainMasjidLocationModel.getAddressLat(),
					mMainMasjidLocationModel.getAddressLong(),
					mMainMasjidLocationModel.getCategoryId(),
					mMainMasjidLocationModel.getCityName());
			
			
			if(result == 0){
				
				alertDialogDB("Masjid item already saved to My Stuff");				
				
			}else if(result == -1){
				
				alertDialogDB("Unable to save the item to My Stuff");			
				
			}else{
				
				alertDialogDB("Masjid item saved successfully to My Stuff");	
			}
			
			mDbHelper.closeDataBase();
			mDbHelper = null;
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			alertDialogDB("Unable to save the item to My Stuff");	
			e.printStackTrace();
		}
		
			break;
			
			
		case R.id.masjid_IMGB_left:
			
			mPosition--;
			
			if(mPosition >= 0){				
			
				if(mPosition == 0)
					mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);
				else
					mPrevious.setBackgroundResource(R.drawable.left_arrow);	
				
				mNext.setBackgroundResource(R.drawable.right_arrow);
				
				if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("stuff")){
					
					mBusinessId = MasjidDataFeeds.get(mPosition).getBusinessId();
					getParsedData();
					
				}else{
										
					mMainMasjidLocationModel = MasjidDataFeeds.get(mPosition);
					setData();					
					
				}
				
			}else{
				
				mPosition++;
				
			}				
		
		break;
		
	case R.id.masjid_IMGB_right:
		
		mPosition++;
		
		if(mPosition <= (MasjidDataFeeds.size()-1)){
					
			if(mPosition == (MasjidDataFeeds.size()-1))
				mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			else
				mNext.setBackgroundResource(R.drawable.right_arrow);
			
			mPrevious.setBackgroundResource(R.drawable.left_arrow);	
			
			if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("stuff")){
				
				mBusinessId = MasjidDataFeeds.get(mPosition).getBusinessId();
				getParsedData();
				
			}else{
									
				mMainMasjidLocationModel = MasjidDataFeeds.get(mPosition);
				setData();					
				
			}
							
		}else{
			
			mPosition--;
		}
		
		break;
		
	
		
	case R.id.masjid_BTN_map:
		
		startActivity(new Intent(MasjidDetailDescription.this,MasjidMapActivity.class)
		.putExtra("data", MasjidDataFeeds.get(mPosition))
		.putExtra("from","desp"));
		
		break;
		
	case R.id.masjid_BTN_direction:
		
		 Intent navigateIntent = new Intent(android.content.Intent.ACTION_VIEW,  
				 Uri.parse("http://maps.google.com/maps?" +
				 		"saddr="+CurrentLocationData.LATITUDE+","+CurrentLocationData.LONGITUDE+
				 		"&daddr="+mMainMasjidLocationModel.getAddressLat()+"," +
				 				""+mMainMasjidLocationModel.getAddressLong())); 
		 startActivity(navigateIntent);	
		
		break;
		
	case R.id.masjid_BTN_share:
		
		mRel_share.setVisibility(View.VISIBLE);
		mRel_share.startAnimation(slidein_left_animation);
		
		break;
		
	case R.id.share_BTN_close:
		
		mRel_share.startAnimation(slideout_left_animation);
		mRel_share.setVisibility(View.GONE);
		
		break;
	case R.id.share_BTN_facebook:
		
			shareUrl = String.format(Urls.SHARE_BUSINESS_URL, mMainMasjidLocationModel.getBusinessId());
			Log.e("", "mtext_to_share "+mtext_to_share);
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			new FacebookShareHelperModified(MasjidDetailDescription.this, mtext_to_facebook_share , mShareName, shareUrl);

				
		break;
		
	case R.id.share_BTN_twitter:
		
			shareUrl = String.format(Urls.SHARE_BUSINESS_URL, mMainMasjidLocationModel.getBusinessId());
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			startActivity(new Intent(MasjidDetailDescription.this,TwitterPostActivity.class)
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
	
		 try
		   {
		     String number = "";  // The number on which you want to send SMS
		           startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)).
		                   putExtra("sms_body", mtext_to_send));
		   }catch (Exception e) {
		     Toast.makeText(MasjidDetailDescription.this, "SMS facility is not available in your device.", Toast.LENGTH_SHORT).show();
		   }
		
		break;
		
	case R.id.masjid_BTN_prayer:
		
		startActivity(new Intent(MasjidDetailDescription.this, PrayerScheduleActivity.class)
		.putExtra("title", mMainMasjidLocationModel.getBusinessTitle()));
		
		break;
		
	case R.id.masjid_BTN_photo:
		
		startActivity(new Intent(MasjidDetailDescription.this,PhotoGalleryActivity.class));		
		
		
		break;
		
	case R.id.masjid_BTN_website:
		
		if(mMainMasjidLocationModel.getAddressWeburl().length() > 9){
			
			String url = "http://"+mMainMasjidLocationModel.getAddressWeburl();
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);			
		
		}else{
			
			Toast.makeText(this, "Website address not found.", Toast.LENGTH_SHORT).show();

		}
		break;
		
	case R.id.masjid_BTN_email:
		
		if(mMainMasjidLocationModel.getAddressEmail().length()>7){
			
			String email = mMainMasjidLocationModel.getAddressEmail();
			Intent intent=new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
			intent.setType("plain/text");
			startActivity(intent);	
		
		  
		}else{
		
			Toast.makeText(this, "Email address not found.", Toast.LENGTH_SHORT).show();

		}
		
		break;
		
	case R.id.masjid_BTN_moreinfo:
		
		Toast.makeText(this, "Currently no data to display.", Toast.LENGTH_SHORT).show();		
		
		break;
			
			
			
		}
	}
	int mPosition;
	
//	Web Service Call
//	****************
	
	ProgressDialog progressDialog; 
	
	   private void getParsedData(){
			
			try {	
				
				MasjidDataFeedsTwo = null;
				MasjidDataFeedsTwo = new ArrayList<MasjidModel>();
				progressDialog = ProgressDialog.show(MasjidDetailDescription.this, "","Loading please wait...", true,false); 
				
				MasjidLocationParser mUserAuth = new MasjidLocationParser(
						String.format(Urls.BASE_URL+"GetMosquedetailsBusinessId?BusinessId=%s",mBusinessId) 
						, mainHandler
						, MasjidDataFeedsTwo);		
				
				mUserAuth.start();
													
							} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		public Handler mainHandler = new Handler() 
	    {
			public void handleMessage(android.os.Message msg) 
			{
				
				progressDialog.dismiss();
				
				String handleErrMsg = "";
				
				handleErrMsg = msg.getData().getString("httpError");
				
				Log.e("", "handleErrMsg "+handleErrMsg);
				
				if(handleErrMsg.equalsIgnoreCase("")){
					
					mMainMasjidLocationModel = null;
					mMainMasjidLocationModel = MasjidDataFeedsTwo.get(0);
					setData();		
					
				}else{
					
				

					new AlertDialogMsg(MasjidDetailDescription.this, handleErrMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							
							MasjidDetailDescription.this.finish();
							
						}
						
					}).create().show();
					
				}				
				
			}
		};
	
//	****************
	
	private void alertDialogDB(String msg){
		
		
		
		new AlertDialogMsg(MasjidDetailDescription.this, msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				
			}
			
		}).create().show();		
	}
}
