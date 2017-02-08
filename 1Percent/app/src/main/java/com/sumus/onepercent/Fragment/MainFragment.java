package com.sumus.onepercent.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.sumus.onepercent.FontBaseActvity;
import com.sumus.onepercent.JoinActivity;
import com.sumus.onepercent.MainActivity;
import com.sumus.onepercent.Object.MySharedPreference;
import com.sumus.onepercent.R;
import com.sumus.onepercent.SQLite.DBManager;
import com.sumus.onepercent.SQLite.MainObject;
import com.sumus.onepercent.SQLite.VoteObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import cz.msebera.android.httpclient.Header;

/**
 * Created by SUNAH on 2016-11-26.
 */

public class MainFragment extends Fragment implements View.OnClickListener{
    /*
        InitWidget() : 위젯 초기화
        IniteData() : 데이터 초기화
        TodayDate() : 오늘 날짜계산
        ClockSet() : Thread 이용한 타이머
        getMain_Server() : 서버 - 오늘의 데이터 get
        getImage_Server() : 서버 - 상품 이미지 get
        byteArrayToBitmap() : Byte를 Bitmap으로 변환
    */

    // frgment init data
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // fragment
    public View views;
    public Context mContext;
    public Activity mActivity;

    // 변수
    MySharedPreference pref;
    String today_YYYYMMDD, nowStr;
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

    // Widget
    TextView main_clockTv, main_timerTv, main_subTimerTv;
    TextView main_giftnameTv;
    Button main_infoBtn;
    ImageView main_giftImg;


    // Timer
    public TimerThread thread;
    public Boolean RunFlag = true;

    // DB
    DBManager manager;

    public MainFragment() {     }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.fragment_main, container, false);
        mContext = getContext();
        mActivity = getActivity();
        pref = new MySharedPreference(mContext);
        FontBaseActvity fontBaseActvity = new FontBaseActvity(); // 폰트 적용
        fontBaseActvity.setGlobalFont(views);

        InitWidget();
        return views;
    }

    void InitWidget(){
        long nowdate = System.currentTimeMillis(); // 현재시간
        today_YYYYMMDD = df.format(nowdate);
        manager =new DBManager(mActivity);

        main_giftnameTv = (TextView)views.findViewById(R.id.main_giftnameTv);

        main_clockTv = (TextView)views.findViewById(R.id.main_clockTv);
        main_clockTv.setPaintFlags(main_clockTv.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        main_timerTv= (TextView)views.findViewById(R.id.main_timerTv);
        main_subTimerTv= (TextView)views.findViewById(R.id.main_subTimerTv);
        main_infoBtn = (Button) views.findViewById(R.id.main_infoBtn);
        main_infoBtn.setOnClickListener(this);

        main_giftImg = (ImageView)views.findViewById(R.id.main_giftImg);
        IniteData();
    }

    void IniteData(){
        MainObject mainObject;
        if((mainObject = manager.selectMain("20161102"))==null)
        {
            Log.d("SUN", "MainFragmet # db null");
            getMain_Server();
        }
        else
        {
            try {
                Log.d("SUN","MainFragment # db not null~!!!");
//                MainObject mainObject =  manager.selectMain("20161102");
                main_giftnameTv.setText(mainObject.getGift_name());
                main_giftImg.setImageBitmap(byteArrayToBitmap(Base64.decode(pref.getPreferences("oneday","giftImg"), Base64.DEFAULT)));
            }
            catch (NullPointerException e){
                Log.d("SUN", "MainFragmet # DB is null");
            }
        }

        if( !pref.getPreferences("app","first").equals("no")){
            getAllVoteResult_Server();
            pref.setPreferences("app","first","no");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_infoBtn:
                Intent intent = new Intent(mActivity, JoinActivity.class);
                startActivity(intent);
                break;
        }
    }


    class TimerThread extends Thread{
        @Override
        public void run() {
            while(RunFlag){
                try {
                    TimerHandler.sendEmptyMessage(0);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    Handler TimerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            TodayDate();
            ClockSet();
        }
    };

    void TodayDate(){ // 오늘 날짜 확인
        long nowdate = System.currentTimeMillis(); // 현재시간
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        today_YYYYMMDD = df.format(nowdate);
        nowStr = today_YYYYMMDD; // 오늘날짜 계산 및 변환
    }

    public void ClockSet(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        long base_time=0, now_time, gap_time;
        try {
            now_time = System.currentTimeMillis(); // 현재시간
            if(    (df.parse(today_YYYYMMDD+" 00:00:00")).getTime() <= now_time && now_time  < (df.parse(today_YYYYMMDD+" 00:00:01")).getTime()  ){
                ((MainActivity)MainActivity.mContext).reActivity();
            }
             if( now_time  < (df.parse(today_YYYYMMDD+" 11:00:00")).getTime()  ){
                base_time = (df.parse(today_YYYYMMDD+" 11:00:00")).getTime();
                 main_timerTv.setText("투표 대기 중");
                 main_subTimerTv.setText("투표 시작까지 남은 시간");
                 ((MainActivity)MainActivity.mContext).NowTimeInt = 1;
            }
            else if(  now_time  < (df.parse(today_YYYYMMDD+" 14:00:00")).getTime()  ){
                base_time = (df.parse(today_YYYYMMDD+" 14:00:00")).getTime();
                 main_timerTv.setText("투표 중");
                 main_subTimerTv.setText("투표 종료까지 남은 시간");
                 ((MainActivity)MainActivity.mContext).NowTimeInt = 2;
            }
            else if( now_time  < (df.parse(today_YYYYMMDD+" 18:45:00")).getTime()  ){
                base_time = (df.parse(today_YYYYMMDD+" 18:45:00")).getTime();
                 main_timerTv.setText("발표 준비 중");
                 main_subTimerTv.setText("발표 까지 남은 시간");
                 ((MainActivity)MainActivity.mContext).NowTimeInt = 3;
            }
            else if( now_time  < (df.parse(today_YYYYMMDD+" 23:59:59")).getTime()  ){
                base_time = (df.parse(today_YYYYMMDD+" 23:59:59")).getTime();
                 main_timerTv.setText("발표 중");
                 main_subTimerTv.setText("발표 종료까지 남은 시간");
                 ((MainActivity)MainActivity.mContext).NowTimeInt = 4;
            }
            else if(   now_time  == (df.parse(today_YYYYMMDD+" 24:00:00")).getTime()  ){

            }
            now_time = System.currentTimeMillis(); // 현재시간
            ((MainActivity)MainActivity.mContext).NowTime = now_time;
            gap_time = (base_time -now_time) / 1000; // 기준 시간 - 현재 시간 (초)

            long hourGap = gap_time / 60 / 60 ; // 시간
            long minGap = ((long)(gap_time / 60)) % 60; // 분
            long secGap = gap_time % 60; // 초

            String resultTime = String.format("%02d", hourGap) + ":" +  String.format("%02d", minGap) + ":" +  String.format("%02d", secGap);
            main_clockTv.setText(resultTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void getMain_Server() {
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("SUN", "MainFragment # getMain_Server()");
        client.get("http://onepercentserver.azurewebsites.net/OnePercentServer/main.do", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {    }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr = object.get("main_result") + "";
                    JSONArray arr = new JSONArray(objStr);
                    for (int i = 0; i < arr.length(); i++) {

                        MySharedPreference pref = new MySharedPreference(mContext);

                        JSONObject obj = (JSONObject) arr.get(i);

                        String gift = (String) obj.get("gift_name");
                        String question = (String) obj.get("question");
                        String today = (String) obj.get("today");
                        String giftPng = (String)obj.get("gift_png");

                        main_giftnameTv.setText(gift);

                        getImage_Server(giftPng); // 이미지
                        String ex[] = new String[4];
                        JSONArray exArr = (JSONArray) obj.get("example");
                        for (int z = 1; z <= 4; z++) {
                            JSONObject exObj = (JSONObject) exArr.get(0);
                            ex[z-1] = (String) exObj.get(z + "");
                        }
                        manager.insertMain(new MainObject(today,question,ex[0],ex[1],ex[2],ex[3],gift,giftPng));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("SUN", "e : " + e.toString());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "MainFragment # getMain_Server # onFailure // statusCode : " + statusCode +  " , error : " + error.toString());
            }
            @Override
            public void onRetry(int retryNo) {  }
        });
    }

    public Bitmap byteArrayToBitmap(byte[] byteArray ) {  // byte -> bitmap 변환 및 반환
        Bitmap bitmap = BitmapFactory.decodeByteArray( byteArray, 0, byteArray.length ) ;
        return bitmap ;
    }

    void getImage_Server(String imgName) {
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("SUN", "MainFragment # getImage_Server()");
        client.get("http://onepercentserver.azurewebsites.net/OnePercentServer/resources/common/image/"+imgName,  new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // byteArrayToBitmap 를 통해 reponse로 받은 이미지 데이터 bitmap으로 변환
                Bitmap bitmap = byteArrayToBitmap(response);
                main_giftImg.setImageBitmap(bitmap);

                String saveImage = Base64.encodeToString(response, Base64.DEFAULT);
                pref.setPreferences("oneday","giftImg", saveImage);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "MainFragment # getImage_Server # onFailure // statusCode : " + statusCode +  " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {    }
        });
    }


    void getAllVoteResult_Server() {
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("SUN", "MainFragment # getAllVoteResult_Server()");
        client.get("http://onepercentserver.azurewebsites.net/OnePercentServer/voteResult.do", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {    }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr = object.get("voteTotalResult") + "";
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
                Log.d("SUN", "MainFragment # getAllVoteResult_Server # onFailure // statusCode : " + statusCode +  " , error : " + error.toString());
            }
            @Override
            public void onRetry(int retryNo) {  }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        RunFlag = true;
        thread = new TimerThread();
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        RunFlag = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RunFlag = false;
    }

}
