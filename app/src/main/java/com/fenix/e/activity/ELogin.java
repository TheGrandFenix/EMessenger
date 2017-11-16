package com.fenix.e.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.fenix.e.R;
import com.fenix.e.extra.E;
import com.fenix.e.extra.LoginPagerAdapter;


public class ELogin extends FragmentActivity {

    private final static String TAG = "ACTIVITY_LOGIN";

    private BroadcastReceiver broadcastReceiver;
    private ViewPager loginPager;
    private ProgressBar loadingIcon;

    private String username;
    private String password;

    //Prepare activity on creation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_pager_layout);

        //Setup login pager
        loginPager = (ViewPager) findViewById(R.id.login_pager);
        LoginPagerAdapter loginPagerAdapter = new LoginPagerAdapter(getSupportFragmentManager());
        loginPager.setAdapter(loginPagerAdapter);

        //Set loading icon
        loadingIcon = (ProgressBar) findViewById(R.id.loading_icon);
    }


    //Register receiver when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        startReceiver();
    }

    //Unregister receiver when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        stopReceiver();
    }


    //Make immersive when activity gains focus
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            makeImmersive();
        }
    }


    @Override
    public void onBackPressed() {
        if (loginPager.getCurrentItem() == 1)
            loginPager.setCurrentItem(0, true);
        Log.d(TAG, "Returned to base login page.");
    }


    private void startReceiver() {
        //Create new receiver instance
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    case E.COMPLETED_LOGIN:
                        handleSuccessfulLogin();
                        break;

                    case E.FAILED_LOGIN:
                        handleFailedLogin();
                        break;
                }
            }
        };

        //Listen for login completion status
        IntentFilter broadcastFilter = new IntentFilter();
        broadcastFilter.addAction(E.COMPLETED_LOGIN);
        broadcastFilter.addAction(E.FAILED_LOGIN);

        //Register local broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, broadcastFilter);

        Log.d(TAG, "Started login activity receiver.");
    }

    private void stopReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    //Method for entering immersive fullscreen
    private void makeImmersive() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    //Login procedure called from BaseLoginFragment
    public void requestLogin(String username, String password) {
        startLoading();
        Intent loginRequest = new Intent(E.REQUEST_LOGIN);
        loginRequest.putExtra(E.BUNDLE_USERNAME, username);
        loginRequest.putExtra(E.BUNDLE_PASSWORD, password);
        LocalBroadcastManager.getInstance(this).sendBroadcast(loginRequest);
    }

    public void localizeCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void continueSignup() {
        loginPager.setCurrentItem(1, true);
        Log.d(TAG, "Switched to extra login page.");
    }

    public void requestSignup(String displayName) {
        startLoading();
        Intent loginRequest = new Intent(E.REQUEST_SIGNUP);
        loginRequest.putExtra(E.BUNDLE_USERNAME, username);
        loginRequest.putExtra(E.BUNDLE_PASSWORD, password);
        loginRequest.putExtra(E.BUNDLE_DISPLAY_NAME, displayName);
        LocalBroadcastManager.getInstance(this).sendBroadcast(loginRequest);
    }

    private void startLoading() {
        loginPager.setVisibility(View.GONE);
        loadingIcon.setVisibility(View.VISIBLE);
    }

    private void stopLoading() {
        loginPager.setVisibility(View.VISIBLE);
        loadingIcon.setVisibility(View.GONE);
    }

    public void handleSuccessfulLogin() {
        //Proceed to chat activity
    }

    public void handleFailedLogin() {
        stopLoading();
        //Display error message
    }
}
