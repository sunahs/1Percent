package com.sumus.onepercent.SQLite;

import java.util.Arrays;

/**
 * Created by SUNAH on 2016-12-01.
 */

public class VoteObject {
    /* 투표 결과 객체 */
    String dataArray[] = new String[14];

    String vote_date;
    String vote_question;
    String vote_ex1, vote_ex2, vote_ex3, vote_ex4;
    String vote_count1;
    String vote_count2;
    String vote_count3;
    String vote_count4;
    String vote_total, vote_prize_total;
    String request_state,todayresult_state;

    public VoteObject(String vote_date, String vote_question, String vote_ex1, String vote_ex2, String vote_ex3, String vote_ex4, String vote_count1, String vote_count2, String vote_count3, String vote_count4, String vote_total, String vote_prize_total, String request_state, String todayresult_state)
    {
        this.vote_date = dataArray[0] = vote_date;
        this.vote_question = dataArray[1] = vote_question;
        this.vote_ex1 = dataArray[2] = vote_ex1;
        this.vote_ex2 = dataArray[3] = vote_ex2;
        this.vote_ex3 = dataArray[4] = vote_ex3;
        this.vote_ex4 = dataArray[5] = vote_ex4;
        this.vote_count1 = dataArray[6] = vote_count1;
        this.vote_count2 = dataArray[7] = vote_count2;
        this.vote_count3 = dataArray[8] = vote_count3;
        this.vote_count4 = dataArray[9] = vote_count4;
        this.vote_total = dataArray[10] = vote_total;
        this.vote_prize_total = dataArray[11] = vote_prize_total;
        this.request_state = dataArray[12] = request_state;
        this.todayresult_state = dataArray[13] = todayresult_state;
    }

    public String getVote_ex(int num){
        String result="";
        switch (num){
            case 1:
                result = vote_ex1;
                break;
            case 2:
                result = vote_ex2;
                break;
            case 3:
                result = vote_ex3;
                break;
            case 4:
                result = vote_ex4;
                break;
        }
        return result;
    }

    public String  getVote_count(int num){
       String result="";
        switch (num){
            case 1:
                result = vote_count1;
                break;
            case 2:
                result = vote_count2;
                break;
            case 3:
                result = vote_count3;
                break;
            case 4:
                result = vote_count4;
                break;
        }
        return result;
    }

    public String[] getArray(){
        return dataArray;
    }

    public String[] getDataArray() {
        return dataArray;
    }

    public String getVote_date() {
        return vote_date;
    }

    public String getVote_question() {
        return vote_question;
    }

    public String getVote_total() {
        return vote_total;
    }

    public String getVote_prize_total() {
        return vote_prize_total;
    }

    public String getRequest_state() {
        return request_state;
    }

    public String getTodayresult_state() {
        return todayresult_state;
    }

    @Override
    public String toString() {
        return "VoteObject{" +
                "dataArray=" + Arrays.toString(dataArray) +
                ", vote_date='" + vote_date + '\'' +
                ", vote_question='" + vote_question + '\'' +
                ", vote_ex1='" + vote_ex1 + '\'' +
                ", vote_ex2='" + vote_ex2 + '\'' +
                ", vote_ex3='" + vote_ex3 + '\'' +
                ", vote_ex4='" + vote_ex4 + '\'' +
                ", vote_count1='" + vote_count1 + '\'' +
                ", vote_count2='" + vote_count2 + '\'' +
                ", vote_count3='" + vote_count3 + '\'' +
                ", vote_count4='" + vote_count4 + '\'' +
                ", vote_total='" + vote_total + '\'' +
                ", vote_prize_total='" + vote_prize_total + '\'' +
                ", request_state='" + request_state + '\'' +
                ", todayresult_state='" + todayresult_state + '\'' +
                '}';
    }
}

