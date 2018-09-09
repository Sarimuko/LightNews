package com.java.wangyihan.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.java.wangyihan.R;

public class OriginalNewsActivity extends AppCompatActivity {
    private String urlLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Intent intent = getIntent();
        urlLink = intent.getStringExtra("link");

        Log.e("url", urlLink);

        WebView newsWeb = findViewById(R.id.news_web);


        newsWeb.getSettings().setJavaScriptEnabled(true);
        newsWeb.getSettings().setAppCacheEnabled(true);
        //设置 缓存模式
        newsWeb.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        newsWeb.getSettings().setDomStorageEnabled(true);


        newsWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        newsWeb.getSettings().setSupportMultipleWindows(true);
        newsWeb.setWebViewClient(new WebViewClient());
        newsWeb.setWebChromeClient(new WebChromeClient());

        newsWeb.loadUrl(urlLink);
    }

}
