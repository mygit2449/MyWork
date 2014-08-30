package com.daleelo.DashBoardClassified.Activities;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Business.Activities.BusinessSpecialOfferDetailDesp;
import com.daleelo.DashBoardClassified.Model.GetClassifiedItemsModel;
import com.daleelo.DashBoardClassified.Parser.GetClassifiedItemsParser;
import com.daleelo.DataBase.DatabaseHelper;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.Utilities.Urls;
import com.daleelo.helper.FacebookShareHelper;
import com.daleelo.helper.FacebookShareHelperModified;
import com.daleelo.helper.ImageDownloader;
import com.daleelo.helper.PhotoGalleryActivity;
import com.daleelo.twitter.android.TwitterPostActivity;

public class ClassifiedItemDetailDesp extends Activity implements OnClickListener{
	
	private Intent reciverIntent;
	private TextView mMainTitle, mName, mAddress, mPhone, mDesp, mPostedOn, mEmail, mCost;
	
	private Button mbtn_close, mbtn_twitter, mbtn_facebook, mbtn_email, mbtn_sms;
	private String mtext_to_share, mtext_to_send, mtext_to_facebook_share
	, mtext_to_twitter_share, shareUrl;;
	
	private ImageButton mNext, mPrevious, mNextImg, mPreviousImg;
	private ImageView mImgSave, mImgPhone, mImgEmail, mImgShare;
	
	private RelativeLayout mDespAddress, mRelImages;
	private LinearLayout mLinClassifiedImages;
	private HorizontalScrollView mHSVImages;
	

	private RelativeLayout mRel_share;
	private ArrayList<GetClassifiedItemsModel> mClassifiedFeeds = null;
	private GetClassifiedItemsModel mGetClassifiedByIdModel = null;
	private DatabaseHelper mDbHelper;
	private ProgressDialog progressdialog;
	private ArrayList<String> imageArray;	
	
	private String mClassifiedId;
	
	private int mPosition;
	

	private Animation slideout_left_animation;
	private Animation slidein_left_animation;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.db_classified_detail_desp);				
		progressdialog = ProgressDialog.show(ClassifiedItemDetailDesp.this, "","Please wait...", true);
		myThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				myhandler.sendEmptyMessage(0);

			}
		});
		myThread.start();

	}
	
	

	Thread myThread;
		
	MyHandelr myhandler= new MyHandelr();
	class MyHandelr extends Handler{		
		public void handleMessage(android.os.Message message){	
			
			progressdialog.dismiss();
			initializeUI();			
			setBottomBar();
			
		}
	}
	
	private ImageButton mHome, mMyStuff;
	private EditText mSearch;
	
	private void setBottomBar(){
		
		
		mHome = (ImageButton)findViewById(R.id.business_IMGB_home);
		mMyStuff = (ImageButton)findViewById(R.id.business_IMGB_mystuff);
		mSearch = (EditText)findViewById(R.id.business_TXT_serach);
		
		mSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(ClassifiedItemDetailDesp.this, GlobalSearchActivity.class));
				
			}
		});	
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(ClassifiedItemDetailDesp.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(ClassifiedItemDetailDesp.this,MyStuffActivity.class)
				.putExtra("from", "bottom")); 
			
			}
		});
		
		
	}
	
	private void initializeUI(){
		reciverIntent = getIntent();
		
		slidein_left_animation = AnimationUtils.loadAnimation(ClassifiedItemDetailDesp.this, R.anim.slide_in_left_animation);
		slideout_left_animation = AnimationUtils.loadAnimation(ClassifiedItemDetailDesp.this, R.anim.slide_out_left_animation);
		mRel_share = (RelativeLayout)findViewById(R.id.give_desc_REL_share);
		
		mHSVImages = (HorizontalScrollView)findViewById(R.id.dashboard_class_HSV_images);
		mLinClassifiedImages = (LinearLayout)findViewById(R.id.dashboard_class_LL_images);
		mRelImages = (RelativeLayout)findViewById(R.id.dashboard_class_REL_desp_images);
		
		mMainTitle = (TextView)findViewById(R.id.dashboard_class_TXT_main_title);
		mName  = (TextView)findViewById(R.id.dashboard_class_TXT_desp_name);
		mAddress   = (TextView)findViewById(R.id.dashboard_class_TXT_desp_address);
		mPhone   = (TextView)findViewById(R.id.dashboard_class_TXT_desp_phone_number);
		mDesp   = (TextView)findViewById(R.id.dashboard_class_TXT_desp);
		mPostedOn   = (TextView)findViewById(R.id.dashboard_class_TXT_posted_on);
		mEmail   = (TextView)findViewById(R.id.dashboard_class_TXT_email);
		mCost  = (TextView)findViewById(R.id.dashboard_class_TXT_cost);
		
		mbtn_close = (Button)findViewById(R.id.share_BTN_close);
		mbtn_facebook = (Button)findViewById(R.id.share_BTN_facebook);
		mbtn_twitter = (Button)findViewById(R.id.share_BTN_twitter);
		mbtn_sms = (Button)findViewById(R.id.share_BTN_sms);
		mbtn_email = (Button)findViewById(R.id.share_BTN_email);
		
		mImgPhone = (ImageView)findViewById(R.id.dashboard_class_IMG_desp_phone_image);
		mImgEmail = (ImageView)findViewById(R.id.dashboard_class_IMG_desp_email_image);
		mImgShare = (ImageView)findViewById(R.id.dashboard_class_BTN_share);
		
		mImgSave = (ImageView)findViewById(R.id.dashboard_class_BTN_save);
		mNext = (ImageButton)findViewById(R.id.dashboard_class_IMGB_right);
		mPrevious = (ImageButton)findViewById(R.id.dashboard_class_IMGB_left);
		mNextImg = (ImageButton)findViewById(R.id.dashboard_class_IMGB_right_arrow);
		mPreviousImg = (ImageButton)findViewById(R.id.dashboard_class_IMGB_left_arrow);
		
		mNext.setVisibility(View.VISIBLE);	
		mPrevious.setVisibility(View.VISIBLE);
		mNext.setOnClickListener(this);		
		mPrevious.setOnClickListener(this);
		mImgSave.setOnClickListener(this);
		mImgShare.setOnClickListener(this);
		mNextImg.setOnClickListener(this);
		mPreviousImg.setOnClickListener(this);
		mbtn_close.setOnClickListener(this);
		mbtn_twitter.setOnClickListener(this);
		mbtn_facebook.setOnClickListener(this);
		mbtn_email.setOnClickListener(this);
		mbtn_sms.setOnClickListener(this);
		mImgPhone.setOnClickListener(this);
		mImgEmail.setOnClickListener(this);
		
		imageArray = new ArrayList<String>();
		
		if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("list")){
		
			mClassifiedFeeds = (ArrayList<GetClassifiedItemsModel>) reciverIntent.getExtras().getSerializable("data");
			mPosition = reciverIntent.getExtras().getInt("position");
			
			if(mPosition == (mClassifiedFeeds.size()-1))
				mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			
			if(mPosition == 0)
				mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);			
			
			mGetClassifiedByIdModel = mClassifiedFeeds.get(mPosition);
			
			setData();			
		
		}else if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("home")){
			
			mGetClassifiedByIdModel = (GetClassifiedItemsModel) reciverIntent.getExtras().getSerializable("data");

			mNext.setVisibility(View.INVISIBLE);	
			mPrevious.setVisibility(View.INVISIBLE);
			
			setData();		
			
		}else if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("item")){
			
			mClassifiedId = reciverIntent.getExtras().getString("id");
			
			try {
				
				getParserData();
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mNext.setVisibility(View.GONE);
			mPrevious.setVisibility(View.GONE);
			
		}else{
			
			mClassifiedFeeds = (ArrayList<GetClassifiedItemsModel>) reciverIntent.getExtras().getSerializable("data");
			mPosition = reciverIntent.getExtras().getInt("position");
			mClassifiedId = mClassifiedFeeds.get(mPosition).getClassifiedId();
			
			if(mPosition == (mClassifiedFeeds.size()-1))
				mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			
			if(mPosition == 0)
				mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);			
			
			
			try {
								
				getParserData();
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	
	}
	
	
	DecimalFormat desimalFormat = new DecimalFormat("##0.00");
	String mShareName, mShareAddressOne, mShareAddressTwo, mShareCity, mShareState, mShareZip;

	private void setData(){
		
		mRelImages.setVisibility(View.INVISIBLE);
		
		mMainTitle.setText(mGetClassifiedByIdModel.getClassifiedTitle());
		mName.setText(mGetClassifiedByIdModel.getClassifiedTitle());
		mAddress.setText(mGetClassifiedByIdModel.getLocation());		
		mDesp.setText(mGetClassifiedByIdModel.getClassifiedInfo());
		mPostedOn.setText("Date Posted: "+mGetClassifiedByIdModel.getClassifiedValidFrom());
	
		mShareName = mGetClassifiedByIdModel.getClassifiedTitle();
		mShareAddressOne = (mGetClassifiedByIdModel.getLocation().length()>0 ? mGetClassifiedByIdModel.getLocation()+", ":"");
		mShareCity =  (mGetClassifiedByIdModel.getCityname().length()>0 ? "\n"+mGetClassifiedByIdModel.getCityname()+" ":"\n");
		
		mtext_to_send = mShareName+"\n"+mShareAddressOne+"\n"+mShareCity;			
				
		mtext_to_share="<center></center><b>"+mShareName+"</b><center></center>" +
		"Address:<center></center>"
		+mShareAddressOne+"<center></center>"
		+mShareCity+"<center></center>";
		
		mtext_to_facebook_share = "Address:<center></center>"
			+mShareAddressOne+"<center></center>"
			+mShareCity+"<center></center>";	
		
		mtext_to_twitter_share = "Check out what I found on @DaleeloApp!";
		
		double tempStr = Double.parseDouble(mGetClassifiedByIdModel.getPrice());
		Log.e("", "mGetClassifiedByIdModel.getPrice() "+mGetClassifiedByIdModel.getPrice());
//		int mCostInt = (int)tempStr;
		mCost.setText(" $ "+desimalFormat.format(tempStr));
		
		
		if(mGetClassifiedByIdModel.getPhoneNum() != null){
			
			if(mGetClassifiedByIdModel.getPhoneNum().length()>0){
				
				mPhone.setText(mGetClassifiedByIdModel.getPhoneNum());
			
			}else{
			
				mImgPhone.setVisibility(View.GONE);
				mPhone.setVisibility(View.GONE);
			}
		}else{
			
			mImgPhone.setVisibility(View.GONE);
			mPhone.setVisibility(View.GONE);
			
		}
			
		
		if(mGetClassifiedByIdModel.getEmail() != null){
			
			if(mGetClassifiedByIdModel.getEmail().length()>0){
				
				mEmail.setText(mGetClassifiedByIdModel.getEmail());
			
			}else{
			
				mImgEmail.setVisibility(View.GONE);
				mEmail.setVisibility(View.GONE);
			}
		}else{
			
			mImgEmail.setVisibility(View.GONE);
			mEmail.setVisibility(View.GONE);
			
		}
		
		
		if(mGetClassifiedByIdModel.getClassifiedImages().size()>0){
			
			mRelImages.setVisibility(View.VISIBLE);
			setClassifiedImages();
			
			
		}
		
	}
	
	
	private void setClassifiedImages() {
		
		mLinClassifiedImages.removeAllViews();
		
		int mWidth = mHSVImages.getWidth()/3;
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mWidth, mWidth);
		
		
		
		for(int i=0; i < mGetClassifiedByIdModel.getClassifiedImages().size(); i++){	
			
			imageArray.add(mGetClassifiedByIdModel.getClassifiedImages().get(i).getImageUrl());
			
			LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = vi.inflate(R.layout.classified_icon_list_row, null);					
			RelativeLayout mData = (RelativeLayout)v.findViewById(R.id.classified_REL_icon);
			ImageView mImg = (ImageView)v.findViewById(R.id.classified_icon_row_IMG_data);
			mData.setLayoutParams(layoutParams);
			v.setId(1);
			v.setTag(i);	
			v.setClickable(true);
			v.setOnClickListener(this);
			new ImageDownloader().download(String.format(Urls.CI_IMG_URL,mGetClassifiedByIdModel
					.getClassifiedImages().get(i).getImageUrl()), mImg);
			mLinClassifiedImages.addView(v);
			
		}
	}

	

	public void onClick(View v){
		
		switch(v.getId()){
		
		case R.id.dashboard_class_IMGB_left_arrow:
			
			
			break;
			
		case R.id.dashboard_class_IMGB_right_arrow:			
			
			
			break;
		
		
		
		case R.id.dashboard_class_IMGB_left:
			
			mPosition--;
			
			if(mPosition >= 0){				
				
			
				
				if(mPosition == 0)
					mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);
				else
					mPrevious.setBackgroundResource(R.drawable.left_arrow);	
				
				mNext.setBackgroundResource(R.drawable.right_arrow);
				
				if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("stuff")){
				
					try {
						
						getParserData();
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					
					mGetClassifiedByIdModel = mClassifiedFeeds.get(mPosition);
					setData();
					
					
				}
				
			}else{
				
				mPosition++;
				
			}				
			
			break;
			
		case R.id.dashboard_class_IMGB_right:
			
			mPosition++;
			
			if(mPosition <= (mClassifiedFeeds.size()-1)){
				
				if(mPosition == (mClassifiedFeeds.size()-1))
					mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
				else
					mNext.setBackgroundResource(R.drawable.right_arrow);
				
				mPrevious.setBackgroundResource(R.drawable.left_arrow);	
				
				if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("stuff")){
					
					try {
						
						getParserData();
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					
					mGetClassifiedByIdModel = mClassifiedFeeds.get(mPosition);
					setData();
					
					
				}
								
			}else{
				
				mPosition--;
			}
			
			break;
			
	
		case R.id.dashboard_class_BTN_save:
			
			try {
				
				mImgSave.setClickable(false);
				
				mDbHelper = new DatabaseHelper(getApplicationContext());
			
				mDbHelper.openDataBase();
				long result = mDbHelper.insertClassifiedItem(
						mGetClassifiedByIdModel.getClassifiedId(), 
						mGetClassifiedByIdModel.getClassifiedTitle(), 
						mGetClassifiedByIdModel.getClassifiedInfo(), 
						mGetClassifiedByIdModel.getImages(),  
						mGetClassifiedByIdModel.getPrice(), 
						mGetClassifiedByIdModel.getLocation(), 
						mGetClassifiedByIdModel.getDistance(), 
						mGetClassifiedByIdModel.getLatitude(), 
						mGetClassifiedByIdModel.getLongitude(), 
						mGetClassifiedByIdModel.getOBO(), 
						mGetClassifiedByIdModel.getCityname());			
				
				if(result == 0){
					
					alertDialogDB("Classified item already saved to My Stuff");				
					
				}else if(result == -1){
					
					alertDialogDB("Unable to save the item to My Stuff");			
					
				}else{
					
					alertDialogDB("Classified item saved successfully to My Stuff");	
				}
				
				mDbHelper.closeDataBase();
				mDbHelper = null;
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				mImgSave.setClickable(true);
				e.printStackTrace();
			}
				
			
			break;	
			
		case R.id.dashboard_class_IMG_desp_phone_image:
			
			String phoneNumber = mGetClassifiedByIdModel.getPhoneNum();
			Uri uri = Uri.fromParts("tel", phoneNumber, null);
			Intent callIntent = new Intent(Intent.ACTION_CALL, uri);
			startActivity(callIntent);
					
			break;
			
		case R.id.dashboard_class_IMG_desp_email_image:
			
			String email = mGetClassifiedByIdModel.getEmail().toString();
			  Intent emailIntent1=new Intent(Intent.ACTION_SEND);
			  emailIntent1.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
			  emailIntent1.putExtra(Intent.ACTION_SENDTO, mGetClassifiedByIdModel.getEmail());
			  emailIntent1.putExtra(Intent.EXTRA_SUBJECT,mShareName );
			  emailIntent1.putExtra(emailIntent1.EXTRA_TEXT, mtext_to_send);
			  emailIntent1.setType("plain/text");
			  startActivity(emailIntent1);			
			
		
			break;
		
		case R.id.dashboard_class_BTN_share:
			
			mRel_share.setVisibility(View.VISIBLE);
			mRel_share.startAnimation(slidein_left_animation);
			
			break;
			
		case R.id.share_BTN_close:
			
				mRel_share.startAnimation(slideout_left_animation);
				mRel_share.setVisibility(View.GONE);
			
			break;
			
		case R.id.share_BTN_facebook:
			
				shareUrl = "www.daleelo.com";
				Log.e("", "mtext_to_share "+mtext_to_share);
				mRel_share.startAnimation(slideout_left_animation);
				mRel_share.setVisibility(View.GONE);
				new FacebookShareHelperModified(ClassifiedItemDetailDesp.this, mtext_to_facebook_share , mShareName, shareUrl);
		
					
			break;
			
		case R.id.share_BTN_twitter:
			
				shareUrl = "www.daleelo.com";
				mRel_share.startAnimation(slideout_left_animation);
				mRel_share.setVisibility(View.GONE);
				startActivity(new Intent(ClassifiedItemDetailDesp.this,TwitterPostActivity.class)
				.putExtra("message", mtext_to_twitter_share)
				.putExtra("shareurl", shareUrl));
						
			
			break;
			
		case R.id.share_BTN_email:
			
				mRel_share.startAnimation(slideout_left_animation);
				mRel_share.setVisibility(View.GONE);
				Intent emailIntent=new Intent(Intent.ACTION_SEND);
				emailIntent.putExtra(Intent.EXTRA_SUBJECT,mShareName );
				emailIntent.putExtra(emailIntent.EXTRA_TEXT, mtext_to_send);
				emailIntent.setType("plain/text");
				startActivity(emailIntent );
			
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
					Toast.makeText(ClassifiedItemDetailDesp.this, "SMS facility is not available in your device.", Toast.LENGTH_SHORT).show();
				}
			
			break;
			
		case 1:
			
			int pos = (Integer) v.getTag();
			Log.e("", "***** position ***** "+pos);
			startActivity(new Intent(ClassifiedItemDetailDesp.this,PhotoGalleryActivity.class)
			.putExtra("images", imageArray)
			.putExtra("position", pos)
			.putExtra("from", "class"));
			
			break;
			
		}
	}
	
//	Web Service Call
//	****************
	
	 public void getParserData() throws MalformedURLException{
	    	
	    	
	    	try {	    		

	        	progressdialog = ProgressDialog.show(ClassifiedItemDetailDesp.this, "","Please wait...", true);
	        	
	        	String mUrl = Urls.BASE_URL+"/GetClassifiedsByClassifiedId?ClassifiedId="+mClassifiedId;
	        	
	    		new mAsyncClassifiedsFeedFetcher(mUrl, new FeedParserHandler()).start();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	

	    }
	    
 

	    
	    class mAsyncClassifiedsFeedFetcher extends Thread
		{
			String  feedUrl;
			Handler handler;
			
			public mAsyncClassifiedsFeedFetcher(String mUrl, Handler handler) throws MalformedURLException {
			
				
				Log.i("", "mUrl********* "+mUrl);			
				
				this.feedUrl = mUrl;			
				this.handler = handler;
			
			} 
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try
				{
					
					new GetClassifiedItemsParser(feedUrl, handler).parser();
				
				}catch(Exception e){
					
					e.printStackTrace();
				}
				super.run();
			}
			
		}
	    
	    
	    String noDataMsg = "Classified data not found";
	    
		class FeedParserHandler extends Handler {
			public void handleMessage(android.os.Message msg) {
				
				progressdialog.dismiss();
				
				
				
				if(msg.what==0)
				{	
					mGetClassifiedByIdModel = null;
					mGetClassifiedByIdModel = (GetClassifiedItemsModel) msg.getData().getSerializable("datafeeds");
					setData();								

				}else if(msg.what==1){					
					
					new AlertDialogMsg(ClassifiedItemDetailDesp.this, noDataMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							
						}
						
					}).create().show();
					
					
				}else if(msg.what==2){
					
					String mmsg = "Error connecting network or server not responding please try again..";
					
					new AlertDialogMsg(ClassifiedItemDetailDesp.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							ClassifiedItemDetailDesp.this.finish();
							
						}
						
					}).create().show();
				}

			}
		}
	
//	****************
	
	
	private void alertDialogDB(String msg){
		
		mImgSave.setClickable(true);
		
		new AlertDialogMsg(ClassifiedItemDetailDesp.this, msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				
			}
			
		}).create().show();
		
		
	}

}
