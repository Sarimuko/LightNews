package com.java.wangyihan.data.database.room;

import android.content.Context;
import android.util.Log;
import com.java.wangyihan.data.model.Category;
import com.java.wangyihan.data.model.FavorateNews;
import com.java.wangyihan.data.model.RssItem;
import com.java.wangyihan.data.model.User;
import com.java.wangyihan.util.MD5Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatabaseHandler {
    public static void readNews(User user, RssItem item, Context context) {
        List<RssItem> mPhones = new ArrayList<RssItem>();
        mPhones.add(item);
        NewsDatabase.getInstance(context).getReadNewsDao().insert(mPhones);

        Map<String, Object> history = user.getHistory();
        if (history.containsKey(new Long(item.getCategoryID()).toString()))
        {
            history.put(new Long(item.getCategoryID()).toString(), (Integer)history.get(new Long(item.getCategoryID()).toString()) + 1);

        }
        else
        {
            history.put(new Long(item.getCategoryID()).toString(), 1);
        }

        Log.e(new Long(item.getCategoryID()).toString(), history.get(new Long(item.getCategoryID()).toString()).toString());
        user.setHistory(history);

        register(user, context);
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


    public static void favorateNews(String username, RssItem item, Context context)
    {
        List<FavorateNews> mNews = new ArrayList<FavorateNews>();
        long cnt = NewsDatabase.getInstance(context).getFavorateNewsDao().getFavorateCount();
        FavorateNews favorateNews = new FavorateNews(item, cnt);
        favorateNews.setOwnerName(username);

        mNews.add(favorateNews);
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

    public static List<RssItem> getFavorateByUser(Context context, String username)
    {

        List<FavorateNews> fList = NewsDatabase.getInstance(context).getFavorateNewsDao().getAllByOwners(username);
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

    public static void register(User user, Context context)
    {
        List<User> list = new ArrayList<User>();
        list.add(user);

        NewsDatabase.getInstance(context).getUserDao().insert(list);
    }

    public static int hasUser(String username, String email, String password, Context context)
    {
        password = MD5Utils.stringToMD5(password);
        String[] user = {username};
        List<User> list = NewsDatabase.getInstance(context).getUserDao().getAllByName(user);

        if (list.isEmpty())
            return 0;//没有

        if (!password.equals(list.get(0).getPassword()) ||!email.equals(list.get(0).getEmail()))
            return -1;//失败
        return 1;
    }

    public static User getUser(String username, String email, String password, Context context)
    {
        password = MD5Utils.stringToMD5(password);
        String[] user = {username};
        List<User> list = NewsDatabase.getInstance(context).getUserDao().getAllByName(user);

        if (list.isEmpty())
            return null;//没有

        if (!password.equals(list.get(0).getPassword()) ||!email.equals(list.get(0).getEmail()))
            return null;//失败
        return list.get(0);
    }

    public static List<Category> getAllCategories(Context context)
    {
        return NewsDatabase.getInstance(context).getCategoryDao().getAll();
    }

    public static long getCategoryCount(Context context)
    {
        return NewsDatabase.getInstance(context).getCategoryDao().getCount();
    }

    public static Category getCategory(long ID, Context context)
    {
        long[] ids = new long[1];
        ids[0] = ID;

        List<Category> categoryList = NewsDatabase.getInstance(context).getCategoryDao().getAllByIds(ids);
        return categoryList == null || categoryList.isEmpty()? null: categoryList.get(0);
    }


}
