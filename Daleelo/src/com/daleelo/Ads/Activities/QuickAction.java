package com.daleelo.Ads.Activities;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.daleelo.R;


public class QuickAction extends Popupwindows implements android.widget.PopupWindow.OnDismissListener{

	private ImageView mArrowUp;

	private ImageView mArrowDown;

	private LayoutInflater inflater;
	
	private int dyTop, dyBottom;
	
	private TextView quickaction_text;

	private OnDismissListener mDismissListener;

	private boolean mDidAction;


	public QuickAction(Context context) {

             super(context);
             inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             setRootViewId(R.layout.quickaction);

	}

	public void setRootViewId(int id) {

		mRootView = (ViewGroup) inflater.inflate(id, null);

		mArrowDown = (ImageView) mRootView.findViewById(R.id.arrow_down);

		mArrowUp = (ImageView) mRootView.findViewById(R.id.arrow_up);

		quickaction_text = (TextView) mRootView.findViewById(R.id.quickaction_text);

		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

		setContentView(mRootView);

	}
	
	/**
	 * 
	 * Show pop up mWindow
	 */
	public void show(View anchor, int type, String text) {

		preShow();
		
		if (type == 1) {
			quickaction_text.getLayoutParams().height = 180;
			quickaction_text.setText(text);
			Log.v(getClass().getSimpleName(), " type1 "+type);
		}
		
		if(type == 2){
			quickaction_text.getLayoutParams().height = 100;
			quickaction_text.setText(text);
			Log.v(getClass().getSimpleName(), " type2 "+type);
		}
		
		
		if(type == 3){
			quickaction_text.getLayoutParams().height = 220;
			quickaction_text.setText(text);
			Log.v(getClass().getSimpleName(), " type2 "+type);
		}
		
		if(type == 4){
			quickaction_text.getLayoutParams().height = 130;
			quickaction_text.setText(text);
			Log.v(getClass().getSimpleName(), " type2 "+type);
		}

		int[] location = new int[2];

		mDidAction = false;

		anchor.getLocationOnScreen(location);

		Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());

		mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		int rootWidth = mRootView.getMeasuredWidth();

		int rootHeight = mRootView.getMeasuredHeight();

		int screenWidth = mWindowManager.getDefaultDisplay().getWidth();

		int screenHeight = mWindowManager.getDefaultDisplay().getHeight();

		int xPos = (screenWidth - rootWidth) / 2;

		int yPos = anchorRect.top - rootHeight;

		boolean onTop = true;

		Log.v(getClass().getSimpleName(), " anchor.getTop() "+anchor.getTop());
		
		dyTop	 = anchorRect.top;
		dyBottom = screenHeight - anchorRect.bottom;

		onTop		= (dyTop > dyBottom) ? true : false;
		
		if (onTop) {
			
		}else {
			yPos = anchorRect.bottom;
			Log.v(getClass().getSimpleName(), "onTop1 "+onTop);
			onTop = false;
		}

		Log.v(getClass().getSimpleName(), "onTop2 "+onTop+" xposition "+xPos+" yPos"+yPos+"anchor.getTop() "+anchor.getTop());

		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up),anchorRect.centerX());

		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
		
	}

	/**
	 * 
	 * Show arrow
	 * 
	 * 
	 * 
	 * @param whichArrow  arrow type resource id
	 * 
	 * @param requestedX  distance from left screen
	 */

	private void showArrow(int whichArrow, int requestedX) {

		final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp	: mArrowDown;

		final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown: mArrowUp;

		final int arrowWidth = mArrowUp.getMeasuredWidth();

		showArrow.setVisibility(View.VISIBLE);

		ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow.getLayoutParams();

		param.leftMargin = requestedX - arrowWidth / 2;

		hideArrow.setVisibility(View.INVISIBLE);

	}

	/**
	 * 
	 * Set listener for window dismissed. This listener will only be fired if
	 * the quick action dialog is dismissed
	 * 
	 * by clicking outside the dialog or clicking on dialog item.
	 */

	public void setOnDismissListener(QuickAction.OnDismissListener listener) {

		setOnDismissListener(this);

		mDismissListener = listener;

	}

	@Override
	public void onDismiss() {

		if (!mDidAction && mDismissListener != null) {

			mDismissListener.onDismiss();

		}

	}

	/**
	 * 
	 * Listener for window dismiss
	 * 
	 * 
	 */

	public interface OnDismissListener {

		public abstract void onDismiss();

	}

}
