package com.java.wangyihan.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RssFeed implements Parcelable {

    private String title; // 标题
    private String pubdate; // 发布日期
    private int itemCount; // 用于计算列表的数目
    private List<RssItem> rssItems; // 用于描述列表item

    public RssFeed() {
        rssItems = new ArrayList<RssItem>();
    }

    // 添加RssItem条目,返回列表长度
    public int addItem(RssItem rssItem) {
        rssItems.add(rssItem);
        itemCount++;
        return itemCount;
    }

    // 根据下标获取RssItem
    public RssItem getItem(int position) {
        return rssItems.get(position);
    }

    public List<RssItem> getItems(){
        return rssItems;
    }

    public List<HashMap<String, Object>> getAllItems() {
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < rssItems.size(); i++) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put(RssItem.TITLE, rssItems.get(i).getTitle());
            item.put(RssItem.PUBDATE, rssItems.get(i).getPubdate());
            data.add(item);
        }
        return data;

    }

    public void setRssItems(List<RssItem> items)
    {
        rssItems = items; itemCount = items.size();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(pubdate);
        parcel.writeInt(itemCount);
        parcel.writeParcelableArray(rssItems.toArray(new RssItem[0]), 0);
    }

    public static final Parcelable.Creator<RssFeed> CREATOR = new Parcelable.Creator<RssFeed>()
    {
        public RssFeed createFromParcel(Parcel in)
        {
            return new RssFeed(in);
        }

        public RssFeed[] newArray(int size)
        {
            return new RssFeed[size];
        }
    };

    private RssFeed(Parcel in)
    {
        title = in.readString();
        pubdate = in.readString();
        itemCount = in.readInt();
        Parcelable[] rssArray = in.readParcelableArray(RssItem.class.getClassLoader());
        for (Parcelable rss: rssArray)
        {
            rssItems.add((RssItem)rss);
        }
    }
}

