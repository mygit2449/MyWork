package com.exp.copperlabs;

import android.app.Activity;
import android.os.Bundle;

public class TheamActivity extends Activity{

	public static int color_change = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		if (color_change == 0) 
			super.setTheme(R.style.customtheme_pink); 
		else if(color_change == 1)
			super.setTheme(R.style.customtheme_blue); 
		else if(color_change == 2)
			super.setTheme(R.style.customtheme_green); 
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		if (color_change == 0) 
			super.setTheme(R.style.customtheme_pink); 
		else 
			super.setTheme(R.style.customtheme_blue); 
		
		super.onResume();
	}

}
