package com.example.applicationproject.View_Controller.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.Model.Taskbeside;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.ClassTemp.TaskString;
import com.example.applicationproject.View_Controller.ShareDataMission;

import java.util.List;

public class TaskBesideAdapter extends RecyclerView.Adapter<TaskBesideAdapter.TaskBesideViewHolder> {
    private List<TaskString> taskBesideList;
    private OnClickItemTaskBeside onItemListener;

    @SuppressLint("NotifyDataSetChanged")
    public TaskBesideAdapter(List<TaskString> taskBesideList, OnClickItemTaskBeside onItemListener) {
        this.taskBesideList = taskBesideList;
        this.onItemListener = onItemListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskBesideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_taskbeside, null, false);
        return new TaskBesideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskBesideAdapter.TaskBesideViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TaskString taskbeside = taskBesideList.get(position);
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.radioButton.isChecked()){
                    holder.radioButton.setChecked(false);
                    holder.editText.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }else{
                    holder.radioButton.setChecked(true);
                    holder.editText.setPaintFlags(0);
                }
            }
        });
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemListener.onItemClick(position);
                taskBesideList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, taskBesideList.size());
            }
        });
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                holder.editText.setText(text);
                taskbeside.setTaskString(text);
                notifyItemChanged(position);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (taskBesideList.isEmpty()) {
            return 0;
        }else{
            return taskBesideList.size();
        }
    }

    public static class TaskBesideViewHolder extends RecyclerView.ViewHolder {
        private RadioButton radioButton;
        private EditText editText;
        private ImageButton imageButton;
        public TaskBesideViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButton_taskBeside);
            editText = itemView.findViewById(R.id.editText_taskBeside);
            imageButton = itemView.findViewById(R.id.imageButton_taskBeside);
        }
    }

    public interface OnClickItemTaskBeside {
        void onItemClick(int position);
    }
}
