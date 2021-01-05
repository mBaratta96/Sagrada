package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.GlassDash;
import it.polimi.ingsw.model.cards.PrivateTargetCard;
import it.polimi.ingsw.net.server.socket.ServerSocketClientHandler;

import java.util.ArrayList;

public class Player {

    private GlassDash glassDash;
    private String username;
    private transient String password;
    private transient boolean skip_next_turn=false;
    private PrivateTargetCard privateTargetCard;
    private int tokens;
    private int points;
    private transient ServerSocketClientHandler connection;
    private transient boolean diceAdded = false;
    private transient boolean toolCardActivated = false;
    private transient boolean toolCardUsed = false;
    private transient boolean tokenAdded = false;
    private transient boolean clientDisconnected=false;
    private transient boolean firstStep = false;
    private transient ArrayList<String> clientDisconnectedUser = new ArrayList<>();
    private transient ArrayList<String> clientReconnectedUser = new ArrayList<>();
    private transient boolean clientReconnected = false;
    private static final String SEPARATOR = "___";

    public Player(String username, String password, ServerSocketClientHandler connection){
        this.username = username;
        this.password = password;
        this.connection=connection;

    }
    public Player(String username){
        this.username = username;
    }

    /**
     *
     * @param skip_next_turn a flag to advise the game if this player has to skip the next turn
     */
    public void setSkipNextTurn(boolean skip_next_turn) {
        this.skip_next_turn = skip_next_turn;
    }

    /**
     *
     * @return a flag to advise the game if this player has to skip the next turn
     */
    public boolean isSkipNextTurn() {
        return skip_next_turn;
    }

    /**
     *
     * @return a flag to check if the player added a dice in his glassdash
     */
    public boolean isDiceAdded() {
        return diceAdded;
    }

    /**
     *
     * @return a flag to check if a player selected a tool card
     */
    public boolean isToolCardActivated() {
        return toolCardActivated;
    }

    /**
     *
     * @param toolCardActivated a flag to check if a player selected a tool card
     */
    public void setToolCardActivated(boolean toolCardActivated) {
        this.toolCardActivated = toolCardActivated;
    }


    /**
     *
     * @param toolCardUsed a flag to check if a player selected and used a tool card
     */
    public void setToolCardUsed(boolean toolCardUsed) {
        this.toolCardUsed = toolCardUsed;
    }

    /**
     *
     * @return a flag to check if a player selected and used a tool card
     */
    public boolean isToolCardUsed() {
        return toolCardUsed;
    }


    /**
     *
     * @param diceAdded a flag to check if the player added a dice in his glassdash
     */
    public void setDiceAdded(boolean diceAdded){
        this.diceAdded=diceAdded;
    }

    /**
     *
     * @param dash the glassdash that the player will use during the game
     */
    public void addGlassDash(GlassDash dash) {
        glassDash = dash;
        setTokens(glassDash.getSchemaCard().getInfo());

    }

    /**
     *
     * @return the player's glassdash
     */
    public GlassDash getGlassDash() {
        return glassDash;
    }

    /**
     *
     * @param privateTargetCard the private target card that the player will use during the game
     */
    public void setPrivateTargetCard(PrivateTargetCard privateTargetCard){
        this.privateTargetCard=privateTargetCard;
    }

    /**
     *
     * @return the private target card that the player will use during the game
     */
    public PrivateTargetCard getPrivateTargetCard() {
        return privateTargetCard;
    }

    /**
     * This method sends through connection the notification that the player cannot play
     */
    public void printNotTurn(){
        checkDisconnected();
        checkReconneced();
        connection.sendMessage("It's not your turn");
    }

    /**
     * This method sends through connection the notification that the player cannot play
     * @param orario a flag that advise whether the turn is of the clockwise sense or not
     */
    public void printYourTurn(boolean orario){
        checkDisconnected();
        checkReconneced();
        connection.sendMessage("Your Turn");
        if(orario){
            connection.sendMessage("1");
        }else{
            connection.sendMessage("2");
        }
    }

    private void checkDisconnected(){
        if(clientDisconnected){
            clientDisconnectedUser.forEach(user -> {
                connection.sendMessage("New Disconnected");
                connection.sendMessage(user);
            });
            clientDisconnectedUser.clear();
            clientDisconnected=false;
        }
    }

    private void checkReconneced(){
        if(clientReconnected){
            clientReconnectedUser.forEach(user -> {
                connection.sendMessage("Reconnected");
                connection.sendMessage(user);
            });
            clientReconnectedUser.clear();
            clientReconnected=false;
        }
    }

    /**
     *
     * @param tokens the number of token assigned to the player
     */
    public void setTokens(String tokens) {
        this.tokens = Integer.parseInt(tokens);
    }

    public void addTokens(int n){
        tokens+=n;
    }

    /**
     *
     * @return the number of current tokens the player has
     */
    public int getTokens() {
        return tokens;
    }

    /**
     *
     * @param n_token the number of tokens that the player added to a tool card
     */
    public void removeTokens(int n_token){
        tokens -= n_token;
    }


    /**
     *
     * @return player's username
     */
    public String getUsername() { return username; }

    /**
     *
     * @return player's password
     */
    public String getPassword() { return password; }

    /**
     *
     * @return the points that the player has gained at the end of the game
     */
    public int getPoints() { return points; }

    /**
     *
     * @param points the points that the player has gained at the end of the game
     */
    public void setPoints(int points){
        this.points=points;
    }

    /**
     *
     * @return the connection that is utilized to communicate to the player through the net
     */
    public ServerSocketClientHandler getConnection() {
        return connection;
    }

    /**
     *
     * @param connection the connection that will be utilized to communicate to the player through the net
     */
    public void setConnection(ServerSocketClientHandler connection){
        this.connection = connection;
    }

    @Override
    public String toString() {
        return String.format("player: %s%n%s%n%s", username, glassDash.toString(), glassDash.getSchemaCard().toString());
    }

    /**
     *
     * @param reserve send throug connection the dices of the reserve
     */
    public void addReserve(ArrayList<Dice> reserve) {
        connection.sendMessage("ReserveView");
        connection.sendMessage(new Gson().toJson(reserve));
    }

    /**
     * This method sends to the player the dice added by another player, in order to update his view
     * @param id the id of the schema card to which the dice has been added
     * @param index the position of the cell where the dice has been added
     * @param d the dice that has been added
     */
    public void updateOtherUserSchema(int id, int index, Dice d) {
        connection.sendMessage("UpdateOtherUserSchema");
        String message = String.format("%d___%d___%s", id, index, new Gson().toJson(d));
        connection.sendMessage(message);
    }

    /**
     * This method is invoked when a dice has been removed from the reserve. It will be also removed from the View
     * @param placeOnReserve the position of the removed dice on the reserve
     *
     */
    public void removeReserve(int placeOnReserve) {
        connection.sendMessage("Remove Reserve");
        connection.sendMessage(String.valueOf(placeOnReserve));
    }

    /**
     * This method is invoked when the placement of a dice is valid, and sends confirmation to the player
     */
    public void validPlacement() {
        connection.sendMessage("ValidPlacement");
    }

    /**
     * This method is invoked when a dice is added to the glass dash of the player. It sends a message to update the view
     * @param index the position in the glass dash
     * @param d the added dice
     */
    public void updateUserSchema(int index, Dice d) {
        connection.sendMessage("UpdateUserSchema");
        connection.sendMessage(String.valueOf(index)+SEPARATOR+new Gson().toJson(d));
    }

    /**
     * This method is invoked when a round is over and the last dice of the reserve is added to the round track. It sends a message to update the view
     * @param dice the added dice
     */
    public void addToRoundTrack(Dice dice) {
        connection.sendMessage("AddToRoundTrack");
        connection.sendMessage(new Gson().toJson(dice));
    }

    /**
     * This method is invoked when a player added some tokens to a tool card
     * @param i the number of the selected tool card
     */
    public void addToken(int i){
        connection.sendMessage("addTokenMessage");
        connection.sendMessage(String.valueOf(i));
    }

    /**
     * This method is invoked at the end of the game. Sends the points of all players
     * @param classification an ordered list of the players. From best to worst
     */
    public void sendClassification(ArrayList<Player> classification) {
        connection.sendMessage("Ranking");
        connection.sendMessage(new Gson().toJson(classification));
    }

    /**
     * This method is invoked when an action performed during the usage of a card tool is not valid
     */
    public void notValidToolCardAction() {
        connection.sendMessage("notValidToolCardAction");
    }

    /**
     * This method is invoked when another player takes a dice from his glass dash. It modifies the player's view
     * @param id the id of the schema card insiede the glass dash. It's utilized to reconize which is the modified glass dash
     * @param index_f the position of the removed dice
     */
    public void removeOtherUserSchema(int id, int index_f) {
        connection.sendMessage("RemoveOtherUserSchema");
        connection.sendMessage(String.format("%d___%d", id, index_f));
    }

    /**
     * This method is invoked when the player has removed a dice from his glass dash
     * @param index_i the position of the removed dice
     */
    public void removeUserSchema(int index_i) {
        connection.sendMessage("RemoveUserSchema");
        connection.sendMessage(String.valueOf(index_i));
    }

    /**
     * This method is invoked when a dice on the roundtrack has been substituted/modified. It modifies the player view
     * @param index_f the position of the dice
     * @param dice the modifies dice
     */
    public void updateRoundTrack(int index_f, Dice dice) {
        connection.sendMessage("UpdateRoundTrack");
        connection.sendMessage(String.valueOf(index_f)+SEPARATOR+new Gson().toJson(dice));

    }

    /**
     * Sends confirmation of the action made by the tool card
     */
    public void validToolCard() {
        connection.sendMessage("validToolCardAction");
    }

    /**
     * add a player to the list of disconnected player. When the turn is passed, the player view is updated.
     * @param username the username of the disconnected player
     */
    public void warningDisconnected(String username) {
        clientDisconnectedUser.add(username);
        clientDisconnected=true;
    }

    /**
     * inform the player's view when he can show the scene of the game
     */
    public void showRoundView() {
        connection.sendMessage("showRoundView");
    }

    /**
     * add a player to the list of reconnected player. When the turn is passed, the player view is updated.
     * @param newPlayer
     */
    public void warningReconnected(String newPlayer) {
        clientReconnectedUser.add(newPlayer);
        clientReconnected = true;
    }

    /**
     * This method is invoked when the player doesn't have enough token to activate a tool card
     * @param card the selected tool card
     */
    public void notEnoughTokens(int card) {
        connection.sendMessage("notEnoughTokens");
        connection.sendMessage(String.valueOf(card));
    }

    /**
     * This method is invoked when the player has enough token to activate a tool card
     * @param card the selected tool card
     * @param tokensRequired the number of tokens added to the tool card
     */
    public void tokenConfirmed(int card, int tokensRequired) {
        connection.sendMessage("tokenConfirmed");
        connection.sendMessage(String.valueOf(card)+SEPARATOR+String.valueOf(tokensRequired));
    }

    /**
     * Sends to the client the number of tokens he has
     */
    public void sendCurrentTokens() {
        connection.sendMessage("Current tokens");
        connection.sendMessage(String.valueOf(tokens));
    }

    /**
     * Advise the client that some token have been removed from a tool card
     * @param i the index of the tool card
     * @param i1 how dice dices have been removed
     */
    public void tokensRemoved(int i, int i1) {
        connection.sendMessage("Tokens removed");
        connection.sendMessage(String.valueOf(i)+SEPARATOR+String.valueOf(i1));
    }

    /**
     * Sends to the client that a tool card needs one token to be selected.
     * @param i the index of the card
     */
    public void toolOneToken(int i) {
        connection.sendMessage("ToolOneToken");
        connection.sendMessage(String.valueOf(i));
    }

    /**
     * Updates the number of tokens the client added
     * @param tokenAdded the number of added token
     */
    public void setTokenAdded(boolean tokenAdded){
        this.tokenAdded=tokenAdded;
    }

    /**
     *
     * @return a flag that checks whether or not a client added some tokens
     */
    public boolean isTokenAdded() {
        return tokenAdded;
    }

    /**
     * This method is invoked when a player activated the tool cards n° 4,11,12
     * @return a flag that checks if the player has made the first step of the tool card
     */
    public boolean isFirstStep() {
        return firstStep;
    }

    /**
     * This method is invoked when a player activated the tool cards n° 4,11,12
     * @param firstStep a flag that checks if the player has made the first step of the tool card
     */
    public void setFirstStep(boolean firstStep) {
        this.firstStep = firstStep;
    }
}
