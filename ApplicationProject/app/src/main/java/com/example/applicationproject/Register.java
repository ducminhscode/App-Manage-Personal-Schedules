package com.example.applicationproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register extends AppCompatActivity {

    private boolean passwordShowing = false;
    private boolean confirmPasswordShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText email = findViewById(R.id.emailET);
        final EditText mobile = findViewById(R.id.mobileET);
        final EditText password = findViewById(R.id.passwordET);
        final EditText confirmPassword = findViewById(R.id.confirmPasswordET);

        final ImageView passwordShowIcon = findViewById(R.id.passwordShowIcon);
        final ImageView confirmPasswordShowIcon = findViewById(R.id.confirmPasswordShowIcon);

        final AppCompatButton signUpBtnRegister = findViewById(R.id.signUpBtnRegister);

        final TextView signInBtnRegister = findViewById(R.id.signInBtnRegister);

        final CheckBox checkLegitBtn = findViewById(R.id.checkLegitBtn);

        passwordShowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordShowing) {

                    passwordShowing = false;
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordShowIcon.setImageResource(R.drawable.show);

                } else {

                    passwordShowing = true;
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordShowIcon.setImageResource(R.drawable.hide);

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
                    confirmPasswordShowIcon.setImageResource(R.drawable.show);

                } else {

                    confirmPasswordShowing = true;
                    confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    confirmPasswordShowIcon.setImageResource(R.drawable.hide);

                }
                confirmPassword.setSelection(confirmPassword.length());
            }
        });

        checkLegitBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                signUpBtnRegister.setEnabled(true);
            } else {
                signUpBtnRegister.setEnabled(false);
            }
        });

        signUpBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLegitBtn.isChecked()) {
                    final String getMobileTxt = mobile.getText().toString();
                    final String getEmailTxt = email.getText().toString();

                    Intent intent = new Intent(Register.this, OTPVerification.class);

                    intent.putExtra("mobile", getMobileTxt);
                    intent.putExtra("email", getEmailTxt);

                    startActivity(intent);
                }
                else{
                    Toast.makeText(Register.this, "You must agree to our terms and conditions", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signInBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}