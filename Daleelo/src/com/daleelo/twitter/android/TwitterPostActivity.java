

package com.daleelo.twitter.android;

import twitter4j.TwitterException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.helper.EditTextLocker;
import com.daleelo.twitter.android.TwitterApp.TwDialogListener;

	public class TwitterPostActivity extends Activity implements OnClickListener {
		
			private TwitterApp mTwitter;
			private TwitterSession mSession;
			Context context;
			
			String review;
			
			SharedPreferences mTweetData;
			int mCount = 0;

			public ProgressDialog dialog ;
				
			private static final String twitter_consumer_key = "tB1ITSZcmfLSIH9lyUvKKA";
			private static final String twitter_secret_key = "XSlizO1k9rfnW3Q9iLIwYfXRpJqLc9h8nBMjo00o";
			
			EditText tweetdata;
			TextView tweetDataLength;
			Button mBtnShare, mBtnCancle;
			LinearLayout mLinShareScreen;
			
			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				requestWindowFeature(Window.FEATURE_NO_TITLE);
				
				setContentView(R.layout.twitter_post_screen);			
				getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				initializeUI();
				
				
				mTwitter = new TwitterApp(TwitterPostActivity.this, twitter_consumer_key,twitter_secret_key);	
				mTwitter.setListener(mTwLoginDialogListener);
				
				if (mTwitter.hasAccessToken()) {			
				
					mLinShareScreen.setVisibility(View.VISIBLE);
					
				} else {
					
					mTwitter.authorize();
					
					
				}
			}
			
			
			
			
			private void initializeUI(){
				
				mTweetData =  getSharedPreferences("MyTweetData", MODE_PRIVATE);
				mSession = new TwitterSession(this);				
				
				context = TwitterPostActivity.this;
				mBtnShare = (Button) findViewById(R.id.twitter_post_BTN_share);
				mBtnCancle = (Button) findViewById(R.id.twitter_post_BTN_cancle);
				mLinShareScreen = (LinearLayout)findViewById(R.id.twitter_post_LIN_main);
				tweetDataLength = (TextView)findViewById(R.id.twitter_post_TXT_post_text_length);
				tweetdata = (EditText)findViewById(R.id.twitter_post_ET_post_text);
				tweetDataLength.bringToFront();
				tweetDataLength.setText(getIntent().getExtras().getString("message").toString().length()+"");

				EditTextLocker editTextLocker = new EditTextLocker(tweetdata, getIntent().getExtras().getString("message"), tweetDataLength, context);
				editTextLocker.limitCharacters(140);			
				
				mBtnShare.setOnClickListener(this);
				mBtnCancle.setOnClickListener(this);
			}
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				switch(v.getId()){
				
				case R.id.twitter_post_BTN_share:
					
					dialog = ProgressDialog.show(TwitterPostActivity.this, "",	"Tweeting please wait...", true);
					postToTwitter(review);
					
					break;
					
				case R.id.twitter_post_BTN_cancle:
					
					finish();
					
					break;
					
				}
				
			}
			
			
			
			private  TwDialogListener mTwLoginDialogListener = new TwDialogListener() {
				@Override
				public void onComplete(String value) {
					
					mLinShareScreen.setVisibility(View.VISIBLE);					
					
				}
				
				@Override
				public void onError(String value) {
					
					finish();
					
				}
			};
			
			
			

	int what;

	private void postToTwitter(final String review) {
		new Thread() {
			@Override
			public void run() {

				

				try {

					try {
						
						mTwitter.updateStatus(tweetdata.getText().toString()+" "+getIntent().getExtras().getString("shareurl"));
					} catch (TwitterException e) {
						// TODO Auto-generated catch block
						if(e.getStatusCode()==403)
			            	  what=403;
			               else
			            	   what=1;
					}
					
				} catch (Exception e) {
					
		            	   what=1;
				}

				mHandler.sendMessage(mHandler.obtainMessage(what));
			}
		}.start();
	}
	String mMsg;
			
			private Handler mHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					
					dialog.dismiss();
					switch (msg.what) {
					
					case 0:
						
						 mMsg = "Successfully posted to Twitter."; 
							new AlertDialogMsg(TwitterPostActivity.this,mMsg ).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									finish();
								}
								
							}).create().show();
						break;
					case 403:
						 mMsg = "This tweet has already been posted."; 
							new AlertDialogMsg(TwitterPostActivity.this,mMsg ).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									TwitterPostActivity.this.finish();
								}
								
							}).create().show();
						break;

					default:
						 mMsg = "Twitter post was not successfull."; 
							new AlertDialogMsg(TwitterPostActivity.this,mMsg ).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									
									TwitterPostActivity.this.finish();
								
								}
								
							}).create().show();
						break;
					}					
				}
			};
		}