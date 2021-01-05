package it.polimi.ingsw.view;

import it.polimi.ingsw.view.dice.DiceView;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;


public class RoundTrackerView extends HBox{
    private static final int SIZE_W = 40;
    private static final int SIZE_H = 40;
    private static final int NROUND = 10;
    private static final int SPACING = 0;
    private int i;
    private int current_index;
    private ArrayList<StackPane> roundpane;

    private static final HashMap<Integer,Image > squares= new HashMap<>();
    static{
        squares.put(0,new Image("view/roundnumber/1.png", SIZE_W, SIZE_H,true,false) );
        squares.put(1,new Image("view/roundnumber/2.png", SIZE_W, SIZE_H,true,false) );
        squares.put(2,new Image("view/roundnumber/3.png", SIZE_W, SIZE_H,true,false) );
        squares.put(3,new Image("view/roundnumber/4.png", SIZE_W, SIZE_H,true,false) );
        squares.put(4,new Image("view/roundnumber/5.png", SIZE_W, SIZE_H,true,false) );
        squares.put(5,new Image("view/roundnumber/6.png", SIZE_W, SIZE_H,true,false) );
        squares.put(6,new Image("view/roundnumber/7.png", SIZE_W, SIZE_H,true,false) );
        squares.put(7,new Image("view/roundnumber/8.png", SIZE_W, SIZE_H,true,false) );
        squares.put(8,new Image("view/roundnumber/9.png", SIZE_W, SIZE_H,true,false) );
        squares.put(9,new Image("view/roundnumber/10.png", SIZE_W, SIZE_H,true,false) );

    }

    public RoundTrackerView() {
        setAlignment(Pos.CENTER);
        setSpacing(SPACING);
        current_index=0;
        roundpane = new ArrayList<>();
        for (i = 0; i < NROUND; i++) {
            StackPane sp_round= new StackPane();
            ImageView imm_round = new ImageView(squares.get(i));
            sp_round.getChildren().addAll(imm_round);
            roundpane.add(sp_round);
            getChildren().add(sp_round);
        }
    }

    /**
     * Adds dice on roundtrack
     * @param d dice to add
     */
    public void addDice(DiceView d){
        Platform.runLater(() -> {
                                d.setFromRoundTrack(true);
                                roundpane.get(current_index).getChildren().add(d);
                                current_index++;
        });

    }

    /**
     * Changes dice in index with a new dice d
     * @param index position on roundtrack
     * @param d new dice
     */
    public void updateDice(int index, DiceView d){
        Platform.runLater(() -> {
                                d.setFromRoundTrack(true);
                                roundpane.get(index).getChildren().add(d);
        });
    }

    /**
     * Getter
     * @return first index of tracker empty
     */
    public int getCurrent_index() {
        return current_index;
    }

}
