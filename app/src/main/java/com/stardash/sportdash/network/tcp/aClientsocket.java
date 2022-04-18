package com.stardash.sportdash.network.tcp;

import com.stardash.sportdash.settings.Account;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class aClientsocket {

    static int serverPort;
    static String ipAddress;
    static boolean localhost;

    Socket socket;
    OutputStream toServer;
    InputStream fromServer;

    public aClientsocket(int port){

        serverPort = port;
        try {

            String ip = "87.184.96.149";
            if (Account.localhost()) {
                ip = "192.168.2.102";
            }
            socket = new Socket(ip, serverPort);

            toServer = socket.getOutputStream();
            fromServer = socket.getInputStream();
            System.out.println("successfully connected to server!");

        } catch (UnknownHostException ex) {
            System.out.println("server can not be found.");
        } catch (IOException e) {
            System.out.println("an error occurred while waiting for server response.");
        }
    }

    public void stopConnection(){
        try {
            socket.close();
            toServer.close();
            fromServer.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public void sendMessage(String message) {
        System.out.println("Trying to send string message to server...");
        try{
            toServer.write(message.getBytes());
        }
        catch(IOException exception){
            System.out.println("An error occured while thrying to send the message");
        }
        System.out.println("Message send attempt over. ");
    }

    public String receiveMessage() {
        String line = "No Answer";
        System.out.println("Trying to receive message from server...");
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            while(true){
                byte currentByte = (byte)fromServer.read();
                if (currentByte == -1){break;}
                byteStream.write(currentByte);
            }

            byte[] data = byteStream.toByteArray();
            line = new String(data);
        }
        catch (IOException e) {
            System.out.println("An error occured while thrying to read the message");
        }
        System.out.println("Message receive attempt over. ");
        return line;
    }

    public void sendSavedFile(String path)
    {
        System.out.println("Trying to send file to server...");
        File file = new File(path);
        FileInputStream filestream;
        try {

            filestream = new FileInputStream(file);

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            while(true){
                byte currentByte = (byte)filestream.read();
                if (currentByte == -1){break;}
                byteStream.write(currentByte);
            }

            byte[] data = byteStream.toByteArray();
            toServer.write(data);

            filestream.close();
        }
        catch (FileNotFoundException exception){
            System.out.println("The file could not be found.");
        }
        catch (IOException exception){
            System.out.println("Trying to save data to the file resulted in an error.");
        }
    }

    public void sendBytes(byte[] data)
    {
        System.out.println("Trying to send file as bytes to server...");
        try {
            toServer.write(data);
        }
        catch (FileNotFoundException exception){
            System.out.println("The file could not be found.");
        }
        catch (IOException exception){
            System.out.println("Trying to save data to the file resulted in an error.");
        }
    }

    public void receiveFile(String destination){
        System.out.println("Trying to receive file from server...");
        File file = new File(destination);
        FileOutputStream filestream;
        try {
            filestream = new FileOutputStream(file);

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            while(true){
                byte currentByte = (byte)fromServer.read();
                if (currentByte == -1){break;}
                byteStream.write(currentByte);
            }

            byte[] data = byteStream.toByteArray();
            filestream.write(data);
            filestream.close();
        }
        catch (FileNotFoundException exception){
            System.out.println("The destinantion path is invalid.");
        }
        catch (IOException exception){
            System.out.println("Trying to receive the file resulted in an error.");
        }
    }

    public byte[] receiveFileBinaries(){
        System.out.println("Trying to receive file from server...");
        byte[] data = null;

        try {

            Boolean streamHasStarted = false;
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            while(true){
                byte currByte = (byte) fromServer.read();
                if(currByte == -1) { break; }
                byteStream.write(currByte);
            }
            data = byteStream.toByteArray();

        }
        catch (IOException exception){
            System.out.println("Trying to receive the file resulted in an error.");
            data = new byte[11];
        }
        return data;
    }
}

