package com.example.applicationproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applicationproject.Database.CreateDatabase;


public class ForgotPasswordChange extends AppCompatActivity {

    private boolean newPasswordShowing = false;
    private boolean confirmPasswordShowing = false;

    private CreateDatabase db;

    private Register valid;

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

        final EditText newPassword = findViewById(R.id.newPasswordET);
        final EditText confirmPassword = findViewById(R.id.confirmPasswordET);


        final ImageView newPasswordShowIcon = findViewById(R.id.newPasswordShowIcon);
        final ImageView confirmPasswordShowIcon = findViewById(R.id.confirmPasswordShowIcon);

        final AppCompatButton agreeBtnFW = findViewById(R.id.agreeBtnFW);

        final String getEmail = getIntent().getStringExtra("email");

        db = new CreateDatabase(this);
        valid = new Register();

        backToForgotPasswordCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordChange.this, ForgotPasswordCheck.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


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
                    boolean updateSuccess = db.updatePassword(getEmail, txtNewPassword);
                    if (updateSuccess) {
                        Toast.makeText(ForgotPasswordChange.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPasswordChange.this, Login.class));
                    } else {
                        Toast.makeText(ForgotPasswordChange.this, "Password not changed", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}