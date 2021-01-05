package it.polimi.ingsw.view.schemacard;

import it.polimi.ingsw.controller.Player;
import it.polimi.ingsw.model.SchemaCell;
import it.polimi.ingsw.model.cards.SchemaCard;
import it.polimi.ingsw.view.dice.DiceView;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;

import java.util.List;

public class SchemaOP extends GridPane {
    private int id_card;
    private String pUser;

    public SchemaOP(Player player) {
        id_card = player.getGlassDash().getSchemaCard().getID();
        List<SchemaCell> cells = player.getGlassDash().getSchemaCard().getSchema();
        cells.forEach(c->add(
                new SquareOP(c.getConstraint()),
                cells.indexOf(c)%5,
                cells.indexOf(c)/5));

        pUser=player.getUsername();

    }

    /**
     * Update view with placement of other players
     * @param index where place dice
     * @param d dice placed
     */
    public void addDice(int index, DiceView d){
       Platform.runLater(() -> {
                               SquareOP s = new SquareOP(d);
                               SquareOP old = (SquareOP) getChildren().get(index);
                               old.getChildren().add(s);
       });

    }

    /**
     *
     * @return info about SchemaCard
     */
    public int getId_card() {
        return id_card;
    }

    /**
     * This method remove dice on schemaOP view
     * @param i index where remove dice on Schema
     */
    public void removeSchemaOP(int i) {
        SquareOP s = (SquareOP) getChildren().get(i);
        s.resetSquareOP();
    }
}
