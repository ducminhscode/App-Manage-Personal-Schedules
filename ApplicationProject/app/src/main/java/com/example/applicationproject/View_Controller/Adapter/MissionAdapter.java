package com.example.applicationproject.View_Controller.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.Database.ToDoDBHelper;
import com.example.applicationproject.Model.Mission;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.Activity.DetailMissionActivity;
import com.google.android.material.internal.TextDrawableHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.MissionViewHolder> implements Filterable {


    private Context mContext;
    private List<Mission> missionList;
    private List<Mission> missionListOld;
    private ToDoDBHelper toDoDBHelper;

    public MissionAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMissionList(List<Mission> missionList) {
        this.missionList = missionList;
        this.missionListOld = missionList;
        notifyDataSetChanged();
    }

    public static class MissionViewHolder extends RecyclerView.ViewHolder {

        private CardView cdItem;
        private ImageView type, ringRing, repeat;
        private TextView title, dateTime, oclock, describe, letter;
        private RelativeLayout relativeLayout;
        public LinearLayout layout_foreground;

        public MissionViewHolder(@NonNull View itemView) {
            super(itemView);
            cdItem = itemView.findViewById(R.id.cdItem);
            letter = itemView.findViewById(R.id.letter);
            type = itemView.findViewById(R.id.type);
            ringRing = itemView.findViewById(R.id.ringRing);
            describe = itemView.findViewById(R.id.describe);
            title = itemView.findViewById(R.id.title);
            dateTime = itemView.findViewById(R.id.dateTime);
            oclock = itemView.findViewById(R.id.oclock);
            repeat = itemView.findViewById(R.id.repeat);
            relativeLayout = itemView.findViewById(R.id.relative_item);
            layout_foreground = itemView.findViewById(R.id.layout_foreground);
        }

    }

    @NonNull
    @Override
    public MissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_item, parent, false);
        return new MissionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MissionViewHolder holder, int position) {
        Mission mission = missionList.get(position);
        holder.relativeLayout.setAlpha(mission.getIsActive().equals("On") ? 1 : 0.5f);
        holder.relativeLayout.setOnClickListener(view -> {
            Intent intent = new Intent(this.mContext, DetailMissionActivity.class);
            intent.putExtra("id_mission", mission.getMission_id());
            this.mContext.startActivity(intent);
        });

        if (mission.getIsSticker().equals("True")){
            holder.type.setVisibility(View.VISIBLE);
            holder.cdItem.setVisibility(View.GONE);
        }else{
            String letter = "A";

            if(mission.getTitle() != null && !mission.getTitle().isEmpty()) {
                letter = mission.getTitle().substring(0, 1);
            }

            Random random = new Random();
            int randomColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            holder.letter.setTextColor(randomColor);
            holder.type.setVisibility(View.GONE);
        }
        if (mission.getIsNotify().equals("True")) {
            holder.ringRing.setVisibility(View.VISIBLE);
        }else{
            holder.ringRing.setVisibility(View.GONE);
        }
        holder.title.setText(mission.getTitle());
        holder.dateTime.setText(mission.getDate());
        holder.oclock.setText(mission.getTime());
        if (mission.getIsRepeat().equals("True")) {
            holder.repeat.setVisibility(View.VISIBLE);
        }else{
            holder.repeat.setVisibility(View.GONE);
        }
        holder.describe.setText(mission.getDescribe());

    }

    @Override
    public int getItemCount() {
        if(missionList != null){
            return missionList.size();
        }
        return 0;
    }

    private String filterType;

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String strSearch = charSequence.toString();
                if(strSearch.isEmpty()){
                    missionList = missionListOld;
                }else{

                    List<Mission> list = new ArrayList<>();
                    for(Mission mission : missionListOld){
                        switch (filterType){
                            case "Title":
                                if(mission.getTitle().toLowerCase().contains(strSearch.toLowerCase())){
                                    list.add(mission);
                                }
                                break;
                            case "DateTime":
                                if(mission.getDate().toLowerCase().contains(strSearch.toLowerCase())){
                                    list.add(mission);
                                }
                                break;
                            case "category_id":
                                if(mission.getCategory_id() == Integer.parseInt(strSearch)){
                                    list.add(mission);
                                }
                                break;
                            case "Active":
                                if(mission.getIsActive().equals(strSearch)){
                                    list.add(mission);
                                }
                                break;
                        }
                    }

                    missionList = list;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = missionList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                missionList = (List<Mission>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void removeItem(int position) {
        missionList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Mission mission, int position) {
        missionList.add(position, mission);
        notifyItemInserted(position);
    }
}
