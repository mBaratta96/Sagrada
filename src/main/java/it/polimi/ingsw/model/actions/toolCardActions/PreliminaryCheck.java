package it.polimi.ingsw.model.actions.toolCardActions;

import it.polimi.ingsw.controller.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class PreliminaryCheck {

    private static final Map<Integer, Predicate<Game>> preCheck = new HashMap<>();

    static {
        preCheck.put(1, game -> true);
        preCheck.put(2, game -> game.getToolCardExecutor().getGlassDash().getGrid().stream().
                                anyMatch(dice -> dice.getColor() != '+'));
        preCheck.put(3, game -> game.getToolCardExecutor().getGlassDash().getGrid().stream().
                anyMatch(dice -> dice.getColor() != '+'));
        preCheck.put(4, game -> game.getToolCardExecutor().getGlassDash().getGrid().stream()
                                .filter(dice -> dice.getColor()!='+')
                                .count()>1);
        preCheck.put(5, game -> game.getRoundTrack().getRound()>0);
        preCheck.put(6, game -> true);
        preCheck.put(7, game -> !game.isClockwise() && !game.getToolCardExecutor().isDiceAdded());
        preCheck.put(8, game -> game.isClockwise());
        preCheck.put(9, game -> true);
        preCheck.put(10, game -> true);
        preCheck.put(11, game -> true);
        preCheck.put(12, game -> game.getToolCardExecutor().getGlassDash().getGrid().stream().
                anyMatch(dice -> dice.getColor() != '+') && game.getRoundTrack().getRound()>0);
    }

    /**
     * This method checks if a tool card can be selected according to the state of the game
     * @param i is used to recognize the card that's been utilized
     * @return a lambda function that analyses the state of the game
     */
    public static Predicate<Game> getPreCheck(int i){
        return preCheck.get(i);
    }
}
