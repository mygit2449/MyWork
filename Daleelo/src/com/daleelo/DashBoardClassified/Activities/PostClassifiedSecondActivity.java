package com.daleelo.DashBoardClassified.Activities;



import java.io.File;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.DashBoardClassified.Model.GetClassifiedItemsModel;
import com.daleelo.DashBoardClassified.Parser.ClassifiedDeleteImageParser;
import com.daleelo.DashBoardClassified.Parser.PostClassifiedParser;
import com.daleelo.Dialog.AddPhotoDialog;
import com.daleelo.Utilities.Urls;
import com.daleelo.helper.ImageDownloader;
import com.daleelo.helper.MediaHelper;

public class PostClassifiedSecondActivity extends Activity implements OnClickListener {


	private static final int REQUEST_PICTURE_CLICK = 1;
	private static final int REQUEST_PICTURE_GALLERY = 2;
	
	private EditText mTitle, mDesp, mPrice;
	private Button btnDone, btnBrowser;
	public SharedPreferences sharedpreference;
	private CheckBox mOBO;
	private LinearLayout mLinPictures;
	private ArrayList<View> myViewAdd;
	private ArrayList<String> myImagPath, uploadImgPath, delClassifedImg;
	private String mCbOBO = "false", mClassifiedId = "0";
	ProgressDialog mProgressDialog;
	GetClassifiedItemsModel mGetClassifiedItemsModel;

	DecimalFormat desimalFormat = new DecimalFormat("##0");
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_classified_post_second_screen);
		 initializeUI() ;
		 
	}

	

	private void initializeUI() {
		
		sharedpreference= getSharedPreferences("userlogin",MODE_PRIVATE);
		
		btnDone = (Button)findViewById(R.id.classified_post_BTN_done);
		btnBrowser = (Button)findViewById(R.id.classified_post_BTN_browse);
		
		mTitle = (EditText)findViewById(R.id.classified_post_ET_title);
		mDesp = (EditText)findViewById(R.id.classified_post_ET_desp);
		mPrice = (EditText)findViewById(R.id.classified_post_ET_price);
		
		mOBO = (CheckBox)findViewById(R.id.classified_post_CB_obo);
		mOBO.setChecked(false);
		
		mLinPictures = (LinearLayout)findViewById(R.id.classified_post_LL_picture);
		
		myViewAdd = new ArrayList<View>();
		myImagPath = new ArrayList<String>();
		uploadImgPath = new ArrayList<String>();
		delClassifedImg = new ArrayList<String>();
		
		setExtraData();
		
		btnBrowser.setOnClickListener(this);
		btnDone.setOnClickListener(this);
		
		mOBO.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	        public void onCheckedChanged(CompoundButton cb, boolean isChecked) {            
	            if(cb.isChecked()) {                    
	                // action
	            	mCbOBO = "false";
	            }
	            else if(isChecked==false) {
	                // action
	            	mCbOBO = "true";
	            }
	        }           
	    });
				
		
	}

	String mCategory, mLocation, mLatitude,	mLongitude,	mCity, mSection, mType;
	
	private void setExtraData(){		
		
		if(getIntent().getExtras().getString("from").equalsIgnoreCase("edit")){		
			
			
			mGetClassifiedItemsModel = (GetClassifiedItemsModel) getIntent().getExtras().getSerializable("data");
			
			mCategory = mGetClassifiedItemsModel.getClassifiedCategory();
			mLocation = mGetClassifiedItemsModel.getLocation();
			mLatitude = mGetClassifiedItemsModel.getLatitude();
			mLongitude = mGetClassifiedItemsModel.getLongitude();
			mCity = mGetClassifiedItemsModel.getCityname();
			mSection = mGetClassifiedItemsModel.getClassifiedSection();
			mType = mGetClassifiedItemsModel.getCSType();
			
			
			mTitle.setText(mGetClassifiedItemsModel.getClassifiedTitle());
			mDesp.setText(mGetClassifiedItemsModel.getClassifiedInfo());
			
			double tempStr = Double.parseDouble(mGetClassifiedItemsModel.getPrice());
        	
			mPrice.setText(desimalFormat.format(tempStr));
			
			if(mGetClassifiedItemsModel.getOBO().equalsIgnoreCase("True")){
				
				mOBO.setChecked(true);
				mCbOBO = "true";
				
			}else{
				
				mOBO.setChecked(false);
				mCbOBO = "false";
			}
								
			if(mGetClassifiedItemsModel.getClassifiedImages().size()>0){
				for(int i = 0; i < mGetClassifiedItemsModel.getClassifiedImages().size(); i++)
					setUploadPicture(mGetClassifiedItemsModel.getClassifiedImages().get(i).getImageUrl(),0);
			}
			
		}else{
			
			mCategory = getIntent().getExtras().getString("category");
			mLocation = getIntent().getExtras().getString("location");
			mLatitude = getIntent().getExtras().getString("latitude");
			mLongitude = getIntent().getExtras().getString("longitude");
			mCity = getIntent().getExtras().getString("city");
			mSection = getIntent().getExtras().getString("section");
			mType = getIntent().getExtras().getString("type");			
			
		}
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.classified_post_BTN_done:
			
			uploadImgPath.clear();
			for(int i=0; i<myImagPath.size() ;i++){
				
				if(!myImagPath.get(i).equalsIgnoreCase("remove") && !myImagPath.get(i).toString().startsWith("remove"))
					uploadImgPath.add(myImagPath.get(i));
				
			}
			
			
			Log.e("", " path "+myImagPath);
			Log.e("", " path "+uploadImgPath);
			Log.e("", " service image "+delClassifedImg);
			
			if(allFieldsValidator(mTitle.getText().toString(), mDesp.getText().toString(), mPrice.getText().toString())){
				
				try {			
					
					mProgressDialog = ProgressDialog.show(PostClassifiedSecondActivity.this, "", "Posting classified.");
					
    				ArrayList<String> data= new ArrayList<String>();
    				data.add(mTitle.getText().toString());
    				data.add(mDesp.getText().toString());
    				data.add(sharedpreference.getString("nickname", "none"));
    				data.add(sharedpreference.getString("userid", ""));
    				data.add(mCategory);
    				data.add(mPrice.getText().toString());
    				data.add(mLocation);
    				data.add(sharedpreference.getString("phone", ""));
    				data.add(sharedpreference.getString("useremail", "none"));
    				data.add(mLatitude);
    				data.add(mLongitude);
    				data.add("Android");
    				data.add(mCbOBO);
    				data.add(mCity);
    				data.add(mSection);
    				data.add("sample");
    				data.add(mType);
    				Log.e("", " data "+data);
					
					
    				
    				if(getIntent().getExtras().getString("from").equalsIgnoreCase("edit")){		
    				
    				PostClassifiedParser mUserAuth = new PostClassifiedParser(String.format(
							Urls.BASE_URL+"UpdateClassifiedMobile?" +
									"Classified_Id=%s&Classified_Title=%s&Classified_Info=%s&Classified_Owner=%s&" +
									"Classified_CreatedBy=%s&Classified_Category=%s&Price=%s&" +
									"Location=%s&PhoneNum=%s&Email=%s&Latitude=%s&Longitude=%s&PostFrom=%s&" +
									"obo=%s&cityname=%s&Classified_Section=%s&CSType=%s",
							mGetClassifiedItemsModel.getClassifiedId(),
							URLEncoder.encode(data.get(0).toString(),"UTF-8"),
							URLEncoder.encode(data.get(1).toString(),"UTF-8"),
							URLEncoder.encode(data.get(2).toString(),"UTF-8"),
							URLEncoder.encode(data.get(3).toString(),"UTF-8"),
							URLEncoder.encode(data.get(4).toString(),"UTF-8"),
							URLEncoder.encode(data.get(5).toString(),"UTF-8"),
							URLEncoder.encode(data.get(6).toString(),"UTF-8"),
							URLEncoder.encode(data.get(7).toString(),"UTF-8"),
							URLEncoder.encode(data.get(8).toString(),"UTF-8"),
							URLEncoder.encode(data.get(9).toString(),"UTF-8"),
							URLEncoder.encode(data.get(10).toString(),"UTF-8"),
							URLEncoder.encode(data.get(11).toString(),"UTF-8"),	
							URLEncoder.encode(data.get(12).toString(),"UTF-8"),
							URLEncoder.encode(data.get(13).toString(),"UTF-8"),
							URLEncoder.encode(data.get(14).toString(),"UTF-8"),
							URLEncoder.encode(data.get(16).toString(),"UTF-8")), mainHandler);
					
					mUserAuth.start();
					
					
    				}else{
    					
    					
        				PostClassifiedParser mUserAuth = new PostClassifiedParser(String.format(
    							Urls.BASE_URL+"AddClassifiedMobile?" +
    									"Classified_Title=%s&Classified_Info=%s&Classified_Owner=%s&" +
    									"Classified_CreatedBy=%s&Classified_Category=%s&Price=%s&" +
    									"Location=%s&PhoneNum=%s&Email=%s&Latitude=%s&Longitude=%s&PostFrom=%s&" +
    									"obo=%s&cityname=%s&Classified_Section=%s&PaymentResponse=%s&CSType=%s",									
    							URLEncoder.encode(data.get(0).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(1).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(2).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(3).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(4).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(5).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(6).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(7).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(8).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(9).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(10).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(11).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(12).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(13).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(14).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(15).toString(),"UTF-8"),
    							URLEncoder.encode(data.get(16).toString(),"UTF-8")), mainHandler);		
    					
    					mUserAuth.start();
    					
    					
    					
    				}
											
								} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			break;
		
		case R.id.classified_icon_row_IMG_del:
			
			Toast.makeText(PostClassifiedSecondActivity.this, "ok", Toast.LENGTH_SHORT).show();
			mLinPictures.removeView(myViewAdd.get((Integer)v.getTag()));	
			
			if(mGetClassifiedItemsModel.getClassifiedImages().size()>0){
				for(int i = 0; i < mGetClassifiedItemsModel.getClassifiedImages().size(); i++){
					
					if(myImagPath.get((Integer)v.getTag()).equalsIgnoreCase("remove"+mGetClassifiedItemsModel.getClassifiedImages().get(i).getImageUrl()))
						delClassifedImg.add(mGetClassifiedItemsModel.getClassifiedImages().get(i).getCSID());				
				}
			}			
			
			myImagPath.remove(Integer.parseInt(v.getTag().toString()));
			myImagPath.add(Integer.parseInt(v.getTag().toString()), "remove");
			
			break;
	

		case R.id.classified_post_BTN_browse:
			
			
			new AddPhotoDialog(PostClassifiedSecondActivity.this)
			.setPositiveButton("Camera", new android.content.DialogInterface.OnClickListener(){

				

				@Override
				public void onClick(DialogInterface dialog, int which) {

					
					String fileName = "new-photo-name.jpg";
					ContentValues values = new ContentValues();
					values.put(MediaStore.Images.Media.TITLE, fileName);
					
					values.put(MediaStore.Images.Media.DESCRIPTION,	"Image capture by camera");
					
					imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
					
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
					startActivityForResult(intent, REQUEST_PICTURE_CLICK);
				}
				
			})
			.setNeutralButton("Photo Library", new android.content.DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
					photoPickerIntent.setType("image/*");
					startActivityForResult(photoPickerIntent, REQUEST_PICTURE_GALLERY);
					
				}
				
			})
			.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					
				}
			}).create().show();
			

			
			
			break;

		}

	}
	
	
//	Classified data post handler
//	****************************

	public Handler mainHandler = new Handler() 
    {
		public void handleMessage(android.os.Message msg) 
		{
			
			
			String result;
			
			result = msg.getData().getString("result");
			
			if(getIntent().getExtras().getString("from").equalsIgnoreCase("edit"))
				mClassifiedId = mGetClassifiedItemsModel.getClassifiedId();			
			else					
				mClassifiedId = msg.getData().getString("id");
			
			Log.e("", "mClassifiedId "+mClassifiedId);
			
			if(result.equalsIgnoreCase("True")){
				
				
				if(uploadImgPath.size()>0){
					
					mPostImagePath = uploadImgPath.get(mPostImagePosition);
					new AsynClassifiedPostImage().execute();
					
				}else if(delClassifedImg.size()>0){
					
					deleteImage();
					
				}else{
					
					mProgressDialog.dismiss();
					
					if(getIntent().getExtras().getString("from").equalsIgnoreCase("edit")){	
						
						PostClassifiedSecondActivity.this.finish();
						
					}else{ 
						
						startActivity(new Intent(PostClassifiedSecondActivity.this, ClassifiedPostConfirmationActivity.class)
						.putExtra("email", sharedpreference.getString("useremail", "none")));
						
					}
				
				}
			
			}else{
				
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "Post unsuccessful", Toast.LENGTH_LONG).show();	
			}				
			
		}
	};
	

//	****************************
	
//	Upload classified images
//	****************************
	
	String mPostImagePath = "";
	private int mPostImagePosition = 0;
	

	class AsynClassifiedPostImage extends AsyncTask<Void, Void, String>
    {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
		}

		@Override
		protected String doInBackground(Void... params) {
			
		

                try
                {
    				File file= new File(mPostImagePath);
    				return new UploadImage().postClassified(Urls.CLASSIFIED_POST_IMG, file, mClassifiedId);
    			
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
		
                
  
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			
			String mResult = result.trim();			
			Log.e("", "mResult "+mResult);
			
			if(mResult.equalsIgnoreCase("Success")){
				
				mPostImagePosition++;
				
				if(mPostImagePosition < uploadImgPath.size()){
					
					mPostImagePath = uploadImgPath.get(mPostImagePosition);
					new AsynClassifiedPostImage().execute();
					
				}else{					
					
					if(getIntent().getExtras().getString("from").equalsIgnoreCase("edit")){	
						
						
						if(delClassifedImg.size()>0){
							
							deleteImage();
							
						}else{
							
							mProgressDialog.dismiss();
							PostClassifiedSecondActivity.this.finish();
						}
						
					}else{
						
						mProgressDialog.dismiss();						
						startActivity(new Intent(PostClassifiedSecondActivity.this, ClassifiedPostConfirmationActivity.class)
						.putExtra("email", sharedpreference.getString("useremail", "none")));
						
					}
				
				}
				
			}else{
				
				mProgressDialog.dismiss();			
				Toast.makeText(getApplicationContext(), "Post unsuccessful", Toast.LENGTH_LONG).show();

			}

			
		}
    }

	

//	****************************
	
//	Delete classified old images
//	****************************
	
	int mPosition = 0;
	
	private void deleteImage(){
		
		try{
			
			ClassifiedDeleteImageParser mUserAuth = new ClassifiedDeleteImageParser(
					String.format(Urls.BASE_URL+"DeleteClassifiedsImage?CSID=%s&",delClassifedImg.get(mPosition)), imageDeleteHandler);		
			
			mUserAuth.start();
			}catch (Exception e) {
				// TODO: handle exception
			}
			
	}

	public Handler imageDeleteHandler = new Handler() 
	{
		public void handleMessage(android.os.Message msg) 
		{
			String handleErrMsg="";
			
			
			handleErrMsg=msg.getData().getString("httpError");
			
			if(handleErrMsg.equalsIgnoreCase("success")){
				
				mPosition++;
				
				if(mPosition < delClassifedImg.size()){
					
					deleteImage();
					
				}else{
					
					mProgressDialog.dismiss();
					PostClassifiedSecondActivity.this.finish();
					
				}								
				
			}else{
				
				mProgressDialog.dismiss();
				PostClassifiedSecondActivity.this.finish();	
				
			
			}
			
			
		}
	};
	
	
//	****************************
	
	
	private Uri 		imageUri;
	private String 		selectedImagePath = "";	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		
		case REQUEST_PICTURE_CLICK:
			
			if(resultCode==RESULT_OK){
				
				File takePhoto = MediaHelper.convertImageUriToFile(imageUri, this);
				Log.e("Photo path", takePhoto.getAbsolutePath());
				BitmapFactory.Options options = new BitmapFactory.Options();
		        options.inSampleSize = 4;
		        selectedImagePath=takePhoto.getAbsolutePath();
		        Bitmap bitmap = BitmapFactory.decodeFile( selectedImagePath, options );
				BitmapDrawable bmpDrawable = new BitmapDrawable(bitmap);     
		        setUploadPicture(bmpDrawable);		        

			}else if(resultCode==RESULT_CANCELED){
				
			}
			break;
		
		case REQUEST_PICTURE_GALLERY:

				  if (data != null) {
					  Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
					  cursor.moveToFirst(); 
					  int idx = cursor.getColumnIndex(ImageColumns.DATA);
					  selectedImagePath = cursor.getString(idx);	 
					  BitmapFactory.Options options = new BitmapFactory.Options();
					  options.inSampleSize = 4;
					  Bitmap bitmapPreview = BitmapFactory.decodeFile(selectedImagePath, options); 
					  BitmapDrawable bmpDrawable = new BitmapDrawable(bitmapPreview);
					  setUploadPicture(bmpDrawable);
					  
				  }else{
					  
						selectedImagePath = "";
						Log.e("canceled", "canceled");

				  }

			break;
	
		default:
			break;
			
		}
	}
	
	
	int mImgPostion = 0;
	
	private void setUploadPicture(BitmapDrawable bmpDrawable) {
		
		
		LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = vi.inflate(R.layout.db_classified_pic_list_row, null);		
		ImageView mImgData = (ImageView)v.findViewById(R.id.classified_icon_row_IMG_data);
		ImageView mImgDel = (ImageView)v.findViewById(R.id.classified_icon_row_IMG_del);			
		mImgData.setImageDrawable(bmpDrawable);		
		mImgDel.setOnClickListener(this);
		mImgDel.setTag(mImgPostion);
		v.setClickable(true);
		mLinPictures.addView(v);	
		myViewAdd.add(v);	
		myImagPath.add(selectedImagePath);
		mImgPostion++;
		
	}
	
	
	private void setUploadPicture(String imageName, int dummy) {
		
		
		LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = vi.inflate(R.layout.db_classified_pic_list_row, null);		
		ImageView mImgData = (ImageView)v.findViewById(R.id.classified_icon_row_IMG_data);
		ImageView mImgDel = (ImageView)v.findViewById(R.id.classified_icon_row_IMG_del);
		new ImageDownloader().download(String.format(Urls.CI_IMG_URL,imageName), mImgData);
		mImgDel.setOnClickListener(this);
		mImgDel.setTag(mImgPostion);
		v.setClickable(true);
		mLinPictures.addView(v);	
		myViewAdd.add(v);	
		myImagPath.add("remove"+imageName);
		mImgPostion++;
		
	}

	
	public boolean allFieldsValidator(String title, String desp, String price){
		

		if(title.equalsIgnoreCase("")){
			Toast.makeText(this, "Please enter the title", Toast.LENGTH_SHORT).show();
			return false;
		}else if(desp.equalsIgnoreCase("")){
			Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show();
			return false;
		}
		else if(price.equalsIgnoreCase("")){
			Toast.makeText(this, "Please enter price", Toast.LENGTH_SHORT).show();
			return false;
		}else 
			

		
		return true;
	}
	
	

}
