package com.example.myomoshiroi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MenuActivity extends AppCompatActivity {

    TextView txtlevel,txtocoin,txtpoint;

    Button arcade,mission,token,ranking,history, buttonNext, buttonSkip, buttonWalk;
    ImageView profile, setting;
    LinearLayout linearBackground, linearLayout;

    private Toast exitToast;

    private DatabaseReference databaseReference,uidRef;
    private FirebaseAuth mAuth;

    Intent intent;
    SharedPreferences prefs;
    String music, soundEffect, walk;

    int[] back_images;
    int image_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_menu);

        txtlevel = findViewById(R.id.textLevel);
        txtpoint = findViewById(R.id.textPoint);
        txtocoin = findViewById(R.id.textOcoin);

        arcade = findViewById(R.id.btn_arcade);
        mission = findViewById(R.id.btn_mission);
        token = findViewById(R.id.btn_tokens);
        ranking = findViewById(R.id.btn_ranking);
        history = findViewById(R.id.btn_history);
        profile = findViewById(R.id.profile);
        setting = findViewById(R.id.setting);

        linearLayout = findViewById(R.id.linearLayout);
        linearBackground = findViewById(R.id.linearBGWalkthought);
        buttonNext = findViewById(R.id.nextWalkButton);
        buttonSkip = findViewById(R.id.skipButton);
        buttonWalk = findViewById(R.id.walkThroughButton);

        MusicSoundSettings();
        FirebaseAuth();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
                startActivity(intent);
                SoundPoolManager.playSound(1);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
                startActivity(intent);
                SoundPoolManager.playSound(1);
            }
        });
        arcade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayArcadePopUp popUpClass = new PlayArcadePopUp();
                popUpClass.showPopupWindow(v);
                SoundPoolManager.playSound(1);
            }
        });
        mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MissionActivity.class);
                startActivity(intent);
                SoundPoolManager.playSound(1);
            }
        });
        token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ShopActivity.class);
                startActivity(intent);
                SoundPoolManager.playSound(1);
            }
        });
        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RankingActivity.class);
                startActivity(intent);
                SoundPoolManager.playSound(1);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, HistoryActivity.class);
                startActivity(intent);
                SoundPoolManager.playSound(1);
            }
        });
        buttonWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearBackground.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            }
        });
        back_images = new int[]{R.raw.walk1,R.raw.walk2,R.raw.walk3,R.raw.walk4,R.raw.walk5,R.raw.walk6,R.raw.walk7,R.raw.walk8,R.raw.walk1};
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                }
                linearBackground.setBackground(ContextCompat.getDrawable(MenuActivity.this, back_images[image_length]));
            }
        });
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearBackground.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("walkthrough","off");
                editor.apply();
            }
        });
        leveling();
    }
    private void MusicSoundSettings(){
        //condition of background music and sound effect
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        music = prefs.getString("music setting",null);
        intent = new Intent(this, BackgroundSoundService.class);
        if (music.equals("on")){
            startService(intent);
        }
        else if (music.equals("off")){
            stopService(intent);
        }

        soundEffect = prefs.getString("sound setting",null);
        if (soundEffect.equals("on")){
            SoundPoolManager.instantiate(this);
        }
        walk = prefs.getString("walkthrough",null);
        if (walk.equals("on")){
            linearBackground.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }
    }

    private void FirebaseAuth(){
        //        Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uidRef = databaseReference.child("UserData").child(uid);
        uidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String level = Objects.requireNonNull(snapshot.child("Level").getValue()).toString();
                String coin = Objects.requireNonNull(snapshot.child("Ocoin").getValue()).toString();
                String points = Objects.requireNonNull(snapshot.child("Point").getValue()).toString();
                txtlevel.setText("Level: " + level);
                txtocoin.setText(coin);
                txtpoint.setText("Points: " + points);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("level",Integer.parseInt(level));
                editor.putInt("points",Integer.parseInt(points));
                editor.putInt("ocoins",Integer.parseInt(coin));
                editor.apply();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // [START_EXCLUDE]
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }

    private void leveling() {
        int getpoints = prefs.getInt("points", 0);
        if (getpoints <= 1199) {
            uidRef.child("Level").setValue(1);
        } else if (getpoints < 1360) {
            uidRef.child("Level").setValue(2);
        } else if (getpoints < 1544) {
            uidRef.child("Level").setValue(3);
        } else if (getpoints < 1772) {
            uidRef.child("Level").setValue(4);
        } else if (getpoints < 2063) {
            uidRef.child("Level").setValue(5);
        } else if (getpoints < 2444) {
            uidRef.child("Level").setValue(6);
        } else if (getpoints < 2955) {
            uidRef.child("Level").setValue(7);
        } else if (getpoints < 3659) {
            uidRef.child("Level").setValue(8);
        } else if (getpoints < 4659) {
            uidRef.child("Level").setValue(9);
        } else if (getpoints >= 4660) {
            uidRef.child("Level").setValue(10);
        }
    }

    @Override
    public void onBackPressed() {
        if (exitToast == null || exitToast.getView() == null || exitToast.getView().getWindowToken() == null) {
            exitToast = Toast.makeText(this, "Press again to exit", Toast.LENGTH_LONG);
            exitToast.show();
            SoundPoolManager.playSound(0);
        } else {
            stopService(intent);
            SoundPoolManager.cleanUp();
            finishAffinity();
            System.exit(0);
            exitToast.cancel();
            super.onBackPressed();
        }
    }
}