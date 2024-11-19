package com.example.applicationproject.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.example.applicationproject.R;

public class ForgotPasswordCheck extends AppCompatActivity {

    CreateDatabase db;

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
        final AppCompatButton nextBtnFW = findViewById(R.id.nextBtnFW);

        db = new CreateDatabase(this);

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nextBtnFW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isValidEmail = db.checkEmailExists(emailAddressFW.getText().toString());

                if (emailAddressFW.getText().toString().isEmpty()) {
                    emailAddressFW.setError("Please enter your email");
                    emailAddressFW.requestFocus();

                } else if (emailAddressFW.getText().toString().contains(" ")) {
                    emailAddressFW.setError("The string contains spaces");
                    emailAddressFW.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddressFW.getText().toString()).matches()) {
                    emailAddressFW.setError("Email not valid");
                    emailAddressFW.requestFocus();

                } else if (!isValidEmail) {
                    Toast.makeText(ForgotPasswordCheck.this, "Email not found", Toast.LENGTH_SHORT).show();
                } else {

                    final String getEmailTxt = emailAddressFW.getText().toString();

                    Intent intent = new Intent(ForgotPasswordCheck.this, ForgotPasswordChange.class);
                    intent.putExtra("email", getEmailTxt);

                    startActivity(intent);
                }
            }
        });
    }
}