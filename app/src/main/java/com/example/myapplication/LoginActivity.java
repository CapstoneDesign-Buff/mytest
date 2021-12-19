package com.example.myapplication;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {
    String user_list_file;

    EditText id;
    EditText pw;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_list_file = getFilesDir() + "users_list.txt";

        id = (EditText) findViewById(R.id.userId);
        pw = (EditText) findViewById(R.id.password);


        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //need to check username exist or not...
                boolean succeed = checkValidUser();
                if(succeed) { //로그인 성공
                    Intent intent = new Intent(getApplicationContext(), BusInfoActivity.class);
                    intent.putExtra("user_id", id.getText().toString());
                    startActivity(intent);
                }
                else    {
                    Toast.makeText(getApplicationContext(), "[로그인 오류] 올바르지않은 아이디 또는 비밀번호입니다", Toast.LENGTH_SHORT).show();
                }



            }
        });



        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "[회원 등록 페이지]준비중", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkValidUser() {
        //회원 파일을 읽어서 처리
        String ret = "";
        ArrayList<ItemModel> items = new ArrayList<>();

        try {
            String login_id = id.getText().toString();
            String login_pw = pw.getText().toString();
            FileReader reader = new FileReader(user_list_file);

            if ( reader != null ) {
                BufferedReader bufferedReader = new BufferedReader(reader);
                String receiveString = "";

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    //id,pw,phone,email
                    String[] data = receiveString.split(",");
                    String saved_id = data[0];
                    String saved_pw = data[1];

                    if( login_id.equals(saved_id) && login_pw.equals(saved_pw) ) {
                        return true;
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
        return false;
    }

    @Override
    public void onRestart() {
        super.onRestart();

        //로그인 후 이전으로 돌아오기 모든 contents refresh
        id.setText("");
        pw.setText("");
    }
}