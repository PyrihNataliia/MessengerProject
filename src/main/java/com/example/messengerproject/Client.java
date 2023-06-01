package com.example.messengerproject;

import java.io.*;
import java.net.Socket;
import java.util.List;


public class Client {

    private Socket socket;
    private BufferedWriter bufferedWriter;

    private BufferedReader bufferedReader;
    private DeserializeClass ds;


    public Client(Socket socket){
        this.socket=socket;
        try {
            bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            closeAll(socket, bufferedReader, bufferedWriter);
            //throw new RuntimeException(e);
        }
    }

    public void initializeUser(String name, String password, String type ){
        String str= String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message><type>%s</type><user><name>%s</name><password>%s</password></user></message>", type, name, password);
       sendMessage(str);
    }
    public void sendForUserList(){
        String str= "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message><type>userList</type></message>";
        sendMessage(str);
    }

    public void sendUserMessage(String sender, String recipient, String text){
        String str=String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message><type>chat</type><sms><sender>%s</sender><recipient>%s</recipient><text>%s</text></sms></message>", sender, recipient, text);
        sendMessage(str);
    }
    public void sendFoAllMessages(String user1, String user2){
        String str=String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message><type>getAllChat</type><smsInfo><user>%s</user><user>%s</user></smsInfo></message>", user1, user2);
        sendMessage(str);
    }
    public void sendForNewMessages(String user1, String user2){
        String str=String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message><type>getNewChat</type><smsInfo><user>%s</user><user>%s</user></smsInfo></message>", user1, user2);
        sendMessage(str);
    }
    private void sendMessage(String str){
        try {
            bufferedWriter.write(str);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeAll(socket, bufferedReader, bufferedWriter);
            //throw new RuntimeException(e);
        }
    }
    private void workWithMessages(){
        try {
            String messageFromServer= bufferedReader.readLine();
            ds = new DeserializeClass(messageFromServer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getUserList(){
        workWithMessages();
        return ds.getUserList();
    }

    public String getStatus(){
        workWithMessages();
        return ds.getStatus();
    }
    public List<Message> getMessageList(){
    workWithMessages();
    return ds.getMessageList();
    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            if(bufferedWriter!= null){
                bufferedWriter.close();
            }
            if(socket!=null){
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
