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

package com.incc.softwareproject.socialngatutor.mentions;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.linkedin.android.spyglass.mentions.Mentionable;
import com.linkedin.android.spyglass.tokenization.QueryToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple class to get suggestions from a JSONArray (represented as a file on disk), which can then
 * be mentioned by the user by tapping on the suggestion.
 */
public  abstract class MentionsLoader<T extends Mentionable> {

    protected T[] mData;
    private static final String TAG = MentionsLoader.class.getSimpleName();

    public MentionsLoader(final String res) {
        new LoadJSONArray(res).execute();
    }

    public abstract T[] loadData(JSONArray arr);

    // Returns a subset
    public List<T> getSuggestions(QueryToken queryToken) {
        String prefix = queryToken.getKeywords().toLowerCase();
        List<T> suggestions = new ArrayList<>();
        if (mData != null) {
            for (T suggestion : mData) {
                String name = suggestion.getSuggestiblePrimaryText().toLowerCase();
                if (name.startsWith(prefix)) {
                    suggestions.add(suggestion);
                }
            }
        }
        return suggestions;
    }

    // Loads data from JSONArray file, defined in the raw resources folder
    private class LoadJSONArray extends AsyncTask<Void, Void, JSONArray> {

        private final String rawjson;

        public LoadJSONArray(String rawjson) {
           this.rawjson = rawjson;
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            JSONArray arr = null;
            try {
                JSONObject jo = new JSONObject(rawjson);
                arr = jo.getJSONArray("User");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception while reading JSON", e);


            }
            return arr;
        }

        @Override
        protected void onPostExecute(JSONArray arr) {
            super.onPostExecute(arr);
            mData = loadData(arr);
        }
    }
}
