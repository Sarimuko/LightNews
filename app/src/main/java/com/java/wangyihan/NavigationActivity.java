package com.java.wangyihan;

import android.content.Intent;
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

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewsFragment.OnFragmentInteractionListener, CategoryListFragment.OnFragmentInteractionListener
, LoginFragment.OnFragmentInteractionListener{


    private String user = "default user";
    private String email = "default email";

    private NewsFragment newsFragment;
    private CategoryListFragment categoryListFragment = CategoryListFragment.newInstance();
    private NavigationView navigationView;
    private LoginFragment loginFragment = LoginFragment.newInstance();

    private ArrayList<String> testLinks = new ArrayList<String>();
    private ArrayList<String> nameList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log.e("nav", "create navigation view");


        testLinks.add("http://news.qq.com/newsgn/rss_newsgn.xml");
        nameList.add("国内新闻");

        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        newsFragment = NewsFragment.newInstance(1, testLinks, nameList);
        getFragmentManager().beginTransaction().replace(R.id.news_list_frame, newsFragment).commit();


        /*展示用户信息*/
        Intent intent = getIntent();
        if (intent != null)
        {
            user = intent.getStringExtra("username");
            email = intent.getStringExtra("email");

            TextView userText = navigationView.getHeaderView(0).findViewById(R.id.username_text);
            userText.setText(user);

            TextView emailText = navigationView.getHeaderView(0).findViewById(R.id.email_address_text);
            emailText.setText(email);
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUser(String user) {
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

            getFragmentManager().beginTransaction().replace(R.id.news_list_frame, newsFragment).commit();
            newsFragment.setUsername(user);
            newsFragment.showFavorate(user);

        } else if (id == R.id.nav_sign) {
            getFragmentManager().beginTransaction().replace(R.id.news_list_frame, loginFragment).commit();

        } else if (id == R.id.nav_local) {
            getFragmentManager().beginTransaction().replace(R.id.news_list_frame, newsFragment).commit();
            newsFragment.setUsername(user);
            newsFragment.showFavorate(user);

        } else if (id == R.id.nav_recommend) {

        } else if (id == R.id.nav_categories_setting) {

        } else if (id == R.id.nav_home)
        {
            getFragmentManager().beginTransaction().replace(R.id.news_list_frame, newsFragment).commit();
            newsFragment.refetch();
            newsFragment.update();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentInteraction(String name, String link, boolean checked) {
        if (!checked && nameList.contains(name))
        {
            nameList.remove(name);
            testLinks.remove(link);
        }
        else if (checked && !nameList.contains(name))
        {
            nameList.add(name);
            testLinks.add(link);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String user, String email) {
        TextView userText = navigationView.getHeaderView(0).findViewById(R.id.username_text);
        userText.setText(user);

        TextView emailText = navigationView.getHeaderView(0).findViewById(R.id.email_address_text);
        emailText.setText(email);
    }
}
