package com.java.wangyihan;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.java.wangyihan.Data.Category;
import com.java.wangyihan.Data.User;
import com.java.wangyihan.Util.WXTool;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewsFragment.OnFragmentInteractionListener, CategoryListFragment.OnFragmentInteractionListener
, LoginFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener{


    private static User user;
    {
        if (user == null)
            user = new User("default", "default", "default");
    }

    enum State{FAVORATE, LOCAL, RECOMMEND, HOME};
    private State state;

    public State getState() {
        return state;
    }

    public User getUser() {
        return user;
    }

    private NewsFragment newsFragment;
    private CategoryListFragment categoryListFragment = CategoryListFragment.newInstance();
    private NavigationView navigationView;
    private LoginFragment loginFragment = LoginFragment.newInstance();
    private AddCategoryFragment addCategoryFragment = AddCategoryFragment.newInstance();

    public ArrayList<Category> categoryList = new ArrayList<Category>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //user = new User("default", "default", "default");

        setTheme(Configure.noBarTheme);
        WXTool.register(this);

        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        newsFragment = NewsFragment.newInstance(categoryList);
        //newsFragment.showRecommend(user);
        getFragmentManager().beginTransaction().replace(R.id.news_list_frame, newsFragment).commit();


        /*展示用户信息*/
        Intent intent = getIntent();
        if (intent != null)
        {
            //user.setUsername(intent.getStringExtra("username"));
            //user.setUsername(intent.getStringExtra("email"));
            User tmp = intent.getParcelableExtra("user");
            user = tmp == null? user: tmp;

            TextView userText = navigationView.getHeaderView(0).findViewById(R.id.username_text);
            userText.setText(user.getUsername());

            TextView emailText = navigationView.getHeaderView(0).findViewById(R.id.email_address_text);
            emailText.setText(user.getEmail());
        }
    }

    /*public void setEmail(String email) {
        user.u = email;
    }*/

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            getFragmentManager().beginTransaction().replace(R.id.news_list_frame, addCategoryFragment).commit();
            return true;
        }
        if (id == R.id.setting_item)
        {
            SettingFragment settingFragment = SettingFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.news_list_frame, settingFragment).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_categories) {
            getFragmentManager().beginTransaction().replace(R.id.news_list_frame, categoryListFragment).commit();


        } else if (id == R.id.nav_favorate) {



            state = State.FAVORATE;
            //newsFragment.setUsername(user.getUsername());
            newsFragment.showFavorate(user.getUsername());
            //Log.e("nav", "favorate" + Integer.toString(newsFragment.mRssFeed.getItemCount()));

            getFragmentManager().beginTransaction().replace(R.id.news_list_frame, newsFragment).commit();

        } else if (id == R.id.nav_sign) {
            getFragmentManager().beginTransaction().replace(R.id.news_list_frame, loginFragment).commit();

        } else if (id == R.id.nav_local) {

            //todo: local
            state = State.LOCAL;
            newsFragment.showLocal();
            getFragmentManager().beginTransaction().replace(R.id.news_list_frame, newsFragment).commit();


        } else if (id == R.id.nav_recommend) {
            //todo: recommendation
            state = State.RECOMMEND;
            newsFragment.showRecommend(user);

            getFragmentManager().beginTransaction().replace(R.id.news_list_frame, newsFragment).commit();


        } else if (id == R.id.nav_categories_setting) {
            getFragmentManager().beginTransaction().replace(R.id.news_list_frame, addCategoryFragment).commit();

        } else if (id == R.id.nav_home)
        {
            Log.e("nav", "home");
            state = State.HOME;
            //newsFragment.refetch();
            newsFragment.showHome();

            getFragmentManager().beginTransaction().replace(R.id.news_list_frame, newsFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentInteraction(String name, String link, int id, boolean checked) {
        if (!checked)
        {
            for (Category category: categoryList)
            {
                if (category.getName().equals(name))
                {
                    categoryList.remove(category);
                    break;
                }
            }

        }
        else if (checked)
        {
            boolean flag = false;
            for (Category category: categoryList)
            {
                if (category.getName().equals(name))
                {
                    categoryList.remove(category);
                    flag = true;
                    break;
                }
            }
            if (!flag)
            {
                Category category = new Category();
                category.setUrl(link);
                category.setName(name);
                category.setId(id);
                categoryList.add(category);
            }

        }

        Log.e("category in nav", Integer.toString(categoryList.size()));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(User user) {
        TextView userText = navigationView.getHeaderView(0).findViewById(R.id.username_text);
        this.user = user;
        userText.setText(user.getUsername());

        TextView emailText = navigationView.getHeaderView(0).findViewById(R.id.email_address_text);
        //this.email = email;
        emailText.setText(user.getEmail());
    }

    @Override
    public void onFragmentInteraction() {
        setTheme(Configure.noBarTheme);
        recreate();
    }
}
