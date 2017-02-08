package com.sumus.onepercent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;
import com.sumus.onepercent.Object.MySharedPreference;
import com.sumus.onepercent.SQLite.DBManager;
import com.sumus.onepercent.SQLite.MainObject;
import com.sumus.onepercent.SQLite.MyObject;
import com.sumus.onepercent.SQLite.PrizeObject;
import com.sumus.onepercent.SQLite.VoteObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import cz.msebera.android.httpclient.Header;

public class SplashActivity extends AppCompatActivity {
    public static Context mContext;
    MySharedPreference pref;
    public String today_YYYYMMDD;
    public String circld_day_YYYYMMDD;
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat df_cicle = new SimpleDateFormat("yyyy.MM.dd");

    DBManager manager;
    Handler mHandler;
    int data_load=0;
    boolean handlerFlag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mContext = this;
        pref = new MySharedPreference(mContext);
        manager = new DBManager(mContext);

        long nowdate = System.currentTimeMillis(); // 현재시간
        today_YYYYMMDD = df.format(nowdate);
        circld_day_YYYYMMDD = df_cicle.format(nowdate);
        Log.d("SUN", "SplashActivity # today_YYYYMMDD " + today_YYYYMMDD);

//        pref.setPreferences("app","visted",today_YYYYMMDD);
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();       // 2 초후 이미지를 닫아버림
            }
        }, 3000);


    }


}
