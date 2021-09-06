package com.example.myomoshiroi.model;

public class UserData {
    public String Fullname,LRN,EmailId,Gender,Avatar;
    public int Level, Point, Ocoin, TotalOcoins;

    public UserData(String fullname, String lrn, String emailId, String gender, int level, int point,
                    int coin, String avatar, int totalocoins) {
        Fullname = fullname;
        LRN = lrn;
        EmailId = emailId;
        Gender = gender;
        Level = level;
        Point = point;
        Ocoin= coin;
        Avatar = avatar;
        TotalOcoins = totalocoins;
    }
}
