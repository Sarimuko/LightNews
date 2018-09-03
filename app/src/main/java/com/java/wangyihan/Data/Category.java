package com.java.wangyihan.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tb_category")
public class Category {

    @PrimaryKey
    private long id;

    @ColumnInfo
    private String name;

    @Ignore
    public long getId()
    {
        return id;
    }

    @Ignore
    public String getName()
    {
        return name;
    }

    @Ignore
    public void setId(long id)
    {
        this.id = id;
    }

    @Ignore
    public void setName(String name)
    {
        this.name = name;
    }
}
