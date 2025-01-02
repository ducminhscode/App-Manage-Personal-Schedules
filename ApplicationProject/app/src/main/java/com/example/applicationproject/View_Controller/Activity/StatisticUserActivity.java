package com.example.applicationproject.View_Controller.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationproject.Model.Category;
import com.example.applicationproject.Model.Mission;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.FragmentLogin.MissionFragment;
import com.example.applicationproject.View_Controller.ShareDataMission;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticUserActivity extends AppCompatActivity {

    private PieChart pieChart;
    private ShareDataMission shareDataDialogs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_layout_user);
        shareDataDialogs = MissionFragment.shareDataDialogs;
        pieChart = findViewById(R.id.pieChart);
        setupPieChart();

    }

    private void setupPieChart() {
        // List of PieEntries (each PieEntry represents a slice of the pie)
        ArrayList<PieEntry> entries = new ArrayList<>();

        List<Mission> missions = shareDataDialogs.getMainList().getValue();
        if (missions == null) {
            missions = new ArrayList<>();
            missions.add(new Mission(1, 1, "2024-12-29", "Complete the project report", "true", "true", "daily", 1, "09:00", "Morning meeting", 1, "5", "10 minutes before", "true", "On"));
            missions.add(new Mission(1, 1, "2024-11-29", "Complete the project report", "true", "true", "daily", 2, "09:00", "Morning meeting", 1, "5", "10 minutes before", "true", "On"));
            missions.add(new Mission(1, 1, "2024-10-29", "Complete the project report", "true", "true", "daily", 3, "09:00", "Morning meeting", 2, "5", "10 minutes before", "true", "On"));
            missions.add(new Mission(1, 1, "2024-09-29", "Complete the project report", "true", "true", "daily", 4, "09:00", "Morning meeting", 2, "5", "10 minutes before", "true", "On"));
            missions.add(new Mission(1, 1, "2024-07-29", "Complete the project report", "true", "true", "daily", 5, "09:00", "Morning meeting", 3, "5", "10 minutes before", "true", "On"));
            missions.add(new Mission(1, 1, "2024-06-29", "Complete the project report", "true", "true", "daily", 6, "09:00", "Morning meeting", 3, "5", "10 minutes before", "true", "On"));

        }

        List<Category> categories = shareDataDialogs.getCategoryList().getValue();
        if (categories == null) {
            categories = new ArrayList<>();
            categories.add(new Category(1, "Technology", 1));
            categories.add(new Category(2, "Math", 2));
            categories.add(new Category(3, "Literater", 3));
        }

        for (Category category : categories) {
            int count = missions.stream().filter(mission -> mission.getCategory_id() == category.getCategory_id()).collect(Collectors.toList()).size();
            entries.add(new PieEntry((float)count,(float) category.getCategory_id()));
        }


        // Create a PieDataSet from the entries
        PieDataSet dataSet = new PieDataSet(entries, "Tasks by Category");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Set color for the slices
        dataSet.setValueTextSize(12f); // Set text size for values
        dataSet.setValueTextColor(android.graphics.Color.BLACK); // Set text color for values

        // Set PieData to PieChart
        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        // Additional settings
        pieChart.invalidate(); // Refresh the chart
        pieChart.setCenterText("Tasks by Category"); // Set center text for the pie chart
        pieChart.setCenterTextSize(16f); // Set size for the center text
    }

}
