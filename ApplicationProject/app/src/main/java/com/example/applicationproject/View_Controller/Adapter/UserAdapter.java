package com.example.applicationproject.View_Controller.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.Model.Mission;
import com.example.applicationproject.Model.User;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.Activity.UserActivity;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.ShareDataMission;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements Filterable {

    private List<User> userlist;
    private OnClickItemUser onItemListener;
    private final Context context;
    private List<User> mlistUserOld;

    public UserAdapter(Context context, OnClickItemUser onItemListener) {
        this.context = context;
        this.onItemListener = onItemListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUserlist(List<User> userlist) {
        this.userlist = userlist;
        this.mlistUserOld = userlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_user, null, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User user = userlist.get(position);
        if (user.getUser_role().equals("admin")){
            holder.imageView_user.setVisibility(View.VISIBLE);
        }else{
            holder.imageView_user.setVisibility(View.GONE);
        }
        holder.textView_name.setText(user.getUser_name());
        holder.textView_email.setText(user.getUser_email());
        holder.textView_phone.setText(user.getPhone_number());
        holder.imageButton_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemListener.onItemClick(position, user.getUser_name(), user.getUser_id());
                if (user.getUser_role().equals("admin")){
                    user.setUser_role("user");
                    holder.imageView_user.setVisibility(View.GONE);
                }else{
                    user.setUser_role("admin");
                    holder.imageView_user.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.imageButton_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Thông báo");
                if (DAO.checkAdmin(context, user.getUser_name())){
                    alertDialog.setMessage(user.getUser_name() + " bị hủy bỏ tư cấp làm admin");
                }else{
                    alertDialog.setMessage(user.getUser_name() + " được cấp quyền làm admin");
                }
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialogInterface, i) -> {
                    DAO.upgradeUser(context, user.getUser_name());
                    notifyItemChanged(position);
                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Hủy", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
                alertDialog.show();
            }
        });
        holder.imageButton_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemListener.onItemClick(position, user.getUser_name(), user.getUser_id());
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Thông báo");
                alertDialog.setMessage("Bạn có chắc muốn xóa");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialogInterface, i) -> {
                    userlist.remove(position);
                    notifyItemRemoved(position);
                    DAO.deleteUser(context, user.getUser_id());
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Hủy", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (userlist == null) {
            return 0;
        }else{
            return userlist.size();
        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private ImageButton imageButton_edit;
        private ImageButton imageButton_delete;
        private ImageView imageView_user, userImage;
        private TextView textView_name;
        private TextView textView_email;
        private TextView textView_phone;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageButton_edit = itemView.findViewById(R.id.btn_edit);
            imageButton_delete = itemView.findViewById(R.id.btn_delete);
            imageView_user = itemView.findViewById(R.id.user_image);
            textView_name = itemView.findViewById(R.id.user_name);
            textView_email = itemView.findViewById(R.id.user_email);
            textView_phone = itemView.findViewById(R.id.user_phone);
            userImage = itemView.findViewById(R.id.userImage);
        }
    }

    private String filterType;

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterPattern = constraint.toString().toLowerCase().trim();

                if (filterPattern.isEmpty()) {
                    userlist = mlistUserOld;
                } else {
                    if (filterType.equals("id")) {
                        List<User> filteredList = new ArrayList<>();
                        for (User user : mlistUserOld) {
                            if (user.getUser_id() == Integer.parseInt(filterPattern)) {
                                filteredList.add(user);
                            }
                        }
                        userlist = filteredList;
                    }else if (filterType.equals("Name")){
                        List<User> filteredList = new ArrayList<>();
                        for (User user : mlistUserOld) {
                            if (user.getUser_name().toLowerCase().contains(filterPattern)) {
                                filteredList.add(user);
                            }
                        }
                        userlist = filteredList;
                    }
                }


                FilterResults results = new FilterResults();
                results.values = userlist;
                results.count = userlist.size();
                return results;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    userlist = (List<User>) results.values;
                    notifyDataSetChanged();  // Cập nhật RecyclerView sau khi lọc
                }
            }
        };
    }

    public interface OnClickItemUser {
        void onItemClick(int position, String name, int id);
    }
}
