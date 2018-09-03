package com.java.wangyihan.Data;

import android.arch.persistence.room.*;
import android.support.annotation.NonNull;


@Entity(tableName = "tb_favorate_news", indices = @Index(value = {"link"}, unique = true))
public class FavorateNews {

    @PrimaryKey
    @NonNull
    private String title;
    @ColumnInfo
    private String description;
    @ColumnInfo
    private String link;
    @ColumnInfo
    private String category;
    @ColumnInfo
    private String pubdate;


    public String getTitle() {
        if (title.length() > 20) {
            return title.substring(0, 19) + "...";
        }
        return title;
    }

    @Ignore
    public FavorateNews(RssItem item)
    {
        this.title = item.getTitle();
        this.description = item.getDescription();
        this.category = item.getCategory();
        this.link = item.getLink();
        this.pubdate = item.getPubdate();
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
