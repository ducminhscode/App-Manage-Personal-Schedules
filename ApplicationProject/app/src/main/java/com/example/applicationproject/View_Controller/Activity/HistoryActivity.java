package com.example.applicationproject.View_Controller.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.Model.Mission;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.Adapter.MissionAdapter;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.ShareDataMission;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ImageButton btnBack, btnDelete;
    private RecyclerView recyclerView;
    private ShareDataMission shareDataMission;
    private MissionAdapter historyAdapter;
    private TextView emptyViewItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareDataMission = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ShareDataMission.class);
        initwiget();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initwiget() {
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        recyclerView = findViewById(R.id.recyclerViewCalendar);
        emptyViewItem = findViewById(R.id.emptyViewItem);
        btnBack.setOnClickListener(v -> finish());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        historyAdapter= new MissionAdapter(this);
        recyclerView.setAdapter(historyAdapter);
        List<Mission> missionList = new ArrayList<>();
        shareDataMission.getMainList().observe(this, missions -> {
            if (missions != null) {
                missionList.clear();
                missionList.addAll(missions);
                checkEmptyData();
            }else{
                missionList.clear();
                checkEmptyData();
            }
        });
        historyAdapter.setMissionList(missionList);
        historyAdapter.setFilterType("Active");
        historyAdapter.getFilter().filter("Off");

        btnDelete.setOnClickListener(view -> {
            for (Mission mission : missionList) {
                if (mission.getIsActive().equals("Off")) {
                    boolean isDeleted = DAO.deleteMission(this, mission.getMission_id());
                    if (isDeleted) {
                        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                    missionList.remove(mission);
                    historyAdapter.notifyDataSetChanged();
                }
            }
            shareDataMission.setMainList(missionList);
        });

    }

    private void checkEmptyData() {
        if (historyAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyViewItem.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyViewItem.setVisibility(View.GONE);
        }
    }

}
