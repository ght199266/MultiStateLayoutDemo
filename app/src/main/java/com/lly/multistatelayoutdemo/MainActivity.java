package com.lly.multistatelayoutdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lly.multistatelayout.MultiStateLayout;
import com.lly.multistatelayout.anim.FadeScaleViewAnimProvider;
import com.lly.multistatelayout.anim.TranslationFadeViewAnimProvider;

public class MainActivity extends AppCompatActivity {


    private MultiStateLayout mMultiStateLayoutl;

    private TextView tv_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMultiStateLayoutl = (MultiStateLayout) findViewById(R.id.state_layout);
        tv_content = (TextView) findViewById(R.id.tv_content);

        //内容先隐藏
        mMultiStateLayoutl.setContentView(tv_content);

        //错误页面点击重试接口,可不写
        mMultiStateLayoutl.setRetryListener(new MultiStateLayout.onRetryListener() {
            @Override
            public void onClick(View v) {
                mMultiStateLayoutl.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //加载完成调用该方法
                        mMultiStateLayoutl.onComplete();
                    }
                }, 2000);
            }
        });
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.empty:
                mMultiStateLayoutl.showEmptyLayout();
                break;
            case R.id.error:
                mMultiStateLayoutl.showErrorLayout("数据加载错误");
                break;
            case R.id.login:
                mMultiStateLayoutl.showLoginLayout();
                break;
            case R.id.network:
                mMultiStateLayoutl.showNoNetWorkLayout();
                break;
            case R.id.anim1:
                mMultiStateLayoutl.setViewAnimOperation(new FadeScaleViewAnimProvider());
                break;
            case R.id.anim2:
                mMultiStateLayoutl.setViewAnimOperation(new TranslationFadeViewAnimProvider());
                break;
        }
    }

}
