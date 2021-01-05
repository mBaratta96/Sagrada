package it.polimi.ingsw.net.client.socket;

import it.polimi.ingsw.net.client.ping.PingClient;
import it.polimi.ingsw.view.PlayerView;
import it.polimi.ingsw.view.generalview.MessageView;
import javafx.scene.Scene;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientSocket {
    private String ip;
    private int port;
    private Scanner socketIn;
    private Scanner input;
    private PrintWriter socketOut;
    private Socket socket;
    private boolean close;
    private PlayerView playerView;

    public void setPlayerView(PlayerView p){
        this.playerView = p;
        this.input = new Scanner(playerView.getBuffer());
}



    public ClientSocket(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        this.socket = new Socket(ip, port);
        this.socketIn = new Scanner(socket.getInputStream());
        this.socketOut = new PrintWriter(socket.getOutputStream());
        this.close = false;
    }


    /**
     * Receives the message from the Server that
     */
    public void init() {
        System.out.println("Connection by Socket with server established.");
        String socketLine = socketIn.nextLine();
        if(socketLine.equals("true")){
            startClient();
        }else{
            System.out.println(socketLine);
            playerView.messageScene(playerView.getActualScene(), "Non è possibile collegarsi al Server");
        }
    }

    /**
     * Starts the ping client and the thread who handled the messages
     */
    private void startClient(){
        PingClient pingClient = null;
        try {
            pingClient = new PingClient(ip,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(pingClient!=null) {
            pingClient.start();
        }
        MessageReader messageReader = new MessageReader();
        messageReader.start();
    }

    /**
     * This private class is the class who directs the messages that arrive from the server
     */
    private class MessageReader extends Thread{ //thread sempre attivo che legge i messaggi socketIn e li stampa a video
        public void run() {
            boolean quit = false;
            try {
                while (!quit && !isClosed()) {
                    String socketLine = socketIn.nextLine(); //ricevo sul mio socket
                    System.out.println(socketLine);
                    if (socketLine.equals("quit")) {
                        quit = true;
                        setClose(true);
                    } else if (ClientSocketMap.isPresent(socketLine)) {
                        ClientSocketMap.getActions(socketLine).accept(socketIn, playerView);
                    } else {
                        System.out.println(socketLine);
                    }
                }
            }catch(NoSuchElementException e){
                setClose(true);
            }catch (IllegalStateException ie){
                setClose(true);
            }
            closeStream();
        }
    }

    /**
     * This method close all the stream used to communicate with the Server
     */
    private void closeStream() {
        socketOut.println("closing"); //scrivo su socket server
        socketOut.flush();
        socketIn.close();
        socketOut.close();
        input.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return if the connection is closed or not
     */
    public synchronized boolean isClosed(){
        return close;
    }

    /**
     * set the boolean close equals true and shows through the GUI the message "SERVER DOWN"
     * @param how to set close
     */
    public synchronized void setClose(boolean how) {
        this.close=how;
        playerView.messageScene(playerView.getActualScene(),"\t  SERVER DOWN.\nRiprova a connetterti più tardi.");
        notifyAll();
    }

    /**
     * This method is used to send a message to Server
     * @param s is the message to send
     */
    public void sendMessage(String s){
        socketOut.println(s);
        socketOut.flush();
        System.out.println(s);
    }


}
