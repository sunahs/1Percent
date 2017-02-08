package com.sumus.onepercent.SQLite;

import java.util.Arrays;

/**
 * Created by SUNAH on 2016-12-01.
 */

public class PrizeObject {
    /* 당첨결과 객체 */
    String dataArray[] = new String[4];

    String prize_date;
    String prize_peopLe;
    String prize_gift;
    String prize_img;

    public PrizeObject(String prize_date, String prize_peopLe, String prize_gift, String prize_img) {
        this.prize_date = dataArray[0] =prize_date;
        this.prize_peopLe  = dataArray[1] = prize_peopLe;
        this.prize_gift  = dataArray[2] = prize_gift;
        this.prize_img  = dataArray[3] = prize_img;
    }

    public String[] getArray(){
        return dataArray;
    }


    @Override
    public String toString() {
        return "PrizeObject{" +
                "dataArray=" + Arrays.toString(dataArray) +
                ", prize_date='" + prize_date + '\'' +
                ", prize_peopLe='" + prize_peopLe + '\'' +
                ", prize_gift='" + prize_gift + '\'' +
                ", prize_img='" + prize_img + '\'' +
                '}';
    }
}
