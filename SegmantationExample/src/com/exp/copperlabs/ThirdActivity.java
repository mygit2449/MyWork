package com.exp.copperlabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ThirdActivity extends TheamActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.third);
	}

	
	public void onThirdclick(View v){
		startActivity(new Intent(ThirdActivity.this, SecondActivity.class));
	}
}
