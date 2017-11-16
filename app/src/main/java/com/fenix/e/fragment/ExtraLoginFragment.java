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

public class ExtraLoginFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "FRAG_LOGIN_EXTRA";

    private ELogin activity;

    private EditText displayNameInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_extra_layout, container, false);

        activity = (ELogin) getActivity();

        displayNameInput = (EditText) view.findViewById(R.id.display_name_input);
        Button completeSignupButton = (Button) view.findViewById(R.id.complete_signup_button);
        completeSignupButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        String displayName = displayNameInput.getText().toString();
        activity.requestSignup(displayName);
        Log.d(TAG, "Signup pressed.");
    }
}
