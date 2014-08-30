package com.pratice.exp1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Exp1Activity extends Activity {
    /** Called when the activity is first created. */
	static int  iCheck = 0;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        callingHere();
        withOutLoop(iCheck);
    }
    
    
    public void callingHere(){
    	
    	
    	switch (iCheck) {
		case 11:
			System.exit(0); 
			break;

		default:
			System.out.println( " integer value "+ iCheck++ + "change value "+iCheck * iCheck);
			break;
		}
    	callingHere();
    }
    
    
   int withOutLoop(int i){
	   System.out.println( " integer value "+ i + "change value "+i * i);
    	 return ((i ^ 10) & withOutLoop(-(~i)));
    }
    
}