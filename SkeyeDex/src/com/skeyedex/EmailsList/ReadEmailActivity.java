package com.skeyedex.EmailsList;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;

import android.app.Activity;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.skeyedex.R;
import com.skeyedex.EmailDataBase.EmailDataBaseHelper;
import com.skeyedex.dialog.AlertDialogMsg;


public class ReadEmailActivity extends Activity implements OnClickListener{

	private static final String LOG_TAG = ReadEmailActivity.class.getSimpleName();
	
	private Intent receiverIntent;
	private EditText mEmail_from_Edt, mSubject_Edt;
	private Button mReply_Btn;
	private WebView mEmailbody_Web;
	private String attachment_array[] ;
	private TextView attachment_text;
	private String mTitle = "Skeyedex";
	private String mMessage = "Do you want to download the attachment?";
	private String file = "";
	private long mSelectedUId = 0;
	private String mEmail_Id = "";
	private ArrayList<TextView> mAttachments = null;
	    
	private SQLiteDatabase mDatabase;
	
	private EmailDataBaseHelper mEmailDataBaseHelper;

	private String sdcard_path = "";
	private File fileIn = null;
	private String attachment_name = "";
	private int iCtr;
    private int mAlertType = 0;
    private String email_from = "";
    private String subject = "";
    private String messageBody = "";


	@Override
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_email);
		receiverIntent = getIntent();
		initializeUI();
		email_from = receiverIntent.getExtras().getString("email_from");
		subject = receiverIntent.getExtras().getString("subject");
		messageBody = receiverIntent.getExtras().getString("message_body");
		
		attachment_array = receiverIntent.getExtras().getStringArray("attachments");
		mSelectedUId = receiverIntent.getExtras().getLong("selectedUid");
		mEmail_Id = receiverIntent.getExtras().getString("emailId");
			 	
		mEmailDataBaseHelper = EmailDataBaseHelper.getDBAdapterInstance(this);
		mDatabase = mEmailDataBaseHelper.getReadableDatabase();
		
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.read_email_LL_Txt);
				
		Log.v("attachment", "no of attachments "+attachment_array.length);
		
		mAttachments = new ArrayList<TextView>();
		
		for (iCtr = 0; iCtr < attachment_array.length; iCtr++)
		  {
			 attachment_text = new TextView(this);
			 LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			 attachment_text.setLayoutParams(params);
			 attachment_text.setTextColor(getResources().getColor(R.color.textcolor));
			 attachment_text.setTextSize(getResources().getDimension(R.dimen.textsize));
			
			 file = attachment_array[iCtr];
		
			 attachment_text.setText(file);
			 attachment_text.setId(1);
			 attachment_text.setTag(iCtr);
			 attachment_text.setClickable(true);
			 attachment_text.setOnClickListener(this);	
			 mAttachments.add(attachment_text);
			 ll.addView(attachment_text, params);
		  }
		
		String mime = "text/html";
		String encoding = "utf-8";
		mEmail_from_Edt.setText(""+email_from);
		mSubject_Edt.setText(subject);
		mEmailbody_Web.loadDataWithBaseURL(null, messageBody, mime, encoding, null);
	}
	
	private void initializeUI() {
		// TODO Auto-generated method stub
		mEmail_from_Edt = (EditText)findViewById(R.id.read_email_Txt);
		mSubject_Edt = (EditText)findViewById(R.id.read_email_Txt_Subject);
		mEmailbody_Web = (WebView)findViewById(R.id.readmail_webview);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case 1:
			
//			Toast.makeText(this, mAttachments.get(Integer.parseInt(v.getTag().toString())).getText(), Toast.LENGTH_SHORT).show();

			 sdcard_path = String.format(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Skeyedex");
			 attachment_name = (String) mAttachments.get(Integer.parseInt(v.getTag().toString())).getText();
			 
			 fileIn = new File(sdcard_path+ "/"+attachment_name);	
			 alertWithMsg(mTitle, mMessage);
			break;
		
		}
	}
	
	public void alertWithMsg(String title, String msg){
		
		new AlertDialogMsg(this, title, msg).setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				DownloadAttachmentAsync mAttachmentAsync = new DownloadAttachmentAsync(ReadEmailActivity.this, mEmail_Id, mSelectedUId);
				mAttachmentAsync.execute();
			}
		}).setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).setIcon(R.drawable.ic_menu_more).create().show();

	}
	
	
	public class DownloadAttachmentAsync extends AsyncTask<String, Void, Void>{

		Context mContext;
		ProgressDialog mDialog;
		String mErrorMsg = "";
		long mMsgUid;
		String mEmail_id;

		
		public DownloadAttachmentAsync(Context context, String emailId, long msgUid) 
		{
			
			mContext = context;
			mMsgUid = msgUid;
			mEmail_id = emailId;
			mDialog = ProgressDialog.show(context, "", "Downloading attachment....", true, false);
			mDialog.setCancelable(true);
		
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try
			{
				connectToServer(mEmail_id, mMsgUid);
				
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
					alertWithMsg("Skeyedex", mErrorMsg);
					
				}else {
					//Toast.makeText(ReadEmailActivity.this, "attachment saved", Toast.LENGTH_SHORT).show();
				
	                Intent intent = new Intent();
	                intent.setAction(Intent.ACTION_VIEW);
				    Uri uri = Uri.fromFile(fileIn);

	                if(attachment_name.endsWith(".jpeg")||attachment_name.endsWith("png")||attachment_name.endsWith(".gif"))
	                {
	                    intent.setDataAndType(uri, "image/*");
	                    startActivity(intent);
	                }
	                else if(attachment_name.endsWith(".mp4")||attachment_name.endsWith(".3gp"))
	                {
	                    intent.setDataAndType(uri, "video/*");
	                    startActivity(intent);
	                }
	                else if(attachment_name.endsWith(".mp3"))
	                {
	                    intent.setDataAndType(uri, "audio/*");
	                    startActivity(intent);
	                }else if(attachment_name.endsWith(".doc"))
	                {
	                    intent.setDataAndType(uri, "application/msword");
	                    startActivity(intent);
	                }else if(attachment_name.endsWith(".pdf"))
	                {
	                    intent.setDataAndType(uri, "*/*");
	                    startActivity(intent);
	                }else if(attachment_name.endsWith(".rtf"))
	                {
	                    intent.setDataAndType(uri, "application/vnd.ms-excel");
	                    startActivity(intent);
	                }else if(attachment_name.endsWith(".ppt") || attachment_name.endsWith(".pptx"))
	                {
	                    intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
	                    startActivity(intent);
	                }else if(attachment_name.endsWith(".rtf"))
	                {
	                    intent.setDataAndType(uri, "application/vnd.ms-excel");
	                    startActivity(intent);
	                }else if(attachment_name.endsWith(".txt"))
	                {
	                    intent.setDataAndType(uri, "text/plain");
	                    startActivity(intent);
	                }else if(attachment_name.endsWith(".csv"))
	                {
	                    intent.setDataAndType(uri, "text/csv");
	                    startActivity(intent);
	                }else if(attachment_name.endsWith(".xml"))
	                {
	                    intent.setDataAndType(uri, "text/xml");
	                    startActivity(intent);
	                }else if(attachment_name.endsWith(".html"))
	                {
	                    intent.setDataAndType(uri, "text/html");
	                    startActivity(intent);
	                }
	                else {
						intent.setDataAndType(uri, "*/*");
						startActivity(intent);
					}

				}
		
	}

	
		// method for downloading attachment
			public void connectToServer(String emailId, long msgUid)
			{
				
					Log.v(LOG_TAG,"email Id "+emailId+" Uid "+msgUid);
				
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
							            	 String contentType = messages[iCtr].getContentType(); 
							            	 if (contentType.contains("multipart")) 
							            	 {
									        	 
									        	 Multipart multiPart = (Multipart)messages[iCtr].getContent();  
									        	 
									                int partCount = multiPart.getCount();  
									                
									                for (int j = 0; j < partCount; j++)
									                { 
									                	
									                    BodyPart part = multiPart.getBodyPart(j);  
							                    
									                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))
										                    { 
									                    		Log.v(LOG_TAG, "attachment name "+part.getFileName());
													    	    saveFile(part.getFileName(), part.getInputStream());

										                    }
									                }
									         }
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
						 
				  	
			}// for connec
			
	}
			
			public static boolean isSdPresent() {
				return android.os.Environment.getExternalStorageState().equals(
						android.os.Environment.MEDIA_MOUNTED);
			}
			
			public void saveFile(String filename, InputStream inputstream)
			{
				 if(isSdPresent())
				 {
					     
					 String skeyedexFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Skeyedex";
					 Log.e(LOG_TAG, "file path"+skeyedexFolderPath);
					 
					 File skeyedexDirectory=new File(skeyedexFolderPath);
					 String filepath = skeyedexFolderPath + "/"+ filename;
					 if(!skeyedexDirectory.exists())
						 skeyedexDirectory.mkdirs();
					 try 
					 {

							BufferedOutputStream outfile= new BufferedOutputStream(new FileOutputStream(filepath));
							byte[] buffer= new byte[1024];
							
							int length;
					        while ((length = inputstream.read(buffer))>0) 
					        {
					        	outfile.write(buffer, 0, length);
					        }
					        Log.v(LOG_TAG, "write to sd card");
							outfile.close();
							
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 } 
				 
			  }
}
