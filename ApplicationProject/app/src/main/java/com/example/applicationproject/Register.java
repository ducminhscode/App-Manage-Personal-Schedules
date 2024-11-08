package com.example.applicationproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
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

import com.example.applicationproject.Database.CreateDatabase;


public class Register extends AppCompatActivity {

    private boolean passwordShowing = false;
    private boolean confirmPasswordShowing = false;

    private CreateDatabase db;

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

        final EditText name = findViewById(R.id.userNameET);
        final EditText email = findViewById(R.id.emailET);
        final EditText mobile = findViewById(R.id.mobileET);
        final EditText password = findViewById(R.id.passwordET);
        final EditText confirmPassword = findViewById(R.id.confirmPasswordET);

        final ImageView passwordShowIcon = findViewById(R.id.passwordShowIcon);
        final ImageView confirmPasswordShowIcon = findViewById(R.id.confirmPasswordShowIcon);

        final AppCompatButton signUpBtnRegister = findViewById(R.id.signUpBtnRegister);

        final TextView signInBtnRegister = findViewById(R.id.signInBtnRegister);

        final CheckBox checkLegitBtn = findViewById(R.id.checkLegitBtn);

        final TextView termsOfServiceBtn = findViewById(R.id.termsOfServiceBtn);
        final TextView privacyPolicyBtn = findViewById(R.id.privacyPolicyBtn);

        db = new CreateDatabase(this);

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

        checkLegitBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                signUpBtnRegister.setEnabled(true);
            } else {
                signUpBtnRegister.setEnabled(false);
            }
        });

        privacyPolicyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, PrivacyPolicyAndTermsOfService.class);
                startActivity(intent);
            }
        });

        termsOfServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, PrivacyPolicyAndTermsOfService.class);
                startActivity(intent);
            }
        });

        signUpBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkLegitBtn.isChecked()) {

                    boolean isValidUser = db.checkUsernameExists(name.getText().toString());
                    boolean isValidEmail = db.checkEmailExists(email.getText().toString());
                    boolean isValidMobile = db.checkExists(mobile.getText().toString());

                    if (name.getText().toString().isEmpty()) {
                        name.setError("Please enter your username");
                        name.requestFocus();

                    } else if (name.getText().toString().contains(" ")) {
                        name.setError("The string contains spaces");
                        name.requestFocus();

                    } else if (isValidUser) {
                        name.setError("Username already exists");
                        name.requestFocus();

                    } else if (email.getText().toString().isEmpty()) {
                        email.setError("Please enter your email");
                        email.requestFocus();

                    } else if (email.getText().toString().contains(" ")) {
                        email.setError("The string contains spaces");
                        email.requestFocus();

                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                        email.setError("Email not valid");
                        email.requestFocus();

                    } else if (isValidEmail) {
                        email.setError("Email already exists");
                        email.requestFocus();

                    } else if (mobile.getText().toString().isEmpty()) {
                        mobile.setError("Please enter your mobile");
                        mobile.requestFocus();

                    } else if (!mobile.getText().toString().matches("^(0[1-9]\\d{8,9})$")) {
                        mobile.setError("Mobile not valid");
                        mobile.requestFocus();

                    } else if (isValidMobile) {
                        mobile.setError("Mobile already exists");
                        mobile.requestFocus();

                    } else if (password.getText().toString().isEmpty()) {
                        password.setError("Please enter your password");
                        password.requestFocus();

                    } else if (!isPasswordValid(password.getText().toString())) {
                        password.setError("Password must contain at least 8 characters, including a letter, a digit, and a special character");
                        password.requestFocus();

                    } else if (confirmPassword.getText().toString().isEmpty()) {
                        confirmPassword.setError("Please enter confirm password");
                        confirmPassword.requestFocus();

                    } else if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                        confirmPassword.setError("Password not match");
                        confirmPassword.requestFocus();

                    } else {
                        CreateDatabase credb = new CreateDatabase(Register.this);
                        credb.addUser(name.getText().toString().trim(), email.getText().toString().trim(),
                                mobile.getText().toString().trim(), password.getText().toString().trim());

                    /*final String getMobileTxt = mobile.getText().toString();
                    final String getEmailTxt = email.getText().toString();

                    Intent intent = new Intent(Register.this, OTPVerification.class);

                    intent.putExtra("mobile", getMobileTxt);
                    intent.putExtra("email", getEmailTxt);

                    startActivity(intent);*/
                        Toast.makeText(Register.this, "Register successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, Login.class));
                    }
                } else {
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

    protected boolean isPasswordValid(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isWhitespace(c)) {
                hasSpecialChar = true;
            }
        }
        return hasLetter && hasDigit && hasSpecialChar;
    }
}