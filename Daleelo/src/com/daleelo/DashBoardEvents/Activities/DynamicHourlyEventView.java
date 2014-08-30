package com.daleelo.DashBoardEvents.Activities;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.Color;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DynamicHourlyEventView extends LinearLayout {

	public DynamicHourlyEventView(Context context) {
		super(context);
		this.setOrientation(LinearLayout.HORIZONTAL);
		// TODO Auto-generated constructor stub
	}
	
	public void setMarginInAbs(int x, int width){
		AbsoluteLayout.LayoutParams params = (AbsoluteLayout.LayoutParams) this.getLayoutParams();
		params.width = width;
		params.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		params.x =x;
		params.y =0;
		
		this.setLayoutParams(params);
	}
	
	public void addParamsForRel(){
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
		params.width = android.view.ViewGroup.LayoutParams.FILL_PARENT;
//		params.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		params.height = 30;
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		this.setLayoutParams(params);
	}

}
