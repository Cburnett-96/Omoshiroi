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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText edit_txt_resetEmail;
    private Button button_resetPassword;
    private ProgressBar resetPassword_progress;
    FirebaseAuth firebaseAuth;

    String ResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_forgot_password);
//        get all view id from XML
        edit_txt_resetEmail = findViewById(R.id.edit_txt_resetEmail);
        button_resetPassword = findViewById(R.id.button_resetPassword);
        resetPassword_progress = findViewById(R.id.resetPassword_progress);

        //        Get Firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();
        button_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail()) {
                    return;
                }
                resetPassword_progress.setVisibility(View.VISIBLE);

                firebaseAuth.sendPasswordResetEmail(edit_txt_resetEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                resetPassword_progress.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {

                                        Toast.makeText(ForgotPasswordActivity.this, "Password reset link sent to your Email", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(ForgotPasswordActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                                    }
                            }
                        });
            }
        });
    }
    private boolean validateEmail() {
        ResetPassword = edit_txt_resetEmail.getText().toString().trim();
        if (TextUtils.isEmpty(ResetPassword)) {
            Toast.makeText(ForgotPasswordActivity.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(ResetPassword).matches()) {
            Toast.makeText(ForgotPasswordActivity.this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}