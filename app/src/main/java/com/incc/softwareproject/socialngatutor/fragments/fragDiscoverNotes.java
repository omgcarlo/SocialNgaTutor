package com.incc.softwareproject.socialngatutor.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.Server.Events;
import com.incc.softwareproject.socialngatutor.Server.Search;
import com.incc.softwareproject.socialngatutor.adapters.EventRecyclerAdapter;
import com.incc.softwareproject.socialngatutor.adapters.NoteRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by carlo on 21/12/2015.
 */
public class fragDiscoverNotes extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private List<String> fullname = new ArrayList<>();
    private List<String> userId = new ArrayList<>();
    private List<String> note_description = new ArrayList<>();
    private List<String> datetime = new ArrayList<>();
    private List<String> file_url = new ArrayList<>();
    private List<String> file_description = new ArrayList<>();

    private RecyclerView recyclerView;

    SharedPreferences sData;
    private String SCHOOL_ID;
    SwipeRefreshLayout srl;
    public static fragDiscoverNotes createInstance() {
        fragDiscoverNotes frag = new fragDiscoverNotes();
        Bundle bundle = new Bundle();
        frag.setArguments(bundle);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_discover_notes, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.tab_discover_notes_recyclerView);
        srl = (SwipeRefreshLayout) view.findViewById(R.id.tab_discover_notes_swipe_refresh_layout);
        srl.setOnRefreshListener(this);
        sData = this.getActivity().getSharedPreferences("ShareData", Context.MODE_PRIVATE);
        SCHOOL_ID = sData.getString("SchoolId", "wala");
        new displayNotes().execute("discoverNotes","",SCHOOL_ID,getActivity().getIntent().getStringExtra("Code"));
        return view;
    }

    private void setupRecyclerView(RecyclerView nrecycler) {
        nrecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        NoteRecyclerAdapter nRecyclerAdapter = new NoteRecyclerAdapter(fullname, userId, note_description, file_description, datetime, file_url);
        nrecycler.setAdapter(nRecyclerAdapter);
    }

    private void clearData() {
        fullname.clear();
        userId.clear();
        note_description.clear();
        datetime.clear();
        file_url.clear();
        file_description.clear();
    }

    private void convertJSON(String result) {
        try {
            clearData();
            JSONObject reader = new JSONObject(result);
            JSONArray data = reader.getJSONArray("Discover");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonobject = data.getJSONObject(i);
                fullname.add(jsonobject.getString("full_name"));
                userId.add(jsonobject.getString("ownerId"));
                note_description.add(jsonobject.getString("note_description"));
                datetime.add(jsonobject.getString("datetime"));
                file_url.add(jsonobject.getString("fileUrl"));
                file_description.add(jsonobject.getString("file_description"));
            }
            srl.setRefreshing(false);
            setupRecyclerView(recyclerView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        clearData();
        new displayNotes().execute("discoverNotes", "", SCHOOL_ID, getActivity().getIntent().getStringExtra("Code"));
    }

    private class displayNotes extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //Log.e("Params:", params[0]);
            Search ev = new Search();
            return ev.dicoverNotes(params[0],params[1],params[2],params[3]);
        }

        @Override
        protected void onPostExecute(String s) {
            convertJSON(s);
            //Log.e("Result:", s);
        }
    }
}
