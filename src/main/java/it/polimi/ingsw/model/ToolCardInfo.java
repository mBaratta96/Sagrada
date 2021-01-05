package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.net.server.socket.ServerSocketClientHandler;

import java.util.ArrayList;

public class ToolCardInfo {
    private int index_i;
    private int index_f;
    private int index_i2;
    private int index_f2;
    private int chosenFace;
    private boolean secondStep11;
    private boolean upDown;
    private boolean reserveModified;
    private boolean roundTrackModified;
    private Dice dice;
    private Dice dice2;
    private ArrayList<Dice> reserve;


    public ToolCardInfo(){
        index_i=-1;
        index_f=-1;
        index_f2=-1;
        index_i2=-1;
        reserveModified=false;
        roundTrackModified=false;
        chosenFace=-1;
        secondStep11=false;
    }

    public void resetIndex(){
        index_i=-1;
        index_f=-1;
        index_f2=-1;
        index_i2=-1;
        chosenFace=-1;
        reserveModified=false;
        roundTrackModified=false;
        secondStep11=false;
    }

    /**
     * This method is used for the tol card named Diluente per Pasta Calda
     * @return a flag advising if the player is making the second step in the execution of the tool card
     */
    public boolean isSecondStep11() {
        return secondStep11;
    }

    /**
     *
     * @param secondStep11  a flag advising if the player is making the second step in the execution of the tool card
     */
    public void setSecondStep11(boolean secondStep11) {
        this.secondStep11 = secondStep11;
    }

    /**
     * This method is used for the tol card named Diluente per Pasta Calda
     * @param chosenFace the face of the dice that has been extracted from the dicebag
     */
    public void setChosenFace(int chosenFace) {
        this.chosenFace = chosenFace;
    }

    /**
     * This method is used for the tol card named Diluente per Pasta Calda
     * @return the face of the dice that has been extracted from the dicebag
     */
    public int getChosenFace() {
        return chosenFace;
    }

    /**
     *
     * @param roundTrackModified a flag used to advise the game if the roundtrack has been modified
     */
    public void setRoundTrackModified(boolean roundTrackModified) {
        this.roundTrackModified = roundTrackModified;
    }

    /**
     *
     * @return a flag used to advise the game if the roundtrack has been modified
     */
    public boolean isRoundTrackModified() {
        return roundTrackModified;
    }

    /**
     *
     * @param reserveModified a flag used to advise the game if the reserve has been modified
     */
    public void setReserveModified(boolean reserveModified) {
        this.reserveModified = reserveModified;
    }

    /**
     *
     * @return a flag used to advise the game if the roundtrack has been modified
     */
    public boolean isReserveModified() {
        return reserveModified;
    }

    /**
     * This method is invoked if a second addition of a dice takes place
     * @param index_f2 the ending position of the dice
     */
    public void setIndex_f2(int index_f2) {
        this.index_f2 = index_f2;
    }
    /**
     * This method is invoked if a second addition of a dice takes place
     * @param index_i2 the original position of the dice
     */
    public void setIndex_i2(int index_i2) {
        this.index_i2 = index_i2;
    }

    /**
     * This method is invoked if a second addition of a dice takes place
     * @return the ending position of the dice
     */
    public int getIndex_f2() {
        return index_f2;
    }

    /**
     * This method is invoked if a second addition of a dice takes place
     * @return  the original position of the dice
     */
    public int getIndex_i2() {
        return index_i2;
    }

    /**
     * This method is invoked for the tool card named Pinza Sgrossatrice
     * @param upDown a flag that indicates whether the dice's needs to be increased/decreased
     */
    public void setUpDown(boolean upDown) {
        this.upDown = upDown;
        setReserveModified(true);
    }

    /**
     *
     * @param dice a dice that needs to be moved in some operation
     */
    public void setDice(Dice dice) {
        this.dice = dice;
    }

    /**
     *
     * @return the dice that needs to be moved in some operation
     */
    public Dice getDice() {
        return dice;
    }

    /**
     * This method is invoked when different instances of ToolCardAction need to make some operation one the reserve
     * @param reserve the reserve of the game
     */
    public void setReserve(ArrayList<Dice> reserve) {
        this.reserve = reserve;
    }

    /**
     *
     * @return the reserve of the game
     */
    public ArrayList<Dice> getReserve() {
        return reserve;
    }

    /**
     * This method is invoked if an addition of a dice takes place
     * @param index_f the ending position of the dice
     */
    public void setIndex_f(int index_f) {
        this.index_f = index_f;
    }
    /**
     * This method is invoked if an addition of a dice takes place
     * @param index_i the ending position of the dice
     */
    public void setIndex_i(int index_i) {
        this.index_i = index_i;
    }
    /****
     * This method is invoked for the tool card named Pinza Sgrossatrice
     * @return a flag that indicates whether the dice's needs to be increased/decreased
     */
    public boolean isUpDown() {
        return upDown;
    }
    /**
     * This method is invoked if an addition of a dice takes place
     * @return  the original position of the dice
     */
    public int getIndex_i() {
        return index_i;
    }
    /**
     * This method is invoked if an addition of a dice takes place
     * @return the ending position of the dice
     */
    public int getIndex_f() {
        return index_f;
    }





}
