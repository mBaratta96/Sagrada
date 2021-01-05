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

class PublicTargetCardTest {

    private GlassDash dash;
    private PublicTargetCard card = new PublicTargetCard("prova", 2, "prova");

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
        card = new PublicTargetCard("Colori diversi - Riga", 0, "Righe senza colori ripetuti");
    }

    @Test
    void testEsegui(){
        System.out.println(dash.toString());
        assertEquals(6, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 1, "prova");
        assertEquals(10, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 2, "prova");
        assertEquals(5, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 3, "prova");
        assertEquals(4,card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 4, "prova");
        assertEquals(8, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 5, "prova");
        assertEquals(4, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 6, "prova");
        assertEquals(4, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 7, "prova");
        assertEquals(10, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 8, "prova");
        assertEquals(12, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 9, "prova");
        assertEquals(12, card.execute(dash.getGrid()));
        //test with uncompleted dash
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/test/test_resources/TestFile/uncompleteGlassDashJSON.txt"));
            String cardJSON=reader.readLine();
            dash = new Gson().fromJson(cardJSON, GlassDash.class);
            reader.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        card = new PublicTargetCard("Colori diversi - Riga", 0, "Righe senza colori ripetuti");
        System.out.println(dash.toString());
        assertEquals(0, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 1, "prova");
        assertEquals(0, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 2, "prova");
        assertEquals(5, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 3, "prova");
        assertEquals(0,card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 4, "prova");
        assertEquals(6, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 5, "prova");
        assertEquals(2, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 6, "prova");
        assertEquals(4, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 7, "prova");
        assertEquals(5, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 8, "prova");
        assertEquals(11, card.execute(dash.getGrid()));
        card = new PublicTargetCard("prova", 9, "prova");
        assertEquals(4, card.execute(dash.getGrid()));

    }




    @Test
    void testGetName(){
        assertEquals("Colori diversi - Riga", card.getName());
    }
    @Test
    void testGetID(){
        assertEquals(0, card.getID());
    }
    @Test
    void testGetInfo(){
        assertEquals("Righe senza colori ripetuti", card.getInfo());
    }

}