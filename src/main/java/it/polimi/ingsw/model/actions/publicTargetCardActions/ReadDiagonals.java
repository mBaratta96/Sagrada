package it.polimi.ingsw.model.actions.publicTargetCardActions;

import it.polimi.ingsw.model.Dice;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class ReadDiagonals extends PublicTargetAction {
    /**
     * This method is invoked to calculate the points gained with the public target card Diagonali Colorate
     * It parse the grid and adds a point for every dice that has at least an equal color in his left/right diagonal
     * @param id is used to recognize the card that's been utilized
     * @param grid the grid that is analyzed
     * @return the total of gained points
     */
    @Override
    public int execute(int id, ArrayList<Dice> grid) {
        return IntStream.range(0,20)
                .filter(i-> grid.get(i).getColor()!='+')
                .map(i -> IntStream.of(i+4, i-4, i+6, i-6)
                            .filter(j ->  j>=0 && j<20 && Math.abs(i%5-j%5)==1 && Math.abs(i/5-j/5)==1)
                            .map(j -> grid.get(j).getColor()==grid.get(i).getColor() ? 1 : 0)
                            .sum())
                .map(i -> i>0 ? 1 : 0)
                .sum();
    }
}
