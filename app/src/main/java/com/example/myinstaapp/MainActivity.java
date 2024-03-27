package com.example.myinstaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity {
    EditText edtname,edtemail,edtpass,edtpass2;
    Button btn;
    TextView t;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtname=findViewById(R.id.username);
        edtemail=findViewById(R.id.email);
        edtpass=findViewById(R.id.editTextTextPassword);
        edtpass2=findViewById(R.id.editTextTextPassword2);
        btn=findViewById(R.id.button);
        t=findViewById(R.id.textView);
        if(ParseUser.getCurrentUser()!=null){
            Intent i=new Intent(MainActivity.this,MainActivity2.class);
            startActivity(i);
        }


    }

    public void BtnIsTapped(View v){



        ParseUser user=new ParseUser();
        String email,name,pass,pass2;
        if(flag==0){

            email = edtemail.getText().toString();
            name = edtname.getText().toString();
            pass = edtpass.getText().toString();
            pass2 = edtpass2.getText().toString();
            if(email.equals("")){
                edtemail.setError("PLEASE ENTER VALID EMAIL");
                return;
            }
        if(name.equals("")){
            edtname.setError("PLEASE ENTER VALID USER NAME");
            return;
        }
        if(pass.equals("")){
            edtpass.setError("PLEASE ENTER VALID PASSWORD");
            return;
        }
        if(!pass.equals(pass2)){
            edtpass2.setError("PASSWORD NOT MATCHING");
            return;
        }
        user.setEmail(email);
        user.setUsername(name);
        user.setPassword(pass);
            ProgressDialog dialog=new ProgressDialog(this);
            dialog.setMessage("Signing In...Plzz wait");
        dialog.show();

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null) {
                    //Toast.makeText(MainActivity.this, name + " signed in successfully", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(MainActivity.this,MainActivity2.class);
                    startActivity(i);
                }
                else
                    Toast.makeText(MainActivity.this, "Sorry..plzz check your internet", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        }
        else{
            name=edtname.getText().toString();
            pass=edtpass.getText().toString();
            if(name.equals("")){
                edtname.setError("Plzz Enter User Name");
                return;
            }
            if(pass.equals("")){
                edtpass.setError("Plzz Enter Valid Password");
                return;
            }
            ProgressDialog dialog=new ProgressDialog(this);
            dialog.setMessage("Logging In...Plzz wait");
            dialog.show();

            ParseUser.logInInBackground(name, pass, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(user!=null&&e==null){
                        //Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(MainActivity.this,MainActivity2.class);
                        startActivity(i);

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "ERROR..WRONG USERNAME OR PASSWORD", Toast.LENGTH_SHORT).show();                    }
                    dialog.dismiss();

                }
            });



        }



    }
    public void textistapped(View view){
        if(flag==0){
            t.setText("CLICK HERE TO SIGN UP");
            flag=1;
            edtemail.setVisibility(View.GONE);
            edtpass2.setVisibility(View.GONE);
            btn.setText("LOG IN");
        }
        else {
            t.setText("CLICK HERE TO LOG IN");
            edtemail.setVisibility(View.VISIBLE);
            edtpass2.setVisibility(View.VISIBLE);
            btn.setText("SIGN IN ");
            flag=0;

        }
    }
}