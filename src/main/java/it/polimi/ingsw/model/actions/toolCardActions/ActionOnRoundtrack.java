package it.polimi.ingsw.model.actions.toolCardActions;

import it.polimi.ingsw.model.RoundTrack;
import it.polimi.ingsw.model.ToolCardInfo;
import it.polimi.ingsw.model.actions.toolCardActions.toolCardFunctions.RoundTrackFunctions;
import it.polimi.ingsw.model.exceptions.NotValidException;

public class ActionOnRoundtrack extends ToolCardAction {
    private RoundTrack roundTrack;

    public ActionOnRoundtrack(RoundTrack roundTrack, ToolCardAction nextAction){
        this.roundTrack=roundTrack;
        this.nextAction=nextAction;
    }

    @Override
    public boolean activate(int id, ToolCardInfo info) {
        if(!RoundTrackFunctions.getAllowedActivate(id).test(info, roundTrack.getRoundTrack())) {
            return false;
        }
        return nextAction == null || nextAction.activate(id, info);
    }


    public void perform(int id, ToolCardInfo info, boolean alreadyChecked) throws NotValidException {
        if(!alreadyChecked && !activate(id, info)) throw new NotValidException("can't do");
        else {
            RoundTrackFunctions.getActions(id).accept(info, roundTrack.getRoundTrack());
            if (nextAction != null) {
                nextAction.perform(id, info, true);
            }
            if (id != 12)
                info.setRoundTrackModified(true);
        }
    }
}
