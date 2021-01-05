package it.polimi.ingsw.net.server.socket;


import it.polimi.ingsw.net.database.PlayersDB;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SocketConnection extends Thread{
    private int port;
    private PlayersDB playersDB;
    private ServerSocket serverSocket;

    public SocketConnection(int port){
        this.port = port;
        this.playersDB = PlayersDB.getInstance();
    }

    public void run(){
        this.startServer();
    }

    /**
     * Starts Server Socket and make the "accept"
     * also check if there're free place in Game for the new Client accepted
     */
    private void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool(); //identity of players will be handler in class Game
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // port not aviable
            return;
        }
        System.out.println("ServerSocket ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                PrintWriter socketOut = new PrintWriter(socket.getOutputStream());

                if(playersDB.freePlace()){ //playing < 4????
                    ServerSocketClientHandler newPlayer = new ServerSocketClientHandler(socket,playersDB);
                    playersDB.addTempConnection(newPlayer);
                    System.out.println("Client accepted with SOCKET CONNECTION.");
                    socketOut.println("true");
                    socketOut.flush();
                    executor.submit(newPlayer);

                }else{

                    System.out.println("Someone is trying to connect but Game is full!");
                    socketOut.println("We're sorry but this lobby it's full.");
                    socketOut.flush();
                    socketOut.close();
                    socket.close();
                }
            }catch(IOException e){
                break;
            }
        }
        executor.shutdown();
    }
}
