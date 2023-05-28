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

public class ServerLogic implements Runnable {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private User user;

    public ServerLogic(Socket socket){
        this.socket=socket;
        try {
            bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
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
                user=saxp.getUser();
                doAction(saxp.getType());
            } catch (IOException | SAXException | ParserConfigurationException e) {
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
            throw new RuntimeException(e);
        }
    }
    private void doAction(String type) {
        String status="";
        if(type.equals("logIn")){
            status = logInUser();
        }
        else if(type.equals("signUp")){
            status = initializeUser();
        }
        System.out.println("Ready to send message");
        String str= String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message><type>%s</type><status>%s</status></message>", type, status);
        sendToUser(str);
    }

    private String initializeUser() {
        DbHandler dbHandler= new DbHandler();
        ResultSet rs= dbHandler.checkUnique(user);
        String status= checkPresence(rs);
        System.out.println(status);
        if(status.equals("Success")){
           return "Fail" ;
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
           /////
       System.out.println("Ok");
       return "Success";}
       else{
           //////
           System.out.println("Problem");
           return "Fail";
       }
    }

    @Override
    public void run() {
       getUserInformation();
        }


}
