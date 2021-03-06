package com.java.wangyihan.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.java.wangyihan.data.database.room.DatabaseHandler;
import com.java.wangyihan.data.model.*;
import com.java.wangyihan.R;
import com.java.wangyihan.util.Tools;

import java.util.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class NewsFragment extends Fragment implements Runnable{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_NUM = "linkNum";
    private static final String ARG_LINKS = "links";
    private static final String ARG_NAMES = "names";
    private static final String ARG_IDS = "IDs";

    static boolean startFlag = true;

    //private String username = "default user";

    private View root;
    private Context rootContext;

    RssFeed mRssFeed;
    List<RssItem> findList = new ArrayList<RssItem>();
    private List<Map<String,Object> > newsList = new ArrayList<Map<String,Object> >();
    List<String> readList = new ArrayList<String>();
    int pos = 0;


    // TODO: Rename and change types of parameters
    private int mNum = 0;
    private ArrayList<String> mLinks;
    //private ArrayList<String> mNames;
    private ArrayList<Integer> mIDs;

    public void showRecommend(User user)
    {
        for (int i=0;i<3;i++)
        {
            Category category = user.getRecommend(rootContext.getApplicationContext());
            if (category != null)
            {
                mLinks.add(category.getUrl());
                mIDs.add(new Long(category.getId()).intValue());
            }
        }

        refetch();
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //完成主界面更新,拿到数据
                    String data = (String)msg.obj;

                    update();
                    //textView.setText(data);
                    break;
                default:
                    break;
            }
        }

    };

    private OnFragmentInteractionListener mListener;

    public NewsFragment() {
        // Required empty public constructor
    }

    /*public void setUsername(String username) {
        this.username = username;
    }*/

    class MySimpleAdapter extends SimpleAdapter
    {
        NavigationActivity navigationActivity = (NavigationActivity) rootContext;

        MySimpleAdapter(Context context, List<Map<String, Object>> dataList, int id, String[] headers, int[] view_ids)
        {
            super(context, dataList, id, headers, view_ids);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = super.getView(position, convertView, parent);
            HashMap<String, Object> item = (HashMap<String, Object>)getItem(position);
            if (readList.contains(item.get("title")) && navigationActivity.getState() != NavigationActivity.State.FAVORATE && navigationActivity.getState() != NavigationActivity.State.LOCAL)
            {
                view.setBackgroundColor(rootContext.getColor(R.color.colorClickedItem));
            }
            else
            {
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = view.getContext().getTheme();
                try {
                    theme.resolveAttribute(R.attr.colorTextBackground, typedValue, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Resources resources = getResources();
                try {
                    int color = view.getContext().getColor(typedValue.resourceId); // 获取颜色值
                    view.setBackgroundColor(color);
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }

            return view;

        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     *
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(ArrayList<Category> categories) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        //args.putInt(ARG_NUM, num);
        ArrayList<String> links = new ArrayList<String>();
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<Integer> IDs = new ArrayList<Integer>();
        for (Category category: categories)
        {
            links.add(category.getUrl());
            names.add(category.getName());
            Long id = category.getId();
            IDs.add(id.intValue());
        }



        args.putStringArrayList(ARG_LINKS, links);
        args.putStringArrayList(ARG_NAMES, names);
        args.putIntegerArrayList(ARG_IDS, IDs);


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mNames = getArguments().getStringArrayList(ARG_NAMES);
            mLinks = getArguments().getStringArrayList(ARG_LINKS);
            mIDs = getArguments().getIntegerArrayList(ARG_IDS);
        }

    }

    public void showLocal()
    {
        mRssFeed = new RssFeed();
        //Log.e("username", username);
        mRssFeed.setRssItems(DatabaseHandler.getAllRead(rootContext.getApplicationContext()));

        Log.e("nav", "local" + Integer.toString(mRssFeed.getItemCount()));

        update();
    }
    public void showFavorate(String username)
    {
        mRssFeed = new RssFeed();
        //Log.e("username", username);
        mRssFeed.setRssItems(DatabaseHandler.getFavorateByUser(rootContext.getApplicationContext(), username));

        Log.e("nav", "favorate" + Integer.toString(mRssFeed.getItemCount()));

        update();

    }

    @Override
    public void run() {
        mRssFeed = new RssFeed();
        Log.e("mLink", Integer.toString(mLinks.size()));
        try
        {
            for (int i=0;i<mLinks.size();i++)
            {
                RssFeed rssFeed = RssFeed_SAXParser.getInstance().getFeed(mLinks.get(i), mIDs.get(i));
                Log.e("get rssFeed", Integer.toString(rssFeed.getItemCount()));
                for (int j=0;j<rssFeed.getItemCount();j++)
                {
                    mRssFeed.addItem(rssFeed.getItem(j));
                }
            }

            if (mRssFeed == null)
            {
                mRssFeed = new RssFeed();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Log.e("mRssFeed", Integer.toString(mRssFeed.getItemCount()));

        mHandler.sendEmptyMessage(0);
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public void showHome()
    {
        NavigationActivity navigationActivity = (NavigationActivity)rootContext;
        List<Category> categoryList = navigationActivity.categoryList;

        mLinks.clear();
        for (Category category: categoryList)
        {
            if (Tools.isURL(category.getUrl()))
            {
                mLinks.add(category.getUrl());
                mIDs.add((new Long(category.getId()).intValue()));
                //Log.e("valid url", category.getUrl());
            }
        }

        Log.e("count of mLinks", Integer.toString(mLinks.size()));

        refetch();
    }

    public void refetch()
    {
        if (!isNetworkConnected(rootContext.getApplicationContext()))
        {
            Toast.makeText(rootContext, "无网络连接", Toast.LENGTH_SHORT).show();
            return;
        }

        Thread thread = new Thread(this);
        try
        {
            thread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //update();
    }



    public void update ()
    {
        //Log.e("refetched", Integer.toString(mRssFeed.getItemCount()));
        showList(mRssFeed.getItems());
    }

    private void nextPage(final List<RssItem> itemList, int pos)
    {

        ListView lv = root.findViewById(R.id.news_list);

        MySimpleAdapter sa = (MySimpleAdapter)lv.getAdapter();
        for (int i=pos;i<Math.min(pos + 10, itemList.size());i++)
        {
            Map<String,Object> item = new HashMap<String,Object>();
            //一行记录，包含多个控件
            item.put("title", itemList.get(i).getTitle());
            item.put("pubDate", itemList.get(i).getPubdate());
            this.pos ++;

            newsList.add(item);

        }

        sa.notifyDataSetChanged();

    }

    private void showList(final List<RssItem> itemList)
    {
        Collections.shuffle(itemList);
        newsList.clear();
        pos = 0;

        for (int i=0;i<Math.min(10, itemList.size());i++)
        {
            Map<String,Object> item = new HashMap<String,Object>();
            //一行记录，包含多个控件
            item.put("title", itemList.get(i).getTitle());
            item.put("pubDate", itemList.get(i).getPubdate());
            pos ++;

            newsList.add(item);

        }



        ListView lv = (ListView)(root.findViewById(R.id.news_list));
        final MySimpleAdapter sa = new MySimpleAdapter(rootContext,
                newsList,//data 不仅仅是数据，而是一个与界面耦合的数据混合体
                R.layout.fragment_news_display,
                new String[] {"title","pubDate"},//from 从来来
                new int[] {R.id.news_title,R.id.news_date}//to 到那里去

        );
        lv.setAdapter(sa);

        lv.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState){
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getFirstVisiblePosition() == 0) {
                        //加载更多功能的代码
                        refetch();
                        Toast.makeText(rootContext, "刷新成功", Toast.LENGTH_SHORT).show();
                    }
                    if (view.getLastVisiblePosition() == view.getCount() - 1)
                    {
                        nextPage(itemList, pos);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                RssItem item = itemList.get(i);

                //view.setBackgroundColor(rootContext.getColor(R.color.colorClickedItem));
                readList.add(item.getTitle());
                sa.notifyDataSetChanged();

                Intent intent = new Intent(view.getContext(), NewsDetailActivity.class);
                intent.putExtra("title", item.getTitle());

                //Log.e("raw description", item.getDescription());
                intent.putExtra("description", item.getDescription());
                intent.putExtra("pubDate", item.getPubdate());
                intent.putExtra("link", item.getLink());

                NavigationActivity navigationActivity = (NavigationActivity)rootContext;
                intent.putExtra("username", navigationActivity.getUser().getUsername());

                DatabaseHandler.readNews(navigationActivity.getUser(), item, rootContext.getApplicationContext());

                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();


        SearchView searchView = root.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query))
                {
                    Toast.makeText(rootContext, "请输入查找内容！", Toast.LENGTH_SHORT).show();
                    //refetch();
                    //update();
                }
                else
                {
                    findList.clear();
                    for(int i = 0; i < mRssFeed.getItemCount(); i++)
                    {
                        RssItem information = mRssFeed.getItem(i);
                        if(information.getTitle().contains(query))
                        {
                            findList.add(information);
                            break;
                        }
                    }
                    if(findList.size() == 0)
                    {
                        Toast.makeText(rootContext, "没有相关新闻", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(rootContext, "查找成功", Toast.LENGTH_SHORT).show();
                        showList(findList);
                    }
                }
                return true;

            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_news, container, false);
        if (startFlag)
        {
            NavigationActivity navigationActivity = (NavigationActivity)rootContext;
            showRecommend(navigationActivity.getUser());
            startFlag = false;
        }

        return root;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        rootContext = context;

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
