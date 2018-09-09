package com.java.wangyihan.data.database.room;

import android.arch.persistence.room.*;
import com.java.wangyihan.data.model.FavorateNews;

import java.util.List;

@Dao
public interface FavorateNewsDao {
    @Query("SELECT * FROM tb_favorate_news")
    List<FavorateNews> getAll();

    @Query("SELECT * FROM tb_favorate_news WHERE title IN (:titles)")
    List<FavorateNews> getAllByTitles(String[] titles);

    @Query("SELECT count(*) from tb_favorate_news")
    long getFavorateCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<FavorateNews> entities);

    @Query("SELECT * FROM tb_favorate_news WHERE ownerName = :owner")
    List<FavorateNews> getAllByOwners(String owner);

    @Delete
    void delete(FavorateNews entity);

    @Update
    void update(FavorateNews entity);
}
