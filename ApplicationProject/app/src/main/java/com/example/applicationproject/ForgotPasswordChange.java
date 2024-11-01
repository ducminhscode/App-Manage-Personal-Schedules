package com.example.applicationproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Currency;

public class ForgotPasswordChange extends AppCompatActivity {

    private boolean passwordShowing = false;
    private boolean confirmPasswordShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_change);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final ImageView backToForgotPasswordCheck = findViewById(R.id.backToForgotPasswordCheck);
        final EditText password = findViewById(R.id.passwordET);
        final EditText confirmPassword = findViewById(R.id.confirmPasswordET);

        final ImageView passwordShowIcon = findViewById(R.id.passwordShowIcon);
        final ImageView confirmPasswordShowIcon = findViewById(R.id.confirmPasswordShowIcon);

        final AppCompatButton agreeBtnFW = findViewById(R.id.agreeBtnFW);

        backToForgotPasswordCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordChange.this, ForgotPasswordCheck.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        passwordShowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordShowing) {

                    passwordShowing = false;
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordShowIcon.setImageResource(R.drawable.hide);

                } else {

                    passwordShowing = true;
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordShowIcon.setImageResource(R.drawable.show);

                }
                password.setSelection(password.length());
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
    }
}