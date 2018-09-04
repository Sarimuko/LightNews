package com.java.wangyihan.Data.DataBaseHandler;

import android.content.Context;
import com.java.wangyihan.Data.Category;
import com.java.wangyihan.Data.FavorateNews;
import com.java.wangyihan.Data.RssItem;
import com.java.wangyihan.Data.User;
import com.java.wangyihan.MD5Utils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    public static void readNews(RssItem item, Context context) {
        List<RssItem> mPhones = new ArrayList<RssItem>();
        mPhones.add(item);
        NewsDatabase.getInstance(context).getReadNewsDao().insert(mPhones);
    }

    public static List<RssItem> getAllRead(Context context)
    {
         return NewsDatabase.getInstance(context).getReadNewsDao().getAll();
    }

    static RssItem findReadByTitle(Context context, String title)
    {
        String[] titles = new String[1];
        titles[0] = title;

        List<RssItem> ansList = NewsDatabase.getInstance(context).getReadNewsDao().getAllByTitles(titles);
        return ansList.isEmpty()? null: ansList.get(0);
    }


    public static void favorateNews(RssItem item, Context context)
    {
        List<FavorateNews> mNews = new ArrayList<FavorateNews>();
        mNews.add(new FavorateNews(item));
        NewsDatabase.getInstance(context).getFavorateNewsDao().insert(mNews);
    }

    public static List<RssItem> getAllFavorate(Context context)
    {
        List<FavorateNews> fList = NewsDatabase.getInstance(context).getFavorateNewsDao().getAll();
        List<RssItem> itemList = new ArrayList<RssItem>();

        for (FavorateNews f: fList)
        {
            itemList.add(new RssItem(f));
        }

        return itemList;
    }

    public static void addCategory(String name, String url, Context context)
    {
        long totalNum = NewsDatabase.getInstance(context).getCategoryDao().getCount();
        Category item = new Category();

        item.setId(totalNum);
        item.setName(name);
        item.setUrl(url);

        List<Category> list = new ArrayList<Category>();
        list.add(item);

        NewsDatabase.getInstance(context).getCategoryDao().insert(list);

    }

    public static void register(String name, String email, String password, Context context)
    {
        password = MD5Utils.stringToMD5(password);
        User user = new User(name, email, password);

        List<User> list = new ArrayList<User>();
        list.add(user);

        NewsDatabase.getInstance(context).getUserDao().insert(list);
    }

    public static boolean hasUser(String username, String password, Context context)
    {
        password = MD5Utils.stringToMD5(password);
        String[] user = {username};
        List<User> list = NewsDatabase.getInstance(context).getUserDao().getAllByName(user);

        if (list.isEmpty())
            return false;

        if (!password.equals(list.get(0).getPassword()) || !username.equals(list.get(0).getUsername()))
            return false;
        return true;
    }


}
