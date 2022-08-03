/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  TextView loginTextView;
  RelativeLayout relativeLayout;
  ImageView icon;
  boolean mode=true;//true for signup and false for login

  public void showUserList(){
    Intent intent=new Intent(getApplicationContext(),UserListActivity.class);
    startActivity(intent);
  }

  public void onClick(View view){
      if(view.getId()==R.id.loginTextView){
        if(mode){
          mode=false;
          loginTextView.setText("Or, Sign Up");
          Button temp=findViewById(R.id.signupButton);
          temp.setText("Login");
        }
        else{
          mode=true;
          loginTextView.setText("Or, Login");
          Button temp=findViewById(R.id.signupButton);
          temp.setText("Sign Up");
        }
      }
      else if(view.getId()==R.id.iconImageView || view.getId()==R.id.relativeLayout){
        InputMethodManager keyboard=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
      }
  }
  public void signUpClicked(View view){
    EditText Username=(EditText)findViewById(R.id.usernameEditText);
    EditText Password=(EditText)findViewById(R.id.passwordEditText);
    if(Username.getText().toString().compareTo("")==0 || Password.getText().toString().compareTo("")==0){
      Toast.makeText(this, "Username and Password must not be empty", Toast.LENGTH_SHORT).show();
      return;
    }
    ParseUser user=new ParseUser();
    user.setUsername(Username.getText().toString());
    user.setPassword(Password.getText().toString());
    if(mode)
      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
          if(e!=null){
            Toast.makeText(getApplicationContext(),"Username unavailable",Toast.LENGTH_SHORT).show();
            return;
          }
          System.out.println("signed up.");
          showUserList();
        }
      });
    else
      ParseUser.logInInBackground(Username.getText().toString(), Password.getText().toString(), new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {
          if(e!=null){
            Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
            return;
          }
          System.out.println("logged in.");
          showUserList();
        }
      });
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("Instagram");

    loginTextView=findViewById(R.id.loginTextView);
    loginTextView.setOnClickListener(this);
    relativeLayout=findViewById(R.id.relativeLayout);
    relativeLayout.setOnClickListener(this);
    icon=findViewById(R.id.iconImageView);
    icon.setOnClickListener(this);

    if(ParseUser.getCurrentUser()!=null){
      showUserList();
    }

    /*ParseObject tweet=new ParseObject("Score");
    tweet.put("username","Goog_Player");
    tweet.put("score",17);
    tweet.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if(e==null){
          System.out.println("score saved");
        }
        else{
          e.printStackTrace();
        }
      }
    });*/

    /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");
    query.getInBackground("cKlzyJpwUd", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {
        if(e==null){
          System.out.println("Tweet by Manoj_Yadav: "+object.getString("tweet"));
          object.put("tweet","Tweet changed!");
          object.saveInBackground();
          for(int i=0;i<100000;i++){}
          System.out.println("Tweet by Manoj_Yadav: "+object.getString("tweet"));

        } else {
          e.printStackTrace();
        }
      }
    });*/

    /*ParseQuery<ParseObject> query= ParseQuery.getQuery("Score");
    query.whereLessThan("score",50);
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {
        for(int i=0;i<objects.size();i++){
          int score=objects.get(i).getInt("score");
          objects.get(i).put("score",score+20);
          objects.get(i).saveInBackground();
        }
        System.out.println("scores updated");
      }
    });*/

    /*ParseUser user=new ParseUser();
    user.setUsername("Harshwardhan_Yadav");
    user.setPassword("Harsh@123");
    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        System.out.println("done");
      }
    });*/

    /*ParseUser.logInInBackground("Harshwardhan_Yadav", "Harsh@123", new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        System.out.println("Logged in");
      }
    });*/

    //ParseUser.logOut();

    /*if(ParseUser.getCurrentUser()!=null){
      System.out.println("Logged_in: "+ParseUser.getCurrentUser().getUsername());
    }*/

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}