package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class BusReserveConfirmFragment extends Fragment {
    String user_id;

    public BusReserveConfirmFragment() {
        this("unknown");
    }

    public BusReserveConfirmFragment(String user_id)   {
        this.user_id = user_id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_bus_reserve_confirm, container, false);

        TextView fragment_tv = (TextView)rootView.findViewById(R.id.round_tv);

        //get value from activity
        ReserveActivity activity = (ReserveActivity) getActivity();

        ArrayList<ItemModel> itemsList = new ArrayList<>();
        ListView list = (ListView) rootView.findViewById(R.id.listview);

        // data read.
        Context c = container.getContext();
        itemsList = sortAndAddSections(readFromFile());

        ListAdapter adapter = new ListAdapter(c, itemsList);
        list.setAdapter(adapter);

        return rootView;
    }

    private ArrayList sortAndAddSections(ArrayList<ItemModel> itemList)
    {

        ArrayList<ItemModel> tempList = new ArrayList<>();
        //First we sort the array
        if( itemList != null) {
            Collections.sort(itemList);
        }

        //Loops thorugh the list and add a section before each sectioncell start
        String header = "";
        for(int i = 0; i < itemList.size(); i++)
        {
            //If it is the start of a new section we create a new listcell and add it to our array
            if(!(header.equals(itemList.get(i).getDate()))) {
                ItemModel sectionCell = new ItemModel(null, null,null,itemList.get(i).getDate());
                sectionCell.setToSectionHeader();
                tempList.add(sectionCell);
                header = itemList.get(i).getDate();
            }
            tempList.add(itemList.get(i));
        }

        return tempList;
    }


    public class ListAdapter extends ArrayAdapter {

        LayoutInflater inflater;
        public ListAdapter(Context context, ArrayList items) {
            super(context, 0, items);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ItemModel cell = (ItemModel) getItem(position);

            //If the cell is a section header we inflate the header layout
            if(cell.isSectionHeader())
            {
                v = inflater.inflate(R.layout.section_header, null);

                v.setClickable(false);

                TextView header = (TextView) v.findViewById(R.id.section_header);
                header.setText(cell.getDate());
            }
            else
            {
                v = inflater.inflate(R.layout.row_item, null);
                TextView time_time = (TextView) v.findViewById(R.id.time_time);
                TextView tv_item_sysdia = (TextView) v.findViewById(R.id.tv_item_sysdia);

                TextView tv_item_plus = (TextView) v.findViewById(R.id.tv_item_plus);

                time_time.setText(cell.getDeptPos());
                tv_item_sysdia.setText(cell.getDeptTime());
                tv_item_plus.setText(cell.getArrivalPos());


            }
            return v;
        }
    }


    // sample data
//    private ArrayList<ItemModel>  getItems(){
//        ArrayList<ItemModel> items = new ArrayList<>();
//        items.add(new ItemModel("금정역", "군포시평생학습원","9:20","31 Oct 17"));
//        items.add(new ItemModel("삼성마을5단지", "팔곡마을주공아파트","13:20","31 Oct 17"));
//        items.add(new ItemModel("군포중학교", "비봉중고등학교","15:40","Tue,31 Oct 17"));
//        items.add(new ItemModel("군포G샘병원", "도금단지","10:15","29 Oct 17"));
//        items.add(new ItemModel("남양읍행정복지센터", "마도교차로","12:50","29 Oct 17"));
//        return  items;
//    }


    @SuppressLint("LongLogTag")
    private ArrayList<ItemModel> readFromFile() {

        String ret = "";
        ArrayList<ItemModel> items = new ArrayList<>();

        try {
            FileReader reader = new FileReader(getContext().getFilesDir() + "reserve_"+user_id+".txt");

            if ( reader != null ) {
                BufferedReader bufferedReader = new BufferedReader(reader);
                String receiveString = "";

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    //items.add(new ItemModel("금정역", "군포시평생학습원","9:20","31 Oct 17"));
                    String[] data = receiveString.split(",");

                    //ex data) "date@#@" +reserveDate+","
                    String stName = "";
                    String edName = "";
                    String time = "";
                    String date = "";

                    for(String datum : data)  {
                        String key = datum.split("@#@")[0];
                        String val = datum.split("@#@")[1];

                        switch(key)    {
                            case "date":
                                date = val;
                                break;

                            case "stSTOP":
                                stName = val;
                                break;

                            case "edSTOP":
                                edName = val;
                                break;

                            case "time":
                                time = val;
                                break;
                        }
                    }

                    //check prevous day reservation => skip to display
                    if(!isPrevousReserve(date)) continue;



                    items.add(new ItemModel(stName, edName,time,date));
                }

                bufferedReader.close();
                reader.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("BusReserveConfirmFragment", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("BusReserveConfirmFragment", "Can not read file: " + e.toString());
        } catch (ParseException e) {
            Log.e("BusReserveConfirmFragment", "date pattern is not available " + e.toString());
        }

        return items;
    }


    public boolean isPrevousReserve(String reserveDate) throws ParseException {
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Date today = sdformat.parse(todayStr);
        Date resDay = sdformat.parse(reserveDate);

//        if(today.compareTo(resDay) > 0) {
////            System.out.println("Date 1 occurs after Date 2");
//
//        } else
        if(today.compareTo(resDay) > 0) {
//            System.out.println("Date 1 occurs before Date 2");

            //bus reserve before today
            return false;

        }
//        } else if(d1.compareTo(d2) == 0) {
//            System.out.println("Both dates are equal");
//        }
        return true;
    }
}


