package com.sumus.onepercent.SQLite;

import java.util.Arrays;

/**
 * Created by SUNAH on 2016-12-01.
 */

public class PrizeObject {
    /* 당첨결과 객체 */
    String prize_date="";
    String prize_people="";
    String prize_gift="";
    String prize_img="";

    public PrizeObject(String prize_date, String prize_people, String prize_gift, String prize_img) {
        this.prize_date  =prize_date;
        this.prize_people  = prize_people;
        this.prize_gift = prize_gift;
        this.prize_img = prize_img;
    }

    public PrizeObject( String prize_gift, String prize_img) {
        this.prize_gift = prize_gift;
        this.prize_img  =prize_img;
    }

    public String getPrize_date() {
        return prize_date;
    }

    public String getPrize_people() {
        return prize_people;
    }

    public String getPrize_gift() {
        return prize_gift;
    }

    public String getPrize_img() {
        return prize_img;
    }


    @Override
    public String toString() {
        return "PrizeObject{" +
                " prize_date='" + prize_date + '\'' +
                ", prize_peopLe='" + prize_people + '\'' +
                ", prize_gift='" + prize_gift + '\'' +
                ", prize_img='" + prize_img + '\'' +
                '}';
    }
}
