package com.sojay.demo.article.bean;

import java.io.Serializable;

/**
 * @author zhaoshujie
 * @desc
 * @time 2019/5/23 15:02
 */
public class ImageBean implements Serializable {

    private long id;
    private String url;

    public ImageBean(long id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url == null ? "" : url;
    }
}
