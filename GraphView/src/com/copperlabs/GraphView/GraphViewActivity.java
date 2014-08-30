package com.copperlabs.GraphView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GraphViewActivity extends Activity implements OnClickListener {
	Button weight,height,circum;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initializeUI();
    }
	private void initializeUI() {
		// TODO Auto-generated method stub
		weight = (Button) findViewById(R.id.weight);
		height = (Button) findViewById(R.id.height);
		circum = (Button) findViewById(R.id.circum);
		weight.setOnClickListener(this);
		height.setOnClickListener(this);
		circum.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.weight:
			Intent widthGraph = new Intent(this,Weight.class);
			startActivity(widthGraph);
			break;
		case R.id.height:
			Intent heightGraph = new Intent(this,Height.class);
			startActivity(heightGraph);
			break;
		case R.id.circum:
			Intent circumGraph = new Intent(this,Circum.class);
			startActivity(circumGraph);
			break;
		}
			
	}
	
	
   }


