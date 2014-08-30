package com.android.amin;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class AnimationThird extends Activity implements AnimationListener{

	private int state_machine = 0;
	private Animation mAnim = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_animation);
        mAnim = AnimationUtils.loadAnimation(this, R.anim.xform_left_to_right_begin);
		mAnim.setAnimationListener(this);
		ImageView img = (ImageView)findViewById(R.id.blip);
		img.clearAnimation();
		img.setAnimation(mAnim);
		img.startAnimation(mAnim);
    }
    
    @Override
	protected void onStop() {
    	super.onStop();
		try {
			ImageView img = (ImageView)findViewById(R.id.blip);
			img.clearAnimation();
			mAnim.setAnimationListener(null);
		} catch (Exception e) {
			//log
		}
	}
	
	@Override
	public void onAnimationEnd(Animation a) {
		a.setAnimationListener(null);
		switch (state_machine) {
		case 0:
			a = AnimationUtils.loadAnimation(this, R.anim.xform_to_peek);
			state_machine=1;
			break;
		case 1:
			a = AnimationUtils.loadAnimation(this, R.anim.xform_from_peek);
			state_machine=2;
			break;
		case 2:
			a = AnimationUtils.loadAnimation(this, R.anim.xform_left_to_right_end);
			state_machine=3;
			break;
		case 3:
			a = AnimationUtils.loadAnimation(this, R.anim.xform_left_to_right_begin);
			state_machine=0;
			break;
		}
		a.setAnimationListener(this);
		ImageView img = (ImageView)findViewById(R.id.blip);
		img.clearAnimation();
		img.setAnimation(a);
		img.startAnimation(a);
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
	}

	@Override
	public void onAnimationStart(Animation arg0) {
	}
}
