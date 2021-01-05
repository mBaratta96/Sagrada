package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.ToolCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolCardTest {

   @Test
    public void testGetName() {
        String name = "name";
        int id = 0;
        String info = "info";
        ToolCard test = new ToolCard(name, id, info);
        String exp = "name";
        assertEquals(exp, test.getName());
    }


    @Test
    public void testGetID() {
        String name = "name";
        int id = 0;
        String info = "info";
        ToolCard test = new ToolCard(name, id, info);
        int exp = 0;
        assertEquals(exp, test.getID());
    }

    @Test
    public void testGetInfo(){
        String name="name";
        int id=0;
        String info = "info";
        ToolCard test= new ToolCard(name, id, info);
        String exp="info";
        assertEquals(exp,test.getInfo());
    }

    @Test
    public void testAddToken(){
        String name="name";
        int id=0;
        String info = "info";
        ToolCard test= new ToolCard(name, id, info);
        test.setTokenRequired(1);
        int tok = 1;
        test.addToken(tok);
        int exp=1;
        assertEquals(exp,tok);
        assertEquals(tok+1, test.getTokenRequired());
    }

    @Test
    public void testShowToken(){
        String name="name";
        int id=0;
        String info = "info";
        ToolCard test= new ToolCard(name, id, info);
        test.setTokenRequired(1);
        int exp=0;
        assertEquals(exp,test.showToken());
        assertEquals(exp+1, test.getTokenRequired());
    }


}