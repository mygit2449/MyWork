package com.daleelo.Business.Activities;

import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Business.Parser.WriteReviewParser;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.User.Activities.RegisterConfirmationActivity;
import com.daleelo.User.Activities.RegistrationActivity;
import com.daleelo.User.Parser.ClassifiedUserRegistrationParser;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;

public class BusinessWriteReviewScreen extends Activity implements OnClickListener{
	
	
	private ProgressDialog progressdialog;
	ImageView mStarOne, mStarTwo, mStarThree, mStarFour, mStarFive;
	ArrayList<ImageView> mImgForEffect;
	int mRating=0;
	Button mClear, mSubmit;
	EditText mTitle, mContent;
	
	Animation myFadeInAnimationOne, myFadeInAnimationTwo, myFadeInAnimationThree, myFadeInAnimationFour, myFadeInAnimationFive;
	
	
	public SharedPreferences sharedpreference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.business_write_review_screen);		
		initializeUI();
		
	}
	
	private void initializeUI(){
		
		
		sharedpreference = getSharedPreferences("userlogin",MODE_PRIVATE);
		
		mImgForEffect = new ArrayList<ImageView>();
		
		mClear = (Button)findViewById(R.id.business_review_BTN_clear);
		mSubmit = (Button)findViewById(R.id.business_review_BTN_submit);
		
		mTitle = (EditText)findViewById(R.id.business_review_ET_title);
		mContent = (EditText)findViewById(R.id.business_review_ET_content);
		
		mStarOne = (ImageView)findViewById(R.id.business_review_IMG_star_one);
		mStarTwo = (ImageView)findViewById(R.id.business_review_IMG_star_two);
		mStarThree = (ImageView)findViewById(R.id.business_review_IMG_star_three);
		mStarFour = (ImageView)findViewById(R.id.business_review_IMG_star_four);
		mStarFive = (ImageView)findViewById(R.id.business_review_IMG_star_five);		
		
		myFadeInAnimationOne = AnimationUtils.loadAnimation(this, R.anim.fadein_one);
		myFadeInAnimationTwo = AnimationUtils.loadAnimation(this, R.anim.fadein_two);
		myFadeInAnimationThree = AnimationUtils.loadAnimation(this, R.anim.fadein_three);
		myFadeInAnimationFour = AnimationUtils.loadAnimation(this, R.anim.fadein_four);
		myFadeInAnimationFive = AnimationUtils.loadAnimation(this, R.anim.fadein_five);
		
		mSubmit.setOnClickListener(this);
		mClear.setOnClickListener(this);
		mStarOne.setOnClickListener(this);				
		mStarTwo.setOnClickListener(this);
		mStarThree.setOnClickListener(this);
		mStarFour.setOnClickListener(this);
		mStarFive.setOnClickListener(this);
		
	
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		
		case R.id.business_review_BTN_submit:
			
			try {
				postReview();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		
		case R.id.business_review_IMG_star_one:
			
			mRating = 1;
			
			mStarOne.setBackgroundResource(R.drawable.icon_start_orange);				
			mStarTwo.setBackgroundResource(R.drawable.icon_start_two);
			mStarThree.setBackgroundResource(R.drawable.icon_start_three);
			mStarFour.setBackgroundResource(R.drawable.icon_start_four);
			mStarFive.setBackgroundResource(R.drawable.icon_start_five);
			
			mStarOne.startAnimation(myFadeInAnimationOne);
			
			break;
			
		case R.id.business_review_IMG_star_two:
			
			mRating = 2;
			

			mStarOne.setBackgroundResource(R.drawable.icon_start_orange);
			mStarTwo.setBackgroundResource(R.drawable.icon_start_orange);			
			mStarThree.setBackgroundResource(R.drawable.icon_start_three);
			mStarFour.setBackgroundResource(R.drawable.icon_start_four);
			mStarFive.setBackgroundResource(R.drawable.icon_start_five);
			
			mStarOne.startAnimation(myFadeInAnimationOne);
			mStarTwo.startAnimation(myFadeInAnimationTwo);
			
			break;
			
		case R.id.business_review_IMG_star_three:
			
			mRating = 3;
			
			mStarOne.setBackgroundResource(R.drawable.icon_start_orange);
			mStarTwo.setBackgroundResource(R.drawable.icon_start_orange);
			mStarThree.setBackgroundResource(R.drawable.icon_start_orange);			
			mStarFour.setBackgroundResource(R.drawable.icon_start_four);
			mStarFive.setBackgroundResource(R.drawable.icon_start_five);
			
			mStarOne.startAnimation(myFadeInAnimationOne);
			mStarTwo.startAnimation(myFadeInAnimationTwo);
			mStarThree.startAnimation(myFadeInAnimationThree);
			
			break;
			
		case R.id.business_review_IMG_star_four:
			
			mRating = 4;
			
			mStarOne.setBackgroundResource(R.drawable.icon_start_orange);
			mStarTwo.setBackgroundResource(R.drawable.icon_start_orange);
			mStarThree.setBackgroundResource(R.drawable.icon_start_orange);
			mStarFour.setBackgroundResource(R.drawable.icon_start_orange);		
			mStarFive.setBackgroundResource(R.drawable.icon_start_five);
			
			mStarOne.startAnimation(myFadeInAnimationOne);
			mStarTwo.startAnimation(myFadeInAnimationTwo);
			mStarThree.startAnimation(myFadeInAnimationThree);
			mStarFour.startAnimation(myFadeInAnimationFour);
			
			break;
			
		case R.id.business_review_IMG_star_five:
			
			mRating = 5;			
			
			mStarOne.setBackgroundResource(R.drawable.icon_start_orange);
			mStarTwo.setBackgroundResource(R.drawable.icon_start_orange);
			mStarThree.setBackgroundResource(R.drawable.icon_start_orange);
			mStarFour.setBackgroundResource(R.drawable.icon_start_orange);		
			mStarFive.setBackgroundResource(R.drawable.icon_start_orange);
			
			mStarOne.startAnimation(myFadeInAnimationOne);
			mStarTwo.startAnimation(myFadeInAnimationTwo);
			mStarThree.startAnimation(myFadeInAnimationThree);
			mStarFour.startAnimation(myFadeInAnimationFour);
			mStarFive.startAnimation(myFadeInAnimationFive);
					
			
			break;
			
		case R.id.business_review_BTN_clear:
			
			mRating = 0;
			
			mStarOne.setBackgroundResource(R.drawable.icon_start_one);				
			mStarTwo.setBackgroundResource(R.drawable.icon_start_two);
			mStarThree.setBackgroundResource(R.drawable.icon_start_three);
			mStarFour.setBackgroundResource(R.drawable.icon_start_four);
			mStarFive.setBackgroundResource(R.drawable.icon_start_five);
			
			mStarOne.startAnimation(myFadeInAnimationOne);
			mStarTwo.startAnimation(myFadeInAnimationTwo);
			mStarThree.startAnimation(myFadeInAnimationThree);
			mStarFour.startAnimation(myFadeInAnimationFour);
			mStarFive.startAnimation(myFadeInAnimationFive);
			
			break;
		}
		
	}

	
	private Boolean validateData(){
		
		if(mRating == 0){
			
			Toast.makeText(this, "Please add your rating", Toast.LENGTH_SHORT).show();
			return false;
		
		}else if(mTitle.getText().toString().equalsIgnoreCase("")){
			
			Toast.makeText(this, "Please enter review title", Toast.LENGTH_SHORT).show();
		
			return false;
			
		}else if(mContent.getText().toString().equalsIgnoreCase("")){
			
			Toast.makeText(this, "Please enter your review", Toast.LENGTH_SHORT).show();
			
			return false;
			
		}
		
		return true;
	}
	
	
	
	
	private void postReview() throws MalformedURLException{
		    	
		    	
		    	try {
		        	
		        	if(validateData()){
		        		
		        		progressdialog = ProgressDialog.show(BusinessWriteReviewScreen.this, "","Please wait...", true);
        	
						String mUrl = Urls.BASE_URL+"AddReviews?" +
	        			"ReviewContent="+URLEncoder.encode(mContent.getText().toString(),"UTF-8") +"&" +
	        			"Rating="+mRating +"&" +
	        			"PostedBy="+sharedpreference.getString("useremail", "unknown") +"&" +
	        			"Businessid="+BusinessDetailDesp.mBusinessId +"&" +
	        			"ReviewTittle="+URLEncoder.encode(mTitle.getText().toString(),"UTF-8");
						
						WriteReviewParser mUserAuth = new WriteReviewParser(mUrl, mainHandler);
						mUserAuth.start();
		        		
		    			
		        	}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	

		    }
	
	
	
	public Handler mainHandler = new Handler() 
    {
		public void handleMessage(android.os.Message msg) 
		{
			String handleErrMsg="";
			progressdialog.dismiss();
			
			handleErrMsg=msg.getData().getString("httpError");
			
			if(handleErrMsg.equalsIgnoreCase("success")){
							
				Toast.makeText(BusinessWriteReviewScreen.this, "Review posted Successfully", Toast.LENGTH_SHORT).show();						
				
			}else{
				
				Toast.makeText(BusinessWriteReviewScreen.this, handleErrMsg, Toast.LENGTH_SHORT).show();
			
			}
			
			
		}
	};

}