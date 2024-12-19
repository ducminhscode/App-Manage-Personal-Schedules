package com.example.applicationproject.View_Controller.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.Database.ToDoDBContract;
import com.example.applicationproject.Model.User;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.Adapter.UserAdapter;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.ShareDataMission;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class UserActivity extends AppCompatActivity implements UserAdapter.OnClickItemUser, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXSIT_USER = 999;
    private static final int RQ_SPEECH_REC = 102;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ImageButton imgBack, imgClear, imgSearchByMic;
    private EditText tvSearching;
    private TextView btnAddUser;
    private ShareDataMission shareDataDialogs;
    private List<User> userlist;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manageusers_layout_admin);
        initWiget();
        getSupportLoaderManager().initLoader(EXSIT_USER, null, this);
        shareDataDialogs = new ViewModelProvider(this).get(ShareDataMission.class);
        userlist = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        userAdapter = new UserAdapter(this, this);
        recyclerView.setAdapter(userAdapter);

        imgBack.setOnClickListener(view -> {
            finish();
        });

        btnAddUser.setOnClickListener(view -> {
            showdialogAddUser();
        });

        imgSearchByMic.setOnClickListener(view -> {
            askSpeechInput();
        });

        tvSearching.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString().trim();
                if (query.matches("\\d+")) {
                    userAdapter.getFilter().filter(query);
                    userAdapter.setFilterType("id");
                } else if (!query.isEmpty()) {
                    userAdapter.getFilter().filter(query);
                    userAdapter.setFilterType("Name");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        imgClear.setOnClickListener(view -> {
            tvSearching.setText("");
            tvSearching.requestFocus();
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void showdialogAddUser() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_manageusers_admin);
        dialog.setCanceledOnTouchOutside(true);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        EditText etName = dialog.findViewById(R.id.edtUserName);
        EditText etPassWord = dialog.findViewById(R.id.edtPassWord);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnCreateAccount = dialog.findViewById(R.id.btnCreateAccount);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalTime time = LocalTime.now();
            etName.setText(String.valueOf(time.format(DateTimeFormatter.ISO_LOCAL_TIME)));
            etName.requestFocus();
        }
        etPassWord.setText("1");

        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        btnCreateAccount.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String password = etPassWord.getText().toString();
            if (name.isEmpty()){
                Toast.makeText(this, "Tên tài khoản không được để trống", Toast.LENGTH_SHORT).show();
                etName.requestFocus();
            }else if (password.isEmpty()){
                Toast.makeText(this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                etPassWord.requestFocus();
            }else{
                if (DAO.checkUser(UserActivity.this, name, password)){
                    Toast.makeText(this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                    etName.requestFocus();
                }else{
                    DAO.addUser(this, name, null, null, password, "admin");
                    int id = DAO.getUserId(this, name);
                    userlist.add(new User(null, null, id, name, password, "admin"));
                    userAdapter.notifyDataSetChanged();
                    userAdapter.notifyItemInserted(userlist.size() - 1);
                    Toast.makeText(this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

    }


    private void askSpeechInput() {
        if(!SpeechRecognizer.isRecognitionAvailable(this)){
            Toast.makeText(this, "Speech recognition is not avaialable!!", Toast.LENGTH_SHORT).show();
        }else{
            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!!!");
            startActivityForResult(i, RQ_SPEECH_REC);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RQ_SPEECH_REC && resultCode == Activity.RESULT_OK){
            if (data == null) return;
            List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result == null) return;
            tvSearching.setText(result.get(0));
        }
    }

    private void initWiget() {
        imgBack = findViewById(R.id.imgBack);
        imgClear = findViewById(R.id.imgClear);
        imgSearchByMic = findViewById(R.id.imgSearchByMic);
        btnAddUser = findViewById(R.id.btnAddUser);
        recyclerView = findViewById(R.id.recyclerView);
        tvSearching = findViewById(R.id.tvSearching);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemClick(int position, String name, int id) {

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id != EXSIT_USER) return null;
        String[] projection = {
                ToDoDBContract.UserEntry.USER_ID,
                ToDoDBContract.UserEntry.USER_NAME,
                ToDoDBContract.UserEntry.USER_EMAIL,
                ToDoDBContract.UserEntry.USER_MOBILE,
                ToDoDBContract.UserEntry.USER_PASSWORD,
                ToDoDBContract.UserEntry.USER_ROLE
        };
        return new CursorLoader(this, ToDoDBContract.UserEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        List<User> mList = new ArrayList<>();
        if (data == null || data.getCount() < 1) {
            mList = new ArrayList<>();
        }else{
            while(data.moveToNext()){
                @SuppressLint("Range") int id = data.getInt(data.getColumnIndex(ToDoDBContract.UserEntry.USER_ID));
                @SuppressLint("Range") String name = data.getString(data.getColumnIndex(ToDoDBContract.UserEntry.USER_NAME));
                @SuppressLint("Range") String email = data.getString(data.getColumnIndex(ToDoDBContract.UserEntry.USER_EMAIL));
                @SuppressLint("Range") String mobile = data.getString(data.getColumnIndex(ToDoDBContract.UserEntry.USER_MOBILE));
                @SuppressLint("Range") String password = data.getString(data.getColumnIndex(ToDoDBContract.UserEntry.USER_PASSWORD));
                @SuppressLint("Range") String role = data.getString(data.getColumnIndex(ToDoDBContract.UserEntry.USER_ROLE));
                mList.add(new User(mobile, email, id, name, password, role));
            }
        }
        userlist = mList;
        userAdapter.setUserlist(userlist);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
