package com.example.myomoshiroi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.security.Provider;

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
        com.google.android.material.switchmaterial.SwitchMaterial switchmusic = findViewById(R.id.switchMusic);
        com.google.android.material.switchmaterial.SwitchMaterial switchSound = findViewById(R.id.switchSound);
        Button buttonFeedback = findViewById(R.id.btn_feedback);
        Button buttonHelpFAQ = findViewById(R.id.btn_HelpFAQ);
        Button buttonBack = findViewById(R.id.backButton);
        Button buttonExit = findViewById(R.id.exitButton);

        //saving state into shared preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        switchmusic.setChecked(prefs.getBoolean("music value",true));
        switchSound.setChecked(prefs.getBoolean("sound value",true));

        switchmusic.setButtonDrawable(prefs.getInt("music saveImageButton",R.drawable.music_on));
        switchSound.setButtonDrawable(prefs.getInt("sound saveImageButton",R.drawable.sound_on));

        switchmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // change background on click
                if (switchmusic.isChecked()) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("music value",true);
                    editor.putString("music setting","on");
                    editor.putInt("music saveImageButton",R.drawable.music_on);
                    editor.apply();
                    switchmusic.setChecked(true);
                    switchmusic.setButtonDrawable(R.drawable.music_on);
                    startService(intent);
                } else {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("music value",false);
                    editor.putString("music setting","off");
                    editor.putInt("music saveImageButton",R.drawable.music_off);
                    editor.apply();
                    switchmusic.setChecked(false);
                    switchmusic.setButtonDrawable(R.drawable.music_off);
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
                    editor.putInt("sound saveImageButton",R.drawable.sound_on);
                    editor.apply();
                    switchSound.setChecked(true);
                    switchSound.setButtonDrawable(R.drawable.sound_on);
                } else {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("sound value",false);
                    editor.putString("sound setting","off");
                    editor.putInt("sound saveImageButton",R.drawable.sound_off);
                    editor.apply();
                    SoundPoolManager.stop();
                    switchSound.setChecked(false);
                    switchSound.setButtonDrawable(R.drawable.sound_off);
                }
            }
        });

        buttonFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://albertzz.somee.com/Contact"));
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
                .setMessage("Are you sure, want to EXIT?")
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