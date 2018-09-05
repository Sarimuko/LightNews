package com.java.wangyihan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.java.wangyihan.Data.Category;
import com.java.wangyihan.Data.DataBaseHandler.DatabaseHandler;
import com.java.wangyihan.Data.RssItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private Context rootContext;
    private View root;

    private List<Category> checkedList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private List<Map<String,Object>> categoryAdapterList = new ArrayList<Map<String,Object> >();

    // TODO: Rename and change types of parameters


    private OnFragmentInteractionListener mListener;

    public CategoryListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryListFragment newInstance() {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_category_list, container, false);

        categoryList = DatabaseHandler.getAllCategories(rootContext.getApplicationContext());
        for (Category category: categoryList)
        {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("name", category.getName());
            item.put("link", category.getUrl());
        }

        ListView lv = (ListView)(root.findViewById(R.id.category_list));
        SimpleAdapter sa = new SimpleAdapter(rootContext,
                categoryAdapterList,//data 不仅仅是数据，而是一个与界面耦合的数据混合体
                R.layout.fragment_category_checkbox,
                new String[] {"name", "link"},//from 从来来
                new int[] {R.id.checkable_category, R.id.category_link}//to 到那里去
        );
        lv.setAdapter(sa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CheckedTextView check = (CheckedTextView)(view.findViewById(R.id.checkable_category));
                TextView textView = view.findViewById(R.id.category_link);
                check.setChecked(!check.isChecked());

                if (mListener != null)
                {
                    mListener.onFragmentInteraction(check.getText().toString(), textView.getText().toString(), check.isChecked());
                }
            }
        });

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event


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
        void onFragmentInteraction(String name, String link, boolean checked);
    }
}
