package com.skeyedex.GroupList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.skeyedex.R;
import com.skeyedex.EmailDataBase.EmailDataBase;
import com.skeyedex.EmailDataBase.EmailDataBaseHelper;
import com.skeyedex.EmailDownLoader.EmailDownLoadService;
import com.skeyedex.EmailsList.Emails_ListActivity;
import com.skeyedex.EmailsList.ReadEmailActivity;
import com.skeyedex.GroupMessagesAndEmails.BussinessAndDocumentsReader;
import com.skeyedex.GroupMessagesAndEmails.EventsAndAddressReader;
import com.skeyedex.GroupMessagesAndEmails.GeneralAndFamilyReader;
import com.skeyedex.GroupMessagesAndEmails.MediaAndPhotosReader;
import com.skeyedex.GroupMessagesAndEmails.ThirdDayMessagesReader;
import com.skeyedex.GroupMessagesAndEmails.ToDayMessagesReader;
import com.skeyedex.GroupMessagesAndEmails.YestarDayMessagesReader;
import com.skeyedex.Menu.MenuOptions;
import com.skeyedex.Model.EmailModel;
import com.skeyedex.Model.SMSmodel;
import com.skeyedex.TabListActivities.SkeyedexTabListActivity;
import com.skeyedex.TextMessages.SMSreader;
import com.skeyedex.dialog.AlertDialogMsg;

public class Group_ListActivity extends MenuOptions implements OnItemClickListener
{
	
	private static final String LOG_TAG = Group_ListActivity.class.getSimpleName();
	
    private static final int  GROUP_LIST_ITEMS =0, EVENTS_ADDRESSES = 1,
    						  MEDIA_PHOTOS = 2,  GENERAL_FAMILY = 3,
    						  BUSINESS_DOCUMENTS = 4, TODAY_EMAIL_SMS = 5,
    		     			  YESTERDAY_EMAIL_SMS = 6,  THREEDAYS_EMAIL_SMS = 7;
    
	SMSmodel sms = null;
	
	ArrayList deleteItemsArray = null;
	
	String[] arrAttachment;
	private HashMap<String, Object> mEmailsList, mSmsList; 
	
	String msg = "You are about to exceed the limit of emails for this category. Delete Some to make Room.";
	
	int mAlertType = 0;
	int item_position =0;
	
	ListView group_list = null;
	
	ArrayList arrAllMessages = null;
	
	GroupListItemAdapter mGroupListItemAdapter = null;
	EmailDataBaseHelper mEmailDataBaseHelper;
	SQLiteDatabase mDatabase;
	 
	EmailModel mEmailModel = null;
    EmailDataBase mEmailDatabase = null;
	
     @Override
	public void onCreate(Bundle savedInstanceState)
	{
    	 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_listview_items);

		mEmailsList = new HashMap<String, Object>();
		mSmsList = new HashMap<String, Object>();
		group_list = (ListView)findViewById(R.id.groupList_listview);
		group_list.setOnItemClickListener(this);
	
		mEmailDataBaseHelper = EmailDataBaseHelper.getDBAdapterInstance(this);
		mDatabase = mEmailDataBaseHelper.getReadableDatabase();
		mEmailDatabase = new EmailDataBase(getApplicationContext());
     
	}

    @Override
 	public void onResume(){
 		super.onResume();
 		refresh();
 		
 	}
     
     @Override
     public void  refresh()
     {
    	 
    	 try 
	        {
    		 
	        	Intent intent=getIntent();
	        	item_position =intent.getExtras().getInt("item_position");
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
			
		    switch(item_position)
		    {
		    
		    	case GROUP_LIST_ITEMS : 
		    	{
		    		group_list.setAdapter(new GroupListItemAdapter(this));
		    		break;
		    	}
		    	case TODAY_EMAIL_SMS:
		    	{
		    		
		    		EmailAsync mEmailAsync = new EmailAsync(this, 5);
		    		mEmailAsync.execute();

		    	}
		    	break;
		    	
		    	case YESTERDAY_EMAIL_SMS:
		    	{
		    		
		    		EmailAsync mEmailAsync = new EmailAsync(this, 6);
		    		mEmailAsync.execute();
		    		
		    	}
		    	break;
		    	
		    	case THREEDAYS_EMAIL_SMS:
		    	{
		    		
		    		EmailAsync mEmailAsync = new EmailAsync(this, 7);
		    		mEmailAsync.execute();
		    		
		    	}
		    	break;
		    
		    	case EVENTS_ADDRESSES:
		    	{
		    		
		    		EmailAsync mEmailAsync = new EmailAsync(this, 1);
		    		mEmailAsync.execute();
		    		
		    	}
		    	break;
		    	
		    	case GENERAL_FAMILY:
		    	{
		    		
		    		EmailAsync mEmailAsync = new EmailAsync(this, 3);
		    		mEmailAsync.execute();

		    	}
		    	break;
		    	
		    	case BUSINESS_DOCUMENTS:
		    	{
		    		
		    		EmailAsync mEmailAsync = new EmailAsync(this, 4);
		    		mEmailAsync.execute();
		    	}
		    	break;
		    	
		    	case MEDIA_PHOTOS:
		    	{
		    		
		    		EmailAsync mEmailAsync = new EmailAsync(this, 2);
		    		mEmailAsync.execute();

		    	}
		    	break;
		    }
     }
     
	
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
	{
		
		Intent intent = null;
		if(item_position == 0)
		{
			if(position == 4)
			{
				intent=new Intent(Group_ListActivity.this,SkeyedexTabListActivity.class);
				intent.putExtra("item_position", TODAY_EMAIL_SMS);
				intent.putExtra("tag","GroupTab");
				startActivity(intent);			
			}else if (position == 5) {
				intent=new Intent(Group_ListActivity.this,SkeyedexTabListActivity.class);
				intent.putExtra("item_position", YESTERDAY_EMAIL_SMS);
				intent.putExtra("tag","GroupTab");
				startActivity(intent);	
			}else if (position == 6) {
				intent=new Intent(Group_ListActivity.this,SkeyedexTabListActivity.class);
				intent.putExtra("item_position", THREEDAYS_EMAIL_SMS);
				intent.putExtra("tag","GroupTab");
				startActivity(intent);	
			}else if (position == 1) {
				intent=new Intent(Group_ListActivity.this,SkeyedexTabListActivity.class);
				intent.putExtra("item_position", EVENTS_ADDRESSES);
				intent.putExtra("tag","GroupTab");
				startActivity(intent);	
			}else if (position == 2) {
				intent=new Intent(Group_ListActivity.this,SkeyedexTabListActivity.class);
				intent.putExtra("item_position", BUSINESS_DOCUMENTS);
				intent.putExtra("tag","GroupTab");
				startActivity(intent);	
			}else if (position == 3) {
				intent=new Intent(Group_ListActivity.this,SkeyedexTabListActivity.class);
				intent.putExtra("item_position", MEDIA_PHOTOS);
				intent.putExtra("tag","GroupTab");
				startActivity(intent);	
			}else if (position == 0) {
				intent=new Intent(Group_ListActivity.this,SkeyedexTabListActivity.class);
				intent.putExtra("item_position", GENERAL_FAMILY);
				intent.putExtra("tag","GroupTab");
				startActivity(intent);	
			}
		}
	}

	public class GroupListItemAdapter extends BaseAdapter
	{
		
		String[] group_categories  = {"General Family     300- 500","Events,Addresses      1 - 50","Business, Docs      100-199","Media,Photos     200 - 299","Today","Yesterday","3 Days Ago"};
		
		int[] category_background = {R.drawable.gl_white_bar,R.drawable.gl_yellow_bar,R.drawable.gl_blue_bar,R.drawable.gl_green_bar,R.drawable.gl_red_bar,R.drawable.gl_purple_bar,R.drawable.gl_brown_bar};
		Context context;

		public GroupListItemAdapter(Context context)
		{
			
			this.context=context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return group_categories.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
				// TODO Auto-generated method stub
			if(convertView == null)
			{
				LayoutInflater inflater= LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.group_listview_row, null);
			}
			
				TextView  category= (TextView)convertView.findViewById(R.id.grouplistrow_Text_Category);
				convertView.setBackgroundResource(category_background[position]);
				category.setText(group_categories[position]);

			return convertView;
	    }


	}

	public void alertDialogWithMsg(String title, String msg)
	{
			
		new AlertDialogMsg(Group_ListActivity.this, title, msg ).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){
	
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (mAlertType == 1) {
					
					finish();
				}
			}
			
		}).create().show();		
	
	}

	public class EmailAsync extends AsyncTask<String, Void, Void>
	{
  
		
		Context mContext;
		ProgressDialog mDialog;
		String mErrorMsg = "";
		int type;
		String count_value = "", username,  messageBody, attachment_name, email_from, subject;
		long msgId;
		int count = 0,   status, read_status = 1;
		
		StringBuffer stringBuff = new StringBuffer();
		
		GeneralAndFamilyAdapter mGeneralAndFamilyAdapter = null;
		
		EventsAdapter mEventsAdapter = null;
		
		MediaAndPhotosAdapter mMediaAndPhotosAdapter = null;
		
		BusinessAndDocumentsAdapter mBusinessAndDocumentsAdapter = null;
		
		TodayMessagesAdapter mTodayMessagesAdapter = null;
		
		YestardayMessagesAdapter mYestardayMessagesAdapter = null;
		
		ThirdDayMessagesAdapter mThirdDayMessagesAdapter = null; 
		
		
		
		public EmailAsync(Context context, int type) 
		{
			
			mContext = context;
			mDialog = ProgressDialog.show(context, "", "Loading Emails....", true, false);
			mDialog.setCancelable(true);
			this.type = type;
			
		}
		
		@Override
		protected Void doInBackground(String... params) 
		{
			
			GeneralAndFamilyReader generalmessages = new GeneralAndFamilyReader(mContext);
			
			EventsAndAddressReader eventsmessages = new EventsAndAddressReader(mContext);
			
			MediaAndPhotosReader mediamessages = new MediaAndPhotosReader(mContext);
			
			BussinessAndDocumentsReader businessmessages = new BussinessAndDocumentsReader(mContext);
			
			ToDayMessagesReader toDayMessages = new ToDayMessagesReader(mContext);
			
			YestarDayMessagesReader yestarDaymessages = new YestarDayMessagesReader(mContext);
			
			ThirdDayMessagesReader thirdDaymessages = new ThirdDayMessagesReader(mContext);
			
			try
			{
				if (type == 1) {
					
					arrAllMessages = eventsmessages.get_Events_messages(stringBuff);
					
				}else if (type == 2) {
					
					arrAllMessages = mediamessages.get_Media_messages(stringBuff);
				}else if (type == 3) {
					
					arrAllMessages = generalmessages.get_General_messages(stringBuff);
					
				}else if (type == 4) {
					
					arrAllMessages = businessmessages.get_Business_messages(stringBuff);
				}else if (type == 5) {
					
					arrAllMessages = toDayMessages.getTodayMessages();
				}else if (type == 6) {
					
					arrAllMessages = yestarDaymessages.get_Yestarday_messages();
				}else if (type == 7) {
					
					arrAllMessages = thirdDaymessages.get_Third_messages();
				}
				 
				
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				mErrorMsg = "Error in processing Emails ," + e.getMessage();
			}
			return null;
		}
		
		public void onPostExecute(Void result) 
		{
			
			mEmailModel = new EmailModel();
			mEmailDatabase = new EmailDataBase(getApplicationContext());
			
			try 
			 {
				 mDialog.dismiss();
				 mDialog = null;
			 } catch (Exception e) {
			        // nothing
			    }
			
			if (mErrorMsg.length() > 0) 
			{
				mAlertType = 0;
				alertDialogWithMsg("Skeyedex", mErrorMsg);
			}else{
				
				if (type == 1) 
				{
					
					mEventsAdapter = new EventsAdapter(mContext, arrAllMessages, mEmailsList, mSmsList);
					group_list.setAdapter(mEventsAdapter);
					
					group_list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
							// TODO Auto-generated method stub
			
							Object obj = arrAllMessages.get(position);
							
							
							 
							if (obj instanceof EmailModel) 
							{
								
								ArrayList<Uri> uris = new ArrayList<Uri>();
								attachment_name = ((EmailModel) arrAllMessages.get(position)).getAttachmentName();
								arrAttachment = attachment_name.split(",");
								
								String sdcard_path = "";
								 
								for (int iCtr = 0; iCtr < arrAttachment.length; iCtr++)
								{
								
									 String file = arrAttachment[iCtr];
									 Log.v(LOG_TAG, "file name "+file);
									 sdcard_path = String.format(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Skeyedex");
									 Log.v(LOG_TAG, "file name "+sdcard_path+ "/"+file);
								     File fileIn = new File(sdcard_path+ "/"+file);
								     Uri u = Uri.fromFile(fileIn);
								     uris.add(u);
								     
								 }
							 
									 mEmailModel = (EmailModel)arrAllMessages.get(position);
									 
									 status = mEmailModel.getStatus();
									 msgId = mEmailModel.getFetched_id();
									 username = mEmailModel.getEmail_id();
									 email_from = mEmailModel.getEmail_Sender();
									 subject = mEmailModel.getSubject();
									 //messageBody = Html.fromHtml(mEmailModel.getMessageBody()).toString();
									 
									 messageBody = mEmailModel.getMessageBody();
									 EmailAsyncRead mEmailAsyncRead = new EmailAsyncRead(mContext, status, username, msgId, email_from, subject,messageBody, uris);
									 mEmailAsyncRead.execute();

								 
							}else{
								
									 sms = (SMSmodel) arrAllMessages.get(position);
									 Intent intent = new Intent(Intent.ACTION_VIEW);
									 intent.setData(Uri.parse("content://mms-sms/conversations/"+sms.getthreadID()));
									 startActivity(intent);
							}

							
						}//item click
							
						
					});
					
					count_value = stringBuff.toString();
					count = Integer.parseInt(count_value);
					
					if (count >= 30 && count <= 45 ) 
					{
						mAlertType = 0;
						alertDialogWithMsg("Skeyedex", msg);
						
					}else if(count > 100){
						mAlertType = 1;
						alertDialogWithMsg("Skeyedex", msg);
					}
					
					
				
					
				}else if (type == 2) {
					
					mMediaAndPhotosAdapter = new MediaAndPhotosAdapter(mContext, arrAllMessages, mEmailsList, mSmsList);
					group_list.setAdapter(mMediaAndPhotosAdapter);
					
					group_list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
							// TODO Auto-generated method stub
							
							Object obj = arrAllMessages.get(position);
							
							 
							if (obj instanceof EmailModel) 
							{
								
								ArrayList<Uri> uris = new ArrayList<Uri>();
								attachment_name = ((EmailModel) arrAllMessages.get(position)).getAttachmentName();
								arrAttachment = attachment_name.split(",");
								
								String sdcard_path = "";
								 
								for (int iCtr = 0; iCtr < arrAttachment.length; iCtr++)
								{
								
									 String file = arrAttachment[iCtr];
									 Log.v(LOG_TAG, "file name "+file);
									 sdcard_path = String.format(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Skeyedex");
									 Log.v(LOG_TAG, "file name "+sdcard_path+ "/"+file);
								     File fileIn = new File(sdcard_path+ "/"+file);
								     Uri u = Uri.fromFile(fileIn);
								     uris.add(u);
								     
								 }
							 
									 mEmailModel = (EmailModel)arrAllMessages.get(position);
									 
									 status = mEmailModel.getStatus();
									 msgId = mEmailModel.getFetched_id();
									 username = mEmailModel.getEmail_id();
									 email_from = mEmailModel.getEmail_Sender();
									 subject = mEmailModel.getSubject();
									 //messageBody = Html.fromHtml(mEmailModel.getMessageBody()).toString();
									 
									 messageBody = mEmailModel.getMessageBody();

									 EmailAsyncRead mEmailAsyncRead = new EmailAsyncRead(mContext, status, username, msgId, email_from, subject,messageBody, uris);
									 mEmailAsyncRead.execute();

								 
							}else{
									
									 sms = (SMSmodel) arrAllMessages.get(position);
									 Intent intent = new Intent(Intent.ACTION_VIEW);
									 intent.setData(Uri.parse("content://mms-sms/conversations/"+sms.getthreadID()));
									 startActivity(intent);
							}

					}//item click
							 
					});
					
					
					count_value = stringBuff.toString();
					count = Integer.parseInt(count_value);
					
					if (count >= 85 && count <= 90 )
					{
						mAlertType = 0;
						alertDialogWithMsg("Skeyedex", msg);
						
					}else if(count > 100)
					{
						mAlertType = 1;
						alertDialogWithMsg("Skeyedex", msg);
					}
					
				}else if (type == 3) {
					
					mGeneralAndFamilyAdapter = new GeneralAndFamilyAdapter(mContext, arrAllMessages, mEmailsList, mSmsList);
					group_list.setAdapter(mGeneralAndFamilyAdapter);
					
					
					group_list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
							// TODO Auto-generated method stub
							
							Object obj = arrAllMessages.get(position);
							
							
							 
							if (obj instanceof EmailModel) 
							{
								
								ArrayList<Uri> uris = new ArrayList<Uri>();
								attachment_name = ((EmailModel) arrAllMessages.get(position)).getAttachmentName();
								arrAttachment = attachment_name.split(",");
								
								String sdcard_path = "";
								 
								for (int iCtr = 0; iCtr < arrAttachment.length; iCtr++)
								{
								
									 String file = arrAttachment[iCtr];
									 Log.v(LOG_TAG, "file name "+file);
									 sdcard_path = String.format(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Skeyedex");
									 Log.v(LOG_TAG, "file name "+sdcard_path+ "/"+file);
								     File fileIn = new File(sdcard_path+ "/"+file);
								     Uri u = Uri.fromFile(fileIn);
								     uris.add(u);
								     
								 }
							 
									 mEmailModel = (EmailModel)arrAllMessages.get(position);
									 
									 status = mEmailModel.getStatus();
									 msgId = mEmailModel.getFetched_id();
									 username = mEmailModel.getEmail_id();
									 email_from = mEmailModel.getEmail_Sender();
									 subject = mEmailModel.getSubject();
									 //messageBody = Html.fromHtml(mEmailModel.getMessageBody()).toString();
									 
									 messageBody = mEmailModel.getMessageBody();

									 EmailAsyncRead mEmailAsyncRead = new EmailAsyncRead(mContext, status, username, msgId, email_from, subject,messageBody, uris);
									 mEmailAsyncRead.execute();
								 
							}else{
								
									 sms = (SMSmodel) arrAllMessages.get(position);
									 Intent intent = new Intent(Intent.ACTION_VIEW);
									 intent.setData(Uri.parse("content://mms-sms/conversations/"+sms.getthreadID()));
									 startActivity(intent);
							}
							 
					}// item click
					});
					count_value = stringBuff.toString();
					count = Integer.parseInt(count_value);
					
					if (count >= 185 && count <= 195 ) 
					{
						mAlertType = 0;
						alertDialogWithMsg("Skeyedex", msg);
						
					}else if(count > 200){
						mAlertType = 1;
						alertDialogWithMsg("Skeyedex", msg);
					}
				}else if (type == 4) {
					
					mBusinessAndDocumentsAdapter = new BusinessAndDocumentsAdapter(mContext, arrAllMessages, mEmailsList, mSmsList);
					
					group_list.setAdapter(mBusinessAndDocumentsAdapter);
					
					group_list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View v,	int position, long arg3) {
							// TODO Auto-generated method stub
							
							Object obj = arrAllMessages.get(position);
							
							if (obj instanceof EmailModel) 
							{
								
								ArrayList<Uri> uris = new ArrayList<Uri>();
								attachment_name = ((EmailModel) arrAllMessages.get(position)).getAttachmentName();
								arrAttachment = attachment_name.split(",");
								
								String sdcard_path = "";
								 
								for (int iCtr = 0; iCtr < arrAttachment.length; iCtr++)
								{
								
									 String file = arrAttachment[iCtr];
									 Log.v(LOG_TAG, "file name "+file);
									 sdcard_path = String.format(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Skeyedex");
									 Log.v(LOG_TAG, "file name "+sdcard_path+ "/"+file);
								     File fileIn = new File(sdcard_path+ "/"+file);
								     Uri u = Uri.fromFile(fileIn);
								     uris.add(u);
								     
								 }
							 
									 mEmailModel = (EmailModel)arrAllMessages.get(position);
									 
									 status = mEmailModel.getStatus();
									 msgId = mEmailModel.getFetched_id();
									 username = mEmailModel.getEmail_id();
									 email_from = mEmailModel.getEmail_Sender();
									 subject = mEmailModel.getSubject();
									// messageBody = Html.fromHtml(mEmailModel.getMessageBody()).toString();
									 
									 messageBody = mEmailModel.getMessageBody();

									 EmailAsyncRead mEmailAsyncRead = new EmailAsyncRead(mContext, status, username, msgId, email_from, subject,messageBody, uris);
									 mEmailAsyncRead.execute();

								 
							}else{
								
									 sms = (SMSmodel) arrAllMessages.get(position);
									 Intent intent = new Intent(Intent.ACTION_VIEW);
									 intent.setData(Uri.parse("content://mms-sms/conversations/"+sms.getthreadID()));
									 startActivity(intent);
								 
							}
							
						}// item click
						
					});
					
					count_value = stringBuff.toString();
					count = Integer.parseInt(count_value);
					
					if (count >= 85 && count <= 95 )
					{
						mAlertType = 0;
						alertDialogWithMsg("Skeyedex", msg);
						
					}else if(count > 100) {
						mAlertType = 1;
						alertDialogWithMsg("Skeyedex", msg);
					}
					
				}else if (type == 5) {
					
					mTodayMessagesAdapter = new TodayMessagesAdapter(mContext, arrAllMessages, mEmailsList, mSmsList);
					
					group_list.setAdapter(mTodayMessagesAdapter);
					
					group_list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View v,	int position, long arg3) {
							// TODO Auto-generated method stub
							
							Object obj = arrAllMessages.get(position);
							
							
							 
							if (obj instanceof EmailModel) 
							{
								
								ArrayList<Uri> uris = new ArrayList<Uri>();
								attachment_name = ((EmailModel) arrAllMessages.get(position)).getAttachmentName();
								arrAttachment = attachment_name.split(",");
								
								String sdcard_path = "";
								 
								for (int iCtr = 0; iCtr < arrAttachment.length; iCtr++)
								{
								
									 String file = arrAttachment[iCtr];
									 Log.v(LOG_TAG, "file name "+file);
									 sdcard_path = String.format(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Skeyedex");
									 Log.v(LOG_TAG, "file name "+sdcard_path+ "/"+file);
								     File fileIn = new File(sdcard_path+ "/"+file);
								     Uri u = Uri.fromFile(fileIn);
								     uris.add(u);
								     
								 }
							 
									 mEmailModel = (EmailModel)arrAllMessages.get(position);
									 
									 status = mEmailModel.getStatus();
									 msgId = mEmailModel.getFetched_id();
									 username = mEmailModel.getEmail_id();
									 email_from = mEmailModel.getEmail_Sender();
									 subject = mEmailModel.getSubject();
									 //messageBody = Html.fromHtml(mEmailModel.getMessageBody()).toString();
									 
									 messageBody = mEmailModel.getMessageBody();

									 EmailAsyncRead mEmailAsyncRead = new EmailAsyncRead(mContext, status, username, msgId, email_from, subject,messageBody, uris);
									 mEmailAsyncRead.execute();

								 
							}else{
								
									 sms = (SMSmodel) arrAllMessages.get(position);
									 Intent intent = new Intent(Intent.ACTION_VIEW);
									 intent.setData(Uri.parse("content://mms-sms/conversations/"+sms.getthreadID()));
									 startActivity(intent);
								 
							}
							
						}// item click
					});
				}else if (type == 6) {
					
					mYestardayMessagesAdapter = new YestardayMessagesAdapter(mContext, arrAllMessages, mEmailsList, mSmsList);
					group_list.setAdapter(mYestardayMessagesAdapter);
					
					group_list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View v,	int position, long arg3) {
							// TODO Auto-generated method stub
							
							Object obj = arrAllMessages.get(position);
							
						
							 
							if (obj instanceof EmailModel) 
							{
								
								ArrayList<Uri> uris = new ArrayList<Uri>();
								attachment_name = ((EmailModel) arrAllMessages.get(position)).getAttachmentName();
								arrAttachment = attachment_name.split(",");
								
								String sdcard_path = "";
								 
								for (int iCtr = 0; iCtr < arrAttachment.length; iCtr++)
								{
								
									 String file = arrAttachment[iCtr];
									 Log.v(LOG_TAG, "file name "+file);
									 sdcard_path = String.format(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Skeyedex");
									 Log.v(LOG_TAG, "file name "+sdcard_path+ "/"+file);
								     File fileIn = new File(sdcard_path+ "/"+file);
								     Uri u = Uri.fromFile(fileIn);
								     uris.add(u);
								     
								 }
							 
									 mEmailModel = (EmailModel)arrAllMessages.get(position);
									 
									 status = mEmailModel.getStatus();
									 msgId = mEmailModel.getFetched_id();
									 username = mEmailModel.getEmail_id();
									 email_from = mEmailModel.getEmail_Sender();
									 subject = mEmailModel.getSubject();
									 //messageBody = Html.fromHtml(mEmailModel.getMessageBody()).toString();
									 
									 messageBody = mEmailModel.getMessageBody();

									 EmailAsyncRead mEmailAsyncRead = new EmailAsyncRead(mContext, status, username, msgId, email_from, subject,messageBody, uris);
									 mEmailAsyncRead.execute();

								 
							}else{
								
									 sms = (SMSmodel) arrAllMessages.get(position);
									 Intent intent = new Intent(Intent.ACTION_VIEW);
									 intent.setData(Uri.parse("content://mms-sms/conversations/"+sms.getthreadID()));
									 startActivity(intent);
								 
							}
							 
						}// item click
					});
				}else if (type == 7) {
					
					mThirdDayMessagesAdapter = new ThirdDayMessagesAdapter(mContext, arrAllMessages, mEmailsList, mSmsList);
					group_list.setAdapter(mThirdDayMessagesAdapter);
					
					group_list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View v,	int position, long arg3) {
							// TODO Auto-generated method stub
							
							Object obj = arrAllMessages.get(position);
							
							
							 
							if (obj instanceof EmailModel) 
							{
								
								ArrayList<Uri> uris = new ArrayList<Uri>();
								attachment_name = ((EmailModel) arrAllMessages.get(position)).getAttachmentName();
								arrAttachment = attachment_name.split(",");
								
								String sdcard_path = "";
								 
								for (int iCtr = 0; iCtr < arrAttachment.length; iCtr++)
								{
								
									 String file = arrAttachment[iCtr];
									 Log.v(LOG_TAG, "file name "+file);
									 sdcard_path = String.format(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Skeyedex");
									 Log.v(LOG_TAG, "file name "+sdcard_path+ "/"+file);
								     File fileIn = new File(sdcard_path+ "/"+file);
								     Uri u = Uri.fromFile(fileIn);
								     uris.add(u);
								     
								 }
							 
									 mEmailModel = (EmailModel)arrAllMessages.get(position);
									 
									 status = mEmailModel.getStatus();
									 msgId = mEmailModel.getFetched_id();
									 username = mEmailModel.getEmail_id();
									 email_from = mEmailModel.getEmail_Sender();
									 subject = mEmailModel.getSubject();
									 //messageBody = Html.fromHtml(mEmailModel.getMessageBody()).toString();
									 
									 messageBody = mEmailModel.getMessageBody();

									 EmailAsyncRead mEmailAsyncRead = new EmailAsyncRead(mContext, status, username, msgId, email_from, subject,messageBody, uris);
									 mEmailAsyncRead.execute();

								 
							}else{
								
									 sms = (SMSmodel) arrAllMessages.get(position);
									 Intent intent = new Intent(Intent.ACTION_VIEW);
									 intent.setData(Uri.parse("content://mms-sms/conversations/"+sms.getthreadID()));
									 startActivity(intent);
								 
							}

							 
						}//for on item click
					});
					
				}// for elseif type 7
    			
			}// for else block
		}// for on post execute
	}
	
	@Override
	public void deleteEmails()
	{
	
		
//		stopService(new Intent(Group_ListActivity.this, EmailDownLoadService.class)); 

		SMSreader smsreader = null;
		
		ArrayList mail_deletion_temp = null, array_sms_deletion = null;
		
		Iterator email_iterator =mEmailsList.keySet().iterator();
		Iterator sms_iterator = mSmsList.keySet().iterator();
	
		String sms_key = "", email_key = "";
	    String email_Id = "";
		long msgUid = 0;
		long fetchId = 0;
		
		while(email_iterator. hasNext())
		{ 
			
			email_key = (String) email_iterator.next();

			deleteItemsArray = new ArrayList();
			
			deleteItemsArray = (ArrayList) mEmailsList.get(email_key);
			
			Log.v(LOG_TAG, "email type");
            Log.v(LOG_TAG, "Hash Map Value "+email_key+" selected Items Size "+deleteItemsArray.size());
            
         }
	
		while(sms_iterator. hasNext())
		{ 
			
			sms_key = (String) sms_iterator.next();

			array_sms_deletion = new ArrayList();
			
			array_sms_deletion = (ArrayList) mSmsList.get(sms_key);
			
            Log.v(LOG_TAG, "Hash Map Value "+sms_key+" selected Items Size "+array_sms_deletion.size());
            
            Log.v(LOG_TAG, "sms type");
            
         }
		
	
		if (array_sms_deletion  != null) 
		{
			for (int iCtr = 0; iCtr < array_sms_deletion.size(); iCtr++) 
			{
					
				smsreader = new SMSreader(this); 
				Log.v(LOG_TAG, "Sms Deletion Checking");
				smsreader.deleteMessage(Group_ListActivity.this,((SMSmodel) array_sms_deletion.get(iCtr)).getmessageID());
					
			}
		}
		
	
		if (deleteItemsArray != null) 
		{
	
			mail_deletion_temp = new ArrayList();
		    
			for (int iCtr = 0; iCtr < deleteItemsArray.size(); iCtr++)
			{
		
				mEmailModel = (EmailModel) deleteItemsArray.get(iCtr);
				msgUid = mEmailModel.getFetched_id();
				mail_deletion_temp.add(msgUid);
				Log.v(LOG_TAG, "message Id "+msgUid);
	
			}
			
		}
		
		
	    if (mail_deletion_temp != null) 
	    {
			     
            for (int iCrt = 0; iCrt < mail_deletion_temp.size(); iCrt++)
            {
				
            	
            	fetchId = (Long) mail_deletion_temp.get(iCrt);
            	
            	Log.v(LOG_TAG, "messages  "+fetchId);
            	
			    email_Id = mEmailDatabase.getEmailIds_From_DataBase(fetchId);
					    
			    if (email_Id != null) 
			    {
					
			 		 Cursor cursor  = mDatabase.rawQuery("SELECT  Username, password ,IMAP_Server , Port  from ServerSettings  where Email_Id ='" +email_Id+"'", null);
						
					 Log.i("Query  ","SELECT  Username, password ,IMAP_Server , Port  from ServerSettings  where Email_Id ='" +sms_key+"'");
				
					 try
			         {
						 
						    if (cursor != null && cursor.moveToFirst()) 
						    {
								
						    	String user = (String)cursor.getString(0);
								String password = (String) cursor.getString(1); 
								String imap_server =(String) cursor.getString(2);
								int port = Integer.parseInt((String ) cursor.getString(3));
							
								Folder folder = null, inboxFolder = null;
								Store mStore = null;
								Message[] messages = null;
					
								
								Properties properties = System.getProperties();
								properties.put("mail.imap.host", "imaps");
								
								Session session = Session.getInstance(properties, null);
								mStore = session.getStore("imaps");
								mStore.connect(imap_server, port,  user, password);
								
								Log.i(LOG_TAG, " starting to retrieve the mail for the provider " + imap_server + " User Name "+ sms_key);
							
								inboxFolder = mStore.getDefaultFolder();
								folder = inboxFolder.getFolder("INBOX");
					            UIDFolder ufolder = (UIDFolder) folder;
					            
					            folder.open(Folder.READ_WRITE);
					            
					            messages = ufolder.getMessagesByUID(fetchId, fetchId);
				            	
					            FetchProfile fp = new FetchProfile();
				                fp.add(FetchProfile.Item.ENVELOPE);
					            fp.add(FetchProfile.Item.FLAGS);
					            fp.add("X-Mailer");
					            folder.fetch(messages, fp);
				            
						       for (int iMsg = 0; iMsg < messages.length; iMsg++) 
						       {
			
					    	     String subject = messages[iMsg].getSubject().toString();
					             Log.v(LOG_TAG, "subject "+subject);
					             messages[iMsg].setFlag(Flags.Flag.DELETED, true);
						            
						       }    
					            	
				            mEmailDatabase.deleteEmails(fetchId);
						}//cursor if loop
				    }catch (Exception e) {
					// TODO: handle exception
		        	 e.printStackTrace();
				}finally
				{
					try
					{
						cursor.close();
					}catch(Exception ex) {
						Log.e("EmailService", "" + ex.toString());
					}
				}
			}//emailid if loop
		           

	     }//for loop 
			
	   }//if loop for mail_deletion_temp
	    
	refresh();
	
    if (mail_deletion_temp != null) 
    {
	 mail_deletion_temp.clear();
    }
 
    if (deleteItemsArray != null) 
    {
	 deleteItemsArray.clear();
    }

 }// for delete emails
	
	
	
	// this is for opening a particular mail from list
	public class EmailAsyncRead extends AsyncTask<String, Void, Void>
	{
  
		
		Context mContext;
		ProgressDialog mDialog;
		String mErrorMsg = "";
		int mStatus;
		long mMsgUid;
		String mEmail_id;
		String mEmail_from, mSubject, mMessageBody;
		ArrayList<Uri> mUri;
		
		public EmailAsyncRead(Context context, int status, String emailId, long msgUid, String email_from, String subject, String msgBody, ArrayList<Uri> uris) 
		{
			
			mContext = context;
			mStatus = status;
			mMsgUid = msgUid;
			mEmail_id = emailId;
			mEmail_from = email_from;
			mSubject = subject;
			mMessageBody = msgBody;
			mUri  =uris;
			mDialog = ProgressDialog.show(context, "", "Opening Email....", true, false);
			mDialog.setCancelable(true);
		
		}
		
		@Override
		protected Void doInBackground(String... params) 
		{
			try
			{
				connectToServer(mStatus, mEmail_id, mMsgUid);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				mErrorMsg = "Error in processing Emails ," + e.getMessage();
			}
			return null;
		}
		
		public void onPostExecute(Void result) 
		{
			
			 try 
			 {
				 
				 mDialog.dismiss();
				 mDialog = null;
				 
			 } catch (Exception e) {
			        // nothing
			    }

				if (mErrorMsg.length() > 0) 
				{
					
					mAlertType = 0;
					alertDialogWithMsg("Skeyedex", mErrorMsg);
					
				}else{

//					 Intent intent=new Intent(Intent.ACTION_SEND_MULTIPLE);
//					 intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
//					 intent.putExtra(Intent.EXTRA_EMAIL, mEmail_from);
//					 intent.putExtra(Intent.EXTRA_TEXT, mMessageBody);
//					 intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,mUri);
//					 intent.setType("*/*");
//
//					 startActivity(intent);
					 
					
					 Intent intent=new Intent(Group_ListActivity.this, ReadEmailActivity.class);
					 intent.putExtra("subject", mSubject);
					 intent.putExtra("email_from", mEmail_from);
					 intent.putExtra("message_body", mMessageBody);
					 intent.putExtra("attachments", arrAttachment);
					 

					 startActivity(intent);
				}
				
		}
	
	}// for EmailAsyncRead 		
	
			
	// method for read and unread messages
	public void connectToServer(int status, String emailId, long msgUid)
	{
		
		int read_status = 1;
		
		Log.v(LOG_TAG, " msgStatus "+status+ " emailId "+emailId+ " MsgUid "+msgUid);
		if (status == 0)
		  {
		
	    	 mEmailDatabase.updateEmailStatus(read_status, status, msgUid);
	
	    	 Cursor cursor  = mDatabase.rawQuery("SELECT  Username, password ,IMAP_Server , Port  from ServerSettings  where Email_Id ='" +emailId+"'", null);
			
			 Log.i("Query  ","SELECT  Username, password ,IMAP_Server , Port  from ServerSettings  where Email_Id ='" +emailId+"'");
			 
				 try
					{   
						if(cursor !=null  )
						{
							
							while(cursor.moveToNext())
							{
	
								String user = (String)cursor.getString(0);
								String password = (String) cursor.getString(1); 
								String imap_server =(String) cursor.getString(2);
								int port = Integer.parseInt((String ) cursor.getString(3));
							
								Folder folder = null, inboxFolder = null;
								Store mStore = null;
								Message[] messages = null;
					
								
								Properties properties = System.getProperties();
								properties.put("mail.imap.host", "imaps");
								
								Session session = Session.getInstance(properties, null);
								mStore = session.getStore("imaps");
								mStore.connect(imap_server, port,  user, password);
								//Log.i(LOG_TAG, " starting to retrieve the mail for the provider " + imap_server + " User Name "+ emailId);
							
								inboxFolder = mStore.getDefaultFolder();
								folder = inboxFolder.getFolder("INBOX");
					            UIDFolder ufolder = (UIDFolder) folder;
					            
					            folder.open(Folder.READ_WRITE);
					            
					            messages = ufolder.getMessagesByUID(msgUid, msgUid);
					            
					            FetchProfile fp = new FetchProfile();
					            fp.add(FetchProfile.Item.ENVELOPE);
					            fp.add(FetchProfile.Item.FLAGS);
					            fp.add("X-Mailer");
					            folder.fetch(messages, fp);
					            
					           
					            
					            for (int iCtr = 0; iCtr < messages.length; iCtr++) 
					            {
									 String subject = messages[iCtr].getSubject().toString();
									 Log.v(LOG_TAG, "subject "+subject);
									 messages[iCtr].setFlag(Flags.Flag.SEEN, true);// set email flag as seen in email server.
								}
					           
							}
						}
					}catch (Exception e) {
						// TODO: handle exception
					}finally
					{
						try
						{
					
							cursor.close();
						   
						   
						}catch(Exception ex) {
							Log.e("EmailService", "" + ex.toString());
						}
					}
				 
		  	}
	}// for connect to server..


}
	

