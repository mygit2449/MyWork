package com.skeyedex.EmailsList;

import java.io.File;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skeyedex.R;
import com.skeyedex.EmailDataBase.EmailDataBase;
import com.skeyedex.EmailDataBase.EmailDataBaseHelper;
import com.skeyedex.Menu.MenuOptions;
import com.skeyedex.Model.EmailModel;
import com.skeyedex.dialog.AlertDialogMsg;

public class Emails_ListActivity extends MenuOptions implements OnItemClickListener{
	
		EmailReader mEmailReader = null;
		ArrayList<EmailModel> array_emails_list;
		EmailModel mEmailModel =null;
		
		ListView mEmail_listview;
		
		TextView loading_text;
		 
		String subject, messageBody, attachment_name;
		
		EmailAdapter mEmailAdapter = null;
	    
		int mAlertType = 0;
		
	    EmailDataBase mEmailDatabase = null;
	    
	    SQLiteDatabase mDatabase;
	    
	    private HashMap<String, Object> mSelectedEmail; 
	    
	    ArrayList deleteItemsArray = null;
	    
	    ArrayList<Object> newEmail ;
	    ArrayList<Uri> uris = null;
	    private ArrayList<EmailModel> mAttachmentsList = null;
	    Thread mThread;
	    Context context;
	    
	    String[] arrAttachment;
	    private long selectedUid = 0;
	    private String  emailId = "";
	    
	    private static final String LOG_TAG = Emails_ListActivity.class.getSimpleName();
	    
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			
				super.onCreate(savedInstanceState);
				setContentView(R.layout.emails_listview);
				
				array_emails_list = new ArrayList<EmailModel>();
                mEmail_listview = (ListView)findViewById(R.id.email_List);
                loading_text = (TextView)findViewById(R.id.email_List_Txt);
               
   			 	EmailDataBaseHelper mEmailDataBaseHelper;
   			 	
   			 	mEmailDataBaseHelper = EmailDataBaseHelper.getDBAdapterInstance(this);
   			 	mDatabase = mEmailDataBaseHelper.getReadableDatabase();
   			
   			 	mEmailDatabase = new EmailDataBase(getApplicationContext());
				mEmail_listview.setOnItemClickListener((OnItemClickListener) this);
				
				newEmail = new ArrayList<Object>();
				mAttachmentsList = new ArrayList<EmailModel>();
				

		}
		
		@Override
		public void refresh()
		{
			
			EmailAsync mEmailTask = new EmailAsync(this);
			mEmailTask.execute();
			
//			if (array_emails_list.size() > 0) 
//            	loading_text.setVisibility(View.INVISIBLE);
//            else
//            	loading_text.setVisibility(View.VISIBLE);
			
		}
		
		@Override
		public void onResume()
		{
			super.onResume();
			refresh();
		}

		
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
		{
			
			
		     int status = array_emails_list.get(position).getStatus();
		     int read_status = 1;
	 
		     
		     long msgUid = array_emails_list.get(position).getFetched_id();

			 String from = array_emails_list.get(position).getEmail_Sender();
			 
			 emailId = array_emails_list.get(position).getEmail_id();
		     
			 EmailAsyncRead mEmailAsync = new EmailAsyncRead(this, status, emailId, msgUid, from);
			 mEmailAsync.execute();

			  uris = new ArrayList<Uri>();
			  
			  subject = array_emails_list.get(position).getSubject();
			  
			  //messageBody = Html.fromHtml(array_emails_list.get(position).getMessageBody().toString()).toString();
				
			  messageBody = array_emails_list.get(position).getMessageBody();
			  attachment_name = array_emails_list.get(position).getAttachmentName();
			  selectedUid = array_emails_list.get(position).getFetched_id();
			  //String[] arrAttachment = attachment_name.split(",");
			 
			  arrAttachment = attachment_name.split(",");
			  String sdcard_path = "";
			 
			  for (int iCtr = 0; iCtr < arrAttachment.length; iCtr++)
			  {
			
				 String file = arrAttachment[iCtr];
				 sdcard_path = String.format(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Skeyedex");
			     File fileIn = new File(sdcard_path+ "/"+file);
			     Uri u = Uri.fromFile(fileIn);
			     uris.add(u);
			     
			  }

		}

		public void alertDialogWithMsg(String title, String msg)
		{
				
			new AlertDialogMsg(Emails_ListActivity.this, title, msg ).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){
		
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					
				}
				
			}).create().show();		
		
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
				mDialog = ProgressDialog.show(context, "", "Loading Emails....", true, false);
				mDialog.setCancelable(true);
			
			}
			
			@Override
			protected Void doInBackground(String... params) 
			{
				try
				{
					 mEmailReader = new EmailReader(mContext);
					 array_emails_list = mEmailReader.getMailsFromWeb(1);
					 Log.i("mails size",""+array_emails_list.size());
					
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					mErrorMsg = "Error in processing Emails ," + e.getMessage();
				}
				return null;
			}
			
			public void onPostExecute(Void result) 
			{
				
					//mDialog.dismiss();
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
						mEmailAdapter = new EmailAdapter(mContext, array_emails_list);
						mEmail_listview.setAdapter(mEmailAdapter);
					}
					
			}
		}
		
		public class EmailAdapter extends ArrayAdapter<EmailModel>
		{
				private boolean mClickdiable = true;
				
				private String selectType = "manual";
				private Context context;
				private ArrayList<EmailModel> mEmails;
		
				String emailDate, emailTime;
				Format timeformatter ;
				SimpleDateFormat datetformatter;
				
				private static final int TYPE_ITEM = 0;
				private static final int TYPE_SEPARATOR = 1;
	     		private static final int TYPE_MAX_COUNT = 2;
	     		
	     		
					public EmailAdapter(Context context, 	ArrayList<EmailModel> emailMessage)
					{
						
						super(context,  R.layout.email_listview_row, emailMessage);
						this.context = context;
					    this.mEmails =emailMessage;
					    mSelectedEmail = new HashMap<String, Object>();
					    
					}
			
	
					@Override
					public boolean areAllItemsEnabled() {
						// TODO Auto-generated method stub
						return false;
					}
	
	
					 @Override
					 public boolean isEnabled(int position)
					 { 
						
						 if(mEmails.get(position).seperator.startsWith("--")) 
						 {
							 	return	 false; 
						 } 
						 return true;
					 
					 }
	 
					 @Override 
					 public int getItemViewType(int position) 
					 {
						 if(mEmails.get(position).seperator.startsWith("--"))
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
						return mEmails.size();
		
					}
	
					@Override
			        public EmailModel getItem(int position) {
			            return mEmails.get(position);
			        }
		 
			        @Override
			        public long getItemId(int position) {
			            return position;
			        }
			        
				        
			        @Override
					public View getView(final int position, View convertView, ViewGroup parent) 
					{
					
						int type = getItemViewType(position);
						RelativeLayout email_relative;
						Date emDate = null;
						
						String email_date = "";
						String email_time = "";
					    String current_date="";
						
					    timeformatter = new SimpleDateFormat("hh:mm a");
					   	datetformatter = new SimpleDateFormat("MMM dd");
					   	
						if (convertView == null)
						{
							
							LayoutInflater inflater = LayoutInflater.from(context);
							
							if (type == TYPE_ITEM) 
							{
								convertView = inflater.inflate(R.layout.email_listview_row, null);
							}else {
								convertView = inflater.inflate(R.layout.email_seperator, null);
							}
							
						}
						
						if (type == TYPE_ITEM)
						{
						
							    
							    Date currentDate = new Date(System.currentTimeMillis());
							   
							    email_relative = (RelativeLayout)convertView.findViewById(R.id.email_listview_Rel);
								TextView subject = (TextView)convertView.findViewById(R.id.emaillistrow_txt_partofemail);
								TextView sender = (TextView)convertView.findViewById(R.id.emaillistrow_txt_sender);
								TextView date = (TextView)convertView.findViewById(R.id.emaillistrow_txt_datetime);
								
								CheckBox email_check = (CheckBox)convertView.findViewById(R.id.emaillistrow_chk_check);
								email_check.setTag(position);
								
								if (array_emails_list.get(position).isSelected()) {
									
									selectType =  "auto";
									email_check.setChecked(true);
								}else{
	
        							selectType =  "auto";
									email_check.setChecked(false);
								}
								
								
								selectType = "manual";
							
								 
								email_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
									@SuppressWarnings("unchecked")
									@Override
									public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
										// TODO Auto-generated method stub
										Log.v("cheked item",""+mClickdiable);
										
										if(selectType == "manual")
										{
											array_emails_list.get( (Integer) buttonView.getTag()).setSelected(isChecked);
											
											Log.e("", "Email "+array_emails_list.get((Integer) buttonView.getTag()).getEmail_id());
											
											ArrayList<Object> email = (ArrayList<Object>) mSelectedEmail.get(array_emails_list.get((Integer) buttonView.getTag()).getEmail_id());
											
											if(isChecked){										
												
												if(email == null ){
													
												
													
														Log.e("LogCat", "email  null ");
												
													
														newEmail.add(array_emails_list.get((Integer) buttonView.getTag()));
													
													    mSelectedEmail.put(array_emails_list.get((Integer) buttonView.getTag()).getEmail_id() , newEmail);
													
												}else{
															
													Log.e("LogCat", "email  not null"+array_emails_list.get((Integer) buttonView.getTag()).getEmail_id());
													
													email.add(array_emails_list.get((Integer) buttonView.getTag()));
													
													mSelectedEmail.put(array_emails_list.get((Integer) buttonView.getTag()).getEmail_id() , email);								
												
												}
												
											}else {
											
												ArrayList<EmailModel> emails ;
												emails =(ArrayList<EmailModel>) mSelectedEmail.put(array_emails_list.get((Integer) buttonView.getTag()).getEmail_id() , email);
												
												for(int i = 0 ; i< emails.size(); i++){
													
													Log.e("LogCat", " email some"+emails.get(i).getDate_time());
													
												}
												
												email.remove(array_emails_list.get((Integer) buttonView.getTag()));
												
												mSelectedEmail.put(array_emails_list.get((Integer) buttonView.getTag()).getEmail_id() , email);		
												
												
												emails =(ArrayList<EmailModel>) mSelectedEmail.put(array_emails_list.get((Integer) buttonView.getTag()).getEmail_id() , email);
												
												for(int i = 0 ; i< emails.size(); i++){
													
													Log.e("LogCat", " email some delete"+emails.get(i).getDate_time());
													
												}
												
											}
											
										}
										
										selectType = "manual";
										
									}
									
								});
							
								ImageView icon_image = (ImageView) convertView.findViewById(R.id.emaillistrow_icon_indicator);
								ImageView events_image = (ImageView) convertView.findViewById(R.id.emaillistrow_icon_yellow);
								ImageView business_image = (ImageView) convertView.findViewById(R.id.emaillistrow_icon_blue);
								ImageView media_image = (ImageView) convertView.findViewById(R.id.emaillistrow_icon_green);
								ImageView general_image = (ImageView) convertView.findViewById(R.id.emaillistrow_icon_white);
								
								subject.setText(""+mEmails.get(position).getSubject());
								sender.setText(""+mEmails.get(position).getEmail_Sender());
								icon_image.setImageResource(mEmails.get(position).getImage_resourceId());
								email_relative.setBackgroundResource(mEmails.get(position).getBackground_color());
	
								SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								DateFormat stringFormatter = new SimpleDateFormat("MMM dd"); 
								DateFormat stringTime = new SimpleDateFormat("HH:mm a"); 
								
								try
								{
									emDate = formatter.parse(mEmails.get(position).getDate_time());
									email_date = stringFormatter.format(emDate);
									email_time = stringTime.format(emDate);
									current_date = stringFormatter.format(currentDate);
								
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							
								if (email_date.equalsIgnoreCase(current_date))
									date.setText(""+email_time);
							   else
	 								date.setText(""+email_date);
							
								events_image.setVisibility(View.INVISIBLE);
								if (mEmails.get(position).getEvents_status() == 1) {
									events_image.setVisibility(View.VISIBLE);
								}
								business_image.setVisibility(View.INVISIBLE);
								if (mEmails.get(position).getBusiness_status() == 1) {
									business_image.setVisibility(View.VISIBLE);
								}
								media_image.setVisibility(View.INVISIBLE);
								if (mEmails.get(position).getMedia_status() == 1) {
									media_image.setVisibility(View.VISIBLE);
								}
								general_image.setVisibility(View.INVISIBLE);
								if (mEmails.get(position).getGeneral_status() == 1) {
									general_image.setVisibility(View.VISIBLE);
								}
								
								
								if (mEmails.get(position).getStatus()==0) 
								{
										
										sender.setTypeface(Typeface.DEFAULT_BOLD);
										subject.setTypeface(Typeface.DEFAULT_BOLD);
										date.setTypeface(Typeface.DEFAULT_BOLD);
										
									}else {
										
										sender.setTypeface(Typeface.DEFAULT);
										subject.setTypeface(Typeface.DEFAULT);
										date.setTypeface(Typeface.DEFAULT);
										
									}
								
					   	}else if (type == TYPE_SEPARATOR) 
					   	{
					   			ImageView icon = (ImageView) convertView.findViewById(R.id.email_seperator_indicator_img);
								if (mEmails.get(position).seperator.equalsIgnoreCase("--t"))
								{
									icon.setImageResource(R.drawable.blk_t_icon);
				            		convertView.setBackgroundResource(R.drawable.red_bar);
				            
								} else if (mEmails.get(position).seperator.equalsIgnoreCase("--y"))
								{
						
									icon.setImageResource(R.drawable.blk_y_icon);
									convertView.setBackgroundResource(R.drawable.purple_bar);
								    
								} else {
									
									icon.setImageResource(R.drawable.blk_3d_icon);
									convertView.setBackgroundResource(R.drawable.brown_bar);
									
								}
		
						}
						return convertView;
					}
		}

		

		//deleting mails from sever and database..
		@Override
		public void deleteEmails()
		{
			
//			stopService(new Intent(Emails_ListActivity.this, EmailDownLoadService.class)); 
			
			String key = "", email_Id = " ";
			long msgUid = 0 ;
			
			long lastUid = 0;
			
			long fetchId = 0;
			
			ArrayList  mail_deletion_temp = null;
			
			Iterator iterator =mSelectedEmail.keySet().iterator();
			
			while(iterator. hasNext())
			{ 
				
				key = ( String ) iterator.next();
				
				deleteItemsArray = new ArrayList();
				
				deleteItemsArray = (ArrayList) mSelectedEmail.get(key);
				
		        Log.v(LOG_TAG, "Hash Map Value "+key+" selected Items Size "+deleteItemsArray.size());
	            
	         }
						
			
	         mail_deletion_temp = new ArrayList();
	    
			 for (int iCtr = 0; iCtr < deleteItemsArray.size(); iCtr++)
			 {

				mEmailModel = (EmailModel) deleteItemsArray.get(iCtr);
				msgUid = mEmailModel.getFetched_id();
				mail_deletion_temp.add(msgUid);
				Log.v(LOG_TAG, "message Id "+msgUid);
	
			 }
				        
			 Log.v(LOG_TAG, "delete array "+mail_deletion_temp.size());

             for (int iCtr = 0; iCtr < mail_deletion_temp.size(); iCtr++)
             {
				
            	
	            	fetchId = (Long) mail_deletion_temp.get(iCtr);
	            	
	            	Log.v(LOG_TAG, "messages  "+fetchId);
	            	
				    email_Id = mEmailDatabase.getEmailIds_From_DataBase(fetchId);
				    
				    if (email_Id != null) 
				    {
						
				 		 Cursor cursor  = mDatabase.rawQuery("SELECT  Username, password ,IMAP_Server , Port  from ServerSettings  where Email_Id ='" +email_Id+"'", null);
							
						 Log.i("Query  ","SELECT  Username, password ,IMAP_Server , Port  from ServerSettings  where Email_Id ='" +key+"'");
					
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
									
									Log.i(LOG_TAG, " starting to retrieve the mail for the provider " + imap_server + " User Name "+ key);
								
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
							            messages[iMsg].setFlag(Flags.Flag.DELETED, true);
							            
							       }    
					            	
						            mEmailDatabase.deleteEmails(fetchId);
						   }//cursor loop
							    
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
				}
            }//for loop of deletion message
            
	 refresh();
	 deleteItemsArray.clear();
	 mail_deletion_temp.clear();
		 
	}

		public class EmailAsyncRead extends AsyncTask<String, Void, Void>
		{
      
			
			Context mContext;
			ProgressDialog mDialog;
			String mErrorMsg = "";
			int mStatus;
			long mMsgUid;
			String mEmail_id;
			String mEmail_from;
			
			public EmailAsyncRead(Context context, int status, String emailId, long msgUid, String email_from) 
			{
				
				mContext = context;
				mStatus = status;
				mMsgUid = msgUid;
				mEmail_id = emailId;
				mEmail_from = email_from;
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
//
//						 Intent intent=new Intent(Intent.ACTION_SEND_MULTIPLE);
//						 intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//						 intent.putExtra(Intent.EXTRA_EMAIL, mEmail_from);
//						 intent.putExtra(Intent.EXTRA_TEXT, messageBody);
//						 intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,uris);
//						 intent.setType("*/*");
//
//						 startActivity(intent);
						
						
						 Intent intent=new Intent(Emails_ListActivity.this, ReadEmailActivity.class);
						 intent.putExtra("subject", subject);
						 intent.putExtra("email_from", mEmail_from);
						 intent.putExtra("message_body", messageBody);
						 intent.putExtra("attachments", arrAttachment);
						 intent.putExtra("selectedUid", selectedUid);
						 intent.putExtra("emailId", emailId);
//						 intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,uris);
//						 intent.setType("*/*");

						 startActivity(intent);
					}
					
			}
		}		
		
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
		}// for connect to server...
	
}
