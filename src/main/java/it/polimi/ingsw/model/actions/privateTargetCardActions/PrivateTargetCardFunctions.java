package it.polimi.ingsw.model.actions.privateTargetCardActions;

import it.polimi.ingsw.model.Dice;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class PrivateTargetCardFunctions {

    private static final Map<Integer, Predicate<Dice>> filter = new HashMap<>();
    static {
        filter.put(0, dice -> dice.getColor()=='r');
        filter.put(1, dice -> dice.getColor()=='y');
        filter.put(2, dice -> dice.getColor()=='g');
        filter.put(3, dice -> dice.getColor()=='b');
        filter.put(4, dice -> dice.getColor()=='p');
    }

    /**
     * Gives access to a filter criteria based on a particular private target card
     * @param id is used to recognize the card that's been utilized
     * @return a lambda function to filter dices
     */
    public static Predicate<Dice> getFilter(int id){
        return filter.get(id);
    }
}
