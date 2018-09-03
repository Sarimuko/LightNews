package com.java.wangyihan.Data.DataBaseHandler;

import android.arch.persistence.room.*;
import com.java.wangyihan.Data.RssItem;

import java.util.List;

@Dao
public interface ReadNewsDao {

    @Query("SELECT * FROM tb_read_news")
    List<RssItem> getAll();

    @Query("SELECT * FROM tb_read_news WHERE title IN (:titles)")
    List<RssItem> getAllByTitles(String[] titles);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<RssItem> entities);

    @Delete
    void delete(RssItem entity);

    @Update
    void update(RssItem entity);
}
