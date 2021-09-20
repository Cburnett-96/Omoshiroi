package com.example.myomoshiroi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myomoshiroi.model.Attempt;
import com.example.myomoshiroi.model.UserHistory;
import com.example.myomoshiroi.other.EasyModeTenses;
import com.example.myomoshiroi.other.SoundPoolManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.Calendar;

public class MissionActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonBack, dailyRewardButton, btnDailyTask01;
    LinearLayout layoutDaily, layoutDailyTask, layoutLevel, linearDailyTask01, linearLevelTask01, linearLevelTask02, linearLevelTask03, linearLevelTask04;
    TextView selectTask, itemDaily, itemLevel,
            task01Count, levelTask01Count;
    Button btnLevelTask01,btnLevelTask02,btnLevelTask03, btnLevelTask04;
    ColorStateList def;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,uidRef, uidRefMission;
    String title, descriptions, createdTime, currentDate;
    int attempt1, attempt2, attempt3, attempt4;
    SharedPreferences prefs;
    int gettask01, getocoins, getTotalocoins, getTotalDailyCountEasy, gettask02, gettask04,
            gettask03;
    int dailyRewardTask01, levelRewardTask01, levelRewardTask02, levelRewardTask03, levelRewardTask04;
    boolean DailyTask,LevelTask01,LevelTask02,LevelTask03,LevelTask04;
    ImageView nothingBackgroundImage, nothingBackgroundImageLevel;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_mission);

        buttonBack = findViewById(R.id.backButton);
        dailyRewardButton = findViewById(R.id.dailyRewardButton);
        layoutDaily = findViewById(R.id.layout_daily);
        layoutDailyTask = findViewById(R.id.layout_dailyTask);
        layoutLevel = findViewById(R.id.layout_level);
        nothingBackgroundImage = findViewById(R.id.image_backgroundNothing);
        nothingBackgroundImageLevel  = findViewById(R.id.image_backgroundNothingLevel);
        linearDailyTask01 = findViewById(R.id.layout_task01);
        linearLevelTask01 = findViewById(R.id.layout_level_task01);
        linearLevelTask02 = findViewById(R.id.layout_level_task02);
        linearLevelTask03 = findViewById(R.id.layout_level_task03);
        linearLevelTask04 = findViewById(R.id.layout_level_task04);
        selectTask = findViewById(R.id.selectTask);
        itemDaily = findViewById(R.id.itemDaily);
        itemLevel = findViewById(R.id.itemLevel);
        task01Count = findViewById(R.id.task01_count);
        levelTask01Count = findViewById(R.id.level_task01_count);
        btnDailyTask01 = findViewById(R.id.btn_task01);
        btnLevelTask01 = findViewById(R.id.btn_level_task01);
        btnLevelTask02 = findViewById(R.id.btn_level_task02);
        btnLevelTask03 = findViewById(R.id.btn_level_task03);
        btnLevelTask04 = findViewById(R.id.btn_level_task04);

        def = itemLevel.getTextColors();
        itemLevel.setOnClickListener(this);
        itemDaily.setOnClickListener(this);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uidRef = databaseReference.child("UserData").child(uid);
        uidRefMission = databaseReference.child("UserMission").child(uid);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        getocoins = prefs.getInt("ocoins",0);
        getTotalocoins = prefs.getInt("totalOcoins",0);
        gettask01 = prefs.getInt("task01",0);
        getTotalDailyCountEasy = prefs.getInt("taskDailyEasyCount",0);
        gettask02 = prefs.getInt("task02",0);
        gettask04 = prefs.getInt("task04",0);
        gettask03 = prefs.getInt("task03",0);
        DailyTask = prefs.getBoolean("b_dailyTask",false);
        LevelTask01 = prefs.getBoolean("b_task01",false);
        LevelTask02 = prefs.getBoolean("b_task02",false);
        LevelTask03 = prefs.getBoolean("b_task03",false);
        LevelTask04 = prefs.getBoolean("b_task04",false);

        dailyRewardTask01 = 250;
        levelRewardTask01 = 800;
        levelRewardTask02 = 50;
        levelRewardTask03 = 100;
        levelRewardTask04 = 300;

        Attempt attempt = new Attempt(
                Calendar.getInstance().getTimeInMillis(),
                attempt1,
                attempt2,
                attempt3,
                attempt4
        );

        currentDate = EasyModeTenses.formatDate(attempt.getCreatedTime());
        createdTime = currentDate;

        TaskCount();

        dailyRewardButton.setOnClickListener(v -> {
            DailyReward popupDailyReward = new DailyReward();
            popupDailyReward.showPopupWindow(v);
        });
        btnDailyTask01.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            if(getTotalDailyCountEasy >= 5){
                Toast.makeText(this, "Successfully claimed your rewards!", Toast.LENGTH_SHORT).show();
                linearDailyTask01.setVisibility(View.GONE);
                uidRef.child("Ocoin").setValue(getocoins + dailyRewardTask01);
                uidRef.child("TotalOcoins").setValue(getTotalocoins + dailyRewardTask01);
                uidRefMission.child("DailyTask").setValue(true);
                DailyTask = true;

                title = "Daily Mission";
                descriptions = "You've finished the daily task #01 and earned 250 ocoins.";
                SaveHistory();
            }else{
                EasyModeChoosen popUp = new EasyModeChoosen();
                popUp.showPopupWindow(v);
            }
        });
        btnLevelTask01.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            if(gettask01 >= 20){
                Toast.makeText(this, "Successfully claimed your rewards!", Toast.LENGTH_SHORT).show();
                linearLevelTask01.setVisibility(View.GONE);
                uidRef.child("Ocoin").setValue(getocoins + levelRewardTask01);
                uidRef.child("TotalOcoins").setValue(getTotalocoins + levelRewardTask01);
                uidRefMission.child("LevelTask01").setValue(true);
                LevelTask01 = true;

                title = "Level Mission";
                descriptions = "You've finished the level task #01 and earned 800 ocoins";
                SaveHistory();
            }else{
                EasyModeChoosen popUp = new EasyModeChoosen();
                popUp.showPopupWindow(v);
            }
        });
        btnLevelTask02.setOnClickListener(v -> {
            SoundPoolManager.playSound(0);
            if(gettask02 >= 1){
                Toast.makeText(this, "Successfully claimed your rewards!", Toast.LENGTH_SHORT).show();
                linearLevelTask02.setVisibility(View.GONE);
                uidRef.child("Ocoin").setValue(getocoins + levelRewardTask02);
                uidRef.child("TotalOcoins").setValue(getTotalocoins + levelRewardTask02);
                uidRefMission.child("LevelTask02").setValue(true);
                LevelTask02 = true;

                title = "Level Mission";
                descriptions = "You've finished the level task #02 and earned 50 ocoins";
                SaveHistory();
            }else {
                Intent intent = new Intent(MissionActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnLevelTask03.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            if(gettask03 >= 1){
                Toast.makeText(this, "Successfully claimed your rewards!", Toast.LENGTH_SHORT).show();
                linearLevelTask03.setVisibility(View.GONE);
                uidRef.child("Ocoin").setValue(getocoins + levelRewardTask03);
                uidRef.child("TotalOcoins").setValue(getTotalocoins + levelRewardTask03);
                uidRefMission.child("LevelTask03").setValue(true);
                LevelTask03 = true;

                title = "Level Mission";
                descriptions = "You've finished the level task #03 and earned 100 ocoins";
                SaveHistory();
            }else {
                Intent intent = new Intent(MissionActivity.this, ShopActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnLevelTask04.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            if(gettask04 == 10){
                Toast.makeText(this, "Successfully claimed your rewards!", Toast.LENGTH_SHORT).show();
                linearLevelTask04.setVisibility(View.GONE);
                uidRef.child("Ocoin").setValue(getocoins + levelRewardTask04);
                uidRef.child("TotalOcoins").setValue(getTotalocoins + levelRewardTask04);
                uidRefMission.child("LevelTask04").setValue(true);
                LevelTask04 = true;

                title = "Level Mission";
                descriptions = "You've finished the level task #04 and earned 300 ocoins";
                SaveHistory();
            }else {
                EasyModeChoosen popUp = new EasyModeChoosen();
                popUp.showPopupWindow(v);
            }
        });
        buttonBack.setOnClickListener(v -> {
            SoundPoolManager.playSound(0);
            Intent intent = new Intent(MissionActivity.this, MenuActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });
    }
    @Override
    public void onClick(View view) {
        //TABS
        if (view.getId() == R.id.itemDaily) {
            SoundPoolManager.playSound(1);
            selectTask.animate().x(0).setDuration(100);
            itemDaily.setTextColor(getResources().getColor(R.color.forest_green));
            itemLevel.setTextColor(def);
            layoutLevel.setVisibility(View.GONE);
            layoutDaily.setVisibility(View.VISIBLE);
            nothingBackgroundImageLevel.setVisibility(View.GONE);
        } else if (view.getId() == R.id.itemLevel) {
            SoundPoolManager.playSound(1);
            itemDaily.setTextColor(def);
            itemLevel.setTextColor(getResources().getColor(R.color.forest_green));
            int size = itemLevel.getWidth();
            selectTask.animate().x(size).setDuration(100);
            layoutDaily.setVisibility(View.GONE);
            layoutLevel.setVisibility(View.VISIBLE);
            nothingBackgroundImage.setVisibility(View.GONE);
        }
    }
    public void TaskCount(){
        task01Count.setText(getTotalDailyCountEasy+"/5");
        levelTask01Count.setText(gettask01+"/20");

        if(getTotalDailyCountEasy >= 5){
            btnDailyTask01.setText("Claim");
            task01Count.setText("Completed!");
        }
        if(DailyTask){
            linearDailyTask01.setVisibility(View.GONE);
            layoutDailyTask.setVisibility(View.GONE);
            nothingBackgroundImage.setVisibility(View.VISIBLE);
        }

        if (gettask01 >= 20){
            btnLevelTask01.setText("Claim");
            levelTask01Count.setText("Completed!");
        }
        if (LevelTask01){
            linearLevelTask01.setVisibility(View.GONE);
        }

        if (gettask02 >= 1){
            btnLevelTask02.setText("Claim");
        }
        if (LevelTask02){
            linearLevelTask02.setVisibility(View.GONE);
        }

        if (gettask03 >= 1){
            btnLevelTask03.setText("Claim");
        }
        if (LevelTask03){
            linearLevelTask03.setVisibility(View.GONE);
        }

        if (gettask04 == 10){
            btnLevelTask04.setText("Claim");
        }
        if (LevelTask04){
            linearLevelTask04.setVisibility(View.GONE);
        }

        if (LevelTask01 & LevelTask02 & LevelTask03 & LevelTask04){
            layoutLevel.setVisibility(View.GONE);
            nothingBackgroundImageLevel.setVisibility(View.VISIBLE);
        }

    }
    private  void SaveHistory(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserHistory");
        UserHistory history = new UserHistory(title, descriptions, createdTime);
        FirebaseDatabase.getInstance().getReference("UserHistory")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(history);
    }
}