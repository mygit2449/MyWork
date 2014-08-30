package com.android.amin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class AndroidAnimActivity extends Activity {
	
	ImageView first_img, second_img, third_img, curSlidingImage;
	Animation animslideIn, animslideOut;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        first_img = (ImageView)findViewById(R.id.first_img);
        second_img = (ImageView)findViewById(R.id.second_img);
        third_img = (ImageView)findViewById(R.id.third_img);
        
        animslideIn = AnimationUtils.loadAnimation(AndroidAnimActivity.this,  R.anim.slidedown_in);
        animslideOut = AnimationUtils.loadAnimation(AndroidAnimActivity.this, R.anim.slidedown_out);
        
        
//        animslideIn.setDuration(2000);
//        animslideOut.setDuration(2000);
        
        animslideIn.setAnimationListener(mAnimationSlideInLeftListener);
        animslideOut.setAnimationListener(mAnimationSlideOutRightListener);
        
        curSlidingImage = first_img;
        first_img.startAnimation(animslideIn);
        first_img.setVisibility(View.VISIBLE);
        
    }
    
    
    
    public void onNextClick(View v){
    	startActivity(new Intent(this, AnimationSecond.class));
    }
    
    public void onHereClick(View v){
    	startActivity(new Intent(this, AnimationThird.class));
    }
    AnimationListener mAnimationSlideInLeftListener = new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			if (curSlidingImage == first_img) {
				first_img.startAnimation(animslideOut);
			}else if (curSlidingImage == second_img) {
				second_img.startAnimation(animslideOut);
			}else if (curSlidingImage == third_img) {
				third_img.startAnimation(animslideOut);
			}
		}
	};
	
	
	AnimationListener mAnimationSlideOutRightListener = new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			if (curSlidingImage == first_img) {
				curSlidingImage = second_img;
				second_img.startAnimation(animslideIn);
				first_img.setVisibility(View.INVISIBLE);
				second_img.setVisibility(View.VISIBLE);
				third_img.setVisibility(View.INVISIBLE);
			}else if (curSlidingImage == second_img) {
				curSlidingImage = third_img;
				third_img.startAnimation(animslideIn);
				first_img.setVisibility(View.INVISIBLE);
				third_img.setVisibility(View.VISIBLE);
				second_img.setVisibility(View.INVISIBLE);
			}else if (curSlidingImage == third_img) {
				curSlidingImage = first_img;
				first_img.startAnimation(animslideIn);
				third_img.setVisibility(View.INVISIBLE);
				first_img.setVisibility(View.VISIBLE);
				second_img.setVisibility(View.INVISIBLE);
			}
		}
	};
}