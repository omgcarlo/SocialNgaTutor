package com.incc.softwareproject.socialngatutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import static android.support.design.widget.Snackbar.*;


public class SignUpActivity extends AppCompatActivity {

    private View mRootView;
    private static int RESULT_LOAD_IMAGE = 1;
    private String picturePath;
    ProgressDialog mPDialog;
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
    public void selectPicBtn(View v){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    //  LIVE PREVIEW SA IMAGE
    //  UG preparation sa IMAGE URI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            ImageView imageView = (ImageView) findViewById(R.id.pp_btn);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

    }
    public void registerProcess() {
        String schoolId,username, password, rpassword, fullname, birthdate,email;
        /***** GET DATA *****/
        schoolId = ((EditText) findViewById(R.id.s_schoolId)).getText().toString();
        username = ((EditText) findViewById(R.id.s_username)).getText().toString();
        password = ((EditText) findViewById(R.id.s_password)).getText().toString();
        rpassword = ((EditText) findViewById(R.id.s_rpassword)).getText().toString();
        fullname = ((EditText) findViewById(R.id.s_fullname)).getText().toString();
        email = ((EditText) findViewById(R.id.s_email)).getText().toString();
        /*********** GET DATE PICKER  ************/
        DatePicker dp = (DatePicker) findViewById(R.id.brithdatePicker);
        birthdate = dp.getYear() + "-" + (dp.getMonth()+1) + "-" + dp.getDayOfMonth();

        if (password.equals("") || rpassword.equals("")) {
            Snackbar.make(mRootView, "Password field is empty", LENGTH_LONG).show();
        } else if (password.equals(rpassword) && !password.equals("") && !rpassword.equals("")) {
            Intent i = new Intent(this, SignUpProcessActivity.class);
            i.putExtra("schoolId", schoolId);
            i.putExtra("username", username);
            i.putExtra("password", password);
            i.putExtra("fullname", fullname);
            i.putExtra("birthdate",birthdate);
            i.putExtra("email",email);
            i.putExtra("imagepath",picturePath);
            //add program
            startActivity(i);
            finish();
        } else {
            Snackbar.make(mRootView, "Password does not match", LENGTH_LONG).show();
        }
    }

}
