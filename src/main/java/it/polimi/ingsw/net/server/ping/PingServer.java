package it.polimi.ingsw.net.server.ping;

import it.polimi.ingsw.net.database.PlayersDB;
import it.polimi.ingsw.net.server.socket.ServerSocketClientHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PingServer extends Thread {

    private Socket socket;
    private Scanner in;
    private PrintWriter sOut;
    private PlayersDB playersDB;
    private ServerSocketClientHandler connection;
    private static final int PING_TIMER = 1000;

    public PingServer(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new Scanner(socket.getInputStream());
        this.sOut = new PrintWriter(socket.getOutputStream());
        this.playersDB = PlayersDB.getInstance();
    }

    /**
     * Divided the Ping's actions into three phases
     */
    public void run() {
        connection = playersDB.getTempConnection();
        try {
            startPing();
            lostConnection();
            closeStream();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * It's the core business of ping class. Through the socket's output stream send different messages to client
     * and expects to receive the same message input stream
     * @throws InterruptedException
     */
    private void startPing() throws InterruptedException{
        boolean ping = true;
        int serial = 1;
        while (ping) {
            sOut.println("Ping" + serial);
            sOut.flush();
            sleep(PING_TIMER);
            if (in.hasNextLine()) {
                if ((serial == 1 && in.nextLine().equals("Ping2")) || (serial == 2 && in.nextLine().equals("Ping1"))) {
                    ping = false;
                } else {
                    if (serial == 1) serial = 2;
                    else serial = 1;
                }
            } else {
                ping = false;
            }
        }
    }

    /**
     * Close all the stream
     * @throws IOException
     */
    private void closeStream() throws IOException {
        in.close();
        sOut.close();
        socket.close();
    }

    /**
     * this method is used to save or no a disconnected client in Disconnected's Array in PlayerDB
     */
    private void lostConnection(){
        if (connection.wasInGame()) {
            String name = connection.getPlayer().getUsername();
            System.out.println("Client " + name + " disconnected!");
            playersDB.addDisconnected(connection.getPlayer());
        } else {
            if (connection.isLogged()) {
                int id = connection.getID();
                playersDB.removeFromLogged(connection.getPlayer());
                System.out.println("Client " + id + " disconnected!");
            } else {
                System.out.println("Someone has disconnected");
                playersDB.removeTempConnection(connection);
            }
        }
    }

}
