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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button enterBtn;
    private EditText txtemail, txtpassword;
    private TextView text_view_signup, forgot_password;
    LinearLayout linearProgress;
    FirebaseAuth mAuth;

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
        FirstBootCheckInternetConnection();
        //deviceID = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        //  get all view id from XML
        txtemail = findViewById(R.id.editTextEmail);
        txtpassword = findViewById(R.id.editTextPassword);
        forgot_password = findViewById(R.id.text_view_forget_password);
        linearProgress = findViewById(R.id.linearLoadingProgress);
        text_view_signup = findViewById(R.id.text_view_signup);
        enterBtn = findViewById(R.id.btn_continue);

        //  saving settings
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //  Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        //  handle login button
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail() | !validatePassword()) {
                    return;
                }
                //    progressbar VISIBLE
                linearProgress.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(loginemail, loginpassword).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //    progressbar GONE
                                linearProgress.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("music setting","on");
                                    editor.putString("sound setting","on");
                                    editor.putString("walkthrough","on");
                                    editor.apply();
                                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    //    progressbar GONE
                                    linearProgress.setVisibility(View.GONE);
                                    Toast.makeText(MainActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
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
                Intent switchActivityIntent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(switchActivityIntent);
            }
        });

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
            SharedPreferences.Editor editor = prefs.edit();
            music = prefs.getString("music setting",null);
            sound = prefs.getString("sound setting",null);
            //walk = prefs.getString("walkthrough",null);
            if (music.equals("off")){
                editor.putString("music setting","off");
            }else{
                editor.putString("music setting","on");
            }
            if (sound.equals("off")){
                editor.putString("sound setting","off");
            }else{
                editor.putString("sound setting","on");
            }
            editor.apply();
            CheckInternetConnection();
        }
    }
    private void FirstBootCheckInternetConnection(){
        if(!isNetworkAvailable())
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("OMOSHIROI Need Internet Connection Access")
                    .setMessage("Please Check Your Internet Connection")
                    .setCancelable(false)
                    .setPositiveButton("Try again!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirstBootCheckInternetConnection();
                        }
                    })
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SoundPoolManager.cleanUp();
                            finishAffinity();
                            System.exit(0);
                        }
                    })
                    .show();
        }
    }
    private void CheckInternetConnection(){
        if(!isNetworkAvailable())
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("OMOSHIROI Need Internet Connection Access")
                    .setMessage("Please Check Your Internet Connection")
                    .setCancelable(false)
                    .setPositiveButton("Try again!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CheckInternetConnection();
                        }
                    })
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SoundPoolManager.cleanUp();
                            finishAffinity();
                            System.exit(0);
                        }
                    })
                    .show();
        }else{
            Intent i = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(i);
            finish();
        }

    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

                        return true;
                    } else return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                }
            }
        }
        return false;
    }
}