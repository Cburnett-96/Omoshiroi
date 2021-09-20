package com.example.myomoshiroi.other;

import android.content.Context;

import com.example.myomoshiroi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EasyModeMatching {

    public static Map<String, Map<String,Boolean>> getMatchingStage1(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("courteous",true);
        answer1.put("sincere",false);
        answer1.put("charming",false);
        questions.put("Which word is closest in meaning to \"polite\"? ",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("diligent",true);
        answer2.put("pro-active",false);
        answer2.put("resourceful",false);
        questions.put("Which word is closest in meaning to \"hard-working\"?",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("humorous",true);
        answer3.put("convivial",false);
        answer3.put("plucky",false);
        questions.put("Which word is closest in meaning to \"funny\"?",answer3);

        HashMap<String,Boolean> answer4 = new HashMap<>();
        answer4.put("gregarious",true);
        answer4.put("amusing",false);
        answer4.put("diplomatic",false);
        questions.put("Which word is closest in meaning to \"sociable\"?",answer4);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("drug",true);
        answer5.put("remedy",false);
        answer5.put("vaccine",false);
        questions.put("Which word is closest in meaning to \"medicine\"?",answer5);

        HashMap<String,Boolean> answer6 = new HashMap<>();
        answer6.put("kids",true);
        answer6.put("babies",false);
        answer6.put("youngsters",false);
        questions.put("Which word is closest in meaning to \"children\"?",answer6);

        HashMap<String,Boolean> answer7 = new HashMap<>();
        answer7.put("sequence",true);
        answer7.put("arrangement",false);
        answer7.put("series",false);
        questions.put("Which word is closest in meaning to \"order\"?",answer7);

        HashMap<String,Boolean> answer8 = new HashMap<>();
        answer8.put("lecturer",true);
        answer8.put("reader",false);
        answer8.put("talker",false);
        questions.put("Which word is closest in meaning to \"speaker\"?",answer8);

        HashMap<String,Boolean> answer9 = new HashMap<>();
        answer9.put("justice",false);
        answer9.put("magistrate",true);
        answer9.put("court",false);
        questions.put("Which word is closest in meaning to \"judge\"?",answer9);

        HashMap<String,Boolean> answer10 = new HashMap<>();
        answer10.put("lecture",true);
        answer10.put("speech",false);
        answer10.put("presentation",false);
        questions.put("Which word is closest in meaning to \"talk\"?",answer10);

        HashMap<String,Boolean> answer11 = new HashMap<>();
        answer11.put("instrument",true);
        answer11.put("equipment",false);
        answer11.put("apparatus",false);
        questions.put("Which word is closest in meaning to \"tool\"?",answer11);

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getRandomMatching(Context context, String subject, int SIZE){
        Map<String,Map<String,Boolean>> questionsMap = new HashMap<>();
        Map<String, Map<String, Boolean>> originalQuestion;

        //if (subject.equals(context.getString(R.string.multiple_stage_01))){
            originalQuestion = getMatchingStage1();
        /*}else if (subject.equals(context.getString(R.string.multiple_stage_02))){
            originalQuestion = getMultipleStage2();
        }else{
            originalQuestion = getMultipleStage3();
        }*/

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
