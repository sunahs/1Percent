package com.sumus.onepercent.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sumus.onepercent.FontBaseActvity;
import com.sumus.onepercent.JoinActivity;
import com.sumus.onepercent.MainActivity;
import com.sumus.onepercent.Object.MyCalendar;
import com.sumus.onepercent.Object.MySharedPreference;
import com.sumus.onepercent.R;
import com.sumus.onepercent.SQLite.DBManager;
import com.sumus.onepercent.SQLite.MainObject;
import com.sumus.onepercent.SQLite.MyObject;
import com.sumus.onepercent.SQLite.VoteObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by SUNAH on 2016-11-26.
 */

public class VoteFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener{
    /*
         InitWidget() : 위젯 초기화
        IniteData() : 데이터 초기화
        calenderSelect() : 달력 날짜 선택시 ui 변화
        init_Vote_result() : 투표 데이터 초기화
        vote_result_setting() : 날짜 선택에 따른 투표 데이터 동기화
        setVote_Server() : 서버 - 투표 결과 전송 set
        getMain_Server() : 서버 - 오늘의 데이터 get
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
    int vote_number = 0;
    String today_YYYYMMDD, day_YYYYMMDD;
    String calender_YYYYMMDD;
    SimpleDateFormat df_circle = new SimpleDateFormat("yyyy.MM.dd");
    SimpleDateFormat df= new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat df_time = new SimpleDateFormat("yyyyMMdd HH:mm:ss");


    Animation click_animation;


    // Widget
    ImageButton vote_voteBtn, vote_calender_preBtn, vote_calender_nextBtn;
    RadioGroup vote_radiogroup;
    int radio_int[] = {R.id.vote_radio1,R.id.vote_radio2, R.id.vote_radio3, R.id.vote_radio4};
    RadioButton vote_radioButton[] = new RadioButton[4];
    TextView vote_dateTv, vote_questionTv, vote_prize_totalTv, vote_totalTv;
    LinearLayout vote_prize_before, vote_prize_after;
    int vote_result_int[] = {R.id.vote1_my_select, R.id.vote2_my_select, R.id.vote3_my_select, R.id.vote4_my_select};
    TextView vote_resultTv[] = new TextView[4];
    int vote_value_int[] = {R.id.vote1_valueTv, R.id.vote2_valueTv, R.id.vote3_valueTv, R.id.vote4_valueTv};
    TextView vote_valueTv[] = new TextView[4];
    int vote_on_int[] = {R.id.vote1_on_btn, R.id.vote2_on_btn, R.id.vote3_on_btn, R.id.vote4_on_btn};
    TextView vote_on_btn[] = new TextView[4];
    int vote_off_int[] = {R.id.vote1_off_btn, R.id.vote2_off_btn, R.id.vote3_off_btn, R.id.vote4_off_btn};
    TextView vote_off_btn[] = new TextView[4];
    int vote_count_int[] = {R.id.vote1_countTv, R.id.vote2_countTv, R.id.vote3_countTv, R.id.vote4_countTv};
    TextView vote_countTv[] = new TextView[4];
    DBManager manager;

    public VoteFragment(){}

    public static VoteFragment newInstance(String param1, String param2) {
        VoteFragment fragment = new VoteFragment();
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
        views = inflater.inflate(R.layout.fragment_vote, container, false);
        mContext = getContext();
        mActivity = getActivity();
        pref = new MySharedPreference(mContext);

        InitWidget();

        FontBaseActvity fontBaseActvity = new FontBaseActvity(); // 폰트 적용
        fontBaseActvity.setGlobalFont(views);
        return views;
    }

    void InitWidget(){
        click_animation = AnimationUtils.loadAnimation(mActivity,R.anim.alpha);

        manager = new DBManager(mContext);

        vote_voteBtn = (ImageButton)views.findViewById(R.id.vote_voteBtn);
        vote_calender_preBtn = (ImageButton)views.findViewById(R.id.vote_calender_preBtn);
        vote_calender_nextBtn = (ImageButton)views.findViewById(R.id.vote_calender_nextBtn);

        vote_voteBtn.setOnClickListener(this);
        vote_calender_preBtn.setOnClickListener(this);
        vote_calender_nextBtn.setOnClickListener(this);

        vote_radiogroup = (RadioGroup)views.findViewById(R.id.vote_radiogroup);
        vote_radiogroup.setOnCheckedChangeListener(this);

        for(int i=0; i<4; i++) {
            vote_radioButton[i] = (RadioButton) views.findViewById(radio_int[i]);
            vote_resultTv[i] = (TextView)views.findViewById(vote_result_int[i]);
            vote_valueTv[i] = (TextView)views.findViewById(vote_value_int[i]);
            vote_on_btn[i] = (TextView)views.findViewById(vote_on_int[i]);
            vote_off_btn[i] = (TextView)views.findViewById(vote_off_int[i]);
            vote_countTv[i] = (TextView)views.findViewById(vote_count_int[i]);
        }

        long nowdate = System.currentTimeMillis(); // 현재시간
        today_YYYYMMDD = df.format(nowdate).toString();
        day_YYYYMMDD = df_circle.format(nowdate).toString();

        vote_dateTv = (TextView)views.findViewById(R.id.vote_dateTv);
        vote_dateTv.setText(day_YYYYMMDD);
        vote_dateTv.setOnClickListener(this);

        vote_prize_before = (LinearLayout) views.findViewById(R.id.vote_prize_before);
        vote_prize_after = (LinearLayout) views.findViewById(R.id.vote_prize_after);

        vote_questionTv = (TextView)views.findViewById(R.id.vote_questionTv);
        vote_prize_totalTv = (TextView)views.findViewById(R.id.vote_prize_totalTv);
        vote_totalTv = (TextView)views.findViewById(R.id.vote_totalTv);


        InitData();
    }

    void InitData(){
        VoteObject voteObject;
        if((voteObject=manager.selectVote(today_YYYYMMDD))==null)
        {
            Log.d("SUN", "VoteFragmet # db null");
            getMain_Server();
        }
        else
        {
            try {
                Log.d("SUN","VoteFragment # selectMain");
                vote_questionTv.setText(voteObject.getVote_question());
                vote_radioButton[0].setText(voteObject.getVote_ex(1));
                vote_radioButton[1].setText(voteObject.getVote_ex(2));
                vote_radioButton[2].setText(voteObject.getVote_ex(3));
                vote_radioButton[3].setText(voteObject.getVote_ex(4));

                if(manager.selectMy(today_YYYYMMDD) != null)
                {
                    MyObject myObject =manager.selectMy(today_YYYYMMDD);
                    vote_radioButton [Integer.parseInt(myObject.getMy_select_number())-1].setChecked(true);
                    for(int i=0; i<4; i++)
                    {
                        vote_radioButton[i].setClickable(false);
                    }
                    vote_voteBtn.setBackground(getResources().getDrawable(R.mipmap.icon_vote_able_btn));
                }
            }
            catch (NullPointerException e){
                Log.d("SUN", "MainFragment # DB is null");
            }
        }

        long now_time = System.currentTimeMillis(); // 현재시간
        today_YYYYMMDD = df.format(now_time);
        try {
            if( (df_time.parse(today_YYYYMMDD+" 00:00:00")).getTime()  <=  now_time  && now_time < (df_time.parse(today_YYYYMMDD+" 15:00:00")).getTime()  ) // 투표결과발표전
            {
                vote_prize_before.setVisibility(View.VISIBLE);
                vote_prize_after.setVisibility(View.GONE);
            }
            else{
                vote_prize_before.setVisibility(View.GONE);
                vote_prize_after.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId){
            case R.id.vote_radio1:
                vote_number = 1;
                break;

            case R.id.vote_radio2:
                vote_number = 2;
                break;

            case R.id.vote_radio3:
                vote_number = 3;
                break;
            case R.id.vote_radio4:
                vote_number = 4;
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.vote_voteBtn:
                vote_voteBtn.startAnimation(click_animation);
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                long NowTime =  ((MainActivity) MainActivity.mContext).NowTime;
                try {
                    if( (df.parse(today_YYYYMMDD+" 00:00:00")).getTime()  <=  NowTime  && NowTime < (df.parse(today_YYYYMMDD+" 14:00:00")).getTime()  ) // 투표가능시간
                    {
                        if(vote_number<=0){
                            Toast.makeText(mActivity, "보기를 선택해 주세요", Toast.LENGTH_SHORT).show();
                        }
                        else if(manager.selectMy(today_YYYYMMDD)==null)
                        {
                            LayoutInflater inflate = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View layout = inflate.inflate(R.layout.dialog_login, null);
                            FontBaseActvity fontBaseActvity = new FontBaseActvity(); // 폰트 적용
                            fontBaseActvity.setGlobalFont(layout);

                            Button dialog_loginCancleBtn = (Button) layout.findViewById(R.id.dialog_loginCancleBtn);
                            Button dialog_loginOkBtn = (Button) layout.findViewById(R.id.dialog_loginOkBtn);
                            TextView dialog_loginTv = (TextView) layout.findViewById(R.id.dialog_loginTv);

                            AlertDialog.Builder aDialog = new AlertDialog.Builder(mActivity);
                            aDialog.setView(layout);

                            final AlertDialog ad = aDialog.create();
                            ad.show();

                            dialog_loginCancleBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ad.cancel();
                                }
                            });

                            if (pref.getPreferences("user", "userID").equals("")) { // 로그인 안했을 때
                                dialog_loginTv.setText("로그인 하셔야 투표가능합니다. \n로그인하시겠습니까?");
                                dialog_loginOkBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(mActivity, JoinActivity.class);
                                        startActivity(intent);
                                        ad.cancel();
                                    }
                                });
                            }
                            else {
                                dialog_loginTv.setText(vote_number + "번으로 투표하시겠습니까?");
                                dialog_loginOkBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setVote_Server( pref.getPreferences("user", "userID"),day_YYYYMMDD, vote_number);
                                        ad.cancel();
                                    }
                                });
                            }
                        }
                        else
                            Toast.makeText(mActivity, "이미 투표 하셨습니다", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.d("SUN","투표시간이 아닙니다");
                        Toast.makeText(mActivity, "투표시간이 아닙니다.", Toast.LENGTH_SHORT).show();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }

                break;
            case R.id.vote_dateTv:
                if(pref.getPreferences("user","userID").equals("")) {
                    Toast.makeText(mActivity, "로그인 시 이용가능합니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    vote_dateTv.startAnimation(click_animation);
                    LayoutInflater inflate = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflate.inflate(R.layout.dialog_calender, null);

                    AlertDialog.Builder aDialog = new AlertDialog.Builder(mActivity);
                    aDialog.setView(layout);
                    final AlertDialog ad = aDialog.create();
                    ad.show();

                    Button calender_cancleBtn = (Button) layout.findViewById(R.id.calender_cancleBtn);
                    Button calender_okBtn = (Button) layout.findViewById(R.id.calender_okBtn);
                    final CalendarView calendarView = (CalendarView) layout.findViewById(R.id.calendarView);

                    calendarView.setMaxDate(System.currentTimeMillis());

                    calender_okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            long now = calendarView.getDate();
                            calender_YYYYMMDD = df_circle.format(now);
                            vote_dateTv.setText(calender_YYYYMMDD);
                            calenderSelect(calender_YYYYMMDD);
                            ad.cancel();
                        }
                    });

                    calender_cancleBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            long now = System.currentTimeMillis();
                            calender_YYYYMMDD = df_circle.format(now);
                            vote_dateTv.setText(calender_YYYYMMDD);
                            calenderSelect(calender_YYYYMMDD);
                            ad.cancel();
                        }
                    });
                }

                break;
            case R.id.vote_calender_preBtn:
                if(pref.getPreferences("user","userID").equals("")) {
                    Toast.makeText(mActivity, "로그인 시 이용가능합니다.", Toast.LENGTH_SHORT).show();
                }
                else if("2017.01.01".equals(vote_dateTv.getText().toString())){
                    Log.d("SUN", "2017.01.01 이전 X");
                }
                else {
                    vote_calender_preBtn.startAnimation(click_animation);
                    String dates = vote_dateTv.getText().toString();
                    MyCalendar myCalendar = new MyCalendar();
                    String yesterday = myCalendar.yesterday(dates);
                    vote_dateTv.setText(yesterday);
                    calenderSelect(yesterday);
                }
                break;
            case R.id.vote_calender_nextBtn:
                if(pref.getPreferences("user","userID").equals("")) {
                    Toast.makeText(mActivity, "로그인 시 이용가능합니다.", Toast.LENGTH_SHORT).show();
                }
                else if(today_YYYYMMDD.equals(vote_dateTv.getText().toString().replace(".",""))){
                    Log.d("SUN", "오늘 다음날 X");
                }
                else {
                    vote_calender_nextBtn.startAnimation(click_animation);
                    String dates1 = vote_dateTv.getText().toString();
                    MyCalendar myCalendar1 = new MyCalendar();
                    String tomorrow = myCalendar1.tomorrow(dates1);
                    vote_dateTv.setText(tomorrow);
                    calenderSelect(tomorrow);
                }
                break;
        }
    }

    void calenderSelect(String dates){
        String select_date = dates.replace(".","");
        Log.d("SUN","calenderSelect : " +dates +" / " + select_date);

        if(!dates.equals("")) {
            if (dates.equals(today_YYYYMMDD)) {
                long NowTime = ((MainActivity) MainActivity.mContext).NowTime;
                try {
                    if ((df_time.parse(today_YYYYMMDD + " 00:00:00")).getTime() <= NowTime && NowTime < (df_time.parse(today_YYYYMMDD + " 18:45:00")).getTime()) {
                        vote_prize_before.setVisibility(View.VISIBLE);
                        vote_prize_after.setVisibility(View.GONE);
                    } else {
                        vote_prize_before.setVisibility(View.GONE);
                        vote_prize_after.setVisibility(View.VISIBLE);
                        init_Vote_result();
                        vote_result_setting(select_date);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                vote_prize_before.setVisibility(View.GONE);
                vote_prize_after.setVisibility(View.VISIBLE);
                try {
                    init_Vote_result();
                    vote_result_setting(select_date);
                } catch (Exception e) {
                    Log.d("SUN", e.toString());
                }
            }
        }
    }
    void init_Vote_result(){
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, mActivity.getResources().getDisplayMetrics());
        for(int i=0; i<4; i++) {
            vote_resultTv[i].setVisibility(View.INVISIBLE);
            vote_valueTv[i].setText("");
            vote_countTv[i].setText("");
            vote_on_btn[i].setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int)px, 50));
            vote_off_btn[i].setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,(int)px, 50));
        }
        vote_questionTv.setText("");
        vote_prize_totalTv.setText(" ");
        vote_totalTv.setText("");
    }
    void vote_result_setting(String select_date){
        MyObject myObject;
        VoteObject voteObject;
        if((voteObject = manager.selectVote(select_date)) != null){
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, mActivity.getResources().getDisplayMetrics());

            vote_questionTv.setText(voteObject.getVote_question());
            vote_prize_totalTv.setText(voteObject.getVote_prize_total());
            vote_totalTv.setText(voteObject.getVote_total());

            int total_count = Integer.parseInt(voteObject.getVote_total());
            for(int i=0; i<4; i++) {
                vote_valueTv[i].setText(voteObject.getVote_ex(i+1));
                vote_countTv[i].setText(voteObject.getVote_count(i+1));

                int count = Integer.parseInt(voteObject.getVote_count(i+1));
                float percent = (count/(float)total_count)*100;
                vote_on_btn[i].setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int)px, 100-percent));
                vote_off_btn[i].setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,(int)px, percent));
            }
        }
        if((myObject = manager.selectMy(select_date)) != null) {
            vote_resultTv[Integer.parseInt(myObject.getMy_select_number()) - 1].setVisibility(View.VISIBLE);
        }
    }

    void setVote_Server(String user_id, String vote_date, final int vote_answer) {
        JSONObject json = new JSONObject();
        StringEntity entitiy = null;
        try{
            json.put("user_id",user_id);
            json.put("vote_date",vote_date);
            json.put("vote_answer",vote_answer);
            entitiy = new StringEntity(json.toString());
            Log.d("SUN","JoinActivity # json.toString : "+json.toString());
        }catch (JSONException e){

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("SUN", "MainFragment # setVote_Server()");
        client.post(mContext,"http://onepercentserver.azurewebsites.net/OnePercentServer/insertVote.do",entitiy,  "application/json;",new AsyncHttpResponseHandler(){
            @Override
            public void onStart() {    }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr = object.get("vote_result") + "";

                    JSONArray arr = new JSONArray(objStr);

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = (JSONObject) arr.get(i);
                        String state = (String) obj.get("state");

                        if (state.equals("success")) {
                            Toast.makeText(mActivity, "투표 완료", Toast.LENGTH_SHORT).show();
                            manager.insertMy(new MyObject(today_YYYYMMDD, vote_number+"", vote_radioButton[vote_number-1].getText().toString()));
                            vote_radioButton [vote_number-1].setChecked(true);
                            for(int z=0; z<4; z++)
                            {
                                vote_radioButton[z].setClickable(false);
                            }
                            vote_voteBtn.setBackground(getResources().getDrawable(R.mipmap.icon_vote_able_btn));
                        } else {
                            Toast.makeText(mActivity, "이미 투표 ", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("SUN", "VoteFragment # setVote_Server e : " + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "VoteFragment # setVote_Server # onFailure // statusCode : " + statusCode +  " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {  }
        });
    }

    void getMain_Server() {
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("SUN", "VoteFragment # getMain_Server()");
        client.get("http://onepercentserver.azurewebsites.net/OnePercentServer/todayQuestion.do", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {    }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr = object.get("todayQuestion") + "";
                    JSONArray arr = new JSONArray(objStr);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = (JSONObject) arr.get(i);
                        String question = (String) obj.get("vote_question");
                        String ex[] = new String[4];
                        for (int z = 0; z < 4; z++) {
                            ex[z] = (String) obj.get("ex"+(z+1)+"_value");
                            vote_radioButton[z].setText(ex[z]);
                        }
                        vote_questionTv.setText(question);
                        manager.insertVote(new VoteObject(today_YYYYMMDD,question,ex[0],ex[1],ex[2],ex[3]));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("SUN", "VoteFragment # getMain_Server e : " + e.toString());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "VoteFragment # getMain_Server # onFailure // statusCode : " + statusCode +  " , error : " + error.toString());
            }
            @Override
            public void onRetry(int retryNo) {  }
        });
    }

}
