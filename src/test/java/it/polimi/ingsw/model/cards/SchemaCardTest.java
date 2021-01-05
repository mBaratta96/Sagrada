package it.polimi.ingsw.model.cards;

import com.google.gson.Gson;
import it.polimi.ingsw.model.SchemaCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SchemaCardTest {

    private SchemaCard card;
    private String cardJSON;

    @BeforeEach
    void setUp(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/test/test_resources/TestFile/testSchemaCard.txt"));
            cardJSON=reader.readLine();
            card = new Gson().fromJson(cardJSON, SchemaCard.class);
            reader.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetName(){
        String name = "Kaleidoscopic Dream";
        assertEquals(name, card.getName());
    }

    @Test
    void testGetId(){
        int id = 1;
        assertEquals(id, card.getID());

    }

    @Test
    void testGetInfo(){
        String info = "4";
        assertEquals(info, card.getInfo());

    }

    @Test
    void testGetSchema(){
        String schemaCellString = "yb++1g+5+43+r+g2++by";
        //a string that formalizes the cells in the SchemaCard:
        // '+': cell without restriction
        // '1' to '6': cell with shade restriction
        // 'b'/'y'/'g'/'p'/'r': cell with color restriction - only BLUE/YELLOW/GREEN/PURPLE/RED
        SchemaCell cellNoRestriction = new SchemaCell();
        List<SchemaCell> schema = card.getSchema();
        schema.stream()
                .filter(cellTest -> cellTest.getConstraint() == '+')
                .forEach(cellTest -> assertEquals(cellNoRestriction.toString(), cellTest.toString()));
        schema.stream()
                .filter(cellTest -> cellTest.getConstraint() != '+')
                .forEach(cellTest -> {
                                        char c = cellTest.getConstraint();
                                        SchemaCell cell = null;
                                        try {
                                            cell = new SchemaCell(c);
                                        } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
                                            e.printStackTrace();
                                        }
                    assertEquals(cell.toString(), cellTest.toString());
                });
        System.out.println(card.toString());


    }
}