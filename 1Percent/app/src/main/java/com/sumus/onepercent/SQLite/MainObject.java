package com.sumus.onepercent.SQLite;

import java.util.Arrays;

/**
 * Created by SUNAH on 2017-01-22.
 */

public class MainObject {
    /* 오늘의 데이터 객체 */
    String dataArray[] = new String[8];

    String main_date, main_question;
    String main_ex1, main_ex2, main_ex3, main_ex4;
    String gift_name, gift_png;

    public MainObject(String main_date, String main_question, String main_ex1, String main_ex2, String main_ex3, String main_ex4, String gift_name, String gift_png) {
        this.main_date = dataArray[0] = main_date;
        this.main_question = dataArray[1]  = main_question;
        this.main_ex1 = dataArray[2]  = main_ex1;
        this.main_ex2 = dataArray[3]  = main_ex2;
        this.main_ex3 = dataArray[4]  = main_ex3;
        this.main_ex4 = dataArray[5] = main_ex4;
        this.gift_name = dataArray[6]  = gift_name;
        this.gift_png = dataArray[7]  = gift_png;
    }

    public String[] getArray() {
        return dataArray;
    }

    @Override
    public String toString() {
        return "MainObject{" +
                "dataArray=" + Arrays.toString(dataArray) +
                ", main_date='" + main_date + '\'' +
                ", main_question='" + main_question + '\'' +
                ", main_ex1='" + main_ex1 + '\'' +
                ", main_ex2='" + main_ex2 + '\'' +
                ", main_ex3='" + main_ex3 + '\'' +
                ", main_ex4='" + main_ex4 + '\'' +
                ", gift_name='" + gift_name + '\'' +
                ", gift_png='" + gift_png + '\'' +
                '}';
    }

    public String getMain_date() {
        return main_date;
    }

    public String getMain_question() {
        return main_question;
    }

    public String getMain_ex1() {
        return main_ex1;
    }

    public String getMain_ex2() {
        return main_ex2;
    }

    public String getMain_ex3() {
        return main_ex3;
    }

    public String getMain_ex4() {
        return main_ex4;
    }

    public String getGift_name() {
        return gift_name;
    }

    public String getGift_png() {
        return gift_png;
    }
}
