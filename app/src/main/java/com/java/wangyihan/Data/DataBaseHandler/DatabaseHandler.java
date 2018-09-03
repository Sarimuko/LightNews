package com.java.wangyihan.Data.DataBaseHandler;

import android.content.Context;
import com.java.wangyihan.Data.FavorateNews;
import com.java.wangyihan.Data.RssItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    public static void readNews(RssItem item, Context context) {
        List<RssItem> mPhones = new ArrayList<RssItem>();
        mPhones.add(item);
        ReadNewsDatabase.getInstance(context).getReadNewsDao().insert(mPhones);
    }

    public static List<RssItem> getAllRead(Context context)
    {
         return ReadNewsDatabase.getInstance(context).getReadNewsDao().getAll();
    }

    static RssItem findReadByTitle(Context context, String title)
    {
        String[] titles = new String[1];
        titles[0] = title;

        List<RssItem> ansList = ReadNewsDatabase.getInstance(context).getReadNewsDao().getAllByTitles(titles);
        return ansList.isEmpty()? null: ansList.get(0);
    }


    public static void favorateNews(RssItem item, Context context)
    {
        List<FavorateNews> mNews = new ArrayList<FavorateNews>();
        mNews.add(new FavorateNews(item));
        FavorateNewsDatabase.getInstance(context).getFavorateNewsDao().insert(mNews);
    }

    public static List<RssItem> getAllFavorate(Context context)
    {
        List<FavorateNews> fList = FavorateNewsDatabase.getInstance(context).getFavorateNewsDao().getAll();
        List<RssItem> itemList = new ArrayList<RssItem>();

        for (FavorateNews f: fList)
        {
            itemList.add(new RssItem(f));
        }

        return itemList;
    }


}
