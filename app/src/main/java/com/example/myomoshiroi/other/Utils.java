package com.example.myomoshiroi.other;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Utils {

    public static String formatDate(long time){
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());
    }

    public static Map<String,Map<String,Boolean>> getEasyQuestions(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("travel",true);
        answer1.put("voyage",false);
        answer1.put("journey",false);
        questions.put("Jenny likes her job but she hates the long _____ to work every day.",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("vehicles ",true);
        answer2.put("traffic",false);
        answer2.put("transport",false);
        questions.put("It's a great place to live apart from the increasing volume of _____ that passes under my window every day.",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("about",true);
        answer3.put("against",false);
        answer3.put("for",false);
        questions.put("Japhet is very angry _____ him boss's decision to sack several members of staff.",answer3);

        HashMap<String,Boolean> answer4 = new HashMap<>();
        answer4.put("Past",true);
        answer4.put("Present",false);
        answer4.put("Future",false);
        questions.put("Loved?",answer4);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("Present",true);
        answer5.put("Past",false);
        answer5.put("Future",false);
        questions.put("Teach?",answer5);

        HashMap<String,Boolean> answer6 = new HashMap<>();
        answer6.put("was displeased",true);
        answer6.put("displeased",false);
        answer6.put("had displeased",false);
        questions.put("I _____ with her.",answer6);

        HashMap<String,Boolean> answer7 = new HashMap<>();
        answer7.put("should be taken",true);
        answer7.put("should take",false);
        answer7.put("should be took",false);
        questions.put("This medicine on _____ time.",answer7);

        HashMap<String,Boolean> answer8 = new HashMap<>();
        answer8.put("are called",true);
        answer8.put("called",false);
        answer8.put("is called",false);
        questions.put("Wait here till you _____.",answer8);

        HashMap<String,Boolean> answer9 = new HashMap<>();
        answer9.put("should not be given",true);
        answer9.put("would give",false);
        answer9.put("should not give",false);
        questions.put("Children _____ undue importance.",answer9);

        HashMap<String,Boolean> answer10 = new HashMap<>();
        answer10.put("being repaired",true);
        answer10.put("been repaired",false);
        answer10.put("repaired",false);
        questions.put("You cannot go along here because the road is _____.",answer10);

        HashMap<String,Boolean> answer11 = new HashMap<>();
        answer11.put("Future",true);
        answer11.put("Past",false);
        answer11.put("Present",false);
        questions.put("I will eat.",answer11);

        HashMap<String,Boolean> answer12 = new HashMap<>();
        answer12.put("Future",true);
        answer12.put("Present",false);
        answer12.put("Past",false);
        questions.put("I will be ten on Tuesday",answer12);

        HashMap<String,Boolean> answer13 = new HashMap<>();
        answer13.put("Past",true);
        answer13.put("Future",false);
        answer13.put("Present",false);
        questions.put("We opened presents.",answer13);

        HashMap<String,Boolean> answer14 = new HashMap<>();
        answer14.put("Past",true);
        answer14.put("Present",false);
        answer14.put("Future",false);
        questions.put("You were there.",answer14);

        HashMap<String,Boolean> answer15 = new HashMap<>();
        answer15.put("Present",true);
        answer15.put("Future",false);
        answer15.put("Past",false);
        questions.put("She always plays",answer15);

        HashMap<String,Boolean> answer16 = new HashMap<>();
        answer16.put("be solved",true);
        answer16.put("have solved",false);
        answer16.put("be solving",false);
        questions.put("This puzzle can not _____",answer16);

        HashMap<String,Boolean> answer17 = new HashMap<>();
        answer17.put("to be paid",true);
        answer17.put("to pay",false);
        answer17.put("to be paying",false);
        questions.put("It is time for the electric bill _____",answer17);

        HashMap<String,Boolean> answer18 = new HashMap<>();
        answer18.put("was opened",true);
        answer18.put("opened",false);
        answer18.put("was open",false);
        questions.put("The exhibition _____ by the chairman",answer18);

        HashMap<String,Boolean> answer19 = new HashMap<>();
        answer19.put("addressed",true);
        answer19.put("address",false);
        answer19.put("addresses",false);
        questions.put("We will be _____ by our quest speaker.",answer19);

        HashMap<String,Boolean> answer20 = new HashMap<>();
        answer20.put("had been broken",true);
        answer20.put("broke",false);
        answer20.put("has broken",false);
        questions.put("All window glasses _____ by the cyclonic wind storm last week.",answer20);

        HashMap<String,Boolean> answer21 = new HashMap<>();
        answer21.put("is prohibited",true);
        answer21.put("was prohibited",false);
        answer21.put("has been prohibited",false);
        questions.put("Smoking _____ here.",answer21);

        HashMap<String,Boolean> answer22 = new HashMap<>();
        answer22.put("to be hanged",true);
        answer22.put("to hanging",false);
        answer22.put("to hang",false);
        questions.put("He was ordered by the judge _____.",answer22);

        HashMap<String,Boolean> answer23 = new HashMap<>();
        answer23.put("will have been proved",true);
        answer23.put("will be proved",false);
        answer23.put("would be proved",false);
        questions.put("This theory _____ by 2025.",answer23);

        HashMap<String,Boolean> answer24 = new HashMap<>();
        answer24.put("was given up",true);
        answer24.put("gave up",false);
        answer24.put("was gave up",false);
        questions.put("Smoking _____ by me last year.",answer24);

        HashMap<String,Boolean> answer25 = new HashMap<>();
        answer25.put("has been completed",true);
        answer25.put("has completed",false);
        answer25.put("is completing",false);
        questions.put("This project _____ this week.",answer25);

        HashMap<String,Boolean> answer26 = new HashMap<>();
        answer26.put("was affected",true);
        answer26.put("affecting",false);
        answer26.put("will affected",false);
        questions.put("The traffic _____ because of the heavy rain.",answer26);

        HashMap<String,Boolean> answer27 = new HashMap<>();
        answer27.put("having been finished",true);
        answer27.put("had finishing",false);
        answer27.put("being finish",false);
        questions.put("The work _____, he went home.",answer27);

        HashMap<String,Boolean> answer28 = new HashMap<>();
        answer28.put("will be",true);
        answer28.put("should be",false);
        answer28.put("to be",false);
        questions.put("We _____ addressed by our quest speaker.",answer28);

        HashMap<String,Boolean> answer29 = new HashMap<>();
        answer29.put("ordered",true);
        answer29.put("order",false);
        answer29.put("ordering",false);
        questions.put("He was _____ by the judge to be hanged.",answer29);

        HashMap<String,Boolean> answer30 = new HashMap<>();
        answer30.put("here",true);
        answer30.put("there",false);
        answer30.put("their",false);
        questions.put("Smoking is prohibited _____.",answer30);

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getRandomEasy(int SIZE){
        Map<String,Map<String,Boolean>> questionsMap = new HashMap<>();
        Map<String, Map<String, Boolean>> originalQuestion;

        originalQuestion = getEasyQuestions();

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
