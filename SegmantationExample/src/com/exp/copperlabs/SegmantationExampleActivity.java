package com.exp.copperlabs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SegmantationExampleActivity extends TheamActivity implements OnClickListener {
	

	/** Called when the activity is first created. */
	
	Context mContext;
	
	Dialog dialog;
	
	Button red_btn, blue_btn, green_btn;
	AlertDialog alertDialog;
	RelativeLayout theme_layout;
	
	TextView heading_text;
	Button choose;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theam);
		mContext = this.getApplicationContext();
		dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.second);
		dialog.setTitle("Custom Dialog");		
		theme_layout = (RelativeLayout)findViewById(R.id.theme_rel);
		heading_text = (TextView)findViewById(R.id.title);
		choose = (Button)findViewById(R.id.redbutton);
		Log.v(getClass().getSimpleName(), "c1 value "+TheamActivity.color_change);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v(getClass().getSimpleName(), "start");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.v(getClass().getSimpleName(), "start");

	}
	
	
	
	public void onRedclick(View v) 
	{
		Log.i(getClass().getSimpleName(), "red");
		AlertDialog.Builder builder = null;
		
		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.second, (ViewGroup) findViewById(R.id.root_layout));

		builder = new AlertDialog.Builder(SegmantationExampleActivity.this);
		builder.setView(layout);
		alertDialog = builder.show();
		
		red_btn = (Button)layout.findViewById(R.id.red);
		blue_btn = (Button)layout.findViewById(R.id.blue);
		green_btn = (Button)layout.findViewById(R.id.green);

		
		red_btn.setOnClickListener(this);
		blue_btn.setOnClickListener(this);
		green_btn.setOnClickListener(this);
//		 startActivity(new Intent(this, SecondActivity.class));
	}
	
	
	public void onBlueclick(View v) {
		 startActivity(new Intent(this, SecondActivity.class));
	}


	public void onThirdclick(View v){
		 startActivity(new Intent(this, ThirdActivity.class));

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.red:
			
			Log.v(getClass().getSimpleName(), "red");
			TheamActivity.color_change = 0;
			theme_layout.setBackgroundResource(R.color.red);
//			heading_text.setTextSize(20);
//			heading_text.setBackgroundResource(R.color.white);
			heading_text.setTextAppearance(getApplicationContext(), R.style.boldText);
			choose.setBackgroundResource(R.color.green);
			
			alertDialog.dismiss();
			
			break;

			
		case R.id.blue:
			
			TheamActivity.color_change = 1;
			theme_layout.setBackgroundResource(R.color.grey);
//			heading_text.setBackgroundResource(R.color.green);
//			heading_text.setTextSize(25);

			heading_text.setTextAppearance(getApplicationContext(), R.style.greenText);
			choose.setBackgroundResource(R.color.red);

			alertDialog.dismiss();
			Log.v(getClass().getSimpleName(), "blue");
			
			break;
			
		case R.id.green:
	
			TheamActivity.color_change = 2;
			theme_layout.setBackgroundResource(R.color.green);
			heading_text.setTextAppearance(getApplicationContext(), R.style.redText);

			choose.setBackgroundResource(R.color.blue);

			alertDialog.dismiss();
			Log.v(getClass().getSimpleName(), "green");
			
			break;
		
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	 Intent intent = new Intent(this,SegmantationExampleActivity.class);
		      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		      startActivity(intent);
	       return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}