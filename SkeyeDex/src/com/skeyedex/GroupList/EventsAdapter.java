package com.skeyedex.GroupList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skeyedex.R;
import com.skeyedex.Model.EmailModel;
import com.skeyedex.Model.SMSmodel;

public class EventsAdapter extends BaseAdapter{

	
	private static final String LOG_TAG = EventsAdapter.class.getSimpleName();
	
	private static String MONTH[] = { "Jan", "Feb", "Mar", "April", "May","June", "July", "Aug", "Sept", "Oct", "Nov", "Dec" };

	private HashMap<String, Object> mSelectedEmail, mSelectedSms;
	private String selectType = "manual";
	Object obj;
	
	Context context;
	ArrayList arrEventsMessages;
	ArrayList<Object> newEmail = new ArrayList<Object>();
	
	public EventsAdapter(Context context, ArrayList _allMessages, HashMap<String, Object> checkEmailMap, HashMap<String, Object> checkSmsMap)
	{
		
		this.context=context;
		this.arrEventsMessages = _allMessages;
		
		mSelectedEmail = checkEmailMap;
		mSelectedSms = checkSmsMap;
		
	}
	
	 
	  
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrEventsMessages.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		
		
			RelativeLayout email_relative;
			Date emailDate = null;
			String email_date = "";
		    String current_date="";
			String allsubject = "";
			String msg_sender = "";
			int serial_Num = 0;
			
			DateFormat stringFormatter = new SimpleDateFormat("MMM dd");
	        obj= arrEventsMessages.get(position);
			Date currentDate = new Date(System.currentTimeMillis());
		    
				if (convertView == null)
				{
					
					LayoutInflater inflater = LayoutInflater.from(context);
					convertView = inflater.inflate(R.layout.category_listview, null);
					
				}
			
					email_relative = (RelativeLayout)convertView.findViewById(R.id.group_listview_Rel);
					TextView serialnumber = (TextView) convertView.findViewById(R.id.textlistrow_txt_serialnumber);
					TextView subject = (TextView)convertView.findViewById(R.id.emaillistrow_txt_partofemail);
					TextView sender = (TextView)convertView.findViewById(R.id.emaillistrow_txt_sender);
					TextView date = (TextView)convertView.findViewById(R.id.emaillistrow_txt_datetime);
				
					ImageView sms_image = (ImageView)convertView.findViewById(R.id.emaillistrow_icon_txt);
					ImageView email_image = (ImageView) convertView.findViewById(R.id.emaillistrow_icon_em);
					ImageView icon_image = (ImageView) convertView.findViewById(R.id.emaillistrow_icon_indicator);
					ImageView sms_icon_image = (ImageView) convertView.findViewById(R.id.sms_icon_indicator);
					
					CheckBox email_check = (CheckBox)convertView.findViewById(R.id.emaillistrow_chk_check);
					email_check.setTag(position);
					
					email_relative.setBackgroundResource(R.drawable.gl_yellow_bar);
					
					
					if(obj instanceof EmailModel)
					{
								
							 Log.v(LOG_TAG, " The object is of type Email Model");
							 EmailModel emailModel = (EmailModel)arrEventsMessages.get(position);
							 allsubject= emailModel.getSubject();
							 msg_sender = emailModel.getEmail_Sender();
							 serial_Num = emailModel.getSerial_no();
							 
							 icon_image.setVisibility(View.VISIBLE);
							 sms_icon_image.setVisibility(View.INVISIBLE);
							 icon_image.setImageResource(((EmailModel) arrEventsMessages.get(position)).getImage_resourceId());
							 SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							
							 // emails check box selection
							 if (((EmailModel) arrEventsMessages.get(position)).isSelected())
								{
									
									selectType =  "auto";
									Log.e("LogCat Email", "select  true");
									email_check.setChecked(true);
									
								}else 
								{
									selectType =  "auto";
									Log.e("LogCat Email", "select  false");
									email_check.setChecked(false);
								}
								
								selectType = "manual";
								 
							try
							{
								
								emailDate = formatter.parse(emailModel.getDate_time());
								email_date = stringFormatter.format(emailDate);
								current_date = stringFormatter.format(currentDate);
								
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			
								
							if (emailModel.getStatus()==0) 
							{
									
									sender.setTypeface(Typeface.DEFAULT_BOLD);
									subject.setTypeface(Typeface.DEFAULT_BOLD);
									date.setTypeface(Typeface.DEFAULT_BOLD);
									
							}else {
								
									sender.setTypeface(Typeface.DEFAULT);
									subject.setTypeface(Typeface.DEFAULT);
									date.setTypeface(Typeface.DEFAULT);
									
								}
							
							 email_image.setVisibility(View.VISIBLE);
							 serialnumber.setVisibility(View.VISIBLE);
							 sms_image.setVisibility(View.INVISIBLE);
							 serialnumber.setText(" "+serial_Num);
			
							 
					}else
						{
						
							 sms_icon_image.setVisibility(View.VISIBLE);
							 SMSmodel smsModel = (SMSmodel) arrEventsMessages.get(position);
							 sms_icon_image.setImageResource(smsModel.getImage_resourceId());
							 allsubject= smsModel.getBody();
							 msg_sender = smsModel.getSender();
							 
							 Log.v(LOG_TAG, " The object is of type Sms model");
							 sms_image.setVisibility(View.VISIBLE);
							 email_image.setVisibility(View.INVISIBLE);
							 serialnumber.setVisibility(View.INVISIBLE);
							 
							 //sms check box selection
							 if (((SMSmodel) arrEventsMessages.get(position)).isSelected())
								{
									
									selectType =  "auto";
									Log.e("LogCat SMS", "select  true");
									email_check.setChecked(true);
									
								}else 
								{
									
									selectType =  "auto";
									Log.e("LogCat SMS", "select  false");
									email_check.setChecked(false);
									
								}
								
								selectType = "manual";
								
								
							 if (smsModel.getStatus()==0) 
								{
										
										sender.setTypeface(Typeface.DEFAULT_BOLD);
										subject.setTypeface(Typeface.DEFAULT_BOLD);
										date.setTypeface(Typeface.DEFAULT_BOLD);
										
								}else {
									
										sender.setTypeface(Typeface.DEFAULT);
										subject.setTypeface(Typeface.DEFAULT);
										date.setTypeface(Typeface.DEFAULT);
										
									}
						}
				
				subject.setText(allsubject);
			
				if (email_date.equalsIgnoreCase(current_date))
					date.setText("Today");
			   else
					date.setText(""+email_date);
				
					sender.setText(msg_sender);
				
				
				// check box selection for emails and sms's
				email_check.setOnCheckedChangeListener(new OnCheckedChangeListener()
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
					{
						obj = arrEventsMessages.get(position);
						
						if(selectType == "manual")
						{
							if (obj instanceof EmailModel) 
							{
							
								Log.v(LOG_TAG, " obj type is email");
								((EmailModel) arrEventsMessages.get((Integer) buttonView.getTag())).setSelected(isChecked);
								Log.e("", "Email "+((EmailModel) arrEventsMessages.get((Integer) buttonView.getTag())).getEmail_id());
								
								ArrayList<Object> email = (ArrayList<Object>) mSelectedEmail.get(((EmailModel) arrEventsMessages.get((Integer) buttonView.getTag())).getEmail_id());
								
								if(isChecked) 
								{										
									
									if(email == null )
									{
										
										
											Log.e("LogCat", "email  null");
									
											
										
											newEmail.add(arrEventsMessages.get((Integer) buttonView.getTag()));
										
										    mSelectedEmail.put(((EmailModel) arrEventsMessages.get((Integer) buttonView.getTag())).getEmail_id() , newEmail);
										
									}else{
												
										Log.e("LogCat", "email  not null"+((EmailModel) arrEventsMessages.get((Integer) buttonView.getTag())).getEmail_id());
										
										email.add(arrEventsMessages.get((Integer) buttonView.getTag()));
										
										mSelectedEmail.put(((EmailModel) arrEventsMessages.get((Integer) buttonView.getTag())).getEmail_id() , email);								
										
									}
									
								}else 
								{
								
										ArrayList<EmailModel> emails ;
										emails =(ArrayList<EmailModel>) mSelectedEmail.put(((EmailModel) arrEventsMessages.get((Integer) buttonView.getTag())).getEmail_id() , email);
										
										for(int i = 0 ; i< emails.size(); i++){
											
											Log.e("LogCat", " email some"+emails.get(i).getDate_time());
										}
										
										email.remove(arrEventsMessages.get((Integer) buttonView.getTag()));
										
										mSelectedEmail.put(((EmailModel) arrEventsMessages.get((Integer) buttonView.getTag())).getEmail_id() , email);		
										
										
										emails =(ArrayList<EmailModel>) mSelectedEmail.put(((EmailModel) arrEventsMessages.get((Integer) buttonView.getTag())).getEmail_id() , email);
										
										for(int i = 0 ; i< emails.size(); i++){
											
											Log.e("LogCat", " email some delete"+emails.get(i).getDate_time());
										}
									
								}
								
							}else //sms checking
							  {
								
								Log.v(LOG_TAG, " obj type is SMS");
								((SMSmodel) arrEventsMessages.get((Integer) buttonView.getTag())).setSelected(isChecked);
								Log.e("", "Sms "+((SMSmodel) arrEventsMessages.get((Integer) buttonView.getTag())).getSender());
								
								ArrayList<Object> sms = (ArrayList<Object>) mSelectedSms.get(((SMSmodel) arrEventsMessages.get((Integer) buttonView.getTag())).getSender());  // saving sms based on sender
								
								if(isChecked)
								{										
									
									if(sms == null )
									{
										
											Log.e("LogCat", "sms  null");
											ArrayList<Object> newSms = new ArrayList<Object>();
											newSms.add(arrEventsMessages.get((Integer) buttonView.getTag()));
											mSelectedSms.put(((SMSmodel) arrEventsMessages.get((Integer) buttonView.getTag())).getSender() , newSms);
										
									}else
									{
												
										Log.e("LogCat", "sms  not null"+((SMSmodel) arrEventsMessages.get((Integer) buttonView.getTag())).getSender());
										sms.add(arrEventsMessages.get((Integer) buttonView.getTag()));
										mSelectedSms.put(((SMSmodel) arrEventsMessages.get((Integer) buttonView.getTag())).getSender(), sms);								
									}
									
								}else
								 {
									
										ArrayList<SMSmodel> smsArr ;
										smsArr = (ArrayList<SMSmodel>) mSelectedSms.put(((SMSmodel) arrEventsMessages.get((Integer) buttonView.getTag())).getSender() , sms);
										
										for(int i = 0 ; i< smsArr.size(); i++){
											
											Log.e("LogCat", " sms some"+smsArr.get(i).getBody());
										}
										
										sms.remove(arrEventsMessages.get((Integer) buttonView.getTag()));
										
										mSelectedSms.put(((SMSmodel) arrEventsMessages.get((Integer) buttonView.getTag())).getSender() , sms);		
										
										
										smsArr = (ArrayList<SMSmodel>) mSelectedSms.put(((SMSmodel) arrEventsMessages.get((Integer) buttonView.getTag())).getSender(), sms);
										
										for(int i = 0 ; i< smsArr.size(); i++){
											
											Log.e("LogCat", " sms some delete 1"+smsArr.get(i).getBody());
										}
										
								}
							 }
					     }
						selectType = "manual";
					}
				});
			return convertView;

	}

}
