package it.polimi.ingsw.net.server.socket;


import com.google.gson.Gson;
import it.polimi.ingsw.controller.Game;
import it.polimi.ingsw.controller.Player;
import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.GlassDash;
import it.polimi.ingsw.model.ToolCardInfo;
import it.polimi.ingsw.model.cards.PrivateTargetCard;
import it.polimi.ingsw.model.cards.SchemaCard;
import it.polimi.ingsw.model.exceptions.NotValidException;
import it.polimi.ingsw.net.database.PlayersDB;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServerSocketClientHandler implements Runnable{
    private Socket socket;
    private Scanner socketIn;
    private PrintWriter socketOut;
    private PlayersDB playersDB;
    private Player player;
    private Game game;
    private boolean reconnected;
    private int idClient;
    private boolean loginValid;
    private boolean inGame;
    private static final String DIVISOR = ":";
    private static final String PUNCTUATION = "!\"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~";

    public ServerSocketClientHandler(Socket socket, PlayersDB playersDB) throws IOException{
        this.socket = socket;
        this.socketIn = new Scanner(socket.getInputStream());
        this.socketOut = new PrintWriter(socket.getOutputStream());
        this.playersDB=playersDB;
        this.reconnected = false;
        this.inGame = false;
    }

    public void run() {
        login();
        if(!reconnected){
            while(!playersDB.startGame());
            this.game = playersDB.getGame();
            setGame();
            play();
        }else{
            this.game=playersDB.getGame();
            play();
        }
    }

    /**
     * this method is the core of the server-side game, it through the messages who receives call different methods of the class Game
     */
    private void play() {
        inGame = true;
        while(game.getRoundTrack().getRound()<=10){
            while(!game.isMyTurn(player));
            String message = socketIn.nextLine();
            System.out.println("message in play____"+message);
            if(message.equals("token")) {
                String nToolCard = socketIn.nextLine();
                game.addToken(Integer.parseInt(nToolCard), player);
            }else if(message.equals("skip")) {
                game.endTurn();
            }else if(message.equals("tool deselected")){
                String nToolCard = socketIn.nextLine();
                game.removeToken(Integer.parseInt(nToolCard), player);
            }else if(message.equals("toolCard")){
                String toolcard = socketIn.nextLine();
                System.out.println(toolcard);
                String[] parts = toolcard.split("___");
                game.executeToolCard(Integer.parseInt(parts[0]), new Gson().fromJson(parts[1], ToolCardInfo.class), player);
            }else {
                String[] parts = message.split(":");
                String placeOnReserve = parts[0];
                String index = parts[1];
                try {
                    game.addDice(Integer.parseInt(placeOnReserve), Integer.valueOf(index), player);
                } catch (NotValidException e) {
                    notValidDice();
                }
            }
        }
    }

    private void notValidDice(){
        sendMessage("notValidDice");
    }

    /**
     * Send to client all the object that he needs to start the game
     * @param playerDash is the GlassDash of this player
     * @param rec if the player to send the game is a REConnected player
     */
    public void sendGame(GlassDash playerDash, boolean rec) {
        sendMessage("Dash");
        sendMessage(new Gson().toJson(playerDash));
        System.out.println("sended Dash" + idClient);
        sendMessage("PublicTarget");
        sendMessage(new Gson().toJson(game.getPublicTargetCards()));
        System.out.println("sended public" + idClient);
        sendMessage("Tool");
        sendMessage(new Gson().toJson(game.getToolCards()));
        System.out.println("sended tool" + idClient);
        sendMessage("Players");
        sendMessage(new Gson().toJson(game.seeOtherPlayers(player)));
        player.sendCurrentTokens();
        System.out.println("sended OP" + idClient);
        System.out.println("sendedReserve" + idClient);
        sendMessage("ReserveView");
        sendMessage(new Gson().toJson(game.getReserve()));
        sendMessage("Timer");
        sendMessage("" + playersDB.getTurnTimer());
        if (!rec) {
            if (game.isMyTurn(player)) {
                sendMessage("Your Turn");
                sendMessage("1");
            }
        }

    }

    /**
     * send to client the gson of SchemaCard and PrivateCard
     */
    private void setGame() {
        sendMessage("SchemaCard");
        List<SchemaCard> possibleCards = game.getSchemaCards(idClient);
        sendMessage(new Gson().toJson(possibleCards));
        sendMessage("Private");
        PrivateTargetCard privateTargetCard = game.getPrivateTargetCard(player);
        player.setPrivateTargetCard(privateTargetCard);
        sendMessage(new Gson().toJson(privateTargetCard));
        String chosenCard = readMessage();
        System.out.println("chosen: " + chosenCard);
        GlassDash dash = new GlassDash(possibleCards.get(Integer.parseInt(chosenCard)), "+");
        player.addGlassDash(dash);
        playersDB.setGlassDash();
        while (!playersDB.allDashed()){}
        sendGame(dash, false);
    }

    /**
     * Close the Stream with the Client
     */
    private void closeStream() {
        try {
            socketIn.close();
            socketOut.close();
            socket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        System.out.println("Connection closed with Client " + idClient+".");
    }

    /**
     * Take Username and Password by client and check if:
     * 1.is a disconnected Player who would continue the game
     * 2.Username and Password are both without punctuation
     * 3.The Username is already used
     * 4.Username and Password are strings != ""
     */
    private void login() {
        sendMessage("Login");
        loginValid = false;
        sendMessage("inizio Login");
        do {
            String userAndPass = socketIn.nextLine();
            String[] parts = userAndPass.split(DIVISOR,2);
            if(checkPunctuation(parts)) {
                try {
                    String user = parts[0];
                    String pass = parts[1];
                    if (!parts[0].equals("") && !parts[1].equals("")) {
                        if (playersDB.checkDisconnected(user, pass)) {
                            sendMessage("Continue");
                            player = playersDB.getDisconnected(user, pass);
                            player.setConnection(this);
                            playersDB.removeFromDisconnected(player);
                            reconnected = true;
                            loginValid = true;
                        } else {
                            boolean ok = false;
                            while(!ok){
                                if(!checkPunctuation(parts)){
                                    sendMessage("Punctuation found");
                                    userAndPass = socketIn.nextLine();
                                    parts = userAndPass.split(DIVISOR,2);
                                    user = parts[0];
                                    pass = parts[1];
                                }else if(playersDB.sameUsername(user)){
                                    sendMessage("Username already used");
                                    userAndPass = socketIn.nextLine();
                                    parts = userAndPass.split(DIVISOR,2);
                                    user = parts[0];
                                    pass = parts[1];
                                }else if(parts[0].equals("") || parts[1].equals("")){
                                    sendMessage("Invalid Enter");
                                    userAndPass = socketIn.nextLine();
                                    parts = userAndPass.split(DIVISOR,2);
                                    user = parts[0];
                                    pass = parts[1];
                                }else{
                                    ok = true;
                                }
                            }

                            if (playersDB.freePlace() && !playersDB.arePlaying()) {
                                player = new Player(user,pass,this);
                                idClient = playersDB.addPlayer(player);
                                System.out.println("Client " + idClient + " loggato");
                                loginValid = true;
                                player.setConnection(this);
                                //playersDB.setTempConnection(this, idClient);
                                sendMessage("Login OK");
                            } else {
                                System.out.println("Someone is trying to connect but Game is full!");
                                sendMessage("Game already started");
                                closeStream();
                            }
                        }
                    } else {
                        sendMessage("Invalid Enter");
                    }
                } catch (IndexOutOfBoundsException e) {
                    sendMessage("Invalid Enter");
                }
            }else{
                sendMessage("Punctuation found");
            }
        } while (!loginValid);
    }

    /**
     *
     * @param parts
     * @return if parts doesn't contain punctuation
     */
    private boolean checkPunctuation(String[] parts) {
        char[] punctuation = PUNCTUATION.toCharArray();
        for(String s: parts){
            for(char c : punctuation)
                if(s.contains(c+"")){
                    return false;
                }
        }
        return true;
    }

    /**
     * Send the String message to Client
     * @param message
     */
    public void sendMessage(String message){
        System.out.println(message+"____"+idClient);
        socketOut.println(message);
        socketOut.flush();
    }


    /**
     * @return the message posted by Client
     */
    public String readMessage(){
        String message = socketIn.nextLine();
        return message;
    }

    /**
     *
     * @return if the Player were or not in game
     */
    public boolean wasInGame(){
        return inGame;
    }

    /**
     *
     * @return if the Player is correctly logged
     */
    public boolean isLogged(){
        return loginValid;
    }

    /**
     *
     * @return an instance of this player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @return ID of this Player
     */
    public int getID() {
        return idClient;
    }
}