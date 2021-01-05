package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NotValidException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DicePlacementPermission {
    private static final List<Character> colorsName = Arrays.asList('b', 'p', 'g', 'r', 'y');
    private static final Map<Character, Integer> colorMap;
    static {
        colorMap= new HashMap<>();
        colorMap.put('b', 0);
        colorMap.put('p', 1);
        colorMap.put('g', 2);
        colorMap.put('r', 3);
        colorMap.put('y', 4);
    }
    private SchemaCell cell;
    private ArrayList<Boolean> colors;
    private ArrayList<Boolean> numbers;
    private boolean cellActivared;

    public DicePlacementPermission(SchemaCell cell){
        this.cell=cell;
        colors = IntStream.rangeClosed(1, 5)
                            .mapToObj(i -> true)
                            .collect(Collectors.toCollection(ArrayList::new));
        numbers = IntStream.rangeClosed(1, 6)
                            .mapToObj(i -> true)
                            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * A cell is considered activated when there's at least one possible set of face/color that can be placed in it.
     * A cell that, for example, isn't adjacent to any placed dice, is considered not activated
     * @param activation a flag that signals whether a dice can be placed in a cell or not
     */
    public void setActivate(boolean activation){
        allTrueFalse(activation);
        this.cellActivared=activation;
    }

    /**
     *
     * @return a flag that signals whether a dice can be placed in a cell or not
     */
    public boolean isCellActivared() {
        return cellActivared;
    }

    /**
     *
     * @param c the color which has to be validated for placement
     * @return a flag stating the possibility of placing a dice of that color
     * @throws NotValidException if the color is not allowed
     */
    public boolean isAllowedColors(char c) throws NotValidException {
        char cellConstraint = cell.getConstraint();
        if(!colorMap.containsKey(c)){throw new NotValidException(c +" wrong color");}
        if(cellConstraint!='+' && colorsName.contains(cellConstraint)){
            return c==cell.getConstraint() ? colors.get(colorMap.get(c)) : false;
        }
        return colors.get(colorMap.get(c));
    }

    /**
     *
     * @param c the face which has to be validated for placement
     * @return a flag stating the possibility of placing a dice of that face
     * @throws NotValidException if the number is not allowed
     */
    public boolean isAllowedNumber(char c) throws NotValidException {
        char cellConstraint = cell.getConstraint();
        if(!(c>='1' && c<='6')){throw new NotValidException(c +" wrong number");}
        if(cellConstraint!='+' && !(colorsName.contains(cellConstraint))){
            return c==cell.getConstraint() ? numbers.get(c-'0'-1) : false;
        }
        return numbers.get(c-'0'-1);
    }

    /**
     * For a specific color, sets if a dice of that color can/can't be placed
     * @param c the color to which a restriction must be imposed
     * @param permission the possibility of placing a dice of that color
     * @throws NotValidException if the color is not allowed
     */
    public void setColors(char c, boolean permission) throws NotValidException {
        if(!colorMap.containsKey(c)){throw new NotValidException(c +" wrong color");}
        colors.set(colorMap.get(c), permission);
    }
    /**
     * For a specific face, sets if a dice with that face can/can't be placed
     * @param c the face to which a restriction must be imposed
     * @param permission the possibility of placing a dice of that face
     * @throws NotValidException if the face is not allowed
     */
    public void setNumbers(char c, boolean permission) throws NotValidException {
        if(!(c>='1' && c<='6')){throw new NotValidException(c +" wrong number");}
        numbers.set(c-'0'-1, permission);
    }


    private void allColorsTrueFalse(boolean permission){
        colors.replaceAll(c -> permission);
    }

    private void allNumbersTrueFalse(boolean permission){
        numbers.replaceAll(n -> permission);
    }

    private void allTrueFalse(boolean permission){
        this.allColorsTrueFalse(permission);
        this.allNumbersTrueFalse(permission);
    }

    @Override
    public String toString() {
        String descrizione = "Avaiable colors: ";
        List<Character> aColors = colors.stream()
                .filter(c -> c)
                .map(c -> colorsName.get(colors.indexOf(c)))
                .collect(Collectors.toList());
        descrizione += aColors.toString() + '\n';

        descrizione += "Avaiable numbers: ";
        List<Integer> avaiableNumbers = IntStream.range(0, numbers.size())
                .filter(i -> numbers.get(i))
                .mapToObj(i -> i + 1)
                .collect(Collectors.toList());
        descrizione += avaiableNumbers.toString()+'\n';
        return descrizione;
    }
}
