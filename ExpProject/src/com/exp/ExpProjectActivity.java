package com.exp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ExpProjectActivity extends Activity {
	
	TextView mMain_txt;
	String normal_str,  modified_str;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        normal_str = "AABBCC";
        modified_str = withOutduplicates(normal_str);
        
        mMain_txt = (TextView)findViewById(R.id.main_txt);
        mMain_txt.setText(modified_str);
    }
    
    public String withOutduplicates(String modify){
    	
    	if (modify.length() <= 1 )
    		return modify;
    	
        if(modify.substring(1,2).equals(modify.substring(0,1)) )
        {
        	Log.v(getClass().getSimpleName(), "check  "+modify.substring(1,2)+" "+modify.substring(0,1)+" "+modify.substring(1));
        	return withOutduplicates(modify.substring(1));
        }else {
        	String a = modify.substring(0,1);
        	String b = withOutduplicates(modify.substring(1));
        	Log.v(getClass().getSimpleName(), "1111"+a+"b "+b);
        	return a + b;
        }
    }
}