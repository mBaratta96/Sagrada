package it.polimi.ingsw.model.actions.toolCardActions;

import it.polimi.ingsw.model.DiceBag;
import it.polimi.ingsw.model.ToolCardInfo;
import it.polimi.ingsw.model.actions.toolCardActions.toolCardFunctions.DicebagFunctions;
import it.polimi.ingsw.model.exceptions.NotValidException;

public class ActionOnDicebag extends ToolCardAction {

    private DiceBag diceBag;

    public ActionOnDicebag(DiceBag diceBag, ToolCardAction nextAction){
        this.diceBag=diceBag;
        this.nextAction=nextAction;
    }

    @Override
    public boolean activate(int id, ToolCardInfo info) {
        if(!DicebagFunctions.getAllowedActivate(id).test(info, diceBag)){
            return false;
        }
        return nextAction == null || nextAction.activate(id, info);
    }


    public void perform(int id, ToolCardInfo info, boolean alreadyChecked) throws NotValidException {
        if(!alreadyChecked && !activate(id, info)) throw new NotValidException("can't do");
        DicebagFunctions.getActions(id).accept(info, diceBag);
        if (nextAction!=null){
            nextAction.perform(id, info, true);
        }
    }
}
