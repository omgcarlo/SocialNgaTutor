package com.incc.softwareproject.socialngatutor.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class File extends RealmObject {

    @PrimaryKey
    private int id;
    private String url;
    private String type;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
