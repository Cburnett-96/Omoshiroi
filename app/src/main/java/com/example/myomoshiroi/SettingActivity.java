package com.example.myomoshiroi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myomoshiroi.other.BackgroundSoundService;
import com.example.myomoshiroi.other.SoundPoolManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingActivity extends AppCompatActivity {

    DatabaseReference databaseReference, uidRefMission;
    Intent intent;
    SharedPreferences prefs;
    SwitchCompat switchmusic, switchSound;
    Button buttonAbout, buttonHelpFAQ, buttonBack, buttonExit, buttonWalk, buttonNext, buttonSkip, buttonClose;
    TextView version, tvContactUs;
    LinearLayout linearBackground, linearLayout, linearAbout;
    int[] back_images;
    int image_length;

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
        linearLayout = findViewById(R.id.linearSettings);
        switchmusic = findViewById(R.id.switchMusic);
        switchSound = findViewById(R.id.switchSound);
        buttonAbout = findViewById(R.id.btn_aboutUs);
        buttonHelpFAQ = findViewById(R.id.btn_HelpFAQ);
        buttonBack = findViewById(R.id.backButton);
        buttonExit = findViewById(R.id.exitButton);

        linearBackground = findViewById(R.id.linearBGWalkthought);
        buttonWalk = findViewById(R.id.walkThroughButton);
        buttonNext = findViewById(R.id.nextWalkButton);
        buttonSkip = findViewById(R.id.skipButton);

        linearAbout = findViewById(R.id.linearAbout);
        buttonClose = findViewById(R.id.btn_close);
        tvContactUs = findViewById(R.id.tv_contactUs);
        version = findViewById(R.id.tv_Version);

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

        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                linearLayout.setVisibility(View.GONE);
                linearAbout.setVisibility(View.VISIBLE);
            }
        });

        version.setText("App Version: "+BuildConfig.VERSION_NAME);
        tvContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","albert.pasco@lspu.edu.ph", null));
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                linearAbout.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

        buttonHelpFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://albertzz.somee.com/PostSearch?omoshiroi#"));
                startActivity(intent);
            }
        });

        buttonWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this);
                alert.setTitle("OMOSHIROI Walk-through!")
                        .setMessage("Need game instructions again?\n" +
                                "Read carefully our instructions :)")
                        .setIcon(android.R.drawable.ic_menu_info_details)
                        .setCancelable(false)
                        .setPositiveButton("Okay!", (dialog, which) -> {
                                    linearBackground.setVisibility(View.VISIBLE);
                                    linearLayout.setVisibility(View.GONE);
                                }
                        );
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });

        back_images = new int[]{R.raw.walk1,R.raw.walk2,R.raw.walk3,R.raw.walk4,R.raw.walk5,R.raw.walk6,R.raw.walk7,R.raw.walk8,R.raw.walk1};
        buttonNext.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            image_length++;
            image_length = image_length % back_images.length;
            if (image_length == 8){
                linearBackground.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("walkthrough","off");
                editor.apply();
            }else if (image_length == 7){
                buttonNext.setText(R.string.finish);
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference();
                uidRefMission = databaseReference.child("UserMission").child(uid);
                uidRefMission.child("Task02").setValue(1);
            }
            linearBackground.setBackground(ContextCompat.getDrawable(SettingActivity.this, back_images[image_length]));
        });
        buttonSkip.setOnClickListener(v -> {
            SoundPoolManager.playSound(0);
            linearBackground.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("walkthrough","off");
            editor.apply();
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                Intent intent = new Intent(SettingActivity.this, MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
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