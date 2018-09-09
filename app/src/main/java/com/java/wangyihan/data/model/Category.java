package com.java.wangyihan.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tb_category")
public class Category {

    @PrimaryKey
    private long id;

    @ColumnInfo
    private String name;

    @ColumnInfo
    private String url;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getId()
    {
        return id;
    }


    public String getName()
    {
        return name;
    }


    public void setId(long id)
    {
        this.id = id;
    }


    public void setName(String name)
    {
        this.name = name;
    }
}
