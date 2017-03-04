package com.sumus.onepercent.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;
import com.sumus.onepercent.FontBaseActvity;
import com.sumus.onepercent.JoinActivity;
import com.sumus.onepercent.MainActivity;
import com.sumus.onepercent.Object.MyCalendar;
import com.sumus.onepercent.Object.MySharedPreference;
import com.sumus.onepercent.R;
import com.sumus.onepercent.SQLite.DBManager;
import com.sumus.onepercent.SQLite.MyObject;
import com.sumus.onepercent.SQLite.PrizeObject;
import com.sumus.onepercent.SQLite.VoteObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

/**
 * Created by SUNAH on 2016-11-26.
 */

public class PrizeFragment extends Fragment implements View.OnClickListener {

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
    String today_YYYYMMDD, day_YYYYMMDD;
    String calender_YYYYMMDD;
    SimpleDateFormat df_circle = new SimpleDateFormat("yyyy.MM.dd");
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat df_time = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    Animation click_animation;

    int gridHeight = 0;

    // 위젯
    TextView prize_dateTv, prize_giftTv, prize_totalTv;
    ImageButton prize_calender_preBtn, prize_calender_nextBtn;
    ImageView prize_giftImg;
    Button prize_moreBtn;
    GridLayout prize_gridLayout;
    LinearLayout prize_beforeLayout, prize_afterLayout;

    // DB
    DBManager manager;

    public PrizeFragment() {
    }

    public static PrizeFragment newInstance(String param1, String param2) {
        PrizeFragment fragment = new PrizeFragment();
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
        views = inflater.inflate(R.layout.fragment_prize, container, false);
        mContext = getContext();
        mActivity = getActivity();
        pref = new MySharedPreference(mContext);

        InitWidget();

        FontBaseActvity fontBaseActvity = new FontBaseActvity(); // 폰트 적용
        fontBaseActvity.setGlobalFont(views);
        return views;
    }

    public void InitWidget() {
        click_animation = AnimationUtils.loadAnimation(mActivity, R.anim.alpha);
        manager = new DBManager(mActivity);

        long nowdate = System.currentTimeMillis(); // 현재시간
        today_YYYYMMDD = df.format(nowdate).toString();
        day_YYYYMMDD = df_circle.format(nowdate).toString();

        prize_dateTv = (TextView) views.findViewById(R.id.prize_dateTv);
        prize_dateTv.setText(day_YYYYMMDD);
        prize_dateTv.setOnClickListener(this);

        prize_giftTv = (TextView) views.findViewById(R.id.prize_giftTv);
        prize_totalTv = (TextView) views.findViewById(R.id.prize_totalTv);

        prize_calender_preBtn = (ImageButton) views.findViewById(R.id.prize_calender_preBtn);
        prize_calender_nextBtn = (ImageButton) views.findViewById(R.id.prize_calender_nextBtn);
        prize_calender_preBtn.setOnClickListener(this);
        prize_calender_nextBtn.setOnClickListener(this);

        prize_giftImg = (ImageView) views.findViewById(R.id.prize_giftImg);
        prize_gridLayout = (GridLayout) views.findViewById(R.id.prize_gridLayout);

        prize_moreBtn = (Button) views.findViewById(R.id.prize_moreBtn);
        prize_moreBtn.setOnClickListener(this);

        prize_beforeLayout = (LinearLayout) views.findViewById(R.id.prize_beforeLayout);
        prize_afterLayout = (LinearLayout) views.findViewById(R.id.prize_afterLayout);

//        prize_gridLayout.getViewTreeObserver()
//                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        gridHeight = prize_gridLayout.getHeight();
////                        Log.d("SUN", "gridHeight; : " + gridHeight);
//                        //리스너 해제
////                        imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    }
//                });
        initData();
    }

    public void initData() {
        PrizeObject prizeObject;
        if ((prizeObject = manager.selectPrize(today_YYYYMMDD)) == null) {
            Log.d("SUN", "PrizeFragment # db null");
            getTodayGift_Server(day_YYYYMMDD);
        } else {
            try {
                Log.d("SUN", "PrizeFragment # db not null~!!!");
                prize_giftTv.setText(prizeObject.getPrize_gift());
                prize_giftImg.setImageBitmap(byteArrayToBitmap(Base64.decode(pref.getPreferences("oneday", "giftImg"), Base64.DEFAULT)));
            } catch (NullPointerException e) {
                Log.d("SUN", "PrizeFragment # DB is null");
            }
        }


        long now_time = System.currentTimeMillis(); // 현재시간
        today_YYYYMMDD = df.format(now_time);
        try {
            if ((df_time.parse(today_YYYYMMDD + " 00:00:00")).getTime() <= now_time && now_time < (df_time.parse(today_YYYYMMDD + " 18:45:00")).getTime()) // 투표결과발표전
            {
                prize_beforeLayout.setVisibility(View.VISIBLE);
                prize_afterLayout.setVisibility(View.GONE);
            } else {
                prize_beforeLayout.setVisibility(View.GONE);
                prize_afterLayout.setVisibility(View.VISIBLE);

                if (manager.selectPrizePeole(today_YYYYMMDD).equals("")) {
                    getWinnerResultSince_Server(day_YYYYMMDD);
                }
                prizeResult_setting(today_YYYYMMDD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prize_dateTv:
                if (pref.getPreferences("user", "userID").equals("")) {
                    //Toast.makeText(mActivity, "로그인 시 이용가능합니다.", Toast.LENGTH_SHORT).show();
                    loginPopup();
                } else {
                    prize_dateTv.startAnimation(click_animation);
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
                            prize_dateTv.setText(calender_YYYYMMDD);
                            calenderSelect(calender_YYYYMMDD);
                            ad.cancel();
                        }
                    });

                    calender_cancleBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            long now = System.currentTimeMillis();
                            calender_YYYYMMDD = df_circle.format(now);
                            prize_dateTv.setText(calender_YYYYMMDD);
                            calenderSelect(calender_YYYYMMDD);
                            ad.cancel();
                        }
                    });
                }
                break;
            case R.id.prize_calender_preBtn:
                if (pref.getPreferences("user", "userID").equals("")) {
                    loginPopup();
                } else if ("2017.01.01".equals(prize_dateTv.getText().toString())) {
                    Log.d("SUN", "2017.01.01 이전 X");
                } else {
                    prize_calender_preBtn.startAnimation(click_animation);
                    String dates = prize_dateTv.getText().toString();
                    MyCalendar myCalendar = new MyCalendar();
                    String yesterday = myCalendar.yesterday(dates);
                    prize_dateTv.setText(yesterday);
                    calenderSelect(yesterday);
                }
                break;
            case R.id.prize_calender_nextBtn:
                if (pref.getPreferences("user", "userID").equals("")) {
                    loginPopup();
                } else if (today_YYYYMMDD.equals(prize_dateTv.getText().toString().replace(".", ""))) {
                    Log.d("SUN", "오늘 다음날 X");
                } else {
                    prize_calender_nextBtn.startAnimation(click_animation);
                    String dates1 = prize_dateTv.getText().toString();
                    MyCalendar myCalendar1 = new MyCalendar();
                    String tomorrow = myCalendar1.tomorrow(dates1);
                    prize_dateTv.setText(tomorrow);
                    calenderSelect(tomorrow);
                }
                break;
            case R.id.prize_moreBtn:
                PrizeObject prizeObject;
                if ((prizeObject = manager.selectPrize(prize_dateTv.getText().toString().replace(".", ""))) != null) {
                    String people = prizeObject.getPrize_people();
                    String list[] = people.split(" ");
                    prize_gridLayout.setColumnCount(2);

                    int length = list.length;
                    prize_moreBtn.setVisibility(View.GONE);


                    for (int i = 10; i < length; i++) {
                        TextView tvChild = new TextView(mActivity);
                        tvChild.setText(list[i]);
                        tvChild.setTextSize(14f);
                        tvChild.setTextColor(getResources().getColor(R.color.colorDarkGray));
                        tvChild.setGravity(Gravity.CENTER);
                        if (pref.getPreferences("user", "userID").equals(list[i].replace("-", ""))) {
                            tvChild.setTextColor(getResources().getColor(R.color.colorPoint));
                        }

                        GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f));
                        param.setGravity(Gravity.CENTER);
                        prize_gridLayout.addView(tvChild, param);
                    }
                }
                FontBaseActvity fontBaseActvity = new FontBaseActvity(); // 폰트 적용
                fontBaseActvity.setGlobalFont(prize_gridLayout);
                break;
        }
    }

    void calenderSelect(String dates) {
        String select_date = dates.replace(".", "");
        Log.d("SUN", "calenderSelect : " + today_YYYYMMDD + " / " + select_date);

        if (select_date.equals(today_YYYYMMDD)) {
            long NowTime = ((MainActivity) MainActivity.mContext).NowTime;
            try {
                if ((df_time.parse(today_YYYYMMDD + " 00:00:00")).getTime() <= NowTime && NowTime < (df_time.parse(today_YYYYMMDD + " 18:45:00")).getTime()) {
                    prize_beforeLayout.setVisibility(View.VISIBLE);
                    prize_afterLayout.setVisibility(View.GONE);
                    Log.d("SUN", "당첨발표전");
                } else {
                    prize_beforeLayout.setVisibility(View.GONE);
                    prize_afterLayout.setVisibility(View.VISIBLE);
                    init_Prize_result();
                    prizeResult_setting(select_date);
                    Log.d("SUN", "당첨발표후");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            prize_beforeLayout.setVisibility(View.GONE);
            prize_afterLayout.setVisibility(View.VISIBLE);
            init_Prize_result();
            prizeResult_setting(select_date);
        }
    }

    void init_Prize_result() {
        prize_gridLayout.removeAllViews();
        prize_giftTv.setText(" ");
        prize_totalTv.setText(" ");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void prizeResult_setting(String select_date) {
        PrizeObject prizeObject;
        if ((prizeObject = manager.selectPrize(select_date)) != null) {


            prize_giftTv.setText(prizeObject.getPrize_gift());
            String people = prizeObject.getPrize_people();
//            List<String> peo = Arrays.asList(people.split(" "));
            String list[] = people.split(" ");
            prize_gridLayout.setColumnCount(2);

            int length = list.length;
            prize_totalTv.setText(length + "");
            if (length > 10) {
                prize_moreBtn.setVisibility(View.VISIBLE);
                length = 10;
            } else
                prize_moreBtn.setVisibility(View.GONE);

            for (int i = 0; i < length; i++) {
                TextView tvChild = new TextView(mActivity);
                tvChild.setText(list[i]);
                tvChild.setTextSize(14f);
                tvChild.setTextColor(getResources().getColor(R.color.colorDarkGray));
                tvChild.setGravity(Gravity.CENTER);
                if (pref.getPreferences("user", "userID").equals(list[i].replace("-", ""))) {
                    tvChild.setTextColor(getResources().getColor(R.color.colorPoint));
                }

                GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f));
                param.setGravity(Gravity.CENTER);
                prize_gridLayout.addView(tvChild, param);
            }
            FontBaseActvity fontBaseActvity = new FontBaseActvity(); // 폰트 적용
            fontBaseActvity.setGlobalFont(prize_gridLayout);
        }
    }

    void getWinnerResultSince_Server(String vote_date) {
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("SUN", "PrizeFragment # getWinnerResultSince_Server()");
        RequestParams params = new RequestParams();
        params.add("vote_date", vote_date);
        client.get("http://onepercentserver.azurewebsites.net/OnePercentServer/WinnerResultSince.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr = object.get("winnerResultSince") + "";
                    JSONArray arr = new JSONArray(objStr);
                    for (int i = 0; i < arr.length(); i++) {

                        JSONObject obj = (JSONObject) arr.get(i);

                        String vote_date = (String) obj.get("vote_date");
                        String giftName = (String) obj.get("gift_name");
                        String giftPng = (String) obj.get("gift_png");
                        String winner = (String) obj.get("winner");

                        manager.insertPrize(vote_date.replace(".", ""), new PrizeObject(winner));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("SUN", "PrizeFragment # getWinnerResultSince_Server # e : " + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "PrizeFragment # getWinnerResultSince_Server # onFailure // statusCode : " + statusCode + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }


    void getTodayGift_Server(final String vote_date) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("vote_date", vote_date);
        Log.d("SUN", "PrizeFragment # getTodayGift_Server()");
        client.get("http://onepercentserver.azurewebsites.net/OnePercentServer/todayGift.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr = object.get("todayGift_result") + "";
                    JSONArray arr = new JSONArray(objStr);
                    for (int i = 0; i < arr.length(); i++) {

                        MySharedPreference pref = new MySharedPreference(mContext);

                        JSONObject obj = (JSONObject) arr.get(i);

                        String gift = (String) obj.get("gift_name");
                        String giftPng = (String) obj.get("gift_png");

                        prize_giftTv.setText(gift);
                        getImage_Server(giftPng); // 이미지

                        manager.insertPrize(new PrizeObject(vote_date.replace(".", ""), gift, giftPng));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("SUN", "PrizeFragment # getTodayGift_Server # e : " + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "PrizeFragment # getTodayGift_Server # onFailure // statusCode : " + statusCode + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }

    void getImage_Server(String imgName) {
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("SUN", "MainFragment # getImage_Server()");
        client.get("http://onepercentserver.azurewebsites.net/OnePercentServer/resources/common/image/" + imgName, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // byteArrayToBitmap 를 통해 reponse로 받은 이미지 데이터 bitmap으로 변환
                Bitmap bitmap = byteArrayToBitmap(response);
                prize_giftImg.setImageBitmap(bitmap);

                String saveImage = Base64.encodeToString(response, Base64.DEFAULT);
                pref.setPreferences("oneday", "giftImg", saveImage);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "MainFragment # getImage_Server # onFailure // statusCode : " + statusCode + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }

    public Bitmap byteArrayToBitmap(byte[] byteArray) {  // byte -> bitmap 변환 및 반환
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }

    public void loginPopup() {
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

        dialog_loginTv.setText("로그인 하셔야 이용가능합니다. \n로그인하시겠습니까?");
        dialog_loginCancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.cancel();
            }
        });
        dialog_loginOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, JoinActivity.class);
                startActivity(intent);
                ad.cancel();
            }
        });
    }
}
