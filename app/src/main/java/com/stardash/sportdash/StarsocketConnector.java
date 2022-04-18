package com.stardash.sportdash;

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
        aClientsocket socket = new aClientsocket(nodeMessageSendServerPort);
        socket.sendMessage(message);
        socket.stopConnection();
    }


    public static String getMessage(){
        aClientsocket socket = new aClientsocket(nodeMessageReceiveServerPort);
        String message = socket.receiveMessage();
        socket.stopConnection();
        return message;
    }


    static byte[] getFileBinaries(){
        aClientsocket socket = new aClientsocket(nodeFileSendServerPort);
        byte[] data = socket.receiveFileBinaries();
        socket.stopConnection();
        return data;
    }

}
