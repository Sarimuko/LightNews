package com.java.wangyihan.data.model;

import android.arch.persistence.room.*;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "tb_read_news", indices = @Index(value = {"link"}, unique = true))
public class RssItem implements Parcelable {

    @PrimaryKey
    @NonNull
    private String title = "";
    @ColumnInfo
    private String description = "";
    @ColumnInfo
    private String link = "";
    @ColumnInfo
    private String category = "";
    @ColumnInfo
    private String pubdate = "";
    @ColumnInfo
    private long categoryID = 0;
    @ColumnInfo
    private byte[] image = new byte[0];

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Ignore
    public static final String TITLE = "title";
    public static final String PUBDATE = "pubdate";

    public RssItem() {

    }

    public long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }


    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(link);
        parcel.writeString(category);
        parcel.writeString(pubdate);
        parcel.writeByteArray(image);


    }

    @Ignore
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

    @Ignore
    private RssItem(Parcel in)
    {
        title = in.readString();
        description = in.readString();
        link = in.readString();
        category = in.readString();
        pubdate = in.readString();
        in.writeByteArray(image);
    }

    @Ignore
    public RssItem(FavorateNews item)
    {
        this.pubdate = item.getPubdate();
        this.title = item.getTitle();
        this.category = item.getCategory();
        this.link = item.getLink();
        this.description = item.getDescription();
        this.image = item.getImage();
    }


    public String getTitle() {
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

    @Ignore
    @Override
    public String toString() {
        return "RssItem [title=" + title + ", description=" + description
                + ", link=" + link + ", category=" + category + ", pubdate="
                + pubdate + "]";
    }

}

