package com.example.messengerproject;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection {

    private static ClientConnection instance;
    private Client client;

    private String ip;
    private ClientConnection(String ip){
        try {
            client= new Client(new Socket(ip, 8080));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static synchronized ClientConnection getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ClientServer instance is not initialized. Call initialize() method first.");
        }
        return instance;
    }

    public static synchronized void initialize(String parameter) {
        if (instance != null) {
            throw new IllegalStateException("ClientServer instance is already initialized.");
        }
        instance = new ClientConnection(parameter);
    }
    public Client getClient(){
        return client;
    }
}
