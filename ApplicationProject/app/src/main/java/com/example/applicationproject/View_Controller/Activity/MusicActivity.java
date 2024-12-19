package com.example.applicationproject.View_Controller.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.Database.ToDoDBContract;
import com.example.applicationproject.Model.Ringtone;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.Adapter.RingToneAdapter;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.ShareDataMission;

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity implements RingToneAdapter.OnClickItemRingtoneAdmin, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MY_PERMISSION_REQUEST_CODE = 999;
    private static final int MY_RINGS = 998;
    private static final int ANOTHER_PERMISSION_REQUEST_CODE = 888;
    private LinearLayout layoutGetRingsFromDV, layoutGetRingsFromAudio;
    private RecyclerView recyclerViewGetRingsFromDV;
    private RingToneAdapter adapter;
    private ImageButton btnBack;
    private ShareDataMission shareDataDialogs;
    private List<Ringtone> ringslist;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_rings);
        initWiget();
        shareDataDialogs = new ViewModelProvider(this).get(ShareDataMission.class);
        shareDataDialogs.getRingtoneList().observe(this, ringtoneList -> {
            if (ringtoneList != null) {
                ringslist.clear();
                ringslist.addAll(ringtoneList);
            }else{
                ringslist.clear();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewGetRingsFromDV.setLayoutManager(layoutManager);
        adapter = new RingToneAdapter(this, this);
        adapter.notifyDataSetChanged();
        recyclerViewGetRingsFromDV.setAdapter(adapter);
        btnBack.setOnClickListener(v -> finish());
        layoutGetRingsFromAudio.setOnClickListener(v -> {
            Toast.makeText(this, "onclick", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestAnotherPermission();
            }
        });
        layoutGetRingsFromDV.setOnClickListener(v -> {
            Toast.makeText(this, "onclick", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermission();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestAnotherPermission() {
        Toast.makeText(this, "requsetPermission", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_AUDIO},
                    ANOTHER_PERMISSION_REQUEST_CODE);
            Toast.makeText(this, "Requesting permittion", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            anotherExtracted();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestPermission() {
        Toast.makeText(this, "requsetPermission", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_AUDIO},
                    MY_PERMISSION_REQUEST_CODE);
            Toast.makeText(this, "Requesting permittion", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            extracted();
        }
    }

    private void initWiget() {
        layoutGetRingsFromDV = findViewById(R.id.layoutGetRingsFromDV);
        layoutGetRingsFromAudio = findViewById(R.id.layoutGetRingsFromAudio);
        recyclerViewGetRingsFromDV = findViewById(R.id.recyclerViewGetRingsFromDV);
        btnBack = findViewById(R.id.btn_back);
    }

    @Override
    public void onItemClick(int position, int id_ringtone) {

    }

    @SuppressLint("IntentReset")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_CODE){
            Toast.makeText(this, "Correct Path", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "" + grantResults[0], Toast.LENGTH_SHORT).show();
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted Correct", Toast.LENGTH_SHORT).show();
                extracted();
            }
        }else if (requestCode == ANOTHER_PERMISSION_REQUEST_CODE){
            Toast.makeText(this, "Correct Path", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "" + grantResults[0], Toast.LENGTH_SHORT).show();
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted Correct", Toast.LENGTH_SHORT).show();
                anotherExtracted();
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void anotherExtracted() {
        ContentResolver contentResolver = getContentResolver();
        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA};
        Cursor cursor = contentResolver.query(audioUri, projection, null, null, null);
        if (cursor != null){
            Toast.makeText(this, "Cursor: " + cursor.getCount(), Toast.LENGTH_SHORT).show();
            while(cursor.moveToNext()){
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                if (DAO.checkRingtone(this, path)){
                    Toast.makeText(this, "Ringtone already exists", Toast.LENGTH_SHORT).show();
                }else{
                    boolean check = DAO.insertRingtone(this, path, name);
                    if (check) {
                        int id = DAO.getStickerId(this, path);
                        if (id != -1) {
                            ringslist.add(new Ringtone(id, path, name));
                            adapter.notifyDataSetChanged();
                            Toast.makeText(this, "Insert Success", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        }
    }

    @SuppressLint("IntentReset")
    private void extracted() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.setType("audio/*");
        startActivityForResult(intent, MY_RINGS);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, MY_RINGS);
//        } else {
//            Toast.makeText(this, "No app found to pick audio file", Toast.LENGTH_SHORT).show();
//        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_RINGS) {
            if (data != null && resultCode == RESULT_OK) {
                Uri audio = data.getData();
                if (audio != null) {
                    if (DAO.checkRingtone(this, audio.toString())) {
                        Toast.makeText(this, "Ringtone already exists", Toast.LENGTH_SHORT).show();
                    }else{
                        boolean check = DAO.insertRingtone(this, audio.getPath(), audio.toString());
                        if (check) {
                            int id = DAO.getStickerId(this, audio.toString());
                            if (id != -1) {
                                ringslist.add(new Ringtone(id, audio.toString(), audio.toString()));
                                adapter.notifyDataSetChanged();
                                adapter.notifyItemInserted(ringslist.size() - 1);
                                shareDataDialogs.setRingtoneList(ringslist);
                                Toast.makeText(this, "Insert Success", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(this, "Insert Fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection3 = {
                ToDoDBContract.RingtoneEntry.RINGTONE_ID,
                ToDoDBContract.RingtoneEntry.RINGTONE_TITLE,
                ToDoDBContract.RingtoneEntry.RINGTONE_PATH
        };
        return new CursorLoader(this, ToDoDBContract.RingtoneEntry.CONTENT_URI, projection3, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            ringslist = new ArrayList<>();
        }else{
            ringslist = new ArrayList<>();
            while (data.moveToNext()) {
                int id = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.RingtoneEntry.RINGTONE_ID));
                String title = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.RingtoneEntry.RINGTONE_TITLE));
                String path = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.RingtoneEntry.RINGTONE_PATH));
                ringslist.add(new Ringtone(id, title, path));
            }
        }
        adapter.setRingtoneList(ringslist);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
