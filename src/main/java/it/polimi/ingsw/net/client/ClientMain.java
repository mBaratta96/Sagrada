package it.polimi.ingsw.net.client;

import it.polimi.ingsw.net.client.socket.ClientSocket;
import it.polimi.ingsw.view.PlayerView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;

public class ClientMain extends Application{
    private static final int DEFAULT_PORT = 1234;
    private static final int DEFAULT_PING_PORT = 1500;
    private static final String FILE_NAME= "src/main/resources/ClientConfig/ConnectionConfig.txt";

    public static void main(String[] args){
        launch(args);
    }

    /**
     * This method instants both the client and the main method of the GUI
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        String[] parts = readFromFile();
        String ip = parts[0];
        int port = DEFAULT_PORT;
        try {
            port = Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            System.out.println("Wrong port format");
        }

        if(port==DEFAULT_PING_PORT){
            port = DEFAULT_PORT;
        }

        try {
            ClientSocket client = new ClientSocket(ip, port);
            PlayerView p1 = new PlayerView("Player Unknown", client);
            p1.start(primaryStage);
            client.init();
        }catch (NullPointerException | SocketException e){
            System.err.println("Server offline");
            System.exit(0);
        }
    }

    /**
     * This method is used to read from file ip and port of the server
     * @return user and password
     */
    private static String[] readFromFile() {
        String[] parts = new String[3];
        String[] part = new String[2];
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                for (int i = 0; i < 2; i++) {
                    String s = reader.readLine();
                    part = s.split(":");
                    parts[i] = part[1].trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parts;
    }
}
