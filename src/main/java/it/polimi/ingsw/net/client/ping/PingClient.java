package it.polimi.ingsw.net.client.ping;

import com.sun.org.apache.xpath.internal.SourceTree;
import it.polimi.ingsw.net.client.socket.ClientSocket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class PingClient extends Thread{
    private static final int PINGPORT = 1500;
    private Socket socket;
    private Scanner socketIn;
    private PrintWriter socketOut;
    private ClientSocket myClient;

    public PingClient(String ip, ClientSocket myClient) throws IOException {
        this.socket = new Socket(ip, PINGPORT);
        this.socketIn = new Scanner(socket.getInputStream());
        this.socketOut = new PrintWriter(socket.getOutputStream());
        this.myClient = myClient;
    }

    /**
     * This is the method who handle the Ping in the client side
     */
    public void run(){
        boolean ping = true;
        int serial = 1;
        try {
            while (!myClient.isClosed() && ping) {
                socketOut.println("Ping"+serial);
                socketOut.flush();
                sleep(1000);
               if(socketIn.hasNextLine()){
                    if((serial==1 && socketIn.nextLine().equals("Ping2")) || (serial==2 && socketIn.nextLine().equals("Ping1"))){
                        ping = false;
                    } else {
                        if(serial == 1) {
                            serial = 2;
                        }else{
                            serial = 1;
                        }
                    }
               } else {
                    ping = false;
               }
            }
        }catch (NoSuchElementException | InterruptedException e){
            myClient.setClose(true);
        }finally {
            socketIn.close();
            socketOut.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            myClient.setClose(true);
        }

    }

}
