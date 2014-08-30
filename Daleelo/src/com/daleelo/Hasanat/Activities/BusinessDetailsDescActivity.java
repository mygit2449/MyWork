package com.daleelo.Hasanat.Activities;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.Business.Activities.BusinessSpecialOfferDetailDesp;
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

public class BusinessDetailsDescActivity extends Activity implements OnClickListener {
	
	Intent reciverIntent;
	private BusinessDetailModel mBusinessDetailModel = null;
	
	private TextView mMainTitle, mName, mAddress, mPhone, mTitle, mSubTitle, mTimings, mCondtion;
	private TextView mImam, mDemographics, mDenomination, mLanguage, mServices;
	private Button mDonateNow, mbtn_share, mbtn_close, mbtn_twitter, mbtn_facebook, mbtn_email, mbtn_sms;
	private Button mbtn_map,mbtn_directions,mbtn_save, mBtn_photo, mBtn_website, mBtn_moreinfo, mBtn_email, mBtn_prayer;
	private RelativeLayout mRel_share;
	private Animation slideout_left_animation;
	private Animation slidein_left_animation;
	private String mtext_to_send, mtext_to_share, mtext_to_facebook_share, mtext_to_twitter_share, shareUrl;
	private ImageButton mbtn_call;
	
	private DatabaseHelper mDbHelper;
	
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
		
		setContentView(R.layout.hasanat_detail_desc);		
		initializeUI();
		
	}
	
	private void initializeUI(){
		reciverIntent = getIntent();
		
		mBusinessDetailModel =  (BusinessDetailModel) reciverIntent.getExtras().getSerializable("data");
		
		mMainTitle = (TextView)findViewById(R.id.hasanat_desc_TXT_main_title);
		mName = (TextView)findViewById(R.id.hasanat_desc_TXT_name);
		mAddress  = (TextView)findViewById(R.id.hasanat_desc_TXT_address);
		mPhone  = (TextView)findViewById(R.id.hasanat_desc_TXT_phone_number);
		mTitle  = (TextView)findViewById(R.id.hasanat_desc_TXT_title);
		
		mImam = (TextView)findViewById(R.id.hasanat_desc_TXT_imam);
		mDemographics = (TextView)findViewById(R.id.hasanat_desc_TXT_demograghics);
		mDenomination = (TextView)findViewById(R.id.hasanat_desc_TXT_denomination);
		mLanguage = (TextView)findViewById(R.id.hasanat_desc_TXT_language);
		mServices = (TextView)findViewById(R.id.hasanat_desc_TXT_services);
		
		mRel_share = (RelativeLayout)findViewById(R.id.hasanat_desc_REL_share);
		
		mbtn_call = (ImageButton)findViewById(R.id.hasanat_desc_IMGB_call);
		
		mBtn_photo = (Button)findViewById(R.id.hasanat_desc_BTN_photo);
		mBtn_website = (Button)findViewById(R.id.hasanat_desc_BTN_website);
		mBtn_email = (Button)findViewById(R.id.hasanat_desc_BTN_email);
		mBtn_moreinfo = (Button)findViewById(R.id.hasanat_desc_BTN_moreinfo);
		mBtn_prayer = (Button)findViewById(R.id.hasanat_desc_BTN_prayer);
		
		mDonateNow = (Button)findViewById(R.id.hasanat_desc_BTN_donate);
		
		mbtn_share = (Button)findViewById(R.id.hasanat_desc_BTN_share);
		mbtn_facebook = (Button)findViewById(R.id.share_BTN_facebook);
		mbtn_twitter = (Button)findViewById(R.id.share_BTN_twitter);
		mbtn_sms = (Button)findViewById(R.id.share_BTN_sms);
		mbtn_email = (Button)findViewById(R.id.share_BTN_email);
		mbtn_close = (Button)findViewById(R.id.share_BTN_close);
		
		mbtn_map = (Button)findViewById(R.id.hasanat_desc_BTN_map);
		mbtn_directions = (Button)findViewById(R.id.hasanat_desc_BTN_direction);
		mbtn_save = (Button)findViewById(R.id.hasanat_desc_BTN_save);
		
		if(mBusinessDetailModel.getAddressPhone().length() > 5)
			mbtn_call.setOnClickListener(this);
		
		mbtn_share.setOnClickListener(this);
		mbtn_sms.setOnClickListener(this);
		mbtn_facebook.setOnClickListener(this);
		mbtn_twitter.setOnClickListener(this);
		mbtn_email.setOnClickListener(this);
		mbtn_close.setOnClickListener(this);
		mbtn_map.setOnClickListener(this);
		mBtn_moreinfo.setOnClickListener(this);
		mBtn_email.setOnClickListener(this);
		mBtn_photo.setOnClickListener(this);
		mBtn_website.setOnClickListener(this);
		mbtn_directions.setOnClickListener(this);
		mbtn_save.setOnClickListener(this);
		mBtn_prayer.setOnClickListener(this);
		
		slidein_left_animation = AnimationUtils.loadAnimation(BusinessDetailsDescActivity.this, R.anim.slide_in_left_animation);
		slideout_left_animation = AnimationUtils.loadAnimation(BusinessDetailsDescActivity.this, R.anim.slide_out_left_animation);
		
		mDonateNow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(BusinessDetailsDescActivity.this, HasanatDonateActivity.class));
				
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
				
				startActivity(new Intent(BusinessDetailsDescActivity.this, GlobalSearchActivity.class));
				
			}
		});
		
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(BusinessDetailsDescActivity.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(BusinessDetailsDescActivity.this,MyStuffActivity.class)
				.putExtra("from", "bottom"));  
			
			}
		});
		
		
	}
	
	String mShareName, mShareAddressOne, mShareAddressTwo, mShareCity, mShareState, mShareZip;

	
	private void setData(){
		
		mMainTitle.setText(mBusinessDetailModel.getBusinessTitle());
		mName.setText(mBusinessDetailModel.getBusinessTitle());
		mPhone.setText(mBusinessDetailModel.getAddressPhone());
		
		if(mBusinessDetailModel.getMosqueList().size() > 0){
			mImam.setText(mBusinessDetailModel.getMosqueList().get(0).getMosqueImam());
			mDemographics.setText(mBusinessDetailModel.getMosqueList().get(0).getMosqueDemographic());
			mDenomination.setText(mBusinessDetailModel.getMosqueList().get(0).getMosqueDenomination());
			mLanguage.setText(mBusinessDetailModel.getMosqueList().get(0).getMosqueLanguages());
		}else{
			mImam.setText("");
			mDemographics.setText("");
			mDenomination.setText("");
			mLanguage.setText("");
		}
		
		mServices.setText(mBusinessDetailModel.getBusinessDescription());
		
		
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
		
		case R.id.hasanat_desc_IMGB_call:
			new AlertDialogMsg(BusinessDetailsDescActivity.this, "Are you sure you want to call" 
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
		
		case R.id.hasanat_desc_BTN_share:
			mRel_share.setVisibility(View.VISIBLE);
			mRel_share.startAnimation(slidein_left_animation);
			break;
		
		case R.id.hasanat_desc_BTN_save:
			try {
				
				mbtn_save.setClickable(false);
				
				mDbHelper = new DatabaseHelper(getApplicationContext());
			
				mDbHelper.openDataBase();
				long result = mDbHelper.insertHasanatItem(mBusinessDetailModel.getBusinessId(), 
						mBusinessDetailModel.getBusinessTitle(), 
						mBusinessDetailModel.getBusinessAddress(), 
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
		
		case R.id.hasanat_desc_BTN_photo:
			
			startActivity(new Intent(BusinessDetailsDescActivity.this, PhotoGalleryActivity.class));
			
			break;
			
		case R.id.hasanat_desc_BTN_prayer:
			startActivity(new Intent(BusinessDetailsDescActivity.this, PrayerScheduleActivity.class)
				.putExtra("title", mBusinessDetailModel.getBusinessTitle()));
			break;
			
		case R.id.hasanat_desc_BTN_map:
			
			startActivity(new Intent(BusinessDetailsDescActivity.this, HasanatMapActivity.class)
			.putExtra("data", mBusinessDetailModel)
			.putExtra("from","desp"));
			
			break;
		
		case R.id.hasanat_desc_BTN_direction:
			 Intent navigateIntent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse("http://maps.google.com/maps?saddr="+CurrentLocationData.LATITUDE+","+CurrentLocationData.LONGITUDE
	                   +"&daddr="+mBusinessDetailModel.getAddressLat()+","+mBusinessDetailModel.getAddressLong())); 
	        startActivity(navigateIntent);

			break;
			
		case R.id.hasanat_desc_BTN_website:
			
			if(mBusinessDetailModel.getAddressWeburl().length() > 9){
				
				String url = "http://"+mBusinessDetailModel.getAddressWeburl();
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);			
			
			}else{
				
				Toast.makeText(this, "Website address not found.", Toast.LENGTH_SHORT).show();

			}
				
			break;
		
		case R.id.hasanat_desc_BTN_email:
			
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
			
		case R.id.hasanat_desc_BTN_moreinfo:
			
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
			new FacebookShareHelperModified(BusinessDetailsDescActivity.this, mtext_to_facebook_share , mShareName, shareUrl);
					
			break;
			
		case R.id.share_BTN_twitter:
			
			shareUrl = String.format(Urls.SHARE_BUSINESS_URL, mBusinessDetailModel.getBusinessId());
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			startActivity(new Intent(BusinessDetailsDescActivity.this,TwitterPostActivity.class)
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
			     Toast.makeText(BusinessDetailsDescActivity.this, "SMS facility is not available in your device.", Toast.LENGTH_SHORT).show();
			   }
			
			break;
			  
		default:
			break;
		}
		
	}
	
	
	private void alertDialogDB(String msg){
		
		
		mbtn_save.setClickable(true);
		
		
		new AlertDialogMsg(BusinessDetailsDescActivity.this, msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				
			}
			
		}).create().show();
		
		
	}
	
	
//	http://i01.i.aliimg.com/photo/v0/284205569/Love_greeting_card.jpg
//		http://www.prestat.co.uk/product_image/large/2279-greetings_cards_x-ray_roses_nxr810.jpg
//		http://www.urlbaron.com/ubcards/images/p6495.gif
//		http://www.hollyhobbieworld.com/HollyHobbieGreetingCards2005_1.jpg
//		http://image.made-in-china.com/2f0j00MBTEngIFEWcO/Re-Recordable-Greeting-Card.jpg
//	
	

}
