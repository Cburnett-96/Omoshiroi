package com.example.myomoshiroi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private EditText edit_txt_Fullname, edit_txt_LRN, edit_txt_Email, edit_txt_Pass, edit_txt_CoPass;
    private RadioButton radioMale, radioFemale;
    private Button button_register;
    private TextView text_view_login;
    LinearLayout linearProgress;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    SharedPreferences prefs;

    String fullname, lrn, email, password, co_password;
    int level, point, coin;
    String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_signup);
        //        get all view id from XML
        linearProgress= findViewById(R.id.linearLoadingProgress);
        edit_txt_Fullname = findViewById(R.id.edit_txt_Fullname);
        edit_txt_LRN = findViewById(R.id.edit_txt_LRN);
        edit_txt_Email = findViewById(R.id.edit_txt_Email);
        edit_txt_Pass = findViewById(R.id.edit_txt_Pass);
        edit_txt_CoPass = findViewById(R.id.edit_txt_CoPass);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        text_view_login = findViewById(R.id.text_view_login);
        button_register = findViewById(R.id.button_register);
        //starting level, point and coin value
        level = 1;
        point = 0;
        coin = 0;

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //        Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserData");
        //        Login Firebase auth instance
        text_view_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //        handle user SignUp button
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateFullname() | !validateLRN() | !validateEmail() | !validatePassword() | checkUserGender()) {
                    return;
                }

                if (password.equals(co_password)) {

                    //    progressbar VISIBLE
                    linearProgress.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener
                            (new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        UserData data = new UserData(fullname, lrn, email, gender, level, point, coin);

                                        FirebaseDatabase.getInstance().getReference("UserData")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(data).
                                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        SharedPreferences.Editor editor = prefs.edit();
                                                        editor.putString("music setting","on");
                                                        editor.putString("sound setting","on");
                                                        editor.apply();

                                                        //    progressbar GONE
                                                        linearProgress.setVisibility(View.GONE);
                                                        Toast.makeText(SignupActivity.this, "Successful Registered", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                    } else {
                                        //    progressbar GONE
                                        linearProgress.setVisibility(View.GONE);
                                        Toast.makeText(SignupActivity.this, "Connection Error or Email Already Used", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SignupActivity.this, "Password didn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean validateFullname() {
        fullname = edit_txt_Fullname.getText().toString().trim();
        if (TextUtils.isEmpty(fullname)) {
            Toast.makeText(SignupActivity.this, "Enter Your Full Name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateLRN() {
        lrn = edit_txt_LRN.getText().toString().trim();
        if (TextUtils.isEmpty(lrn)) {
            Toast.makeText(SignupActivity.this, "Enter Your LRN", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEmail() {
        email = edit_txt_Email.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(SignupActivity.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignupActivity.this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword() {
        password = edit_txt_Pass.getText().toString().trim();
        co_password = edit_txt_CoPass.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(SignupActivity.this, "Enter Your Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(co_password)) {
            Toast.makeText(SignupActivity.this, "Enter Your Co-Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() <= 6) {
            Toast.makeText(SignupActivity.this, "Password is Very Short", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkUserGender() {

        if (radioMale.isChecked()) {
            gender = "Male";
            return false;

        }
        if (radioFemale.isChecked()) {
            gender = "Female";
            return false;

        } else {
            Toast.makeText(SignupActivity.this, "Select Your Gender", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}