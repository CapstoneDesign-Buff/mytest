package com.example.myapplication;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.groupsalgo.DateSpliter;
import com.example.myapplication.groupsalgo.Grouping;
import com.example.myapplication.groupsalgo.Point;
import com.example.myapplication.groupsalgo.Utils;
import com.github.vipulasri.timelineview.TimelineView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

public class MainFragment extends Fragment {

    private ListView listView;
    private TimeAxisAdapter mTimeAxisAdapter;
    private List<HashMap<String, Object>> list;
    private Vector<Grouping> busScheduler;
    private String currentDate;
    private ViewGroup rootView;
    private int groupId = 0;
    private boolean shouldRefreshOnResume = false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        TextView fragment_tv = (TextView)rootView.findViewById(R.id.round_tv);

        //get value from activity
        BusInfoActivity activity = (BusInfoActivity) getActivity();
        String myDataFromActivity = activity.getRoundInfo();

        //set text
        fragment_tv.setText(myDataFromActivity);

        //load data and create a groups

        Bundle bundle = getArguments();
        if(bundle != null) {
            int value = bundle.getInt("groupId");
            initView(value);
        }
        else    {
            initView(0);
        }

        return rootView;
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initView(int i) {
        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        listView = (ListView) rootView.findViewById(R.id.timelineview);
        listView.setDividerHeight(0);
//                 mTimeAxisAdapter = new TimeAxisAdapter(rootView.getContext(), getList());
        mTimeAxisAdapter = new TimeAxisAdapter(rootView.getContext(), getGroup(i));
        listView.setAdapter(mTimeAxisAdapter);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<HashMap<String, Object>> getGroup(int i) {
        List<HashMap<String, Object>> listChild = new ArrayList<HashMap<String, Object>>();
        Map<Integer, String> mapRoute = new HashMap<Integer, String>();

        boolean reserveDataExist = false;
        for(Grouping g : busScheduler)  {
            if(g.getDate().equals(currentDate)) {
                reserveDataExist = true;
//                return g.getGroups("Group" + i);
                for(Point p : g.getGroups("Group" + i)) {

                    int stStopIdx = p.getStPos();
                    int edStopIdx = p.getEdPos();

                    mapRoute.put(stStopIdx, Utils.transformTime(p.getDepTime()));  //stStop, departure time
                        // arrival stop and expected time
                    mapRoute.put(edStopIdx, "expected:" + Utils.transformTime(p.getExpArrivalTime()));
                }

                //add route.
                //sort by stop number

                Object[] mapkey = mapRoute.keySet().toArray();
                Vector<Integer> temp = new Vector<Integer>();
                for(Object object :mapkey	) {
                    temp.add((int)object);
                }

//                System.out.println("$$$before sort"+temp);
                temp.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1-o2;
                    }
                });

//                System.out.println("$$$after sort"+temp);

                for(int key : temp)    {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("content", Utils.getStopName(key)); //stpos
                    map.put("time", mapRoute.get(key));    //st time
                    listChild.add(map);
                }
            }
        }

        if(!reserveDataExist)   {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("content", "예약된 정보가 없습니다."); //stpos
            map.put("time", "00:00");    //st time
            listChild.add(map);
        }
        return listChild;
    }

//    public List<HashMap<String, Object>> getList() {
//        List<HashMap<String, Object>> listChild = new ArrayList<HashMap<String, Object>>();
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        map.put("content", "금정역");
//        map.put("time", "9:05");
//        listChild.add(map);
//
//        HashMap<String, Object> map1 = new HashMap<String, Object>();
//        map1.put("content", "천지사입구");
//        map1.put("time", "9:15");
//        listChild.add(map1);
//
//        HashMap<String, Object> map2 = new HashMap<String, Object>();
//        map2.put("content", "당동우체국");
//        map2.put("time", "9:25");
//        listChild.add(map2);
//
//        HashMap<String, Object> map3 = new HashMap<String, Object>();
//        map3.put("content", "삼성마을5단지");
//        map3.put("time", "9:30");
//        listChild.add(map3);
//
//        HashMap<String, Object> map4 = new HashMap<String, Object>();
//        map4.put("content", "서해아파트건너편");
//        map4.put("time", "9:45");
//        listChild.add(map4);
//
//        HashMap<String, Object> map5 = new HashMap<String, Object>();
//        map5.put("content", "팔곡주유소건너편");
//        map5.put("time", "9:55");
//        listChild.add(map5);
//
//        return listChild;
//    }

    void setBusScheduler(Vector<Grouping> busScheduler)  {
        this.busScheduler = busScheduler;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void refreshTimeLine(int i) {

        initView(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        // Check should we need to refresh the fragment
        if(shouldRefreshOnResume){
//             refresh fragment
            initView(0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }
}