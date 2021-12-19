package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.groupsalgo.Utils;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

public class ReserveActivity extends AppCompatActivity {
    Vector<Fragment> busReserveFragment;
    TabLayout tabs_reserve;
    Button confirmBtn;
    String reserveDate;
    BusReserveFragment busReserveFrag;
    BusReserveConfirmFragment busConfirmFrag;
    String reserve_time;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        Intent mIntent = getIntent();
        int index = mIntent.getIntExtra("tabIndex", 0);
        user_id = mIntent.getStringExtra("user_id");
        TextView userName = (TextView) findViewById(R.id.user_id_box);
        userName.setText(user_id);

        tabs_reserve = findViewById(R.id.tabs_reserve);
        confirmBtn = (Button)findViewById(R.id.bus_confirm);

        Button reserveBtn = findViewById(R.id.bus_reserve_ractivity);

        initBusReserveMode(index);


        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "예약 하시겠습니까?", Toast.LENGTH_SHORT).show();
                if(tabs_reserve.getSelectedTabPosition() == 1)  {   //예약 확인 탭에서 예약하기 누름
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_reserve, busReserveFragment.get(0)).commit();
                    tabs_reserve.getTabAt(0).select();
                    changeBottomButtons(0);
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("예약하시겠습니까?");

                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String reserve_hour = busReserveFrag.getHour_spinner().getSelectedItem().toString();
                            String reserve_minute = busReserveFrag.getMinute_spinner().getSelectedItem().toString();
                            reserve_time = reserve_hour + ":" + reserve_minute;
//                            Toast.makeText(getBaseContext(),"Date: " + reserveDate + ", " + reserve_time ,Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            // save reserve data
                            boolean succeed = writeToFile();
                            if(succeed) {
                                Intent intent = new Intent(ReserveActivity.this, ReserveActivity.class);
                                intent.putExtra("tabIndex", 1);
                                intent.putExtra("user_id", user_id);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish(); // Call once you redirect to another activity
                            }
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    public void initBusReserveMode(int index)    {
        busReserveFragment = new Vector<>();

        //set reserve date default value: today
        reserveDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()).toString();

        tabs_reserve.addTab(tabs_reserve.newTab().setText("버스예약"));
        tabs_reserve.addTab(tabs_reserve.newTab().setText("예약확인"));
        tabs_reserve.selectTab(tabs_reserve.getTabAt(index));

        busReserveFrag = new BusReserveFragment(user_id);
        busConfirmFrag = new BusReserveConfirmFragment(user_id);
        busReserveFragment.add(busReserveFrag);
        busReserveFragment.add(busConfirmFrag);

        getSupportFragmentManager().beginTransaction().add(R.id.container_reserve, busReserveFragment.get(index)).commit();
        changeBottomButtons(index);

        tabs_reserve.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = busReserveFragment.get(position);
                tabs_reserve.selectTab(tabs_reserve.getTabAt(position));

                changeBottomButtons(position);

                getSupportFragmentManager().beginTransaction().replace(R.id.container_reserve, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @SuppressLint("WrongConstant")
    private boolean writeToFile() {
        try {

            String fileName = getFilesDir() + "reserve_"+user_id+".txt";


            FileOutputStream  out = new FileOutputStream( fileName ,true );

            out.write(("date@#@" +reserveDate+",").getBytes(StandardCharsets.UTF_8));
            out.write(("stSTOP@#@" + busReserveFrag.getStartStopName().getText().toString()+",").getBytes(StandardCharsets.UTF_8));
            out.write(("edSTOP@#@" +busReserveFrag.getEndStopName().getText().toString()+",").getBytes(StandardCharsets.UTF_8));
            out.write(("time@#@" +reserve_time+"\n").getBytes(StandardCharsets.UTF_8));
            out.close();


            String totalBusReserveFile = getFilesDir().toString()+"demo_dataset.csv";
            out = new FileOutputStream( totalBusReserveFile ,true );

            out.write((Utils.getStopNumber(busReserveFrag.getStartStopName().getText().toString())+",").getBytes(StandardCharsets.UTF_8));
            out.write((Utils.getStopNumber(busReserveFrag.getEndStopName().getText().toString())+",").getBytes(StandardCharsets.UTF_8));
            out.write((reserve_time+",").getBytes(StandardCharsets.UTF_8));
            out.write((reserveDate+"\n").getBytes(StandardCharsets.UTF_8));
            String builder = Utils.getStopNumber(busReserveFrag.getStartStopName().getText().toString())+"," + Utils.getStopNumber(busReserveFrag.getEndStopName().getText().toString())+"," + reserve_time+"," + reserveDate+"\n";

//            Toast.makeText(getBaseContext(),"reserve check: " + builder,Toast.LENGTH_SHORT).show();
            out.close();
            return true;
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            return false;
        }
    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = "";
        if(0<day && day<10)    {
            day_string = "0" + day;
        }
        else    {
            day_string = Integer.toString(day);
        }

        String year_string = Integer.toString(year);
        reserveDate = year_string + "-" + month_string + "-" + day_string;

        busReserveFrag.getTv_date().setText("예약일시: " + reserveDate);
//        Toast.makeText(getBaseContext(),"Date: " + reserveDate + ", " + hour + "시 " + minute + "분",Toast.LENGTH_SHORT).show();
    }

    public void changeBottomButtons(int position)   {
        if(position == 1)  {
            confirmBtn.setText("돌아가기");
        }
        else    {
            confirmBtn.setText("취소");
        }
    }

}