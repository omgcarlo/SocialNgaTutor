package com.incc.softwareproject.socialngatutor.fragments;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.Server.Events;
import com.incc.softwareproject.socialngatutor.adapters.EventRecyclerAdapter;
import com.incc.softwareproject.socialngatutor.adapters.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by carlo on 21/12/2015.
 */
public class fragTab3 extends Fragment {
    private String MONTH[] = {"January","February","March",
                                "April","May","June","July","August",
                                "September","October","November","December"};
    private String DOW[] = {"Sunday","Monday","Tuesday","Wednesday",
                                "Thursday","Friday","Saturday"};

    private List<String> eventId = new ArrayList<>();
    private List<String> title = new ArrayList<>();
    private List<String> description = new ArrayList<>();
    private List<String> eventdate = new ArrayList<>();

    private RecyclerView recyclerView;
    public static fragTab3 createInstance() {
        fragTab3 frag_tab3 = new fragTab3();
        Bundle bundle = new Bundle();
        frag_tab3.setArguments(bundle);
        return frag_tab3;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_3, container, false);
        Typeface myTypeface = Typeface.createFromAsset(view.getContext().getAssets(), "VintageOne.ttf");
        TextView monthday = (TextView) view.findViewById(R.id.t3_monthday);
        TextView dateyear = (TextView) view.findViewById(R.id.t3_dateyear);
        recyclerView = (RecyclerView) view.findViewById(R.id.t3_e_recyclerView);
        Calendar c = Calendar.getInstance();
        monthday.setText(String.format("%s %d", MONTH[c.get(Calendar.MONTH)], c.get(Calendar.DATE)));
        dateyear.setText(String.format("%s•%d", DOW[(c.get(Calendar.DAY_OF_WEEK)) - 1], c.get(Calendar.YEAR)));
        monthday.setTypeface(myTypeface);
        String datenow = c.get(Calendar.YEAR) +
                "-" + (c.get(Calendar.MONTH)+1) +           // +1 kay 0 gastart ang counting
                "-" + c.get(Calendar.DATE);
        new displayEvents().execute(datenow);
        return view;
    }
    private void setupRecyclerView(RecyclerView erecycler) {
        erecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        EventRecyclerAdapter eRecyclerAdapter = new EventRecyclerAdapter(eventId,title,description,eventdate);
        erecycler.setAdapter(eRecyclerAdapter);
    }
    private void clearData(){
        title.clear();
        description.clear();
        eventdate.clear();
        eventId.clear();
    }
    private void convertJSON(String result) {
        try {
            clearData();
            JSONObject reader = new JSONObject(result);
            JSONArray data = reader.getJSONArray("Event");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonobject = data.getJSONObject(i);
                eventId.add(jsonobject.getString("event_id"));
                eventdate.add(jsonobject.getString("event_date"));
                title.add(jsonobject.getString("Title"));
                description.add(jsonobject.getString("description"));
            }
            setupRecyclerView(recyclerView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class displayEvents extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //Log.e("Params:", params[0]);
            Events ev = new Events();
            return ev.getUpcommentEvents(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            convertJSON(s);
            //Log.e("Result:", s);
        }
    }
}
