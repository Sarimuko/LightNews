package com.java.wangyihan.Data.DataBaseHandler;

import android.arch.persistence.room.*;
import com.java.wangyihan.Data.User;

import java.util.List;

@Dao
public interface UserDao {
    
    @Query("SELECT * FROM tb_users")
    List<User> getAll();

    @Query("SELECT * FROM tb_users WHERE username IN (:names)")
    List<User> getAllByName(String[] names);

    @Query("SELECT count(*) FROM tb_users")
    long getUserCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<User> entities);

    @Delete
    void delete(User entity);

    @Update
    void update(User entity);
}
