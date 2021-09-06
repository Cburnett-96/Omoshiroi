package com.example.myomoshiroi.model;

public class UserRankingTwoThree {
    public String fullname;
    public String avatar;
    int point;

    public UserRankingTwoThree(String fullName, String avataR, int poinT)
    {
        fullname = fullName;
        avatar = avataR;
        point = poinT;
    }
    public UserRankingTwoThree(){

    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }
}
