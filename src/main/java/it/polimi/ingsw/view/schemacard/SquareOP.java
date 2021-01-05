package it.polimi.ingsw.view.schemacard;

import it.polimi.ingsw.view.dice.DiceView;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;

public class SquareOP extends StackPane{
    private ImageView face;
    private static final Integer size_w= 20;
    private static final Integer size_h= 20;
    private char cellConstraint;

    private static final HashMap<Character,Color> colors= new HashMap<>();
    static{
        colors.put('b',Color.web("#2DAABE") );
        colors.put('p',Color.web("#996299") );
        colors.put('g',Color.web("#A1BB51") );
        colors.put('r',Color.web("#E54245") );
        colors.put('y',Color.web("#F8D330") );
        colors.put('+',Color.web("#FFFFFF") );
    }

    private static final HashMap<Character,Image> faces= new HashMap<>();
    static{
        faces.put('1',new Image("view/dadifacce/dado1.png",size_w,size_h,true,false) );
        faces.put('2',new Image("view/dadifacce/dado2.png",size_w,size_h,true,false) );
        faces.put('3',new Image("view/dadifacce/dado3.png",size_w,size_h,true,false) );
        faces.put('4',new Image("view/dadifacce/dado4.png",size_w,size_h,true,false) );
        faces.put('5',new Image("view/dadifacce/dado5.png",size_w,size_h,true,false) );
        faces.put('6',new Image("view/dadifacce/dado6.png",size_w,size_h,true,false) );
    }

    /**
     *Square with color restriction
     * @param c color
     */
    public SquareOP(char c) {
        if (colors.keySet().contains(c)) {
            squareColor(c);
        }else {
            squareNumb(c);
        }
        cellConstraint=c;
    }

    /**
     * Square with value restriction
     * @param d diceface value
     */
    public SquareOP(DiceView d){
        face = d.getFace();
        face.setPreserveRatio(true);
        face.setFitHeight(size_h);
        Rectangle rec= new Rectangle(size_w, size_h);
        rec.setFill(d.getRec().getFill());
        rec.setStroke(Color.BLACK);
        getChildren().add(rec);
        getChildren().add(face);
    }

    /**
     * Method, after a dice removal from schema, shows restriction of square again
     */
    public void resetSquareOP(){
        Platform.runLater(() -> {
            if (colors.keySet().contains(cellConstraint)) {
                squareColor(cellConstraint);
            }else {
                squareNumb(cellConstraint);
            }
        });
    }

    /**
     * It Creates square colored
     * @param c color
     */
    private void squareColor(char c) {
        Rectangle rec= new Rectangle(size_w, size_h);
        rec.setFill(colors.get(c));
        rec.setStroke(Color.BLACK);
        getChildren().add(rec);
    }

    /**
     * It creates square with value
     * @param c value
     */
    private void squareNumb(char c) {
        face = new ImageView(faces.get(c));
        Rectangle rec= new Rectangle(size_w,size_h);
        rec.setFill(Color.WHITE);
        rec.setStroke(Color.BLACK);
        getChildren().add(rec);
        getChildren().add(face);

    }


}
