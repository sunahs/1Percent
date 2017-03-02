package com.sumus.onepercent.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumus.onepercent.FontBaseActvity;
import com.sumus.onepercent.Object.MySharedPreference;
import com.sumus.onepercent.R;

/**
 * Created by SUNAH on 2016-11-26.
 */

public class MoreFragment extends Fragment{

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

    }
}
