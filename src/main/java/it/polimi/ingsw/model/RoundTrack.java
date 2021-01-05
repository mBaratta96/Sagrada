package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NotValidException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoundTrack {
    private ArrayList<Dice> roundTrack;
    private int round;

    public RoundTrack(){
        roundTrack = new ArrayList<>();
        round=0;

    }

    /**
     * Increases the count of the rounds
     * @param dice the last dice on the reserve
     */
    public void nextRound(Dice dice){
        if(round<=10){
            roundTrack.add(round, dice);
            round++;
        }
    }

    /**
     * replace the last dice in the roundtrack
     * @param dice the new dice
     */
    public void replaceDice(Dice dice){
        roundTrack.set(round-1, dice);
    }

    /**
     *
     * @return the current round
     */
    public int getRound(){
        return round;
    }

    /**
     *
     * @return the dices placed on the roundtrack
     */
    public ArrayList<Dice> getRoundTrack(){
        return roundTrack;
    }

    /**
     *
     * @return the last dice on the roundtrack
     * @throws NotValidException if there's no dice on the roundtrack
     */
    public Dice showLastDice() throws NotValidException {
        if(round==0) throw new NotValidException("no dices");
        return roundTrack.get(round-1);
    }

    @Override
    public String toString() {
        return roundTrack.toString();
    }
}
