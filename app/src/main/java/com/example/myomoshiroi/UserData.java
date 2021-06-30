package com.example.myomoshiroi;

public class UserData {
    public String Fullname,LRN,EmailId,Gender;
    public int Level, Point, Ocoin;

    public UserData()
    {

    }
    public UserData(String fullname, String lrn, String emailId, String gender, int level, int point, int coin) {
        Fullname = fullname;
        LRN = lrn;
        EmailId = emailId;
        Gender = gender;
        Level = level;
        Point = point;
        Ocoin= coin;
    }
}
