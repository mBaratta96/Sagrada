package it.polimi.ingsw.net.server;


import it.polimi.ingsw.net.database.PlayersDB;
import it.polimi.ingsw.net.server.ping.MultiPingServer;
import it.polimi.ingsw.net.server.socket.SocketConnection;

public class ServerMain {
    private static final int SOCKET_P = 1234;

    /**
     * This main starts the Server's application
     * @param args
     */
    public static void main (String[] args){
        ServerMain server = new ServerMain();
        server.startConnections();
    }

    /**
     * Starts Server Socket for the comunication with Client
     * Start the Ping' Server used to check disconnected Players
     */
    private void startConnections() {
        SocketConnection socketConnection = new SocketConnection(SOCKET_P);
        MultiPingServer multiPingServer = new MultiPingServer();
        multiPingServer.start();
        socketConnection.start();
    }
}
