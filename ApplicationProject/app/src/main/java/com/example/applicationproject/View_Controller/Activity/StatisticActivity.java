package com.example.applicationproject.View_Controller.Activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.applicationproject.Database.ToDoDBContract;
import com.example.applicationproject.Model.Category;
import com.example.applicationproject.Model.User;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.ShareDataMission;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.time.LocalDate;
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_layout_admin);

        // Initialize views and data
        initWidget();
        getDataUser();

        // Set the default date and update UI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = LocalDate.now();
            tv_month.setText("Tháng " + date.getMonth().toString());
        }

        // Set listeners for navigation buttons
        btn_back.setOnClickListener(v -> finish());
        btn_left.setOnClickListener(v -> handleLeftButtonClick());
        btn_right.setOnClickListener(v -> handleRightButtonClick());

        // Initially load the chart data for the current month
        updateChartData(date);
    }

    @SuppressLint("SetTextI18n")
    private void handleLeftButtonClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (date.getMonthValue() == 1) {
                btn_left.setVisibility(View.GONE);
            } else {
                btn_left.setVisibility(View.VISIBLE);
                date = date.minusMonths(1);
                tv_month.setText("Tháng " + date.getMonth().toString());
                updateChartData(date); // Update the chart data for the previous month
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void handleRightButtonClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (date.getMonthValue() == 12) {
                btn_right.setVisibility(View.GONE);
            } else {
                btn_right.setVisibility(View.VISIBLE);
                date = date.plusMonths(1);
                tv_month.setText("Tháng " + date.getMonth().toString());
                updateChartData(date); // Update the chart data for the next month
            }
        }
    }

    private void getDataUser() {
        List<User> userList = new ArrayList<>();
        Cursor cursor = DAO.getCursorUser(this);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_EMAIL));
                String mobile = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_MOBILE));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_PASSWORD));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_ROLE));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDBContract.UserEntry.USER_ID));
                User user = new User(mobile, email, userId, name, password, role);
                userList.add(user);
            }
        }
        shareDataMission.setUserList(userList);
    }

    // Method to update chart data based on the given date
    private void updateChartData(LocalDate date) {
        String dates;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dates = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            dates = null;
        }
        ArrayList<Entry> entries = new ArrayList<>();

        // Observe user list and update chart once data is fetched
        shareDataMission.getUserList().observe(this, users -> {
            for (User user : users) {
                List<Category> categoryList = Objects.requireNonNull(shareDataMission.getCategoryList().getValue()).stream()
                        .filter(w -> w.getUser_id() == user.getUser_id())
                        .collect(Collectors.toList());

                for (Category category : categoryList) {
                    int count = (int) Objects.requireNonNull(shareDataMission.getMainList().getValue()).stream()
                            .filter(w -> w.getCategory_id() == category.getCategory_id() && w.getDate().equals(dates))
                            .count();
                    entries.add(new Entry(user.getUser_id(), count));
                }
            }

            // Only update the chart if there is data
            if (!entries.isEmpty()) {
                // Create a new LineDataSet each time to ensure we don’t retain old data
                lineDataSet = new LineDataSet(entries, "Sample Data");
                lineDataSet.setColor(Color.BLUE);
                lineDataSet.setValueTextColor(Color.BLACK);
                lineDataSet.setValueTextSize(12f);

                LineData lineData = new LineData(lineDataSet);
                linebar.setData(lineData);
                linebar.invalidate(); // Refresh the chart
            }
        });
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
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
