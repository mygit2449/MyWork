package com.skeyedex.EmailDownLoader;

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

/**
 *
 * @author Dj
 */
public class RenderableMessage implements Renderable {
    
    private String bodytext;
    ArrayList<Attachment> attachments;
    
    /** Creates a new instance of RenderableMessage */
    public RenderableMessage(Message m) throws MessagingException,IOException {
        attachments=new ArrayList<Attachment>();
        extractPart(m);
    }
    
    private void extractPart(final Part part) throws MessagingException, IOException {
        if(part.getContent() instanceof Multipart) {
            Multipart mp=(Multipart)part.getContent();
            for(int i=0;i<mp.getCount();i++) {
                extractPart(mp.getBodyPart(i));
            }
            return;
        }
        
        if(part.getContentType().startsWith("text/html") || part.getContentType().startsWith("TEXT/HTML")) {
            if(bodytext==null) {
                bodytext=(String)part.getContent();
            } else {
                bodytext=bodytext+"<HR/>"+(String)part.getContent();
            }
            if(part.getFileName() != null){
            	Attachment attachment=new Attachment();
            	attachment.setContenttype(part.getContentType());
            	attachment.setFilename(part.getFileName());
            	filterAttachment(part.getFileName(), attachment);
            	attachments.add(attachment);
            	
            }
        } else if(!part.getContentType().startsWith("text/plain")) {
            /*InputStream in=part.getInputStream();
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
           
            byte[] buffer=new byte[8192];
            int count=0;
            while((count=in.read(buffer))>=0) bos.write(buffer,0,count);
            in.close();
            attachment.setContent(bos.toByteArray());
            */
            if(part.getFileName() != null){
            	Attachment attachment=new Attachment();
            	attachment.setContenttype(part.getContentType());
            	attachment.setFilename(part.getFileName());
            	filterAttachment(part.getFileName(), attachment);
            	attachments.add(attachment);
            	
            }
           
            
        }
    }
    
 
    
    public String getBodytext() {
        return bodytext;
    }
    
    public int getAttachmentCount() {
        if(attachments==null) return 0;
        return attachments.size();
    }
    
    public Attachment getAttachment(int i) {
        return attachments.get(i);
    }
    
    public void filterAttachment(String filename, Attachment attachment)
    {
    	try{
    	// for filtering the file names to respective groups
    	if(filename.length() > 0 )
    	{
	    	int indexPos = filename.indexOf(".");
	    	String fileExtension = "";
	    	if(indexPos>=0)
	    	{	
	    	fileExtension = filename.substring(indexPos + 1, filename.length());
	
	    	attachment.mediaAndPhotos = 0;
			if (    fileExtension.equalsIgnoreCase("jpeg")    ||    fileExtension.equalsIgnoreCase("bmp")  || 
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
				
				attachment.mediaAndPhotos = 1;
			}
			attachment.businessAndDocuments = 0;
			 if (  fileExtension.equalsIgnoreCase("doc")  ||  fileExtension.equalsIgnoreCase("docx")  || 
					fileExtension.equalsIgnoreCase("pdf") 	 || fileExtension.equalsIgnoreCase("xls")    || 
					fileExtension.equalsIgnoreCase("xlsx")   	 || fileExtension.equalsIgnoreCase("pptx")      || 
					fileExtension.equalsIgnoreCase("pps ppt")	 || fileExtension.equalsIgnoreCase("odp") ||
					fileExtension.equalsIgnoreCase("odt") || fileExtension.equalsIgnoreCase("ods")){ 
				 
				 attachment.businessAndDocuments =1;
			 }	
			 attachment.eventsAndaddress = 0;
			if (  fileExtension.equalsIgnoreCase("csv") || fileExtension.equalsIgnoreCase("txt") || fileExtension.equalsIgnoreCase("rtf")
					|| fileExtension.equalsIgnoreCase("ics")) {
					 
				attachment.eventsAndaddress = 1;
			}
	    	}
    	}
    	}catch(Exception ex) {ex.printStackTrace();}
    }
    
}
