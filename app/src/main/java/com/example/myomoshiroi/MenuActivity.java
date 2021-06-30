package com.example.myomoshiroi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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

    Button arcade,mission,token,ranking,history;
    ImageView profile, setting;
    Animation scaleUp, scaleDown;
    boolean singleBack = false;


    private DatabaseReference databaseReference,uidRef;
    private FirebaseAuth mAuth;

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

        scaleUp = AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this,R.anim.scale_down);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // [START_EXCLUDE]
                System.out.println("The read failed: " + error.getMessage());
            }
        });

        profile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    profile.startAnimation(scaleUp);

                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    profile.startAnimation(scaleDown);
                }
                return true;
            }
        });
        setting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    setting.startAnimation(scaleUp);

                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    setting.startAnimation(scaleDown);
                }
                return true;
            }
        });
        arcade.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    arcade.startAnimation(scaleUp);

                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    arcade.startAnimation(scaleDown);
                }
                return true;
            }
        });
        mission.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    mission.startAnimation(scaleUp);

                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    mission.startAnimation(scaleDown);
                }
                return true;
            }
        });
        token.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    token.startAnimation(scaleUp);

                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    token.startAnimation(scaleDown);
                }
                return true;
            }
        });
        ranking.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    ranking.startAnimation(scaleUp);

                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    ranking.startAnimation(scaleDown);
                }
                return true;
            }
        });
        history.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    history.startAnimation(scaleUp);

                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    history.startAnimation(scaleDown);
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (singleBack) {
            super.onBackPressed();
            return;
        }
        this.singleBack = true;
        Toast.makeText(this, "Double Back to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                singleBack=false;
            }
        }, 2000);
    }
}