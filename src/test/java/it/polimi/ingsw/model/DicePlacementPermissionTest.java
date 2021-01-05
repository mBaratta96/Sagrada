package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import static org.junit.jupiter.api.Assertions.*;

class DicePlacementPermissionTest {


    DicePlacementPermission perm;

    private final char[] colors = {'b', 'p', 'g', 'r', 'y'};

    @BeforeEach
    void setUp(){
        perm = new DicePlacementPermission(new SchemaCell());
    }

    @Test
    void testConstructor() {
        ArrayList<Boolean> bool = IntStream.rangeClosed(1,5)
                                            .mapToObj(i -> true)
                                            .collect(Collectors.toCollection(ArrayList::new));
        bool.forEach(b -> {
            try {
                assertEquals(b, perm.isAllowedColors(colors[bool.indexOf(b)]));
            } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
                e.printStackTrace();
            }
        });
        bool.add(true);
        bool.forEach(b -> {
            try {
                assertEquals(b, perm.isAllowedNumber(Character.forDigit(bool.indexOf(b)+1, 10)));
            } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
                e.printStackTrace();
            }
        });
    }


    @Test
    void testSetColors(){
        final char testI = 'p';
        final boolean testP= false;
        try {
            perm.setColors(testI, testP);
            assertEquals(testP, perm.isAllowedColors(testI));
            assertThrows(NotValidException.class, () -> perm.setColors('v', true));

            perm = new DicePlacementPermission(new SchemaCell('b'));
            perm.setColors('p', true);
            //set doesn't change cell constraint
            assertEquals(false, perm.isAllowedColors('p'));
            assertEquals(true, perm.isAllowedColors('b'));
        } catch (NotValidException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSetNumbers(){

        try {
            perm.setNumbers('2', false);
            assertEquals(false, perm.isAllowedNumber('2'));
            assertThrows(NotValidException.class, () -> perm.setNumbers('9', true));

            perm = new DicePlacementPermission(new SchemaCell('4'));
            perm.setNumbers('1', true);
            //set doesn't change cell constraint
            assertEquals(false, perm.isAllowedNumber('1'));
            assertEquals(true, perm.isAllowedNumber('4'));
        } catch (NotValidException  e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSetActivate(){
        perm.setActivate(false);
        assertFalse(perm.isCellActivared());
        ArrayList<Boolean> bool = IntStream.rangeClosed(1,5)
                .mapToObj(i -> false)
                .collect(Collectors.toCollection(ArrayList::new));
        bool.forEach(b -> {
            try {
                assertEquals(b, perm.isAllowedColors(colors[bool.indexOf(b)]));
            } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
                e.printStackTrace();
            }
        });
        bool.add(false);
        bool.forEach(b -> {
            try {
                assertEquals(b, perm.isAllowedNumber(Character.forDigit(bool.indexOf(b)+1, 10)));
            } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
                e.printStackTrace();
            }
        });


    }

}