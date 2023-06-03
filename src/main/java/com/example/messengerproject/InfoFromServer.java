package com.example.messengerproject;

import java.util.List;

public class InfoFromServer {

    private String status;
    private List<String> onlineUsers;
    private List<Message> receivedMessages;


    private Boolean delivered=false;
    private Message message;
public InfoFromServer(){

}

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOnlineUsers(List<String> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public void setReceivedMessages(List<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }
    public String getStatus() {
        return status;
    }
    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }
    public List<String> getOnlineUsers() {
        return onlineUsers;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }
}
