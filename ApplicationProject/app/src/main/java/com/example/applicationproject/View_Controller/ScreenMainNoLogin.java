package com.example.applicationproject.View_Controller;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applicationproject.View_Controller.FragmentNoLogin.CalendarGuestFragment;
import com.example.applicationproject.View_Controller.FragmentNoLogin.GuestFragment;
import com.example.applicationproject.View_Controller.FragmentNoLogin.MissionGuestFragment;
import com.example.applicationproject.R;

public class ScreenMainNoLogin extends AppCompatActivity {

    private int selectedTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen_main_no_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final LinearLayout missionLayout = findViewById(R.id.missionGuestLayout);
        final LinearLayout calendarLayout = findViewById(R.id.calendarGuestLayout);
        final LinearLayout meLayout = findViewById(R.id.meGuestLayout);

        final ImageView missionImage = findViewById(R.id.missionGuestImage);
        final ImageView calendarImage = findViewById(R.id.calendarGuestImage);
        final ImageView meImage = findViewById(R.id.meGuestImage);

        final TextView missionText = findViewById(R.id.missionGuestText);
        final TextView calendarText = findViewById(R.id.calendarGuestText);
        final TextView meText = findViewById(R.id.meGuestText);

        final Toolbar toolbar = findViewById(R.id.toolbarMainNoLogin);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentGuestContainer, MissionGuestFragment.class,null).commit();

        missionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab !=1){

                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentGuestContainer, MissionGuestFragment.class,null).commit();

                    calendarText.setVisibility(View.GONE);
                    meText.setVisibility(View.GONE);

                    calendarImage.setImageResource(R.drawable.calendar);
                    meImage.setImageResource(R.drawable.me);

                    calendarLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    meLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    missionText.setVisibility(View.VISIBLE);
                    missionImage.setImageResource(R.drawable.mission_selected);
                    missionLayout.setBackgroundResource(R.drawable.round_back_mission_100);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    missionLayout.startAnimation(scaleAnimation);

                    selectedTab = 1;
                }
            }
        });

        calendarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab !=2){

                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentGuestContainer, CalendarGuestFragment.class,null).commit();

                    missionText.setVisibility(View.GONE);
                    meText.setVisibility(View.GONE);

                    missionImage.setImageResource(R.drawable.mission);
                    meImage.setImageResource(R.drawable.me);

                    missionLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    meLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    calendarText.setVisibility(View.VISIBLE);
                    calendarImage.setImageResource(R.drawable.calendar_selected);
                    calendarLayout.setBackgroundResource(R.drawable.round_back_mission_100);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    calendarLayout.startAnimation(scaleAnimation);

                    selectedTab = 2;
                }
            }
        });

        meLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab !=3){

                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentGuestContainer, GuestFragment.class,null).commit();

                    calendarText.setVisibility(View.GONE);
                    missionText.setVisibility(View.GONE);

                    calendarImage.setImageResource(R.drawable.calendar);
                    missionImage.setImageResource(R.drawable.mission);

                    calendarLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    missionLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    meText.setVisibility(View.VISIBLE);
                    meImage.setImageResource(R.drawable.me_selected);
                    meLayout.setBackgroundResource(R.drawable.round_back_mission_100);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    meLayout.startAnimation(scaleAnimation);

                    selectedTab = 3;
                }
            }
        });
    }
}