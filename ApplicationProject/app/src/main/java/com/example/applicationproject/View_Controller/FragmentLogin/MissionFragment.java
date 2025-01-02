package com.example.applicationproject.View_Controller.FragmentLogin;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.applicationproject.Database.ToDoDBContract;
import com.example.applicationproject.Database.ToDoDBHelper;
import com.example.applicationproject.Model.Category;
import com.example.applicationproject.Model.Mission;
import com.example.applicationproject.Model.Notification;
import com.example.applicationproject.Model.Ringtone;
import com.example.applicationproject.Model.Sticker;
import com.example.applicationproject.Model.Taskbeside;
import com.example.applicationproject.View_Controller.Activity.HistoryActivity;
import com.example.applicationproject.View_Controller.Activity.ManageListActivity;
import com.example.applicationproject.View_Controller.Activity.StatisticUserActivity;
import com.example.applicationproject.View_Controller.Adapter.ItemCategoryAdapter;
import com.example.applicationproject.View_Controller.Adapter.ItemCategoryAdapter.OnClickItemCategoryItem;
import com.example.applicationproject.View_Controller.Adapter.MissionAdapter;
import com.example.applicationproject.View_Controller.Adapter.RingToneUserAdapter;
import com.example.applicationproject.View_Controller.Adapter.StickerAdapter;
import com.example.applicationproject.View_Controller.Adapter.TaskBesideAdapter;
import com.example.applicationproject.View_Controller.ClassTemp.NotificationTemp;
import com.example.applicationproject.View_Controller.ClassTemp.TaskString;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.Service.AlarmScheduler;
import com.example.applicationproject.View_Controller.Service.CheckActiveService;
import com.example.applicationproject.View_Controller.ShareDataMission;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.Utils.ItemTouchHelperListener;
import com.example.applicationproject.View_Controller.Utils.RecyclerViewItemTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MissionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MissionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnClickItemCategoryItem , TaskBesideAdapter.OnClickItemTaskBeside, RingToneUserAdapter.OnClickItemRingtone, StickerAdapter.OnClickStickerListener, ItemTouchHelperListener {


    private List<Mission> mainList;
    private ItemCategoryAdapter itemCategoryAdapter;
    private MissionAdapter missionAdapter;
    private LinearLayout emptyView;
    private RecyclerView rVM;
    public static ShareDataMission shareDataDialogs;
    private ToDoDBHelper toDoDBHelper;
    private LocalDate localDate;
    private int user;
    private int category_id_1;
    private String currentUser;
    private String password;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;
    private static final int EXISTING_MISIONS_LOADER = 0;
    private static final int EXISTING_CATEGORIES_LOADER = 1;
    private static final int EXISTING_RINGTONES_LOADER = 2;
    private static final int EXISTING_STICKERS_LOADER = 3;
    private static final int EXISTING_TASKBESIDES_LOADER = 4;
    private static final int EXISTING_NOTIFICATIONS_LOADER = 5;
    private RelativeLayout relativeLayout;
    private boolean bound;
    private SearchView searchView;
    private CheckActiveService myService;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MissionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MissionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MissionFragment newInstance(String param1, String param2) {
        MissionFragment fragment = new MissionFragment();
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

    @SuppressLint({"NotifyDataSetChanged", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mission, container, false);
        Intent intent = requireActivity().getIntent();
        currentUser = intent.getStringExtra("name");
        Log.e("onCreateView:1", currentUser  + " ");
        password = intent.getStringExtra("password");
        user = DAO.getUserId(this.requireContext(), currentUser);
        category_id_1 = intent.getIntExtra("category_id", -1);
        Log.e("onCreateView:2", String.valueOf(user));
        shareDataDialogs = new ViewModelProvider(requireActivity()).get(ShareDataMission.class);
        getLoaderManager().initLoader(EXISTING_CATEGORIES_LOADER, null, this);
        getLoaderManager().initLoader(EXISTING_MISIONS_LOADER, null, this);
        getLoaderManager().initLoader(EXISTING_RINGTONES_LOADER, null, this);
        getLoaderManager().initLoader(EXISTING_STICKERS_LOADER, null, this);
        getLoaderManager().initLoader(EXISTING_TASKBESIDES_LOADER, null, this);
        getLoaderManager().initLoader(EXISTING_NOTIFICATIONS_LOADER, null, this);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        rVM = view.findViewById(R.id.recyclerViewMission);
        emptyView = view.findViewById(R.id.emptyView);
        relativeLayout = view.findViewById(R.id.missionLayout);

        missionAdapter = new MissionAdapter(requireContext());
        missionAdapter.getUserId(currentUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        rVM.setLayoutManager(linearLayoutManager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localDate = LocalDate.now();
        }

//        List<Mission> missions = new ArrayList<>();
//        missions.add(new Mission(1, 1, "29/12/2024", "Complete the project report", "true", "true", "daily", 1, "19:00", "Morning meeting", 1, "5", "10 minutes before", "true", "On"));
//        missions.add(new Mission(1, 1, "28/11/2024", "Complete the project report", "true", "true", "daily", 2, "10:00", "Morning meeting", 1, "5", "10 minutes before", "true", "On"));
//        missions.add(new Mission(1, 1, "27/10/2024", "Complete the project report", "true", "true", "daily", 3, "11:00", "Morning meeting", 2, "5", "10 minutes before", "true", "On"));
//        missions.add(new Mission(1, 1, "26/09/2024", "Complete the project report", "true", "true", "daily", 4, "02:00", "Morning meeting", 2, "5", "10 minutes before", "true", "On"));
//        missions.add(new Mission(1, 1, "25/07/2024", "Complete the project report", "true", "true", "daily", 5, "09:00", "Morning meeting", 3, "5", "10 minutes before", "true", "On"));
//        missions.add(new Mission(1, 1, "28/06/2024", "Complete the project report", "true", "true", "daily", 6, "13:00", "Morning meeting", 3, "5", "10 minutes before", "true", "On"));
//        missions.add(new Mission(1, 1, "28/06/2024", "Complete the project report", "true", "true", "daily", 7, "14:00", "Morning meeting", 1, "5", "10 minutes before", "true", "On"));
//        missions.add(new Mission(1, 1, "28/06/2025", "Complete the project report", "true", "true", "daily", 8, "15:00", "Morning meeting", 3, "5", "10 minutes before", "true", "On"));
//        missions.add(new Mission(1, 1, "28/06/2025", "Complete the project report", "true", "true", "daily", 9, "16:00", "Morning meeting", 3, "5", "10 minutes before", "true", "On"));
//        missions.add(new Mission(1, 1, "29/06/2025", "Complete the project report", "true", "true", "daily", 10, "13:00", "Morning meeting", 2, "5", "10 minutes before", "true", "On"));
//        missions.add(new Mission(1, 1, "29/06/2025", "Complete the project report", "true", "true", "daily", 11, "14:00", "Morning meeting", 3, "5", "10 minutes before", "true", "On"));
//        missions.add(new Mission(1, 1, "29/06/2025", "Complete the project report", "true", "true", "daily", 12, "15:00", "Morning meeting", 3, "5", "10 minutes before", "true", "On"));
//        missions.add(new Mission(1, 1, "27/06/2025", "Complete the project report", "true", "true", "daily", 13, "16:00", "Morning meeting", 3, "5", "10 minutes before", "true", "On"));
//        shareDataDialogs.setMainList(missions);




//        Notification notifycation = new Notification(1, "18:05", 1, "28/12/2024");
//        setalarm(notifycation.getTime(), notifycation.getDate(), notifycation.getMission_id(), "Không", 0, notifycation.getMission_id());

//        shareDataDialogs.getMainList().observe(getViewLifecycleOwner(), missions -> {
//            if (missions != null){
//                mainList.clear();
//                mainList.addAll(missions);
//                missionAdapter.setMissionList(mainList);
//                missionAdapter.notifyDataSetChanged();
//            }else{
//                mainList.clear();
//                missionAdapter.setMissionList(mainList);
//                missionAdapter.notifyDataSetChanged();
//            }
//        });

//        shareDataDialogs.getMainList().observe(getViewLifecycleOwner(), missions -> {
//            missionAdapter.setMissionList(missions);
//            missionAdapter.notifyDataSetChanged();
//        });

        shareDataDialogs.getMainList().observe(getViewLifecycleOwner(), m -> {
            Log.e("missionList", "onCreateView: " + m.size());
            if (!m.isEmpty()){
                missionAdapter.setMissionList(m);
                rVM.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }else{
                missionAdapter.setMissionList(new ArrayList<>());
                rVM.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        });

        rVM.setAdapter(missionAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback= new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rVM);

        setHasOptionsMenu(true);

//        checkEmptyData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // xét dữ liệu mặc định
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    shareDataDialogs.setDataDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    shareDataDialogs.setDataTime("Không");
                }
                shareDataDialogs.setDataNotice("Không");
                shareDataDialogs.setDataRepeat("Không");
                shareDataDialogs.setDataRepeatNo(0);
                shareDataDialogs.setDataRepeatType("Không");
                shareDataDialogs.setReminder("");
                shareDataDialogs.setReminder2("");
                shareDataDialogs.setReminder3("");
                shareDataDialogs.setReminder4("");
                shareDataDialogs.setReminder5("");
                shareDataDialogs.setReminder6("");
                shareDataDialogs.setReminder7("");
                shareDataDialogs.setDataSticker(0);
                shareDataDialogs.setDataRingTone(1);
                shareDataDialogs.setDataTimeHour(0);
                shareDataDialogs.setDataTimeMinute(0);
//                List<String> list = new ArrayList<>();
//                List<TaskString> taskStringList = new ArrayList<>();
//                List<NotificationTemp> notificationTemps = new ArrayList<>();
                shareDataDialogs.setDatataskBesideList(new ArrayList<>());
                shareDataDialogs.setDatareminders(new ArrayList<>());
                shareDataDialogs.setDataNotificationTemp(new ArrayList<>());
                shareDataDialogs.setDataRemindType("Thông báo");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate localDate = LocalDate.now();
                    shareDataDialogs.setDataDate(localDate.format(formatter));
                }
                showBottomDialog();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), CheckActiveService.class);
        requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (bound){
            requireActivity().unbindService(connection);
            bound = false;
        }
    }

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CheckActiveService.LocalBinder binder = (CheckActiveService.LocalBinder) service;
            myService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
        }
    };

    public static void checkActive(Context context, ShareDataMission shareDataDialogs) {
        Log.d("CheckTasks", "checkActive called at: " + System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate lcd = LocalDate.now();
            LocalTime lct = LocalTime.now();

            List<Mission> missionList = shareDataDialogs.getMainList().getValue();
            if (missionList == null) {
                missionList = new ArrayList<>();
            }
            Log.e("missionList1", "checkActive: " + missionList.size());
            for (Mission mission : missionList) {
                LocalDate missionDate = LocalDate.parse(mission.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalTime missionTime = LocalTime.parse(mission.getTime(), DateTimeFormatter.ofPattern("H:mm"));
                LocalDateTime missionDateTime = LocalDateTime.of(missionDate, missionTime);
                if (missionDateTime.isBefore(LocalDateTime.of(lcd, lct))){
                    Log.e("missionList2", "checkActive: " + mission.getTitle());
                    DAO.updateActiveMission(context, mission.getMission_id(), "Off");
                    mission.setIsActive("Off");
                }else{
                    Log.e("missionList3", "kocheckdcActive: " + mission.getTitle());
                }
            }
            shareDataDialogs.setMainList(missionList);
        }
    }

    //Kiểm tra dữ liệu có rỗng
//    private void checkEmptyData() {
//        if (missionAdapter.getItemCount() == 0) {
//            rVM.setVisibility(View.GONE);
//            emptyView.setVisibility(View.VISIBLE);
//        } else {
//            rVM.setVisibility(View.VISIBLE);
//            emptyView.setVisibility(View.GONE);
//        }
//    }

//    public List<Mission> getListMission() {
//
//
//        return list;
//    }

    @SuppressLint({"ResourceType", "NotifyDataSetChanged"})
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mission_layout, menu);

        Toolbar toolbar =  requireActivity().findViewById(R.id.toolbarMain);
        View customView = getLayoutInflater().inflate(R.layout.custom_toolbar_view, null);
        //loc
//        MenuItem loc = menu.findItem(R.id.filter);
//        if (loc != null) {
//            loc.setActionView(null);
//        }else{
//            loc.setActionView(customView);
//        }
//
//        RecyclerView recyclerView = customView.findViewById(R.id.recycleview_horizontal_managelist);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
//        itemCategoryAdapter = new ItemCategoryAdapter(getContext(), this, false);
//        List<Category> categories = new ArrayList<>();
//        shareDataDialogs.getCategoryList().observe(this, data -> {
//            if (data != null){
//                categories.clear();
//                categories.addAll(data);
//            }else{
//                categories.clear();
//            }
//        });
//        itemCategoryAdapter.setCategoryList(categories);
//        recyclerView.setAdapter(itemCategoryAdapter);
//        ImageButton btnFilter = customView.findViewById(R.id.btn_openPopupMenuMission);
//        btnFilter.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 loc.setActionView(null);
//             }
//         });


        //Tìm kiếm theo tiêu đề của lịch trình
        MenuItem searchItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        assert searchView != null;
        searchView.setQueryHint("Tìm kiếm...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                missionAdapter.setFilterType("Title");
                missionAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                missionAdapter.setFilterType("Title");
                missionAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.theLoai) {
            Intent intent = new Intent(requireContext(), ManageListActivity.class);
            intent.putExtra("name", currentUser);
            intent.putExtra("password", password);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.thongke) {
            Intent intent = new Intent(requireContext(), StatisticUserActivity.class);
            intent.putExtra("name", currentUser);
            startActivity(intent);
            return true;
        }else if (item.getItemId() == R.id.history){
            Intent intent = new Intent(requireContext(), HistoryActivity.class);
            intent.putExtra("name", currentUser);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showBottomDialog() {

        //Tạo bottom dialog cho nút thêm lịch trình
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_mission_add);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        EditText editext = dialog.findViewById(R.id.editText_title_mission);
        Button btnChoseCategory = dialog.findViewById(R.id.btn_chose_category);
        ImageButton btnBeside = dialog.findViewById(R.id.mission_beside);
        ImageButton btnTemplate = dialog.findViewById(R.id.template_task);
        ImageView remove = dialog.findViewById(R.id.remove);
        ImageButton calendar = dialog.findViewById(R.id.calendarBtn);
        ImageButton pushData = dialog.findViewById(R.id.pushData);
        RecyclerView recyclerView_taskBeside = dialog.findViewById(R.id.recyclerView_taskBeside);

        shareDataDialogs.getCategoryList().observe(getViewLifecycleOwner(), categorylist -> {
            if (categorylist != null){
                Log.e("category", "showBottomDialog: " + categorylist.size());
                int data;
                if (shareDataDialogs.getDataCategory().getValue() == null){
                    data = categorylist.get(0).getCategory_id();
                    shareDataDialogs.setDataCategory(data);
                    Log.e("category_id", "showBottomDialog: " + data);
                    btnChoseCategory.setText(categorylist.stream().anyMatch(category -> category.getCategory_id() == data) ? categorylist.stream().filter(category -> category.getCategory_id() == data).findFirst().get().getCategory_name() : "Không thể loại");
                }else{
                    data = shareDataDialogs.getDataCategory().getValue();
                    Log.e("category_id", "showBottomDialog: " + data);
                    btnChoseCategory.setText(categorylist.stream().anyMatch(category -> category.getCategory_id() == data) ? categorylist.stream().filter(category -> category.getCategory_id() == data).findFirst().get().getCategory_name() : "Không thể loại");
                }

            }else{
                Toast.makeText(getContext(), "Không có danh sách thể loại", Toast.LENGTH_SHORT).show();
            }
        });

        //xử lý sự kiện khi click vào nút category
        btnChoseCategory.setOnClickListener(view -> {
            showDialogCategory();
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_taskBeside.setLayoutManager(layoutManager);
        TaskBesideAdapter taskBesideAdapter = new TaskBesideAdapter(getContext(), this);
        recyclerView_taskBeside.setAdapter(taskBesideAdapter);

        shareDataDialogs.getDatataskBesideList().observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                taskBesideAdapter.setTaskBesideList(data);
                recyclerView_taskBeside.setVisibility(View.VISIBLE);
            } else {
                taskBesideAdapter.setTaskBesideList(new ArrayList<>());
                recyclerView_taskBeside.setVisibility(View.GONE);
            }
        });

        //xử lý sự kiện khi click vào nút ImageButton
        btnBeside.setOnClickListener(view -> {
            TaskString taskString = new TaskString("");
            List<TaskString> mtaskTaskbesides =  shareDataDialogs.getDatataskBesideList().getValue();
            if (mtaskTaskbesides == null){
                mtaskTaskbesides = new ArrayList<>();
            }
            mtaskTaskbesides.add(taskString);
            shareDataDialogs.setDatataskBesideList(mtaskTaskbesides);
        });

        //xử lý sự kiện khi click vào nút template
        btnTemplate.setOnClickListener(view -> {
            showDialogTemplate();
        });

        //Xử lý sự kiện khi click vào nút remove
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //Xử lý sự kiện khi click vào nút calendar
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialogCalendar();
            }
        });

        pushData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editext.getText().toString().isEmpty()){
                    dialog.dismiss();
                }else{
                    AtomicInteger sticker_id = new AtomicInteger();
                    shareDataDialogs.getDataSticker().observe(getViewLifecycleOwner(), sticker_id::set);
                    AtomicInteger ringTone_id = new AtomicInteger();
                    shareDataDialogs.getDataRingTone().observe(getViewLifecycleOwner(), ringTone_id::set);
                    AtomicReference<String> date = new AtomicReference<>();
                    shareDataDialogs.getDataDate().observe(getViewLifecycleOwner(), date::set);
                    String describe = null;
                    AtomicReference<String> isNotify = new AtomicReference<>();
                    shareDataDialogs.getDataNotice().observe(getViewLifecycleOwner(), data -> {
                        if (data.equals("Không")){
                            isNotify.set("False");
                        }else{
                            isNotify.set("True");
                        }
                    });
                    AtomicReference<String> isRepeat = new AtomicReference<>();
                    shareDataDialogs.getDataRepeat().observe(getViewLifecycleOwner(), data -> {
                        if (data.equals("Không")){
                            isRepeat.set("False");
                        }else{
                            isRepeat.set("True");
                        }
                    });
                    AtomicReference<String> repeatType = new AtomicReference<>();
                    shareDataDialogs.getDataRepeatType().observe(getViewLifecycleOwner(), repeatType::set);
                    AtomicReference<String> time = new AtomicReference<>();
                    shareDataDialogs.getDataTime().observe(getViewLifecycleOwner(), time::set);
                    String title = editext.getText().toString();
                    AtomicInteger category_id = new AtomicInteger();
                    shareDataDialogs.getDataCategory().observe(getViewLifecycleOwner(), category_id::set);
                    AtomicReference<String> repeatNo = new AtomicReference<>();
                    shareDataDialogs.getDataRepeatNo().observe(getViewLifecycleOwner(), data -> {
                        if (data == 0){
                            repeatNo.set("Không");
                        }else{
                            repeatNo.set(data.toString());
                        }
                    });
                    AtomicReference<String> reminderType = new AtomicReference<>();
                    shareDataDialogs.getDataRemindType().observe(getViewLifecycleOwner(), reminderType::set);
                    String isSticker;
                    if (sticker_id.get() == 0){
                        isSticker = "False";
                    }else{
                        isSticker = "True";
                    }
                    String isActive = "On";

                    if (date.get().equals("Không")){
                        dialog.dismiss();
                    }else if (time.get().equals("Không")){
                        dialog.dismiss();
                    }else{
                        boolean check = DAO.insertMission(requireContext(), sticker_id.get(), ringTone_id.get(), date.get(), null,
                                isNotify.get(), isRepeat.get(), repeatType.get(), time.get(), title,
                                category_id.get(), repeatNo.get(), reminderType.get(), isSticker, isActive);
                        if (check){
                            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                            int id = DAO.findmissionbytitle(requireContext(), title);
                            getLoaderManager().restartLoader(EXISTING_MISIONS_LOADER, null, MissionFragment.this);
//                            Mission newMission = new Mission(sticker_id.get(), ringTone_id.get(), date.get(), null,
//                                    isNotify.get(), isRepeat.get(), repeatType.get(), id ,time.get(), title,
//                                    category_id.get(), repeatNo.get(), reminderType.get(), isSticker, isActive);
//                            List<Mission> missionList = shareDataDialogs.getMainList().getValue();
//                            if (missionList == null){
//                                missionList = new ArrayList<>();
//                            }
//                            missionList.add(newMission);
//                            shareDataDialogs.setMainList(missionList);
                            List<NotificationTemp> notificationTemps = new ArrayList<>();
                            shareDataDialogs.getDataNotificationTemp().observe(getViewLifecycleOwner(), data -> {
                                if (!data.isEmpty()){
                                    notificationTemps.clear();
                                    notificationTemps.addAll(data);
                                }else{
                                    notificationTemps.clear();
                                }
                            });

                            AtomicReference<String> retype = new AtomicReference<>();
                            shareDataDialogs.getDataRemindType().observe(getViewLifecycleOwner(), retype::set);

                            AtomicInteger reNo = new AtomicInteger();
                            shareDataDialogs.getDataRepeatNo().observe(getViewLifecycleOwner(), reNo::set);

                            if (notificationTemps.isEmpty()){
                                Toast.makeText(getContext(), "Không có lời nhắc nào!!", Toast.LENGTH_SHORT).show();
                            }else{
                                for (NotificationTemp notificationTemp : notificationTemps){
                                    boolean checkNotification = DAO.insertNotification(requireContext(), notificationTemp.getTime(), notificationTemp.getDate(), id);
                                    if (!checkNotification){
                                        Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }else{
                                        Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                                        int id_notification = DAO.findnotificationbyTimeDate(requireContext(), notificationTemp.getTime(), notificationTemp.getDate());
                                        getLoaderManager().restartLoader(EXISTING_NOTIFICATIONS_LOADER, null, MissionFragment.this);
//                                        Notification newNotification = new Notification(id_notification, notificationTemp.getTime(), id, notificationTemp.getDate());
//                                        List<Notification> notificationList = shareDataDialogs.getNotificationList().getValue();
//                                        if (notificationList == null){
//                                            notificationList = new ArrayList<>();
//                                        }
//                                        notificationList.add(newNotification);
//                                        shareDataDialogs.setNotificationList(notificationList);
                                        if (DAO.findnotificationbyTimeDate(requireContext(), notificationTemp.getTime(), notificationTemp.getDate()) == -1){
                                            Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }else{
                                            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                                            setalarm(notificationTemp.getTime(), notificationTemp.getDate(), id, retype.get(), reNo.get(), id_notification, currentUser);
                                        }
                                    }
                                }
                            }

                            List<TaskString> taskbesides = shareDataDialogs.getDatataskBesideList().getValue();
                            if (taskbesides == null){
                                taskbesides = new ArrayList<>();
                            }
                            if (taskbesides.isEmpty()){
                                Toast.makeText(getContext(), "Không có task beside nào!!", Toast.LENGTH_SHORT).show();
                            }else{
                                for (TaskString taskString : taskbesides) {
                                    if (!String.valueOf(taskString).isEmpty()){
                                        boolean checkTaskBeside = DAO.insertTaskbeside(requireContext(), taskString.getTaskString(), id);
                                        if (!checkTaskBeside) {
                                            Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                                            getLoaderManager().restartLoader(EXISTING_TASKBESIDES_LOADER, null, MissionFragment.this);
                                        }
                                    }
                                }
                            }
                            dialog.dismiss();

                        }else{
                            Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                }
            }
        });
    }

    private void setalarm(String time, String date, int mission_id, String type, int repeatNo, int notify_id, String name){
        Calendar mCalender;
        LocalDate mLocalDate;
        LocalTime mLocalTime;
        long mRepeat;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLocalDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            mLocalTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("H:mm"));
            mCalender = Calendar.getInstance();
            mCalender.set(mLocalDate.getYear(), mLocalDate.getMonthValue() - 1,
                    mLocalDate.getDayOfMonth(), mLocalTime.getHour(), mLocalTime.getMinute());
            long selectedTimestamp =  mCalender.getTimeInMillis();

            if (type.equals("Không")){
                new AlarmScheduler().setAlarm(requireContext(), selectedTimestamp, mission_id, notify_id, name);
                Toast.makeText(getContext(), "Đặt lời nhắc thành công" + selectedTimestamp, Toast.LENGTH_SHORT).show();
            }else{
                if (type.equals("Giờ")){
                    mRepeat = repeatNo * milHour;
                }else if (type.equals("Ngày")){
                    mRepeat = repeatNo * milDay;
                }else if (type.equals("Tuần")){
                    mRepeat = repeatNo * milWeek;
                }else{
                    mRepeat = repeatNo * milMonth;
                }
                new AlarmScheduler().setRepeatAlarm(requireContext(), selectedTimestamp, mission_id, mRepeat, notify_id, name);
                Toast.makeText(getContext(), "Đặt lời nhắc thành công" + selectedTimestamp, Toast.LENGTH_SHORT).show();
            }
        }

    }

    @SuppressLint({"ClickableViewAccessibility", "NotifyDataSetChanged"})
    private void showDialogTemplate() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sticker_layout);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        dialog.getWindow().setGravity(Gravity.CENTER);

        RecyclerView recyclerView = dialog.findViewById(R.id.listItem_stickers);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        StickerAdapter stickerAdapter = new StickerAdapter(this.requireContext(), this);
        recyclerView.setAdapter(stickerAdapter);
        shareDataDialogs.getStickerList().observe(getViewLifecycleOwner(), stickers -> {
            if (stickers != null){
                stickerAdapter.setStickerList(stickers);
            }else{
                stickerAdapter.setStickerList(new ArrayList<>());
            }
        });
//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                dialog.dismiss();
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void showDialogCategory() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_missonlist);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        // Khởi tạo RecyclerView và Adapter
        RecyclerView recyclerView = dialog.findViewById(R.id.listItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        itemCategoryAdapter = new ItemCategoryAdapter(getContext(), this, true);
        shareDataDialogs.getCategoryList().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null){
                itemCategoryAdapter.setCategoryList(categories);
            }else{
                itemCategoryAdapter.setCategoryList(new ArrayList<>());
            }
        });
        recyclerView.setAdapter(itemCategoryAdapter);
        shareDataDialogs.getDataCategory().observe(getViewLifecycleOwner(), data -> {
            if (data != null){
                Toast.makeText(getContext(), "data: " + data, Toast.LENGTH_SHORT).show();
                itemCategoryAdapter.setChosingbyId(data);
            }
        });

        // Xử lý sự kiện nhấp vào item trong RecyclerView
//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                // Nếu nhấp vào item, đóng dialog
//
//                dialog.dismiss();
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                // Không cần xử lý gì ở đây
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//                // Không sử dụng ở đây
//            }
//        });
        // Xử lý sự kiện nút "Thêm nhiều"
        LinearLayout btnAddMore = dialog.findViewById(R.id.btn_addMore);
        btnAddMore.setOnClickListener(view -> {
            // Mở Activity quản lý danh sách
            Intent intent = new Intent(requireActivity(), ManageListActivity.class);
            intent.putExtra("name", currentUser);
            intent.putExtra("password", password);
            startActivity(intent);
        });
    }



    private void showBottomDialogCalendar() {

        //Xuất hiện dialog chọn ngày giờ
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_mission_add_calendar);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        dialog.getWindow().setGravity(Gravity.CENTER);

        AppCompatButton cancel = dialog.findViewById(R.id.cancelBtn);
        AppCompatButton done = dialog.findViewById(R.id.doneBtn);


        LinearLayout layoutRemind = dialog.findViewById(R.id.layoutTypeofReminder);
        LinearLayout layoutTime = dialog.findViewById(R.id.layoutTime);
        LinearLayout layoutNotice = dialog.findViewById(R.id.layoutNotification);
        LinearLayout layoutRepeat = dialog.findViewById(R.id.layoutRepeat);
        DatePicker datePicker = dialog.findViewById(R.id.datePicker);

//        shareDataDialogs.getDataDate().observe(getViewLifecycleOwner(), data -> {
//            if (data != null && !data.equals("Không")){
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    LocalDate lDate = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//                    datePicker.updateDate(lDate.getYear(), lDate.getMonthValue() - 1, lDate.getDayOfMonth());
//                }
//            }
//        });

//        datePicker.setOnClickListener(view -> {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                LocalDate localdate = LocalDate.of(datePicker.getYear(), datePicker.getMonth() - 1, datePicker.getDayOfMonth());
//                if (localdate.isAfter(localDate) || localdate.isEqual(localDate)){
//                    shareDataDialogs.setDataDate(localdate.format(formatter));
//                    layoutTime.setClickable(true);
//                    Log.e("chosing calendar", "showBottomDialogCalendar: Valid" + localdate.format(formatter));
//                }
//                if(localdate.isBefore(localDate)){
//                    layoutTime.setClickable(false);
//                    shareDataDialogs.setDataTime("Không");
//                    Log.e("chosing calendar", "showBottomDialogCalendar: Novalid" + localdate.format(formatter));
//                }
//            }
//        });

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            LocalDate localdate = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                            if (localdate.isAfter(localDate) || localdate.isEqual(localDate)){
                                shareDataDialogs.setDataDate(localdate.format(formatter));
                                layoutTime.setClickable(true);
                                Log.e("chosing calendar", "showBottomDialogCalendar: Valid" + localdate.format(formatter));
                            }
                            if(localdate.isBefore(localDate)){
                                layoutTime.setClickable(false);
                                shareDataDialogs.setDataTime("Không");
                                Log.e("chosing calendar", "showBottomDialogCalendar: Novalid" + localdate.format(formatter));
                            }
                            shareDataDialogs.setDataNotice("Không");
                            shareDataDialogs.setDataRepeat("Không");
                            shareDataDialogs.setDataRepeatNo(0);
                            shareDataDialogs.setDataRepeatType("Không");
                            shareDataDialogs.setReminder("");
                            shareDataDialogs.setReminder2("");
                            shareDataDialogs.setReminder3("");
                            shareDataDialogs.setReminder4("");
                            shareDataDialogs.setReminder5("");
                            shareDataDialogs.setReminder6("");
                            shareDataDialogs.setReminder7("");
                            shareDataDialogs.setDatataskBesideList(new ArrayList<>());
                            shareDataDialogs.setDatareminders(new ArrayList<>());
                            shareDataDialogs.setDataNotificationTemp(new ArrayList<>());
                            shareDataDialogs.setDataRemindType("Thông báo");
                        }
                    }
                });

//        shareDataDialogs.getDataDate().observe(getViewLifecycleOwner(), data -> {
//            if (data != null && !data.equals("Không")){
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    LocalDate localdate = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//                    datePicker.updateDate(localdate.getYear(), localdate.getMonthValue() - 1, localdate.getDayOfMonth());
//                }
//            }
//        });

        shareDataDialogs.getDataTime().observe(getViewLifecycleOwner(), data -> {
            TextView textViewTime = dialog.findViewById(R.id.textViewTime);
            textViewTime.setText(data);
            if (data.equals("Không")) {
                layoutNotice.setClickable(false);

            }else{
                layoutNotice.setClickable(true);
            }
        });

        shareDataDialogs.getDataNotice().observe(getViewLifecycleOwner(), data -> {
            TextView textViewNotice = dialog.findViewById(R.id.textViewNotice);
            textViewNotice.setText(data);
            if (data.equals("Không")){
                layoutRemind.setVisibility(View.GONE);
                layoutRepeat.setClickable(false);
            }else{
                layoutRemind.setVisibility(View.VISIBLE);
                layoutRepeat.setClickable(true);
            }
        });

        shareDataDialogs.getDataRemindType().observe(getViewLifecycleOwner(), data -> {
            TextView textViewRemind = dialog.findViewById(R.id.textViewTypeofReminder);
            textViewRemind.setText(data);
        });

        shareDataDialogs.getDataRepeat().observe(getViewLifecycleOwner(), data -> {
            TextView textViewRepeat = dialog.findViewById(R.id.textViewRepeat);
            textViewRepeat.setText(data);
        });

        layoutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("layoutTime", "layoutTime Click");
                if (!layoutTime.isClickable()){
                    return;
                }
                showBottomDialogTimePicker();
            }
        });

        layoutNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("layoutNotice", "layoutNotice Click");
                if (!layoutNotice.isClickable()){
                    return;
                }
                showBottomDialogNotification();
            }
        });

        layoutRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("layoutRepeat", "layoutRepeat Click");
                if (!layoutRepeat.isClickable()){
                    return;
                }
                showBottomDialogRepeat();
            }
        });
        
        layoutRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("layoutRemind", "layoutRemind Click");
                showBottomDialogReminder();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    shareDataDialogs.setDataDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    shareDataDialogs.setDataTime("Không");
                    shareDataDialogs.setDataNotice("Không");
                    shareDataDialogs.setDataRepeat("Không");
                    shareDataDialogs.setDataRepeatNo(0);
                    shareDataDialogs.setDataRepeatType("Không");
                    shareDataDialogs.setReminder("");
                    shareDataDialogs.setReminder2("");
                    shareDataDialogs.setReminder3("");
                    shareDataDialogs.setReminder4("");
                    shareDataDialogs.setReminder5("");
                    shareDataDialogs.setReminder6("");
                    shareDataDialogs.setReminder7("");
                    shareDataDialogs.setDataTimeHour(0);
                    shareDataDialogs.setDataTimeMinute(0);
                    shareDataDialogs.setDatataskBesideList(new ArrayList<>());
                    shareDataDialogs.setDatareminders(new ArrayList<>());
                    shareDataDialogs.setDataNotificationTemp(new ArrayList<>());
                    shareDataDialogs.setDataRemindType("Thông báo");
                }
                dialog.dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtomicReference<String> time = new AtomicReference<>();
                shareDataDialogs.getDataTime().observe(getViewLifecycleOwner(), time::set);
                AtomicReference<String> date = new AtomicReference<>();
                shareDataDialogs.getDataDate().observe(getViewLifecycleOwner(), date::set);
                if (date.get().equals("Không")){
                    dialog.dismiss();
                }
                int id = DAO.findmissionbyDateTime(getContext(), date.get(), time.get());
                if(id != -1){
                    Toast.makeText(getContext(), "Đã tồn tại", Toast.LENGTH_SHORT).show();
                    shareDataDialogs.setDataDate("Không");
                    shareDataDialogs.setDataTime("Không");
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                }
            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private  void  showBottomDialogReminder(){
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_detail_notify_typeofrings);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        dialog.getWindow().setGravity(Gravity.CENTER);
        
        RadioButton rdbtn_annoucement = dialog.findViewById(R.id.rdbtn_annoucement);
        RadioButton rdbtn_alarm = dialog.findViewById(R.id.rdbtn_alarm);
        
        shareDataDialogs.getDataRemindType().observe(this, data -> {
            if (data.equals("Thông báo")){
                rdbtn_annoucement.setChecked(true);
                rdbtn_alarm.setChecked(false);
            }else{
                rdbtn_alarm.setChecked(true);
                rdbtn_annoucement.setChecked(false);
            }
        });

        RecyclerView recyclerView = dialog.findViewById(R.id.listItem_music);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        RingToneUserAdapter musicAdapter = new RingToneUserAdapter(this.requireContext(), this);
        recyclerView.setAdapter(musicAdapter);
        shareDataDialogs.getRingtoneList().observe(getViewLifecycleOwner(), ringtonelist -> {
            if (ringtonelist != null){
                musicAdapter.setRingtoneList(ringtonelist);
            }else{
                musicAdapter.setRingtoneList(new ArrayList<>());
            }
        });

        rdbtn_annoucement.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  shareDataDialogs.setDataRemindType("Thông báo");
                  rdbtn_alarm.setChecked(false);
              }
         });

        rdbtn_alarm.setOnClickListener(view -> {
            shareDataDialogs.setDataRemindType("Báo Thức");
            rdbtn_annoucement.setChecked(false);
        });
        
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel_typeReminds);
        Button btn_save = dialog.findViewById(R.id.btn_save_typeReminds);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(view -> {
            shareDataDialogs.setDataRemindType("Thông báo");
            shareDataDialogs.setDataRingTone(1);
            dialog.dismiss();
        });
    }

    private void showBottomDialogTimePicker() {

        //Xuất hiện dialog chọn thời gian
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_mission_add_calendar_time);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        dialog.getWindow().setGravity(Gravity.CENTER);

        TimePicker timePicker = dialog.findViewById(R.id.timePicker);
        AppCompatButton cancel = dialog.findViewById(R.id.cancelBtnTime);
        AppCompatButton ok = dialog.findViewById(R.id.okBtnTime);

        timePicker.setIs24HourView(true);

        shareDataDialogs.getDataTime().observe(getViewLifecycleOwner(), data -> {
            if (!data.equals("Không")) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalTime localTime = LocalTime.parse(data, DateTimeFormatter.ofPattern("H:mm"));
                    timePicker.setHour(localTime.getHour());
                    timePicker.setMinute(localTime.getMinute());
                }
            }
        });
//        shareDataDialogs.getDataTimeHour().observe(getViewLifecycleOwner(), data -> {
//            if (data != null){
//                timePicker.setHour(data);
//            }
//        });
//
//        shareDataDialogs.getDataTimeMinute().observe(getViewLifecycleOwner(), data -> {
//            if (data != null){
//                timePicker.setMinute(data);
//            }
//        });

//        timePicker.setOnTimeChangedListener((timePicker1, hourChange, minuteChange) -> {
//            ok.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        shareDataDialogs.getDataDate().observe(getViewLifecycleOwner(), data -> {
//                            if (!data.equals("Không")){
//                                LocalDate localDate1 = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//                                LocalTime localTime = LocalTime.now();
//                                LocalTime localTime1;
//                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
//                                if (minuteChange < 10){
//                                    localTime1 = LocalTime.parse(hourChange + ":" + "0" + minuteChange, formatter);
//                                }else{
//                                    localTime1 = LocalTime.parse(hourChange + ":" + minuteChange, formatter);
//                                }
//                                if (localDate1.equals(localDate)){
//                                    if (localTime1.isAfter(localTime)){
//                                        shareDataDialogs.setDataTime(String.valueOf(localTime1));
//                                        shareDataDialogs.setDataTimeHour(hourChange);
//                                        shareDataDialogs.setDataTimeMinute(minuteChange);
//                                        dialog.dismiss();
//                                    }else{
//                                        Toast.makeText(requireContext(), "Thời gian không hợp lệ", Toast.LENGTH_SHORT).show();
//                                        shareDataDialogs.setDataTime("Không");
//                                        shareDataDialogs.setDataTimeHour(0);
//                                        shareDataDialogs.setDataTimeMinute(0);
//                                        dialog.dismiss();
//                                    }
//                                }else if (localDate1.isAfter(localDate)){
//                                    shareDataDialogs.setDataTime(String.valueOf(localTime1));
//                                    shareDataDialogs.setDataTimeHour(hourChange);
//                                    shareDataDialogs.setDataTimeMinute(minuteChange);
//                                    dialog.dismiss();
//                                }else{
//                                    Toast.makeText(requireContext(), "Thời gian không hợp lệ", Toast.LENGTH_SHORT).show();
//                                    shareDataDialogs.setDataTime("Không");
//                                    shareDataDialogs.setDataTimeHour(0);
//                                    shareDataDialogs.setDataTimeMinute(0);
//                                    dialog.dismiss();
//                                }
//                            }
//                        });
//
//                    }
//                }
//            });
//        });

        timePicker.setOnTimeChangedListener((timePicker1, hourChange, minuteChange) -> {
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        shareDataDialogs.getDataDate().observe(getViewLifecycleOwner(), data -> {
                            if (!data.equals("Không")) {
                                // Convert the selected date string to LocalDate
                                LocalDate localDate1 = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                                // Get current date and time
                                LocalDate localDate = LocalDate.now();
                                LocalTime localTimeNow = LocalTime.now(); // Time right now

                                // Format the time from the TimePicker
                                LocalTime localTime1;
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
                                if (minuteChange < 10) {
                                    localTime1 = LocalTime.parse(hourChange + ":" + "0" + minuteChange, formatter);
                                } else {
                                    localTime1 = LocalTime.parse(hourChange + ":" + minuteChange, formatter);
                                }

                                // Combine the date and time into LocalDateTime for comparison
                                LocalDateTime dateTimeNow = LocalDateTime.of(localDate, localTimeNow);
                                LocalDateTime selectedDateTime = LocalDateTime.of(localDate1, localTime1);

                                // Compare the selected time with the current time
                                if (selectedDateTime.isAfter(dateTimeNow)) {
                                    // Valid selection
                                    shareDataDialogs.setDataTime(String.valueOf(localTime1));
                                    shareDataDialogs.setDataTimeHour(hourChange);
                                    shareDataDialogs.setDataTimeMinute(minuteChange);
                                    dialog.dismiss();
                                } else {
                                    // Invalid time selection
                                    Toast.makeText(requireContext(), "Thời gian không hợp lệ", Toast.LENGTH_SHORT).show();
                                    shareDataDialogs.setDataTime("Không");
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                }
            });
            shareDataDialogs.setDataNotice("Không");
            shareDataDialogs.setDataRepeat("Không");
            shareDataDialogs.setDataRepeatNo(0);
            shareDataDialogs.setDataRepeatType("Không");
            shareDataDialogs.setReminder("");
            shareDataDialogs.setReminder2("");
            shareDataDialogs.setReminder3("");
            shareDataDialogs.setReminder4("");
            shareDataDialogs.setReminder5("");
            shareDataDialogs.setReminder6("");
            shareDataDialogs.setReminder7("");
            shareDataDialogs.setDatataskBesideList(new ArrayList<>());
            shareDataDialogs.setDatareminders(new ArrayList<>());
            shareDataDialogs.setDataNotificationTemp(new ArrayList<>());
            shareDataDialogs.setDataRemindType("Thông báo");
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    shareDataDialogs.getDataDate().observe(getViewLifecycleOwner(), data -> {
                        if (!data.equals("Không")) {
                            // Convert the selected date string to LocalDate
                            LocalDate localDate1 = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                            // Get current date and time
                            LocalDate localDate = LocalDate.now();
                            LocalTime localTimeNow = LocalTime.now(); // Time right now

                            // Format the time from the TimePicker
                            LocalTime localTime1;
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
                            if (timePicker.getMinute() < 10) {
                                localTime1 = LocalTime.parse(timePicker.getHour() + ":" + "0" + timePicker.getMinute(), formatter);
                            } else {
                                localTime1 = LocalTime.parse(timePicker.getHour() + ":" + timePicker.getMinute(), formatter);
                            }

                            // Combine the date and time into LocalDateTime for comparison
                            LocalDateTime dateTimeNow = LocalDateTime.of(localDate, localTimeNow);
                            LocalDateTime selectedDateTime = LocalDateTime.of(localDate1, localTime1);

                            // Compare the selected time with the current time
                            if (selectedDateTime.isAfter(dateTimeNow)) {
                                // Valid selection
                                shareDataDialogs.setDataTime(String.valueOf(localTime1));
                                shareDataDialogs.setDataTimeHour(timePicker.getHour());
                                shareDataDialogs.setDataTimeMinute(timePicker.getMinute());
                                dialog.dismiss();
                            } else {
                                // Invalid time selection
                                Toast.makeText(requireContext(), "Thời gian không hợp lệ", Toast.LENGTH_SHORT).show();
                                shareDataDialogs.setDataTime("Không");
                                dialog.dismiss();
                            }
                        }
                    });
                }
                dialog.dismiss();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDataDialogs.setDataTime("Không");
                shareDataDialogs.setDataTimeHour(0);
                shareDataDialogs.setDataTimeMinute(0);
                dialog.dismiss();
            }
        });
    }

    private void showBottomDialogNotification() {

        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_mission_add_calendar_notification);

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        dialog.getWindow().setGravity(Gravity.CENTER);

        AppCompatButton yes = dialog.findViewById(R.id.yesBtnNotification);
        AppCompatButton no = dialog.findViewById(R.id.noBtnNotification);
        RadioButton rdSameTime = dialog.findViewById(R.id.rd_sametime);
        RadioButton rd5mAgo = dialog.findViewById(R.id.rd_5m_ago);
        RadioButton rd10mAgo = dialog.findViewById(R.id.rd_10m_ago);
        RadioButton rd30mAgo = dialog.findViewById(R.id.rd_30m_ago);
        RadioButton rd15mAgo = dialog.findViewById(R.id.rd_15m_ago);
        RadioButton rd1dAgo = dialog.findViewById(R.id.rd_1d_ago);
        RadioButton rd2dAgo = dialog.findViewById(R.id.rd_2d_ago);

        shareDataDialogs.getReminder().observe(getViewLifecycleOwner(), data -> {
            rdSameTime.setChecked(!data.isEmpty());
        });

        shareDataDialogs.getReminder2().observe(getViewLifecycleOwner(), data -> {
            rd5mAgo.setChecked(!data.isEmpty());
        });

        shareDataDialogs.getReminder3().observe(getViewLifecycleOwner(), data -> {
            rd10mAgo.setChecked(!data.isEmpty());
        });

        shareDataDialogs.getReminder4().observe(getViewLifecycleOwner(), data -> {
            rd15mAgo.setChecked(!data.isEmpty());
        });

        shareDataDialogs.getReminder5().observe(getViewLifecycleOwner(), data -> {
            rd30mAgo.setChecked(!data.isEmpty());
        });

        shareDataDialogs.getReminder6().observe(getViewLifecycleOwner(), data -> {
            rd1dAgo.setChecked(!data.isEmpty());
        });

        shareDataDialogs.getReminder7().observe(getViewLifecycleOwner(), data -> {
            rd2dAgo.setChecked(!data.isEmpty());
        });

        AtomicBoolean rd5mAgoClick = new AtomicBoolean(rd5mAgo.isClickable());
        AtomicBoolean rd10mAgoClick = new AtomicBoolean(rd10mAgo.isClickable());
        AtomicBoolean rd30mAgoClick = new AtomicBoolean(rd30mAgo.isClickable());
        AtomicBoolean rd15mAgoClick = new AtomicBoolean(rd15mAgo.isClickable());
        AtomicBoolean rd1dAgoClick = new AtomicBoolean(rd1dAgo.isClickable());
        AtomicBoolean rd2dAgoClick = new AtomicBoolean(rd2dAgo.isClickable());
        AtomicBoolean rdSameTimeClick = new AtomicBoolean(rdSameTime.isClickable());

        shareDataDialogs.getDataDate().observe(getViewLifecycleOwner(), days -> {
            Log.e("days", "show: " + days);
            if (days != null && !days.equals("Không")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDate daySetting = LocalDate.parse(days, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    Log.e("daySetting", "show: " + daySetting);
                    if (daySetting.equals(localDate)){
                        Log.e("daySetting", "show: " + daySetting);
                        rd1dAgoClick.set(false);
                        Log.e("rd1dAgo", "rd1dAgo.isclickable: " + rd1dAgo.isClickable());
                        rd2dAgoClick.set(false);
                        AtomicInteger hour = new AtomicInteger();
                        AtomicInteger minute = new AtomicInteger();
                        shareDataDialogs.getDataTimeHour().observe(getViewLifecycleOwner(), hour::set);
                        shareDataDialogs.getDataTimeMinute().observe(getViewLifecycleOwner(), minute::set);
                        LocalTime localTime = LocalTime.now();
                        Log.e("time", "showBottomDialogNotification: " + localTime.getHour());
                        if (hour.get() == localTime.getHour()){
                            if (minute.get() - localTime.getMinute() < 5){
                                rdSameTimeClick.set(true);
                                rd5mAgoClick.set(false);
                                rd10mAgoClick.set(false);
                                rd15mAgoClick.set(false);
                                rd30mAgoClick.set(false);
                            }else if (minute.get() - localTime.getMinute() < 10 && minute.get() - localTime.getMinute() >= 5){
                                rdSameTimeClick.set(true);
                                rd5mAgoClick.set(true);
                                rd10mAgoClick.set(false);
                                rd15mAgoClick.set(false);
                                rd30mAgoClick.set(false);
                            }else if (minute.get() - localTime.getMinute() < 15 && minute.get() - localTime.getMinute() >= 10){
                                rdSameTimeClick.set(true);
                                rd5mAgoClick.set(true);
                                rd10mAgoClick.set(true);
                                rd15mAgoClick.set(false);
                                rd30mAgoClick.set(false);
                            }else if (minute.get() - localTime.getMinute() < 30 && minute.get() - localTime.getMinute() >= 15){
                                rdSameTimeClick.set(true);
                                rd5mAgoClick.set(true);
                                rd10mAgoClick.set(true);
                                rd15mAgoClick.set(true);
                                rd30mAgoClick.set(false);
                            }else if (minute.get() - localTime.getMinute() >= 30){
                                rdSameTimeClick.set(true);
                                rd5mAgoClick.set(true);
                                rd10mAgoClick.set(true);
                                rd15mAgoClick.set(true);
                                rd30mAgoClick.set(true);
                            }else{
                                rdSameTimeClick.set(false);
                                rd5mAgoClick.set(false);
                                rd10mAgoClick.set(false);
                                rd15mAgoClick.set(false);
                                rd30mAgoClick.set(false);
                            }
                        }
                    }else{
                        if (daySetting.minusDays(1).equals(localDate)){
                            rd2dAgoClick.set(false);
                            rd1dAgoClick.set(true);
                        }else if (daySetting.minusDays(2).equals(localDate) || daySetting.minusDays(2).isAfter(localDate)){
                            rd2dAgoClick.set(true);
                            rd1dAgoClick.set(true);
                        }else{
                            rd2dAgoClick.set(false);
                            rd1dAgoClick.set(false);
                        }
                    }
                }
            }
        });

//        shareDataDialogs.getDatareminders().observe(getViewLifecycleOwner(), reminders -> {
//            Log.e("reminders", "reminders size: " + (reminders != null ? reminders.size() : "null"));
//            if (reminders != null && !reminders.isEmpty()) {
//                Log.e("reminders", "reminders: " + reminders.getClass().getName());
//                list.clear();
//                Log.e("reminders", "Before adding, list size: " + list.size());
//                for (String reminder : reminders){
//                    Log.e("reminder", "reminder: " + reminder);
//                    list.add(reminder);
//                }
//                Log.e("reminders", "After adding, list size: " + list.size());
//            } else {
//                Log.e("reminders", "No data to add");
//            }
//        });
//
//        shareDataDialogs.getDataNotificationTemp().observe(getViewLifecycleOwner(), notificationList1 -> {
//            Log.e("notificationList1", "notificationList1: " + notificationList1.size());
//            if (notificationList1 != null){
//                list1.clear();
//                list1.addAll(notificationList1);
//            }
//            Log.e("notificationList1", "showBottomDialogNotification: " + list1.size());
//        });

        AtomicReference<String> day = new AtomicReference<>();
        shareDataDialogs.getDataDate().observe(getViewLifecycleOwner(), days -> {
            if (days != null && !days.equals("Không")){
                day.set(days);
            }else{
                dialog.dismiss();
            }
        });

        AtomicReference<String> time = new AtomicReference<>();
        shareDataDialogs.getDataTime().observe(getViewLifecycleOwner(), data -> {
            if (data != null && !data.equals("Không")) {
                time.set(data);
            } else {
                dialog.dismiss();
            }
        });

        AtomicInteger hour = new AtomicInteger();
        AtomicInteger minute = new AtomicInteger();

        shareDataDialogs.getDataTimeHour().observe(getViewLifecycleOwner(), hour::set);
        Log.e("hour", "showBottomDialogNotification: " + hour.get());

        shareDataDialogs.getDataTimeMinute().observe(getViewLifecycleOwner(), minute::set);

        AtomicBoolean rdSameTimeCheck = new AtomicBoolean(rdSameTime.isChecked());
        rdSameTime.setOnClickListener(view -> {
            if (!rdSameTimeClick.get()){
                return;
            }
            if (rdSameTimeCheck.get()){
                rdSameTime.setChecked(false);
                rdSameTimeCheck.set(false);
                String data = shareDataDialogs.getReminder().getValue();
                if (data != null && !data.isEmpty()){
                    String time1 = data.split(" ")[1];
                    List<String> list = shareDataDialogs.getDatareminders().getValue();
                    if (list == null) list = new ArrayList<>();
                    list.remove(day.get() + " " + time1);
                    NotificationTemp notificationTemp = new NotificationTemp(day.get(), time1);
                    List<NotificationTemp> list1 = shareDataDialogs.getDataNotificationTemp().getValue();
                    if (list1 == null) list1 = new ArrayList<>();
                    list1.remove(notificationTemp);
                    shareDataDialogs.setDataNotificationTemp(list1);
                    shareDataDialogs.setReminder("");
                }else{
                    shareDataDialogs.setReminder("");
                }
            }else{
                rdSameTime.setChecked(true);
                rdSameTimeCheck.set(true);
                String data = shareDataDialogs.getDataTime().getValue();
                List<String> list = shareDataDialogs.getDatareminders().getValue();
                if (list == null) list = new ArrayList<>();
                list.add(day.get() + " " + data);
                shareDataDialogs.setDatareminders(list);
                NotificationTemp notificationTemp = new NotificationTemp(day.get(), data);
                List<NotificationTemp> list1 = shareDataDialogs.getDataNotificationTemp().getValue();
                if (list1 == null) list1 = new ArrayList<>();
                list1.add(notificationTemp);
                shareDataDialogs.setDataNotificationTemp(list1);
                shareDataDialogs.setReminder(day.get() + " " + data);
            }
        });

        AtomicBoolean rd5mAgoCheck = new AtomicBoolean(rd5mAgo.isChecked());
        Log.e("rd5mAgoCheck", "rd5mAgoCheck: " + rd5mAgoCheck);
        rd5mAgo.setOnClickListener(view -> {
            if (!rd5mAgoClick.get()){
                return;
            }
            if (rd5mAgoCheck.get()) {
                rd5mAgo.setChecked(false);
                rd5mAgoCheck.set(false);
                String data = shareDataDialogs.getReminder2().getValue();
                if (data != null && !data.isEmpty()){
                    String time1 = data.split(" ")[1];
                    List<String> list = shareDataDialogs.getDatareminders().getValue();
                    if (list == null) list = new ArrayList<>();
                    list.remove(data);
                    shareDataDialogs.setDatareminders(list);
                    NotificationTemp notificationTemp = new NotificationTemp(day.get(), time1);
                    List<NotificationTemp> list1 = shareDataDialogs.getDataNotificationTemp().getValue();
                    if (list1 == null) list1 = new ArrayList<>();
                    list1.remove(notificationTemp);
                    shareDataDialogs.setDataNotificationTemp(list1);
                    shareDataDialogs.setReminder2("");
                }else{
                    shareDataDialogs.setReminder2("");
                }
            } else {
                rd5mAgo.setChecked(true);
                rd5mAgoCheck.set(true);
                if (minute.get() >= 5){
                    minute.set(minute.get() - 5);
                }else{
                    hour.set(hour.get() - 1);
                    minute.set(60 + minute.get() - 5);
                }
                String s;
                if (minute.get() < 10){
                    s = hour.get() + ":" + "0" + minute.get();
                }else{
                    s = hour.get() + ":" + minute.get();
                }
                List<String> list = shareDataDialogs.getDatareminders().getValue();
                if (list == null) list = new ArrayList<>();
                list.add(day.get() + " " + s);
                Log.e("list", "Datareminders: " + list.size());
                shareDataDialogs.setDatareminders(list);
                NotificationTemp notificationTemp = new NotificationTemp(day.get(), s);
                List<NotificationTemp> list1 = shareDataDialogs.getDataNotificationTemp().getValue();
                if (list1 == null) list1 = new ArrayList<>();
                list1.add(notificationTemp);
                Log.e("list1", "DataNotificationTemp: " + list1.size());
                shareDataDialogs.setDataNotificationTemp(list1);
                shareDataDialogs.setReminder2(day.get() + " " + s);
                Log.e("list", "Datareminders: " + Objects.requireNonNull(shareDataDialogs.getDatareminders().getValue()).size());
                Log.e("list1", "DataNotificationTemp: " + Objects.requireNonNull(shareDataDialogs.getDataNotificationTemp().getValue()).size());
            }
        });



//        rd5mAgo.setOnClickListener(view ->{
//            if (rd5mAgo.isChecked()){
//                rd5mAgo.setChecked(false);

//                shareDataDialogs.getReminder2().observe(getViewLifecycleOwner(), data -> {
//                    Log.e("Reminder2 Data", "Data received: " + data);
//                    if (!data.isEmpty()){
//                        String time1 = data.split(" ")[1];
//                        list.remove(data);
//                        NotificationTemp notificationTemp = new NotificationTemp(day.get(), time1);
//                        list1.remove(notificationTemp);
//                        shareDataDialogs.setReminder2("");
//                    }else{
//                        shareDataDialogs.setReminder2("");
//                    }
//                });
//            }else{
//                rd5mAgo.setChecked(true);
//                if (minute.get() >= 5){
//                    minute.set(minute.get() - 5);
//                }else{
//                    hour.set(hour.get() - 1);
//                    minute.set(60 + minute.get() - 5);
//                }
//                String s;
//                if (minute.get() < 10){
//                    s = hour.get() + ":" + "0" + minute.get();
//                }else{
//                    s = hour.get() + ":" + minute.get();
//                }
//                list.add(day.get() + " " + s);
//                NotificationTemp notificationTemp = new NotificationTemp(day.get(), s);
//                list1.add(notificationTemp);
//                shareDataDialogs.setReminder2(day.get() + " " + s);
//            }
//        });
        AtomicBoolean rd10mAgoCheck = new AtomicBoolean(rd10mAgo.isChecked());
        rd10mAgo.setOnClickListener(view -> {
            if (!rd10mAgoClick.get()){
                return;
            }
            if (rd10mAgoCheck.get()) {
                rd10mAgo.setChecked(false);
                rd10mAgoCheck.set(false);
                String data = shareDataDialogs.getReminder3().getValue();
                if (data != null && !data.isEmpty()){
                    String time1 = data.split(" ")[1];
                    List<String> list = shareDataDialogs.getDatareminders().getValue();
                    if (list == null) list = new ArrayList<>();
                    list.remove(data);
                    shareDataDialogs.setDatareminders(list);
                    NotificationTemp notificationTemp = new NotificationTemp(day.get(), time1);
                    List<NotificationTemp> list1 = shareDataDialogs.getDataNotificationTemp().getValue();
                    if (list1 == null) list1 = new ArrayList<>();
                    list1.remove(notificationTemp);
                    shareDataDialogs.setDataNotificationTemp(list1);
                    shareDataDialogs.setReminder3("");
                }else{
                    shareDataDialogs.setReminder3("");
                }
            }else{
                rd10mAgo.setChecked(true);
                rd10mAgoCheck.set(true);
                if (minute.get() >= 10){
                    minute.set(minute.get() - 10);
                }else{
                    hour.set(hour.get() - 1);
                    minute.set(60 + minute.get() - 10);
                }
                String s;
                if (minute.get() < 10){
                    s = hour.get() + ":" + "0" + minute.get();
                }else{
                    s = hour.get() + ":" + minute.get();
                }
                List<String> list = shareDataDialogs.getDatareminders().getValue();
                if (list == null) list = new ArrayList<>();
                list.add(day.get() + " " + s);
                shareDataDialogs.setDatareminders(list);
                NotificationTemp notificationTemp = new NotificationTemp(day.get(), s);
                List<NotificationTemp> list1 = shareDataDialogs.getDataNotificationTemp().getValue();
                if (list1 == null) list1 = new ArrayList<>();
                list1.add(notificationTemp);
                shareDataDialogs.setDataNotificationTemp(list1);
                shareDataDialogs.setReminder3(day.get() + " " + s);
            }
        });

        AtomicBoolean rd15mAgoCheck = new AtomicBoolean(rd15mAgo.isChecked());
        rd15mAgo.setOnClickListener(view -> {
            if (!rd15mAgoClick.get()){
                return;
            }
            if (rd15mAgoCheck.get()){
                rd15mAgo.setChecked(false);
                rd15mAgoCheck.set(false);
                String data = shareDataDialogs.getReminder4().getValue();
                if (data != null && !data.isEmpty()){
                    String time1 = data.split(" ")[1];
                    List<String> list = shareDataDialogs.getDatareminders().getValue();
                    if (list == null) list = new ArrayList<>();
                    list.remove(data);
                    shareDataDialogs.setDatareminders(list);
                    NotificationTemp notificationTemp = new NotificationTemp(day.get(), time1);
                    List<NotificationTemp> list1 = shareDataDialogs.getDataNotificationTemp().getValue();
                    if (list1 == null) list1 = new ArrayList<>();
                    list1.remove(notificationTemp);
                    shareDataDialogs.setDataNotificationTemp(list1);
                    shareDataDialogs.setReminder4("");
                }else{
                    shareDataDialogs.setReminder4("");
                }
            }else{
                rd15mAgo.setChecked(true);
                rd15mAgoCheck.set(true);
                if (minute.get() >= 15){
                    minute.set(minute.get() - 15);
                }else{
                    hour.set(hour.get() - 1);
                    minute.set(60 + minute.get() - 15);
                }
                String s;
                if (minute.get() < 10){
                    s = hour.get() + ":" + "0" + minute.get();
                }else{
                    s = hour.get() + ":" + minute.get();
                }
                List<String> list = shareDataDialogs.getDatareminders().getValue();
                if (list == null) list = new ArrayList<>();
                list.add(day.get() + " " + s);
                shareDataDialogs.setDatareminders(list);
                NotificationTemp notificationTemp = new NotificationTemp(day.get(), s);
                List<NotificationTemp> list1 = shareDataDialogs.getDataNotificationTemp().getValue();
                if (list1 == null) list1 = new ArrayList<>();
                list1.add(notificationTemp);
                shareDataDialogs.setDataNotificationTemp(list1);
                shareDataDialogs.setReminder4(day.get() + " " + s);
            }
        });

        AtomicBoolean rd30mAgoCheck = new AtomicBoolean(rd30mAgo.isChecked());
        rd30mAgo.setOnClickListener(view -> {
            if (!rd30mAgoClick.get()){
                return;
            }
            if (rd30mAgoCheck.get()){
                rd30mAgo.setChecked(false);
                rd30mAgoCheck.set(false);
                String data = shareDataDialogs.getReminder5().getValue();
                if (data != null && !data.isEmpty()){
                    String time1 = data.split(" ")[1];
                    List<String> list = shareDataDialogs.getDatareminders().getValue();
                    if (list == null) list = new ArrayList<>();
                    list.remove(data);
                    shareDataDialogs.setDatareminders(list);
                    NotificationTemp notificationTemp = new NotificationTemp(day.get(), time1);
                    List<NotificationTemp> list1 = shareDataDialogs.getDataNotificationTemp().getValue();
                    if (list1 == null) list1 = new ArrayList<>();
                    list1.remove(notificationTemp);
                    shareDataDialogs.setDataNotificationTemp(list1);
                    shareDataDialogs.setReminder5("");
                }else{
                    shareDataDialogs.setReminder5("");
                }
            }else{
                rd30mAgo.setChecked(true);
                rd30mAgoCheck.set(true);
                if (minute.get() >= 30){
                    minute.set(minute.get() - 30);
                }else {
                    hour.set(hour.get() - 1);
                    minute.set(60 + minute.get() - 30);
                }
                String s;
                if (minute.get() < 10){
                    s = hour.get() + ":" + "0" + minute.get();
                }else{
                    s = hour.get() + ":" + minute.get();
                }
                List<String> list = shareDataDialogs.getDatareminders().getValue();
                if (list == null) list = new ArrayList<>();
                list.add(day.get() + " " + s);
                shareDataDialogs.setDatareminders(list);
                NotificationTemp notificationTemp = new NotificationTemp(day.get(), s);
                List<NotificationTemp> list1 = shareDataDialogs.getDataNotificationTemp().getValue();
                if (list1 == null) list1 = new ArrayList<>();
                list1.add(notificationTemp);
                shareDataDialogs.setDataNotificationTemp(list1);
                shareDataDialogs.setReminder5(day.get() + " " + s);
            }
        });

        AtomicBoolean rd1dAgoCheck = new AtomicBoolean(rd1dAgo.isChecked());
        rd1dAgo.setOnClickListener(view -> {
            Log.e("rd1dAgo", "rd1dAgo: " + rd1dAgo.isClickable());
            if (!rd1dAgoCheck.get()){
                return;
            }
            if (rd1dAgoCheck.get()){
                rd1dAgo.setChecked(false);
                rd1dAgoCheck.set(false);
                String data = shareDataDialogs.getReminder6().getValue();
                if (data != null && !data.isEmpty()){
                    String time1 = data.split(" ")[1];
                    List<String> list = shareDataDialogs.getDatareminders().getValue();
                    if (list == null) list = new ArrayList<>();
                    list.remove(data);
                    shareDataDialogs.setDatareminders(list);
                    NotificationTemp notificationTemp = new NotificationTemp(day.get(), time1);
                    List<NotificationTemp> list1 = shareDataDialogs.getDataNotificationTemp().getValue();
                    if (list1 == null) list1 = new ArrayList<>();
                    list1.remove(notificationTemp);
                    shareDataDialogs.setDataNotificationTemp(list1);
                    shareDataDialogs.setReminder6("");
                }else{
                    shareDataDialogs.setReminder6("");
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDate dateDay = LocalDate.parse(day.get(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    dateDay = dateDay.minusDays(1);
                    String s = dateDay.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        rd1dAgo.setChecked(true);
                        rd1dAgoCheck.set(true);
                        List<String> list = shareDataDialogs.getDatareminders().getValue();
                        if (list == null) list = new ArrayList<>();
                        list.add(s + " " + time.get());
                        shareDataDialogs.setDatareminders(list);
                        NotificationTemp notificationTemp = new NotificationTemp(s, time.get());
                        List<NotificationTemp> list1 = shareDataDialogs.getDataNotificationTemp().getValue();
                        if (list1 == null) list1 = new ArrayList<>();
                        list1.add(notificationTemp);
                        shareDataDialogs.setDataNotificationTemp(list1);
                        shareDataDialogs.setReminder6(s + " " + time.get());
                }
            }
        });

        AtomicBoolean rd2dAgoCheck = new AtomicBoolean(rd2dAgo.isChecked());
        rd2dAgo.setOnClickListener(view -> {
            Log.e("rd2dAgo", "rd2dAgo: " + rd2dAgo.isClickable());
            if (!rd2dAgoCheck.get()){
                return;
            }
            if (rd2dAgoCheck.get()) {
                rd2dAgo.setChecked(false);
                rd2dAgoCheck.set(false);
                String data = shareDataDialogs.getReminder7().getValue();
                if (data != null && !data.isEmpty()) {
                    String time1 = data.split(" ")[1];
                    List<String> list = shareDataDialogs.getDatareminders().getValue();
                    if (list == null) list = new ArrayList<>();
                    list.remove(data);
                    shareDataDialogs.setDatareminders(list);
                    NotificationTemp notificationTemp = new NotificationTemp(day.get(), time1);
                    List<NotificationTemp> list1 = shareDataDialogs.getDataNotificationTemp().getValue();
                    if (list1 == null) list1 = new ArrayList<>();
                    list1.remove(notificationTemp);
                    shareDataDialogs.setDataNotificationTemp(list1);
                    shareDataDialogs.setReminder7("");
                } else {
                    shareDataDialogs.setReminder7("");
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDate dateDay = LocalDate.parse(day.get(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    dateDay = dateDay.minusDays(2);
                    String s = dateDay.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    rd2dAgo.setChecked(true);
                    rd2dAgoCheck.set(true);
                    List<String> list = shareDataDialogs.getDatareminders().getValue();
                    if (list == null) list = new ArrayList<>();
                    list.add(s + " " + time.get());
                    shareDataDialogs.setDatareminders(list);
                    NotificationTemp notificationTemp = new NotificationTemp(s, time.get());
                    List<NotificationTemp> list1 = shareDataDialogs.getDataNotificationTemp().getValue();
                    if (list1 == null) list1 = new ArrayList<>();
                    list1.add(notificationTemp);
                    shareDataDialogs.setDataNotificationTemp(list1);
                    shareDataDialogs.setReminder7(s + " " + time.get());
                }
            }
        });

        Log.e("list", "Datareminders: " + shareDataDialogs.getDatareminders().getValue());

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDataDialogs.getDatareminders().observe(getViewLifecycleOwner(), data -> {
                    Log.e("list", "Datareminders: " + data);
                    if (data.isEmpty()){
                        shareDataDialogs.setDataNotice("Không");
                        dialog.dismiss();
                    }else{
                        final String[] s = {""};
                        data.forEach(d -> {
                            s[0] = s[0] + d + ",";
                        });
                        shareDataDialogs.setDataNotice(s[0]);
                        dialog.dismiss();
                    }
                });

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDataDialogs.setDataNotice("Không");
                shareDataDialogs.setDataRepeat("Không");
                shareDataDialogs.setDataRepeatNo(0);
                shareDataDialogs.setDataRepeatType("Không");
                shareDataDialogs.setReminder("");
                shareDataDialogs.setReminder2("");
                shareDataDialogs.setReminder3("");
                shareDataDialogs.setReminder4("");
                shareDataDialogs.setReminder5("");
                shareDataDialogs.setReminder6("");
                shareDataDialogs.setReminder7("");
                shareDataDialogs.setDataTimeHour(0);
                shareDataDialogs.setDataTimeMinute(0);
                shareDataDialogs.setDatataskBesideList(new ArrayList<>());
                shareDataDialogs.setDatareminders(new ArrayList<>());
                shareDataDialogs.setDataNotificationTemp(new ArrayList<>());
                shareDataDialogs.setDataRemindType("Thông báo");
                dialog.dismiss();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void showBottomDialogRepeat() {
        Log.e("click", "showBottomDialogRepeat: onclick");
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_mission_add_calendar_repeat);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        dialog.getWindow().setGravity(Gravity.CENTER);

        RelativeLayout repeat = dialog.findViewById(R.id.repeat);
        SwitchCompat repeatSwitch = dialog.findViewById(R.id.repeat_switch);
        RelativeLayout repeatNo = dialog.findViewById(R.id.RepeatNo);
        RelativeLayout repeatType = dialog.findViewById(R.id.RepeatType);
        TextView setRepeatNo = dialog.findViewById(R.id.set_repeat_no);
        TextView setRepeatType = dialog.findViewById(R.id.set_repeat_type);

        shareDataDialogs.getDataRepeatNo().observe(getViewLifecycleOwner(), data -> {
            if (data != null && data != 0){
                setRepeatNo.setText(String.valueOf(data));
            }else {
                setRepeatNo.setText("Không");
            }
        });

        shareDataDialogs.getDataRepeatType().observe(getViewLifecycleOwner(), data -> {
            if (data != null && !data.isEmpty()){
                setRepeatType.setText(data);
            }else{
                setRepeatType.setText("Không");
            }
        });

//
//        shareDataDialogs.getDataRepeatBool().observe(getViewLifecycleOwner(), data -> {
//            if (data == 0){
//                repeatSwitch.setChecked(false);
//                repeatNo.setClickable(false);
//                repeatType.setClickable(false);
//                shareDataDialogs.setDataRepeatNo(0);
//                shareDataDialogs.setDataRepeatType("Không");
//                shareDataDialogs.setDataRepeat("Không");
//            }else{
//                repeatSwitch.setChecked(true);
//                repeatNo.setClickable(true);
//                repeatType.setClickable(true);
//                shareDataDialogs.setDataRepeat("Mỗi" + " " + time.get() + " " + type.get() + "(s)");
//            }
//        });

//
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeatSwitch.isChecked()){
                    repeatSwitch.setChecked(false);
                    repeatType.setClickable(false);
                    repeatNo.setClickable(false);
                    shareDataDialogs.setDataRepeat("Không");
                    shareDataDialogs.setDataRepeatNo(0);
                    shareDataDialogs.setDataRepeatType("Không");
                }else{
                    repeatSwitch.setChecked(true);
                    repeatType.setClickable(true);
                    repeatNo.setClickable(true);
                    shareDataDialogs.setDataRepeatNo(1);
                    shareDataDialogs.setDataRepeatType("Giờ");
                    shareDataDialogs.setDataRepeat("Mỗi" + " " + 1 + " " + "Giờ" + "(s)");
                }
            }
        });

        repeatSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeatSwitch.isChecked()){
                    repeatSwitch.setChecked(false);
                    shareDataDialogs.setDataRepeat("Không");
                    shareDataDialogs.setDataRepeatNo(0);
                    shareDataDialogs.setDataRepeatType("Không");
                    repeatType.setClickable(false);
                    repeatNo.setClickable(false);
                }else{
                    repeatSwitch.setChecked(true);
                    repeatType.setClickable(true);
                    repeatNo.setClickable(true);
                    shareDataDialogs.setDataRepeatNo(1);
                    shareDataDialogs.setDataRepeatType("Giờ");
                    shareDataDialogs.setDataRepeat("Mỗi" + " " + 1 + " " + "Giờ" + "(s)");
                }
            }
        });

        repeatType.setOnClickListener(view -> {
            final String[] items = new String[4];
            items[0] = "Giờ";
            items[1] = "Ngày";
            items[2] = "Tuần";
            items[3] = "Tháng";

            // Create List Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this.requireContext());
            builder.setTitle("Chọn thể loại");
            builder.setItems(items, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int item) {
                    if (item != 0 && item != 1 && item != 2 && item != 3){
                        shareDataDialogs.setDataRepeatType("Giờ");
                        int time;
                        if (shareDataDialogs.getDataRepeatNo().getValue() == null || shareDataDialogs.getDataRepeatNo().getValue() == 0){
                            time = 1;
                        }else{
                            time = shareDataDialogs.getDataRepeatNo().getValue();
                        }
                        shareDataDialogs.setDataRepeat("Mỗi" + " " + time + " " + "Giờ" + "(s)");
                        dialog.dismiss();
                    }
                    shareDataDialogs.setDataRepeatType(items[item]);
                    int time;
                    if (shareDataDialogs.getDataRepeatNo().getValue() == null || shareDataDialogs.getDataRepeatNo().getValue() == 0){
                        time = 1;
                    }else{
                        time = shareDataDialogs.getDataRepeatNo().getValue();
                    }
                    shareDataDialogs.setDataRepeat("Mỗi" + " " + time + " " + items[item] + "(s)");
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        });

        repeatNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Nhập số lần lặp lại");

                // Create EditText box to input repeat number
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(input);
                alert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (input.getText().toString().isEmpty() || input.getText().toString().equals("0")) {
                                    shareDataDialogs.setDataRepeatNo(1);
                                }else{
                                    shareDataDialogs.setDataRepeatNo(Integer.parseInt(input.getText().toString()));
                                }
                                String type = shareDataDialogs.getDataRepeatType().getValue();
                                if (type == null || type.isEmpty()){
                                    type = "Giờ";
                                }
                                int time;
                                if (shareDataDialogs.getDataRepeatNo().getValue() == null || shareDataDialogs.getDataRepeatNo().getValue() == 0){
                                    time = 1;
                                }else{
                                    time = shareDataDialogs.getDataRepeatNo().getValue();
                                }
                                shareDataDialogs.setDataRepeat("Mỗi" + " " + time + " " + type + "(s)");

                                dialog.dismiss();
                            }
                        });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        shareDataDialogs.setDataRepeatNo(1);
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch(id){
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
                        ToDoDBContract.MissionEntry.MISSION_REMINDER_TYPE,
                        ToDoDBContract.MissionEntry.MISSION_RINGTONE_ID,
                        ToDoDBContract.MissionEntry.MISSION_DESCRIPTION,
                        ToDoDBContract.MissionEntry.MISSION_CATEGORY_ID,
                        ToDoDBContract.MissionEntry.MISSION_STICKER_ID,
                        ToDoDBContract.MissionEntry.MISSION_isSTICKER,
                        ToDoDBContract.MissionEntry.MISSION_isACTIVE
                };
                return new CursorLoader(this.requireContext(), ToDoDBContract.MissionEntry.CONTENT_URI, projection, null, null, null);
            case EXISTING_CATEGORIES_LOADER:
                String[] projection1 = {
                        ToDoDBContract.CategoryEntry.CATEGORY_USER_ID,
                        ToDoDBContract.CategoryEntry.CATEGORY_TITLE,
                        ToDoDBContract.CategoryEntry.CATEGORY_USER_ID
                };
                return new CursorLoader(this.requireContext(), ToDoDBContract.CategoryEntry.CONTENT_URI, projection1, null, null, null);
            case EXISTING_STICKERS_LOADER:
                String[] projection2 = {
                        ToDoDBContract.StickerEntry.STICKER_ID,
                        ToDoDBContract.StickerEntry.STICKER_TITLE,
                        ToDoDBContract.StickerEntry.STICKER_PATH
                };
                return new CursorLoader(this.requireContext(), ToDoDBContract.StickerEntry.CONTENT_URI, projection2, null, null, null);
            case EXISTING_RINGTONES_LOADER:
                String[] projection3 = {
                        ToDoDBContract.RingtoneEntry.RINGTONE_ID,
                        ToDoDBContract.RingtoneEntry.RINGTONE_TITLE,
                        ToDoDBContract.RingtoneEntry.RINGTONE_PATH
                };
                return new CursorLoader(this.requireContext(), ToDoDBContract.RingtoneEntry.CONTENT_URI, projection3, null, null, null);
            case EXISTING_TASKBESIDES_LOADER:
                String[] projection4 = {
                        ToDoDBContract.TaskbesideEntry.TASKBESIDE_ID,
                        ToDoDBContract.TaskbesideEntry.TASKBESIDE_TITLE,
                        ToDoDBContract.TaskbesideEntry.TASKBESIDE_MISSION_ID
                };
                return new CursorLoader(this.requireContext(), ToDoDBContract.TaskbesideEntry.CONTENT_URI, projection4, null, null, null);
            case EXISTING_NOTIFICATIONS_LOADER:
                String[] projection5 = {
                        ToDoDBContract.NotificationEntry.NOTIFICATION_ID,
                        ToDoDBContract.NotificationEntry.NOTIFICATION_TIME,
                        ToDoDBContract.NotificationEntry.NOTIFICATION_DATE,
                        ToDoDBContract.NotificationEntry.NOTIFICATION_MISSION_ID
                };
                return new CursorLoader(this.requireContext(), ToDoDBContract.NotificationEntry.CONTENT_URI, projection5, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        List<Ringtone> ringtoneList = new ArrayList<>();
        List<Sticker> stickerList = new ArrayList<>();
        List<Taskbeside> taskbesideList = new ArrayList<>();
        List<Notification> notificationList = new ArrayList<>();
        List<Category> mcategoryList = new ArrayList<>();
        List<Mission> missionList = new ArrayList<>();
        switch (loader.getId()) {
            case EXISTING_MISIONS_LOADER:
                if (data == null || data.getCount() < 1) {
                    return;
                }
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
                        @SuppressLint("Range") String reminderType = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_REMINDER_TYPE));
                        @SuppressLint("Range") String isActive = data.getString(data.getColumnIndex(ToDoDBContract.MissionEntry.MISSION_isACTIVE));
                        Mission mission = new Mission(sticker_id, ringTone_id, date, describe, isNotify, isRepeat, repeatType, mission_id, time, title, category_id, repeatNo, reminderType, isSticker, isActive);
                        missionList.add(mission);
                    } while (data.moveToNext());
                }
                break;
            case EXISTING_CATEGORIES_LOADER:
                if (data == null || data.getCount() < 1) {
                    return;
                }
                while (data.moveToNext()) {
                    int id = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.CategoryEntry.CATEGORY_USER_ID));
                    String title = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.CategoryEntry.CATEGORY_TITLE));
                    int userId = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.CategoryEntry.CATEGORY_USER_ID));
                    mcategoryList.add(new Category(id, title, userId));
                }
                break;
            case EXISTING_STICKERS_LOADER:
                if (data == null || data.getCount() < 1) {
                    return;
                }
                while (data.moveToNext()) {
                    int id = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.StickerEntry.STICKER_ID));
                    String title = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.StickerEntry.STICKER_TITLE));
                    String path = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.StickerEntry.STICKER_PATH));
                    stickerList.add(new Sticker(id, title, path));
                }
                break;
            case EXISTING_RINGTONES_LOADER:
                if (data == null || data.getCount() < 1) {
                    return;
                }
                while (data.moveToNext()) {
                    int id = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.RingtoneEntry.RINGTONE_ID));
                    String title = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.RingtoneEntry.RINGTONE_TITLE));
                    String path = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.RingtoneEntry.RINGTONE_PATH));
                    ringtoneList.add(new Ringtone(id, title, path));
                }
                break;
            case EXISTING_TASKBESIDES_LOADER:
                if (data == null || data.getCount() < 1) {
                    return;
                }
                while (data.moveToNext()) {
                    int id = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.TaskbesideEntry.TASKBESIDE_ID));
                    String title = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.TaskbesideEntry.TASKBESIDE_TITLE));
                    int mission_id = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.TaskbesideEntry.TASKBESIDE_MISSION_ID));
                    taskbesideList.add(new Taskbeside(id, title, mission_id));
                }
                break;
            case EXISTING_NOTIFICATIONS_LOADER:
                if (data == null || data.getCount() < 1) {
                    return;
                }
                while (data.moveToNext()) {
                    int id = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.NotificationEntry.NOTIFICATION_ID));
                    String time = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.NotificationEntry.NOTIFICATION_TIME));
                    int mission_id = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.NotificationEntry.NOTIFICATION_MISSION_ID));
                    @SuppressLint("Range") String date = data.getString(data.getColumnIndex(ToDoDBContract.NotificationEntry.NOTIFICATION_DATE));
                    notificationList.add(new Notification(id, time, mission_id, date));
                }
                break;
            default:
                return;
        }
        if (!mcategoryList.isEmpty()) {
            mcategoryList = mcategoryList.stream().filter(category -> category.getUser_id() == user).collect(Collectors.toList());
            shareDataDialogs.setCategoryList(mcategoryList);
            Log.e("mcategoryList1", "onLoadFinished: " + mcategoryList.size());
        }
        shareDataDialogs.setDataCategory(category_id_1);
        if (!missionList.isEmpty()) {
            Toast.makeText(getContext(), "data2" + missionList.size(), Toast.LENGTH_SHORT).show();
            List<Mission> mainList = new ArrayList<>();
            if (shareDataDialogs.getCategoryList().getValue() == null){
                shareDataDialogs.setCategoryList(new ArrayList<>());
                shareDataDialogs.setMainList(mainList);
            }else{
                for (Category category : shareDataDialogs.getCategoryList().getValue()) {
                    Log.e("loop1", "category: " + category.getCategory_id());
                    for (Mission mission : missionList) {
                        Log.e("loop1", "mission: " + mission.getCategory_id());
                        if (category.getCategory_id() == mission.getCategory_id()) {
                            mainList.add(mission);
                        }
                    }
                }
                Log.e("checkcolumn2", "onLoadFinished: " + mainList.size() + " " + mcategoryList.size());
                shareDataDialogs.setMainList(mainList);
                Log.e("mainlist", "onLoadFinished: " + shareDataDialogs.getMainList().getValue().size());
            }
        }
        if (!stickerList.isEmpty()) {
            shareDataDialogs.setStickerList(stickerList);
            Log.e("checkcolumn3", "onLoadFinished: " + stickerList.size());
        }else{
            shareDataDialogs.setStickerList(new ArrayList<>());
        }
        if (!ringtoneList.isEmpty()) {
            shareDataDialogs.setRingtoneList(ringtoneList);
            Log.e("checkcolumn4", "onLoadFinished: " + ringtoneList.size());
        }else {
            shareDataDialogs.setRingtoneList(new ArrayList<>());
        }
        if (!taskbesideList.isEmpty()) {
            shareDataDialogs.setTaskbesideList(taskbesideList);
            Log.e("checkcolumn5", "onLoadFinished: " + taskbesideList.size());
        }else{
            shareDataDialogs.setTaskbesideList(new ArrayList<>());
        }
        if (!notificationList.isEmpty()) {
            shareDataDialogs.setNotificationList(notificationList);
            Log.e("checkcolumn6", "onLoadFinished: " + notificationList.size());
        }else {
            shareDataDialogs.setNotificationList(new ArrayList<>());
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(int position, int id, boolean isChosing, int selectionItem) {
        Toast.makeText(getContext(), "pos: " + position + "id:" + id, Toast.LENGTH_SHORT).show();
        shareDataDialogs.setDataCategory(id);
    }

    @Override
    public void onRestart() {

    }

    public void onDestroy(){
        super.onDestroy();
        shareDataDialogs.setDataCheckFirsTimeIN("true");
    }

    @Override
    public void onItemClick(int position, String name) {
        Toast.makeText(getContext(), "name: " + position, Toast.LENGTH_SHORT).show();
        List<TaskString> taskStringList = shareDataDialogs.getDatataskBesideList().getValue();
        if (taskStringList == null){
            Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
            taskStringList = new ArrayList<>();
        }
        taskStringList.remove(position);
        shareDataDialogs.setDatataskBesideList(taskStringList);
    }

    @Override
    public void onItemClick(int position, int id_ringtone) {
        shareDataDialogs.setDataRingTone(id_ringtone);
    }

    @Override
    public void onClickSticker(int sticker_id, int position) {
        shareDataDialogs.setDataSticker(sticker_id);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof MissionAdapter.MissionViewHolder) {
            AlertDialog alert = new AlertDialog.Builder(getContext()).create();
            alert.setTitle("Bạn có chắc chắn muốn xóa");
            alert.setCanceledOnTouchOutside(true);
            alert.setButton(AlertDialog.BUTTON_POSITIVE, "Có", (dialog, which) -> {
                Mission mission = Objects.requireNonNull(shareDataDialogs.getMainList().getValue()).get(position);
                String s = mission.getTitle();
                missionAdapter.removeItem(position);
                dialog.dismiss();
                Snackbar snackbar = Snackbar.make(relativeLayout, s + "remove", Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", view -> {
                    //restore item
                    missionAdapter.restoreItem(mission, position);
                });
                snackbar.addCallback(new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        DAO.deleteMission(getContext(), mission.getMission_id(), currentUser);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            });
            alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Không", (dialog, which) -> dialog.dismiss());
            alert.show();
        }
    }
}
