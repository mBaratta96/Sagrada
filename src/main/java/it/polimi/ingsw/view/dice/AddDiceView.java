package it.polimi.ingsw.view.dice;

import it.polimi.ingsw.net.client.socket.ClientSocket;
import it.polimi.ingsw.view.schemacard.SquareUser;
import javafx.application.Platform;

/**
 * This class is used to send to server all the information needed for a dice placement.
 * It sends the position of the dice in the reserve and the position on the grid where the player wants to place it.
 * It checks also when a player can select a dice for the placement.
 */
public class AddDiceView {

    private ClientSocket connectionInterface;
    private int pos_reserve_selected =-1;
    private int index_grid_selected= -1;
    private boolean myTurn = false;
    private DiceView diceView;
    private SquareUser squareUser;

    public AddDiceView(ClientSocket connectionInterface) {
        this.connectionInterface = connectionInterface;
    }

    /**
     * Sets the possibility of the view to execute a placement
     */
    public synchronized void myTurn(){
        System.out.println("it's now my turn");
        myTurn = true;
    }

    /**
     * Sets the prohibition of the view to execute a placement
     */
    public synchronized void notMyTurn(){
        myTurn=false;
    }

    /**
     *
     * @return a flag the allows or not the view to execute a placement
     */
    public synchronized boolean isMyTurn() {
        return myTurn;
    }

    /**
     *
     * @param r_dice the position of the dice on the reserve
     * @param diceView the view's representation of the dice
     */
    public void addDiceReserve(int r_dice, DiceView diceView) {
        pos_reserve_selected = r_dice;
        this.diceView=diceView;
    }

    /**
     *
     * @param g_dice the position of the grid on which the player wants to add the dice
     * @param squareUser the the view's representation of the grid's cell
     */
    public void addDiceGrid(int g_dice, SquareUser squareUser) {
       if(pos_reserve_selected!=-1){
           index_grid_selected = g_dice;
           this.squareUser=squareUser;
           play();
       }else {
           System.out.println("select reserve, retry");
           squareUser.deselection();
           return;
       }

    }

    private void play() {
        if(pos_reserve_selected!=-1 && index_grid_selected!=-1 ){
            connectionInterface.sendMessage(String.valueOf(pos_reserve_selected)+":"+String.valueOf(index_grid_selected));
        }else {
            System.out.println("play not valid");
        }
    }

    private void clean(){
       this.pos_reserve_selected = -1;
       this.index_grid_selected= -1;
       diceView=null;
       squareUser=null;

    }

    /**
     *
     * @return the position of the dice selectd from the reserve
     */
    public int getPos_reserve_selected(){
        return pos_reserve_selected;
    }

    /**
     * This method is invoked when a placement is not valid and the deselection of the elements on the view is needed
     */
    public void notValidDice() {

        Platform.runLater(() -> {
                    diceView.deselectionDice();
                    squareUser.deselection();
                    clean();
                });

    }

    /**
     * This method is invoked when a placement is valid and the class becomes ready to get new information
     */
    public void validPlacement() {
        clean();
    }

    /**
     * This method is invoked when a dice meant to placed is instead deselected.
     *
     */
    public void removeSelection() {
        pos_reserve_selected=-1;
    }
}
