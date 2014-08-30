package com.daleelo.Ads.Activities;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.authorize.Merchant;
import net.authorize.TransactionType;
import net.authorize.aim.Result;
import net.authorize.aim.Transaction;
import net.authorize.data.EmailReceipt;
import net.authorize.data.creditcard.CreditCard;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Ads.Model.CitiesModel;
import com.daleelo.Ads.Model.StatesModel;
import com.daleelo.Ads.Model.SubCategoriesModel;
import com.daleelo.Ads.Parser.CategoriesListParser;
import com.daleelo.Ads.Parser.CitiesFeedParser;
import com.daleelo.Ads.Parser.PostBusinessAddParser;
import com.daleelo.Ads.Parser.StatesFeedParser;
import com.daleelo.Ads.Parser.SubCategoriesParser;
import com.daleelo.Ads.Parser.ValidateBusinessParser;
import com.daleelo.Dashboard.Model.GetHomePageCategoriesModel;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.More.Activities.TermsAndConditionsActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;

public class AdvertiseActivity extends Activity implements OnClickListener,OnItemSelectedListener{
	
	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;
	private final int FOUND_CATEGORIES = 3;
	private final int FOUND_CITIES = 4;
	private final int FOUND_SUB_CATEGARY = 5;
	private final int FOUND_BUSINESS = 6;
	private final int BUSINESS_POST_SUCCESS = 7;
	
	private QuickAction mQuickAction;

	private ImageView  mFeatured_Img, mTop4_Img, mTop9_Img, mGeneral_Img;
	private Button mBTNSubmit;
	
	private TextView mFeature_Txt, mTop4_Txt, mTop9_Txt, mGeneral_Txt, mTermsAndCondition;
	
	private EditText mETBusinessName, mETPhoneNumber, mETFax, mETEmail,mETBusinessAddress,
					 mETCcType, mETCardHolderName, mETCcNumber, mETCvv2Code, mETZipCode, 
					 mETExpDate;
	
	private RadioButton mRadio_Local, mRadio_National;
	
	private Spinner mCategorySpinner, mSubCategorySpinner, mState_Spinner, mCity_Spinner, mNumberOfPayments_Spinner;
	
	private CheckBox mAgreeCheckBox;
	
	private String mChoosePrice = "";

	private ProgressDialog mProgressDialog;
	
	private StatesFeedParser mStatesFeedParser;
	private CategoriesListParser mCategoriesParser;
	private CitiesFeedParser mCitiesFeedParser;
	private SubCategoriesParser mSubCategoriesParser;
	private ValidateBusinessParser mValidateBusinessParser;
	private PostBusinessAddParser mPostBusinessAddParser;
	
	private ArrayList<StatesModel> mAllstatesArry = null;
	private ArrayList<GetHomePageCategoriesModel> mAllCategoriesArray = null;
	private ArrayList<CitiesModel> mAllCitiesArray = null;
	private ArrayList<SubCategoriesModel> mAllSubCategories = null;
	
	private ArrayAdapter<String> mStatesAdapter, mCategoriesAdapter, mCitiesAdapter, mSubCatagoryAdapter, mPaymentsAdapter;
	
	private String[] mStatesList, mCategoriesList, mNumberofpayments;
	
	private String[] mNumberofpayments30And36And27 = {"1", "2", "3", "4"},mNumberofpayments9 = {"1", "2", "3"}, 
					 mNumberofpayments75And54And60 = {"1", "2"},mNumberofpayments18 = {"1"};
	
	private ArrayList<String> mCitiesList_array, mSubCategoriesList_array;
	
	private boolean mStatesFound = false, mCategoryFound = false, choosePrice_flag = false;
	
	private String mState_code, mCategory_id, mNumberofpayments_Str , mSelectedCityName_Str, mNationalType, mTobePlaced,
				   mMasterCategory_id, mCity_id;
	
	private int final_paymentAmount = 0;
	
	private int mPaymentAmount;

	private String paymentResponce_Str = "";
	
	final private String mApiLoginId = "9AmH6s8Cg";
	final private String mTransactionKey = "4Hz9YjGg585M5m3g";
	
	private Result<Transaction> result;	
	
	RelativeLayout quick_parent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_advertisement_screen);
        initializeUI();

		mQuickAction = new QuickAction(this);
		
        mAllstatesArry = new ArrayList<StatesModel>();
        mAllCategoriesArray = new ArrayList<GetHomePageCategoriesModel>();
        mAllCitiesArray = new ArrayList<CitiesModel>();
        mAllSubCategories = new ArrayList<SubCategoriesModel>();
        mCitiesList_array = new ArrayList<String>();
        mSubCategoriesList_array = new ArrayList<String>();
        
        mNationalType = "1";
        mTobePlaced = "1";
        choosePrice_flag = true;
        
        mProgressDialog = ProgressDialog.show(AdvertiseActivity.this, "", "Please Wait....");
        new mAsyncStateFeedFetcher(new AdvertiseHandler()).start();
        
        setSubcategoriesSpinner();
		setNumberofPaymentsSpinner();
		gettingCitiesAndSubCategories();
		
    }

    
    // initializing ui here
	private void initializeUI() {
	
		mBTNSubmit = (Button)findViewById(R.id.home_advertisement_BTN_submit);
		
		mAgreeCheckBox = (CheckBox)findViewById(R.id.home_advertisement_CheckBox_agree);
		
		mRadio_Local = (RadioButton)findViewById(R.id.business_filter_RB_Local);
		mRadio_National = (RadioButton)findViewById(R.id.business_filter_RB_National);
		
		// ad Location TextViews
		
		mFeature_Txt = (TextView)findViewById(R.id.home_advertisement_screen_TXT_Featured);
		mTop4_Txt = (TextView)findViewById(R.id.home_advertisement_screen_TXT_Top4);
		mTop9_Txt = (TextView)findViewById(R.id.home_advertisement_screen_TXT_Top9);
		mGeneral_Txt = (TextView)findViewById(R.id.home_advertisement_screen_TXT_GeneralListing);
		mTermsAndCondition = (TextView)findViewById(R.id.home_advertisement_termsAndConditions);
		
		//ad location ImageViews
		
		mFeatured_Img = (ImageView)findViewById(R.id.home_advertisement_IMG_Featured);
		mTop4_Img = (ImageView)findViewById(R.id.home_advertisement_IMG_Top4);
		mTop9_Img = (ImageView)findViewById(R.id.home_advertisement_IMG_Top9);
		mGeneral_Img = (ImageView)findViewById(R.id.home_advertisement_IMG_GeneralListing);
		
		
		// spinners
		mState_Spinner = (Spinner)findViewById(R.id.home_advertisement_Spiner_State);
		mCity_Spinner = (Spinner)findViewById(R.id.home_advertisement_Spiner_City);
		mNumberOfPayments_Spinner = (Spinner)findViewById(R.id.home_advertisement_Spiner_Numberofpayments);
		mCategorySpinner = (Spinner)findViewById(R.id.home_advertisement_Spiner_Category);
		mSubCategorySpinner = (Spinner)findViewById(R.id.home_advertisement_Spiner_SubCategory);
		mNumberOfPayments_Spinner = (Spinner)findViewById(R.id.home_advertisement_Spiner_Numberofpayments);
		
		//Business user information
		
		mETBusinessName = (EditText)findViewById(R.id.home_advertisement_ETXT_BusinessName);
		mETPhoneNumber = (EditText)findViewById(R.id.home_advertisement_ETXT_BusinessPhone);
		mETFax = (EditText)findViewById(R.id.home_advertisement_ETXT_BusinessFax);
		mETEmail = (EditText)findViewById(R.id.home_advertisement_ETXT_BusinessEmail);

		mETBusinessAddress= (EditText)findViewById(R.id.home_advertisement_ETXT_BusinessAddress);
		
		//payment Information
		mETCcType = (EditText)findViewById(R.id.home_advertisement_ETXT_CCType);
		mETCardHolderName = (EditText)findViewById(R.id.home_advertisement_ETXT_Cardholdername);
		mETCcNumber = (EditText)findViewById(R.id.home_advertisement_ETXT_CCNumber);
		mETExpDate = (EditText)findViewById(R.id.home_advertisement_ETXT_ExpDate);
		mETCvv2Code = (EditText)findViewById(R.id.home_advertisement_ETXT_CVV2Code);

		mETZipCode = (EditText)findViewById(R.id.home_advertisement_ETXT_Zipcode);
		
		mBTNSubmit.setOnClickListener(this);
		
		mFeature_Txt.setOnClickListener(this);
		mFeature_Txt.setTextColor(Color.BLUE);
		mFeature_Txt.setBackgroundResource(R.drawable.blue_rect);
		mRadio_Local.setText("$900 / Yr");
		mRadio_National.setText("$3600 / Yr");
		
		mTop4_Txt.setOnClickListener(this);
		mTop9_Txt.setOnClickListener(this);
		mGeneral_Txt.setOnClickListener(this);
		
		mFeatured_Img.setOnClickListener(this);
		mTop4_Img.setOnClickListener(this);
		mTop9_Img.setOnClickListener(this);
		mGeneral_Img.setOnClickListener(this);
		
		/* radio button listener */
		mRadio_Local.setOnClickListener(this);
		mRadio_National.setOnClickListener(this);
		
		mState_Spinner.setOnItemSelectedListener(this);
		
		mAgreeCheckBox.setOnClickListener(this);
		mTermsAndCondition.setOnClickListener(this);
	}
		
	
	/**
	 *  getting cities and sub categories
	 */
	public void gettingCitiesAndSubCategories(){
		
		mCity_Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
					mSelectedCityName_Str = mAllCitiesArray.get(arg2).getCity_name();
					Log.v(getClass().getSimpleName(), "city name "+mSelectedCityName_Str);
					mCity_id = mAllCitiesArray.get(arg2).getCity_id();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mSubCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
					if (mAllSubCategories.size() > 0) {
						mCategory_id = mAllSubCategories.get(arg2).getSub_CategoryId();
						mMasterCategory_id = mAllSubCategories.get(arg2).getSub_MatsterCategoryId();
						Log.v(getClass().getSimpleName(), " sub categories id "+mCategory_id+" master id "+mMasterCategory_id);
					}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	/**
	 *  setting cities based on selected state.
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
		if (arg2 != 0) {
			
			mState_code = mAllstatesArry.get(arg2 - 1).getState_code();
			Log.v(getClass().getSimpleName(), " selected state code is "+mState_code+" arg2 "+arg2);
	        mProgressDialog = ProgressDialog.show(AdvertiseActivity.this, "", "Please Wait....");
	        mCitiesList_array.clear();
	    
	        try
	        {
				
				 new mAsynCityFeedFetcher(Urls.CITIES_BY_STATE, mState_code, new AdvertiseHandler()).start();
				
				 mCitiesAdapter = new ArrayAdapter<String>(AdvertiseActivity.this, 
							android.R.layout.simple_spinner_item, mCitiesList_array);
				 mCitiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				 mCity_Spinner.setAdapter(mCitiesAdapter);
				 
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
    /**
     *  setting the sub categories based on selected category id.
     */
    private void setSubcategoriesSpinner(){
    	 mCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

 			@Override
 			public void onItemSelected(AdapterView<?> arg0, View arg1,
 					int arg2, long arg3) {
 				
 				if (arg2 != 0) 
 				{
 					
 					mCategory_id = mAllCategoriesArray.get(arg2 - 1).getCategoryId();
 	 				mMasterCategory_id = mAllCategoriesArray.get(arg2 - 1).getMatsterCategoryId();
 	 		        mProgressDialog = ProgressDialog.show(AdvertiseActivity.this, "", "Please Wait....");
 	 				Log.v(getClass().getSimpleName(), " selected category id "+mCategory_id+" master id "+mMasterCategory_id);
 	 				mSubCategoriesList_array.clear();
 	 				
 	 				try 
 	 				{
 	 					
 	 					new mAsynSubCategiesFeedFetcher(
 	 							Urls.SUBCATEGORIES_BY_CATEGORYID, 
 	 							mCategory_id, 
 	 							new AdvertiseHandler()).start();
 	 					
 	 					mSubCatagoryAdapter = new ArrayAdapter<String>(AdvertiseActivity.this, 
 	 							android.R.layout.simple_spinner_item, mSubCategoriesList_array);
 	 					
 	 					mSubCatagoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 	 					mSubCategorySpinner.setAdapter(mSubCatagoryAdapter);
 	 					
 	 				} catch (UnsupportedEncodingException e) {
 	 					// TODO Auto-generated catch block
 	 					e.printStackTrace();
 	 				} 
 				}

 			}

 			@Override
 			public void onNothingSelected(AdapterView<?> arg0) {
 				// TODO Auto-generated method stub
 				
 			}
 		});
    }
	
    
    /**
     * set the states and master categories to the spinner.
     */
    
    private void setSpinnerItem(){
    	
    	mStatesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStatesList);
        mStatesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mState_Spinner.setAdapter(mStatesAdapter);
        
        mCategoriesAdapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mCategoriesList);
        mCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(mCategoriesAdapter);
    }
    
	public void onClick(View v) {
	
		switch (v.getId()) {
		
		case R.id.business_user_BTN_done:
				
				break;
				
		case R.id.home_advertisement_BTN_submit:
			 if(validateFields().equalsIgnoreCase(" ")){
				 
				 Log.v(getClass().getSimpleName(), "city"+mSelectedCityName_Str);
				 try
				 {
					 
					 if (mState_code != null)
					 {
						 Log.v(getClass().getSimpleName(), " valid business "+Urls.VALIDATE_BUSINESS_URL+" name "+mETBusinessName.getText().toString());
						
						 new mAsynValidateBusiness(Urls.VALIDATE_BUSINESS_URL, mETBusinessName.getText().toString(), 
								 mETBusinessAddress.getText().toString(), " ", mSelectedCityName_Str, mState_code, mCategory_id, 
								 mNationalType, new AdvertiseHandler()).start();
						 
					 }else {
						 
						 alertDialog("Please Check Selected State");
						
					 }
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			 }else
				 alertDialog(validateFields());
			  break;
			  
		case R.id.home_advertisement_screen_TXT_Featured:
			
			mFeature_Txt.setTextColor(Color.BLUE);
			mTop4_Txt.setTextColor(Color.BLACK);
			mTop9_Txt.setTextColor(Color.BLACK);
			mGeneral_Txt.setTextColor(Color.BLACK);
			mFeature_Txt.setBackgroundResource(R.drawable.blue_rect);
			mTop9_Txt.setBackgroundResource(Color.TRANSPARENT);
			mGeneral_Txt.setBackgroundResource(Color.TRANSPARENT);
			mTop4_Txt.setBackgroundResource(Color.TRANSPARENT);
			mRadio_Local.setText("$900 / Yr");
			mRadio_National.setText("$3600 / Yr");
			setNumberofPaymentsSpinner();
			mTobePlaced = "1";
			
			break;
			
		case R.id.home_advertisement_screen_TXT_Top4:
			
			mFeature_Txt.setTextColor(Color.BLACK);
			mTop4_Txt.setTextColor(Color.BLUE);
			mTop9_Txt.setTextColor(Color.BLACK);
			mGeneral_Txt.setTextColor(Color.BLACK);
			mTop4_Txt.setBackgroundResource(R.drawable.blue_rect);
			mTop9_Txt.setBackgroundResource(Color.TRANSPARENT);
			mGeneral_Txt.setBackgroundResource(Color.TRANSPARENT);
			mFeature_Txt.setBackgroundResource(Color.TRANSPARENT);
			mRadio_Local.setText("$750 / Yr");
			mRadio_National.setText("$3000 / Yr");
			setNumberofPaymentsSpinner();
			mTobePlaced = "2";
			
			break;
			
		case R.id.home_advertisement_screen_TXT_Top9:
			
			mFeature_Txt.setTextColor(Color.BLACK);
			mTop4_Txt.setTextColor(Color.BLACK);
			mTop9_Txt.setTextColor(Color.BLUE);
			mGeneral_Txt.setTextColor(Color.BLACK);
			mTop9_Txt.setBackgroundResource(R.drawable.blue_rect);
			mTop4_Txt.setBackgroundResource(Color.TRANSPARENT);
			mFeature_Txt.setBackgroundResource(Color.TRANSPARENT);
			mGeneral_Txt.setBackgroundResource(Color.TRANSPARENT);
			mRadio_Local.setText("$540 / Yr");
			mRadio_National.setText("$2700 / Yr");
			setNumberofPaymentsSpinner();
			mTobePlaced = "3";
			
			break;
			
		case R.id.home_advertisement_screen_TXT_GeneralListing:
			
			mFeature_Txt.setTextColor(Color.BLACK);
			mTop4_Txt.setTextColor(Color.BLACK);
			mTop9_Txt.setTextColor(Color.BLACK);
			mGeneral_Txt.setTextColor(Color.BLUE);
			mGeneral_Txt.setBackgroundResource(R.drawable.blue_rect);
			mTop9_Txt.setBackgroundResource(Color.TRANSPARENT);
			mTop4_Txt.setBackgroundResource(Color.TRANSPARENT);
			mFeature_Txt.setBackgroundResource(Color.TRANSPARENT);
			mRadio_Local.setText("$180 / Yr");
			mRadio_National.setText("$600 / Yr");
			setNumberofPaymentsSpinner();
			mTobePlaced = "4";
			
			break;
			
		case R.id.business_filter_RB_Local:
			
			mChoosePrice = (String) mRadio_Local.getText();
			Log.v(getClass().getSimpleName(), "selected rb amount local "+mChoosePrice);
			choosePrice_flag = true;
			setNumberofPaymentsSpinner();
			mNationalType = "1";
			
			break;
			
		case R.id.business_filter_RB_National:
			
			mChoosePrice = (String) mRadio_National.getText();
			Log.v(getClass().getSimpleName(), "selected rb amount national "+mChoosePrice);
			choosePrice_flag = false;
			setNumberofPaymentsSpinner();
			mNationalType = "2";
			
			break;
			
		case R.id.home_advertisement_IMG_Featured:
		
			mQuickAction.show(mFeatured_Img, 1, getResources().getString(R.string.featured));
		
			break;
			
		case R.id.home_advertisement_IMG_Top4:
			
			mQuickAction.show(mTop4_Img, 1, getResources().getString(R.string.top4));

			break;
			
		case R.id.home_advertisement_IMG_Top9:
			
			mQuickAction.show(mTop9_Img, 1, getResources().getString(R.string.top9));
			
			break;
			
		case R.id.home_advertisement_IMG_GeneralListing:
			
			mQuickAction.show(mGeneral_Img, 2, getResources().getString(R.string.general));
			
			break;
			
		case R.id.home_advertisement_CheckBox_agree:
			
			if (mAgreeCheckBox.isChecked()) {
				
				Log.v("check box ", "check box");
				mBTNSubmit.setClickable(true);
				mBTNSubmit.setBackgroundResource(R.drawable.btn_icon_submit_long);
				
			}else{
				
				mBTNSubmit.setClickable(false);
				mBTNSubmit.setBackgroundResource(R.drawable.btn_icon_submit_long_disable);

			}
			
		break;
	
		case R.id.home_advertisement_termsAndConditions:
			
			startActivity(new Intent(AdvertiseActivity.this, TermsAndConditionsActivity.class));
	
		}
	}

	
	/**
	 *  set Number of payments based on price
	 */
   private void setNumberofPaymentsSpinner(){
	
	   if (choosePrice_flag == true) 
			mChoosePrice = (String) mRadio_Local.getText();
	   else
		    mChoosePrice = (String) mRadio_National.getText();
	   
	   int index = mChoosePrice.indexOf("/");

	   String substring = mChoosePrice.substring(1, index); 
	   
	   Log.v(getClass().getSimpleName(), "choose text "+mChoosePrice+" choose price index "+substring);
	   
	   if (mChoosePrice.equalsIgnoreCase("$3000 / Yr") || mChoosePrice.equalsIgnoreCase("$3600 / Yr") 
			                                           || mChoosePrice.equalsIgnoreCase("$2700 / Yr")) {
		   mNumberofpayments = new String[mNumberofpayments30And36And27.length];
		   System.arraycopy(mNumberofpayments30And36And27, 0, mNumberofpayments, 0, mNumberofpayments30And36And27.length);
		   
	   }
	   
	   if (mChoosePrice.equalsIgnoreCase("$750 / Yr") || mChoosePrice.equalsIgnoreCase("$540 / Yr") 
			   										  || mChoosePrice.equalsIgnoreCase("$600 / Yr")) {
		   mNumberofpayments = new String[mNumberofpayments75And54And60.length];
		   System.arraycopy(mNumberofpayments75And54And60, 0, mNumberofpayments, 0, mNumberofpayments75And54And60.length);
		   
       }
	   
	   if (mChoosePrice.equalsIgnoreCase("$900 / Yr")) {
		   
		   mNumberofpayments = new String[mNumberofpayments9.length];
		   System.arraycopy(mNumberofpayments9, 0, mNumberofpayments, 0, mNumberofpayments9.length);
		   
	   }
	   
	   if (mChoosePrice.equalsIgnoreCase("$180 / Yr")) {
		   
		   mNumberofpayments = new String[mNumberofpayments18.length];
		   System.arraycopy(mNumberofpayments18, 0, mNumberofpayments, 0, mNumberofpayments18.length);
		   
	   }
	   
	   mPaymentsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mNumberofpayments);
	   mPaymentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   mNumberOfPayments_Spinner.setAdapter(mPaymentsAdapter);
	 
	   mNumberOfPayments_Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				mNumberofpayments_Str = mNumberofpayments[arg2];
				
			}
	
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
   }
	
   /**
    * validating the input parameters.
    * @return a string  message.
    */
	private String validateFields(){
		
		if(mETBusinessName.getText().toString().length() == 0)
			
			return  "Please enter business name";
		
		
		if(mETPhoneNumber.getText().toString().length() < 10)
			
			return "Please enter valid phone number";
		
		if(mETEmail.getText().toString().length() == 0 || !emailvalidator(mETEmail.getText().toString()))
			
			return "Please enter valid email address";
		
		if(mETBusinessAddress.getText().toString().length() == 0)
			
			return  "Please enter valid address";
		
		if(mETZipCode.getText().toString().length() == 0 || mETZipCode.getText().toString().length() > 5)
			
			return "Please enter valid zip code";
		
		if(mETCcType.getText().toString().length() == 0)
			 
			return  "Please enter Cc Type";
		
			
		if(mETCardHolderName.getText().toString().length() == 0)
						
			return  "Please enter Card Holder name";


		if(mETCcNumber.getText().toString().length() < 13 || mETCcNumber.getText().toString().length() > 16)
			
			return "Credit card number in between 13 - 16 digits";
		
		
		if(mETExpDate.getText().toString().length() == 0)
			
			return  "Please enter valid experie date";
		
		
		if(mETCvv2Code.getText().toString().length() != 3)
			
			return "Cvv should have 3 digits";
		
		
		if(mETPhoneNumber.getText().toString().length() < 10)
			
			return "Please enter valid phone number";
					
		return " ";
																				
		
	}
	
	
	
	/**
	 *  validating the user email id
	 * @param email
	 * @return true if valid else returns false
	 */
	public Boolean emailvalidator(String email) {
	      
		  Pattern emailPattern = Pattern
	               .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
	                       + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
	                       + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
	       Matcher matcher = emailPattern.matcher(email);
	       return matcher.matches();

	   }
	
	/**
	 *  Parsing states and categories
	 * @author android
	 *
	 */
	public class mAsyncStateFeedFetcher extends Thread{
		
		Handler handler;
		public mAsyncStateFeedFetcher(Handler handler){
			
			this.handler = handler;
			
			String states_url = Urls.STATES_URL;
			
			String categories_url = Urls.BASE_URL+"GetMastercategores";
			
			Log.v(getClass().getSimpleName(), "states url is "+states_url);
			Log.v(getClass().getSimpleName(), " categories url "+categories_url);
			
			mCategoriesParser = new CategoriesListParser(categories_url, handler);
			mStatesFeedParser = new StatesFeedParser(states_url, handler);			
		
		}
		
		@Override
		public void run() {
			try
			{
				mStatesFeedParser.parser();
				mCategoriesParser.parser();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.run();
		}
	}
	
	SetDataHandler mSetDataHandler = new SetDataHandler();
	
	class SetDataHandler extends Handler {
		public void handleMessage(android.os.Message msg) {
			
			if (mCategoryFound == true && mStatesFound == true) {
				
				mProgressDialog.dismiss();
				setSpinnerItem();
			
			}
			
		}
	}
	
	/**
	 *  Handler for dealing all the parsers.
	 * @author android
	 *
	 */
	class AdvertiseHandler extends Handler{
		
		public void handleMessage(android.os.Message msg){
			

			if (msg.what == FOUND_RESULT) {
				
				mStatesFound = true;
				mAllstatesArry = (ArrayList<StatesModel>) msg.getData().getSerializable("statesFeeds");
				mStatesList = new String[mAllstatesArry.size() + 1];
				
				for (int iCtr = 0; iCtr < mAllstatesArry.size() + 1; iCtr++) {
					
					if (iCtr == 0) 
						mStatesList[0] = "select state";
					else
					mStatesList[iCtr] = mAllstatesArry.get(iCtr - 1).getState_name();
				}
				
				mSetDataHandler.sendEmptyMessage(0);
				
			}else if(msg.what == FOUND_CATEGORIES) {
				
				mCategoryFound = true;
				mAllCategoriesArray = (ArrayList<GetHomePageCategoriesModel>) msg.getData().getSerializable("categoriesfeed");
				mCategoriesList = new String[mAllCategoriesArray.size() + 1];
				mProgressDialog.dismiss();
				
				for (int iCtr = 0; iCtr < mAllCategoriesArray.size() + 1; iCtr++) {
					
					if (iCtr == 0) 
						mCategoriesList[0] = "select category";
					else
						mCategoriesList[iCtr] = mAllCategoriesArray.get(iCtr - 1).getCategoryName();
					
				}
				
				mSetDataHandler.sendEmptyMessage(0);

			}else if (msg.what == FOUND_CITIES) {
				
				mAllCitiesArray = (ArrayList<CitiesModel>) msg.getData().getSerializable("citiesFeeds");
				Log.v(getClass().getSimpleName(), " cities sizze "+mAllCitiesArray.size());
				mProgressDialog.dismiss();
				
				for (int iCtr = 0; iCtr < mAllCitiesArray.size(); iCtr++) {
					mCitiesAdapter.add(mAllCitiesArray.get(iCtr).getCity_name());
				}
				
			}else if (msg.what == FOUND_SUB_CATEGARY) {
				
				mAllSubCategories = (ArrayList<SubCategoriesModel>) msg.getData().getSerializable("subcategoriesfeed");
				mProgressDialog.dismiss();
				Log.v(getClass().getSimpleName(), " mAllSubCategories sizze "+mAllSubCategories.size());
				
				for (int iCtr = 0; iCtr < mAllSubCategories.size(); iCtr++) {
					mSubCatagoryAdapter.add(mAllSubCategories.get(iCtr).getSub_CategoryName());
				}
				
			}else if (msg.what == FOUND_BUSINESS) {
				
				String responce_flag = (String) msg.getData().getSerializable("flag");
				String responce_msg = (String) msg.getData().getSerializable("responceData");
				
				Log.v(getClass().getSimpleName(), "Server responce flag "+responce_flag+" responce message "+responce_msg);
				
				if (responce_flag.equalsIgnoreCase("True")) {
					
					Log.v(getClass().getSimpleName(), "creditCardNumber"+mETCcNumber.getText().toString()+"cardHolderName"+ mETCardHolderName.getText().toString()+
							"expirationDate"+mETExpDate.getText().toString()+"cvvNumber"+mETCvv2Code.getText().toString()+
							"email"+mETEmail.getText().toString());
	
					new paymentAsync().execute(); 
					
				}else if (responce_flag.equalsIgnoreCase("False")) {
					
					new AlertDialogMsg(AdvertiseActivity.this, responce_msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					}).create().show();
					
				}
				
				
			}else if (msg.what == BUSINESS_POST_SUCCESS) {
				
				mProgressDialog.dismiss();
				String businesspost_responce = (String) msg.getData().getSerializable("businesspost_responce_flag");
				String businesspost_responce_messsage = (String) msg.getData().getSerializable("businesspost_responce_messsage");
				
				Log.v(getClass().getSimpleName(), "Server responce flag "+businesspost_responce+" responce message "+businesspost_responce_messsage);
				
				if (businesspost_responce.equalsIgnoreCase("True")) {
					new AlertDialogMsg(AdvertiseActivity.this, businesspost_responce_messsage).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					}).create().show();
				}else if (businesspost_responce.equalsIgnoreCase("False")) {
					
				}
				
			}else if (msg.what == NO_RESULT) {
				
				mProgressDialog.dismiss();
				mSubCategorySpinner.setContentDescription("No Categories");
				mSubCatagoryAdapter.add("No Sub Catagories");
				
				new AlertDialogMsg(AdvertiseActivity.this, "No Data Found").setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				}).create().show();
				
			}else if (msg.what == ERROR_WHILE_GETTING_RESULT) {
				String errormessage = "Error While connecting to the server or server not responding. please try again.";
				mProgressDialog.dismiss();

				new AlertDialogMsg(AdvertiseActivity.this, errormessage).setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).create().show();
			}
			
		}
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}
	
	/**
	 *  Thread for parsing the cities.
	 * @author android
	 *
	 */
	public class mAsynCityFeedFetcher extends Thread{
			
			Handler handler;
			public mAsynCityFeedFetcher(String url, String state_code,Handler handler) throws UnsupportedEncodingException{
				this.handler = handler;
				
				String cities_by_state_url = String.format(url, URLEncoder.encode(state_code, "UTF-8"), handler);
				Log.v(getClass().getSimpleName(), "cities url "+cities_by_state_url);

				mCitiesFeedParser = new CitiesFeedParser(cities_by_state_url, handler);
			}
			
			
			@Override
			public void run() {
				try
				{
					mCitiesFeedParser.parser();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				super.run();
			}
	}

	/**
	 * Thread for parsing sub categories.
	 * @author android
	 *
	 */
	public class mAsynSubCategiesFeedFetcher extends Thread{
		
		Handler handler;
		public mAsynSubCategiesFeedFetcher(String url, String category_id,Handler handler) throws UnsupportedEncodingException{
			this.handler = handler;
			
			String sub_categories_url = String.format(url, URLEncoder.encode(category_id, "UTF-8"), handler);
			Log.v(getClass().getSimpleName(), "sub_categories_url "+sub_categories_url);

			mSubCategoriesParser = new SubCategoriesParser(sub_categories_url, handler);
		}
		
		
		@Override
		public void run() {
			try
			{
				mSubCategoriesParser.parser();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.run();
		}
		
	}
	
	/**
	 * Thread for validating business
	 * @author android
	 *
	 */
	public class mAsynValidateBusiness extends Thread{
		
		Handler handler;
		String validate_business_url = "";
		public mAsynValidateBusiness(String url, String Business_Title, String Address_Line1, String Address_Line2, 
				String CityName, String StateCode, String CategoryID, 
				String NationalType,Handler handler) 	throws UnsupportedEncodingException{
			this.handler = handler;
			
			if (CategoryID != null) 
			{
				
				validate_business_url = String.format(url, URLEncoder.encode(Business_Title, "UTF-8"),
				URLEncoder.encode(Address_Line1, "UTF-8"),URLEncoder.encode(Address_Line2, "UTF-8"),
				URLEncoder.encode(CityName, "UTF-8"),URLEncoder.encode(StateCode, "UTF-8"),
				URLEncoder.encode(CategoryID, "UTF-8"),URLEncoder.encode(NationalType, "UTF-8"),handler);
		
				Log.v(getClass().getSimpleName(), "validate_business_url "+validate_business_url);
				mValidateBusinessParser = new ValidateBusinessParser(validate_business_url, handler);
			}else{
				alertDialog("Please Check Selected Category");
			}
		
		}
		
		
		@Override
		public void run() {
			try
			{
				mValidateBusinessParser.getServerResponce();
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.run();
		}
		
	}

	/**
	 *  Final Posting of the business add to server.
	 * @author android
	 *
	 */
	public class mAsynBusinessPost extends Thread{
		
		Handler handler;
		public mAsynBusinessPost(String url, String Business_Title, String Tobeplaced,String UserID, String Category_Id, 
				String CategoryMasterId, String Location, String CityID, 
				String Zipcode,String Business_Phone,String Fax,String Email,String Lat,
				String Long,String PaymentResponse,String Noofpayments,String Billdate,
				String billcycletype,String PayAmount,String nationaltype,String PostFrom,Handler handler) 	throws UnsupportedEncodingException{
			this.handler = handler;
			
		
			String post_business_url = String.format(url, URLEncoder.encode(Business_Title, "UTF-8"),
					URLEncoder.encode(Tobeplaced, "UTF-8"),URLEncoder.encode(UserID, "UTF-8"),
					URLEncoder.encode(Category_Id, "UTF-8"),URLEncoder.encode(CategoryMasterId, "UTF-8"),
					URLEncoder.encode(Location, "UTF-8"),
					URLEncoder.encode(CityID, "UTF-8"),
					URLEncoder.encode(Zipcode, "UTF-8"),
					URLEncoder.encode(Business_Phone, "UTF-8"),
					URLEncoder.encode(Fax, "UTF-8"),
					URLEncoder.encode(Email, "UTF-8"),
					URLEncoder.encode(Lat, "UTF-8"),URLEncoder.encode(Long, "UTF-8"),
					URLEncoder.encode(PaymentResponse, "UTF-8"),URLEncoder.encode(Noofpayments, "UTF-8"),
					URLEncoder.encode(Billdate, "UTF-8"),URLEncoder.encode(billcycletype, "UTF-8"),
					URLEncoder.encode(PayAmount, "UTF-8"),URLEncoder.encode(nationaltype, "UTF-8"),
					URLEncoder.encode(PostFrom, "UTF-8"),handler);
			
			
			
			Log.v(getClass().getSimpleName(), "validate_business_url "+post_business_url);

			mPostBusinessAddParser = new PostBusinessAddParser(post_business_url, handler);
		}
		
		
		@Override
		public void run() {
			try
			{
				mPostBusinessAddParser.getBusinessResponce();
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.run();
		}
		
	}

	/**
	 *  posting business ads to the server.
	 */
	private void postBusinessAdd()
	{
		
        mProgressDialog = ProgressDialog.show(AdvertiseActivity.this, "", "Please Wait....");

	    Log.v(getClass().getSimpleName(), "final payment "+""+final_paymentAmount+"mETBusinessPhone "+mETPhoneNumber.getText().toString()+"mETEmail "+mETEmail.getText().toString());
	    
	    int paymentAmount = caluculateAmount();
	    
		Calendar calender = Calendar.getInstance();
		int mDay = calender.get(Calendar.DAY_OF_MONTH);
		
		try 
		{
			
			new mAsynBusinessPost(Urls.POST_BUSINESS_ADD_URL, mETBusinessName.getText().toString(), mTobePlaced, "54", 
				mCategory_id, mMasterCategory_id, mETBusinessAddress.getText().toString(), mCity_id, mETZipCode.getText().toString(), 
				mETPhoneNumber.getText().toString(),mETFax.getText().toString(),mETEmail.getText().toString(), CurrentLocationData.LATITUDE, 
				CurrentLocationData.LONGITUDE, "valid", mNumberofpayments_Str, Integer.toString(mDay), "", Integer.toString(paymentAmount), mNationalType, "2",
				new AdvertiseHandler()).start();
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**
	 *  Calculating amount for the advertisement based on number of payments.
	 * @return  final payment amount as integer.
	 */
	private int caluculateAmount(){

		if (choosePrice_flag == true) 
			mChoosePrice = (String) mRadio_Local.getText();
		else
			mChoosePrice = (String) mRadio_National.getText();
	   
	    int index = mChoosePrice.indexOf(" ");

	    String payment_amount = mChoosePrice.substring(1, index); 
	
	    int changed_payment = Integer.parseInt(payment_amount);

	    if (mNumberofpayments_Str.equalsIgnoreCase("1")) 
	    	final_paymentAmount = changed_payment/1;
		
	    
	    if (mNumberofpayments_Str.equalsIgnoreCase("2")) 
	    	final_paymentAmount = changed_payment/2;
	    
	    if (mNumberofpayments_Str.equalsIgnoreCase("3")) 
	    	final_paymentAmount = changed_payment/3;
	    
	    if (mNumberofpayments_Str.equalsIgnoreCase("4")) 
	    	final_paymentAmount = changed_payment/4;
	    
	    return final_paymentAmount;
	}
	
	
	/**
	 *  Async task  Authorized.Net payment.
	 */
	class  paymentAsync extends AsyncTask<Void, Void, Void>{

        ProgressDialog mProgressDialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
	        mProgressDialog = ProgressDialog.show(AdvertiseActivity.this, "", "Please Wait......");
		}
		
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			authorizePayment();
			return null;
		}

		@Override
		protected void onPostExecute(Void result1) {
			// TODO Auto-generated method stub
			String transactionId = "";
			super.onPostExecute(result1);
			mProgressDialog.dismiss();
			
			if (result.isApproved()) {
				paymentResponce_Str = "success";
				transactionId = "";
//				Toast.makeText(AdvertiseActivity.this, "Approved: "+result.getResponseText(), Toast.LENGTH_SHORT).show();
				transactionId = AdvertiseActivity.this.result.getTarget().getTransactionId();
				postBusinessAdd(); // posting business after transaction..
			}else if (result.isDeclined()) {

				transactionId = "";
				paymentResponce_Str = AdvertiseActivity.this.result.getResponseText();
//				Toast.makeText(AdvertiseActivity.this, "Declined: "+result.getResponseText(), Toast.LENGTH_SHORT).show();
				transactionId = AdvertiseActivity.this.result.getTarget().getTransactionId();
				alertDialog(paymentResponce_Str);
				
			}else {

				transactionId = "";
				paymentResponce_Str = AdvertiseActivity.this.result.getResponseText();
//				Toast.makeText(AdvertiseActivity.this, "error: "+result.getResponseText(), Toast.LENGTH_SHORT).show();
				transactionId = AdvertiseActivity.this.result.getTarget().getTransactionId();
				alertDialog(paymentResponce_Str);
				
			}
		}
	}
	
	/**
	 *  method for payment transaction in authorized .net
	 */
	private void authorizePayment(){
		
		Merchant merchant = Merchant.createMerchant(net.authorize.Environment.SANDBOX, mApiLoginId, mTransactionKey);

		mPaymentAmount = caluculateAmount();
		String amount = mETExpDate.getText().toString();
		Log.v(getClass().getSimpleName(), " date "+mETExpDate.toString());
		String[] arrDate = amount.split("/");
		String mExpirationMonth = arrDate[0];
		String mExpirationYear = arrDate[1];
		
		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCreditCardNumber(mETCcNumber.getText().toString());
		creditCard.setExpirationMonth(mExpirationMonth);
		creditCard.setExpirationYear(mExpirationYear);
		
		EmailReceipt.createEmailReceipt();
		
		Transaction captureTransaction = merchant.createAIMTransaction(TransactionType.AUTH_CAPTURE,new BigDecimal(mPaymentAmount));
		captureTransaction.setCreditCard(creditCard);
		
		result = (Result<Transaction>) merchant.postTransaction(captureTransaction);
		
	}
	
	/**
	 *  alert for showing message
	 * @param msg
	 */
	private void alertDialog(String msg){
		
		new AlertDialogMsg(AdvertiseActivity.this, msg).setPositiveButton("ok", 
				new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
			
		}).create().show();
	}
	
}