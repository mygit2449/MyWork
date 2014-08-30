package com.daleelo.Hasanat.Activities;

import java.math.BigDecimal;

import net.authorize.Merchant;
import net.authorize.TransactionType;
import net.authorize.aim.Result;
import net.authorize.aim.Transaction;
import net.authorize.data.EmailReceipt;
import net.authorize.data.creditcard.CreditCard;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.Utilities.Utils;

public class HasanatDonateActivity extends Activity implements OnClickListener{

	private EditText medt_DonateAmt, medt_DonateName,medt_DonateCCNumber, medt_DonateCCType, medt_DonateCity ;
	private EditText medt_DonateCvv2Code, medt_DonateExpDate, medt_DonateState, medt_DonateZipCode, medt_DonateBillingAddress;
	private Button mbtn_Submit;
	private CheckBox mchkbx_Terms;

	private int mPaymentAmount;

	private String paymentResponce_Str;
	
	final private String mApiLoginId = "9AmH6s8Cg";
	final private String mTransactionKey = "4Hz9YjGg585M5m3g";
	
	private Result<Transaction> result;	

	private int amount;
	boolean mStatus = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hasanat_donate_now);
		initializeUI();
		
		
		
		Log.v(getClass().getSimpleName(), "zakat due amount "+amount+" status "+mStatus);
		medt_DonateAmt.setText(""+Utils.mZakatDueAmount);
		mbtn_Submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.mZakatDueAmount = 0;
				if(validateFields().equalsIgnoreCase(" ")){
					
					new paymentAsync().execute();
					
					}else{
					alertDialog(validateFields());
				}
			}
		});
	}
	
	
	
	private void initializeUI(){
		
		medt_DonateAmt = (EditText)findViewById(R.id.hasanat_donate_EDT_amt);
		medt_DonateName = (EditText)findViewById(R.id.hasanat_donate_EDT_cardholdername);
		medt_DonateCCNumber = (EditText)findViewById(R.id.hasanat_donate_EDT_ccnumber);
		medt_DonateCCType = (EditText)findViewById(R.id.hasanat_donate_EDT_cctype);
		medt_DonateCity = (EditText)findViewById(R.id.hasanat_donate_EDT_city);
		medt_DonateCvv2Code = (EditText)findViewById(R.id.hasanat_donate_EDT_cvv2code);
		medt_DonateExpDate = (EditText)findViewById(R.id.hasanat_donate_EDT_exp_date);
		medt_DonateState = (EditText)findViewById(R.id.hasanat_donate_EDT_state);
		medt_DonateZipCode = (EditText)findViewById(R.id.hasanat_donate_EDT_zipcode);
		medt_DonateBillingAddress = (EditText)findViewById(R.id.hasanat_donate_EDT_billingadd);
		
		mbtn_Submit = (Button)findViewById(R.id.hasanat_donate_BTN_submit);
		
		mchkbx_Terms = (CheckBox)findViewById(R.id.hasanat_donate_chkbox);
		mchkbx_Terms.setOnClickListener(this);
		
	}
	
	/**
	    * validating the input parameters.
	    * @return a string  message.
	    */
		private String validateFields(){
			
			if(medt_DonateAmt.getText().toString().length() == 0)
				
				return  "Please enter donation amount";
			
			
			if(medt_DonateBillingAddress.getText().toString().length() == 0)
				
				return "Please enter valid billing address";
			
			if(medt_DonateCity.getText().toString().length() == 0)
				
				return "Please enter valid billing address";
			
			if(medt_DonateCCType.getText().toString().length() == 0)
				
				return  "Please enter valid cc type";
			
			if(medt_DonateZipCode.getText().toString().length() == 0 || medt_DonateZipCode.getText().toString().length() > 5)
				
				return "Please enter valid zip code";
			
				
			if(medt_DonateName.getText().toString().length() == 0)
							
				return  "Please enter Card Holder name";


			if(medt_DonateCCNumber.getText().toString().length() < 13 || medt_DonateCCNumber.getText().toString().length() > 16)
				
				return "Credit card number in between 13 - 16 digits";
			
			
			if(medt_DonateExpDate.getText().toString().length() == 0)
				
				return  "Please enter valid experie date";
			
			
			if(medt_DonateCvv2Code.getText().toString().length() != 3)
				
				return "Cvv should have 3 digits";
			
			
			if(medt_DonateState.getText().toString().length() == 0)
				
				return "Please enter valid state";
						
			return " ";
																					
			
		}
	private void alertDialog(String msg){
		
		
		new AlertDialogMsg(HasanatDonateActivity.this, msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				HasanatDonateActivity.this.finish();				
			}
			
		}).create().show();
		
		
	}
	
	
	/**
	 *  Async task  Authorized.Net payment.
	 */
		

	class  paymentAsync extends AsyncTask<Void, Void, Void>{

        ProgressDialog mProgressDialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
	        mProgressDialog = ProgressDialog.show(HasanatDonateActivity.this, "", "Please Wait........");
		}
		
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			authorizePayment();
			return null;
		}

		@Override
		protected void onPostExecute(Void result1) {
			// TODO Auto-generated method stub
			super.onPostExecute(result1);
			mProgressDialog.dismiss();
			
			if (result.isApproved()) {
				paymentResponce_Str = HasanatDonateActivity.this.result.getResponseText();
				String transactionId = "";
//				Toast.makeText(HasanatDonateActivity.this, "Approved: "+result.getResponseText(), Toast.LENGTH_SHORT).show();
				transactionId = HasanatDonateActivity.this.result.getTarget().getTransactionId();
				alertDialog(paymentResponce_Str);
			}else if (result.isDeclined()) {

				String transactionId = "";
				paymentResponce_Str = HasanatDonateActivity.this.result.getResponseText();
//				Toast.makeText(HasanatDonateActivity.this, "Declined: "+result.getResponseText(), Toast.LENGTH_SHORT).show();
				transactionId = HasanatDonateActivity.this.result.getTarget().getTransactionId();
				alertDialog(paymentResponce_Str);
			}else {

				String transactionId = "";
				paymentResponce_Str = HasanatDonateActivity.this.result.getResponseText();
//				Toast.makeText(HasanatDonateActivity.this, "error: "+result.getResponseText(), Toast.LENGTH_SHORT).show();
				transactionId = HasanatDonateActivity.this.result.getTarget().getTransactionId();
				alertDialog(paymentResponce_Str);
			}
		}
	}
	
	/**
	 *  method for payment transaction in authorized .net
	 */
	
	private void authorizePayment(){
		
		Merchant merchant = Merchant.createMerchant(net.authorize.Environment.SANDBOX, mApiLoginId, mTransactionKey);

		String amount = medt_DonateExpDate.getText().toString();
		Log.v(getClass().getSimpleName(), " date "+medt_DonateExpDate.toString());
		String[] arrDate = amount.split("/");
		String mExpirationMonth = arrDate[0];
		String mExpirationYear = arrDate[1];
		
		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCreditCardNumber(medt_DonateCCNumber.getText().toString());
		creditCard.setExpirationMonth(mExpirationMonth);
		creditCard.setExpirationYear(mExpirationYear);
		
		EmailReceipt.createEmailReceipt();
		
		
		mPaymentAmount = Integer.parseInt(medt_DonateAmt.getText().toString());
		
		Transaction captureTransaction = merchant.createAIMTransaction(TransactionType.AUTH_CAPTURE,
				new BigDecimal(mPaymentAmount));
		captureTransaction.setCreditCard(creditCard);
		
		result = (Result<Transaction>) merchant.postTransaction(captureTransaction);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.hasanat_donate_chkbox:
			
			if (mchkbx_Terms.isChecked()) {
				Log.v("check box ", "check box");
				mbtn_Submit.setClickable(true);
				mbtn_Submit.setBackgroundResource(R.drawable.btn_icon_submit_long);
			}else{
				mbtn_Submit.setClickable(false);
				mbtn_Submit.setBackgroundResource(R.drawable.btn_icon_submit_long_disable);

			}
			
			break;
		}
	}

}
