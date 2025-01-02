package com.example.applicationproject.View_Controller.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.Model.Ringtone;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.ShareDataMission;

import java.io.IOException;
import java.util.List;

public class RingToneUserAdapter extends RecyclerView.Adapter<RingToneUserAdapter.RingToneUserViewHolder> {
    private List<Ringtone> ringtoneList;
    private OnClickItemRingtone onItemListener;
    private int selectionItem = -1;
    private int oldposition = -1;
    private final Context context;
    private MediaPlayer mediaPlayer;

    public RingToneUserAdapter(Context context, OnClickItemRingtone onItemListener) {
        this.onItemListener = onItemListener;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRingtoneList(List<Ringtone> ringtoneList) {
        this.ringtoneList = ringtoneList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RingToneUserAdapter.RingToneUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_ringtone_user, null, false);
        return new RingToneUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RingToneUserAdapter.RingToneUserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Ringtone ringTone = ringtoneList.get(position);
        if (selectionItem == position) {
            holder.radioButton.setChecked(true);
            holder.imageButton.setBackgroundResource(R.drawable.baseline_pause_24);
        }
        else{
            holder.radioButton.setChecked(false);
            holder.imageButton.setBackgroundResource(R.drawable.baseline_play_arrow_24);
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemListener.onItemClick(position, ringTone.getRingTone_id());

                // Giải phóng MediaPlayer nếu đang phát
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null; // Giải phóng MediaPlayer sau khi dừng
                }

                try {
                    // Tạo MediaPlayer mới và thiết lập các thuộc tính
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build());

                    // Set data source và chuẩn bị MediaPlayer
                    mediaPlayer.setDataSource(ringTone.getRingTone_path());
                    mediaPlayer.prepareAsync(); // Sử dụng prepareAsync để không block UI

                    mediaPlayer.setOnPreparedListener(mp -> {
                        // Khi chuẩn bị xong, bắt đầu phát nhạc
                        mediaPlayer.start();

                        // Thực hiện các thay đổi giao diện sau khi phát nhạc
                        holder.imageButton.setBackgroundResource(R.drawable.baseline_pause_24); // Đổi nút thành "pause"
                        notifyItemChanged(position);

                        // Lưu vị trí hiện tại và thay đổi giao diện item đang phát
                        oldposition = selectionItem;
                        selectionItem = position;
                        notifyItemChanged(selectionItem);
                        notifyItemChanged(oldposition);

                        // Cập nhật danh sách nhạc chuông
                    });

                    // Đăng ký listener khi phát xong
                    mediaPlayer.setOnCompletionListener(mp -> {
                        // Khi phát xong, thay đổi giao diện nút thành "play"
                        holder.imageButton.setBackgroundResource(R.drawable.baseline_play_arrow_24);
                        notifyItemChanged(position);
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    // Nếu có lỗi xảy ra khi thiết lập MediaPlayer, hiển thị thông báo
                    Toast.makeText(context, "Error playing ringtone", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemListener.onItemClick(position, ringTone.getRingTone_id());
                // Giải phóng MediaPlayer nếu đang phát
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null; // Giải phóng MediaPlayer sau khi dừng
                }

                try {
                    // Tạo MediaPlayer mới và thiết lập các thuộc tính
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build());

                    // Set data source và chuẩn bị MediaPlayer
                    mediaPlayer.setDataSource(ringTone.getRingTone_path());
                    mediaPlayer.prepareAsync(); // Sử dụng prepareAsync để không block UI

                    mediaPlayer.setOnPreparedListener(mp -> {
                        // Khi chuẩn bị xong, bắt đầu phát nhạc
                        mediaPlayer.start();

                        // Thực hiện các thay đổi giao diện sau khi phát nhạc
                        holder.imageButton.setBackgroundResource(R.drawable.baseline_pause_24); // Đổi nút thành "pause"
                        notifyItemChanged(position);

                        // Lưu vị trí hiện tại và thay đổi giao diện item đang phát
                        oldposition = selectionItem;
                        selectionItem = position;
                        notifyItemChanged(selectionItem);
                        notifyItemChanged(oldposition);

                        // Cập nhật danh sách nhạc chuông
                    });

                    // Đăng ký listener khi phát xong
                    mediaPlayer.setOnCompletionListener(mp -> {
                        // Khi phát xong, thay đổi giao diện nút thành "play"
                        holder.imageButton.setBackgroundResource(R.drawable.baseline_play_arrow_24);
                        notifyItemChanged(position);
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    // Nếu có lỗi xảy ra khi thiết lập MediaPlayer, hiển thị thông báo
                    Toast.makeText(context, "Error playing ringtone", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemListener.onItemClick(position, ringTone.getRingTone_id());
                oldposition = selectionItem;
                selectionItem = position;
                notifyItemChanged(selectionItem);
                notifyItemChanged(oldposition);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (ringtoneList == null) {
            return 0;
        }else{
            return ringtoneList.size();
        }
    }

    public static class RingToneUserViewHolder extends RecyclerView.ViewHolder {
        private RadioButton radioButton;
        private LinearLayout linearLayout;
        private ImageButton imageButton;
        public RingToneUserViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radiobtn_items_ring);
            linearLayout = itemView.findViewById(R.id.layout_item_rings_user);
            imageButton = itemView.findViewById(R.id.btn_play_rings);
        }
    }

    public interface OnClickItemRingtone {
        void onItemClick(int position, int id_ringtone);
    }
}
