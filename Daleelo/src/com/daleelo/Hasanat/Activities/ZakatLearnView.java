package com.daleelo.Hasanat.Activities;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.daleelo.R;

public class ZakatLearnView extends Activity implements OnClickListener{

	private TextView mDescription_TXT;
	private Button mBTN_Faquaraa,mBTN_Masakeen,mBTN_Ameleena,mBTN_Qolobohom,mBTN_Riqab,mBTN_Gharimeena,mBTN_lbn, mBTN_fisabi;
	private TextView mTXT_Faquaraa,mTXT_Masakeen,mTXT_Ameleena,mTXT_Qolobohom,mTXT_Riqab,mTXT_Gharimeena,mTXT_lbn, mTXT_fisabi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zakat_learn);
		
		initiallizeUI();
	}

	public void initiallizeUI(){
		
		mBTN_Faquaraa = (Button)findViewById(R.id.zakat_learn_BTN_fuqaraa);
		mBTN_Masakeen = (Button)findViewById(R.id.zakat_learn_BTN_masakeen);
		mBTN_Ameleena = (Button)findViewById(R.id.zakat_learn_BTN_ameleenaalayha);
		mBTN_Qolobohom = (Button)findViewById(R.id.zakat_learn_BTN_muallafaqolobohom);
		mBTN_Riqab = (Button)findViewById(R.id.zakat_learn_BTN_raqab);
		mBTN_Gharimeena = (Button)findViewById(R.id.zakat_learn_BTN_ghrimeen);
		mBTN_fisabi = (Button)findViewById(R.id.zakat_learn_BTN_fisabilillah);
		mBTN_lbn = (Button)findViewById(R.id.zakat_learn_BTN_lbnassabil);
		
		
		mTXT_Faquaraa = (TextView)findViewById(R.id.zakat_learn_TXT_fuqaraa);
		mTXT_Masakeen = (TextView)findViewById(R.id.zakat_learn_TXT_masakeen);
		mTXT_Ameleena = (TextView)findViewById(R.id.zakat_learn_TXT_ameleenaalayha);
		mTXT_Qolobohom = (TextView)findViewById(R.id.zakat_learn_TXT_muallafaqolobohom);
		mTXT_Riqab = (TextView)findViewById(R.id.zakat_learn_TXT_raqab);
		mTXT_Gharimeena = (TextView)findViewById(R.id.zakat_learn_TXT_ghrimeen);
		mTXT_fisabi = (TextView)findViewById(R.id.zakat_learn_TXT_fisabilillah);
		mTXT_lbn = (TextView)findViewById(R.id.zakat_learn_TXT_lbnassabil);


		mBTN_Faquaraa.setOnClickListener(this);
		mBTN_Masakeen.setOnClickListener(this);
		mBTN_Ameleena.setOnClickListener(this);
		mBTN_Qolobohom.setOnClickListener(this);
		mBTN_Riqab.setOnClickListener(this);
		mBTN_Gharimeena.setOnClickListener(this);
		mBTN_fisabi.setOnClickListener(this);
		mBTN_lbn.setOnClickListener(this);
		
		
		
		mDescription_TXT = (TextView)findViewById(R.id.zakat_learn_TXT_heading);
		mDescription_TXT.setText(Html.fromHtml("<html> <b>"+
				"<font color=\"#008ee4\">Zakat:</font> </b>"+
				"<font color=\"black\"> The third pillar of Islam. It is mandatory when two  conditions are met, " +
				"which are \"Nisab\" and \"Due Date\"</font><b>"+
				"<BR/><BR/>"+
				"<font color=\"#008ee4\">Nisab:</font></b>"+
				"<font color=\"black\"> Nisab is a minimum amount of wealth you must own in order to be liable to pay Zakat. " +
				"It is the price of 3 oz. (85 Grams) of pure gold, on the day in which Zakat is to be paid.</font> " +
				"<a href=\"http://www.goldprice.org\"><BR/><font color=\"#261BE6\">Current Gold Price</font></a>"+
				"<BR /><b><BR/>"+
				"<font color=\"#008ee4\">Due Date:</font></b>"+
				"<font color=\"black\"> Zakat is due after a time span of one lunar year (Hijri) passes while you own the Nisab, " +
				"and if you always possess the minimum Nisab value, then Zakat is due once every year at " +
				"a date of your choosing.</font>"+
				"<BR><BR><b><font color=\"#008ee4\">The 8 Recipients of Zakat</font></b></BR></BR>"+
				"<BR><BR><font color=\"black\">From the Qur’anic verse ordaining zakat, " +
				"eight classes of recipients have been identified</font></BR></BR>"+
				"<BR><BR><font color=\"black\">\"Alms are for the poor and the needy, and those employed to administer the (funds);" +
				" for those whose hearts have been (recently_ reconciled (to the truth); for those in bondage and " +
				"in debt; in the cause of God; and for the wayfarer: (thus is it) ordained by God, and God is full of knowledge and" +
				" wisdom.\"(Quran 9:60)</font></BR></BR></html>"));
		
		mDescription_TXT.setMovementMethod(LinkMovementMethod.getInstance());

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Drawable img, img_next;
		switch (v.getId()) {
		
		case R.id.zakat_learn_BTN_fuqaraa:
			
			if (mTXT_Faquaraa.getVisibility() == View.VISIBLE) {
				
				mTXT_Faquaraa.setVisibility(View.GONE);
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_Faquaraa.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );
				
				}else{
					
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_orangebtn);
				mTXT_Faquaraa.setVisibility(View.VISIBLE);
				mBTN_Faquaraa.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );
				img_next = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_Masakeen.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Ameleena.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Qolobohom.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Riqab.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Gharimeena.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_fisabi.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_lbn.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				
				}
			mTXT_Masakeen.setVisibility(View.GONE);
			mTXT_Ameleena.setVisibility(View.GONE);
			mTXT_Qolobohom.setVisibility(View.GONE);
			mTXT_Riqab.setVisibility(View.GONE);
			mTXT_Gharimeena.setVisibility(View.GONE);
			mTXT_fisabi.setVisibility(View.GONE);
			mTXT_lbn.setVisibility(View.GONE);
			
			break;

		case R.id.zakat_learn_BTN_masakeen:
			
			if (mTXT_Masakeen.getVisibility() == View.VISIBLE){ 
				
				mTXT_Masakeen.setVisibility(View.GONE);
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_Masakeen.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );
				
				
			}else{
				
				mTXT_Masakeen.setVisibility(View.VISIBLE);
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_orangebtn);
				mBTN_Masakeen.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );
				img_next = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_Ameleena.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Qolobohom.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Riqab.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Gharimeena.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_fisabi.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_lbn.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Faquaraa.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
			}
			
			mTXT_Faquaraa.setVisibility(View.GONE);
			mTXT_Ameleena.setVisibility(View.GONE);
			mTXT_Qolobohom.setVisibility(View.GONE);
			mTXT_Riqab.setVisibility(View.GONE);
			mTXT_Gharimeena.setVisibility(View.GONE);
			mTXT_fisabi.setVisibility(View.GONE);
			mTXT_lbn.setVisibility(View.GONE);
			
			break;
		case R.id.zakat_learn_BTN_ameleenaalayha:
			
			if (mTXT_Ameleena.getVisibility() == View.VISIBLE){ 
				
				mTXT_Ameleena.setVisibility(View.GONE);
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_Ameleena.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );

			}else{
				
				mTXT_Ameleena.setVisibility(View.VISIBLE);
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_orangebtn);
				mBTN_Ameleena.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );
				img_next = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_Faquaraa.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Masakeen.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Qolobohom.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Riqab.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Gharimeena.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_fisabi.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_lbn.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
			}
			
			mTXT_Faquaraa.setVisibility(View.GONE);
			mTXT_Masakeen.setVisibility(View.GONE);
			mTXT_Qolobohom.setVisibility(View.GONE);
			mTXT_Riqab.setVisibility(View.GONE);
			mTXT_Gharimeena.setVisibility(View.GONE);
			mTXT_fisabi.setVisibility(View.GONE);
			mTXT_lbn.setVisibility(View.GONE);
	
			break;
		case R.id.zakat_learn_BTN_muallafaqolobohom:
	
			if (mTXT_Qolobohom.getVisibility() == View.VISIBLE){ 
				mTXT_Qolobohom.setVisibility(View.GONE);
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_Qolobohom.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );
				
			}else{
				mTXT_Qolobohom.setVisibility(View.VISIBLE);
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_orangebtn);
				mBTN_Qolobohom.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );
				img_next = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_Faquaraa.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Masakeen.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Ameleena.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Riqab.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Gharimeena.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_fisabi.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_lbn.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
			}
			mTXT_Faquaraa.setVisibility(View.GONE);
			mTXT_Masakeen.setVisibility(View.GONE);
			mTXT_Ameleena.setVisibility(View.GONE);
			mTXT_Riqab.setVisibility(View.GONE);
			mTXT_Gharimeena.setVisibility(View.GONE);
			mTXT_fisabi.setVisibility(View.GONE);
			mTXT_lbn.setVisibility(View.GONE);
			
			break;
		case R.id.zakat_learn_BTN_raqab:
	
			if (mTXT_Riqab.getVisibility() == View.VISIBLE){ 
				mTXT_Riqab.setVisibility(View.GONE);
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_Riqab.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );
			}else{
				mTXT_Riqab.setVisibility(View.VISIBLE);
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_orangebtn);
				mBTN_Riqab.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );
				img_next = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_Faquaraa.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Masakeen.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Qolobohom.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Ameleena.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Gharimeena.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_fisabi.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_lbn.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
			}
			mTXT_Faquaraa.setVisibility(View.GONE);
			mTXT_Masakeen.setVisibility(View.GONE);
			mTXT_Ameleena.setVisibility(View.GONE);
			mTXT_Qolobohom.setVisibility(View.GONE);
			mTXT_Gharimeena.setVisibility(View.GONE);
			mTXT_fisabi.setVisibility(View.GONE);
			mTXT_lbn.setVisibility(View.GONE);
			
			break;
		case R.id.zakat_learn_BTN_ghrimeen:
	
			if (mTXT_Gharimeena.getVisibility() == View.VISIBLE){ 
				
				mTXT_Gharimeena.setVisibility(View.GONE);
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_Gharimeena.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );
				
			}else{
				
				mTXT_Gharimeena.setVisibility(View.VISIBLE);
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_orangebtn);
				mBTN_Gharimeena.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );
				img_next = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_Faquaraa.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Masakeen.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Qolobohom.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Riqab.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Ameleena.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_fisabi.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_lbn.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				
			}
			
			mTXT_Faquaraa.setVisibility(View.GONE);
			mTXT_Masakeen.setVisibility(View.GONE);
			mTXT_Ameleena.setVisibility(View.GONE);
			mTXT_Qolobohom.setVisibility(View.GONE);
			mTXT_Riqab.setVisibility(View.GONE);
			mTXT_fisabi.setVisibility(View.GONE);
			mTXT_lbn.setVisibility(View.GONE);
			
			break;
		case R.id.zakat_learn_BTN_fisabilillah:
			
			if (mTXT_fisabi.getVisibility() == View.VISIBLE){ 
				
				mTXT_fisabi.setVisibility(View.GONE);
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_fisabi.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );
		
			}else{
				
				mTXT_fisabi.setVisibility(View.VISIBLE);
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_orangebtn);
				mBTN_fisabi.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );
				img_next = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_Faquaraa.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Masakeen.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Qolobohom.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Riqab.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Gharimeena.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Ameleena.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_lbn.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
			}
			
			mTXT_Faquaraa.setVisibility(View.GONE);
			mTXT_Masakeen.setVisibility(View.GONE);
			mTXT_Ameleena.setVisibility(View.GONE);
			mTXT_Qolobohom.setVisibility(View.GONE);
			mTXT_Riqab.setVisibility(View.GONE);
			mTXT_Gharimeena.setVisibility(View.GONE);
			mTXT_lbn.setVisibility(View.GONE);
//	
			break;
		case R.id.zakat_learn_BTN_lbnassabil:
			
			if (mTXT_lbn.getVisibility() == View.VISIBLE){ 
				
				mTXT_lbn.setVisibility(View.GONE);
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_lbn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );
				
			}else{
				
				mTXT_lbn.setVisibility(View.VISIBLE);
				img = getBaseContext().getResources().getDrawable(R.drawable.faq_orangebtn);
				mBTN_lbn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null );
				img_next = getBaseContext().getResources().getDrawable(R.drawable.faq_bluebtn);
				mBTN_Faquaraa.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Masakeen.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Qolobohom.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Riqab.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Gharimeena.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_fisabi.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				mBTN_Ameleena.setCompoundDrawablesWithIntrinsicBounds(img_next, null, null, null );
				
			}
			
			mTXT_Faquaraa.setVisibility(View.GONE);
			mTXT_Masakeen.setVisibility(View.GONE);
			mTXT_Ameleena.setVisibility(View.GONE);
			mTXT_Qolobohom.setVisibility(View.GONE);
			mTXT_Riqab.setVisibility(View.GONE);
			mTXT_Gharimeena.setVisibility(View.GONE);
			mTXT_fisabi.setVisibility(View.GONE);
			
			break;
		}
	}
}
