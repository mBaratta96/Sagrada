package it.polimi.ingsw.model.actions.publicTargetCardActions;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.actions.publicTargetCardActions.publicTargetCardFunctions.RowAndColumnsFunctions;

import java.util.ArrayList;

public class RowAndColumns extends PublicTargetAction {

    /**
     * This method is invoked to calculate the points gained with the following public target cards:
     * Colori Diversi - Riga
     * Colori Diversi - Colonna
     * Sfumature Diverse - Riga
     * Sfumature Diverse - Colonna
     * It transform every row/column in a string consisting of characters identifying the color/shade of each dice, then verifies if the succession of them satisfies the restriction imposed by the card.
     * @param id is used to recognize the card that's been utilized
     * @param grid is the grid that is analyzed
     * @return the total of gained points
     */
    @Override
    public int execute(int id, ArrayList<Dice> grid) {
        return (int) (RowAndColumnsFunctions.getMultiplier(id)*RowAndColumnsFunctions.getValues(id).stream()
                                                                .map(i -> RowAndColumnsFunctions.getTransform(id).apply(i, grid))
                                                                .filter(RowAndColumnsFunctions.getFilter(id))
                                                                .count());
    }
}
