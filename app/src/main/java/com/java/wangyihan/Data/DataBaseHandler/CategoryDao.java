package com.java.wangyihan.Data.DataBaseHandler;

import android.arch.persistence.room.*;
import com.java.wangyihan.Data.Category;
import com.java.wangyihan.Data.RssItem;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM tb_category")
    List<Category> getAll();

    @Query("SELECT * FROM tb_category WHERE id IN (:ids)")
    List<Category> getAllByIds(long[] ids);

    @Query("SELECT count(*) FROM tb_category")
    long getCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Category> entities);

    @Delete
    void delete(Category entity);

    @Update
    void update(Category entity);
}
