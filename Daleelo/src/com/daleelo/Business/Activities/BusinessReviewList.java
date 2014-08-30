package com.daleelo.Business.Activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.Business.Model.ReviewModel;
import com.daleelo.DashBoardClassified.Activities.MyClassifiedListActivity;
import com.daleelo.User.Activities.LoginActivity;
import com.daleelo.helper.DateFormater;

public class BusinessReviewList extends Activity{
	
	Intent reciverIntent;
	MyAdapter mDataAdapter;
	ListView mBusinessItemList ;	
	ArrayList<ReviewModel> mReviewModelList;	
	ImageButton mWrite;
	
	public  static final String[] 	MONTH			=	{
		
		"January"
		,"February"
		,"March"
		,"April"
		,"May"
		,"June"
		,"July"
		,"August"
		,"September"
		,"October"
		,"November"
		,"December"}; 
	
	public SharedPreferences sharedpreference;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.business_review_list_screen);
   
        intilizationUI();
       
    }
	

	private void intilizationUI() {
		// TODO Auto-generated method stub
		
		reciverIntent = getIntent();		
		mReviewModelList = (ArrayList<ReviewModel>) reciverIntent.getExtras().getSerializable("data");
		
		sharedpreference = getSharedPreferences("userlogin",MODE_PRIVATE);
		
		mWrite = (ImageButton)findViewById(R.id.business_review_IBTN_write);
		mWrite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent;
				if(!sharedpreference.getString("userid", "0").equalsIgnoreCase("0")){
					
//					Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
				
					intent = new Intent(BusinessReviewList.this, BusinessWriteReviewScreen.class);
					startActivity(intent);
				
				}else{
					
					myAlertDialog();
				}
				
			}
		});
		 
		setListData();
		
	}
	
	
	public void setData(){
	
	
	}
	
	
	
	public void setListData(){
		
		
		mDataAdapter = new MyAdapter(this, mReviewModelList);
		mBusinessItemList = (ListView)findViewById(R.id.business_review_LIST_view);
		mBusinessItemList.setAdapter(mDataAdapter);
		
		
		
		mBusinessItemList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View vv, int position,long arg3) {
				
					startActivity(new Intent(BusinessReviewList.this,BusinessReviewDetailDesp.class)
					.putExtra("data", mReviewModelList)
					.putExtra("position", position));			
				
			}
			
		});
		
	
		
	}
	
		
		

	
	class MyAdapter extends ArrayAdapter<ReviewModel>{
    	
    	private LayoutInflater mInflater;
    	ArrayList<ReviewModel> mDataFeeds;
    	Context context;
    	
    	public MyAdapter(Context context, ArrayList<ReviewModel> mDataFeeds) {
    		
    		super(context, R.layout.business_review_list_row, mDataFeeds);
    		this.context=context;
        	this.mDataFeeds = mDataFeeds;
          
        }


		@Override
		public ReviewModel getItem(int position) {
			// TODO Auto-generated method stub
			return mDataFeeds.get(position);
		}

		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
						 
			 LayoutInflater mInflater= LayoutInflater.from(context);
			 
			 convertView = mInflater.inflate(R.layout.business_review_list_row, null);
			 
			 TextView mName = (TextView)convertView.findViewById(R.id.business_review_TXT_name);
			 TextView mPostedOn = (TextView)convertView.findViewById(R.id.business_review_TXT_posted_on);
			 TextView mReview = (TextView)convertView.findViewById(R.id.business_review_TXT_review);
			 ImageView mRating = (ImageView)convertView.findViewById(R.id.business_review_IMG_phone);
			 
			 
			 mName.setText(mDataFeeds.get(position).getReviewTittle());
			 
			 try {
				
				 mPostedOn.setText("by "+mDataFeeds.get(position).getReviewPostedBy().toString().split("@")[0]+" on "+postedOn(mDataFeeds.get(position).getReviewPostedOn()));
			 } catch (Exception e) {
				// TODO: handle exception
				mPostedOn.setText("by "+mDataFeeds.get(position).getReviewPostedBy()+" on "+postedOn(mDataFeeds.get(position).getReviewPostedOn()));
			 }
			 
			 mReview.setText(mDataFeeds.get(position).getReviewContent());
			 
			 if(mDataFeeds.get(position).getReviewRank().length()>0){
					
					int rate = Integer.parseInt(mDataFeeds.get(position).getReviewRank().toString());
					
					if(rate == 1)		
						mRating.setImageResource(R.drawable.icon_one_star);
					if(rate == 2)		
						mRating.setImageResource(R.drawable.icon_two_star);
					if(rate == 3)		
						mRating.setImageResource(R.drawable.icon_three_star);
					if(rate == 4)		
						mRating.setImageResource(R.drawable.icon_four_star);
					if(rate == 5)		
						mRating.setImageResource(R.drawable.icon_five_star);
					
				}
			 	 
			
			 

			return convertView;
		}
		
	}
	
	
	private String postedOn(String mdate){
		
		long timeStampOne = DateFormater.parseDate(mdate, "MM/dd/yyyy h:mm:ss a");
		
		Date dateOne = new Date(timeStampOne);
		
		Calendar calendarOne=Calendar.getInstance();
		calendarOne.setTime(dateOne);
	
		
		String mDayOne = MONTH[dateOne.getMonth()]+" "+dateOne.getDate()+", " + (dateOne.getYear()+1900);
		
		return mDayOne;
		
		
	}
	
	
private void myAlertDialog(){
		
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Daleelo");
		builder.setMessage("Please login to post your review")
		       .setCancelable(false)
		       .setPositiveButton("Login", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
						startActivity(new Intent(BusinessReviewList.this,LoginActivity.class)
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
	
	