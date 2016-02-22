package com.incc.softwareproject.socialngatutor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.incc.softwareproject.socialngatutor.Server.College;
import com.incc.softwareproject.socialngatutor.adapters.CollegeRecyclerAdapter;
import com.incc.softwareproject.socialngatutor.adapters.CourseRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {
    private List<String> courseId = new ArrayList<>();
    private List<String> courseNo = new ArrayList<>();
    private List<String> courseName = new ArrayList<>();
    private List<String> code = new ArrayList<>();
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("Code"));
        recyclerView = (RecyclerView) findViewById(R.id.discover_course);
        new initCourse().execute(getIntent().getStringExtra("Code"));
    }
    public void clrData(){
        courseId.clear();
        courseNo.clear();
        code.clear();
        courseName.clear();
    }
    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CourseRecyclerAdapter crecyclerAdapter = new CourseRecyclerAdapter(courseId,code,courseName,courseNo);
        recyclerView.setAdapter(crecyclerAdapter);
    }
    private class initCourse extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            College sv = new College();
            String result = sv.getCourse(params[0]);
            clrData();
            try {
                JSONObject reader = new JSONObject(result);
                JSONArray ja = reader.getJSONArray("Course");
                for(int i = 0; i < ja.length(); i++){
                    JSONObject data = ja.getJSONObject(i);
                    courseId.add(data.getString("courseId"));
                    code.add(data.getString("code"));
                    courseNo.add(data.getString("courseNo"));
                    courseName.add(data.getString("name"));
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
