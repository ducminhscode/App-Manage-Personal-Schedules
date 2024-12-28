package com.example.applicationproject.View_Controller.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.Model.Ringtone;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.ShareDataMission;

import java.io.FileDescriptor;
import java.io.InputStream;
import java.util.List;

public class RingToneAdapter extends RecyclerView.Adapter<RingToneAdapter.RingToneViewHolder> {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private List<Ringtone> ringtoneList;
    private OnClickItemRingtoneAdmin onItemListener;
    private int selectionItem = -1;
    private int oldposition = -1;
    private final Context context;

    @SuppressLint("NotifyDataSetChanged")
    public RingToneAdapter(Context context, OnClickItemRingtoneAdmin onItemListener) {
        this.onItemListener = onItemListener;
        this.context = context;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRingtoneList(List<Ringtone> ringtoneList) {
        this.ringtoneList = ringtoneList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RingToneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_rings, null, false);
        return new RingToneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RingToneAdapter.RingToneViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Ringtone ringTone = ringtoneList.get(position);
        Uri audioUri = Uri.parse(ringTone.getRingTone_path());
        holder.textView_name_rings.setText(ringTone.getRingTone_name());
        if (selectionItem == position) {
            holder.imageButton_play_rings.setBackgroundResource(R.drawable.baseline_pause_circle_24);
        }
        else{
            holder.imageButton_play_rings.setBackgroundResource(R.drawable.baseline_play_circle_24);
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemListener.onItemClick(position, ringTone.getRingTone_id());
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null; // Set lại mediaPlayer là null để chuẩn bị cho MediaPlayer mới
                }
                try{
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build());

                    // Set data source và chuẩn bị MediaPlayer
                    ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openAssetFileDescriptor(audioUri, "r").getParcelFileDescriptor();
                    if (parcelFileDescriptor == null) {
                        return;
                    }
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    mediaPlayer.setDataSource(fileDescriptor);
                    mediaPlayer.prepareAsync(); // Sử dụng prepareAsync để không block UI
                    mediaPlayer.setOnCompletionListener(mp -> {
                        mediaPlayer.start();
                        holder.imageButton_play_rings.setBackgroundResource(R.drawable.baseline_pause_circle_24);
                        notifyItemChanged(position);
                        oldposition = selectionItem;
                        selectionItem = position;
                        notifyItemChanged(selectionItem);
                        notifyItemChanged(oldposition);
                    });
                    mediaPlayer.setOnCompletionListener(mp -> {
                        holder.imageButton_play_rings.setBackgroundResource(R.drawable.baseline_play_circle_24);
                        notifyItemChanged(position);
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        holder.imageButton_play_rings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemListener.onItemClick(position, ringTone.getRingTone_id());
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null; // Set lại mediaPlayer là null để chuẩn bị cho MediaPlayer mới
                }
                try{
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build());

                    // Set data source và chuẩn bị MediaPlayer
                    mediaPlayer.setDataSource(ringTone.getRingTone_path());
                    mediaPlayer.prepareAsync(); // Sử dụng prepareAsync để không block UI
                    mediaPlayer.setOnCompletionListener(mp -> {
                        mediaPlayer.start();
                        holder.imageButton_play_rings.setBackgroundResource(R.drawable.baseline_pause_circle_24);
                        notifyItemChanged(position);
                        oldposition = selectionItem;
                        selectionItem = position;
                        notifyItemChanged(selectionItem);
                        notifyItemChanged(oldposition);
                    });
                    mediaPlayer.setOnCompletionListener(mp -> {
                        holder.imageButton_play_rings.setBackgroundResource(R.drawable.baseline_play_circle_24);
                        notifyItemChanged(position);
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        holder.imageButton_delete_rings.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                onItemListener.onItemClick(position, ringTone.getRingTone_id());
                ringtoneList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                boolean check = DAO.deleteRingtone(context, ringTone.getRingTone_id());
                if (check){
                    Toast.makeText(context, "del success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "del fail", Toast.LENGTH_SHORT).show();
                }
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

    public static class RingToneViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout linearLayout;
        private final ImageButton imageButton_play_rings;
        private final ImageButton imageButton_delete_rings;
        private final TextView textView_name_rings;
        public RingToneViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name_rings = itemView.findViewById(R.id.tv_name_rings_admin);
            linearLayout = itemView.findViewById(R.id.layout_item_rings_admin);
            imageButton_play_rings = itemView.findViewById(R.id.btn_play_rings_admin);
            imageButton_delete_rings = itemView.findViewById(R.id.btn_delete_rings_admin);
        }
    }

    public interface OnClickItemRingtoneAdmin {
        void onItemClick(int position, int id_ringtone);
    }
}
