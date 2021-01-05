package it.polimi.ingsw.model.cards;

import com.google.gson.Gson;
import it.polimi.ingsw.model.GlassDash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PrivateTargetCardTest {

    private GlassDash dash;
    private PrivateTargetCard card;

    @BeforeEach
    void setUp(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/test/test_resources/TestFile/completeGlassDashJSON.txt"));
            String cardJSON=reader.readLine();
            dash = new Gson().fromJson(cardJSON, GlassDash.class);
            reader.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        card = new PrivateTargetCard("Sfumature rosse - privata", 0, "Somma dei valori di tutti i dadi rossi");
    }

    @Test
    void testEsegui() {
        System.out.println(dash.toString());
        assertEquals(23, card.execute(dash.getGrid()));
        card = new PrivateTargetCard("prova", 1, "prova");
        assertEquals(17, card.execute(dash.getGrid()));
        card = new PrivateTargetCard("prova", 2, "prova");
        assertEquals(4, card.execute(dash.getGrid()));
        card = new PrivateTargetCard("prova", 3, "prova");
        assertEquals(8, card.execute(dash.getGrid()));
        card = new PrivateTargetCard("prova", 4, "prova");
        assertEquals(10, card.execute(dash.getGrid()));

    }
    @Test
    void testGetName(){
        assertEquals("Sfumature rosse - privata", card.getName());
    }
    @Test
    void testGetID(){
        assertEquals(0, card.getID());
    }
    @Test
    void testGetInfo(){
        assertEquals("Somma dei valori di tutti i dadi rossi", card.getInfo());
    }
}