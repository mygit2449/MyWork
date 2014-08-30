package com.skeyedex.TextMessages;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skeyedex.R;
import com.skeyedex.Menu.MenuOptions;
import com.skeyedex.Model.SMSmodel;

public class Text_listActivity extends MenuOptions implements OnItemLongClickListener, OnItemClickListener,android.view.View.OnClickListener{

	private static String MONTH[] = { "Jan", "Feb", "Mar", "April", "May","June", "July", "Aug", "Sept", "Oct", "Nov", "Dec" };
	private ArrayList<SMSmodel> txtMessages;
	private SMSreader smsreader;
	SmsAdapter mSmsAdapter;
	ListView textlist;
	String[] items = { "Copy text message", "Delete message" };
	int index;
	ListView mSMS_ListView;
	RelativeLayout mMessage_Compose;

	@Override 
	protected void onCreate(Bundle savedInstanceState)
	{
		
		String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textmessage_mainview);
		mMessage_Compose = (RelativeLayout) findViewById(R.id.textlistmainview_composenewmessage);
		mSMS_ListView = (ListView) findViewById(R.id.SMSlistView);
		TextView time_text = (TextView)findViewById(R.id.textlistmainview_composenewmessage_time);
		time_text.setText(currentDateTimeString);
		
		
	}

	public void refresh()
    {
		
		smsreader = new SMSreader(Text_listActivity.this);
		txtMessages = smsreader.getTxtMessages(7, 1, -1); // -1 is for all sms's filters should't apply for categories
		mSmsAdapter = new SmsAdapter(this, txtMessages);
	    mSMS_ListView.setAdapter(mSmsAdapter);
		mSMS_ListView.setOnItemLongClickListener(this);
		mSMS_ListView.setOnItemClickListener(this);
		mMessage_Compose.setOnClickListener(this);
		mSmsAdapter.notifyDataSetChanged();
		
    }

	@Override
	public void onResume()
	{
	    super.onResume();
	    refresh();
	}

	protected AlertDialog createDialog(int position) {
		// TODO Auto-generated method stub
		index = position;
		AlertDialog temp = new AlertDialog.Builder(Text_listActivity.this)
				.setTitle("Message Options")
				.setItems(items, new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
							clipboard.setText(txtMessages.get(index).getBody());
							Log.e("$$$$", "message copied to clipboard  "	+ clipboard.getText());
							break;
						case 1:
							smsreader.deleteMessage(Text_listActivity.this,txtMessages.get(index).getmessageID());
							refresh();
							Toast.makeText(getApplicationContext(),"YOUR Message Deleted", Toast.LENGTH_SHORT).show();
							break;
						}
					}
				}).create();
 
		return temp;
	}

		public class SmsAdapter extends ArrayAdapter<SMSmodel>
		{

			
				private Context context;
				private ArrayList<SMSmodel> message;
				private static final int TYPE_ITEM = 0;
				private static final int TYPE_SEPARATOR = 1;
		
				private static final int TYPE_MAX_COUNT = 2;
		
				public SmsAdapter(Context context, ArrayList<SMSmodel> message) 
				{
					
					super(context, R.layout.textmessage_listview_row, message);
					this.context = context;
					this.message = message;
		
		        }

				@Override
				public boolean areAllItemsEnabled() {
					// TODO Auto-generated method stub
					return false;
				}

		
			 @Override
			 public boolean isEnabled(int position)
			 { 
				
				 if(message.get(position).getSender().startsWith("--")) 
				 {
					 	return	 false; 
				 } 
				 return true;
			 
			 }
		 

		 @Override 
		 public int getItemViewType(int position) 
		 {
			 if(message.get(position).getSender().startsWith("--"))
			 {
				 return  TYPE_SEPARATOR; 
			 }
		  return TYPE_ITEM;
    	 }
		  
		  @Override 
		  public int getViewTypeCount() 
		  { 
			  return TYPE_MAX_COUNT;
		  }
		 
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return message.size();

		}

		@Override
        public SMSmodel getItem(int position) {
            return message.get(position);
        }
 
        @Override
        public long getItemId(int position) {
            return position;
        }
		@Override
		public View getView(int position, View row, ViewGroup parent) 
		{
		
			int type = getItemViewType(position);
			System.out.println("getView " + position + " " + row + " type = " + type);
			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				
				if (type == TYPE_ITEM) 
				{
					row = inflater.inflate(R.layout.textmessage_listview_row, null);
				}else {
					row = inflater.inflate(R.layout.sms_seperator, null);
				}
			}

	
			if (type == TYPE_ITEM) 
			{
				
				TextView serialnumber = (TextView) row.findViewById(R.id.textlistrow_txt_serialnumber);
				TextView body = (TextView) row.findViewById(R.id.textlistrow_txt_partofmessage);
				TextView date = (TextView) row.findViewById(R.id.textlistrow_txt_date);
				TextView sender = (TextView) row.findViewById(R.id.textlistrow_txt_sender);
				TextView time = (TextView) row.findViewById(R.id.textlistrow_txt_time);
				ImageView icon_image = (ImageView) row.findViewById(R.id.textlistrow_icon_indicator);
				
			    serialnumber.setText(""+message.get(position).getSerial_No());
				icon_image.setImageResource(message.get(position).getImage_resourceId());
				body.setText(message.get(position).getBody()); 
				date.setText(getMessageDate(message.get(position).getDate()));
				sender.setText(message.get(position).getSender());
				time.setText(getMessageTime(message.get(position).getDate()));
				
				if (message.get(position).getStatus()==0) {
					 serialnumber.setTypeface(Typeface.DEFAULT_BOLD);
					 body.setTypeface(Typeface.DEFAULT_BOLD);
					 sender.setTypeface(Typeface.DEFAULT_BOLD);
				}else {
					 serialnumber.setTypeface(Typeface.DEFAULT);
					 body.setTypeface(Typeface.DEFAULT);
					 sender.setTypeface(Typeface.DEFAULT);
				}
				
			} else if (type == TYPE_SEPARATOR) {
				ImageView icon = (ImageView) row.findViewById(R.id.seperator_indicator_img);
				
				
				
				if (message.get(position).getSender().equalsIgnoreCase("--t"))
				{
					icon.setImageResource(R.drawable.blk_t_icon);
            		row.setBackgroundResource(R.drawable.red_bar);
				} else if (message.get(position).getSender().equalsIgnoreCase("--y"))
				{
		
					icon.setImageResource(R.drawable.blk_y_icon);
					row.setBackgroundResource(R.drawable.purple_bar);
				} else {
					
					icon.setImageResource(R.drawable.blk_3d_icon);
					row.setBackgroundResource(R.drawable.brown_bar);
				}

			}

			return row;
		}

		private String getMessageDate(long timeinmills) 
		{
			Calendar calander = Calendar.getInstance();
			int messageDate, messageMonth;

			calander.setTimeInMillis(timeinmills);
			messageDate = calander.get(Calendar.DATE);
			messageMonth = calander.get(Calendar.MONTH);

			Date currentDate = new Date(System.currentTimeMillis());

			if (calander.get(Calendar.DATE) == currentDate.getDate())
				return "TODAY";
			else
				return MONTH[messageMonth] + " " + messageDate;
		}

		private String getMessageTime(long timeinmills) 
		{
			Calendar calander = Calendar.getInstance();
			String amORpm;
			String messageHour, messageMinute;

			calander.setTimeInMillis(timeinmills);

			if (calander.get(Calendar.HOUR) < 10)
				messageHour = "0" + calander.get(Calendar.HOUR);
			else
				messageHour = "" + calander.get(Calendar.HOUR);

			if (calander.get(calander.MINUTE) < 10)
				messageMinute = "0" + calander.get(calander.MINUTE);
			else
				messageMinute = "" + calander.get(calander.MINUTE);

			if (calander.get(calander.AM_PM) == 0)
				amORpm = "AM";
			else
				amORpm = "PM";
			return messageHour + ":" + messageMinute + " " + amORpm;
		}

		private boolean isSeprator(String sender) 
		{
			if (sender.startsWith("--"))
				return true;
			else
				return false;
		}

	}

	public class ImageStatus {
		boolean today, yesterday, threedaybefore;
	}

	

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,long arg3)
	{
		AlertDialog dialog= createDialog(position);
		dialog.show();			
		return true;
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
	{
		
			String messageSize = txtMessages.get(position).getBody();
			int size = messageSize.length();
			Log.v("Message Length"," "+size);
			
			
			if (size < 20)
			{
				showAlert(position);
			}else{
				
				try 
				{
					SMSreader.setMessageRead(Text_listActivity.this, txtMessages.get(position).getmessageID(), 1);
					Intent intent=new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("content://mms-sms/conversations/"+txtMessages.get(position).getthreadID()));
					startActivity(intent);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		
	}

	
	
	public void showAlert(int position)
	{
		
		 index = position;
		 Log.v("position",""+index);
		 AlertDialog.Builder dialog=new AlertDialog.Builder(Text_listActivity.this);
		 dialog.setTitle("Text Message");
		 dialog.setMessage("This Message Contains less than 20 characters would you like to save or delete It.");
		 
		 dialog.setPositiveButton("Save", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int which) {
				
				Toast.makeText(Text_listActivity.this, "Message Is Saved", Toast.LENGTH_SHORT).show();
				
				SMSreader.setMessageRead(Text_listActivity.this, txtMessages.get(index).getmessageID(), 1);
				Intent intent=new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("content://mms-sms/conversations/"+txtMessages.get(index).getthreadID()));
				startActivity(intent);
			}
		})
		.setNegativeButton("Delete", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				smsreader.deleteMessage(Text_listActivity.this,txtMessages.get(index).getmessageID());
				refresh();
			}
		}).create().show();
		 
	}
	
	
	@Override
	public void onClick(View v) {

		if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)){
			  // THIS PHONE HAS SMS FUNCTIONALITY
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:"));
			startActivity(intent);
			}else{
			  // NO SMS HERE :(
			}

		
		//return true;
	}

}
