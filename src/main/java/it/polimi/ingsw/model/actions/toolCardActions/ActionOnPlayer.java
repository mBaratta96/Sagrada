package it.polimi.ingsw.model.actions.toolCardActions;

import it.polimi.ingsw.controller.Player;
import it.polimi.ingsw.model.ToolCardInfo;
import it.polimi.ingsw.model.actions.toolCardActions.toolCardFunctions.PlayerFunctions;

import it.polimi.ingsw.model.exceptions.NotValidException;

public class ActionOnPlayer extends ToolCardAction{

    private Player player;

    public ActionOnPlayer(Player player, ToolCardAction nextAction){
        this.nextAction=nextAction;
        this.player=player;
    }

    @Override
    public boolean activate(int id, ToolCardInfo info) {
       if(!PlayerFunctions.getAllowedActivate(id).test(info, player)){
            return false;
        }
        return nextAction == null || nextAction.activate(id, info);
    }


    public void perform(int id, ToolCardInfo info, boolean alreadyChecked) throws NotValidException {
        if(!alreadyChecked && !activate(id, info)) throw new NotValidException("can't do");
        else {
            player.setToolCardActivated(true);
            PlayerFunctions.getActions(id).accept(info, player);
            if (nextAction != null) {
                nextAction.perform(id, info, true);
            }

        }
    }
}
