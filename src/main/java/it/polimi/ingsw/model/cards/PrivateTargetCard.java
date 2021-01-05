package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.actions.privateTargetCardActions.PrivateTargetCardAction;

import java.util.ArrayList;

public class PrivateTargetCard extends Card {

    private PrivateTargetCardAction action;

    public PrivateTargetCard(String name, int id, String info){
        super(name, id, info);
    }


    public int execute(ArrayList<Dice> grid){
        if(action==null){
            action = new PrivateTargetCardAction(id);
        }
        return action.execute(grid);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Nome: %s%nID: %d%nInfo: %s", name, id, info);
    }
}
