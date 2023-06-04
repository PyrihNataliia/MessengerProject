package server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

public class Server extends JFrame implements RegistrationCallback{

    private JList<String> userList;
    private DefaultListModel<String> listModel;
    private JTextArea chatTextArea;
    private JTextArea TextArea2;
    private ServerSocket serverSocket;
    private ArrayList<String> users;
    private ServerLogic serverLogic;

    public Server() {
        try {
            serverSocket = new ServerSocket(8080);
            users=ServerLogic.getUsers();
        }catch (SocketException e) {
            System.out.println("Server is closed");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        DrawGraphics();
        writeText("Server started");
    }
    public void chooseUser(){
        userList.setSelectedIndex(0);
        updateChat("Chat with the user: " + userList.getSelectedValue());

        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedUser = userList.getSelectedValue();
                ServerLogic.setCurrentUserName(selectedUser);
                updateChat("\t Chat with the user: "+selectedUser+"\n");
                for(Message m: ServerLogic.getMessages(selectedUser)){
                    addChat(m.getSender()+": "+m.getText()+"\n");
                }
            }
        });
    }
    public void onRegistration(String username) {
        listModel.addElement(username);
    }
    public void getNewMessages(Message message){
        addChat(message.getSender()+": "+message.getText()+"\n");
    }
    public void getUser(String username, boolean status){
        if(status){
            writeText(username+" connected");
        }
        else{
            writeText(username+" disconnected");
        }
    }
    public void getList(Set<String> onlineNames){
        writeText("Users online "+onlineNames);
    }
    private void updateChat(String chatText) {
        chatTextArea.setText(chatText);
    }
    private void addChat(String chatText){
        chatTextArea.append(chatText);
    }
    private void connectClient(){
        while(!serverSocket.isClosed()){
            try {
                Socket clientSocket = serverSocket.accept();
                serverLogic = new ServerLogic(clientSocket, this);
                new Thread(serverLogic).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void DrawGraphics(){
        listModel = new DefaultListModel<>();
        userList = new JList<>(listModel);

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(userList), BorderLayout.WEST);
        panel.add(new JScrollPane(chatTextArea), BorderLayout.CENTER);

        JPanel secondPanel = new JPanel(new BorderLayout());
        TextArea2 = new JTextArea();
        secondPanel.add(new JScrollPane(TextArea2), BorderLayout.CENTER);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("User correspondence", panel);
        tabbedPane.addTab("Service information", secondPanel);
        container.add(tabbedPane, BorderLayout.CENTER);

        setTitle("Server");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getUserList();
    }
    private void writeText(String m) {
        LocalDateTime date = LocalDateTime.now();
        TextArea2.append(date + " --> "+ m+ "\n");
    }

    private void getUserList(){
        for(String s:  ServerLogic.getUsers()){
            listModel.addElement(s);
        }
    }

    public static void main(String[] args) {
        Server frame = new Server();
        frame.setVisible(true);

        frame.chooseUser();

        while(true){
            frame.connectClient();
        }
    }
}

