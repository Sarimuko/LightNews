package com.java.wangyihan.Data.DataBaseHandler;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.java.wangyihan.Data.Category;
import com.java.wangyihan.Data.FavorateNews;
import com.java.wangyihan.Data.RssItem;
import com.java.wangyihan.Data.User;

@Database(entities = {FavorateNews.class, RssItem.class, Category.class, User.class}, version = 1, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {

    private static NewsDatabase INSTANCE;
    private static final Object sLock = new Object();

    public static NewsDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = buildDatabase(context);
            }
            return INSTANCE;
        }
    }


    /*public static NewsDatabase getDefault(Context context)
    {
        return buildDatabase(context);
    }*/

    private static NewsDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), NewsDatabase.class, "news.db")
                .allowMainThreadQueries()
                .build();
    }

    public abstract FavorateNewsDao getFavorateNewsDao();

    public abstract CategoryDao getCategoryDao();
    public abstract ReadNewsDao getReadNewsDao();

    public abstract UserDao getUserDao();

}