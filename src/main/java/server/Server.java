package server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class Server extends JFrame{
    private static volatile boolean isStarted = false;
    private JTextArea textArea1;
    private JButton startButton;
    private JButton stopButton;
    private JPanel serverPanel;
    private ServerConnector serverConnector;

    public Server(ServerConnector sc) {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });
        serverConnector=sc;
    }

    public static void main(String[] args) {
        Server s= new Server(new ServerConnector());
        s.setContentPane(s.serverPanel);
        s.setTitle("Server side");
        s.setSize(480, 320);
        s.setVisible(true);
        s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        while (true) {
            if (isStarted) {
                s.connectClient();
                isStarted = false;
            }
        }
    }

    private void start(){
            isStarted= true;
            writeText("Server was started");
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
    }

    private void connectClient(){
            isStarted = true;
            serverConnector.connectClient();
            writeText("New user has connected");
    }
    private void stop(){
        int stopQuestion= JOptionPane.showConfirmDialog(this, "Do you confirm the stop of the server?","Confirm the stop", JOptionPane.YES_NO_OPTION);
        if(stopQuestion==JOptionPane.YES_OPTION){
            serverConnector.closeServer();
            writeText("The server was stopped");
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }
    private void writeText(String m) {
        LocalDateTime date = LocalDateTime.now();
        textArea1.append(date + " "+ m+ "\n");
    }

}
