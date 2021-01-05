package it.polimi.ingsw.model.actions.toolCardActions.toolCardFunctions;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.ToolCardInfo;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;


public class ReserveFunctions {
    private static final Map<Integer,BiPredicate<ToolCardInfo, ArrayList<Dice>>> allowedActivate = new HashMap<>();
    private static final Map<Integer, BiConsumer<ToolCardInfo, ArrayList<Dice>>> actions = new HashMap<>();
    private static final String UP = "up";
    private static final int MIN = 1;
    private static final int MAX = 6;

    static {
        allowedActivate.put(1, (info, reserve) ->{
                                                if(info.getIndex_i()>=reserve.size())
                                                    return false;
                                                if ((info.isUpDown() && reserve.get(info.getIndex_i()).getFace() >= MAX) ||
                                                        (!info.isUpDown() && reserve.get(info.getIndex_i()).getFace() <= MIN))
                                                            return false;
                                                return true;

        });
        allowedActivate.put(5, (info, reserve) ->  {
                                                    if(info.getIndex_i()>=reserve.size())
                                                        return false;
                                                    info.setDice(reserve.get(info.getIndex_i()));
                                                    info.setReserve(reserve);
                                                    return true;
        });

        allowedActivate.put(6, (info, reserve) ->  {
                                                    if(info.getIndex_i()>=reserve.size())
                                                        return false;
                                                    Dice d = reserve.get(info.getIndex_i());
                                                    d.throwDice();
                                                    info.setDice(d);
                                                    info.setReserve(reserve);
                                                    return true;
        });
        allowedActivate.put(7, (info, reserve) -> info.getIndex_i()==1);
        allowedActivate.put(8, (info, reserve) -> {
                                                    if(info.getIndex_i()>=reserve.size())
                                                        return false;
                                                    info.setDice(reserve.get(info.getIndex_i()));
                                                    return true;
        });
        allowedActivate.put(9, (info, reserve) -> {
                                                    if(info.getIndex_i()>=reserve.size())
                                                        return false;
                                                    info.setDice(reserve.get(info.getIndex_i()));
                                                    return true;
        });
        allowedActivate.put(10, (info, reserve) -> info.getIndex_i()<reserve.size());
        allowedActivate.put(11, (info, reserve) -> {
                                                    if (info.getIndex_i() >= reserve.size())
                                                        return false;
                                                    if(!info.isSecondStep11() && info.getChosenFace()==-1) {
                                                        info.setDice(reserve.get(info.getIndex_i()));
                                                        info.setReserve(reserve);
                                                        reserve.remove(info.getDice());
                                                    }else {
                                                        info.setReserve(reserve);
                                                    }
                                                    return true;
        });


    }

    static {
        actions.put(1, (info, reserve) -> {
                                            try {
                                                reserve.get(info.getIndex_i()).setFace(info.isUpDown() ?
                                                        reserve.get(info.getIndex_i()).getFace() + 1 :  reserve.get(info.getIndex_i()).getFace() - 1);
                                            } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
                                                e.printStackTrace();
                                            }
            info.setReserveModified(true);
        });
        actions.put(5, (info, reserve) -> {});
        actions.put(6, (info, reserve)-> {});
        actions.put(7, (info, reserve) -> reserve.forEach(dice -> dice.throwDice()));
        actions.put(8, (info, reserve) -> reserve.remove(info.getDice()));
        actions.put(9, (info, reserve) -> reserve.remove(info.getDice()));
        actions.put(10, (info, reserve) -> {
                                            try {
                                                reserve.get(info.getIndex_i()).setFace(7-reserve.get(info.getIndex_i()).getFace());
                                            } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
                                                e.printStackTrace();
                                            }
        });
        actions.put(11, (info, reserve) -> {if(info.isSecondStep11()) {
                                                info.setDice(reserve.get(reserve.size()-1));
                                                reserve.remove(reserve.size()-1);
                                            }else {
                                                info.setReserveModified(true);
                                            }
        });
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
    public static BiConsumer<ToolCardInfo,  ArrayList<Dice>> getActions(int id) {
        return actions.get(id);
    }
}
