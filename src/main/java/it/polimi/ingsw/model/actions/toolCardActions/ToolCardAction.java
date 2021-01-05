package it.polimi.ingsw.model.actions.toolCardActions;

import it.polimi.ingsw.controller.Game;
import it.polimi.ingsw.model.ToolCardInfo;
import it.polimi.ingsw.model.exceptions.NotValidException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class ToolCardAction {

    protected ToolCardAction nextAction;

    private static final Map<Integer, Function<Game, ToolCardAction>> actions = new HashMap<>();
    static {
        actions.put(1, game -> new ActionOnReserve(game.getReserve(), null));
        actions.put(2, game -> new ActionOnPlayer(game.getToolCardExecutor(), null));
        actions.put(3, game -> new ActionOnPlayer(game.getToolCardExecutor(), null));
        actions.put(4, game -> new ActionOnPlayer(game.getToolCardExecutor(), null));
        actions.put(5, game -> new ActionOnReserve(game.getReserve(), new ActionOnRoundtrack(game.getRoundTrack(), null)));
        actions.put(6, game -> new ActionOnReserve(game.getReserve(), new ActionOnPlayer(game.getToolCardExecutor(), null)));
        actions.put(7, game -> new ActionOnReserve(game.getReserve(), null));
        actions.put(8, game -> new ActionOnReserve(game.getReserve(), new ActionOnPlayer(game.getToolCardExecutor(), null)));
        actions.put(9, game -> new ActionOnReserve(game.getReserve(), new ActionOnPlayer(game.getToolCardExecutor(), null)));
        actions.put(10, game -> new ActionOnReserve(game.getReserve(), null));
        actions.put(11, game -> new ActionOnReserve(game.getReserve(), new ActionOnDicebag(game.getDiceBag(), new ActionOnPlayer(game.getToolCardExecutor(), null))));
        actions.put(12, game -> new ActionOnPlayer(game.getToolCardExecutor(), new ActionOnRoundtrack(game.getRoundTrack(), null)));
    }

    public static ToolCardAction getInstance(int id, Game game){
        return actions.get(id).apply(game);
    }

    /**
     * This method check if the player's move respects the conditions imposed by all the parts of the game involved in the action of the tool card.
     * @param id is used to recognize the card that's been utilized
     * @param info is an instance of ToolCardInfo that contains all the information describing the player's move
     * @return
     */
    public abstract boolean activate(int id, ToolCardInfo info);

    /**
     * This method performs the action of a specific tool card, chaining all the modifications that must be made to the required parts of the game
     * @param id is used to recognize the card that's been utilized
     * @param info is an instance of ToolCardInfo that contains all the information describing the player's move
     * @param alreadyChecked check if for that part of the game the method activate has already been invoked
     * @throws NotValidException thrown if the method activate return a false value, meaning that the player's move is not valid
     */
    public abstract void perform(int id, ToolCardInfo info, boolean alreadyChecked) throws NotValidException;



}
