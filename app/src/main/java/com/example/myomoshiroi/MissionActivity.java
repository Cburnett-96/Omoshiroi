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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myomoshiroi.other.SoundPoolManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;

public class MissionActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonBack, dailyRewardButton, btnDailyTask01;
    LinearLayout layoutDaily, layoutLevel, linearDailyTask01, linearLevelTask01, linearLevelTask02, linearLevelTask03, linearLevelTask04;
    TextView selectTask, itemDaily, itemLevel,
            task01Count, levelTask01Count;
    Button btnLevelTask01,btnLevelTask02,btnLevelTask03, btnLevelTask04;
    ColorStateList def;

    DatabaseReference databaseReference,uidRef;
    SharedPreferences prefs;
    int getTotalCountEasy, getocoins, getTotalocoins, dailyTask01, levelTask01;

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
        layoutLevel = findViewById(R.id.layout_level);
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

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        getocoins = prefs.getInt("ocoins",0);
        getTotalocoins = prefs.getInt("totalOcoins",0);
        getTotalCountEasy = prefs.getInt("taskEasyCount",0);
        dailyTask01 = 250;
        levelTask01 = 500;
        TaskCount();

        dailyRewardButton.setOnClickListener(v -> {
            DailyReward popupDailyReward = new DailyReward();
            popupDailyReward.showPopupWindow(v);
        });
        btnDailyTask01.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            if(getTotalCountEasy >= 5){
                Toast.makeText(this, "Successfully claimed your rewards!", Toast.LENGTH_SHORT).show();
                linearDailyTask01.setVisibility(View.GONE);
                uidRef.child("Ocoin").setValue(getocoins + dailyTask01);
                uidRef.child("TotalOcoins").setValue(getTotalocoins + dailyTask01);
            }else{
                Intent intent = new Intent(MissionActivity.this, EasyTopicActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnLevelTask01.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            if(getTotalCountEasy == 10){
                Toast.makeText(this, "Successfully claimed your rewards!", Toast.LENGTH_SHORT).show();
                linearLevelTask01.setVisibility(View.GONE);
                uidRef.child("Ocoin").setValue(getocoins + levelTask01);
                uidRef.child("TotalOcoins").setValue(getTotalocoins + levelTask01);
            }else{
                Intent intent = new Intent(MissionActivity.this, EasyTopicActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnLevelTask02.setOnClickListener(v -> {
            SoundPoolManager.playSound(0);
            Intent intent = new Intent(MissionActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });
        btnLevelTask03.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            Intent intent = new Intent(MissionActivity.this, ShopActivity.class);
            startActivity(intent);
            finish();
        });
        btnLevelTask04.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            Intent intent = new Intent(MissionActivity.this, EasyTopicActivity.class);
            startActivity(intent);
            finish();
        });
        buttonBack.setOnClickListener(v -> {
            SoundPoolManager.playSound(0);
            Intent intent = new Intent(MissionActivity.this, MenuActivity.class);
            startActivity(intent);
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
        } else if (view.getId() == R.id.itemLevel) {
            SoundPoolManager.playSound(1);
            itemDaily.setTextColor(def);
            itemLevel.setTextColor(getResources().getColor(R.color.forest_green));
            int size = itemLevel.getWidth();
            selectTask.animate().x(size).setDuration(100);
            layoutDaily.setVisibility(View.GONE);
            layoutLevel.setVisibility(View.VISIBLE);
        }
    }
    public void TaskCount(){
        task01Count.setText(getTotalCountEasy+"/5");
        levelTask01Count.setText(getTotalCountEasy+"/10");

        if(getTotalCountEasy == 5){
            btnDailyTask01.setText("✅");
            task01Count.setText("DONE!");
        }else if(getTotalCountEasy > 5){
            linearDailyTask01.setVisibility(View.GONE);
            uidRef.child("Ocoin").setValue(getocoins + dailyTask01);
            uidRef.child("TotalOcoins").setValue(getTotalocoins + dailyTask01);
            Toast.makeText(this, "Successfully claimed your rewards!", Toast.LENGTH_SHORT).show();
        }

        if (getTotalCountEasy == 10){
            btnLevelTask01.setText("✅");
            levelTask01Count.setText("DONE!");
        }else if (getTotalCountEasy > 10){
            linearLevelTask01.setVisibility(View.GONE);
            uidRef.child("Ocoin").setValue(getocoins + levelTask01);
            uidRef.child("TotalOcoins").setValue(getTotalocoins + levelTask01);
            Toast.makeText(this, "Successfully claimed your rewards!", Toast.LENGTH_SHORT).show();
        }
    }
}