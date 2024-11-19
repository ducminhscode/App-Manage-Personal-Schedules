package com.example.applicationproject.Controller;

import android.content.Context;
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


public class OTPVerification extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private TextView resendBtn;

    private boolean resendEnabled = false;
    private int resendTime = 90;

    private int selectedETPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpverification);
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

        resendBtn = findViewById(R.id.resendBtn);

        final Button verifyBtn = findViewById(R.id.verifyBtn);
        final TextView otpEmail = findViewById(R.id.otpEmail);
        final TextView otpMobile = findViewById(R.id.otpMobile);

        final String getEmail = getIntent().getStringExtra("email");
        final String getMobile = getIntent().getStringExtra("mobile");

        final ImageView backToRegister = findViewById(R.id.backToRegister);

        otpEmail.setText(getEmail);
        otpMobile.setText(getMobile);


        otp1.addTextChangedListener(textWatcher);
        otp2.addTextChangedListener(textWatcher);
        otp3.addTextChangedListener(textWatcher);
        otp4.addTextChangedListener(textWatcher);
        otp5.addTextChangedListener(textWatcher);
        otp6.addTextChangedListener(textWatcher);

        final String generateOtp = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString() + otp5.getText().toString() + otp6.getText().toString();

        showKeyboard(otp1);

        startCountDownTimer();

        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resendEnabled) {

                    startCountDownTimer();

                }
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (generateOtp.length() == 6) {
                    //handle your otp verification here
                }
            }
        });
        backToRegister.setOnClickListener(new View.OnClickListener() {
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
        resendBtn.setTextColor(Color.parseColor("#99000000"));

        new CountDownTimer(resendTime * 1000, 1000) {

            @Override
            public void onTick(long l) {
                resendBtn.setText("Resend Code(" + l / 1000 + ")");
            }

            @Override
            public void onFinish() {

                resendEnabled = true;
                resendBtn.setText("Resend Code");
                resendBtn.setTextColor(getResources().getColor(R.color.primary));

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