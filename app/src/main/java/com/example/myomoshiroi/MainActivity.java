package com.example.myomoshiroi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings;
import android.provider.Settings.System;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    //String deviceID;

    private Button enterBtn;
    private EditText txtemail, txtpassword;
    private TextView text_view_signup, forgot_password;
    com.google.android.material.progressindicator.CircularProgressIndicator login_progress;
    FirebaseAuth mAuth;

    String loginemail, loginpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);

        //deviceID = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

        //        get all view id from XML
        txtemail = findViewById(R.id.editTextEmail);
        txtpassword = findViewById(R.id.editTextPassword);
        forgot_password = findViewById(R.id.text_view_forget_password);
        login_progress = findViewById(R.id.register_Progress);
        text_view_signup = findViewById(R.id.text_view_signup);
        enterBtn = findViewById(R.id.btn_continue);
        //        Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        //        handle login button
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail() | !validatePassword()) {
                    return;
                }
                //    progressbar VISIBLE
                login_progress.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(loginemail, loginpassword).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //    progressbar GONE
                                login_progress.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    //    progressbar GONE
                                    login_progress.setVisibility(View.GONE);
                                    Toast.makeText(MainActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        //        handle forgot button
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        //        handle signUp textview
        text_view_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTest();
                finish();
            }
        });
    }
    private void switchTest() {
        Intent switchActivityIntent = new Intent(this, SignupActivity.class);
        startActivity(switchActivityIntent);
    }
    private boolean validateEmail() {
        loginemail = txtemail.getText().toString().trim();
        if (TextUtils.isEmpty(loginemail)) {
            Toast.makeText(MainActivity.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(loginemail).matches()) {
            Toast.makeText(MainActivity.this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword() {
        loginpassword = txtpassword.getText().toString().trim();

        if (TextUtils.isEmpty(loginpassword)) {
            Toast.makeText(MainActivity.this, "Enter Your Password", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(i);
            finish();
        }
    }
}