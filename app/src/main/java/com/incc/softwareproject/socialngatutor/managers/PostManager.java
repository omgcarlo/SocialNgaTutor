package com.incc.softwareproject.socialngatutor.managers;

import android.content.Context;
import android.util.Log;

import com.incc.softwareproject.socialngatutor.models.Post;

import java.util.List;

import io.realm.Realm;

public final class PostManager {

    private static final String TAG = "PostManager";

    private static PostManager mSoleInstance;

    private Realm realm;

    public static PostManager getInstance (Context context) {
        if (mSoleInstance == null) {
            mSoleInstance = new PostManager(context);
        }

        return mSoleInstance;
    }

    private PostManager (Context context) {
        realm = Realm.getInstance(context);
    }

    public Post get (int postId) {
        return realm.where(Post.class).equalTo("id", postId).findFirst();
    }

    public List<Post> getAll() {
        return realm.where(Post.class).findAll();
    }

    public Post create (String description, int ownerId, String tags) {
        Post post = new Post();
        post.setId(getNextId());
        post.setDescription(description);
        post.setOwnerId(ownerId);
        post.setTags(tags);

        realm.beginTransaction();
        post = realm.copyToRealm(post);
        realm.commitTransaction();
        
        return post;
    }

    public boolean update (int postId, String description, int ownerId, String tags) {
        Post post = realm.where(Post.class).equalTo("id", postId).findFirst();

        if (post != null) {
            realm.beginTransaction();
            post.setDescription(description);
            post.setOwnerId(ownerId);
            post.setTags(tags);
            realm.commitTransaction();
            return true;
        } else {
            Log.e(TAG, "Error updating post#" + postId + " --> not found");
            return false;
        }
    }

    public boolean delete (int postId) {
        Post post = realm.where(Post.class).equalTo("id", postId).findFirst();

        if (post != null) {
            realm.beginTransaction();
            post.removeFromRealm();
            realm.commitTransaction();
            return true;
        } else {
            Log.e(TAG, "Error deleting post#" + postId + " --> not found");
            return false;
        }
    }

    private int getNextId () {
        try {
            return realm.where(Post.class).max("id").intValue() + 1;
        } catch (Exception e) {
            return 1;
        }
    }
}
