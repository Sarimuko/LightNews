package com.java.wangyihan.Data.DataBaseHandler;

import android.arch.persistence.room.*;
import com.java.wangyihan.Data.FavorateNews;
import com.java.wangyihan.Data.RssItem;

import java.util.List;

@Dao
public interface FavorateNewsDao {
    @Query("SELECT * FROM tb_favorate_news")
    List<FavorateNews> getAll();

    @Query("SELECT * FROM tb_favorate_news WHERE title IN (:titles)")
    List<FavorateNews> getAllByTitles(String[] titles);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<FavorateNews> entities);

    @Delete
    void delete(FavorateNews entity);

    @Update
    void update(FavorateNews entity);
}