package com.skeyedex.Settings;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skeyedex.R;
import com.skeyedex.EmailDataBase.ContactsDataBase;
import com.skeyedex.Model.ContactsModel;

public class PhoneContactDetails extends Activity implements OnItemClickListener 
{

	private String emailIdOfContact;
	private int emailType;
	private TextView emailText;
	private int phonecontactId = 0;
	
	ArrayList<ContactsModel> total_Contacts = null;
	ArrayList<ContactsModel> mEmail_Ids = null;
	
	ContactsModel mContactsModel = null;
	ContactsDataBase mContactsDataBase = null;
	DisplayContactsAdapter mDisplayContactsAdapter = null;
	ListView contacts_list;

	@Override
	public void onCreate(Bundle settingsInstance)
	{
		super.onCreate(settingsInstance);
		setContentView(R.layout.contacts_list);
		contacts_list = (ListView) findViewById(R.id.contacts_listview);
		contacts_list.setOnItemClickListener(this);
	}
	
	@Override
    public void onResume()
    {
    	super.onResume();
		
    	EmailAsync mEmailAsync = new EmailAsync(this);
    	mEmailAsync.execute();
    
    }

	
	
	
	public class EmailAsync extends AsyncTask<String, Void, Void>
	{
  
		
		Context mContext;
		ProgressDialog mDialog;
		String mErrorMsg = "";
		int type;
		
		public EmailAsync(Context context) 
		{
			
			mContext = context;
			mDialog = ProgressDialog.show(context, "", "Loading Contacts....", true, false);
			mDialog.setCancelable(true);
		
		}
		
		@Override
		protected Void doInBackground(String... params) 
		{
			try
			{
				
				total_Contacts = new ArrayList<ContactsModel>();
				total_Contacts = displayContacts();
			    Log.e("Phone Contacts ", " count "+total_Contacts.size());
			    
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				mErrorMsg = "Error in processing Emails ," + e.getMessage();
			}
			return null;
		}
		
		public void onPostExecute(Void result) 
		{
			
			mDialog.dismiss();
			mDisplayContactsAdapter = new DisplayContactsAdapter(PhoneContactDetails.this, total_Contacts);
			contacts_list.setAdapter(mDisplayContactsAdapter);
			
		}
	}
	
	private ArrayList<ContactsModel> displayContacts()
	{
		ArrayList<ContactsModel> contact_details = new ArrayList<ContactsModel>();
		
		ContentResolver cr = getContentResolver();
		Cursor cursor = null;
		
		try
		{
			//
		    //  Find contact based on name.
		    //
		    cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,null, null, null);
		    
		    Log.v("Phone contacts", " no of contacts " + cursor.getCount());
		    while(cursor.moveToNext()) 
		    {
		    	 mContactsModel = new ContactsModel();
			     String contactId =cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
		         String name = cursor.getString( cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		         mContactsModel.setContact_name(name);
		         mContactsModel.setContact_id(contactId);
			
		    	 Log.v("Phone contacts", "The display name is " + name + "contact id "+contactId);
		    	
		    	
		         Cursor phones = cr.query(Phone.CONTENT_URI, null,Phone.CONTACT_ID + " = " + contactId, null, null);
		       
		         while (phones.moveToNext()) 
		         {
		        	 
		            String number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
		            phonecontactId = phones.getInt(phones.getColumnIndex(PhoneLookup._ID));
		            Log.v("contact Id", "contact id"+phonecontactId);
		            int type = phones.getInt(phones.getColumnIndex(Phone.TYPE));
		            
		            switch (type) 
		            {
		                case Phone.TYPE_HOME:
		                	Log.v("Phone contacts", "The mobile no is  " + number);
		                	mContactsModel.setPhone_number(number);
		                	phonecontactId = getContactIDFromNumber(number, PhoneContactDetails.this);

		                    // do something with the Home number here...
		                    break;
		                case Phone.TYPE_MOBILE:
		                	Log.v("Phone contacts", "The mobile no is  " + number);
		                
		                		mContactsModel.setPhone_number(number);
			                	phonecontactId = getContactIDFromNumber(number, PhoneContactDetails.this);
			                	Log.v("phone", "Contact Id"+phonecontactId);
							
		                	// do something with the Mobile number here...
		                    break;
		                case Phone.TYPE_WORK:
		                	Log.v("Phone contacts", "The mobile no is  " + number);
		                	mContactsModel.setPhone_number(number);
		                	phonecontactId = getContactIDFromNumber(number, PhoneContactDetails.this);
		                    // do something with the Work number here...
		                    break;
		                }
		      
		        
		        }
		        phones.close();
		        //
		        //  Get all email addresses.
		        //
		        Cursor emails = cr.query(Email.CONTENT_URI, null,Email.CONTACT_ID + " = " + contactId, null, null);
		        while (emails.moveToNext()) 
		        {
		        	
		            String email = emails.getString(emails.getColumnIndex(Email.DATA));
		            int type = emails.getInt(emails.getColumnIndex(Phone.TYPE));
		            switch (type)
		            {
		                case Email.TYPE_HOME:
		                	Log.v("Phone contacts", "The email id  is  " + email);
		                		mContactsModel.setEmail_id(email);
							
		                    break;
		                case Email.TYPE_WORK:
		                    // do something with the Work email here...
		                	Log.v("Phone contacts", "The email id  is  TYPE_WORK" + email);
		                		mContactsModel.setEmail_id(email);
				        	
		                    break;
		                  
		                case Email.TYPE_MOBILE:

		                	Log.v("Phone contacts", "The email id  is TYPE_MOBILE " + email);
		                		mContactsModel.setEmail_id(email);
				            break;
		                case Email.TYPE_OTHER:
		                	
		                	Log.v("Phone contacts", "The email id  is  TYPE_OTHER" + email);
		                		mContactsModel.setEmail_id(email);
						    break;
		            }
		        }
		        emails.close();
		        
		        contact_details.add(mContactsModel);
				mContactsModel = null;
		    }
		    cursor.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally{
			try{
				cursor.close();
			}catch(Exception ex){}
		}
		return contact_details;
}


	
	
	// Adapter For Displaying Conatct Name And Email_Id
	class DisplayContactsAdapter extends ArrayAdapter<ContactsModel> 
	{

		ArrayList<ContactsModel> display_Array;
		Context context;
        
		public DisplayContactsAdapter(Context context,ArrayList<ContactsModel> result) 
		{
			super(context, R.layout.contact_details_row, result);
			this.context = context;
			this.display_Array = result;
			Log.v("display array size", ""+display_Array.size());
		}

	
		@Override
		public View getView(final int position, View convertView,ViewGroup parent)
		{

			if(convertView == null)
			{
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.contact_details_row, null);
			}
			
			TextView contact_name = (TextView) convertView.findViewById(R.id.contact_name);
			TextView email_id = (TextView) convertView	.findViewById(R.id.email_id);
			contact_name.setText(display_Array.get(position).getContact_name());
			email_id.setText(display_Array.get(position).getEmail_id());
			
			return convertView;

		}
	}

	public void onItemClick(AdapterView<?> adapter, View view, int position,	long arg) 
	{
		long mInsertResult = 0;
		Log.e("total count ", "no of item to insert "+total_Contacts.size());
		mContactsModel = total_Contacts.get(position);
		
		mContactsDataBase = new ContactsDataBase(getApplicationContext());
		
		if(mContactsDataBase.saveContactsInDataBase(mContactsModel.getContact_name(), 	mContactsModel.getEmail_id(), mContactsModel.getPhone_number(), mContactsModel.getContact_id()) > 0)
		{
			Toast.makeText(PhoneContactDetails.this, "Contact is Successfully added",Toast.LENGTH_LONG).show();
			finish();
		}else{ 
			Toast.makeText(PhoneContactDetails.this, "This contact is already existed",Toast.LENGTH_SHORT).show();
			
			SharedPreferences sharedPreferences;
		    sharedPreferences = this.getSharedPreferences("skeyedex", MODE_WORLD_READABLE);
			SharedPreferences.Editor preferencesEdit;
			preferencesEdit = sharedPreferences.edit();
			preferencesEdit.putBoolean("AccountExists",true);
			preferencesEdit.commit();

			
		}
	}
	
	private static int getContactIDFromNumber(String contactNumber,Context context)
	{
	    contactNumber = Uri.encode(contactNumber);
	    int phoneContactID = new Random().nextInt();
	    Cursor contactLookupCursor = context.getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,Uri.encode(contactNumber)),new String[] {PhoneLookup.DISPLAY_NAME, PhoneLookup._ID}, null, null, null);
	        while(contactLookupCursor.moveToNext()){
	            phoneContactID = contactLookupCursor.getInt(contactLookupCursor.getColumnIndexOrThrow(PhoneLookup._ID));
	            }
	        contactLookupCursor.close();

	    return phoneContactID;
	}

}
