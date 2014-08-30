package com.skeyedex.EmailDownLoader;


class Attachment {
        private String contenttype;
        private String filename;
        private byte[] content;
        private String contentid;
        
        public int mediaAndPhotos = 0;
        public int businessAndDocuments = 0;
        public int eventsAndaddress = 0;
        
    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

}
