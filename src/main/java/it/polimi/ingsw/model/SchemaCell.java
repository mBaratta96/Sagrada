package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NotValidException;

import java.util.*;

public class SchemaCell {

   private static final String[] printColors = {"\u001B[34m", "\u001B[35m", "\u001B[32m", "\u001B[31m", "\u001B[33m", "\u001B[37m"};
   private static final String ANSI_RESET = "\u001B[0m";
   private static final List<Character> charAllowed = Arrays.asList('b', 'p', 'g', 'r', 'y', '1', '2', '3', '4', '5', '6', '+');
   private char constraint;

   public SchemaCell(){
      constraint = '+';
   }

   public SchemaCell(char constraint) throws NotValidException {
      if(!charAllowed.contains(constraint)){throw new NotValidException(constraint + "is a wrong character");}
      this.constraint=constraint;

   }

    /**
     * This method set a constraint that a cell must respect according to the schema card it belongs.
     * If there is no constraint, that it receives, as a convention, the character '+'
     * @param constraint the constraint the cell must respect
     * @throws NotValidException if the constraint is not valid for the game
     */
    public void setConstraint(char constraint) throws NotValidException {
        if(!charAllowed.contains(constraint)){throw new NotValidException(constraint + "is a wrong character");}
        this.constraint=constraint;
    }

    /**
     *
     * @return the constraint of the cell
     */
    public char getConstraint() {
        return constraint;
    }

    @Override
    public String toString() {
        if(constraint == '+') {
            return "[ ]";
        }else if(constraint >= '1'  && constraint<='6'){
            return "["+constraint+"]";
        }else {
            return printColors[charAllowed.indexOf(constraint)] + "[" + constraint + "]" + ANSI_RESET;
        }
    }
}
