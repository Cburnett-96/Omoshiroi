package com.example.myomoshiroi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myomoshiroi.other.SoundPoolManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {
    ImageView banner, loading;
    TextView version;

    FirebaseAuth mAuth;

    SharedPreferences prefs;
    String music, sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_splash_screen);

        //  Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        banner = findViewById(R.id.banner);
        loading = findViewById(R.id.loading);
        version = findViewById(R.id.tv_Version);

        version.setText(BuildConfig.VERSION_NAME);
        FirstBootCheckInternetConnection();
    }

    private void FirstBootCheckInternetConnection() {

        Glide.with(this)
                .load(R.raw.splash_screen_loading)
                .into(loading);

        if (!isNetworkAvailable()) {
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
        } else if (mAuth.getCurrentUser() != null) {
            SharedPreferences.Editor editor = prefs.edit();
            music = prefs.getString("music setting", null);
            sound = prefs.getString("sound setting", null);
            //walk = prefs.getString("walkthrough",null);
            if (music.equals("off")) {
                editor.putString("music setting", "off");
            } else {
                editor.putString("music setting", "on");
            }
            if (sound.equals("off")) {
                editor.putString("sound setting", "off");
            } else {
                editor.putString("sound setting", "on");
            }
            editor.apply();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    overridePendingTransition(0, 0);
                    Intent i = new Intent(SplashScreenActivity.this, MenuActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                }
            }, 3000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    overridePendingTransition(0, 0);
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                }
            }, 5000);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
