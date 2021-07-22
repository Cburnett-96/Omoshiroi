package com.example.myomoshiroi.other;

public class Attempt {
    private final int correct;
    private final int incorrect;
    private final int earned;
    private final int ocoin;

    public Attempt(int correct, int incorrect, int earned, int ocoin) {
        this.correct = correct;
        this.incorrect = incorrect;
        this.earned = earned;
        this.ocoin = ocoin;
    }
    public int getCorrect() {
        return correct;
    }
    public int getIncorrect() {
        return incorrect;
    }
    public int getEarned() {
        return earned;
    }
    public int getOcoin() {
        return ocoin;
    }
}
