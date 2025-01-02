package com.example.applicationproject.View_Controller.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.Database.ToDoDBContract;
import com.example.applicationproject.Model.Category;
import com.example.applicationproject.Model.Mission;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.Adapter.MissionAdapter;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.FragmentLogin.MissionFragment;
import com.example.applicationproject.View_Controller.ScreenMainLogin;
import com.example.applicationproject.View_Controller.ShareDataMission;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HistoryActivity extends AppCompatActivity{

    private ImageButton btnBack, btnDelete;
    private RecyclerView recyclerView;
    private MissionAdapter historyAdapter;
    private TextView emptyViewItem;
    private ShareDataMission shareDataMission;
    private static final int EXISTING_CATEGORIES_LOADER = 1;
    private static final int EXISTING_MISIONS_LOADER = 0;
    private int user;
    private List<Mission> missionList1;
    private String currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        initwitget();
        Intent intent = getIntent();
        currentUser = intent.getStringExtra("name");
        shareDataMission = MissionFragment.shareDataDialogs;
//        Intent intent = getIntent();
//        String username = intent.getStringExtra("name");
//        user = DAO.getUserId(this.getBaseContext(), username);
//        getSupportLoaderManager().initLoader(EXISTING_CATEGORIES_LOADER, null, this);
//        getSupportLoaderManager().initLoader(EXISTING_MISIONS_LOADER, null, this);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        historyAdapter= new MissionAdapter(this);
        recyclerView.setAdapter(historyAdapter);
        List<Mission> filterMission = shareDataMission.getMainList().getValue();
        if (filterMission == null) {
            filterMission = new ArrayList<>();
        }else{
            filterMission = filterMission.stream().filter(mission -> mission.getIsActive().equals("Off")).collect(Collectors.toList());
        }
        shareDataMission.setMainList(filterMission);
        shareDataMission.getMainList().observe(this, missions -> {
            if (!missions.isEmpty()) {
                historyAdapter.setMissionList(missions);
                recyclerView.setVisibility(View.VISIBLE);
                emptyViewItem.setVisibility(View.GONE);
            } else {
                historyAdapter.setMissionList(new ArrayList<>());
                recyclerView.setVisibility(View.GONE);
                emptyViewItem.setVisibility(View.VISIBLE);
            }
        });
        
        btnDelete.setOnClickListener(view -> {
            if (shareDataMission.getMainList().getValue() == null){
                return;
            }
            for (Mission mission : Objects.requireNonNull(shareDataMission.getMainList().getValue())) {
                if (mission.getIsActive().equals("Off")) {
                    boolean isDeleted = DAO.deleteMission(this, mission.getMission_id(), currentUser);
                    missionList1.remove(mission);
                    historyAdapter.setMissionList(missionList1);
                    if (isDeleted) {
                        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initwitget() {
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        recyclerView = findViewById(R.id.recyclerViewCalendar);
        emptyViewItem = findViewById(R.id.emptyView);
    }
    

//    @NonNull
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
//        if (id == EXISTING_CATEGORIES_LOADER) {
//            String[] projection = {
//                    ToDoDBContract.CategoryEntry.CATEGORY_ID,
//                    ToDoDBContract.CategoryEntry.CATEGORY_TITLE,
//                    ToDoDBContract.CategoryEntry.CATEGORY_USER_ID
//            };
//            return new CursorLoader(this, ToDoDBContract.CategoryEntry.CONTENT_URI, projection, null, null, null);
//        }else if (id == EXISTING_MISIONS_LOADER){
//            String[] projection = {
//                    ToDoDBContract.MissionEntry.MISSION_ID,
//                    ToDoDBContract.MissionEntry.MISSION_TITLE,
//                    ToDoDBContract.MissionEntry.MISSION_DATE,
//                    ToDoDBContract.MissionEntry.MISSION_TIME,
//                    ToDoDBContract.MissionEntry.MISSION_isNOTIFY,
//                    ToDoDBContract.MissionEntry.MISSION_isREPEAT,
//                    ToDoDBContract.MissionEntry.MISSION_REPEAT_TYPE,
//                    ToDoDBContract.MissionEntry.MISSION_REPEAT_NO,
//                    ToDoDBContract.MissionEntry.MISSION_REMINDER_TYPE,
//                    ToDoDBContract.MissionEntry.MISSION_RINGTONE_ID,
//                    ToDoDBContract.MissionEntry.MISSION_DESCRIPTION,
//                    ToDoDBContract.MissionEntry.MISSION_CATEGORY_ID,
//                    ToDoDBContract.MissionEntry.MISSION_STICKER_ID,
//                    ToDoDBContract.MissionEntry.MISSION_isSTICKER,
//                    ToDoDBContract.MissionEntry.MISSION_isACTIVE
//            };
//            return new CursorLoader(this, ToDoDBContract.MissionEntry.CONTENT_URI, projection, null, null, null);
//        }else{
//            return null;
//        }
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    @Override
//    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
//        List<Category> mcategoryList = null;
//        List<Mission> missionList = null;
//        switch (loader.getId()) {
//            case EXISTING_MISIONS_LOADER:
//                if (data == null || data.getCount() < 1) {
//                    return;
//                }
//                missionList = new ArrayList<>();
//                if (data.moveToFirst()) {
//                    do {
//                        @SuppressLint("Range") String isSticker = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_isSTICKER));
//                        @SuppressLint("Range") int sticker_id = data.getInt(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_RINGTONE_ID));
//                        @SuppressLint("Range") int category_id = data.getInt(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_CATEGORY_ID));
//                        @SuppressLint("Range") int mission_id = data.getInt(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_ID));
//                        @SuppressLint("Range") int ringTone_id = data.getInt(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_RINGTONE_ID));
//                        @SuppressLint("Range") String date = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_DATE));
//                        @SuppressLint("Range") String describe = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_DESCRIPTION));
//                        @SuppressLint("Range") String isNotify = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_isNOTIFY));
//                        @SuppressLint("Range") String isRepeat = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_isREPEAT));
//                        @SuppressLint("Range") String repeatType = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_REPEAT_TYPE));
//                        @SuppressLint("Range") String time = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_TIME));
//                        @SuppressLint("Range") String title = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_TITLE));
//                        @SuppressLint("Range") String repeatNo = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_REPEAT_NO));
//                        @SuppressLint("Range") String reminderType = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_REMINDER_TYPE));
//                        @SuppressLint("Range") String isActive = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_isACTIVE));
//                        Mission mission = new Mission(sticker_id, ringTone_id, date, describe, isNotify, isRepeat, repeatType, mission_id, time, title, category_id, repeatNo, reminderType, isSticker, isActive);
//                        missionList.add(mission);
//                    } while (data.moveToNext());
//                }
//                break;
//            case EXISTING_CATEGORIES_LOADER:
//                if (data == null || data.getCount() < 1) {
//                    return;
//                }
//                mcategoryList = new ArrayList<>();
//                while (data.moveToNext()) {
//                    int id = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.CategoryEntry.CATEGORY_ID));
//                    String title = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.CategoryEntry.CATEGORY_TITLE));
//                    int userId = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.CategoryEntry.CATEGORY_USER_ID));
//                    mcategoryList.add(new Category(id, title, userId));
//                }
//                break;
//        }
//        if (mcategoryList != null) {
//            mcategoryList = mcategoryList.stream().filter(category -> category.getUser_id() == user).collect(Collectors.toList());
////            Log.e("checkcolumn1", "onLoadFinished: categoyList" + mcategoryList.get(0).getCategory_name());
//        } else {
//            mcategoryList = new ArrayList<>();
//        }
//        if (missionList == null) {
//            missionList = new ArrayList<>();
//        }else if (!missionList.isEmpty() && !mcategoryList.isEmpty()) {
//            List<Mission> mainList = new ArrayList<>();
//            for (Category category : mcategoryList) {
//                mainList.addAll(missionList.stream().filter(mission -> category.getCategory_id() == mission.getCategory_id()).collect(Collectors.toList()));
//            }
//            missionList1 = mainList;
//            Log.e("checkcolumn2", "onLoadFinished: " + mainList.size());
//        } else {
//            missionList1 = new ArrayList<>();
//        }
//        historyAdapter.setMissionList(missionList1);
//    }
//
//    @Override
//    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
