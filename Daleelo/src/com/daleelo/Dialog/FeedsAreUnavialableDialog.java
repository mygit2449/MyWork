package com.daleelo.Dialog;

import android.app.AlertDialog;
import android.content.Context;

public class FeedsAreUnavialableDialog extends AlertDialog.Builder{
	
	Context context;
	public FeedsAreUnavialableDialog(Context context) 
	{
		super(context);
		this.setMessage("No Feed is available!!");
		this.setTitle("Daleelo");
	}

}
