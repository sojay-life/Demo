package com.zhihu.matisse.internal.entity;

public class SaveItem {
    private long id;
    private String mimeType;
    private String uri;
    private String path;
    private long size;
    private long duration;

    public SaveItem(long id,String mimeType,String uri,long size,long duration){
        this.id=id;
        this.mimeType=mimeType;
        this.uri=uri;
        this.size=size;
        this.duration=duration;

    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
