package com.java.wangyihan;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import com.java.wangyihan.Data.DataBaseHandler.DatabaseHandler;
import com.java.wangyihan.Data.RssItem;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        RssItem item = new RssItem();
        item.setTitle("test title");
        item.setDescription("this is a test");

        //DatabaseHandler.readNews(item, appContext);

        List<RssItem> ans = DatabaseHandler.getAllRead(appContext);

        assertEquals(1, ans.size());

        //Log.e("size:", Integer.toString(ans.size()));
    }
}