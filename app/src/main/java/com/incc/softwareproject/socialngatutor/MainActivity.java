package com.incc.softwareproject.socialngatutor;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeFontSaTitle();
    }

    private void changeFontSaTitle(){
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
    public void mainButtons(View v){
        Intent i;
        if(v.getId() == R.id.loginBtn){
            i = new Intent(this,AfterLoginActivity.class);
            startActivity(i);
        }
    }
}
