package com.kotenkov.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ArchiveServer {

    private ServerSocket ss;
    private Socket socket;
    private BufferedReader fromClient;
    private BufferedWriter toClient;

    public ArchiveServer(ServerSocket ss) throws IOException {
        this.ss = ss;
        socket = ss.accept();
        fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        toClient = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
    }

    public void sendMessage(String message){
        try {
            toClient.write(message);
            toClient.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage(){
        String response = null;
        try {
            response = fromClient.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
