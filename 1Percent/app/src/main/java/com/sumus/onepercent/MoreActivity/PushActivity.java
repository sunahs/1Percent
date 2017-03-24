package com.sumus.onepercent.MoreActivity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.sumus.onepercent.FontBaseActvity;
import com.sumus.onepercent.MainActivity;
import com.sumus.onepercent.Object.MySharedPreference;
import com.sumus.onepercent.R;
import com.sumus.onepercent.SQLite.DBManager;
import com.sumus.onepercent.SQLite.SettingObject;

public class PushActivity extends FontBaseActvity{

    public DBManager manager;
    public static Context mContext;
    Switch set_push, set_vibe;
    MySharedPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        mContext = this;

        initWidget();
    }

    public void initWidget(){
        pref = new MySharedPreference(mContext);
        manager = new DBManager(mContext);
        set_push = (Switch)findViewById(R.id.set_push);
        set_vibe = (Switch)findViewById(R.id.set_vibe);

        set_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.setPreferences("fcm", "push", isChecked == true ? "yes" : "no");
            }
        });
        set_vibe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.setPreferences("fcm", "vibe", isChecked == true ? "no" : "yes");
            }
        });

        initData();
    }

    public void initData(){
        String push = pref.getPreferences("fcm","push");
        String pushvibe = pref.getPreferences("fcm","vibe");
        Log.d("SUN","push : "+ push + " pushvibe : "+ pushvibe);

        if(push.equals("yes"))
            set_push.setChecked(true);
        else
            set_push.setChecked(false);

        if(pushvibe.equals("yes"))
            set_vibe.setChecked(true);
        else
            set_vibe.setChecked(false);

    }

}
