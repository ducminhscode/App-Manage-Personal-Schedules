package com.example.applicationproject.View_Controller.Adapter;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.Model.Taskbeside;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.ClassTemp.TaskString;
import com.example.applicationproject.View_Controller.ShareDataMission;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskBesideAdapter extends RecyclerView.Adapter<TaskBesideAdapter.TaskBesideViewHolder> {
    private List<TaskString> taskBesideList;
    private OnClickItemTaskBeside onItemListener;
    private Context context;

    public TaskBesideAdapter(Context context, OnClickItemTaskBeside onItemListener) {
        this.context = context;
        this.onItemListener = onItemListener;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setTaskBesideList(List<TaskString> taskBesideList) {
        this.taskBesideList = taskBesideList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskBesideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_taskbeside, null, false);
        return new TaskBesideViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull TaskBesideAdapter.TaskBesideViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TaskString taskbeside = taskBesideList.get(position);
        AtomicBoolean isChecked = new AtomicBoolean(holder.radioButton.isChecked());
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChecked.get()){
                    holder.radioButton.setChecked(false);
                    isChecked.set(false);
                    holder.editText.setPaintFlags(0);
                }else{
                    holder.radioButton.setChecked(true);
                    isChecked.set(true);
                    holder.editText.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemListener.onItemClick(position, taskbeside.getTaskString());
                holder.editText.setText("");
                taskbeside.setTaskString("");
                notifyItemChanged(position);
                holder.editText.setPaintFlags(0);
            }
        });
        holder.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Kiểm tra nếu nút Enter hoặc Done đã được nhấn
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // Thực hiện hành động bạn muốn khi nhấn Enter (hoàn thành thay đổi dữ liệu)
                    String text = holder.editText.getText().toString();
                    taskbeside.setTaskString(text);
                    notifyItemChanged(position);

                    // Ẩn bàn phím
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(holder.itemView.getWindowToken(), 0);
                    }

                    return true; // Trả về true để ngừng xử lý thêm sự kiện
                }
                return false;
            }
        });

        holder.editText.setText(taskbeside.getTaskString());
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
        private final RadioButton radioButton;
        private final EditText editText;
        private final ImageButton imageButton;
        public TaskBesideViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButton_taskBeside);
            editText = itemView.findViewById(R.id.editText_taskBeside);
            imageButton = itemView.findViewById(R.id.imageButton_taskBeside);
        }
    }

    public interface OnClickItemTaskBeside {
        void onItemClick(int position, String taskString);
    }
}
