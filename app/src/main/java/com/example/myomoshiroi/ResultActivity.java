package com.example.myomoshiroi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.myomoshiroi.other.Attempt;
import com.example.myomoshiroi.other.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ResultActivity extends AppCompatActivity {

    private TextView tvCorrect, tvIncorrect, tvEarned, tvOcoin, greet;
    private Button exitButton, playAgainButton;

    private DatabaseReference databaseReference,uidRef;
    private FirebaseAuth mAuth;
    SharedPreferences prefs;
    int getpoints, getocoins, earnedPoints, earnedOcoins, correctAnswer, incorrectAnswer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_result);

        exitButton = findViewById(R.id.closeButton);
        playAgainButton = findViewById(R.id.playAgainButton);

        Intent intent = getIntent();
        correctAnswer = intent.getIntExtra(Constants.CORRECT, 0);
        incorrectAnswer = intent.getIntExtra(Constants.INCORRECT, 0);
        earnedPoints = (correctAnswer * Constants.CORRECT_POINT) - (incorrectAnswer * Constants.INCORRECT_POINT);
        earnedOcoins = (correctAnswer * Constants.OCOINS);

        tvCorrect = findViewById(R.id.textValueCorrect);
        tvIncorrect = findViewById(R.id.textValueInCorrect);
        tvEarned = findViewById(R.id.textValuePointEarn);
        tvOcoin = findViewById(R.id.textValueOcoinEarn);
        greet = findViewById(R.id.GreetingsText);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                Intent intent = new Intent(ResultActivity.this,MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                Intent intent = new Intent(ResultActivity.this,EasyTopicActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Attempt attempt = new Attempt(
                correctAnswer,
                incorrectAnswer,
                earnedPoints,
                earnedOcoins
        );
        displayData(attempt);
        Greetings();
        RetrievedSaved();
    }
    private void displayData(Attempt attempt) {

        tvCorrect.setText(String.valueOf(attempt.getCorrect()));
        tvIncorrect.setText(String.valueOf(attempt.getIncorrect()));
        tvEarned.setText(String.valueOf(attempt.getEarned()));
        tvOcoin.setText(String.valueOf(attempt.getOcoin()));
    }
    private void Greetings() {

        if (correctAnswer >= 8){
            greet.setTextColor(getResources().getColor(R.color.yellow));
            greet.setText("Excellent!");
        }else if (correctAnswer >= 5){
            greet.setTextColor(getResources().getColor(R.color.mediumPurple));
            greet.setText("Good Job!");
        }else if (correctAnswer >= 3){
            greet.setTextColor(getResources().getColor(R.color.purple_500));
            greet.setText("Keep it up!");
        }else{
            greet.setTextColor(getResources().getColor(R.color.red));
            greet.setText("Opss Try Again!");
        }
    }
    private void RetrievedSaved(){
        //Retrieving and Saving points, level and ocoins
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        getocoins = prefs.getInt("ocoins",0);
        getpoints = prefs.getInt("points",0);
        //        Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uidRef = databaseReference.child("UserData").child(uid);
        uidRef.child("Ocoin").setValue(getocoins + earnedOcoins);
        uidRef.child("Point").setValue(getpoints + earnedPoints);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MenuActivity.class);
        startActivity(intent);
        finish();
    }
}