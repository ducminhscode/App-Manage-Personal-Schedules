package com.example.applicationproject.View_Controller.Activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.applicationproject.Database.ToDoDBContract;
import com.example.applicationproject.Model.Category;
import com.example.applicationproject.Model.Mission;
import com.example.applicationproject.Model.User;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.ShareDataMission;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class StatisticActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ImageButton btn_back;
    private ImageView btn_left;
    private ImageView btn_right;
    private TextView tv_month;
    private com.github.mikephil.charting.charts.LineChart linebar;
    private LocalDate date;
    private ShareDataMission shareDataMission;
    private LineDataSet lineDataSet;
    private static final int EXISTING_MISIONS_LOADER = 0;
    private static final int EXISTING_CATEGORIES_LOADER = 1;
    private static final int EXISTING_USERS_LOADER = 2;
    private List<Mission> missionList;
    private List<Category> mcategoryList;
    private List<User> muserList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_layout_admin);

        // Initialize views and data
        initWidget();
//        getDataUser();
        shareDataMission = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ShareDataMission.class);
        mcategoryList = new ArrayList<>();
        muserList = new ArrayList<>();
        missionList = new ArrayList<>();

        getSupportLoaderManager().initLoader(EXISTING_CATEGORIES_LOADER, null, this);
        getSupportLoaderManager().initLoader(EXISTING_MISIONS_LOADER, null, this);
        getSupportLoaderManager().initLoader(EXISTING_USERS_LOADER, null, this);

        // Set the default date and update UI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = LocalDate.now();
            tv_month.setText(date.getMonth().toString());
        }

        // Set listeners for navigation buttons
        btn_back.setOnClickListener(v -> finish());
        btn_left.setOnClickListener(v -> handleLeftButtonClick());
        btn_right.setOnClickListener(v -> handleRightButtonClick());

        // Initially load the chart data for the current month
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            updateChartData(date);
        }
    }

    @SuppressLint("SetTextI18n")
    private void handleLeftButtonClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (date.getMonthValue() == 1) {
                return;
            } else {
                date = date.minusMonths(1);
                tv_month.setText(date.getMonth().toString());
                updateChartData(date); // Update the chart data for the previous month
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void handleRightButtonClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (date.getMonthValue() == 12) {
                return;
            } else {
                date = date.plusMonths(1);
                tv_month.setText(date.getMonth().toString());
                updateChartData(date); // Update the chart data for the next month
            }
        }
    }

////    private void getDataUser() {
////        List<User> userList = new ArrayList<>();
////        Cursor cursor = DAO.getCursorUser(this);
////        if (cursor != null) {
////            while (cursor.moveToNext()) {
////                String name = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_NAME));
////                String email = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_EMAIL));
////                String mobile = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_MOBILE));
////                String password = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_PASSWORD));
////                String role = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_ROLE));
////                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_ID));
////                User user = new User(mobile, email, userId, name, password, role);
////                userList.add(user);
////            }
////        }
////        shareDataMission.setUserList(userList);
//    }

    // Method to update chart data based on the given date
    private void updateChartData(LocalDate date) {
        ArrayList<Entry> entries = new ArrayList<>();
        List<User> users = new ArrayList<>();
        users.add(new User("0987654321", "user1@example.com", 1, "Nguyen Thi Lan", "password123", "user"));
        users.add(new User("0987654320", "user2@example.com", 2, "Nguyen Thi Mai", "password123", "user"));
        users.add(new User("0987654319", "user3@example.com", 3, "Nguyen Thi Hoa", "password123", "user"));
        List<Mission> missions = new ArrayList<>();
        missions.add(new Mission(1, 1, "2024-12-29", "Complete the project report", "true", "true", "daily", 1, "09:00", "Morning meeting", 1, "5", "10 minutes before", "time", "true", "true"));
        missions.add(new Mission(1, 1, "2024-11-29", "Complete the project report", "true", "true", "daily", 2, "09:00", "Morning meeting", 1, "5", "10 minutes before", "time", "true", "true"));
        missions.add(new Mission(1, 1, "2024-10-29", "Complete the project report", "true", "true", "daily", 3, "09:00", "Morning meeting", 2, "5", "10 minutes before", "time", "true", "true"));
        missions.add(new Mission(1, 1, "2024-09-29", "Complete the project report", "true", "true", "daily", 4, "09:00", "Morning meeting", 2, "5", "10 minutes before", "time", "true", "true"));
        missions.add(new Mission(1, 1, "2024-07-29", "Complete the project report", "true", "true", "daily", 5, "09:00", "Morning meeting", 3, "5", "10 minutes before", "time", "true", "true"));
        missions.add(new Mission(1, 1, "2024-06-29", "Complete the project report", "true", "true", "daily", 6, "09:00", "Morning meeting", 3, "5", "10 minutes before", "time", "true", "true"));
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Technology", 1));
        categories.add(new Category(2, "Math", 2));
        categories.add(new Category(3, "Literater", 3));

        // Observe user list and update chart once data is fetched
//        shareDataMission.getUserList().observe(this, users -> {
//            for (User user : users) {
//                List<Category> categoryList = Objects.requireNonNull(shareDataMission.getCategoryList().getValue()).stream()
//                        .filter(w -> w.getUser_id() == user.getUser_id())
//                        .collect(Collectors.toList());
//
//                for (Category category : categoryList) {
//                    int count = (int) Objects.requireNonNull(shareDataMission.getMainList().getValue()).stream()
//                            .filter(w -> w.getCategory_id() == category.getCategory_id() && w.getDate().equals(dates))
//                            .count();
//                    entries.add(new Entry(user.getUser_id(), count));
//                }
//            }
//
//            // Only update the chart if there is data
//            if (!entries.isEmpty()) {
//                // Create a new LineDataSet each time to ensure we don’t retain old data
//                lineDataSet = new LineDataSet(entries, "User");
//                lineDataSet.setColor(Color.BLUE);
//                lineDataSet.setValueTextColor(Color.BLACK);
//                lineDataSet.setValueTextSize(12f);
//
//                LineData lineData = new LineData(lineDataSet);
//                linebar.setData(lineData);
//                linebar.invalidate(); // Refresh the chart
//            }
//        });
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Month dates = date.getMonth();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (linebar != null) {
                for (User user : users) {
                    if (user != null) {
                        List<Category> categoryList = categories.stream()
                                .filter(w -> w.getUser_id() == user.getUser_id())
                                .collect(Collectors.toList());

                        for (Category category : categoryList) {
                            if (category != null) {
                                List<Mission> newones = missions.stream()
                                        .filter(w -> w.getCategory_id() == category.getCategory_id() && LocalDate.parse(w.getDate(), formatter).getMonth().equals(dates)).collect(Collectors.toList());
                                Log.e("count", "updateChartData: " + user.getUser_id() + " " + newones.size());
                                entries.add(new Entry(user.getUser_id(), newones.size()));
                            }
                        }
                    }
                }

                if (!entries.isEmpty()) {
                    lineDataSet = new LineDataSet(entries, "User");
                    lineDataSet.setColor(Color.BLUE);
                    lineDataSet.setValueTextColor(Color.BLACK);
                    lineDataSet.setValueTextSize(12f);

                    LineData lineData = new LineData(lineDataSet);
                    linebar.setData(lineData);
                    linebar.invalidate();
                }
            }
        }


    }

    private void initWidget() {
        btn_back = findViewById(R.id.btn_back);
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
        tv_month = findViewById(R.id.tv_month);
        linebar = findViewById(R.id.linebar);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch(id) {
            case EXISTING_MISIONS_LOADER:
                String[] projection = {
                        ToDoDBContract.MissionEntry.MISSION_ID,
                        ToDoDBContract.MissionEntry.MISSION_TITLE,
                        ToDoDBContract.MissionEntry.MISSION_DATE,
                        ToDoDBContract.MissionEntry.MISSION_TIME,
                        ToDoDBContract.MissionEntry.MISSION_isNOTIFY,
                        ToDoDBContract.MissionEntry.MISSION_isREPEAT,
                        ToDoDBContract.MissionEntry.MISSION_REPEAT_TYPE,
                        ToDoDBContract.MissionEntry.MISSION_REPEAT_NO,
                        ToDoDBContract.MissionEntry.MISSION_REMINDER,
                        ToDoDBContract.MissionEntry.MISSION_REMINDER_TYPE,
                        ToDoDBContract.MissionEntry.MISSION_RINGTONE_ID,
                        ToDoDBContract.MissionEntry.MISSION_DESCRIPTION,
                        ToDoDBContract.MissionEntry.MISSION_CATEGORY_ID,
                        ToDoDBContract.MissionEntry.MISSION_STICKER_ID,
                        ToDoDBContract.MissionEntry.MISSION_isSTICKER,
                        ToDoDBContract.MissionEntry.MISSION_isACTIVE
                };
                return new CursorLoader(this, ToDoDBContract.MissionEntry.CONTENT_URI, projection, null, null, null);
            case EXISTING_CATEGORIES_LOADER:
                String[] projection1 = {
                        ToDoDBContract.CategoryEntry.CATEGORY_USER_ID,
                        ToDoDBContract.CategoryEntry.CATEGORY_TITLE,
                        ToDoDBContract.CategoryEntry.CATEGORY_USER_ID
                };
                return new CursorLoader(this, ToDoDBContract.CategoryEntry.CONTENT_URI, projection1, null, null, null);
            case EXISTING_USERS_LOADER:
                String[] projection2 = {
                        ToDoDBContract.UserEntry.USER_ID,
                        ToDoDBContract.UserEntry.USER_NAME,
                        ToDoDBContract.UserEntry.USER_EMAIL,
                        ToDoDBContract.UserEntry.USER_MOBILE,
                        ToDoDBContract.UserEntry.USER_PASSWORD,
                        ToDoDBContract.UserEntry.USER_ROLE
                };
                return new CursorLoader(this, ToDoDBContract.UserEntry.CONTENT_URI, projection2, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case EXISTING_MISIONS_LOADER:
                if (data == null || data.getCount() < 1) {
                    return;
                }
                missionList = new ArrayList<>();
                if (data.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String isSticker = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_isSTICKER));
                        @SuppressLint("Range") int sticker_id = data.getInt(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_RINGTONE_ID));
                        @SuppressLint("Range") int category_id = data.getInt(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_CATEGORY_ID));
                        @SuppressLint("Range") int mission_id = data.getInt(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_ID));
                        @SuppressLint("Range") int ringTone_id = data.getInt(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_RINGTONE_ID));
                        @SuppressLint("Range") String date = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_DATE));
                        @SuppressLint("Range") String describe = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_DESCRIPTION));
                        @SuppressLint("Range") String isNotify = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_isNOTIFY));
                        @SuppressLint("Range") String isRepeat = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_isREPEAT));
                        @SuppressLint("Range") String repeatType = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_REPEAT_TYPE));
                        @SuppressLint("Range") String time = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_TIME));
                        @SuppressLint("Range") String title = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_TITLE));
                        @SuppressLint("Range") String repeatNo = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_REPEAT_NO));
                        @SuppressLint("Range") String reminder = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_REMINDER));
                        @SuppressLint("Range") String reminderType = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_REMINDER_TYPE));
                        @SuppressLint("Range") String isActive = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_isACTIVE));
                        Mission mission = new Mission(sticker_id, ringTone_id, date, describe, isNotify, isRepeat, repeatType, mission_id, time, title, category_id, repeatNo, reminder, reminderType, isSticker, isActive);
                        missionList.add(mission);
                    } while (data.moveToNext());
                }
                break;
            case EXISTING_CATEGORIES_LOADER:
                if (data == null || data.getCount() < 1) {
                    return;
                }
                mcategoryList = new ArrayList<>();
                while (data.moveToNext()) {
                    int id = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.CategoryEntry.CATEGORY_USER_ID));
                    String title = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.CategoryEntry.CATEGORY_TITLE));
                    int userId = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.CategoryEntry.CATEGORY_USER_ID));
                    mcategoryList.add(new Category(id, title, userId));
                }
                break;
            case EXISTING_USERS_LOADER:
                if (data == null || data.getCount() < 1) {
                    return;
                }
                muserList = new ArrayList<>();
                while (data.moveToNext()) {
                    int id = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_ID));
                    String name = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_NAME));
                    String email = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_EMAIL));
                    String mobile = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_MOBILE));
                    String password = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_PASSWORD));
                    String role = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_ROLE));
                    muserList.add(new User(mobile, email, id, name, password, role));
                }
            default:
                return;
        }
        if (mcategoryList != null){
            shareDataMission.setCategoryList(mcategoryList);
        }else{
            List<Category> categoryList = new ArrayList<>();
            shareDataMission.setCategoryList(categoryList);
        }
        if (missionList != null){
            shareDataMission.setMainList(missionList);
        }else{
            List<Mission> missionList1 = new ArrayList<>();
            shareDataMission.setMainList(missionList1);
        }
        if (muserList != null){
            shareDataMission.setUserList(muserList);
        }else{
            List<User> userList = new ArrayList<>();
            shareDataMission.setUserList(userList);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
