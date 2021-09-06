package com.example.myomoshiroi.model;

public class UserRankingOne {
    public String fullname;
    public String avatar;
    int point;

    public UserRankingOne(String fullName, String avataR, int poinT)
    {
        fullname = fullName;
        avatar = avataR;
        point = poinT;
    }
    public UserRankingOne(){

    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }
}
