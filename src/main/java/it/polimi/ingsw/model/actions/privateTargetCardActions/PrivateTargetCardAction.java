package it.polimi.ingsw.model.actions.privateTargetCardActions;

import it.polimi.ingsw.model.Dice;

import java.util.ArrayList;

public class PrivateTargetCardAction {

    private int id;

    public PrivateTargetCardAction(int id){
        this.id=id;
    }

    /**
     * Calculates the number of dices that respect the instruction of the private target card
     * @param grid the glass dash of the player
     * @return the points gained
     */
    public int execute(ArrayList<Dice> grid){
        return grid.stream()
                .filter(PrivateTargetCardFunctions.getFilter(id))
                .mapToInt(dice -> dice.getFace())
                .sum();
    }



}
