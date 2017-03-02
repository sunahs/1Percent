package com.sumus.onepercent.SQLite;

        import java.util.Arrays;

/**
 * Created by SUNAH on 2016-12-01.
 */

public class VoteObject {


    /* 투표 결과 객체 */
    String vote_date ="";
    String vote_question ="";
    String vote_ex1 ="", vote_ex2="", vote_ex3="", vote_ex4="";
    String vote_count1="0", vote_count2="0", vote_count3="0",vote_count4="0";
    String vote_total="0", vote_prize_total="0";
    String request_state="0",todayresult_state="0";

    public VoteObject(String vote_date, String vote_question, String vote_ex1, String vote_ex2, String vote_ex3, String vote_ex4)
    {
        this.vote_date = vote_date;
        this.vote_question = vote_question;
        this.vote_ex1 = vote_ex1;
        this.vote_ex2 = vote_ex2;
        this.vote_ex3 = vote_ex3;
        this.vote_ex4 = vote_ex4;
    }

    public VoteObject(String vote_count1, String vote_count2, String vote_count3, String vote_count4, String vote_total, String vote_prize_total, String request_state, String todayresult_state)
    {
        this.vote_count1 = vote_count1;
        this.vote_count2 = vote_count2;
        this.vote_count3 = vote_count3;
        this.vote_count4 = vote_count4;
        this.vote_total = vote_total;
        this.vote_prize_total  = vote_prize_total;
        this.request_state = request_state;
        this.todayresult_state = todayresult_state;
    }

    public VoteObject(String vote_date, String vote_question, String vote_ex1, String vote_ex2, String vote_ex3, String vote_ex4, String vote_count1, String vote_count2, String vote_count3, String vote_count4, String vote_total, String vote_prize_total, String request_state, String todayresult_state)
    {
        this.vote_date = vote_date;
        this.vote_question = vote_question;
        this.vote_ex1  = vote_ex1;
        this.vote_ex2  = vote_ex2;
        this.vote_ex3  = vote_ex3;
        this.vote_ex4  = vote_ex4;
        this.vote_count1  = vote_count1;
        this.vote_count2 = vote_count2;
        this.vote_count3  = vote_count3;
        this.vote_count4  = vote_count4;
        this.vote_total = vote_total;
        this.vote_prize_total  = vote_prize_total;
        this.request_state  = request_state;
        this.todayresult_state  = todayresult_state;
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
        return "VoteObject{"+
                "vote_date='" + vote_date + '\'' +
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


