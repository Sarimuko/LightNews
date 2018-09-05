package com.java.wangyihan.Data;

import android.arch.persistence.room.*;
import android.support.annotation.NonNull;


@Entity(tableName = "tb_favorate_news")
public class FavorateNews {

    @PrimaryKey
    @NonNull
    long id;

    @ColumnInfo
    private String title;
    @ColumnInfo
    private String description;
    @ColumnInfo
    private String link;
    @ColumnInfo
    private String category;
    @ColumnInfo
    private String pubdate;
    @ColumnInfo
    private String ownerName;


    public String getTitle() {
        if (title.length() > 20) {
            return title.substring(0, 19) + "...";
        }
        return title;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    @Ignore
    public FavorateNews(RssItem item, long id)
    {
        this.title = item.getTitle();
        this.description = item.getDescription();
        this.category = item.getCategory();
        this.link = item.getLink();
        this.pubdate = item.getPubdate();
        this.id = id;
    }


    public FavorateNews(){}





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
}
