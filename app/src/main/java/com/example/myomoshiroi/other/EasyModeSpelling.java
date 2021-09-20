package com.example.myomoshiroi.other;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EasyModeSpelling {
    public static Map<String, Map<String,Boolean>> getSpellingStage1(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("courteous",true);
        answer1.put("cuorteous",false);
        answer1.put("courteuos",false);
        questions.put("courteous",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("diligent",true);
        answer2.put("deligent",false);
        answer2.put("dilegent",false);
        questions.put("diligent",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("humorous",true);
        answer3.put("homurous",false);
        answer3.put("humoruos",false);
        questions.put("humorous",answer3);

        HashMap<String,Boolean> answer4 = new HashMap<>();
        answer4.put("gregarious",true);
        answer4.put("grigerious",false);
        answer4.put("gregareuos",false);
        questions.put("gregarious",answer4);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("versatile",true);
        answer5.put("virsetile",false);
        answer5.put("versitale",false);
        questions.put("versatile",answer5);

        HashMap<String,Boolean> answer6 = new HashMap<>();
        answer6.put("compassionate",true);
        answer6.put("cumpassionite",false);
        answer6.put("compassionati",false);
        questions.put("compassionate",answer6);

        HashMap<String,Boolean> answer7 = new HashMap<>();
        answer7.put("sequence",true);
        answer7.put("siquence",false);
        answer7.put("sequince",false);
        questions.put("sequence",answer7);

        HashMap<String,Boolean> answer8 = new HashMap<>();
        answer8.put("adventurous",true);
        answer8.put("advinturous",false);
        answer8.put("adventorous",false);
        questions.put("adventurous",answer8);

        HashMap<String,Boolean> answer9 = new HashMap<>();
        answer9.put("easygoing",true);
        answer9.put("easy-going",false);
        answer9.put("easy-gowing",false);
        questions.put("easygoing",answer9);

        HashMap<String,Boolean> answer10 = new HashMap<>();
        answer10.put("Handkerchief",true);
        answer10.put("Handkarchief",false);
        answer10.put("Handkirchief",false);
        questions.put("Handkerchief",answer10);

        HashMap<String,Boolean> answer11 = new HashMap<>();
        answer11.put("Pronunciation",true);
        answer11.put("Pronanciation",false);
        answer11.put("Pronunsiation",false);
        questions.put("Pronunciation",answer11);

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getRandomSpelling(Context context, String subject, int SIZE){
        Map<String,Map<String,Boolean>> questionsMap = new HashMap<>();
        Map<String, Map<String, Boolean>> originalQuestion;

        //if (subject.equals(context.getString(R.string.multiple_stage_01))){
        originalQuestion = getSpellingStage1();
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
