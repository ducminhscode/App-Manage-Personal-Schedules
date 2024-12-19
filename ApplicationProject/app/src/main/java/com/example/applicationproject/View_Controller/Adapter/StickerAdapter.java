package com.example.applicationproject.View_Controller.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.Model.Sticker;
import com.example.applicationproject.R;

import java.io.IOException;
import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.StickerViewHolder> {

    private List<Sticker> stickerList;
    private OnClickStickerListener onClickStickerListener;
    private Context context;
    private int selectionItem = -1;
    private int oldposition = -1;

    @SuppressLint("NotifyDataSetChanged")
    public StickerAdapter(Context context, List<Sticker> stickerList, OnClickStickerListener onClickStickerListener) {
        this.context = context;
        this.onClickStickerListener = onClickStickerListener;
        this.stickerList = stickerList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_stickers, parent, false);
        return new StickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerAdapter.StickerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Sticker sticker = stickerList.get(position);
        Uri uri = Uri.parse(sticker.getSticker_path());
        if (uri == null) return;
        try {
            Bitmap bimap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            if (bimap != null) {
                holder.imageView.setImageBitmap(bimap);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStickerListener.onClickSticker(sticker.getSticker_id(), position);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (stickerList != null) {
            return stickerList.size();
        }else{
            return 0;
        }
    }

    public static class StickerViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private LinearLayout linearLayout;
        public StickerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_sticker);
            linearLayout = itemView.findViewById(R.id.layout_stickers);
        }
    }

    public interface OnClickStickerListener {
        void onClickSticker(int sticker_id, int position);
    }
}
