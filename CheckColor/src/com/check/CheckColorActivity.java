package com.check;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class CheckColorActivity extends Activity {
	Button mPopupButton;
	ImageView a1;
	Point p;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mPopupButton = (Button)findViewById(R.id.button_popup);
        a1 = (ImageView)findViewById(R.id.arrow_bottom_img);
        mPopupButton.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 //Open popup window
					       if (p != null)
					       showPopup(CheckColorActivity.this, p);
					       a1.setVisibility(View.VISIBLE);
			}
		});
    }
    
    
    @Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		int[] location = new int[2];
		mPopupButton.getLocationOnScreen(location);
		
		p = new Point();
		p.x = location[0];
		p.y = location[1];
		
	}
    
public void showPopup(final Activity context, Point p){
		
		LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup_layout_LL);
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);
		
		
		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(layout);
		popup.setWidth(LayoutParams.WRAP_CONTENT);
		popup.setHeight(LayoutParams.WRAP_CONTENT);
		popup.setFocusable(true);
		
		int OFFSET_X = -10;
	    int OFFSET_Y = -120;
	    
	    popup.setBackgroundDrawable(new BitmapDrawable());
	    
	    popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
	}
}