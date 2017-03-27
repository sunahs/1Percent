package com.sumus.onepercent.MoreActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.sumus.onepercent.FontBaseActvity;
import com.sumus.onepercent.R;

public class CustomerCenterActivity extends FontBaseActvity implements View.OnClickListener{

    WebView center_webView;
    Button center_closeBtn;

    Animation click_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_center);

        InitWidget();
    }

    public void InitWidget(){
        click_animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        center_webView = (WebView)findViewById(R.id.center_webView);
        center_closeBtn = (Button) findViewById(R.id.center_closeBtn);
        center_closeBtn.setOnClickListener(this);
        InitData();
    }

    public void InitData(){
        center_webView.getSettings().setJavaScriptEnabled(true);
        center_webView.loadUrl("http://www.naver.com");
        center_webView.setWebViewClient(new WebViewClientClass());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.center_closeBtn:
                center_closeBtn.startAnimation(click_animation);
                finish();
                break;
        }
    }

    public class WebViewClientClass extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
//            view.loadUrl(url);
//            return true;
        }
    }
}
