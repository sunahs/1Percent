package com.sumus.onepercent.MoreActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.sumus.onepercent.FontBaseActvity;
import com.sumus.onepercent.JoinActivity;
import com.sumus.onepercent.MainActivity;
import com.sumus.onepercent.Object.MySharedPreference;
import com.sumus.onepercent.R;
import com.sumus.onepercent.SQLite.DBManager;

public class AdminActivity extends FontBaseActvity implements View.OnClickListener {
    Context mContext;
    TextView admin_idTv;

    MySharedPreference pref;

    RelativeLayout admin_logout, more_admin;
    ImageButton admin_logoutBtn;
    TextView admin_logState;

    DBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mContext = this;
        pref = new MySharedPreference(mContext);
        manager = new DBManager(mContext);

        initWidget();


    }

    public void initWidget() {
        admin_idTv = (TextView) findViewById(R.id.admin_idTv);
        admin_logout = (RelativeLayout) findViewById(R.id.admin_logout);
        more_admin = (RelativeLayout) findViewById(R.id.more_admin);
        admin_logoutBtn = (ImageButton) findViewById(R.id.admin_logoutBtn);
        more_admin.setOnClickListener(this);
        admin_logout.setOnClickListener(this);
        admin_logoutBtn.setOnClickListener(this);
        admin_logState = (TextView)findViewById(R.id.admin_logState);

        initData();
    }

    public void initData(){
        if(!pref.getPreferences("user","userID").equals("")){
            admin_idTv.setText(pref.getPreferences("user", "userID"));
            admin_logState.setText("로그아웃");
        }
        else{
            admin_idTv.setText("-");
            admin_logState.setText("로그인");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.admin_logoutBtn:
            case R.id.admin_logout: LayoutInflater inflate = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflate.inflate(R.layout.dialog_logout, null);
                FontBaseActvity fontBaseActvity = new FontBaseActvity(); // 폰트 적용
                fontBaseActvity.setGlobalFont(layout);

                Button dialog_logoutCancleBtn = (Button) layout.findViewById(R.id.dialog_logoutCancleBtn);
                Button dialog_logoutOkBtn = (Button) layout.findViewById(R.id.dialog_logoutOkBtn);
                final TextView dialog_logoutTv = (TextView) layout.findViewById(R.id.dialog_logoutTv);

                AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
                aDialog.setView(layout);

                final AlertDialog ad = aDialog.create();
                ad.show();

                if(!pref.getPreferences("user","userID").equals("")){
                    dialog_logoutTv.setText("로그아웃 하시겠습니까?");
                    dialog_logoutOkBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pref.setPreferences("user","userID","");
                            manager.deleteMy();
                            finish();
                            ad.cancel();
                            ((MainActivity)MainActivity.mContext).reActivity();
                        }
                    });
                }
                else{
                    dialog_logoutTv.setText("로그인 하시겠습니까?");
                    dialog_logoutOkBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, JoinActivity.class);
                            startActivity(intent);
                            ad.cancel();
                        }
                    });
                }
                dialog_logoutCancleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.cancel();
                    }
                });
                break;
            case R.id.more_admin:
                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                Log.d("SUN","deviceToken : "+deviceToken);
                break;
        }
    }
}
