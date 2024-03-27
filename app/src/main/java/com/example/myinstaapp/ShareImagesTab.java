package com.example.myinstaapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


public class ShareImagesTab extends Fragment implements View.OnClickListener{
    EditText caption;
    Button Sharebtn;
    ImageView myimg;
    Bitmap recievedimg;


    public ShareImagesTab() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_share_images_tab, container, false);
        caption=view.findViewById(R.id.mycaption);
        Sharebtn=view.findViewById(R.id.sharebtn);
        myimg=view.findViewById(R.id.myimage);
        myimg.setOnClickListener(ShareImagesTab.this);
        Sharebtn.setOnClickListener(ShareImagesTab.this);

        return view;
    }






    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1000){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                chooseimg();
            }
        }
    }

    private void chooseimg() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2000){
            if(resultCode== Activity.RESULT_OK){
                try{
                    Uri selectedimg=data.getData();
                    String[] filepathcolm={ MediaStore.Images.Media.DATA};
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        Cursor cursor=getActivity().getContentResolver().query(selectedimg,filepathcolm,null,null);
                        cursor.moveToFirst();
                        int collumindex=cursor.getColumnIndex(filepathcolm[0]);
                        String picturepath=cursor.getString(collumindex);
                        cursor.close();
                         recievedimg= BitmapFactory.decodeFile(picturepath);
                        myimg.setImageBitmap(recievedimg);
                    }



                }catch (Exception e){

                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.myimage:{
                if( ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    FancyToast.makeText(getContext(),"Hello World !",FancyToast.LENGTH_LONG, FancyToast.SUCCESS,true).show();

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1000);

                }
                else
                    chooseimg();
                break;
            }
            case R.id.sharebtn:{
                if(recievedimg==null||caption.getText().toString().equals("")){
                    FancyToast.makeText(getContext()," Error...Plzz make sure no field is empty  ",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                    return;
                }
               // Toast.makeText(getContext(), "kjrenferj", Toast.LENGTH_SHORT).show();
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                recievedimg.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                byte[]bytes=byteArrayOutputStream.toByteArray();
                ParseFile parseFile=new ParseFile("pic.png",bytes);
                ParseObject parseObject=new ParseObject("photos");
                parseObject.put("picture",parseFile);
                parseObject.put("caption",caption.getText().toString());
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                ProgressDialog dialog=new ProgressDialog(getContext());
                dialog.setMessage("Uploading...Plzz wait");
                dialog.show();

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null) {
                            FancyToast.makeText(getContext(), "Done", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            myimg.setImageResource(R.drawable.upload);
                            caption.setText("");
                            dialog.dismiss();
                        }
                        else
                            FancyToast.makeText(getContext(),"Error..Plzz try again",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                    }
                });


            }
            default:return;
        }

    }
}