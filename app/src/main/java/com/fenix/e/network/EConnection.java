package com.fenix.e.network;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class EConnection {

    private final static String TAG = "ECON_RUNNABLE";

    private ENetworkService service;
    private Socket socket;

    private DataOutputStream out;
    private DataInputStream in;

    void connectSocket() {
        Log.d(TAG, "Started network runnable.");
        try {
            socket = new Socket("192.168.1.32", 25081);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            ENetworkService.setConnected(true);
        } catch (IOException e) {
            Log.d(TAG, "Failed to connect to server...");
            e.printStackTrace();
        }
    }

    void sendMessage(String message) {
        try {
            out.writeInt(ENetworkService.userID);
            out.writeInt(0);
            out.writeInt(message.getBytes().length);
            out.write(message.getBytes());
            out.flush();
        } catch (IOException e) {
            Log.d(TAG, "Failed to send message: " + message);
            e.printStackTrace();
        }
    }
}
