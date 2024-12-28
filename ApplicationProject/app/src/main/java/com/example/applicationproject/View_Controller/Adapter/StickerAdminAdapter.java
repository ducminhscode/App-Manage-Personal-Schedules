package com.example.applicationproject.View_Controller.Adapter;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.applicationproject.Model.Sticker;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.ShareDataMission;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class StickerAdminAdapter extends RecyclerView.Adapter<StickerAdminAdapter.StickerViewHolder> {

    private List<Sticker> stickerList;
    private final Context context;

    @SuppressLint("NotifyDataSetChanged")
    public StickerAdminAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setStickerList(List<Sticker> stickerList) {
        this.stickerList = stickerList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StickerAdminAdapter.StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_stickers, parent, false);
        return new StickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerAdminAdapter.StickerViewHolder holder, int position) {
        Sticker sticker = stickerList.get(position);
        Uri stickerUri = Uri.parse(sticker.getSticker_path());
//        Glide.with(context)
//                .load(stickerUri)
//                .diskCacheStrategy(DiskCacheStrategy.NONE) // Không sử dụng bộ nhớ cache
//                .into(holder.stickerImage);
//        Cursor cursor = context.getContentResolver().query(stickerUri, null, null, null, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            Log.d("Image Path", path);
//            cursor.close();
//
//            // Đưa đường dẫn vào Glide để tải ảnh
//            Glide.with(context)
//                    .load(path)
//                    .into(holder.stickerImage);
//        }
        if (stickerUri != null) {
            // Sử dụng ContentResolver để query thông tin ảnh
            Cursor cursor = context.getContentResolver().query(stickerUri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Kiểm tra lại và lấy URI Content thay vì path từ MediaStore
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Log.d("Image Path", path);
                cursor.close();

                // Đưa URI Content vào Glide để tải ảnh lên
                Glide.with(context)
                        .load(stickerUri)
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // Không sử dụng bộ nhớ cache
                        .into(holder.stickerImage);
            } else {
                // Nếu không có dữ liệu trong cursor, có thể ảnh không tồn tại hoặc URI không hợp lệ
                Log.e("Error", "Unable to find the image for the URI: " + stickerUri);
            }
        }

        holder.stickerImage.invalidate();
        holder.stickerLayout.setOnClickListener(v -> {
            Toast.makeText(context, "Sticker ID: " + sticker.getSticker_id(), Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xóa danh mục");
            builder.setMessage("Bạn có muốn xóa hình ảnh này không ?");
            builder.setPositiveButton("Có", (dialog, which) -> {
                stickerList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, stickerList.size());
                DAO.deleteSticker(context, sticker.getSticker_id());
                if (DAO.deleteSticker(context, sticker.getSticker_id())){
                    Toast.makeText(context, "Delete successfull", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            });
            builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        if (stickerList != null) {
            return stickerList.size();
        } else {
            return 0;
        }
    }

    public static class StickerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView stickerImage;
        private final LinearLayout stickerLayout;
        public StickerViewHolder(@NonNull View itemView) {
            super(itemView);
            stickerImage = itemView.findViewById(R.id.image_sticker);
            stickerLayout = itemView.findViewById(R.id.layout_stickers);
        }
    }
}
