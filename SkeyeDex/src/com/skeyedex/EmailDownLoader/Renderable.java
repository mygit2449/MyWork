/*
 * Renderable.java
 *
 * Created on 10 November 2005, 10:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.skeyedex.EmailDownLoader;

/**
 *
 * @author Dj
 */
public interface Renderable {
    Attachment getAttachment(int i);

    int getAttachmentCount();

    String getBodytext();

    
}
