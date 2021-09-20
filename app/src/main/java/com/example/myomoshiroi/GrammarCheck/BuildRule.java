package com.example.myomoshiroi.GrammarCheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.myomoshiroi.SpellSearch.Dictionary;

public class BuildRule {
    public Set<String> samples;
    public Set<String> RuleSet;
    public int n;
    public Counter NgramCount;
    public double trainNum;
    public Dictionary root;

    class Counter
    {
        public int depth;
        public HashMap<String, Counter> map;
        public double count;
        public double gtcount;
        public Counter(int depth)
        {
            this.depth = depth;
            if (depth == 0) {
                this.map = null;
                this.count = 0.0;
            } else {
                // We are not a leaf node, set up child node link hash.
                this.map = new HashMap<String, Counter>();
            }
        }

        public void insert(String[] ngram)
        {

            if (depth == 1) {
                count++;
            }
            if (depth == 0) {
                count++;
                return;
            }

            Counter next;
            if (map.containsKey(ngram[ngram.length-depth])) {
                next = map.get(ngram[ngram.length-depth]);
            } else {
                next = new Counter(depth-1);
                map.put(ngram[ngram.length-depth], next);
            }
            next.insert(ngram);
            return;
        }

        public double count(String[] ngram)
        {
            if (depth == 0) {
                return count;
            }
            return map.containsKey(ngram[ngram.length-depth])?map.get(ngram[ngram.length-depth]).count(ngram) : 0.0;
        }

        public double depth1Count(String[] ngram)
        {
            if (depth == 1) {
                return count;
            }

            return map.containsKey(ngram[ngram.length-depth] ) ?map.get(ngram[ngram.length-depth]).depth1Count(ngram) : 0.0;
        }


    }

    public BuildRule(HashSet<String> samples, int n, Dictionary root)
    {
        this.samples = samples;
        this.n = n;
        this.NgramCount = new Counter(n);
        this.RuleSet = new HashSet<String>();
        this.root = root;
        this.trainNum = 0;
    }


    public void train()
    {
        String regexp = "('?\\w+|\\p{Punct}\\.\\,)";
        Pattern pattern = Pattern.compile(regexp);
        for (String sample : samples) {
            ArrayList<String> Rules = new ArrayList<String>(); // local variable to record rules of n words
            Matcher matcher = pattern.matcher(sample);

            while (matcher.find()) {
                String match = matcher.group();
                if(root.search(match)){
                    Rules.add(root.tagger);
                    RuleSet.add(root.tagger);
                }
                else{
                    Rules.add("LS");
                    RuleSet.add("LS");
                }
            }
            String[] nGram = new String[n];
            for (int i = 0; i < n; i++) {
                nGram[i] = "<S>"; // start symbol
            }
            for (String word : Rules) {

                for (int i = 0; i < n-1; i++) {
                    nGram[i] = nGram[i+1];
                }
                nGram[n-1] = word;
                trainNum += 1;
                NgramCount.insert(nGram);

            }
        }

    }

    public double addOne(String[] ngrams){
        return (NgramCount.count(ngrams) + 1.0) / (NgramCount.depth1Count(ngrams) + RuleSet.size());
    }

    public ArrayList<Double> getCount(String test)
    {
        ArrayList<Double> Appearance = new ArrayList<Double>();
        ArrayList<Double> Probabilities = new ArrayList<Double>();
        String[] samples = test.split("[,]");
        String regexp = "('?\\w+|\\p{Punct}\\.)";
        Pattern pattern = Pattern.compile(regexp);
        String[] nGram = new String[n];

        for (String sample : samples) {
            Matcher matcher = pattern.matcher(sample.toLowerCase());

            for (int i = 0; i < n; i++) {
                nGram[i] = "<S>";
            }

            while (matcher.find()) {
                String match = matcher.group();
                //System.out.println(match);
                String property = "";
                if(root.search(match)){
                    property = root.tagger;
                }
                for (int i = 0; i < n-1; i++) {
                    nGram[i] = nGram[i+1];
                }

                nGram[n-1] = property;
                System.out.println("NGRAMS: "+nGram[0] + "  " +nGram[1] +"  " +  nGram[2] +"  "+ NgramCount.count(nGram));
                Appearance.add(NgramCount.count(nGram));
                Probabilities.add(addOne(nGram));
                //double a = (ngc.count(words) + 1.0) / (ngc.deptch1Count(words) + RuleSet.size());
            }
        }
        System.out.println("Probabilities: "+Probabilities);
        return Appearance;
    }
}
