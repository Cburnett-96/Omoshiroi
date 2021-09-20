package com.example.myomoshiroi.other;

import android.content.Context;

import com.example.myomoshiroi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EasyModeMultiple {

    public static Map<String, Map<String,Boolean>> getMultipleStage1(){
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
        answer4.put("was displeased",true);
        answer4.put("displeased",false);
        answer4.put("had displeased",false);
        questions.put("I _____ with her.",answer4);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("should be taken",true);
        answer5.put("should take",false);
        answer5.put("should be took",false);
        questions.put("This medicine on _____ time.",answer5);

        HashMap<String,Boolean> answer6 = new HashMap<>();
        answer6.put("are called",true);
        answer6.put("called",false);
        answer6.put("is called",false);
        questions.put("Wait here till you _____.",answer6);

        HashMap<String,Boolean> answer7 = new HashMap<>();
        answer7.put("should not be given",true);
        answer7.put("would give",false);
        answer7.put("should not give",false);
        questions.put("Children _____ undue importance.",answer7);

        HashMap<String,Boolean> answer8 = new HashMap<>();
        answer8.put("being repaired",true);
        answer8.put("been repaired",false);
        answer8.put("repaired",false);
        questions.put("You cannot go along here because the road is _____.",answer8);

        HashMap<String,Boolean> answer9 = new HashMap<>();
        answer9.put("be solved",true);
        answer9.put("have solved",false);
        answer9.put("be solving",false);
        questions.put("This puzzle can not _____",answer9);

        HashMap<String,Boolean> answer10 = new HashMap<>();
        answer10.put("to be paid",true);
        answer10.put("to pay",false);
        answer10.put("to be paying",false);
        questions.put("It is time for the electric bill _____",answer10);

        HashMap<String,Boolean> answer11 = new HashMap<>();
        answer11.put("is prohibited",true);
        answer11.put("was prohibited",false);
        answer11.put("has been prohibited",false);
        questions.put("Smoking _____ here.",answer11);

        return questions;
    }

    public static Map<String, Map<String,Boolean>> getMultipleStage2(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("was opened",true);
        answer1.put("opened",false);
        answer1.put("was open",false);
        questions.put("The exhibition _____ by the chairman",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("addressed",true);
        answer2.put("address",false);
        answer2.put("addresses",false);
        questions.put("We will be _____ by our quest speaker.",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("had been broken",true);
        answer3.put("broke",false);
        answer3.put("has broken",false);
        questions.put("All window glasses _____ by the cyclonic wind storm last week.",answer3);

        HashMap<String,Boolean> answer4 = new HashMap<>();
        answer4.put("is prohibited",true);
        answer4.put("was prohibited",false);
        answer4.put("has been prohibited",false);
        questions.put("Smoking _____ here.",answer4);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("to be hanged",true);
        answer5.put("to hanging",false);
        answer5.put("to hang",false);
        questions.put("He was ordered by the judge _____.",answer5);

        HashMap<String,Boolean> answer6 = new HashMap<>();
        answer6.put("will have been proved",true);
        answer6.put("will be proved",false);
        answer6.put("would be proved",false);
        questions.put("This theory _____ by 2025.",answer6);

        HashMap<String,Boolean> answer7 = new HashMap<>();
        answer7.put("was given up",true);
        answer7.put("gave up",false);
        answer7.put("was gave up",false);
        questions.put("Smoking _____ by me last year.",answer7);

        HashMap<String,Boolean> answer8 = new HashMap<>();
        answer8.put("has been completed",true);
        answer8.put("has completed",false);
        answer8.put("is completing",false);
        questions.put("This project _____ this week.",answer8);

        HashMap<String,Boolean> answer9 = new HashMap<>();
        answer9.put("was affected",true);
        answer9.put("affecting",false);
        answer9.put("will affected",false);
        questions.put("The traffic _____ because of the heavy rain.",answer9);

        HashMap<String,Boolean> answer10 = new HashMap<>();
        answer10.put("having been finished",true);
        answer10.put("had finishing",false);
        answer10.put("being finish",false);
        questions.put("The work _____, he went home.",answer10);

        HashMap<String,Boolean> answer11 = new HashMap<>();
        answer11.put("here",true);
        answer11.put("on this",false);
        answer11.put("there",false);
        questions.put("Smoking is prohibited _____.",answer11);

        return questions;
    }

    public static Map<String, Map<String,Boolean>> getMultipleStage3(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("will be",true);
        answer1.put("should be",false);
        answer1.put("to be",false);
        questions.put("We _____ addressed by our quest speaker.",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("ordered",true);
        answer2.put("order",false);
        answer2.put("ordering",false);
        questions.put("He was _____ by the judge to be hanged.",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("listening",true);
        answer3.put("listen",false);
        answer3.put("listened",false);
        questions.put("She's been _____ to music all day.",answer3);

        HashMap<String,Boolean> answer4 = new HashMap<>();
        answer4.put("bought",true);
        answer4.put("buying",false);
        answer4.put("buys",false);
        questions.put("When was the last time you _____ a new shirt?",answer4);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("play",true);
        answer5.put("played",false);
        answer5.put("playing",false);
        questions.put("Do you still _____ tennis on Sunday?",answer5);

        HashMap<String,Boolean> answer6 = new HashMap<>();
        answer6.put("talking",true);
        answer6.put("talk",false);
        answer6.put("talked",false);
        questions.put("Stop _____ and listen to me.",answer6);

        HashMap<String,Boolean> answer7 = new HashMap<>();
        answer7.put("painted",true);
        answer7.put("painting",false);
        answer7.put("paint",false);
        questions.put("I don't know who _____ this wall.",answer7);

        HashMap<String,Boolean> answer8 = new HashMap<>();
        answer8.put("snows",true);
        answer8.put("snowing",false);
        answer8.put("snow",false);
        questions.put("It never _____ here in winter.",answer8);

        HashMap<String,Boolean> answer9 = new HashMap<>();
        answer9.put("prefers",true);
        answer9.put("preferring",false);
        answer9.put("prefer",false);
        questions.put("He _____ jazz to pop music.",answer9);

        HashMap<String,Boolean> answer10 = new HashMap<>();
        answer10.put("are delivered",true);
        answer10.put("is deliver",false);
        answer10.put("had delivered",false);
        questions.put("Those newspapers _____ in a big truck.",answer10);

        HashMap<String,Boolean> answer11 = new HashMap<>();
        answer11.put("bought",true);
        answer11.put("buyed",false);
        answer11.put("buys",false);
        questions.put("We went shopping and I _____ a new pair of jeans..",answer11);

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getRandomMultiple(Context context, String subject, int SIZE){
        Map<String,Map<String,Boolean>> questionsMap = new HashMap<>();
        Map<String, Map<String, Boolean>> originalQuestion;

        if (subject.equals(context.getString(R.string.multiple_stage_01))){
            originalQuestion = getMultipleStage1();
        }else if (subject.equals(context.getString(R.string.multiple_stage_02))){
            originalQuestion = getMultipleStage2();
        }else{
            originalQuestion = getMultipleStage3();
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
