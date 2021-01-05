package it.polimi.ingsw.model.actions.toolCardActions.toolCardFunctions;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.DiceBag;
import it.polimi.ingsw.model.ToolCardInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class DicebagFunctions {
    private static final Map<Integer,BiPredicate<ToolCardInfo, DiceBag>> allowedActivate = new HashMap<>();
    private static final Map<Integer, BiConsumer<ToolCardInfo, DiceBag>> actions = new HashMap();

    static {
        allowedActivate.put(11, (info, dicebag) -> {
                                                    if(!info.isSecondStep11() && info.getChosenFace()==-1) {
                                                        dicebag.insertDices(Arrays.asList(info.getDice().getColor()));
                                                        Dice d = dicebag.extract(1).get(0);
                                                        info.setDice(d);
                                                        info.getReserve().add(d);
                                                    }
                                                    return true;
        });
    }

    static {
        actions.put(11, (info, dicebag) -> {});
    }

    /**
     *
     * @param id is used to recognize the card that's been utilized
     * @return the action performed by the tool card
     */
    public static BiConsumer<ToolCardInfo, DiceBag> getActions(int id) {
        return actions.get(id);
    }

    /**
     *
     * @param id is used to recognize the card that's been utilized
     * @return a function that checks if the tool card can be activated according to the move of the player
     */
    public static  BiPredicate<ToolCardInfo, DiceBag> getAllowedActivate(int id) {
        return allowedActivate.get(id);
    }
}
