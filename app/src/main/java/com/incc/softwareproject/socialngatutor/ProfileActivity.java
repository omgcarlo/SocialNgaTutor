package com.incc.softwareproject.socialngatutor;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    String schoolId;
    private SharedPreferences spreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile");
        spreferences = getSharedPreferences("ShareData",MODE_PRIVATE);
        initData();
    }
    public void initData(){

        schoolId = spreferences.getString("SchoolId","wala");
        ((TextView) findViewById(R.id.profile_fullname)).setText(spreferences.getString("FullName","Wala"));
        ((TextView) findViewById(R.id.profile_username)).setText("@" + spreferences.getString("Username","Wala"));
    }
}
