package com.daleelo.DashBoardEvents.Activities;

import com.daleelo.R;
import com.daleelo.Hasanat.Activities.ZakatFilterActivity;
import com.daleelo.Utilities.Utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class EventsCalendarSettingsActivity extends Activity{
	
	private Button mbtn_done;
	private Spinner mSpinner_hijri;
	private ToggleButton mtgb_fundraiser;
	private ToggleButton mtgb_events;
	
	private String[] mSpinnerArray = {"-3","-2","-1","0","1","2","3"};
	SharedPreferences spinnerPrefs;
	SharedPreferences.Editor prefsEditor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_settings);
		initializeUI();      


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(EventsCalendarSettingsActivity.this,android.R.layout.simple_spinner_item, mSpinnerArray);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    mSpinner_hijri.setAdapter(adapter);
	    mSpinner_hijri.setSelection(spinnerPrefs.getInt("position", 3));

	    mSpinner_hijri.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {				
				
				 prefsEditor.putInt("position", arg2);
				 prefsEditor.commit();

				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    
	    mbtn_done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				
				
				if(mtgb_events.isChecked()){
					Utils.normal_events = true;
				}else{
					Utils.normal_events = false;
				}
				
				if(mtgb_fundraiser.isChecked()){
					Utils.fundraising_events = true;
				}else{
					Utils.fundraising_events = false;
				}
					
				EventsCalendarSettingsActivity.this.finish();
			}
		});
	}
	
	private void initializeUI(){
		mbtn_done = (Button)findViewById(R.id.calendar_settings_BTN_done);
		mSpinner_hijri = (Spinner)findViewById(R.id.calendar_settings_SPN_hijri);
		mtgb_fundraiser = (ToggleButton)findViewById(R.id.calendar_settings_TGB_fundraiser);
		mtgb_events = (ToggleButton)findViewById(R.id.calendar_settings_TGB_events);
		spinnerPrefs = this.getSharedPreferences("spinner_Prefs", MODE_WORLD_READABLE);
        prefsEditor = spinnerPrefs.edit();    
       
		
		
		if(Utils.normal_events){
			mtgb_events.setChecked(true);			
		}else{
			mtgb_events.setChecked(false);	
		}
		
		if(Utils.fundraising_events){
			mtgb_fundraiser.setChecked(true);
		}else{
			mtgb_fundraiser.setChecked(false);
		}
			
			
	}

}
