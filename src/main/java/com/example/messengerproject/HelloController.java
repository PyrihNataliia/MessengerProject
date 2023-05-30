package com.example.messengerproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

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

    private ObservableList<String> connectedUsers;

    private ClientConnection clientConnection=ClientConnection.getInstance();

    public void setName(String name){
        userName_m.setText(name);
        chatWith_m.setText("");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectedUsers = FXCollections.observableArrayList();
       bt_send.setOnAction(event->{
            getConnectedUsers();
            getUsertoChat();
        });
    }
public void getConnectedUsers() {
    clientConnection.getClient().sendForUserList();
    for (String s : clientConnection.getClient().getUserList()) {
        connectedUsers.add(s);
    }
    lv_connected.setItems(connectedUsers);
}

public void getUsertoChat(){
    lv_connected.setOnMouseClicked(event -> {
        String selectedUser = (String) lv_connected.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            chatWith_m.setText(selectedUser);
        }
    });
}
}