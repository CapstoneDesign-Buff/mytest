package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    String user_list_file;
    EditText id;
    EditText passwd;
    EditText phone;
    EditText email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user_list_file = getBaseContext().getFilesDir() + "users_list.txt";

        id = (EditText) findViewById(R.id.reg_userId);
        passwd = (EditText) findViewById(R.id.reg_password);
        phone = (EditText) findViewById(R.id.reg_phone);
        email = (EditText) findViewById(R.id.reg_email);

        Button reg_btn = (Button) findViewById(R.id.reg_confirm);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //save user_id
                boolean succeed = checkValidUserId();
                if(succeed) {   // 자동 로그인

                    //회원가입 성공
                    //입력 정보 저장
                    saveNewUser();


                    Intent intent = new Intent(getApplicationContext(), BusInfoActivity.class);
                    intent.putExtra("user_id", id.getText().toString());
                    startActivity(intent);
                }
                else    {   //회원가입 실패[아이디 중복]
                    Toast.makeText(getApplicationContext(), "[중복 오류] 존재하는 회원 아이디입니다", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void saveNewUser() {
        try {
            FileOutputStream out = new FileOutputStream( user_list_file ,true );

            out.write((id.getText().toString() + ",").getBytes(StandardCharsets.UTF_8));
            out.write((passwd.getText().toString() + ",").getBytes(StandardCharsets.UTF_8));
            out.write((phone.getText().toString() + ",").getBytes(StandardCharsets.UTF_8));
            out.write((email.getText().toString() + "\n").getBytes(StandardCharsets.UTF_8));

            out.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    private boolean checkValidUserId() {
        //회원 파일을 읽어서 처리

        try {
            String newUserID = id.getText().toString();
            FileReader reader = new FileReader(user_list_file);

            if ( reader != null ) {
                BufferedReader bufferedReader = new BufferedReader(reader);
                String receiveString = "";

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    //id,pw,phone,email
                    String[] data = receiveString.split(",");
                    String prev_id = data[0];

                    if( newUserID.equals(prev_id) ) {
                        return false;
                    }
                }

                bufferedReader.close();
                reader.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("RegisterActivity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("RegisterActivity", "Can not read file: " + e.toString());
        }
        return true;
    }

    @Override
    public void onRestart() {
        super.onRestart();
        
        // 로그인 후 앞으로 다시 넘어오면 refresh 처리
        id.setText("");
        passwd.setText("");
        phone.setText("");
        email.setText("");
    }

}
