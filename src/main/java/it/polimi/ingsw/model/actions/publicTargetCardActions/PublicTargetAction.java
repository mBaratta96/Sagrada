package it.polimi.ingsw.model.actions.publicTargetCardActions;

import it.polimi.ingsw.model.Dice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public abstract  class PublicTargetAction {

    private static final ArrayList<PublicTargetAction> actions = new ArrayList<>();
    private static final Map<Integer, Integer> indexes = new HashMap<>();

    static {
        actions.add(0, new RowAndColumns());
        actions.add(1, new CountAllGrid());
        actions.add(2, new ReadDiagonals());
    }

    static {
        indexes.put(0,0);
        indexes.put(1,0);
        indexes.put(2,0);
        indexes.put(3,0);
        indexes.put(4,1);
        indexes.put(5,1);
        indexes.put(6,1);
        indexes.put(7,1);
        indexes.put(8,2);
        indexes.put(9,1);
    }

    /**
     *
     * @param id is used to recognize the card that's been utilized
     * @return the type of action that needs to be performed
     */
    public static PublicTargetAction getAction(int id){
            return actions.get(indexes.get(id));

    }
    public abstract int execute(int id, ArrayList<Dice> grid);
}
