package com.example.myomoshiroi.SpellSearch;

import java.util.HashSet;
import java.util.Set;

public class LevDistance {
    public Set<String> getEdits(String word) {
        Set<String> edits = new HashSet<String>();
        int wordLen = word.length();

        // Swapping i with i+1
        for (int i = 1; i < wordLen - 1; i++) {
            edits.add(word.substring(0, i) + word.charAt(i + 1)
                    + word.charAt(i) + word.substring(i + 2));
        }

        // deleting one char, skipping i
        for (int i = 0; i < wordLen; i++) {
            edits.add(word.substring(0, i) + word.substring(i + 1));
        }

        // inserting one char
        for (int i = 0; i < wordLen + 1; i++) {
            for (char j = 'a'; j <= 'z'; j++)
                edits.add(word.substring(0, i) + j + word.substring(i));
        }

        // replacing one char
        for (int i = 0; i < wordLen; i++) {
            for (char j = 'a'; j <= 'z'; j++)
                edits.add(word.substring(0, i) + j + word.substring(i + 1));
        }

        return edits;
    }
    public static int computeDistance(String word1, String word2){
        int[][] dpup = new int[word1.length()+1][word2.length()+1];
        for (int ii = 0; ii <= word1.length(); ii++) {
            dpup[ii][0] = ii;
        }

        for (int jj = 0; jj <= word2.length(); jj++) {
            dpup[0][jj] = jj;
        }

        for (int ii = 0; ii < word1.length(); ii++) {
            char c1 = word1.charAt(ii);
            for (int jj = 0; jj < word2.length(); jj++) {
                char c2 = word2.charAt(jj);
                if (c1 == c2) {
                    dpup[ii + 1][jj + 1] = dpup[ii][jj];
                } else {
                    dpup[ii + 1][jj + 1] = Math.min(dpup[ii][jj]+1, Math.min(dpup[ii][jj+1]+1, dpup[ii+1][jj]+1));
                }
                if (ii > 1 && jj > 1 && word1.charAt(ii) == word2.charAt(jj-1) && word1.charAt(ii-1) == word2.charAt(jj) )
                    dpup[ii][jj] = Math.min(dpup[ii][jj], dpup[ii-2][jj-2] + c1 == c2?0:1) ; // transposition
            }
        }
        return dpup[word1.length()][word2.length()];
    }


    public Set<String> findCorrection(Set<String> words, String pattern, int maxError) {
        Set<String> acceptedWords = new HashSet<String>();

        for(String word : words) {
            if(computeDistance(word, pattern) <= maxError) {
                acceptedWords.add(word);
            }
        }

        return acceptedWords;
    }
}

