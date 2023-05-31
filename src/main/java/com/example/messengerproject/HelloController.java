package com.example.messengerproject;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Label userName_m;
    @FXML
   private  Label chatWith_m;
    @FXML
    private Button bt_send;
    @FXML
    ListView lv_connected;

    @FXML
    TextField tf_text;

    @FXML
    ScrollPane sp_conversation;

    @FXML
    VBox vb_messages;

    private ObservableList<String> connectedUsers;

    private ClientConnection clientConnection=ClientConnection.getInstance();

    private class UpdateService extends ScheduledService<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception {
                    clientConnection.getClient().sendForUserList();
                    Platform.runLater(() -> {
                        connectedUsers.setAll(clientConnection.getClient().getUserList());
                    });

                    return null;
                }
            };
        }
    }

    public void setName(String name){
        userName_m.setText(name);
        chatWith_m.setText("");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectedUsers = FXCollections.observableArrayList();
           UpdateService updateService = new UpdateService();
           updateService.setPeriod(Duration.seconds(2));
           updateService.start();
           lv_connected.setItems(connectedUsers);
           getUsertoChat();

    }
    @FXML
    public void sendMessage(){
        String sender= userName_m.getText();
        String messageToSend = tf_text.getText();
        String recipient=chatWith_m.getText();
        if(!messageToSend.isEmpty()&&!recipient.isEmpty()){
            clientConnection.getClient().sendUserMessage(sender, recipient, messageToSend);
            tf_text.clear();
        }
    }
public void getUsertoChat(){
    lv_connected.setOnMouseClicked(event -> {
        String selectedUser = (String) lv_connected.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            chatWith_m.setText(selectedUser);
        }
        clientConnection.getClient().sendFoAllMessages(userName_m.getText(), chatWith_m.getText());
        System.out.println(clientConnection.getClient().getMessageList());
    });
}
}