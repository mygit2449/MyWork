package com.daleelo.Dialog;

import android.app.AlertDialog;
import android.content.Context;

public class LocationNotFoundDialog extends AlertDialog.Builder{
	
	Context context;
	public LocationNotFoundDialog(Context context) 
	{
		super(context);
		this.setMessage("Unable to get current location please select the location manually.");
		this.setTitle("Daleelo");
		this.setCancelable(false);
	}

}
