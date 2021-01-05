package it.polimi.ingsw.model.actions.publicTargetCardActions;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.actions.publicTargetCardActions.publicTargetCardFunctions.CountAllGridFunctions;

import java.util.*;
import java.util.stream.Collectors;

public class CountAllGrid extends PublicTargetAction {
    /**
     * This method in invoked to calculate the points gained with the following public target cards:
     * Sfumature Chiare
     * Sfumature Medie
     * Sfumature Scure
     * Sfumature Diverse
     * Varietà di Colore
     * The grid is parsed in its entirety
     * Every dice is filtered according to a particular restriction of face/color, that it's collected in a list following the same criteria.
     * The minimum amount of common dices will be multiplied for the respective point multiplier.
     * @param id is used to recognize the card that's been utilized
     * @param grid the grid that's analyzed
     * @return the total of gained points
     */
    @Override
    public int execute(int id, ArrayList<Dice> grid) {
        int result=0;
        Map<Integer, List<Dice>> map = grid.stream()
                                        .filter(CountAllGridFunctions.getFilters(id))
                                        .collect(Collectors.groupingBy(CountAllGridFunctions.getCollector(id)));

        if(map.values().size()<CountAllGridFunctions.getNumberOfElements(id)){
            return 0;
        }

        Optional<List<Dice>> min = map.values().stream()
                                    .min(Comparator.comparingInt(List::size));

        if(min.isPresent()) {
            if (id == 9) {
                result = (CountAllGridFunctions.getNumberOfElements(id) - 1) * min.get().size();
            } else {
                result = CountAllGridFunctions.getNumberOfElements(id) * min.get().size();
            }
        }
        return result;
    }
}
