package com.incc.softwareproject.socialngatutor;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by carlo on 2/18/16.
 */
public class AboutActivity extends AppCompatActivity{

    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //   setSupportActionBar(toolbar);

       /* android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile");
        */
        final Toolbar toolbar = (Toolbar) findViewById(R.id.about_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(getIntent().getStringExtra("EventTitle"));
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar3);
        collapsingToolbarLayout.setTitle("About");

        dynamicToolbarColors();
        toolbarTextApperances();
    }
    private void dynamicToolbarColors() {

        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.color_primary_blue));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.color_primary_blue_dark));

    }

    private void toolbarTextApperances() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar2);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar2);
    }

}
