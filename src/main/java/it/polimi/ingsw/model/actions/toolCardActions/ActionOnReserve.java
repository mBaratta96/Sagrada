package it.polimi.ingsw.model.actions.toolCardActions;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.ToolCardInfo;
import it.polimi.ingsw.model.actions.toolCardActions.toolCardFunctions.ReserveFunctions;
import it.polimi.ingsw.model.exceptions.NotValidException;

import java.util.ArrayList;

public class ActionOnReserve extends ToolCardAction {

    private ArrayList<Dice> reserve;


    public ActionOnReserve(ArrayList<Dice> reserve, ToolCardAction nextAction){
        this.reserve=reserve;
        this.nextAction=nextAction;
    }

    @Override
    public boolean activate(int id, ToolCardInfo info) {
        if(!ReserveFunctions.getAllowedActivate(id).test(info, reserve)){
            return false;
        }
        return nextAction == null || nextAction.activate(id, info);
    }


    public void perform(int id, ToolCardInfo info, boolean alreadyChecked) throws NotValidException {
        if(!alreadyChecked && !activate(id, info)) throw new NotValidException("can't do");
        else {
            info.setReserveModified(true);
            ReserveFunctions.getActions(id).accept(info, reserve);
            if (nextAction != null) {
                nextAction.perform(id, info, true);
            }

        }
    }
}
