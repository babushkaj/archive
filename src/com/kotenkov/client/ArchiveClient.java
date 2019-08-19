package com.kotenkov.client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ArchiveClient {

    private Socket socket;
    private BufferedReader fromServer;
    private BufferedWriter toServer;

    public ArchiveClient(Socket socket) throws IOException {
        this.socket = socket;
        this.socket.connect(new InetSocketAddress("localhost",8118), 2000);
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        toServer = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
    }

    public void sendMessage(String message){
        try {
            toServer.write(message);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage(){
        String response = null;
        try {
            response = fromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


}
