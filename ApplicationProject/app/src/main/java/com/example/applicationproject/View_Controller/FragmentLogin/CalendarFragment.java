package com.example.applicationproject.View_Controller.FragmentLogin;

import static androidx.core.app.ActivityCompat.recreate;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applicationproject.Model.Mission;
import com.example.applicationproject.View_Controller.Adapter.MissionAdapter;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.ShareDataMission;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

//    private static final Log log = LogFactory.getLog(CalendarFragment.class);
    private LocalTime localTime;
    private LocalDate localDate;
    private List<Mission> mainList;
    private CalendarView calendarView;
    private boolean isCalendarVisible = true;
    private MenuItem visibleItem;
    private LinearLayout layoutCalendar;
    private RecyclerView rVC;
    private MissionAdapter calendarAdapter;
    private TextView emptyViewItem;
    private ShareDataMission shareDataMission;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        rVC = view.findViewById(R.id.recyclerViewCalendar);
        layoutCalendar = view.findViewById(R.id.layoutCalendar);
        emptyViewItem = view.findViewById(R.id.emptyViewItem);

        shareDataMission = new ViewModelProvider(this).get(ShareDataMission.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localDate = LocalDate.now();
            localTime = LocalTime.now();
        }

        shareDataMission.setDataOrdering(2);
        shareDataMission.setDataChangeLanguage(2);

        MissionFragment missionFragment = new MissionFragment();
        calendarAdapter = new MissionAdapter(requireContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        rVC.setLayoutManager(linearLayoutManager);
        mainList = new ArrayList<>();
        shareDataMission.getMainList().observe(getViewLifecycleOwner(), missons -> {
            if (missons != null) {
                mainList.clear();
                mainList.addAll(missons);
            } else {
                mainList.clear();
            }
            calendarAdapter.setMissionList(mainList);
        });
        rVC.setAdapter(calendarAdapter);

        setHasOptionsMenu(true);

        checkEmptyData();

        calendarView = view.findViewById(R.id.calendarViewCalendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                //Xử lý sự kiện lọc danh sách khi chọn các ngày trong lịch
                String selectedDate = i2 + "-" + (i1 + 1) + "-" + i;
                calendarAdapter.setFilterType("DateTime");
                calendarAdapter.getFilter().filter(selectedDate);
            }
        });

        return view;
    }

    //Kiểm tra dữ liệu có rỗng
    private void checkEmptyData() {
        if (calendarAdapter.getItemCount() == 0) {
            rVC.setVisibility(View.GONE);
            emptyViewItem.setVisibility(View.VISIBLE);
        } else {
            rVC.setVisibility(View.VISIBLE);
            emptyViewItem.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        //Tạo menu cho toolbar
        inflater.inflate(R.menu.calendar_layout, menu);
        visibleItem = menu.findItem(R.id.visible);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sapXep) {
            showdialogOrder();
            return true;
        } else if (item.getItemId() == R.id.Calendar_language) {
            showdialogChangelanguage();
            return true;
        } else if (item.getItemId() == R.id.visible) {

            //Xử lý ẩn hiện lịch
            if (isCalendarVisible) {
                layoutCalendar.setVisibility(View.GONE);
                visibleItem.setIcon(R.drawable.up_arrow);
                int actionBarSize = getActionBarSize();
                setRecyclerViewMargin(actionBarSize + 25);
            } else {
                layoutCalendar.setVisibility(View.VISIBLE);
                visibleItem.setIcon(R.drawable.down_arrow);
                setRecyclerViewMargin(5);
            }
            isCalendarVisible = !isCalendarVisible;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showdialogChangelanguage() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.popup_detail_notify_language);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.TOP);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        RadioButton radioButton1 = dialog.findViewById(R.id.vi);
        RadioButton radioButton2 = dialog.findViewById(R.id.en);
        RadioButton radioButton3 = dialog.findViewById(R.id.jb);
        RadioButton radioButton4 = dialog.findViewById(R.id.cn);
        RadioButton radioButton5 = dialog.findViewById(R.id.kr);

        shareDataMission.getDataChangeLanguage().observe(getViewLifecycleOwner(), i -> {
            if (i == 1) {
                radioButton1.setChecked(true);
            } else if (i == 2) {
                radioButton2.setChecked(true);
            }else if (i == 3) {
                radioButton3.setChecked(true);
            }else if (i == 4) {
                radioButton4.setChecked(true);
            }else if (i == 5) {
                radioButton5.setChecked(true);
            }
        });

        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            if (i == radioButton1.getId()) {
                setLocale("vi");
                shareDataMission.setDataChangeLanguage(1);
            }else if (i == radioButton2.getId()){
                setLocale("en");
                shareDataMission.setDataChangeLanguage(2);
            }else if (i == radioButton3.getId()){
                setLocale("ja");
                shareDataMission.setDataChangeLanguage(3);
            }else if (i == radioButton4.getId()){
                setLocale("zh");
                shareDataMission.setDataChangeLanguage(4);
            }else if (i == radioButton5.getId()){
                setLocale("ko");
                shareDataMission.setDataChangeLanguage(5);
            }
            dialog.dismiss();
        });
    }


    private void setLocale(String languageCode) {
        // Đặt Locale cho ứng dụng
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        // Cập nhật lại cấu hình ứng dụng với locale mới (dành cho Android 7.0 trở lên)
        android.content.res.Configuration config = getResources().getConfiguration();
        config.setLocale(locale); // Dùng setLocale thay vì cập nhật config.locale trực tiếp
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Làm mới lại CalendarView
        // Bạn có thể gọi lại activity để làm mới giao diện
        recreate(requireActivity()); // Đây sẽ làm cho activity được tái tạo và áp dụng cấu hình mới

        // Hoặc nếu bạn không muốn recreate toàn bộ Activity, có thể ẩn và hiện lại CalendarView
        calendarView.setVisibility(View.GONE);
        calendarView.setVisibility(View.VISIBLE);
    }

    private void showdialogOrder() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.popup_ordermission);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.TOP);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        RadioButton radioButton1 = dialog.findViewById(R.id.radioButtonST);
        RadioButton radioButton2 = dialog.findViewById(R.id.radioButtonL_B);
        RadioButton radioButton3 = dialog.findViewById(R.id.radioButtonL_T);
        RadioButton radioButton4 = dialog.findViewById(R.id.radioButtonA_Z);
        RadioButton radioButton5 = dialog.findViewById(R.id.radioButtonZ_A);

        shareDataMission.getDataOrdering().observe(getViewLifecycleOwner(), i -> {
            if (i == 1) {
                radioButton1.setChecked(true);
            } else if (i == 2) {
                radioButton2.setChecked(true);
            }else if (i == 3) {
                radioButton3.setChecked(true);
            }else if (i == 4) {
                radioButton4.setChecked(true);
            }else if (i == 5) {
                radioButton5.setChecked(true);
            }
        });

        if (mainList == null) {
            mainList = new ArrayList<>();
        }else{
            mainList = new ArrayList<>();
        }

        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            if (i == radioButton1.getId()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mainList.sort((mission, t1) -> {
                        String date1 = mission.getDate();
                        String date2 = t1.getDate();
                        String time1 = mission.getTime();
                        String time2 = t1.getTime();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("H:mm");
                        LocalDate localDate1 = LocalDate.parse(date1, formatter);
                        LocalDate localDate2 = LocalDate.parse(date2, formatter);
                        LocalTime localTime1 = LocalTime.parse(time1, formatter1);
                        LocalTime localTime2 = LocalTime.parse(time2, formatter1);
                        if (localDate1.isBefore(localDate2)) {
                            return -1;
                        }else if (localDate1.equals(localDate2)){
                            if (localTime1.isBefore(localTime2)){
                                return -1;
                            }else if (localTime1.equals(localTime2)) {
                                return 0;
                            }else{
                                return 1;
                            }
                        }
                        return 1;
                    });
                }
                shareDataMission.setDataOrdering(1);
            }else if (i == radioButton2.getId()){
                mainList.sort(Comparator.comparing(Mission::getMission_id));
                shareDataMission.setDataOrdering(2);
            }else if (i == radioButton3.getId()){
                mainList.sort(Comparator.comparing(Mission::getMission_id).reversed());
                shareDataMission.setDataOrdering(3);
            }else if (i == radioButton4.getId()){
                mainList.sort(Comparator.comparing(Mission::getTitle));
                shareDataMission.setDataOrdering(4);
            }else if (i == radioButton5.getId()){
                mainList.sort(Comparator.comparing(Mission::getTitle).reversed());
                shareDataMission.setDataOrdering(5);
            }
            dialog.dismiss();
        });

        radioGroup.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }

    //Lấy kích thước của ActionBar
    private int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        if (requireContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            return TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }
        return 0;
    }

    //Cập nhật marginTop cho RecyclerView
    private void setRecyclerViewMargin(int marginTop) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rVC.getLayoutParams();
        layoutParams.topMargin = marginTop;
        rVC.setLayoutParams(layoutParams);
    }

}