package com.example.myomoshiroi.other;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EasyModeTorF {
    public static Map<String, Map<String,Boolean>> getTorFStage1(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("TRUE",true);
        answer1.put("FALSE",false);
        questions.put("FOUNDED is the past tense of FOUND.",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("TRUE ",true);
        answer2.put("FALSE",false);
        questions.put("ANSWER can be used as a noun and a verb.",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("TRUE",true);
        answer3.put("FALSE",false);
        questions.put("EQUIVALENT TO is (more or less) the same as EQUAL TO.",answer3);

        HashMap<String,Boolean> answer4 = new HashMap<>();
        answer4.put("TRUE",true);
        answer4.put("FALSE",false);
        questions.put("The past tense of FIND is FOUND.",answer4);

        HashMap<String,Boolean> answer5 = new HashMap<>();
        answer5.put("FALSE",true);
        answer5.put("TRUE",false);
        questions.put("USED TO DOING and USED TO DO mean the same thing. ",answer5);

        HashMap<String,Boolean> answer6 = new HashMap<>();
        answer6.put("FALSE",true);
        answer6.put("TRUE",false);
        questions.put("BEGIN is the past tense of BEGUN.",answer6);

        HashMap<String,Boolean> answer7 = new HashMap<>();
        answer7.put("TRUE",true);
        answer7.put("FALSE",false);
        questions.put("SAW is the past tense of SEE.",answer7);

        HashMap<String,Boolean> answer8 = new HashMap<>();
        answer8.put("FALSE",true);
        answer8.put("TRUE",false);
        questions.put("The closest meaning of POLITE is SINCERE?",answer8);

        HashMap<String,Boolean> answer9 = new HashMap<>();
        answer9.put("FALSE",true);
        answer9.put("TRUE",false);
        questions.put("BY is not same pronunciation of BUY?",answer9);

        HashMap<String,Boolean> answer10 = new HashMap<>();
        answer10.put("FALSE",true);
        answer10.put("TRUE",false);
        questions.put("It is correct? \"This is to difficult for me.\"",answer10);

        HashMap<String,Boolean> answer11 = new HashMap<>();
        answer11.put("TRUE",true);
        answer11.put("FALSE",false);
        questions.put("The closest meaning of HARD WORKING is DILIGENT?.",answer11);

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getRandomTorF(Context context, String subject, int SIZE){
        Map<String,Map<String,Boolean>> questionsMap = new HashMap<>();
        Map<String, Map<String, Boolean>> originalQuestion;

        //if (subject.equals(context.getString(R.string.multiple_stage_01))){
        originalQuestion = getTorFStage1();
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
