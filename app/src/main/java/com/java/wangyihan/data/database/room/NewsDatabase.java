package com.java.wangyihan.data.database.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import com.java.wangyihan.data.model.Category;
import com.java.wangyihan.data.model.FavorateNews;
import com.java.wangyihan.data.model.RssItem;
import com.java.wangyihan.data.model.User;
import com.java.wangyihan.util.Converter;

@TypeConverters({Converter.class})
@Database(entities = {FavorateNews.class, RssItem.class, Category.class, User.class}, version = 5, exportSchema = false)
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
                .allowMainThreadQueries().fallbackToDestructiveMigration()
                .build();
    }



    public abstract FavorateNewsDao getFavorateNewsDao();

    public abstract CategoryDao getCategoryDao();
    public abstract ReadNewsDao getReadNewsDao();

    public abstract UserDao getUserDao();

}
