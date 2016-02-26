package com.incc.softwareproject.socialngatutor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.incc.softwareproject.socialngatutor.Server.College;
import com.incc.softwareproject.socialngatutor.adapters.CollegeRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CollegeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> code = new ArrayList<>();
    private List<String> name = new ArrayList<>();
    private List<String> dean = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Choose College");

        recyclerView = (RecyclerView) findViewById(R.id.discover_colleges);
        new initColleges().execute();


        
    }
    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CollegeRecyclerAdapter crecyclerAdapter = new CollegeRecyclerAdapter(code,name,dean);
        recyclerView.setAdapter(crecyclerAdapter);
    }
    public void clrData(){
        name.clear();
        dean.clear();
        code.clear();
    }
    private class initColleges extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            College sv = new College();
            String result = sv.getCollege();
            clrData();
            try {
                JSONObject reader = new JSONObject(result);
                JSONArray ja = reader.getJSONArray("College");
                for(int i = 0; i < ja.length(); i++){
                    JSONObject data = ja.getJSONObject(i);
                    name.add(data.getString("name"));
                    code.add(data.getString("code"));
                    dean.add(data.getString("dean"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setupRecyclerView(recyclerView);
        }
    }
}
