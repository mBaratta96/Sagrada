package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.SchemaCard;
import it.polimi.ingsw.model.exceptions.NotValidException;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GlassDash {
    
    private ArrayList<Dice> grid;
    private ArrayList<DicePlacementPermission> permissions;
    private SchemaCard schemaCard;
    private String dashColor;
    private boolean firstMoved;
    private boolean suspendColor;
    private boolean suspendNumber;
    private boolean allActivated;
    private static final int COLUMN = 5;
    private static final int ROWS = 4;


    public GlassDash(SchemaCard schemaCard, String color){
        this.schemaCard = schemaCard;
        this.dashColor = color;
        this.grid = new ArrayList <>();
        permissions = new ArrayList<>();
        initGrid();
        firstMoved=true;
        suspendColor=false;
        suspendNumber=false;
        allActivated=false;
    }

    /**
     * This method is invoked when the tool card Alesatore per Lamina di Rame is utilized
     * @param suspendNumber a flag that suspends the control of face permission
     */
    public void setSuspendNumber(boolean suspendNumber) {
        this.suspendNumber = suspendNumber;
    }
    /**
     * This method is invoked when the tool card Pennello per Eglomise is utilized
     * @param suspendColor a flag that suspends the control of face permission
     */
    public void setSuspendColor(boolean suspendColor){
        this.suspendColor=suspendColor;
    }
    /**
     * This method is invoked when the tool card Riga in Sughero is utilized
     * @param allActivated a flag that suspends the control of face permission
     */
    public void setAllActivated(boolean allActivated) {
        this.allActivated = allActivated;
        permissions.stream()
                .filter(p -> grid.get(permissions.indexOf(p)).getColor()=='+' && checkAdjacent(permissions.indexOf(p)))
                .forEach(p -> p.setActivate(allActivated));
    }

    /**
     * Sets the grid according to rules of the initial state of the game
     */
    public void initGrid(){
        schemaCard.getSchema().stream().forEach(cell -> permissions.add(new DicePlacementPermission(cell)));
        permissions.stream().forEach(p -> p.setActivate(true));
        IntStream.range(0,20).forEach(i -> {
            try {
                grid.add(new Dice('+'));
            } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
                e.printStackTrace();
            }
        });
        IntStream.rangeClosed(6,8)
                .forEach(i -> permissions.get(i).setActivate(false));
        IntStream.rangeClosed(11,13)
                .forEach(i -> permissions.get(i).setActivate(false));

    }

    /**
     *
     * @param dice the dice that must be added to the grid
     * @param index the position on the grid
     * @throws NotValidException if the placement doesn't respect the restrictions
     */
    public void addDice(Dice dice, int index) throws NotValidException {
        grid.set(index,dice);
        setPermissions(index, dice, false);
    }

    /**
     *
     * @param index the position of the dice that has to be removed
     */
    public void removeDice(int index){
        Dice d = grid.get(index);
        try {
            grid.set(index, new Dice('+'));
            setRemovePermissions(index, d, true);
        } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
            e.printStackTrace();
        }
    }

    private void setRemovePermissions(int i, Dice d, boolean permission) {
        IntStream.of(i+1, i-1, i+5, i-5, i+6, i-6, i+4, i-4)
                .filter(j -> j>=0 && j<20 && ((i%5 == j%5 || i/5==j/5) ||  (Math.abs(i%5-j%5)==1 && Math.abs(i/5-j/5)==1)))
                .filter(j -> grid.get(j).getColor()=='+')
                .forEach(j -> {
                    if(checkAdjacent(j)){
                        permissions.get(j).setActivate(false);
                    }else {
                        try {
                            if(checkColorAdjacent(j, d.getColor())) {
                                permissions.get(j).setColors(d.getColor(), permission);
                            }
                            if(checkNumberAdjacent(j, d.getFace())) {
                                permissions.get(j).setNumbers((char) ('0' + d.getFace()), permission);
                            }
                        } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    /**
     * This method is invoked to check if the placement of a dice is allowed
     * @param dice the dice that could be added
     * @param index the position on the grid
     * @return confirm/denial of the possibility to add the dice
     */
    public boolean allowedToPlaceDice(Dice dice, int index) {
        try {
            return (grid.get(index).getColor()=='+' &&
                    (allActivated || permissions.get(index).isCellActivared()) &&
                    (suspendColor || permissions.get(index).isAllowedColors(dice.getColor())) &&
                    (suspendNumber || permissions.get(index).isAllowedNumber((char) ('0'+dice.getFace()))));
        } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @return the current grid of the player
     */
    public ArrayList<Dice> getGrid(){
       return grid;
    }

    /**
     *
     * @return the schema card assigned to the glass dash
     */
    public SchemaCard getSchemaCard() {
        return schemaCard;
    }

    /**
     *
     * @return the color of the glass dash
     */
    public String getColor() {
        return dashColor;
    }

    @Override
    public String toString() {
        return grid.stream()
                    .map(dice -> (grid.indexOf(dice)+1)%5==0 ? dice.toString()+'\n' : dice.toString())
                    .collect(Collectors.joining());
    }


    /**
     * This method organizes the placement's restriction after the addition/removal of a dice
     * @param index the position of the dice
     * @param dice the dice that modifies the restriction of the grid
     * @param permission false if the dice is added, true if the dice has been removed
     */
    public void setPermissions(int index, Dice dice, boolean permission) {
        if(firstMoved){
            permissions.forEach(d -> d.setActivate(index==permissions.indexOf(d)));
            firstMoved=false;
        }
        int index_colummn = index%COLUMN;
        int index_row = index/COLUMN;

        IntStream.of(index+1, index-1, index-5, index+5)
                    .filter(i -> i>=0 && i<20 && (i%5 == index_colummn || i/5==index_row))
                    .forEach(i -> {
                                    if(!permissions.get(i).isCellActivared()){
                                        permissions.get(i).setActivate(true);
                                    }
                                    try {
                                        permissions.get(i).setColors(dice.getColor(), permission);
                                        permissions.get(i).setNumbers((char) ('0'+dice.getFace()), permission);
                                  } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
                                        e.printStackTrace();
                                    }
                    });
        IntStream.of(index+4, index-4, index+6, index-6)
                .filter(i ->  i>=0 && i<20 && Math.abs(i%COLUMN-index_colummn)==1 && Math.abs(i/COLUMN-index_row)==1)
                .forEach(i -> {
                                if(!permissions.get(i).isCellActivared()){
                                    permissions.get(i).setActivate(true);
                                }
        });
    }

    /**
     *
     * @return the number of empty cells (not occupied by a dice)
     */
    public int emptyCells(){
        return (int) grid.stream()
                    .filter(d -> d.getColor()=='+')
                    .count();
    }

    /**
     *
     * @param i the position on the grid to check
     * @return a flag that notifies whether or not there are dices adjacent to a cell in the gris
     */
    public boolean checkAdjacent(int i){
        return (IntStream.of(i+1, i-1, i+5, i-5, i+6, i-6, i+4, i-4)
                .filter(j -> j>=0 && j<20 && ((i%5 == j%5 || i/5==j/5) ||  (Math.abs(i%5-j%5)==1 && Math.abs(i/5-j/5)==1)))
                .filter(j -> grid.get(j).getColor()!='+')
                .count())==0;

    }


    private boolean checkColorAdjacent(int i, char color){
        return (IntStream.of(i+1, i-1, i+5, i-5, i+6, i-6, i+4, i-4)
                .filter(j -> j>=0 && j<20 && ((i%5 == j%5 || i/5==j/5) ||  (Math.abs(i%5-j%5)==1 && Math.abs(i/5-j/5)==1)))
                .filter(j -> grid.get(j).getColor()!='+' && grid.get(j).getColor()==color)
                .count())==0;
    }

    private boolean checkNumberAdjacent(int i, int n){
        return (IntStream.of(i+1, i-1, i+5, i-5, i+6, i-6, i+4, i-4)
                .filter(j -> j>=0 && j<20 && ((i%5 == j%5 || i/5==j/5) ||  (Math.abs(i%5-j%5)==1 && Math.abs(i/5-j/5)==1)))
                .filter(j -> grid.get(j).getColor()!='+' && grid.get(j).getFace()==n)
                .count())==0;
    }

    /**
     * If after a removal of a dice the cell has no adjacent dice, the cell is deactivated
     * @param i the index of the cell to check
     */
    public void confirmRemove(int i){
        if(checkAdjacent(i)){
            permissions.get(i).setActivate(false);
        }
    }



}
