package server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;

public class Server extends JFrame{
    private ServerSocket serverSocket;
    private static volatile boolean isStarted = false;
    private JTextArea textArea1;
    private JButton startButton;
    private JButton stopButton;
    private JPanel serverPanel;

    public Server() {
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
    }

    public static void main(String[] args) {
        Server s= new Server();
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
        try  {
            serverSocket = new ServerSocket(8080);
            isStarted= true;
            writeText("Server was started");
            startButton.setEnabled(false);
            stopButton.setEnabled(true);

        }
        catch (SocketException e) {
            System.out.println("Server is closed");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void  connectClient(){
            while(true){
                try {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new ServerLogic(clientSocket)).start();
                    isStarted = true;

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

    }
    private void stop(){
        int stopQuestion= JOptionPane.showConfirmDialog(this, "Do you confirm the stop of the server?","Confirm the stop", JOptionPane.YES_NO_OPTION);
        if(stopQuestion==JOptionPane.YES_OPTION){
            try {
                if(serverSocket!=null){
                serverSocket.close();
                System.out.println(serverSocket.isClosed());}
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
