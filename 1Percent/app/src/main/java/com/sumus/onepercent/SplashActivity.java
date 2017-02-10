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

        if( !pref.getPreferences("app","visted").equals("")) {
            getSinceVoteResult_Server(pref.getPreferences("app", "visted"));
        }
        pref.setPreferences("app", "visted", circld_day_YYYYMMDD);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();       // 2 초후 이미지를 닫아버림
            }
        }, 3000);


    }

    void getSinceVoteResult_Server(String vote_date) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("vote_date", vote_date);
        Log.d("SUN", "SplashActivity # getSinceVoteResult_Server() vote_date : "+ vote_date);
        client.get("http://onepercentserver.azurewebsites.net/OnePercentServer/voteResultSince.do",params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {    }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr = object.get("voteResultSince") + "";
                    JSONArray arr = new JSONArray(objStr);
                    for (int i = 0; i < arr.length(); i++) {

                        MySharedPreference pref = new MySharedPreference(mContext);

                        JSONObject obj = (JSONObject) arr.get(i);

                        String vote_date = (String) obj.get("vote_date");
                        String vote_question = (String) obj.get("vote_question");
                        String vote_ex[] = new String[4];
                        int vote_count[] = new int[4];
                        for (int z=0; z<4; z++)
                        {
                            vote_ex[z] =  (String) obj.get("ex"+(z+1)+"_value");
                            vote_count[z] = (int) obj.get("ex"+(z+1)+"_count");
                        }

                        int vote_total = (int)obj.get("total_count");
                        int vote_prize_total = (int)obj.get("prize_count");

                        manager.insertVote(new VoteObject(vote_date.replace(".",""),vote_question,vote_ex[0],vote_ex[1],vote_ex[2],vote_ex[3],vote_count[0]+"",vote_count[1]+"",vote_count[2]+"",vote_count[3]+"",vote_total+"",vote_prize_total+"","1","0"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("SUN", "e : " + e.toString());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "SplashActivity # getSinceVoteResult_Server # onFailure // statusCode : " + statusCode +  " , error : " + error.toString());
            }
            @Override
            public void onRetry(int retryNo) {  }
        });
    }
}
