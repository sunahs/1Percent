package com.sumus.onepercent.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sumus.onepercent.MoreActivity.CustomerCenterActivity;
import com.sumus.onepercent.FontBaseActvity;
import com.sumus.onepercent.MoreActivity.PushActivity;
import com.sumus.onepercent.Object.MySharedPreference;
import com.sumus.onepercent.R;

/**
 * Created by SUNAH on 2016-11-26.
 */

public class MoreFragment extends Fragment implements  View.OnClickListener{

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
    Animation click_animation;

    // 위젯
    RelativeLayout more_admin, more_notice, more_center, more_push;
    ImageButton  more_adminBtn, more_noticeBtn, more_centerBtn, more_pushBtn;
    TextView more_adminTv;

    public MoreFragment (){}

    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
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
        views = inflater.inflate(R.layout.fragment_more, container, false);
        mContext = getContext();
        mActivity = getActivity();
        pref = new MySharedPreference(mContext);

        InitWidget();

        FontBaseActvity fontBaseActvity = new FontBaseActvity(); // 폰트 적용
        fontBaseActvity.setGlobalFont(views);
        return views;
    }

    public void InitWidget(){

        click_animation = AnimationUtils.loadAnimation(mActivity,R.anim.alpha);

        more_admin = (RelativeLayout)views.findViewById(R.id.more_admin);
        more_center = (RelativeLayout)views.findViewById(R.id.more_center);
        more_push = (RelativeLayout)views.findViewById(R.id.more_push);

        more_admin.setOnClickListener(this);
        more_center.setOnClickListener(this);
        more_push.setOnClickListener(this);

        more_centerBtn = (ImageButton) views.findViewById(R.id.more_centerBtn);
        more_pushBtn = (ImageButton) views.findViewById(R.id.more_pushBtn);

        more_centerBtn.setOnClickListener(this);
        more_pushBtn.setOnClickListener(this);

        more_adminTv = (TextView)views.findViewById(R.id.more_adminTv);
        InitData();
    }

    public void InitData(){
        if(!pref.getPreferences("user","userID").equals("")){
            more_adminTv.setText(pref.getPreferences("user", "userID"));
        }
        else{
            more_adminTv.setText("-");
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.more_centerBtn:
            case R.id.more_center:
                more_center.startAnimation(click_animation);
                intent = new Intent(mActivity, CustomerCenterActivity.class);
                startActivity(intent);
                break;

            case R.id.more_pushBtn:
            case R.id.more_push:
                more_push.startAnimation(click_animation);
                intent = new Intent(mActivity, PushActivity.class);
                startActivity(intent);
                break;
        }
    }
}
