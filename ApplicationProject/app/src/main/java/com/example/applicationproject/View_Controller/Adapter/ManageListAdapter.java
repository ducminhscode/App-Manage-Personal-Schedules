package com.example.applicationproject.View_Controller.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.Model.Category;
import com.example.applicationproject.Model.Mission;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.Activity.ManageListActivity;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.ShareDataMission;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ManageListAdapter extends RecyclerView.Adapter<ManageListAdapter.ManageListViewHolder>{

    private List<Category> categoryList;
    private final OnClickItemCategoryMore onItemListener;
    private final Context context;
    private List<Mission> missionList;
    private int userId;

    @SuppressLint("NotifyDataSetChanged")
    public ManageListAdapter(Context context, OnClickItemCategoryMore onItemListener) {
       this.context = context;
       this.onItemListener = onItemListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUpdate(List<Category> categoryList, List<Mission> missionList){
        this.categoryList = categoryList;
        this.missionList = missionList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ManageListAdapter.ManageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.managelist_items, parent, false);
        return new ManageListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Category categoryCustom = categoryList.get(position);
        holder.managelist_item.setText(categoryCustom.getCategory_name());
        holder.managelist_item_count.setText(String.valueOf(getEvents(categoryCustom.getCategory_id()).size()));
        holder.managelist_item_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.onItemClick(position, categoryCustom.getCategory_name(), categoryCustom.getCategory_id());
                PopupMenu popupMenu = new PopupMenu(context, holder.managelist_item_more);
                popupMenu.getMenuInflater().inflate(R.menu.manage_list_context_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.editing_menu) {
                            Dialog dialog = new Dialog(context);
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
                            editText_managelist.setText(categoryCustom.getCategory_name());
                            btn_save.setOnClickListener(view -> {
                                if (DAO.checkCategory(context, editText_managelist.getText().toString())){
                                    Toast.makeText(context, "Danh mục đã tồn tại", Toast.LENGTH_SHORT).show();
                                }else{
                                    if (DAO.updateCategory(context, categoryCustom.getCategory_id(), editText_managelist.getText().toString(), userId)){
                                        Toast.makeText(context, "Update Successfull", Toast.LENGTH_SHORT).show();
                                        notifyItemChanged(position);
                                        dialog.dismiss();
                                    }else{
                                        Toast.makeText(context, "Fail Update", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            });
                            btn_cancel.setOnClickListener(view -> {
                                dialog.dismiss();
                            });
                        return false;
                    }
                    else if (item.getItemId() == R.id.deleting_menu) {
                        Toast.makeText(context, "check" + position, Toast.LENGTH_SHORT).show();
                        Log.e("onclick", "onItemClick: " + position + " " + categoryCustom.getCategory_id());
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Xóa danh mục");
                        builder.setMessage("Bạn có muốn xóa danh mục " + categoryCustom.getCategory_name() + " không?");
                        builder.setPositiveButton("Yes", (di, which) -> {
                            Log.e("onclick", "onClick: " + position + " " + categoryCustom.getCategory_id());
                            if (DAO.deleteCategory(context, categoryCustom.getCategory_id())){
                                Toast.makeText(context, "Delete Successfull", Toast.LENGTH_SHORT).show();
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, getItemCount());
                            }else{
                                Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                            };
                        });
                        builder.setNegativeButton("No", (di, which) -> {
                            di.dismiss();
                        });
                        builder.show();
                        return false;
                    }
                    return false;
                });
            }
        });
    }

    public void getUserid (int id){
        this.userId = id;
    }

    private List<Mission> getEvents(int id) {
        return missionList.stream().filter(mission -> mission.getCategory_id() == id).collect(Collectors.toList());
    }

    @Override
    public int getItemCount() {
        if (categoryList == null) {
            return 0;
        }
        return categoryList.size();
    }

    public static class ManageListViewHolder extends RecyclerView.ViewHolder {
        public final TextView managelist_item;
        public final TextView managelist_item_count;
        public final ImageButton managelist_item_more;

        public ManageListViewHolder(@NonNull View itemView) {
            super(itemView);
            managelist_item = itemView.findViewById(R.id.managelist_item);
            managelist_item_count = itemView.findViewById(R.id.managelist_item_count);
            managelist_item_more = itemView.findViewById(R.id.managelist_item_more);
        }
    }

    public interface OnClickItemCategoryMore {
        void onItemClick(int position, String categoryName, int id);
    }
}
