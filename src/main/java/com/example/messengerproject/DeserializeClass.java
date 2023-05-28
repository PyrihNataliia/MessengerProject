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
        else if(messageType.equals("UserList")){
            List<Element> userElements=rootElement.getChild("users").getChildren("user");
            userList = new ArrayList<>();
            for (Element userElement : userElements) {
                String username = userElement.getText();
                userList.add(username);
            }
        }
    }

    public String getStatus() {
        return status;
    }

    public List<String> getUserList() {
        return userList;
    }

}
