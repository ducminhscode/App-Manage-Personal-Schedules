package com.example.applicationproject.View_Controller.UserLogin;

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

import com.example.applicationproject.Database.ToDoDBHelper;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.DAO;


public class Register extends AppCompatActivity {

    private boolean passwordShowing = false;
    private boolean confirmPasswordShowing = false;

    private ToDoDBHelper db;

    private OTPVerification isOTP;

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

        db = new ToDoDBHelper(this);

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

                    String txtName = name.getText().toString();
                    String txtEmail = email.getText().toString();
                    String txtMobile = mobile.getText().toString();

                    boolean isUserExists = DAO.checkUsernameExists(getBaseContext(), txtName);
                    boolean isEmailExists = DAO.checkEmailExists(getBaseContext(), txtEmail);
                    boolean isMobileExists = DAO.checkMobileExists(getBaseContext(),txtMobile);

                    if (txtName.isEmpty()) {
                        name.setError("Please enter your username");
                        name.requestFocus();

                    } else if (txtName.contains(" ")) {
                        name.setError("The string contains spaces");
                        name.requestFocus();

                    } else if (isUserExists) {
                        name.setError("Username already exists");
                        name.requestFocus();

                    } else if (txtEmail.isEmpty()) {
                        email.setError("Please enter your email");
                        email.requestFocus();

                    } else if (txtEmail.contains(" ")) {
                        email.setError("The string contains spaces");
                        email.requestFocus();

                    } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
                        email.setError("Email not valid");
                        email.requestFocus();

                    } else if (isEmailExists) {
                        email.setError("Email already exists");
                        email.requestFocus();

                    } else if (txtMobile.isEmpty()) {
                        mobile.setError("Please enter your mobile");
                        mobile.requestFocus();

                    } else if (!txtMobile.matches("^(0[1-9]\\d{8,9})$")) {
                        mobile.setError("Mobile not valid");
                        mobile.requestFocus();

                    } else if (isMobileExists) {
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

                        final String getNameTxt = txtName.trim();
                        final String getEmailTxt = txtEmail.trim();
                        final String getMobileTxt = txtMobile.trim();
                        final String getPasswordTxt = password.getText().toString().trim();


                        Intent intent = new Intent(Register.this, OTPVerification.class);

                        intent.putExtra("name", getNameTxt);
                        intent.putExtra("email", getEmailTxt);
                        intent.putExtra("mobile", getMobileTxt);
                        intent.putExtra("password", getPasswordTxt);

                        startActivity(intent);

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

    public boolean isPasswordValid(String password) {
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