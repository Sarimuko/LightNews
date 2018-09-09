package com.java.wangyihan.data.database.room;

import android.arch.persistence.room.*;
import com.java.wangyihan.data.model.Category;

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
