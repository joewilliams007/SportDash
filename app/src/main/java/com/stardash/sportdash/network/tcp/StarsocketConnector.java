package com.stardash.sportdash.network.tcp;

import android.os.Handler;
import android.os.Looper;

import com.stardash.sportdash.settings.Account;

public class StarsocketConnector {

    static int nodeFileReceiveServerPort = 2222;
    static int nodeFileSendServerPort = 2223;
    static int nodeMessageSendServerPort = 2224;
    static int nodeMessageReceiveServerPort = 2225;

    static void getFile(String destination){
        aClientsocket socket = new aClientsocket(nodeFileSendServerPort);
        socket.receiveFile(destination);
        socket.stopConnection();
    }

    static void sendSavedFile(String path){
        aClientsocket socket = new aClientsocket(nodeFileReceiveServerPort);
        socket.sendSavedFile(path);
        socket.stopConnection();
    }

    static void sendFileBinaries(byte[] fileData){
        aClientsocket socket = new aClientsocket(nodeFileReceiveServerPort);
        socket.sendBytes(fileData);
        socket.stopConnection();
    }

    public static void sendMessage(String message){

                try  {
                    aClientsocket socket = new aClientsocket(nodeMessageSendServerPort);
                    socket.sendMessage(Account.userid()+" "+Account.password()+" "+Account.username()+" §"+message);
                    socket.stopConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }


    }


    public static String getMessage(){


                    final Handler handler = new Handler(Looper.getMainLooper());
                    aClientsocket socket = new aClientsocket(nodeMessageReceiveServerPort);
                    String message = socket.receiveMessage();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            socket.stopConnection();
                        }
                    }, 300);

        return message;
    }


    static byte[] getFileBinaries(){
        aClientsocket socket = new aClientsocket(nodeFileSendServerPort);
        byte[] data = socket.receiveFileBinaries();
        socket.stopConnection();
        return data;
    }

}
