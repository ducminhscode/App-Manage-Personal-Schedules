package com.example.applicationproject.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applicationproject.R;

public class ForgotPasswordOTP extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private TextView resendBtnFW;

    private boolean resendEnabled = false;
    private int resendTime = 90;

    private int selectedETPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        otp1 = findViewById(R.id.otpET1);
        otp2 = findViewById(R.id.otpET2);
        otp3 = findViewById(R.id.otpET3);
        otp4 = findViewById(R.id.otpET4);
        otp5 = findViewById(R.id.otpET5);
        otp6 = findViewById(R.id.otpET6);

        resendBtnFW = findViewById(R.id.resendBtnFW);

        final Button verifyBtnFW = findViewById(R.id.verifyBtnFW);
        final TextView otpEmailFW = findViewById(R.id.otpEmailFW);

        final String getEmail = getIntent().getStringExtra("email");

        final ImageView backToForgotPasswordCheck = findViewById(R.id.backToForgotPasswordCheck);

        otpEmailFW.setText(getEmail);

        otp1.addTextChangedListener(textWatcher);
        otp2.addTextChangedListener(textWatcher);
        otp3.addTextChangedListener(textWatcher);
        otp4.addTextChangedListener(textWatcher);
        otp5.addTextChangedListener(textWatcher);
        otp6.addTextChangedListener(textWatcher);

        showKeyboard(otp1);

        startCountDownTimer();

        resendBtnFW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resendEnabled) {

                    startCountDownTimer();

                }
            }
        });
        verifyBtnFW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String generateOtp = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString() + otp5.getText().toString() + otp6.getText().toString();

                if (generateOtp.length() == 6) {
                    //handle your otp verification here
                }
                //if confirm correct otp
                startActivity(new Intent(ForgotPasswordOTP.this, ForgotPasswordChange.class));
            }
        });

        backToForgotPasswordCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void showKeyboard(EditText otpET) {

        otpET.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otpET, InputMethodManager.SHOW_IMPLICIT);
    }

    private void startCountDownTimer() {

        resendEnabled = false;
        resendBtnFW.setTextColor(Color.parseColor("#99000000"));

        new CountDownTimer(resendTime * 1000, 1000) {

            @Override
            public void onTick(long l) {
                resendBtnFW.setText("Resend Code(" + l / 1000 + ")");
            }

            @Override
            public void onFinish() {

                resendEnabled = true;
                resendBtnFW.setText("Resend Code");
                resendBtnFW.setTextColor(getResources().getColor(R.color.primary));

            }
        }.start();
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (editable.length() > 0) {
                if (selectedETPosition == 0) {

                    selectedETPosition = 1;
                    showKeyboard(otp2);

                } else if (selectedETPosition == 1) {

                    selectedETPosition = 2;
                    showKeyboard(otp3);

                } else if (selectedETPosition == 2) {

                    selectedETPosition = 3;
                    showKeyboard(otp4);
                } else if (selectedETPosition == 3) {

                    selectedETPosition = 4;
                    showKeyboard(otp5);

                } else if (selectedETPosition == 4) {

                    selectedETPosition = 5;
                    showKeyboard(otp6);

                }
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_DEL) {

            if (selectedETPosition == 5) {

                selectedETPosition = 4;
                showKeyboard(otp5);

            } else if (selectedETPosition == 4) {

                selectedETPosition = 3;
                showKeyboard(otp4);

            } else if (selectedETPosition == 3) {

                selectedETPosition = 2;
                showKeyboard(otp3);

            } else if (selectedETPosition == 2) {

                selectedETPosition = 1;
                showKeyboard(otp2);

            } else if (selectedETPosition == 1) {

                selectedETPosition = 0;
                showKeyboard(otp1);

            }
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }
}