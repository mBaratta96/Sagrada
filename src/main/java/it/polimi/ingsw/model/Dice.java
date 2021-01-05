package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NotValidException;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Dice {

    private char color; /*'b':blue  'p':purple  'g':green   'r':red   'y':yellow '+':not yet defined*/
    private int face;
    private static final String[] printColors = {"\u001B[34m", "\u001B[35m", "\u001B[32m", "\u001B[31m", "\u001B[33m", "\u001B[30m"};
    public static final String ANSI_RESET = "\u001B[0m";
    private static final List<Character> colorsAllowed = Arrays.asList('b', 'p', 'g', 'r', 'y', '+');
    private static final int minNumber = 1;
    private static final int maxNumber = 6;


    public Dice(char color) throws NotValidException {
        if(!allowedColor(color)) throw new NotValidException("color");
        this.color = color;
        this.face = 0;
    }

    /**
     * Genererates a random face for the dice
     */
    public void throwDice(){
        this.face = this.generateRandom();
    }

    /*generate random number form 1 to 6*/
    private int generateRandom(){
        Random r = new Random();
        return r.nextInt(maxNumber) + minNumber;
    }

    /**
     *
     * @return the color of the dice
     */
    public char getColor() {
        return color;
    }

    /**
     *
     * @return the face of the dice
     */
    public int getFace(){
        return face;
    }

    /**
     *
     * @param color a color for the dice
     * @throws NotValidException if the parameter passed doesn't follow the color convention
     */
    public void setColor(char color) throws NotValidException {
        if(!allowedColor(color)) throw new NotValidException("color");
        this.color = color;
    }

    /**
     *
     * @param num the face of the dice
     * @throws NotValidException if the parameter if <1 or >6
     */
    public void setFace(int num) throws NotValidException {
        if(!allowedNumber(num)) throw new NotValidException("number");
        this.face = num;
    }


    private boolean allowedNumber(int num){
        return (num >= minNumber && num <= maxNumber);
    }


    private boolean allowedColor(char color) {
        return colorsAllowed.contains(color);
    }

    @Override
    public String toString() {
        return printColors[colorsAllowed.indexOf(color)] + '['+face+']'+ANSI_RESET;
    }

}
