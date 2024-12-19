package com.example.applicationproject.View_Controller.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.Model.Category;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.ShareDataMission;

import java.util.List;

public class ItemCategoryAdapter extends RecyclerView.Adapter<ItemCategoryAdapter.ItemCategoryViewHolder> {

    private List<Category> categoryList;
    private OnClickItemCategoryItem onItemListener;
    private boolean isChosing;
    private int selectionItem = -1;
    private Context context;

    @SuppressLint("NotifyDataSetChanged")
    public ItemCategoryAdapter(Context context, OnClickItemCategoryItem onItemListener, boolean isChosing) {
        this.context = context;
        this.onItemListener = onItemListener;
        this.isChosing = isChosing;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_items, parent, false);
        return new ItemCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCategoryAdapter.ItemCategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Category category = categoryList.get(position);
        if (isChosing) {
            if(position == selectionItem){
                holder.layoutItemCate.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primary));
                holder.itemCategory.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            }else{
                holder.layoutItemCate.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.caption5));
                holder.itemCategory.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));
            }
        }else{
            if (position == 0 && selectionItem == -1){
                selectionItem = position;
            }
            if(position == selectionItem){
                holder.layoutItemCate.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primary));
                holder.itemCategory.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            }else{
                holder.layoutItemCate.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.caption5));
                holder.itemCategory.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));
            }
        }
        holder.itemCategory.setText(category.getCategory_name());
        holder.layoutItemCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.onItemClick(position, category.getCategory_id(), isChosing, selectionItem);
                if (isChosing) {

                }else{
                    if(selectionItem != position) {
                        int oldPosition = selectionItem;
                        selectionItem = position;
                        notifyItemChanged(position);
                        if (oldPosition != -1) {
                            notifyItemChanged(oldPosition);
                        }
                    }
                }
            }
        });

    }

    public void setSelectionItem(int selectionItem) {
        this.selectionItem = selectionItem;
        notifyItemChanged(selectionItem);
    }

    @Override
    public int getItemCount() {
        if (categoryList == null) {
            return 0;
        }else{
            return categoryList.size();
        }
    }

    public static class ItemCategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemCategory;
        private final LinearLayout layoutItemCate;
        public ItemCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCategory = itemView.findViewById(R.id.category_item);
            layoutItemCate = itemView.findViewById(R.id.layout_item_cate);
        }
    }

    public interface OnClickItemCategoryItem {
        void onItemClick(int position, int id, boolean isChosing, int selectionItem);

        void onRestart();
    }
}
