package com.example.myomoshiroi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myomoshiroi.other.SoundPoolManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button enterBtn;
    private EditText txtemail, txtpassword;
    TextView text_view_signup, forgot_password;
    LinearLayout linearProgress;
    private DatabaseReference databaseReference, uidRef;
    FirebaseAuth mAuth;
    ImageView loading;

    SharedPreferences prefs;
    String loginemail, loginpassword, music, sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);
        //deviceID = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        //  get all view id from XML
        txtemail = findViewById(R.id.editTextEmail);
        txtpassword = findViewById(R.id.editTextPassword);
        forgot_password = findViewById(R.id.text_view_forget_password);
        linearProgress = findViewById(R.id.linearLoadingProgress);
        text_view_signup = findViewById(R.id.text_view_signup);
        enterBtn = findViewById(R.id.btn_continue);
        loading = findViewById(R.id.loading);

        //  saving settings
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //  Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        //  handle login button
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLogIn();
            }
        });
        // handle enter password edit text
        txtpassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    validateLogIn();
                    return true;
                }
                return false;
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
                Intent switchActivityIntent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(switchActivityIntent);
            }
        });

    }

    private void validateLogIn(){
        if (!validateEmail() | !validatePassword()) {
            return;
        }
        //    progressbar VISIBLE
        linearProgress.setVisibility(View.VISIBLE);
        Glide.with(MainActivity.this)
                .load(R.raw.splash_screen_loading)
                .into(loading);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uidRef = databaseReference.child("UserData");
        Query query = uidRef.orderByChild("EmailId").equalTo(loginemail);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mAuth.signInWithEmailAndPassword(loginemail, loginpassword).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Dismiss keyboard on Click
                                        InputMethodManager inputManager = (InputMethodManager)
                                                getSystemService(Context.INPUT_METHOD_SERVICE);
                                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                                InputMethodManager.HIDE_NOT_ALWAYS);

                                        //    progressbar GONE
                                        linearProgress.setVisibility(View.GONE);
                                        Glide.with(MainActivity.this).clear(loading);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putString("avatarName", "profile");
                                        editor.putString("music setting", "on");
                                        editor.putString("sound setting", "on");
                                        editor.putString("walkthrough", "on");
                                        editor.apply();
                                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        //    progressbar GONE
                                        linearProgress.setVisibility(View.GONE);
                                        Glide.with(MainActivity.this).clear(loading);
                                        Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    linearProgress.setVisibility(View.GONE);
                    Glide.with(MainActivity.this).clear(loading);
                    Toast.makeText(MainActivity.this, "Wrong Email Address", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        query.addListenerForSingleValueEvent(eventListener);
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
}