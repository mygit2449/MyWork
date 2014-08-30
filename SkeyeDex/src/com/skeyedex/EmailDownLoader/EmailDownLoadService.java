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
import java.util.TimeZone;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
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
import android.util.Log;

import com.skeyedex.EmailDataBase.EmailDataBase;
import com.skeyedex.EmailDataBase.EmailDataBaseHelper;
import com.skeyedex.Model.EmailModel;

public class EmailDownLoadService extends Service {
	
	private static final String LOG_TAG = EmailDownLoadService.class.getSimpleName();
	private Renderable latestMessage = null;
	
    SharedPreferences app_preferences;
    SharedPreferences.Editor editor;
	boolean isMailSynThreadInterrupt = false;
	ArrayList<EmailModel>  mLatestMails = null;
	EmailModel mEmailModel = null;
	SQLiteDatabase mDatabase = null;
	EmailDataBaseHelper mEmailDataBase;
    EmailDataBase mEmailDatabase_check = null;

    String email_id = "";
    
	ArrayList<String> attachments = null;

	private final IBinder mIbinder = new SkyedexBinder();
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mIbinder;
	}

	public void onCreate() 
	{
		
		super.onCreate();
  	    app_preferences=getSharedPreferences("skeyedex",MODE_WORLD_READABLE);
  	    editor = app_preferences.edit();
  	    mLatestMails= new ArrayList<EmailModel>();
  	    attachments = new ArrayList<String>();
	}

	Thread thred;
	public static boolean mBreak= false;
	
	
	
	public static void stopDownload(){
		
		mBreak= true;
		
	}

	@Override
	public int onStartCommand(Intent intent, int flages, int startId) {
		super.onStart(intent, startId);
		thred = new Thread() {
			public void run() {
				
				while(!isMailSynThreadInterrupt)
				{
					try{
						
						Log.i(LOG_TAG, "Syn process triggered");
						mBreak= false;
						receiveMailsFromWeb();
						Thread.sleep(30000); // 1000 * 60
						
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
				
			}
		};
		thred.start();
		return START_STICKY;
	}

		private class SkyedexBinder extends Binder
		{
				EmailDownLoadService getService() 	
				{
					return EmailDownLoadService.this;
				}
		}
	
	 public void receiveMailsFromWeb() 
	 {

		 
		 Log.e("LogCat", "************************ service Call");
		 	long start_time = System.currentTimeMillis();
			ArrayList<ArrayList> arrImap = new ArrayList<ArrayList>();
			mEmailDataBase = EmailDataBaseHelper.getDBAdapterInstance(this);
			mEmailDatabase_check = new EmailDataBase(getApplicationContext());
			mDatabase = mEmailDataBase.getReadableDatabase();
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
			       
					
					Calendar dayBeforeYesterday = Calendar.getInstance();
					dayBeforeYesterday.add(Calendar.DAY_OF_MONTH, -3);
					
					Date dby_date = dayBeforeYesterday.getTime();
					
					SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
					SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
					formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
					String dt1 = dateFormatter.format(dby_date);
					
					Log.v("current date time " , dt1);
					
					Date value = null;
					try{
						value = dateFormatter.parse(dt1);
					}catch(Exception ex) {}
					
					Log.v("current date time " ," Date "+ value);
					long dby_in_milli = value.getTime();
					
					Date msg_recived_date;
					long msg_time_in_milli = 0;
					
					while(imapCtr < arrImap.size())
					{
						
						if(mBreak)
							break;
						
						Store mStore = null;
						Message[] messages = null;
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
						Folder inboxFolder = null;
						Folder folder = null;
				
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
				                
					           long  fetchedLastUid = 0 ; // it will have the last uid for the Email that was downloaded earlier
					           long latestuid = lasUid;
					           
				                int iCtr  =0;		
				                
				                //if(messages.length > 0 )
				                	//mDatabase.beginTransaction();
				                
				                
								for (iCtr = messages.length - 1; iCtr >= 0  ; iCtr--) 
								{
									
									if(mBreak)
										break;
									
									mEmailModel = new EmailModel();
									latestuid = ufolder.getUID(messages[messages.length - 1]);
									
									String from = "";
									int msg_status =0;
									Address[] froms = null;
									String date = "";
									String time = "";
									String date_Time = "";
									String subject = "";
									String Time_Zone = "";
									long first_in_milli = 0;
									String email_time_zone = "";
									//Read Or Unread
									if(!messages[iCtr].isSet(Flags.Flag.SEEN))
										  msg_status = 0;
									else
										msg_status = 1;
									// FROMs
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
										
										date = formatterDate.format(messages[iCtr].getReceivedDate()).toString();
										time = formatterTime.format(messages[iCtr].getReceivedDate()).toString();
										
										msg_recived_date = formatterDate.parse(date);
										//msg_time_in_milli = msg_recived_date.getTime();
										date_Time =  date + " " + time; 
										
										email_time_zone = messages[iCtr].getReceivedDate().toString();
										

										
										/*
										Calendar first_mail = Calendar.getInstance();
										first_mail.setTime(messages[messages.length - 1].getReceivedDate());// first mail time
										first_mail.add(Calendar.DAY_OF_MONTH, -3);
										first_mail.set(Calendar.HOUR_OF_DAY, 0);
										first_mail.set(Calendar.MINUTE, 0);
										first_mail.set(Calendar.SECOND, 0);
										Date first_date = first_mail.getTime();
										first_in_milli = first_date.getTime();
										*/
										
										Calendar email_timeZone = Calendar.getInstance();
										email_timeZone.setTime(messages[iCtr].getReceivedDate());
										Date email_time = email_timeZone.getTime();
										
										String em_Time = formatter.format(email_time);
										
										Date email_date = null;
										try{
											email_date = formatter.parse(em_Time);
										}catch(Exception ex) {}
										
										 msg_time_in_milli = email_date.getTime();
										
										Log.v(LOG_TAG," email time string "+em_Time+" date "+email_date);
										SimpleDateFormat mail_sdf = new SimpleDateFormat("z");
										//mail_sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
										Time_Zone = mail_sdf.format(messages[iCtr].getReceivedDate());
									    //Log.v(LOG_TAG, "email time zone"+mail_sdf.format(messages[iCtr].getReceivedDate()));
									     
								   }catch(Exception ex){ex.printStackTrace();}
								   
								   try
								   {
									   
									   subject = messages[iCtr].getSubject().toString();
									   
								   }catch(Exception ex){ex.printStackTrace();}
								  
									fetchedLastUid = ufolder.getUID(messages[iCtr]);
									Log.v(LOG_TAG ,  "Date " +  messages[iCtr].getReceivedDate() + " subject " + subject + " from " + from );

									setLatestMessage(messages[iCtr]);
									
									
						            int mediaAndPhotos = 0;
						            int businessAndDocuments = 0;
						            int eventsAndaddress = 0;
						            int generalAndFamily =0;
						            String attachment = "";
						            String body ="";
						            
									if(latestMessage != null)
									{
										
										if(mBreak)
											break;
										Log.e(LOG_TAG, "dby="+dby_in_milli + "  mtime "+ msg_time_in_milli+" latest uid "+latestuid);

										
										if(msg_time_in_milli > dby_in_milli)
										{
//											if(mBreak)
//												break;
											
											body = latestMessage.getBodytext();
								            String delimeter = "";
								            String filename = "";
								            StringBuilder attachment_filename = new StringBuilder();
								            for(int iLctr=0;iLctr<latestMessage.getAttachmentCount();iLctr++) 
								            {
								                Attachment at=latestMessage.getAttachment(iLctr);
								                //attachment_filename.append(delimeter + at.getFilename());
//								                attachment = delimeter + at.getFilename();
								                //delimeter = ",";
								                Log.v("Email", "attachment name "+at.getFilename());
//								                attachment = delimeter + at.getFilename();
//								                delimeter = ",";
								               filename = at.getFilename();
								               
								              if (filename != null) {
									               attachments.add(filename);
								              }
								               
								               for (String name : attachments)
										         {
										        	 attachment += name + ",";
										         }
						                    	 
						                    	attachments.clear();
								                if(mediaAndPhotos == 0)
								                	 mediaAndPhotos = at.mediaAndPhotos;
								                if(businessAndDocuments == 0)
								                	businessAndDocuments = at.businessAndDocuments;
								                if(eventsAndaddress == 0)
								                	eventsAndaddress = at.eventsAndaddress;
								            }
								        	//Log.e(LOG_TAG, " E-As "+eventsAndaddress+"  B-Ds "+businessAndDocuments+"  M-Ps "+mediaAndPhotos+" Time Zone "+Time_Zone);
											      
											      mEmailModel.setEmail_Date(date);
												  mEmailModel.setEmail_id(email_id);
												  mEmailModel.setEmail_Time(time);
												  mEmailModel.setEmail_Sender(from);
												  mEmailModel.setSubject(subject);
												  mEmailModel.setFetched_id(fetchedLastUid);
												  mEmailModel.setStatus(msg_status);
												  mEmailModel.setEvents_status(eventsAndaddress);
												  mEmailModel.setBusiness_status(businessAndDocuments);
												  mEmailModel.setMedia_status(mediaAndPhotos);
												  mEmailModel.setGeneral_status(generalAndFamily);
												  mEmailModel.setDate_time(date_Time);
												  mEmailModel.setMessageBody(body);
												  mEmailModel.setAttachmentName(attachment);
												  mEmailModel.setTimezone(email_time_zone);
												  mLatestMails.add(mEmailModel);												  
												  mEmailModel = null;														
												 												  
												  
										}else{
																					
												break;
											}
									}

									
								} // for the messaged for loop
								
								

								Log.e(LOG_TAG, "Email Insertion Started "+" latest uid "+latestuid);

									
								if(!mBreak)
								{
									for(EmailModel email: mLatestMails)
									{

										
										long status = 
											saveMailsInDataBase(mDatabase, email.getEmail_Date(), email.getEmail_id(), email.getEmail_Time(),  email.getEmail_Sender(), email.getSubject(),email.getFetched_id(), email.getStatus(), 
													email.getEvents_status(), email.getBusiness_status(), email.getMedia_status(), email.getGeneral_status(), email.getDate_time(),email.getMessageBody() ,
													email.getAttachmentName(),email.getTimezone());
									
										if (status > 0) 
										{
											editor.putLong(imap_server, latestuid);		                
											editor.commit();
										}
									
									}
								}
								
								long stop_time = System.currentTimeMillis();
							    long diff = stop_time - start_time;
						        long diffSeconds = diff / 1000;
						        long diffMinutes = diff / (60 * 1000);
						        
						        Log.v(LOG_TAG,"downloading time "+diffMinutes+" : "+diffSeconds+" Numbre Of mails downloaded "+mLatestMails.size());
						        mLatestMails.clear();
								latestMessage = null;
								inboxFolder = null;
								mStore.close();
								mStore = null;
								messages = null;
								//if (mDatabase.inTransaction()) 	
								//{
									
								//	mDatabase.setTransactionSuccessful();
									//mDatabase.endTransaction();
									
								//}
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
								//if (mDatabase.inTransaction()) 	
									//mDatabase.endTransaction();
								
							}catch(Exception ex) {
								Log.e("EmailService", "" + ex.toString());
							}
						}
						imapCtr ++;

					} // for the cursor loop				 
					


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
	
	void setLatestMessage(Message message) 
	{
        if(message==null) {
            latestMessage=null;
            return;
        }
        
        try {
            if(message.getContentType().startsWith("text/plain") ) {
                latestMessage=new RenderablePlainText(message);
            } else {
                latestMessage=new RenderableMessage(message);
            }
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	 	 
    public Renderable getLatestMessage() {
        return latestMessage;
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
	

	private long saveMailsInDataBase(SQLiteDatabase _mDatabase, String date, String email, String time, String from, String subject,long fetchedLastUid, int msg_status, int eventsAndaddress, int businessAndDocuments, int mediaAndPhotos, int generalAndFamily, String date_Time, String MessageBody, String Attachment, String TimeZone)
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
	        values.put("TimeZone", TimeZone);
	        return _mDatabase.insert("ReceivedMails", null, values);
	       
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		finally  
		{
		}
		return -1;
		
	}
	
	
}
