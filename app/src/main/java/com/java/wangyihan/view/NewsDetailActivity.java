package com.java.wangyihan.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.java.wangyihan.data.database.mongodb.MongodbHelper;
import com.java.wangyihan.data.database.room.DatabaseHandler;
import com.java.wangyihan.data.model.Comment;
import com.java.wangyihan.data.model.ImageUrlFetcher;
import com.java.wangyihan.data.model.RssItem;
import com.java.wangyihan.R;
import com.java.wangyihan.data.model.User;
import com.java.wangyihan.util.ImageService;
import com.java.wangyihan.util.Tools;
import com.java.wangyihan.util.WXTool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;


public class NewsDetailActivity extends AppCompatActivity implements Runnable{
    private String title;
    private String pubDate;
    private String author;
    private String description;
    private String linkDescription;
    private String link;

    private String username;
    private byte[] imageToShow = new byte[0];

    private static TextToSpeech textToSpeech;

    @Override
    public void run() {

        try
        {
            ImageUrlFetcher.getInstance().getUrlList(link);
            List<String> imageLinks = ImageUrlFetcher.getInstance().getList();
            int mSize = 0;
            imageToShow = new byte[0];
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



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        mHandler.sendEmptyMessage(0);

        try
        {
            Socket socket = new Socket("123.206.43.232", 8889);
            BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter os=new PrintWriter(socket.getOutputStream());

            Toast.makeText(this, "等待NER解析", Toast.LENGTH_SHORT).show();

            os.println(description);
            os.flush();

            linkDescription = is.readLine();

            os.close();
            is.close();
            socket.close();

            mHandler.sendEmptyMessage(1);

        }
        catch (IOException e)//如果未能连接
        {
            Toast.makeText(this, "未能连接NER服务器", Toast.LENGTH_SHORT).show();
        }
    }

    static
    {

    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //完成主界面更新,拿到数据
                    //String data = (String)msg.obj;

                    ImageView imageView = findViewById(R.id.news_detail_image);
                    Bitmap detailImage = BitmapFactory.decodeByteArray(imageToShow, 0, imageToShow.length);


                    if (detailImage != null && (detailImage.getHeight() > 4096 || detailImage.getWidth() > 4096))
                    {
                        Toast.makeText(imageView.getContext(), "图片太大，显示失败", Toast.LENGTH_SHORT).show();
                    }

                    //detailImage = Bitmap.createScaledBitmap(detailImage, 600,true);
                    imageView.setImageBitmap(detailImage);
                    //textView.setText(data);
                    break;
                case 1:
                    TextView descripionView = findViewById(R.id.news_detail_description);
                    CharSequence charSequence = Html.fromHtml(linkDescription);
                    descripionView.setMovementMethod(LinkMovementMethod.getInstance());
                    descripionView.setText(charSequence);
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Configure.noBarTheme);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent intent = getIntent();
        title = intent.getStringExtra("title");
        pubDate = intent.getStringExtra("pubDate");
        //author = intent.getStringExtra("author");
        description = intent.getStringExtra("description");
        link = intent.getStringExtra("link");
        username = intent.getStringExtra("username");
        //Log.e("deHtml", description);
        description = Tools.deHtml(description);

        imageToShow = new byte[0];
        ImageView imageView = findViewById(R.id.news_detail_image);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageToShow, 0, imageToShow.length));


        TextView titleView = findViewById(R.id.news_detail_title);
        titleView.setText(title);

        TextView pubDateView = findViewById(R.id.news_detail_pubDate);
        pubDateView.setText(pubDate);

        TextView descripionView = findViewById(R.id.news_detail_description);
        if (linkDescription != null && !linkDescription.isEmpty())
            descripionView.setText(linkDescription);
        else
        {

            descripionView.setText(description);
        }



        if (textToSpeech == null)
        {
            textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == textToSpeech.SUCCESS) {
                        int result = textToSpeech.setLanguage(Locale.CHINA);
                        if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                                && result != TextToSpeech.LANG_AVAILABLE){
                            Toast.makeText(getApplicationContext(), "TTS暂时不支持这种语音的朗读！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        Thread thread = new Thread(this);
        thread.start();


        /**
         * 显示评论列表
         */
        List<Comment> commentList = MongodbHelper.getInstance().getCommentByTitle(title);
        ListView lv = findViewById(R.id.comment_list);

        List<Map<String, Object>> commentMapList = new ArrayList<Map<String, Object>>();
        for (Comment comment: commentList)
        {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("author", comment.getUsername());
            item.put("content", comment.getComment());

            commentMapList.add(item);
        }

        SimpleAdapter sa = new SimpleAdapter(this, commentMapList, R.layout.fragment_comment_display, new String[]{"author", "content"}, new int[]{R.id.comment_author, R.id.comment_content});

        lv.setAdapter(sa);

        /**
         * 朗读按钮
         */
        Button soundButton = findViewById(R.id.text_to_sound_button);
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(description, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        Button linkButton = findViewById(R.id.link_button);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(view.getContext(), OriginalNewsActivity.class);
                intent1.putExtra("link", link);

                startActivity(intent1);
            }
        });

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
                item.setImage(imageToShow);

                Log.e("user favorate", username);

                DatabaseHandler.favorateNews(username, item, getApplicationContext());
            }
        });

        Button shareButton = findViewById(R.id.share_wechat_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageToShow.length > 0)
                    WXTool.getInstance().shareUrl(1, link, title, description, BitmapFactory.decodeByteArray(imageToShow, 0, imageToShow.length));
                else
                    WXTool.getInstance().shareUrl(1, link, title, description, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_lightnews));
            }
        });


        Button commentButton = findViewById(R.id.comment_button);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText commentText = view.findViewById(R.id.comment_input);
                String comment = commentText.getText().toString();


                if (!TextUtils.isEmpty(comment))
                {
                    MongodbHelper.getInstance().addComment(username, title, comment);
                }
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
