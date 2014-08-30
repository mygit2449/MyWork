package com.quickactions;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class QuickActionsActivity extends Activity {
	/** Called when the activity is first created. */
	QuickAction mQuickAction;

	Button example1Btn, example2Btn, example3Btn, example4Btn, example5Btn, example6Btn, example7Btn, example8Btn
	,example9Btn, example10Btn;

	SeekBar mSkbSample;
	TextView mTxvSeekBarValue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);


		example1Btn = (Button) findViewById(R.id.btn1);

		example2Btn = (Button) findViewById(R.id.btn2);
		example3Btn = (Button) findViewById(R.id.btn3);
		example4Btn = (Button) findViewById(R.id.btn4);
		example5Btn = (Button) findViewById(R.id.btn5);
		

		mQuickAction = new QuickAction(this);

		
		mQuickAction.setOnDismissListener(new QuickAction.OnDismissListener() {

			public void onDismiss() {

				mQuickAction.mWindow.dismiss();

				Toast.makeText(getApplicationContext(), "Ups..dismissed",
						Toast.LENGTH_SHORT).show();

			}

		});
		
		
		mTxvSeekBarValue = (TextView) this.findViewById(R.id.txvSeekBarValue);
		mSkbSample = (SeekBar) this.findViewById(R.id.skbSample);
		
		
		mSkbSample.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					
//					ShowSeekValue((int) event.getX(), mTxvSeekBarValue.getTop());
					mQuickAction.show(v);
					Log.v("values",""+mSkbSample.getWidth()+(int)event.getX()+","+mTxvSeekBarValue.getTop());
					mTxvSeekBarValue.setVisibility(View.VISIBLE);
					
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					
					mQuickAction.show(v);
					int c = (int) event.getX() - mSkbSample.getProgress();
//					Log.v("action move","action move"+(int) event.getX()+"action move1  "+mTxvSeekBarValue.getLeft());
					
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					
					mTxvSeekBarValue.setVisibility(View.INVISIBLE);
					Log.v("action up","action up");
				}
				return false;
			}
		});
		
//    	mSeekbarAlarmvolume = (SeekBar)findViewById(R.id.qiblah_settings_seekbar_alarmvolume);
//
//    	
//    	mSeekbarAlarmvolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//			
//			@Override
//			public void onStopTrackingTouch(SeekBar seekBar) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onStartTrackingTouch(SeekBar seekBar) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onProgressChanged(SeekBar seekBar, int progress,
//					boolean fromUser) {
//				// TODO Auto-generated method stub
//				mQuickAction.show(seekBar);
//			}
//		});


		example1Btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				// mQuickAction.setText("Hi Google");

				mQuickAction.show(v);

			}

		});
		
		example2Btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				// mQuickAction.setText("Hi Google");

				mQuickAction.show(v);

			}

		});
		
		example2Btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				// mQuickAction.setText("Hi Google");

				mQuickAction.show(v);

			}

		});
		example3Btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				// mQuickAction.setText("Hi Google");

				mQuickAction.show(v);

			}

		});
		example4Btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				// mQuickAction.setText("Hi Google");

				mQuickAction.show(v);

			}

		});
		example5Btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				// mQuickAction.setText("Hi Google");

				mQuickAction.show(v);

			}

		});
		
	}
	

}