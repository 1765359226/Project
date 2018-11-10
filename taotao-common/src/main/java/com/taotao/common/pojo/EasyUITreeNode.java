package com.taotao.common.pojo;

import java.io.Serializable;

public class EasyUITreeNode implements Serializable {
//    id
    private long id;
//    节点名
    private String text;
//    是否还有节点
    private String state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
