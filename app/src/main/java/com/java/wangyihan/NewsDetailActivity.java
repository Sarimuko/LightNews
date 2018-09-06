package com.java.wangyihan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.java.wangyihan.Data.DataBaseHandler.DatabaseHandler;
import com.java.wangyihan.Data.ImageUrlFetcher;
import com.java.wangyihan.Data.RssItem;
import com.java.wangyihan.Util.ImageService;
import com.java.wangyihan.Util.WXTool;

import java.util.ArrayList;
import java.util.List;


public class NewsDetailActivity extends AppCompatActivity {
    private String title;
    private String pubDate;
    private String author;
    private String description;
    private String link;

    private String username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final Intent intent = getIntent();
        title = intent.getStringExtra("title");
        pubDate = intent.getStringExtra("pubDate");
        //author = intent.getStringExtra("author");
        description = intent.getStringExtra("description");
        link = intent.getStringExtra("link");
        username = intent.getStringExtra("username");

        try
        {
            ImageUrlFetcher.getInstance().getUrlList(link);
            List<String> imageLinks = ImageUrlFetcher.getInstance().getList();
            byte[] imageToShow = new byte[0];
            int mSize = 0;
            for (String image: imageLinks)
            {

                byte[] data = ImageService.getInstance().getImage(image);
                if (data.length > mSize && data.length > 10000)
                {
                    mSize = data.length;
                    imageToShow = data;
                }
            }

            Log.e("image item", Integer.toString(imageToShow.length));

            ImageView imageView = findViewById(R.id.news_detail_image);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageToShow, 0, imageToShow.length));


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }





        TextView titleView = findViewById(R.id.news_detail_title);
        titleView.setText(title);

        TextView pubDateView = findViewById(R.id.news_detail_pubDate);
        pubDateView.setText(pubDate);

        //TextView authorView = findViewById(R.id.news_detail_author);
        //authorView.setText(author);

        TextView descripionView = findViewById(R.id.news_detail_description);
        descripionView.setText(description);



        Button linkButton = findViewById(R.id.link_button);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(view.getContext(), OriginalNewsActivity.class);
                intent1.putExtra("link", link);

                startActivity(intent1);
            }
        });
        //linkButton.setText(link);

        /**
         * 添加到收藏
         */
        Button favorateButton = findViewById(R.id.favorate_button);
        favorateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RssItem item = new RssItem();
                item.setTitle(title);
                item.setDescription(description);
                item.setLink(link);
                item.setPubdate(pubDate);

                Log.e("user favorate", username);

                DatabaseHandler.favorateNews(username, item, getApplicationContext());
            }
        });

        Button shareButton = findViewById(R.id.share_wechat_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WXTool.getInstance().shareUrl(1, link, title, description);
            }
        });

    }

    public void requestImage(String url, final List<Bitmap> imageList){
        //final ImageView tempView = mImageView;
        //Bitmap image;
        ImageRequest request =new ImageRequest(url,
                new Response.Listener<Bitmap>(){
            @Override
            public void onResponse(Bitmap bitmap){
                Toast.makeText(NewsDetailActivity.this,"success",Toast.LENGTH_SHORT).show();
                imageList.add(bitmap);
            }
        },160,120,Bitmap.Config.RGB_565,new Response.ErrorListener(){
            public void onErrorResponse(VolleyError error){

            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }





}
