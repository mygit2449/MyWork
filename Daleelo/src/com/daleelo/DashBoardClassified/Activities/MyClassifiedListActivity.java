package com.daleelo.DashBoardClassified.Activities;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.DashBoardClassified.Model.GetClassifiedItemsModel;
import com.daleelo.DashBoardClassified.Parser.DeleteClassifiedParser;
import com.daleelo.DashBoardClassified.Parser.GetClassifiedItemsParser;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.User.Activities.UserSettingsActivity;
import com.daleelo.Utilities.Urls;

public class MyClassifiedListActivity extends Activity implements OnClickListener, OnCheckedChangeListener{
	
	

	private ProgressDialog progressdialog;
	Intent reciverIntent;
	MyAdapter mDataAdapter;

	TextView mTitle, mTxtUser, mTxtNoRecords;
	ListView mClassifiedList ;
	RelativeLayout mRelAddNew;
	
	GetClassifiedItemsModel mGetMyClassfiedItemsModel, mGetMyClassfiedItemsModelTemp;
	ArrayList<GetClassifiedItemsModel> mDataModelList;
	
	Button mDone ,mRemove, mEdit, mClassifides, mSettings;
	String mMsg, mOptionType = "none";
	
	int mSelectedPosition;
	
	 public SharedPreferences sharedpreference;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.db_my_classified_list_screen);
   
        intilizationUI();
        
        try {
			getParserData();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                
    }
	
	private void intilizationUI() {
		// TODO Auto-generated method stub
		
		sharedpreference = getSharedPreferences("userlogin",MODE_PRIVATE);
		
		mDone = (Button)findViewById(R.id.my_classified_BTN_done);
		mRemove = (Button)findViewById(R.id.my_classified_BTN_remove);
		mEdit = (Button)findViewById(R.id.my_classified_BTN_edit);
		mClassifides = (Button)findViewById(R.id.my_classified_BTN_classifieds);
		mSettings = (Button)findViewById(R.id.my_classified_BTN_settings);	
		
		mTxtUser = (TextView)findViewById(R.id.my_classified_TXT_user);		
		mTxtNoRecords = (TextView)findViewById(R.id.my_classified_TXT_no_records);		  
		mRelAddNew = (RelativeLayout)findViewById(R.id.my_classified_REL_add_new);
		
		mClassifiedList = (ListView)findViewById(R.id.my_classified_LIST_view);
		
		mTxtUser.setText("Welcome "+sharedpreference.getString("nickname", "User"));
		
		mDone.setOnClickListener(this);
		mRemove.setOnClickListener(this);
		mEdit.setOnClickListener(this);
		mRelAddNew.setOnClickListener(this);
		mClassifides.setOnClickListener(this);
		mSettings.setOnClickListener(this);
		 
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
		if(mOptionType.equalsIgnoreCase("Edit")){
				
			Arrays.fill(setRadio, Boolean.TRUE);
			mDataAdapter.notifyDataSetChanged();			
			
			
		}
		
	}

	public void setListData(){
		
		setRadio = new boolean[mDataModelList.size()];
		Arrays.fill(setRadio, Boolean.FALSE);
		
		mDataAdapter = new MyAdapter(this, mDataModelList);		
		mClassifiedList.setAdapter(mDataAdapter);		
		
		
		
		mClassifiedList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View vv, int position,long arg3) {
								
				
				if(mOptionType.equalsIgnoreCase("none")){
					startActivity(new Intent(MyClassifiedListActivity.this,ClassifiedItemDetailDesp.class)
					.putExtra("data", mDataModelList)
					.putExtra("position", position)
					.putExtra("from", "list"));
				}
				
				
			}
			
		});
	}
	
		
		
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Intent intent;
		
		switch (v.getId()) {
		
		case R.id.my_classified_BTN_done:
			
				Arrays.fill(setRadio, Boolean.FALSE);	
				mDataAdapter.notifyDataSetChanged();

				mDone.setVisibility(View.GONE);
				mRemove.setVisibility(View.VISIBLE);
				mEdit.setVisibility(View.VISIBLE);
				
				mOptionType = "none";
				
			break;
			
		case R.id.my_classified_BTN_edit:
				
				if(mDataModelList != null){
					if(mDataModelList.size()>0){
						
						Arrays.fill(setRadio, Boolean.TRUE);
						mDataAdapter.notifyDataSetChanged();
						
						mDone.setVisibility(View.VISIBLE);
						mRemove.setVisibility(View.GONE);
						mEdit.setVisibility(View.GONE);
						
						mMsg = "Do you want to edit this classified.";
						mOptionType = "Edit";
						
					}
				}
				
			break;
			
		case R.id.my_classified_BTN_remove:
				
				if(mDataModelList != null){
					if(mDataModelList.size()>0){
						
						Arrays.fill(setRadio, Boolean.TRUE);
						mDataAdapter.notifyDataSetChanged();
						
						mDone.setVisibility(View.VISIBLE);
						mRemove.setVisibility(View.GONE);
						mEdit.setVisibility(View.GONE);
						
		
						mMsg = "Do you want to remove this classified.";
						mOptionType = "Remove";
					}
				}
				
			break;
			
			
		case R.id.my_classified_REL_add_new:
			
			startActivity(new Intent(MyClassifiedListActivity.this, PostClassifiedActivity.class));
			
			break;
			
		case R.id.my_classified_BTN_classifieds:
			
			startActivity(new Intent(MyClassifiedListActivity.this,ClassifiedListActivity.class)
			.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
			finish();
			
			break;
		
		case R.id.my_classified_BTN_settings:
		
			startActivity(new Intent(MyClassifiedListActivity.this,UserSettingsActivity.class)); 
			
			break;
		
		
		}
		
		
	}
	

	boolean[] setRadio;
	
	class MyAdapter extends ArrayAdapter<GetClassifiedItemsModel>{
    	
    	private LayoutInflater mInflater;
    	ArrayList<GetClassifiedItemsModel> mDataFeeds;
    	Context context;
    	
    	public MyAdapter(Context context, ArrayList<GetClassifiedItemsModel> mDataFeeds) {
    		
    		super(context, R.layout.db_my_classified_list_row, mDataFeeds);
    		this.context=context;
        	this.mDataFeeds = mDataFeeds;
        	
          
        }


		@Override
		public GetClassifiedItemsModel getItem(int position) {
			// TODO Auto-generated method stub
			return mDataFeeds.get(position);
		}

		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
						 
			 LayoutInflater mInflater= LayoutInflater.from(context);
			 
			 convertView = mInflater.inflate(R.layout.db_my_classified_list_row, null);
			 
			 TextView mName = (TextView)convertView.findViewById(R.id.my_classified_row_TXT_name);
			 CheckBox mSelect = (CheckBox)convertView.findViewById(R.id.my_classified_row_CB_select);
			 
			 if(setRadio[position]){
				 
				 mSelect.setVisibility(View.VISIBLE);
				 
			 }else{
				 
				 mSelect.setVisibility(View.GONE);	
			 }
			 
			 			 
			 mSelect.setOnCheckedChangeListener(MyClassifiedListActivity.this);
			 mSelect.setTag(position);
			 
			 mName.setText(mDataFeeds.get(position).getClassifiedTitle());
			
			return convertView;
		}
		
	}
			
		
		
	 
    
    public void getParserData() throws MalformedURLException{
    	
    	
    	try {
        	progressdialog = ProgressDialog.show(MyClassifiedListActivity.this, "","Please wait...", true);
//        	GetpostedClassifieds?UserId=string
        	String mUserId = sharedpreference.getString("userid", "0");
        	String mUrl = Urls.BASE_URL+"GetpostedClassifieds?UserId="+mUserId;
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
				
//				new BuyProductsByBrandIDParser(feedUrl,handler).parser();
				new GetClassifiedItemsParser(feedUrl, handler).parser();
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			super.run();
		}
		
	}
    
    
    String noDataMsg = "Classifieds not found";
    
	class FeedParserHandler extends Handler {
		public void handleMessage(android.os.Message msg) {
			
			progressdialog.dismiss();
			
			
			
			if(msg.what==0)
			{	
				mClassifiedList.setVisibility(View.VISIBLE);
				mDone.setVisibility(View.VISIBLE); 
				mRemove.setVisibility(View.VISIBLE);
				mDataModelList = null;
				mDataModelList = (ArrayList<GetClassifiedItemsModel>) msg.getData().getSerializable("datafeeds");
				
				setListData();			
				

			}else if(msg.what==1){
				
				mEdit.setVisibility(View.INVISIBLE); 
				mRemove.setVisibility(View.INVISIBLE);
				mClassifiedList.setVisibility(View.INVISIBLE);
				mTxtNoRecords.setVisibility(View.VISIBLE);
				
				new AlertDialogMsg(MyClassifiedListActivity.this, noDataMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
					}
					
				}).create().show();
				
				
			}else if(msg.what==2){
				
				mEdit.setVisibility(View.INVISIBLE); 
				mRemove.setVisibility(View.INVISIBLE);
				mClassifiedList.setVisibility(View.INVISIBLE);
				
				String mmsg = "Error connecting network or server not responding please try again..";
				
				new AlertDialogMsg(MyClassifiedListActivity.this, mmsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
//						MainHomeScreen.this.finish();
						
					}
					
				}).create().show();
			}

		}
	}







	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
//		Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
		
		mSelectedPosition = (Integer) buttonView.getTag();	
		Log.e("", " "+mSelectedPosition);
		myAlertDialog();
		
		
	}
	
	
	
	

	private void myAlertDialog(){
		
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Daleelo");
		builder.setMessage(mMsg)
		       .setCancelable(false)
		       .setPositiveButton(mOptionType, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
		                dialog.cancel();       
		                
		                if(mOptionType.equalsIgnoreCase("remove")){
		                	
		                	deleteClassified();
		                	
		                }else if(mOptionType.equalsIgnoreCase("edit")){
		                	
		                	 startActivity(new Intent(MyClassifiedListActivity.this, PostClassifiedSecondActivity.class)
				               .putExtra("data", mDataModelList.get(mSelectedPosition))
				               .putExtra("from", "edit"));              	
		                	
		                	
		                }
		                
		               

		               
		           }
		       })
		       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
		                dialog.cancel();
		                mDataAdapter.notifyDataSetChanged();
		                
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	
	private void deleteClassified(){
		
		progressdialog = ProgressDialog.show(MyClassifiedListActivity.this, "","Please wait...", true);
		
//		DeleteClassifieds?ClassifiedId=string
		
		DeleteClassifiedParser mUserAuth = new DeleteClassifiedParser(String.format(
				Urls.BASE_URL+"DeleteClassifieds?ClassifiedId=%s",mDataModelList.get(mSelectedPosition).getClassifiedId()), mainHandler);		
		
		mUserAuth.start();
	}

	
	
	public Handler mainHandler = new Handler() 
    {
		public void handleMessage(android.os.Message msg) 
		{
			String handleErrMsg="";
			progressdialog.dismiss();
			
			handleErrMsg=msg.getData().getString("httpError");
			
			if(handleErrMsg.equalsIgnoreCase("success")){
			
				Toast.makeText(MyClassifiedListActivity.this, "Classified deleted successfully", Toast.LENGTH_SHORT).show();
				mDataModelList.remove(mSelectedPosition);
				 mDataAdapter.notifyDataSetChanged();			
				
			}else{
				
				Toast.makeText(MyClassifiedListActivity.this, handleErrMsg, Toast.LENGTH_SHORT).show();
			
			}
			
			
		}
	};


}
	