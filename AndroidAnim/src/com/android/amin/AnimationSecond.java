package com.android.amin;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class AnimationSecond extends Activity {

	ImageView fade_image, move_img;

	Animation fadeInanim, fadeOutanim;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_animation);

		fade_image = (ImageView) findViewById(R.id.next_img);
		move_img = (ImageView)findViewById(R.id.move_img);
		
		fadeInanim = AnimationUtils.loadAnimation(this, R.anim.falling);
		fadeOutanim = AnimationUtils.loadAnimation(this, R.anim.moving);
		
		// fadeInanim.setAnimationListener(fadeInListener);
//		fadeOutanim.setAnimationListener(fadeOutListener);
		move_img.setAnimation(fadeOutanim);
		fade_image.startAnimation(fadeInanim);

	}

	AnimationListener fadeInListener = new AnimationListener() {

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
			fade_image.startAnimation(fadeOutanim);
		}
	};

	AnimationListener fadeOutListener = new AnimationListener() {

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
			fade_image.startAnimation(fadeInanim);
		}
	};
}
