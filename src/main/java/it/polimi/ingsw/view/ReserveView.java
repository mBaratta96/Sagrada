package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.view.dice.AddDiceView;
import it.polimi.ingsw.view.dice.DiceView;
import it.polimi.ingsw.view.toolcard.ToolCardInfoView;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class ReserveView extends HBox{

    private static final int SPACING = 10;
    private AddDiceView adview;
    private ToolCardInfoView toolCardInfoView;


    public ReserveView(ArrayList<Dice> dices, AddDiceView adView, ToolCardInfoView toolCardInfoView){
        this.adview=adView;
        this.toolCardInfoView=toolCardInfoView;
        int n_dices;
        setSpacing(SPACING);
        setAlignment(Pos.CENTER);
        for (n_dices=0; n_dices<dices.size(); n_dices++){
            char c;
            int value;
            c=dices.get(n_dices).getColor();
            value=dices.get(n_dices).getFace();
            DiceView d = new DiceView(c,value, n_dices, adView, toolCardInfoView);
            d.setFromReserve(true);
            getChildren().add(n_dices, d);
        }

    }

    /**
     * Removes dice from pos
     * @param pos index of hbox where remove dice
     */
    public void remove(int pos){
        Platform.runLater(() -> {
            DiceView d = (DiceView) getChildren().get(pos);
            getChildren().removeAll(d);
            for(Node n : getChildren()){
                DiceView dice = (DiceView) n;
                dice.updatePos(getChildren().indexOf(n));
            }});


    }

    /**
     *
     * @param dices
     */
    public void updateReserve(ArrayList<Dice> dices){
        Platform.runLater(() -> {
            getChildren().clear();
            dices.forEach(d -> {
                DiceView dice = new DiceView(d.getColor(), d.getFace(), dices.indexOf(d), adview, toolCardInfoView);
                dice.setFromReserve(true);
                getChildren().add(dices.indexOf(d), dice);
            });
        });
   }



}
