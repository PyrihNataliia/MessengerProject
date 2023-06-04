package com.example.messengerproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class ipFormController {
    @FXML
    TextField tf_ip;

    @FXML
    Button bt_send;

    private CommonMethods commonMethods= new CommonMethods();


    @FXML
    void initialize(){
        bt_send.setOnAction(event->{
            String ip =tf_ip.getText().trim();
            if(!ip.isBlank()){
                ClientConnection.initialize(ip);
                commonMethods.changeScene(bt_send, "logIn.fxml", "Log in form");
            }
        });
    }

}