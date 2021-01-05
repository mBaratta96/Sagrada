package it.polimi.ingsw.net.server.ping;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MultiPingServer extends Thread{
    private static final int PINGPORT = 1500;
    private ServerSocket serverSocket;

    /**
     * Accept new Ping Client
     */
    public void run(){
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket(PINGPORT);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new PingServer(socket));
            } catch(IOException | NullPointerException e) {
                break;
            }
        }
        executor.shutdown();
    }
}
