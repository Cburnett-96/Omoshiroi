package com.example.myomoshiroi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myomoshiroi.other.BackgroundSoundService;
import com.example.myomoshiroi.other.SoundPoolManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private DatabaseReference databaseReference,uidRef, uidRefItem;
    private FirebaseAuth mAuth;

    Intent musicIntent;
    SharedPreferences prefs;

    ImageView changeAvatar;
    String avatarName;
    TextView txtName, txtlevel,txtocoin,txtpoint, txtCurrentLvl, txtNextLvl,
            timerAddCount, timerFreezeCount, timerStopCount;
    Button logoutButton, editButton, backButton, helpLevel, spendButton, claimButton;

    private int CurrentProgress;
    int currentpoints;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_profile);

        changeAvatar = findViewById(R.id.profileImg);
        txtName = findViewById(R.id.ProfileName);
        txtlevel = findViewById(R.id.Level);
        txtocoin = findViewById(R.id.Ocoin);
        txtpoint = findViewById(R.id.Points);
        txtCurrentLvl = findViewById(R.id.currentLevel);
        txtNextLvl = findViewById(R.id.nextLevel);
        timerAddCount = findViewById(R.id.timerAddOneMinCount);
        timerFreezeCount = findViewById(R.id.timerFreezeCount);
        timerStopCount = findViewById(R.id.timerStopCount);

        editButton = findViewById(R.id.editButton);
        logoutButton = findViewById(R.id.logoutButton);
        backButton = findViewById(R.id.ProfilebackButton);
        helpLevel = findViewById(R.id.btn_HelpLevel);
        spendButton = findViewById(R.id.spendButton);
        claimButton = findViewById(R.id.claimButton);
        progressBar = findViewById(R.id.progressBar);

        //saving state into shared preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        musicIntent = new Intent(this, BackgroundSoundService.class);
        avatarName = prefs.getString("avatarName",null);
        currentpoints = prefs.getInt("points", 0);
        CurrentProgress = currentpoints;

        FirebaseAuth();
        GetAvatarName();
        leveling();

        //Change Avatar Image
        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                Intent intent = new Intent(ProfileActivity.this, ChangeAvatarActivity.class);
                startActivity(intent);
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                EditProfile editProfile = new EditProfile();
                editProfile.showPopupWindow(v);
            }
        });

        spendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                Intent intent = new Intent(ProfileActivity.this, ShopActivity.class);
                startActivity(intent);
                finish();
            }
        });

        claimButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                DailyReward popupDailyReward = new DailyReward();
                popupDailyReward.showPopupWindow(v);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);

                AlertDialog.Builder alertExit = new AlertDialog.Builder(ProfileActivity.this);
                alertExit.setTitle("Logout Account")
                        .setMessage("Are you sure, want to log out your account?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Logout the Account and clear the Application data settings
                                mAuth.signOut();
                                prefs.edit().clear().apply();
                                SoundPoolManager.cleanUp();
                                stopService(musicIntent);

                                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                Intent intent = new Intent(ProfileActivity.this, MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        helpLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertExit = new AlertDialog.Builder(v.getContext());
                alertExit.setTitle("Leveling Pattern")
                        .setMessage("Level 1 = 0 to 1999 xp\n" +
                                "Level 2 = 2000 to 3999 xp\n" +
                                "Level 3 = 4000 to 5999 xp\n" +
                                "Level 4 = 6000 to 7999 xp\n" +
                                "Level 5 = 8000 to 9999 xp\n" +
                                "Level 6 = 10000 to 11999 xp\n" +
                                "Level 7 = 12000 to 13999 xp\n" +
                                "Level 8 = 14000 to 15999 xp\n" +
                                "Level 9 = 16000 to 17999 xp\n" +
                                "Level 10 = 18000 to 19999 xp ...")
                        .setCancelable(false)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = alertExit.create();
                dialog.show();
            }
        });
    }
    private void FirebaseAuth(){
        //        Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uidRef = databaseReference.child("UserData").child(uid);
        uidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = Objects.requireNonNull(snapshot.child("Fullname").getValue()).toString();
                String level = Objects.requireNonNull(snapshot.child("Level").getValue()).toString();
                String coin = Objects.requireNonNull(snapshot.child("Ocoin").getValue()).toString();

                txtName.setText(name);
                txtlevel.setText(level);
                txtocoin.setText(coin);
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
                String timerAdd = Objects.requireNonNull(snapshot.child("TimerAddOnemin").getValue()).toString();
                String timerStop = Objects.requireNonNull(snapshot.child("TimerStop").getValue()).toString();
                String timerFreeze = Objects.requireNonNull(snapshot.child("TimerFreeze").getValue()).toString();

                timerAddCount.setText(timerAdd);
                timerFreezeCount.setText(timerFreeze);
                timerStopCount.setText(timerStop);
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
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_cactus": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_cactus);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_chick": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_chick);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_cup": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_cup);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_hat": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_hat);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_pinaple": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_pinaple);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_android": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_android);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_cliprobot": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_cliprobot);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_head": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_head);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_monitor": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_monitor);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_robot": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_robot);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_skull": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_skull);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
            default: {
                InputStream imageStream = getResources().openRawResource(R.raw.profile);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                changeAvatar.setImageBitmap(bitmap);
                break;
            }
        }
    }
    private void leveling() {
        if (currentpoints <= 1999) {
            txtpoint.setText(currentpoints+"/1999");
            txtCurrentLvl.setText("1");
            txtNextLvl.setText("2");
            progressBar.setProgress((CurrentProgress * 100) / 1999);
        } else if (currentpoints < 3999 ) {
            txtpoint.setText(currentpoints+"/3999");
            txtCurrentLvl.setText("2");
            txtNextLvl.setText("3");
            progressBar.setProgress((CurrentProgress * 100) / 3999);
        } else if (currentpoints < 5999 ) {
            txtpoint.setText(currentpoints+"/5999");
            txtCurrentLvl.setText("3");
            txtNextLvl.setText("4");
            progressBar.setProgress((CurrentProgress * 100) / 5999);
        } else if (currentpoints < 7999 ) {
            txtpoint.setText(currentpoints+"/7999");
            txtCurrentLvl.setText("4");
            txtNextLvl.setText("5");
            progressBar.setProgress((CurrentProgress * 100) / 7999);
        } else if (currentpoints < 9999 ) {
            txtpoint.setText(currentpoints+"/9999");
            txtCurrentLvl.setText("5");
            txtNextLvl.setText("6");
            progressBar.setProgress((CurrentProgress * 100) / 9999);
        } else if (currentpoints < 11999 ) {
            txtpoint.setText(currentpoints+"/11999");
            txtCurrentLvl.setText("6");
            txtNextLvl.setText("7");
            progressBar.setProgress((CurrentProgress * 100) / 11999);
        } else if (currentpoints < 13999 ) {
            txtpoint.setText(currentpoints+"/13999");
            txtCurrentLvl.setText("7");
            txtNextLvl.setText("8");
            progressBar.setProgress((CurrentProgress * 100) / 13999);
        } else if (currentpoints < 15999 ) {
            txtpoint.setText(currentpoints+"/15999");
            txtCurrentLvl.setText("8");
            txtNextLvl.setText("9");
            progressBar.setProgress((CurrentProgress * 100) / 15999);
        } else if (currentpoints < 17999) {
            txtpoint.setText(currentpoints+"/17999");
            txtCurrentLvl.setText("9");
            txtNextLvl.setText("10");
            progressBar.setProgress((CurrentProgress * 100) / 17999);
        } else if (currentpoints < 19999 ) {
            txtpoint.setText(currentpoints+"/19999");
            txtCurrentLvl.setText("10");
            txtNextLvl.setText("11");
            progressBar.setProgress((CurrentProgress * 100) / 19999);
        } else if (currentpoints < 21999 ) {
            txtpoint.setText(currentpoints+"/21999");
            txtCurrentLvl.setText("11");
            txtNextLvl.setText("12");
            progressBar.setProgress((CurrentProgress * 100) / 21999);
        } else if (currentpoints < 23999 ) {
            txtpoint.setText(currentpoints+"/23999");
            txtCurrentLvl.setText("12");
            txtNextLvl.setText("13");
            progressBar.setProgress((CurrentProgress * 100) / 23999);
        } else if (currentpoints < 25999 ) {
            txtpoint.setText(currentpoints+"/25999");
            txtCurrentLvl.setText("13");
            txtNextLvl.setText("14");
            progressBar.setProgress((CurrentProgress * 100) / 25999);
        } else if (currentpoints < 27999 ) {
            txtpoint.setText(currentpoints+"/27999");
            txtCurrentLvl.setText("14");
            txtNextLvl.setText("15");
            progressBar.setProgress((CurrentProgress * 100) / 27999);
        } else if (currentpoints >= 29999 ) {
            txtpoint.setText(currentpoints+"/Max");
            txtCurrentLvl.setText("15");
            txtNextLvl.setText("Max");
            progressBar.setProgress((CurrentProgress * 100) / 29999);
        }
    }
}