package com.example.messengerproject;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class signUpController {
    @FXML
    Button bt_sigup_s;
    @FXML
    Button bt_login_s;
    @FXML
    TextField tf_name_s;
    @FXML
    TextField tf_password_s;

    private ClientConnection clientConnection= ClientConnection.getInstance();
    private CommonMethods commonMethods= new CommonMethods();

    @FXML
    void initialize(){

        bt_sigup_s.setOnAction(event->{
            String Name=tf_name_s.getText().trim();
            String Password= tf_password_s.getText().trim();

            if(!Name.isBlank()&& !Password.isBlank()){
                clientConnection.getClient().initializeUser(Name, Password, "signUp");
                String status="Success";
                if(status.equals("Success")){
                    commonMethods.changeScene(bt_sigup_s,"hello-view.fxml", "Main page" );
                }
                else{
                    Alert alert= new Alert(Alert.AlertType.CONFIRMATION,"Do you already have an account?" , ButtonType.YES,ButtonType.NO);
                    alert.setTitle("Have an account?");
                    Optional<ButtonType> result=alert.showAndWait();
                    if(result.get()==ButtonType.NO){
                        commonMethods.alertMessage("Wrong input","Sorry, but this username is taken. Choose another one!", "Try to sign up again" );
                        tf_password_s.clear();
                    }
                    else{
                        commonMethods.alertMessage("Log in","Please, log in","");
                        commonMethods.changeScene(bt_login_s, "logIn.fxml", "Log in form");
                    }
                }
            }
            else{
                commonMethods.alertMessage("Error", "Double check your input", "You haven't enter your username or password!");
            }
        });
    }
    @FXML
    public void logInclick(){
        commonMethods.changeScene(bt_login_s, "logIn.fxml", "Log in form");
    }



}