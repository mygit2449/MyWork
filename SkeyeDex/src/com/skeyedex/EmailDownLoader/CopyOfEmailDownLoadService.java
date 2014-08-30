package com.skeyedex.EmailDownLoader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.internet.InternetAddress;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;

import com.skeyedex.EmailDataBase.ContactsDataBase;
import com.skeyedex.EmailDataBase.EmailDataBaseHelper;
import com.skeyedex.Model.ContactsModel;

public class CopyOfEmailDownLoadService extends Service {
	
	private static final String LOG_TAG = CopyOfEmailDownLoadService.class.getSimpleName();
		
    SharedPreferences app_preferences;
    SharedPreferences.Editor editor;
    String email_id = "";
	Folder folder = null, inboxFolder = null;
	
	boolean isMailSynThreadInterrupt = false;

	private final IBinder mIbinder = new SkyedexBinder();
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mIbinder;
	}

	public void onCreate() 
	{
		
		super.onCreate();
  	    app_preferences=getSharedPreferences("prefs",0);
  	    editor = app_preferences.edit();
  	    
	}

	@Override
	public int onStartCommand(Intent intent, int flages, int startId) {
		super.onStart(intent, startId);
		Thread thred = new Thread() {
			public void run() {
				
				while(!isMailSynThreadInterrupt)
				{
					try{
						
						Log.i(LOG_TAG, "Syn process triggered");

						receiveMailsFromWeb();
						Thread.sleep(30000); // 3000 * 60
						
					}catch(Exception ex){
						
					}
				}
				
			}
		};
		thred.start();
		return START_STICKY;
	}

		private class SkyedexBinder extends Binder
		{
				CopyOfEmailDownLoadService getService() 	
				{
					return CopyOfEmailDownLoadService.this;
				}
		}
	
	 public void receiveMailsFromWeb() 
	 {
		 	Calendar startDateTime = Calendar.getInstance();
			ArrayList<ContactsModel> phone_Contacts = null;
			EmailDataBaseHelper mEmailDataBase;
			SQLiteDatabase mDatabase;
			mEmailDataBase = EmailDataBaseHelper.getDBAdapterInstance(this);

			/*ContactsDataBase mContactsDataBase =null;
			mContactsDataBase = new ContactsDataBase(this);
			phone_Contacts = mContactsDataBase.getContacts_From_DataBase();
			mContactsDataBase = null;*/
			
			mDatabase = mEmailDataBase.getReadableDatabase();
			ArrayList<ArrayList> arrImap = new ArrayList<ArrayList>();
			
			Cursor cursor  = mDatabase.rawQuery("SELECT  Username, password ,IMAP_Server , Port, Email_Type, Email_Id  from ServerSettings", null);
			try{
				if(cursor !=null  )
				{
					
					Log.i(LOG_TAG, " the no of accounts available are " + cursor.getCount());
					
					while(cursor.moveToNext())
					{
						ArrayList<String> arrImapDetails = new ArrayList<String>();
						arrImapDetails.add((String)cursor.getString(0));
						arrImapDetails.add((String)cursor.getString(1));
						arrImapDetails.add((String)cursor.getString(2));
						arrImapDetails.add((String)cursor.getString(3));
						arrImapDetails.add((String)cursor.getString(4));
						arrImapDetails.add((String)cursor.getString(5));
						
						arrImap.add(arrImapDetails);
						
					}
				}
			}catch(Exception ex){ ex.printStackTrace();}
			
			finally
			{
				try
				{
			
					cursor.close();
				}catch(Exception ex) {
					Log.e("EmailService", "" + ex.toString());
				}
			}
				
			
			try
			{   
				
					
					Log.i(LOG_TAG, " the no of accounts available are " + arrImap.size());
					int imapCtr = 0;
					while(imapCtr < arrImap.size())
					{
						
						Store mStore = null;
						Message[] messages = null;
						Address[] froms = null;
						Properties properties = System.getProperties();
						properties.put("mail.imap.host", "imaps");
						properties.put("mail.imap.fetchsize", "1048576");
						properties.put("mail.imaps.partialfetch", "false");
						ArrayList<String> arrImapDetails = (ArrayList<String>) arrImap.get(imapCtr);
						
						String username = (String)arrImapDetails.get(0);
						String password = (String) arrImapDetails.get(1); 
						String imap_server =(String) arrImapDetails.get(2);
						String Email_Type = (String)arrImapDetails.get(4);
						int port = Integer.parseInt((String ) arrImapDetails.get(3));
						email_id = (String)arrImapDetails.get(5);
						
						Log.i(LOG_TAG, " starting to retrieve the mail for the provider " + imap_server + " User Name "+ username);
						
						Session session = Session.getInstance(properties, null);
						
				
						try
						{
							
								mStore = session.getStore("imaps");
								mStore.connect(imap_server, port,  username, password);
								
								inboxFolder = mStore.getDefaultFolder();
								folder = inboxFolder.getFolder("INBOX");
					            UIDFolder ufolder = (UIDFolder) folder;
	
								folder.open(Folder.READ_ONLY);
						            
					            long lasUid =app_preferences.getLong(imap_server, 1);
					            
					            // get only the email based on the last UID retrieved earlier and plus 500000 
					            messages = ufolder.getMessagesByUID(lasUid + 1, lasUid+50000);
					            FetchProfile fp = new FetchProfile();
					            fp.add(FetchProfile.Item.ENVELOPE);
					            fp.add(FetchProfile.Item.FLAGS);
					            fp.add("X-Mailer");
					            folder.fetch(messages, fp);
				
					            Log.i(LOG_TAG,"No of  mails retrieved for the " + Email_Type + "  is  " + messages.length + " last uid " + lasUid);
				                
					           long  fetchedLastUid = lasUid; // it will have the las uid for the eamil that was downloaded earlier
					           
				                int iCtr  =0;		
				                
				                if(messages.length > 0 )
				                	mDatabase.beginTransaction();
				                
								for (iCtr = 0; iCtr < messages.length; iCtr++) 
								{
								
										
									
									String from = "";
									String subject = "";
									String date = "";
									String time = "";
									String date_Time = "";
								    String textMessage = "";
								    String fileExtension = "";
								    String filename ="";
								    String attachment = "";
								    String delimeter = "";
									int msg_status, eventsAndaddress = 0 , businessAndDocuments =0, mediaAndPhotos =0, generalAndFamily ;

									//Read Or Unread
									if(!messages[iCtr].isSet(Flags.Flag.SEEN))
										  msg_status = 0;
									else
										msg_status = 1;
									
									    // FROM
									froms  = messages[iCtr].getFrom();
									
									try{
										if (froms != null) 
											from =  ((InternetAddress) froms[0]).getAddress();
									}catch (Exception e) {
										// TODO: handle exception
									}
									

								   try
								   {	
									
										SimpleDateFormat	formatterDate = new SimpleDateFormat("yyyy-MM-dd");
										SimpleDateFormat	formatterTime = new SimpleDateFormat("HH:mm:ss");
										
										
										date = formatterDate.format(messages[iCtr].getSentDate()).toString();
										time = formatterTime.format(messages[iCtr].getSentDate()).toString();
										date_Time =  date + " " + time; 
									
								   }catch(Exception ex){ex.printStackTrace();}
								   
								   try
								   {
									   
									   subject = messages[iCtr].getSubject().toString();
									   
								   }catch(Exception ex){ex.printStackTrace();}
								  
									fetchedLastUid = ufolder.getUID(messages[iCtr]);
									Log.v(LOG_TAG ,  "Date " +  date_Time + " subject " + subject + " from " + froms );
									
									 
									generalAndFamily = 0 ;
									/*try{
										for (int ifamilyCtr = 0; ifamilyCtr < phone_Contacts.size(); ifamilyCtr++) 
										{
											if (from.equalsIgnoreCase(phone_Contacts.get(ifamilyCtr).getEmail_id())) 
											{
												generalAndFamily = 1;
												break;
											}
											
										}
										
									}catch(Exception ex){ ex.printStackTrace();}
									*/		
									
								    String contentType = messages[iCtr].getContentType().toLowerCase();  
								    Log.v(LOG_TAG, "The Content Type is " + contentType);
								    
							        try{
							        
								    if (contentType.indexOf("text/plain") >=0 ) 
							        { 
							        	 if (messages[iCtr].getContent() !=  null) 
							        		 textMessage =  messages[iCtr].getContent().toString();
							        	 else
							        		 textMessage = "";
							        	 
							        	 Log.v(LOG_TAG , "The mail content type is text/plain");
							        //if no subject then this condition will satisfy
							        }else if(contentType.indexOf("text/html") >=0 ){
							     	   //textMessage="<HR/>"+messages[iCtr].getContent().toString();
							        	 textMessage = textMessage+ Html.fromHtml(messages[iCtr].getContent().toString()).toString();
							     	     Log.v(LOG_TAG , "The mail content type is text/html");

							        }else if (contentType.indexOf("multipart") >=0 ) {
							        	
							        	 	Multipart multiPart = (Multipart)messages[iCtr].getContent();  
							        	 	Log.v(LOG_TAG , "The Multi part content type part size is " + multiPart.getCount());

							                int partCount = multiPart.getCount();  
							                
							                for (int iPtr = 0; iPtr < partCount; iPtr++)
							                { 
							                	
							                    BodyPart part = multiPart.getBodyPart(iPtr);  
							                    String sp = part.getContentType().toLowerCase();		
							                    Log.v(LOG_TAG , "The Multi part content type  " + sp + "( part " + iPtr + ")");
							                    
							                    if(sp.indexOf("text/plain") >=0)
							                    {
							                    	   
							                    	   textMessage = Html.fromHtml(part.getContent().toString()).toString();
							                    	   Log.v(LOG_TAG , "The multi part content type is text/plain ");
							                    	   
							                     }else if (sp.indexOf("text/html") >=0) {

							                    	   textMessage= Html.fromHtml(part.getContent().toString()).toString();
							                    	   Log.v(LOG_TAG , "The multi part content type is text/html");
							                    	   
							                      }else if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) ||  Part.INLINE.equalsIgnoreCase(part.getDisposition()) ){ 
							                    	  
								                   
							                    	    Log.v(LOG_TAG , "The multi part content type is attachmemnt");
							                    	    
								                    	filename = part.getFileName().toString();
								                    	attachment = delimeter + filename;
												    	
								                    	// for filtering the file names to respective groups
								                    	int indexPos = filename.indexOf(".");
												    	
												    	fileExtension = filename.substring(indexPos + 1, filename.length());
											    	
											    	    saveFile(part.getFileName(), part.getInputStream()); // save files in sdcard
										
											    		mediaAndPhotos = 0;
														if (   fileExtension.equalsIgnoreCase("jpeg")    ||    fileExtension.equalsIgnoreCase("bmp")  || 
																fileExtension.equalsIgnoreCase("png") 	   ||    fileExtension.equalsIgnoreCase("psd")    || 
																fileExtension.equalsIgnoreCase("tiff")   	   ||    fileExtension.equalsIgnoreCase("gif")      || 
															    fileExtension.equalsIgnoreCase("mpeg") ||     fileExtension.equalsIgnoreCase("mpg")   ||
															    fileExtension.equalsIgnoreCase("mp4")   ||     fileExtension.equalsIgnoreCase("mp3")  ||
															    fileExtension.equalsIgnoreCase("mov")   ||     fileExtension.equalsIgnoreCase("DV")     || 
															    fileExtension.equalsIgnoreCase("SWF")   ||     fileExtension.equalsIgnoreCase("WMV") || 
															    fileExtension.equalsIgnoreCase("avi")      ||     fileExtension.equalsIgnoreCase("WAV") || 
															    fileExtension.equalsIgnoreCase("3gp")    ||     fileExtension.equalsIgnoreCase("act")    || 
															    fileExtension.equalsIgnoreCase("flv")       || 	  fileExtension.equalsIgnoreCase("amr")  ||
																fileExtension.equalsIgnoreCase("ra")       ||     fileExtension.equalsIgnoreCase("wma")) {
															
																mediaAndPhotos = 1;
														}
														businessAndDocuments = 0;
														 if (  fileExtension.equalsIgnoreCase("doc")  ||  fileExtension.equalsIgnoreCase("docx")  || 
																fileExtension.equalsIgnoreCase("pdf") 	 || fileExtension.equalsIgnoreCase("xls")    || 
																fileExtension.equalsIgnoreCase("xlsx")   	 || fileExtension.equalsIgnoreCase("pptx")      || 
																fileExtension.equalsIgnoreCase("pps ppt")	 || fileExtension.equalsIgnoreCase("odp") ||
																fileExtension.equalsIgnoreCase("odt") || fileExtension.equalsIgnoreCase("ods")){ 
															 
															 businessAndDocuments =1;
														 }	
														 eventsAndaddress = 0;
														if (  fileExtension.equalsIgnoreCase("csv") || fileExtension.equalsIgnoreCase("txt") || fileExtension.equalsIgnoreCase("rtf")) {
																 
																  eventsAndaddress = 1;
														}
																 
							                      }else if(part.getContent() != null){
							                    	  Log.v(LOG_TAG , "The multi part content type is unknown");
							                    	  //textMessage = Html.fromHtml(part.getContent().toString()).toString();
							                    	  textMessage = Html.fromHtml(getBody(multiPart.getBodyPart(iPtr))).toString();
							                      }
										 
								        }  // loop for checking multipart  
							            							            
							                
								    }// if condition for checking multipart    
									}catch(Exception ex){
							        	ex.printStackTrace();
							        }
							        Log.v(LOG_TAG, "Text Message " + textMessage);
								    Log.e(LOG_TAG, " E-As "+eventsAndaddress+"  B-Ds "+businessAndDocuments+"  M-Ps "+mediaAndPhotos);
									saveMailsInDataBase(date, email_id, time,  from, subject,fetchedLastUid, mDatabase, msg_status, eventsAndaddress, businessAndDocuments, mediaAndPhotos, generalAndFamily, date_Time, textMessage, attachment);
								
									
								} // for the messaged for loop
							
							   if(iCtr>0){
								   
				                  editor.putLong(imap_server, fetchedLastUid);		                
								  editor.commit();
							   }
							   
								inboxFolder = null;
								mStore.close();
								mStore = null;
								messages = null;
								if (mDatabase.inTransaction()) 	
								{
									
									mDatabase.setTransactionSuccessful();
									mDatabase.endTransaction();
									
								}
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}finally
						{
							try
							{
								try{
									if(inboxFolder !=null)
									{
										inboxFolder.close(false);
										inboxFolder = null;
									}
								}catch(Exception ex)
								{}
								if(mStore !=null) 
								{
									mStore.close();
									mStore = null;
								}
								if (mDatabase.inTransaction()) 	
									mDatabase.endTransaction();
								
							}catch(Exception ex) {
								Log.e("EmailService", "" + ex.toString());
							}
						}
						imapCtr ++;
					} // for the cursor loop
				 
			        Calendar endDateTime = Calendar.getInstance();
			        long milliseconds1 = startDateTime.getTimeInMillis();
			        long milliseconds2 = endDateTime.getTimeInMillis();
			        long diff = milliseconds2 - milliseconds1;
			        
			        long min = (diff/60000);
			        
			        
			        Log.v(LOG_TAG,"The time lapsed for the process  is " +  min );

			} catch (Exception e) {
				Log.e("EmailService", "" + e.toString());

			}
			
			finally
			{
				try
				{
			
					
				}catch(Exception ex) {
					Log.e("EmailService", "" + ex.toString());
				}
			}
				
				
		
	}
	
	 

	 public String getBody(Part p) throws     MessagingException, IOException 
	 {
		 if (p.isMimeType("text/*")) 
		 {
	            String body = (String)p.getContent();
	        
	            return body;
	     }

		 if (p.isMimeType("multipart/alternative"))
		 {
	            // prefer html text over plain text
	            Multipart mp = (Multipart)p.getContent();
	            String text = null;
	            for (int i = 0; i < mp.getCount(); i++) 
	            {
	                Part bp = mp.getBodyPart(i);
	                if (bp.isMimeType("text/plain")) 
	                {
	                    if (text == null)
	                        text = getBody(bp);
	                    continue;
	                } else if (bp.isMimeType("text/html")) {
	                    String body_text = getBody(bp);
	                    if (body_text != null)
	                        return body_text;
	                } else {
	                    return getBody(bp);
	                }
	            }
	            return text;
		 }else if (p.isMimeType("multipart/*")) {
			 
	            Multipart mp = (Multipart)p.getContent();
	            for (int i = 0; i < mp.getCount(); i++) 
	            {
	                String body_part = getBody(mp.getBodyPart(i));
	                if (body_part != null)
	                    return body_part;
	            }
	            
	        }
		return null;
		 
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
	

	private void saveMailsInDataBase(String date, String email, String time, String from, String subject,	long fetchedLastUid, SQLiteDatabase _mDatabase, int msg_status, int eventsAndaddress, int businessAndDocuments, int mediaAndPhotos, int generalAndFamily, String date_Time, String MessageBody, String Attachment)
	{
			     
		// TODO Auto-generated method stub
		try 
		{
			
			ContentValues values = new ContentValues();
			values.put("EmailDate", date);
			values.put("Email_Id", email);
			values.put("EmailTime", time);
	        values.put("EmailFrom", from);
	        values.put("Subject", subject);
	        values.put("Uid", fetchedLastUid);
	        values.put("EmailStatus", msg_status);
	        values.put("EnF_Category", eventsAndaddress);
	        values.put("BnD_Category", businessAndDocuments);
	        values.put("MnP_Category", mediaAndPhotos);
	        values.put("GnF_Category", generalAndFamily);
	        values.put("Email_dateTime", date_Time);
	        values.put("MessageBody", MessageBody);
	        values.put("Attachment", Attachment);
	        _mDatabase.insert("ReceivedMails", null, values);
	       
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally  
		{
			
		}
	}
	
	
}
