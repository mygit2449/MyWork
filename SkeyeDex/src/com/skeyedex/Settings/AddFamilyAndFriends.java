package com.skeyedex.Settings;

import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skeyedex.R;
import com.skeyedex.EmailDataBase.ContactsDataBase;
import com.skeyedex.Menu.DeleteOption;
import com.skeyedex.Model.ContactsModel;
import com.skeyedex.dialog.AlertDialogMsg;

public class AddFamilyAndFriends extends DeleteOption implements OnClickListener, OnItemClickListener{

	Button mAddFamily_btn;
    ListView mContacts_List;
    Intent receiverIntent;
    String contact_name, email_id;
    ContactsDataBase mContactsDataBase = null;
    ArrayList<ContactsModel> total_contacts = null;
    FamilyContactsAdapter mFamilyContactsAdapter = null;
    String item_position;
    boolean onclickFlag = false;
    Boolean[] selectedItem = null;
	@Override
	public void onCreate(Bundle settingsInstance) 
	{
		super.onCreate(settingsInstance);
		setContentView(R.layout.contacts_details);
		mAddFamily_btn = (Button) findViewById(R.id.add_family_BTN_add);
		mContacts_List = (ListView)findViewById(R.id.contacts_list);
	  	mAddFamily_btn.setOnClickListener(this);
	  	mContacts_List.setOnItemClickListener((OnItemClickListener) this);
	  	mContacts_List.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	 	
	}

	public void refresh()
	{
		
		mContactsDataBase = new ContactsDataBase(getApplicationContext());
		total_contacts = new ArrayList<ContactsModel>();
		total_contacts = mContactsDataBase.getContacts_From_DataBase();
		Log.e("Family Contacts ", " Count "+total_contacts.size());
		mFamilyContactsAdapter = new FamilyContactsAdapter(AddFamilyAndFriends.this, total_contacts);
    	mContacts_List.setAdapter(mFamilyContactsAdapter);
    	mFamilyContactsAdapter.notifyDataSetChanged();
    	
	}
	
	@Override
    public void onResume()
    {
    	super.onResume();
    	 refresh();
    	
    }	
	public void onClick(View v) 
	{
		startActivity(new Intent(AddFamilyAndFriends.this,	PhoneContactDetails.class));
	}
	
	public class FamilyContactsAdapter extends BaseAdapter
	{
		
		ArrayList<ContactsModel> display_Array;
		Context context;
		public FamilyContactsAdapter(Context context, ArrayList<ContactsModel> result) {
			
			super();
			this.context = context;
			this.display_Array = result;
			
			selectedItem = new Boolean[display_Array.size()];
			for (int count = 0; count < selectedItem.length; count++) {
				selectedItem[count] = false;
			}
		}


		public int getCount() {
			// TODO Auto-generated method stub
			return display_Array.size();
		}

		
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(convertView == null)
			{
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.contact_details_row, null);
			}
			
			TextView contact_name = (TextView)convertView.findViewById(R.id.contact_name);
			TextView email_id = (TextView)convertView.findViewById(R.id.email_id);
			
			contact_name.setText(display_Array.get(position).getContact_name());
			email_id.setText(display_Array.get(position).getEmail_id());
			Log.v("contact name "+display_Array.get(position).getContact_name(), "Email Id "+display_Array.get(position).getEmail_id());
			
			if (selectedItem[position]) 
				convertView.setBackgroundColor(Color.CYAN);
			else
				convertView.setBackgroundColor(Color.BLACK);
			
			return convertView;
		}
	}

	int position_delete;
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		// TODO Auto-generated method stub

		item_position = total_contacts.get(position).getContact_name();
		for (int iCtr = 0; iCtr < selectedItem.length; iCtr++) {
			selectedItem[iCtr] = false;
		}
		selectedItem[position] = true;
		mFamilyContactsAdapter.notifyDataSetChanged();
		onclickFlag = true;
		
	}
	
	@Override
	public void deleteAccount() {
		
		if (onclickFlag) {
			alertDialogWithMsg("Family/Friends", "Are you sure you want to delete the contact from family/friends contact list?");
		    onclickFlag = false;
		}
		else
			Toast.makeText(AddFamilyAndFriends.this, "Please Select Any One Contact To Delete", Toast.LENGTH_SHORT).show();
	 
	}
	
	public void alertDialogWithMsg(String title, String msg)
	{
			
		new AlertDialogMsg(AddFamilyAndFriends.this, title, msg).setPositiveButton("Delete", new android.content.DialogInterface.OnClickListener(){
	
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				mContactsDataBase.deleteSelectedContact(item_position);
				Log.v("Item to delete"," "+item_position);
				refresh();
			}
			
			
		})
		.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener(){
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				//finish();
			}
			
			
		}).create().show();		
	
	}
}
