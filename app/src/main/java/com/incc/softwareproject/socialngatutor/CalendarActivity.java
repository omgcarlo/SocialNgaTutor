package com.incc.softwareproject.socialngatutor;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.incc.softwareproject.socialngatutor.Server.Events;
import com.incc.softwareproject.socialngatutor.adapters.CommentRecyclerAdapter;
import com.incc.softwareproject.socialngatutor.adapters.EventRecyclerAdapter;
import com.incc.softwareproject.socialngatutor.adapters.SearchRecyclerAdapter;
import com.incc.softwareproject.socialngatutor.calendar.CustomEvent;
import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.exception.HighValueException;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendar.view.SquareCellView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, FlexibleCalendarView.OnMonthChangeListener {

    private Map<Integer, List<CustomEvent>> eventMap;
    private FlexibleCalendarView calendarView;
    private String MONTH[] = {"January", "February", "March",
            "April", "May", "June", "July", "August",
            "September", "October", "November", "December"};
    private TextView month_txt;
    private static final String TAG = "CalendarActivity";
    private android.support.v7.widget.Toolbar mToolbar;
    private List<String> eventId = new ArrayList<>();
    private List<String> title = new ArrayList<>();
    private List<String> description = new ArrayList<>();
    private List<String> eventdate = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.calendar_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = (RecyclerView) findViewById(R.id.calendar_e_recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        month_txt = (TextView) findViewById(R.id.calendar_month);
        Calendar c = Calendar.getInstance();
        month_txt.setText(MONTH[c.get(Calendar.MONTH)]);   //  TEMP DISPLAY BEFORE MO CHANGE SA ONCHANGELISTENER
        String datenow = c.get(Calendar.YEAR) +
                "-" + (c.get(Calendar.MONTH)+1) +           // +1 kay 0 gastart ang counting
                "-" + c.get(Calendar.DATE);

        calendarView = (FlexibleCalendarView) findViewById(R.id.calendar_view);
        calendarView.setMonthViewHorizontalSpacing(10);
        calendarView.setMonthViewVerticalSpacing(10);
        calendarView.setShowDatesOutsideMonth(false);
        calendarView.setOnMonthChangeListener(this);

        calendarView.setOnDateClickListener(new FlexibleCalendarView.OnDateClickListener() {
            String clickedDate;

            @Override
            public void onDateClick(int year, int month, int day) {
                clickedDate = year + "-" + (month + 1) + "-" + day;
                clearData();
                new displayEvents().execute(clickedDate);
            }
        });
        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {
            @Override
            public BaseCellView getCellView(int position, View convertView, ViewGroup parent, int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(CalendarActivity.this);
                    cellView = (BaseCellView) inflater.inflate(R.layout.calendar_date_cell_view, null);
                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(int position, View convertView, ViewGroup parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(CalendarActivity.this);
                    cellView = (BaseCellView) inflater.inflate(R.layout.calendar_week_cell_view, null);
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
                return null;
            }
        });

        new displayEvents().execute(datenow);
    }

    public List<CustomEvent> getEvents(int year, int month, int day) {
        return eventMap.get(day);
    }

    private void clearData() {
        title.clear();
        description.clear();
        eventdate.clear();
        eventId.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        try {
            calendarView.selectDate(year, monthOfYear, dayOfMonth);
        } catch (HighValueException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMonthChange(int year, int month, int direction) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        month_txt.setText(cal.getDisplayName(Calendar.MONTH,
                Calendar.LONG, Locale.ENGLISH) + " " + year);
        String clickedDate = year + "-" + (month + 1) + "-" + 01;
        clearData();
        new displayUpcomingEvents().execute(clickedDate);

    }

    public void onCloseCalendar(View view) {

        finish();

    }

    private void setupRecyclerView(RecyclerView erecycler) {
        erecycler.setLayoutManager(new LinearLayoutManager(this));
        EventRecyclerAdapter eRecyclerAdapter = new EventRecyclerAdapter(eventId, title, description, eventdate);
        erecycler.setAdapter(eRecyclerAdapter);
    }

    private void convertJSON(String result) {
        try {
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
            return ev.getEvents(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            convertJSON(s);
            //Log.e("Result:", s);
        }
    }
    private class displayUpcomingEvents extends AsyncTask<String, Void, String> {

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
