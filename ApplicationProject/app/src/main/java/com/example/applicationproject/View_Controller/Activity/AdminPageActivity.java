package com.example.applicationproject.View_Controller.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationproject.R;
//import com.example.applicationproject.View_Controller.Utils.GmailHelper;

public class AdminPageActivity extends AppCompatActivity {

    private TextView textView_Username;
    private LinearLayout layout_inbox, layout_statistic, layout_setting, layout_user, layout_picture, layout_music, layout_get_upgrade_user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout_admin);
        initWiget();
        final String getName = this.getIntent().getStringExtra("name");
        Log.e("Usernam", "onCreate: " + getName);
        final String getPass = this.getIntent().getStringExtra("password");
        textView_Username.setText(getName);
        layout_inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                GmailHelper Gmail = new GmailHelper(AdminPageActivity.this);
//                Gmail.signIn(AdminPageActivity.this);
//                Gmail.handleSignInResult(getIntent(), AdminPageActivity.this);
//                Gmail.listEmails();
            }
        });
        layout_statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPageActivity.this, StatisticActivity.class);
                intent.putExtra("name", getName);
                intent.putExtra("password", getPass);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        layout_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPageActivity.this, SettingActivity.class);
                intent.putExtra("name", getName);
                intent.putExtra("password", getPass);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        layout_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPageActivity.this, UserActivity.class);
                intent.putExtra("name", getName);
                intent.putExtra("password", getPass);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        layout_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPageActivity.this, PictureActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        layout_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPageActivity.this, MusicActivity.class);
                intent.putExtra("name", getName);
                intent.putExtra("password", getPass);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        layout_get_upgrade_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPageActivity.this, UserActivity.class);
                intent.putExtra("name", getName);
                intent.putExtra("password", getPass);
                startActivity(intent);
            }
        });
    }

    private void initWiget() {
        textView_Username = findViewById(R.id.tVNameUser);
        layout_inbox = findViewById(R.id.layout_inbox);
        layout_statistic = findViewById(R.id.layout_statistic);
        layout_setting = findViewById(R.id.layout_setting);
        layout_user = findViewById(R.id.layout_user);
        layout_picture = findViewById(R.id.layout_picture);
        layout_music = findViewById(R.id.layout_music);
        layout_get_upgrade_user = findViewById(R.id.layout_get_upgrade_user);
    }
}
