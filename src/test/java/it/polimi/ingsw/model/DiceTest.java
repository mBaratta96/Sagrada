package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {
    private Dice dice;

    @BeforeEach
    void setUp() {
        try {
            dice = new Dice('b');
        } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
            e.printStackTrace();
        }

    }

    @Test
    void testGetColor() {
        assertEquals('b', dice.getColor());
    }

    @Test
    void testGetFace() {
        assertEquals(0,dice.getFace());
    }

    @Test
    void testThrowDice() { /*how to test random-number's generator?*/
        boolean result = false;
        dice.throwDice();
        if(dice.getFace()>=1 && dice.getFace()<=6) {result = true;}
        assertEquals(result,true);
    }

    @Test
    void testSetColor() {
        try {
            dice.setColor('y');
        } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
            e.printStackTrace();
        }
        assertEquals('y',dice.getColor());
    }

    @Test
    void testSetFace() {
        try {
            dice.setFace(5);
        } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
            e.printStackTrace();
        }
        assertEquals(5,dice.getFace());
    }

    @Test
    void testToString() {
        try {
            dice.setFace(3);
            dice.setColor('g');
        } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
            e.printStackTrace();
        }
        assertEquals("\u001B[32m"+"[3]"+"\u001B[0m" ,dice.toString());
    }
}