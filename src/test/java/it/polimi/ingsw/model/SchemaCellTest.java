package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NotValidException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchemaCellTest {

   @Test
    void testConstructor(){
       try{
           SchemaCell cell = new SchemaCell();
           assertEquals('+', cell.getConstraint());
           cell = new SchemaCell('b');
           assertEquals('b', cell.getConstraint());
       } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
           e.printStackTrace();
       }
       assertThrows(NotValidException.class, () -> new SchemaCell('v'));
   }

   @Test
    void testSetConstraint(){
       SchemaCell cell = null;
       try{
           cell = new SchemaCell('b');
           cell.setConstraint('g');
           assertEquals('g', cell.getConstraint());
       } catch (it.polimi.ingsw.model.exceptions.NotValidException e) {
           e.printStackTrace();
       }
       SchemaCell finalCell = cell;
       assertThrows(NotValidException.class, () -> finalCell.setConstraint('v'));
   }

}