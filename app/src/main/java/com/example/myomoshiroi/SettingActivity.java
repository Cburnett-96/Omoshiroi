package com.example.myomoshiroi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.myomoshiroi.other.BackgroundSoundService;
import com.example.myomoshiroi.other.SoundPoolManager;

public class SettingActivity extends AppCompatActivity {

    Intent intent;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_setting);
        intent = new Intent(this, BackgroundSoundService.class);

        //Initialize the elements of our window, install the handler
        SwitchCompat switchmusic = findViewById(R.id.switchMusic);
        SwitchCompat switchSound = findViewById(R.id.switchSound);
        Button buttonFeedback = findViewById(R.id.btn_feedback);
        Button buttonHelpFAQ = findViewById(R.id.btn_HelpFAQ);
        Button buttonBack = findViewById(R.id.backButton);
        Button buttonExit = findViewById(R.id.exitButton);

        //saving state into shared preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        switchmusic.setChecked(prefs.getBoolean("music value",true));
        switchSound.setChecked(prefs.getBoolean("sound value",true));

        switchmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // change background on click
                if (switchmusic.isChecked()) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("music value",true);
                    editor.putString("music setting","on");
                    editor.apply();
                    switchmusic.setChecked(true);
                    startService(intent);
                } else {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("music value",false);
                    editor.putString("music setting","off");
                    editor.apply();
                    switchmusic.setChecked(false);
                    stopService(intent);
                }
            }
        });

        switchSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // change background on click
                if (switchSound.isChecked()) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("sound value",true);
                    editor.putString("sound setting","on");
                    editor.apply();
                    switchSound.setChecked(true);
                } else {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("sound value",false);
                    editor.putString("sound setting","off");
                    editor.apply();
                    SoundPoolManager.stop();
                    switchSound.setChecked(false);
                }
            }
        });

        buttonFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://albertzz.somee.com/PostSearch?omoshiroi#"));
                startActivity(intent);
            }
        });

        buttonHelpFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://albertzz.somee.com/Contact"));
                startActivity(intent);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                Intent intent = new Intent(SettingActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                AlertDialog.Builder alertExit = new AlertDialog.Builder(SettingActivity.this);
                alertExit.setTitle("Exit Game")
                .setMessage("Are you sure, want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Exit the Application
                        SoundPoolManager.cleanUp();
                        finishAffinity();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = alertExit.create();
                dialog.show();
            }
        });
    }
}