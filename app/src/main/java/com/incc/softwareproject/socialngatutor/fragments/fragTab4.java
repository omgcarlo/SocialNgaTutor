package com.incc.softwareproject.socialngatutor.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.Server.Activity;
import com.incc.softwareproject.socialngatutor.adapters.ActivityRecyclerAdapter;
import com.incc.softwareproject.socialngatutor.adapters.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlo on 21/12/2015.
 */
public class fragTab4 extends Fragment {
    List<String> activity = new ArrayList<>();

    List<String> from_userId = new ArrayList<>();
    List<String> from_username = new ArrayList<>();
    List<String> from_fullname = new ArrayList<>();
    List<String> description = new ArrayList<>();
    List<String> postId = new ArrayList<>();
    List<String> datetime = new ArrayList<>();

    private RecyclerView recyclerView;

    private SharedPreferences sData;
    private String schoolId;
    public static fragTab4 createInstance() {
        fragTab4 frag_tab4 = new fragTab4();
        Bundle bundle = new Bundle();
        frag_tab4.setArguments(bundle);
        return frag_tab4;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.tab_1, container, false);
        // Inflate the layout for this fragment
        sData = this.getActivity().getSharedPreferences("ShareData", Context.MODE_PRIVATE);
        schoolId= sData.getString("SchoolId", "wala");
        new initActivity()
                .execute(schoolId);
        return recyclerView;
    }

    public void clear() {
        from_username.clear();
        from_fullname.clear();
        postId.clear();
        from_userId.clear();
        datetime.clear();
        activity.clear();
        description.clear();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ActivityRecyclerAdapter recyclerAdapter = new ActivityRecyclerAdapter(activity,from_fullname, from_username, postId, from_userId,description, datetime);
        recyclerView.setAdapter(recyclerAdapter);
    }

    protected void convertJSON(String result) {

        try {
            clear();
            JSONObject reader = new JSONObject(result);
            JSONArray data = reader.getJSONArray("Activity");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonobject = data.getJSONObject(i);
                activity.add(jsonobject.getString("Activity"));

                from_username.add(jsonobject.getString("from_username"));
                from_fullname.add(jsonobject.getString("from_full_name"));
                from_userId.add(jsonobject.getString("from_userId"));

                //  IF THE ACTIVITY IS COMMENT THEN POST ID SHOULD BE ADDED
                if (jsonobject.getString("Activity").equals("comment")) {
                    postId.add(jsonobject.getString("postId"));
                } else {
                    postId.add("");
                }

                description.add(jsonobject.getString("description"));
                datetime.add(jsonobject.getString("datetime"));
            }
            setupRecyclerView(recyclerView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class initActivity extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Activity svConn = new Activity();
            return svConn.getActivities(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("")) {
                convertJSON(result);
            } else {
                Log.d("TAB1", "error");
            }
        }
    }

}