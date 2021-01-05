package it.polimi.ingsw.view;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.Player;
import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.GlassDash;
import it.polimi.ingsw.model.cards.PrivateTargetCard;
import it.polimi.ingsw.model.cards.PublicTargetCard;
import it.polimi.ingsw.model.cards.SchemaCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.net.client.socket.ClientSocket;
import it.polimi.ingsw.view.dice.AddDiceView;
import it.polimi.ingsw.view.generalview.*;
import it.polimi.ingsw.view.toolcard.ToolCardInfoView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import java.util.Timer;
import java.util.TimerTask;


public class PlayerView extends Application {

    private String name;
    private Stage primaryStage;
    private String pass;
    private PrivateTargetCard privateTargetCard;
    private GlassDash glassDash;
    private String buffer;
    private ClientSocket connection;
    private List <SchemaCard> possibleCards;
    private List<PublicTargetCard> publicTargetCards;
    private List<ToolCard> toolCards;
    private int token;
    private List<Player> OPschemaCards;
    private ArrayList<Dice> reserve;

    private boolean logged;
    private boolean used;
    private boolean started = false;
    private AddDiceView addDiceView;
    private ToolCardInfoView toolCardInfoView;
    private RoundView roundRoot;
    private FinalView finalRoot;

    private Object lock = new Object();
    private boolean allData;
    private boolean rooted = false;
    private int turnTimer;
    private boolean skipped;
    private boolean invalidEnter;
    private boolean full;
    private boolean reconnected = false;
    private boolean second_turn;
    private static final int N_RADIOBUTTON = 4;
    private static final String DIVISOR = ":";
    private static final String TITLE = "SAGRADA";
    private static final String SEPARATOR = "___";
    private Scene actualScene;
    private boolean punctuation;


    public PlayerView(String name, ClientSocket connection) throws NullPointerException{
        this.name = name;
        this.connection=connection;
        this.buffer = new String();
        connection.setPlayerView(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = logInScene();
        scene.getStylesheets().add("style.css");
        this.primaryStage=primaryStage;
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle(TITLE + " - " + name.toUpperCase());
        exitOnClose(primaryStage);
    }


    private void setSize(Pane pane, Scene scene){
        pane.prefWidthProperty().bind(scene.widthProperty());
        pane.prefHeightProperty().bind(scene.heightProperty());
    }


    private void exitOnClose(Stage primaryStage) {
        primaryStage.setOnCloseRequest(windowEvent -> {
            connection.setClose(true);
            Platform.exit();
            System.exit(0);
        });
    }




    //LoginView
    private Scene logInScene(){
        LoginView root = new LoginView();
        Scene scene = new Scene(root);
        actualScene = scene;
        setSize(root.getSpane(),scene);
        root.getPass_field().setOnAction(t->loginEnter(root,scene));
        root.getUser_field().setOnAction(t->loginEnter(root,scene));
        root.getLogin_but().setOnAction(t->loginEnter(root,scene));
        return scene;
    }


    private void loginEnter(LoginView root, Scene scene){
        synchronized (lock) {
            loadBuffer(root.getUserPass_field());
            lock.notifyAll();
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            if(logged) { messageScene(scene, "In attesa di altri giocatori..."); }
            if(used) {
                root.printError("Username già in uso.");
                used=false;
            }
            if(invalidEnter){
                root.printError("Completa tutti i campi.");
                invalidEnter=false;
            }
            if(punctuation){
                root.printError("Simboli non ammessi.");
                punctuation=false;
            }
            if(full){ messageScene(scene, "E' in corso un'altra partita, riprova tra qualche minuto...");
            }
            if(reconnected) {
                messageScene(scene, "Attendi che finisca il round attuale..");
                lock.notifyAll();
            }

        }
    }

    /**
     * this method is used to accept or no the player who is trying to login
     * @param socketIn the socket input reader
     */
    public void login(Scanner socketIn) {
        synchronized(lock) {
            invalidEnter = false;
            logged=false;
            used = false;
            full = false;
            punctuation = false;
            reconnected = false;
            String line = socketIn.nextLine();
            System.out.println(line);
            while (!line.equals("Login OK") && !line.equals("Continue")) {
                switch (line){
                    case "Invalid Enter":
                        invalidEnter = true;
                        lock.notifyAll();
                        break;
                    case "Username already used":
                        used = true;
                        lock.notifyAll();
                        break;
                    case "Punctuation found":
                        punctuation = true;
                        lock.notifyAll();
                        break;
                    case "Game already started":
                        full = true;
                        lock.notifyAll();
                        break;
                }
                try {
                    lock.wait(); //aspetto user and password
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                connection.sendMessage(buffer);
                line = socketIn.nextLine();
                System.out.println(line);
            }
            saveUserAndPass(buffer);
            if(line.equals("Login OK"))
                logged=true;
            else
                reconnected=true;
            lock.notifyAll();
        }

    }


    private void saveUserAndPass(String s) {
        String[] userAndPass = s.split(DIVISOR);
        this.name = userAndPass[0];
        this.pass = userAndPass[1];
        Platform.runLater(() -> {
            primaryStage.setTitle(TITLE + " - " + name.toUpperCase());
        });
    }


    /**
     * This method updates the scene into the waiting screen. It's used when a plyer needs to wait for other players to login.
     * @param scene1 the scene where to print the message
     * @param message to print
     */
    public void messageScene(Scene scene1, String message) {
        System.out.println("sono entrato in wait OP");
        MessageView root = new MessageView(message);
        scene1.setRoot(root);
        setSize(root.getSpane(), scene1);
    }


    private Scene ChooseSchemaScene() {
        ChooseSchemaView root= new ChooseSchemaView(privateTargetCard, possibleCards);
        Scene scene = new Scene(root);
        actualScene = scene;
        scene.getStylesheets().add("style.css");
        setSize(root.getSpane(),scene);
        scene.setRoot(root);
        root.getEnter().setOnAction(t -> { chooseSchemaAction(root,scene);});
        for(int i=0; i<N_RADIOBUTTON; i++){
            root.getRadioButton(i).setOnKeyPressed(t -> {
                if(t.getCode() == KeyCode.ENTER) {
                    chooseSchemaAction(root,scene);
                }
            });
        }
        return scene;
    }

    private void chooseSchemaAction(ChooseSchemaView root, Scene scene){
        if (root.getSelection().equals("*")) {
            root.setWarning_text("Scegli lo schema!");
        } else {
            loadBuffer(root.getSelection());
            connection.sendMessage(buffer);
            root.setWarning_text("");
            messageScene(scene, "Altri giocatori stanno scegliendo lo schema...");
        }
    }

    /**
     * Creates the view representation of the private target card
     * @param s the JSON string of the card
     */
    public synchronized void setPrivateCards(String s) {
        this.privateTargetCard = new Gson().fromJson(s, PrivateTargetCard.class);
        System.out.println(privateTargetCard.toString());

        if(!reconnected) {
            Platform.runLater(() -> {
                primaryStage.setScene(ChooseSchemaScene());
                primaryStage.show();
                primaryStage.setTitle(TITLE + " - " + name.toUpperCase());
                exitOnClose(primaryStage);
            });
        }


    }

    /**
     * Creates the view representation of the possible schema cards that the player can use. It's invoked during the setting state of the game
     * @param s the JSON string of the cards
     */
    public void setSchemaCards(String s) {
        possibleCards = new Gson().fromJson(s, new TypeToken <List<SchemaCard>>() {}.getType());
        allData = true;
    }

    /**
     * Creates the view representation of the player's glass dash.
     * @param s the JSON string of the glass dash
     */
    public void setGlassDash(String s) {
        this.glassDash = new Gson().fromJson(s, GlassDash.class);
    }


    //RoundView
    private void roundScene(RoundView root) {
        Platform.runLater(() -> {
            primaryStage.setScene(setRoundView(root));
            primaryStage.show();
            primaryStage.setTitle(TITLE + " - " + name.toUpperCase());
            exitOnClose(primaryStage);
        });

    }

    private Scene setRoundView(RoundView root){
        Scene roundScene = new Scene(root);
        roundScene.getStylesheets().add("style.css");
        setSize(root.getSpane(), roundScene);
        roundScene.setRoot(root);
        return roundScene;
    }

    /**
     * Creates the view representation of the public target cards used in the game.
     * @param s the JSON string of the cards
     */
    public void setPublicTargetCard(String s) {
        publicTargetCards = new Gson().fromJson(s, new TypeToken <List<PublicTargetCard>>(){}.getType());
        System.out.println(publicTargetCards.toString());
    }

    /**
     * Creates the view representation of the tool cards used in the game
     * @param s the JSON string of the cards
     */
    public void setToolCards(String s) {
        toolCards = new Gson().fromJson(s, new TypeToken <List<ToolCard>>(){}.getType());
        System.out.println(toolCards.toString());
    }

    /**
     * Creates the view representation of the other players' glass dash
     * @param s the JSON string of the players
     */
    public void setOtherPlayer(String s) {
        if(OPschemaCards!=null) {
            OPschemaCards.addAll(new Gson().fromJson(s, new TypeToken<List<Player>>() {}.getType()));
        }else{
            OPschemaCards = new Gson().fromJson(s, new TypeToken<List<Player>>() {}.getType());
        }

    }

    /**
     * Creates the view representation of the game's reserve. It also used by the view as a signal to change the waiting scene into the actual game scene.
     * @param s the JSON string of the reserve
     */
    public void setReserve(String s) {
        if(!started) {
            reserve = new Gson().fromJson(s, new TypeToken<ArrayList<Dice>>() {}.getType());
            addDiceView = new AddDiceView(connection);
            toolCardInfoView = new ToolCardInfoView(connection);
            roundRoot = new RoundView(toolCardInfoView, connection,addDiceView, OPschemaCards, glassDash.getSchemaCard(), toolCards, publicTargetCards, privateTargetCard, reserve, token);
            started=true;
            if(!reconnected)
                roundScene(roundRoot);
        }else {
            roundRoot.setReserve(new Gson().fromJson(s, new TypeToken<ArrayList<Dice>>() {}.getType()));

        }
    }

    /**
     * This method in invoked when it's time for this player to play.
     * It updates the state of the view, making it selectable, and sets the turn message.
     * @param str a flag that notifies whether the turn is/is not clockwise.
     */
    public void setIsYourTurn(String str) {

        Platform.runLater(() -> {
                    addDiceView.myTurn();
                    toolCardInfoView.myTurn();
                    if (str.equals("1")) {
                        roundRoot.setTurn_text("Primo turno");
                    } else if (str.equals("2")) {
                        second_turn = true;
                        roundRoot.setTurn_text("Secondo turno");
                    }
                startTimer(str);
                });

    }

    /**
     * start and run the turn timer
     * @param turn indicates if is my first or second turn in this round
     */
    private synchronized void startTimer(String turn){
        Timer timer = new Timer();
        skipped = false;
        for(int seconds=1; seconds<=turnTimer; seconds++) {
            int duration = turnTimer-seconds;
            timer.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                            if(skipped) {
                                Platform.runLater(
                                        () -> {
                                            roundRoot.setTimerview(0);
                                        }
                                );
                                second_turn = false;
                                timer.cancel();  //Terminates this timer,discarding any currently scheduled tasks.
                                timer.purge();   // Removes all cancelled tasks from this timer's task queue.
                            }else if((turn.equals("1") && second_turn)){
                                second_turn =false;
                                timer.cancel();  //Terminates this timer,discarding any currently scheduled tasks.
                                timer.purge();   // Removes all cancelled tasks from this timer's task queue.
                            }else{
                                Platform.runLater(
                                        () -> {
                                            roundRoot.setTimerview(duration);
                                        }
                                );
                                //System.out.println("Ti restano: " + duration + " secondi."); //ila deve aggiungere un display
                                if(duration==0){
                                    System.out.println("Auto-skip");
                                    connection.sendMessage("skip");
                                }
                            }


                                            }
            },(long)seconds*1000);
        }
    }

    /**
     * Removes a dice of the reserve from the view.
     * @param s the position of the dice
     */
    public void removeReseve(String s) {
        roundRoot.removeReserve(Integer.parseInt(s));
    }

    /**
     * Adds a dice in the player's schema
     * @param s the position on the schema + the JSON string of the dice
     */
    public void updateUserSchema(String s) {
        String[] parts = s.split(SEPARATOR);
        roundRoot.updateUserSchema(Integer.parseInt(parts[0]), new Gson().fromJson(parts[1], Dice.class), addDiceView);
    }

    /**
     * Adds a dice in the scheme of an opponent
     * @param s the id of the schema card of the opponent + the position of the dice + the JSON string of the dice
     */
    public void updateOtherUserSchema(String s) {
        String[] parts = s.split(SEPARATOR);
        roundRoot.updateOtherUserSchema(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), new Gson().fromJson(parts[2], Dice.class));
    }

    /**
     * A flag sent by the server to state that the placement is not valid
     */
    public void notValidDice() {
        addDiceView.notValidDice();
    }

    /**
     * A flag sent by the server to state that the placement is valid
     */
    public void validPlacement() {
        addDiceView.validPlacement();
    }

    /**
     * This method is invoked when a player can get some of his played token back
     * @param s a string stating the number of tokens
     */
    public void addToken(String s) {
        roundRoot.addTokenMessage(Integer.parseInt(s));
    }

    /**
     * This method is invoked when it's not the turn of the player.
     * It updates the state of the view, making it not selectable, and sets the turn message.
     */
    public void setIsNotYourTurn() {
        addDiceView.notMyTurn();
        toolCardInfoView.notMyTurn();
        roundRoot.setTurn_text("Non è il tuo turno");
        skipped = true;
        Platform.runLater(
                () -> {
                    roundRoot.setTimerview(0);
                }
        );
    }

    /**
     * Adds a dice to the view's representation of the roundtrack.
     * @param str the JSON string of the dice
     */
    public void addToRoundTrack(String str) {
        roundRoot.addRoundTrack(new Gson().fromJson(str, Dice.class));
    }


    private Scene FinalScene(ArrayList<Player> players){
        finalRoot = new FinalView(players);
        Scene scene3 = new Scene(finalRoot);
        actualScene = scene3;
        setSize(finalRoot.getSpane(),scene3);
        scene3.setRoot(finalRoot);
        return scene3;
    }

    /**
     * At the end of the game, the player receives an ordered array of the players according to their final classification.
     * @param s The JSON string of the players.
     */
    public void finalPoints(String s) {
        Platform.runLater(() -> {
            ArrayList<Player> players = new Gson().fromJson(s, new TypeToken<ArrayList<Player>>() {}.getType());
            primaryStage.setScene(FinalScene(players));
            primaryStage.show();
            primaryStage.setTitle(TITLE + " - " + name.toUpperCase());
            exitOnClose(primaryStage);
        });
    }

    /**
     *
     * @return  the internal buffer
     */
    public synchronized String getBuffer() {
        return buffer;
    }


    private synchronized void loadBuffer(String s){
        buffer = s;
    }

    /**
     * Removes a dice from the user's schema
     * @param str a string indicating the position of the dice
     */
    public synchronized void removeUserSchema(String str) {
        roundRoot.removeUserSchema(Integer.parseInt(str));
    }

    /**
     * Removes a dice from the opponent's schema
     * @param str the id of the opponent's schema + the position of the dice
     */
    public void removeOtherUserSchema(String str) {
        String[] parts = str.split(SEPARATOR);
        roundRoot.removeOtherUserSchema(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    /**
     * This method is invoked when a players made an invalid tool card move. It sends a notification to the view
     */
    public synchronized void notVaildToolCardAction() {
        toolCardInfoView.notValidToolCard();
    }

    /**
     * Modifies a dice on the view representation of roundtrack
     * @param str the position of the dice + the JSON string of the dice
     */
    public synchronized void updateRoundTracker(String str){
        String[] parts = str.split(SEPARATOR);
        roundRoot.updateRoundTrack(Integer.parseInt(parts[0]), new Gson().fromJson(parts[1], Dice.class));
    }

    /**
     * This method is invoked when a players made an valid tool card move. It sends a notification to the view
     */
    public synchronized void validToolCard() {
        toolCardInfoView.valid();
    }

    /**
     * Set the variable the value of the turn timer
     * @param str that contains the value of the timer
     */
    public synchronized void setTurnTimer(String str) {
        this.turnTimer = Integer.parseInt(str)/1000;
        System.out.println(str);
    }

    /**
     * Set this player as offline in gui
     * @param user to set offline
     */
    public void setOfflinePlayer(String user) {
        System.out.println("2.New disconnected  + user");
        roundRoot.getNewDisconnected(user);
    }

    /**
     * During reconnection, the player needs to receive all the information about the current state of the game.
     * When all of the information is received, the server sends a flag that invokes this method, making possile to load the scene of the game.
     */
    public void showRoundView() {
        roundScene(roundRoot);
    }

    /**
     * When a player gets back in the game, the server sends to all the other players a flag that invokes this method and update the view.
     * @param str the name of the reconnected player
     */
    public void setOnlinePlayer(String str) {
        roundRoot.newReconnected(str);
    }

    /**
     * If a player can select a tool card, the server sends a flag to him that invokes this method.
     * @param str the index of the selected tool card + the number of tokens to add
     */
    public void tokenConfirmed(String str) {
        String[] parts = str.split(SEPARATOR);
        roundRoot.tokenConfirmed(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    /**
     * If a player can't select a tool card, the server sends a flag to him that invokes this method.
     * @param str the index of the selected tool card.
     */
    public void notEnoughTokens(String str) {
        roundRoot.notEnoughTokens(Integer.parseInt(str));
    }

    /**
     * Sets of many tokens a player has.
     * @param str that contains the number of tokens
     */
    public void setTokens(String str) {
        token=Integer.parseInt(str);

    }

    /**
     *
     * @return the current scene
     */
    public Scene getActualScene(){
        return actualScene;
    }

    /**
     * If some tokens needs to be removed from a tool card, this method is invoked.
     * @param str the selected tool card + the number of token
     */
    public void removeTokens(String str) {
        String[] parts = str.split(SEPARATOR);
        roundRoot.removeTokens(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    /**
     * If a tool card can be selected ith one token, a flag is sent to the player and this method is invoked
     * @param str the index of the tool card
     */
    public void setToolOneToken(String str) {
        roundRoot.setToolOneToken(Integer.parseInt(str));
    }
}
