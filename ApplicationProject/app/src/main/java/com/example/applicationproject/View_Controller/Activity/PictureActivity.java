package com.example.applicationproject.View_Controller.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.applicationproject.Database.ToDoDBContract;
import com.example.applicationproject.Model.Sticker;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.Adapter.StickerAdminAdapter;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.ShareDataMission;
import com.example.applicationproject.View_Controller.Utils.UrlValidator;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;

public class PictureActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_STICKERS_LOADER = 3;
    private static final int REQUEST_PERMISSION = 1;
    private Button btnPhoToFromURL, btnPhotoFromDevice;
    private ImageButton btnBack;
    private RecyclerView recyclerViewImages;
    private StickerAdminAdapter pictureAdapter;
    private ShareDataMission shareDataMission;
    private List<Sticker> stickerList;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_manage_picture);
        initWidget();
        getSupportLoaderManager().initLoader(EXISTING_STICKERS_LOADER, null, this);
        shareDataMission = new ViewModelProvider(this).get(ShareDataMission.class);
        btnBack.setOnClickListener(v -> finish());
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewImages.setLayoutManager(layoutManager);
        pictureAdapter = new StickerAdminAdapter(this);
        recyclerViewImages.setAdapter(pictureAdapter);

        btnPhotoFromDevice.setOnClickListener(v -> {
            // Thực hiện hành động thêm hình ảnh từ thiết bị
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermission();
            }
        });

        btnPhoToFromURL.setOnClickListener(v -> {
            // Thực hiện hành động thêm hình ảnh từ URL
            requestImageFormUrl();
        });

    }

    private void requestImageFormUrl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermission();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showdialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Nhập url của ảnh đó url:");

        // Create EditText box to input url
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alert.setView(input);
        alert.setPositiveButton("OK", (dialog, whichButton) -> {
            if (input.getText().toString().isEmpty()) {
                dialog.dismiss();
            }else{
                String url = input.getText().toString();
                if (!UrlValidator.isValidUrl(url)) {
                    Toast.makeText(this, "UnValid URL", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else{
                    Uri uri = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(System.currentTimeMillis()));
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                    request.setTitle(uri.getLastPathSegment());
                    request.setDescription("Downloading...");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    if (downloadManager != null) {
                        downloadManager.enqueue(request);
                    }
                    Toast.makeText(this, "Download Success", Toast.LENGTH_SHORT).show();
                    if (DAO.checkSticker(PictureActivity.this, uri.toString())){
                        Toast.makeText(PictureActivity.this, "Sticker already exists", Toast.LENGTH_SHORT).show();
                    }else{
                        DAO.insertSticker(PictureActivity.this, uri.toString(), uri.getLastPathSegment());
                        int stickerId = DAO.getStickerId(PictureActivity.this, uri.toString());
                        Sticker sticker = new Sticker(stickerId, uri.toString(), uri.getLastPathSegment());
                        stickerList.add(sticker);
                        pictureAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
            dialog.dismiss();
        });
        alert.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                selectImageFromCalery();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(PictureActivity.this, "Permission Denied\n", Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void selectImageFromCalery() {
        // Thực hiện hành động chọn hình ảnh từ thư viện
        TedImagePicker.with(this)
                .startMultiImage(new OnMultiSelectedListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSelected(@NonNull List<? extends android.net.Uri> uriList) {
                        if (!uriList.isEmpty()) {
                            for (android.net.Uri uri : uriList) {
                                if (DAO.checkSticker(PictureActivity.this, uri.toString())){
                                    Toast.makeText(PictureActivity.this, "Sticker already exists", Toast.LENGTH_SHORT).show();
                                }else{
                                    Log.e("Uri Hợp lệ", "onSelected: " + uri);
                                    DAO.insertSticker(PictureActivity.this, uri.toString(), uri.getLastPathSegment());
                                    int stickerId = DAO.getStickerId(PictureActivity.this, uri.toString());
                                    Sticker sticker = new Sticker(stickerId, uri.toString(), uri.getLastPathSegment());
                                    stickerList.add(sticker);
                                    pictureAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
    }

    private void initWidget() {
        btnPhoToFromURL = findViewById(R.id.btnAddImageFromURL);
        btnPhotoFromDevice = findViewById(R.id.btnAddImageFromGallery);
        btnBack = findViewById(R.id.btnBack);
        recyclerViewImages = findViewById(R.id.recyclerViewImages);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void checkPermission() {
        // Check if the necessary permissions are granted
        if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            // Permissions already granted
            Toast.makeText(this, "All permissions granted!", Toast.LENGTH_SHORT).show();
            showdialog();
        } else {
            // Request permissions that are not granted
            String[] permissions = {
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.CAMERA
            };
            requestPermissions(permissions, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    showdialog();
                }
            }
        }else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection2 = {
                ToDoDBContract.StickerEntry.STICKER_ID,
                ToDoDBContract.StickerEntry.STICKER_TITLE,
                ToDoDBContract.StickerEntry.STICKER_PATH
        };
        return new CursorLoader(this, ToDoDBContract.StickerEntry.CONTENT_URI, projection2, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            stickerList = new ArrayList<>();
        }else{
            stickerList = new ArrayList<>();
            while (data.moveToNext()) {
                int id = data.getInt(data.getColumnIndexOrThrow(ToDoDBContract.StickerEntry.STICKER_ID));
                String title = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.StickerEntry.STICKER_TITLE));
                String path = data.getString(data.getColumnIndexOrThrow(ToDoDBContract.StickerEntry.STICKER_PATH));
                stickerList.add(new Sticker(id, title, path));
            }
        }
        pictureAdapter.setStickerList(stickerList);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
