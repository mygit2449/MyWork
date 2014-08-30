package com.daleelo.More.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.daleelo.R;

public class PersonalProfileDetails extends Activity{

	EditText mETFirstName, mETLastName, mETEmailId, mETPhoneNo, mETLocation, mETGender, mETDob;
	ImageView mIMGAddUser;
	Button mBTNSubmit;
	CheckBox mCheckBox;
	
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_personal_profile_details);
		initializeUI();
		
		
	}

	private void initializeUI() {
	
		mETFirstName = (EditText)findViewById(R.id.more_personal_profile_details_ETXT_firstname);
		mETLastName = (EditText)findViewById(R.id.more_personal_profile_details_ETXT_lastname);
		
		mETEmailId = (EditText)findViewById(R.id.more_personal_profile_details_ETXT_emailId);
		mETPhoneNo = (EditText)findViewById(R.id.more_personal_profile_details_ETXT_phoneNumber);
		mETLocation = (EditText)findViewById(R.id.more_personal_profile_details_ETXT_Location);
		mETGender = (EditText)findViewById(R.id.more_personal_profile_details_ETXT_Gender);
		mETDob = (EditText)findViewById(R.id.more_personal_profile_details_ETXT_DOB);
		
		mIMGAddUser = (ImageView)findViewById(R.id.more_personal_profile_details_Img_userphoto);
				
		mBTNSubmit = (Button)findViewById(R.id.personalProfile_BTN_submit);
		
		mCheckBox = (CheckBox)findViewById(R.id.personalProfile_checkbox);
		
	}


}
