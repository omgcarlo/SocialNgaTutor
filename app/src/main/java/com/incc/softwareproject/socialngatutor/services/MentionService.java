package com.incc.softwareproject.socialngatutor.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;


public class MentionService extends IntentService {

    public static final String TAG = "MentionService";
    public static final String ACTION = MentionService.class.getCanonicalName();
    public MentionService() {
        super("MentionService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

    }


}
