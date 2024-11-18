package com.example.applicationproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applicationproject.Database.CreateDatabase;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class Login extends AppCompatActivity {

    private boolean passwordShowing = false;
    private boolean remember = true;

    private CreateDatabase db;

    protected GoogleSignInClient mGoogleSignInClient;
    protected static final int RC_SIGN_IN = 9001;

    private TextView txtWrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText userNameET = findViewById(R.id.userNameET);
        final EditText passwordET = findViewById(R.id.passwordET);
        final ImageView passwordShowIcon = findViewById(R.id.passwordShowIcon);
        final TextView signUpBtnLogin = findViewById(R.id.signUpBtnLogin);
        final RelativeLayout signInWithGoogle = findViewById(R.id.signInWithGoogle);
        final TextView forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);
        final AppCompatButton signInBtnLogin = findViewById(R.id.signInBtnLogin);
        final CheckBox checkRememberBtn = findViewById(R.id.checkRememberBtn);

        txtWrong = findViewById(R.id.txtWrong);

        db = new CreateDatabase(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        String un = pref.getString("USERNAME", "");
        String ps = pref.getString("PASSWORD", "");
        userNameET.setText(un);
        passwordET.setText(ps);

        checkRememberBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                remember = true;
            } else {
                remember = false;
            }
        });

        passwordShowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordShowing) {
                    passwordShowing = false;
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordShowIcon.setImageResource(R.drawable.hide);
                } else {
                    passwordShowing = true;
                    passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordShowIcon.setImageResource(R.drawable.show);
                }
                passwordET.setSelection(passwordET.length());
            }
        });

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgotPasswordCheck.class));
            }
        });

        signInBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txtUserNameET = userNameET.getText().toString();
                String txtPasswordET = passwordET.getText().toString();

                boolean isValidUser = db.checkUser(txtUserNameET, txtPasswordET);

                if (txtUserNameET.isEmpty()) {
                    userNameET.setError("Please enter your username");
                    userNameET.requestFocus();
                } else if (txtPasswordET.isEmpty()) {
                    passwordET.setError("Please enter your password");
                    passwordET.requestFocus();
                } else if (!isValidUser) {
                    userNameET.setText(null);
                    passwordET.setText(null);
                    userNameET.clearFocus();
                    passwordET.clearFocus();
                    txtWrong.setText("Invalid username or password");
                    txtWrong.setVisibility(View.VISIBLE);
                } else {
                    txtWrong.setVisibility(View.GONE);
                    rememberUser(txtUserNameET, txtPasswordET, remember);
                    startActivity(new Intent(Login.this, MainActivity.class));
                }
            }
        });

        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signInWithGG();
            }
        });

        signUpBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }

    private void rememberUser(String u, String p, boolean status) {
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (!status) {
            editor.clear();
        } else {
            editor.putString("USERNAME", u);
            editor.putString("PASSWORD", p);
            editor.putBoolean("REMEMBER", status);
        }
        editor.commit();
    }

    private void signInWithGG() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);

                String personName = account.getDisplayName();
                String personEmail = account.getEmail();

                if (!db.checkEmailExists(personEmail) || !db.checkUsernameExists(personName)) {

                    CreateDatabase credb = new CreateDatabase(Login.this);
                    credb.addUser(personName, personEmail, null, null);

                }
                Toast.makeText(Login.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, MainActivity.class));

            } catch (ApiException e) {

                Toast.makeText(Login.this, "Sign in failed", Toast.LENGTH_SHORT).show();

            }
        }
    }
}