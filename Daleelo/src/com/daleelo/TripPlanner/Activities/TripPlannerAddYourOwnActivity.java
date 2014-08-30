package com.daleelo.TripPlanner.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.daleelo.R;
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.Business.Activities.BusinessMoreCategories;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.MyStuff.Activities.MyStuffActivity;

public class TripPlannerAddYourOwnActivity  extends Activity implements OnClickListener{	
    
	private RelativeLayout destination_layout = null;
	private RelativeLayout addressbook_layout = null;
	private RelativeLayout businesslist_layout = null;
	private RelativeLayout mystuff_layout = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripplanner_addyourownlist);   
        initializeUI();
        
        
    }

	private void initializeUI() {		
		destination_layout =(RelativeLayout)findViewById(R.id.add_destination_layout);
		addressbook_layout =(RelativeLayout)findViewById(R.id.add_addressbook_layout);
		businesslist_layout=(RelativeLayout)findViewById(R.id.add_businesslisting_layout);
		mystuff_layout=(RelativeLayout)findViewById(R.id.add_mystuff_layout);
		
		destination_layout.setOnClickListener(this);
		addressbook_layout.setOnClickListener(this);
		businesslist_layout.setOnClickListener(this);
		mystuff_layout.setOnClickListener(this);
		
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
				
				startActivity(new Intent(TripPlannerAddYourOwnActivity.this, GlobalSearchActivity.class));
				
			}
		});
		
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(TripPlannerAddYourOwnActivity.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(TripPlannerAddYourOwnActivity.this,MyStuffActivity.class)
				.putExtra("from", "bottom")); 
			
			}
		});
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
			case R.id.add_destination_layout:				
				
				Intent dest_intent = new Intent(TripPlannerAddYourOwnActivity.this,TripPlannerAddActivity.class).putExtra("heading",1);
				startActivityForResult(dest_intent, 10);
				break;
				
			case R.id.add_addressbook_layout:
				
				Intent add_contact = new Intent(TripPlannerAddYourOwnActivity.this,TripPlannerAddFromContacts.class).putExtra("heading",2);				
				startActivityForResult(add_contact, 10);
				 
				break;	
			case R.id.add_businesslisting_layout:
				
				Intent busi_intent = new Intent(TripPlannerAddYourOwnActivity.this,BusinessMoreCategories.class);
				busi_intent.putExtra("from", "trip");
				startActivityForResult(busi_intent, 10);
				break;
			case R.id.add_mystuff_layout:
				
				Intent stuf_intent = new Intent(TripPlannerAddYourOwnActivity.this,MyStuffActivity.class);
				stuf_intent.putExtra("from", "trip");
				startActivityForResult(stuf_intent, 10);
				break;
	
			default:
				break;
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode==10 && resultCode == RESULT_OK){
			setResult(RESULT_OK, data);			
			finish();
		}
		
	}
	
    
}    