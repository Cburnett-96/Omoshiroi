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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText edit_txt_resetEmail;
    Button button_resetPassword;
    private ProgressBar resetPassword_progress;
    FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference, uidRef;

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

                databaseReference = FirebaseDatabase.getInstance().getReference();
                uidRef = databaseReference.child("UserData");
                Query query = uidRef.orderByChild("EmailId").equalTo(ResetPassword);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            firebaseAuth.sendPasswordResetEmail(edit_txt_resetEmail.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                resetPassword_progress.setVisibility(View.GONE);
                                                Toast.makeText(ForgotPasswordActivity.this, "You will receive an email with a link to reset your password.", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                resetPassword_progress.setVisibility(View.GONE);
                                                Toast.makeText(ForgotPasswordActivity.this, "Something Wrong!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else {
                            resetPassword_progress.setVisibility(View.GONE);
                            Toast.makeText(ForgotPasswordActivity.this, "We couldn't find your email address in the Omoshiroi registered user database!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                query.addListenerForSingleValueEvent(eventListener);
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