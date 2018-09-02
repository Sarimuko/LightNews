package com.java.wangyihan.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.util.Log;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * 这个类用于从给定Url抓取Rss内容
 */
public class RssFeed_SAXParser implements Runnable{
    private static RssFeed_SAXParser instance = new RssFeed_SAXParser();

    /**
     *
     * @return 获得一个单例对象
     */
    public static RssFeed_SAXParser getInstance()
    {
        return instance;
    }

    private RssFeed_SAXParser(){
        Log.e("e", "new rss handler");
        rssHandler = new RssHandler();
    }

    String mUrl;
    RssHandler rssHandler;

    /**
     *
     * @param urlStr 要抓取的url地址
     * @return RssFeed对象，包含一个RssItem列表
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public RssFeed getFeed(String urlStr) throws ParserConfigurationException, SAXException, IOException, InterruptedException{

        mUrl = urlStr;
        /*BufferedReader bin = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = bin.readLine()) != null)
        {
            System.out.println(line);
        }*/
        Thread thread = new Thread(this);
        thread.start();

        thread.join();
        Log.e("e", Boolean.toString(rssHandler.getRssFeed() == null));


        return rssHandler.getRssFeed();
    }

    public void getContent(String urlStr) throws IOException
    {
        URL url = new URL(urlStr);
        URLConnection urlConnection = url.openConnection();

        BufferedReader bin = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        while ((line = bin.readLine()) != null)
        {
            System.out.println(line);
        }



    }

    @Override
    public void run() {
        try
        {
            URL url = new URL(mUrl);
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance(); //构建SAX解析工厂
            SAXParser saxParser = saxParserFactory.newSAXParser(); //解析工厂生产解析器
            XMLReader xmlReader = saxParser.getXMLReader(); //通过saxParser构建xmlReader阅读器

            //RssHandler rssHandler=new RssHandler();
            xmlReader.setContentHandler(rssHandler);
            //使用url打开流，并将流作为 xmlReader解析的输入源并解析
            //InputSource inputSource = new InputSource(url.openStream());
            InputStream urlStream = url.openStream();
            BufferedReader bin = new BufferedReader(new InputStreamReader(urlStream));
            String head = bin.readLine();
            InputStreamReader inputStreamReader;
            //InputSource inputSource = new InputSource(url.openStream());
            if (head.contains("gb2312"))
                inputStreamReader = new InputStreamReader(url.openStream(), "GB2312");
            else
                inputStreamReader = new InputStreamReader(url.openStream(), "UTF-8");
            InputSource inputSource = new InputSource(inputStreamReader);
            xmlReader.parse(inputSource);

            //Log.e("e", Boolean.toString(rssHandler.getRssFeed() == null));

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
            Log.e("exception", e.getMessage());
        }


    }
}


