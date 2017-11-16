package com.fenix.e.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.fenix.e.R;
import com.fenix.e.network.ENetworkService;


public class EStartup extends Activity {
    //Shared preferences
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set blank UI layout and make it fullscreen
        setContentView(R.layout.loading_layout);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //Load shared preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Start network service
        Intent networkService = new Intent(this, ENetworkService.class);
        startService(networkService);

        //Check if user is locally logged in
        boolean localLogin = prefs.getBoolean("localLogin", false);
        if (localLogin)
            startChatActivity();
        else
            startLoginActivity();
    }

    //Start the main E chat activity
    private void startChatActivity() {
        Intent chatIntent = new Intent(this, EChat.class);
        startActivity(chatIntent);
        finish();
    }

    //Start the login activity
    private void startLoginActivity() {
        Intent loginIntent = new Intent(this, ELogin.class);
        startActivity(loginIntent);
        finish();
    }
}
