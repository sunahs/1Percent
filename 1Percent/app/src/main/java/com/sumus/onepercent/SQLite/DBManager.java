package com.sumus.onepercent.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

//DB를 총괄관리
public class DBManager {

    private static final String SD_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    private final String ROOT_DIR = SD_PATH + "/Onepercent/";

    // DB관련 상수 선언
    private static final String dbName = "Onepercent.db";
    private static final String voteTable = "VoteTable";
    private static final String prizeTable = "PrizeTable";
    private static final String myTable = "MyTable";
    public static final int dbVersion = 1;

    // DB관련 객체 선언
    private OpenHelper opener; // DB opener
    private SQLiteDatabase db; // DB controller

    // 부가적인 객체들
    private Context context;

    // 생성자
    public DBManager(Context context) {
        this.context = context;
        this.opener = new OpenHelper(context, ROOT_DIR+dbName, null, dbVersion);
        Log.d("SUN","ROOT_DIR+dbName : "+ ROOT_DIR+dbName);
        db = opener.getWritableDatabase();
    }

    // Opener of DB and Table
    private class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, null, version);
            // TODO Auto-generated constructor stub
        }

        // 생성된 DB가 없을 경우에 한번만 호출됨
        @Override
        public void onCreate(SQLiteDatabase arg0) {

            String createSql1 = "create table " + voteTable + " (vote_date text not null, vote_question text, vote_ex1 text, vote_ex2 text, vote_ex3 text, vote_ex4 text, " +
                                 "vote_count1 text, vote_count2 text, vote_count3 text, vote_count4 text," +
                                "vote_total text, vote_prize_total text, request_state text, todayresult_state text, primary key(vote_date));";
            String createSql2 = "create table " + prizeTable + " (prize_date text not null,prize_people text, prize_gift text, prize_img text , primary key(prize_date));";
            String createSql3 = "create table " + myTable + " (my_date text not null, my_select_number text, my_select_value text, primary key(my_date));";

            arg0.execSQL(createSql1);
            arg0.execSQL(createSql2);
            arg0.execSQL(createSql3);

        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
        }
    }

    /**************************************** vote Table **********************************************/
    public void insertVote(VoteObject info)  // 삽입
    {
        ContentValues values = new ContentValues();
        values.put("vote_date", info.getVote_date());
        values.put("vote_question", info.getVote_question());
        values.put("vote_ex1", info.getVote_ex(1));
        values.put("vote_ex2", info.getVote_ex(2));
        values.put("vote_ex3", info.getVote_ex(3));
        values.put("vote_ex4", info.getVote_ex(4));
        values.put("vote_count1", info.getVote_count(1));
        values.put("vote_count2", info.getVote_count(2));
        values.put("vote_count3", info.getVote_count(3));
        values.put("vote_count4", info.getVote_count(4));
        values.put("vote_total", info.getVote_total());
        values.put("vote_prize_total", info.getVote_prize_total());
        values.put("request_state", info.getRequest_state());
        values.put("todayresult_state", info.getTodayresult_state());
        try{
            db.insert(voteTable, null, values);
            Log.d("SUN","insertVote : "+values.toString());
        }catch(Exception e) {
        }
    }

    public void insertVote(String date, VoteObject info)  // 업데이트
    {
        ContentValues values = new ContentValues();
        values.put("vote_count1",info.getVote_count(1));
        values.put("vote_count2",info.getVote_count(2));
        values.put("vote_count3",info.getVote_count(3));
        values.put("vote_count4",info.getVote_count(4));
        values.put("vote_total",info.getVote_total());
        values.put("vote_prize_total",info.getVote_prize_total());
        values.put("request_state",info.getRequest_state());
        values.put("todayresult_state",info.getTodayresult_state());
        try{
            db.update(voteTable,values,"vote_date=?",new String[]{String.valueOf(date)});
//            db.close();
            Log.d("SUN","insertVote date : "+values.toString());
        }catch(Exception e) {
        }
    }

    public String selectTodayVoteResult(String vote_date) // 조회
    {
        String sql = "select * from " + voteTable + " where vote_date like '"+vote_date+"';";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();
        String todayresult_state="";
        while (!results.isAfterLast()) {
            todayresult_state = results.getString(13);
            Log.d("SUN","todayresult_state : "+todayresult_state);
            results.moveToNext();
        }
        results.close();
        return todayresult_state;
    }


    public VoteObject selectVote(String vote_date) // 조회
    {
        String sql = "select * from " + voteTable + " where vote_date like '"+vote_date+"';";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();
        VoteObject info = null;
        while (!results.isAfterLast()) {
            info = new VoteObject(results.getString(0), results.getString(1),results.getString(2),results.getString(3),results.getString(4),results.getString(5),results.getString(6),results.getString(7),results.getString(8),results.getString(9),results.getString(10),results.getString(11),results.getString(12),results.getString(13));
            Log.d("SUN","selectVote : "+info.toString());
            results.moveToNext();
        }
        results.close();
        return info;
    }

    /**************************************** prize Table **********************************************/

    public void insertPrize(PrizeObject info)  // 삽입
    {
        ContentValues values = new ContentValues();
        values.put("prize_date",info.getPrize_date());
        values.put("prize_people",info.getPrize_people());
        values.put("prize_gift",info.getPrize_gift());
        values.put("prize_img",info.getPrize_img());

        try{
            db.insert(prizeTable, null, values);
//            db.close();
            Log.d("SUN","insertPrize : "+values.toString());
        }catch(Exception e) {
        }
    }

    public void insertPrize(String date, PrizeObject info)  // 업데이트
    {
        ContentValues values = new ContentValues();
        values.put("prize_people",info.getPrize_people());
        try{
            db.update(prizeTable,values,"vote_date=?",new String[]{String.valueOf(date)});
//            db.close();
            Log.d("SUN","insertPrize : "+values.toString());
        }catch(Exception e) {
        }
    }

    public PrizeObject selectPrize(String prize_date) // 조회
    {
        String sql = "select * from " + prizeTable + " where prize_date like '%"+prize_date+"%';";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();
        PrizeObject info = null;
        while (!results.isAfterLast()) {
            info = new PrizeObject(results.getString(0),results.getString(2),results.getString(3), results.getString(1));
            Log.d("SUN","selectPrize : "+info.toString());
            results.moveToNext();
        }
        results.close();
        return info;
    }

    /**************************************** my Table **********************************************/
    public void insertMy(MyObject info)  // 삽입
    {
        String sql = "insert into " + myTable + " values(?,?,?);";
        try{
            db.execSQL(sql,info.getArray());
            Log.d("SUN","insertMy : "+info.toString());
        }catch(Exception e) {
        }
    }

    public MyObject selectMy(String my_date) // 조회
    {
            String sql = "select * from " + myTable + " where my_date like '%" + my_date + "%';";
            Cursor results = db.rawQuery(sql, null);

            results.moveToFirst();
            MyObject info = null;
            while (!results.isAfterLast()) {
                info = new MyObject(results.getString(0), results.getString(1), results.getString(2));
                Log.d("SUN", "selectMy : " + info.toString());
                results.moveToNext();
            }
            results.close();
            return info;

}

}