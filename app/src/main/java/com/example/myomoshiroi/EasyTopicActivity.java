package com.example.myomoshiroi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myomoshiroi.other.Constants;
import com.example.myomoshiroi.other.SoundPoolManager;
import com.example.myomoshiroi.other.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class EasyTopicActivity extends AppCompatActivity {

    DatabaseReference databaseReference, uidRefItem;

    private Toast exitToast;

    Button btnNext, btnBack, btnPowerUps, btnspeak;
    TextView tvQuestion, tvQuestionNumber, titleQuestion, textViewShowTime;
    private RadioGroup radioGroup;
    private RadioButton radioButton1, radioButton2, radioButton3;

    private static final long START_TIME_IN_MILLIS = 90000;
    private static final long ADD_TIME_IN_MILLIS = 60000;
    private CountDownTimer mCountDownTimer;
    boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private ProgressBar mProgressBar;
    boolean pause = false;

    private int currentQuestionIndex = 0;
    private List<String> questions;
    private int correctQuestion = 0;
    private Map<String, Map<String, Boolean>> questionsAnswerMap;
    LinearLayout linearQuestionAnswer, backgroundImage, linearProgress;
    int[] background_images;
    boolean answer;

    private String getAnswer;
    TextToSpeech textToSpeech;

    ImageView imageAdd, imageFreeze, imageStop;
    TextView textViewAdd, textViewFreeze, textViewStop;
    int getTimerAdd, getTimerFreeze, getTimerStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_easy_topic);
        // Prevent taking screenshot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        linearQuestionAnswer = findViewById(R.id.linearQuestionAnswer);
        titleQuestion = findViewById(R.id.titleText);
        tvQuestion = findViewById(R.id.textViewQuestion);
        tvQuestionNumber = findViewById(R.id.titleTextCountQuestion);
        textViewShowTime = findViewById(R.id.text_view_countdown);
        btnspeak = findViewById(R.id.buttonSpeak);
        btnNext = findViewById(R.id.nextButton);
        btnBack = findViewById(R.id.backButton);
        btnPowerUps = findViewById(R.id.btn_powerUps);
        radioGroup = findViewById(R.id.radioGroupAnswer);

        radioButton1 = findViewById(R.id.radioButtonAnswer1);
        radioButton2 = findViewById(R.id.radioButtonAnswer2);
        radioButton3 = findViewById(R.id.radioButtonAnswer3);

        linearProgress = findViewById(R.id.linearLoadingProgress);
        linearProgress.setVisibility(View.VISIBLE);
        mProgressBar = findViewById(R.id.progressBarCircle);
        mProgressBar.setMax((int)START_TIME_IN_MILLIS / 1000);
        backgroundImage = findViewById(R.id.linearBackgroundQuestion);
        background_images = new int[]{R.drawable.easybg, R.drawable.easybg1,
                R.drawable.easybg2};

        questionsAnswerMap = Utils.getRandomEasy(Constants.QUESTION_SHOWING);
        questions = new ArrayList<>(questionsAnswerMap.keySet());

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uidRefItem = databaseReference.child("UserItem").child(uid);

        btnBack.setOnClickListener(v -> {
            linearQuestionAnswer.setVisibility(View.GONE);
            SoundPoolManager.playSound(0);
            AlertDialog.Builder alertExit = new AlertDialog.Builder(EasyTopicActivity.this);
            alertExit.setTitle("Exit Current Game")
                    .setMessage("Are you certain you want to leave?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        //Back to Menu Activity
                        Intent intent = new Intent(EasyTopicActivity.this,MenuActivity.class);
                        startActivity(intent);
                        finish();
                        stopTimer();
                    })
                    .setNegativeButton("No", (dialog, which) -> linearQuestionAnswer.setVisibility(View.VISIBLE));
            AlertDialog dialog = alertExit.create();
            dialog.show();
        });
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButtonSelect = findViewById(checkedId);
            if(radioButtonSelect != null){
                getAnswer = radioButtonSelect.getText().toString();
            }
        });
        btnNext.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            int array_length = background_images.length;
            Random random = new Random();
            int random_number = random.nextInt(array_length);
            RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
            if(radioButton != null){
                backgroundImage.setBackground(ContextCompat.getDrawable(getApplicationContext(), background_images[random_number]));
                answer = questionsAnswerMap.get(questions.get(currentQuestionIndex)).get(radioButton.getText());
                if (answer){
                    correctQuestion++;
                }
                if (pause){
                    startTimer();
                    pause = false;
                }
                currentQuestionIndex++;
                if (btnNext.getText().equals(getString(R.string.next))){
                    displayNextQuestions();
                }else{
                    stopTimer();
                    Intent intentResult = new Intent(EasyTopicActivity.this,ResultActivity.class);
                    intentResult.putExtra(Constants.CORRECT,correctQuestion);
                    intentResult.putExtra(Constants.INCORRECT,Constants.QUESTION_SHOWING - correctQuestion);
                    intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentResult.putExtra("EasyCount",1);
                    startActivity(intentResult);
                    finish();
                }
                radioGroup.clearCheck();
                getAnswer = null;
            }
            else {
                Toast.makeText(EasyTopicActivity.this, "Select Your Answer", Toast.LENGTH_SHORT).show();
            }
        });
        btnPowerUps.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            showPopupWindow(v);
        });
        textToSpeech = new TextToSpeech(getApplicationContext()
                , status -> {
                    if(status == TextToSpeech.SUCCESS){
                        textToSpeech.setLanguage(Locale.ENGLISH);
                    }
                });
        btnspeak.setOnClickListener(v -> {
            String getQuestion = tvQuestion.getText().toString();
            if (getAnswer != null){
                getQuestion = getQuestion.replace("_____",getAnswer);
            }else{
                getQuestion = getQuestion.replace("_____","blank");
            }
            textToSpeech.setPitch(1.3f);
            textToSpeech.setSpeechRate(0.7f);
            textToSpeech.speak(getQuestion, TextToSpeech.QUEUE_FLUSH,null);
        });
        displayData();
        startTimer();
    }
    public void onPause(){
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }
    private void displayNextQuestions() {
        tvQuestion.setText(questions.get(currentQuestionIndex));
        tvQuestionNumber.setText((currentQuestionIndex + 1) + " / 10");
        setAnswersToRadioButton();
        if (currentQuestionIndex == Constants.QUESTION_SHOWING  - 1){
            btnNext.setText(getText(R.string.finish));
        }
    }
    private void displayData() {
        tvQuestion.setText(questions.get(currentQuestionIndex));
        tvQuestionNumber.setText((currentQuestionIndex + 1)+ " / 10");
        setAnswersToRadioButton();
        linearProgress.setVisibility(View.GONE);
    }
    private void setAnswersToRadioButton(){
        ArrayList<String> questionKey = new ArrayList(Objects.requireNonNull(questionsAnswerMap.get(questions.get(currentQuestionIndex))).keySet());
        radioButton1.setText(questionKey.get(0));
        radioButton2.setText(questionKey.get(1));
        radioButton3.setText(questionKey.get(2));
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                AlertDialog.Builder alertExit = new AlertDialog.Builder(EasyTopicActivity.this);
                alertExit.setTitle("Time's up!")
                        .setIcon(android.R.drawable.ic_menu_info_details)
                        .setMessage("Did you wish to finish or restart from the start?")
                        .setCancelable(false)
                        .setPositiveButton("Finish", (dialog, which) -> {
                            mTimerRunning = false;
                            Intent intentResult = new Intent(EasyTopicActivity.this,ResultActivity.class);
                            intentResult.putExtra(Constants.CORRECT,correctQuestion);
                            intentResult.putExtra(Constants.INCORRECT,Constants.QUESTION_SHOWING - correctQuestion);
                            intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intentResult);
                            finish();
                        })
                        .setNegativeButton("Restart", (dialog, which) -> {
                            mTimerRunning = false;
                            Intent intent = new Intent(EasyTopicActivity.this,EasyTopicActivity.class);
                            startActivity(intent);
                            finish();
                        });
                AlertDialog dialog = alertExit.create();
                dialog.show();
            }
        }.start();
        mTimerRunning = true;
        //btnTest.setText("Pause");
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        //btnTest.setText("Start");
    }
    private void stopTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewShowTime.setText(timeLeftFormatted);
        mProgressBar.setProgress((int)(mTimeLeftInMillis / 1000));
    }

    @Override
    public void onBackPressed() {
        SoundPoolManager.playSound(0);
        if (exitToast == null || exitToast.getView() == null || exitToast.getView().getWindowToken() == null) {
            exitToast = Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT);
            exitToast.show();
        } else {
            stopTimer();
            Intent intent = new Intent(EasyTopicActivity.this,MenuActivity.class);
            startActivity(intent);
            exitToast.cancel();
            finish();
            super.onBackPressed();
        }
    }
    //PopupWindow display method
    public void showPopupWindow(final View view) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.power_ups_ingame, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        imageAdd = popupView.findViewById(R.id.imageAddOneMin);
        imageFreeze = popupView.findViewById(R.id.imageFreeze);
        imageStop = popupView.findViewById(R.id.imageStop);

        textViewAdd = popupView.findViewById(R.id.timerAddOneMinCount);
        textViewFreeze = popupView.findViewById(R.id.timerFreezeCount);
        textViewStop = popupView.findViewById(R.id.timerStopCount);

        UpdateCount();

        imageAdd.setOnClickListener(v -> {
            if (getTimerAdd > 0){
                SoundPoolManager.playSound(1);
                uidRefItem.child("TimerAddOnemin").setValue(ServerValue.increment(-1));
                pauseTimer();
                mTimeLeftInMillis += ADD_TIME_IN_MILLIS;
                mProgressBar.setMax((int)(mTimeLeftInMillis / 1000));
                updateCountDownText();
                startTimer();
                UpdateCount();
                Toast.makeText(EasyTopicActivity.this, "Added one minute!", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            } else {
                SoundPoolManager.playSound(0);
                Toast.makeText(EasyTopicActivity.this, "You don't have enough power-ups to use this!", Toast.LENGTH_LONG).show();
            }
        });
        imageFreeze.setOnClickListener(v -> {
            if (getTimerFreeze > 0){
                SoundPoolManager.playSound(1);
                uidRefItem.child("TimerFreeze").setValue(ServerValue.increment(-1));
                pauseTimer();
                textViewShowTime.setText("🥶");
                Toast.makeText(EasyTopicActivity.this, "The timer has been frozen!", Toast.LENGTH_SHORT).show();
                pause = true;
                UpdateCount();
                popupWindow.dismiss();
            } else {
                SoundPoolManager.playSound(0);
                Toast.makeText(EasyTopicActivity.this, "You don't have enough power-ups to use this!", Toast.LENGTH_LONG).show();
            }
        });
        imageStop.setOnClickListener(v -> {
            if (getTimerStop > 0){
                SoundPoolManager.playSound(1);
                uidRefItem.child("TimerStop").setValue(ServerValue.increment(-1));
                stopTimer();
                textViewShowTime.setText("🚫");
                UpdateCount();
                Toast.makeText(EasyTopicActivity.this, "The timer has been stopped!", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            } else {
                SoundPoolManager.playSound(0);
                Toast.makeText(EasyTopicActivity.this, "You don't have enough power-ups to use this!", Toast.LENGTH_LONG).show();
            }

        });

        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }
    private void UpdateCount(){
        uidRefItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String timerAdd = Objects.requireNonNull(snapshot.child("TimerAddOnemin").getValue()).toString();
                String timerStop = Objects.requireNonNull(snapshot.child("TimerStop").getValue()).toString();
                String timerFreeze = Objects.requireNonNull(snapshot.child("TimerFreeze").getValue()).toString();

                textViewAdd.setText(timerAdd);
                textViewFreeze.setText(timerFreeze);
                textViewStop.setText(timerStop);

                getTimerAdd = Integer.parseInt(timerAdd);
                getTimerFreeze = Integer.parseInt(timerFreeze);
                getTimerStop = Integer.parseInt(timerStop);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }
}