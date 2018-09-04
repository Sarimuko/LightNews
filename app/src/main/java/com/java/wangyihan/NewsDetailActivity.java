package com.java.wangyihan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.java.wangyihan.Data.DataBaseHandler.DatabaseHandler;
import com.java.wangyihan.Data.DataBaseHandler.FavorateNewsDatabase;
import com.java.wangyihan.Data.RssItem;
import org.w3c.dom.Text;

public class NewsDetailActivity extends AppCompatActivity {
    private String title;
    private String pubDate;
    private String author;
    private String description;
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final Intent intent = getIntent();
        title = intent.getStringExtra("title");
        pubDate = intent.getStringExtra("pubDate");
        //author = intent.getStringExtra("author");
        description = intent.getStringExtra("description");
        link = intent.getStringExtra("link");


        TextView titleView = findViewById(R.id.news_detail_title);
        titleView.setText(title);

        TextView pubDateView = findViewById(R.id.news_detail_pubDate);
        pubDateView.setText(pubDate);

        //TextView authorView = findViewById(R.id.news_detail_author);
        //authorView.setText(author);

        TextView descripionView = findViewById(R.id.news_detail_description);
        descripionView.setText(description);



        Button linkButton = findViewById(R.id.link_button);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(view.getContext(), OriginalNewsActivity.class);
                intent1.putExtra("link", link);

                startActivity(intent1);
            }
        });
        //linkButton.setText(link);

        /**
         * 添加到收藏
         */
        Button favorateButton = findViewById(R.id.favorate_button);
        favorateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RssItem item = new RssItem();
                item.setTitle(title);
                item.setDescription(description);
                item.setLink(link);
                item.setPubdate(pubDate);

                DatabaseHandler.favorateNews(item, getApplicationContext());
            }
        });

    }



}
