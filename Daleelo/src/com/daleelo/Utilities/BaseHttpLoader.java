package com.daleelo.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;

import android.util.Log;

public abstract class BaseHttpLoader {
	

    final URL feedUrl;

    protected BaseHttpLoader(String feedUrl){
        try {
            this.feedUrl = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    protected InputStream getInputStream() {
        try {
            return feedUrl.openConnection().getInputStream();
        } catch (IOException e) {
        	Log.e("TAG", "internet connection lose");
            throw new RuntimeException(e);
        }
    }

}
