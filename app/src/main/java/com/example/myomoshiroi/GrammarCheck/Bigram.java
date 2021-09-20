package com.example.myomoshiroi.GrammarCheck;

import android.os.Build;

import java.util.Objects;
import java.util.regex.Pattern;


import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Bigram
{
    public HashSet<String> samples;
    public HashSet<String> wordSet;
    public HashMap<String,  HashMap<String, Double>> BigramCount;
    public HashMap<Double, Double> BigramCountMap;
    public HashMap<String, Double> wordCount;
    public int trainNum = 0;
    public int maxCount = 0;
    public boolean goodTuringCountsAvailable = false;
    public boolean WrongUsage = false;
    public boolean Suspicious = false;
    public ArrayList<ArrayList<Integer>> wrongIndex;
    public ArrayList<ArrayList<Integer>>  suspicious;
    public Bigram(HashSet<String> samples)
    {
        this.samples = samples;
        this.wordSet = new HashSet<String>();
        this.BigramCount = new HashMap<>();
        this.wordCount = new HashMap<>();
        this.BigramCountMap = new HashMap<>();
        this.wrongIndex = new ArrayList<>();
        this.suspicious = new ArrayList<>();
    }
    public void train()
    {
        String regexp = "('?\\w+|\\p{Punct}\\.\\,)";
        Pattern pattern = Pattern.compile(regexp);
        for (String sample : samples) {
            Matcher matcher = pattern.matcher(sample.toLowerCase());
            String pre = "<S>";
            while (matcher.find()) {
                String match = matcher.group();
                wordSet.add(match);
                trainNum += 1;
                if (wordCount.containsKey(pre)) {
                    wordCount.put(pre, wordCount.get(pre)+1.0);
                    maxCount = (int) Math.max(maxCount, wordCount.get(pre)+1.0);
                }
                else
                    wordCount.put(pre, 1.0);

                HashMap<String, Double> innerCounts;
                if (BigramCount.containsKey(pre)) {
                    innerCounts = BigramCount.get(pre);
                } else {
                    innerCounts = new HashMap<String, Double>();
                    BigramCount.put(pre, innerCounts);
                }

                double count = 0.0;
                if (innerCounts.containsKey(match)) {
                    count = innerCounts.get(match);
                    BigramCountMap.put(count, BigramCountMap.get(count) - 1.0);
                }
                innerCounts.put(match, count+1.0);

                BigramCountMap.put(count+1.0, BigramCountMap.getOrDefault(count+1.0, 0.0) + 1.0);

                pre = match;
            }
        }
    }

    public double count(String word1, String word2)
    {
        if (BigramCount.containsKey(word1) && BigramCount.get(word1).containsKey(word2)) {
            return BigramCount.get(word1).get(word2);
        }
        return 0.0;
    }


    public double getProbability(String word1, String word2)
    {
        if (BigramCount.containsKey(word1)) {
            if (BigramCount.get(word1).containsKey(word2)) {
                return BigramCount.get(word1).get(word2) / wordCount.getOrDefault(word1, 0.0);
            } else {
                return 0.0;
            }
        } else {
            return 0.0;
        }
    }
    public double goodturing(String word1, String word2, double k){

        double count = count(word1,word2);
        System.out.println(word1+ "   " + word2+ "   " + count);
        //System.out.println(BigramCountMap.getOrDefault(count + 1.0 , 0.0));
        if(count  <= k && count >= 1){
            //return (count + 1.0) * (BigramCountMap.getOrDefault(count + 1.0,0.0)) / (BigramCountMap.getOrDefault(count, 0.0));
            double turing = (count + 1.0) * (BigramCountMap.getOrDefault(count + 1.0, 0.0)) / (BigramCountMap.getOrDefault(count, 0.0));
            double turing2 = count  * (k + 1.0) * (BigramCountMap.get(count + 1.0))  / (BigramCountMap.get(1.0)) ;
            double turing3 = 1.0 - (k+1.0) * (BigramCountMap.get(count + 1.0))  / (BigramCountMap.get(1.0)) ;
            return (turing - turing2) / turing3 ;
        }
        return count / wordCount.get(word1);


        //return count > 0 ? (count / wordCount.getOrDefault(word1, 0.0)) : (BigramCountMap.getOrDefault(1.0, 0.0) / trainNum) ;
    }

    public void GoodTuring()
    {

        for (String word1 : BigramCount.keySet()) {
            HashMap<String, Double> innerMap = BigramCount.get(word1);
            double Count = 0.0;
            for (String word2 : innerMap.keySet()) {
                double count = innerMap.getOrDefault(word2, 0.0);
                if (!BigramCountMap.containsKey(count+1.0)) {
                    BigramCountMap.put(count+1.0 , 0.0);
                }
                double TuringCount = (count + 1.0)*(BigramCountMap.get(count+1.0))/(BigramCountMap.get(count));
                innerMap.put(word2, TuringCount);
                Count += TuringCount;
            }
            wordCount.put(word1, Count);
        }
    }

    public double perplexity(String test) {
        float product = 1;
        int wordCountId = 0;
        //System.out.println(test);
        ArrayList<Double> products = new ArrayList<Double>();
        String[] samples = test.split("[,]");
        String regexp = "('?\\w+|\\p{Punct}\\.)";
        Pattern pattern = Pattern.compile(regexp);
        GoodTuring();
        for(String sample : samples){
            Matcher matcher = pattern.matcher(sample.toLowerCase());
            String pre = "<S>";
            while (matcher.find()) {
                ArrayList<Integer> inner= new ArrayList<>();
                String match = matcher.group();
                double count = count(pre,match);
                if(count(pre,match) == 0){
                    WrongUsage = true;
                    inner.add(wordCountId-1);
                    inner.add(wordCountId);

                    wrongIndex.add(new ArrayList<Integer>(inner));
                }
                else if(count(pre,match) < 7){
                    inner= new ArrayList<>();
                    Suspicious = true;
                    inner.add(wordCountId-1);
                    inner.add(wordCountId);
                    suspicious.add(new ArrayList<Integer>(inner));
                }
                products.add(goodturing(pre, match, 3.0));
                wordCountId++;
                pre = match;
            }
        }
        if(WrongUsage)
            return 200.0;

        double power = 1.0 / (double)wordCountId;

        for (Double num : products) {
            product *= Math.pow(num, power);
        }

        System.out.println(products);
        return (double)(1.0 / product);
    }


    public double addOne(String word1, String word2)
    {
        return (count(word1, word2) + 1.0)*(wordCount.getOrDefault(word1, 0.0)) / (wordCount.getOrDefault(word1, 0.0) + wordSet.size());
    }
}