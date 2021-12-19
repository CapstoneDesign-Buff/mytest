//package com.example.myapplication;
//
//import android.content.Context;
//import android.os.Build;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.RequiresApi;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.github.vipulasri.timelineview.TimelineView;
//
//import java.util.List;
//
//class TimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private List<TimeLineModel> timeLineModelList;
//    private Context context;
//
//    TimeLineAdapter(Context context, List<TimeLineModel> timeLineModelList) {
//        this.timeLineModelList = timeLineModelList;
//        this.context = context;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main, parent, false);
//        return new RecyclerView.ViewHolder(view, viewType);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        ((ViewHolder)holder).textView.setText(timeLineModelList.get(position).getName());
//        ((RecyclerView.ViewHolder) holder).textViewDescription.setText(timeLineModelList.get(position).getDescription());
//        ((RecyclerView.ViewHolder)holder).textViewTime.setText(timeLineModelList.get(position).getTime());
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return TimelineView.getTimeLineViewType(position, getItemCount());
//    }