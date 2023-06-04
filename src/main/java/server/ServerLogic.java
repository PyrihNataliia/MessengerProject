package server;

import server.database.Consts;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerLogic implements Runnable {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private User user;
    private static String currentUserName;
    private Message message;
    private static Map<String, ServerLogic> users= new HashMap<>();
    private RegistrationCallback registrationCallback;

    public ServerLogic(Socket socket, RegistrationCallback registrationCallback){
        this.socket=socket;
        try {
            bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.registrationCallback=registrationCallback;

        } catch (IOException e) {
            closeAllandRemove(socket, bufferedReader, bufferedWriter, user.getName());
        }
    }

    public static ArrayList<String> getUsers(){
        ArrayList<String> names= new ArrayList<>();
        DbHandler dbHandler= new DbHandler();
        ResultSet rs=dbHandler.getRegisteredUsers();
        try {
            while(rs.next()){
                names.add(rs.getString(Consts.USER_NAME));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return names;
    }
    public static ArrayList<Message> getMessages(String userName){
        ArrayList<Message> messeges= new ArrayList<>();
        DbHandler dbHandler= new DbHandler();
        ResultSet rs=dbHandler.getUserChat(userName);
        try {
            while(rs.next()){
                messeges.add(new Message(rs.getString(Consts.SENDER), rs.getString(Consts.TEXT)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return messeges;
    }

    public static void setCurrentUserName(String name){
        currentUserName=name;
    }
    public void setRegistrationCallback(RegistrationCallback callback) {
        this.registrationCallback = callback;
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
                doAction(saxp,saxp.getType());
            } catch (IOException e){
                closeAllandRemove(socket, bufferedReader, bufferedWriter, user.getName());
                for (ServerLogic user : users.values()) {
                    user.sendToUser(getUserList());
                }
                registrationCallback.getUser(user.getName(), false);
                registrationCallback.getList(users.keySet());
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
            users.put(user.getName(), this);
            String usersStr=getUserList();
            for (ServerLogic user : users.values()) {
                user.sendToUser(usersStr);
            }
            registrationCallback.getUser(user.getName(), true);
            registrationCallback.getList(users.keySet());
            }
        }
        else{
            if(type.equals("userList")){
                String str=getUserList();
                sendToUser(str);
            }
            else if(type.equals("chat")){
                message=saxp.getMessage();
                setMessage();
                ServerLogic recipientClient = users.get(message.getRecipient());
                if(recipientClient!=null){
                    String str=getMessage();
                    sendToUser(str);
                    recipientClient.sendToUser(str);
                }
                if(currentUserName!=null){
                    if(currentUserName.equals(message.getRecipient())||currentUserName.equals(message.getSender())){
                        registrationCallback.getNewMessages(message);
                    }
                }
            }
            else if(type.equals("getAllChat")){
                String str=getChat(saxp.getChatNames(), type);
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
        if (registrationCallback != null) {
                registrationCallback.onRegistration(user.getName());
        }
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
        for(String s: users.keySet()){
            str+=String.format("<user>%s</user>", s);
        }
        str+="</users></message>";
        return str;
    }

    private void setMessage(){
        DbHandler dbHandler= new DbHandler();
        dbHandler.WriteMessage(message);
    }
   private String getMessage(){
        String str= String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message><type>getMessage</type><sms><sender>%s</sender><recipient>%s</recipient><text>%s</text></sms></message>", message.getSender(), message.getRecipient(), message.getText());
        return str;
    }
    private String getChat(List<String> names, String type)  {
        DbHandler dbHandler= new DbHandler();
        ResultSet rs=dbHandler.getChat(names);
        String str= String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message><type>%s</type><smss>", type);
        try {
            while(rs.next()) {
                str+= String.format("<sms><sender>%s</sender><recipient>%s</recipient><text>%s</text></sms>", rs.getString(Consts.SENDER), rs.getString(Consts.RECIPIENT), rs.getString(Consts.TEXT));
            }
        }
        catch (SQLException e) {
                throw new RuntimeException(e);
            }

        str+="</smss></message>";
        return str;
    }
    public void closeAllandRemove(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter, String name){
        users.remove(name);
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
    @Override
    public void run() {
       getUserInformation();
        }
}
