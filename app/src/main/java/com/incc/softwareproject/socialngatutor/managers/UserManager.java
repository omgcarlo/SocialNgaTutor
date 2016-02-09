package com.incc.softwareproject.socialngatutor.managers;

import com.incc.softwareproject.socialngatutor.models.Post;

import io.realm.Realm;

/**
 * Created by carlo on 1/26/16.
 */
public class UserManager {
    public static final String TAG = "UserManager";
    private Realm realm;
    private int getNextId () {
        try {
            return realm.where(Post.class).max("id").intValue() + 1;
        } catch (Exception e) {
            return 1;
        }
    }
}
