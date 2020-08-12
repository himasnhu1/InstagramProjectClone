package com.example.instagramproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener {

    TextView textView;
    EditText edittextusername;
    EditText edittextpassword;
    Button loginuserButton;
    boolean signUpModeIsActivate =false;

    public void showUserList(){

        Intent intent = new Intent(getApplicationContext(),userListActivity.class);
        startActivity(intent);

    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent){

        if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){

            clicktologin(view);
        }

        return false;
    }

    public void onClick(View view) {

        if (view.getId() == R.id.textView)
            {
                loginuserButton = (Button)findViewById(R.id.login);

                                 if(signUpModeIsActivate)
                                 {
                                     signUpModeIsActivate = false;
                                     loginuserButton.setText("Login");
                                     textView.setText("or,Signup");
                                     Toast.makeText(this,"false",Toast.LENGTH_SHORT).show();

                                 }
                                 else
                                 {
                                        signUpModeIsActivate = true;
                                        loginuserButton.setText("Signup");
                                         textView.setText("or,login");
                                     Toast.makeText(this,"true ",Toast.LENGTH_SHORT).show();

                                 }
            }
        else if(view.getId() == R.id.backgroundRelatedlayout || view.getId() == R.id.logoimageView)
         {
             InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
             inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
 
    }
}

    public  void clicktologin(View view) {
        edittextpassword = (EditText)findViewById(R.id.editTextPassword);
        edittextusername = (EditText)findViewById(R.id.ediTexUsername);

        if(edittextusername.getText().toString().matches("") || edittextpassword.getText().toString().matches("")){
            Toast.makeText(this,"A username and password requied",Toast.LENGTH_SHORT).show();
        }
        else {
            if(signUpModeIsActivate) {
                ParseUser user = new ParseUser();
                user.setUsername(edittextusername.getText().toString());
                user.setPassword(edittextpassword.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            Log.i("Signup", "Successfully");
                            Toast.makeText(MainActivity.this,"Signup Sucessfully",Toast.LENGTH_SHORT).show();

                            showUserList();
                        } else {

                            Toast.makeText(MainActivity.this,"error" , Toast.LENGTH_SHORT).show();

                            //e.getMessage()
                        }
                    }
                });
            } else{

                ParseUser.logInInBackground(edittextusername.getText().toString(), edittextpassword.getText().toString(), (user, e) -> {
                    if (user == null) {

                        Log.i("Login", "Successfully");
                        showUserList();
                    } else {

                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Instagram");
    //    ParseUser.logOut();
        if(ParseUser.getCurrentUser() != null){
            showUserList();
        }

        textView = (TextView)findViewById(R.id.textView);
        textView.setOnClickListener(this);
        edittextpassword = (EditText)findViewById(R.id.editTextPassword);
        edittextpassword.setOnKeyListener( this);

        RelativeLayout backgroundrelatedlayout = (RelativeLayout)findViewById(R.id.backgroundRelatedlayout);
        ImageView logoimageview = (ImageView) findViewById(R.id.logoimageView);
        backgroundrelatedlayout.setOnClickListener(this);
        logoimageview.setOnClickListener(this);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

}



//       ParseUser.logOut();
//        if(ParseUser.getCurrentUser() != null) {
//
//            Log.i("currentUser","User logged in "+ParseUser.getCurrentUser().getUsername());
//
//        } else  {
//
//            Log.i("CurrentUser","User not logged in");
//
//        }

/*
        ParseUser.logInInBackground("himanshusaini", "mypass", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null){
                    Log.i("Login","Successfully");
                }
                else {
                     Log.i("Login","Failed:"+e.toString());
                }
            }
        });

*/
//        ParseUser user = new ParseUser();
//        user.setUsername("himanshusaini");
//        user.setPassword("mypass");
//
//        user.signUpInBackground(new SignUpCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e == null){
//
//                    Log.i("Sign up","Successful");
//                }else{
//                 Log.i("Sign Up","Failed");
//                }
//            }
//        });


//        ParseQuery<ParseObject> query = ParseQuery.getQuery("score");
//        query.whereGreaterThan("score",200);
//
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//
//                if(e == null && objects != null) for (ParseObject objected : objects) {
//
//                    objected.put("score", objected.getInt("score") + 150);
//                    objected.saveInBackground();
//                }
//
//            }
//        });

//        ParseObject score = new ParseObject("Score");
//        score.put("Username","Himanshu");
//        score.put("score",86);
//        score.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e == null){
//
//                    Log.i("User_data saved","Successfull");
//                }
//                else{
//
//                    Log.i("user_data saving","failed");
//                }
//            }
//        });

//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//
//                if(e == null ){
//                    Log.i("findInBackground","Retrieved" +objects.size()+" objects");
//                    if(objects.size() > 0){
//                        for(ParseObject object : objects){
//
//                            Log.i("findInBackground",Integer.toString(object.getInt("score")));
//                        }
//                    }
//                }
//            }
//        });


//        query.getInBackground("nyEOpdAzAY", new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//              if( e == null && object != null){
//
//                  object.put("score",200);
//                  object.saveInBackground();
//                  Log.i("objectValue",object.getString("Username"));
//                  Log.i("objectValue",Integer.toString(object.getInt("score")));
//              }
//            }
//        });
