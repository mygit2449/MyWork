package com.daleelo.Hasanat.Activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.Business.Activities.BusinessSpecialOfferDetailDesp;
import com.daleelo.DashBoardClassified.Activities.ClassifiedItemDetailDesp;
import com.daleelo.DataBase.DatabaseHelper;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Hasanat.Model.BusinessDetailModel;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.helper.FacebookShareHelper;
import com.daleelo.helper.FacebookShareHelperModified;
import com.daleelo.helper.PhotoGalleryActivity;
import com.daleelo.helper.PrayerScheduleActivity;
import com.daleelo.twitter.android.TwitterPostActivity;

public class GiveDetailDescActivity extends Activity implements OnClickListener{
	
	Intent reciverIntent;
	TextView mMainTitle, mName, mAddress, mPhone, mTitle, mSubTitle, mTimings, mCondtion;
	BusinessDetailModel mBusinessDetailModel = null;
	private ArrayList<BusinessDetailModel> mDetailList;
	private TextView mImam, mDemographics, mDenomination, mLanguage, mBasicInfoDesp;
	private Button mbtn_DonateNow, mbtn_left , mbtn_right, mbtn_share, mbtn_close, mbtn_twitter, mbtn_facebook, mbtn_email, mbtn_sms;
	private Button mBtn_prayerSchedule, mBtn_photo, mBtn_website, mBtn_moreinfo, mBtn_email, mbtn_save, mbtn_map, mbtn_direction, mBtnViewMore;
	private ImageButton mBtn_call;
	private int position ;
	private RelativeLayout mRel_share;
	private Animation slideout_left_animation;
	private Animation slidein_left_animation;
	private String mtext_to_share, mtext_to_send, mtext_to_facebook_share
	, mtext_to_twitter_share, shareUrl;
	private LinearLayout mLinBasicInfo;
	
	private DatabaseHelper mDbHelper;
	private ArrayList<String> imageArray;	
	
	private boolean viewMore = false;
	
	private String mBasicInfoSubString = "";

	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.give_detail_desc);		
		initializeUI();
		
		
	}
	
	private void initializeUI(){
		
		reciverIntent = getIntent();
		
		imageArray = new ArrayList<String>();
		mDetailList =  (ArrayList<BusinessDetailModel>) reciverIntent.getExtras().getSerializable("data");
		position = (Integer) reciverIntent.getExtras().get("pos");
		mBusinessDetailModel = mDetailList.get(position);
		
		mMainTitle = (TextView)findViewById(R.id.give_desc_TXT_main_title);
		mName = (TextView)findViewById(R.id.give_desc_TXT_name);
		mAddress  = (TextView)findViewById(R.id.give_desc_TXT_address);
		mPhone  = (TextView)findViewById(R.id.give_desc_TXT_phone_number);
//		mTitle  = (TextView)findViewById(R.id.give_desc_TXT_title);		
//		mImam = (TextView)findViewById(R.id.give_desc_TXT_imam);
//		mDemographics = (TextView)findViewById(R.id.give_desc_TXT_demograghics);
//		mDenomination = (TextView)findViewById(R.id.give_desc_TXT_denomination);
//		mLanguage = (TextView)findViewById(R.id.give_desc_TXT_language);
//		mServices = (TextView)findViewById(R.id.give_desc_TXT_services);

		mLinBasicInfo = (LinearLayout)findViewById(R.id.give_LIN_desc_basic);
		mBasicInfoDesp = (TextView)findViewById(R.id.give_desc_TXT_basic_desp);
		mBtnViewMore = (Button)findViewById(R.id.give_desc_BTN_basic_view_more);

		mRel_share = (RelativeLayout)findViewById(R.id.give_desc_REL_share);
		
		slidein_left_animation = AnimationUtils.loadAnimation(GiveDetailDescActivity.this, R.anim.slide_in_left_animation);
		slideout_left_animation = AnimationUtils.loadAnimation(GiveDetailDescActivity.this, R.anim.slide_out_left_animation);
		
		mBtn_photo = (Button)findViewById(R.id.give_desc_BTN_photo);
		mBtn_website = (Button)findViewById(R.id.give_desc_BTN_website);
		mBtn_email = (Button)findViewById(R.id.give_desc_BTN_email);
		mBtn_moreinfo = (Button)findViewById(R.id.give_desc_BTN_moreinfo);
		mBtn_prayerSchedule = (Button)findViewById(R.id.give_desc_BTN_prayer);
		
		mBtn_call = (ImageButton)findViewById(R.id.give_desc_IMGB_call);
		mbtn_save = (Button)findViewById(R.id.give_desc_BTN_save);
		mbtn_map = (Button)findViewById(R.id.give_desc_BTN_map);
		mbtn_direction = (Button)findViewById(R.id.give_desc_BTN_direction);
		
		mbtn_DonateNow = (Button)findViewById(R.id.give_desc_BTN_donate);
		
		mbtn_left = (Button)findViewById(R.id.give_desc_BTN_left);
		mbtn_right = (Button)findViewById(R.id.give_desc_BTN_right);
		
		mbtn_share = (Button)findViewById(R.id.give_desc_BTN_share);
		mbtn_facebook = (Button)findViewById(R.id.share_BTN_facebook);
		mbtn_twitter = (Button)findViewById(R.id.share_BTN_twitter);
		mbtn_sms = (Button)findViewById(R.id.share_BTN_sms);
		mbtn_email = (Button)findViewById(R.id.share_BTN_email);
		mbtn_close = (Button)findViewById(R.id.share_BTN_close);
		
		if(mBusinessDetailModel.getAddressPhone().length() > 5)
			mBtn_call.setOnClickListener(this);
		
		mbtn_sms.setOnClickListener(this);
		mbtn_facebook.setOnClickListener(this);
		mbtn_twitter.setOnClickListener(this);
		mbtn_email.setOnClickListener(this);
		mbtn_close.setOnClickListener(this);
		mbtn_left.setOnClickListener(this);
		mbtn_right.setOnClickListener(this);
		mbtn_share.setOnClickListener(this);
		mBtn_moreinfo.setOnClickListener(this);
		mBtn_email.setOnClickListener(this);
		mBtn_photo.setOnClickListener(this);
		mBtn_website.setOnClickListener(this);
		mbtn_save.setOnClickListener(this);
		mbtn_map.setOnClickListener(this);
		mbtn_direction.setOnClickListener(this);
		mBtn_prayerSchedule.setOnClickListener(this);
		
		if(mDetailList.size() == 1){
			
			mbtn_left.setClickable(false);
			mbtn_left.setBackgroundResource(R.drawable.disabled_left_arrow);
			mbtn_right.setClickable(false);
			mbtn_right.setBackgroundResource(R.drawable.disabled_right_arrow);
			
		}else if(mDetailList.size() > 1){
			
			if(position != 0){
				if(position == (mDetailList.size()-1) ){
					mbtn_left.setClickable(true);
					mbtn_left.setBackgroundResource(R.drawable.left_arrow);
					mbtn_right.setClickable(false);
					mbtn_right.setBackgroundResource(R.drawable.disabled_right_arrow);
				}else{
					mbtn_left.setClickable(true);
					mbtn_left.setBackgroundResource(R.drawable.left_arrow);
					mbtn_right.setClickable(true);
					mbtn_right.setBackgroundResource(R.drawable.right_arrow);
				}
			}else{
				mbtn_left.setClickable(false);
				mbtn_left.setBackgroundResource(R.drawable.disabled_left_arrow);
				mbtn_right.setClickable(true);
				mbtn_right.setBackgroundResource(R.drawable.right_arrow);
			}
		}
		
		
		mbtn_DonateNow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(GiveDetailDescActivity.this, HasanatDonateActivity.class));
				
			}
		});
		
		
		mBtnViewMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				if(viewMore){
					
					mBasicInfoDesp.setText(mBusinessDetailModel.getBusinessDescription());
					mBtnViewMore.setBackgroundResource(R.drawable.btn_view_less_data);
					
					viewMore = false;
					
				}else{
					
					mBasicInfoDesp.setText(mBasicInfoSubString);
					mBtnViewMore.setBackgroundResource(R.drawable.btn_view_more_data);
					
					viewMore = true;
				}
				
			}
		});

		
		setBottomBar();
		setData();		
		
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
				
				startActivity(new Intent(GiveDetailDescActivity.this, GlobalSearchActivity.class));
				
			}
		});		
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(GiveDetailDescActivity.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(GiveDetailDescActivity.this,MyStuffActivity.class)
				.putExtra("from", "bottom")); 
			
			}
		});
		
		
	}	
	
	String mShareName, mShareAddressOne, mShareAddressTwo, mShareCity, mShareState, mShareZip;
	
	private void setData(){
		
		mBusinessDetailModel = mDetailList.get(position);
		Log.e("position",position+" ");
		
		mMainTitle.setText(mBusinessDetailModel.getBusinessTitle());
		mName.setText(mBusinessDetailModel.getBusinessTitle());
		//mAddress.setText(mBusinessDetailModel.getBusinessAddress());
		
		mLinBasicInfo.setVisibility(View.GONE);

		if(mBusinessDetailModel.getBusinessDescription()!=null){
			
//			mBusinessDetailModel.setBusinessDescription("On Mondays, Business Line's main section has 16 pages. Page headings include Market Mood, economy, Information technology, AgriBusiness, Transport, Commodities Investment The New Manager-Keep your date with leaders from the world of business, Mentor-A comprehensive resource on taxation, financial systems and books, Money & Banking, Editorial & Opinion, states and Variety.");
			
			if(mBusinessDetailModel.getBusinessDescription().length()>0){

				mLinBasicInfo.setVisibility(View.VISIBLE);
				if(mBusinessDetailModel.getBusinessDescription().length()<=100){
					
					mBasicInfoDesp.setText(mBusinessDetailModel.getBusinessDescription());
					
				}else{					
					
					mBasicInfoSubString = mBusinessDetailModel.getBusinessDescription().substring(0, 99);
					mBasicInfoDesp.setText(mBasicInfoSubString);
					mBtnViewMore.setVisibility(View.VISIBLE);
					viewMore = true;
				}
					
			
			}
		}
		
		
			
		
		mPhone.setText(mBusinessDetailModel.getAddressPhone());
		
//		if(mBusinessDetailModel.getMosqueList().size() > 0){
//			mImam.setText(mBusinessDetailModel.getMosqueList().get(0).getMosqueImam());
//			mDemographics.setText(mBusinessDetailModel.getMosqueList().get(0).getMosqueDemographic());
//			mDenomination.setText(mBusinessDetailModel.getMosqueList().get(0).getMosqueDenomination());
//			mLanguage.setText(mBusinessDetailModel.getMosqueList().get(0).getMosqueLanguages());
//		}else{
//			mImam.setText("");
//			mDemographics.setText("");
//			mDenomination.setText("");
//			mLanguage.setText("");
//		}
//		
	

		mShareName = mBusinessDetailModel.getBusinessTitle();
		mShareAddressOne = (mBusinessDetailModel.getAddressLine1().length()>0 ? mBusinessDetailModel.getAddressLine1()+", " :"");
		mShareAddressTwo = (mBusinessDetailModel.getAddressLine2().length()>0 ? mBusinessDetailModel.getAddressLine2()+", " :"");
		mShareCity =  (mBusinessDetailModel.getCityName().length()>0 ? mBusinessDetailModel.getCityName()+", ":"");
		mShareState =  (mBusinessDetailModel.getStateCode().length()>0 ? mBusinessDetailModel.getStateCode()+", ":"");
		mShareZip = (mBusinessDetailModel.getAddressZipcode().length()>0 ? mBusinessDetailModel.getAddressZipcode():"");
		
		mtext_to_send = mShareName+"\n"+mShareAddressOne+mShareAddressTwo+"\n"+mShareCity+""+mShareState+""+mShareZip;			
		
		mtext_to_share="<center></center><b>"+mShareName+"</b><center></center>" +
		"Address:<center></center>"
		+mShareAddressOne+""+mShareAddressTwo+"<center></center>"
		+mShareCity+""+mShareState+""+mShareZip+"<center></center>";
		
		mtext_to_facebook_share = "Address:<center></center>"
			+mShareAddressOne+""+mShareAddressTwo+"<center></center>"
			+mShareCity+""+mShareState+""+mShareZip+"<center></center>";	
		
		mtext_to_twitter_share = "Check out what I found on @DaleeloApp!";
		
		mAddress.setText(mShareAddressOne+""+mShareAddressTwo+"\n"+mShareCity+""+mShareState+""+mShareZip);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.give_desc_BTN_left:
			if(position != 0){
				position-- ;
				mbtn_right.setBackgroundResource(R.drawable.right_arrow);
				mbtn_right.setClickable(true);
				setData();
				if(position == 0){
					mbtn_left.setBackgroundResource(R.drawable.disabled_left_arrow);
					mbtn_left.setClickable(false);
				}
			}
			
			break;
			
		case R.id.give_desc_BTN_right:
			if(position < (mDetailList.size()-1) ){
				position++ ;
				mbtn_left.setBackgroundResource(R.drawable.left_arrow);
				mbtn_left.setClickable(true);
				setData();
				if(position == (mDetailList.size()-1) ){
					mbtn_right.setBackgroundResource(R.drawable.disabled_right_arrow);
					mbtn_right.setClickable(false);
				}
			}
			
			break;
		
		case R.id.give_desc_IMGB_call:
			new AlertDialogMsg(GiveDetailDescActivity.this, "Are you sure you want to call" 
					+ ((mBusinessDetailModel.getAddressPhone() != null)?mBusinessDetailModel.getAddressPhone():""))
			.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					String phoneNumber = mBusinessDetailModel.getAddressPhone();
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
			
		case R.id.give_desc_BTN_save:
			try {
				
				mbtn_save.setClickable(false);
				
				mDbHelper = new DatabaseHelper(getApplicationContext());
			
				mDbHelper.openDataBase();
				long result = mDbHelper.insertHasanatItem(mBusinessDetailModel.getBusinessId(), 
						mBusinessDetailModel.getBusinessTitle(), 
						mShareAddressOne+mShareAddressTwo, 
						mBusinessDetailModel.getAddressPhone(),
						mBusinessDetailModel.getAddressLat(),
						mBusinessDetailModel.getAddressLong(), 
						mBusinessDetailModel.getCategoryId(), 
						mBusinessDetailModel.getCityName());			
				
				if(result == 0){
					
					alertDialogDB("Hasanat item already saved to My Stuff");				
					
				}else if(result == -1){
					
					alertDialogDB("Unable to save the item to My Stuff");			
					
				}else{
					
					alertDialogDB("Hasanat item saved successfully to My Stuff");	
				}
				
				mDbHelper.closeDataBase();
				mDbHelper = null;
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				mbtn_save.setClickable(true);
				e.printStackTrace();
			}
			break;
			
		case R.id.give_desc_BTN_share:
			mRel_share.setVisibility(View.VISIBLE);
			mRel_share.startAnimation(slidein_left_animation);
			break;
			
		case R.id.give_desc_BTN_map:
			
			startActivity(new Intent(GiveDetailDescActivity.this, HasanatMapActivity.class)
			.putExtra("data", mBusinessDetailModel)
			.putExtra("from","desp"));
			
			break;
			
		case R.id.give_desc_BTN_direction:
			 Intent navigateIntent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse("http://maps.google.com/maps?saddr="+CurrentLocationData.LATITUDE+","+CurrentLocationData.LONGITUDE
	                   +"&daddr="+mBusinessDetailModel.getAddressLat()+","+mBusinessDetailModel.getAddressLong())); 
	        startActivity(navigateIntent);

			break;
			
		
		case R.id.give_desc_BTN_photo:

			startActivity(new Intent(GiveDetailDescActivity.this,PhotoGalleryActivity.class)
			.putExtra("images", imageArray)
			.putExtra("position", 0)
			.putExtra("from", "hasanat"));
			
			break;
			
		case R.id.give_desc_BTN_prayer:
			startActivity(new Intent(GiveDetailDescActivity.this, PrayerScheduleActivity.class)
				.putExtra("title", mBusinessDetailModel.getBusinessTitle()));
			break;
			
		case R.id.give_desc_BTN_website:
			
			if(mBusinessDetailModel.getAddressWeburl().length() > 9){
				
				String url = "http://"+mBusinessDetailModel.getAddressWeburl();
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);			
			
			}else{
				
				Toast.makeText(this, "Website address not found.", Toast.LENGTH_SHORT).show();

			}
			
			break;
		
		case R.id.give_desc_BTN_email:
			  
			if(mBusinessDetailModel.getAddressEmail().length()>7){
				
				String email = mBusinessDetailModel.getAddressEmail();
				Intent emailIntent=new Intent(Intent.ACTION_SEND);
				emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
				emailIntent.setType("plain/text");
				startActivity(emailIntent );	
			
			  
			}else{
			
				Toast.makeText(this, "Email address not found.", Toast.LENGTH_SHORT).show();
	
			}
			
			break;
			
		case R.id.give_desc_BTN_moreinfo:

			Toast.makeText(this, "Currently no data to display.", Toast.LENGTH_SHORT).show();		
			
			break;
			
		case R.id.share_BTN_close:
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			break;
			
			
		case R.id.share_BTN_facebook:
			
			shareUrl = String.format(Urls.SHARE_BUSINESS_URL, mBusinessDetailModel.getBusinessId());
			Log.e("", "mtext_to_share "+mtext_to_share);
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			new FacebookShareHelperModified(GiveDetailDescActivity.this, mtext_to_facebook_share , mShareName, shareUrl);
						
			break;
			
		case R.id.share_BTN_twitter:
			
			shareUrl = String.format(Urls.SHARE_BUSINESS_URL, mBusinessDetailModel.getBusinessId());
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			startActivity(new Intent(GiveDetailDescActivity.this,TwitterPostActivity.class)
			.putExtra("message", mtext_to_twitter_share)
			.putExtra("shareurl", shareUrl));
			
			break;
			
		case R.id.share_BTN_email:
			
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			  Intent emailIntent=new Intent(Intent.ACTION_SEND);
			  emailIntent.putExtra(Intent.EXTRA_SUBJECT, mShareName );
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
			     Toast.makeText(GiveDetailDescActivity.this, "SMS facility is not available in your device.", Toast.LENGTH_SHORT).show();
			   }
			
			break;
		default:
			break;
		}
		
	}
	
	
	private void alertDialogDB(String msg){
		
		
		mbtn_save.setClickable(true);
		
		
		new AlertDialogMsg(GiveDetailDescActivity.this, msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				
			}
			
		}).create().show();
		
		
	}
	
	

}
