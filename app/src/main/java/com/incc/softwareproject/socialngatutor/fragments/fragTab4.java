package com.incc.softwareproject.socialngatutor.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
public class fragTab4 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    List<String> activity = new ArrayList<>();

    List<String> from_userId = new ArrayList<>();
    List<String> from_username = new ArrayList<>();
    List<String> from_fullname = new ArrayList<>();
    List<String> description = new ArrayList<>();
    List<String> postId = new ArrayList<>();
    List<String> datetime = new ArrayList<>();
    List<String> pic_url = new ArrayList<>();
    List<String> activityId = new ArrayList<>();
    private RecyclerView recyclerView;

    private SharedPreferences sData;
    private String schoolId;
    SwipeRefreshLayout srl;
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
        //recyclerView = (RecyclerView) inflater.inflate(R.layout.tab_1, container, false);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_4, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.t4_a_recyclerView);

        sData = this.getActivity().getSharedPreferences("ShareData", Context.MODE_PRIVATE);
        schoolId= sData.getString("SchoolId", "wala");
        new initActivity()
                .execute(schoolId);
        srl = (SwipeRefreshLayout) view.findViewById(R.id.t4_swipe_refresh_layout);
        srl.setOnRefreshListener(this);
        return view;
    }

    public void clear() {
        from_username.clear();
        from_fullname.clear();
        postId.clear();
        from_userId.clear();
        datetime.clear();
        activity.clear();
        description.clear();
        pic_url.clear();
        activityId.clear();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ActivityRecyclerAdapter recyclerAdapter = new ActivityRecyclerAdapter(activity,from_fullname,
                                                            from_username, postId, from_userId,description,
                                                            datetime,pic_url,activityId);
        recyclerView.setAdapter(recyclerAdapter);
    }

    protected void convertJSON(String result) {

        try {
            clear();
            //Log.e("tab4",result);
            JSONObject reader = new JSONObject(result);
            JSONArray data = reader.getJSONArray("Activity");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonobject = data.getJSONObject(i);
                activity.add(jsonobject.getString("Activity"));
                activityId.add(jsonobject.getString("activityId"));

                from_username.add(jsonobject.getString("from_username"));
                from_fullname.add(jsonobject.getString("from_full_name"));
                from_userId.add(jsonobject.getString("from_userId"));

                //  IF THE ACTIVITY IS COMMENT THEN POST ID SHOULD BE ADDED
                if (jsonobject.getString("Activity").equals("comment")) {
                    postId.add(jsonobject.getString("postId"));
                }
                else if (jsonobject.getString("Activity").equals("mention")) {
                    postId.add(jsonobject.getString("postId"));
                }
                else {
                    postId.add("");
                }

                description.add(jsonobject.getString("description"));
                datetime.add(jsonobject.getString("datetime"));
                pic_url.add(jsonobject.getString("pic_url"));

            }
            srl.setRefreshing(false);
            setupRecyclerView(recyclerView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        new initActivity()
                .execute(schoolId);

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
