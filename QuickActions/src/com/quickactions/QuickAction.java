package com.quickactions;

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

public class QuickAction extends PopupWindows implements android.widget.PopupWindow.OnDismissListener{

	private ImageView mArrowUp;

	private ImageView mArrowDown;

	private LayoutInflater inflater;

	TextView quickaction_text;

	private OnDismissListener mDismissListener;

	private boolean mDidAction;

	private int mChildPos;

	/**
	 * 
	 * Constructor.
	 * 
	 * 
	 * 
	 * @param context
	 *            Context
	 */

	public QuickAction(Context context) {

		super(context);

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		setRootViewId(R.layout.quickaction);

		mChildPos = 0;

	}

	/**
	 * 
	 * Get action item at an index
	 * 
	 * 
	 * 
	 * @param index
	 *            Index of item (position from callback)
	 * 
	 * 
	 * 
	 * @return Action Item at the position
	 */

	/**
	 * 
	 * Set root view.
	 * 
	 * 
	 * 
	 * @param id
	 *            Layout resource id
	 */

	public void setRootViewId(int id) {

		mRootView = (ViewGroup) inflater.inflate(id, null);

		mArrowDown = (ImageView) mRootView.findViewById(R.id.arrow_down);

		mArrowUp = (ImageView) mRootView.findViewById(R.id.arrow_up);

		quickaction_text = (TextView) mRootView
				.findViewById(R.id.quickaction_text);

		// This was previously defined on show() method, moved here to prevent
		// force close that occured

		// when tapping fastly on a view to show quickaction dialog.

		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		setContentView(mRootView);

	}

	/**
	 * 
	 * Show popup mWindow
	 */

	public void show(View anchor) {

		preShow();

		int[] location = new int[2];

		mDidAction = false;

		anchor.getLocationOnScreen(location);

		Rect anchorRect = new Rect(location[0], location[1], location[0]
				+ anchor.getWidth(), location[1]

		+ anchor.getHeight());

		mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		int rootWidth = mRootView.getMeasuredWidth();

		int rootHeight = mRootView.getMeasuredHeight();

		int screenWidth = mWindowManager.getDefaultDisplay().getWidth();

		// int screenHeight = mWindowManager.getDefaultDisplay().getHeight();

		int xPos = (screenWidth - rootWidth) / 2;

		int yPos = anchorRect.top - rootHeight;

		boolean onTop = true;

		// display on bottom

		Log.v(getClass().getSimpleName(), " anchor.getTop() "+anchor.getTop());
		if (rootHeight > anchor.getTop()) {

			yPos = anchorRect.bottom;
			Log.v(getClass().getSimpleName(), "onTop1 "+onTop);
			onTop = false;

		}

		Log.v(getClass().getSimpleName(), "onTop2 "+onTop+" xposition "+xPos+" yPos"+yPos+"anchor.getTop() "+anchor.getTop());

//		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up),anchorRect.centerX());

		int id;
		if (onTop) 
			id = R.id.arrow_down;
		else
			id = R.id.arrow_up;
		
		showArrow(id, anchorRect.centerX());
		
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
		
	}

	/**
	 * 
	 * Show arrow
	 * 
	 * 
	 * 
	 * @param whichArrow
	 *            arrow type resource id
	 * 
	 * @param requestedX
	 *            distance from left screen
	 */

	private void showArrow(int whichArrow, int requestedX) {

		final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp
				: mArrowDown;

		final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown
				: mArrowUp;

		final int arrowWidth = mArrowUp.getMeasuredWidth();

		showArrow.setVisibility(View.VISIBLE);

		ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow
				.getLayoutParams();

		param.leftMargin = requestedX - arrowWidth / 2;

		hideArrow.setVisibility(View.INVISIBLE);

	}

	/**
	 * 
	 * Set listener for window dismissed. This listener will only be fired if
	 * the quicakction dialog is dismissed
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

	public void setText(String text)

	{

		quickaction_text.setText(text);

	}
}
