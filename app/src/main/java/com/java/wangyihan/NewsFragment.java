package com.java.wangyihan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.java.wangyihan.Data.DataBaseHandler.DatabaseHandler;
import com.java.wangyihan.Data.RssFeed;
import com.java.wangyihan.Data.RssFeed_SAXParser;
import com.java.wangyihan.Data.RssItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private static final String ARG_NUM = "linkNum";
    private static final String ARG_LINKS = "links";
    private static final String ARG_NAMES = "names";

    private String username = "default user";

    private View root;
    private Context rootContext;

    RssFeed mRssFeed;
    List<RssItem> findList = new ArrayList<RssItem>();
    private List<Map<String,Object> > newsList = new ArrayList<Map<String,Object> >();
    List<Long> readList = new ArrayList<Long>();


    // TODO: Rename and change types of parameters
    private int mNum = 0;
    private ArrayList<String> mLinks;
    private ArrayList<String> mNames;

    private OnFragmentInteractionListener mListener;

    public NewsFragment() {
        // Required empty public constructor
    }

    public void setUsername(String username) {
        this.username = username;
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
    public static NewsFragment newInstance(int num, ArrayList<String> links, ArrayList<String> names) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUM, num);
        args.putStringArrayList(ARG_LINKS, links);
        args.putStringArrayList(ARG_NAMES, names);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNum = getArguments().getInt(ARG_NUM);
            mNames = getArguments().getStringArrayList(ARG_NAMES);
            mLinks = getArguments().getStringArrayList(ARG_LINKS);
        }

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
        try
        {
            for (int i=0;i<mNum;i++)
            {
                RssFeed rssFeed = RssFeed_SAXParser.getInstance().getFeed(mLinks.get(i));
                for (int j=0;j<rssFeed.getItemCount();j++)
                {
                    mRssFeed.addItem(rssFeed.getItem(j));
                }
            }

            if (mRssFeed == null)
            {
                mRssFeed = new RssFeed();
                //mRssFeed.setRssItems(DatabaseHandler.getAllRead(rootContext.getApplicationContext()));
                //Log.e("info", "get date from database" + Integer.toString(mRssFeed.getItemCount()));
            }

        }
        catch (Exception e)
        {
            Log.e("E", e.getMessage());
        }
    }

    public void refetch()
    {
        Thread thread = new Thread(this);
        try
        {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        update();
    }

    public void update ()
    {
        //Log.e("refetched", Integer.toString(mRssFeed.getItemCount()));
        showList(mRssFeed.getItems());
    }

    private void showList(final List<RssItem> itemList)
    {
        newsList.clear();

        for (int i=0;i<itemList.size();i++)
        {
            Map<String,Object> item = new HashMap<String,Object>();
            //一行记录，包含多个控件
            item.put("title", itemList.get(i).getTitle());
            item.put("pubDate", itemList.get(i).getPubdate());

            newsList.add(item);

        }

        class MySimpleAdapter extends SimpleAdapter
        {
            MySimpleAdapter(Context context, List<Map<String, Object>> dataList, int id, String[] headers, int[] view_ids)
            {
                super(context, dataList, id, headers, view_ids);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                if (readList.contains(getItemId(position)))
                {
                    view.setBackgroundColor(rootContext.getColor(R.color.colorClickedItem));
                }
                else
                {
                    view.setBackgroundColor(rootContext.getColor(R.color.cardview_light_background));
                }

                return view;

            }
        }

        ListView lv = (ListView)(root.findViewById(R.id.news_list));
        final MySimpleAdapter sa = new MySimpleAdapter(rootContext,
                newsList,//data 不仅仅是数据，而是一个与界面耦合的数据混合体
                R.layout.fragment_news_display,
                new String[] {"title","pubDate"},//from 从来来
                new int[] {R.id.news_title,R.id.news_date}//to 到那里去

        );
        lv.setAdapter(sa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                RssItem item = itemList.get(i);

                //view.setBackgroundColor(rootContext.getColor(R.color.colorClickedItem));
                readList.add(l);
                sa.notifyDataSetChanged();

                Intent intent = new Intent(view.getContext(), NewsDetailActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("description", item.getDescription());
                intent.putExtra("pubDate", item.getPubdate());
                intent.putExtra("link", item.getLink());
                intent.putExtra("username", username);

                DatabaseHandler.readNews(item, rootContext.getApplicationContext());

                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_news, container, false);

        NavigationActivity navigationActivity = (NavigationActivity) rootContext;
        if (navigationActivity.getState() == NavigationActivity.State.HOME)
        {
            refetch();
        }
        else if (navigationActivity.getState() == NavigationActivity.State.FAVORATE)
        {
            setUsername(navigationActivity.getUser());
            showFavorate(navigationActivity.getUser());
        }
        else if (navigationActivity.getState() == NavigationActivity.State.LOCAL)
        {
            //todo: local
            setUsername(navigationActivity.getUser());
            showFavorate(navigationActivity.getUser());
        }
        else if (navigationActivity.getState() == NavigationActivity.State.RECOMMEND)
        {
            //todo:recommend
        }


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
