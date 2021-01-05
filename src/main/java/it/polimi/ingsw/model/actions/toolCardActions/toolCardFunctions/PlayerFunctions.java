package it.polimi.ingsw.model.actions.toolCardActions.toolCardFunctions;

import it.polimi.ingsw.controller.Player;
import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.ToolCardInfo;


import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayerFunctions {
    private static final Map<Integer,BiPredicate<ToolCardInfo, Player>> allowedActivate = new HashMap<>();
    private static final Map<Integer, BiConsumer<ToolCardInfo, Player>> actions = new HashMap();

    static {
        allowedActivate.put(2, (info, p) -> {
                                            if(p.getGlassDash().getGrid().get(info.getIndex_i()).getColor()=='+')
                                                        return false;
                                            info.setDice(p.getGlassDash().getGrid().get(info.getIndex_i()));
                                            removeDice(info.getIndex_i(), p);
                                            checkIfReset(p);
                                            p.getGlassDash().setSuspendColor(true);
                                            if(!p.getGlassDash().allowedToPlaceDice(info.getDice(), info.getIndex_f())){
                                                addDice(info.getIndex_i(), info.getDice(), p);
                                                p.getGlassDash().setSuspendColor(false);
                                                return false;
                                            }
                                            return true;
        });
        allowedActivate.put(3, (info, p) -> {
                                            if(p.getGlassDash().getGrid().get(info.getIndex_i()).getColor()=='+')
                                                return false;
                                            info.setDice(p.getGlassDash().getGrid().get(info.getIndex_i()));
                                            removeDice(info.getIndex_i(), p);
                                            checkIfReset(p);
                                            p.getGlassDash().setSuspendNumber(true);
                                            if(!p.getGlassDash().allowedToPlaceDice(info.getDice(), info.getIndex_f())){
                                                addDice(info.getIndex_i(), info.getDice(), p);
                                                p.getGlassDash().setSuspendNumber(false);
                                                return false;
                                            }
                                            return true;
        });
        allowedActivate.put(4, PlayerFunctions::checkPlacement);
        allowedActivate.put(6, (info, p) -> {
                                            List<Integer> allowed = IntStream.range(0,20)
                                                                            .filter(i -> p.getGlassDash().allowedToPlaceDice(info.getDice(), i))
                                                                            .boxed().collect(Collectors.toList());
                                            if(allowed.size()==0){
                                                info.getReserve().set(info.getIndex_i(), info.getDice());
                                            }
                                            return true;
        });
        allowedActivate.put(8, (info, p) -> p.getGlassDash().allowedToPlaceDice(info.getDice(), info.getIndex_f()));
        allowedActivate.put(9, (info, p) -> {
                                             p.getGlassDash().setAllActivated(true);
                                            if(!p.getGlassDash().allowedToPlaceDice(info.getDice(), info.getIndex_f()) ||
                                                    !p.getGlassDash().checkAdjacent(info.getIndex_f())) {
                                                p.getGlassDash().setAllActivated(false);
                                                return false;
                                            }
                                            return true;

        });
        allowedActivate.put(11, (info, p) -> {
                                            if(info.getChosenFace()==-1) {
                                                p.addReserve(info.getReserve());
                                            }else if(info.getChosenFace()!=-1 && !info.isSecondStep11()) {
                                                Dice d = info.getReserve().get(info.getReserve().size()-1);
                                                try {
                                                    d.setFace(info.getChosenFace());
                                                } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
                                                    e.printStackTrace();
                                                }
                                                info.getReserve().set(info.getReserve().size()-1, d);
                                                p.addReserve(info.getReserve());
                                                info.setReserveModified(true);
                                            }else {
                                                if (!p.getGlassDash().allowedToPlaceDice(info.getDice(), info.getIndex_f()))
                                                    return false;
                                                info.setDice(info.getReserve().get(info.getReserve().size()-1));
                                            }
                                            return true;
        });
        allowedActivate.put(12, PlayerFunctions::checkPlacement);

    }

    private static boolean checkPlacement(ToolCardInfo info, Player p) {
        if(p.getGlassDash().getGrid().get(info.getIndex_i()).getColor()=='+')
            return false;
        info.setDice(p.getGlassDash().getGrid().get(info.getIndex_i()));
        removeDice(info.getIndex_i(), p);
        checkIfReset(p);
        if(!p.getGlassDash().allowedToPlaceDice(info.getDice(), info.getIndex_f())){
            addDice(info.getIndex_i(), info.getDice(), p);
            return false;
        }
        return true;
    }

    static {
        actions.put(2, (info, p) -> {
                                    addDice(info.getIndex_f(), info.getDice(), p);
                                    p.getGlassDash().confirmRemove(info.getIndex_i());
                                    p.getGlassDash().setSuspendColor(false);
                                    p.removeUserSchema(info.getIndex_i());
                                    p.updateUserSchema(info.getIndex_f(), info.getDice());


        });
        actions.put(3, (info, p) -> {
                                    addDice(info.getIndex_f(), info.getDice(), p);
                                    p.getGlassDash().confirmRemove(info.getIndex_i());
                                    p.getGlassDash().setSuspendNumber(false);
                                    p.removeUserSchema(info.getIndex_i());
                                    p.updateUserSchema(info.getIndex_f(), info.getDice());

        });
        actions.put(4, (info, p) -> {
                                    addDice(info.getIndex_f(), info.getDice(), p);
                                    p.getGlassDash().confirmRemove(info.getIndex_i());
                                    p.removeUserSchema(info.getIndex_i());
                                    p.updateUserSchema(info.getIndex_f(), info.getDice());


        });
        actions.put(6, (info, p)-> {
                                    p.setToolCardActivated(false);
        });


        actions.put(8, (info, p) -> {
                                    addDice(info.getIndex_f(), info.getDice(), p);
                                    p.updateUserSchema(info.getIndex_f(), info.getDice());
                                    p.setSkipNextTurn(true);
        });
        actions.put(9, (info, p)-> {
                                    addDice(info.getIndex_f(), info.getDice(), p);
                                    p.setDiceAdded(true);
                                    p.getGlassDash().setAllActivated(false);
            p.getGlassDash().setPermissions(info.getIndex_f(), info.getDice(), true);
            p.updateUserSchema(info.getIndex_f(), info.getDice());

        });
        actions.put(11, (info, p)-> {
                                    if(info.isSecondStep11()) {
                                        addDice(info.getIndex_f(), info.getDice(), p);
                                        p.updateUserSchema(info.getIndex_f(), info.getDice());
                                        p.setDiceAdded(true);
                                    }else {
                                        p.setToolCardActivated(false);
                                    }
        });
        actions.put(12, (info, p) -> {
                                    addDice(info.getIndex_f(), info.getDice(), p);
                                    p.removeUserSchema(info.getIndex_i());
                                    p.updateUserSchema(info.getIndex_f(), info.getDice());
        });
    }
    /**
     *
     * @param id is used to recognize the card that's been utilized
     * @return the action performed by the tool card
     */
    public static BiConsumer<ToolCardInfo, Player> getActions(int id) {
        return actions.get(id);
    }
    /**
     *
     * @param id is used to recognize the card that's been utilized
     * @return a function that checks if the tool card can be activated according to the move of the player
     */
    public static BiPredicate<ToolCardInfo, Player> getAllowedActivate(int id) {
        return allowedActivate.get(id);
    }

    private static void addDice(int index, Dice dice, Player p){
        try {
            p.getGlassDash().addDice(dice, index);
        } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
            e.printStackTrace();
        }

    }

    private static void removeDice(int index, Player p){
        p.getGlassDash().removeDice(index);
    }

    private static void checkIfReset(Player p){
        int nDices = (int) p.getGlassDash().getGrid().stream()
                .filter(d -> d.getColor()!='+')
                .count();
        if(nDices==0)
            p.getGlassDash().initGrid();
    }


}
