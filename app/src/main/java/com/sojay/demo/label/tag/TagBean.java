package com.sojay.demo.label.tag;

import java.io.Serializable;

/**
 * @author zhaoshujie
 * @desc
 * @time 2019/5/25 14:07
 */
public class TagBean implements Serializable {

    private String id;
    private String tag;
    private boolean isChecked;

    public TagBean(String tag) {
        this.tag = tag;
    }

    public TagBean(String id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public String getTag() {
        return tag == null ? "" : tag;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
