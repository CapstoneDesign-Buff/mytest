package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.myapplication.groupsalgo.CreateBusInfo;
import com.example.myapplication.groupsalgo.Utils;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            File busfile = new File(getFilesDir().toString()+"busInfo.csv");
            File sample_dataset = new File(getFilesDir().toString()+"demo_dataset.csv");

            if(!busfile.exists() || !sample_dataset.exists())   {   // demo data doesn't exist
                CreateBusInfo.CreateData(getFilesDir().toString());
            }
//            CreateBusInfo.CreateData(getFilesDir().toString());
            Utils.parseBusInfo(getFilesDir().toString() + "busInfo.csv");
            //check serveral file successfully created..
//            FileReader reader = new FileReader(getFilesDir() + "busInfo.csv");
//            BufferedReader bufferedReader = new BufferedReader(reader);
//            String receiveString = bufferedReader.readLine();
//            receiveString = bufferedReader.readLine();
//            Toast.makeText(getApplicationContext(), receiveString, Toast.LENGTH_SHORT).show();
//            reader.close();
//            bufferedReader.close();
//
//            reader = new FileReader(getFilesDir() + "demo_dataset.csv");
//            bufferedReader = new BufferedReader(reader);
//            receiveString = bufferedReader.readLine();
//            receiveString = bufferedReader.readLine();
//            Toast.makeText(getApplicationContext(), receiveString, Toast.LENGTH_SHORT).show();
//            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageView bg_gif = (ImageView) findViewById(R.id.main_bg);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(bg_gif);
        Glide.with(this).load(R.drawable.background).into(gifImage);

        loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }



}