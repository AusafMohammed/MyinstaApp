package com.example.myinstaapp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;


public class ProfileTab extends Fragment {
    LinearLayout layout;
    EditText name,bio;
    Button btn;



    public ProfileTab() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile_tab, container, false);
        name=v.findViewById(R.id.profilefragname);
        bio=v.findViewById(R.id.profilefragbio);
        btn=v.findViewById(R.id.profilefragbtn);
        layout=v.findViewById(R.id.profileLayout);
        ParseUser user=ParseUser.getCurrentUser();


        name.setText(user.getUsername());
        if(user.get("mybio")==null){
            bio.setText("");
        }else {
            bio.setText(user.get("mybio")+"");
        }

        ParseQuery<ParseObject> myquery=new ParseQuery<ParseObject>("photos");
        myquery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        myquery.orderByDescending("createdAt");
        ProgressDialog dialog=new ProgressDialog(getContext());
        dialog.setMessage("Loading...Plzz wait");
        dialog.show();
        myquery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null&&objects.size()>0){
                    for(ParseObject obj: objects){
                        TextView t=new TextView(getContext());
                        t.setText(obj.get("caption").toString());
                        ParseFile parseFile=(ParseFile) obj.get("picture");
                        parseFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(data!=null&&e==null){
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView postimg=new ImageView(getContext());
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





        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namestr=name.getText().toString();
                String biostr=bio.getText().toString();
                if(namestr.equals("")){
                    name.setError("FIELD IS EMPTY");
                    return;
                }
                if(biostr.equals("")){
                    bio.setError("FIELD IS EMPTY");
                    return;
                }
                user.setUsername(namestr);
                user.put("mybio",biostr);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            FancyToast.makeText(getContext(),"Update Successfull",FancyToast.LENGTH_LONG, FancyToast.SUCCESS,true).show();
                        }
                        else{
                            FancyToast.makeText(getContext(),"Something went wrong..Plzz try again",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                        }
                    }
                });

            }
        });



        return  v;
    }
}