package com.example.applicationproject.View_Controller;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationproject.View_Controller.Adapter.ItemCategoryAdapter;
import com.example.applicationproject.View_Controller.FragmentLogin.CalendarFragment;
import com.example.applicationproject.View_Controller.FragmentLogin.MeFragment;
import com.example.applicationproject.View_Controller.FragmentLogin.MissionFragment;
import com.example.applicationproject.R;

public class ScreenMainLogin extends AppCompatActivity implements ItemCategoryAdapter.OnClickItemCategoryItem {

    private int selectedTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen_main_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final LinearLayout missionLayout = findViewById(R.id.missionLayout);
        final LinearLayout calendarLayout = findViewById(R.id.calendarLayout);
        final LinearLayout meLayout = findViewById(R.id.meLayout);

        final ImageView missionImage = findViewById(R.id.missionImage);
        final ImageView calendarImage = findViewById(R.id.calendarImage);
        final ImageView meImage = findViewById(R.id.meImage);

        final TextView missionText = findViewById(R.id.missionText);
        final TextView calendarText = findViewById(R.id.calendarText);
        final TextView meText = findViewById(R.id.meText);

        final LinearLayout filterView = findViewById(R.id.layout_custom_toolbar);
        final Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

//        final ShareDataMission shareDataDialogs = new ViewModelProvider(this).get(ShareDataMission.class);
//
//        Menu menu = toolbar.getMenu();
//        menu.getItem(1).setOnMenuItemClickListener(menuItem -> {
//            toolbar.setVisibility(View.GONE);
//            filterView.setVisibility(View.VISIBLE);
//            return false;
//        });
//
//        final ImageButton btnFilter = findViewById(R.id.btn_openPopupMenuMission);
//        btnFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filterView.setVisibility(View.GONE);
//                toolbar.setVisibility(View.VISIBLE);
//            }
//        });
//        RecyclerView recyclerView = filterView.findViewById(R.id.recycleview_horizontal_managelist);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
//        shareDataDialogs.getCategoryList().observe(this, data -> {
//            ItemCategoryAdapter itemCategoryAdapter = new ItemCategoryAdapter(data, this, false);
//            recyclerView.setAdapter(itemCategoryAdapter);
//        });

        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer, MissionFragment.class,null).commit();

        missionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab !=1){

                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer, MissionFragment.class,null).commit();

                    final String getName = getIntent().getStringExtra("name");
                    
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


                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer, CalendarFragment.class,null).commit();

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

                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer, MeFragment.class,null).commit();

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

    @Override
    public void onItemClick(int position, int id, boolean isChosing, int selectionItem) {

    }

    @Override
    public void onRestart() {

        super.onRestart();
    }
}