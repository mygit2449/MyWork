package com.daleelo.Business.Activities;

import java.net.MalformedURLException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Business.Model.BusinessListModel;
import com.daleelo.Business.Model.DealModel;
import com.daleelo.Business.Model.ReviewModel;
import com.daleelo.Business.Parser.BusinessDealsParser;
import com.daleelo.Business.Parser.BusinessListParser;
import com.daleelo.Business.Parser.BusinessReviewsParser;
import com.daleelo.DataBase.DatabaseHelper;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.User.Activities.LoginActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.helper.FacebookShareHelperModified;
import com.daleelo.helper.PhotoGalleryActivity;
import com.daleelo.twitter.android.TwitterPostActivity;

public class BusinessDetailDesp extends Activity implements OnClickListener{
	
	Intent reciverIntent;
	TextView mMainTitle, mName, mAddress, mPhone, mReviews, mSubTitle, mDesp,
	mHours, mPayment, mReviesCount, mOfferLabel, mOfferCount, mTxtMoreInfo;
	
	StringBuffer mPayOptins;
	ImageButton mWrite, mCall,mNext, mPrevious;
	Button mViewMore, mViewLess, mbtn_close, mbtn_twitter, mbtn_facebook, mbtn_email, mbtn_sms;
	String mStrAddress;
	ScrollView mMainScrollView;
	
	RelativeLayout mRelHours, mRelPayments, mReview, mSpecilaOffer, mSpecilaOfferLabel, mPhotos, mWebSite,
	mEmail, mMoreInfo;
	Button	mBtnPhotos, mBtnWebSite, mBtnEmail, mBtnMoreInfo;
	
	LinearLayout   mSpecilaOfferData, mSpecilaOfferDataList;
	
	ImageView mRating, mRveiwArrow, mDealArrow, mMap, mDirection, mShare, mImgSave;
	View mHourPaySeperator;
	
	BusinessListModel mMainBusinessListModel = null;
	ArrayList<BusinessListModel> mDataModelList = null;
	DatabaseHelper mDbHelper;
	
	String mRate, mReviewCount = "1";
	
	
	public static String mBusinessId;
	
	public SharedPreferences sharedpreference;
	
	boolean mDealListSet = false;

	private RelativeLayout mRel_share;
	private Animation slideout_left_animation;
	private Animation slidein_left_animation;
	private String mtext_to_send, mtext_to_share, mSubject, mMoreInfoMsg = "More information not provided", shareUrl, mtext_to_facebook_share
	, mtext_to_twitter_share;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.business_detail_desp);		
		initializeUI();
		
	}
	
	private void initializeUI(){
		reciverIntent = getIntent();
		
		
		slidein_left_animation = AnimationUtils.loadAnimation(BusinessDetailDesp.this, R.anim.slide_in_left_animation);
		slideout_left_animation = AnimationUtils.loadAnimation(BusinessDetailDesp.this, R.anim.slide_out_left_animation);
		mRel_share = (RelativeLayout)findViewById(R.id.give_desc_REL_share);
		
		sharedpreference = getSharedPreferences("userlogin",MODE_PRIVATE);
		
		mMainScrollView = (ScrollView)findViewById(R.id.business_desp_SCROLL_one);
		mbtn_close = (Button)findViewById(R.id.share_BTN_close);
		mbtn_facebook = (Button)findViewById(R.id.share_BTN_facebook);
		mbtn_twitter = (Button)findViewById(R.id.share_BTN_twitter);
		mbtn_sms = (Button)findViewById(R.id.share_BTN_sms);
		mbtn_email = (Button)findViewById(R.id.share_BTN_email);
		
		mMainTitle = (TextView)findViewById(R.id.business_desp_TXT_main_title);
		mName  = (TextView)findViewById(R.id.business_desp_TXT_name);
		mAddress   = (TextView)findViewById(R.id.business_desp_TXT_address);
		mPhone   = (TextView)findViewById(R.id.business_desp_TXT_phone_number);
		mReviews   = (TextView)findViewById(R.id.business_desp_TXT_reviews);
		mReviesCount  = (TextView)findViewById(R.id.business_desp_TXT_reviews_count);
		mHours = (TextView)findViewById(R.id.business_desp_TXT_hour_operation_data);
		mPayment = (TextView)findViewById(R.id.business_desp_TXT_pay_option_data);
		mOfferLabel = (TextView)findViewById(R.id.business_desp_TXT_special_offer_label);
		mOfferCount = (TextView)findViewById(R.id.business_desp_TXT_special_offer_count);
		mTxtMoreInfo = (TextView)findViewById(R.id.business_desp_TXT_moreinfo);
		
		mRelHours  = (RelativeLayout)findViewById(R.id.business_desp_REL_hour_operation);
		mRelPayments  = (RelativeLayout)findViewById(R.id.business_desp_REL_pay_option);
		mSpecilaOfferLabel = (RelativeLayout)findViewById(R.id.business_desp_REL_special_offer_label);
		
		mPhotos = (RelativeLayout)findViewById(R.id.business_desp_REL_photo);
		mWebSite = (RelativeLayout)findViewById(R.id.business_desp_REL_website);
		mEmail = (RelativeLayout)findViewById(R.id.business_desp_REL_email);
		mMoreInfo = (RelativeLayout)findViewById(R.id.business_desp_REL_moreinfo);
		
		mSpecilaOffer = (RelativeLayout)findViewById(R.id.business_desp_REL_special_offers);
		mReview = (RelativeLayout)findViewById(R.id.business_desp_REL_review);
		
		mSpecilaOfferData = (LinearLayout)findViewById(R.id.business_desp_REL_special_offer_data);	
		mSpecilaOfferDataList = (LinearLayout)findViewById(R.id.business_desp_REL_special_offer_data_list);
		
		mHourPaySeperator = (View)findViewById(R.id.business_desp_VIEW_hour_pay_seperator);
		
		mViewMore = (Button)findViewById(R.id.business_desp_BTN_special_offer_more);
		mViewLess = (Button)findViewById(R.id.business_desp_BTN_special_offer_less);
		mBtnMoreInfo = (Button)findViewById(R.id.business_desp_BTN_moreinfo);
		
		mRating = (ImageView)findViewById(R.id.business_desp_IMG_rate);
		mRveiwArrow = (ImageView)findViewById(R.id.business_desp_IMG_reviews_arrow);
		mDealArrow  = (ImageView)findViewById(R.id.business_desp_IMG_special_offer_arrow);
		mImgSave = (ImageView)findViewById(R.id.business_desp_IMG_save);
		mMap = (ImageView)findViewById(R.id.business_desp_IMG_map);
		mDirection = (ImageView)findViewById(R.id.business_desp_IMG_direction);
		mShare = (ImageView)findViewById(R.id.business_desp_IMG_share);
		
		mWrite = (ImageButton)findViewById(R.id.business_desp_IMGB_write);
		mCall = (ImageButton)findViewById(R.id.business_desp_IMGB_call);
		mNext  = (ImageButton)findViewById(R.id.business_desp_IMGB_right);
		mPrevious  = (ImageButton)findViewById(R.id.business_desp_IMGB_left);
		
		mBtnPhotos = (Button)findViewById(R.id.business_desp_BTN_photo);
		mBtnWebSite = (Button)findViewById(R.id.business_desp_BTN_website);
		mBtnEmail = (Button)findViewById(R.id.business_desp_BTN_email);
		mBtnMoreInfo = (Button)findViewById(R.id.business_desp_BTN_moreinfo);
		
		
		mWrite.setOnClickListener(this);
		mCall.setOnClickListener(this);
		mReview.setOnClickListener(this);	
		mMap.setOnClickListener(this);
		mDirection.setOnClickListener(this);
		mShare.setOnClickListener(this);
		mImgSave.setOnClickListener(this);
		mViewMore.setOnClickListener(this);
		mViewLess.setOnClickListener(this);
		mSpecilaOfferLabel.setOnClickListener(this);
		mPhotos.setOnClickListener(this);
		mWebSite.setOnClickListener(this);
		mEmail.setOnClickListener(this);
		mMoreInfo.setOnClickListener(this);
		mbtn_close.setOnClickListener(this);
		mbtn_twitter.setOnClickListener(this);
		mbtn_facebook.setOnClickListener(this);
		mbtn_email.setOnClickListener(this);
		mbtn_sms.setOnClickListener(this);
		
		mBtnPhotos.setOnClickListener(this);
		mBtnWebSite.setOnClickListener(this);
		mBtnEmail.setOnClickListener(this); 
		mBtnMoreInfo.setOnClickListener(this);
		
		
		
		
		if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("list")){	
			
			mDataModelList = (ArrayList<BusinessListModel>) reciverIntent.getExtras().getSerializable("data");
			
			mPosition = reciverIntent.getExtras().getInt("position");
			
			Log.e("", "mPosition "+mPosition);
			
			if(Integer.parseInt(mDataModelList.get(mPosition).getItemsCount())>10)
				mDataModelList.remove(mDataModelList.size()-1);
			
			if(mPosition == (mDataModelList.size()-1))
				mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			
			if(mPosition == 0)
				mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);			
			
			mMainBusinessListModel = mDataModelList.get(mPosition);	
			mBusinessId = mMainBusinessListModel.getBusinessId();
			
			mNext.setOnClickListener(this);		
			mPrevious.setOnClickListener(this);	
			setData();
		
		}else if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("featured")){	
			
			mMainBusinessListModel = (BusinessListModel) reciverIntent.getExtras().getSerializable("data");			
			mPosition = reciverIntent.getExtras().getInt("position");			

			mBusinessId = mMainBusinessListModel.getBusinessId();
			
			mNext.setVisibility(View.GONE);
			mPrevious.setVisibility(View.GONE);
			
			progressdialog = ProgressDialog.show(BusinessDetailDesp.this, "","Please wait...", true);
			setData();
		
		}else if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("item")){
			
			mMainScrollView.setVisibility(View.INVISIBLE);			

			mBusinessId = reciverIntent.getExtras().getString("id");
			
			try {
				 mMainBusinessListModel = null;
				getParserData();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mNext.setVisibility(View.GONE);
			mPrevious.setVisibility(View.GONE);
			
		}else if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("stuff")){
			
			mMainScrollView.setVisibility(View.INVISIBLE);
			
			mDataModelList = (ArrayList<BusinessListModel>) reciverIntent.getExtras().getSerializable("data");
			mPosition = reciverIntent.getExtras().getInt("position");
			
			
			if(mPosition == (mDataModelList.size()-1))
				mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			
			if(mPosition == 0)
				mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);			
			
			mBusinessId = mDataModelList.get(mPosition).getBusinessId();	
			mNext.setOnClickListener(this);		
			mPrevious.setOnClickListener(this);	
			
			try {
				 mMainBusinessListModel = null;
				getParserData();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

		setBottomBar();
		
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
				
				startActivity(new Intent(BusinessDetailDesp.this, GlobalSearchActivity.class));
				
			}
		});
		
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(BusinessDetailDesp.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(BusinessDetailDesp.this,MyStuffActivity.class)
				.putExtra("from", "bottom")); 
			
			}
		});
	}
	
	
	int rate  = 0;
	
	String mShareName, mShareAddressOne, mShareAddressTwo, mShareCity, mShareState, mShareZip;
	
	private void setData(){
		
		mMainScrollView.setVisibility(View.VISIBLE);
		
		getReviewAndDeal();
		
		mBusinessId = mMainBusinessListModel.getBusinessId();		
		mMainTitle.setText(mMainBusinessListModel.getBusinessTitle());
		mName.setText(mMainBusinessListModel.getBusinessTitle());
		
		mShareName = mMainBusinessListModel.getBusinessTitle();
		mShareAddressOne = (mMainBusinessListModel.getAddressLine1().length()>0 ? mMainBusinessListModel.getAddressLine1()+", ":"");
		mShareAddressTwo = (mMainBusinessListModel.getAddressLine2().length()>0 ? mMainBusinessListModel.getAddressLine2()+", ":"");
		mShareCity = (mMainBusinessListModel.getCityName().length()>0 ? mMainBusinessListModel.getCityName()+", ":"");
		mShareState =  (mMainBusinessListModel.getStateCode().length()>0 ? mMainBusinessListModel.getStateCode()+", ":"");
		mShareZip = (mMainBusinessListModel.getAddressZipcode().length()>0 ? mMainBusinessListModel.getAddressZipcode():"");
		
		mStrAddress = mShareAddressOne+""+mShareAddressTwo+"\n"+mShareCity+""+mShareState+""+mShareZip;			
		mtext_to_send = mShareName+"\n"+mStrAddress;
		
		mtext_to_share="<center></center><b>"+mShareName+"</b><center></center>" +
		"Address:<center></center>"
		+mShareAddressOne+""+mShareAddressTwo+"<center></center>"
		+mShareCity+""+mShareState+""+mShareZip+"<center></center>";		
		
		mtext_to_facebook_share = "Address:<center></center>"
			+mShareAddressOne+""+mShareAddressTwo+"<center></center>"
			+mShareCity+""+mShareState+""+mShareZip+"<center></center>";	
		
		mtext_to_twitter_share = "Check out what I found on @DaleeloApp!";
		
		mAddress.setText(mStrAddress);
		mPhone.setText(mMainBusinessListModel.getAddressPhone());
		
		if(mMainBusinessListModel.getBusinessHours().equalsIgnoreCase("")){
		
			mHours.setText("Not provided");			
		
		}else{
			
			mHours.setText(mMainBusinessListModel.getBusinessHours());			
		}
		
		mPayOptins = new StringBuffer();
		
		if(mMainBusinessListModel.getmPayOptions() != null){
			if(mMainBusinessListModel.getmPayOptions().size()>0){
			
				for(int i=0 ; i<mMainBusinessListModel.getmPayOptions().size();i++)
				
					mPayOptins.append(mMainBusinessListModel.getmPayOptions().get(i)+" ");			
					mPayment.setText(mPayOptins.toString());
		
			}else{
			
				mPayment.setText("Not provided");
			
			
			}
		}else{
			
			mPayment.setText("Not provided");
		}
		
		

		
		Log.e("", "RETING "+mMainBusinessListModel.getBusinessRating());
		
			try{
			
			rate = Integer.parseInt(mMainBusinessListModel.getBusinessRating().toString());
			
			if(rate == 1)		
				mRating.setImageResource(R.drawable.icon_one_star);
			else if(rate == 2)		
				mRating.setImageResource(R.drawable.icon_two_star);
			else if(rate == 3)		
				mRating.setImageResource(R.drawable.icon_three_star);
			else if(rate == 4)		
				mRating.setImageResource(R.drawable.icon_four_star);
			else if(rate == 5)		
				mRating.setImageResource(R.drawable.icon_five_star);
			
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			if(mMainBusinessListModel.getMasterCategoryId().equalsIgnoreCase("85")){
				
				mMoreInfoMsg = "Menu information not provided";
				mTxtMoreInfo.setText("Menu");
				mBtnMoreInfo.setBackgroundResource(R.drawable.icon_menu);
				
			}else if(mMainBusinessListModel.getMasterCategoryId().equalsIgnoreCase("33")){
				
				mMoreInfoMsg = "Speciality information not provided";
				mTxtMoreInfo.setText("Speciality");
				mBtnMoreInfo.setBackgroundResource(R.drawable.icon_spcilized);
			}
				

		
	}
	
	
	
//	****************************	
	
//	OnclickListener actions
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.business_desp_IMGB_left:
			
			mPosition--;
			
			if(mPosition >= 0){				
				
				
				if(mPosition == 0)
					mPrevious.setBackgroundResource(R.drawable.disabled_left_arrow);
				else
					mPrevious.setBackgroundResource(R.drawable.left_arrow);	
				
				mNext.setBackgroundResource(R.drawable.right_arrow);			
				
				if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("stuff")){
					
					mBusinessId = mDataModelList.get(mPosition).getBusinessId();	
					
					try {
						getParserData();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					
					mMainBusinessListModel = mDataModelList.get(mPosition);
					setData();
				}
				
			}else{
				
				mPosition++;
				
			}				
		
		break;
		
	case R.id.business_desp_IMGB_right:
		
		mPosition++;
		
		if(mPosition < (mDataModelList.size())){
			
						
			if(mPosition == (mDataModelList.size()-1))
				mNext.setBackgroundResource(R.drawable.disabled_right_arrow);
			else
				mNext.setBackgroundResource(R.drawable.right_arrow);
			
			mPrevious.setBackgroundResource(R.drawable.left_arrow);	
			
			if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("stuff")){
				
				mBusinessId = mDataModelList.get(mPosition).getBusinessId();	
				
				try {
					getParserData();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else{
				
				mMainBusinessListModel = mDataModelList.get(mPosition);
				setData();
			}
							
		}else{
			
			mPosition--;
		}
		
		break;
		
		case R.id.business_desp_REL_special_offer_label:
			
			if(mAllDealModel.size()==1){
			
				startActivity(new Intent(BusinessDetailDesp.this,BusinessSpecialOfferDetailDesp.class)
				.putExtra("dealdata", mAllDealModel.get(0))
				.putExtra("data",mMainBusinessListModel)
				.putExtra("reviewcount",mReviewCount));
				
			}
			
			
			break;
		
		case 1:
			
			startActivity(new Intent(BusinessDetailDesp.this,BusinessSpecialOfferDetailDesp.class)
			.putExtra("dealdata", mAllDealModel.get((Integer) v.getTag()))
			.putExtra("data",mMainBusinessListModel)
			.putExtra("reviewcount",mReviewCount));
			
			break;
			
			
		case R.id.business_desp_BTN_special_offer_less:
			
			mSpecilaOfferLabel.setVisibility(View.VISIBLE);
			mSpecilaOfferData.setVisibility(View.GONE);			
			
			break;
			
		case R.id.business_desp_BTN_special_offer_more:
			
			mSpecilaOfferLabel.setVisibility(View.GONE);
			mSpecilaOfferData.setVisibility(View.VISIBLE);			
			
			
			if(!mDealListSet){
			
				for(int i=0; i<mAllDealModel.size(); i++){
						
					mDealListSet = true;
					Log.e("", "deal "+mAllDealModel.get(i).getDealTittle().toString());
					try {

						LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View tempView = vi.inflate(R.layout.business_special_offer_list_row, null);			
						TextView mName = (TextView)tempView.findViewById(R.id.special_offer_row_TXT_name);	
						mName.setText(mAllDealModel.get(i).getDealTittle());	
						tempView.setId(1);
						tempView.setTag(i);	
						tempView.setClickable(true);
						tempView.setOnClickListener(this);					
						mSpecilaOfferDataList.addView(tempView);
			
					
					} catch (Exception e) {
					// TODO: handle exception
					}
				}
			}
			break;
			
		case R.id.business_desp_IMGB_call:
			
			
			String phoneNumber = mMainBusinessListModel.getAddressPhone();
			Uri uri = Uri.fromParts("tel", phoneNumber, null);
			Intent callIntent = new Intent(Intent.ACTION_CALL, uri);
			startActivity(callIntent);
		
			
			break;
			
			
			
		case R.id.business_desp_IMGB_write:
			
		
			writeReview();
		
			
			break;
			
			
		case R.id.business_desp_REL_review:
			
			if(mReviewCount.equalsIgnoreCase("0")){
			
				writeReview();
				
				
			}else{
				
				startActivity(new Intent(BusinessDetailDesp.this,BusinessReviewList.class)
				.putExtra("data", mAllReviewModel));
				
			}
			
			break;
			
			
		case R.id.business_desp_IMG_save:
			
			
			try {
				
				mDbHelper = new DatabaseHelper(getApplicationContext());
			
				Log.e("", "rate "+rate+" mReviewCount "+mReviewCount);
				mDbHelper.openDataBase();
				long result = mDbHelper.insertBusinessItem(
						mMainBusinessListModel.getBusinessId(), 
						mMainBusinessListModel.getBusinessTitle(), 
						mStrAddress, 
						""+rate, 
						mReviewCount,  
						mMainBusinessListModel.getAddressLat(),
						mMainBusinessListModel.getAddressLong(),
						mMainBusinessListModel.getCityName());
				
				
				if(result == 0){
					
					alertDialogDB("Business item already saved to My Stuff");				
					
				}else if(result == -1){
					
					alertDialogDB("Unable to save the item to My Stuff");			
					
				}else{
					
					alertDialogDB("Business item saved successfully to My Stuff");	
				}
				
				mDbHelper.closeDataBase();
				mDbHelper = null;
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			break;
			
		case R.id.business_desp_REL_photo:
			
			startActivity(new Intent(BusinessDetailDesp.this,PhotoGalleryActivity.class));		
		
			break;
			
		case R.id.business_desp_BTN_photo:
			
			startActivity(new Intent(BusinessDetailDesp.this,PhotoGalleryActivity.class));		
		
			break;
			
		case R.id.business_desp_REL_website:
			
			if(mMainBusinessListModel.getAddressWeburl().length()>8){
				
				String url = "http://"+mMainBusinessListModel.getAddressWeburl();
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);			
			
			}else{
				
				Toast.makeText(this, "Website address not found.", Toast.LENGTH_SHORT).show();

			}
			
			break;
			
		case R.id.business_desp_BTN_website:
			
			if(mMainBusinessListModel.getAddressWeburl().length()>8){
				
				String url = "http://"+mMainBusinessListModel.getAddressWeburl();
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);			
			
			}else{
				
				Toast.makeText(this, "Website address not found.", Toast.LENGTH_SHORT).show();

			}
			
			break;
			
		case R.id.business_desp_REL_email:		
			

			if(mMainBusinessListModel.getAddressEmail().length()>7){
				
				String email = mMainBusinessListModel.getAddressEmail();
				Intent emailIntent=new Intent(Intent.ACTION_SEND);
				emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
				emailIntent.setType("plain/text");
				startActivity(emailIntent );		
			
			}else{
				
				Toast.makeText(this, "Email address not found.", Toast.LENGTH_SHORT).show();
		
			}
			
		case R.id.business_desp_BTN_email:		
			

			if(mMainBusinessListModel.getAddressEmail().length()>7){
				
				String email = mMainBusinessListModel.getAddressEmail();
				Intent emailIntent=new Intent(Intent.ACTION_SEND);
				emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
				emailIntent.setType("plain/text");
				startActivity(emailIntent );		
			
			}else{
				
				Toast.makeText(this, "Email address not found.", Toast.LENGTH_SHORT).show();
		
			}
			
			
			
			break;
			
		case R.id.business_desp_REL_moreinfo:
			
			Toast.makeText(this, mMoreInfoMsg, Toast.LENGTH_SHORT).show();			
			
			break;
			
		case R.id.business_desp_BTN_moreinfo:
			
			Toast.makeText(this, mMoreInfoMsg, Toast.LENGTH_SHORT).show();			
			
			break;
			
		case R.id.business_desp_IMG_map:
			
			if(mMainBusinessListModel!= null){
				
				startActivity(new Intent(BusinessDetailDesp.this,BusinessMapActivity.class)
				.putExtra("data", mMainBusinessListModel)
				.putExtra("from","desp"));			
				
			}
			
			break;
			
		case R.id.business_desp_IMG_direction:
			
			
			 Intent navigateIntent = new Intent(android.content.Intent.ACTION_VIEW,  
					 Uri.parse("http://maps.google.com/maps?" +
					 		"saddr="+CurrentLocationData.LATITUDE+","+CurrentLocationData.LONGITUDE+
					 		"&daddr="+mMainBusinessListModel.getAddressLat()+"," +
					 				""+mMainBusinessListModel.getAddressLong())); 
			 startActivity(navigateIntent);	
			
			break;
			
		case R.id.business_desp_IMG_share:
			
			mRel_share.setVisibility(View.VISIBLE);
			mRel_share.startAnimation(slidein_left_animation);
			
			break;
			
		case R.id.share_BTN_close:
			
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			
			break;
			
		case R.id.share_BTN_facebook:
			
			
			shareUrl = String.format(Urls.SHARE_BUSINESS_URL, mMainBusinessListModel.getBusinessId());
			Log.e("", "mtext_to_share "+mtext_to_share);
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			new FacebookShareHelperModified(BusinessDetailDesp.this, mtext_to_facebook_share , mShareName, shareUrl);
					
			break;
			
		case R.id.share_BTN_twitter:
			
			shareUrl = String.format(Urls.SHARE_BUSINESS_URL, mMainBusinessListModel.getBusinessId());
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			startActivity(new Intent(BusinessDetailDesp.this,TwitterPostActivity.class)
			.putExtra("message", mtext_to_twitter_share)
			.putExtra("shareurl", shareUrl));
			
			break;
			
		case R.id.share_BTN_email:	
			
			mRel_share.startAnimation(slideout_left_animation);
			mRel_share.setVisibility(View.GONE);
			  Intent emailIntentone=new Intent(Intent.ACTION_SEND);
			  emailIntentone.putExtra(Intent.EXTRA_SUBJECT,mShareName );
			  emailIntentone.putExtra(emailIntentone.EXTRA_TEXT, mtext_to_send);
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
			                   putExtra("sms_body", mtext_to_send));
			   }catch (Exception e) {
			     Toast.makeText(BusinessDetailDesp.this, "SMS facility is not available in your device.", Toast.LENGTH_SHORT).show();
			   }
			
			break;
			
			
		}
	}
	int mPosition;
	
	
	private void writeReview(){
		
		if(!sharedpreference.getString("userid", "0").equalsIgnoreCase("0")){
		
			startActivity(new Intent(BusinessDetailDesp.this, BusinessWriteReviewScreen.class));
						
		}else{
					
			myAlertDialog();
		}				
		
	}
	
	
	private void alertDialogDB(String msg){
		
		
		
		new AlertDialogMsg(BusinessDetailDesp.this, msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				
			}
			
		}).create().show();
		
		
	}
	
	

	
	
	
//	**************************
//	WebService call
	
	
	private void getReviewAndDeal(){
		
		
		if(reciverIntent.getExtras().getString("from").equalsIgnoreCase("list"))			
			progressdialog = ProgressDialog.show(BusinessDetailDesp.this, "","Please wait...", true);
		
		try {
			
			getReviewsData(); 
			getDealData();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
	}
	
	private ProgressDialog progressdialog;
	
	
	public void getParserData() throws MalformedURLException{
    	
    	
    	try {
        	progressdialog = ProgressDialog.show(BusinessDetailDesp.this, "","Please wait...", true);
        	
        	String mUrl = String.format(Urls.BUSINESS_DETAILS_BY_ID_URL,mBusinessId);
        	
    		new mBusinessDataFetcher(mUrl, new FeedParserHandler()).start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }
    
    
    class mBusinessDataFetcher extends Thread
	{
		String  feedUrl;
		Handler handler;
		
		public mBusinessDataFetcher(String mUrl, Handler handler) throws MalformedURLException {
		
			
			Log.i("", "mUrl********* "+mUrl);			
			
			this.feedUrl = mUrl;			
			this.handler = handler;
		
		} 
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try
			{
				
//				new BuyProductsByBrandIDParser(feedUrl,handler).parser();
				new BusinessListParser(feedUrl, handler).parser();
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			super.run();
		}
		
	}
    
    
    String noDataMsg = "Business data not found", mTotalCount;
    int mTotalItems;
    
    
    BusinessListModel tempBusinessListModel = null;
    
	class FeedParserHandler extends Handler {
		public void handleMessage(android.os.Message msg) {
			
			
			if(msg.what==0)
			{	
				mMainBusinessListModel = ((ArrayList<BusinessListModel>) msg.getData().getSerializable("datafeeds")).get(0);
				setData();				

			}else if(msg.what==1){
				
				progressdialog.dismiss();				
				
				new AlertDialogMsg(BusinessDetailDesp.this, noDataMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
						
					}
					
				}).create().show();
				
				
			}else if(msg.what==2){
				
				progressdialog.dismiss();
				
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(BusinessDetailDesp.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						BusinessDetailDesp.this.finish();
						
					}
					
				}).create().show();
			}

		}
		
	}
	
	 public void getReviewsData() throws MalformedURLException{
	    	
	    	
	    	try {

	        	String mUrl = String.format(Urls.BUSINESS_GET_REVIEWS_URL,mBusinessId);
	        	
	    		new BusinessReviewDataFetcher(mUrl, new ReviewParserHandler()).start();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	

	    }
	    
	    
	    class BusinessReviewDataFetcher extends Thread
		{
			String  feedUrl;
			Handler handler;
			
			public BusinessReviewDataFetcher(String mUrl, Handler handler) throws MalformedURLException {
			
				
				Log.i("", "mUrl********* "+mUrl);			
				
				this.feedUrl = mUrl;			
				this.handler = handler;
			
			} 
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try
				{
	
					new BusinessReviewsParser(feedUrl, handler).parser();
				
				}catch(Exception e){
					
					e.printStackTrace();
				}
				super.run();
			}
			
		}
	    
	    
		
		ArrayList<ReviewModel> mAllReviewModel = null;	
	    
		class ReviewParserHandler extends Handler {
			public void handleMessage(android.os.Message msg) {
				
				
				
				
				if(msg.what==0)
				{	
					mAllReviewModel = null;
					mAllReviewModel = (ArrayList<ReviewModel>) msg.getData().getSerializable("datafeeds");	
					mReviesCount.setText(mAllReviewModel.size()+" Reviews");
					mReviews.setText(mAllReviewModel.get(0).getReviewContent().toString());
					mReviewCount = ""+mAllReviewModel.size();

				}else if(msg.what==1){
					
					mReviesCount.setText("No Reviews");
					mReviews.setText("No Reviews: Be the First Review");
					mReviewCount ="0";			
					
				}else if(msg.what==2){
				
					mReviesCount.setText("No Reviews");
					mReviews.setText("No Reviews: Be the First Review");
					mReviewCount ="0";
					
				}
				
				mGotReview = true;				
				mDealAndReviewGotHandler.sendEmptyMessage(0);

			}
			
		}
		
		
		
		 
		public void getDealData() throws MalformedURLException{
		    	
		    	
		    	try {
		        	

		        	String mUrl = String.format(Urls.BUSINESS_DEALS_BY_ID_URL,mBusinessId);
		        	
		    		new BusinessDealDataFetcher(mUrl, new DealParserHandler()).start();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	

		    }
		    
		    
		    class BusinessDealDataFetcher extends Thread
			{
				String  feedUrl;
				Handler handler;
				
				public BusinessDealDataFetcher(String mUrl, Handler handler) throws MalformedURLException {
				
					
					Log.i("", "mUrl********* "+mUrl);			
					
					this.feedUrl = mUrl;			
					this.handler = handler;
				
				} 
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try
					{
		
						new BusinessDealsParser(feedUrl, handler).parser();
					
					}catch(Exception e){
						
						e.printStackTrace();
					}
					super.run();
				}
				
			}
		    
		    
			
		    ArrayList<DealModel> mAllDealModel = null;		
		    
			class DealParserHandler extends Handler {
				public void handleMessage(android.os.Message msg) {
					
					
					if(msg.what==0)
					{	
						mAllDealModel = null;
						mAllDealModel = (ArrayList<DealModel>) msg.getData().getSerializable("datafeeds");
						mSpecilaOffer.setVisibility(View.VISIBLE);
						if(mAllDealModel.size()>1){
							
							mOfferLabel.setText(mAllDealModel.get(0).getDealTittle().toString());
							mOfferCount.setText("("+mAllDealModel.size()+" Offers)");
							
						}else{
							
							mOfferLabel.setText(mAllDealModel.get(0).getDealTittle().toString());
							mOfferCount.setVisibility(View.INVISIBLE);
							mDealArrow.setVisibility(View.VISIBLE);
							mViewMore.setVisibility(View.GONE);
							
						}
						

					}else if(msg.what==1){
						
						mSpecilaOffer.setVisibility(View.GONE);											
						
					}else if(msg.what==2){
					
						mSpecilaOffer.setVisibility(View.GONE);	
						
					}
					
					mGotDeal = true;
					mDealAndReviewGotHandler.sendEmptyMessage(0);

				}
				
			}
		
		
		
		DealAndReviewHandler mDealAndReviewGotHandler = new DealAndReviewHandler();
		boolean mGotReview = false, mGotDeal = false;
		
		class DealAndReviewHandler extends Handler {
			public void handleMessage(android.os.Message msg) {
				
				
				if(mGotReview == true && mGotDeal ==true){
					progressdialog.dismiss();					
				}
			}
		}
			
	
	
//		****************************			  
//		Alert Dialogs 
		
		
		private void myAlertDialog(){
			
			
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Daleelo");
			builder.setMessage("Please login to post your review")
			       .setCancelable(false)
			       .setPositiveButton("Login", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   
							startActivity(new Intent(BusinessDetailDesp.this,LoginActivity.class)
							.putExtra("from", "review"));
			               
			           }
			       })
			       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		}

}
