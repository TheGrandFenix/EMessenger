package com.fenix.e.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fenix.e.R;
import com.fenix.e.activity.ELogin;

public class BaseLoginFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "FRAG_LOGIN";

    private ELogin activity;

    private EditText usernameInput;
    private EditText passwordInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_base_layout, container, false);

        activity = (ELogin) getActivity();

        usernameInput = (EditText) view.findViewById(R.id.username_input);
        passwordInput = (EditText) view.findViewById(R.id.password_input);

        Button loginButton = (Button) view.findViewById(R.id.login_button);
        Button signupButton = (Button) view.findViewById(R.id.signup_button);

        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        switch (v.getId()){
            case R.id.login_button:
                Log.d(TAG, "Login pressed.");
                activity.requestLogin(username, password);
                break;

            case R.id.signup_button:
                activity.localizeCredentials(username, password);
                activity.continueSignup();
                break;
        }
    }
}
