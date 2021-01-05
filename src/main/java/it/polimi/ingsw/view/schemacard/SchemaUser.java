package it.polimi.ingsw.view.schemacard;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.SchemaCell;
import it.polimi.ingsw.model.cards.SchemaCard;
import it.polimi.ingsw.view.dice.AddDiceView;
import it.polimi.ingsw.view.dice.DiceView;
import it.polimi.ingsw.view.toolcard.ToolCardInfoView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;


public class SchemaUser extends GridPane {

    public SchemaUser(SchemaCard card, AddDiceView adView, ToolCardInfoView toolCardInfoView) {
        List<SchemaCell> cells = card.getSchema();
        cells.forEach(c->add(
                        new SquareUser(c.getConstraint(), adView, cells.indexOf(c), toolCardInfoView),
                        cells.indexOf(c)%5,
                        cells.indexOf(c)/5));

    }

    public SchemaUser(SchemaCard card) {
        List<SchemaCell> cells = card.getSchema();
        cells.forEach(c->add(
                new SquareUser(c.getConstraint(), cells.indexOf(c)),
                cells.indexOf(c)%5,
                cells.indexOf(c)/5));

    }

    /**
     * This method adds dice d in position index of schema
     * @param index position
     * @param d dice
     */
    public void add(int index, DiceView d){
        SquareUser s = (SquareUser) getChildren().get(index);
        d.setFromGrid(true);
        s.addDice(d);
    }

    /**
     * This method removes a dice from schema in position i
     * @param i position
     */
    public void removeSquareUser(int i) {
        SquareUser s = (SquareUser) getChildren().get(i);
        s.resetSchemaUser();
    }
}
