package com.example.applicationproject;

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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applicationproject.Database.CreateDatabase;
import com.example.applicationproject.OTPCode.EmailOTP;

import java.util.Random;

public class ForgotPasswordOTP extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private TextView resendBtnFW;

    private boolean resendEnabled = false;
    private int resendTime = 90;
    private String isOTP, generateOtp;
    private long otpCreateOnTime;

    private int atp = 0;
    private final int MAX_ATP = 3;

    private int selectedETPosition = 0;

    private TextView txtWarning;
    private Button verifyBtnFW;


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
        verifyBtnFW = findViewById(R.id.verifyBtnFW);

        final TextView otpEmailFW = findViewById(R.id.otpEmailFW);

        final String getEmail = getIntent().getStringExtra("email");

        isOTP = createOTP(6);
        new EmailOTP(getEmail, "Your OTP Code", "Your OTP is: " + isOTP + ". This OTP will expire in 5 minutes.").execute();

        final ImageView backToForgotPasswordCheck = findViewById(R.id.backToForgotPasswordCheck);

        txtWarning = findViewById(R.id.txtWarning);

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

                    isOTP = createOTP(6);
                    new EmailOTP(getEmail, "Your OTP Code", "Your OTP is: " + isOTP + ". This OTP will expire in 5 minutes.").execute();
                    txtWarning.setVisibility(View.GONE);
                    verifyBtnFW.setEnabled(true);
                    atp = 0;
                    startCountDownTimer();

                }
            }
        });

        verifyBtnFW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateOtp = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString() + otp5.getText().toString() + otp6.getText().toString();

                if (System.currentTimeMillis() - otpCreateOnTime > 300000) {
                    Toast.makeText(ForgotPasswordOTP.this, "OTP expired", Toast.LENGTH_SHORT).show();
                    verifyBtnFW.setEnabled(false);
                } else {
                    if (generateOtp.length() == 6) {
                        if (generateOtp.equals(isOTP)) {

                            isOTP = null;
                            atp = 0;
                            Intent intent = new Intent(ForgotPasswordOTP.this, ForgotPasswordChange.class);
                            intent.putExtra("email", getEmail);
                            startActivity(intent);

                        } else {
                            enterWrongOTPCode();
                        }
                    } else {
                        enterWrongOTPCode();
                    }
                }
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

            if(editable.length() > 0){
                if(selectedETPosition==0){

                    selectedETPosition=1;
                    showKeyboard(otp2);

                }
                else if(selectedETPosition==1){

                    selectedETPosition=2;
                    showKeyboard(otp3);

                }
                else if(selectedETPosition==2){

                    selectedETPosition=3;
                    showKeyboard(otp4);
                }
                else if(selectedETPosition==3){

                    selectedETPosition=4;
                    showKeyboard(otp5);

                }
                else if(selectedETPosition==4){

                    selectedETPosition=5;
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
    protected String createOTP(int length) {

        String nums = "0123456789";
        StringBuilder otp = new StringBuilder();

        Random rand = new Random();

        for (int i = 0; i < length; i++) {
            otp.append(nums.charAt(rand.nextInt(nums.length())));
        }
        otpCreateOnTime = System.currentTimeMillis();
        return otp.toString();
    }

    protected void enterWrongOTPCode() {
        atp++;
        txtWarning.setText(String.format("OTP not match. You have %d attempts left", MAX_ATP-atp));
        txtWarning.setVisibility(View.VISIBLE);
        if (atp >= MAX_ATP) {
            verifyBtnFW.setEnabled(false);
        }
    }
}