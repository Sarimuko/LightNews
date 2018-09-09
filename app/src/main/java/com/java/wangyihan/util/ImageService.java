package com.java.wangyihan.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageService implements Runnable{

    private static ImageService instance = new ImageService();

    public static ImageService getInstance()
    {
        return instance;
    }

    private byte[] data;
    private String mPath;
    public byte[] getImage(String path) throws IOException {
        mPath = path;

        Thread thread = new Thread(this);
        try
        {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return data;

    }

    @Override
    public void run() {
        try
        {
            URL url = new URL("http:" + mPath);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");  //设置请求方法为GET
            conn.setReadTimeout(5*1000);  //设置请求过时时间为5秒
            InputStream inputStream = conn.getInputStream();  //通过输入流获得图片数据
            data = StreamTool.readInputStream(inputStream);   //获得图片的二进制数据
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}