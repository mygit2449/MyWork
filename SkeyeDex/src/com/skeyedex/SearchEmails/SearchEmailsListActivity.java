package com.skeyedex.SearchEmails;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skeyedex.R;
import com.skeyedex.EmailDataBase.EmailDataBase;
import com.skeyedex.EmailDataBase.EmailDataBaseHelper;
import com.skeyedex.EmailsList.EmailReader;
import com.skeyedex.EmailsList.Emails_ListActivity;
import com.skeyedex.EmailsList.ReadEmailActivity;
import com.skeyedex.EmailsList.Emails_ListActivity.EmailAsyncRead;
import com.skeyedex.Model.EmailModel;

public class SearchEmailsListActivity extends Activity implements OnItemClickListener{

	EmailReader mEmailReader = null;
	EmailModel mEmailModel = null;
	ArrayList<EmailModel> array_emails_list = null, array_emails_listDump = null;
	ListView email_Listview;
	Context mContext;
	SearchEmailsAdapter mSearchEmailsAdapter = null, mSearchEmailsAdapterdump = null;
	EditText filter_edit;
	String subject, messageBody, attachment_name, from ;
    private String  emailId = "";
    ArrayList<Uri> uris = null;
    private long selectedUid = 0;
    String[] arrAttachment;
    private EmailDataBase mEmailDataBase = null;
    SQLiteDatabase mDatabase;
    int status;
    int read_status;
    long msgUid;
    
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
			super.onCreate(savedInstanceState);
			setContentView(R.layout.search_listview);
			email_Listview = (ListView)findViewById(R.id.search_listview);
			email_Listview.setOnItemClickListener(this);
			filter_edit = (EditText)findViewById(R.id.search_email_Edit);
			array_emails_list = new ArrayList<EmailModel>();
			array_emails_listDump = new ArrayList<EmailModel>();
			uris = new ArrayList<Uri>();

			EmailDataBaseHelper mEmailDataBaseHelper;
			 	
			mEmailDataBaseHelper = EmailDataBaseHelper.getDBAdapterInstance(this);
			mDatabase = mEmailDataBaseHelper.getReadableDatabase();
			mEmailDataBase = new EmailDataBase(SearchEmailsListActivity.this);
			 
			filter_edit.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					array_emails_listDump.clear();
					mSearchEmailsAdapterdump.clear();
					
					int text_length = filter_edit.getText().length();
					if (text_length == 0) 
					{
						email_Listview.setAdapter(mSearchEmailsAdapter);
						
					}else
					 {
						for (int iCtr = 0; iCtr < array_emails_list.size(); iCtr++) 
						{
			        		  
							String subject =((String)array_emails_list.get(iCtr).getSubject().toString().trim().toLowerCase());
							int subject_length = subject.indexOf(filter_edit.getText().toString().trim().toLowerCase());
							
							if (subject_length >= 0) {
								array_emails_listDump.add(array_emails_list.get(iCtr));
							}
						}
						email_Listview.setAdapter(mSearchEmailsAdapterdump);
					}
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
			 
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		
		
	     status = array_emails_list.get(position).getStatus();
	     read_status = 1;
 
	     
	     msgUid = array_emails_list.get(position).getFetched_id();

		 from = array_emails_list.get(position).getEmail_Sender();
		 
		 emailId = array_emails_list.get(position).getEmail_id();
	     
		 SearchAsync mSearchAsync = new SearchAsync(SearchEmailsListActivity.this, 2);
		 mSearchAsync.execute();

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
	public void refresh()
	{
		SearchAsync mSearchAsync = new SearchAsync(this, 1);
		mSearchAsync.execute();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		refresh();
	
	}
	
	public class SearchAsync extends AsyncTask<String, Void, Void>{

		Context mContext;
		ProgressDialog mDialog;
		String mErrorMsg = "";
		int type;
		public SearchAsync(Context context, int type) 
		{
			
			mContext = context;
			mDialog = ProgressDialog.show(context, "", "Loading Emails....", true, false);
			mDialog.setCancelable(true);
			this.type = type;
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			mEmailReader = new EmailReader(mContext);

			if (type == 1) 
			{
				try 
				 {
					array_emails_list = mEmailReader.getMailsFromWeb(1);
				 } catch (Exception e) 
				 {
					// TODO Auto-generated catch block
					e.printStackTrace();
				 }
				 Log.e("Search ", " size"+array_emails_list.size());
			}else if (type == 2) {
				connectToServer(status, mErrorMsg, msgUid);
			}
			return null;
		}
		
		public void onPostExecute(Void result) 
		{
			 try 
			 {
				 
				 mDialog.dismiss();
				 mDialog = null;
				 
			 } catch (Exception ex) {
				 ex.printStackTrace();
			    }
			 
			if (type == 1) 
			{
				mSearchEmailsAdapter = new SearchEmailsAdapter(mContext, array_emails_list);
				mSearchEmailsAdapterdump = new SearchEmailsAdapter(mContext, array_emails_listDump);
				email_Listview.setAdapter(mSearchEmailsAdapter);
			}else if (type == 2) {
				
				 Intent intent=new Intent(SearchEmailsListActivity.this, ReadEmailActivity.class);
				 intent.putExtra("subject", subject);
				 intent.putExtra("email_from", from);
				 intent.putExtra("message_body", messageBody);
				 intent.putExtra("attachments", arrAttachment);
				 intent.putExtra("selectedUid", selectedUid);
				 intent.putExtra("emailId", emailId);
				 startActivity(intent);
			}
		}
		
	}

    public class SearchEmailsAdapter extends ArrayAdapter<EmailModel>{
    	
    	ArrayList<EmailModel> mEmails;
    	Context context;
    	
    	private static final int TYPE_ITEM = 0;
		private static final int TYPE_SEPARATOR = 1;
 		private static final int TYPE_MAX_COUNT = 2;
 		
    	public SearchEmailsAdapter(Context context, ArrayList<EmailModel> emailMessages){
    		super(context, R.layout.search_email_list_row, emailMessages);
    		this.context = context;
    		this.mEmails = emailMessages;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			int type = getItemViewType(position);
			RelativeLayout email_relative;
			
			Date emDate = null;
			
			String email_date = "";
			String email_time = "";
		    String current_date = "";
		    
		    if (convertView == null)
			{
				
				LayoutInflater inflater = LayoutInflater.from(context);
				
				if (type == TYPE_ITEM) 
				{
					convertView = inflater.inflate(R.layout.search_email_list_row, null);
				}else {
					convertView = inflater.inflate(R.layout.email_seperator, null);
				}
				
			}
			
		    if (type == TYPE_ITEM)
			{
			
				email_relative = (RelativeLayout)convertView.findViewById(R.id.search_email_list_row);
	
				TextView email_subject = (TextView)convertView.findViewById(R.id.search_email_listview_row_txt_Subject);
				TextView email_sender  = (TextView)convertView.findViewById(R.id.search_email_listview_row_txt_Sender);
				TextView email_Date = (TextView)convertView.findViewById(R.id.search_email_listview_row_txt_Datetime);
				ImageView email_attachment = (ImageView)convertView.findViewById(R.id.search_email_listview_row_img_attachment);
				ImageView email_icon = (ImageView)convertView.findViewById(R.id.search_email_listview_row_img_icon);
	
				email_relative.setBackgroundResource(mEmails.get(position).getBackground_color());
				email_icon.setImageResource(mEmails.get(position).getImage_resourceId());
				
			    Date currentDate = new Date(System.currentTimeMillis());
	
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
			
			    String attachment;
				attachment = mEmails.get(position).getAttachmentName();
				
				if (attachment.length() > 0) 
					email_attachment.setVisibility(View.VISIBLE);
				else
					email_attachment.setVisibility(View.INVISIBLE);
				
				if (email_date.equalsIgnoreCase(current_date))
					email_Date.setText(""+email_time);
			    else
				   email_Date.setText(""+email_date);
				
				   email_subject.setText(mEmails.get(position).getSubject());
				   email_sender.setText(mEmails.get(position).getEmail_Sender());
				   
				   if (mEmails.get(position).getStatus()==0) 
					{
						email_subject.setTypeface(Typeface.DEFAULT_BOLD);
				   		email_sender.setTypeface(Typeface.DEFAULT_BOLD);
				   		email_Date.setTypeface(Typeface.DEFAULT_BOLD);
					}else 
					{
						email_subject.setTypeface(Typeface.DEFAULT);
						email_sender.setTypeface(Typeface.DEFAULT);
						email_Date.setTypeface(Typeface.DEFAULT);
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
	
			   }// else if for type seperator   
			   
			return convertView;
		}// for getview..
    	
    }
	
    
 // method for read and unread messages
 		public void connectToServer(int status, String emailId, long msgUid)
 		{
 			
 			int read_status = 1;
 			
 			Log.v(getClass().getSimpleName(), " msgStatus "+status+ " emailId "+emailId+ " MsgUid "+msgUid);
 			if (status == 0)
 			  {
 			
 				mEmailDataBase.updateEmailStatus(read_status, status, msgUid);
 		
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
 										 Log.v(getClass().getSimpleName(), "subject "+subject);
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
