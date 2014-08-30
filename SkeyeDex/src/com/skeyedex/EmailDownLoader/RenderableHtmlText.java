package com.skeyedex.EmailDownLoader;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

/**
 *
 * @author Dj
 */
public class RenderableHtmlText implements Renderable {
    
    String bodytext;
    
    /** Creates a new instance of RenderablePlainText */
    public RenderableHtmlText(Message message) throws MessagingException, IOException {
        bodytext=(String)message.getContent();
    }
    
    public Attachment getAttachment(int i) {
        return null;
    }
    
    public int getAttachmentCount() {
        return 0;
    }
    
    public String getBodytext() {
        return "<HR/>"+ bodytext;
    }
    
    
}
