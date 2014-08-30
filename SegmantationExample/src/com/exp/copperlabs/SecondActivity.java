package com.exp.copperlabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class SecondActivity extends TheamActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		setTheme(android.R.style.Theme_Dialog);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ss);
		Log.v(getClass().getSimpleName(), " c value "+TheamActivity.color_change);
	}

	
	public void onHomeclick(View v){
		startActivity(new Intent(this, SegmantationExampleActivity.class));
	}
	
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        Intent intent = new Intent(this,SegmantationExampleActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(intent);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}*/

	
}
