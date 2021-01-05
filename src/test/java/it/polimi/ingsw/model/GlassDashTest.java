package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.SchemaCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlassDashTest {
    GlassDash glassDashTest;
    Dice testDice;
    String cardJSON = "{\"cells\":[{\"constraint\":\"y\"},{\"constraint\":\"b\"},{\"constraint\":\"+\"},{\"constraint\":\"+\"},{\"constraint\":\"1\"},{\"constraint\":\"g\"},{\"constraint\":\"+\"},{\"constraint\":\"5\"},{\"constraint\":\"+\"},{\"constraint\":\"4\"},{\"constraint\":\"3\"},{\"constraint\":\"+\"},{\"constraint\":\"r\"},{\"constraint\":\"+\"},{\"constraint\":\"g\"},{\"constraint\":\"2\"},{\"constraint\":\"+\"},{\"constraint\":\"+\"},{\"constraint\":\"b\"},{\"constraint\":\"y\"}],\"name\":\"Kaleidoscopic Dream\",\"id\":1,\"info\":\"4\"}";


    @BeforeEach
    void testSetUp() {
            glassDashTest = new GlassDash (new Gson().fromJson(cardJSON, SchemaCard.class), "red");
            try {
                testDice = new Dice('y');
                testDice.throwDice();
            } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
                e.printStackTrace();
            }
    }

    @Test
    void testAddDice() {
        //row goes from 1 to 4
        //column goes from 1 to 5
        try {
            glassDashTest.addDice(testDice,0);
        } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
            e.printStackTrace();
        }
        assertEquals(testDice.getFace(), glassDashTest.getGrid().get(0).getFace());
        assertEquals(testDice.getColor(),glassDashTest.getGrid().get(0).getColor());
        assertEquals(20, glassDashTest.getGrid().size());
    }

    @Test
    void testGetGrid() {
        assertEquals(20,glassDashTest.getGrid().size());
    }

    @Test
    void testGetSchemaCard() {

        SchemaCard testSchema =new Gson().fromJson(cardJSON, SchemaCard.class);
        assertEquals(testSchema.toString(), glassDashTest.getSchemaCard().toString());

    }

    @Test
    void testGetColor() {
        assertEquals("red",glassDashTest.getColor());
    }
}