package com.example.myomoshiroi.model;

public class UserMission {
    public int DailyEasyCount;
    public int Task01;
    public int Task02;
    public int Task03;
    public int Task04;

    public boolean DailyTask;
    public boolean LevelTask01;
    public boolean LevelTask02;
    public boolean LevelTask03;
    public boolean LevelTask04;

    public UserMission(int dailyEasyCount, int task01, int task02, int task03, int task04,
                       boolean dailyTask, boolean levelTask01, boolean levelTask02, boolean levelTask03, boolean levelTask04) {
        DailyEasyCount = dailyEasyCount;
        Task01 = task01;
        Task02 = task02;
        Task03 = task03;
        Task04 = task04;
        DailyTask = dailyTask;
        LevelTask01 = levelTask01;
        LevelTask02 = levelTask02;
        LevelTask03 = levelTask03;
        LevelTask04 = levelTask04;
    }
}
