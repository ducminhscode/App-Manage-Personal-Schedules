package com.example.applicationproject.View_Controller.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.Model.Category;
import com.example.applicationproject.Model.Mission;
import com.example.applicationproject.Model.Notification;
import com.example.applicationproject.Model.Ringtone;
import com.example.applicationproject.Model.Sticker;
import com.example.applicationproject.Model.Taskbeside;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.Adapter.ItemCategoryAdapter;
import com.example.applicationproject.View_Controller.Adapter.RingToneUserAdapter;
import com.example.applicationproject.View_Controller.Adapter.StickerAdapter;
import com.example.applicationproject.View_Controller.Adapter.TaskBesideAdapter;
import com.example.applicationproject.View_Controller.ClassTemp.NotificationTemp;
import com.example.applicationproject.View_Controller.ClassTemp.TaskString;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.FragmentLogin.MissionFragment;
import com.example.applicationproject.View_Controller.Service.AlarmScheduler;
import com.example.applicationproject.View_Controller.ShareDataMission;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class DetailMissionActivity extends AppCompatActivity implements ItemCategoryAdapter.OnClickItemCategoryItem, StickerAdapter.OnClickStickerListener, TaskBesideAdapter.OnClickItemTaskBeside, RingToneUserAdapter.OnClickItemRingtone {

    private ItemCategoryAdapter itemCategoryAdapter;
    private int id;
    private String userName;
    private ShareDataMission shareDataDialogs;
    private Button btn;
    private ImageButton imagBack, imagSticker, imgSave;
    private EditText ediText;
    private RecyclerView recyclerView;
    private LinearLayout layOutNV, layOutTime, layOutNotify, layOutRepeat, layOutNote;
    private TextView tvDate, tvTime, tvRepeat;
    private LocalDate localDate;
    private TaskBesideAdapter taskBesideAdapter;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;
    private Mission missionMain;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localDate = LocalDate.now();
        }
        shareDataDialogs = MissionFragment.shareDataDialogs;
        int id_mission = getIntent().getIntExtra("id_mission", 0);
        userName = getIntent().getStringExtra("name_user");
        if (id_mission != 0) {
            id = id_mission;
            List<Mission> missionList = shareDataDialogs.getMainList().getValue();
            if (missionList == null) {
                missionList = new ArrayList<>();
            }
            missionMain = missionList.stream().filter(mission -> mission.getMission_id() == id).findFirst().get();
            List<Taskbeside> taskbesideList = shareDataDialogs.getTaskbesideList().getValue();
            if (taskbesideList == null) {
                taskbesideList = new ArrayList<>();
            }else{
                taskbesideList = taskbesideList.stream().filter(v -> v.getMission_id() == id).collect(Collectors.toList());
            }
            shareDataDialogs.setTaskbesideList(taskbesideList);
            List<Notification> notificationList = shareDataDialogs.getNotificationList().getValue();
            if (notificationList == null) {
                notificationList = new ArrayList<>();
            }else{
                notificationList = notificationList.stream().filter(v -> v.getMission_id() == id).collect(Collectors.toList());
            }
            shareDataDialogs.setNotificationList(notificationList);
            List<Ringtone> ringtoneList = shareDataDialogs.getRingtoneList().getValue();
            if (ringtoneList == null) {
                ringtoneList = new ArrayList<>();
            }
            List<Category> categoryList = shareDataDialogs.getCategoryList().getValue();
            if (categoryList == null) {
                categoryList = new ArrayList<>();
            }
            List<Sticker> St = shareDataDialogs.getStickerList().getValue();
            if (St == null) {
                St = new ArrayList<>();
            }
            shareDataDialogs.setDataCategory(missionMain.getCategory_id());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                shareDataDialogs.setDataTime(missionMain.getTime());
            }
            if (shareDataDialogs.getNotificationList().getValue().isEmpty()){
                shareDataDialogs.setDataNotice("Không");
            }else{
                List<String> list = new ArrayList<>();
                String[] s = {""};
                List<NotificationTemp> list1 = new ArrayList<>();
                for (Notification notification : shareDataDialogs.getNotificationList().getValue()) {
                    list1.add(new NotificationTemp(notification.getDate(), notification.getTime()));
                    list.add(notification.getDate() + " " + notification.getTime());
                    s[0] = s[0] + notification.getDate() + " " + notification.getTime() + ",";
                }
                shareDataDialogs.setDataNotificationTemp(list1);
                shareDataDialogs.setDatareminders(list);
                shareDataDialogs.setDataNotice(s[0]);
            }
            if (missionMain.isRepeat().equals("False")){
                shareDataDialogs.setDataRepeat("Không");
            }else{
                String count = missionMain.getRepeatNo();
                String type = missionMain.getReminderType();
                if (count.equals("Không")){
                    shareDataDialogs.setDataRepeat("Không");
                    shareDataDialogs.setDataRepeatNo(0);
                }else if (type.equals("Không")){
                    shareDataDialogs.setDataRepeat("Không");
                    shareDataDialogs.setDataRepeatType("Không");
                }else{
                    shareDataDialogs.setDataRepeatNo(Integer.valueOf(count));
                    shareDataDialogs.setDataRepeatType(type);
                    shareDataDialogs.setDataRepeat("Mỗi" + " " + count + " " + type + "(s)");
                }
            }

            shareDataDialogs.setReminder("");
            shareDataDialogs.setReminder2("");
            shareDataDialogs.setReminder3("");
            shareDataDialogs.setReminder4("");
            shareDataDialogs.setReminder5("");
            shareDataDialogs.setReminder6("");
            shareDataDialogs.setReminder7("");

            if (!shareDataDialogs.getNotificationList().getValue().isEmpty()){
                for (Notification notification : shareDataDialogs.getNotificationList().getValue()) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        LocalDate date = LocalDate.parse(notification.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        LocalTime time = LocalTime.parse(notification.getTime(), DateTimeFormatter.ofPattern("H:mm"));
                        LocalDate dateSelected = LocalDate.parse(missionMain.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        LocalTime timeSelected = LocalTime.parse(missionMain.getTime(), DateTimeFormatter.ofPattern("H:mm"));
                        int hour = time.getHour();
                        int minute = time.getMinute();
                        if (date.equals(dateSelected)){
                            if (hour == timeSelected.getHour()){
                                switch (minute - timeSelected.getMinute()){
                                    case 0:
                                        shareDataDialogs.setReminder(date + " " + time);
                                        break;
                                    case 5:
                                        shareDataDialogs.setReminder2(date + " " + time);
                                        break;
                                    case 10:
                                        shareDataDialogs.setReminder3(date + " " + time);
                                        break;
                                    case 15:
                                        shareDataDialogs.setReminder4(date + " " + time);
                                        break;
                                    case 30:
                                        shareDataDialogs.setReminder5(date + " " + time);
                                        break;
                                }
                            }else{
                                switch (60 + timeSelected.getMinute() - minute){
                                    case 0:
                                        shareDataDialogs.setReminder(date + " " + time);
                                        break;
                                    case 5:
                                        shareDataDialogs.setReminder2(date + " " + time);
                                        break;
                                    case 10:
                                        shareDataDialogs.setReminder3(date + " " + time);
                                        break;
                                    case 15:
                                        shareDataDialogs.setReminder4(date + " " + time);
                                        break;
                                    case 30:
                                        shareDataDialogs.setReminder5(date + " " + time);
                                        break;
                                }
                            }
                        }else{
                            if (date.minusDays(1).equals(dateSelected)){
                                shareDataDialogs.setReminder6(date + " " + time);
                            }
                            if (date.minusDays(2).equals(dateSelected)){
                                shareDataDialogs.setReminder7(date + " " + time);
                            }
                        }
                    }
                }
            }
            shareDataDialogs.setDataSticker(missionMain.getSticker_id());
            shareDataDialogs.setDataRingTone(missionMain.getRingTone_id());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalTime localTime = LocalTime.parse(missionMain.getTime(), DateTimeFormatter.ofPattern("H:mm"));
                shareDataDialogs.setDataTimeHour(localTime.getHour());
                shareDataDialogs.setDataTimeMinute(localTime.getMinute());
            }

            List<TaskString> taskStringList = new ArrayList<>();
            if (!taskbesideList.isEmpty()){
                for (Taskbeside taskbeside : taskbesideList) {
                    taskStringList.add(new TaskString(taskbeside.getName()));
                }
            }

            shareDataDialogs.setDatataskBesideList(taskStringList);
            shareDataDialogs.setDataRemindType(missionMain.getReminderType());
            shareDataDialogs.setDecription(missionMain.getDescribe() == null ? "" : missionMain.getDescribe());
            shareDataDialogs.setTitle(missionMain.getTitle());

            initwiget();

            shareDataDialogs.getDataCategory().observe(this, data -> {
                if (data != null){
                    btn.setText(data);
                }else{
                    btn.setText("Category");
                }
            });

            shareDataDialogs.getDataSticker().observe(this, data -> {
                if (data != null){
                    imagSticker.setImageURI(Uri.parse(shareDataDialogs.getStickerList().getValue().stream().filter(sticker -> sticker.getSticker_id() == data).findFirst().get().getSticker_path()));
                }
            });

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            taskBesideAdapter = new TaskBesideAdapter(this, this);
            recyclerView.setAdapter(taskBesideAdapter);

            shareDataDialogs.getDatataskBesideList().observe(this, data -> {
                if (data != null){
                    taskBesideAdapter.setTaskBesideList(data);
                    recyclerView.setVisibility(View.VISIBLE);
                }else {
                    recyclerView.setVisibility(View.GONE);
                }
            });


            layOutNV.setOnClickListener(view -> {
                TaskString taskString = new TaskString("");
                List<TaskString> list = shareDataDialogs.getDatataskBesideList().getValue();
                if (list == null){
                    list = new ArrayList<>();
                }
                list.add(taskString);
                shareDataDialogs.setDatataskBesideList(list);
            });

            layOutNote.setOnClickListener(view -> {
                showDialogNote();
            });

            btn.setOnClickListener(view -> {
                showDialogCategory();
            });

            imagSticker.setOnClickListener(view -> {
                showDialogTemplate();
            });

            layOutTime.setOnClickListener(view -> {
                showBottomDialogCalendar();
            });

            layOutNotify.setOnClickListener(view -> {
                showBottomDialogCalendar();
            });

            layOutRepeat.setOnClickListener(view -> {
                showBottomDialogCalendar();
            });

            imagBack.setOnClickListener(view -> {
                finish();
            });

            shareDataDialogs.getTitle().observe(this, data -> {
                ediText.setText(data);
            });

            imgSave.setOnClickListener(view -> {
                AtomicInteger sticker_id = new AtomicInteger();
                shareDataDialogs.getDataSticker().observe(this, sticker_id::set);
                AtomicInteger ringTone_id = new AtomicInteger();
                shareDataDialogs.getDataRingTone().observe(this, ringTone_id::set);
                AtomicReference<String> date = new AtomicReference<>();
                shareDataDialogs.getDataDate().observe(this, date::set);
                String describe = shareDataDialogs.getDecription().getValue();
                AtomicReference<String> isNotify = new AtomicReference<>();
                shareDataDialogs.getDataNotice().observe(this, data -> {
                    if (data.equals("Không")){
                        isNotify.set("False");
                    }else{
                        isNotify.set("True");
                    }
                });
                AtomicReference<String> isRepeat = new AtomicReference<>();
                shareDataDialogs.getDataRepeat().observe(this, data -> {
                    if (data.equals("Không")){
                        isRepeat.set("False");
                    }else{
                        isRepeat.set("True");
                    }
                });
                AtomicReference<String> repeatType = new AtomicReference<>();
                shareDataDialogs.getDataRepeatType().observe(this, repeatType::set);
                AtomicReference<String> time = new AtomicReference<>();
                shareDataDialogs.getDataTime().observe(this, time::set);
                String title = ediText.getText().toString();
                AtomicInteger category_id = new AtomicInteger();
                shareDataDialogs.getDataCategory().observe(this, category_id::set);
                AtomicReference<String> repeatNo = new AtomicReference<>();
                shareDataDialogs.getDataRepeatNo().observe(this, data -> {
                    if (data == 0){
                        repeatNo.set("Không");
                    }else{
                        repeatNo.set(data.toString());
                    }
                });
                AtomicReference<String> reminderType = new AtomicReference<>();
                shareDataDialogs.getDataRemindType().observe(this, reminderType::set);
                String isSticker;
                if (sticker_id.get() == 0){
                    isSticker = "False";
                }else{
                    isSticker = "True";
                }
                String isActive = missionMain.getIsActive();

                if (date.get().equals("Không")){
                    Toast.makeText(this, "Ngày không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (time.get().equals("Không")) {
                    Toast.makeText(this, "Thời gian không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (title.isEmpty()){
                    Toast.makeText(this, "Tiêu đề không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    boolean check = DAO.updateMission(this, sticker_id.get(), ringTone_id.get(), date.get(), describe,
                            isNotify.get(), isRepeat.get(), repeatType.get(), time.get(), title,
                            category_id.get(), repeatNo.get(), reminderType.get(), isSticker, isActive, id);
                    if (check){
                        AtomicReference<String> retype = new AtomicReference<>();
                        shareDataDialogs.getDataRemindType().observe(this, retype::set);

                        AtomicInteger reNo = new AtomicInteger();
                        shareDataDialogs.getDataRepeatNo().observe(this, reNo::set);

                        List<NotificationTemp> notificationNewTemps = shareDataDialogs.getDataNotificationTemp().getValue();
                        if (notificationNewTemps == null){
                            notificationNewTemps = new ArrayList<>();
                        }
                        if (notificationNewTemps.isEmpty()){
                            Toast.makeText(this, "Không có lời nhắc nào!!", Toast.LENGTH_SHORT).show();
                        }else{
                            for (Notification notification : shareDataDialogs.getNotificationList().getValue()){
                                boolean checked = DAO.deleteNotification(this, notification.getNotification_id());
                                new AlarmScheduler().cancelAlarm(this, id, notification.getNotification_id(), userName);
                                if (!checked){
                                    Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                            for (NotificationTemp notificationTemp : notificationNewTemps){
                                boolean checkNotification = DAO.insertNotification(this, notificationTemp.getTime(), notificationTemp.getDate(), id);
                                if (!checkNotification){
                                    Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                    return;
                                }else{
                                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    if (DAO.findnotificationbyTimeDate(this, notificationTemp.getTime(), notificationTemp.getDate()) == -1){
                                        Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                        return;
                                    }else{
                                        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                        int id_notification = DAO.findnotificationbyTimeDate(this, notificationTemp.getTime(), notificationTemp.getDate());
                                        setalarm(notificationTemp.getTime(), notificationTemp.getDate(), id, retype.get(), reNo.get(), id_notification);
                                    }
                                }
                            }
                        }

                        List<TaskString> taskbesides = shareDataDialogs.getDatataskBesideList().getValue();
                        if (taskbesides == null){
                            taskbesides = new ArrayList<>();
                        }
                        if (taskbesides.isEmpty()){
                            Toast.makeText(this, "Không có task beside nào!!", Toast.LENGTH_SHORT).show();
                        }else{
                            if (!shareDataDialogs.getTaskbesideList().getValue().isEmpty()){
                                for (Taskbeside taskbeside : shareDataDialogs.getTaskbesideList().getValue()){
                                    boolean checked = DAO.deleteTaskbeside(this, taskbeside.getTaskbeside_id());
                                    if (!checked){
                                        Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                        return;
                                    }else{
                                        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            for (TaskString taskString : taskbesides) {
                                boolean checkTaskBeside = DAO.insertTaskbeside(this, taskString.getTaskString(), id);
                                if (!checkTaskBeside) {
                                    Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
        }else{
            Toast.makeText(this, "Không có id", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setalarm(String time, String date, int mission_id, String type, int repeatNo, int notify_id){
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
                new AlarmScheduler().setAlarm(this, selectedTimestamp, mission_id, notify_id, userName);
                Toast.makeText(this, "Đặt lời nhắc thành công" + selectedTimestamp, Toast.LENGTH_SHORT).show();
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
                new AlarmScheduler().setRepeatAlarm(this, selectedTimestamp, mission_id, mRepeat, notify_id, userName);
                Toast.makeText(this, "Đặt lời nhắc thành công" + selectedTimestamp, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void showDialogNote() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.decriptionnotelayout);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        EditText editText = dialog.findViewById(R.id.editTextMultiLine);
        shareDataDialogs.getDecription().observe(this, data -> {
            if (data != null)
            {
                editText.setText(data);
            }else{
                editText.setText("");
            }
        });
        if (editText.getText()!= null && !editText.getText().toString().isEmpty()){
            shareDataDialogs.setDecription(editText.getText().toString());
        }else{
            shareDataDialogs.setDecription("");
        }
    }

    private void initwiget() {
        btn = findViewById(R.id.btn_category);
        imagBack = findViewById(R.id.imageBack);
        imagSticker = findViewById(R.id.image_sticker);
        imgSave = findViewById(R.id.imageCheck);
        ediText = findViewById(R.id.et_title);
        recyclerView = findViewById(R.id.recyclerView);
        layOutNV = findViewById(R.id.btn_add_task);
        layOutTime = findViewById(R.id.btn_add_calendarTime);
        layOutNotify = findViewById(R.id.btn_add_notification);
        layOutRepeat = findViewById(R.id.btn_add_repeat);
        layOutNote = findViewById(R.id.btn_add_note);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvRepeat = findViewById(R.id.tvRepeat);
    }

    private void showDialogCategory() {
        final Dialog dialog = new Dialog(this);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        itemCategoryAdapter = new ItemCategoryAdapter(this, this, true);
        shareDataDialogs.getCategoryList().observe(this, categories -> {
            if (categories != null){
                itemCategoryAdapter.setCategoryList(categories);
            }else{
                itemCategoryAdapter.setCategoryList(new ArrayList<>());
            }
        });
        recyclerView.setAdapter(itemCategoryAdapter);
        shareDataDialogs.getDataCategory().observe(this, data -> {
            if (data != null){
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
            Intent intent = new Intent(this, ManageListActivity.class);
            intent.putExtra("name", userName);
            startActivity(intent);
        });
    }

    private void showDialogTemplate() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sticker_layout);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        dialog.getWindow().setGravity(Gravity.CENTER);

        RecyclerView recyclerView = dialog.findViewById(R.id.listItem_stickers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        StickerAdapter stickerAdapter = new StickerAdapter(this, this);
        recyclerView.setAdapter(stickerAdapter);
        shareDataDialogs.getStickerList().observe(this, stickers -> {
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


    private void showBottomDialogCalendar() {

        //Xuất hiện dialog chọn ngày giờ
        final Dialog dialog = new Dialog(this);
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

        shareDataDialogs.getDataDate().observe(this, data -> {
            if (data != null && !data.equals("Không")){
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate lDate = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    datePicker.updateDate(lDate.getYear(), lDate.getMonthValue() - 1, lDate.getDayOfMonth());
                }
            }
        });

        datePicker.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate localdate1 = LocalDate.of(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                if (localdate1.isAfter(localDate) || localdate1.isEqual(localDate)){
                    shareDataDialogs.setDataDate(localdate1.format(formatter));
                    layoutTime.setClickable(true);
                }else{
                    Toast.makeText(this, "Ngày không hợp lệ", Toast.LENGTH_SHORT).show();
                    layoutTime.setClickable(false);
                    shareDataDialogs.setDataTime("Không");
                    shareDataDialogs.setDataDate("Không");
                }
            }

        });

        shareDataDialogs.getDataTime().observe(this, data -> {
            TextView textViewTime = dialog.findViewById(R.id.textViewTime);
            textViewTime.setText(data);
            if (data.equals("Không")) {
                layoutNotice.setClickable(false);
                shareDataDialogs.setDataNotice("Không");
            }else{
                layoutNotice.setClickable(true);
            }
        });

        shareDataDialogs.getDataNotice().observe(this, data -> {
            TextView textViewNotice = dialog.findViewById(R.id.textViewNotice);
            textViewNotice.setText(data);
            if (data.equals("Không")){
                layoutRemind.setVisibility(View.GONE);
                layoutRepeat.setClickable(false);
                shareDataDialogs.setDataRepeatType("Không");
            }else{
                layoutRemind.setVisibility(View.VISIBLE);
            }
        });

        shareDataDialogs.getDataRemindType().observe(this, data -> {
            TextView textViewRemind = dialog.findViewById(R.id.textViewTypeofReminder);
            textViewRemind.setText(data);
        });

        shareDataDialogs.getDataRepeat().observe(this, data -> {
            TextView textViewRepeat = dialog.findViewById(R.id.textViewRepeat);
            textViewRepeat.setText(data);
        });

        layoutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialogTimePicker();
            }
        });

        layoutNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialogNotification();
            }
        });

        layoutRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialogRepeat();
            }
        });

        layoutRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialogReminder();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDataDialogs.setDataDate("Không");
                shareDataDialogs.setDataTime("Không");
                shareDataDialogs.setDataNotice("Không");
                shareDataDialogs.setDataRepeat("Không");
                dialog.dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtomicReference<String> time = new AtomicReference<>();
                shareDataDialogs.getDataTime().observe(DetailMissionActivity.this, time::set);
                AtomicReference<String> date = new AtomicReference<>();
                shareDataDialogs.getDataDate().observe(DetailMissionActivity.this, date::set);
                if (date.get().equals("Không")){
                    dialog.dismiss();
                }
                int id = DAO.findmissionbyDateTime(getBaseContext(), date.get(), time.get());
                if(id != -1){
                    Toast.makeText(getBaseContext(), "Đã tồn tại", Toast.LENGTH_SHORT).show();
                    shareDataDialogs.setDataDate("Không");
                    shareDataDialogs.setDataTime("Không");
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                }
            }
        });

    }

    private void showBottomDialogTimePicker() {

        //Xuất hiện dialog chọn thời gian
        final Dialog dialog = new Dialog(this);
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

        shareDataDialogs.getDataTime().observe(this, data -> {
            if (!data.equals("Không")){
                LocalTime localTime = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    localTime = LocalTime.parse(data, DateTimeFormatter.ofPattern("H:mm"));
                    timePicker.setHour(localTime.getHour());
                    timePicker.setMinute(localTime.getMinute());
                }
            }
        });

        shareDataDialogs.getDataTimeHour().observe(this, data -> {
            if (data != null){
                timePicker.setHour(data);
            }
        });

        shareDataDialogs.getDataTimeMinute().observe(this, data -> {
            if (data != null){
                timePicker.setMinute(data);
            }
        });

        timePicker.setOnTimeChangedListener((timePicker1, hourChange, minuteChange) -> {
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        shareDataDialogs.getDataDate().observe(DetailMissionActivity.this, data -> {
                            if (!data.equals("Không")){
                                LocalDate localDate1 = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                if (localDate1.equals(localDate)){
                                    LocalTime localTime = LocalTime.now();
                                    LocalTime localTime1;
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
                                    if (minuteChange < 10){
                                        localTime1 = LocalTime.parse(hourChange + ":" + "0" + minuteChange, formatter);
                                    }else{
                                        localTime1 = LocalTime.parse(hourChange + ":" + minuteChange, formatter);
                                    }
                                    if (localTime1.isAfter(localTime)){
                                        shareDataDialogs.setDataTime(String.valueOf(localTime1));
                                        shareDataDialogs.setDataTimeHour(hourChange);
                                        shareDataDialogs.setDataTimeMinute(minuteChange);
                                        dialog.dismiss();
                                    }else{
                                        Toast.makeText(DetailMissionActivity.this, "Thời gian không hợp lệ", Toast.LENGTH_SHORT).show();
                                        shareDataDialogs.setDataTime("Không");
                                        shareDataDialogs.setDataTimeHour(0);
                                        shareDataDialogs.setDataTimeMinute(0);
                                        dialog.dismiss();
                                    }
                                }
                            }
                        });

                    }
                }
            });
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

        final Dialog dialog = new Dialog(this);
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

        shareDataDialogs.getReminder().observe(DetailMissionActivity.this, data -> {
            rdSameTime.setChecked(!data.isEmpty());
        });

        shareDataDialogs.getReminder2().observe(DetailMissionActivity.this, data -> {
            rd5mAgo.setChecked(!data.isEmpty());
        });

        shareDataDialogs.getReminder3().observe(this, data -> {
            rd10mAgo.setChecked(!data.isEmpty());
        });

        shareDataDialogs.getReminder4().observe(this, data -> {
            rd15mAgo.setChecked(!data.isEmpty());
        });

        shareDataDialogs.getReminder5().observe(this, data -> {
            rd30mAgo.setChecked(!data.isEmpty());
        });

        shareDataDialogs.getReminder6().observe(this, data -> {
            rd1dAgo.setChecked(!data.isEmpty());
        });

        shareDataDialogs.getReminder7().observe(this, data -> {
            rd2dAgo.setChecked(!data.isEmpty());
        });

        shareDataDialogs.getDataDate().observe(this, days -> {
            if (days != null && !days.equals("Không")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDate daySetting = LocalDate.parse(days, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    if (daySetting.equals(localDate)){
                        rd1dAgo.setClickable(false);
                        rd2dAgo.setClickable(false);
                        AtomicInteger hour = new AtomicInteger();
                        AtomicInteger minute = new AtomicInteger();
                        shareDataDialogs.getDataTimeHour().observe(this, hour::set);
                        shareDataDialogs.getDataTimeMinute().observe(this, minute::set);
                        LocalTime localTime = LocalTime.now();
                        if (hour.get() == localTime.getHour()){
                            if (minute.get() - localTime.getMinute() < 5){
                                rd5mAgo.setClickable(false);
                                rd10mAgo.setClickable(false);
                                rd15mAgo.setClickable(false);
                                rd30mAgo.setClickable(false);
                            }else if (minute.get() - localTime.getMinute() < 10 && minute.get() - localTime.getMinute() >= 5){
                                rd5mAgo.setClickable(true);
                                rd10mAgo.setClickable(false);
                                rd15mAgo.setClickable(false);
                                rd30mAgo.setClickable(false);
                            }else if (minute.get() - localTime.getMinute() < 15 && minute.get() - localTime.getMinute() >= 10){
                                rd5mAgo.setClickable(true);
                                rd10mAgo.setClickable(true);
                                rd15mAgo.setClickable(false);
                                rd30mAgo.setClickable(false);
                            }else if (minute.get() - localTime.getMinute() < 30 && minute.get() - localTime.getMinute() >= 15){
                                rd5mAgo.setClickable(true);
                                rd10mAgo.setClickable(true);
                                rd15mAgo.setClickable(true);
                                rd30mAgo.setClickable(false);
                            }else if (minute.get() - localTime.getMinute() >= 30){
                                rd5mAgo.setClickable(true);
                                rd10mAgo.setClickable(true);
                                rd15mAgo.setClickable(true);
                                rd30mAgo.setClickable(true);
                            }
                        }
                    }else{
                        if (daySetting.getMonth().equals(localDate.getMonth())){
                            if (Math.abs(daySetting.getDayOfMonth() - localDate.getDayOfMonth()) == 1){
                                rd2dAgo.setClickable(false);
                                rd1dAgo.setClickable(true);
                                rd5mAgo.setClickable(true);
                                rd10mAgo.setClickable(true);
                                rd15mAgo.setClickable(true);
                                rd30mAgo.setClickable(true);
                            }else if (Math.abs(daySetting.getDayOfMonth() - localDate.getDayOfMonth()) >= 2){
                                rd1dAgo.setClickable(true);
                                rd2dAgo.setClickable(true);
                                rd5mAgo.setClickable(true);
                                rd10mAgo.setClickable(true);
                                rd15mAgo.setClickable(true);
                                rd30mAgo.setClickable(true);
                            }
                        }
                    }
                }
            }
        });

        List<String> list = new ArrayList<>();
        shareDataDialogs.getDatareminders().observe(this, reminders -> {
            if (reminders != null){
                list.clear();
                list.addAll(reminders);
            }else{
                list.clear();
            }
        });

        List<NotificationTemp> list1 = new ArrayList<>();
        shareDataDialogs.getDataNotificationTemp().observe(this, notificationList1 -> {
            if (notificationList1 != null){
                list1.clear();
                list1.addAll(notificationList1);
            }else{
                list1.clear();
            }
        });

        AtomicReference<String> day = new AtomicReference<>();
        shareDataDialogs.getDataDate().observe(this, days -> {
            if (days != null && !days.equals("Không")){
                day.set(days);
            }else{
                dialog.dismiss();
            }
        });

        AtomicReference<String> time = new AtomicReference<>();
        shareDataDialogs.getDataTime().observe(this, data -> {
            if (data != null && !data.equals("Không")) {
                time.set(data);
            } else {
                dialog.dismiss();
            }
        });

        AtomicInteger hour = new AtomicInteger();
        AtomicInteger minute = new AtomicInteger();

        shareDataDialogs.getDataTimeHour().observe(this, hour::set);

        shareDataDialogs.getDataTimeMinute().observe(this, minute::set);

        rdSameTime.setOnClickListener(view -> {
            if (rdSameTime.isChecked()){
                rdSameTime.setChecked(false);
                shareDataDialogs.getDataTime().observe(this, data -> {
                    list.remove(day.get() + " " + data);
                    NotificationTemp notificationTemp = new NotificationTemp(day.get(), data);
                    list1.remove(notificationTemp);
                    shareDataDialogs.setReminder("");
                });
            }else{
                rdSameTime.setChecked(true);
                shareDataDialogs.getDataTime().observe(this, data -> {
                    list.add(day.get() + " " + data);
                    NotificationTemp notificationTemp = new NotificationTemp(day.get(), data);
                    list1.add(notificationTemp);
                    shareDataDialogs.setReminder(day.get() + " " + data);
                });
            }
        });

        rd5mAgo.setOnClickListener(view -> {
            if (rd5mAgo.isChecked()){
                rd5mAgo.setChecked(false);
                shareDataDialogs.getReminder2().observe(this, data -> {
                    String time1 = data.split(" ")[1];
                    list.remove(data);
                    NotificationTemp notificationTemp = new NotificationTemp(day.get(), time1);
                    list1.remove(notificationTemp);
                    shareDataDialogs.setReminder2("");
                });
            }else{
                rd5mAgo.setChecked(true);
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
                list.add(day.get() + " " + s);
                NotificationTemp notificationTemp = new NotificationTemp(day.get(), s);
                list1.add(notificationTemp);
                shareDataDialogs.setReminder2(day.get() + " " + s);
            }
        });

        rd10mAgo.setOnClickListener(view -> {
            if (rd10mAgo.isChecked()){
                rd10mAgo.setChecked(false);
                shareDataDialogs.getReminder3().observe(this, data -> {
                    list.remove(data);
                    String time1 = data.split(" ")[1];
                    NotificationTemp notificationTemp = new NotificationTemp(day.get(), time1);
                    list1.remove(notificationTemp);
                    shareDataDialogs.setReminder3("");
                });
            }else{
                rd10mAgo.setChecked(true);
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
                list.add(day.get() + " " + s);
                NotificationTemp notificationTemp = new NotificationTemp(day.get(), s);
                list1.add(notificationTemp);
                shareDataDialogs.setReminder3(day.get() + " " + s);
            }
        });

        rd15mAgo.setOnClickListener(view -> {
            if (rd15mAgo.isChecked()){
                rd15mAgo.setChecked(false);
                shareDataDialogs.getReminder4().observe(this, data -> {
                    String time1 = data.split(" ")[1];
                    list.remove(data);
                    NotificationTemp notificationTemp = new NotificationTemp(day.get(), time1);
                    list1.remove(notificationTemp);
                    shareDataDialogs.setReminder4("");
                });
            }else{
                rd15mAgo.setChecked(true);
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
                list.add(day.get() + " " + s);
                NotificationTemp notificationTemp = new NotificationTemp(day.get(), s);
                list1.add(notificationTemp);
                shareDataDialogs.setReminder4(day.get() + " " + s);
            }
        });

        rd30mAgo.setOnClickListener(view -> {
            if (rd30mAgo.isChecked()){
                rd30mAgo.setChecked(false);
                shareDataDialogs.getReminder5().observe(this, data -> {
                    String time1 = data.split(" ")[1];
                    list.remove(data);
                    NotificationTemp notificationTemp = new NotificationTemp(day.get(), time1);
                    list1.remove(notificationTemp);
                    shareDataDialogs.setReminder5("");
                });
            }else{
                rd30mAgo.setChecked(true);
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
                list.add(day.get() + " " + s);
                NotificationTemp notificationTemp = new NotificationTemp(day.get(), s);
                list1.add(notificationTemp);
                shareDataDialogs.setReminder5(day.get() + " " + s);
            }
        });

        rd1dAgo.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate dateDay = LocalDate.parse(day.get(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                dateDay.minusDays(1);
                String s = dateDay.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (rd1dAgo.isChecked()){
                    rd1dAgo.setChecked(false);
                    shareDataDialogs.getReminder6().observe(this, data -> {
                        list.remove(data);
                        NotificationTemp notificationTemp = new NotificationTemp(s, time.get());
                        list1.remove(notificationTemp);
                        shareDataDialogs.setReminder6("");
                    });
                }else{
                    rd1dAgo.setChecked(true);
                    list.add(s + " " + time.get());
                    NotificationTemp notificationTemp = new NotificationTemp(s, time.get());
                    list1.add(notificationTemp);
                    shareDataDialogs.setReminder6(s + " " + time.get());
                }
            }
        });

        rd2dAgo.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate dateDay = LocalDate.parse(day.get(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                dateDay.minusDays(2);
                String s = dateDay.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (rd2dAgo.isChecked()){
                    rd2dAgo.setChecked(false);
                    shareDataDialogs.getReminder7().observe(this, data -> {
                        list.remove(data);
                        NotificationTemp notificationTemp = new NotificationTemp(s, time.get());
                        list1.remove(notificationTemp);
                        shareDataDialogs.setReminder7("");
                    });
                }else{
                    rd2dAgo.setChecked(true);
                    list.add(s + " " + time.get());
                    NotificationTemp notificationTemp = new NotificationTemp(s, time.get());
                    list1.add(notificationTemp);
                    shareDataDialogs.setReminder7(s + " " + time.get());
                }
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDataDialogs.setDatareminders(list);
                shareDataDialogs.setDataNotificationTemp(list1);
                shareDataDialogs.getDatareminders().observe(DetailMissionActivity.this, data -> {
                    if (data.isEmpty()){
                        dialog.dismiss();
                    }else{
                        final String[] s = {""};
                        data.forEach(d -> {
                            s[0] = s[0] + d + " ";
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
                shareDataDialogs.setDatareminders(new ArrayList<>());
                shareDataDialogs.setDataNotificationTemp(new ArrayList<>());
                shareDataDialogs.setDataNotice("Không");
                shareDataDialogs.setReminder("");
                shareDataDialogs.setReminder2("");
                shareDataDialogs.setReminder3("");
                shareDataDialogs.setReminder4("");
                shareDataDialogs.setReminder5("");
                shareDataDialogs.setReminder6("");
                shareDataDialogs.setReminder7("");
                dialog.dismiss();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void showBottomDialogRepeat() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_mission_add_calendar_repeat);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        dialog.getWindow().setGravity(Gravity.CENTER);

        RelativeLayout repeat = dialog.findViewById(R.id.repeat);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch repeatSwitch = dialog.findViewById(R.id.repeat_switch);
        RelativeLayout repeatNo = dialog.findViewById(R.id.RepeatNo);
        RelativeLayout repeatType = dialog.findViewById(R.id.RepeatType);
        TextView setRepeatNo = dialog.findViewById(R.id.set_repeat_no);
        TextView setRepeatType = dialog.findViewById(R.id.set_repeat_type);

        AtomicInteger time = new AtomicInteger();
        shareDataDialogs.getDataRepeatNo().observe(this, data -> {
            if (data != null){
                time.set(data);
            }else {
                time.set(0);
            }
        });

        AtomicReference<String> type = new AtomicReference<>();
        shareDataDialogs.getDataRepeatType().observe(this, data -> {
            if (data != null){
                type.set(data);
                setRepeatType.setText(data);
            }else{
                type.set("Không");
                setRepeatType.setText("Không");
            }
        });

        shareDataDialogs.getDataRepeat().observe(this, data -> {
            if (data.equals("Không")){
                shareDataDialogs.setDataRepeatBool(0);
            }else{
                shareDataDialogs.setDataRepeatBool(1);
            }
        });

        shareDataDialogs.getDataRepeatBool().observe(this, data -> {
            if (data == 0){
                repeatSwitch.setChecked(false);
                repeatNo.setClickable(false);
                repeatType.setClickable(false);
                shareDataDialogs.setDataRepeatNo(0);
                shareDataDialogs.setDataRepeatType("Không");
                shareDataDialogs.setDataRepeat("Không");
            }else{
                repeatSwitch.setChecked(true);
                repeatNo.setClickable(true);
                repeatType.setClickable(true);
                shareDataDialogs.setDataRepeat("Mỗi" + " " + time.get() + " " + type.get() + "(s)");
            }
        });

        shareDataDialogs.getDataRepeatNo().observe(this, data -> {
            if (data == 0){
                setRepeatNo.setText("Không");
            }else{
                setRepeatNo.setText(data);
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeatSwitch.isChecked()){
                    shareDataDialogs.setDataRepeatBool(0);
                    repeatSwitch.setChecked(false);
                }else{
                    shareDataDialogs.setDataRepeatBool(1);
                    repeatSwitch.setChecked(true);
                }
            }
        });

        repeatSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeatSwitch.isChecked()) {
                    repeatSwitch.setChecked(false);
                    shareDataDialogs.setDataRepeatBool(0);
                } else {
                    repeatSwitch.setChecked(true);
                    shareDataDialogs.setDataRepeatBool(1);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Chọn thể loại");
            builder.setItems(items, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int item) {
                    shareDataDialogs.setDataRepeatType(items[item]);
                    int time;
                    if (shareDataDialogs.getDataRepeatNo().getValue() == null){
                        time = 0;
                    }else{
                        time = shareDataDialogs.getDataRepeatNo().getValue();
                    }
                    String type = shareDataDialogs.getDataRepeatType().getValue();
                    shareDataDialogs.setDataRepeat("Mỗi" + " " + time + " " + type + "(s)");
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        });

        repeatNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DetailMissionActivity.this);
                alert.setTitle("Nhập số lần lặp lại");

                // Create EditText box to input repeat number
                final EditText input = new EditText(DetailMissionActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(input);
                alert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                if (input.getText().toString().isEmpty()) {
                                    shareDataDialogs.setDataRepeatNo(1);
                                    int time;
                                    if (shareDataDialogs.getDataRepeatNo().getValue() == null){
                                        time = 0;
                                    }else{
                                        time = shareDataDialogs.getDataRepeatNo().getValue();
                                    }
                                    String type = shareDataDialogs.getDataRepeatType().getValue();
                                    shareDataDialogs.setDataRepeat("Mỗi" + " " + time + " " + type + "(s)");
                                }
                                else {
                                    shareDataDialogs.setDataRepeatNo(Integer.parseInt(input.getText().toString()));
                                    int time;
                                    if (shareDataDialogs.getDataRepeatNo().getValue() == null){
                                        time = 0;
                                    }else{
                                        time = shareDataDialogs.getDataRepeatNo().getValue();
                                    }
                                    String type = shareDataDialogs.getDataRepeatType().getValue();
                                    shareDataDialogs.setDataRepeat("Mỗi" + " " + time + " " + type + "(s)");
                                }
                            }
                        });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });
    }

    private  void  showBottomDialogReminder(){
        final Dialog dialog = new Dialog(this);
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RingToneUserAdapter musicAdapter = new RingToneUserAdapter(DetailMissionActivity.this, this);
        recyclerView.setAdapter(musicAdapter);
        shareDataDialogs.getRingtoneList().observe(this, ringtonelist -> {
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
            dialog.dismiss();
        });
    }

    @Override
    public void onItemClick(int position, int id, boolean isChosing, int selectionItem) {
        if (isChosing) {
            shareDataDialogs.setDataCategory(id);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClickSticker(int sticker_id, int position) {
        shareDataDialogs.setDataSticker(sticker_id);
    }

    @Override
    public void onItemClick(int position, String name) {
        TaskString taskString = new TaskString(name);
        List<TaskString> taskStringList = shareDataDialogs.getDatataskBesideList().getValue();
        if (taskStringList == null){
            taskStringList = new ArrayList<>();
        }
        taskStringList.remove(taskString);
        shareDataDialogs.setDatataskBesideList(taskStringList);
    }

    @Override
    public void onItemClick(int position, int id_ringtone) {
        shareDataDialogs.setDataRingTone(id_ringtone);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, MissionFragment.class);
        startActivity(intent);
    }

}
