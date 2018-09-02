package com.java.wangyihan.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class RssItem implements Parcelable {

    private String title;
    private String description;
    private String link;
    private String category;
    private String pubdate;

    public static final String TITLE = "title";
    public static final String PUBDATE = "pubdate";

    public RssItem() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(link);
        parcel.writeString(category);
        parcel.writeString(pubdate);


    }

    public static final Parcelable.Creator<RssItem> CREATOR = new Parcelable.Creator<RssItem>()
    {
        public RssItem createFromParcel(Parcel in)
        {
            return new RssItem(in);
        }

        public RssItem[] newArray(int size)
        {
            return new RssItem[size];
        }
    };

    private RssItem(Parcel in)
    {
        title = in.readString();
        description = in.readString();
        link = in.readString();
        category = in.readString();
        pubdate = in.readString();
    }

    public String getTitle() {
        if (title.length() > 20) {
            return title.substring(0, 19) + "...";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    @Override
    public String toString() {
        return "RssItem [title=" + title + ", description=" + description
                + ", link=" + link + ", category=" + category + ", pubdate="
                + pubdate + "]";
    }

}

