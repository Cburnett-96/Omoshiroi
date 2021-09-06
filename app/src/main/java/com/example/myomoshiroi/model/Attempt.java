package com.example.myomoshiroi.model;

public class Attempt {
    private final long createdTime;
    private final int correct;
    private final int incorrect;
    private final int earned;
    private final int ocoin;

    public Attempt(long createdTime, int correct, int incorrect, int earned, int ocoin) {
        this.createdTime = createdTime;
        this.correct = correct;
        this.incorrect = incorrect;
        this.earned = earned;
        this.ocoin = ocoin;
    }
    public long getCreatedTime() {
        return createdTime;
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
