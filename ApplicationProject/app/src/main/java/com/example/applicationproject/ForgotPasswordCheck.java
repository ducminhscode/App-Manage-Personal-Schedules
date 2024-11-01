package com.example.applicationproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotPasswordCheck extends AppCompatActivity {

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

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nextBtnFW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getEmailTxt = emailAddressFW.getText().toString();

                Intent intent = new Intent(ForgotPasswordCheck.this, ForgotPasswordOTP.class);

                intent.putExtra("email", getEmailTxt);

                startActivity(intent);
            }
        });
    }
}