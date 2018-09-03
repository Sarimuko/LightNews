package com.java.wangyihan.Data.DataBaseHandler;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import com.java.wangyihan.Data.RssItem;

@Database(entities = {RssItem.class}, version =  1, exportSchema = true)
public abstract class ReadNewsDatabase extends RoomDatabase {

    private static ReadNewsDatabase INSTANCE;
    private static final Object sLock = new Object();

    public static ReadNewsDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = buildDatabase(context);
            }
            return INSTANCE;
        }
    }

    /*public static ReadNewsDatabase getDefault(Context context)
    {
        return buildDatabase(context);
    }*/

    private static ReadNewsDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), ReadNewsDatabase.class, "Read_news.db")
                .allowMainThreadQueries()
                .build();
    }

    public abstract ReadNewsDao getReadNewsDao();

}
