package it.polimi.ingsw.model.actions.toolCardActions.toolCardFunctions;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.ToolCardInfo;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class RoundTrackFunctions {

    private static final Map<Integer, BiPredicate<ToolCardInfo, ArrayList<Dice>>> allowedActivate = new HashMap<>();
    private static final Map<Integer, BiConsumer<ToolCardInfo,  ArrayList<Dice>>> actions = new HashMap<>();

    static {
        allowedActivate.put(5, (info, round) -> info.getIndex_f()-1<round.size());
        allowedActivate.put(12, (info, round) -> round.stream()
                                                        .mapToInt(d -> d.getColor()==info.getDice().getColor() ? 1:0)
                                                        .count()>0);

    }

    static {
        actions.put(5, (info, round) -> {
                                        Dice diceRound = round.get(info.getIndex_f());
                                        round.set(info.getIndex_f(), info.getDice());
                                        info.getReserve().set(info.getIndex_i(), diceRound);

        });
        actions.put(12, (info, round)->{});
    }
    /**
     *
     * @param id is used to recognize the card that's been utilized
     * @return the action performed by the tool card
     */
    public static BiPredicate<ToolCardInfo, ArrayList<Dice>> getAllowedActivate(int id) {
        return allowedActivate.get(id);
    }
    /**
     *
     * @param id is used to recognize the card that's been utilized
     * @return a function that checks if the tool card can be activated according to the move of the player
     */
    public static BiConsumer<ToolCardInfo, ArrayList<Dice>> getActions(int id) {
        return actions.get(id);
    }
}
