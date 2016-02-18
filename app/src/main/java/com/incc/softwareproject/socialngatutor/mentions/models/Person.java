/*
* Copyright 2015 LinkedIn Corp. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/

package com.incc.softwareproject.socialngatutor.mentions.models;

import android.content.res.Resources;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.util.Log;


import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.mentions.MentionsLoader;
import com.linkedin.android.spyglass.mentions.Mentionable;
import com.linkedin.android.spyglass.tokenization.QueryToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Model representing a person.
 */
public class Person implements Mentionable {

    private final String mUserName;
    private final String mFullName;
    private final String mPictureURL;

    public Person(String userName, String lastName, String pictureURL) {
        mUserName = userName;
        mFullName = lastName;
        mPictureURL = pictureURL;
    }

    public String getUserName() {
        return mUserName;
    }
    public String getFullName() {
        return mFullName;
    }

    public String getPictureURL() {
        return mPictureURL;
    }

    // --------------------------------------------------
    // Mentionable/Suggestible Implementation
    // --------------------------------------------------

    @NonNull
    @Override
    public String getTextForDisplayMode(MentionDisplayMode mode) {
        switch (mode) {
            case FULL:
                return getFullName();
            case PARTIAL:
                String[] words = getFullName().split(" ");
                return (words.length > 1) ? words[0] : "";
            case NONE:
            default:
                return "";
        }
    }

    @Override
    public MentionDeleteStyle getDeleteStyle() {
        // People support partial deletion
        // i.e. "John Doe" -> DEL -> "John" -> DEL -> ""
        return MentionDeleteStyle.PARTIAL_NAME_DELETE;
    }

    @Override
    public int getSuggestibleId() {
        return getFullName().hashCode();
    }

    @Override
    public String getSuggestiblePrimaryText() {
        return getFullName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUserName);
        dest.writeString(mFullName);
        dest.writeString(mPictureURL);
    }

    public Person(Parcel in) {
        mUserName = in.readString();
        mFullName = in.readString();
        mPictureURL = in.readString();
    }

    public static final Creator<Person> CREATOR
            = new Creator<Person>() {
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    // --------------------------------------------------
    // PersonLoader Class (loads people from JSON file)
    // --------------------------------------------------

    public static class PersonLoader extends MentionsLoader<Person> {
        private static final String TAG = PersonLoader.class.getSimpleName();

        public PersonLoader(String rawjson) {
            super(rawjson);
        }

        @Override
        public Person[] loadData(JSONArray arr) {
            Person[] data = new Person[arr.length()];
            try {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String username = obj.getString("username");
                    String fullname = obj.getString("full_name");
                    String url = obj.getString("pic_url");
                    data[i] = new Person(username, fullname , url);
                }
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception while parsing person JSONArray", e);
            }

            return data;
        }

        // Modified to return suggestions based on both first and last name
        @Override
        public List<Person> getSuggestions(QueryToken queryToken) {
            String prefix = queryToken.getKeywords().toLowerCase();
            List<Person> suggestions = new ArrayList<>();
            if (mData != null) {
                for (Person suggestion : mData) {
                    String firstName = suggestion.getUserName().toLowerCase();
                    String lastName = suggestion.getFullName().toLowerCase();
                    if (firstName.startsWith(prefix) || lastName.startsWith(prefix)) {
                        suggestions.add(suggestion);
                    }
                }
            }
            return suggestions;
        }
    }
}
