package com.java.wangyihan.Data.DataBaseHandler;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.java.wangyihan.Data.FavorateNews;

@Database(entities = {FavorateNews.class}, version = 1, exportSchema = false)
public abstract class FavorateNewsDatabase extends RoomDatabase {

    private static FavorateNewsDatabase INSTANCE;
    private static final Object sLock = new Object();

    public static FavorateNewsDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = buildDatabase(context);
            }
            return INSTANCE;
        }
    }


    /*public static FavorateNewsDatabase getDefault(Context context)
    {
        return buildDatabase(context);
    }*/

    private static FavorateNewsDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), FavorateNewsDatabase.class, "Favorate_news.db")
                .allowMainThreadQueries()
                .build();
    }

    public abstract FavorateNewsDao getFavorateNewsDao();
}
