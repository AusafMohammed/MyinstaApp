package com.example.myinstaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity3 extends AppCompatActivity {
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        layout=findViewById(R.id.linear);
        Intent i=getIntent();
        String user=i.getStringExtra("username");
       // Toast.makeText(this, user+" ", Toast.LENGTH_SHORT).show();
        setTitle(user+"'s"+" posts");
        ParseQuery<ParseObject>myquery=new ParseQuery<ParseObject>("photos");
        myquery.whereEqualTo("username",user);
        myquery.orderByDescending("createdAt");
        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Loading...Plzz wait");
        dialog.show();
        myquery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null&&objects.size()>0){
                    for(ParseObject obj: objects){
                        TextView t=new TextView(MainActivity3.this);
                        t.setText(obj.get("caption").toString());
                        ParseFile parseFile=(ParseFile) obj.get("picture");
                        parseFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(data!=null&&e==null){
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView postimg=new ImageView(MainActivity3.this);
                                    LinearLayout.LayoutParams img=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1000);
                                    img.setMargins(10,10,10,10);
                                    postimg.setLayoutParams(img);
                                    postimg.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postimg.setImageBitmap(bitmap);


                                    LinearLayout.LayoutParams cap=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    cap.setMargins(10,10,10,10);
                                    t.setLayoutParams(cap);
                                    t.setGravity(Gravity.CENTER);
                                    t.setTextColor(Color.BLACK);
                                    t.setTextSize(22f);

                                    layout.addView(postimg);
                                    layout.addView(t);


                                }
                            }
                        });
                    }
                }
                dialog.dismiss();
            }
        });
    }
}