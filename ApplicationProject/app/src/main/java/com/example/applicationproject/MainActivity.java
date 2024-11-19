package com.example.applicationproject;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.res.Resources;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements OnCreateContextMenuListener, NavigationView.OnNavigationItemSelectedListener {

    TextView name, email;
    AppCompatButton signOutBtn;
    BottomNavigationView btn;
    DrawerLayout drawer;
    NavigationView nvb;
    ImageButton img;
    Toolbar tl;
    FloatingActionButton fab;
//    private Login login = new Login();
//    private GoogleSignInClient nGoogleSignInClient = login.mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> {
            return false;
        });

        setContentView(R.layout.activity_main);
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            // Áp dụng window insets listener
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                if (insets != null) {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                }
                return null;
            });
        }

        btn = findViewById(R.id.bottomNavigationView);
        drawer = findViewById(R.id.drawer_layout);
        nvb = findViewById(R.id.navigation_bar_item_side);
        tl = findViewById(R.id.toolbar);
        setSupportActionBar(tl);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Hien len", Toast.LENGTH_SHORT).show();
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tl, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        nvb.setNavigationItemSelectedListener(this);



//        name = findViewById(R.id.name);
//        email = findViewById(R.id.email);
//        signOutBtn = findViewById(R.id.signOutBtn);




//        btn.setBackground(null);
//        btn.getMenu().getItem(0).setEnabled(false);
//        btn.getMenu().getItem(1).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
//        signOutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                signOutWithGG();
//
//            }
//        });


//    private void signOutWithGG() {
//        nGoogleSignInClient.signOut().addOnCompleteListener(this,
//                new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(Task<Void> task) {
//                        startActivity(new Intent(MainActivity.this, Login.class));
//                    }
//                });
//    }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_drawer, menu);
        return true;
    }



}