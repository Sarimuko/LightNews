package com.java.wangyihan.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import com.java.wangyihan.Data.DataBaseHandler.DatabaseHandler;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

@Entity(tableName = "tb_users")
public class User implements Parcelable {

    @PrimaryKey
    @NonNull
    private String username;

    @ColumnInfo
    private String email;

    @ColumnInfo
    private String password;

    @ColumnInfo
    private Map<String, Object> history = new TreeMap<String, Object>();


    public Map<String, Object> getHistory() {
        return history;
    }

    public void setHistory(Map<String, Object> history) {
        this.history = history;
    }

    public User(String username, String email, String password)
    {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeMap(history);
    }


    @Ignore
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {

            String username = in.readString();
            String email = in.readString();
            String password = in.readString();
            Map<String, Object> history = new TreeMap<String, Object>();
            in.readMap(history, TreeMap.class.getClassLoader());

            User user = new User(username, email, password);
            user.setHistory(history);

            return null;
        }

        @Override
        public User[] newArray(int i) {
            return new User[0];
        }
    };

    public Category getRecommend(Context context)
    {
        long categoryCnt = DatabaseHandler.getCategoryCount(context);
        Random random = new Random();
        if (history.isEmpty())
        {
            return DatabaseHandler.getCategory(random.nextLong() % categoryCnt, context);
        }
        else
        {
            int totalCnt = 0;
            for (String key: history.keySet())
            {
                totalCnt += (Integer) history.get(key);
            }

            Log.e("total history", Integer.toString(totalCnt));
            int r = random.nextInt() % totalCnt;
            TreeMap<String, Object> treeHistory = (TreeMap<String, Object>)history;
            for (String key: treeHistory.descendingKeySet())
            {
                if ((Integer)treeHistory.get(key) > r)
                {
                    return DatabaseHandler.getCategory(Long.parseLong(key), context);
                }
            }

        }
        return null;
    }

}

