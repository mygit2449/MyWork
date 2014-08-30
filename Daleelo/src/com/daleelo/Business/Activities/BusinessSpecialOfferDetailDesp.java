package com.daleelo.Business.Activities;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Business.Model.BusinessListModel;
import com.daleelo.Business.Model.DealModel;
import com.daleelo.DataBase.DatabaseHelper;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.Utilities.Urls;
import com.daleelo.helper.FacebookShareHelper;
import com.daleelo.helper.FacebookShareHelperModified;
import com.daleelo.twitter.android.TwitterPostActivity;

public class BusinessSpecialOfferDetailDesp extends Activity implements OnClickListener{
	
	Intent reciverIntent;
	TextView mMainTitle, mName, mAddress, mPhone, mOfferName, mOfferDesp, mDesp,
	mHours, mPayment;
	
	StringBuffer mPayOptins;
	

	ImageButton mIBTNShare, mIBTNSave;	
	
	ImageView mRating;
	View mHourPaySeperator;
	
	DealModel mDealModel = null;
	BusinessListModel mMainBusinessListModel = null;
	
	private RelativeLayout mRel_share;
	private Animation slideout_left_animation;
	private Animation slidein_left_animation;
	private String mtext_to_send, mtext_to_share, mSubject, mtext_to_facebook_share
	, mtext_to_twitter_share, shareUrl;
	
	Button mbtn_close, mbtn_twitter, mbtn_facebook, mbtn_email, mbtn_sms;
	DatabaseHelper mDbHelper;
	int rate  = 0;
	
	String mReviewCount = "0";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.business_specila_offer_detail_desp);		
		initializeUI();
		
	}
	
	private void initializeUI(){
		
		reciverIntent = getIntent();
		
		
		slidein_left_animation = AnimationUtils.loadAnimation(BusinessSpecialOfferDetailDesp.this, R.anim.slide_in_left_animation);
		slideout_left_animation = AnimationUtils.loadAnimation(BusinessSpecialOfferDetailDesp.this, R.anim.slide_out_left_animation);
		mRel_share = (RelativeLayout)findViewById(R.id.give_desc_REL_share);
		
		
		mbtn_close = (Button)findViewById(R.id.share_BTN_close);
		mbtn_facebook = (Button)findViewById(R.id.share_BTN_facebook);
		mbtn_twitter = (Button)findViewById(R.id.share_BTN_twitter);
		mbtn_sms = (Button)findViewById(R.id.share_BTN_sms);
		mbtn_email = (Button)findViewById(R.id.share_BTN_email);
		
		mDealModel = (DealModel) reciverIntent.getExtras().getSerializable("dealdata");
		mMainBusinessListModel =  (BusinessListModel) reciverIntent.getExtras().getSerializable("data");
		mReviewCount =  reciverIntent.getExtras().getString("reviewcount");
		
		mMainTitle = (TextView)findViewById(R.id.business_special_TXT_main_title);
		mName  = (TextView)findViewById(R.id.business_special_TXT_desp_name);
		mAddress   = (TextView)findViewById(R.id.business_special_TXT_desp_address);
		mPhone   = (TextView)findViewById(R.id.business_special_TXT_desp_phone_number);
		mOfferName   = (TextView)findViewById(R.id.business_special_TXT_offer_name);
		mOfferDesp = (TextView)findViewById(R.id.business_special_TXT_offer_desp);
		
		mIBTNShare = (ImageButton)findViewById(R.id.business_special_IMGB_share);
		mIBTNSave = (ImageButton)findViewById(R.id.business_special_IMGB_save);		

		mbtn_close.setOnClickListener(this);
		mbtn_twitter.setOnClickListener(this);
		mbtn_facebook.setOnClickListener(this);
		mbtn_email.setOnClickListener(this);
		mbtn_sms.setOnClickListener(this);
		
		mIBTNSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				try {
					
					mDbHelper = new DatabaseHelper(getApplicationContext());
				
					Log.e("", "rate "+rate+" mReviewCount "+mReviewCount);
					mDbHelper.openDataBase();
					long result = mDbHelper.insertBusinessItem(
							mMainBusinessListModel.getBusinessId(), 
							mMainBusinessListModel.getBusinessTitle(), 
							mStrAddress, 
							""+rate, 
							mReviewCount,  
							mMainBusinessListModel.getAddressLat(),
							mMainBusinessListModel.getAddressLong(),
							mMainBusinessListModel.getCityName());
					
					
					if(result == 0){
						
						alertDialogDB("Business item already saved to My Stuff");				
						
					}else if(result == -1){
						
						alertDialogDB("Unable to save the item to My Stuff");			
						
					}else{
						
						alertDialogDB("Business item saved successfully to My Stuff");	
					}
					
					mDbHelper.closeDataBase();
					mDbHelper = null;
			
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		
		
		mIBTNShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				mRel_share.setVisibility(View.VISIBLE);
				mRel_share.startAnimation(slidein_left_animation);
				
				
			
				
				
			}
		});
		
		
		mbtn_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				mRel_share.startAnimation(slideout_left_animation);
				mRel_share.setVisibility(View.GONE);
				
				
			}
		});
		
		setData();
		
	}
	

	String mShareName, mShareAddressOne, mShareAddressTwo, mShareCity, mShareState, mShareZip;
	private String mStrAddress;
	
	
	private void setData(){
		
		mMainTitle.setText(mMainBusinessListModel.getBusinessTitle());
		mName.setText(mMainBusinessListModel.getBusinessTitle());
		
		mShareName = mMainBusinessListModel.getBusinessTitle();
		mShareAddressOne = (mMainBusinessListModel.getAddressLine1().length()>0 ? mMainBusinessListModel.getAddressLine1()+", ":"");
		mShareAddressTwo = (mMainBusinessListModel.getAddressLine2().length()>0 ? mMainBusinessListModel.getAddressLine2()+", ":"");
		mShareCity = (mMainBusinessListModel.getCityName().length()>0 ? mMainBusinessListModel.getCityName()+", ":"");
		mShareState =  (mMainBusinessListModel.getStateCode().length()>0 ? mMainBusinessListModel.getStateCode()+", ":"");
		mShareZip = (mMainBusinessListModel.getAddressZipcode().length()>0 ? mMainBusinessListModel.getAddressZipcode():"");
		
		mStrAddress = mShareAddressOne+""+mShareAddressTwo+"\n"+mShareCity+""+mShareState+""+mShareZip;			
		mtext_to_send = mShareName+"\n"+mStrAddress;
		
		mtext_to_share="<center></center><b>"+mShareName+"</b><center></center>" +
		"Address:<center></center>"
		+mShareAddressOne+""+mShareAddressTwo+"<center></center>"
		+mShareCity+""+mShareState+""+mShareZip+"<center></center>";		
		
		mtext_to_facebook_share = "Address:<center></center>"
			+mShareAddressOne+""+mShareAddressTwo+"<center></center>"
			+mShareCity+""+mShareState+""+mShareZip+"<center></center>";	
		
		mtext_to_twitter_share = "Check out what I found on @DaleeloApp!";
		
		mAddress.setText(mStrAddress);
		mPhone.setText(mMainBusinessListModel.getAddressPhone());
		
		mOfferName.setText(mDealModel.getDealTittle());
		mOfferDesp.setText(mDealModel.getDealInfo());
		
		
		
		try {
			
			rate = Integer.parseInt(mMainBusinessListModel.getBusinessRating().toString());

		} catch (Exception e) {
			// TODO: handle exception
		}
			
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		
		
		case R.id.share_BTN_facebook:
			
			shareUrl = String.format(Urls.SHARE_BUSINESS_URL, mMainBusinessListModel.getBusinessId());
			Log.e("", "mtext_to_share "+mtext_to_share);
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			new FacebookShareHelperModified(BusinessSpecialOfferDetailDesp.this, mtext_to_facebook_share , mShareName, shareUrl);
									
			break;
			
		case R.id.share_BTN_twitter:
			
			
			shareUrl = String.format(Urls.SHARE_BUSINESS_URL, mMainBusinessListModel.getBusinessId());
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			startActivity(new Intent(BusinessSpecialOfferDetailDesp.this,TwitterPostActivity.class)
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
			     Toast.makeText(BusinessSpecialOfferDetailDesp.this, "SMS facility is not available in your device.", Toast.LENGTH_SHORT).show();
			   }
			
			break;
			
		}
		
	}
	
	
	
//	****************************	
	

	private void alertDialogDB(String msg){
		
		
		
		new AlertDialogMsg(BusinessSpecialOfferDetailDesp.this, msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				
			}
			
		}).create().show();
		
		
	}
	
	

	

}
