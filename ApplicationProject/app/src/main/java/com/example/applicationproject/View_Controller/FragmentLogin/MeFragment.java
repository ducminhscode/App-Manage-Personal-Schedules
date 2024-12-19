package com.example.applicationproject.View_Controller.FragmentLogin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applicationproject.Database.ToDoDBHelper;
import com.example.applicationproject.View_Controller.Activity.AdminPageActivity;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.ScreenMainNoLogin;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.Service.FouroundService;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeFragment extends Fragment {

    private String appPackageName;
    private boolean nightMode;
    private static final String CHANEL_ID = "channel_service";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeFragment() {
        // Required empty public constructor
    }


    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        appPackageName = requireActivity().getPackageName();

        RelativeLayout layoutRatingRespose = view.findViewById(R.id.layout_rating_response);

        layoutRatingRespose.setOnClickListener(view1 -> {
            showdialogRating();
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RelativeLayout layoutSharing = view.findViewById(R.id.layout_sharing);

        layoutSharing.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Check this app\n" + "https://www.youtube.com/watch?v=dQw4w9WgXcQ" + appPackageName);
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, "Share this app"));
        });

        final String getName = requireActivity().getIntent().getStringExtra("name");
        final String getPass = requireActivity().getIntent().getStringExtra("password");
        ToDoDBHelper toDoDBHelper = new ToDoDBHelper(this.requireContext());



        TextView userName = view.findViewById(R.id.userName);
        RelativeLayout signOutUser = view.findViewById(R.id.signOutUser);
        LinearLayout moveToAdmin = view.findViewById(R.id.moveToAdmin);

        if (DAO.checkAdmin(this.requireContext(), getName)){
            moveToAdmin.setVisibility(View.VISIBLE);
        }else{
            moveToAdmin.setVisibility(View.GONE);
        }

        moveToAdmin.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), AdminPageActivity.class);
            intent.putExtra("name", getName);
            intent.putExtra("password", getPass);
            startActivity(intent);
        });

        userName.setText(getName);

        signOutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), ScreenMainNoLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        RelativeLayout layoutNotify = view.findViewById(R.id.layout_notifycation);
        SwitchCompat switchCompat = view.findViewById(R.id.switchNotify);

        SwitchCompat switchCompat1 = view.findViewById(R.id.switchMode);

        if (nightMode){
            switchCompat1.setChecked(true);
        }else{
            switchCompat1.setChecked(false);
        }

        switchCompat1.setOnClickListener(view1 -> {
            if (nightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        layoutNotify.setOnClickListener(view1 -> {
            if (switchCompat.isChecked()){
                switchCompat.setChecked(false);
                onStopService();
            }else{
                switchCompat.setChecked(true);
                onStartService();
            }
        });

        switchCompat.setOnClickListener(view1 -> {
            if (switchCompat.isChecked()){
                switchCompat.setChecked(false);
                onStopService();
            }else{
                switchCompat.setChecked(true);
                onStartService();
            }
        });

        return view;
    }

    private void showdialogRating() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rating_app_layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        dialog.getWindow().setGravity(Gravity.CENTER);

        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        TextView textViewContent = dialog.findViewById(R.id.textView_Content);
        AppCompatButton btnSubmit = dialog.findViewById(R.id.btn_submit);
        AppCompatButton btnResponse = dialog.findViewById(R.id.btn_respose);

        ratingBar.setOnRatingBarChangeListener((ratingBar1, v, b) -> {
            switch ((int) ratingBar1.getRating()){
                case 1: textViewContent.setText("Very Bad! :(");
                    break;
                case 2: textViewContent.setText("Bad!");
                    break;
                case 3: textViewContent.setText("Normal!");
                    break;
                case 4: textViewContent.setText("Good!");
                    break;
                case 5: textViewContent.setText("Awsome! Excellent!");
                    break;
                default:
                    break;
            }
        });

        btnSubmit.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ" + appPackageName)));
        });

        btnResponse.setOnClickListener(view -> {
            moveToGmail();
        });

    }

    @SuppressLint("QueryPermissionsNeeded")
    private void moveToGmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // chỉ định gửi email
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"2251052068minh@gmail.com"}); // Địa chỉ email người nhận
        intent.putExtra(Intent.EXTRA_SUBJECT, "Phản Hồi"); // Chủ đề email

// Kiểm tra xem có ứng dụng email nào khả dụng không
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Không có ứng dụng email nào, có thể thông báo cho người dùng
            Toast.makeText(this.requireContext(), "Không tìm thấy ứng dụng email", Toast.LENGTH_SHORT).show();
        }

    }

    private void onStartService() {
        Intent intent = new Intent(this.requireContext(), FouroundService.class);
        intent.putExtra("title", "ToDoList");
        intent.putExtra("content", "Thêm nhiệm vụ");
        requireActivity().startService(intent);
    }

    private void onStopService(){
        Intent intent = new Intent(this.requireContext(), FouroundService.class);
        requireActivity().stopService(intent);
    }
}