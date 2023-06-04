package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ServerConnector {
    private ServerSocket serverSocket;
    private ArrayList<String> userList;
    private ServerLogic serverLogic;

    public ServerConnector(){
        try {
            serverSocket = new ServerSocket(8080);
            userList=ServerLogic.getUsers();
        }catch (SocketException e) {
            System.out.println("Server is closed");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /*public void connectClient(){
        while(!serverSocket.isClosed()){
            try {
                Socket clientSocket = serverSocket.accept();
                serverLogic = new ServerLogic(clientSocket);
                new Thread(serverLogic).start();
                //System.out.println("User connected");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
public ArrayList<String> getUserList(){
        return userList;
}*/
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
