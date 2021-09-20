package com.example.myomoshiroi.other;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.myomoshiroi.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class EasyModeTenses {

    public static String formatDate(long time){
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());
    }

    public static Map<String,Map<String,Boolean>> getTensesStage1(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("Past Participle",true);
        answer1.put("Past Tense",false);
        answer1.put("Base Form",false);
        questions.put("Begun",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("Base Form",true);
        answer2.put("Past Participle",false);
        answer2.put("Past Tense",false);
        questions.put("Teach",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("Past Tense",true);
        answer3.put("Base Form",false);
        answer3.put("Past Participle",false);
        questions.put("Saw",answer3);

        HashMap<String,Boolean> answer4 = new HashMap<>();
        answer4.put("Past Tense",true);
        answer4.put("Past Participle",false);
        answer4.put("Base Form",false);
        questions.put("Broke",answer4);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("Past Participle",true);
        answer5.put("Past Tense",false);
        answer5.put("Base Form",false);
        questions.put("Been able to",answer5);

        HashMap<String,Boolean> answer6 = new HashMap<>();
        answer6.put("Base Form",true);
        answer6.put("Past Tense",false);
        answer6.put("Past Participle",false);
        questions.put("Do",answer6);

        HashMap<String,Boolean> answer7 = new HashMap<>();
        answer7.put("Past Tense",true);
        answer7.put("Base Form",false);
        answer7.put("Past participle",false);
        questions.put("Ate",answer7);

        HashMap<String,Boolean> answer8 = new HashMap<>();
        answer8.put("Past Participle",false);
        answer8.put("Base Form",false);
        answer8.put("Past Tense",true);
        questions.put("Flown",answer8);

        HashMap<String,Boolean> answer9 = new HashMap<>();
        answer9.put("Base Form",true);
        answer9.put("Past Participle",false);
        answer9.put("Past Tense",false);
        questions.put("Drink",answer9);

        HashMap<String,Boolean> answer10 = new HashMap<>();
        answer10.put("Past Tense",false);
        answer10.put("Base Form",false);
        answer10.put("Past Participle",true);
        questions.put("Drove",answer10);

        HashMap<String,Boolean> answer11 = new HashMap<>();
        answer11.put("Past Participle",false);
        answer11.put("Base Form",false);
        answer11.put("Past Tense",true);
        questions.put("Seen",answer11);

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getTensesStage2(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("Present",false);
        answer1.put("Future",false);
        answer1.put("Past",true);
        questions.put("We went shopping and I bought a new pair of jeans.",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("Present",false);
        answer2.put("Future",false);
        answer2.put("Past",true);
        questions.put("Did you sleep well last night?",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("Present",true);
        answer3.put("Future",false);
        answer3.put("Past",false);
        questions.put("I used to make a lot of money, but I don't make much now.",answer3);

        HashMap<String,Boolean> answer4 = new HashMap<>();
        answer4.put("Present",false);
        answer4.put("Future",true);
        answer4.put("Past",false);
        questions.put("We were really surprised when we won the game.",answer4);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("Present",true);
        answer5.put("Future",false);
        answer5.put("Past",false);
        questions.put("We thought we would lose for sure",answer5);

        HashMap<String,Boolean> answer6 = new HashMap<>();
        answer6.put("Present",false);
        answer6.put("Future",true);
        answer6.put("Past",false);
        questions.put("Have you ever met a movie star?",answer6);

        HashMap<String,Boolean> answer7 = new HashMap<>();
        answer7.put("Present",true);
        answer7.put("Future",false);
        answer7.put("Past",false);
        questions.put("Mum taught our sisters how to cook, but she didn't teach us.",answer7);

        HashMap<String,Boolean> answer8 = new HashMap<>();
        answer8.put("Present",false);
        answer8.put("Future",false);
        answer8.put("Past",true);
        questions.put("Have you find your lost dog yet?",answer8);

        HashMap<String,Boolean> answer9 = new HashMap<>();
        answer9.put("Present",true);
        answer9.put("Future",false);
        answer9.put("Past",false);
        questions.put("She has spoken too softly.",answer9);

        HashMap<String,Boolean> answer10 = new HashMap<>();
        answer10.put("Present",false);
        answer10.put("Future",false);
        answer10.put("Past",true);
        questions.put("But we heard everything she said.",answer10);

        HashMap<String,Boolean> answer11 = new HashMap<>();
        answer11.put("Past",true);
        answer11.put("Future",false);
        answer11.put("Present",false);
        questions.put("We opened presents.",answer11);

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getRandomTenses(Context context, String subject, int SIZE){
        Map<String,Map<String,Boolean>> questionsMap = new HashMap<>();
        Map<String, Map<String, Boolean>> originalQuestion;

        if (subject.equals(context.getString(R.string.tenses_stage_01))){
            originalQuestion = getTensesStage1();
        }else{
            originalQuestion = getTensesStage2();
        }

        int originalSize =  originalQuestion.size();
        ArrayList<String> keyList = new ArrayList<String>(originalQuestion.keySet());

        while (questionsMap.size()<=SIZE){
            Random random = new Random();
            int randomNumber = random.nextInt(originalSize);
            String question = keyList.get(randomNumber);
            if (!questionsMap.containsKey(question)){
                questionsMap.put(question,originalQuestion.get(question));
            }
        }
        return questionsMap;
    }
}
