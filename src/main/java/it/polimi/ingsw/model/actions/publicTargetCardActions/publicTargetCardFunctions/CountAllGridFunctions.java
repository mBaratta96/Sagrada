package it.polimi.ingsw.model.actions.publicTargetCardActions.publicTargetCardFunctions;

import it.polimi.ingsw.model.Dice;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class CountAllGridFunctions {
    private static final Map<Integer, Predicate<Dice>> filters = new HashMap<>();
    private static final Map<Integer, Integer> numberOfElements = new HashMap<>();
    private static final Map<Integer, Function<Dice, Integer>> collectors = new HashMap<>();

    static {
        filters.put(4, d-> d.getFace()==1 || d.getFace()==2);
        filters.put(5, d-> d.getFace()==3 || d.getFace()==4);
        filters.put(6, d-> d.getFace()==5 || d.getFace()==6);
        filters.put(7, d->d.getColor()!='+');
        filters.put(9, d->d.getColor()!='+');


    }

    static {
        collectors.put(4, Dice::getFace);
        collectors.put(5, Dice::getFace);
        collectors.put(6, Dice::getFace);
        collectors.put(7, Dice::getFace);
        collectors.put(9, d -> Integer.valueOf(d.getColor()));
    }

    static{
        numberOfElements.put(4,2);
        numberOfElements.put(5,2);
        numberOfElements.put(6,2);
        numberOfElements.put(7,5);
        numberOfElements.put(9,5);
    }
    /**
     * Gives access to a filter criteria based on a particular public target card
     * @param id is used to recognize the card that's been utilized
     * @return a lambda function to filter dices
     */
    public static Predicate<Dice> getFilters(int id){
        return filters.get(id);
    }

    /**
     * Gives access to a collector criteria of a public card. This criteria is used to calculate the number of dices that respect the  card's restriction
     * @param id is used to recognize the card that's been utilized
     * @return a lambda function to collect dices
     */
    public static Function<Dice, Integer> getCollector(int id){
        return collectors.get(id);}

    /**
     *
     * @param id is used to recognize the card that's been utilized
     * @return the number of elements that a collection is required to have in order to respect the card's restriction
     */
    public static Integer getNumberOfElements(int id){
        return numberOfElements.get(id);
    }
}
