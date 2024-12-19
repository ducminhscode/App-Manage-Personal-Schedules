package com.example.applicationproject.View_Controller.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.Application.MyApplication;
import com.example.applicationproject.Database.ToDoDBContract;
import com.example.applicationproject.Model.Category;
import com.example.applicationproject.Model.Mission;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.Adapter.ManageListAdapter;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.ShareDataMission;
import com.sun.mail.imap.SortTerm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ManageListActivity extends AppCompatActivity implements ManageListAdapter.OnClickItemCategoryMore, LoaderManager.LoaderCallbacks<Cursor>{

    private List<Category> categoryList;
    private RecyclerView recyclerView;
    private LinearLayout managelist_addmore;
    private ShareDataMission shareDataDialogs;
    private String username;
    private static final int EXISTING_CATEGORIES_LOADER = 1;
    private static final int EXISTING_MISIONS_LOADER = 0;
    private ImageButton imageButton;
    private List<Mission> missionList1;
    private int user;
    private ManageListAdapter manageListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_list);
        initWiget();
        shareDataDialogs = new ViewModelProvider(this).get(ShareDataMission.class);
        getSupportLoaderManager().initLoader(EXISTING_CATEGORIES_LOADER, null, this);
        getSupportLoaderManager().initLoader(EXISTING_MISIONS_LOADER, null, this);
        setManageList();
        Intent intent = getIntent();
        username = intent.getStringExtra("name");
        user = DAO.getUserId(this.getBaseContext(), username);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0){

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int positionDragger = viewHolder.getAdapterPosition();
                int positionTarget = target.getAdapterPosition();
                Collections.swap(categoryList, positionDragger, positionTarget);
                Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(positionDragger, positionTarget);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @SuppressLint("NotifyDataSetChanged")
    private void setManageList() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        manageListAdapter = new ManageListAdapter(this, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(manageListAdapter);
    }

    private void initWiget() {
        recyclerView = findViewById(R.id.managelist_recyclerview);
        managelist_addmore = findViewById(R.id.managelist_addmore);
        managelist_addmore.setOnClickListener(v -> {
            action(null, -1, -1);
        });
        imageButton = findViewById(R.id.btn_back);
        imageButton.setOnClickListener(v -> finish());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addToList(String resultData) {
        if (DAO.getUserId(this.getBaseContext(), username) == -1){
            Toast.makeText(this.getBaseContext(), "Không có user", Toast.LENGTH_SHORT).show();
        }else{
            if (DAO.checkCategory(this.getBaseContext(), resultData)){
                Toast.makeText(this.getBaseContext(), "Danh mục đã tồn tại", Toast.LENGTH_SHORT).show();
            }else{
                if (DAO.insertCategory(this.getBaseContext(), resultData, user)){
                    Toast.makeText(this.getBaseContext(), "Insert Successfull", Toast.LENGTH_SHORT).show();
                    int id = DAO.getCategoryId(this.getBaseContext(), resultData);
                    Log.e("add", "addToList: " + id );
                    categoryList.add(new Category(id, resultData, DAO.getUserId(this.getBaseContext(), username)));
                    manageListAdapter.notifyDataSetChanged();
                    manageListAdapter.notifyItemInserted(categoryList.size() - 1);
                }else{
                    Toast.makeText(this.getBaseContext(), "Fail", Toast.LENGTH_SHORT).show();
                }
                Objects.requireNonNull(recyclerView.getAdapter()).notifyItemInserted(categoryList.size() - 1);
            }
        }
    }

    private void updateList(String resultData, int idManage, int position){
//        shareDataDialogs.getCategoryList().observe(this, categories -> {
//            if (categories != null){
//                categoryList.clear();
//                categoryList.addAll(categories);
//            }else{
//                categoryList.clear();
//            }
//        });
//        if (DAO.checkCategory(this.getBaseContext(), resultData)){
//            Toast.makeText(this.getBaseContext(), "Danh mục đã tồn tại", Toast.LENGTH_SHORT).show();
//        }else{
//                Log.e("checkCate", "updateList: " + DAO.getUserId(this.getBaseContext(), username) + " " + idManage);
//            if (DAO.updateCategory(this.getBaseContext(), idManage, resultData, DAO.getUserId(this.getBaseContext(), username))){
//                Toast.makeText(this.getBaseContext(), "Update Successfull", Toast.LENGTH_SHORT).show();
//                manageListAdapter.notifyItemChanged(position);
//            }else{
//                Toast.makeText(this.getBaseContext(), "Fail Update", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    private void action(String resultData, int id, int position) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_managelist);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        EditText editText_managelist = dialog.findViewById(R.id.editText_managelist);
        editText_managelist.requestFocus();
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        Button btn_save = dialog.findViewById(R.id.btn_save);

        if (resultData != null){
            editText_managelist.setText(resultData);
            btn_save.setOnClickListener(view -> {
                Toast.makeText(this, "check1", Toast.LENGTH_SHORT).show();
                if (editText_managelist.getText().toString().isEmpty()){
                    dialog.dismiss();
                }else {
                    Toast.makeText(this, "check3" + id, Toast.LENGTH_SHORT).show();
                    updateList(editText_managelist.getText().toString(), id, position);
                    dialog.dismiss();
                }
            });
        }else{
            btn_save.setOnClickListener(view -> {
                Toast.makeText(this, "check2", Toast.LENGTH_SHORT).show();
                if (editText_managelist.getText().toString().isEmpty()){
                    dialog.dismiss();
                }else {
                    addToList(editText_managelist.getText().toString());
                    dialog.dismiss();
                }
            });
        }
        btn_cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }

    @Override
    public void onItemClick(int position, String categoryName, int id) {
        manageListAdapter.getUserid(user);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == EXISTING_CATEGORIES_LOADER) {
            String[] projection = {
                    ToDoDBContract.CategoryEntry.CATEGORY_ID,
                    ToDoDBContract.CategoryEntry.CATEGORY_TITLE,
                    ToDoDBContract.CategoryEntry.CATEGORY_USER_ID
            };
            return new CursorLoader(this, ToDoDBContract.CategoryEntry.CONTENT_URI, projection, null, null, null);
        }else if (id == EXISTING_MISIONS_LOADER){
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
        }else{
            return null;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        List<Category> mcategoryList = null;
        List<Mission> missionList = null;
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
                    int id = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.CategoryEntry.CATEGORY_ID));
                    String title = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.CategoryEntry.CATEGORY_TITLE));
                    int userId = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.CategoryEntry.CATEGORY_USER_ID));
                    mcategoryList.add(new Category(id, title, userId));
                }
                break;
        }
        if (mcategoryList != null) {
            mcategoryList = mcategoryList.stream().filter(category -> category.getUser_id() == user).collect(Collectors.toList());
            Log.e("checkcolumn1", "onLoadFinished: categoyList" + mcategoryList.get(0).getCategory_name());
        } else {
            mcategoryList = new ArrayList<>();
        }
        categoryList = mcategoryList;
        if (missionList != null) {
            List<Mission> mainList = new ArrayList<>();
            for (Category category : mcategoryList) {
                mainList.addAll(missionList.stream().filter(mission -> category.getCategory_id() == mission.getCategory_id()).collect(Collectors.toList()));
            }
            missionList1 = mainList;
            Log.e("checkcolumn2", "onLoadFinished: " + mainList.size());
        } else {
            missionList1 = new ArrayList<>();
        }
        manageListAdapter.setUpdate(categoryList, missionList1);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
//        if (loader.getId() == EXISTING_CATEGORIES_LOADER) {
//            categoryList.clear();
//            shareDataDialogs.setCategoryList(categoryList);
//            manageListAdapter.notifyDataSetChanged();
//        }
//        if (loader.getId() == EXISTING_MISIONS_LOADER) {
//            missionList.clear();
//            shareDataDialogs.setMainList(missionList);
//            manageListAdapter.notifyDataSetChanged();
//        }
    }
}
