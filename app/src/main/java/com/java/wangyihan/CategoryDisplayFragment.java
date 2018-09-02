package com.java.wangyihan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryDisplayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryDisplayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CATEGORYNAME = "category_name";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mCategoryName;
    //private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View root;
    private TextView categoryText;

    public CategoryDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param category_name name of the category to be displayed.
     * @return A new instance of fragment CategoryDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryDisplayFragment newInstance(String category_name) {
        CategoryDisplayFragment fragment = new CategoryDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORYNAME, category_name);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        categoryText = root.findViewById(R.id.category_name);
        categoryText.setText(args.getString(ARG_CATEGORYNAME));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategoryName = getArguments().getString(ARG_CATEGORYNAME);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_category_display, container, false);
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
