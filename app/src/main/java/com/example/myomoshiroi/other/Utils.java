package com.example.myomoshiroi.other;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Utils {

    public static Map<String,Map<String,Boolean>> getLiteratureQuestions(){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();

        HashMap<String,Boolean> answer1 = new HashMap<>();
        answer1.put("travel",true);
        answer1.put("voyage",false);
        answer1.put("journey",false);
        questions.put("Jenny likes her job but she hates the long ________ to work every day.",answer1);

        HashMap<String,Boolean> answer2 = new HashMap<>();
        answer2.put("vehicles ",true);
        answer2.put("traffic",false);
        answer2.put("transport",false);
        questions.put("It's a great place to live apart from the increasing volume of ________ that passes under my window every day.",answer2);

        HashMap<String,Boolean> answer3 = new HashMap<>();
        answer3.put("about",true);
        answer3.put("against",false);
        answer3.put("for",false);
        questions.put("Japhet is very angry ________ him boss's decision to sack several members of staff.",answer3);

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
        questions.put("I _______ with her.",answer6);

        HashMap<String,Boolean> answer7 = new HashMap<>();
        answer7.put("should be taken",true);
        answer7.put("should take",false);
        answer7.put("should be took",false);
        questions.put("This medicine on ______ time.",answer7);

        HashMap<String,Boolean> answer8 = new HashMap<>();
        answer8.put("are called",true);
        answer8.put("called",false);
        answer8.put("is called",false);
        questions.put("Wait here till you _______.",answer8);

        HashMap<String,Boolean> answer9 = new HashMap<>();
        answer9.put("should not be given",true);
        answer9.put("would give",false);
        answer9.put("should not give",false);
        questions.put("Children _______ undue importance.",answer9);

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

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getRandomLiterature(int SIZE){
        Map<String,Map<String,Boolean>> questionsMap = new HashMap<>();
        Map<String, Map<String, Boolean>> originalQuestion;

        originalQuestion = getLiteratureQuestions();

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
