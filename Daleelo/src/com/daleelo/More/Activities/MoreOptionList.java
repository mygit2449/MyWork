package com.daleelo.More.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;
import com.daleelo.helper.FacebookShareHelperModified;
import com.daleelo.twitter.android.TwitterPostActivity;

public class MoreOptionList extends Activity implements OnClickListener{

	RelativeLayout mPersonalProfile, mCopyright, mPrivatePolicy, mAboutUs, mInvite, mQuestionsAndSuggestions, mFaqs;
	LinearLayout mSetRadius_Rel;
	
	Spinner mSpinnerSetRadius;
	
	final String mSetRadius[] = {"5 Miles", "10 Miles", "25 Miles ", "50 Miles ", "100 Miles"};
	
	private AlertDialog.Builder builder;
	private AlertDialog alert;
	private String mSelected_Radius_Str="";
	private int mSelected = -1;
	private TextView mTxtMiles;
	
	private Button mbtn_close, mbtn_twitter, mbtn_facebook, mbtn_email, mbtn_sms;
	private RelativeLayout mRel_share;
	private Animation slideout_left_animation;
	private Animation slidein_left_animation;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_list_options);
		initializeUI();
		builder = new AlertDialog.Builder(this);
	    builder.setTitle("Set Radius");
	    mTxtMiles.setText(Utils.RANGE+" Miles");
	}
	
	
	private void initializeUI() {
		
		
		slidein_left_animation = AnimationUtils.loadAnimation(MoreOptionList.this, R.anim.slide_in_left_animation);
		slideout_left_animation = AnimationUtils.loadAnimation(MoreOptionList.this, R.anim.slide_out_left_animation);
		mRel_share = (RelativeLayout)findViewById(R.id.give_desc_REL_share);
		mbtn_close = (Button)findViewById(R.id.share_BTN_close);
		mbtn_facebook = (Button)findViewById(R.id.share_BTN_facebook);
		mbtn_twitter = (Button)findViewById(R.id.share_BTN_twitter);
		mbtn_sms = (Button)findViewById(R.id.share_BTN_sms);
		mbtn_email = (Button)findViewById(R.id.share_BTN_email);
		
		mTxtMiles = (TextView)findViewById(R.id.more_TXT_setRadius_data);
		
		mCopyright = (RelativeLayout)findViewById(R.id.more_REL_CopyRight);
		mPrivatePolicy = (RelativeLayout)findViewById(R.id.more_REL_privacypolicy);
		mSetRadius_Rel = (LinearLayout)findViewById(R.id.more_REL_setRadius);
		mAboutUs = (RelativeLayout)findViewById(R.id.more_REL_aboutUs);
		mInvite = (RelativeLayout)findViewById(R.id.more_REL_share);
		mQuestionsAndSuggestions  =(RelativeLayout)findViewById(R.id.more_REL_questionsAndSuggestions);
		mFaqs = (RelativeLayout)findViewById(R.id.more_REL_FAQS);
		
		mQuestionsAndSuggestions.setOnClickListener(this);
		mFaqs.setOnClickListener(this);
		mInvite.setOnClickListener(this);
		mCopyright.setOnClickListener(this);
		mPrivatePolicy.setOnClickListener(this);
		mAboutUs.setOnClickListener(this);
		mInvite.setOnClickListener(this);
		mFaqs.setOnClickListener(this);
		mSetRadius_Rel.setOnClickListener(this);
		mbtn_close.setOnClickListener(this);
		mbtn_twitter.setOnClickListener(this);
		mbtn_facebook.setOnClickListener(this);
		mbtn_email.setOnClickListener(this);
		mbtn_sms.setOnClickListener(this);
	}

	public void onClick(View v) {
		
		Intent urlIntent;
		
		switch (v.getId()) {

     	case R.id.more_REL_share:
     		
     		mRel_share.setVisibility(View.VISIBLE);
			mRel_share.startAnimation(slidein_left_animation);
			
     		break;     	
		
		case R.id.share_BTN_close:
			
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			
			break;
			
		case R.id.share_BTN_facebook:
			
			
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			new FacebookShareHelperModified(MoreOptionList.this, "www.Daleelo.com" , "","");
					
			break;
			
		case R.id.share_BTN_twitter:
			
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			startActivity(new Intent(MoreOptionList.this,TwitterPostActivity.class)
			.putExtra("message", "Check out what I found on DaleeloApp!"));
			
			break;
			
		case R.id.share_BTN_email:	
			
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			  Intent emailIntentone=new Intent(Intent.ACTION_SEND);
			  emailIntentone.putExtra(Intent.EXTRA_SUBJECT,"Daleelo" );
			  emailIntentone.putExtra(emailIntentone.EXTRA_TEXT, "Check out what I found on DaleeloApp!");
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
			                   putExtra("sms_body", "Check out what I found on DaleeloApp!"));
			   }catch (Exception e) {
			     Toast.makeText(MoreOptionList.this, "SMS facility is not available in your device.", Toast.LENGTH_SHORT).show();
			   }
			
			break;
			
     	case R.id.more_REL_CopyRight:
     		
     		urlIntent = new Intent(MoreOptionList.this,DisplayURLData.class);
     		urlIntent.putExtra("fileUrl", "file:///android_asset/DaleeloCopyright.html");
     		urlIntent.putExtra("title", "© Copyright");
     		startActivity(urlIntent);     		
			break;
			
     	case R.id.more_REL_privacypolicy:
     		
     		urlIntent = new Intent(MoreOptionList.this,DisplayURLData.class);
     		urlIntent.putExtra("fileUrl", "file:///android_asset/DaleeloPrivacyPolicy.html");
     		urlIntent.putExtra("title", "Privacy Policy");

     		startActivity(urlIntent);     		
     		break;
     		
     	case R.id.more_REL_aboutUs:
    	 
    	  	urlIntent = new Intent(MoreOptionList.this,DisplayURLData.class);
     		urlIntent.putExtra("fileUrl", "file:///android_asset/DaleeloAboutUs.html");
     		urlIntent.putExtra("title", "About Us");
    	  	startActivity(urlIntent);
    	  	
    	  	break;    	     		
	
     		
     	case R.id.more_REL_questionsAndSuggestions:
     		
     		Intent emailIntent = new Intent(Intent.ACTION_SEND);
     		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {	"help@daleelo.com"});
     		emailIntent.putExtra(Intent.EXTRA_BCC, "");
     		emailIntent.putExtra(Intent.EXTRA_CC, "");
     		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
     		emailIntent.putExtra(Intent.EXTRA_TEXT, "");
     		emailIntent.setType("text/plain");
     		startActivity(emailIntent);
     		
     		break;
     		
     		
     	case R.id.more_REL_FAQS:
     		
     		startActivity(new Intent(MoreOptionList.this, FrequentlyAskedQuestions.class));
     		
     		break;
		
     	case R.id.more_REL_setRadius:
     		
     		setRadius();
     		alert.show();
     		
     		break;
     		
		}
		
	}

	
	public void setRadius()
	{
		Log.v(getClass().getSimpleName(), " range value1 "+Utils.RANGE);
		
		if (Utils.RANGE.equalsIgnoreCase("10")) {
			mSelected = 0;
		}else if (Utils.RANGE.equalsIgnoreCase("25")) {
			mSelected = 1;
		}else if (Utils.RANGE.equalsIgnoreCase("50")) {
			mSelected = 2;
		}else if (Utils.RANGE.equalsIgnoreCase("100")) {
			mSelected = 3;
		}else if (Utils.RANGE.equalsIgnoreCase("200")) {
			mSelected = 4;
		}
		
	    builder.setSingleChoiceItems(mSetRadius, mSelected, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int item) {
	        	mSelected_Radius_Str = mSetRadius[item];
	        	mSelected = item;
//	        	SharedPreferences.Editor editor = myPrefs.edit();
//	   	        editor.putInt("radius", mSelected);
//	   	        editor.commit();
	        	String[] arrRadius = mSelected_Radius_Str.split(" ");
	    		Utils.RANGE = arrRadius[0];
	    	    mTxtMiles.setText(Utils.RANGE+" Miles");
	    		Log.v(getClass().getSimpleName(), " range value "+Utils.RANGE+" item "+item);
	            alert.cancel();
	        }
	    });
 		alert = builder.create();
	}

}