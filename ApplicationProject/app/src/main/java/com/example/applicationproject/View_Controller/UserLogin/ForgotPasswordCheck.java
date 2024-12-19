package com.example.applicationproject.View_Controller.UserLogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applicationproject.Database.ToDoDBHelper;
import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.DAO;

public class ForgotPasswordCheck extends AppCompatActivity {

    ToDoDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_check);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final ImageView backToLogin = findViewById(R.id.backToLogin);
        final EditText emailAddressFW = findViewById(R.id.emailAddressFW);
        final EditText userNameFW = findViewById(R.id.userNameFW);
        final AppCompatButton nextBtnFW = findViewById(R.id.nextBtnFW);

        db = new ToDoDBHelper(this);

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nextBtnFW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txtUsernameFW = userNameFW.getText().toString();
                String txtEmailAddressFW = emailAddressFW.getText().toString();

                boolean isValidUsernameAndEmail = DAO.checkUsernameAndEmailExists(getBaseContext(),txtUsernameFW, txtEmailAddressFW);

                boolean isUsernameExists = DAO.checkUsernameExists(getBaseContext(),txtUsernameFW);
                boolean isEmailExists = DAO.checkEmailExists(getBaseContext(),txtEmailAddressFW);


                if (txtUsernameFW.isEmpty()) {
                    userNameFW.setError("Please enter your username");
                    userNameFW.requestFocus();
                } else if (txtEmailAddressFW.isEmpty()) {
                    emailAddressFW.setError("Please enter your email");
                    emailAddressFW.requestFocus();

                } else if (!isUsernameExists) {
                    userNameFW.setError("Username not found");
                    userNameFW.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmailAddressFW).matches()) {
                    emailAddressFW.setError("Email not valid");
                    emailAddressFW.requestFocus();

                } else if (!isEmailExists || !isValidUsernameAndEmail) {
                    emailAddressFW.setError("Email not found");
                    emailAddressFW.requestFocus();

                } else {

                    final String getEmailTxt = txtEmailAddressFW;

                    Intent intent = new Intent(ForgotPasswordCheck.this, ForgotPasswordOTP.class);
                    intent.putExtra("email", getEmailTxt);

                    startActivity(intent);
                }
            }
        });
    }
}