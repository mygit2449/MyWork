package com.seekbar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SeekBarActivity extends Activity {
	/** Called when the activity is first created. */

	SeekBar mSkbSample;
	TextView mTxvSeekBarValue;
	int progressPercentage;
	AbsoluteLayout mAbsoluteLayout = null;
	//int progress;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mTxvSeekBarValue = (TextView) this.findViewById(R.id.txvSeekBarValue);
		mSkbSample = (SeekBar) this.findViewById(R.id.skbSample);
		mAbsoluteLayout = (AbsoluteLayout)findViewById(R.id.exp_absolute);
//		mSkbSample.setMax(100);
		mSkbSample.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					
//					ShowSeekValue((int) event.getX(), mTxvSeekBarValue.getTop());
					Log.v("values",""+mSkbSample.getWidth()+(int)event.getX()+","+mTxvSeekBarValue.getTop());
//					mTxvSeekBarValue.setVisibility(View.VISIBLE);
					
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					
					ShowSeekValue((int) event.getX(), mTxvSeekBarValue.getTop());
					mTxvSeekBarValue.setVisibility(View.VISIBLE);
					int c = (int) event.getX() - mSkbSample.getProgress();
					mTxvSeekBarValue.setText(""+mSkbSample.getProgress());
//					Log.v("action move","action move"+(int) event.getX()+"action move1  "+mTxvSeekBarValue.getLeft());
					
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					
					mTxvSeekBarValue.setVisibility(View.INVISIBLE);
					Log.v("action up","action up");
				}
				return false;
			}
		});
		
		
		
		mSkbSample.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				Log.v("progress",""+progress);
				
				
				if(progress == 0)
					progressStr = "0";
				else if(progress == 20)
					progressStr = "1";
				else if(progress == 40)
					progressStr = "2";
				else if(progress == 60)
					progressStr = "3";
				else if(progress == 80)
					progressStr = "4";
				else if(progress == 100)
					progressStr = "5";
				
				mTxvSeekBarValue.setText(""+progressStr);
				
			}
		});
	}
	
	String progressStr = "0";

	private void ShowSeekValue(int x, int y) {
		if (x > 0 && x < mSkbSample.getWidth()) {
		
			int xPos = ((((mSkbSample.getRight() - mSkbSample.getLeft()) * mSkbSample.getProgress()) /
					mSkbSample.getMax()) + mSkbSample.getLeft());

			
			if (mSkbSample.getProgress() == 0) 
				mAbsoluteLayout.setPadding(5, 0, 0, 0);
			
			if(mSkbSample.getProgress() == 20)
				 mAbsoluteLayout.setPadding(4, 0, 0, 0);
			
			if(mSkbSample.getProgress() == 40)
				 mAbsoluteLayout.setPadding(2, 0, 0, 0);
			
			 if(mSkbSample.getProgress() == 60)
				 mAbsoluteLayout.setPadding(0, 0, 0, 0);
			 
			 if(mSkbSample.getProgress() == 80)
				 mAbsoluteLayout.setPadding(-6, 0, 0, 0);
			 
			 if(mSkbSample.getProgress() == 100)
				 mAbsoluteLayout.setPadding(-8, 0, 0, 0);
			 
			int width  = mTxvSeekBarValue.getWidth() / 2;
			
//			Log.v(getClass().getSimpleName(), " position "+xPos+" progress "+mSkbSample.getProgress());
//			Log.v(getClass().getSimpleName(), " test "+mSkbSample.getProgress()+" test2 "+mSkbSample.getBottom()+" test3 "+mSkbSample.getHeight()+" test4 "+mSkbSample.getWidth());
			AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, xPos - width, y);
		
			mTxvSeekBarValue.setLayoutParams(lp);
		}
	}
}