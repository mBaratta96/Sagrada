package it.polimi.ingsw.net.database;

import it.polimi.ingsw.controller.Game;
import it.polimi.ingsw.controller.Player;
import it.polimi.ingsw.net.server.socket.ServerSocketClientHandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PlayersDB {

    private final static int MAX_PLAYER = 4;
    private final static String WAITING_TIMER = "src/main/resources/ServerConfig/WaitingTimer.txt";
    private final static String TURN_TIMER = "src/main/resources/ServerConfig/TurnTimer.txt";
    private static final int nPlayerForStart = 2;
    private static final int MILLS_IN_SEC = 1000;

    private ArrayList<ServerSocketClientHandler> tempConnection;
    private ArrayList<Player> logged;
    private ArrayList<Player> disconnected;

    private boolean timeOut;
    private int turnTimer;
    private int orderPing;

    private Game game;
    private int dashed;
    private boolean started;
    private static PlayersDB instance;


    private PlayersDB(){
        this.tempConnection = new ArrayList <>();
        this.logged = new ArrayList <>();
        this.disconnected = new ArrayList <>();
        this.timeOut = false;
        this.dashed = 0;
        orderPing=0;
        started = false;
        this.turnTimer = getMilliseconds(TURN_TIMER)*MILLS_IN_SEC;
    }

    /**
     * This class is a Singleton and this is the method who instance or not new the class
     * @return the instance of PlayerDB
     */
    public synchronized static PlayersDB getInstance(){
        if(instance==null){
            instance = new PlayersDB();
        }
        return instance;
    }

    /**
     * @return true if there're free place to play
     */
    public synchronized boolean freePlace() {
        return logged.size() < MAX_PLAYER;
    }

    /**
     *
     * @param user to check
     * @param pass to check
     * @return true if the user who is trying to connect is a disconnected player who want to continue the game
     */
    public synchronized boolean checkDisconnected(String user, String pass){
        for(Player p: disconnected){
            if(p.getUsername().equals(user) && p.getPassword().equals(pass)){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param user to check
     * @param pass to check
     * @return the disconnected Player with this User and Password
     */
    public Player getDisconnected(String user, String pass){
        int index = disconnected.stream()
                .filter(p -> p.getUsername().equals(user) && p.getPassword().equals(pass))
                .mapToInt(p -> disconnected.indexOf(p))
                .sum();
        return disconnected.get(index);
    }

    /**
     * Remove a disconnected Player from the array "disconnected" and add him in the Game
     * @param player to remove from diconnected and add to game
     */
    public synchronized void removeFromDisconnected(Player player) {
        disconnected.remove(player);
        game.addPlayer(player);
    }

    /**
     *
     * @param username to check
     * @return true if there's already a Player with this username
     */
    public synchronized boolean sameUsername(String username){
        for(Player p: logged){
            if(username.equals(p.getUsername())){
                return true;
            }
        }
        return false;
    }

    /**
     * Save this player into the logged
     * @param player to add in logged
     * @return the id of the new connected player
     */
    public synchronized int addPlayer(Player player) { //controllo se ci sono posti liberi
        logged.add(player); //controlla indice
        startTimer();
        return logged.indexOf(player);
    }

    /**
     * starts the timer for waiting other player only if the number of logged is equals to 2
     */
    private synchronized void startTimer(){
        if(logged.size()==nPlayerForStart){
            timeOut = false;
            Timer timer = new Timer();
            TimerTask task = new ThreadTimer(this);
            timer.schedule(task, (long) getMilliseconds(WAITING_TIMER)*MILLS_IN_SEC);
        }
    }

    /**
     * Read from file (located in path) the milliseconds of the timer
     * @param path where the file is located
     * @return the milliseconds read
     */
    private int getMilliseconds(String path){
        String s = "Timer -> 1000";
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            s = reader.readLine();
        } catch (FileNotFoundException e) {
            System.err.println("Errore apertura file " + path);
        } catch (IOException e) {
            System.err.println("Errore lettura file " + path);
        } catch (NullPointerException ne) {
            System.out.println("Errore path " + path);
        }
        String[] parts = new String[0];
        if (s != null) {
            parts = s.split("-> ");
        }
        int time = 0;
        try {
            time = Integer.parseInt(parts[1]);
        }catch (Exception e) {
            System.err.println("Si prega di inserire un numero dopo la '->' e di lasciare uno spazio nel file: "+ path);
            System.err.println("Esempio: '-> 60'");
            System.exit(0);
        }
        return time;
    }

    /**
     * set timeOut
     * @param b if timeOut is to set true or false
     */
    public void setTimeOut(boolean b){ this.timeOut = b; }

    /**
     *
     * @return id
     */
    public int getID(){
        return logged.size()-1;
    }

    /**
     *
     * @return true if the timer has finish and logged are more than 1
     */
    private synchronized boolean canStart(){
        return timeOut && logged.size() > 1;
    }

    /**
     * add new connection to the array's tempConnection
     * @param serverSocketClientHandler the instance of the connection
     */
    public synchronized void addTempConnection(ServerSocketClientHandler serverSocketClientHandler){
        tempConnection.add(serverSocketClientHandler);
    }

    /**
     *
     * @return true if the condition for starting the game have been respected
     */
    public synchronized boolean startGame(){
        if(canStart()) {
            this.game = Game.getInstance(logged, disconnected);
            started = true;
            return true;
        }
        return false;
    }

    /**
     *
     * @return true if game is started
     */
    public synchronized boolean arePlaying(){
        return started;
    }

    /**
     *
     * @return an instance of game
     */
    public synchronized Game getGame() {
        return game;
    }

    /**
     * it is used when a player receives correctly the GlassDash
     */
    public synchronized void setGlassDash(){
        dashed++;
    }

    /**
     *
     * @return true if all the players received the GlassDash
     */
    public synchronized boolean allDashed() {
        return dashed==logged.size();
    }

    /**
     * Remove the connection of this player from tempConnection
     * @param player to remove
     */
    public synchronized void removeTempConnection(ServerSocketClientHandler player) {
        tempConnection.remove(player);
        orderPing--;
    }

    /**
     *
     * @return the connection
     */
    public synchronized ServerSocketClientHandler getTempConnection() {
        while(orderPing>=tempConnection.size());
        System.out.println("sto dando al ping il client n:"+orderPing);
        orderPing++;
        return tempConnection.get(orderPing-1);
    }

    /**
     * Add this player to disconnected
     * @param player to add
     */
    public synchronized void addDisconnected(Player player) {
        if(game.getCurrent_player().equals(player)) {
            game.currentPlayerDisconnected();
            disconnected.add(player);
        }else{
            if(game.isClockwise()){
                game.onePlayerLess();
            }
            logged.remove(player);
            disconnected.add(player);
            logged.forEach(p -> p.warningDisconnected(player.getUsername()));
        }
    }

    /**
     * Remove this player from the Logged player
     * @param player to remove from logged
     */
    public synchronized void removeFromLogged(Player player) {
        logged.remove(player);
    }

    /**
     *
     * @return the milliseconds of the timer who handled the turns
     */
    public synchronized int getTurnTimer(){
        return turnTimer;
    }
}
