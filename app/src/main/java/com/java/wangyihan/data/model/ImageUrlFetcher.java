package com.java.wangyihan.data.model;

import android.util.Log;
import org.jsoup.Jsoup;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageUrlFetcher implements Runnable{

    private static ImageUrlFetcher instance = new ImageUrlFetcher();
    public static ImageUrlFetcher getInstance()
    {
        return instance;
    }

    private List<String> urlList = new ArrayList<String>();
    private String content;
    private String murl;

    public static Map<String, String> cookies = new HashMap<String, String>();

    public void getUrlList(String url)
    {
        murl = url;
        urlList.clear();
        Thread thread = new Thread(this);
        thread.start();
        try
        {
            thread.join();
        }
        catch (Exception e)
        {
            Log.e("imageFetcher", e.getMessage());
        }
        Document doc = Jsoup.parse(content);

        //Element content = doc.getElementById("img");
        Elements links = doc.getElementsByTag("img");
        for (Element link : links) {
            urlList.add(link.attr("src"));
        }
    }

    @Override
    public void run() {
        try
        {
            Connection.Response response = Jsoup.connect(murl).cookies(cookies).postDataCharset("utf-8").execute();
            content = response.body();
            //Log.e("content", content);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public List<String> getList()
    {
        return urlList;
    }
}
