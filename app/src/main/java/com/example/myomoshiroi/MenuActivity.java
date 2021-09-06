package com.example.myomoshiroi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myomoshiroi.other.BackgroundSoundService;
import com.example.myomoshiroi.other.SoundPoolManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Objects;

public class MenuActivity extends AppCompatActivity {

    TextView txtlevel,txtocoin,txtpoint;
    Button arcade,mission,token,ranking,history, buttonNext, buttonSkip, buttonWalk;
    ImageView profile, setting;
    LinearLayout linearBackground, linearLayout;

    private Toast exitToast;

    DatabaseReference databaseReference,uidRef, uidRefItem, uidRefMission;

    Intent intent;
    SharedPreferences prefs;
    String music, soundEffect, walk, avatarName;

    int[] back_images;
    int image_length;

    Calendar calendar;
    int year, month, day;
    boolean currentday = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
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

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        avatarName = prefs.getString("avatarName",null);

        MusicSoundSettings();
        FirebaseAuth();
        GetAvatarName();
        leveling();

        profile.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
            startActivity(intent);
            SoundPoolManager.playSound(1);
        });
        setting.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
            startActivity(intent);
            SoundPoolManager.playSound(1);
        });
        arcade.setOnClickListener(v -> {
            PlayArcadePopUp popUpClass = new PlayArcadePopUp();
            popUpClass.showPopupWindow(v);
            SoundPoolManager.playSound(1);
        });
        mission.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            Intent intent = new Intent(MenuActivity.this, MissionActivity.class);
            startActivity(intent);
        });
        token.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ShopActivity.class);
            startActivity(intent);
            SoundPoolManager.playSound(1);
        });
        ranking.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, RankingActivity.class);
            startActivity(intent);
            SoundPoolManager.playSound(1);
        });
        history.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, HistoryActivity.class);
            startActivity(intent);
            SoundPoolManager.playSound(1);
        });
        buttonWalk.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
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
            }
            linearBackground.setBackground(ContextCompat.getDrawable(MenuActivity.this, back_images[image_length]));
        });
        buttonSkip.setOnClickListener(v -> {
            SoundPoolManager.playSound(0);
            linearBackground.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("walkthrough","off");
            editor.apply();
        });
    }
    private void MusicSoundSettings(){
        //condition of background music and sound effect
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
            AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
            alert.setTitle("Welcome to OMOSHIROI!")
                    .setMessage("How about a quick game instructions before we go any further?\n" +
                            "Read carefully our instructions :)")
                    .setIcon(android.R.drawable.ic_menu_info_details)
                    .setCancelable(false)
                    .setPositiveButton("Bring it on!", (dialog, which) -> {
                        linearLayout.setVisibility(View.GONE);
                        linearBackground.setVisibility(View.VISIBLE);
                        leveling();
                    });
            AlertDialog dialog = alert.create();
            dialog.show();
        } else {
            DailyReward();
        }
    }

    private void FirebaseAuth(){
        //        Get Firebase auth instance
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uidRef = databaseReference.child("UserData").child(uid);
        uidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = Objects.requireNonNull(snapshot.child("Fullname").getValue()).toString();
                String level = Objects.requireNonNull(snapshot.child("Level").getValue()).toString();
                String coin = Objects.requireNonNull(snapshot.child("Ocoin").getValue()).toString();
                String totalcoin = Objects.requireNonNull(snapshot.child("TotalOcoins").getValue()).toString();
                String points = Objects.requireNonNull(snapshot.child("Point").getValue()).toString();
                String avatarName = Objects.requireNonNull(snapshot.child("Avatar").getValue()).toString();

                txtlevel.setText("Level: " + level);
                txtocoin.setText(coin);
                txtpoint.setText("XP: " + points);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("name",name);
                editor.putString("level",level);
                editor.putInt("ocoins",Integer.parseInt(coin));
                editor.putInt("totalOcoins",Integer.parseInt(totalcoin));
                editor.putInt("points",Integer.parseInt(points));
                editor.putString("avatarName",avatarName);
                editor.apply();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // [START_EXCLUDE]
                System.out.println("The read failed: " + error.getMessage());
            }
        });

        uidRefMission = databaseReference.child("UserMission").child(uid);
        uidRefMission.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String easyCount = Objects.requireNonNull(snapshot.child("EasyCount").getValue()).toString();

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("taskEasyCount",Integer.parseInt(easyCount));
                editor.apply();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // [START_EXCLUDE]
                System.out.println("The read failed: " + error.getMessage());
            }
        });

        uidRefItem = databaseReference.child("UserItem").child(uid);
        uidRefItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean Andry = (boolean) Objects.requireNonNull(snapshot.child("Andry").getValue());
                boolean Bulby = (boolean) Objects.requireNonNull(snapshot.child("Bulby").getValue());
                boolean Cackytus = (boolean) Objects.requireNonNull(snapshot.child("Cackytus").getValue());
                boolean Chicky = (boolean) Objects.requireNonNull(snapshot.child("Chicky").getValue());
                boolean Clibot = (boolean) Objects.requireNonNull(snapshot.child("Clibot").getValue());
                boolean Cupies = (boolean) Objects.requireNonNull(snapshot.child("Cupies").getValue());
                boolean Hatty = (boolean) Objects.requireNonNull(snapshot.child("Hatty").getValue());
                boolean Headyglass = (boolean) Objects.requireNonNull(snapshot.child("Headyglass").getValue());
                boolean Jobot = (boolean) Objects.requireNonNull(snapshot.child("Jobot").getValue());
                boolean Monisad = (boolean) Objects.requireNonNull(snapshot.child("Monisad").getValue());
                boolean Pineglass = (boolean) Objects.requireNonNull(snapshot.child("Pineglass").getValue());
                boolean Skullyhead = (boolean) Objects.requireNonNull(snapshot.child("Skullyhead").getValue());
                String timerAdd = Objects.requireNonNull(snapshot.child("TimerAddOnemin").getValue()).toString();
                String timerStop = Objects.requireNonNull(snapshot.child("TimerStop").getValue()).toString();
                String timerFreeze = Objects.requireNonNull(snapshot.child("TimerFreeze").getValue()).toString();

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("Andry", Andry);
                editor.putBoolean("Bulby", Bulby);
                editor.putBoolean("Cackytus", Cackytus);
                editor.putBoolean("Chicky", Chicky);
                editor.putBoolean("Clibot", Clibot);
                editor.putBoolean("Cupies", Cupies);
                editor.putBoolean("Hatty", Hatty);
                editor.putBoolean("Headyglass", Headyglass);
                editor.putBoolean("Jobot", Jobot);
                editor.putBoolean("Monisad", Monisad);
                editor.putBoolean("Pineglass", Pineglass);
                editor.putBoolean("Skullyhead", Skullyhead);
                editor.putInt("timerAddOneMin",Integer.parseInt(timerAdd));
                editor.putInt("timerStop",Integer.parseInt(timerStop));
                editor.putInt("timerFreeze",Integer.parseInt(timerFreeze));
                editor.apply();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // [START_EXCLUDE]
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }
    private void GetAvatarName(){
        switch (avatarName) {
            case "avatar_bulb": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_bulb);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                profile.setImageBitmap(bitmap);
                break;
            }
            case "avatar_cactus": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_cactus);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                profile.setImageBitmap(bitmap);
                break;
            }
            case "avatar_chick": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_chick);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                profile.setImageBitmap(bitmap);
                break;
            }
            case "avatar_cup": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_cup);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                profile.setImageBitmap(bitmap);
                break;
            }
            case "avatar_hat": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_hat);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                profile.setImageBitmap(bitmap);
                break;
            }
            case "avatar_pinaple": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_pinaple);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                profile.setImageBitmap(bitmap);
                break;
            }
            case "avatar_android": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_android);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                profile.setImageBitmap(bitmap);
                break;
            }
            case "avatar_cliprobot": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_cliprobot);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                profile.setImageBitmap(bitmap);
                break;
            }
            case "avatar_head": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_head);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                profile.setImageBitmap(bitmap);
                break;
            }
            case "avatar_monitor": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_monitor);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                profile.setImageBitmap(bitmap);
                break;
            }
            case "avatar_robot": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_robot);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                profile.setImageBitmap(bitmap);
                break;
            }
            case "avatar_skull": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_skull);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                profile.setImageBitmap(bitmap);
                break;
            }
            default: {
                InputStream imageStream = getResources().openRawResource(R.raw.profile);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                profile.setImageBitmap(bitmap);
                break;
            }
        }
    }
    private void leveling() {
        int getpoints = prefs.getInt("points", 0);
        if (getpoints <= 1999) {
            uidRef.child("Level").setValue(1);
        } else if (getpoints < 3999) {
            uidRef.child("Level").setValue(2);
        } else if (getpoints < 5999) {
            uidRef.child("Level").setValue(3);
        } else if (getpoints < 7999) {
            uidRef.child("Level").setValue(4);
        } else if (getpoints < 9999) {
            uidRef.child("Level").setValue(5);
        } else if (getpoints < 11999) {
            uidRef.child("Level").setValue(6);
        } else if (getpoints < 13999) {
            uidRef.child("Level").setValue(7);
        } else if (getpoints < 15999) {
            uidRef.child("Level").setValue(8);
        } else if (getpoints < 17999) {
            uidRef.child("Level").setValue(9);
        } else if (getpoints < 19999) {
            uidRef.child("Level").setValue(10);
        } else if (getpoints < 21999) {
            uidRef.child("Level").setValue(11);
        } else if (getpoints < 23999) {
            uidRef.child("Level").setValue(12);
        } else if (getpoints < 25999) {
            uidRef.child("Level").setValue(13);
        } else if (getpoints < 27999) {
            uidRef.child("Level").setValue(14);
        } else if (getpoints >= 29999) {
            uidRef.child("Level").setValue(15);
        }
    }
    private void DailyReward(){
        String todayString = year+ "" + month + "" + day + "";
        currentday = prefs.getBoolean(todayString,false);
        //Daily reward
        if (!currentday && isZoneAutomatic(this) && isTimeAutomatic(this)) { //currentday =false
            Toast.makeText(MenuActivity.this, "To collect your daily prize, look through your profile or mission activity.", Toast.LENGTH_LONG).show();
        }
    }
    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    public static boolean isZoneAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
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