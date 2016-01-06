package com.incc.softwareproject.socialngatutor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import static android.support.design.widget.Snackbar.*;


public class SignUpActivity extends AppCompatActivity {

    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mRootView = findViewById(R.id.root_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.su_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setShowHideAnimationEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_register) {
            registerProcess();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void registerProcess() {
        String username, password, rpassword, fullname, birthdate;
        username = ((EditText) findViewById(R.id.s_username)).getText().toString();
        password = ((EditText) findViewById(R.id.s_password)).getText().toString();
        rpassword = ((EditText) findViewById(R.id.s_rpassword)).getText().toString();
        fullname = ((EditText) findViewById(R.id.s_fullname)).getText().toString();

        /*********** GET DATE PICKER BAI ************/
        DatePicker dp = (DatePicker) findViewById(R.id.brithdatePicker);
        birthdate = dp.getYear() + "-" + dp.getMonth() + "-" + dp.getDayOfMonth();
        if (password.equals("") || rpassword.equals("")) {
            Snackbar.make(mRootView, "Password field is empty", LENGTH_LONG).show();
        } else if (password.equals(rpassword) && !password.equals("") && !rpassword.equals("")) {
            Intent i = new Intent(this, SignUpProcessActivity.class);
            i.putExtra("username", username);
            i.putExtra("password", password);
            i.putExtra("fullname", fullname);
            i.putExtra("birthdate",birthdate);
            //add program
            startActivity(i);
        } else {
            Snackbar.make(mRootView, "Password does not match", LENGTH_LONG).show();
        }
    }

}
