package com.sumus.onepercent.SQLite;

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
    private final String ROOT_DIR = SD_PATH + "/Letter/";

    // DB관련 상수 선언
    private static final String dbName = "Onpercent.db";
    private static final String voteTable = "VoteTable";
    private static final String prizeTable = "PrizeTable";
    private static final String myTable = "MyTable";
    private static final String mainTable = "MainTable";
    public static final int dbVersion = 1;

    // DB관련 객체 선언
    private OpenHelper opener; // DB opener
    private SQLiteDatabase db; // DB controller

    // 부가적인 객체들
    private Context context;

    // 생성자
    public DBManager(Context context) {
        this.context = context;
        this.opener = new OpenHelper(context, dbName, null, dbVersion);
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
            String createSql4 = "create table " + mainTable + "(main_date, main_question, main_ex1, main_ex2, main_ex3, main_ex4, gift_name, gift_png, primary key(main_date));";

            arg0.execSQL(createSql1);
            arg0.execSQL(createSql2);
            arg0.execSQL(createSql3);
            arg0.execSQL(createSql4);

        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
        }
    }

    /**************************************** vote Table **********************************************/
    public void insertVote(VoteObject info)  // 삽입
    {
        String sql = "insert into " + voteTable + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try{
            db.execSQL(sql,info.getArray());
            Log.d("SUN","insertVote : "+info.toString());
        }catch(Exception e) {
        }
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
        String sql = "insert into " + prizeTable + " values(?,?,?,?);";
        try{
            db.execSQL(sql,info.getArray());
            Log.d("SUN","insertPrize : "+info.toString());
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
            info = new PrizeObject(results.getString(0), results.getString(1),results.getString(2),results.getString(3));
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

    /**************************************** main Table **********************************************/
    public void insertMain(MainObject info)  // 삽입
    {
        String sql = "insert into " + mainTable + " values(?,?,?,?,?,?,?,?);";
        try{
            db.execSQL(sql,info.getArray());
            Log.d("SUN","insertMain : "+info.toString());
        }catch(Exception e) {
            Log.d("SUN","insertMain error : " + e.getMessage());
        }
    }

    public MainObject selectMain(String main_date) // 조회
    {
        String sql = "select * from " + mainTable + " where main_date like '%"+main_date+"%';";
        try {
            Cursor results = db.rawQuery(sql, null);

            results.moveToFirst();
            MainObject info = null;
            while (!results.isAfterLast()) {
                info = new MainObject(results.getString(0), results.getString(1), results.getString(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6), results.getString(7));
                Log.d("SUN", "selectMain : " + info.toString());
                results.moveToNext();
            }
            results.close();
            return info;
        }
        catch (Exception e){

        }
        return null;
    }

}