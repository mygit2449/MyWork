package com.daleelo.GlobalSearch;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.DashBoardClassified.Activities.ClassifiedItemDetailDesp;
import com.daleelo.DashBoardEvents.Activities.CalendarEventDetailDesc;
import com.daleelo.Dashboard.Activities.DealsDetailDesp;
import com.daleelo.Dashboard.Activities.SpotlightDetailDesp;
import com.daleelo.Masjid.Model.MasjidModel;

public class GlobalSearchListActivity extends Activity{
	
	Intent reciverIntent;
	MyAdapter mDataAdapter;

	RelativeLayout mRelFeaturedData;
	TextView mTitle, mFeatureItemName, mFeaturedItemAddress, mFeaturedItemPhone;
	ListView mBusinessItemList ;
	Button mLocation;
	
	String mCategoryID;
	
	MasjidModel mMasjidLocationModel;
	ArrayList<GlobalSearchModel> mDataModelList;
	
	int mType = 1;
	
	ArrayList<String> mData;
	ProgressDialog progressDialog; 
	Thread myThread;
    public SharedPreferences sharedpreference;
    boolean DATA_CHANGE = false;
    DecimalFormat desimalFormat = new DecimalFormat("##0.0");
	
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.global_search_listview);   
        intilizationUI();
        
    }
	
	
	Thread mThread;

	private void intilizationUI() {
		// TODO Auto-generated method stub
		
		reciverIntent = getIntent();
		mDataModelList = (ArrayList<GlobalSearchModel>) reciverIntent.getExtras().getSerializable("data");
		setData();		
	}
	
	public void setData(){
		
		setListData();	
	
	}
	
	
	public void setListData(){
		
		if(mDataAdapter != null)
			mDataAdapter.clear();
		
		mDataAdapter = new MyAdapter(this, mDataModelList);
		mBusinessItemList = (ListView)findViewById(R.id.masjidList_listview);
		mBusinessItemList.setAdapter(mDataAdapter);		
	
		
		mBusinessItemList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View vv, int position,long arg3) {
				
				String item_type = mDataModelList.get(position).getTypeinfo();
				String item_id = mDataModelList.get(position).getBusinessID();
				
				if(item_type.equalsIgnoreCase("Deals")){
					
					Intent desc_intent = new Intent(GlobalSearchListActivity.this,DealsDetailDesp.class);
					desc_intent.putExtra("from", "item");
					desc_intent.putExtra("id",item_id);
					startActivity(desc_intent);
					
				}else if(item_type.equalsIgnoreCase("SpotLight")){
					
					Intent desc_intent = new Intent(GlobalSearchListActivity.this,SpotlightDetailDesp.class);
					desc_intent.putExtra("from", "item");
					desc_intent.putExtra("id",item_id);
					startActivity(desc_intent);
					
				}else if(item_type.equalsIgnoreCase("Events")){
					
					Intent desc_intent = new Intent(GlobalSearchListActivity.this, CalendarEventDetailDesc.class);
					desc_intent.putExtra("from", "item");
					desc_intent.putExtra("id",item_id);
					startActivity(desc_intent);
					
				}else if(item_type.equalsIgnoreCase("Classifieds")){
					
					Intent desc_intent = new Intent(GlobalSearchListActivity.this, ClassifiedItemDetailDesp.class);
					desc_intent.putExtra("from", "item");
					desc_intent.putExtra("id",item_id);
					startActivity(desc_intent);
					
				}else if(item_type.equalsIgnoreCase("Business")){
					
					Intent desc_intent = new Intent(GlobalSearchListActivity.this, BusinessDetailDesp.class);
					desc_intent.putExtra("from", "item");
					desc_intent.putExtra("id",item_id);
					startActivity(desc_intent);
				}
			}			
		});		
	}
	
	
	
	class MyAdapter extends ArrayAdapter<GlobalSearchModel>{
    	
    	private LayoutInflater mInflater;
    	ArrayList<GlobalSearchModel> mDataFeeds;
    	Context context;
    	int mainCount; 
    	
    	
    	public MyAdapter(Context context, ArrayList<GlobalSearchModel> mDataFeeds) {
    		
    		super(context, R.layout.global_search_listrow, mDataFeeds);
    		this.context=context;
        	this.mDataFeeds = mDataFeeds;        	
        	
          
        }


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDataFeeds.size();
		}


		@Override
		public GlobalSearchModel getItem(int position) {
			// TODO Auto-generated method stub
			return mDataFeeds.get(position);
		}

		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
						 
			mInflater= LayoutInflater.from(context);
				convertView = mInflater.inflate(R.layout.global_search_listrow, null);

			Log.e("", "mainCount "+mainCount+" position "+position);			
			 
			 TextView mName = (TextView)convertView.findViewById(R.id.global_search_TXT_name);
			 TextView mAddress = (TextView)convertView.findViewById(R.id.global_search_TXT_address);			 	 
			 mName.setText(mDataFeeds.get(position).getBusinessTitle());			 
			 mAddress.setText((mDataFeeds.get(position).getAddressInfo().length()>0 ? mDataFeeds.get(position).getAddressInfo():""));			
			return convertView;
			
		}
		
	}  
	   
}