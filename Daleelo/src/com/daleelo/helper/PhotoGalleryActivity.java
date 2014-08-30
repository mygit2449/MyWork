package com.daleelo.helper;

import java.util.ArrayList;

import com.daleelo.R;
import com.daleelo.Utilities.Urls;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class PhotoGalleryActivity extends Activity implements OnClickListener{
	
	
	private Button mBtn_prev;
	private Button mBtn_next;
	private ImageView mImg_Photo;
	
	private ArrayList<String> imageArray;	
	private int mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.description_photo);
		
		initializeUI();
			
	}
	
	
	
	private void initializeUI(){
		
		mBtn_next = (Button)findViewById(R.id.description_photo_BTN_next);
		mBtn_prev = (Button)findViewById(R.id.description_photo_BTN_prev);		
		mImg_Photo = (ImageView)findViewById(R.id.description_IMG_photo);

		try {
			
			imageArray = (ArrayList<String>) getIntent().getSerializableExtra("images");
			mPosition = getIntent().getIntExtra("position", 0 );
			
			if(imageArray.size()>0){
				
				setImage();
			
				if(mPosition == (imageArray.size()-1))
					mBtn_next.setBackgroundResource(R.drawable.btn_icon_next_pic_disable);
				
				if(mPosition == 0)
					mBtn_prev.setBackgroundResource(R.drawable.btn_icon_previous_pic_disable);		
				
				mBtn_next.setOnClickListener(this);		
				mBtn_prev.setOnClickListener(this);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		switch(v.getId()){
		
			case R.id.description_photo_BTN_prev:
			
				mPosition--;
				
				if(mPosition >= 0){				
					
					
					if(mPosition == 0)
						mBtn_prev.setBackgroundResource(R.drawable.btn_icon_previous_pic_disable);
					else
						mBtn_prev.setBackgroundResource(R.drawable.btn_icon_previous_pic);
					
					mBtn_next.setBackgroundResource(R.drawable.btn_icon_next_pic);			
					
					setImage();
					
				}else{
					
					mPosition++;
					
				}				
			
			break;
			
		case R.id.description_photo_BTN_next:
			
			mPosition++;
			
			if(mPosition < (imageArray.size())){
				
							
				if(mPosition == (imageArray.size()-1))
					mBtn_next.setBackgroundResource(R.drawable.btn_icon_next_pic_disable);
				else
					mBtn_next.setBackgroundResource(R.drawable.btn_icon_next_pic);
				
				mBtn_prev.setBackgroundResource(R.drawable.btn_icon_previous_pic);
				
				setImage();
								
			}else{
				
				mPosition--;
			}
			
			break;
		}
		
	}
	
	
	private void setImage(){
		
		if(getIntent().getStringExtra("from").equalsIgnoreCase("class")){
		
		new ImageDownloader().download(String.format(Urls.CI_IMG_URL,imageArray.get(mPosition)), mImg_Photo);
		
		}
	}
	
}
