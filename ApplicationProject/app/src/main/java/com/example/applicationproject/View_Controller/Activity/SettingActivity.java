package com.example.applicationproject.View_Controller.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.DAO;
import com.example.applicationproject.View_Controller.ScreenMainNoLogin;
import com.example.applicationproject.View_Controller.ShareDataMission;
import com.example.applicationproject.View_Controller.UserLogin.ForgotPasswordChange;
import com.example.applicationproject.View_Controller.UserLogin.Login;
import com.example.applicationproject.View_Controller.UserLogin.Register;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class SettingActivity extends AppCompatActivity {

    private TextView tvUserName, tvEmail;
    private ImageView imgBack;
    private LinearLayout layoutUpdateUserInfo, layoutMoveToAnotherAccount, layoutChangePassWord, layoutLogOut;
    private ShareDataMission shareDataDialogs;
    private boolean newPasswordShowing = false;
    private boolean confirmPasswordShowing = false;
    private ShareDataMission shareDataMission;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout_admin);
        initWiget();
        shareDataDialogs = new ViewModelProvider(this).get(ShareDataMission.class);
        String username = getIntent().getStringExtra("name");
        AtomicReference<String> userEmail = new AtomicReference<>();
        DAO.getUserId(this, username);
        if (DAO.getUserId(this, username) != -1) {
            shareDataDialogs.getUserList().observe(this, userList -> {
                userEmail.set(userList.stream().filter(w -> w.getUser_id() == DAO.getUserId(this, username)).findFirst().get().getUser_email());
            });    
        }
        imgBack.setOnClickListener(view -> {
            finish();
        });
        tvUserName.setText(username);
        tvEmail.setText(userEmail.get());
        layoutUpdateUserInfo.setOnClickListener(view -> {
            Intent intent = new Intent(SettingActivity.this, UpdateUserInfoActivity.class);
            startActivity(intent);
        });
        layoutMoveToAnotherAccount.setOnClickListener(view -> {
            Intent intent = new Intent(SettingActivity.this, Login.class);
            startActivity(intent);
        });
        layoutChangePassWord.setOnClickListener(view -> {
            showdialog(userEmail.get());
        });
        layoutLogOut.setOnClickListener(view -> {
            Intent intent = new Intent(SettingActivity.this, ScreenMainNoLogin.class);
            startActivity(intent);
        });
    }

    private void showdialog(String email) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_change_password);
        dialog.setCanceledOnTouchOutside(true);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        EditText newPassword = dialog.findViewById(R.id.newPasswordET);
        EditText confirmPassword = dialog.findViewById(R.id.confirmPasswordET);


        ImageView newPasswordShowIcon = dialog.findViewById(R.id.newPasswordShowIcon);
        ImageView confirmPasswordShowIcon = dialog.findViewById(R.id.confirmPasswordShowIcon);

        AppCompatButton agreeBtnFW = dialog.findViewById(R.id.agreeBtnFW);

        newPasswordShowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newPasswordShowing) {

                    newPasswordShowing = false;
                    newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    newPasswordShowIcon.setImageResource(R.drawable.hide);

                } else {

                    newPasswordShowing = true;
                    newPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    newPasswordShowIcon.setImageResource(R.drawable.show);

                }
                newPassword.setSelection(newPassword.length());
            }
        });

        confirmPasswordShowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmPasswordShowing) {

                    confirmPasswordShowing = false;
                    confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirmPasswordShowIcon.setImageResource(R.drawable.hide);

                } else {

                    confirmPasswordShowing = true;
                    confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    confirmPasswordShowIcon.setImageResource(R.drawable.show);

                }
                confirmPassword.setSelection(confirmPassword.length());
            }
        });

        agreeBtnFW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register valid = new Register();
                String txtNewPassword = newPassword.getText().toString();
                String txtConfirmPassword = confirmPassword.getText().toString();

                if (txtNewPassword.isEmpty()) {
                    newPassword.setError("Please enter your password");
                    newPassword.requestFocus();

                } else if (!valid.isPasswordValid(txtNewPassword)) {
                    newPassword.setError("Password must contain at least 8 characters, including a letter, a digit, and a special character");
                    newPassword.requestFocus();

                } else if (txtConfirmPassword.isEmpty()) {
                    confirmPassword.setError("Please enter confirm password");
                    confirmPassword.requestFocus();

                } else if (!txtNewPassword.equals(txtConfirmPassword)) {
                    confirmPassword.setError("Password not match");
                    confirmPassword.requestFocus();
                } else {
                    boolean updateSuccess = DAO.updatePassword(getBaseContext(),email, txtNewPassword);
                    if (updateSuccess) {
                        Toast.makeText(SettingActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(SettingActivity.this, "Password not changed", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

            }
        });
    }

    private void initWiget() {
        tvUserName = findViewById(R.id.username);
        tvEmail = findViewById(R.id.userGmail);
        imgBack = findViewById(R.id.imgBack);
        layoutUpdateUserInfo = findViewById(R.id.layout_update_user);
        layoutMoveToAnotherAccount = findViewById(R.id.layoutMoveToAnotherAccount);
        layoutChangePassWord = findViewById(R.id.layoutChangePassWord);
        layoutLogOut = findViewById(R.id.layoutLogOut);
    }
}
