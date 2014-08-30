package com.skeyedex.GroupList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.skeyedex.R;
import com.skeyedex.Model.EmailModel;
import com.skeyedex.Model.SMSmodel;

public class GeneralAndFamilyAdapter extends BaseAdapter{


	private static final String LOG_TAG = GeneralAndFamilyAdapter.class.getSimpleName();
	
	private static String MONTH[] = { "Jan", "Feb", "Mar", "April", "May","June", "July", "Aug", "Sept", "Oct", "Nov", "Dec" };
	private HashMap<String, Object> mSelectedEmail, mSelectedSms; 
	private String selectType = "manual";
	
	Object obj;
	Context context;
	ArrayList arrGeneralMessages;
	ArrayList<Object> newEmail = new ArrayList<Object>();

	public GeneralAndFamilyAdapter(Context context, ArrayList _allMessages, HashMap<String, Object> checkEmailMap, HashMap<String, Object> checkSmsMap)
	{
		
		this.context=context;
		this.arrGeneralMessages = _allMessages;

		mSelectedEmail = checkEmailMap;
		mSelectedSms = checkSmsMap;
		
	}
	
	 
	  
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrGeneralMessages.size();
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
			String email_time = "";
		    String current_date="";
			String allsubject = "";
			String msg_sender = "";
	        int serial_number = 0;
	        
	        DateFormat stringFormatter = new SimpleDateFormat("MMM dd");
	        Date currentDate = new Date(System.currentTimeMillis());
	        
			obj= arrGeneralMessages.get(position);
			
				if (convertView == null)
				{
					LayoutInflater inflater = LayoutInflater.from(context);
					convertView = inflater.inflate(R.layout.category_listview, null);
				}
		
				
					email_relative = (RelativeLayout)convertView.findViewById(R.id.group_listview_Rel);
					
					TextView subject = (TextView)convertView.findViewById(R.id.emaillistrow_txt_partofemail);
					TextView sender = (TextView)convertView.findViewById(R.id.emaillistrow_txt_sender);
					TextView date = (TextView)convertView.findViewById(R.id.emaillistrow_txt_datetime);
					TextView time = (TextView)convertView.findViewById(R.id.emaillistrow_txt_datetime);
					TextView serialnumber = (TextView) convertView.findViewById(R.id.textlistrow_txt_serialnumber);
					
					ImageView sms_image = (ImageView)convertView.findViewById(R.id.emaillistrow_icon_txt);
					ImageView email_image = (ImageView) convertView.findViewById(R.id.emaillistrow_icon_em);
					ImageView icon_image = (ImageView) convertView.findViewById(R.id.emaillistrow_icon_indicator);
					ImageView sms_icon_image = (ImageView) convertView.findViewById(R.id.sms_icon_indicator);
					
					email_relative.setBackgroundResource(R.drawable.gl_white_bar);
					
					CheckBox email_check = (CheckBox)convertView.findViewById(R.id.emaillistrow_chk_check);
					email_check.setTag(position);
					
					if(obj instanceof EmailModel)
					{
								
							 Log.v(LOG_TAG, " The object is of type Email Model");
							 EmailModel emailModel = (EmailModel)arrGeneralMessages.get(position);
							 
							 allsubject= emailModel.getSubject();
							 msg_sender = emailModel.getEmail_Sender();
							 serial_number = emailModel.getSerial_no();
							 
							 icon_image.setVisibility(View.VISIBLE);
							 sms_icon_image.setVisibility(View.INVISIBLE);
							 icon_image.setImageResource(((EmailModel) arrGeneralMessages.get(position)).getImage_resourceId());
							 SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								 
							 //email check box selection
							 if (((EmailModel) arrGeneralMessages.get(position)).isSelected())
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
								Log.i("Em Date",""+email_date);
								
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			
																
							if (emailModel.getStatus()==0) 
							{
									
									sender.setTypeface(Typeface.DEFAULT_BOLD);
									subject.setTypeface(Typeface.DEFAULT_BOLD);
									date.setTypeface(Typeface.DEFAULT_BOLD);
							
							}else 
								{
								
									sender.setTypeface(Typeface.DEFAULT);
									subject.setTypeface(Typeface.DEFAULT);
									date.setTypeface(Typeface.DEFAULT);
								
								}
							
								email_image.setVisibility(View.VISIBLE);
								sms_image.setVisibility(View.INVISIBLE);
								serialnumber.setVisibility(View.VISIBLE);
								sms_image.setVisibility(View.INVISIBLE);
								serialnumber.setText(" "+serial_number);
								
					}else
						{
				
							 SMSmodel smsModel = (SMSmodel) arrGeneralMessages.get(position);
							 Log.v(LOG_TAG, " The object is of type Sms model");
						
							 allsubject= smsModel.getBody();
							 msg_sender = smsModel.getSender();
					
							 sms_icon_image.setVisibility(View.VISIBLE);
							 sms_icon_image.setImageResource(smsModel.getImage_resourceId());
							 sms_image.setVisibility(View.VISIBLE);
							 email_image.setVisibility(View.INVISIBLE);
							 serialnumber.setVisibility(View.INVISIBLE);
							 
							 // sms check box selection
							 if (((SMSmodel) arrGeneralMessages.get(position)).isSelected())
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
					sender.setText(msg_sender);
					
					if (email_date.equalsIgnoreCase(current_date))
						date.setText("Today");
				   else
						date.setText(""+email_date);		
			
					// check box selection for emails and sms's
					email_check.setOnCheckedChangeListener(new OnCheckedChangeListener()
					{
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
						{
							obj = arrGeneralMessages.get(position);
							
							if(selectType == "manual")
							{
								if (obj instanceof EmailModel) 
								{
								
									Log.v(LOG_TAG, " obj type is email");
									((EmailModel) arrGeneralMessages.get((Integer) buttonView.getTag())).setSelected(isChecked);
									Log.e("", "Email "+((EmailModel) arrGeneralMessages.get((Integer) buttonView.getTag())).getEmail_id());
									
									ArrayList<Object> email = (ArrayList<Object>) mSelectedEmail.get(((EmailModel) arrGeneralMessages.get((Integer) buttonView.getTag())).getEmail_id());
									
									if(isChecked) 
									{										
										
										if(email == null )
										{
											
											
												Log.e("LogCat", "email  null");
										
												//ArrayList<Object> newEmail = new ArrayList<Object>();
											
												newEmail.add(arrGeneralMessages.get((Integer) buttonView.getTag()));
											
											    mSelectedEmail.put(((EmailModel) arrGeneralMessages.get((Integer) buttonView.getTag())).getEmail_id() , newEmail);
											
										}else{
													
											Log.e("LogCat", "email  not null"+((EmailModel) arrGeneralMessages.get((Integer) buttonView.getTag())).getEmail_id());
											
											email.add(arrGeneralMessages.get((Integer) buttonView.getTag()));
											
											mSelectedEmail.put(((EmailModel) arrGeneralMessages.get((Integer) buttonView.getTag())).getEmail_id() , email);								
											
										}
										
									}else 
									 {
									
											ArrayList<EmailModel> emails ;
											emails =(ArrayList<EmailModel>) mSelectedEmail.put(((EmailModel) arrGeneralMessages.get((Integer) buttonView.getTag())).getEmail_id() , email);
											
											for(int i = 0 ; i< emails.size(); i++){
												
												Log.e("LogCat", " email some"+emails.get(i).getDate_time());
											}
											
											email.remove(arrGeneralMessages.get((Integer) buttonView.getTag()));
											
											mSelectedEmail.put(((EmailModel) arrGeneralMessages.get((Integer) buttonView.getTag())).getEmail_id() , email);		
											
											
											emails =(ArrayList<EmailModel>) mSelectedEmail.put(((EmailModel) arrGeneralMessages.get((Integer) buttonView.getTag())).getEmail_id() , email);
											
											for(int i = 0 ; i< emails.size(); i++){
												
												Log.e("LogCat", " email some delete"+emails.get(i).getDate_time());
											}
										
									}
									
								}else //sms checking
								  {
									
									Log.v(LOG_TAG, " obj type is SMS");
									((SMSmodel) arrGeneralMessages.get((Integer) buttonView.getTag())).setSelected(isChecked);
									Log.e("", "Sms "+((SMSmodel) arrGeneralMessages.get((Integer) buttonView.getTag())).getSender());
									
									ArrayList<Object> sms = (ArrayList<Object>) mSelectedSms.get(((SMSmodel) arrGeneralMessages.get((Integer) buttonView.getTag())).getSender());  // saving sms based on sender
									
									if(isChecked)
									{										
										
										if(sms == null )
										{
											
												Log.e("LogCat", "sms  null");
												ArrayList<Object> newSms = new ArrayList<Object>();
												newSms.add(arrGeneralMessages.get((Integer) buttonView.getTag()));
												mSelectedSms.put(((SMSmodel) arrGeneralMessages.get((Integer) buttonView.getTag())).getSender() , newSms);
											
										}else
										{
													
											Log.e("LogCat", "sms  not null"+((SMSmodel) arrGeneralMessages.get((Integer) buttonView.getTag())).getSender());
											sms.add(arrGeneralMessages.get((Integer) buttonView.getTag()));
											mSelectedSms.put(((SMSmodel) arrGeneralMessages.get((Integer) buttonView.getTag())).getSender(), sms);								
										}
										
									}else
									 {
										
											ArrayList<SMSmodel> smsArr ;
											smsArr =(ArrayList<SMSmodel>) mSelectedSms.put(((SMSmodel) arrGeneralMessages.get((Integer) buttonView.getTag())).getSender() , sms);
											
											for(int i = 0 ; i< smsArr.size(); i++){
												
												Log.e("LogCat", " sms some"+smsArr.get(i).getBody());
											}
											
											sms.remove(arrGeneralMessages.get((Integer) buttonView.getTag()));
											
											mSelectedSms.put(((SMSmodel) arrGeneralMessages.get((Integer) buttonView.getTag())).getSender() , sms);		
											
											
											smsArr =(ArrayList<SMSmodel>) mSelectedSms.put(((SMSmodel) arrGeneralMessages.get((Integer) buttonView.getTag())).getSender(), sms);
											
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
