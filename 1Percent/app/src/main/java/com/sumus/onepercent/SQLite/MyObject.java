package com.sumus.onepercent.SQLite;

/**
 * Created by SUNAH on 2016-12-01.
 */

public class MyObject {
    /* 내 투표결과 객체 */
    String dataArray[] = new String[3];

    String my_date;
    String my_select_number;


    String my_select_value;

    public MyObject(String my_date, String my_select_number, String my_select_value) {
        this.my_date = dataArray[0] = my_date;
        this.my_select_number = dataArray[1] = my_select_number;
        this.my_select_value = dataArray[2] = my_select_value;
    }

    public String[] getArray() {
        return dataArray;
    }

    public String getMy_date() {
        return my_date;
    }

    public String getMy_select_number() {
        return my_select_number;
    }

    public String getMy_select_value() {
        return my_select_value;
    }

    @Override
    public String toString() {
        return "MyObject{" +
                "my_date='" + my_date + '\'' +
                ", my_select_number='" + my_select_number + '\'' +
                ", my_select_value='" + my_select_value + '\'' +
                '}';
    }
}
