package com.fenix.e.network;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fenix.e.extra.E;

import static com.fenix.e.extra.E.*;


public class ENetworkService extends Service {

    public static final String TAG = "NET_SERVICE";

    public static int userID = 42;

    //Connection state integers
    public final static int DISCONNECTED = 0;
    public final static int CONNECTED = 1;

    //Preferences reference for accessing local user data
    private static SharedPreferences prefs;

    //Service active state
    boolean active = false;;

    //References to the connection instance, thread and handler
    private EConnection connection;
    private Thread networkThread;
    private Handler networkHandler;

    //Connection state
    private static boolean connected = false;
    private static boolean loggedIn = false;

    public static void setConnected(boolean connected) {
        if (connected) {
            ENetworkService.connected = true;
            Log.d(TAG, "Connection state updated - CONNECTED");
        } else {
            setLoggedIn(false);
            ENetworkService.connected = false;
            Log.d(TAG, "Connection state updated - DISCONNECTED");
        }
    }

    public static boolean isConnected() {
        return connected;
    }

    public static void setLoggedIn(boolean loggedIn) {
        if (loggedIn) {
            ENetworkService.loggedIn = true;
            Log.d(TAG, "Login state updated - LOGGED IN");

            prefs.edit().putBoolean("localLogin", true).apply();
            Log.d(TAG, "Login success recorded.");
        } else {
            ENetworkService.loggedIn = false;
            Log.d(TAG, "Login state updated - LOGGED OUT");
        }
    }

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!active) start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void start() {
        //Mark as activated
        active = true;

        //Load app preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Start broadcast listener
        startBroadcastListener();

        //Start connection on separate thread
        startConnection();
    }

    private void startBroadcastListener() {
        //Start local broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(createBroadcastReceiver(), createBroadcastFilter());
        Log.d(TAG, "Started background network service receiver.");
    }

    private IntentFilter createBroadcastFilter() {
        IntentFilter broadcastFilter = new IntentFilter();

        //Actions to listen for
        broadcastFilter.addAction(REQUEST_LOGIN);
        broadcastFilter.addAction(REQUEST_SIGNUP);
        broadcastFilter.addAction(SEND_MESSAGE);

        return broadcastFilter;
    }

    private BroadcastReceiver createBroadcastReceiver() {
        return new BroadcastReceiver() {
            //Handle requested actions
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(intent.getAction()){
                    case REQUEST_LOGIN:
                        Log.d(TAG, "Received login request.");
                        processLoginRequest(intent);
                        break;

                    case REQUEST_SIGNUP:
                        Log.d(TAG, "Received signup request.");
                        processSignupRequest(intent);
                        break;

                    case  SEND_MESSAGE:
                        Log.d(TAG, "Received request to send a message.");
                        processMessageSendRequest(intent);
                        break;
                }
            }
        };
    }

    private void startConnection() {
        networkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                networkHandler = new Handler();
                connection = new EConnection();
                connection.connectSocket();
                testSend("Hello");
                Looper.loop();
            }
        });
        networkThread.start();
    }

    private void processLoginRequest(Intent loginIntent) {
        Bundle loginData = loginIntent.getExtras();
        String username = loginData.getString(BUNDLE_USERNAME, null);
        String password = loginData.getString(BUNDLE_PASSWORD, null);
        Log.d(TAG, "Processing login request " +
                "for user [" + username + "] " +
                "with password [" + password + "] " +
                "from background service.");
    }

    private void processSignupRequest(Intent signupIntent) {
        Bundle signupData = signupIntent.getExtras();
        if (signupData != null) {
            String username = signupData.getString(BUNDLE_USERNAME, null);
            String password = signupData.getString(BUNDLE_PASSWORD, null);
            String displayName = signupData.getString(BUNDLE_DISPLAY_NAME, null);
            Log.d(TAG, "Processing signup request " +
                    "for user [" + username + "] " +
                    "with password [" + password + "] " +
                    "and the display name [" + displayName + "] " +
                    "from background service.");
        }
    }

    private void processMessageSendRequest(Intent messageIntent) {
        //
    }

    private void testSend(final String message) {
        networkHandler.post(new Runnable() {
            @Override
            public void run() {
                connection.sendMessage(message);
            }
        });
    }

}
