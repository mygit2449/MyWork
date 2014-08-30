package com.daleelo.Hasanat.Activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.Ads.Activities.QuickAction;
import com.daleelo.Hasanat.Model.BusinessDetailModel;
import com.daleelo.Utilities.Utils;

public class ZakatCalculatorActivity extends Activity implements OnClickListener{

	private ImageButton mImgBtn_cash, mImgBtn_receivables, mImgBtn_stocks, mImgBtn_gold, mImgBtn_businesscash;
	private ImageButton mImgBtn_totalamt, mImgBtn_zakatdue;
	private EditText mETXT_cash, mETXT_goldAndSilver, mETXT_receivablesAndLoans, 
					 mETXT_stockesAndInvestment, mETXT_bcashAndInventories, mETXT_totalAmount, mETXT_zakatDue;
	private Button mReset_Btn, mCaluculate_Btn, mGive_Btn;
	private LayoutInflater inflater;

	private Intent receiverIntent;
	
	private ArrayList<BusinessDetailModel> mFeaturedDataList;
	
	private String mCategoryId = "";

	private QuickAction mQuickAction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zakat_calculator);
		initializeUI();
		
		receiverIntent = getIntent();
		
		mQuickAction = new QuickAction(ZakatCalculatorActivity.this);
		mFeaturedDataList = (ArrayList<BusinessDetailModel>) receiverIntent.getExtras().get("featuredData");
		mCategoryId = receiverIntent.getExtras().getString("categoryId");
	}
	
	private void initializeUI(){
		
		mImgBtn_cash = (ImageButton)findViewById(R.id.zakat_calculator_IMGB_cash);
		mImgBtn_receivables = (ImageButton)findViewById(R.id.zakat_calculator_IMGB_recievables);
		mImgBtn_stocks = (ImageButton)findViewById(R.id.zakat_calculator_IMGB_stocks);
		mImgBtn_gold = (ImageButton)findViewById(R.id.zakat_calculator_IMGB_gold);
		mImgBtn_businesscash = (ImageButton)findViewById(R.id.zakat_calculator_IMGB_businesscash);
		mImgBtn_totalamt = (ImageButton)findViewById(R.id.zakat_calculator_IMGB_totalamt);
		mImgBtn_zakatdue = (ImageButton)findViewById(R.id.zakat_calculator_IMGB_zakatdue);
		
		mETXT_cash = (EditText)findViewById(R.id.zakat_calculator_EDT_cash);
		mETXT_goldAndSilver = (EditText)findViewById(R.id.zakat_calculator_EDT_gold);
		mETXT_receivablesAndLoans = (EditText)findViewById(R.id.zakat_calculator_EDT_recievables);
		mETXT_stockesAndInvestment = (EditText)findViewById(R.id.zakat_calculator_EDT_stocks);
		mETXT_bcashAndInventories = (EditText)findViewById(R.id.zakat_calculator_EDT_businesscash);
		mETXT_totalAmount = (EditText)findViewById(R.id.zakat_calculator_EDT_totalamt);
		mETXT_zakatDue = (EditText)findViewById(R.id.zakat_calculator_EDT_zakatdue);
		
		mReset_Btn = (Button)findViewById(R.id.zakat_calculator_BTN_reset);
		mCaluculate_Btn = (Button)findViewById(R.id.zakat_calculator_BTN_calculate);
		mGive_Btn = (Button)findViewById(R.id.zakat_calculator_BTN_give);
		
		
		mImgBtn_businesscash.setOnClickListener(this);
		mImgBtn_cash.setOnClickListener(this);
		mImgBtn_stocks.setOnClickListener(this);
		mImgBtn_gold.setOnClickListener(this);
		mImgBtn_receivables.setOnClickListener(this);
		mImgBtn_totalamt.setOnClickListener(this);
		mImgBtn_zakatdue.setOnClickListener(this);
		
		mReset_Btn.setOnClickListener(this);
		mCaluculate_Btn.setOnClickListener(this);
		mGive_Btn.setOnClickListener(this);
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.zakat_calculator_IMGB_cash:

			inflater = ZakatCalculatorActivity.this.getLayoutInflater();
			mQuickAction.show(mImgBtn_cash,2, getResources().getString(R.string.help1));

			break;
			
		case R.id.zakat_calculator_IMGB_gold:

			inflater = ZakatCalculatorActivity.this.getLayoutInflater();
			mQuickAction.show(mImgBtn_gold,3, getResources().getString(R.string.help2));

			break;
			
		case R.id.zakat_calculator_IMGB_recievables:

			inflater = ZakatCalculatorActivity.this.getLayoutInflater();
			mQuickAction.show(mImgBtn_receivables,4, getResources().getString(R.string.help3));

			break;
			
		case R.id.zakat_calculator_IMGB_businesscash:

			inflater = ZakatCalculatorActivity.this.getLayoutInflater();
			mQuickAction.show(mImgBtn_businesscash,2, getResources().getString(R.string.help4));

			break;
			
		case R.id.zakat_calculator_IMGB_stocks:

			inflater = ZakatCalculatorActivity.this.getLayoutInflater();
			mQuickAction.show(mImgBtn_stocks,2, getResources().getString(R.string.help5));

			break;
			
		case R.id.zakat_calculator_IMGB_totalamt:

			inflater = ZakatCalculatorActivity.this.getLayoutInflater();
			mQuickAction.show(mImgBtn_totalamt,2, getResources().getString(R.string.help6));

			break;
			
		case R.id.zakat_calculator_IMGB_zakatdue:

			inflater = ZakatCalculatorActivity.this.getLayoutInflater();
			mQuickAction.show(mImgBtn_zakatdue,4, getResources().getString(R.string.help7));

			break;

			
		case R.id.zakat_calculator_BTN_calculate:
			
			int personalcash = (int) Float.parseFloat(mETXT_cash.getText().toString());
			int goldAndsilver = (int) Float.parseFloat(mETXT_goldAndSilver.getText().toString());
			int receivablesAndloans = (int) Float.parseFloat(mETXT_receivablesAndLoans.getText().toString());
			int stokesAndinvestments = (int) Float.parseFloat(mETXT_stockesAndInvestment.getText().toString());
			int bcashAndinventories = (int) Float.parseFloat(mETXT_bcashAndInventories.getText().toString());

			int totalamount = personalcash + goldAndsilver + receivablesAndloans + stokesAndinvestments + bcashAndinventories;
			
			Log.v(getClass().getSimpleName(), " amount "+totalamount+" zakat due "+totalamount * 0.025);

			if (totalamount < 4700) {
				mETXT_totalAmount.setText(""+totalamount);
				mETXT_zakatDue.setText("0.0");
				Utils.mZakatDueAmount = 0;
			}else{
				Utils.mZakatDueAmount = (int) (totalamount * 0.025);
				mETXT_totalAmount.setText(""+totalamount);
				mETXT_zakatDue.setText(""+Utils.mZakatDueAmount);
			}
			
			Log.v(getClass().getSimpleName(), " amount "+totalamount+" zakat due "+totalamount * 0.025);
			break;
			
		case R.id.zakat_calculator_BTN_reset:
			
			mETXT_cash.setText("0.0");
			mETXT_goldAndSilver.setText("0.0");
			mETXT_receivablesAndLoans.setText("0.0");
			mETXT_stockesAndInvestment.setText("0.0");
			mETXT_bcashAndInventories.setText("0.0");
			mETXT_totalAmount.setText("0.0");
			mETXT_zakatDue.setText("0.0");
			
			break;
			
		case R.id.zakat_calculator_BTN_give:
			
			startActivity(new Intent(ZakatCalculatorActivity.this, GiveActivity.class).putExtra("featuredData", mFeaturedDataList).putExtra("categoryId", mCategoryId));

			break;
			
		default:
			break;
		}
		
	}

}
