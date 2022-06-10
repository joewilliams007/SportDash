package com.stardash.sportdash.network.tcp;

import android.os.Handler;
import android.os.Looper;

public class getMessage implements Runnable{
    public static volatile String value;

    @Override
    public void run() {
        try {
            final Handler handler = new Handler(Looper.getMainLooper());
            aClientsocket socket = new aClientsocket(2225);
            String message = socket.receiveMessage();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    socket.stopConnection();
                }
            }, 300);
            value = message;
        } catch (Exception ignored){

        }
    }

    public static String getAsyncMessage() {
        return value;
    }
}
