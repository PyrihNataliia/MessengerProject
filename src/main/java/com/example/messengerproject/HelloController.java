package com.example.messengerproject;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

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
    private CommonMethods commonMethods = new CommonMethods();

    private class UpdateService extends ScheduledService<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception {
                    //clientConnection.getClient().sendForUserList();
                    Platform.runLater(() -> {
                        if(clientConnection.getClient().getUserList()!=null&&!clientConnection.getClient().getUserList().isEmpty()) {
                            List<String> updatedUserList = new ArrayList<>(clientConnection.getClient().getUserList());
                            updatedUserList.remove(userName_m.getText());
                            connectedUsers.setAll(updatedUserList);
                        }
                    });
                    return null;
                }
            };
        }
    }
    private class NewMessages extends ScheduledService<Void> {

        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception {
                    Platform.runLater(() -> {
                        getNewMessages();
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
        clientConnection.getClient().startListening();
        connectedUsers = FXCollections.observableArrayList();
           UpdateService updateService = new UpdateService();
           updateService.setPeriod(Duration.seconds(3));
           updateService.start();
           lv_connected.setItems(connectedUsers);
           getUsertoChat();

           vb_messages.heightProperty().addListener(new ChangeListener<Number>() {
               @Override
               public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                   sp_conversation.setVvalue((Double)t1);
               }
           });

        NewMessages newMessages= new NewMessages();
        newMessages.setDelay(Duration.seconds(4));
        newMessages.setPeriod(Duration.seconds(2));
        newMessages.start();
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
            vb_messages.getChildren().clear();
            clientConnection.getClient().sendFoAllMessages(userName_m.getText(), chatWith_m.getText());
            showAllChat();
        }
    });
}
public void getNewMessages(){
        Message ms=clientConnection.getClient().getMessage();
        if(ms!=null) {
            if(chatWith_m.getText().equals(ms.getRecipient())||chatWith_m.getText().equals(ms.getSender())){
                showMessage(ms);
            }
            else{
                String header="You have a new message from "+ ms.getSender();
                commonMethods.alertMessage("New message", header, "To read the message, open the correspondence with this user");
            }
        }

}
public void showAllChat(){
    List<Message> messageList = clientConnection.getClient().getMessageList();
    if (messageList != null) {
        for (Message s : messageList) {
            showMessage(s);
        }
    }
}
public void showMessage(Message s){
    HBox hBox= new HBox();
    Text textMessage= new Text(s.getText());
    textMessage.setFont(Font.font("Microsoft YaHei Light"));
    TextFlow textFlow= new TextFlow(textMessage);
    if((s.getSender()).equals(userName_m.getText())){
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5,5, 5, 10));
        textFlow.setStyle("-fx-color: rgb(239,242,255); "+
                "-fx-background-color: rgb(160,82,45);"+
                "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        textMessage.setFill(Color.color(0.934, 0.945, 0.996));
    }
    else{
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5, 5, 10));
        textFlow.setStyle("-fx-background-color: rgb(233,233,235);"+
                "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
    }
    hBox.getChildren().add(textFlow);
    vb_messages.getChildren().add(hBox);
}
}