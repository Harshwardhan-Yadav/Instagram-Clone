package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class UserFeed extends AppCompatActivity {

    LinearLayout linearLayout;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public class DownloadTask extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            try{
                URL url;
                HttpURLConnection urlConnection=null;
                url=new URL(strings[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                Bitmap bitmap=BitmapFactory.decodeStream(in);
                return bitmap;
            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        Intent intent=getIntent();
        String username=intent.getStringExtra("username");
        setTitle(""+username+"'s Images");

        linearLayout = findViewById(R.id.linearLayout);

        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Image");
        query.whereEqualTo("username",username);
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()>0){
                    for(ParseObject i:objects){
                        ParseFile file=(ParseFile)i.get("image");
                        int index=file.getUrl().indexOf("parse");
                        String src="http://18.119.109.210:80/"+file.getUrl().substring(index);
                        DownloadTask task=new DownloadTask();
                        try {
                            Bitmap bitmap1 = task.execute(src).get();
                            ImageView imageView = new ImageView(getApplicationContext());
                            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                            imageView.setImageBitmap(bitmap1);
                            linearLayout.addView(imageView);
                        } catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"No feed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}