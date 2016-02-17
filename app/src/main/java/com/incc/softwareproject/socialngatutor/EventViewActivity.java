 package com.incc.softwareproject.socialngatutor;

        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.support.v4.view.ViewCompat;
        import android.support.v7.app.ActionBar;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.content.SharedPreferences;
        import android.support.v7.graphics.Palette;
        import android.os.Bundle;
        import android.support.design.widget.CollapsingToolbarLayout;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

public class EventViewActivity extends AppCompatActivity{

    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventview);

        //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //   setSupportActionBar(toolbar);

       /* android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile");
        */
        final Toolbar toolbar = (Toolbar) findViewById(R.id.eventview_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(getIntent().getStringExtra("EventTitle"));
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar2);
        collapsingToolbarLayout.setTitle("");

        dynamicToolbarColors();
        toolbarTextApperances();
        ((TextView) findViewById(R.id.event_view_dateyear)).setText(getIntent().getStringExtra("EventDate") + " " + getIntent().getStringExtra("EventMonthYear") );
        ((TextView) findViewById(R.id.event_view_description)).setText(getIntent().getStringExtra("EventDescription"));
        ((TextView) findViewById(R.id.event_view_monthday)).setText(getIntent().getStringExtra("EventTitle"));
    }
    private void dynamicToolbarColors() {

        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.color_primary_red));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.color_primary_red_dark));

    }

    private void toolbarTextApperances() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar2);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar2);
    }
}

