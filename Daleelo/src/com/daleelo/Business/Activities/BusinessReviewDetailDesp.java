package com.daleelo.Business.Activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.Business.Model.ReviewModel;
import com.daleelo.helper.DateFormater;

public class BusinessReviewDetailDesp extends Activity implements OnClickListener{
	
	TextView mName, mPostedOn, mReview, mTitle;
	ImageView mRating;
	ImageButton mNext, mPrevious;
	ArrayList<ReviewModel> ReviewModelFeeds;
	ReviewModel mReviewModel;
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
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.business_review_detail_desp);
   
        intilizationUI();
       
    }
	

	private void intilizationUI() {
		// TODO Auto-generated method stub
	
		mTitle = (TextView)findViewById(R.id.business_review_TXT_main_title);
		mName = (TextView)findViewById(R.id.business_review_TXT_name);
		mPostedOn = (TextView)findViewById(R.id.business_review_TXT_posted_on);
		mReview = (TextView)findViewById(R.id.business_review_TXT_review);
		mRating = (ImageView)findViewById(R.id.business_review_IMG_phone);	
		mNext = (ImageButton)findViewById(R.id.business_review_IMGB_right);
		mPrevious = (ImageButton)findViewById(R.id.business_review_IMGB_left);
		
		ReviewModelFeeds = (ArrayList<ReviewModel>) getIntent().getExtras().getSerializable("data");
		
		mPosition = getIntent().getExtras().getInt("position");
		
		
		
		if(mPosition == (ReviewModelFeeds.size()-1))
			mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
		
		if(mPosition == 0)
			mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);			
		
		mReviewModel = ReviewModelFeeds.get(mPosition);	
		
		mNext.setOnClickListener(this);		
		mPrevious.setOnClickListener(this);	
		setData();
		
	}
	
	
	private void setData(){
		
	
		String PostedBy;
		try {
			
			PostedBy = mReviewModel.getReviewPostedBy().toString().split("@")[0];
			
		} catch (Exception e) {
			// TODO: handle exception
			
			PostedBy = mReviewModel.getReviewPostedBy().toString();
		}
		
		mTitle.setText(PostedBy);
		mName.setText(mReviewModel.getReviewTittle());
		mPostedOn.setText("by "+PostedBy+" on "+postedOn(mReviewModel.getReviewPostedOn()));
		mReview.setText(mReviewModel.getReviewContent());
		 
		 if(mReviewModel.getReviewRank().length()>0){
				
				int rate = Integer.parseInt(mReviewModel.getReviewRank().toString());
				
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
				
			}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		switch(v.getId()){
		
			case R.id.business_review_IMGB_left:
			
				mPosition--;
				
				if(mPosition >= 0){				
					
					
					if(mPosition == 0)
						mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);
					else
						mPrevious.setBackgroundResource(R.drawable.left_arrow);	
					
					mNext.setBackgroundResource(R.drawable.right_arrow);			
					
						
					mReviewModel = ReviewModelFeeds.get(mPosition);
						setData();
					
					
				}else{
					
					mPosition++;
					
				}				
			
			break;
			
		case R.id.business_review_IMGB_right:
			
			mPosition++;
			
			if(mPosition < (ReviewModelFeeds.size())){
				
							
				if(mPosition == (ReviewModelFeeds.size()-1))
					mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
				else
					mNext.setBackgroundResource(R.drawable.right_arrow);
				
				mPrevious.setBackgroundResource(R.drawable.left_arrow);	
				
				mReviewModel = ReviewModelFeeds.get(mPosition);
				setData();
								
			}else{
				
				mPosition--;
			}
			
			break;
		}
		
	}
	
	
	private String postedOn(String mdate){
		
		long timeStampOne = DateFormater.parseDate(mdate, "MM/dd/yyyy h:mm:ss a");
		
		Date dateOne = new Date(timeStampOne);
		
		Calendar calendarOne=Calendar.getInstance();
		calendarOne.setTime(dateOne);
	
		
		String mDayOne = MONTH[dateOne.getMonth()]+" "+dateOne.getDate()+", " + (dateOne.getYear()+1900);
		
		return mDayOne;
		
		
	}
	
}
	
	