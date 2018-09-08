package com.java.wangyihan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.java.wangyihan.Data.DataBaseHandler.DatabaseHandler;
import com.java.wangyihan.Data.User;
import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;
    private Context rootContext;
    private View root;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        root = inflater.inflate(R.layout.fragment_login, container, false);

        Button submitButton = root.findViewById(R.id.sign_button);
        final TextInputEditText username_input = root.findViewById(R.id.username_input);
        final TextInputEditText email_input = root.findViewById(R.id.email_input);
        final TextInputEditText password_input = root.findViewById(R.id.password_input);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(username_input.getText().toString(), email_input.getText().toString(), password_input.getText().toString());
                username_input.clearComposingText();
                email_input.clearComposingText();
                password_input.clearComposingText();
            }
        });
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String user, String email, String password) {
        if (mListener != null) {
            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
            {
                Toast.makeText(rootContext, "请输入完整信息", Toast.LENGTH_SHORT).show();
            }
            else if (DatabaseHandler.hasUser(user, email, password, rootContext.getApplicationContext()) == -1)
            {
                Toast.makeText(rootContext, "登录失败", Toast.LENGTH_SHORT).show();
            }
            else if(DatabaseHandler.hasUser(user, email, password, rootContext.getApplicationContext()) == 0)
            {
                DatabaseHandler.register(user, email, password, rootContext.getApplicationContext());
                Toast.makeText(rootContext, "注册成功", Toast.LENGTH_SHORT).show();
                User userObj = DatabaseHandler.getUser(user, email, password, rootContext.getApplicationContext());
                mListener.onFragmentInteraction(userObj);
            }
            else
            {
                Toast.makeText(rootContext, "登录成功", Toast.LENGTH_SHORT).show();
                User userObj = DatabaseHandler.getUser(user, email, password, rootContext.getApplicationContext());
                mListener.onFragmentInteraction(userObj);
            }
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
        void onFragmentInteraction(User user);
    }
}
