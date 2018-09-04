package com.java.wangyihan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.java.wangyihan.Data.DataBaseHandler.DatabaseHandler;
import com.java.wangyihan.Data.RssFeed;
import com.java.wangyihan.Data.RssFeed_SAXParser;
import com.java.wangyihan.Data.RssHandler;
import com.java.wangyihan.Data.RssItem;
import org.xml.sax.SAXException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Activity_News extends AppCompatActivity{

    final String textUrl = "http://news.qq.com/newsgn/rss_newsgn.xml";
    RssFeed mRssFeed;
    List<RssItem> findList = new ArrayList<RssItem>();


    private List<Map<String,Object> > newsList = new ArrayList<Map<String,Object> >();

    public void updateShow()
    {
        showList(mRssFeed.getItems());
    }

    private void showList(final List<RssItem> itemList)
    {
        newsList.clear();

        for (int i=0;i<itemList.size();i++)
        {
            Map<String,Object> item = new HashMap<String,Object>();
            //一行记录，包含多个控件
            item.put("title", itemList.get(i).getTitle());
            item.put("pubDate", itemList.get(i).getPubdate());

            newsList.add(item);

        }

        ListView lv = (ListView)findViewById(R.id.news_list);
        SimpleAdapter sa = new SimpleAdapter(this,
                newsList,//data 不仅仅是数据，而是一个与界面耦合的数据混合体
                R.layout.fragment_news_display,
                new String[] {"title","pubDate"},//from 从来来
                new int[] {R.id.news_title,R.id.news_date}//to 到那里去
        );
        lv.setAdapter(sa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                RssItem item = itemList.get(i);

                view.setBackgroundColor(getColor(R.color.colorClickedItem));

                Intent intent = new Intent(view.getContext(), NewsDetailActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("description", item.getDescription());
                intent.putExtra("pubDate", item.getPubdate());
                intent.putExtra("link", item.getLink());

                DatabaseHandler.readNews(item, getApplicationContext());

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__news);
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

        //Log.v("test", "testInfo");
        mRssFeed = null;

        try
        {
            mRssFeed = RssFeed_SAXParser.getInstance().getFeed(textUrl);
            //Log.e("Rss Count", Integer.toString(mRssFeed.getItemCount()));
            //Log.v("Rss title", mRssFeed.getTitle());
        }
        catch (IOException e)
        {
            Log.e("exception", "get rss error 1");
        }
        catch (ParserConfigurationException e)
        {
            Log.e("exception", "get rss error 2");
        }
        catch (SAXException e)
        {
            Log.e("exception", "get rss error 3");
        }
        catch (InterruptedException e)
        {
            Log.e("exception", "get rss error 4");
        }

        Log.e("is Rss Null?", Boolean.toString(mRssFeed == null));

        if (mRssFeed == null || mRssFeed.getItemCount() == 0)
        {
            mRssFeed = new RssFeed();
            mRssFeed.setRssItems(DatabaseHandler.getAllRead(getApplicationContext()));
            Log.e("info", "get date from database" + Integer.toString(mRssFeed.getItemCount()));
        }

        if (mRssFeed != null)
        {
            Log.e("create View", "create main view");
            updateShow();
        }

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query))
                {
                    Toast.makeText(Activity_News.this, "请输入查找内容！", Toast.LENGTH_SHORT).show();
                    updateShow();
                }
                else
                {
                    findList.clear();
                    for(int i = 0; i < mRssFeed.getItemCount(); i++)
                    {
                        RssItem information = mRssFeed.getItem(i);
                        if(information.getTitle().contains(query))
                        {
                            findList.add(information);
                            break;
                        }
                    }
                    if(findList.size() == 0)
                    {
                        Toast.makeText(Activity_News.this, "没有相关新闻", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Activity_News.this, "查找成功", Toast.LENGTH_SHORT).show();
                        showList(findList);
                    }
                }
                return true;

            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {


        //Log.e("create View", "create main view");

        return super.onCreateView(name, context, attrs);
    }

}
