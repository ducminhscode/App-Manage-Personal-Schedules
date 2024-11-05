package com.example.applicationproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applicationproject.Database.CreateDatabase;


public class Login extends AppCompatActivity {

    private boolean passwordShowing = false;
    private boolean remember = false;

    private CreateDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText userNameET = findViewById(R.id.userNameET);
        final EditText passwordET = findViewById(R.id.passwordET);
        final ImageView passwordShowIcon = findViewById(R.id.passwordShowIcon);
        final TextView signUpBtnLogin = findViewById(R.id.signUpBtnLogin);
        final RelativeLayout signInWithGoogle = findViewById(R.id.signInWithGoogle);
        final TextView forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);
        final AppCompatButton signInBtnLogin = findViewById(R.id.signInBtnLogin);
        final CheckBox checkRememberBtn = findViewById(R.id.checkRememberBtn);

        db = new CreateDatabase(this);

        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        String un = pref.getString("USERNAME", "");
        String ps = pref.getString("PASSWORD", "");
        userNameET.setText(un);
        passwordET.setText(ps);

        checkRememberBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                remember = true;
            } else {
                remember = false;
            }
        });

        passwordShowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordShowing) {
                    passwordShowing = false;
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordShowIcon.setImageResource(R.drawable.hide);
                } else {
                    passwordShowing = true;
                    passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordShowIcon.setImageResource(R.drawable.show);
                }
                passwordET.setSelection(passwordET.length());
            }
        });

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgotPasswordCheck.class));
            }
        });

        signInBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isValidUser = db.checkUser(userNameET.getText().toString(),passwordET.getText().toString());

                if (userNameET.getText().toString().isEmpty()) {
                    userNameET.setError("Please enter your username");
                    userNameET.requestFocus();
                } else if (passwordET.getText().toString().isEmpty()) {
                    passwordET.setError("Please enter your password");
                    passwordET.requestFocus();
                } else if(!isValidUser) {
                    Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }else{
                    rememberUser(userNameET.getText().toString(), passwordET.getText().toString(), remember);
                    startActivity(new Intent(Login.this, MainActivity.class));
                }
            }
        });

        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        signUpBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }

    private void rememberUser(String u, String p, boolean status) {
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (!status) {
            editor.clear();
        } else {
            editor.putString("USERNAME", u);
            editor.putString("PASSWORD", p);
            editor.putBoolean("REMEMBER", status);
        }
        editor.commit();
    }
}