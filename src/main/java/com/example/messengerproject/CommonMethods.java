package com.example.messengerproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;

public class CommonMethods {
    public CommonMethods(){}
    public void changeScene(Button btn, String fileName, String title){
        btn.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(logInContoller.class.getResource(fileName));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Parent root =fxmlLoader.getRoot();
        Stage stage= new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }

    public void changeScene(Button btn, String fileName, String title, String name){
        btn.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(logInContoller.class.getResource(fileName));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Parent root =fxmlLoader.getRoot();
        HelloController helloController= fxmlLoader.getController();
        helloController.setName(name);
        Stage stage= new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();

    }
    public void alertMessage(String Title, String Header, String Context){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Title);
        alert.setHeaderText(Header);
        alert.setContentText(Context);
        alert.showAndWait();
    }


}
