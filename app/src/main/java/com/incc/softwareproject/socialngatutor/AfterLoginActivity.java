package com.incc.softwareproject.socialngatutor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.incc.softwareproject.socialngatutor.alarm.AlarmManagerBroadcastReceiver;
import com.incc.softwareproject.socialngatutor.fragments.fragTab1;
import com.incc.softwareproject.socialngatutor.fragments.fragTab2;
import com.incc.softwareproject.socialngatutor.fragments.fragTab3;
import com.incc.softwareproject.socialngatutor.fragments.fragTab4;
import com.incc.softwareproject.socialngatutor.services.DeleteService;
import com.incc.softwareproject.socialngatutor.services.PostService;

import java.util.ArrayList;
import java.util.List;

public class AfterLoginActivity extends AppCompatActivity {

    private String schoolId;
    private SharedPreferences spreferences;
    private BroadcastReceiver broadcastReceiver;
    private AlarmManagerBroadcastReceiver alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeRed);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        initToolbar();

        alarm = new AlarmManagerBroadcastReceiver();
        Context context = this.getApplicationContext();
        //Log.e("ALA", "KAWEW");
        ConnectivityManager conMgr = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            Toast.makeText(AfterLoginActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
        alarm.SetAlarm(context);

        spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
        schoolId = spreferences.getString("SchoolId", "wala");

        initViewPagerAndTabs();
        broadcastReceiver = new MyBroadcastReceiver();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(PostService.ACTION));
        //registerReceiver(broadcastReceiver, new IntentFilter(DeleteService.ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
    //  ====== ALARM ==============================
    @Override
    protected void onStart() {
        super.onStart();
    }
    public void startRepeatingTimer() {
        Log.e("ALA", "KAWEW");
        Context context = this.getApplicationContext();
        if (alarm != null) {
            alarm.SetAlarm(context);
        } else {
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelRepeatingTimer() {
        Context context = this.getApplicationContext();
        if (alarm != null) {
            alarm.CancelAlarm(context);
        } else {
            Toast.makeText(context, "Notification is Turned off", Toast.LENGTH_SHORT).show();
        }
    }

    public void onetimeTimer() {
        Context context = this.getApplicationContext();
        if (alarm != null) {
            alarm.setOnetimeTimer(context);
        } else {
            Toast.makeText(context, "Alarm is nul", Toast.LENGTH_SHORT).show();
        }
    }

    //=======================================
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            Intent i = new Intent(this, ProfileActivity.class);
            if (!spreferences.getString("SchoolId", "Wala").equals("Wala")) {
                startActivity(i);
            }
        } else if (id == R.id.action_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Do you want to log out?")
                    .setMessage("Are you sure you want to log out? :(")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // log out
                            Intent i = new Intent(AfterLoginActivity.this, MainActivity.class);
                            i.putExtra("Logout", true);
                            startActivity(i);
                            AfterLoginActivity.this.finish();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else if(id == R.id.action_settings)
        {
            Intent i = new Intent(AfterLoginActivity.this, SettingsActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.animate1, R.animator.animate2);
        }
        return super.onOptionsItemSelected(item);

    }


    public void onViewCalendar(View view) {

        Intent i = new Intent(AfterLoginActivity.this, CalendarActivity.class);
        startActivity(i);
        overridePendingTransition(R.animator.animate3,R.animator.animate2);

    }


    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "IntriqueScript_PersonalUse.ttf");
        toolbarTitle.setTypeface(myTypeface);
    }

    private void initViewPagerAndTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(fragTab1.newInstance(), getString(R.string.tab1));
        pagerAdapter.addFragment(fragTab2.createInstance(), getString(R.string.tab2));
        pagerAdapter.addFragment(fragTab3.createInstance(), getString(R.string.tab3));
        pagerAdapter.addFragment(fragTab4.createInstance(), getString(R.string.tab4));
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void addPostBtn(View v) {
        Intent i = new Intent(this, PostActivity.class);
        startActivity(i);
        overridePendingTransition(R.animator.animate3, R.animator.animate2);
    }

    public void searchBtns(View view) {
        String action;
        String sq = ((EditText) this.findViewById(R.id.search_text)).getText().toString();
        Log.e("Q:", sq);
        Intent i = null;
        switch (view.getId()) {
            case R.id.t2_people:
                action = "people";
                i = new Intent(this, SearchActivity.class);
                break;
            case R.id.t2_topics:
                action = "topics";
                i = new Intent(this, SearchActivity.class);
                break;
            default:
                action = "discover";
                i = new Intent(this, CollegeActivity.class);
                break;
        }

        i.putExtra("Queries", sq);
        i.putExtra("Action", action);
        startActivity(i);
        overridePendingTransition(R.animator.animate1, R.animator.animate2);
    }


    static class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra("Success", false)) {
                Toast.makeText(getParent(), "Successfully Posted Something", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
