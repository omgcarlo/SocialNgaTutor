package com.incc.softwareproject.socialngatutor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MainActivity extends AppCompatActivity {
    static SharedPreferences spreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            if (getIntent().getBooleanExtra("Logout", false)){
                getSharedPreferences("ShareData", MODE_PRIVATE)
                        .edit()
                        .clear()
                        .commit();
            }
        }
        try {
            spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
            if (spreferences.getString("SchoolId", null) != null) {
                Intent i = new Intent(this, AfterLoginActivity.class);
                startActivity(i);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        changeFontSaTitle();

    }

    private void changeFontSaTitle() {
        TextView appTitle = (TextView) findViewById(R.id.app_title_in_banner);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "IntriqueScript_PersonalUse.ttf");
        appTitle.setTypeface(myTypeface);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void mainButtons(View v) {
        Intent i;
        if (v.getId() == R.id.loginBtn) {
            /**
             * No Encryption for now
             * bacos nagdali oks
             */

            String username = ((EditText) findViewById(R.id.username)).getText().toString();
            String password = ((EditText) findViewById(R.id.password)).getText().toString();
            if (!username.equals("") || !password.equals("")) {
                //if login is a success
                i = new Intent(this, LoginProcessActivity.class);
                i.putExtra("username", username);
                i.putExtra("password", password);
                startActivity(i);
                finish();
            } else {
                Snackbar.make(v, "Wrong input po", Snackbar.LENGTH_LONG).show();

            }

        } else {
            i = new Intent(this, SignUpActivity.class);
            startActivity(i);
        }


    }

}
