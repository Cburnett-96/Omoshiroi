package com.example.myomoshiroi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myomoshiroi.other.Constants;
import com.example.myomoshiroi.other.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class EasyTopicActivity extends AppCompatActivity {

    private Toast exitToast;

    private Button btnNext, btnBack;
    private TextView tvQuestion, tvQuestionNumber;
    private RadioGroup radioGroup;
    private RadioButton radioButton1, radioButton2, radioButton3;

    private int currentQuestionIndex = 0;
    private List<String> questions;
    private int correctQuestion = 0;
    private Map<String, Map<String, Boolean>> questionsAnswerMap;
    LinearLayout backgroundImage, linearProgress;
    int[] background_images;
    boolean answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_easy_topic);

        tvQuestion = findViewById(R.id.textViewQuestion);
        tvQuestionNumber = findViewById(R.id.titleTextCountQuestion);
        btnNext = findViewById(R.id.nextButton);
        btnBack = findViewById(R.id.backButton);
        radioGroup = findViewById(R.id.radioGroupAnswer);

        radioButton1 = findViewById(R.id.radioButtonAnswer1);
        radioButton2 = findViewById(R.id.radioButtonAnswer2);
        radioButton3 = findViewById(R.id.radioButtonAnswer3);

        linearProgress = findViewById(R.id.linearLoadingProgress);
        linearProgress.setVisibility(View.VISIBLE);
        backgroundImage = findViewById(R.id.linearBackgroundQuestion);
        background_images = new int[]{R.drawable.easybg, R.drawable.easybg1,
                R.drawable.easybg2, R.drawable.easybg3,R.drawable.easybg4};

        questionsAnswerMap = Utils.getRandomLiterature(Constants.QUESTION_SHOWING);
        questions = new ArrayList<>(questionsAnswerMap.keySet());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(0);
                AlertDialog.Builder alertExit = new AlertDialog.Builder(EasyTopicActivity.this);
                alertExit.setTitle("Exit Current Game")
                        .setMessage("Are you sure, want to EXIT?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Back to Menu Activity
                                Intent intent = new Intent(EasyTopicActivity.this,MenuActivity.class);
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
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButtonSelect = findViewById(checkedId);
                Toast.makeText(EasyTopicActivity.this, "You Select: "+radioButtonSelect.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.playSound(1);
                int array_length = background_images.length;
                Random random = new Random();
                int random_number = random.nextInt(array_length);
                backgroundImage.setBackground(ContextCompat.getDrawable(getApplicationContext(), background_images[random_number]));

                RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                answer = questionsAnswerMap.get(questions.get(currentQuestionIndex)).get(radioButton.getText());

                if (answer){
                    correctQuestion++;
                }
                currentQuestionIndex++;

                if (btnNext.getText().equals(getString(R.string.next))){
                    displayNextQuestions();
                }else{
                    Intent intentResult = new Intent(EasyTopicActivity.this,ResultActivity.class);
                    intentResult.putExtra(Constants.CORRECT,correctQuestion);
                    intentResult.putExtra(Constants.INCORRECT,Constants.QUESTION_SHOWING - correctQuestion);
                    intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentResult);
                    finish();
                }
            }
        });
        displayData();
    }
    private void displayNextQuestions() {
        setAnswersToRadioButton();
        tvQuestion.setText(questions.get(currentQuestionIndex));
        tvQuestionNumber.setText((currentQuestionIndex + 1) + " / 10");

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
    @Override
    public void onBackPressed() {
        SoundPoolManager.playSound(0);
        if (exitToast == null || exitToast.getView() == null || exitToast.getView().getWindowToken() == null) {
            exitToast = Toast.makeText(this, "Press again to exit", Toast.LENGTH_LONG);
            exitToast.show();
        } else {
            Intent intent = new Intent(EasyTopicActivity.this,MenuActivity.class);
            startActivity(intent);
            finish();
            exitToast.cancel();
            super.onBackPressed();
        }
    }
}