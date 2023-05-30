package server;

import server.database.DbHandler;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServerLogic implements Runnable {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private User user;

    private static ArrayList<String> userNames= new ArrayList<>();

    public ServerLogic(Socket socket){
        this.socket=socket;
        try {
            bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            closeAllandRemove(socket, bufferedReader, bufferedWriter, user.getName());
        }
    }

    private void getUserInformation(){
        String message;
        while(socket.isConnected()){
            try {
                message= bufferedReader.readLine();
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                SaxParser saxp = new SaxParser();
                InputStream m2 = new ByteArrayInputStream(message.getBytes());
                parser.parse(m2, saxp);
                //userNames.add(user.getName());
                doAction(saxp,saxp.getType());
            } catch (IOException e){
                userNames.remove(user.getName());
                closeAllandRemove(socket, bufferedReader, bufferedWriter, user.getName());
                System.out.println(userNames);
                break;
            }
            catch (SAXException | ParserConfigurationException e) {
                throw new RuntimeException(e);
            }
    }
    }

    private void sendToUser(String message){
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeAllandRemove(socket, bufferedReader, bufferedWriter, user.getName());
            //closeAll(socket, bufferedReader, bufferedWriter);
            //throw new RuntimeException(e);
        }
    }
    private void doAction(SaxParser saxp, String type) {
        String status="";
        if(type.equals("logIn")||type.equals("signUp")){
            user=saxp.getUser();
        if(type.equals("logIn")){
            status = logInUser();
        }
        else if(type.equals("signUp")){
            status = initializeUser();
        }
        String str= String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message><type>%s</type><status>%s</status></message>", type, status);
        sendToUser(str);
        if(status.equals("Success")){
            userNames.add(user.getName());
            System.out.println("user:"+userNames);
            }
        }
        else{
            if(type.equals("userList")){
                String str=getUserList();
                System.out.println(str);
                sendToUser(str);
            }
        }
    }

    private String initializeUser() {
        DbHandler dbHandler= new DbHandler();
        ResultSet rs= dbHandler.checkUnique(user);
        String status= checkPresence(rs);
        if(status.equals("Success")){
           return "Fail";
        }
        else{
        dbHandler.WriteUser(user);
        return "Success";
        }
    }

    private String logInUser(){
        DbHandler dbHandler= new DbHandler();
        ResultSet rs= dbHandler.getUser(user);
        return checkPresence(rs);
    }

    private String checkPresence(ResultSet resulSet) {
       int counter=0;
       try {
           while (resulSet.next()) {
               counter++;
           }
       }
        catch (SQLException e) {
           throw new RuntimeException(e);
       }
       if(counter==1){
       return "Success";}
       else{
           return "Fail";
       }
    }
    private String getUserList(){
        String str = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message><type>userList</type><users>");
       for(String s:userNames){
           str+=String.format("<user>%s</user>", s);
       }
        str+="</users></message>";
        return str;
    }
    public void removeUser(String name){
        userNames.remove(name);
        //System.out.println(userNames);
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
    public void closeAllandRemove(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter, String name){
        removeUser(name);
        closeAll(socket, bufferedReader, bufferedWriter);
    }
    @Override
    public void run() {
       getUserInformation();
        }
}
