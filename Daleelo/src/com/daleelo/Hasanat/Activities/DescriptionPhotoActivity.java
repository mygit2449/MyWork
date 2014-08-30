package com.daleelo.Hasanat.Activities;

import com.daleelo.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class DescriptionPhotoActivity extends Activity{
	
	
	private Button mBtn_prev;
	private Button mBtn_next;
	private ImageView mImg_Photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.description_photo);
	}
	
	private void initializeUI(){
		mBtn_next = (Button)findViewById(R.id.description_photo_BTN_next);
		mBtn_prev = (Button)findViewById(R.id.description_photo_BTN_prev);
		
		mImg_Photo = (ImageView)findViewById(R.id.description_IMG_photo);
	}

}
