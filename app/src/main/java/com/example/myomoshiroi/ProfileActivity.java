package com.example.myomoshiroi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private DatabaseReference databaseReference,uidRef;
    private FirebaseAuth mAuth;

    Intent musicIntent;
    SharedPreferences prefs;

    TextView txtName, txtlevel,txtocoin,txtpoint;
    Button logoutButton, editButton, backButton, helpLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_profile);

        txtName = findViewById(R.id.ProfileName);
        txtlevel = findViewById(R.id.Level);
        txtocoin = findViewById(R.id.Ocoin);
        txtpoint = findViewById(R.id.Points);

        editButton = findViewById(R.id.editButton);
        logoutButton = findViewById(R.id.logoutButton);
        backButton = findViewById(R.id.ProfilebackButton);
        helpLevel = findViewById(R.id.btn_HelpLevel);

        //saving state into shared preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        musicIntent = new Intent(this, BackgroundSoundService.class);

        FirebaseAuth();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                EditProfile editProfile = new EditProfile();
                editProfile.showPopupWindow(v);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);

                AlertDialog.Builder alertExit = new AlertDialog.Builder(ProfileActivity.this);
                alertExit.setTitle("Logout Account")
                        .setMessage("Are you sure, want to LOGOUT your Account?")
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
                finish();
            }
        });
        helpLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertExit = new AlertDialog.Builder(v.getContext());
                alertExit.setTitle("Leveling Pattern")
                        .setMessage("Level 1 = 0 to 1199 pts\n" +
                                "Level 2 = (1000*0.2)+1000 = 1200 pts\n" +
                                "Level 3 = (1200*0.3)+1000 = 1360 pts\n" +
                                "Level 4 = (1360*0.4)+1000 = 1544 pts\n" +
                                "Level 5 = (1544*0.5)+1000 = 1772 pts\n" +
                                "Level 6 = (1772*0.6)+1000 = 2063 pts\n" +
                                "Level 7 = (2063*0.7)+1000 = 2444 pts\n" +
                                "Level 8 = (2444*0.8)+1000 = 2955 pts\n" +
                                "Level 9 = (2955*0.9)+1000 = 3659 pts\n" +
                                "Level 10 = (3659*1.0)+1000 = 4659 pts ...")
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
                String points = Objects.requireNonNull(snapshot.child("Point").getValue()).toString();
                txtName.setText(name);
                txtlevel.setText(level);
                txtocoin.setText(coin);
                txtpoint.setText(points);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // [START_EXCLUDE]
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }
}