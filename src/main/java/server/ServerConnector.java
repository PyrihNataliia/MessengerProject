package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerConnector {
    private ServerSocket serverSocket;

    public ServerConnector(){
        try {
            serverSocket = new ServerSocket(8080);
        }catch (SocketException e) {
            System.out.println("Server is closed");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void connectClient(){
        while(!serverSocket.isClosed()){
            try {
                Socket clientSocket = serverSocket.accept();
                ServerLogic serverLogic = new ServerLogic(clientSocket);
                new Thread(serverLogic).start();
                System.out.println("User connected");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeServer(){
        try {
            if(serverSocket!=null){
                serverSocket.close();
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
