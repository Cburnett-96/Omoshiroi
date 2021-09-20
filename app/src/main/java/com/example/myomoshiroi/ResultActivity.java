package com.example.myomoshiroi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myomoshiroi.model.UserHistory;
import com.example.myomoshiroi.model.Attempt;
import com.example.myomoshiroi.other.Constants;
import com.example.myomoshiroi.other.SoundPoolManager;
import com.example.myomoshiroi.other.EasyModeTenses;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.Calendar;

public class ResultActivity extends AppCompatActivity {

    private TextView tvCorrect, tvIncorrect, tvEarned, tvOcoin;
    Button exitButton, playAgainButton;
    private ImageView imageViewGreet;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, uidRef, uidRefMission;
    SharedPreferences prefs;
    int getpoints, getocoins, getTotalocoins, earnedPoints, earnedOcoins, correctAnswer, incorrectAnswer,
            gettask01, getTotalCountEasy, dailyEasyCount, getTotalDailyEasyCount, gettask04;
    String title, descriptions, createdTime, currentDate;

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
        tvCorrect = findViewById(R.id.textValueCorrect);
        tvIncorrect = findViewById(R.id.textValueInCorrect);
        tvEarned = findViewById(R.id.textValuePointEarn);
        tvOcoin = findViewById(R.id.textValueOcoinEarn);
        imageViewGreet = findViewById(R.id.imageViewGreetings);

        Intent intent = getIntent();
        correctAnswer = intent.getIntExtra(Constants.CORRECT, 0);
        incorrectAnswer = intent.getIntExtra(Constants.INCORRECT, 0);
        gettask01 = intent.getIntExtra("EasyCount", 0);
        dailyEasyCount = intent.getIntExtra("EasyCount", 0);
        earnedPoints = (correctAnswer * Constants.CORRECT_POINT) - (incorrectAnswer * Constants.INCORRECT_POINT);
        earnedOcoins = (correctAnswer * Constants.OCOINS);

        if (correctAnswer == 10){
            gettask04 = 10;
        }

        Intent intentSubject = getIntent();
        String subject = intentSubject.getStringExtra(Constants.SUBJECT);

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
                intent.putExtra(Constants.SUBJECT, subject);
                startActivity(intent);
                finish();
            }
        });
        Attempt attempt = new Attempt(
                Calendar.getInstance().getTimeInMillis(),
                correctAnswer,
                incorrectAnswer,
                earnedPoints,
                earnedOcoins
        );

        currentDate = EasyModeTenses.formatDate(attempt.getCreatedTime());
        title = "Easy Mode ("+subject+")";
        descriptions = "You've got "+correctAnswer+" correct and "+incorrectAnswer+" incorrect answer. " +
                "Earned "+earnedPoints+ " experience points and "+earnedOcoins+" ocoins";
        createdTime = currentDate;

        displayData(attempt);
        Greetings();
        RetrievedSaved();
        SaveHistory();
    }
    private void displayData(Attempt attempt) {
        tvCorrect.setText(String.valueOf(attempt.getCorrect()));
        tvIncorrect.setText(String.valueOf(attempt.getIncorrect()));
        tvEarned.setText(String.valueOf(attempt.getEarned()));
        tvOcoin.setText(String.valueOf(attempt.getOcoin()));
    }
    private void Greetings() {

        if (correctAnswer >= 8){
            int dimensionInPixelWid = 266;
            int dimensionInPixelHei = 66;
            imageViewGreet.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixelHei, getResources().getDisplayMetrics());
            imageViewGreet.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixelWid, getResources().getDisplayMetrics());
            imageViewGreet.requestLayout();

            InputStream imageStream = this.getResources().openRawResource(R.raw.awesome);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageViewGreet.setImageBitmap(bitmap);
        }else if (correctAnswer >= 4){
            int dimensionInPixelWid = 199;
            int dimensionInPixelHei = 199;
            imageViewGreet.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixelHei, getResources().getDisplayMetrics());
            imageViewGreet.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixelWid, getResources().getDisplayMetrics());
            imageViewGreet.requestLayout();

            InputStream imageStream = this.getResources().openRawResource(R.raw.goodjob);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageViewGreet.setImageBitmap(bitmap);
        }else{
            int dimensionInPixelWid = 199;
            int dimensionInPixelHei = 133;
            imageViewGreet.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixelHei, getResources().getDisplayMetrics());
            imageViewGreet.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixelWid, getResources().getDisplayMetrics());
            imageViewGreet.requestLayout();

            InputStream imageStream = this.getResources().openRawResource(R.raw.tryagain);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageViewGreet.setImageBitmap(bitmap);
        }
    }
    private void RetrievedSaved(){
        //Retrieving and Saving points, level and ocoins
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        getocoins = prefs.getInt("ocoins",0);
        getTotalocoins = prefs.getInt("totalOcoins",0);
        getpoints = prefs.getInt("points",0);
        getTotalCountEasy = prefs.getInt("task01",0);
        getTotalDailyEasyCount = prefs.getInt("taskDailyEasyCount",0);
        //        Get Firebase auth instance
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uidRef = databaseReference.child("UserData").child(uid);
        uidRefMission = databaseReference.child("UserMission").child(uid);
        uidRef.child("Ocoin").setValue(getocoins + earnedOcoins);
        uidRef.child("Point").setValue(getpoints + earnedPoints);
        uidRef.child("TotalOcoins").setValue(getTotalocoins + earnedOcoins);
        uidRefMission.child("Task01").setValue(getTotalCountEasy + gettask01);
        uidRefMission.child("DailyEasyCount").setValue(getTotalDailyEasyCount + dailyEasyCount);
        uidRefMission.child("Task04").setValue(gettask04);
    }
    private  void SaveHistory(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserHistory");
        UserHistory history = new UserHistory(title, descriptions, createdTime);
        FirebaseDatabase.getInstance().getReference("UserHistory")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(history);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MenuActivity.class);
        startActivity(intent);
        finish();
    }
}