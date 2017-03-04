package com.sumus.onepercent;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sumus.onepercent.PageAdapter.PagerAdapter;
import com.sumus.onepercent.PageAdapter.SectionsPagerAdapter;

import java.text.SimpleDateFormat;

public class MainActivity extends FontBaseActvity {
    // fragment
    public SectionsPagerAdapter mSectionsPagerAdapter;
    public PagerAdapter mPagerAdapter;
    public ViewPager mViewPager;
    TabLayout tabLayout;
    View actionView;
    TextView action_titleTv;

    // 변수
    public String today_YYYYMMDD, day_YYYYMMDD;
    SimpleDateFormat df_circle = new SimpleDateFormat("yyyy.MM.dd");
    String nowStr;
    public static Context mContext;
    public int TAB_OFF_ICON[] = {R.mipmap.icon_home_off_btn, R.mipmap.icon_vote_off_btn, R.mipmap.icon_prize_off_btn, R.mipmap.icon_more_off_btn};
    public int TAB_ON_ICON[] = {R.mipmap.icon_home_on_btn, R.mipmap.icon_vote_on_btn, R.mipmap.icon_prize_on_btn, R.mipmap.icon_more_on_btn};

    // 시간 관련 변수
    public long NowTime=0;
    public int NowTimeInt = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        InitActionBar();
        InitWidget();
    }

    public void InitWidget() {
        long nowdate = System.currentTimeMillis(); // 현재시간
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        today_YYYYMMDD = df.format(nowdate);

        mPagerAdapter = new PagerAdapter(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        for(int i=0; i<4; i++) {
            if(i==0)
                tabLayout.getTabAt(i).setIcon(getResources().getDrawable(TAB_ON_ICON[i]));
            else
                tabLayout.getTabAt(i).setIcon(getResources().getDrawable(TAB_OFF_ICON[i]));
        }

        tabLayout.setTabTextColors(getResources().getColor(R.color.colorWhite), getResources().getColor(R.color.colorWhite)); // 비선택, 선택

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0; i<4; i++){
                    if(i==position)
                        tabLayout.getTabAt(i).setIcon(getResources().getDrawable(TAB_ON_ICON[i]));
                    else
                        tabLayout.getTabAt(i).setIcon(getResources().getDrawable(TAB_OFF_ICON[i]));
                }

                switch (position) {
                    case 0: // 홈
                        action_titleTv.setText("Home");


                        break;
                    case 1: // 질문
                        action_titleTv.setText("Vote");

                        break;
                    case 2: // 당첨자
                        action_titleTv.setText("Prize");

                        break;
                    case 3: // 더보기
                        action_titleTv.setText("More");

                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    void InitActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(0, 0, 0, 0);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0.0F);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);

        actionView = getSupportActionBar().getCustomView();
        action_titleTv = (TextView) actionView.findViewById(R.id.action_titleTv);

        FontBaseActvity fontBaseActvity = new FontBaseActvity(); // 폰트 적용
        fontBaseActvity.setGlobalFont(actionView);
    }

    public void reActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        startActivity(intent);
        Log.d("SUN","RESTART");
    }


}
