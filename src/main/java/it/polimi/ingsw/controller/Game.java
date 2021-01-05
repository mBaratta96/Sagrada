package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.DiceBag;
import it.polimi.ingsw.model.RoundTrack;
import it.polimi.ingsw.model.ToolCardInfo;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.deks.PrivateTargetCardDeck;
import it.polimi.ingsw.model.deks.PublicTargetCardDeck;
import it.polimi.ingsw.model.deks.SchemaCardDeck;
import it.polimi.ingsw.model.deks.ToolCardDeck;
import it.polimi.ingsw.model.exceptions.NotValidException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game {

    private static Game instance;
    private DiceBag diceBag;
    private SchemaCardDeck schemaCardDeck;
    private PrivateTargetCardDeck privateTargetCardDeck;
    private List<PublicTargetCard> publicTargetCards;
    private List<ToolCard> toolCards;
    private RoundTrack roundTrack;
    private ArrayList<Dice> reserve;
    private ArrayList<Player> players;
    private ArrayList<Player> disconnected;
    private Player toolCardExecutor;
    private boolean clockwise;
    private int current_player;
    private boolean ended;
    private static final String DICE_BAG_PATH = "src/main/resources/GameFiles/DiceBag.json";
    private static final String SCHEMA_CARD_DECK_PATH = "src/main/resources/GameFiles/SchemaCardDeck.json";
    private static final String PRIVATE_TARGET_CARD_DECK_PATH = "src/main/resources/GameFiles/PrivateTargetCardDeck.json";
    private static final String PUBLIC_TARGET_CARD_DECK_PATH = "src/main/resources/GameFiles/PublicTargetCardDeck.json";
    private static final String TOOL_CARD_DECK_PATH = "src/main/resources/GameFiles/ToolCardDeck.json";
    private ArrayList<Player> playersToAdd;


    private Game(ArrayList<Player> players, ArrayList<Player> disconnected){
        diceBag = new Gson().fromJson(readFromFile(DICE_BAG_PATH), DiceBag.class);
        schemaCardDeck = new Gson().fromJson(readFromFile(SCHEMA_CARD_DECK_PATH), SchemaCardDeck.class);
        privateTargetCardDeck = new Gson().fromJson(readFromFile(PRIVATE_TARGET_CARD_DECK_PATH), PrivateTargetCardDeck.class);
        PublicTargetCardDeck publicTargetCardDeck = new Gson().fromJson(readFromFile(PUBLIC_TARGET_CARD_DECK_PATH), PublicTargetCardDeck.class);
        ToolCardDeck toolCardDeck = new Gson().fromJson(readFromFile(TOOL_CARD_DECK_PATH), ToolCardDeck.class);
        toolCardDeck.getDeck().forEach(t -> t.setTokenRequired(1));
        Collections.shuffle(privateTargetCardDeck.getDeck());
        Collections.shuffle(publicTargetCardDeck.getDeck());
        publicTargetCards = Arrays.asList(publicTargetCardDeck.getDeck().get(0),
                                            publicTargetCardDeck.getDeck().get(1),
                                                publicTargetCardDeck.getDeck().get(2));
        Collections.shuffle(schemaCardDeck.getDeck());
        Collections.shuffle(toolCardDeck.getDeck());

        toolCards = Arrays.asList(toolCardDeck.getDeck().get(0), toolCardDeck.getDeck().get(1), toolCardDeck.getDeck().get(2));
        roundTrack = new RoundTrack();
        reserve = new ArrayList<>();
        this.players = players;
        this.disconnected=disconnected;
        clockwise = true;
        current_player=1;
        extractDices();
        ended=false;
    }

    /**
     * Check if it's the turn of a player
     * @param p the player who's asking
     * @return true or fale
     */
    public synchronized boolean isMyTurn(Player p){
        return players.indexOf(p) == current_player-1;
    }

    /**
     * The class Game follows the singleton pattern.
     * @param players an array of the player that will play
     * @param disconnected an array of the players that may disconnect in the future
     * @return the instance of the game.
     */
    public synchronized static Game getInstance(ArrayList<Player> players, ArrayList<Player> disconnected){
        if(instance==null){
            instance = new Game(players, disconnected);
        }
        return instance;
    }

    private void calculateScore(){
        players.forEach(p -> { int risultato = p.getPrivateTargetCard().execute(p.getGlassDash().getGrid()) +
                                                publicTargetCards.stream()
                                                        .mapToInt(c -> c.execute(p.getGlassDash().getGrid()))
                                                        .sum() +
                                                p.getTokens() -
                                                p.getGlassDash().emptyCells();
                                System.out.println("risultato calcolato");
                                p.setPoints(risultato);
        });
        ArrayList<Player> classifica = players.stream()
                                        .sorted(Comparator.comparing(Player::getPoints))
                                        .collect(Collectors.toCollection(ArrayList::new));
        Collections.reverse(classifica);
        System.out.println("sono arrivato qui");
        players.forEach(player -> player.sendClassification(classifica));
    }

    /**
     * @return the instance of the dicebag
     */
    public DiceBag getDiceBag() {
        return diceBag;
    }

    /**
     * @return the public target cards used in the game
     */
    public List<PublicTargetCard> getPublicTargetCards() {
        return publicTargetCards;
    }

    /**
     *@return  the tool cards used in the game
     */
    public List<ToolCard> getToolCards() {
        return toolCards;
    }

    private void extractDices(){
        reserve = diceBag.extract(2*players.size()+1);
    }

    /**
     *Check if the round if following clockwise turn sense
     */
    public boolean isClockwise() {
        return clockwise;
    }

    /**
     * @return the player who's playing in the current turn
     *
     */
    public Player getCurrent_player() {
        return players.get(current_player-1);
    }

    /**
     *
     * @return the username of the current player
     */
    public String getCurrent_playerUser() {
        return players.get(current_player-1).getUsername();
    }

    /**
     * This method is invoked when the current player wants to add a dice to his glassdash.
     * First it checks if the addition is possible, then proceeds to add the dice and notify the other players of the addiction
     * @param placeOnReserve the index of the selected dice on the reserve
     * @param index the index in the glassdash where the player wants to add the dice
     * @param player the player executing the addition
     * @throws NotValidException if the addition is against the rules
     */
    public synchronized void addDice(int placeOnReserve, int index, Player player) throws NotValidException {
        if(!player.getGlassDash().allowedToPlaceDice(reserve.get(placeOnReserve), index) || getCurrent_player().isDiceAdded()){throw new NotValidException("dice not allowed. retry");}
        Dice d = reserve.get(placeOnReserve);
        player.getGlassDash().addDice(d, index);
        reserve.remove(placeOnReserve);
        player.validPlacement();
        player.updateUserSchema(index, d);
        //
        for(Player p : players){
            p.removeReserve(placeOnReserve);
            if(!p.equals(player)){
                p.updateOtherUserSchema(getCurrent_player().getGlassDash().getSchemaCard().getID(), index, d);
            }
        }
        getCurrent_player().setDiceAdded(true);
        checkEndTurn();
    }

    private void checkEndTurn(){
        if(getCurrent_player().isDiceAdded() && getCurrent_player().isToolCardUsed())
            endTurn();
    }

    /**
     * This method is invoked when a player pass his turn. It selects the new current player and puts the old player in a default
     * condition. If the round is over, it sets a new round.
     */
    public synchronized void endTurn(){
        if(players.size()<2){
            calculateScore();
            return;
        }
        getCurrent_player().setToolCardActivated(false);
        getCurrent_player().setDiceAdded(false);
        getCurrent_player().setToolCardUsed(false);
        getCurrent_player().setTokenAdded(false);
        orderRound();
        checkSkipTurn();
        sendTurnMessages();
    }

    private void sendTurnMessages(){
        if(!ended) {
            players.stream()
                    .filter(p -> !p.equals(getCurrent_player()))
                    .forEach(Player::printNotTurn);
            getCurrent_player().printYourTurn(clockwise);
        }
    }

    private void checkSkipTurn(){
        while(getCurrent_player().isSkipNextTurn()) {
            getCurrent_player().setSkipNextTurn(false);
            orderRound();
        }
    }

    /**
     *
     * @return the dices in the reserve
     */
    public ArrayList<Dice> getReserve() {
        return reserve;
    }

    /**
     *
     * @return the instance of the roundtrack
     */
    public synchronized RoundTrack getRoundTrack() {
        return roundTrack;
    }

    /**
     *
     * @param p the player who asks for his private target card
     * @return the private card of p
     */
    public PrivateTargetCard getPrivateTargetCard(Player p) {
        return privateTargetCardDeck.getDeck().get(players.indexOf(p));
    }


    /**
     * This method checks if the current player can place new tokens on a tool card. If the check is positive than proceeds to
     * notify to all players the addition and removes the tokens from the player.
     * @param card the index of the tool card that has been selected by the player
     */
    public void addToken(int card, Player player){
        toolCardExecutor=player;
        int tokensRequired = toolCards.get(card).getTokenRequired();
        if(getCurrent_player().getTokens()<tokensRequired || !toolCards.get(card).getPreCheck(this) || player.isTokenAdded()){
            getCurrent_player().notEnoughTokens(card);
        }else {
            player.setTokenAdded(true);
            getCurrent_player().removeTokens(tokensRequired);
            toolCards.get(card).addToken(tokensRequired);
            getCurrent_player().tokenConfirmed(card, tokensRequired);
            players.forEach(p -> p.addToken(card));
        }
    }

    /**
     * When a player deselects a tool card, it sends to the server a request to remove the played tokens.
     * This method handles the request according to the state of the game and the tool card.
     * @param i the index of the tool card
     * @param player the player making the request
     */
    public void removeToken(int i, Player player) {
        if(!player.isFirstStep()) {
            player.setTokenAdded(false);
            if (toolCards.get(i).showToken() > 1) {
                toolCards.get(i).removeTokens(2);
                player.addTokens(2);
                player.tokensRemoved(i, 2);
            } else {
                toolCards.get(i).removeTokens(1);
                player.addTokens(1);
                player.tokensRemoved(i, 1);
                players.forEach(p -> p.toolOneToken(i));
            }
        }else{
            player.setToolCardUsed(true);
        }
    }


    private void orderRound(){
        if(clockwise){
            if(current_player < players.size()){
                current_player++;
            }else {
                clockwise=false;
            }
        }else {
            if(current_player>1){
                current_player--;
            }else {
                endRound();
             }
        }
    }
    private void sendCurrentRoundTrack(Player player){
        roundTrack.getRoundTrack().forEach(d -> player.addToRoundTrack(d));
    }

    private void sendCurrentOtherUserSchema(Player player){
        seeOtherPlayers(player).forEach(player1 -> {
            ArrayList<Dice> OPgrid = player1.getGlassDash().getGrid();
            OPgrid.stream()
                    .filter(d -> d.getColor()!='+')
                    .forEach(d -> player.updateOtherUserSchema(player1.getGlassDash().getSchemaCard().getID(), OPgrid.indexOf(d), d));
        });
    }
    private void sendCurrentGlassDash(Player player){
        ArrayList<Dice> grid = player.getGlassDash().getGrid();
        System.out.println(grid);
        grid.stream()
                .filter(d -> d.getColor()!='+')
                .forEach(d -> player.updateUserSchema(grid.indexOf(d), d));

    }
    private synchronized void endRound(){
        roundTrack.nextRound(reserve.get(0));
        if(roundTrack.getRound()==10){
            System.out.println("game over");
            calculateScore();
            ended=true;
        }else {
            players.forEach(p -> p.addToRoundTrack(reserve.get(0)));
            if(playersToAdd!=null){
                if(!disconnected.isEmpty()) {
                    playersToAdd.forEach(player -> {
                                    player.getConnection().sendMessage("Players");
                                    player.getConnection().sendMessage(new Gson().toJson(disconnected));
                                    disconnected.forEach(dis -> player.warningDisconnected(dis.getUsername()));
                    });
                }
                playersToAdd.forEach(newPlayer -> players.forEach(p -> p.warningReconnected(newPlayer.getUsername())));
                players.addAll(playersToAdd);
            }
            extractDices();
            if(playersToAdd!=null) {
                playersToAdd.forEach(p -> {
                    p.getConnection().sendMessage("Private");
                    p.getConnection().sendMessage(new Gson().toJson(p.getPrivateTargetCard()));
                    p.getConnection().sendGame(p.getGlassDash(), true);
                    sendCurrentGlassDash(p);
                    sendCurrentOtherUserSchema(p);
                    sendCurrentRoundTrack(p);
                    p.showRoundView();
                });
                playersToAdd=null;
            }
            Player pInit = players.get(0);
            players.remove(0);
            players.add(pInit);
            clockwise = true;
            current_player = 1;
            players.forEach(p -> p.addReserve(reserve));
        }
    }



    /**
     *
     * @param player the player asking for information of other players
     * @return the list of the game's player, with the exclusion of p
     */
    public synchronized List<Player> seeOtherPlayers(Player player){
        return players.stream()
                        .filter(p -> !p.equals(player))
                        .collect(Collectors.toList());
    }

    private static String readFromFile(String filePath){
        String cardJSON = "";
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                cardJSON = reader.readLine();
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cardJSON;
    }

    /**
     * This method is invoked during the setting of the game, when every player needs to choose his own schema card
     * @param idClient the player's Id (assigned during the connection procedure)
     * @return the two schema cards (four sides) that can be choosen by the player
     */
    public List<SchemaCard> getSchemaCards(int idClient) {
        List<SchemaCardBackFront> cards = IntStream.range(0,2)
                                        .mapToObj(i -> schemaCardDeck.getDeck().get(2*idClient+i))
                                        .collect(Collectors.toList());
        return Arrays.asList(cards.get(0).getFront(), cards.get(0).getBack(),
                                                        cards.get(1).getFront(), cards.get(1).getBack());
    }

    /**
     * This method is invoked only after the game has checked if the player has enough tokens
     * @param i the index of the selected tool card
     * @param toolCardInfo the instance of the tool card information class
     */
    public synchronized void executeToolCard(int i, ToolCardInfo toolCardInfo, Player player) {
        toolCardExecutor=player;
        toolCards.get(i).setAction(this);
        try {
            toolCards.get(i).activate(toolCardInfo);
        } catch (NotValidException e) {
            player.notValidToolCardAction();
            toolCards.get(i).resetAction();
            return;
        }
        player.validToolCard();
        player.setToolCardUsed(true);
        if((toolCards.get(i).getID()==11 || toolCards.get(i).getID()==4 || toolCards.get(i).getID()==12) && !toolCardInfo.isSecondStep11()){
            player.setToolCardUsed(false);
            player.setFirstStep(true);
        }else{
            player.setFirstStep(false);
        }
        if(player.isToolCardActivated()){
           updateOtherUser(i, toolCardInfo);
           player.setToolCardActivated(false);
        }
        if(toolCardInfo.isReserveModified()){
            players.forEach(p -> p.addReserve(reserve));
        }
        if(toolCardInfo.isRoundTrackModified()){
            players.forEach(p -> p.updateRoundTrack(toolCardInfo.getIndex_f(), roundTrack.getRoundTrack().get(toolCardInfo.getIndex_f())));
        }
        toolCards.get(i).resetAction();
        toolCardExecutor=null;
        checkEndTurn();
    }

    private void updateOtherUser(int i, ToolCardInfo toolCardInfo){
        players.stream()
                .filter(p -> !p.equals(getCurrent_player()))
                .forEach(p -> p.updateOtherUserSchema(getCurrent_player().getGlassDash().getSchemaCard().getID(), toolCardInfo.getIndex_f(), toolCardInfo.getDice()));
        int id = toolCards.get(i).getID();
        if(id==2 || id ==3 || id==4 || id==12){
            players.stream()
                .filter(p -> !p.equals(getCurrent_player()))
                .forEach(p -> p.removeOtherUserSchema(getCurrent_player().getGlassDash().getSchemaCard().getID(), toolCardInfo.getIndex_i()));
        }

    }

    /**
     * This method is invoked when a disconnected player wants to continue the game. He/She will be added at the start of the next round
     * @param player the reconnected player
     */
    public void addPlayer(Player player) {
        if(playersToAdd==null){
            playersToAdd= new ArrayList<>();
        }
        playersToAdd.add(player);
    }

    /**
     * This method is invoked when a player who's disconnected also happens to be the current player. In this case the game needs
     * to organize the turn order and inform the other player of the disconnection according to the turn in which the player
     * is disconnected, since for different turns (clockwise or not - first/last turn) the procedure for keep the game going is
     * a little different
     */
    public void currentPlayerDisconnected() {
        players.stream()
                .filter(p -> p!=getCurrent_player())
                .forEach(p -> p.warningDisconnected(getCurrent_playerUser()));
        if(clockwise){
            if (players.indexOf(getCurrent_player()) == players.size() - 1) {
                players.remove(getCurrent_player());
                current_player=players.size();
                clockwise=false;
                checkSkipTurn();
                sendTurnMessages();
            }else{
                players.remove(getCurrent_player());
                checkSkipTurn();
                sendTurnMessages();
            }
        }else{
            if(players.indexOf(getCurrent_player()) == 0){
                players.remove(getCurrent_player());
                endRound();
                checkSkipTurn();
                sendTurnMessages();
            }else{
                players.remove(getCurrent_player());
                current_player--;
                checkSkipTurn();
                sendTurnMessages();
            }
        }
    }

    /**
     * advise that a not-current player left/disconnected
     */
    public void onePlayerLess(){
        current_player--;
    }

    /**
     *
     * @return the executor of the tool card
     */
    public Player getToolCardExecutor(){
        return toolCardExecutor;
    }


}
