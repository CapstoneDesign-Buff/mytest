package com.example.myapplication;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.groupsalgo.DateSpliter;
import com.example.myapplication.groupsalgo.Grouping;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

public class BusInfoActivity extends FragmentActivity {
    TabLayout tabs;
    String roundInfo = "1회차";
    Vector<MainFragment> vecFragments;
    String user_id;
    int page = 0;
    int MAX_ROUND = 5;
    private Vector<Grouping> busScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info);



        vecFragments = new Vector<>();

        tabs = findViewById(R.id.tabs);

        Intent mIntent = getIntent();
        user_id = mIntent.getStringExtra("user_id");
        TextView userName = (TextView) findViewById(R.id.user_id_box);
        userName.setText(user_id);


        initBusRouteInfo();

        Button refreshBtn = (Button)findViewById(R.id.refresh);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGroups();
//                for(int i = 0 ; i < MAX_ROUND; i++) {
//                    MainFragment fragment = new MainFragment();
//                    fragment.setBusScheduler(busScheduler);
//
//                    vecFragments.add(fragment);
////                    tabs.addTab(tabs.newTab().setText( (i+1) + "회차"));
//                }
                int position = tabs.getSelectedTabPosition();
                MainFragment selected = vecFragments.get(position-1);
                selected.setBusScheduler(busScheduler);
                selected.refreshTimeLine(position-1);

                finish();
                startActivity(getIntent());
//                getSupportFragmentManager().beginTransaction().detach(selected).attach(selected).commit();

//
//                Bundle bundle = new Bundle();
//                bundle.putInt("groupId", position-1);
//                // set Fragmentclass Arguments
//                selected.setArguments(bundle);
//
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }
        });

        Button reserveBtn = findViewById(R.id.bus_reserve_ractivity);
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "[예약하기 페이지]준비중", Toast.LENGTH_SHORT).show();
                openBusReserveActivity(0);
            }
        });

        Button checkBtn = findViewById(R.id.bus_confirm);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBusReserveActivity(1);
//                Toast.makeText(getApplicationContext(), "[예약 확인 페이지]준비중", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initBusRouteInfo()  {
        tabs.addTab(tabs.newTab().setText("◀"));

        Grouping currentDateGroup = loadGroups();
        MAX_ROUND = currentDateGroup.getCustomGroup().size();

        for(int i = 0 ; i < MAX_ROUND; i++) {
            MainFragment fragment = new MainFragment();
            fragment.setBusScheduler(busScheduler);

            vecFragments.add(fragment);
            tabs.addTab(tabs.newTab().setText( (i+1) + "회차"));
        }

        tabs.addTab(tabs.newTab().setText("▶"));
        tabs.selectTab(tabs.getTabAt(1));
        getSupportFragmentManager().beginTransaction().add(R.id.container, vecFragments.get(0)).commit();

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                MainFragment selected = null;
                int groupId = 0;
                if(position == 0)   {
                    tabs.selectTab(tabs.getTabAt(1));
//                    roundInfo = (page * 5 + 1) + "회차";
                    groupId = page * 5;
                    selected = vecFragments.get(groupId);
                }
                else if(position == MAX_ROUND+1)   {
                    tabs.selectTab(tabs.getTabAt(1));
                    groupId = page * 5;
                    selected = vecFragments.get(groupId);
                }
                else if( 0 < position && position <= MAX_ROUND )  {
                    roundInfo = (page * 5 + position) + "회차";
                    groupId = page * 5 + (position-1);
                    selected = vecFragments.get(groupId);
                }
                else    {
                    selected = vecFragments.get(page * 5);
                }


                Bundle bundle = new Bundle();
                bundle.putInt("groupId", groupId);
                // set Fragmentclass Arguments
                selected.setArguments(bundle);


                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }


    Grouping loadGroups()    {
        String path = getFilesDir() + "demo_dataset.csv";
        DateSpliter ds = new DateSpliter(path);
//		2021-10-17, 333
//		2021-10-15, 324
//		2021-10-16, 343
        busScheduler = new Vector<Grouping>();
        for(String dateKery : ds.getPointsByDate().keySet())	{
            System.out.println(dateKery + ", " + ds.getPointsByDate().get(dateKery).size());
            Grouping temp = new Grouping(ds.getPointsByDate().get(dateKery));

            busScheduler.add(temp);
        }


            //check and print correctly grouping
//        for(Grouping g : busScheduler)	{
////            System.out.println("%" + g.getDate() + "%");
//            for(int i = 0; i < g.getCustomGroup().size(); i++)	{
//                String key = "Group" + i;
//                String builder="";
////                System.out.print((i+1)+"회차:");
//                builder += (i+1)+"회차:";
//                //print groups
//                System.out.println(g.getGroups(key));
//                System.out.println("\t-총 승객수: " + g.getGroups(key).size() + "명");
//                System.out.println("\t-운행 시간: " + g.predictBusRunTime(key));
//                //print path
//                System.out.println("\t-버스 운행경로: " + g.getPath(key)+"\n");
//
//                builder += g.getGroups(key);
//                builder += "\t-총 승객수: " + g.getGroups(key).size() + "명\n";
//                builder += "\t-운행 시간: " + g.predictBusRunTime(key);
//                builder += "\t-버스 운행경로: " + g.getPath(key)+"\n";
//
//                Toast.makeText(getApplicationContext(), builder, Toast.LENGTH_SHORT).show();
//            }
//        }

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        for(Grouping g : busScheduler) {
            if (g.getDate().equals(currentDate)) {
                return g;
            }
        }
        return null;
    }


    public String getRoundInfo() {
        return roundInfo;
    }


    public void openBusReserveActivity(int index){
        Intent intent = new Intent(this, ReserveActivity.class);
        intent.putExtra("tabIndex", index);
        intent.putExtra("user_id", user_id);

        startActivity(intent);
    }


}
