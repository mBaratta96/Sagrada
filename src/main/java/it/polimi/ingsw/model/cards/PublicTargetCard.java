package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.actions.publicTargetCardActions.PublicTargetAction;

import java.util.ArrayList;


public class PublicTargetCard extends Card{

    private PublicTargetAction action;

    public PublicTargetCard(String name, int id, String info){
        super(name, id, info);
    }



    public int execute(ArrayList<Dice> grid){
        if(action == null){
            action=PublicTargetAction.getAction(id);
        }
        return action.execute(id, grid);
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
