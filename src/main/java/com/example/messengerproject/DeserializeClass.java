package com.example.messengerproject;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
public class DeserializeClass {

    SAXBuilder saxBuilder;
    Document document;
    Element rootElement;
    String messageType;

    String status;

    List<String> userList;
    List<Message> messageList;

    public DeserializeClass(String message){
        saxBuilder  = new SAXBuilder();
        try {
            document= saxBuilder.build(new StringReader(message));
        } catch (JDOMException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        rootElement = document.getRootElement();
        messageType = rootElement.getChildText("type");
        workWithMessage();
    }

    public void workWithMessage(){
        if(messageType.equals("logIn")||messageType.equals("signUp") ){
            status = rootElement.getChildText("status");
        }
        else if(messageType.equals("userList")){
            List<Element> userElements=rootElement.getChild("users").getChildren("user");
            userList = new ArrayList<>();
            for (Element userElement : userElements) {
                String username = userElement.getText();
                userList.add(username);
            }
        }
        else if(messageType.equals("allChat")){
            List<Element> messageElements=rootElement.getChild("smss").getChildren("sms");
            messageList = new ArrayList<>();
            for (Element messageElement : messageElements) {
                String sender = messageElement.getChildText("sender");
                String recipient = messageElement.getChildText("recipient");
                String text = messageElement.getChildText("text");
                Message message= new Message(sender, recipient, text);
                messageList.add(message);
            }
        }
    }

    public String getStatus() {
        return status;
    }

    public List<String> getUserList() {
        return userList;
    }

    public List<Message> getMessageList(){
        return messageList;
    }

}
