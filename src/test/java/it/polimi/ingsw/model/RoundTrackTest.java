package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoundTrackTest {
    private RoundTrack roundTrack;
    private Dice testDice; /*Color: blue.   Face: 3*/

    @BeforeEach
    void setUp() {
        roundTrack = new RoundTrack();
        try {
            testDice = new Dice('b');
            testDice.setFace(3);
        } catch (NotValidException e) {
            e.printStackTrace();
        }
    }


    @Test
    void testNewRound() throws NotValidException {
        roundTrack.nextRound(testDice);
        assertEquals(testDice,roundTrack.showLastDice());
        assertEquals(1,roundTrack.getRound());

        for(int i=0; i<9; i++) {roundTrack.nextRound(testDice);}
        assertEquals(testDice,roundTrack.showLastDice());
        assertEquals(10,roundTrack.getRound());
    }


    @Test
    void testReplaceDice() throws NotValidException {
        Dice testDiceTwo = null;
        testDiceTwo = new Dice('y');
        testDiceTwo.setFace(6);

        for(int i=0; i<2; i++) {roundTrack.nextRound(testDice);}
        roundTrack.replaceDice(testDiceTwo);
        assertEquals(2,roundTrack.getRound());
        assertEquals(testDiceTwo, roundTrack.showLastDice());
    }

    @Test
    void testGetRound() {
        assertEquals(0,roundTrack.getRound());
        for(int i=0; i<2; i++) {roundTrack.nextRound(testDice);}
        assertEquals(2,roundTrack.getRound());
    }

    @Test
    void testShowLastDice() throws NotValidException {
        assertThrows(NotValidException.class, () -> roundTrack.showLastDice());
        roundTrack.nextRound(testDice);
        assertEquals(testDice,roundTrack.showLastDice());
    }
}