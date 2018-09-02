package com.java.wangyihan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.java.wangyihan.Data.RssItem;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsDisplayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsDisplayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_Title = "title";
    private static final String ARG_Date = "date";

    private static final String ARG_Description = "description";
    private static final String ARG_Link = "link";
    private static final String ARG_Category = "category";

    // TODO: Rename and change types of parameters
    private String content;
    private String link;
    private String date;
    private String title;
    private String category;

    private OnFragmentInteractionListener mListener;

    public NewsDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     *      * this fragment using the provided parameters.
     * @param item
     * @return
     */
    // TODO: Rename and change types and number of parameters
    public static NewsDisplayFragment newInstance(RssItem item) {
        NewsDisplayFragment fragment = new NewsDisplayFragment();
        Bundle args = new Bundle();

        args.putString(ARG_Description, item.getDescription());
        args.putString(ARG_Date, item.getPubdate());
        args.putString(ARG_Link, item.getLink());
        args.putString(ARG_Title, item.getTitle());
        args.putString(ARG_Category, item.getCategory());


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        title = args.getString(ARG_Title);
        date = args.getString(ARG_Date);
        link = args.getString(ARG_Link);
        content = args.getString(ARG_Description);
        category = args.getString(ARG_Category);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            title = getArguments().getString(ARG_Title);
            date = getArguments().getString(ARG_Date);
            link = getArguments().getString(ARG_Link);
            content = getArguments().getString(ARG_Description);
            category = getArguments().getString(ARG_Category);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_news_display, container, false);
        TextView titleView = root.findViewById(R.id.news_title);
        titleView.setText(title);
        TextView dateView = root.findViewById(R.id.news_date);
        dateView.setText(date);

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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
