package com.incc.softwareproject.socialngatutor.models;

import io.realm.RealmObject;

/**
 * Created by carlo on 1/26/16.
 */
public class User extends RealmObject {
    private String schoolId;
    private String username;
    private String full_name;

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}
