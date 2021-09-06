package com.example.myomoshiroi.model;

public class UserRanking {
    public String fullname;
    public String avatar;
    int point;

    public UserRanking(String fullName, String avataR, int poinT)
    {
        fullname = fullName;
        avatar = avataR;
        point = poinT;
    }
    public UserRanking(){

    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }
}
