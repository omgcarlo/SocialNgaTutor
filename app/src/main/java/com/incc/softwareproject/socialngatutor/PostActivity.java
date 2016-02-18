package com.incc.softwareproject.socialngatutor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.incc.softwareproject.socialngatutor.Server.User;
import com.incc.softwareproject.socialngatutor.mentions.models.Person;
import com.incc.softwareproject.socialngatutor.services.PostService;
import com.linkedin.android.spyglass.suggestions.SuggestionsResult;
import com.linkedin.android.spyglass.suggestions.impl.BasicSuggestionsListBuilder;
import com.linkedin.android.spyglass.suggestions.interfaces.Suggestible;
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsListBuilder;
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsResultListener;
import com.linkedin.android.spyglass.tokenization.QueryToken;
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver;
import com.linkedin.android.spyglass.ui.RichEditorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity implements QueryTokenReceiver {
    private SharedPreferences spreferences;
    private BroadcastReceiver broadcastReceiver;

    boolean vis = false;    //  TAG INPUT VISIBILITY
    private List<String> username = new ArrayList<>();
    private String schoolId;

    // MENTION
    private Person.PersonLoader people;
    private RichEditorView editor;
    private SuggestionsResult lastPersonSuggestions;

    private static final String PERSON_BUCKET = "people-database";
    private static final String CITY_BUCKET = "city-network";
    private static final int PERSON_DELAY = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
        schoolId = spreferences.getString("SchoolId", "Wala");
        new getUsers().execute(schoolId);
        broadcastReceiver = new MyBroadcastReceiver();

        editor = (RichEditorView) findViewById(R.id.pt_desc);
        editor.setQueryTokenReceiver(this);
        editor.setSuggestionsListBuilder(new CustomSuggestionsListBuilder());
        //  GET DATA FROM CLOUD
        new getUsers().execute(schoolId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(PostService.ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }


    public void showTagInput(View v){

        if(vis) {
            findViewById(R.id.pt_tag).setVisibility(View.GONE);
            vis = false;
        }
        else {
            findViewById(R.id.pt_tag).setVisibility(View.VISIBLE);
            vis = true;
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_post){
            String description = ((MultiAutoCompleteTextView) findViewById(R.id.pt_desc)).getText().toString();
            String tags = ((EditText) findViewById(R.id.pt_tag)).getText().toString();
            String type = "Post";
            spreferences = getSharedPreferences("ShareData",MODE_PRIVATE);
            String ownerId = spreferences.getString("SchoolId","wala");
            //post process
            Intent intent = new Intent(this, PostService.class);
            intent.putExtra("description", description);
            intent.putExtra("tags",tags);
            intent.putExtra("type",type);
            intent.putExtra("ownerId", ownerId);
            startService(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public List<String> onQueryReceived(@NonNull final QueryToken queryToken) {
        final boolean hasPeople = true; //  ALWAYS TRUE

        final List<String> buckets = new ArrayList<>();
        final SuggestionsResultListener listener = editor;
        final Handler handler = new Handler(Looper.getMainLooper());

        // Fetch people if necessary
        if (hasPeople) {
            buckets.add(PERSON_BUCKET);
            new Runnable() {
                @Override
                public void run() {
                    List<Person> suggestions = people.getSuggestions(queryToken);
                    lastPersonSuggestions = new SuggestionsResult(queryToken, suggestions);
                    listener.onReceiveSuggestionsResult(lastPersonSuggestions, PERSON_BUCKET);
                }
            };
        }
        return buckets;
    }

    private class getUsers extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            User sv = new User();
            return sv.getFollowingPeople(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            people = new Person.PersonLoader(s);
        }
    }
    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra("Success",false)){
                Toast.makeText(context, "Successfully Posted Something", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(),AfterLoginActivity.class);
                startActivity(i);
                finish();
            }
        }
    }
    private class CustomSuggestionsListBuilder extends BasicSuggestionsListBuilder {

        @NonNull
        @Override
        public View getView(@NonNull Suggestible suggestion,
                            @Nullable View convertView,
                            ViewGroup parent,
                            @NonNull Context context,
                            @NonNull LayoutInflater inflater,
                            @NonNull Resources resources) {

            View v =  super.getView(suggestion, convertView, parent, context, inflater, resources);
            if (!(v instanceof TextView)) {
                return v;
            }

            // Color text depending on the type of mention
            TextView tv = (TextView) v;
            if (suggestion instanceof Person) {
                tv.setTextColor(getResources().getColor(R.color.color_primary_blue));
            }

            return tv;
        }
    }

}
